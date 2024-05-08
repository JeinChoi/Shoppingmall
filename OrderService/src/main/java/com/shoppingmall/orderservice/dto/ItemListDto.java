package com.shoppingmall.orderservice.dto;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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

    private int stockQuantity;


}
