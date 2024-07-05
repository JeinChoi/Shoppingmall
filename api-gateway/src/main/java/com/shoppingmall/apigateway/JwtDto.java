package com.shoppingmall.apigateway;

public record JwtDto(
        String accessToken,
        String refreshToken
) {
}