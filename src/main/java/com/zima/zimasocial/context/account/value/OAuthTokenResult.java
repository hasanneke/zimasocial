package com.zima.zimasocial.context.account.value;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OAuthTokenResult {
    private String email;
    private String name;
    private String surname;
}
