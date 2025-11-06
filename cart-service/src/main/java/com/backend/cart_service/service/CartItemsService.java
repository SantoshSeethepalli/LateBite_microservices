package com.backend.cart_service.service;

import com.backend.cart_service.utils.dto.AddItemToCartItemRequest;
import com.backend.cart_service.utils.dto.RequiredItemDetails;
import com.backend.cart_service.model.Cart;
import com.backend.cart_service.model.CartItem;
import com.backend.cart_service.respository.CartItemRepository;
import com.backend.cart_service.respository.CartRepository;
import com.backend.cart_service.utils.exceptions.exps.CartNotFoundException;
import com.backend.cart_service.utils.exceptions.exps.CartItemNotFoundException;
import com.backend.cart_service.utils.exceptions.exps.RestaurantNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartItemsService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final WebClient.Builder webClient;

    public void addItemToCart(AddItemToCartItemRequest addItemToCartItemRequest) {

         Cart cart = cartRepository.findById(addItemToCartItemRequest.getCartId())
                 .orElseThrow(() -> new CartNotFoundException("Cart with CartId " + addItemToCartItemRequest.getCartId() + " not found"));


        RequiredItemDetails requiredItemDetails = webClient.build()
                .get()
                .uri("http://localhost:8020/api/restaurant/getItemDetails?itemId=" + addItemToCartItemRequest.getCartId())
                .retrieve()
                .bodyToMono(RequiredItemDetails.class)
                .block();

        if (requiredItemDetails == null) {

            throw new CartItemNotFoundException("Item details not found");
        }

        if(!Objects.equals(cart.getRestaurantId(), requiredItemDetails.getRestaurantId())) {

            throw new RestaurantNotFoundException("Restaurant with id: " + requiredItemDetails.getRestaurantId() +" not found");
        }

        cart.setTotalAmount(
                cart.getTotalAmount().add(
                        requiredItemDetails.getUnitPrice()
                ));
        cart.setUpdatedAt(LocalDateTime.now());


        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .itemId(addItemToCartItemRequest.getItemId())
                .itemName(requiredItemDetails.getItemName())
                .quantity(1)
                .unitPrice(requiredItemDetails.getUnitPrice())
                .totalPrice(requiredItemDetails.getUnitPrice())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    public void updateItemQuantity(Long cartItemId, Boolean increaseQuantity) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Not found CartItem with id: " + cartItemId ));

        int quantityChange = increaseQuantity ? 1 : -1;
        int newQuantity = cartItem.getQuantity() + quantityChange;

        // When quantity drops to zero, remove the item
        if (newQuantity <= 0) {

            Cart cart = cartItem.getCart();
            BigDecimal amountToBeRemoved = cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            cart.setTotalAmount(cart.getTotalAmount().subtract(amountToBeRemoved));
            cart.setUpdatedAt(LocalDateTime.now());

            cartItemRepository.delete(cartItem);
            return;
        }

        // Update quantity and total price for the item
        cartItem.setQuantity(newQuantity);
        cartItem.setTotalPrice(
                cartItem.getUnitPrice().multiply(BigDecimal.valueOf(newQuantity))
        );
        cartItem.setUpdatedAt(LocalDateTime.now());

        // Adjust the cart’s total amount by only the change
        Cart cart = cartItem.getCart();
        BigDecimal amountToBeAdded = cartItem.getUnitPrice().multiply(BigDecimal.valueOf(quantityChange));

        cart.setTotalAmount(cart.getTotalAmount().add(amountToBeAdded));
        cart.setUpdatedAt(LocalDateTime.now());

        cartItemRepository.save(cartItem);
    }

    public void clearCartItems(Cart cart) {

        cartItemRepository.deleteByCart(cart);
    }

    public List<CartItem> getItemsByCart(Cart cart) {

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        if(cartItems.isEmpty()) {

            throw new CartItemNotFoundException("No items found for the given cart");
        }

        return cartItems;
    }

    public void deleteByCartId(Cart cart) {

        cartItemRepository.deleteByCart(cart);
    }
}