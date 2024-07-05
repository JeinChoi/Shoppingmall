package com.shoppingmall.apigateway.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.token.access-expiration-time}")
    private long ACCESS_EXPIRED_TIME;

    @Value("${jwt.token.refresh-expiration-time}")
    private long REFRESH_EXPIRED_TIME;

    private final Key key;

    @Value("${jwt.secret}")
    private String SECRET;

    public JwtTokenProvider(@Value("${jwt.secret}") String secreteKey){
        byte[] keyBytes = Decoders.BASE64.decode(secreteKey);

        this.key = Keys.hmacShaKeyFor(keyBytes);

    }
    public String createJwtRefreshToken() {
        Claims claims = (Claims) Jwts.builder().claims();
        claims.put("value", UUID.randomUUID());

        return Jwts.builder()
                .addClaims(claims)
                .setExpiration(
                        new Date(System.currentTimeMillis() + REFRESH_EXPIRED_TIME)
                )
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }



    public String getUserId(String token) {
        return getClaimsFromJwtToken(token).getSubject();
    }

    private Claims getClaimsFromJwtToken(String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET).build().parseSignedClaims(token).getBody();
        } catch (ExpiredJwtException e) {
            log.info("getClaimsFromJwtToken 메서드 예외 발생 ");
            return e.getClaims();
        }
    }

    public String getRefreshTokenId(String token) {
        return getClaimsFromJwtToken(token).get("value").toString();
    }

    public String getRoles(String token) {
        return getClaimsFromJwtToken(token).get("roles").toString();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).build().parseSignedClaims(token);
        } catch (SignatureException  | MalformedJwtException |
                 UnsupportedJwtException | IllegalArgumentException | ExpiredJwtException jwtException) {
            return false;
//            throw jwtException;
        }
        return true;
    }

    public boolean equalRefreshTokenId(String refreshTokenId, String refreshToken) {
        String compareToken = this.getRefreshTokenId(refreshToken);
        return refreshTokenId.equals(compareToken);
    }

}