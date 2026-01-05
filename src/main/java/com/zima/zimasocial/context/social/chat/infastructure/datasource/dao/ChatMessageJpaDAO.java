package com.zima.zimasocial.context.social.chat.infastructure.datasource.dao;

import com.zima.zimasocial.context.social.chat.infastructure.datasource.entity.ChatMessageJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface ChatMessageJpaDAO extends JpaRepository<ChatMessageJpaEntity, UUID> {
    Page<ChatMessageJpaEntity> findByChatRoomIdOrderByCreatedAtDesc(UUID chatRoomId, PageRequest pageRequest);
}
