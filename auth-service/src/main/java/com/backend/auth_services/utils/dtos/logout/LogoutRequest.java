package com.backend.auth_services.utils.dtos.logout;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogoutRequest {
    private Long authUserId;
}
