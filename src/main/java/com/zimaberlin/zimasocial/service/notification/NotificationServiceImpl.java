package com.zimaberlin.zimasocial.service.notification;

import com.zimaberlin.zimasocial.entity.*;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.repository.NotificationRepository;
import com.zimaberlin.zimasocial.utility.CustomUserMapper;
import com.zimaberlin.zimasocial.views.notification.NotificationView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.zimaberlin.zimasocial.utility.CurrentUser.getCurrentUserProfile;

@Service
public class NotificationServiceImpl implements NotificationService{
    Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private NotificationRepository notificationRepository;
    private CustomUserMapper userMapper;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, CustomUserMapper userMapper) {
        this.notificationRepository = notificationRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Page<NotificationView> getNotifications(Long userId, Pageable page) {
        Page<NotificationEntity> notificationEntities = notificationRepository.findByReceiverUserIdOrderByCreatedAt(userId, page);
        return notificationEntities.map((e)->NotificationView.builder()
                .id(e.getId())
                .url(e.getUrl())
                .type(e.getType())
                .content(e.getContent())
                .targetId(e.getTargetId())
                .targetCollection(e.getTargetCollection())
                .postId(e.getPost() != null ? e.getPost().getId() : null)
                .createdAt(e.getCreatedAt())
                .actor(userMapper.entityToDomain(e.getSenderUser()))
                .build());
    }

    @Override
    public void sendPostLikedNotification(LikeEntity like) {
        logger.info(String.format("Post %d liked", like.getPost().getId()));
        UserEntity liker = getCurrentUserProfile();
        String message = liker.getName() + " paylaşımını beğendi";

        NotificationEntity notification = NotificationEntity.builder()
                .type(NotificationType.POST_LIKED)
                .senderUser(liker)
                .receiverUser(like.getPost().getUser())
                .targetCollection(TargetCollection.post)
                .targetId(like.getPost().getId())
                .post(like.getPost())
                .content(message)
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public void sendPostCommentedNotification(CommentEntity comment) {
        logger.info(String.format("Post %d commented", comment.getPost().getId()));
        UserEntity commenter = getCurrentUserProfile();
        UserEntity postOwner = comment.getPost().getUser();
        String message = commenter.getName() + " paylaşımına yorum yaptı";

        NotificationEntity notificationEntity = NotificationEntity.builder()
                .type(NotificationType.POST_COMMENTED)
                .senderUser(commenter)
                .receiverUser(postOwner)
                .targetCollection(TargetCollection.comment)
                .targetId(comment.getId())
                .post(comment.getPost())
                .content(message)
                .build();
        notificationRepository.save(notificationEntity);
    }

    @Override
    public void sendCommentLikedNotification(CommentEntity comment) {
        logger.info(String.format("Comment %d liked", comment.getId()));
        UserEntity postOwner = comment.getUser();
        UserEntity liker = getCurrentUserProfile();
        String message = liker.getName() + " yorumunu beğendi";
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .type(NotificationType.COMMENT_LIKED)
                .senderUser(liker)
                .receiverUser(postOwner)
                .targetCollection(TargetCollection.comment)
                .targetId(comment.getId())
                .post(comment.getPost())
                .content(message)
                .build();
        notificationRepository.save(notificationEntity);
    }

    @Override
    public void sendCommentRepliedNotification(CommentEntity reply) {
        logger.info(String.format("Comment %d replied", reply.getId()));

        UserEntity commentOwner = reply.getParent().getUser();
        UserEntity commenter = getCurrentUserProfile();
        String message = commenter.getName() + "yorumuna yanıt verdi";

        NotificationEntity notificationEntity = NotificationEntity.builder()
                .type(NotificationType.POST_COMMENTED)
                .senderUser(commenter)
                .receiverUser(commentOwner)
                .targetCollection(TargetCollection.comment)
                .targetId(reply.getId())
                .post(reply.getPost())
                .content(message)
                .build();

        notificationRepository.save(notificationEntity);
    }
}
