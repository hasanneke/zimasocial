package com.zima.zimasocial.context.account.infastructure.adapter;

import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.entity.AccountId;
import com.zima.zimasocial.entity.user.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountUserAdapter {
     public Account convertUserEntityToAccount(UserEntity user) {
        if(user == null) return null;
        return new Account(new AccountId(user.getId()), user.getEmail(), user.getSlug(), user.getAuthProvider(), user.getRoles(), user.getIsDisabled(), user.getDisableDate(), user.getDeleteDate(), user.getIsDeleted(), user.isPrivate(), user.getTermsOfUseAccepted(), user.getDeleteReason(), user.getDisableReason());
    }
}
