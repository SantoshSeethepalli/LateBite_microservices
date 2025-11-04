package com.backend.cart_service.utils.dto;

import com.backend.cart_service.model.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddItemToCartItemRequest {

    private Long cartId;
    private Long itemId;
}
