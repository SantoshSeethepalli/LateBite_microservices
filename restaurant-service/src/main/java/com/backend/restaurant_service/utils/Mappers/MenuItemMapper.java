package com.backend.restaurant_service.utils.Mappers;

import com.backend.restaurant_service.model.MenuItem;
import com.backend.restaurant_service.model.Restaurant;
import com.backend.restaurant_service.utils.dto.AddMenuItemRequest;

import java.time.LocalDateTime;

public class MenuItemMapper {

    public static MenuItem fromMenuItemRequest(AddMenuItemRequest request, Restaurant restaurant) {

        return MenuItem.builder()
                .restaurant(restaurant)
                .itemPhoto(request.getItemPhoto())
                .itemName(request.getItemName())
                .description(request.getDescription())
                .unitPrice(request.getUnitPrice())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
