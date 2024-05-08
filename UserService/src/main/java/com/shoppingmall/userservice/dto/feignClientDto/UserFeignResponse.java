package com.shoppingmall.userservice.dto.feignClientDto;

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
