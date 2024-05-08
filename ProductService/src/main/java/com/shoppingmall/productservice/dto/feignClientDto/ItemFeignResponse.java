package com.shoppingmall.productservice.dto.feignClientDto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemFeignResponse {

    private long itemId;

    private String itemName;

    private int price;

    private int stockQuantity;

    private String detail;

    private String itemStateName;

    private Timestamp createdAt;

    private Timestamp modifiedAt;
}
