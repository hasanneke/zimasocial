package com.zima.zimasocial.context.communication.controller;

import com.zima.zimasocial.context.social.api.author.AuthorAuthorViewAdapter;
import com.zima.zimasocial.context.social.infastructure.adapter.AuthorUserEntityAdapter;
import com.zima.zimasocial.entity.NotificationEntity;
import com.zima.zimasocial.entity.NotificationType;
import com.zima.zimasocial.repository.NotificationJpaRepository;
import com.zima.zimasocial.views.notification.NotificationView;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class NotificationReadDBQuery implements NotificationQuery {
    private final NotificationJpaRepository notificationJpaRepository;
    private final AuthorAuthorViewAdapter authorViewAdapter;
    private final AuthorUserEntityAdapter authorUserEntityAdapter;
    private final EntityManager entityManager;
    @Autowired
    public NotificationReadDBQuery(NotificationJpaRepository notificationJpaRepository, AuthorAuthorViewAdapter authorViewAdapter, AuthorUserEntityAdapter authorUserEntityAdapter, EntityManager entityManager) {
        this.notificationJpaRepository = notificationJpaRepository;
        this.authorViewAdapter = authorViewAdapter;
        this.authorUserEntityAdapter = authorUserEntityAdapter;
        this.entityManager = entityManager;
    }

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
                .actor(authorViewAdapter.authorViewFromAuthor(authorUserEntityAdapter.convertUserEntityToAuthor(e.getActor()), false))
                .build());
    }
}
