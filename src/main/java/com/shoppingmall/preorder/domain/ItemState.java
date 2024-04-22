package com.shoppingmall.preorder.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "item_state")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemState {

    @Id
    @Column(name = "item_state_name", length = 50)
    private String itemStateName;
}
