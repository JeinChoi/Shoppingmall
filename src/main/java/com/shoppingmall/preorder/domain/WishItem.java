package com.shoppingmall.preorder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    @GeneratedValue
    @Column(name="wish_item_id")
    private long wishItemId;

    private int orderPrice; //count에 따라 다를 것
    private int count;

    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp modifiedAt;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "item_id")
    private Item wishitem_item;

    public static WishItem createWishItem(User user, Item wishitem_item, int orderPrice, int count){
        WishItem wishItem = new WishItem();
        wishItem.setOrderPrice(orderPrice*count);
        wishItem.setWishitem_item(wishitem_item);
        wishItem.setUser(user);
        wishItem.setCount(count);
        return wishItem;
    }
    public void updateWishItem(int count){
        this.count=count;
        this.orderPrice=count*orderPrice;

    }
}
