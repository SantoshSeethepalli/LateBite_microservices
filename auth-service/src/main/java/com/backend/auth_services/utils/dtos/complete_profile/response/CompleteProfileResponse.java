package com.backend.auth_services.utils.dtos.complete_profile.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompleteProfileResponse {
    private String accessToken;
    private String refreshToken;
    private String role;
    private String phone;
}
