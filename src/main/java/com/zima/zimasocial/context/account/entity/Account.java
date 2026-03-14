package com.zima.zimasocial.context.account.entity;

import com.zima.zimasocial.context.account.event.AccountActivatedEvent;
import com.zima.zimasocial.context.account.event.AccountDeletedEvent;
import com.zima.zimasocial.context.account.event.AccountDisabledEvent;
import com.zima.zimasocial.context.account.exception.AccountIsAlreadyPrivateException;
import com.zima.zimasocial.context.account.exception.AccountIsAlreadyPublicException;
import com.zima.zimasocial.context.account.exception.SlugLengthExceededException;
import com.zima.zimasocial.context.account.value.*;
import com.zima.zimasocial.entity.UserRole;
import com.zima.zimasocial.shared.StaticEventPublisher;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
public class Account {
    private AccountId accountId;
    private String email;
    private String authProvider;
    private String name;
    private String familyName;
    private String slug;
    private Boolean isDisabled;
    private LocalDate disableDate;
    private LocalDate deleteDate;
    private Boolean isDeleted;
    private Boolean isPrivate;
    private Boolean termsOfUseAccepted;
    private DeleteReason deleteReason;
    private DisableReason disableReason;
    private Boolean isBanned;
    private Set<UserRole> roles;
    private Account() {}

    public static Account newAccount(AccountIdentity accountIdentity, PersonalInfo personalInfo) {
        Assert.notNull(accountIdentity.getEmail(), "User must have email address");
        Assert.notNull(accountIdentity.getAuthProvider(), "User must have auth provider info");
        Assert.notNull(accountIdentity.getAccountId(), "User id cannot be null");
        Assert.notNull(accountIdentity.getSlug(), "Slug cannot be null");
        if(accountIdentity.getSlug().length() > 16){
            throw new SlugLengthExceededException();
        }
        Account account = new Account();
        account.accountId = accountIdentity.getAccountId();
        account.isDisabled = false;
        account.isDeleted = false;
        account.isPrivate = false;
        account.termsOfUseAccepted = false;
        account.authProvider = accountIdentity.getAuthProvider();
        account.email = accountIdentity.getEmail();
        account.roles = accountIdentity.getRoles();
        account.name = personalInfo.getName();
        account.familyName = personalInfo.getSurname();
        account.slug = accountIdentity.getSlug();

        return account;
    }

    public static Account reconstitute(AccountIdentity accountIdentity, PersonalInfo personalInfo, AccountState accountState) {
        Assert.notNull(accountIdentity.getEmail(), "User must have email address");
        Assert.notNull(accountIdentity.getAuthProvider(), "User must have auth provider info");
        Assert.notNull(accountIdentity.getAccountId(), "Account id cannot be null");
        Assert.notNull(accountState.getIsDeleted(), "Account is deleted cannot be null");
        Assert.notNull(accountState.getIsDisabled(), "Is disabled cannot be null");
        Assert.notNull(accountState.getIsPrivate(), "Is private cannot be null");
        Assert.notNull(accountIdentity.getSlug(), "Slug cannot be null");
        Assert.notNull(accountState.getTermsOfUseAccepted(), "Terms of use accepted cannot be null");

        Account account = new Account();
        account.slug = accountIdentity.getSlug();
        if(accountState.getIsDeleted()) {
            if(accountState.getDisableDate() == null){
                throw new IllegalArgumentException("Disable date cannot be null if account is disabled");
            }
            if(accountState.getDisableReason() == null){
                throw new IllegalArgumentException("Disable reason cannot be null if account is disabled");
            }
        }
        if(accountState.getIsDeleted()){
            if(accountState.getDeleteDate() == null){
                throw new IllegalArgumentException("Delete date cannot be null if account is disabled");
            }
            if(accountState.getDeleteReason() == null){
                throw new IllegalArgumentException("Delete reason cannot be null if account is disabled");
            }
        }
        account.accountId = accountIdentity.getAccountId();
        account.isDisabled = accountState.getIsDisabled();
        account.disableDate = accountState.getDisableDate();
        account.deleteDate = accountState.getDeleteDate();
        account.isDeleted = accountState.getIsDeleted();
        account.isPrivate = accountState.getIsPrivate();
        account.deleteReason = accountState.getDeleteReason();
        account.disableReason = accountState.getDisableReason();
        account.authProvider = accountIdentity.getAuthProvider();
        account.termsOfUseAccepted = accountState.getTermsOfUseAccepted();
        account.email = accountIdentity.getEmail();
        account.roles = accountIdentity.getRoles();
        account.name = personalInfo.getName();
        account.familyName = personalInfo.getSurname();
        account.isBanned = accountState.getIsBanned();

        return account;
    }
    public void disableAccount(DisableReason reason) {
        if(reason == null) throw new IllegalArgumentException("DisableReason cannot be null when account being disabled");
        this.isDisabled = true;
        this.disableReason = reason;
        this.disableDate = LocalDate.now();
        StaticEventPublisher.publishEvent(new AccountDisabledEvent(accountId));
    }
    public void makeAccountPublic() {
        if(!isPrivate){
            throw new AccountIsAlreadyPublicException();
        }
        this.isPrivate = false;
    }
    public void makeAccountPrivate() {
        if(isPrivate){
            throw new AccountIsAlreadyPrivateException();
        }
        this.isPrivate = true;
    }
    public void deleteAccount(DeleteReason deleteReason){
        Assert.notNull(deleteReason, "Delete reason cannot be null when account being deleted");
        this.isDeleted = true;
        this.deleteReason = deleteReason;
        this.deleteDate = LocalDate.now();
        this.slug = String.valueOf(UUID.randomUUID());
        StaticEventPublisher.publishEvent(new AccountDeletedEvent(accountId));
    }
    public void activateAccount() {
        Assert.isTrue(isDisabled, "Account is not disabled. Therefore cannot be reactivated.");
        this.isDisabled = false;
        this.disableReason = null;
        this.disableDate = null;
        StaticEventPublisher.publishEvent(new AccountActivatedEvent(accountId));
    }

    public void acceptTermsOfUse() {
        this.termsOfUseAccepted = true;
    }
}
