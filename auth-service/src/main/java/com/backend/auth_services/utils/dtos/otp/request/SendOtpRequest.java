package com.backend.auth_services.utils.dtos.otp.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendOtpRequest {

    @NotBlank
    private String phone;

    @NotBlank
    private String role;
}
