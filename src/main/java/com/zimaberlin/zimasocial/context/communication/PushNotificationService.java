package com.zimaberlin.zimasocial.context.communication;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.zimaberlin.zimasocial.context.communication.domain.DeviceToken;
import com.zimaberlin.zimasocial.context.communication.domain.Recipient;
import com.zimaberlin.zimasocial.context.communication.notifications.*;
import com.zimaberlin.zimasocial.context.communication.repository.RecipientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PushNotificationService {
    private final NotificationRepository notificationRepository;
    private final RecipientRepository recipientRepository;
    private final PushNotificationProvider pushNotificationProvider;
    private final RecipientValidator recipientValidator;

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
                default -> throw new RuntimeException();
            };
            try {
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
            }
        }
    }
}
