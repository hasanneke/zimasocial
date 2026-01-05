package com.zima.zimasocial.context.social.chat.event;

import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.chat.entity.ChatMessage;

public record ChatMessageSentEvent (Author sender, Author receiver, ChatMessage message){ }
