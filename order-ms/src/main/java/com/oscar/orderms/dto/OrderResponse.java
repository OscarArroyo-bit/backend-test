package com.oscar.orderms.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;

    private String productName;

    private Integer quantity;

    private BigDecimal price;

    private String status;
}