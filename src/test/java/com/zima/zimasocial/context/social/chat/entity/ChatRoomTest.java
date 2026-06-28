package com.zima.zimasocial.context.social.chat.entity;

import com.zima.zimasocial.AuthorFixture;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.chat.ChatTestUtility;
import com.zima.zimasocial.context.social.chat.exception.AuthorIsNotInRoom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChatRoomTest {
    @Test
    public void testSendMessage_WhenSenderIsNotInRoom_ThrowRecipientOrSenderIsNotInChatRoom() {
        ChatRoom chatRoom = ChatTestUtility.mockChatRoom(0L, 1L);
        // Sender is not in room
        Author sender = AuthorFixture.validAuthor();
        Author receiver = AuthorFixture.validAuthor();
        Assertions.assertThrows(AuthorIsNotInRoom.class,
                () -> chatRoom.sendMessage("", sender));
    }

    @Test
    public void testSendMessage_WhenSuccess_ReturnChatMessage() {
        Author sender = AuthorFixture.validAuthor();
        Author receiver = AuthorFixture.validAuthor();

        ChatRoom chatRoom = ChatTestUtility.mockChatRoom(sender.getId().getValue(), 1L);
        // Sender is not in room
        ChatMessage chatMessage = chatRoom.sendMessage("Message", sender);
        Assertions.assertNotNull(chatMessage.getContent());
        Assertions.assertEquals(chatMessage.getSenderId(), sender.getId());
    }
}

