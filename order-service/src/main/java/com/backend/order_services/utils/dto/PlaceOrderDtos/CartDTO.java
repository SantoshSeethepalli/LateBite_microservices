package com.backend.order_services.utils.dto.PlaceOrderDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    // DTO for creating Order
    private Long userId;
    private Long restaurantId;
    private BigDecimal totalAmount;

    private List<CartItemDTO> orderedItems;
}
