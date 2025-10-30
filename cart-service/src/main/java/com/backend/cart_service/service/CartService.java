package com.backend.cart_service.service;

import com.backend.cart_service.model.Cart;
import com.backend.cart_service.model.CartItem;
import com.backend.cart_service.respository.CartRepository;
import jakarta.transaction.Transactional;
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
    public Cart recalculateTotal(Cart cart) {
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
}

