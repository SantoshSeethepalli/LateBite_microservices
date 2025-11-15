package com.backend.auth_services.utils.dtos.otp.resposne;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOtpResponse {
    private boolean detailsRequired;
    private Long authUserId;
    private String accessToken;
    private String refreshToken;
    private String role;
    private String phone;
}
