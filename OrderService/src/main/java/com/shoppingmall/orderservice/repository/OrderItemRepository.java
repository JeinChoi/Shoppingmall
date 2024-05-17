package com.shoppingmall.orderservice.repository;

import com.shoppingmall.orderservice.domain.OrderItem;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import static org.bouncycastle.asn1.x500.style.BCStyle.T;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
   // @Lock(LockModeType.PESSIMISTIC_WRITE)
   // @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "15000")})
    OrderItem save(OrderItem orderItem);
}
