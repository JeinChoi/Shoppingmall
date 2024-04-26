package com.shoppingmall.preorder.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @Column(name="order_id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User order_user;

    private String deliveryStatus;//구매 or 배달완료

    private String city;
    private String street;
    private String zipcode;

    private Timestamp orderDate;//주문일자
    private Timestamp modifiedAt;

    @OneToMany(mappedBy = "orderItems_order")
    private List<OrderItem> orderItems = new ArrayList<>();

}
