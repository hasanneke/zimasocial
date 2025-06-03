package com.zimaberlin.zimasocial.utility;

import com.zimaberlin.zimasocial.entity.userRelation.Relation;
import com.zimaberlin.zimasocial.entity.userRelation.UserRelationEntity;
import com.zimaberlin.zimasocial.repository.UserRelationRepository;
import com.zimaberlin.zimasocial.views.user.DetailedUserView;
import com.zimaberlin.zimasocial.views.user.UserView;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserViewFactory {
    private final UserRelationRepository userRelationRepository;
    @Autowired
    public UserViewFactory(UserRelationRepository userRelationRepository) {
        this.userRelationRepository = userRelationRepository;
    }

    public UserView populated(UserEntity entity)  {
        Optional<UserRelationEntity> followRelation =
                userRelationRepository
                .findByActorAndReceiverAndRelation(CurrentUser.getCurrentUserProfile(), entity, Relation.followed);
        Optional<UserRelationEntity> blockRelation =
                userRelationRepository.findByActorAndReceiverAndRelation(CurrentUser.getCurrentUserProfile(), entity, Relation.blocked);
        UserView userView = new UserView();
        userView.setId(entity.getId());
        userView.setSlug(entity.getSlug());
        userView.setName(entity.getName());
        userView.setFamilyName(entity.getFamilyName());
        userView.setAvatarUrl(entity.getAvatarFileName());
        userView.setBio(entity.getBio());
        userView.setFollowerCount(entity.getFollowersCount());
        userView.setFollowingCount(entity.getFollowingCount());
        userView.setFollowed(followRelation.isPresent());
        userView.setIsPrivate(entity.isPrivate());
        userView.setIsBlocked(blockRelation.isPresent());
        userView.addLinks();
        return userView;
    }
    public List<UserView> populated(List<UserEntity> entities)  {
        return entities.stream().map(this::populated).toList();
    }

    public DetailedUserView populateDetailed(UserEntity entity)  {
        UserView userView = populated(entity);
        DetailedUserView detailedUserView = new DetailedUserView();
        detailedUserView.mergeUserView(userView);
        detailedUserView.setEmail(entity.getEmail());
        return detailedUserView;
    }
}
