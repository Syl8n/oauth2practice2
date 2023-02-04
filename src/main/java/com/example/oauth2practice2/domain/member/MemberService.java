package com.example.oauth2practice2.domain.member;

import com.example.oauth2practice2.domain.jwt.JwtDto;
import com.example.oauth2practice2.domain.jwt.JwtIssuer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtIssuer jwtIssuer;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return getMemberByEmail(email);
    }

    public Member signUp(SignUpForm form) {
        if(memberRepository.existsByEmail(form.getEmail())){
            throw new RuntimeException("사용중인 이메일입니다.");
        }
        return memberRepository.save(Member.builder()
            .email(form.getEmail())
            .password(passwordEncoder.encode(form.getPassword()))
            .name(form.getName())
            .memberRole(MemberRole.USER)
            .provider(MemberProvider.LOCAL)
            .build());
    }

    public JwtDto signIn(SignUpForm form) {
        Member member = getMemberByEmail(form.getEmail());

        if (!passwordEncoder.matches(form.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("일치하는 정보가 없습니다.");
        }

        return jwtIssuer.createToken(member.getEmail(), member.getMemberRole().name());
    }

    public JwtDto socialSignIn(SignUpForm form) {
        Member member;
        try {
            member = getMemberByEmail(form.getEmail());
        }catch (UsernameNotFoundException e) {
            member = memberRepository.save(Member.builder()
                .email(form.getEmail())
                .name(form.getName())
                .memberRole(MemberRole.USER)
                .provider(MemberProvider.KAKAO)
                .build());
        }

        return jwtIssuer.createToken(member.getEmail(), member.getMemberRole().name());
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("일치하는 정보가 없습니다."));
    }
}
