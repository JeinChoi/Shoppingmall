package com.shoppingmall.productservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    long userId;
    long itemId;
    int count;
    int price;
}
