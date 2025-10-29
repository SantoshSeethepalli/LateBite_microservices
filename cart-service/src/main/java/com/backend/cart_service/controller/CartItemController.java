package com.backend.cart_service.controller;

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
    private final CartRepository cartRepository;

    @PostMapping("/{cartId}/add-item")
    @ResponseStatus(Ht)
    public CartItems addItemToCart(@RequestBody ) {


    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<?> updateQuantity(
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {

        CartItems updated = cartItemsService.updateQuantity(cartItemId, quantity);
        if (updated == null) {
            return ResponseEntity.ok("Item removed from cart");
        }
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{cartId}/items")
    public ResponseEntity<List<CartItems>> getItemsByCart(@PathVariable Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with ID: " + cartId));

        List<CartItems> items = cartItemsService.getItemsByCart(cart);
        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<String> clearCartItems(@PathVariable Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with ID: " + cartId));

        cartItemsService.clearCartItems(cart);
        return ResponseEntity.ok("All items cleared from cart");
    }

}
