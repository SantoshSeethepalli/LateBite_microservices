package com.backend.auth_services.utils.dtos.admin_login;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginRequest {
    private String phone;
    private String otp;
}
