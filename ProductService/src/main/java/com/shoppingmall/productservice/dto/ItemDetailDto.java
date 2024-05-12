package com.shoppingmall.productservice.dto;
import com.shoppingmall.productservice.domain.ItemState;
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

    private ItemState itemStateName;

    private String detail;

}
