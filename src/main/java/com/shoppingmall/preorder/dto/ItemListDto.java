package com.shoppingmall.preorder.dto;

import com.shoppingmall.preorder.domain.ItemState;
import jakarta.persistence.Entity;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemListDto {

    private String itemName;

    private long price;

    private String stockQuantity;


}
