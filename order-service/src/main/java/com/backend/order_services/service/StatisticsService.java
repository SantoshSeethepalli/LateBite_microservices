package com.backend.order_services.service;

import com.backend.order_services.model.Enums.OrderStatus;
import com.backend.order_services.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final OrderRepository orderRepository;

    public BigDecimal getOverallRevenue(Long restaurantId) {
        return Optional.ofNullable(
                orderRepository.sumRevenueByRestaurantAndStatus(
                        restaurantId,
                        OrderStatus.DELIVERED
                )
        ).orElse(BigDecimal.ZERO);
    }

    public BigDecimal getLastWeekRevenue(Long restaurantId) {

        LocalDateTime from = LocalDateTime.now().minusWeeks(1);
        return getRevenueFromDate(restaurantId, from);
    }

    public BigDecimal getLastMonthRevenue(Long restaurantId) {

        LocalDateTime from = LocalDateTime.now().minusMonths(1);
        return getRevenueFromDate(restaurantId, from);
    }

    public BigDecimal getLastYearRevenue(Long restaurantId) {

        LocalDateTime from = LocalDateTime.now().minusYears(1);
        return getRevenueFromDate(restaurantId, from);
    }

    public Long getTotalOrderOfRestaurant(Long restaurantId) {

        return orderRepository.countByRestaurantId(restaurantId);
    }

    public Long getCountOfDeliveredOrderOfRestaurant(Long restaurantId) {

        return orderRepository.countByRestaurantIdAndOrderStatus(restaurantId, OrderStatus.DELIVERED);
    }

    public Long getCountOfCancelledOrdersOfRestaurant(Long restaurantId) {

        return orderRepository.countByRestaurantIdAndOrderStatus(restaurantId, OrderStatus.CANCELLED);
    }

    private BigDecimal getRevenueFromDate(Long restaurantId, LocalDateTime fromTime) {

        return orderRepository.sumRevenueByRestaurantAndStatusFromDate(restaurantId, OrderStatus.DELIVERED, fromTime);
    }
}
