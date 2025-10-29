package com.backend.cart_service.service;


import com.backend.cart_service.dto.AddItemToCartItemRequest;
import com.backend.cart_service.model.Cart;
import com.backend.cart_service.model.CartItems;
import com.backend.cart_service.respository.CartItemRepository;
import com.backend.cart_service.respository.CartRepository;
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

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    @Transactional
    public CartItems addOrUpdateItem(Long itemId, Long cartItemId, AddItemToCartItemRequest request) {

        // 1️⃣ Find or create cart for this user + restaurant
        Cart cart = cartRepository
                .findByUserIdAndRestaurantId(request.getUserId(), request.getRestaurantId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .userId(request.getUserId())
                            .restaurantId(request.getRestaurantId())
                            .totalAmount(BigDecimal.ZERO)
                            .build();
                    return cartRepository.save(newCart);
                });

        // 2️⃣ Check if item already exists in the cart
        List<CartItems> existingItems = cartItemRepository.findByCart(cart);
        Optional<CartItems> existingItemOpt = existingItems.stream()
                .filter(ci -> ci.getItemId().equals(itemId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItems existingItem = existingItemOpt.get();

            int newQuantity = existingItem.getQuantity() + (request.isAddItem() ? 1 : -1);

            if (newQuantity <= 0) {
                cartItemRepository.delete(existingItem);
                updateCartTotal(cart);
                return null;
            }

            existingItem.setQuantity(newQuantity);
            existingItem.setTotalPrice(existingItem.getUnitPrice().multiply(BigDecimal.valueOf(newQuantity)));
            CartItems saved = cartItemRepository.save(existingItem);
            updateCartTotal(cart);
            return saved;
        }

        // 3️⃣ If item not exists and addItem = true → create new item
        if (request.isAddItem()) {
            CartItems newItem = CartItems.builder()
                    .cart(cart)
                    .itemId(itemId)
                    .itemName(request.getItemName())
                    .unitPrice(request.getUnitPrice())
                    .quantity(1)
                    .totalPrice(request.getUnitPrice())
                    .build();

            CartItems saved = cartItemRepository.save(newItem);
            updateCartTotal(cart);
            return saved;
        }

        // 4️⃣ If item doesn’t exist and addItem = false → do nothing
        return null;
    }

    private void updateCartTotal(Cart cart) {
        List<CartItems> items = cartItemRepository.findByCart(cart);
        BigDecimal total = items.stream()
                .map(CartItems::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(total);
        cartRepository.save(cart);
    }
}