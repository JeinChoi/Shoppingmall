package com.shoppingmall.preorder.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Set;

@Entity // DB의 테이블과 1:1 매핑되는 객체
@Table(name = "items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private static final Logger logger = LoggerFactory.getLogger(Item.class);

    @JsonIgnore
    @Id // primary key
    @Column(name = "item_id")
    // 자동 증가 되는
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(name="item_name")
    private String itemName;

    @Column(name = "price")
    private long price;

    @JsonIgnore
    @Column(name = "stock_quantity")
    private int stockQuantity;

    @JsonIgnore
    @Column(name = "detail")
    private String detail;

//    @ManyToOne
//    @JoinTable(name = "item_state")
    @Column(name="item_state_name")
    private String itemStateName;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @JsonIgnore
    @Column(name = "modified_at")
    private Timestamp modifiedAt;


    public Item(String itemName, long price, int stockQuantity, String detail, String itemStateName) {
        this.itemName=itemName;
        this.price=price;
        this.stockQuantity=stockQuantity;
        this.detail=detail;
        this.itemStateName=itemStateName;

    }
}
