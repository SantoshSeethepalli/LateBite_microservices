package com.backend.cart_service.utils.Mappers;

import com.backend.cart_service.model.Cart;
import com.backend.cart_service.utils.dto.CartDetailsResponse;
import com.backend.cart_service.utils.dto.CartItemDTO;
import com.backend.cart_service.utils.dto.ItemToCartItemRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CartMapper {

    public static CartDetailsResponse toCartDto(Cart cart, List<CartItemDTO> orderedItems) {

        return CartDetailsResponse.builder()
                .userId(cart.getUserId())
                .restaurantId(cart.getRestaurantId())
                .totalAmount(cart.getTotalAmount())
                .orderedItems(orderedItems)
                .build();
    }

    public static Cart createNewCart(ItemToCartItemRequest itemToCartItemRequest) {

        return Cart.builder()
                .userId(itemToCartItemRequest.getUserId())
                .restaurantId(itemToCartItemRequest.getRestaurantId())
                .totalAmount(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
