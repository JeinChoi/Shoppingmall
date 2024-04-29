package com.shoppingmall.preorder.repository;

import com.shoppingmall.preorder.domain.Item;
import com.shoppingmall.preorder.domain.Order;
import com.shoppingmall.preorder.domain.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository   {

    @PersistenceContext
    private EntityManager em;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void save(Order order){
        em.persist(order);
    }
    public void saveOrderItem(OrderItem orderItem){
        em.persist(orderItem);
    }
    public List<Order> findAll(){
        return em.createQuery("select i from Item i", Order.class)
                .getResultList();
    }
    public Order findOne(long orderId){ return em.find(Order.class,orderId);}


}
