package com.backend.cart_service.controller;

import com.backend.cart_service.dto.AddItemToCartItemRequest;
import com.backend.cart_service.model.Cart;
import com.backend.cart_service.model.CartItems;
import com.backend.cart_service.respository.CartRepository;
import com.backend.cart_service.service.CartItemsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartItemController {

    private final CartItemsService cartItemsService;

    @PostMapping("/{cartId}/add-item")
    @ResponseStatus(Ht)
    public void addItemToCart(@RequestBody AddItemToCartItemRequest addItemToCartItemRequest) {

            cartItemsService.addItemToCart(addItemToCartItemRequest);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<?> updateQuantity(
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {


    }

    @GetMapping("/{cartId}/items")
    public ResponseEntity<List<CartItems>> getItemsByCart(@PathVariable Long cartId) {

    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<String> clearCartItems(@PathVariable Long cartId) {

    }

}
