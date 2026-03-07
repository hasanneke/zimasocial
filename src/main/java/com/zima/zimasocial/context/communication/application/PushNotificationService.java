package com.zima.zimasocial.context.communication.application;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.zima.zimasocial.context.communication.domain.SubscriberSearch;
import com.zima.zimasocial.context.communication.domain.entity.*;
import com.zima.zimasocial.context.communication.domain.repository.NotificationRepository;
import com.zima.zimasocial.context.communication.domain.repository.RecipientRepository;
import com.zima.zimasocial.context.communication.domain.service.RecipientValidator;
import com.zima.zimasocial.context.communication.domain.value.DeviceToken;
import com.zima.zimasocial.entity.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class PushNotificationService {
    private final NotificationRepository notificationRepository;
    private final RecipientRepository recipientRepository;
    private final PushNotificationProvider pushNotificationProvider;
    private final RecipientValidator recipientValidator;
    private final SubscriberSearch subscriberSearch;

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Value("${webLink}")
    private String webLink;
    @Transactional
    public void startPushing() {
        List<Notification> notificationList = notificationRepository.findAllByIsPushedFalse();
        for (Notification notification : notificationList) {
            if(notification instanceof PostSharedNotification){
                List<Recipient> subscribers = subscriberSearch.findSubscribers(notification.getActorId());
                for (Recipient subscriber : subscribers) {
                    notification.setRecipientId(subscriber.getRecipientId());
                    push(notification, subscriber.getDeviceTokens().stream().toList());
                }
            }else{
                Optional<Recipient> recipient = recipientRepository.findByRecipientId(notification.getRecipientId());
                if (recipient.isPresent()) {
                    boolean eligibleForPush = recipientValidator.canBePushed(recipient.get().getRecipientId());
                    if (eligibleForPush) {
                        Recipient getRecipient = recipient.get();
                        Set<DeviceToken> deviceTokens = getRecipient.getDeviceTokens();
                        push(notification, deviceTokens.stream().toList());
                    }
                }
            }

        }
    }

    public void push(Notification notification, List<DeviceToken> deviceTokens) {
        Assert.notNull(notification, "Notification cannot be null");
        Recipient recipient = recipientRepository.findByRecipientId(notification.getRecipientId()).orElse(null);
        if (recipient == null) return;
        for (DeviceToken deviceToken : deviceTokens) {
            Recipient actor = recipientRepository.findByRecipientId(notification.getActorId()).orElse(null);
            if (actor == null) return;
            PushNotification pushNotification = switch (notification) {
                case AuthorFollowedNotification authorFollowedNotification -> {
                    String linkToSource =  webLink + "/users/" + actor.getSlug();
                    yield PushNotification.builder()
                            .message("@%s seni takip etmeye başladı".formatted(actor.getSlug()))
                            .deviceToken(deviceToken.getToken())
                            .linkToSource(linkToSource)
                            .build();
                }
                case AuthorFollowRequestSentNotification authorFollowRequestSentNotification -> {
                    String linkToSource =  webLink + "/users/" + actor.getSlug();
                    yield PushNotification.builder()
                            .message("@%s takip isteği gönderdi".formatted(actor.getSlug()))
                            .deviceToken(deviceToken.getToken())
                            .linkToSource(linkToSource)
                            .build();
                }
                case CommentLikedNotification commentLikedNotification -> {
                    String linkToSource =  webLink + "/posts/" + commentLikedNotification.getPostId();
                    yield PushNotification.builder()
                            .message("@%s yorumunu beğendi".formatted(actor.getSlug()))
                            .deviceToken(deviceToken.getToken())
                            .linkToSource(linkToSource)
                            .build();
                }
                case CommentRepliedNotification commentRepliedNotification -> {
                    String linkToSource =  webLink + "/posts/" + commentRepliedNotification.getPostId();
                    yield PushNotification.builder()
                            .message("@%s yorumuna yanıt verdi".formatted(actor.getSlug()))
                            .deviceToken( deviceToken.getToken())
                            .linkToSource(linkToSource)
                            .build();
                }
                case PostCommentedNotification postCommentedNotification -> {
                    String linkToSource =  webLink + "/posts/" + postCommentedNotification.getPostId();
                    yield PushNotification.builder()
                            .message(("@%s paylaşımına yanıt verdi".formatted(actor.getSlug())))
                            .deviceToken(deviceToken.getToken())
                            .linkToSource(linkToSource)
                            .build();
                }
                case PostLikedNotification postLikedNotification -> {
                    String linkToSource =  webLink + "/posts/" + postLikedNotification.getPostId();
                    yield PushNotification.builder()
                            .message("@%s paylaşımını beğendi".formatted(actor.getSlug()))
                            .deviceToken(deviceToken.getToken())
                            .linkToSource(linkToSource)
                            .build();
                }
                case AuthorFollowRequestAcceptedNotification authorFollowRequestAcceptedNotification -> {
                    String linkToSource =  webLink + "/users/" + actor.getSlug();
                    yield PushNotification.builder()
                            .message("@%s takip isteğini kabul etti".formatted(actor.getSlug()))
                            .deviceToken(deviceToken.getToken())
                            .linkToSource(linkToSource)
                            .build();
                }
                case ChatMessageSentNotification chatMessageSentNotification -> {
                    String linkToApp = webLink + "/chats/" + actor.getSlug();
                    yield PushNotification.builder()
                            .message("@%s bir mesaj gönderdi: %s".formatted(actor.getSlug(), chatMessageSentNotification.getMessage()))
                            .deviceToken(deviceToken.getToken())
                            .linkToSource(linkToApp)
                            .type("chat")
                            .resourceId(chatMessageSentNotification.getChatRoomId().value().toString())
                            .build();
                }
                case PostSharedNotification postSharedNotification -> {
                    String linkToApp = webLink + "/posts/" + postSharedNotification.getPostId();

                    String message;
                    if(postSharedNotification.getType().equals(MediaType.any)){
                        message = "@%s yeni bir paylaşım yaptı: %s".formatted(
                                actor.getSlug(),
                                postSharedNotification.getContent());
                    }else{
                        message = "@%s yeni bir %s paylaşımı yaptı: %s".formatted(
                                actor.getSlug(),
                                postSharedNotification.getType().getTitle(),
                                postSharedNotification.getContent());
                    }
                    yield PushNotification.builder()
                            .message(message)
                            .deviceToken(deviceToken.getToken())
                            .linkToSource(linkToApp)
                            .type("post")
                            .resourceId(postSharedNotification.getPostId().toString())
                            .build();
                }
                case SimpleNotification simpleNotification ->
                        PushNotification.builder().message(simpleNotification.getMessage()).deviceToken(deviceToken.getToken()).build();
            };
            try {
                notification.push();
                notificationRepository.save(notification);
                pushNotificationProvider.push(pushNotification);
            } catch (FirebaseMessagingException e) {
                switch (e.getMessagingErrorCode()) {
                    case INVALID_ARGUMENT -> {
                        notification.push();
                        notificationRepository.save(notification);
                    }
                    case UNREGISTERED -> {
                        recipient.removeToken(deviceToken);
                        recipientRepository.save(recipient);
                    }
                }
            } catch (Exception e){
                logger.warning("Push Notification couldn't be sent %s".formatted(e.getMessage()));
            }
        }
    }
}
