package com.zimaberlin.zimasocial.context.social.infastructure.repository;

import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.infastructure.adapter.AuthorUserEntityAdapter;
import com.zimaberlin.zimasocial.context.social.authorrelation.*;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.entity.userRelation.Relation;
import com.zimaberlin.zimasocial.entity.userRelation.UserRelationEntity;
import com.zimaberlin.zimasocial.repository.UserRelationJpaRepository;
import com.zimaberlin.zimasocial.repository.UserJpaRepository;
import com.zimaberlin.zimasocial.service.users.exception.UserNotFoundException;
import com.zimaberlin.zimasocial.utility.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRelationDBRepository implements AuthorRelationRepository {
    private final UserRelationJpaRepository userRelationJpaRepository;
    private final UserJpaRepository userRepository;
    private final AuthorUserEntityAdapter authorUserEntityAdapter;

    @Autowired
    public AuthorRelationDBRepository(UserRelationJpaRepository userRelationJpaRepository, UserJpaRepository userRepository, AuthorUserEntityAdapter authorUserEntityAdapter) {
        this.userRelationJpaRepository = userRelationJpaRepository;
        this.userRepository = userRepository;
        this.authorUserEntityAdapter = authorUserEntityAdapter;
    }

    @Override
    public Optional<FollowRelation> findFollowRelationBetween(Long followerId, Long followedId) {
        Optional<UserRelationEntity> followRelation = userRelationJpaRepository.findByActorIdAndReceiverIdAndRelation(followerId, followedId, Relation.followed);
        return followRelation.map(userRelationEntity -> new FollowRelation(userRelationEntity.getActorId(), userRelationEntity.getReceiverId()));
    }

    @Override
    public Optional<BlockRelation> findBlockRelationBetween(Long blockerId, Long blockedId) {
        Optional<UserRelationEntity> blockRelation = userRelationJpaRepository.findByActorIdAndReceiverIdAndRelation(blockerId, blockedId, Relation.blocked);
        return blockRelation.map(userRelationEntity -> new BlockRelation(userRelationEntity.getActorId(), userRelationEntity.getReceiverId()));

    }

    @Override
    public Optional<MutedRelation> findMutedRelationBetween(Long muterId, Long mutedId) {
        Optional<UserRelationEntity> blockRelation = userRelationJpaRepository.findByActorIdAndReceiverIdAndRelation(muterId, mutedId, Relation.muted);
        return blockRelation.map(userRelationEntity -> new MutedRelation(userRelationEntity.getActorId(), userRelationEntity.getReceiverId()));
    }

    @Override
    public Page<Author> findFollowers(String slug, int page, int size) {
        UserEntity user = userRepository.findBySlug(slug).orElseThrow(UserNotFoundException::new);
        Page<UserRelationEntity> followedRelations = userRelationJpaRepository.findByReceiverIdAndRelation(user.getId(), Relation.followed, PageRequest.of(page, size));
        List<UserEntity> followers = followedRelations.map(e->
             userRepository.findById(e.getActorId()).orElseThrow(UserNotFoundException::new)).stream().toList();
        return new PageImpl<>(followers.stream().map(authorUserEntityAdapter::convertUserEntityToAuthor).toList(), PageRequest.of(page, size),followedRelations.getTotalElements());
    }

    @Override
    public Page<Author> findFollowings(String slug, int page, int size) {
        UserEntity user = userRepository.findBySlug(slug).orElseThrow(UserNotFoundException::new);
        Page<UserRelationEntity> followingRelations = userRelationJpaRepository.findByActorIdAndRelation(user.getId(), Relation.followed, PageRequest.of(page, size));
        List<UserEntity> followings = followingRelations.map(e->
                userRepository.findById(e.getReceiverId()).orElseThrow(UserNotFoundException::new)).stream().toList();
        return new PageImpl<>(followings.stream().map(authorUserEntityAdapter::convertUserEntityToAuthor).toList(), PageRequest.of(page, size), followingRelations.getTotalElements());
    }

    @Override
    public Page<Author> findBlocks(int page, int size) {
        UserEntity user = CurrentUser.getCurrentUserProfile();
        Page<UserRelationEntity> blockedRelations = userRelationJpaRepository.findByActorIdAndRelation(user.getId(), Relation.blocked, PageRequest.of(page, size));
        List<UserEntity> blockedAuthors = blockedRelations.map(e->
                userRepository.findById(e.getReceiverId()).orElseThrow(UserNotFoundException::new)).stream().toList();
        return new PageImpl<>(blockedAuthors.stream().map(authorUserEntityAdapter::convertUserEntityToAuthor).toList(), PageRequest.of(page, size), blockedRelations.getTotalElements());

    }

    @Override
    public void save(AuthorRelation relation) {
        switch (relation){
            case FollowRelation followRelation -> userRelationJpaRepository.save(UserRelationEntity.builder().actorId(followRelation.getFollowerId()).receiverId(followRelation.getFollowedId()).relation(Relation.followed).build());
            case BlockRelation blockRelation -> userRelationJpaRepository.save(UserRelationEntity.builder().actorId(blockRelation.getBlockerId()).receiverId(blockRelation.getBlockedId()).relation(Relation.blocked).build());
            case MutedRelation mutedRelation -> userRelationJpaRepository.save(UserRelationEntity.builder().actorId(mutedRelation.getMuterId()).receiverId(mutedRelation.getMutedId()).relation(Relation.muted).build());
            default -> throw new IllegalStateException("Unexpected value: " + relation);
        }
    }

    @Override
    public void delete(AuthorRelation relation) {
        switch (relation){
            case FollowRelation followRelation -> userRelationJpaRepository.deleteByActorIdAndReceiverIdAndRelation(followRelation.getFollowerId(), followRelation.getFollowedId(), Relation.followed);
            case BlockRelation blockRelation -> userRelationJpaRepository.deleteByActorIdAndReceiverIdAndRelation(blockRelation.getBlockerId(), blockRelation.getBlockedId(), Relation.blocked);
            case MutedRelation mutedRelation -> userRelationJpaRepository.deleteByActorIdAndReceiverIdAndRelation(mutedRelation.getMuterId(), mutedRelation.getMutedId(), Relation.muted);
            default -> throw new IllegalStateException("Unexpected value: " + relation);
        }
    }
}
