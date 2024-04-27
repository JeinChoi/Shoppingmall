package com.shoppingmall.preorder.repository;

import com.shoppingmall.preorder.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
