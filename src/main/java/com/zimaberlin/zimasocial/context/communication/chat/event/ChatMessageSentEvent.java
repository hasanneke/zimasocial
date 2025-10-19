package com.zimaberlin.zimasocial.context.communication.chat.event;

import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatMessage;
import com.zimaberlin.zimasocial.context.communication.domain.Recipient;

public record ChatMessageSentEvent (Recipient sender, Recipient receiver, ChatMessage message){ }
