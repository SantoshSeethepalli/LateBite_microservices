package com.backend.restaurant_service.service;

import com.backend.restaurant_service.utils.dto.AddMenuItemRequest;
import com.backend.restaurant_service.model.MenuItem;
import com.backend.restaurant_service.model.Restaurant;
import com.backend.restaurant_service.repository.MenuItemRepository;
import com.backend.restaurant_service.repository.RestaurantRepository;
import com.backend.restaurant_service.utils.exceptions.exps.MenuItemNotFoundException;
import com.backend.restaurant_service.utils.exceptions.exps.RestaurantNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public void addMenuItem(Long restaurantId, AddMenuItemRequest addMenuItemRequest) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("No restaurant found with id: " + restaurantId));

        MenuItem menuItem = MenuItem.builder()
                .restaurant(restaurant)
                .itemPhoto(addMenuItemRequest.getItemPhoto())
                .itemName(addMenuItemRequest.getItemName())
                .description(addMenuItemRequest.getDescription())
                .price(addMenuItemRequest.getPrice())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        menuItemRepository.save(menuItem);
    }

    public void deleteMenuItem(Long menuItemId) {

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                        .orElseThrow(() -> new MenuItemNotFoundException("Menu-item not found with id: " + menuItemId));

        menuItem.setUpdatedAt(LocalDateTime.now());
        menuItemRepository.deleteById(menuItemId);
    }

    public void toggleAvailability(Long menuItemId) {

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu-item not found with id: " + menuItemId));

        menuItem.setIsAvailable(!menuItem.getIsAvailable());
        menuItem.setUpdatedAt(LocalDateTime.now());

        menuItemRepository.save(menuItem);
    }
}
