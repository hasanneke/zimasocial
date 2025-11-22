package com.zimaberlin.zimasocial.context.communication.domain.value;

import lombok.Getter;

import java.util.Objects;

@Getter
public class RecipientId {
    private final Long value;

    public RecipientId(Long value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipientId that = (RecipientId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
