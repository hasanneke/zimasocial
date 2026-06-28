package com.zima.zimasocial.context.account.service.handler.config;

import com.zima.zimasocial.context.account.service.handler.AccountStep;
import com.zima.zimasocial.context.account.service.handler.context.LoginContext;

import java.util.List;

public class AccountLoginChain {
    private final List<AccountStep> stepList;

    public AccountLoginChain(List<AccountStep> stepList) {
        this.stepList = stepList;
    }

    public void start(LoginContext loginContext) throws Exception {
        for (AccountStep accountStep : stepList) {
            accountStep.execute(loginContext);
        }
    }
}
