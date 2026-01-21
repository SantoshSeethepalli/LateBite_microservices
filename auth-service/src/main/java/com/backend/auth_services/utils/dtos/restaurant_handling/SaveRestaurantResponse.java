package com.backend.auth_services.utils.dtos.restaurant_handling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveRestaurantResponse {

    private Long restaurantId;
    private String accessToken;
    private String refreshToken;
    private String role;
    private String phone;
}
