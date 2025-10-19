package com.zimaberlin.zimasocial.context.communication.chat.repository;

import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatMessage;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatMessageId;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoomId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ChatMessageRepository {
    ChatMessageId nextId();
    void save(ChatMessage chatMessage);
    Page<ChatMessage> findAllByChatRoomIdOrderBySentAtDesc(ChatRoomId chatRoomId, PageRequest pageRequest);
}
