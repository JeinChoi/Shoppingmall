package com.shoppingmall.productservice.repository;

import com.shoppingmall.productservice.domain.WishItem;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WishItemRepository extends JpaRepository<WishItem,Long> {
    @Query("select w from WishItem w where w.userId=:userId")
    List<WishItem> findAllByUserId(@Param("userId") long userId);

    @Modifying
    @Query("delete from WishItem w where w.userId=:userId")
    void deleteAllByUserId(@Param("userId") long userId);
}
