package com.example.oauth2practice2.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {
    private String email;
    private String password;
    private String name;
}
