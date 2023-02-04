package com.example.oauth2practice2.domain.social;

import lombok.Getter;

@Getter
public class KakaoToken {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String expires_in;
    private String scope;
    private String refresh_token_expires_in;
}
