package com.zimaberlin.zimasocial.context.social.author;

import java.util.Objects;

public class AuthorId {
    private final Long id;

    public AuthorId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorId authorId = (AuthorId) o;
        return Objects.equals(id, authorId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
