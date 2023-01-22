package com.example.oauth2practice2.web;

import com.example.oauth2practice2.domain.dto.CustomOAuth2User;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @GetMapping("/social/{provider}")
    public void login(HttpServletResponse response, @PathVariable String provider)
        throws IOException {
        response.sendRedirect("/oauth2/authorization/" + provider);
    }

    @GetMapping("/authorized")
    public ResponseEntity<?> authorized(@AuthenticationPrincipal CustomOAuth2User user) {
        return ResponseEntity.ok(user);
    }

}
