package com.backend.restaurant_service.service;

import com.backend.restaurant_service.dto.AddMenuItemRequest;
import com.backend.restaurant_service.model.MenuItem;
import com.backend.restaurant_service.model.Restaurant;
import com.backend.restaurant_service.repository.MenuItemRepository;
import com.backend.restaurant_service.repository.RestaurantRepository;
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
                .orElseThrow(() -> new EntityNotFoundException("No restaurant found with id: " + restaurantId));

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

        if(!menuItemRepository.existsById(menuItemId)) {

            throw new EntityNotFoundException("No menu-item found with id: " + menuItemId);
        }

        menuItemRepository.deleteById(menuItemId);
    }
}
