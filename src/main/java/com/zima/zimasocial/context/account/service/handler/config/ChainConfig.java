package com.zima.zimasocial.context.account.service.handler.config;

import com.zima.zimasocial.context.account.service.handler.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChainConfig {
    @Bean
    public AccountLoginChain accountLoginChain(
            AccountLoaderStep accountLoaderStep,
            AccountBannedStep accountBannedStep,
            AccountActivationStep accountActivationStep,
            AccountCreationStep accountCreationStep,
            TokenIssuerStep tokenIssuerStep,
            AccountDeletedStep accountDeletedStep
    ) {
        return new AccountLoginChain(
                List.of(
                        accountLoaderStep,
                        accountBannedStep,
                        accountDeletedStep,
                        accountActivationStep,
                        accountCreationStep,
                        tokenIssuerStep
                )
        );
    }
}
