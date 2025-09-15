package com.zimaberlin.zimasocial.context.account.entity;

import lombok.Getter;

import java.util.Objects;

@Getter
public class AccountId {
    private final Long value;

    public AccountId(Long value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountId accountId = (AccountId) o;
        return Objects.equals(value, accountId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
