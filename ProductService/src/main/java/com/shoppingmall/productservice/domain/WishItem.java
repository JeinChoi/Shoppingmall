package com.shoppingmall.productservice.domain;

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

    private int orderPrice; //1개당 가격으로 측정
    private int count;

    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp modifiedAt;

//    @JsonBackReference
//    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinColumn(name="user_id")
//    private User user;
    private long userId;

    @ManyToOne(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    @JoinColumn(name = "item_id")
    private Item item;


    public static WishItem createWishItem(long userId, Item item, int orderPrice, int count){
        WishItem wishItem = new WishItem();
        wishItem.setOrderPrice(orderPrice);
        wishItem.setItem(item);
        wishItem.setUserId(userId);
        wishItem.setCount(count);
        return wishItem;
    }
    public void updateWishItem(int count){
        this.count=count;
        this.orderPrice=count*orderPrice;

    }
}
