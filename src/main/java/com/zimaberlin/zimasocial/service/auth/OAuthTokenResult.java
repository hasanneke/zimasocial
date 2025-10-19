package com.zimaberlin.zimasocial.service.auth;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OAuthTokenResult {
    private String email;
    private String name;
    private String surname;
}
