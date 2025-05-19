package com.zimaberlin.zimasocial.utility;

import com.zimaberlin.zimasocial.entity.userRelation.Relation;
import com.zimaberlin.zimasocial.entity.userRelation.UserRelationEntity;
import com.zimaberlin.zimasocial.repository.UserRelationRepository;
import com.zimaberlin.zimasocial.views.user.UserView;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserMapper {
    private final UserRelationRepository userRelationRepository;
    @Autowired
    public CustomUserMapper(UserRelationRepository userRelationRepository) {
        this.userRelationRepository = userRelationRepository;
    }

    public UserView entityToDomain(UserEntity entity)  {
        Optional<UserRelationEntity> userRelationEntity = userRelationRepository
                .findByInitiatedUserAndReceiverUserAndRelation(CurrentUser.getCurrentUserProfile(), entity, Relation.followed);
        UserView userView = new UserView();
        userView.setId(entity.getId());
        userView.setSlug(entity.getSlug());
        userView.setName(entity.getName());
        userView.setFamilyName(entity.getFamilyName());
        userView.setAvatarUrl(entity.getAvatarUrl());
        userView.setBio(entity.getBio());
        userView.setFollowerCount(entity.getFollowersCount());
        userView.setFollowingCount(entity.getFollowingCount());
        userView.setFollowed(userRelationEntity.isPresent());
        userView.addLinks();
        return userView;
    }
}
