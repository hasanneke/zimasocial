package com.zimaberlin.zimasocial.views.notification;

import com.zimaberlin.zimasocial.context.social.api.author.AuthorView;
import com.zimaberlin.zimasocial.entity.NotificationType;
import com.zimaberlin.zimasocial.entity.TargetCollection;
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
