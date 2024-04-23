package com.shoppingmall.preorder.dto;

import com.shoppingmall.preorder.domain.ItemState;

import lombok.*;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDetailDto {


    private String itemName;

    private long price;

    private int stockQuantity;

    private String itemStateName;

    private String detail;

}
