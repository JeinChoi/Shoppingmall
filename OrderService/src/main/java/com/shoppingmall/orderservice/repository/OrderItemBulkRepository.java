package com.shoppingmall.orderservice.repository;

import com.shoppingmall.orderservice.domain.Order;
import com.shoppingmall.orderservice.domain.OrderItem;
import com.shoppingmall.orderservice.dto.OrderDto;
import com.shoppingmall.orderservice.dto.OrderItemBulkDto;
import com.shoppingmall.orderservice.dto.OrderItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderItemBulkRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<OrderDto> orderDtos, List<OrderItemBulkDto> orderItemBulkDtos){
//        String sql = "INSERT INTO ORDER_ITEM (order_item_id,price,count,item_id) " +
//                "VALUES (?,?,?,?)";
//
//        jdbcTemplate.batchUpdate(sql,
//                new BatchPreparedStatementSetter() {
//                    @Override
//                    public void setValues(PreparedStatement ps, int i) throws SQLException {
//                        OrderItemBulkDto orderItemBulkDto = orderItemBulkDtos.get(i);
//                        ps.setLong(1, orderItemBulkDto.getUserId());
//                        ps.setInt(2, orderItemBulkDto.getPrice());
//                        ps.setInt(3, orderItemBulkDto.getCount());
//                        ps.setLong(4, orderItemBulkDto.getItemId());
//                    }
//
//                    @Override
//                    public int getBatchSize() {
//                        return orderItemBulkDtos.size();
//                    }
//                });
//
//        long lastInsertId = jdbcTemplate.queryForObject("SELECT last_insert_id()",Long.class);
//        int orderItemsSize= orderItemBulkDtos .size();
//        for(int i=0;i<=orderItemsSize;i++){
//            orderDtos.get(i).setOrderItemId(lastInsertId+i);
//        }
//
//
//        jdbcTemplate.batchUpdate(sql,
//                new BatchPreparedStatementSetter() {
//                    @Override
//                    public void setValues(PreparedStatement ps, int i) throws SQLException {
//                        OrderDto orderDto= orderDtos.get(i);
//                        ps.setLong(1, orderDto.getUserId());
//                        ps.setLong(2, orderDto.getOrderItemId());
//                        ps.setInt(3, 0);
//                        ps.setInt(4, 0);
//                        ps.setString(5, orderDto.getCity());
//                        ps.setString(6, orderDto.getStreet());
//                        ps.setString(7, orderDto.getZipcode());
//                    }
//
//                    @Override
//                    public int getBatchSize() {
//                        return orderDtos.size();
//                    }
//                });
    }

}
