package com.zimaberlin.zimasocial.context.social.chat.infastructure.datasource.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "chat_room_participant")
public class ChatRoomParticipantJpaEntity {
    @EmbeddedId
    private ChatRoomParticipantId id;

    @JoinColumn(name = "chat_room_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoomJpaEntity chatRoom;
}
