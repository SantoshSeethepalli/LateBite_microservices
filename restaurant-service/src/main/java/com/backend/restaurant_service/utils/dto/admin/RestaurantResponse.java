package com.backend.restaurant_service.utils.dto.admin;

import com.backend.restaurant_service.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponse {

    private Long id;
    private String username;
    private String phoneNumber;
    private String upiId;
    private String profilePhoto;
    private String createdAt;
    private String updatedAt;

    public static RestaurantResponse from(Restaurant r) {
        return new RestaurantResponse(
                r.getId(),
                r.getUsername(),
                r.getPhoneNumber(),
                r.getUpiId(),
                r.getProfilePhoto(),
                r.getCreatedAt().toString(),
                r.getUpdatedAt().toString()
        );
    }
}
