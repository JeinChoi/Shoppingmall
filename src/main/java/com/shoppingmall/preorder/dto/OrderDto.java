package com.shoppingmall.preorder.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    long userId; //주문한 사용자 정보
    long itemId;

}
