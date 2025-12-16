package com.backend.order_services.utils.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantOrderStats {

    private Long totalOrders;
    private Long ordersDelivered;
    private Long ordersCancelled;
    private BigDecimal overallRevenue;
    private BigDecimal lastWeekRevenue;
    private BigDecimal lastMonthRevenue;
    private BigDecimal lastYearRevenue;
}
