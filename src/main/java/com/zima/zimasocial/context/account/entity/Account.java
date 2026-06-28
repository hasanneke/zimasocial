package com.zima.zimasocial.context.account.entity;

import com.zima.zimasocial.context.account.event.AccountActivatedEvent;
import com.zima.zimasocial.context.account.event.AccountDisabledEvent;
import com.zima.zimasocial.context.account.exception.*;
import com.zima.zimasocial.context.account.service.LoginType;
import com.zima.zimasocial.context.account.value.*;
import com.zima.zimasocial.shared.StaticEventPublisher;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Table(name = "users")
@Entity
@Getter
public class Account {
    @EmbeddedId
    @AttributeOverride(
            name = "value",
            column = @Column(name = "id", updatable = false)
    )
    private AccountId accountId;

    @NotEmpty
    @Email
    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    private LoginType loginType;

    @Column(name = "name")
    private String name;

    @Column(name = "family_name")
    private String familyName;

    @Column(name = "slug", unique = true, length = 65)
    private String slug;

    @Column(name = "is_disabled")
    private boolean isDisabled = false;

    @Column(name = "disable_date")
    private LocalDate lastDisabledDate;

    @Column(name = "delete_date")
    private LocalDate lastDeletionDate;

    @Column(name = "IS_DELETED", nullable = false)
    private boolean isDeleted = false;

    @Column(name = "is_private")
    private boolean isPrivate = false;

    @Column(name = "terms_of_use_accepted")
    private Boolean termsOfUseAccepted = false;

    @Column(name = "delete_reason")
    @Enumerated(EnumType.STRING)
    private DeleteReason deleteReason;

    @Column(name = "disable_reason")
    @Enumerated(EnumType.STRING)
    private DisableReason disableReason;

    @Column(name = "is_banned")
    private boolean isBanned = false;

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<UserRole> roles;

    protected Account() {}

    public static Account newAccount(AccountIdentity accountIdentity, PersonalInfo personalInfo) {
        Assert.notNull(accountIdentity.getEmail(), "User must have email address");
        Assert.notNull(accountIdentity.getLoginType(), "User must have auth provider info");
        Assert.notNull(accountIdentity.getAccountId(), "User id cannot be null");
        Assert.notNull(accountIdentity.getSlug(), "Slug cannot be null");
        if(accountIdentity.getSlug().length() > 32){
            throw new SlugLengthExceededException();
        }
        Account account = new Account();
        account.accountId = accountIdentity.getAccountId();
        account.loginType = accountIdentity.getLoginType();
        account.email = accountIdentity.getEmail();
        account.roles = accountIdentity.getRoles();
        account.name = personalInfo.getName();
        account.familyName = personalInfo.getSurname();
        account.slug = accountIdentity.getSlug();

        return account;
    }
    public static Account newAccount(AccountIdentity accountIdentity, PersonalInfo personalInfo, AccountState state) {
        Account account = newAccount(accountIdentity, personalInfo);
        account.isDisabled = state.getIsDisabled();
        account.isDeleted = state.getIsDeleted();
        account.isPrivate = state.getIsPrivate();
        account.termsOfUseAccepted = state.getTermsOfUseAccepted();
        account.disableReason = state.getDisableReason();
        return account;
    }
    public void disableAccount(DisableReason reason) {
        if(disabledBefore() && !sevenDaysPassedSinceLastDisable()) {
            throw new AccountCannotBeDisabledException();
        }
        if(reason == null) throw new IllegalArgumentException("DisableReason cannot be null when account being disabled");
        this.isDisabled = true;
        this.disableReason = reason;
        this.lastDisabledDate = LocalDate.now();
        StaticEventPublisher.publishEvent(new AccountDisabledEvent(accountId));
    }

    private boolean sevenDaysPassedSinceLastDisable() {
        return ChronoUnit.DAYS.between(lastDisabledDate, LocalDate.now()) > 7;
    }

    private boolean disabledBefore() {
        return lastDisabledDate != null;
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
        if(deletedBefore() && !sevenDaysPassedSinceLastDeletion()){
            throw new AccountCannotBeDeletedException();
        }
        Assert.notNull(deleteReason, "Delete reason cannot be null when account being deleted");
        this.isDeleted = true;
        this.deleteReason = deleteReason;
        this.lastDeletionDate = LocalDate.now();
    }

    private boolean sevenDaysPassedSinceLastDeletion() {
        return ChronoUnit.DAYS.between(lastDeletionDate, LocalDate.now()) > 7;
    }

    private boolean deletedBefore() {
        return lastDeletionDate != null;
    }

    public void activateAccount() {
        Assert.isTrue(isDisabled || isDeleted, "Account is not disabled or deleted. Therefore cannot be reactivated.");
        this.isDisabled = false;
        this.disableReason = null;
        this.isDeleted = false;
        StaticEventPublisher.publishEvent(new AccountActivatedEvent(accountId));
    }

    public void acceptTermsOfUse() {
        this.termsOfUseAccepted = true;
    }

    public boolean isMarkedForDeletion() {
        return isDeleted && ChronoUnit.DAYS.between(LocalDate.now(), lastDeletionDate) < 30;
    }
}
