package com.backend.restaurant_service.controller;

import com.backend.restaurant_service.service.RestaurantService;
import com.backend.restaurant_service.utils.dto.CreateRestaurantRequest;
import com.backend.restaurant_service.utils.dto.RestaurantUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/create")
    public ResponseEntity<Long> createRestaurant(
            @RequestBody CreateRestaurantRequest request
    ) {

        Long restaurantId = restaurantService.createRestaurant(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantId);
    }

    @PatchMapping("/update}")
    public ResponseEntity<String> updateRestaurantSettings(
            @RequestHeader("X-Ref-Id") Long restaurantId,
            @RequestHeader("X-Role") String role,
            @RequestBody RestaurantUpdateRequest request
    ) {
        if (!role.equals("RESTAURANT")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        restaurantService.updateSettings(restaurantId, request);

        return ResponseEntity.ok().body("Profile Updated");
    }
}
