package com.zimaberlin.zimasocial.context.communication;

import com.zimaberlin.zimasocial.context.communication.domain.DeviceToken;
import com.zimaberlin.zimasocial.context.communication.domain.Recipient;
import com.zimaberlin.zimasocial.context.communication.notifications.*;
import com.zimaberlin.zimasocial.context.communication.repository.RecipientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final RecipientRepository recipientRepository;
    @Autowired
    public NotificationService(NotificationRepository notificationRepository, RecipientRepository recipientRepository) {
        this.notificationRepository = notificationRepository;
        this.recipientRepository = recipientRepository;
    }

    public void sendPostLikedNotification(PostLikedNotification postLikedNotification) {
        Optional<PostLikedNotification> checkNotifiedBefore = notificationRepository.getPostLikedNotification(postLikedNotification.getActorId(), postLikedNotification.getPostId());
        if(checkNotifiedBefore.isEmpty()){
            notificationRepository.save(postLikedNotification);
        }
    }
    public void sendPostCommentedNotification(PostCommentedNotification postCommentedNotification) {
        notificationRepository.save(postCommentedNotification);
    }
    public void sendCommentLikedNotification(CommentLikedNotification commentLikedNotification) {
        Optional<CommentLikedNotification> checkNotifiedBefore = notificationRepository.getCommentLikedNotification(commentLikedNotification.getActorId(), commentLikedNotification.getCommentId());
        if(checkNotifiedBefore.isEmpty()){
            notificationRepository.save(commentLikedNotification);
        }
    }
    public void sendCommentRepliedNotification(CommentRepliedNotification commentRepliedNotification) {
        notificationRepository.save(commentRepliedNotification);
    }

    public void sendAuthorFollowedYouNotification(AuthorFollowedNotification authorFollowedNotification) {
        notificationRepository.save(authorFollowedNotification);
    }

    public void sendAuthorFollowedRequestAcceptedNotification(AuthorFollowRequestAcceptedNotification authorFollowRequestAcceptedNotification) {
        notificationRepository.save(authorFollowRequestAcceptedNotification);
    }

    public void sendAuthorSentFollowRequestNotification(AuthorFollowRequestSentNotification authorFollowRequestSentNotification) {
        notificationRepository.save(authorFollowRequestSentNotification);
    }
    public void sendChatMessageSentNotification(ChatMessageSentNotification chatMessageSentNotification) {
        notificationRepository.save(chatMessageSentNotification);
    }
    @Transactional
    public void saveDeviceToken(String token) {
        Recipient recipient = recipientRepository.getAuthenticatedRecipient();
        recipient.addToken(new DeviceToken(token, recipient.getRecipientId()));
        recipientRepository.save(recipient);
    }


}
