package com.example.oauth2practice2.domain.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class JwtAuthProvider {

    private final UserDetailsService userDetailsService;
    private final JwtIssuer jwtIssuer;

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        Claims claims = jwtIssuer.getClaims(token);
        if (claims == null) {
            return false;
        }

        /*
         * 추가 검증 로직
         */

        return true;
    }

    public boolean validateToken(JwtDto jwtDto) {
        if (!StringUtils.hasText(jwtDto.getAccessToken())
            || !StringUtils.hasText(jwtDto.getRefreshToken())) {
            return false;
        }

        Claims accessClaims = jwtIssuer.getClaims(jwtDto.getAccessToken());
        Claims refreshClaims = jwtIssuer.getClaims(jwtDto.getRefreshToken());

        /*
         * 추가 검증 로직
         */

        return accessClaims != null && refreshClaims != null
            && jwtIssuer.getSubject(accessClaims).equals(jwtIssuer.getSubject(refreshClaims));
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtIssuer.getClaims(token);
        String email = jwtIssuer.getSubject(claims);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
    }

}
