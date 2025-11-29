package com.zimaberlin.zimasocial.context.communication.application;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.zimaberlin.zimasocial.context.communication.domain.repository.NotificationRepository;
import com.zimaberlin.zimasocial.context.communication.domain.service.RecipientValidator;
import com.zimaberlin.zimasocial.context.communication.domain.entity.*;
import com.zimaberlin.zimasocial.context.communication.domain.repository.RecipientRepository;
import com.zimaberlin.zimasocial.context.communication.domain.value.DeviceToken;
import lombok.RequiredArgsConstructor;
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
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Transactional
    public void startPushing() {
        List<Notification> notificationList = notificationRepository.findAllByIsPushedFalse();
        for (Notification notification : notificationList) {
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

    public void push(Notification notification, List<DeviceToken> deviceTokens) {
        Assert.notNull(notification, "Notification cannot be null");
        Recipient recipient = recipientRepository.findByRecipientId(notification.getRecipientId()).orElse(null);
        if (recipient == null) return;
        for (DeviceToken deviceToken : deviceTokens) {
            Recipient actor = recipientRepository.findByRecipientId(notification.getActorId()).orElse(null);
            if (actor == null) return;
            PushNotification pushNotification = switch (notification) {
                case AuthorFollowedNotification authorFollowedNotification ->
                        new PushNotification("@%s seni takip etmeye başladı".formatted(actor.getSlug()), deviceToken.getToken());
                case AuthorFollowRequestSentNotification authorFollowRequestSentNotification ->
                        new PushNotification("@%s takip isteği gönderdi".formatted(actor.getSlug()), deviceToken.getToken());
                case CommentLikedNotification commentLikedNotification ->
                        new PushNotification("@%s yorumunu beğendi".formatted(actor.getSlug()), deviceToken.getToken());
                case CommentRepliedNotification commentRepliedNotification ->
                        new PushNotification("@%s yorumuna yanıt verdi".formatted(actor.getSlug()), deviceToken.getToken());
                case PostCommentedNotification postCommentedNotification ->
                        new PushNotification("@%s paylaşımına yanıt verdi".formatted(actor.getSlug()), deviceToken.getToken());
                case PostLikedNotification postLikedNotification ->
                        new PushNotification("@%s paylaşımını beğendi".formatted(actor.getSlug()), deviceToken.getToken());
                case AuthorFollowRequestAcceptedNotification authorFollowRequestAcceptedNotification ->
                        new PushNotification("@%s takip isteğini kabul etti".formatted(actor.getSlug()), deviceToken.getToken());
                case ChatMessageSentNotification chatMessageSentNotification ->
                        new PushNotification("@%s bir mesaj gönderdi: %s".formatted(actor.getSlug(), chatMessageSentNotification.getMessage()), deviceToken.getToken());
                case SimpleNotification simpleNotification -> new PushNotification(simpleNotification.getMessage(), deviceToken.getToken());
            };
            try {
                pushNotificationProvider.push(pushNotification);
                notification.push();
                notificationRepository.save(notification);
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
