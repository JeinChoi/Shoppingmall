package com.shoppingmall.orderservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_item_id")
    private long orderItemId;

    private int count;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp modifiedAt;

    private long itemId;

    @OneToOne(mappedBy = "orderItem")
    @JsonIgnore
    private Order order;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="item_id")
//    private Item orderItem_item;


    public static OrderItem createOrderItem(long itemId, int count) {
        OrderItem orderItem = new OrderItem();

        orderItem.setItemId(itemId);
        orderItem.setCount(count);

        //item.removeStock(count); //재고수 지우는 작업을 따로 해야한다.
        return orderItem;
    }


}
