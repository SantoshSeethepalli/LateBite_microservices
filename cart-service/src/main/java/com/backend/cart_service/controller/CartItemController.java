package com.backend.cart_service.controller;

import com.backend.cart_service.model.Cart;
import com.backend.cart_service.utils.dto.ItemToCartItemRequest;
import com.backend.cart_service.service.CartItemsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cartItem")
@AllArgsConstructor
public class CartItemController {

    private final CartItemsService cartItemsService;

    @PutMapping("/addOrUpdate")
    public ResponseEntity<Cart> modifyCartItem(
            @RequestHeader("X-Ref-Id") Long userId,
            @RequestHeader("X-Role") String role,
            @RequestBody ItemToCartItemRequest request,
            @RequestParam(defaultValue = "true") Boolean increaseQuantity) {

        if (!role.equals("USER")) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        if (request.getUserId() == null || !request.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Cart response = cartItemsService.updateItemInCart(request, increaseQuantity);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/{cartId}/add-item")
//    @ResponseStatus(HttpStatus.OK)
//    public void addItemToCart(@RequestBody AddItemToCartItemRequest addItemToCartItemRequest) {
//
//            cartItemsService.addItemToCart(addItemToCartItemRequest);
//    }
//
//    @PutMapping("/{cartItemId}/update")
//    @ResponseStatus(HttpStatus.OK)
//    public void updateItemQuantity(@PathVariable Long cartItemId, @RequestParam Boolean increaseQuantity) {
//
//            cartItemsService.updateItemQuantity(cartItemId, increaseQuantity);
//    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<String> deleteCartItem(
            @PathVariable Long cartItemId,
            @RequestHeader("X-Ref-Id") Long userId,
            @RequestHeader("X-Role") String role) {

        if (!role.equals("USER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        cartItemsService.deleteCartItem(cartItemId, userId);
        return ResponseEntity.ok("Item removed from cart");
    }

    @DeleteMapping("/clear/{cartId}")
    public ResponseEntity<String> clearCart(
            @PathVariable Long cartId,
            @RequestHeader("X-Ref-Id") Long userId,
            @RequestHeader("X-Role") String role) {

        if (!role.equals("USER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        cartItemsService.clearCart(cartId, userId);
        return ResponseEntity.ok("Cart cleared");
    }
}
