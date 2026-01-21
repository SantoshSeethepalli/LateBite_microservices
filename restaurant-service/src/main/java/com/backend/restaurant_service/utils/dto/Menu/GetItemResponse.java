package com.backend.restaurant_service.utils.dto.Menu;

import com.backend.restaurant_service.model.MenuItem;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class GetItemResponse {

    private Long restaurantId;
    private String itemPhoto;
    private String itemName;
    private String description;
    private BigDecimal unitPrice;

    public static GetItemResponse from(MenuItem menuItem) {

        return GetItemResponse.builder()
                .restaurantId(menuItem.getRestaurant().getId())
                .itemPhoto(menuItem.getItemPhoto())
                .itemName(menuItem.getItemName())
                .description(menuItem.getDescription())
                .unitPrice(menuItem.getUnitPrice())
                .build();
    }
}
