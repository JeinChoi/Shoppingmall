package com.shoppingmall.preorder.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "wish_item")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishItem {
    @Id
    @Column(name="wish_item_id")
    private long wishItemId;

    private int orderPrice; //count에 따라 다를 것

    private int count;

    private String itemStatus;

    private Timestamp createdAt;
    private Timestamp modifiedAt;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User wishItem_user;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item wishitem_item;
}
