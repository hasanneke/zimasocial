package com.zima.zimasocial.context.social2.repository;

import com.zima.zimasocial.context.social.author.exception.AuthorNotFoundException;
import com.zima.zimasocial.context.social2.domain.entity.Author;
import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import com.zima.zimasocial.utility.CurrentUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, AuthorId> {
    default Author getAuthenticatedAuthor() {
        return findById(CurrentUser.getCurrentUserId()).orElseThrow(AuthorNotFoundException::new);
    }
}
