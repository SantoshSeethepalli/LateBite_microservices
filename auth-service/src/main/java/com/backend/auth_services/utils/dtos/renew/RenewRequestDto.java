package com.backend.auth_services.utils.dtos.renew;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RenewRequestDto {
        private Long authUserId;
        private String refreshToken;
}

