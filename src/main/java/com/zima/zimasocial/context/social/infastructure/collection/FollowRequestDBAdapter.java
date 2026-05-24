package com.zima.zimasocial.context.social.infastructure.collection;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.context.social.authorrelation.entity.FollowRequest;
import com.zima.zimasocial.entity.FollowRequestEntity;
import org.springframework.stereotype.Component;

@Component
public class FollowRequestDBAdapter {
    public FollowRequest convertFollowRequestEntityToFollowRequest(FollowRequestEntity followRequestEntity) {
        return new FollowRequest(followRequestEntity.getId(), new AuthorDomainId(followRequestEntity.getFollowerId()), new AuthorDomainId(followRequestEntity.getFollowedId()), followRequestEntity.getIsAccepted(), followRequestEntity.getCreatedAt(), followRequestEntity.getUpdatedAt());
    }
}
