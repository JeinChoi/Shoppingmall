package com.shoppingmall.orderservice.dto;
import com.shoppingmall.orderservice.domain.DeliveryStatus;
import com.shoppingmall.orderservice.domain.OrderStatus;
import lombok.*;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    long userId; //주문한 사용자 정보

    long orderItemId;
    DeliveryStatus deliveryStatus;
    OrderStatus orderStatus;

    String city;
    String street;
    String zipcode;


}
