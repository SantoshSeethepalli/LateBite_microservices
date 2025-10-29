package com.backend.cart_service.respository;

import com.backend.cart_service.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserIdAndRestaurantId(Long userId, Long restaurantId);

    Optional<Cart> findByUserId(Long userId);

}
