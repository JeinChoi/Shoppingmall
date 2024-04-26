package com.shoppingmall.preorder.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
@Entity
@Table(name = "order_item")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Id
    @Column(name="order_item_id")
    private long orderItemId;

    private int orderPrice; //count에 따라 다를 것

    private int count;

    private String itemStatus;

    private Timestamp createdAt;
    private Timestamp modifiedAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="order_id")
    private Order orderItems_order;


    @ManyToOne
    @JoinColumn(name="item_id")
    private Item orderItem_item;


}
