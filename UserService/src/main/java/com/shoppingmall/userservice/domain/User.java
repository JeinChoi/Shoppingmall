package com.shoppingmall.userservice.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shoppingmall.userservice.dto.ChangeAddressNPhoneDto;
import jakarta.persistence.*;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity // DB의 테이블과 1:1 매핑되는 객체
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private static final Logger logger = LoggerFactory.getLogger(User.class);

    @JsonIgnore
    @Id // primary key
    @Column(name = "user_id")
    // 자동 증가 되는
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username", length = 50, unique = true)
    private String username;

    @JsonIgnore
    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "phoneNumber", length = 50)
    private String phoneNumber;

    @JsonIgnore
    @Column(name = "activated")
    private boolean activated;

    @JsonIgnore
    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Column(name = "city")
    private String city;

    @JsonIgnore
    @Column(name = "street")
    private String street;

    @JsonIgnore
    @Column(name = "zipcode")
    private String zipcode;

    @JsonIgnore
    @Column(name = "emailAuthenticationToken")
    private String email_authentication_token;

    @ManyToOne
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Authority authority;

//    @JsonIgnore
//    @OneToMany(mappedBy = "order_user" ,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    private List<Order> orderList = new ArrayList<>();
//
//
//    @OneToMany(mappedBy="user",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    private List<WishItem> wishItemList = new ArrayList<>();


    public void updateAuthorityToUser(){

        this.authority= new Authority("ROLE_USER");
        logger.info("수정 후에 authority {}",this.authority.getAuthorityName());
    }

    public void updateAddressNPhone(ChangeAddressNPhoneDto changeAddressNPhoneDto){
        this.city=changeAddressNPhoneDto.getCity();
        this.street=changeAddressNPhoneDto.getStreet();
        this.zipcode=changeAddressNPhoneDto.getZipcode();

        this.phoneNumber= changeAddressNPhoneDto.getPhoneNumber();
    }

    public void updatePassword(String password){
        this.password=password;
    }
}
