package com.backend.order_services.repository;

import com.backend.order_services.model.Enums.OrderStatus;
import com.backend.order_services.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId);

    List<Order> findByRestaurantIdAndOrderStatusOrderByCreatedAtDesc(Long restaurantId, OrderStatus orderStatus);
}
