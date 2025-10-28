package com.backend.order_services.controller;

import com.backend.order_services.dto.PlaceOrderRequest;
import com.backend.order_services.model.Order;
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

    @PostMapping("/placeOrder")
    @ResponseStatus(HttpStatus.CREATED)
    public void placeOrder(@RequestBody PlaceOrderRequest placeOrderRequest) {

        orderService.placeOrder(placeOrderRequest);
    }

    @GetMapping("/restaurant/orders")
    public List<Order> getAllOrder(@RequestParam Long restaurantId) {

        return orderService.getAllOrdersOfRestaurant(restaurantId);
    }
}
