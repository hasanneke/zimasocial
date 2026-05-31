package com.zima.zimasocial.context.social.author.repository;

import com.zima.zimasocial.context.social.author.exception.AuthorNotFoundException;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.utility.CurrentUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, AuthorId> {
    default Author getAuthenticatedAuthor() {
        return findById(CurrentUser.getCurrentUserId()).orElseThrow(AuthorNotFoundException::new);
    }
    Optional<Author> findBySlug(String slug);

    @Query(value = "SELECT * FROM USERS u WHERE (u.name ILIKE %:query% OR u.slug ILIKE %:query%) AND IS_DELETED = false AND IS_DISABLED = false", nativeQuery = true)
    Page<Author> searchUser(String query, Pageable pageable);

    @Query(value = "SELECT * FROM USERS u WHERE u.slug = :slug AND u.is_deleted = false", nativeQuery = true)
    Optional<Author> anyAuthorHasSlug(String slug);
}
