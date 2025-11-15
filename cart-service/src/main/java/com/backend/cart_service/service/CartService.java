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

    @Transactional
    public Cart createCart(Long userId, Long restaurantId) {

        Cart newCart = Cart.builder()
                .userId(userId)
                .restaurantId(restaurantId)
                .totalAmount(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return cartRepository.save(newCart);
    }

    public Cart getCartById(Long cartId) {

        return cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with ID: " + cartId));
    }

    public Cart getCartByUserId(Long userId) {

        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("No cart found for user ID: " + userId));
    }

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
    public Cart reCalculateTotal(Cart cart) {

        List<CartItem> cartItems = cartItemsService.getItemsByCart(cart);

        if(cartItems.isEmpty()) {

            throw new CartItemNotFoundException("Cart no found with id: " + cart.getId());
        }

        BigDecimal total = cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(total);
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Cart cart) {

        cartItemsService.deleteByCartId(cart);

        cart.setTotalAmount(BigDecimal.ZERO);
        cart.setUpdatedAt(LocalDateTime.now());

        cartRepository.save(cart);
    }

    @Transactional
    public void deleteCart(Long cartId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart with id: " + cartId + " not found."));

        cartItemsService.deleteByCartId(cart);
        cartRepository.deleteById(cartId);
    }
}

