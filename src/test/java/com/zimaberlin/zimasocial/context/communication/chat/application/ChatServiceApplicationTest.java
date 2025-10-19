package com.zimaberlin.zimasocial.context.communication.chat.application;


import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoom;
import com.zimaberlin.zimasocial.context.communication.chat.ChatTestUtility;
import com.zimaberlin.zimasocial.context.communication.chat.repository.ChatRoomRepository;
import com.zimaberlin.zimasocial.context.communication.chat.service.ChatService;
import com.zimaberlin.zimasocial.context.communication.domain.Recipient;
import com.zimaberlin.zimasocial.context.communication.domain.RecipientId;
import com.zimaberlin.zimasocial.context.communication.repository.RecipientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatServiceApplicationTest {
    @Mock
    private ChatService chatService;
    @Mock
    private RecipientRepository recipientRepository;
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @InjectMocks
    private ChatServiceApplication chatServiceApplication;

    @Test
    void testCreateOrFindRoomWithParticipant_WhenRoomExistReturnRoom() {
        RecipientId participant1 = ChatTestUtility.mockRecipientId();
        RecipientId participant2 = ChatTestUtility.mockRecipientId();
        ChatRoom chatRoom = ChatTestUtility.mockChatRoomPlain(participant1, participant2);
        when(chatRoomRepository.findByParticipantsBetween(participant1, participant2)).thenReturn(Optional.of(chatRoom));
        when(recipientRepository.getAuthenticatedRecipient()).thenReturn(ChatTestUtility.mockRecipient(participant1.getValue()));
        ChatRoom existingChatRoom =  chatServiceApplication.createOrFindRoomWithParticipant(participant2);

        Assertions.assertEquals(chatRoom.id(), existingChatRoom.id());
        Assertions.assertEquals(chatRoom.participant1(), existingChatRoom.participant1());
        Assertions.assertEquals(chatRoom.participant2(), existingChatRoom.participant2());
    }

    @Test
    void testCreateOrFindRoomWithParticipant_WhenRoomNotExist_CreateRoomAndReturn() {
        Recipient participant1 = ChatTestUtility.mockRecipient();
        Recipient withParticipant = ChatTestUtility.mockRecipient();
        ChatRoom newChatRoom = ChatTestUtility.mockChatRoomPlain(participant1, withParticipant);
        when(chatRoomRepository.findByParticipantsBetween(participant1.getRecipientId(), withParticipant.getRecipientId())).thenReturn(Optional.empty());
        when(recipientRepository.getAuthenticatedRecipient()).thenReturn(participant1);
        when(chatService.createChatRoomWith(withParticipant.getRecipientId())).thenReturn(newChatRoom);
        ChatRoom chatRoom = chatServiceApplication.createOrFindRoomWithParticipant(withParticipant.getRecipientId());
        verify(chatService, times(1)).createChatRoomWith(withParticipant.getRecipientId());
        Assertions.assertEquals(participant1, (chatRoom.participant1()));
        Assertions.assertEquals(withParticipant, (chatRoom.participant2()));
    }
}
