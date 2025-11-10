package com.backend.cart_service.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemToCartItemRequest {

    private Long cartId;
    private Long userId;
    private Long restaurantId;
    private Long itemId;
}
