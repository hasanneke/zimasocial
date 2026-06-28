package com.zima.zimasocial.account;

import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.entity.AccountId;
import com.zima.zimasocial.context.account.event.AccountActivatedEvent;
import com.zima.zimasocial.context.account.event.AccountDisabledEvent;
import com.zima.zimasocial.context.account.service.LoginType;
import com.zima.zimasocial.context.account.value.AccountIdentity;
import com.zima.zimasocial.context.account.value.AccountState;
import com.zima.zimasocial.context.account.value.DisableReason;
import com.zima.zimasocial.context.account.value.PersonalInfo;
import com.zima.zimasocial.shared.StaticEventPublisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class AccountTest {

    private AccountId createId() {
        return new AccountId(0L);
    }

    private AccountIdentity createDefaultIdentity(String slug) {
        return AccountIdentity.builder()
                .accountId(createId())
                .email("test@zima.com")
                .slug(slug)
                .loginType(LoginType.google)
                .roles(Set.of())
                .build();
    }

    private PersonalInfo createDefaultInfo() {
        return PersonalInfo.builder()
                .name("John")
                .surname("Doe")
                .build();
    }

    @Test
    void testDisableAccount_WhenSuccess() {
        try (MockedStatic<StaticEventPublisher> mockedStatic = Mockito.mockStatic(StaticEventPublisher.class)) {
            // Use the Static Factory for a fresh account
            Account testAccount = Account.newAccount(createDefaultIdentity("john-doe"), createDefaultInfo());

            testAccount.disableAccount(DisableReason.SOMETHING_ELSE);

            Assertions.assertEquals(DisableReason.SOMETHING_ELSE, testAccount.getDisableReason());
            Assertions.assertEquals(LocalDate.now(), testAccount.getLastDisabledDate());
            Assertions.assertTrue(testAccount.isDisabled());
            mockedStatic.verify(() -> StaticEventPublisher.publishEvent(new AccountDisabledEvent(createId())));
        }
    }

    @Test
    void testDisableAccount_WhenReasonIsNull_ThenThrowIllegalArgumentException() {
        Account testAccount = Account.newAccount(createDefaultIdentity("john-doe"), createDefaultInfo());
        Assertions.assertThrows(IllegalArgumentException.class, () -> testAccount.disableAccount(null));
    }

    @Test
    void testActivateAccount_WhenSuccess() {
        try (MockedStatic<StaticEventPublisher> mockedStatic = Mockito.mockStatic(StaticEventPublisher.class)) {
            // Use reconstitute to simulate an account that is ALREADY disabled
            AccountState disabledState = AccountState.builder()
                    .isDisabled(true)
                    .disableDate(LocalDate.now())
                    .disableReason(DisableReason.SOMETHING_ELSE)
                    .isDeleted(false)
                    .isPrivate(false)
                    .termsOfUseAccepted(true)
                    .build();

            Account testAccount = Account.newAccount(createDefaultIdentity("john-doe"), createDefaultInfo(), disabledState);

            testAccount.activateAccount();

            Assertions.assertNull(testAccount.getDisableReason());
            Assertions.assertFalse(testAccount.isDisabled());
            mockedStatic.verify(() -> StaticEventPublisher.publishEvent(new AccountActivatedEvent(createId())));
        }
    }

    @Test
    void deleteAccount_WhenDeleteReasonNotPassed_ThrowIllegalArgumentException() {
        Account testAccount = Account.newAccount(createDefaultIdentity("john-doe"), createDefaultInfo());
        Assertions.assertThrows(IllegalArgumentException.class, () -> testAccount.deleteAccount(null));
    }
}
