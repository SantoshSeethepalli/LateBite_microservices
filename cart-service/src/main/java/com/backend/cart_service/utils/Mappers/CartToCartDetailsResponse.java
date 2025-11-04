package com.backend.cart_service.utils.Mappers;

import com.backend.cart_service.model.Cart;
import com.backend.cart_service.model.CartItem;
import com.backend.cart_service.utils.dto.CartDetailsResponse;
import com.backend.cart_service.utils.dto.CartItemDTO;

import java.util.List;

public class CartToCartDetailsResponse {

    public static CartDetailsResponse toCartDto(Cart cart, List<CartItemDTO> orderedItems) {

        return CartDetailsResponse.builder()
                .userId(cart.getUserId())
                .restaurantId(cart.getRestaurantId())
                .totalAmount(cart.getTotalAmount())
                .orderedItems(orderedItems)
                .build();
    }
 }
