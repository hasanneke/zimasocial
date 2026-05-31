package com.zima.zimasocial.context.communication.domain.entity;

import com.zima.zimasocial.context.social.chat.value.ChatRoomId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public final class ChatMessageSentNotification extends Notification{
    private ChatRoomId chatRoomId;
}
