package com.zimaberlin.zimasocial.context.communication.chat.infastructure.datasource.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatRoomParticipantId implements Serializable {
    @Column(name = "chat_room_id")
    private UUID chatRoomId;
    @Column(name = "participant_id")
    private Long participantId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoomParticipantId that = (ChatRoomParticipantId) o;
        return Objects.equals(chatRoomId, that.chatRoomId) && Objects.equals(participantId, that.participantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatRoomId, participantId);
    }
}
