package com.zimaberlin.zimasocial.context.social.chat.entity;

import com.zimaberlin.zimasocial.context.social.chat.ChatTestUtility;
import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.chat.exception.AuthorIsNotInRoom;
import com.zimaberlin.zimasocial.shared.StaticEventPublisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
public class ChatRoomTest {
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @InjectMocks
    private StaticEventPublisher staticEventPublisher;
    @Test
    public void testSendMessage_WhenSenderIsNotInRoom_ThrowRecipientOrSenderIsNotInChatRoom() {
        ChatRoom chatRoom = ChatTestUtility.mockChatRoom(0L, 1L);
        // Sender is not in room
        Author sender = ChatTestUtility.mockAuthor(5L);
        Author receiver = ChatTestUtility.mockAuthor(0L);
        Assertions.assertThrows(AuthorIsNotInRoom.class,
                () -> chatRoom.sendMessage("", sender, receiver, ChatTestUtility.mockChatMessageId()));
    }

    @Test
    public void testSendMessage_WhenSuccess_ReturnChatMessage() {
        ChatRoom chatRoom = ChatTestUtility.mockChatRoom(0L, 1L);
        // Sender is not in room
        Author sender = ChatTestUtility.mockAuthor(0L);
        Author receiver = ChatTestUtility.mockAuthor(1L);
        ChatMessageId mockChatMessageId = ChatTestUtility.mockChatMessageId();
        ChatMessage chatMessage = chatRoom.sendMessage("Message", sender, receiver, mockChatMessageId);
        Assertions.assertNotNull(chatMessage.message());
        Assertions.assertEquals(chatMessage.senderId(), sender.getId());
        Assertions.assertEquals(chatMessage.id(), mockChatMessageId);
    }
}

