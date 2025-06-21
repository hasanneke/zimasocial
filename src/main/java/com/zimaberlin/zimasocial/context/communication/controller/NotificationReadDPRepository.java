package com.zimaberlin.zimasocial.context.communication.controller;

import com.zimaberlin.zimasocial.context.communication.NotificationDBRepository;
import com.zimaberlin.zimasocial.context.social.api.author.AuthorAuthorViewAdapter;
import com.zimaberlin.zimasocial.context.social.infastructure.adapter.AuthorUserEntityAdapter;
import com.zimaberlin.zimasocial.context.social.post.PostRepository;
import com.zimaberlin.zimasocial.entity.NotificationEntity;
import com.zimaberlin.zimasocial.repository.NotificationJpaRepository;
import com.zimaberlin.zimasocial.views.notification.NotificationView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class NotificationReadDPRepository implements NotificationReadRepository{
    private final NotificationJpaRepository notificationJpaRepository;
    private final AuthorAuthorViewAdapter authorViewAdapter;
    private final AuthorUserEntityAdapter authorUserEntityAdapter;
    @Autowired
    public NotificationReadDPRepository(NotificationJpaRepository notificationJpaRepository, AuthorAuthorViewAdapter authorViewAdapter, AuthorUserEntityAdapter authorUserEntityAdapter) {
        this.notificationJpaRepository = notificationJpaRepository;
        this.authorViewAdapter = authorViewAdapter;
        this.authorUserEntityAdapter = authorUserEntityAdapter;
    }

    @Override
    public Page<NotificationView> findByRecipientId(Long recipientId, Pageable pageable) {
        Page<NotificationEntity> notificationEntities = notificationJpaRepository.findByReceiverUserIdOrderByCreatedAtDesc(recipientId, pageable);
        return notificationEntities.map((e)->NotificationView.builder()
                .id(e.getId())
                .url(e.getUrl())
                .type(e.getType())
                .content(e.getContent())
                .targetId(e.getTargetId())
                .targetCollection(e.getTargetCollection())
                .postId(e.getPostId())
                .createdAt(e.getCreatedAt())
                .actor(authorViewAdapter.authorViewFromAuthor(authorUserEntityAdapter.convertUserEntityToAuthor(e.getActor())))
                .build());
    }
}
