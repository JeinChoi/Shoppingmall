package com.shoppingmall.productservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishItemDto {
    long userId; //주문한 사용자 정보
    long itemId;
    int count;
}
