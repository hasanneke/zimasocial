package com.zima.zimasocial.context.account.infastructure.adapter;

import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.entity.AccountId;
import com.zima.zimasocial.context.account.value.AccountIdentity;
import com.zima.zimasocial.context.account.value.AccountState;
import com.zima.zimasocial.context.account.value.PersonalInfo;
import com.zima.zimasocial.entity.user.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountUserAdapter {
     public Account convertUserEntityToAccount(UserEntity user) {
        if(user == null) return null;
         AccountIdentity accountIdentity = AccountIdentity
                 .builder()
                 .accountId(new AccountId(user.getId()))
                 .slug(user.getSlug())
                 .email(user.getEmail())
                 .authProvider(user.getAuthProvider())
                 .roles(user.getRoles())
                 .build();
         AccountState accountState = AccountState.builder()
                 .disableReason(user.getDisableReason())
                 .isDisabled(user.getIsDisabled())
                 .disableDate(user.getDisableDate())
                 .deleteDate(user.getDeleteDate())
                 .deleteReason(user.getDeleteReason())
                 .isDeleted(user.getIsDeleted())
                 .isPrivate(user.isPrivate())
                 .termsOfUseAccepted(user.getTermsOfUseAccepted())
                 .isBanned(user.getIsBanned())
                 .build();
         PersonalInfo personalInfo = PersonalInfo.builder()
                 .name(user.getName())
                 .surname(user.getFamilyName())
                 .build();

        return Account.reconstitute(accountIdentity, personalInfo, accountState);
    }
}
