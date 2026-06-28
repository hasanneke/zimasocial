package com.zima.zimasocial.context.social.author;

import com.zima.zimasocial.account.AccountFixture;
import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.event.AccountDeletedEvent;
import com.zima.zimasocial.context.account.factory.AccountFactory;
import com.zima.zimasocial.context.account.repository.AccountRepository;
import com.zima.zimasocial.context.account.service.AccountService;
import com.zima.zimasocial.context.account.service.AppleUserInfoProvider;
import com.zima.zimasocial.context.account.service.GoogleUserInfoProvider;
import com.zima.zimasocial.context.account.service.UserInfoProvider;
import com.zima.zimasocial.context.account.value.DeleteReason;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    private AccountFactory accountFactory = new AccountFactory(accountRepository);
    private final List<UserInfoProvider> userInfoProvider
            = List.of(mock(AppleUserInfoProvider.class), mock(GoogleUserInfoProvider.class));
    @InjectMocks
    private AccountService accountService;

//    @Test
//    void testLogin_whenAccountNotFound_CreateNewAccount() throws Exception {
//        LoginCredential loginCredential = LoginCredential.builder()
//                .token("googleToken")
//                .loginType(LoginType.google)
//                .build();
//        UserInfo userInfo = UserInfo.builder()
//                .name("zima")
//                .surname("social")
//                .email("zimasocial@gmail.com")
//                .build();
//
//        when(any(UserInfoProvider.class).find(loginCredential)).thenReturn(userInfo);
//        when(accountRepository.findByEmailAndLoginType(userInfo.getEmail(), loginCredential.getLoginType())).thenReturn(Optional.empty());
//        when(accountRepository.nextId()).thenReturn()
//    }

    @Test
    void testDeleteAccount_WhenSuccess() {
        Account newAccount = AccountFixture.newAccount();
        when(accountRepository.getAuthenticatedAccount()).thenReturn(newAccount);
        DeleteReason deleteReason = DeleteReason.SOMETHING_ELSE;

        accountService.deleteAccount(deleteReason);

        assertDeleteReasonApplied(newAccount);
        assertDeletionDateSetAsTodaysDate(newAccount);
        assertIsDeletedBecameTrue(newAccount);

        verifyAccountDeletedEventPublished(newAccount);
    }

    private void assertIsDeletedBecameTrue(Account newAccount) {
        Assertions.assertTrue(newAccount.isDeleted());
    }

    private void assertDeletionDateSetAsTodaysDate(Account newAccount) {
        Assertions.assertEquals(LocalDate.now(), newAccount.getLastDeletionDate());
    }

    private void assertDeleteReasonApplied(Account newAccount) {
        Assertions.assertEquals(DeleteReason.SOMETHING_ELSE, newAccount.getDeleteReason());
    }

    private void verifyAccountDeletedEventPublished(Account newAccount) {
        verify(applicationEventPublisher).publishEvent(new AccountDeletedEvent(newAccount.getAccountId()));
    }
}
