package com.backend.order_services.service;

import com.backend.order_services.utils.dto.GetAllOrdersDtos.OrderResponse;
import com.backend.order_services.utils.dto.PlaceOrderDtos.CartDTO;
import com.backend.order_services.utils.dto.PlaceOrderDtos.PlaceOrderRequest;
import com.backend.order_services.utils.Mapper.OrderResponseMapper;
import com.backend.order_services.utils.dto.statistics.OrderStatistics;
import com.backend.order_services.utils.dto.statistics.RestaurantOrderStats;
import com.backend.order_services.utils.exceptions.exps.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.cglib.core.Local;
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
    private final StatisticsService statisticsService;

    private final Long averageItemPreparingTime = 5L; // in minutes

    private final WebClient.Builder webClientBuilder;

    @Transactional
    public void placeOrder(Long userId, PlaceOrderRequest placeOrderRequest) {

        String utr = placeOrderRequest.getUtrNumber();

        if (utr == null || utr.isBlank()) {
            throw new UtrNumberNotFound("UTR number is required");
        }

        // allow only digits (UPI UTRs are numeric)
        if (!utr.matches("\\d+")) {
            throw new UtrNumberNotValid("Invalid UTR number. Must contain only digits.");
        }

        CartDTO cart = getCartDetails(userId, placeOrderRequest);

        if(!cart.getUserId().equals(userId)) {
            throw new AccessDeniedException("You can't handle this cart");
        }

        if(cart == null || cart.getOrderedItems().isEmpty()) {
            throw new CartNotFoundException("Cart is empty or not found");
        }

        Order savedOrder =
                orderRepository.saveAndFlush(
                OrderResponseMapper.toOrder(cart, placeOrderRequest.getUtrNumber())
        );

        List<OrderItem> orderItems = cart.getOrderedItems().stream()
                .map(item -> OrderResponseMapper.toOrderItem(item, savedOrder))
                .toList();

        orderItemRepository.saveAll(orderItems);

        deleteCart(placeOrderRequest.getCartId(), userId);
    }

    private CartDTO getCartDetails(Long userId, PlaceOrderRequest placeOrderRequest) {

        return webClientBuilder.build()
                .get()
                .uri("http://cart-service/api/cart/{cartId}", placeOrderRequest.getCartId())
                .header("X-Ref-Id", String.valueOf(userId))
                .header("X-Role", "USER")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new CartNotFoundException("Cart not found or invalid cart ID")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RuntimeException("Cart service unavailable")))
                .bodyToMono(CartDTO.class)
                .block();
    }

    private void deleteCart(Long cartId, Long userId) {

        webClientBuilder.build()
                .delete()
                .uri("http://cart-service/api/cart/{cartId}", cartId)
                .header("X-Ref-Id", String.valueOf(userId))
                .header("X-Role", "USER")
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(
                                new CartNotFoundException("Cart not found or already deleted")
                        )
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(
                                new RuntimeException("Cart service unavailable")
                        )
                )
                .toBodilessEntity()
                .block();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrdersOfRestaurant(Long restaurantId, String status) {

        List<Order> orders;

        if(status != null && !status.isEmpty()) {

            orders = orderRepository.findByRestaurantIdAndOrderStatusOrderByCreatedAtDesc(restaurantId, OrderStatus.valueOf(status));
        } else {

            orders = orderRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId);
        }

        if(orders.isEmpty()) return Collections.emptyList();

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

            return new ArrayList<>();
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

    @Transactional(readOnly = true)
    public Long getEstimatedDeliveryTime(Long orderId, Long userId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        if(!order.getUserId().equals(userId)) throw new AccessDeniedException("This is not you order");

        List<Order> activeOrders = orderRepository.findByRestaurantIdAndOrderStatusOrderByCreatedAtAsc(
                order.getRestaurantId(), OrderStatus.CONFIRMED_AND_PREPARING
        );

        List<Order> ordersBeforeIncludingCurrentOrder = activeOrders.stream()
                .takeWhile(o -> !orderId.equals(o.getId()))
                .collect(Collectors.toList());

        ordersBeforeIncludingCurrentOrder.add(order);

        Long totalItemsInQueue = ordersBeforeIncludingCurrentOrder.stream()
                .mapToLong(orderItemRepository::countByOrder)
                .sum();

        return totalItemsInQueue * averageItemPreparingTime;
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found with ID: " + orderId)
                );

        List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());

        return OrderResponse.mapToOrderResponse(order, items);
    }


    @Transactional(readOnly = true)
    public List<Order> getActiveOrdersByUserId(Long userId) {

        List<OrderStatus> activeStatuses = Arrays.asList(
                OrderStatus.AWAITING_VERIFICATION,
                OrderStatus.CONFIRMED_AND_PREPARING,
                OrderStatus.READY_TO_PICKUP
        );

        return orderRepository.findByUserIdAndOrderStatusIn(userId, activeStatuses);
    }

    @Transactional(readOnly = true)
    public OrderStatistics getOrderStatistics() {
        long totalOrders = orderRepository.count();
        long activeOrders = orderRepository.countByOrderStatusIn(Arrays.asList(
                OrderStatus.AWAITING_VERIFICATION,
                OrderStatus.CONFIRMED_AND_PREPARING,
                OrderStatus.READY_TO_PICKUP,
                OrderStatus.DELIVERED
        ));
        long completedOrders = orderRepository.countByOrderStatus(OrderStatus.DELIVERED);
        long cancelledOrders = orderRepository.countByOrderStatus(OrderStatus.CANCELLED);

        return new OrderStatistics(totalOrders, activeOrders, completedOrders, cancelledOrders);
    }

    @Transactional(readOnly = true)
    public RestaurantOrderStats getRestaurantStatistics(Long restaurantId) {

        Long totalOrders = statisticsService.getTotalOrderOfRestaurant(restaurantId);
        Long ordersDelivered = statisticsService.getCountOfDeliveredOrderOfRestaurant(restaurantId);
        Long ordersCancelled = statisticsService.getCountOfCancelledOrdersOfRestaurant(restaurantId);

        BigDecimal overallRevenue = statisticsService.getOverallRevenue(restaurantId);
        BigDecimal lastWeekRevenue = statisticsService.getLastWeekRevenue(restaurantId);
        BigDecimal lastMonthRevenue = statisticsService.getLastMonthRevenue(restaurantId);
        BigDecimal lastYearRevenue = statisticsService.getLastYearRevenue(restaurantId);

        return new RestaurantOrderStats(
                totalOrders,
                ordersDelivered,
                ordersCancelled,
                overallRevenue,
                lastWeekRevenue,
                lastMonthRevenue,
                lastYearRevenue
        );
    }



}