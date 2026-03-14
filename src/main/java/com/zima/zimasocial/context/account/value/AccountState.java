package com.zima.zimasocial.context.account.value;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Getter
public class AccountState {
    private Boolean isDisabled;
    private DisableReason disableReason;
    private Boolean isDeleted;
    private DeleteReason deleteReason;
    private Boolean isPrivate;
    private Boolean termsOfUseAccepted;
    private LocalDate disableDate;
    private LocalDate deleteDate;
    private Boolean isBanned;
}
