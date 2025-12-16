package com.backend.cart_service.service;

import com.backend.cart_service.utils.Mappers.CartItemMapper;
import com.backend.cart_service.utils.Mappers.CartMapper;
import com.backend.cart_service.utils.dto.CartDetailsResponse;
import com.backend.cart_service.model.Cart;
import com.backend.cart_service.model.CartItem;
import com.backend.cart_service.respository.CartRepository;
import com.backend.cart_service.utils.dto.CartItemDTO;
import com.backend.cart_service.utils.exceptions.exps.CartItemNotFoundException;
import com.backend.cart_service.utils.exceptions.exps.CartNotFoundException;
import com.backend.cart_service.utils.exceptions.exps.UserAccessDenied;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemsService cartItemsService;

    @Transactional(readOnly = true)
    public CartDetailsResponse getCompleteCartDetails(Long cartId, Long userId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));

        if(!cart.getUserId().equals(userId)) throw new UserAccessDenied("You cant access this cart");

        List<CartItem> cartItemList = cartItemsService.getItemsByCart(cart);

        List<CartItemDTO> orderedItems = cartItemList.stream()
                .map(CartItemMapper::toCartItemDto)
                .toList();

        return CartMapper.toCartDto(cart, orderedItems);
    }

    @Transactional
    public void deleteCart(Long cartId, Long userId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart with id: " + cartId + " not found."));

        if(!cart.getUserId().equals(userId)) {
            throw new UserAccessDenied("You can't delete this cart");
        }

        cartItemsService.deleteByCartId(cart);
        cartRepository.deleteById(cartId);
    }
}

