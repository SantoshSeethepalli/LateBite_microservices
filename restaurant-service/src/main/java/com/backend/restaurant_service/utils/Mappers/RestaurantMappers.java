package com.backend.restaurant_service.utils.Mappers;

import com.backend.restaurant_service.model.Restaurant;
import com.backend.restaurant_service.utils.dto.CreateRestaurantRequest;

import java.time.LocalDateTime;

public class RestaurantMappers {

    public static Restaurant fromCreateRestaurantRequest(CreateRestaurantRequest request) {

        return Restaurant.builder()
                .username(request.getUsername())
                .phoneNumber(request.getPhoneNumber())
                .upiId(request.getUpiId())
                .profilePhoto(request.getProfilePhoto())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
