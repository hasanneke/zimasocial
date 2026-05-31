package com.zima.zimasocial.context.social.chat.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
public class ChatMessageId {
    @Column(name = "chat_message_id")
    private UUID value;
    protected ChatMessageId() {}
    public ChatMessageId(UUID value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessageId that = (ChatMessageId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
