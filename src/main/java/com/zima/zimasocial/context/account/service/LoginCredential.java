package com.zima.zimasocial.context.account.service;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class LoginCredential {
    private String token;
    private LoginType loginType;
}
