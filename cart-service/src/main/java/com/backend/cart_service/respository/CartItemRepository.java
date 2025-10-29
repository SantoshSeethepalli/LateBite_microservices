package com.backend.cart_service.respository;

import com.backend.cart_service.model.Cart;
import com.backend.cart_service.model.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems, Long> {

    List<CartItems> findByCart(Cart cart);
    void deleteByCart(Cart cart);

}
