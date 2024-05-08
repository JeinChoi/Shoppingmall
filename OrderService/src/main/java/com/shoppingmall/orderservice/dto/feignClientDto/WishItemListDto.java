package com.shoppingmall.orderservice.dto.feignClientDto;

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
