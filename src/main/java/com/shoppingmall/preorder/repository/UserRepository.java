package com.shoppingmall.preorder.repository;


import com.shoppingmall.preorder.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    @PersistenceContext // 영속성 컨텍스트
    private EntityManager em;

    public User save(User user) {
        em.persist(user);
        return user;
    }

    public User findOne(Long id) {
        return em.find(User.class, id);
    }
    public Optional<User> findOneWithAuthoritiesByUsername(String username){
           List<User> findUser = em.createQuery("select u from User u where u.username= :username", User.class)
                   .setParameter("username",username)
                   .getResultList();
            return findUser.stream().findAny();
    }

    public List<User> findAll() {
        List<User> members = em.createQuery("select m from User m", User.class).getResultList();
        return members;
    }

//    public List<User> findById(String user_id) {
//        return em.createQuery("select m from User m where m.user_id = :userid", User.class)
//                .setParameter("userid", user_id)
//                .getResultList();
//    } // 중복회원 검증할 때 필요한 메서드


//    public int updatePassword(String email, String memberPw) {
//        return em.createQuery("update Member as p set p.password = :password where p.email= :email")
//                .setParameter("email", email)
//                .setParameter("password", memberPw)
//                .executeUpdate();
//    }

}

