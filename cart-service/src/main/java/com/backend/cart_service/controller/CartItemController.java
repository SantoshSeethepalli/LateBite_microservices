package com.backend.cart_service.controller;

import com.backend.cart_service.dto.AddItemToCartItemRequest;
import com.backend.cart_service.model.CartItem;
import com.backend.cart_service.service.CartItemsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-item")
@AllArgsConstructor
public class CartItemController {

    private final CartItemsService cartItemsService;

    @PostMapping("/{cartId}/add-item")
    @ResponseStatus(HttpStatus.OK)
    public void addItemToCart(@RequestBody AddItemToCartItemRequest addItemToCartItemRequest) {

            cartItemsService.addItemToCart(addItemToCartItemRequest);
    }

    @PutMapping("/{cartItemId}/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateItemQuantity(@PathVariable Long cartItemId, @RequestParam Boolean increaseQuantity) {

            cartItemsService.updateItemQuantity(cartItemId, increaseQuantity);
    }
}
