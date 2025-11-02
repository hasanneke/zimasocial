package com.zimaberlin.zimasocial.context.social.chat.application;


import com.zimaberlin.zimasocial.context.social.chat.ChatTestUtility;
import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.author.AuthorRepository;
import com.zimaberlin.zimasocial.context.social.chat.entity.ChatRoom;
import com.zimaberlin.zimasocial.context.social.chat.repository.ChatRoomRepository;
import com.zimaberlin.zimasocial.context.social.chat.service.ChatService;
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
    private AuthorRepository authorRepository;
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @InjectMocks
    private ChatServiceApplication chatServiceApplication;

    @Test
    void testCreateOrFindRoomWithParticipant_WhenRoomExistReturnRoom() {
        AuthorId participant1 = ChatTestUtility.mockAuthorId();
        AuthorId participant2 = ChatTestUtility.mockAuthorId();
        ChatRoom chatRoom = ChatTestUtility.mockChatRoomPlain(participant1, participant2);
        when(chatRoomRepository.findByParticipantsBetween(participant1, participant2)).thenReturn(Optional.of(chatRoom));
        when(authorRepository.getAuthenticatedAuthor()).thenReturn(ChatTestUtility.mockAuthor(participant1.getValue()));
        ChatRoom existingChatRoom =  chatServiceApplication.createOrFindRoomWithParticipant(participant2);

        Assertions.assertEquals(chatRoom.getId(), existingChatRoom.getId());
        Assertions.assertEquals(chatRoom.getParticipant1(), existingChatRoom.getParticipant1());
        Assertions.assertEquals(chatRoom.getParticipant2(), existingChatRoom.getParticipant2());
    }

    @Test
    void testCreateOrFindRoomWithParticipant_WhenRoomNotExist_CreateRoomAndReturn() {
        Author participant1 = ChatTestUtility.mockAuthor();
        Author withParticipant = ChatTestUtility.mockAuthor();
        ChatRoom newChatRoom = ChatTestUtility.mockChatRoomPlain(participant1, withParticipant);
        when(chatRoomRepository.findByParticipantsBetween(participant1.getId(), withParticipant.getId())).thenReturn(Optional.empty());
        when(authorRepository.getAuthenticatedAuthor()).thenReturn(participant1);
        when(chatService.createChatRoomWith(withParticipant.getId())).thenReturn(newChatRoom);
        ChatRoom chatRoom = chatServiceApplication.createOrFindRoomWithParticipant(withParticipant.getId());
        verify(chatService, times(1)).createChatRoomWith(withParticipant.getId());
        Assertions.assertEquals(participant1, (chatRoom.getParticipant1()));
        Assertions.assertEquals(withParticipant, (chatRoom.getParticipant2()));
    }
}
