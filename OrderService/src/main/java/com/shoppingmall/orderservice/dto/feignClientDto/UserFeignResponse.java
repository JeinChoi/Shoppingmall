package com.shoppingmall.orderservice.dto.feignClientDto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFeignResponse{

    private Long userId;

    private String username;

    private String phoneNumber;

    private String email;

    private String city;

    private String street;
    private String zipcode;
}
