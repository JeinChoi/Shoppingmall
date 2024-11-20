package com.shoppingmall.userservice.service;

import com.shoppingmall.userservice.domain.RefreshToken;
import com.shoppingmall.userservice.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken save(RefreshToken refreshToken){
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

}
