package com.backend.auth_services.utils.dtos.admin_login;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRestaurantAuthRequest {
    private String phoneNumber;
}
