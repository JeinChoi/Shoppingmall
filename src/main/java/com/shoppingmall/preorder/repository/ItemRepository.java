package com.shoppingmall.preorder.repository;

import com.shoppingmall.preorder.domain.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepository   {

    @PersistenceContext
    private EntityManager em;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
    public Item findOne(long itemId){ return em.find(Item.class,itemId);}
public void save(Item item){
        em.persist(item);
}
    public void saveAll(List<Item> list){
        String sql = "INSERT INTO item(itemName,price,stockQuantity,detail,itemStateName) " +
                "values (:itemName, :price, :stockQuantity, :detail, :itemStateName )";
        MapSqlParameterSource[] params = list.stream()
                .map(i -> new MapSqlParameterSource()
                        .addValue("itmeName",i.getItemName())
                        .addValue("price",i.getPrice())
                        .addValue("stockQauntity",i.getStockQuantity())
                        .addValue("detail",i.getDetail())
                        .addValue("itemStateName",i.getItemStateName()))
                        .toList()
                        .toArray(MapSqlParameterSource[]::new);
        jdbcTemplate.batchUpdate(sql,params);


    }

}
