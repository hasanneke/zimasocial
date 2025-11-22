package com.zimaberlin.zimasocial.context.communication.infastructure;

import com.zimaberlin.zimasocial.context.account.entity.AccountId;
import com.zimaberlin.zimasocial.context.account.service.AccountValidator;
import com.zimaberlin.zimasocial.context.communication.domain.service.RecipientValidator;
import com.zimaberlin.zimasocial.context.communication.domain.value.RecipientId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RecipientAccountValidator implements RecipientValidator {
    private final AccountValidator accountValidator;
    @Override
    public boolean canBePushed(RecipientId recipientId) {
        return accountValidator.hasAccessToApp(new AccountId(recipientId.getValue()));
    }
}
