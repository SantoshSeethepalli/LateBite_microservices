package com.backend.restaurant_service.controller;

import com.backend.restaurant_service.model.Restaurant;
import com.backend.restaurant_service.service.RestaurantService;
import com.backend.restaurant_service.utils.dto.CreateRestaurantRequest;
import com.backend.restaurant_service.utils.dto.RestaurantUpdateRequest;
import com.backend.restaurant_service.utils.dto.admin.RestaurantResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/create")
    public ResponseEntity<Long> createRestaurant(
            @RequestHeader("X-Role") String role,
            @RequestBody CreateRestaurantRequest request
    ) {
        if (!role.equals("ADMIN")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        Long restaurantId = restaurantService.createRestaurant(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantId);
    }

    @PatchMapping("/update")
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

    @GetMapping("/browse")
    public ResponseEntity<List<RestaurantResponse>> getOpenRestaurants(
        @RequestHeader("X-Role") String role
    ) {
        
        if(!role.equals("USER")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(restaurantService.getOpenRestaurants());
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRestaurants(
            @RequestHeader("X-Role") String role
    ) {
        if (!role.equals("ADMIN"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok(restaurantService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRestaurant(
            @RequestHeader("X-Role") String role,
            @PathVariable Long id
    ) {
        if (!role.equals("ADMIN"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        restaurantService.delete(id);

        return ResponseEntity.ok("Deleted");
    }

    @GetMapping("/profile")
    public ResponseEntity<Restaurant> getRestaurantProfile(
            @RequestHeader("X-Ref-Id") Long restaurantId,
            @RequestHeader("X-Role") String role) {

        if (!role.equals("RESTAURANT")) {
            return ResponseEntity.status(403).build();
        }

        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        return ResponseEntity.ok(restaurant);
    }

    @PatchMapping("/toggle-operating-status")
    public ResponseEntity<String> toggleOperatingStatus(
            @RequestHeader("X-Ref-Id") Long restaurantId,
            @RequestHeader("X-Role") String role) {

        if (!role.equals("RESTAURANT")) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok().body(restaurantService.toggleOperatingStatus(restaurantId));
    }
}
