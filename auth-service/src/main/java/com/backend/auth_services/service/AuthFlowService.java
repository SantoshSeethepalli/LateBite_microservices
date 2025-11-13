package com.backend.auth_services.service;

import com.backend.auth_services.model.*;
import com.backend.auth_services.repository.*;
import com.backend.auth_services.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthFlowService {

    private final AuthUserRepository authRepo;
    private final RefreshTokenRepository refreshRepo;
    private final OtpService otpService;
    private final TwilioService sms;
    private final JwtUtil jwt;

    @Value("${jwt.refresh-exp-days}")
    private int refreshDays;

    @Value("${service.user.base-url}")
    private String userService;

    @Value("${service.restaurant.base-url}")
    private String restaurantService;

    private final WebClient.Builder webClient;

    public Map<String, Object> sendOtp(Map<String, String> req) {

        String phone = req.get("phone");
        Role role = Role.valueOf(req.get("role"));

        AuthUser user = authRepo.findByPhoneNumberAndRole(phone, role)
                .orElseGet(() -> authRepo.save(AuthUser.builder()
                        .phoneNumber(phone)
                        .role(role)
                        .status(Status.NEW)
                        .build()));

        String otp = otpService.generateOtp(phone, role.name());
        sms.sendSms("+91" + phone, "Your OTP is: " + otp);

        return Map.of("status", "otp_sent");
    }

    public Map<String, Object> verifyOtp(Map<String, String> req) {

        String phone = req.get("phone");
        Role role = Role.valueOf(req.get("role"));
        String otp = req.get("otp");

        if (!otpService.verifyOtp(phone, role.name(), otp))
            return Map.of("error", "invalid_otp");

        AuthUser user = authRepo.findByPhoneNumberAndRole(phone, role)
                .orElseThrow();

        otpService.deleteOtp(phone, role.name());

        if (user.getStatus() == Status.NEW) {
            user.setStatus(Status.PENDING_VERIFIED);
            authRepo.save(user);

            return Map.of(
                    "detailsRequired", true,
                    "authUserId", user.getId(),
                    "phone", user.getPhoneNumber(),
                    "role", user.getRole().name()
            );
        }

        if (user.getStatus() == Status.PENDING_VERIFIED) {
            return Map.of(
                    "detailsRequired", true,
                    "authUserId", user.getId(),
                    "phone", user.getPhoneNumber(),
                    "role", user.getRole().name()
            );
        }

        String access = jwt.generateAccessToken(
                user.getId(), user.getReferenceId(), role.name(), phone
        );

        String refreshRaw = createRefresh(user.getId());

        return Map.of(
                "detailsRequired", false,
                "accessToken", access,
                "refreshToken", refreshRaw,
                "role", role.name(),
                "phone", phone
        );
    }

    public Map<String, Object> completeProfile(Map<String, Object> req) {

        Long authUserId = Long.valueOf(String.valueOf(req.get("authUserId")));
        Map<String, Object> payload = (Map<String, Object>) req.get("payload");

        AuthUser user = authRepo.findById(authUserId)
                .orElseThrow();

        Long refId;

        if (user.getRole() == Role.USER) {

            refId = webClient.build()
                    .post()
                    .uri(userService + "/api/user/create")
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .map(m -> Long.valueOf(String.valueOf(m.get("id"))))
                    .block();

        } else {

            refId = webClient.build()
                    .post()
                    .uri(restaurantService + "/api/restaurant/create")
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .map(m -> Long.valueOf(String.valueOf(m.get("id"))))
                    .block();
        }

        user.setReferenceId(refId);
        user.setStatus(Status.ACTIVE);
        authRepo.save(user);

        String access = jwt.generateAccessToken(
                user.getId(),
                refId,
                user.getRole().name(),
                user.getPhoneNumber()
        );

        String refresh = createRefresh(user.getId());

        return Map.of(
                "accessToken", access,
                "refreshToken", refresh,
                "role", user.getRole().name(),
                "phone", user.getPhoneNumber()
        );
    }

    public Map<String, Object> renew(Map<String, String> req) {

        Long authUserId = Long.valueOf(req.get("authUserId"));
        String raw = req.get("refreshToken");

        RefreshToken stored = refreshRepo.findByAuthUserId(authUserId).orElse(null);
        if (stored == null) return Map.of("error", "invalid_refresh");

        if (stored.getExpiresAt() < Instant.now().getEpochSecond())
            return Map.of("error", "expired_refresh");

        if (!BCrypt.checkpw(raw, stored.getRefreshTokenHash()))
            return Map.of("error", "invalid_refresh");

        AuthUser user = authRepo.findById(authUserId).orElseThrow();

        String access = jwt.generateAccessToken(
                user.getId(),
                user.getReferenceId(),
                user.getRole().name(),
                user.getPhoneNumber()
        );

        String newRefresh = createRefresh(authUserId);

        return Map.of(
                "accessToken", access,
                "refreshToken", newRefresh
        );
    }

    public Map<String, Object> logout(Map<String, String> req) {

        Long authUserId = Long.valueOf(req.get("authUserId"));
        refreshRepo.deleteByAuthUserId(authUserId);

        return Map.of("status", "logged_out");
    }

    private String createRefresh(Long authUserId) {

        String raw = UUID.randomUUID() + "-" + UUID.randomUUID();
        String hash = BCrypt.hashpw(raw, BCrypt.gensalt());

        refreshRepo.deleteByAuthUserId(authUserId);

        RefreshToken token = RefreshToken.builder()
                .authUserId(authUserId)
                .refreshTokenHash(hash)
                .expiresAt(Instant.now().plusSeconds(refreshDays * 86400L).getEpochSecond())
                .build();

        refreshRepo.save(token);

        return raw;
    }
}
