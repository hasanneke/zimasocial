package com.zimaberlin.zimasocial.context.social.author;

import java.util.Objects;

public class AuthorId {
    private final Long value;

    public AuthorId(Long id) {
        this.value = id;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorId authorId = (AuthorId) o;
        return Objects.equals(value, authorId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
