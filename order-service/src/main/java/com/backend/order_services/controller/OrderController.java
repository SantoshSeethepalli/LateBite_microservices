package com.backend.order_services.controller;

import com.backend.order_services.model.Order;
import com.backend.order_services.utils.dto.GetAllOrdersDtos.OrderResponse;
import com.backend.order_services.utils.dto.PlaceOrderDtos.PlaceOrderRequest;
import com.backend.order_services.service.OrderService;
import com.backend.order_services.utils.dto.statistics.OrderStatistics;
import com.backend.order_services.utils.dto.statistics.RestaurantOrderStats;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<Void> placeOrder(
            @RequestHeader("X-Ref-Id") Long userId,
            @RequestHeader("X-Role") String role,
            @RequestBody PlaceOrderRequest placeOrderRequest
    ) {
        if (!role.equals("USER")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        orderService.placeOrder(userId, placeOrderRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/pastOrders")
    public ResponseEntity<List<OrderResponse>> getAllDeliveredOrdersOfUser(@RequestHeader("X-Ref-Id") Long userId, @RequestHeader("X-Role") String role) {

        if (role == null || !role.equalsIgnoreCase("USER")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        List<OrderResponse> deliveredOrders = orderService.getAllDeliveredOrdersOfUser(userId);

        return ResponseEntity.ok(deliveredOrders);
    }

    @GetMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@RequestHeader("X-Ref-Id") Long currentUserId, @PathVariable Long orderId, @RequestHeader("X-Role") String role) {

        if (!role.equals("USER")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        orderService.cancelOrder(currentUserId, orderId);

        return ResponseEntity.ok().body("Order Cancelled.");
    }


    @GetMapping("/{orderId}/estimated-time")
    public ResponseEntity<Long> getEstimatedDeliveryTime(@PathVariable Long orderId,
                                                         @RequestHeader("X-Ref-Id") Long userId, @RequestHeader("X-Role") String role) {

        if (!role.equals("USER")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        Long estimatedWaitingTime = orderService.getEstimatedDeliveryTime(orderId, userId);

        return ResponseEntity.ok(estimatedWaitingTime);
    }

    @GetMapping("/restaurant/allOrders")
    public ResponseEntity<List<OrderResponse>> getAllOrdersOfRestaurant(
            @RequestHeader("X-Ref-Id") Long restaurantId,
            @RequestHeader("X-Role") String role,
            @RequestParam(required = false) String status
    ) {

        if (!role.equals("RESTAURANT")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        List<OrderResponse> responses = orderService.getAllOrdersOfRestaurant(restaurantId, status);

        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{orderId}/updateStatus")
    public ResponseEntity<Void> updateOrderStatus(
            @RequestHeader("X-Ref-Id") Long restaurantId,
            @RequestHeader("X-Role") String role,
            @PathVariable Long orderId,
            @RequestParam String updatedStatus
    ) {

        if (!role.equals("RESTAURANT")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        orderService.updateOrderStatus(restaurantId, orderId, updatedStatus);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long orderId,
            @RequestHeader("X-Ref-Id") Long userId,
            @RequestHeader("X-Role") String role) {

        OrderResponse order = orderService.getOrderById(orderId);

        if (role.equals("USER") && !order.getUserId().equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        if (role.equals("RESTAURANT") && !order.getRestaurantId().equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(order);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Order>> getActiveOrders(
            @RequestHeader("X-Ref-Id") Long userId,
            @RequestHeader("X-Role") String role) {

        if (!role.equals("USER")) {
            return ResponseEntity.status(403).build();
        }

        List<Order> activeOrders = orderService.getActiveOrdersByUserId(userId);
        return ResponseEntity.ok(activeOrders);
    }

    @GetMapping("/admin/statistics")
    public ResponseEntity<OrderStatistics> getOrderStatistics(
            @RequestHeader("X-Role") String role) {

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(403).build();
        }

        OrderStatistics stats = orderService.getOrderStatistics();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/restaurant/statistics")
    public ResponseEntity<RestaurantOrderStats> getRestaurantStatistics(
            @RequestHeader("X-Ref-Id") Long restaurantId,
            @RequestHeader("X-Role") String role) {

        if (!role.equals("RESTAURANT")) {
            return ResponseEntity.status(403).build();
        }

        RestaurantOrderStats stats = orderService.getRestaurantStatistics(restaurantId);
        return ResponseEntity.ok(stats);
    }
}
