package com.shoppingmall.orderservice.repository;

import com.shoppingmall.orderservice.domain.Order;
import com.shoppingmall.orderservice.domain.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("select o from Order o where o.userId= :userId")
    List<Order> findAllByUserId(@Param("userId") long userId);

    //15분으로 숫자 바꾸기
    @Query("select o from Order o where TIMESTAMPDIFF(minute,o.orderDate,current_timestamp ) > 3 and o.orderStatus = 0")//timeout이 됐다면
    List<Order> findAllTimeout();
}