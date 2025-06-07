package com.zimaberlin.zimasocial.context.social.author;

import org.springframework.data.domain.Page;

import java.util.Optional;

public interface AuthorRepository {
    Author getAuthenticatedAuthor();
    Optional<Author> findById(Long authorId);
    void save(Author author);
    Optional<Author> findBySlug(String slug);
    Page<Author> search(String query, int page, int size);
}
