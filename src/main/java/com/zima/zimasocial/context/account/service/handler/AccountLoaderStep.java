package com.zima.zimasocial.context.account.service.handler;

import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.repository.AccountRepository;
import com.zima.zimasocial.context.account.service.LoginCredential;
import com.zima.zimasocial.context.account.service.UserInfo;
import com.zima.zimasocial.context.account.service.UserInfoProvider;
import com.zima.zimasocial.context.account.service.handler.context.LoginContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

@Component
@RequiredArgsConstructor
public class AccountLoaderStep implements AccountStep{
    private final List<UserInfoProvider> userInfoProviders;
    private final AccountRepository accountRepository;
    @Override
    public void execute(LoginContext loginContext) throws Exception {
        LoginCredential loginCredential = loginContext.getLoginCredential();
        UserInfoProvider userInfoProvider = userInfoProviders.stream()
                .filter(e->e.loginType().equals(loginCredential.getLoginType()))
                .findFirst()
                .orElseThrow(DataFormatException::new);
        UserInfo userInfo = userInfoProvider.find(loginCredential);
        Optional<Account> account = accountRepository.findByEmailAndLoginType(userInfo.getEmail(), loginCredential.getLoginType());
        loginContext.setUserInfo(userInfo);
        account.ifPresent(loginContext::setAccount);
    }
}
