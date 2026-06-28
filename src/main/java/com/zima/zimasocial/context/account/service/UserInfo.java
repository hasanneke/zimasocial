package com.zima.zimasocial.context.account.service;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfo {
    private String email;
    private String name;
    private String surname;
}
