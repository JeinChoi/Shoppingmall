package com.shoppingmall.orderservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

import static com.shoppingmall.orderservice.domain.OrderStatus.*;

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
@Version
private Long version;
//    @ManyToOne
//    @JoinColumn(name="user_id")
//    private User order_user;
    private Long userId;

   // private long orderItemId;
    //OrderItem
   @OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
   @JoinColumn(name="order_item_id")
   private OrderItem orderItem;

    private DeliveryStatus deliveryStatus;//구매 or 배달완료

    private OrderStatus orderStatus;

    private String city;
    private String street;
    private String zipcode;

    @CreationTimestamp
    private Timestamp orderDate;//주문일자

    @UpdateTimestamp
    private Timestamp modifiedAt;

    public Order(long userId,DeliveryStatus deliveryStatus,OrderStatus orderStatus,
                 OrderItem orderItem,String city,String street,String zipcode){
        this.userId = userId;
        this.deliveryStatus=deliveryStatus;
        this.orderStatus = orderStatus;
        this.orderItem = orderItem;
        this.city=city;
        this.street=street;
        this.zipcode=zipcode;
    }

    public void updateOrderStatus(){
        switch (this.orderStatus){
            case READY:
                this.orderStatus = START;
                break;
            case START:
                this.orderStatus = COMPLETED;
                break;

        }

    }
    public void updateDeliveryStatus(DeliveryStatus deliveryStatus){
        this.deliveryStatus = deliveryStatus;
    }
    //하나하나 setOrder를 안해줘서 안된거였음.... setorder해줘야함 order가져와서 orderitem생성할 때 order설정 필요하다.

    public void updateOrderStatusToRefund(){
        this.orderStatus=REFUND;
    }
    public void updateOrderStatusToRefundCompleted(){this.orderStatus=REFUND_COMPLETED;}
    public void updateOrderStatusToRefundImpossible(){
        this.orderStatus=REFUND_IMPOSSIBLE;
    }
}
