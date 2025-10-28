package com.backend.order_services.controller;

import com.backend.order_services.dto.GetAllOrdersDtos.OrderResponse;
import com.backend.order_services.dto.PlaceOrderDtos.PlaceOrderRequest;
import com.backend.order_services.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    @ResponseStatus(HttpStatus.CREATED)
    public void placeOrder(@RequestBody PlaceOrderRequest placeOrderRequest) {

        orderService.placeOrder(placeOrderRequest);
    }

    @GetMapping("/restaurant/{restaurantId}")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getAllOrders(@PathVariable Long restaurantId, @RequestParam(required = false) String status) {

        return orderService.getAllOrdersOfRestaurant(restaurantId, status);
    }

    @PatchMapping("/updateStatus/{restaurantId}/{orderId}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrderStatus(@PathVariable Long restaurantId, @PathVariable Long orderId, @RequestParam String updatedStatus) {

        orderService.updateOrderStatus(restaurantId, orderId, updatedStatus);
    }
}
