package com.zima.zimasocial.context.social.author.value;

import java.util.Objects;

public class AuthorDomainId {

    private final Long value;

    public AuthorDomainId(Long id) {
        this.value = id;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorDomainId authorId = (AuthorDomainId) o;
        return Objects.equals(value, authorId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
