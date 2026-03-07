package com.zima.zimasocial.context.communication.domain.entity;
import com.zima.zimasocial.entity.MediaType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public final class PostSharedNotification extends Notification {
    private Long postId;
    private String content;
    private MediaType type;
}
