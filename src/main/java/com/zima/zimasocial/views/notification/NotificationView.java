package com.zima.zimasocial.views.notification;

import com.zima.zimasocial.context.social.author.api.view.AuthorView;
import com.zima.zimasocial.entity.NotificationType;
import com.zima.zimasocial.entity.TargetCollection;
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
