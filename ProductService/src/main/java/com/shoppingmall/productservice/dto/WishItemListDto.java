package com.shoppingmall.productservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishItemListDto {

    private long itemId;
    private String itemName;
    private int price;
    private int count;
}
