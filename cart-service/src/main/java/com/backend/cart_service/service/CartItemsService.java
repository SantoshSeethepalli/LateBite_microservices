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


        CartItem cartItem = CartItem.builder()
                .itemId(addItemToCartItemRequest.getItemId())
                .itemName(requiredItemDetails.getItemName())
                .quantity(1)
                .unitPrice(requiredItemDetails.getUnitPrice())
                .totalPrice(requiredItemDetails.getUnitPrice())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        cartItemRepository.save(cartItem);
    }

    public void updateItemQuantity(Long cartItemId, Boolean increaseQuantity) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Not found CartItem with id: " + cartItemId ));

        Integer valueToBeAdded = (increaseQuantity) ? 1 : -1;

        cartItem.setQuantity(cartItem.getQuantity() + valueToBeAdded );

        BigDecimal unitPrice = cartItem.getUnitPrice();
        Integer quantity = cartItem.getQuantity();
        BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(quantity));

        cartItem.setTotalPrice(totalAmount);
        cartItem.setUpdatedAt(LocalDateTime.now());

        if(cartItem.getQuantity() <= 0) {

            cartItemRepository.deleteById(cartItemId);
        } else {

            cartItemRepository.save(cartItem);
        }
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
}