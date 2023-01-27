package com.example.oauth2practice2.domain.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Jwt {
    private String accessToken;
    private String refreshToken;

    public static Jwt of(String accessToken, String refreshToken){
        return new Jwt(accessToken, refreshToken);
    }
}
