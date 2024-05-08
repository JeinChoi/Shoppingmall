package com.shoppingmall.productservice.dto;
import lombok.*;

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
