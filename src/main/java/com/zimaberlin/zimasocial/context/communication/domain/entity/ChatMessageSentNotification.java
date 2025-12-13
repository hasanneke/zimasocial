package com.zimaberlin.zimasocial.context.communication.domain.entity;

import com.zimaberlin.zimasocial.context.social.chat.entity.ChatRoomId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public final class ChatMessageSentNotification extends Notification{
    private ChatRoomId chatRoomId;
}
