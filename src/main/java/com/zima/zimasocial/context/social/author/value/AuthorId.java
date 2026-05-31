package com.zima.zimasocial.context.social.author.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorId {
    @Column(name = "user_id",updatable = false)
    private Long value;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AuthorId authorId = (AuthorId) o;
        return Objects.equals(value, authorId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
