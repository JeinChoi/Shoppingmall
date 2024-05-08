package com.shoppingmall.productservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

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
    private int price;

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

//    @OneToMany(mappedBy="wishitem_item",cascade = CascadeType.ALL)
//    List<WishItem> wishitemList = new ArrayList<>();

//    @OneToMany(mappedBy = "orderItem_item",cascade = CascadeType.ALL)
//    List<OrderItem> orderItemList = new ArrayList<>();

    public Item(String itemName, int price, int stockQuantity, String detail, String itemStateName) {
        this.itemName=itemName;
        this.price=price;
        this.stockQuantity=stockQuantity;
        this.detail=detail;
        this.itemStateName=itemStateName;

    }

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new RuntimeException();

        }
        this.stockQuantity = restStock;
    }
}
