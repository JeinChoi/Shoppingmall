package com.shoppingmall.productservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManageProductDto {
    long itemId;
    int count;
    boolean plus;
}
