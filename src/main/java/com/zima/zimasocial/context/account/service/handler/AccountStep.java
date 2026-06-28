package com.zima.zimasocial.context.account.service.handler;

import com.zima.zimasocial.context.account.service.handler.context.LoginContext;

import java.util.zip.DataFormatException;

public interface AccountStep {
    void execute(LoginContext loginContext) throws Exception;
}
