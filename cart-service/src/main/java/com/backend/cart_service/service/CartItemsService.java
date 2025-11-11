package com.backend.cart_service.service;

import com.backend.cart_service.utils.Mappers.CartItemMapper;
import com.backend.cart_service.utils.dto.ItemToCartItemRequest;
import com.backend.cart_service.utils.dto.RequiredItemDetails;
import com.backend.cart_service.model.Cart;
import com.backend.cart_service.model.CartItem;
import com.backend.cart_service.respository.CartItemRepository;
import com.backend.cart_service.respository.CartRepository;
import com.backend.cart_service.utils.exceptions.exps.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartItemsService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    private final WebClient.Builder webClient;
    private final CartService cartService;

    @Transactional
    public Cart updateItemInCart(ItemToCartItemRequest itemToCartItemRequest, Boolean increaseQuantity) {

        Cart cart = cartRepository.findById(itemToCartItemRequest.getCartId())
                .orElseGet(() -> cartService.createCart(
                            itemToCartItemRequest.getUserId(), itemToCartItemRequest.getRestaurantId()
                        )
                );

        if (!cart.getRestaurantId().equals(itemToCartItemRequest.getRestaurantId()))
            throw new RestaurantMisMatchException("This cart with id: " + cart.getId() + " belongs to different restaurant");

        if (!cart.getUserId().equals(itemToCartItemRequest.getUserId()))
            throw new UserAccessDenied("Users with id: " + itemToCartItemRequest.getUserId() + " can't access the cart with id: " + cart.getId());

        CartItem existingCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), itemToCartItemRequest.getItemId());

        if (existingCartItem == null) {

            RequiredItemDetails requiredItemDetails = fetchItemDetails(itemToCartItemRequest.getItemId());

            if (!Objects.equals(cart.getRestaurantId(), requiredItemDetails.getRestaurantId())) {

                throw new RestaurantMisMatchException("Item doesn't belong to this restaurant");
            }

            CartItem cartItem = CartItemMapper.fromCartAndOtherDetails(cart, itemToCartItemRequest, requiredItemDetails);

            cartItemRepository.save(cartItem);
            adjustCartTotal(cart, requiredItemDetails.getUnitPrice());
        } else {

            int quantityChange = increaseQuantity ? 1 : -1;
            int newQuantity = existingCartItem.getQuantity() + quantityChange;

            if (newQuantity <= 0) {

                removeItemFromCart(existingCartItem);
            } else {

                updateItemQuantityAndCartTotal(existingCartItem, cart, quantityChange);
            }
        }
        return cart;
    }

    private RequiredItemDetails fetchItemDetails(Long itemId) {

        RequiredItemDetails requiredItemDetails = webClient.build()
                .get()
                .uri("http://localhost:8020/api/restaurant/getItemDetails?itemId=" + itemId)
                .retrieve()
                .bodyToMono(RequiredItemDetails.class)
                .block();

        if (requiredItemDetails == null) throw new CartItemNotFoundException("Item details not found");

        return requiredItemDetails;
    }

    private void adjustCartTotal(Cart cart, BigDecimal change) {

        cart.setTotalAmount(cart.getTotalAmount().add(change));
        cart.setUpdatedAt(LocalDateTime.now());

        cartRepository.save(cart);
    }

    private void updateItemQuantityAndCartTotal(CartItem cartItem, Cart cart, Integer quantityChange) {

        cartItem.setQuantity(cartItem.getQuantity() + quantityChange);
        cartItem.setTotalPrice(cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));

        cartItem.setUpdatedAt(LocalDateTime.now());
        cartItemRepository.save(cartItem);

        adjustCartTotal(cart, cartItem.getUnitPrice().multiply(BigDecimal.valueOf(quantityChange)));
    }

    private void removeItemFromCart(CartItem cartItem) {

        Cart cart = cartItem.getCart();

        BigDecimal totalToSubtract = cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        adjustCartTotal(cart, totalToSubtract.negate());

        cartItemRepository.delete(cartItem);
    }

//    public void updateItemQuantity(Long cartItemId, Boolean increaseQuantity) {
//
//        CartItem cartItem = cartItemRepository.findById(cartItemId)
//                .orElseThrow(() -> new CartItemNotFoundException("Not found CartItem with id: " + cartItemId ));
//
//        int quantityChange = increaseQuantity ? 1 : -1;
//        int newQuantity = cartItem.getQuantity() + quantityChange;
//
//        // When quantity drops to zero, remove the item
//        if (newQuantity <= 0) {
//
//            Cart cart = cartItem.getCart();
//            BigDecimal amountToBeRemoved = cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
//
//            cart.setTotalAmount(cart.getTotalAmount().subtract(amountToBeRemoved));
//            cart.setUpdatedAt(LocalDateTime.now());
//
//            cartItemRepository.delete(cartItem);
//            return;
//        }
//
//        // Update quantity and total price for the item
//        cartItem.setQuantity(newQuantity);
//        cartItem.setTotalPrice(
//                cartItem.getUnitPrice().multiply(BigDecimal.valueOf(newQuantity))
//        );
//        cartItem.setUpdatedAt(LocalDateTime.now());
//
//        // Adjust the cart’s total amount by only the change
//        Cart cart = cartItem.getCart();
//        BigDecimal amountToBeAdded = cartItem.getUnitPrice().multiply(BigDecimal.valueOf(quantityChange));
//
//        cart.setTotalAmount(cart.getTotalAmount().add(amountToBeAdded));
//        cart.setUpdatedAt(LocalDateTime.now());
//
//        cartItemRepository.save(cartItem);
//    }

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