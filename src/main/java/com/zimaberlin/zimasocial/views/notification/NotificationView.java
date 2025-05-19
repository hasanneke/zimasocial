package com.zimaberlin.zimasocial.views.notification;
import com.zimaberlin.zimasocial.entity.NotificationType;
import com.zimaberlin.zimasocial.entity.TargetCollection;
import com.zimaberlin.zimasocial.views.user.UserView;
import lombok.*;

import java.time.LocalDateTime;


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
    private LocalDateTime createdAt;
    private TargetCollection targetCollection;
    private Long targetId;
    private Long postId;
    private UserView actor;
}
