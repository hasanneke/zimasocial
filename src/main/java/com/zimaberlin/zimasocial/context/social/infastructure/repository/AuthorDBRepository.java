package com.zimaberlin.zimasocial.context.social.infastructure.repository;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.infastructure.adapter.AuthorUserEntityAdapter;
import com.zimaberlin.zimasocial.context.social.author.AuthorRepository;
import com.zimaberlin.zimasocial.repository.UserJpaRepository;
import com.zimaberlin.zimasocial.service.users.exception.UserNotFoundException;
import com.zimaberlin.zimasocial.utility.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AuthorDBRepository implements AuthorRepository {
    private final AuthorUserEntityAdapter authorUserEntityAdapter;
    private final UserJpaRepository userRepository;
    @Autowired
    public AuthorDBRepository(AuthorUserEntityAdapter authorUserEntityAdapter, UserJpaRepository userRepository) {
        this.authorUserEntityAdapter = authorUserEntityAdapter;
        this.userRepository = userRepository;
    }

    @Override
    public Author getAuthenticatedAuthor() {
        UserEntity user = CurrentUser.getCurrentUserProfile();
        return authorUserEntityAdapter.convertUserEntityToAuthor(user);
    }

    @Override
    public Optional<Author> findById(AuthorId authorId) {
        return Optional.ofNullable(authorUserEntityAdapter.convertUserEntityToAuthor(userRepository.findById(authorId.getId()).orElse(null)));
    }

    public void save(Author author){
        UserEntity user = userRepository.findById(author.getId().getId()).orElseThrow(UserNotFoundException::new);
        user.margeAuthor(author);
        userRepository.save(user);
    }

    @Override
    public Optional<Author> findBySlug(String slug) {
        Optional<UserEntity> user = userRepository.findBySlug(slug);
        return user.map(authorUserEntityAdapter::convertUserEntityToAuthor);
    }

    @Override
    public Page<Author> search(String query, int page, int size) {
        Page<UserEntity> userEntities = userRepository.searchUser(query, PageRequest.of(page, size));
        return userEntities.map(authorUserEntityAdapter::convertUserEntityToAuthor);
    }
}
