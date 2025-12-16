package com.backend.auth_services.utils.dtos.restaurant_handling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveRestaurantRequest {
    private String username;
    private String phone;
    private String upiId;
    private String profilePhoto;
}

