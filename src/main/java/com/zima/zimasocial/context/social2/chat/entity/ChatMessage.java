package com.zima.zimasocial.context.social2.chat.entity;


import com.zima.zimasocial.context.social2.chat.value.ChatMessageId;
import com.zima.zimasocial.context.social2.chat.value.ChatRoomId;
import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.Assert;

import java.time.OffsetDateTime;

@Table(name = "chat_message")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class ChatMessage {
    public ChatMessage(ChatMessageId id, String content, ChatRoomId chatRoomId, AuthorId senderId) {
        Assert.hasLength(content, "message cannot be empty");
        Assert.notNull(chatRoomId, "chatRoomId cannot be null");
        Assert.notNull(id, "id cannot be null");
        Assert.notNull(senderId, "senderId cannot be null");
        this.id = id;
        this.content = content;
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
    }

    @EmbeddedId
    @AttributeOverride(
            name = "value",
            column = @Column(name = "id", updatable = false)
    )
    private ChatMessageId id;
    @Column(name = "content")
    private String content;
    @Embedded
    private ChatRoomId chatRoomId;
    @AttributeOverride(
            name = "value",
            column = @Column(name = "sender_id", updatable = false)
    )
    private AuthorId senderId;
    @Column(name = "created_at")
    @CreationTimestamp
    private OffsetDateTime sentAt;
}
