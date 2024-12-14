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
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="order_id")
    private Long orderId;

    private Long userId;

   @OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
   @JoinColumn(name="order_item_id")
   private OrderItem orderItem;

    private DeliveryStatus deliveryStatus;//구매 or 배달완료

    private OrderStatus orderStatus;

    @CreationTimestamp
    private Timestamp orderDate;//주문일자

    @UpdateTimestamp
    private Timestamp modifiedAt;

    public Order(long userId,DeliveryStatus deliveryStatus,OrderStatus orderStatus,
                 OrderItem orderItem){
        this.userId = userId;
        this.deliveryStatus=deliveryStatus;
        this.orderStatus = orderStatus;
        this.orderItem = orderItem;
    }
public void setOrderItem(OrderItem orderItem){
        this.orderItem = orderItem;
        orderItem.setOrder(this);
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
    public static Order createOrder(long userId,DeliveryStatus deliveryStatus,OrderStatus orderStatus,
                                    OrderItem orderItem) {
        Order order = new Order();
        order.setUserId(userId);
        order.setDeliveryStatus(deliveryStatus);
        order.setOrderStatus(orderStatus);
        order.setOrderItem(orderItem);
        return order;
    }
    public void updateDeliveryStatus(DeliveryStatus deliveryStatus){
        this.deliveryStatus = deliveryStatus;
    }
    //하나하나 setOrder를 안해줘서 안된거였음.... setorder해줘야함 order가져와서 orderitem생성할 때 order설정 필요하다.

    public void updateOrderStatusToRefund(){
        this.orderStatus=REFUND;
    }
    public void updateOrderStatusToRefundCompleted(){this.orderStatus=REFUND_COMPLETED;}

}
