package com.backend.cart_service.service;

import com.backend.cart_service.utils.Mappers.CartItemToCartItemDTO;
import com.backend.cart_service.utils.Mappers.CartToCartDetailsResponse;
import com.backend.cart_service.utils.dto.CartDetailsResponse;
import com.backend.cart_service.model.Cart;
import com.backend.cart_service.model.CartItem;
import com.backend.cart_service.respository.CartRepository;
import com.backend.cart_service.utils.dto.CartItemDTO;
import com.backend.cart_service.utils.exceptions.exps.CartNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemsService cartItemsService;

    @Transactional
    public Cart createOrGetCart(Long userId, Long restaurantId) {

        return cartRepository.findByUserIdAndRestaurantId(userId, restaurantId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .userId(userId)
                            .restaurantId(restaurantId)
                            .totalAmount(BigDecimal.ZERO)
                            .build();
                    return cartRepository.save(newCart);
                });
    }

    public Cart getCartById(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with ID: " + cartId));
    }

    public Cart getCartByUser(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No cart found for user ID: " + userId));
    }

    @Transactional
    public Cart reCalculateTotal(Cart cart) {
        List<CartItem> items = cartItemsService.getItemsByCart(cart);

        BigDecimal total = items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(total);
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Cart cart) {
        cartItemsService.clearCartItems(cart);
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    @Transactional
    public void deleteCart(Long cartId) {
        Cart cart = getCartById(cartId);
        cartItemsService.clearCartItems(cart);
        cartRepository.delete(cart);
    }

    @Transactional(readOnly = true)
    public CartDetailsResponse getCompleteCartDetails(Long cartId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));

        List<CartItem> cartItemList = cartItemsService.getItemsByCart(cart);

        List<CartItemDTO> orderedItems = cartItemList.stream()
                .map(CartItemToCartItemDTO::toCartItemDto)
                .toList();

        return CartToCartDetailsResponse.toCartDto(cart, orderedItems);
    }
}

