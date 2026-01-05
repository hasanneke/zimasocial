package com.zima.zimasocial.context.social.infastructure.repository;

import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.entity.user.UserEntity;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.infastructure.adapter.AuthorUserEntityAdapter;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.entity.userRelation.Relation;
import com.zima.zimasocial.entity.userRelation.UserRelationEntity;
import com.zima.zimasocial.repository.UserJpaRepository;
import com.zima.zimasocial.repository.UserRelationJpaRepository;
import com.zima.zimasocial.service.users.exception.UserNotFoundException;
import com.zima.zimasocial.utility.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorDBRepository implements AuthorRepository {
    private final AuthorUserEntityAdapter authorUserEntityAdapter;
    private final UserJpaRepository userRepository;
    private final UserRelationJpaRepository userRelationJpaRepository;
    @Autowired
    public AuthorDBRepository(AuthorUserEntityAdapter authorUserEntityAdapter, UserJpaRepository userRepository, UserRelationJpaRepository userRelationJpaRepository) {
        this.authorUserEntityAdapter = authorUserEntityAdapter;
        this.userRepository = userRepository;
        this.userRelationJpaRepository = userRelationJpaRepository;
    }

    @Override
    public Author getAuthenticatedAuthor() {
        UserEntity user = CurrentUser.getCurrentUserProfile();
        return authorUserEntityAdapter.convertUserEntityToAuthor(user);
    }

    @Override
    public Optional<Author> findById(AuthorId authorId) {
        return Optional.ofNullable(authorUserEntityAdapter.convertUserEntityToAuthor(userRepository.findById(authorId.getValue()).orElse(null)));
    }

    public void save(Author author){
        UserEntity user = userRepository.findById(author.getId().getValue()).orElseThrow(UserNotFoundException::new);
        user.margeAuthor(author);
        userRepository.save(user);
    }

    @Override
    public void saveAll(List<Author> authors) {
        for (Author author : authors) {
            save(author);
        }
    }

    @Override
    public Optional<Author> findBySlugAndIsDisabledFalse(String slug) {
        Optional<UserEntity> user = userRepository.findBySlugAndIsDisabledFalse(slug);
        return user.map(AuthorUserEntityAdapter::convertUserEntityToAuthor);
    }

    @Override
    public Optional<Author> findBySlugAndIsDisabledFalseAndNotBeingBlocked(String slug) {
        UserEntity authenticatedUser = CurrentUser.getCurrentUserProfile();
        Author userToBeFound = findBySlugAndIsDisabledFalse(slug).orElse(null);
        assert userToBeFound != null;
        Optional<UserRelationEntity> blockRelation = userRelationJpaRepository.findByActorIdAndReceiverIdAndRelation(userToBeFound.getId().getValue(), authenticatedUser.getId(), Relation.blocked);
        if(blockRelation.isPresent()){
            return Optional.empty();
        }
        return Optional.of(userToBeFound);
    }

    @Override
    public Page<Author> search(String query, int page, int size) {
        Page<UserEntity> userEntities = userRepository.searchUser(query, PageRequest.of(page, size));
        return userEntities.map(AuthorUserEntityAdapter::convertUserEntityToAuthor);
    }
}
