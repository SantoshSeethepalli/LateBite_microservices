package com.backend.restaurant_service.controller;

import com.backend.restaurant_service.utils.dto.AddMenuItemRequest;
import com.backend.restaurant_service.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/MenuItem")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService  menuItemService;

    @PostMapping("/add/restaurant/{restaurantId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addMenuItem(@PathVariable Long restaurantId, @RequestBody AddMenuItemRequest addMenuItemRequest) {

        menuItemService.addMenuItem(restaurantId, addMenuItemRequest);
    }

    @DeleteMapping("/remove/{menuItemId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteMenuItem(@PathVariable Long menuItemId) {

        menuItemService.deleteMenuItem(menuItemId);
    }

    @PutMapping("/{menuItemId}/toggle-availability")
    @ResponseStatus(HttpStatus.OK)
    public void toggleAvailability(@PathVariable Long menuItemId) {

        menuItemService.toggleAvailability(menuItemId);
    }
}
