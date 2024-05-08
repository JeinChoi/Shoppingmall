package com.shoppingmall.productservice.dto;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemListDto {

    private String itemName;

    private long price;

    private int stockQuantity;


}
