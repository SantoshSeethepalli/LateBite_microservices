package com.backend.auth_services.utils.dtos.complete_profile.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRestaurantRequest implements ProfilePayload {

    @NotBlank
    private String username;

    private String profilePhoto;

    private String phoneNumber;

    @NotBlank
    private String upiId;
}
