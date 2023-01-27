package com.example.oauth2practice2.domain.handler;

import com.example.oauth2practice2.domain.dto.CustomOAuth2User;
import com.example.oauth2practice2.domain.dto.Jwt;
import com.example.oauth2practice2.domain.dto.OAuthAttributes;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {

        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
        OAuthAttributes attributes = user.getOAuthAttributes();

        // Jwt 추가하기 귀찮아서 그냥 이메일/닉네임 넣음
        String accessToken = attributes.getEmail();
        String refreshToken = attributes.getName();
        ResponseEntity<Jwt> responseEntity = ResponseEntity.ok(Jwt.of(accessToken, refreshToken));

        PrintWriter writer = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(writer, responseEntity);
        writer.flush();
        writer.close();
    }
}
