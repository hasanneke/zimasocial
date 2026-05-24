package com.zima.zimasocial.context.social.chat.event;

import com.zima.zimasocial.context.social.author.entity.AuthorDomain;
import com.zima.zimasocial.context.social.chat.entity.ChatMessage;

public record ChatMessageSentEvent (AuthorDomain sender, AuthorDomain receiver, ChatMessage message){ }
