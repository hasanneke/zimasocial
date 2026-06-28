package com.zima.zimasocial.context.account.service;

public interface UserInfoProvider {
    LoginType loginType();
    UserInfo find(LoginCredential loginCredential) throws Exception;
}
