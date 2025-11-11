package com.backend.order_services.controller;

import com.backend.order_services.utils.dto.GetAllOrdersDtos.OrderResponse;
import com.backend.order_services.utils.dto.PlaceOrderDtos.PlaceOrderRequest;
import com.backend.order_services.service.OrderService;
import jakarta.persistence.criteria.CriteriaBuilder;
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
    public ResponseEntity<Void> placeOrder(@RequestBody PlaceOrderRequest placeOrderRequest) {

        orderService.placeOrder(placeOrderRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getAllDeliveredOrdersOfUser(@PathVariable Long userId) {

        List<OrderResponse> deliveredOrders = orderService.getAllDeliveredOrdersOfUser(userId);

        return ResponseEntity.ok(deliveredOrders);
    }

    @GetMapping("/{userId}/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long userId, @PathVariable Long orderId)  {

        orderService.cancelOrder(userId, orderId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{orderId}/estimated-time")
    public ResponseEntity<Long> getEstimatedDeliveryTime(@PathVariable Long orderId, @PathVariable Long restaurantId, @PathVariable Long userId) {

        Long estimatedWaitingTime = orderService.getEstimatedDeliveryTime(userId);

        return ResponseEntity.ok(estimatedWaitingTime);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<OrderResponse>> getAllOrdersOfRestaurant(@PathVariable Long restaurantId, @RequestParam(required = false) String status) {

        List<OrderResponse> responses = orderService.getAllOrdersOfRestaurant(restaurantId, status);

        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/updateStatus/restaurant/{restaurantId}/order/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long restaurantId, @PathVariable Long orderId, @RequestParam String updatedStatus) {

        orderService.updateOrderStatus(restaurantId, orderId, updatedStatus);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
