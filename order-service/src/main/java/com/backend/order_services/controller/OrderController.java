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
    public ResponseEntity<Void> placeOrder(
            @RequestHeader("X-Ref-Id") Long userId,
            @RequestHeader("X-Role") String role,
            @RequestBody PlaceOrderRequest placeOrderRequest
    ) {
        if (!role.equals("USER")) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        orderService.placeOrder(userId, placeOrderRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/pastOrders")
    public ResponseEntity<List<OrderResponse>> getAllDeliveredOrdersOfUser(@RequestHeader("X-Ref-Id") Long userId, @RequestHeader("X-Role") String role) {

        if (role == null || !role.equalsIgnoreCase("USER")) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        List<OrderResponse> deliveredOrders = orderService.getAllDeliveredOrdersOfUser(userId);

        return ResponseEntity.ok(deliveredOrders);
    }

    @GetMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@RequestHeader("X-Ref-Id") Long currentUserId, @PathVariable Long orderId, @RequestHeader("X-Role") String role) {

        if (!role.equals("USER")) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        orderService.cancelOrder(currentUserId, orderId);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{orderId}/estimated-time")
    public ResponseEntity<Long> getEstimatedDeliveryTime(@PathVariable Long orderId,
                                                         @RequestHeader("X-Ref-Id") Long userId, @RequestHeader("X-Role") String role) {

        if (!role.equals("USER")) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        Long estimatedWaitingTime = orderService.getEstimatedDeliveryTime(orderId, userId);

        return ResponseEntity.ok(estimatedWaitingTime);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<OrderResponse>> getAllOrdersOfRestaurant(
            @PathVariable Long restaurantId,
            @RequestHeader("X-Ref-Id") Long refId,
            @RequestHeader("X-Role") String role,
            @RequestParam(required = false) String status
    ) {

        if (!role.equals("RESTAURANT")) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        if (!restaurantId.equals(refId)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

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

        if (!role.equals("RESTAURANT")) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        orderService.updateOrderStatus(restaurantId, orderId, updatedStatus);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
