package com.zimaberlin.zimasocial.service.notification;

import com.zimaberlin.zimasocial.entity.*;
import com.zimaberlin.zimasocial.events.Listeners.PostEventListener;
import com.zimaberlin.zimasocial.repository.NotificationRepository;
import com.zimaberlin.zimasocial.views.notification.NotificationView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zimaberlin.zimasocial.utility.CurrentUser.getCurrentUserProfile;

@Service
public class NotificationServiceImpl implements NotificationService{
    Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Page<NotificationView> getNotifications(Long userId, Pageable page) {
        Page<NotificationEntity> notificationEntities = notificationRepository.findByReceiverUserIdOrderByCreatedAt(userId, page);
        return notificationEntities.map((e)->NotificationView.builder()
                .url(e.getUrl())
                .type(e.getType())
                .content(e.getContent())
                .targetId(e.getTargetId())
                .targetCollection(e.getTargetCollection())
                .postId(e.getPost().getId())
                .createdAt(e.getCreatedAt())
                .build());
    }

    @Override
    public void sendPostLikedNotification(LikeEntity like) {
        logger.info(String.format("Post %d liked", like.getPost().getId()));
        UserEntity liker = getCurrentUserProfile();
        String message = liker.getName() + " paylaşımını beğendi";

        NotificationEntity notification = new NotificationEntity();
        notification.setPost(like.getPost());
        notification.setContent(message);
        notification.setSenderUser(liker);
        notification.setReceiverUser(like.getPost().getUser());

        notification.setTargetCollection(TargetCollection.post);
        notification.setTargetId(like.getPost().getId());
        notification.setPost(like.getPost());

        notificationRepository.save(notification);
    }

    @Override
    public void sendPostCommentedNotification(CommentEntity comment) {
        logger.info(String.format("Post %d commented", comment.getPost().getId()));
        UserEntity commenter = getCurrentUserProfile();
        UserEntity postOwner = comment.getPost().getUser();
        String message = commenter.getName() + " paylaşımına yorum yaptı";
        NotificationEntity notificationEntity = new NotificationEntity();

        notificationEntity.setType(NotificationType.POST_COMMENTED);
        notificationEntity.setSenderUser(commenter);
        notificationEntity.setReceiverUser(postOwner);
        notificationEntity.setTargetCollection(TargetCollection.comment);
        notificationEntity.setTargetId(comment.getId());
        notificationEntity.setPost(comment.getPost());
        notificationEntity.setContent(message);

        notificationRepository.save(notificationEntity);
    }

    @Override
    public void sendCommentLikedNotification(CommentEntity comment) {
        logger.info(String.format("Comment %d liked", comment.getId()));

        UserEntity postOwner = comment.getUser();
        UserEntity liker = getCurrentUserProfile();

        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setType(NotificationType.COMMENT_LIKED);
        notificationEntity.setSenderUser(liker);
        notificationEntity.setReceiverUser(postOwner);

        notificationEntity.setTargetCollection(TargetCollection.comment);
        notificationEntity.setTargetId(comment.getId());
        notificationEntity.setPost(comment.getPost());

        String message = liker.getName() + " yorumunu beğendi";
        notificationEntity.setContent(message);

        notificationRepository.save(notificationEntity);
    }

    @Override
    public void sendCommentRepliedNotification(CommentEntity reply) {
        logger.info(String.format("Comment %d replied", reply.getId()));

        UserEntity commentOwner = reply.getParent().getUser();
        UserEntity commenter = getCurrentUserProfile();

        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setType(NotificationType.POST_COMMENTED);
        notificationEntity.setSenderUser(commenter);
        notificationEntity.setReceiverUser(commentOwner);

        notificationEntity.setTargetCollection(TargetCollection.comment);
        notificationEntity.setTargetId(reply.getId());
        notificationEntity.setPost(reply.getPost());

        String message = commenter.getName() + "yorumuna yanıt verdi";

        notificationEntity.setContent(message);
        notificationRepository.save(notificationEntity);
    }
}
