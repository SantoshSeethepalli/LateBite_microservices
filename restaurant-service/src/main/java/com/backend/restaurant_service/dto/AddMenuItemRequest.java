package com.backend.restaurant_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddMenuItemRequest {

    private String itemPhoto;
    private String itemName;
    private String description;
    private BigDecimal price;
}
