package com.backend.auth_services.controller;

import com.backend.auth_services.service.AuthFlowService;
import com.backend.auth_services.utils.dtos.admin_login.CreateRestaurantAuthRequest;
import com.backend.auth_services.utils.dtos.complete_profile.request.CompleteProfileRequest;
import com.backend.auth_services.utils.dtos.complete_profile.response.CompleteProfileResponse;
import com.backend.auth_services.utils.dtos.logout.*;
import com.backend.auth_services.utils.dtos.otp.request.*;
import com.backend.auth_services.utils.dtos.otp.resposne.*;
import com.backend.auth_services.utils.dtos.renew.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthFlowService service;

    @PostMapping("/send-otp")
    public ResponseEntity<SendOtpResponse> sendOtp(@RequestBody SendOtpRequest req) {

        return ResponseEntity.ok(service.sendOtp(req));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<VerifyOtpResponse> verifyOtp(@RequestBody VerifyOtpRequest req) {

        return ResponseEntity.ok(service.verifyOtp(req));
    }

    @PostMapping("/complete-profile")
    public ResponseEntity<CompleteProfileResponse> complete(@RequestBody CompleteProfileRequest req) {

        return ResponseEntity.ok(service.completeProfile(req));
    }


    @PostMapping("/renew-refresh")
    public ResponseEntity<RenewResponseDto> renew(@RequestBody RenewRequestDto req) {

        return ResponseEntity.ok(service.renew(req));
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestBody LogoutRequest req) {

        return ResponseEntity.ok().body(service.logout(req));
    }
}