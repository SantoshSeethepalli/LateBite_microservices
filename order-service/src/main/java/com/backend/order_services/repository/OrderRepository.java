package com.backend.order_services.repository;

import com.backend.order_services.model.Enums.OrderStatus;
import com.backend.order_services.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId);

    List<Order> findByRestaurantIdAndOrderStatusOrderByCreatedAtDesc(
            Long restaurantId, OrderStatus orderStatus
    );

    List<Order> findByRestaurantIdAndOrderStatusOrderByCreatedAtAsc(
            Long restaurantId, OrderStatus orderStatus
    );

    List<Order> findByUserIdAndOrderStatusOrderByCreatedAtDesc(
            Long userId, OrderStatus orderStatus
    );

    List<Order> findByUserIdAndOrderStatusIn(
            Long userId, List<OrderStatus> statuses
    );

    Long countByOrderStatusIn(List<OrderStatus> statuses);

    Long countByOrderStatus(OrderStatus status);

    Long countByRestaurantId(Long restaurantId);

    Long countByRestaurantIdAndOrderStatus(Long restaurantId, OrderStatus status);

    @Query("""
        SELECT COALESCE(SUM(o.totalAmount), 0)
        FROM Order o
        WHERE o.restaurantId = :restaurantId
          AND o.orderStatus = :status
    """)
    BigDecimal sumRevenueByRestaurantAndStatus(
            @Param("restaurantId") Long restaurantId,
            @Param("status") OrderStatus status
    );

    @Query("""
        SELECT COALESCE(SUM(o.totalAmount), 0)
        FROM Order o
        WHERE o.restaurantId = :restaurantId
          AND o.orderStatus = :status
          AND o.createdAt >= :fromDate
    """)
    BigDecimal sumRevenueByRestaurantAndStatusFromDate(
            @Param("restaurantId") Long restaurantId,
            @Param("status") OrderStatus status,
            @Param("fromDate") LocalDateTime fromDate
    );
}

