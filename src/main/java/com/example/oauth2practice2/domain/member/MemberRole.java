package com.example.oauth2practice2.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {

    GUEST,
    USER;

    private static final String PREFIX = "ROLE_";

    public String getAuthority(){
        return PREFIX + this.name();
    }

}
