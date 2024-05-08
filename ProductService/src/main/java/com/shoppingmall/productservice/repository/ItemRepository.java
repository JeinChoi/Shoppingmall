package com.shoppingmall.productservice.repository;

import com.shoppingmall.productservice.domain.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {

//    public void saveAll(List<Item> list){
//        String sql = "INSERT INTO item(itemName,price,stockQuantity,detail,itemStateName) " +
//                "values (:itemName, :price, :stockQuantity, :detail, :itemStateName )";
//        MapSqlParameterSource[] params = list.stream()
//                .map(i -> new MapSqlParameterSource()
//                        .addValue("itmeName",i.getItemName())
//                        .addValue("price",i.getPrice())
//                        .addValue("stockQauntity",i.getStockQuantity())
//                        .addValue("detail",i.getDetail())
//                        .addValue("itemStateName",i.getItemStateName()))
//                .toList()
//                .toArray(MapSqlParameterSource[]::new);
//        jdbcTemplate.batchUpdate(sql,params);
//    }


}
