package com.backend.order_services.utils.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatistics {
    private long totalOrders;
    private long activeOrders;
    private long completedOrders;
    private long cancelledOrders;
}
