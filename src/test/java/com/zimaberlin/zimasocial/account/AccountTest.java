package com.zimaberlin.zimasocial.account;

import com.zimaberlin.zimasocial.context.account.entity.Account;
import com.zimaberlin.zimasocial.context.account.entity.AccountId;
import com.zimaberlin.zimasocial.context.account.event.AccountActivatedEvent;
import com.zimaberlin.zimasocial.context.account.event.AccountDeletedEvent;
import com.zimaberlin.zimasocial.context.account.event.AccountDisabledEvent;
import com.zimaberlin.zimasocial.context.account.value.DeleteReason;
import com.zimaberlin.zimasocial.context.account.value.DisableReason;
import com.zimaberlin.zimasocial.shared.StaticEventPublisher;
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
    @Test
    void testDisableAccount_WhenSuccess() {
        try(MockedStatic<StaticEventPublisher> mockedStatic = Mockito.mockStatic(StaticEventPublisher.class)){
            AccountId accountId = new AccountId(0L);
            Account testAccount = new Account(accountId, "", "", "", Set.of(), false, null, null, false, false, false, null, null);
            testAccount.disableAccount(DisableReason.SOMETHING_ELSE);
            Assertions.assertEquals(testAccount.getDisableReason(), DisableReason.SOMETHING_ELSE);
            Assertions.assertEquals(testAccount.getDisableDate(), LocalDate.now());
            Assertions.assertEquals(testAccount.getIsDisabled(), true);
            mockedStatic.verify(() -> StaticEventPublisher.publishEvent(new AccountDisabledEvent(accountId)));
        }
    }
    @Test
    void testDisableAccount_WhenReasonIsNull_ThenThrowIllegalArgumentException() {
        Account testAccount = new Account(new AccountId(0L), "", "", "", Set.of(), false, null, null, false, false, false, null, null);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> testAccount.disableAccount(null));
    }
    @Test
    void testActivateAccount_WhenSuccess() {
        try(MockedStatic<StaticEventPublisher> mockedStatic = Mockito.mockStatic(StaticEventPublisher.class)){
            Account testAccount = new Account(new AccountId(0L), "", "","", Set.of(), true, LocalDate.now(), null, false, false, false, null, DisableReason.SOMETHING_ELSE);
            testAccount.activateAccount();
            Assertions.assertNull(testAccount.getDisableReason());
            Assertions.assertNull(testAccount.getDisableDate());
            Assertions.assertEquals(testAccount.getIsDisabled(), false);
            mockedStatic.verify(() -> StaticEventPublisher.publishEvent(new AccountActivatedEvent(new AccountId(0L))));
        }
    }

    @Test
    void testActivateAccount_WhenAccountIsNotDisabled_ThrowAccountIsNotDisabledException() {
        Account testAccount = new Account(new AccountId(0L), "", "", "", Set.of(), false, null, null, false, false, false, null, null);
        Assertions.assertThrows(IllegalArgumentException.class, testAccount::activateAccount);
    }

    @Test
    void testDeleteAccount_WhenSuccess() {
        try(MockedStatic<StaticEventPublisher> mockedStatic = Mockito.mockStatic(StaticEventPublisher.class)){
            Account testAccount = new Account(new AccountId(0L), "", "", "", Set.of(), false, null, null, false, false, false, null, null);
            testAccount.deleteAccount(DeleteReason.SOMETHING_ELSE);
            Assertions.assertEquals(testAccount.getDeleteReason(), DeleteReason.SOMETHING_ELSE);
            Assertions.assertEquals(testAccount.getDeleteDate(), LocalDate.now());
            Assertions.assertTrue(testAccount.getIsDeleted());
            mockedStatic.verify(() -> StaticEventPublisher.publishEvent(new AccountDeletedEvent(new AccountId(0L))));
        }
    }

    @Test
    void deleteAccount_WhenDeleteReasonNotPassed_ThrowIllegalArgumentException() {
        Account testAccount = new Account(new AccountId(0L), "", "", "", Set.of(), false, null, null, false, false, false, null, null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> testAccount.deleteAccount(null));
    }
}
