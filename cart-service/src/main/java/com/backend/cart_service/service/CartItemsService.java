package com.backend.cart_service.service;


import com.backend.cart_service.dto.AddItemToCartItemRequest;
import com.backend.cart_service.model.Cart;
import com.backend.cart_service.model.CartItems;
import com.backend.cart_service.respository.CartItemRepository;
import com.backend.cart_service.respository.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemsService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;


    public void addItemToCart(AddItemToCartItemRequest addItemToCartItemRequest) {

         Cart cart = cartRepository.findById(addItemToCartItemRequest.getCartId())
                 .orElseThrow(() -> new EntityNotFoundException("Cart with CartId " + addItemToCartItemRequest.getCartId() + " not found"));



    }

}