package com.backend.order_services.repository;

import com.backend.order_services.model.Order;
import com.backend.order_services.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);
    Long countByOrder(Order order);

    List<OrderItem> findByOrderId(Long orderId);
}
