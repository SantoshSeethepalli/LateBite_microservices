package com.backend.cart_service.controller;

import com.backend.cart_service.utils.dto.AddItemToCartItemRequest;
import com.backend.cart_service.service.CartItemsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart-item")
@AllArgsConstructor
public class CartItemController {

    private final CartItemsService cartItemsService;

    @PostMapping("/{cartId}/add-item")
    public void addItemToCart(@RequestBody AddItemToCartItemRequest addItemToCartItemRequest) {

            cartItemsService.addItemToCart(addItemToCartItemRequest);
    }

    @PutMapping("/{cartItemId}/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateItemQuantity(@PathVariable Long cartItemId, @RequestParam Boolean increaseQuantity) {

            cartItemsService.updateItemQuantity(cartItemId, increaseQuantity);
    }
}
