package com.shoppingmall.orderservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemBulkDto {
    long orderItemId;
    long userId; //주문한 사용자 정보
    long itemId;
    int price;
    int count;

}
