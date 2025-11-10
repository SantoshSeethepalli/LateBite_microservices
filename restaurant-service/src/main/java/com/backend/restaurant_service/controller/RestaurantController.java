package com.backend.restaurant_service.controller;


import com.backend.restaurant_service.model.Restaurant;
import com.backend.restaurant_service.service.RestaurantService;
import com.backend.restaurant_service.utils.dto.RestaurantUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PatchMapping("/update/{restaurantId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateRestaurantSettings(@PathVariable Long restaurantId, @RequestBody RestaurantUpdateRequest request) {

          restaurantService.updateSettings(restaurantId, request);
    }
}
