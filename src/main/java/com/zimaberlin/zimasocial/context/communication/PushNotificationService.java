package com.zimaberlin.zimasocial.context.communication;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MessagingErrorCode;
import com.zimaberlin.zimasocial.context.communication.domain.DeviceToken;
import com.zimaberlin.zimasocial.context.communication.domain.Recipient;
import com.zimaberlin.zimasocial.context.communication.notifications.*;
import com.zimaberlin.zimasocial.context.communication.repository.RecipientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void push(Notification notification, List<DeviceToken> deviceTokens) throws FirebaseMessagingException {
        Recipient recipient = recipientRepository.findByRecipientId(notification.getRecipientId()).orElse(null);
        if(recipient == null) return;
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
                case SimpleNotification simpleNotification -> throw new IllegalArgumentException();
                case null -> throw new IllegalArgumentException();
                default -> throw new IllegalStateException("Unexpected value: " + notification);
            };

            pushNotificationProvider.push(pushNotification);
        }
    }

    @Transactional
    public void startPushing() {
        List<Notification> notificationList = notificationRepository.findAllByIsPushedFalse();
        for (Notification notification : notificationList) {
            Optional<Recipient> recipient = recipientRepository.findByRecipientId(notification.getRecipientId());
            if (recipient.isPresent()){
                try {
                    boolean eligibleForPush = recipientValidator.canBePushed(recipient.get().getRecipientId());
                    if(eligibleForPush){
                        Recipient getRecipient = recipient.get();
                        Set<DeviceToken> deviceTokens = getRecipient.getDeviceTokens();
                        push(notification, deviceTokens.stream().toList());
                    }
                } catch (FirebaseMessagingException e) {
                    if(e.getMessagingErrorCode().equals(MessagingErrorCode.UNREGISTERED)){
                        notification.push();
                    }
                }
                notification.push();
                notificationRepository.save(notification);
            }
        }
    }
}
