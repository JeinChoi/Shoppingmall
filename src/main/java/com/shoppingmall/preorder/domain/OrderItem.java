package com.shoppingmall.preorder.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Id@GeneratedValue
    @Column(name="order_item_id")
    private long orderItemId;

    private int orderPrice; //count에 따라 다를 것

    private int count;

    private String itemStatus;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp modifiedAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order orderItems_order;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item orderItem_item;

    public static OrderItem createOrderItem(Item item,int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
       // orderItem.setOrderItems_order(orderItems_order);
        orderItem.setOrderItem_item(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }
}
