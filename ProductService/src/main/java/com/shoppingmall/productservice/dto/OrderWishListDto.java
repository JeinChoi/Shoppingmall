package com.shoppingmall.productservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderWishListDto {
    List<OrderItemDto> orderItemDtoList;
    long userId;
}
