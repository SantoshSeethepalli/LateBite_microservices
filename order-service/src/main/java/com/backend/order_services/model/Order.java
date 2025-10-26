package com.backend.order_services.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {

    @Id
    @GeneratedValue(GeneratedValue)
    private Long id;
}
