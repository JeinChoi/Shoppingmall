package com.shoppingmall.productservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeWishItemDto {

    private long wishItemId;

    private int count;
}
