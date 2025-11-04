package com.backend.cart_service.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequiredItemDetails {

    private Long restaurantId;
    private String itemName;
    private String description;
    private BigDecimal unitPrice;
}
