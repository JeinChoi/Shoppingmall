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
    public Order(User order_user,String deliveryStatus,String city,String street,String zipcode,OrderItem orderItem){
        this.order_user = order_user;
        this.deliveryStatus=deliveryStatus;
        this.city=city;
        this.street=street;
        this.zipcode=zipcode;
        orderItems.add(orderItem);
    }
}
