package com.backend.restaurant_service.utils.dto.Menu;

import com.backend.restaurant_service.model.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CompleteMenuResponse {

    private Long id;
    private String itemPhoto;
    private String itemName;
    private String description;
    private BigDecimal unitPrice;
    private Boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CompleteMenuResponse from(MenuItem menuItem) {

        return CompleteMenuResponse.builder()
                .id(menuItem.getId())
                .itemPhoto(menuItem.getItemPhoto())
                .itemName(menuItem.getItemName())
                .description(menuItem.getDescription())
                .unitPrice(menuItem.getUnitPrice())
                .isAvailable(menuItem.getIsAvailable())
                .createdAt(menuItem.getCreatedAt())
                .updatedAt(menuItem.getUpdatedAt())
                .build();
    }
}
