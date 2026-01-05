package com.zima.zimasocial.context.social.chat.repository;

import com.zima.zimasocial.context.social.chat.entity.ChatMessage;
import com.zima.zimasocial.context.social.chat.entity.ChatMessageId;
import com.zima.zimasocial.context.social.chat.entity.ChatRoomId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ChatMessageRepository {
    ChatMessageId nextId();
    void save(ChatMessage chatMessage);
    Page<ChatMessage> findAllByChatRoomIdOrderBySentAtDesc(ChatRoomId chatRoomId, PageRequest pageRequest);
    ChatMessage findById(ChatMessageId lastMessageId);
}
