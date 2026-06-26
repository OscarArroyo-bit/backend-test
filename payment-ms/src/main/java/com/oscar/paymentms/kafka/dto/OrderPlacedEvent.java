package com.oscar.paymentms.kafka.dto;

import java.math.BigDecimal;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPlacedEvent {
    private Long orderId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private String cardNumber;
}
