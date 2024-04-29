package com.shoppingmall.preorder.repository;

import com.shoppingmall.preorder.domain.User;
import com.shoppingmall.preorder.domain.WishItem;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WishItemRepository extends JpaRepository<WishItem, Long> {

 //   @Query("select w from WishItem w where w.wishItem_user.userId = : wishItem_user.userId")
    WishItem findById(long wishItemId);
    List<WishItem> findAllByUser(User user);
}
