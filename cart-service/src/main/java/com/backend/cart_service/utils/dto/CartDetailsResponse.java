package com.backend.cart_service.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailsResponse {

    private Long userId;
    private Long restaurantId;
    private BigDecimal totalAmount;

    private List<CartItemDTO> orderedItems;
}
