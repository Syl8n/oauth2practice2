package com.example.oauth2practice2.web;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @GetMapping("/social/{provider}")
    public void login(HttpServletResponse response, @PathVariable String provider)
        throws IOException {
        response.sendRedirect("/oauth2/authorization/" + provider);
    }

}
