package com.shoppingmall.orderservice.dto.feignClientDto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
