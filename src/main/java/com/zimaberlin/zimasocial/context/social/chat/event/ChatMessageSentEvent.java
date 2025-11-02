package com.zimaberlin.zimasocial.context.social.chat.event;

import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.chat.entity.ChatMessage;

public record ChatMessageSentEvent (Author sender, Author receiver, ChatMessage message){ }
