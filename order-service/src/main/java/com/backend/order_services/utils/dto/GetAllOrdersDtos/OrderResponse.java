package com.backend.order_services.utils.dto.GetAllOrdersDtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;
    private Long userId;
    private Long restaurantId;
    private String orderStatus;
    private BigDecimal totalAmount;
    private BigDecimal utrNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<OrderItemResponse> orderItems;
}
