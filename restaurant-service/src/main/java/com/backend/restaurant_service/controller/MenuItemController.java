package com.backend.restaurant_service.controller;

import com.backend.restaurant_service.utils.dto.AddMenuItemRequest;
import com.backend.restaurant_service.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/MenuItem")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService  menuItemService;

    @PostMapping("/addItem")
    @ResponseStatus(HttpStatus.CREATED)
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
    @ResponseStatus(HttpStatus.OK)
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
    @ResponseStatus(HttpStatus.OK)
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
}
