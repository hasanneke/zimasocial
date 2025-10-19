package com.zimaberlin.zimasocial.context.communication.chat.service;

import com.zimaberlin.zimasocial.context.communication.chat.ChatTestUtility;
import com.zimaberlin.zimasocial.context.communication.chat.entity.*;
import com.zimaberlin.zimasocial.context.communication.chat.exception.ChatRoomAlreadyExist;
import com.zimaberlin.zimasocial.context.communication.chat.repository.ChatMessageRepository;
import com.zimaberlin.zimasocial.context.communication.chat.repository.ChatRoomRepository;
import com.zimaberlin.zimasocial.context.communication.controller.request.ChatMessageRequest;
import com.zimaberlin.zimasocial.context.communication.domain.Recipient;
import com.zimaberlin.zimasocial.context.communication.repository.RecipientRepository;
import com.zimaberlin.zimasocial.shared.StaticEventPublisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @InjectMocks
    private StaticEventPublisher staticEventPublisher;
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private RecipientRepository recipientRepository;
    @Mock
    private ChatMessageRepository chatMessageRepository;
    @InjectMocks
    private ChatService chatService;

    @Test
    void testCreateChatRoom_WhenRoomAlreadyExist_ThrowRoomAlreadyExistException() {
        Recipient participant = ChatTestUtility.mockRecipient();
        Recipient participant2 = ChatTestUtility.mockRecipient();
        ChatRoom chatRoom = ChatTestUtility.mockChatRoomPlain(participant.getRecipientId(), participant2.getRecipientId());
        when(recipientRepository.getAuthenticatedRecipient()).thenReturn(participant);
        when(recipientRepository.findByRecipientId(participant2.getRecipientId())).thenReturn(Optional.of(participant2));
        when(chatRoomRepository.findByParticipantsBetween(participant.getRecipientId(), participant2.getRecipientId())).thenReturn(Optional.of(chatRoom));
        Assertions.assertThrows(ChatRoomAlreadyExist.class, ()-> chatService.createChatRoomWith(participant2.getRecipientId()));
    }

    @Test
    void testCreateChatRoom_WhenSuccess_ReturnRoom() {
        ChatRoomId chatRoomId = ChatTestUtility.mockChatRoomId();
        Recipient participant1 = ChatTestUtility.mockRecipient();
        Recipient participant2 = ChatTestUtility.mockRecipient();
        when(recipientRepository.getAuthenticatedRecipient()).thenReturn(participant1);
        when(recipientRepository.findByRecipientId(participant2.getRecipientId())).thenReturn(Optional.of(participant2));
        when(chatRoomRepository.findByParticipantsBetween(participant1.getRecipientId(), participant2.getRecipientId())).thenReturn(Optional.empty());
        when(chatRoomRepository.nextId()).thenReturn(chatRoomId);
        ChatRoom newChatRoom = chatService.createChatRoomWith(participant2.getRecipientId());
        verify(chatRoomRepository,times(1)).save(any(ChatRoom.class));
        Assertions.assertEquals(chatRoomId, newChatRoom.id());
        Assertions.assertEquals(newChatRoom.participant1(), participant1);
        Assertions.assertEquals(newChatRoom.participant2(), participant2);
    }

    @Test
    void testSendMessage_WhenSuccess_SaveMessage() {
        ChatMessageId chatMessageId = new ChatMessageId(UUID.randomUUID());
        Recipient sender = ChatTestUtility.mockRecipient();
        Recipient receiver = ChatTestUtility.mockRecipient();
        ChatRoom chatRoom = ChatTestUtility.mockChatRoomPlain(sender.getRecipientId(), receiver.getRecipientId());
        ChatMessageRequest chatMessageRequest = new ChatMessageRequest("Test message");
        when(recipientRepository.getAuthenticatedRecipient()).thenReturn(sender);
        when(chatRoomRepository.findById(chatRoom.id())).thenReturn(Optional.of(chatRoom));
        when(chatMessageRepository.nextId()).thenReturn(chatMessageId);
        chatService.sendMessage(chatRoom.id(), chatMessageRequest);
        verify(chatMessageRepository).save(any(ChatMessage.class));
    }
}
