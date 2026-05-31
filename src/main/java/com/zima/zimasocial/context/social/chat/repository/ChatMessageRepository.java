package com.zima.zimasocial.context.social.chat.repository;

import com.zima.zimasocial.context.social.chat.value.ChatMessageId;
import com.zima.zimasocial.context.social.chat.value.ChatRoomId;
import com.zima.zimasocial.context.social.chat.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface ChatMessageRepository extends JpaRepository<ChatMessage, ChatMessageId> {
    Page<ChatMessage> findByChatRoomIdOrderBySentAtDesc(ChatRoomId chatRoomId, Pageable request);
}
