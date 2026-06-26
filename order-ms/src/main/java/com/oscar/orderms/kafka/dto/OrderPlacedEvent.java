package com.oscar.orderms.kafka.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPlacedEvent {

    private Long orderId;
    private String productName;
    private int quantity;
    private BigDecimal price;
    private String cardNumber;
}
