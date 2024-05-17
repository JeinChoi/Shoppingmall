package com.shoppingmall.orderservice.repository;

import com.shoppingmall.orderservice.domain.Order;
import com.shoppingmall.orderservice.domain.OrderStatus;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order,Long> {
   // @Lock(LockModeType.PESSIMISTIC_WRITE)
    //@QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "15000")})
    Order save(Order order);

    @Query("select o from Order o where o.userId= :userId")
    List<Order> findAllByUserId(@Param("userId") long userId);

    //15분으로 숫자 바꾸기
    @Query("select o from Order o where TIMESTAMPDIFF(minute,o.orderDate,current_timestamp ) > 3 and o.orderStatus = 0")//timeout이 됐다면
    List<Order> findAllTimeout();

    @Query("select o from Order o where o.orderStatus = :orderStatus and TIMESTAMPDIFF(day,o.modifiedAt,current_timestamp) < 1 ")
   List<Order> findAllRefund(@Param("orderStatus") OrderStatus orderStatus);
}
