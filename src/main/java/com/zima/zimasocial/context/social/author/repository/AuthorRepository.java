package com.zima.zimasocial.context.social.author.repository;

import com.zima.zimasocial.context.social.author.entity.AuthorDomain;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    AuthorDomain getAuthenticatedAuthor();
    Optional<AuthorDomain> findById(AuthorId id);
    void save(AuthorDomain author);
    void saveAll(List<AuthorDomain> authors);
    Optional<AuthorDomain> findBySlugAndIsDisabledFalse(String slug);
    Optional<AuthorDomain> findBySlugAndIsDisabledFalseAndNotBeingBlocked(String slug);
    Page<AuthorDomain> search(String query, int page, int size);
}
