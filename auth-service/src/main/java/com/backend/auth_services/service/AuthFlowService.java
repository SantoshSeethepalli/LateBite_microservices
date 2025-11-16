package com.backend.auth_services.service;

import com.backend.auth_services.utils.dtos.admin_login.CreateRestaurantAuthRequest;
import lombok.RequiredArgsConstructor;

import com.backend.auth_services.model.*;
import com.backend.auth_services.repository.*;
import com.backend.auth_services.security.JwtUtil;
import com.backend.auth_services.service.sms.SmsService;
import com.backend.auth_services.utils.dtos.complete_profile.request.*;
import com.backend.auth_services.utils.dtos.complete_profile.response.CompleteProfileResponse;
import com.backend.auth_services.utils.dtos.logout.*;
import com.backend.auth_services.utils.dtos.otp.request.*;
import com.backend.auth_services.utils.dtos.otp.resposne.*;
import com.backend.auth_services.utils.dtos.renew.*;
import com.backend.auth_services.utils.exceptions.CustomRuntimeException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthFlowService {

    private final AuthUserRepository authRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OtpService otpService;
    private final SmsService smsService;
    private final JwtUtil jwt;
    private final ObjectMapper objectMapper;


    @Value("${jwt.refresh-exp-days}")
    private int refreshDays;

    @Value("${service.user.base-url}")
    private String userService;

    @Value("${service.restaurant.base-url}")
    private String restaurantService;

    private final WebClient.Builder webClientBuilder;

    // SEND OTP -------------------------------------------------------------

    public SendOtpResponse sendOtp(SendOtpRequest req) {

        String phone = req.getPhone();
        Role role = Role.valueOf(req.getRole());

        AuthUser user = authRepository.findByPhoneNumberAndRole(phone, role)
                .orElseGet(() -> authRepository.save(AuthUser.builder()
                        .phoneNumber(phone)
                        .role(role)
                        .status(Status.NEW)
                        .build()));

        String otp = otpService.generateOtp(phone, role.name());
        smsService.sendSms("+91" + phone, "Your OTP is: " + otp);

        return new SendOtpResponse("otp sent to " + phone);
    }


    // VERIFY OTP -------------------------------------------------------------

    @Transactional
    public VerifyOtpResponse verifyOtp(VerifyOtpRequest req) {

        String phone = req.getPhone();
        Role role = Role.valueOf(req.getRole());
        String otp = req.getOtp();

        if (!otpService.verifyOtp(phone, role.name(), otp))
            throw new CustomRuntimeException("invalid_otp", HttpStatus.UNAUTHORIZED);

        AuthUser user = authRepository.findByPhoneNumberAndRole(phone, role)
                .orElseThrow(() ->
                        new CustomRuntimeException("auth_user_not_found", HttpStatus.NOT_FOUND)
                );

        otpService.deleteOtp(phone, role.name());

        if (user.getStatus() == Status.NEW || user.getStatus() == Status.PENDING_VERIFIED) {
            user.setStatus(Status.PENDING_VERIFIED);
            authRepository.save(user);

            return new VerifyOtpResponse(
                    true,
                    user.getId(),
                    null,
                    null,
                    user.getRole().name(),
                    user.getPhoneNumber()
            );
        }

        String access = jwt.generateAccessToken(
                user.getId(),
                user.getReferenceId(),
                role.name(),
                phone
        );

        String refreshRaw = createRefresh(user.getId());

        return new VerifyOtpResponse(
                false,
                null,
                access,
                refreshRaw,
                role.name(),
                phone
        );
    }


    // COMPLETE PROFILE -------------------------------------------------------------

    @Transactional
    public CompleteProfileResponse completeProfile(CompleteProfileRequest req) {

        Long authUserId = req.getAuthUserId();

        AuthUser user = authRepository.findById(authUserId)
                .orElseThrow(() -> new CustomRuntimeException("auth_user_not_found", HttpStatus.NOT_FOUND));

        Long refId = null;

        if (user.getRole() == Role.USER) {

            CreateUserRequest dto =
                    objectMapper.convertValue(req.getPayload(), CreateUserRequest.class);

            dto.setPhoneNumber(user.getPhoneNumber());

            refId = webClientBuilder.build()
                    .post()
                    .uri(userService + "/api/user/create")
                    .bodyValue(dto)
                    .retrieve()
                    .bodyToMono(Long.class)
                    .block();

            if (refId == null)
                throw new CustomRuntimeException("user_service_failed", HttpStatus.BAD_REQUEST);

        } else if (user.getRole() == Role.RESTAURANT) {

            throw new CustomRuntimeException("restaurant can only be created by admin", HttpStatus.FORBIDDEN);
        }

        user.setReferenceId(refId);
        user.setStatus(Status.ACTIVE);
        authRepository.save(user);

        String access = jwt.generateAccessToken(
                user.getId(),
                refId,
                user.getRole().name(),
                user.getPhoneNumber()
        );

        String refresh = createRefresh(user.getId());

        return new CompleteProfileResponse(
                access,
                refresh,
                user.getRole().name(),
                user.getPhoneNumber()
        );
    }


    // RENEW TOKEN -------------------------------------------------------------

    public RenewResponseDto renew(RenewRequestDto req) {

        Long authUserId = req.getAuthUserId();
        String raw = req.getRefreshToken();

        RefreshToken stored = refreshTokenRepository.findByAuthUserId(authUserId).orElse(null);
        if (stored == null)
            throw new CustomRuntimeException("invalid_refresh", HttpStatus.UNAUTHORIZED);

        if (stored.getExpiresAt() < Instant.now().getEpochSecond())
            throw new CustomRuntimeException("expired_refresh", HttpStatus.UNAUTHORIZED);

        if (!BCrypt.checkpw(raw, stored.getRefreshTokenHash()))
            throw new CustomRuntimeException("invalid_refresh", HttpStatus.UNAUTHORIZED);

        AuthUser user = authRepository.findById(authUserId)
                .orElseThrow(() -> new CustomRuntimeException("auth_user_not_found", HttpStatus.NOT_FOUND));

        String access = jwt.generateAccessToken(
                user.getId(),
                user.getReferenceId(),
                user.getRole().name(),
                user.getPhoneNumber()
        );

        String newRefresh = createRefresh(authUserId);

        return new RenewResponseDto(access, newRefresh);
    }


    // LOGOUT -------------------------------------------------------------

    public LogoutResponse logout(LogoutRequest req) {

        Long authUserId = req.getAuthUserId();
        refreshTokenRepository.deleteByAuthUserId(authUserId);

        return new LogoutResponse("logged_out");
    }


    // REFRESH TOKEN GENERATOR -------------------------------------------------------------

    private String createRefresh(Long authUserId) {

        String raw = UUID.randomUUID() + "-" + UUID.randomUUID();
        String hash = BCrypt.hashpw(raw, BCrypt.gensalt());

        refreshTokenRepository.deleteByAuthUserId(authUserId);

        RefreshToken token = RefreshToken.builder()
                .authUserId(authUserId)
                .refreshTokenHash(hash)
                .expiresAt(Instant.now().plusSeconds(refreshDays * 86400L).getEpochSecond())
                .build();

        refreshTokenRepository.save(token);

        return raw;
    }
}
