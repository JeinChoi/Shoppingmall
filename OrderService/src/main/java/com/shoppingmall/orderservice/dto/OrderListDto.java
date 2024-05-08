package com.shoppingmall.orderservice.dto;

import com.shoppingmall.orderservice.domain.DeliveryStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderListDto {
    private Long orderId;
    private long orderItemId;
    private long itemId;
    private DeliveryStatus deliveryStatus;//구매 or 배달완료

    private String city;
    private String street;
    private String zipcode;

    private Timestamp orderDate;//주문일자

    private int count;
    private int price;
    private int totalPrice;

    private String itemName;

}
