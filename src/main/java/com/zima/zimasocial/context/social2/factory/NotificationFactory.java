package com.zima.zimasocial.context.social2.factory;

import com.zima.zimasocial.context.social2.domain.entity.Like;
import com.zima.zimasocial.entity.NotificationEntity;
import com.zima.zimasocial.entity.NotificationType;
import com.zima.zimasocial.entity.TargetCollection;
import org.springframework.stereotype.Component;

@Component
public class NotificationFactory {
    public NotificationEntity buildLikeNotification(Like like, Long recipientId) {
        return NotificationEntity.builder()
                .postId(like.getPostId())
                .type(NotificationType.POST_LIKED)
                .targetId(like.getPostId())
                .targetCollection(TargetCollection.post)
                .receiverUserId(recipientId)
                .isPushed(false)
                .actorId(like.getAuthorId().getValue())
                .build();
    }
}
