package com.shoppingmall.orderservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderIdDto {
    long orderId;
    boolean success;
}
