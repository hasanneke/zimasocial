package com.zimaberlin.zimasocial.context.social.infastructure.collection;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.authorrelation.FollowRequest;
import com.zimaberlin.zimasocial.entity.FollowRequestEntity;
import org.springframework.stereotype.Component;

@Component
public class FollowRequestDBAdapter {
    public FollowRequest convertFollowRequestEntityToFollowRequest(FollowRequestEntity followRequestEntity) {
        return new FollowRequest(followRequestEntity.getId(), new AuthorId(followRequestEntity.getFollowerId()), new AuthorId(followRequestEntity.getFollowedId()), followRequestEntity.getIsAccepted(), followRequestEntity.getCreatedAt(), followRequestEntity.getUpdatedAt());
    }
}
