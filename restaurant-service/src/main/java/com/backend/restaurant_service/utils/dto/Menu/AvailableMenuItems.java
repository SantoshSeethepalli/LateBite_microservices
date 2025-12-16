package com.backend.restaurant_service.utils.dto.Menu;

import com.backend.restaurant_service.model.MenuItem;
import com.backend.restaurant_service.model.Restaurant;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AvailableMenuItems {

    private Long id;
    private String itemPhoto;
    private String itemName;
    private String description;
    private BigDecimal unitPrice;

    public static AvailableMenuItems from(MenuItem menuItem) {

        return AvailableMenuItems.builder()
                .id(menuItem.getId())
                .itemPhoto(menuItem.getItemPhoto())
                .itemName(menuItem.getItemName())
                .description(menuItem.getDescription())
                .unitPrice(menuItem.getUnitPrice())
                .build();
    }
}
