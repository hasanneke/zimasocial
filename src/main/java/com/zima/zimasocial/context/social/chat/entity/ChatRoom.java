package com.zima.zimasocial.context.social.chat.entity;

import com.zima.zimasocial.context.social.chat.exception.AuthorIsNotInRoom;
import com.zima.zimasocial.context.social.chat.value.ChatMessageId;
import com.zima.zimasocial.context.social.chat.value.ChatRoomId;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

@Table(name = "chat_room")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class ChatRoom {
    @EmbeddedId
    @AttributeOverride(
            name = "value",
            column = @Column(name = "id", updatable = false)
    )
    private ChatRoomId id;
    @ManyToOne
    @JoinColumn(name = "participant_1", insertable = false, updatable = false)
    private Author participant1;
    @ManyToOne
    @JoinColumn(name = "participant_2", insertable = false, updatable = false)
    private Author participant2;
    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "participant_1", updatable = false)
    )
    private AuthorId participant1Id;
    @Column(name = "participant_2")
    @AttributeOverride(
            name = "value",
            column = @Column(name = "participant_2", updatable = false)
    )
    private AuthorId participant2Id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_message_id")
    private ChatMessage lastMessage;

    public ChatRoom(ChatRoomId id, AuthorId participant1Id, AuthorId participant2Id) {
        Assert.notNull(participant1Id, "participant1 cannot be null");
        Assert.notNull(participant2Id, "participant1 cannot be null");
        Assert.isTrue(id != null, "ChatRoomId cannot be null");
        this.id = id;
        this.participant1Id = participant1Id;
        this.participant2Id = participant2Id;
    }



    public ChatMessage sendMessage(
                                    ChatMessageId chatMessageId,
                                    String message,
                                   Author sender,
                                   Author receiver) {
        if(!(sender.getId().equals(participant1Id) || sender.getId().equals(participant2Id))){
            throw new AuthorIsNotInRoom();
        }
        ChatMessage chatMessage = new ChatMessage(chatMessageId, message, id, sender.getId());
        this.lastMessage = chatMessage;

        return chatMessage;
    }

    public Author getOtherParticipant(Author me) {
        if(me.getId().equals(participant1Id)) {
            return participant2;
        }else if(me.getId().equals(participant2Id)){
            return participant1;
        }else{
            throw new AuthorIsNotInRoom();
        }
    }

    public boolean isAuthorInRoom(Author author){
        return author.equals(participant1) || author.equals(participant2);
    }

}
