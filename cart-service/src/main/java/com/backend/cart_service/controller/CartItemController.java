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
    public ResponseEntity<Cart> modifyCartItem(@RequestBody ItemToCartItemRequest request, @RequestParam(defaultValue = "true") Boolean increaseQuantity) {

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
}
