package com.backend.order_services.controller;

import com.backend.order_services.utils.dto.GetAllOrdersDtos.OrderResponse;
import com.backend.order_services.utils.dto.PlaceOrderDtos.PlaceOrderRequest;
import com.backend.order_services.service.OrderService;
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

    @GetMapping("/restaurant/{restaurantId}")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getAllOrdersOfRestaurant(@PathVariable Long restaurantId, @RequestParam(required = false) String status) {

        return orderService.getAllOrdersOfRestaurant(restaurantId, status);
    }

    @PatchMapping("/updateStatus/{restaurantId}/{orderId}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrderStatus(@PathVariable Long restaurantId, @PathVariable Long orderId, @RequestParam String updatedStatus) {

        orderService.updateOrderStatus(restaurantId, orderId, updatedStatus);
    }

    @GetMapping("/user/{userId")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getAllDeliveredOrdersOfUser(@PathVariable Long userId) {

        return orderService.getAllDeliveredOrdersOfUser(userId);
    }

    @GetMapping("/{userId}/{orderId}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelOrder(@PathVariable Long userId, @PathVariable Long orderId)  {

        orderService.cancelOrder(userId, orderId);
    }
}
