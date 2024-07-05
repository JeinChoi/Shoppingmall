package com.shoppingmall.userservice.jwt;

import com.shoppingmall.userservice.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "roles";
    private static final String BEARER_TYPE = "Bearer";

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
    public TokenDto generateTokenDto(Authentication authentication) {
        // 권한들 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_EXPIRED_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())       // payload "sub": "name"
                .claim(AUTHORITIES_KEY, authorities)        // payload "auth": "ROLE_USER"
                .setExpiration(accessTokenExpiresIn)        // payload "exp": 1516239022 (예시)
                .signWith(key, SignatureAlgorithm.HS512)    // header "alg": "HS512"
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_EXPIRED_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public TokenDto createJwtAccessToken(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();

        //Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_EXPIRED_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("roles", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();


        ///Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_EXPIRED_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
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
            return e.getClaims();
        }
    }

    public String getRefreshTokenId(String token) {
        return getClaimsFromJwtToken(token).get("value").toString();
    }

    public String getRoles(String token) {
        return getClaimsFromJwtToken(token).get("roles").toString();
    }

    public boolean validateToken(String token) {
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
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}