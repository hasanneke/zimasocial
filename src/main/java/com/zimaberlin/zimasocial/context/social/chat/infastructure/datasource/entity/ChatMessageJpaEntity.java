package com.zimaberlin.zimasocial.context.social.chat.infastructure.datasource.entity;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.chat.entity.ChatMessage;
import com.zimaberlin.zimasocial.context.social.chat.entity.ChatMessageId;
import com.zimaberlin.zimasocial.context.social.chat.entity.ChatRoomId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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
    private OffsetDateTime createdAt;

    public ChatMessage toDomain() {
        return new ChatMessage(new ChatMessageId(id), new ChatRoomId(chatRoomId), new AuthorId(senderId), content, createdAt);
    }
}
