package com.zimaberlin.zimasocial.context.communication.chat.infastructure.datasource.entity;

import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatMessage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "chat_message")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class ChatMessageJpaEntity {
    public ChatMessageJpaEntity(ChatMessage chatMessage) {
        this.id = chatMessage.id().value();
        this.content = chatMessage.message();
        this.chatRoomId = chatMessage.chatRoomId().value();
        this.senderId = chatMessage.senderId().getValue();
        this.createdAt = chatMessage.sentAt();
    }
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "content")
    private String content;
    @Column(name = "chat_room_id")
    private UUID chatRoomId;
    @Column(name = "sender_id")
    private Long senderId;
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
}
