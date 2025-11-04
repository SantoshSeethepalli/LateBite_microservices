package com.backend.order_services.utils.dto.PlaceOrderDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderRequest {

    private Long cartId;
    private String screenShot;
}
