package com.zimaberlin.zimasocial.context.communication;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.zimaberlin.zimasocial.context.communication.domain.DeviceToken;
import com.zimaberlin.zimasocial.context.communication.domain.Recipient;
import com.zimaberlin.zimasocial.context.communication.notifications.*;
import com.zimaberlin.zimasocial.context.communication.repository.RecipientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PushNotificationService {
    private final PushNotificationProvider pushNotificationProvider;
    private final RecipientRepository recipientRepository;
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
                        new PushNotification("@%s bir mesaj gönderdi".formatted(actor.getSlug()), deviceToken.getToken());
                case SimpleNotification simpleNotification -> throw new IllegalArgumentException();
                case null -> throw new IllegalArgumentException();
                default -> throw new IllegalStateException("Unexpected value: " + notification);
            };

            pushNotificationProvider.push(pushNotification);
        }
    }
}
