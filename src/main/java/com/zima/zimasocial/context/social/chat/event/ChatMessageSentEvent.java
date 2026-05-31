package com.zima.zimasocial.context.social.chat.event;

import com.zima.zimasocial.context.social.chat.entity.ChatMessage;
import com.zima.zimasocial.context.social.author.entity.Author;

public record ChatMessageSentEvent (Author sender, Author receiver, ChatMessage message){ }
