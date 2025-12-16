package com.backend.order_services.utils.dto.GetAllOrdersDtos;

import com.backend.order_services.model.OrderItem;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {


    private Long itemId;
    private String itemName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    public static List<OrderItemResponse> mapToOrderItemResponse(List<OrderItem> items) {

        if (items == null || items.isEmpty()) return List.of();

        return items.stream()
                .map(item -> OrderItemResponse.builder()
                        .itemId(item.getItemId())
                        .itemName(item.getItemName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalPrice(item.getTotalPrice())
                        .build())
                .toList();
    }
}
