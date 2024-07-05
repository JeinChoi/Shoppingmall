package com.shoppingmall.userservice.dto;

import lombok.Data;

public record JwtDto(
        String accessToken,
        String refreshToken
) {
}