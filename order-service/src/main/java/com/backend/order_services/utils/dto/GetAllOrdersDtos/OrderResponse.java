package com.backend.order_services.utils.dto.GetAllOrdersDtos;

import com.backend.order_services.model.Order;
import com.backend.order_services.model.OrderItem;
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
    private String utrNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<OrderItemResponse> orderItems;

    public static OrderResponse mapToOrderResponse(Order order, List<OrderItem> items) {

        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .restaurantId(order.getRestaurantId())
                .orderStatus(order.getOrderStatus().name())
                .totalAmount(order.getTotalAmount())
                .utrNumber(order.getUtrNumber())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .orderItems(OrderItemResponse.mapToOrderItemResponse(items))
                .build();
    }
}
