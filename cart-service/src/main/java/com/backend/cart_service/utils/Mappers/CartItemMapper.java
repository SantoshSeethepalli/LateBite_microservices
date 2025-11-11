package com.backend.cart_service.utils.Mappers;

import com.backend.cart_service.model.Cart;
import com.backend.cart_service.model.CartItem;
import com.backend.cart_service.utils.dto.CartItemDTO;
import com.backend.cart_service.utils.dto.ItemToCartItemRequest;
import com.backend.cart_service.utils.dto.RequiredItemDetails;

import java.time.LocalDateTime;

public class CartItemMapper {

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

    public static CartItem fromCartAndOtherDetails(Cart cart, ItemToCartItemRequest itemToCartItemRequest, RequiredItemDetails requiredItemDetails) {

        return CartItem.builder()
                .cart(cart)
                .itemId(itemToCartItemRequest.getItemId())
                .itemName(requiredItemDetails.getItemName())
                .quantity(1)
                .unitPrice(requiredItemDetails.getUnitPrice())
                .totalPrice(requiredItemDetails.getUnitPrice())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
