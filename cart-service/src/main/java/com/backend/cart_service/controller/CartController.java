package com.backend.cart_service.controller;

import com.backend.cart_service.utils.dto.CartDetailsResponse;
import com.backend.cart_service.model.Cart;
import com.backend.cart_service.service.CartService;
import com.backend.cart_service.utils.exceptions.exps.IllegalAccessException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping("/getCartDetails/cartId")
    public ResponseEntity<CartDetailsResponse> getCartDetails(
            @RequestHeader("X-Ref-Id") Long userId,
            @RequestHeader("X-Role") String role,
            @RequestParam Long cartId
    ) {

        if (!role.equals("USER")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        CartDetailsResponse response = cartService.getCompleteCartDetails(cartId, userId);
        cartService.deleteCart(cartId);

        return ResponseEntity.ok(response);
    }
}
