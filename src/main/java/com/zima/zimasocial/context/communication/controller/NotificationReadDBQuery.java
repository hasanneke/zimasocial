package com.zima.zimasocial.context.communication.controller;

import com.zima.zimasocial.context.social2.api.adapter.AuthorViewAdapter;
import com.zima.zimasocial.entity.NotificationEntity;
import com.zima.zimasocial.entity.NotificationType;
import com.zima.zimasocial.repository.NotificationJpaRepository;
import com.zima.zimasocial.views.notification.NotificationView;
import com.zima.zimasocial.views.notification.NotificationView2;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class NotificationReadDBQuery implements NotificationQuery {
    private final NotificationJpaRepository notificationJpaRepository;
    private final AuthorViewAdapter authorViewAdapter;

    @Override
    public Page<NotificationView> findByRecipientId(Long recipientId, Pageable pageable) {
        Set<NotificationType> filteredNotificationsTypes = Set.of(NotificationType.NEW_MESSAGE);
        Page<NotificationEntity> notificationEntities = notificationJpaRepository.findByReceiverUserIdAndTypeNotInOrderByCreatedAtDesc(recipientId, filteredNotificationsTypes, pageable);
        return notificationEntities.map((e)->NotificationView.builder()
                .id(e.getId())
                .url(e.getUrl())
                .type(e.getType())
                .content(e.getContent())
                .targetId(e.getTargetId())
                .targetCollection(e.getTargetCollection())
                .postId(e.getPostId())
                .createdAt(e.getCreatedAt())
                .actor(authorViewAdapter.toRichView(e.getActor()))
                .build());
    }

    @Override
    public Page<NotificationView2> findByRecipientIdV2(Long recipientId, Pageable pageable) {
        Set<NotificationType> filteredNotificationsTypes = Set.of(NotificationType.NEW_MESSAGE);
        return notificationJpaRepository.findAllNotifications(recipientId, filteredNotificationsTypes, pageable);
    }
}
