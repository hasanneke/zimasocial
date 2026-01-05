package com.zima.zimasocial.context.social.author.repository;

import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Author getAuthenticatedAuthor();
    Optional<Author> findById(AuthorId id);
    void save(Author author);
    void saveAll(List<Author> authors);
    Optional<Author> findBySlugAndIsDisabledFalse(String slug);
    Optional<Author> findBySlugAndIsDisabledFalseAndNotBeingBlocked(String slug);
    Page<Author> search(String query, int page, int size);
}
