package com.zimaberlin.zimasocial.context.account.entity;

import com.zimaberlin.zimasocial.context.account.event.AccountActivatedEvent;
import com.zimaberlin.zimasocial.context.account.event.AccountDeletedEvent;
import com.zimaberlin.zimasocial.context.account.event.AccountDisabledEvent;
import com.zimaberlin.zimasocial.entity.UserRole;
import com.zimaberlin.zimasocial.context.account.exception.AccountIsAlreadyPrivateException;
import com.zimaberlin.zimasocial.context.account.exception.AccountIsAlreadyPublicException;
import com.zimaberlin.zimasocial.context.account.value.DeleteReason;
import com.zimaberlin.zimasocial.context.account.value.DisableReason;
import com.zimaberlin.zimasocial.shared.StaticEventPublisher;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Set;

@Getter
public class Account {
    public Account(Long userId, String email, String authProvider, Set<UserRole> roles, Boolean isDisabled, LocalDate disableDate, LocalDate deleteDate, Boolean isDeleted, Boolean isPrivate, DeleteReason deleteReason, DisableReason disableReason) {
        Assert.notNull(email, "User must have email address");
        Assert.notNull(email, "User must have auth provider info");
        Assert.notNull(userId, "User id cannot be null");
        Assert.notNull(isDeleted, "Account is deleted cannot be null");
        Assert.notNull(isDisabled, "Is disabled cannot be null");
        Assert.notNull(isPrivate, "Is private cannot be null");
        if(isDisabled) {
            if(disableDate == null){
                throw new IllegalArgumentException("Disable date cannot be null if account is disabled");
            }
            if(disableReason == null){
                throw new IllegalArgumentException("Disable reason cannot be null if account is disabled");
            }
        }
        if(isDeleted){
            if(deleteDate == null){
                throw new IllegalArgumentException("Delete date cannot be null if account is disabled");
            }
            if(deleteReason == null){
                throw new IllegalArgumentException("Delete reason cannot be null if account is disabled");
            }
        }
        this.userId = userId;
        this.isDisabled = isDisabled;
        this.disableDate = disableDate;
        this.deleteDate = deleteDate;
        this.isDeleted = isDeleted;
        this.isPrivate = isPrivate;
        this.deleteReason = deleteReason;
        this.disableReason = disableReason;
        this.authProvider = authProvider;
        this.email = email;
        this.roles = roles;
    }
    private final Long userId;
    private final String email;
    private final String authProvider;
    private Boolean isDisabled;
    private LocalDate disableDate;
    private LocalDate deleteDate;
    private Boolean isDeleted;
    private Boolean isPrivate;
    private DeleteReason deleteReason;
    private DisableReason disableReason;
    private final Set<UserRole> roles;
    public void disableAccount(DisableReason reason) {
        if(reason == null) throw new IllegalArgumentException("DisableReason cannot be null when account being disabled");
        this.isDisabled = true;
        this.disableReason = reason;
        this.disableDate = LocalDate.now();
        StaticEventPublisher.publishEvent(new AccountDisabledEvent(userId));
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
        StaticEventPublisher.publishEvent(new AccountDeletedEvent(userId));
    }
    public void activateAccount() {
        Assert.isTrue(isDisabled, "Account is not disabled. Therefore cannot be reactivated.");
        this.isDisabled = false;
        this.disableReason = null;
        this.disableDate = null;
        StaticEventPublisher.publishEvent(new AccountActivatedEvent(userId));
    }
}
