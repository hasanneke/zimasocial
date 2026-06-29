package com.zima.zimasocial.context.communication.domain.entity;

import com.zima.zimasocial.context.social.post.value.PostId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public final class AuthorAddedYourMediaToTheirListNotification extends Notification{
    private PostId postIdReferencedFrom;
}
