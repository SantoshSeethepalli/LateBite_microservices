package com.backend.auth_services.controller;

import com.backend.auth_services.model.Role;
import com.backend.auth_services.service.AuthFlowService;
import com.backend.auth_services.utils.dtos.CompleteProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthFlowService service;

    @PostMapping("/send_otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> req) {

        return ResponseEntity.ok(service.sendOtp(req));
    }

    @PostMapping("/verify_otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> req) {

        return ResponseEntity.ok(service.verifyOtp(req));
    }

    @PostMapping("/complete_profile")
    public ResponseEntity<?> complete(@RequestBody CompleteProfileRequest req) {

        return ResponseEntity.ok(service.completeProfile(req));
    }

    @PostMapping("/renew_refresh")
    public ResponseEntity<?> renew(@RequestBody Map<String, String> req) {

        return ResponseEntity.ok(service.renew(req));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> req) {

        return ResponseEntity.ok(service.logout(req));
    }
}