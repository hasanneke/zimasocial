package com.zima.zimasocial.context.social2.domain.event;

import com.zima.zimasocial.context.social2.chat.entity.ChatMessage;
import com.zima.zimasocial.context.social2.domain.entity.Author;

public record ChatMessageSentEvent (Author sender, Author receiver, ChatMessage message){ }
