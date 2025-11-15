package com.backend.auth_services.utils.dtos.otp.resposne;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendOtpResponse {
    private String status;
}
