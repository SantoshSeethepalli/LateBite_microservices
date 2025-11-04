package com.backend.cart_service.controller;

import com.backend.cart_service.utils.dto.CartDetailsResponse;
import com.backend.cart_service.model.Cart;
import com.backend.cart_service.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/create")
    public ResponseEntity<Cart> createOrGetCart(
            @RequestParam Long userId,
            @RequestParam Long restaurantId) {
        Cart cart = cartService.createOrGetCart(userId, restaurantId);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/getCartDetails/cartId")
    public ResponseEntity<CartDetailsResponse> getCartDetails(@RequestParam Long cartId) {

        CartDetailsResponse response = cartService.getCompleteCartDetails(cartId);
        cartService.deleteCart(cartId);

        return ResponseEntity.ok(response);
    }
}
