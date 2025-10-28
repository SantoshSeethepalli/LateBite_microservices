package com.backend.order_services.service.Helpers;

import com.backend.order_services.dto.GetAllOrdersDtos.OrderItemResponse;
import com.backend.order_services.dto.GetAllOrdersDtos.OrderResponse;
import com.backend.order_services.model.Order;
import com.backend.order_services.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderResponseMapper {

    // converts one OrderItem entity -> OrderItemResponse DTO
    public static OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getItemId(),
                item.getItemName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice()
        );
    }

    // converts one Order entity (and its items) -> OrderResponse DTO
    public static OrderResponse toOrderResponse(Order order, List<OrderItem> items) {
        List<OrderItemResponse> itemResponses = new ArrayList<>();

        for (OrderItem item : items) {
            itemResponses.add(toItemResponse(item));
        }

        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .restaurantId(order.getRestaurantId())
                .orderStatus(order.getOrderStatus().name())
                .totalAmount(order.getTotalAmount())
                .screenShot(order.getScreenShot())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .orderItems(itemResponses)
                .build();
    }
}
