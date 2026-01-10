package com.zima.zimasocial.context.social.chat.infastructure.datasource.entity;

import com.zima.zimasocial.context.social.chat.entity.ChatRoom;
import com.zima.zimasocial.context.social.chat.entity.ChatRoomId;
import com.zima.zimasocial.entity.user.UserEntity;
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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_message_id")
    private ChatMessageJpaEntity lastMessage;

    public ChatRoomJpaEntity(ChatRoom chatRoom) {
        this.id = chatRoom.getId().value();
        this.participant1Id = chatRoom.getParticipant1().getId().getValue();
        this.participant2Id = chatRoom.getParticipant2().getId().getValue();
        if(chatRoom.getLastMessage() != null){
            this.lastMessage = new ChatMessageJpaEntity(chatRoom.getLastMessage());
        }
    }

    public ChatRoom toDomain() {
        if(lastMessage == null){
            return new ChatRoom(new ChatRoomId(id), participant1.toDomain(), participant2.toDomain());
        }
        return new ChatRoom(new ChatRoomId(id),
                participant1.toDomain(),
                participant2.toDomain(),
                lastMessage.toDomain());
    }
}
