package com.backend.cart_service.utils.Mappers;

import com.backend.cart_service.model.CartItem;
import com.backend.cart_service.utils.dto.CartItemDTO;

public class CartItemToCartItemDTO {

    public static CartItemDTO toCartItemDto(CartItem cartItem) {

        return CartItemDTO.builder()
                .itemId(cartItem.getItemId())
                .restaurantId(cartItem.getCart().getRestaurantId())
                .itemName(cartItem.getItemName())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getUnitPrice())
                .totalPrice(cartItem.getTotalPrice())
                .build();
    }
}
