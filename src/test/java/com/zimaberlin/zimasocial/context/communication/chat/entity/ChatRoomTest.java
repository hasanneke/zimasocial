package com.zimaberlin.zimasocial.context.communication.chat.entity;

import com.zimaberlin.zimasocial.context.communication.chat.ChatTestUtility;
import com.zimaberlin.zimasocial.context.communication.chat.exception.RecipientOrSenderIsNotInChatRoom;
import com.zimaberlin.zimasocial.context.communication.domain.Recipient;
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
        Recipient sender = ChatTestUtility.mockRecipient(5L);
        Recipient receiver = ChatTestUtility.mockRecipient(0L);
        Assertions.assertThrows(RecipientOrSenderIsNotInChatRoom.class,
                () -> chatRoom.sendMessage("", sender, receiver, ChatTestUtility.mockChatMessageId()));
    }

    @Test
    public void testSendMessage_WhenSuccess_ReturnChatMessage() {
        ChatRoom chatRoom = ChatTestUtility.mockChatRoom(0L, 1L);
        // Sender is not in room
        Recipient sender = ChatTestUtility.mockRecipient(0L);
        Recipient receiver = ChatTestUtility.mockRecipient(1L);
        ChatMessageId mockChatMessageId = ChatTestUtility.mockChatMessageId();
        ChatMessage chatMessage = chatRoom.sendMessage("Message", sender, receiver, mockChatMessageId);
        Assertions.assertNotNull(chatMessage.message());
        Assertions.assertEquals(chatMessage.senderId(), sender.getRecipientId());
        Assertions.assertEquals(chatMessage.id(), mockChatMessageId);
    }
}

