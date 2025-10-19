package com.zimaberlin.zimasocial.utility;

import com.zimaberlin.zimasocial.entity.userRelation.Relation;
import com.zimaberlin.zimasocial.entity.userRelation.UserRelationEntity;
import com.zimaberlin.zimasocial.repository.UserRelationJpaRepository;
import com.zimaberlin.zimasocial.context.social.api.author.DetailedAuthorView;
import com.zimaberlin.zimasocial.context.social.api.author.AuthorView;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserViewFactory {
    private final UserRelationJpaRepository userRelationJpaRepository;
    @Autowired
    public UserViewFactory(UserRelationJpaRepository userRelationJpaRepository) {
        this.userRelationJpaRepository = userRelationJpaRepository;
    }

    public AuthorView populated(UserEntity entity)  {
        Optional<UserRelationEntity> followingRelation =
                userRelationJpaRepository
                .findByActorIdAndReceiverIdAndRelation(CurrentUser.getCurrentUserProfile().getId(), entity.getId(), Relation.followed);
        Optional<UserRelationEntity> followMeRelation =
                userRelationJpaRepository
                        .findByActorIdAndReceiverIdAndRelation(entity.getId(), CurrentUser.getCurrentUserProfile().getId(), Relation.followed);
        Optional<UserRelationEntity> blockRelation =
                userRelationJpaRepository.findByActorIdAndReceiverIdAndRelation(CurrentUser.getCurrentUserProfile().getId(), entity.getId(), Relation.blocked);
        AuthorView authorView = new AuthorView();
        authorView.setSlug(entity.getSlug());
        authorView.setName(entity.getName());
        authorView.setFamilyName(entity.getFamilyName());
        authorView.setAvatarUrl(entity.getAvatarFileName());
        authorView.setBio(entity.getBio());
        authorView.setFollowerCount(entity.getFollowersCount());
        authorView.setFollowingCount(entity.getFollowingCount());
        authorView.setFollowed(followingRelation.isPresent());
        authorView.setFollowingMe(followMeRelation.isPresent());
        authorView.setIsPrivate(entity.isPrivate());
        authorView.setIsBlocked(blockRelation.isPresent());
        authorView.addLinks();
        return authorView;
    }
    public List<AuthorView> populated(List<UserEntity> entities)  {
        return entities.stream().map(this::populated).toList();
    }

    public DetailedAuthorView populateDetailed(UserEntity entity)  {
        AuthorView authorView = populated(entity);
        DetailedAuthorView detailedAuthorView = new DetailedAuthorView();
        detailedAuthorView.mergeAuthorView(authorView);
        detailedAuthorView.setEmail(entity.getEmail());
        return detailedAuthorView;
    }
}
