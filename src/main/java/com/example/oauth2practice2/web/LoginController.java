package com.example.oauth2practice2.web;

import com.example.oauth2practice2.domain.member.Member;
import com.example.oauth2practice2.domain.jwt.JwtDto;
import com.example.oauth2practice2.domain.member.MemberService;
import com.example.oauth2practice2.domain.member.SignUpForm;
import com.example.oauth2practice2.domain.social.SocialLoginService;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final MemberService memberService;
    private final SocialLoginService socialLoginService;
    @GetMapping("/oauth2/{provider}")
    public void tryOAuth2(@PathVariable String provider, HttpServletResponse response)
        throws IOException {
        String url = socialLoginService.tryOAuth2(provider);
        response.sendRedirect(url);
    }
    @GetMapping("/oauth2/code/{provider}")
    public ResponseEntity<JwtDto> authorized(@PathVariable String provider, @RequestParam String code) {
        return socialLoginService.connectToSocialSignIn(provider, code);
    }
    @PostMapping("/social/{provider}")
    public ResponseEntity<JwtDto> socialSignIn(@PathVariable String provider, @RequestBody String code) {
        SignUpForm signUpForm = socialLoginService.signIn(provider, code);
        return ResponseEntity.ok(memberService.socialSignIn(signUpForm));
    }

    @PostMapping("/local/signup")
    public ResponseEntity<Member> signUp(@RequestBody SignUpForm form){
        return ResponseEntity.ok(memberService.signUp(form));
    }

    @PostMapping("/local/signin")
    public ResponseEntity<JwtDto> signIn(@RequestBody SignUpForm form){
        return ResponseEntity.ok(memberService.signIn(form));
    }

}
