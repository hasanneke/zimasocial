package com.zima.zimasocial.context.account.service.handler;

import com.zima.zimasocial.context.account.service.JWTService;
import com.zima.zimasocial.context.account.service.handler.context.LoginContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenIssuerStep implements AccountStep{
    private final JWTService jwtService;
    @Override
    public void execute(LoginContext loginContext) throws Exception {
        if(loginContext.account().isEmpty()) return;
        loginContext.setToken(jwtService.createRefreshToken(loginContext.account().get()));
    }
}
