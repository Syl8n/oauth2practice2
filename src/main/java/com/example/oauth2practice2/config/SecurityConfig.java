package com.example.oauth2practice2.config;

import com.example.oauth2practice2.domain.handler.OAuth2SuccessHandler;
import com.example.oauth2practice2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .formLogin().disable()
            .httpBasic().disable()
            .csrf().disable()
            .headers().frameOptions().disable()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/", "/tests/**", "/login/**", "/error").permitAll()
            .anyRequest().authenticated()
            .and()
            .logout().logoutSuccessUrl("/")
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            .and()
            .oauth2Login().userInfoEndpoint().userService(customOAuth2UserService)
            .and()
            .successHandler(oAuth2SuccessHandler());
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler(){
        return new OAuth2SuccessHandler();
    }

}
