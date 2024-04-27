package com.shoppingmall.preorder.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    long userId; //주문한 사용자 정보
    long itemId;
    int count;
}
