package com.backend.order_services.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderDTO {

    // DTO for creating Order

    private Long cartId;
    private Long userId;
    private Long restaurantId;
    private BigDecimal totalAmount;
}
