package com.backend.restaurant_service.controller;

import com.backend.restaurant_service.utils.dto.AddMenuItemRequest;
import com.backend.restaurant_service.service.MenuItemService;
import com.backend.restaurant_service.utils.dto.Menu.AvailableMenuItems;
import com.backend.restaurant_service.utils.dto.Menu.CompleteMenuResponse;
import com.backend.restaurant_service.utils.dto.Menu.GetItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-item")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PostMapping("/addItem")
    public ResponseEntity<String> addMenuItem(
            @RequestHeader("X-Ref-Id") Long restaurantId,
            @RequestHeader("X-Role") String role,
            @RequestBody AddMenuItemRequest addMenuItemRequest) {

        if (!role.equals("RESTAURANT")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        menuItemService.addMenuItem(restaurantId, addMenuItemRequest);

        return ResponseEntity.ok().body("Added");
    }

    @DeleteMapping("/remove/{menuItemId}")
    public ResponseEntity<String> deleteMenuItem(
            @RequestHeader("X-Ref-Id") Long restaurantId,
            @RequestHeader("X-Role") String role,
            @PathVariable Long menuItemId) {

        if (!role.equals("RESTAURANT")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        menuItemService.deleteMenuItem(menuItemId, restaurantId);

        return ResponseEntity.ok().body("Deleted");
    }

    @PutMapping("/{menuItemId}/toggle-availability")
    public ResponseEntity<String> toggleAvailability(
            @RequestHeader("X-Ref-Id") Long restaurantId,
            @RequestHeader("X-Role") String role,
            @PathVariable Long menuItemId) {

        if (!role.equals("RESTAURANT")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        menuItemService.toggleAvailability(menuItemId, restaurantId);

        return ResponseEntity.ok().body("Request processed");
    }

    @GetMapping("/restaurant")
    public ResponseEntity<List<AvailableMenuItems>> getAvailableItemsOfRestaurantToUser(
            @RequestHeader("X-Role") String role,
            @RequestParam Long restaurantId) {

        if (!role.equals("USER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok().body(menuItemService.getAvailableItemsOfRestaurantToUser(restaurantId));
    }

    @GetMapping("/restaurant/self")
    public ResponseEntity<List<CompleteMenuResponse>> getMenuOfRestaurant(
            @RequestHeader("X-Ref-Id") Long restaurantId,
            @RequestHeader("X-Role") String role) {

        if (!role.equals("RESTAURANT")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(menuItemService.getMenuOfRestaurant(restaurantId));
    }

    @GetMapping("/{menuItemId}")
    public ResponseEntity<GetItemResponse> getItemDetails(
            @RequestHeader("X-Role") String role,
            @PathVariable Long menuItemId) {

        if (!role.equals("USER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok().body(menuItemService.getItemDetails(menuItemId));
    }
}