package com.backend.order_services.utils.Mapper;

import com.backend.order_services.model.Enums.OrderStatus;
import com.backend.order_services.utils.dto.GetAllOrdersDtos.OrderItemResponse;
import com.backend.order_services.utils.dto.GetAllOrdersDtos.OrderResponse;
import com.backend.order_services.model.Order;
import com.backend.order_services.model.OrderItem;
import com.backend.order_services.utils.dto.PlaceOrderDtos.CartDTO;
import com.backend.order_services.utils.dto.PlaceOrderDtos.CartItemDTO;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
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

    public static Order toOrder(CartDTO cart, String screenshot) {

        return Order.builder()
                .userId(cart.getUserId())
                .restaurantId(cart.getRestaurantId())
                .totalAmount(cart.getTotalAmount())
                .screenShot(screenshot)
                .orderStatus(OrderStatus.AWAITING_VERIFICATION)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static OrderItem toOrderItem(CartItemDTO cartItemDTO, Order newOrder) {

        return OrderItem.builder()
                .order(newOrder)
                .itemId(cartItemDTO.getItemId())
                .itemName(cartItemDTO.getItemName())
                .quantity(cartItemDTO.getQuantity())
                .unitPrice(cartItemDTO.getUnitPrice())
                .totalPrice(cartItemDTO.getTotalPrice())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
