package com.shoppingmall.preorder.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    @GeneratedValue
    @Column(name="order_id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User order_user;

    private String deliveryStatus;//구매 or 배달완료

    private String city;
    private String street;
    private String zipcode;

    @CreationTimestamp
    private Timestamp orderDate;//주문일자

    @UpdateTimestamp
    private Timestamp modifiedAt;


    @OneToMany(mappedBy = "orderItems_order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
    public Order(User order_user,String deliveryStatus,String city,String street,String zipcode,List<OrderItem> orderItemList){
        this.order_user = order_user;
        this.deliveryStatus=deliveryStatus;
        this.city=city;
        this.street=street;
        this.zipcode=zipcode;
        for(OrderItem orderItem : orderItemList){
            orderItem.setOrderItems_order(this);
        }
    }

    //하나하나 setOrder를 안해줘서 안된거였음.... setorder해줘야함 order가져와서 orderitem생성할 때 order설정 필요하다.
}
