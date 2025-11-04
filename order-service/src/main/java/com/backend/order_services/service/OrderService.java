package com.backend.order_services.service;

import com.backend.order_services.utils.dto.GetAllOrdersDtos.OrderResponse;
import com.backend.order_services.utils.dto.PlaceOrderDtos.CartDTO;
import com.backend.order_services.utils.dto.PlaceOrderDtos.CartItemDTO;
import com.backend.order_services.utils.dto.PlaceOrderDtos.PlaceOrderRequest;
import com.backend.order_services.utils.Mapper.OrderResponseMapper;
import com.backend.order_services.utils.exceptions.exps.AccessDeniedException;
import com.backend.order_services.utils.exceptions.exps.CartNotFoundException;
import com.backend.order_services.utils.exceptions.exps.OrderNotFoundException;
import com.backend.order_services.utils.exceptions.exps.UnauthorizedOrderUpdateException;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.backend.order_services.model.*;
import com.backend.order_services.model.Enums.OrderStatus;
import com.backend.order_services.repository.*;
import reactor.core.publisher.Mono;


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
                .uri("http://localhost:8020/api/cart/getCartDetails/cartId?cartId=" + placeOrderRequest.getCartId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new CartNotFoundException("Cart not found or invalid cart ID")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RuntimeException("Cart service unavailable")))
                .bodyToMono(CartDTO.class)
                .block();


        if (cart == null || cart.getOrderedItems().isEmpty()) throw new CartNotFoundException("Cart is empty or not found");

        Order newOrder = OrderResponseMapper.toOrder(cart, placeOrderRequest.getScreenShot());
        orderRepository.save(newOrder);

        for(CartItemDTO cartItemDTO : cart.getOrderedItems()) {

            OrderItem newOrderItem = OrderResponseMapper.toOrderItem(cartItemDTO, newOrder);
            orderItemRepository.save(newOrderItem);
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrdersOfRestaurant(Long restaurantId, String status) {

        List<Order> orders;

        if(status != null && !status.isEmpty()) {

            orders = orderRepository.findByRestaurantIdAndOrderStatusOrderByCreatedAtDesc(restaurantId, OrderStatus.valueOf(status));
        } else {

            orders = orderRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId);
        }

        if(orders.isEmpty()) {

            throw new OrderNotFoundException("No orders found for the Restaurant with id: " + restaurantId);
        }

        List<OrderResponse> responses = new ArrayList<>();

        for (Order order : orders) {

            List<OrderItem> items = orderItemRepository.findByOrder(order);
            OrderResponse response = OrderResponseMapper.toOrderResponse(order, items);

            responses.add(response);
        }

        return responses;
    }

    @Transactional
    public void updateOrderStatus(Long restaurantId, Long orderId, String updatedStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));;

        if (!Objects.equals(order.getRestaurantId(), restaurantId)) {

            throw new UnauthorizedOrderUpdateException("You are not allowed to update this order.");
        }

        // Update fields
        order.setOrderStatus(OrderStatus.valueOf(updatedStatus));
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllDeliveredOrdersOfUser(Long userId) {


        List<Order> orders = orderRepository.findByUserIdAndOrderStatusOrderByCreatedAtDesc(userId, OrderStatus.DELIVERED);

        if(orders.isEmpty()) {

            throw new OrderNotFoundException("No orders found with user id: " + userId);
        }

        List<OrderResponse> responses = new ArrayList<>();

        for (Order order : orders) {

            List<OrderItem> items = orderItemRepository.findByOrder(order);
            OrderResponse response = OrderResponseMapper.toOrderResponse(order, items);

            responses.add(response);
        }

        return responses;
    }

    @Transactional
    public void cancelOrder(Long userId, Long orderId) {

        Order orderToBeCancelled = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        if (!orderToBeCancelled.getUserId().equals(userId)) {

            throw new AccessDeniedException("You are not allowed to cancel this order.");
        }

        if(!orderToBeCancelled.getOrderStatus().equals(OrderStatus.AWAITING_VERIFICATION)) throw new UnauthorizedOrderUpdateException("Cannot cancel the order!");

        orderToBeCancelled.setOrderStatus(OrderStatus.CANCELLED);
        orderToBeCancelled.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(orderToBeCancelled);
    }
}