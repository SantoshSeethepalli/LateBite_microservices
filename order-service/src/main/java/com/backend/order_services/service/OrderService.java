package com.backend.order_services.service;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.backend.order_services.dto.*;
import com.backend.order_services.model.*;
import com.backend.order_services.model.Enums.OrderStatus;
import com.backend.order_services.repository.*;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final WebClient.Builder webClient;

    @Transactional
    public void placeOrder(PlaceOrderRequest placeOrderRequest) {

        CartDTO cart = webClient.build()
                .get()
                .uri("http://localhost:8020/api/cart/getCartDetails?cartId=" + placeOrderRequest.getCartId())
                .retrieve()
                .bodyToMono(CartDTO.class)
                .block();

        if (cart == null || cart.getOrderedItems().isEmpty()) {

            throw new IllegalStateException("Cart is empty or not found");
        }

        Order newOrder = Order.builder()
                .userId(cart.getUserId())
                .restaurantId(cart.getRestaurantId())
                .totalAmount(cart.getTotalAmount())
                .screenShot(placeOrderRequest.getScreenShot())
                .orderStatus(OrderStatus.AWAITING_VERIFICATION)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        orderRepository.save(newOrder);


        for(CartItemDTO cartItemDTO : cart.getOrderedItems()) {

            OrderItem newOrderItem = OrderItem.builder()
                    .order(newOrder)
                    .itemId(cartItemDTO.getItemId())
                    .itemName(cartItemDTO.getItemName())
                    .quantity(cartItemDTO.getQuantity())
                    .unitPrice(cartItemDTO.getUnitPrice())
                    .totalPrice(cartItemDTO.getTotalPrice())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            orderItemRepository.save(newOrderItem);
        }
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrdersOfRestaurant(Long restaurantId) {

        return orderRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId);
    }

}
