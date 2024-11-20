package com.shoppingmall.userservice.repository;

import com.shoppingmall.userservice.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);
    @Query("SELECT m FROM Member m where m.email=:email")
    Optional<Member> findByEmail(@Param("email") String email);

    Optional<Member> findByMemberId(Long userId);
//    public long updateAddressNPhone(ChangeAddressNPhoneDto changeAddressNPhoneDto, long userId){
//        return em.createQuery("update User as u set u.city = :city, u.street = :street, u.zipcode = :zipcode ," +
//                        "u.phoneNumber = :phoneNumber where u.userId= :userId")
//                .setParameter("city",changeAddressNPhoneDto.getCity())
//                .setParameter("street", changeAddressNPhoneDto.getStreet())
//                .setParameter("zipcode", changeAddressNPhoneDto.getZipcode())
//                .setParameter("phoneNumber",changeAddressNPhoneDto.getPhoneNumber())
//                .setParameter("userId",userId)
//                .executeUpdate();
//    }
//    public long updatePassword(ChangePasswordDto changePasswordDto, long userId){
//        return em.createQuery("update User as u set u.password = :password where u.userId= :userId")
//                .setParameter("password",changePasswordDto.getPassword())
//                .setParameter("userId",userId)
//                .executeUpdate();
//    }
}
