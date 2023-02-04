package com.example.oauth2practice2.domain.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtIssuer {

    private static String SECRET_KEY = "secretKeyForJsonWebTokenTutorial";
    public static final long EXPIRE_TIME = 1000 * 60 * 5;
    public static final long REFRESH_EXPIRE_TIME = 1000 * 60 * 15;
    public static final String KEY_ROLES = "roles";

    @PostConstruct
    void init(){
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    public JwtDto createToken(String userEmail, String role) {
        String encryptedEmail = Aes256Util.encrypt(userEmail);

        Claims claims = Jwts.claims().setSubject(encryptedEmail);
        claims.put(KEY_ROLES, role);

        Date now = new Date();

        String accessToken = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + EXPIRE_TIME))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();

        claims.setSubject(encryptedEmail);

        String refreshToken = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + REFRESH_EXPIRE_TIME))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();

        return JwtDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public String getSubject(Claims claims) {
        return Aes256Util.decrypt(claims.getSubject());
    }

    public Claims getClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        } catch (Exception e) {
            throw new BadCredentialsException("유효한 토큰이 아닙니다.");
        }
        return claims;
    }

}
