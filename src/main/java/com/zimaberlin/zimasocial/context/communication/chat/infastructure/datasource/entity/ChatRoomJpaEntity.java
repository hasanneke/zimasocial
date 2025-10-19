package com.zimaberlin.zimasocial.context.communication.chat.infastructure.datasource.entity;

import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoom;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Table(name = "chat_room")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class ChatRoomJpaEntity {
    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "participant_1", insertable = false, updatable = false)
    private UserEntity participant1;
    @ManyToOne
    @JoinColumn(name = "participant_2", insertable = false, updatable = false)
    private UserEntity participant2;
    @Column(name = "participant_1")
    private Long participant1Id;
    @Column(name = "participant_2")
    private Long participant2Id;

    public ChatRoomJpaEntity(ChatRoom chatRoom) {
        this.id = chatRoom.id().value();
        this.participant1Id = chatRoom.participant1().getRecipientId().getValue();
        this.participant2Id = chatRoom.participant2().getRecipientId().getValue();
    }
}
