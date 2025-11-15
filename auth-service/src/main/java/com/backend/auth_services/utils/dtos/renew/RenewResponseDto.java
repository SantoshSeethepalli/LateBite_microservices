package com.backend.auth_services.utils.dtos.renew;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RenewResponseDto {
    private String accessToken;
    private String refreshToken;
}
