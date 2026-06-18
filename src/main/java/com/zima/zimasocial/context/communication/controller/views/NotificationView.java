package com.zima.zimasocial.context.communication.controller.views;

import com.zima.zimasocial.context.social.author.api.view.AuthorView;
import com.zima.zimasocial.context.communication.value.NotificationType;
import com.zima.zimasocial.context.communication.value.TargetCollection;
import lombok.*;

import java.time.OffsetDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class NotificationView {
    private Long id;
    private String content;
    private String url;
    private NotificationType type;
    private OffsetDateTime createdAt;
    private TargetCollection targetCollection;
    private Long targetId;
    private Long postId;
    private AuthorView actor;
}
