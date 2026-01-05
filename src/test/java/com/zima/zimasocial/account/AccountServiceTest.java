package com.zima.zimasocial.account;

import com.zima.zimasocial.context.account.repository.AccountRepository;
import com.zima.zimasocial.context.account.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountService accountService;
    @BeforeEach
    void setUp() {}
}
