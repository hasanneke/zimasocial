package com.zima.zimasocial.context.social.chat.application;


import com.zima.zimasocial.AuthorFixture;
import com.zima.zimasocial.context.social.chat.ChatTestUtility;
import com.zima.zimasocial.context.social.chat.entity.ChatRoom;
import com.zima.zimasocial.context.social.chat.repository.ChatRoomRepository;
import com.zima.zimasocial.context.social.chat.service.ChatService;
import com.zima.zimasocial.context.social.chat.service.ChatServiceApplication;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.shared.StaticEventPublisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatServiceApplicationTest {
    @Mock
    private StaticEventPublisher staticEventPublisher;
    @Mock
    private ChatService chatService;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @InjectMocks
    private ChatServiceApplication chatServiceApplication;

    @Test
    void testCreateOrFindRoomWithParticipant_WhenRoomExistReturnRoom() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Author participant1 = AuthorFixture.validAuthor();
        Author participant2 = AuthorFixture.validAuthor();
        ChatRoom chatRoom = ChatTestUtility.mockChatRoom(participant1, participant2);
        when(chatRoomRepository.findBetween(participant1.getId(), participant2.getId())).thenReturn(Optional.of(chatRoom));
        when(authorRepository.getAuthenticatedAuthor()).thenReturn(participant1);
        ChatRoom existingChatRoom =  chatServiceApplication.createOrFindRoomWithParticipant(participant2.getId());

        Assertions.assertEquals(chatRoom.getId(), existingChatRoom.getId());
        Assertions.assertEquals(chatRoom.getParticipant1(), existingChatRoom.getParticipant1());
        Assertions.assertEquals(chatRoom.getParticipant2(), existingChatRoom.getParticipant2());
    }

    @Test
    void testCreateOrFindRoomWithParticipant_WhenRoomNotExist_CreateRoomAndReturn() {
        Author participant1 = AuthorFixture.validAuthor();
        Author withParticipant = AuthorFixture.validAuthor();
        ChatRoom newChatRoom = ChatTestUtility.mockChatRoom(participant1, withParticipant);
        when(chatRoomRepository.findBetween(participant1.getId(), withParticipant.getId())).thenReturn(Optional.empty());
        when(authorRepository.getAuthenticatedAuthor()).thenReturn(participant1);
        when(chatService.createChatRoomWith(withParticipant.getId())).thenReturn(newChatRoom);
        ChatRoom chatRoom = chatServiceApplication.createOrFindRoomWithParticipant(withParticipant.getId());
        verify(chatService, times(1)).createChatRoomWith(withParticipant.getId());
        Assertions.assertEquals(participant1, (chatRoom.getParticipant1()));
        Assertions.assertEquals(withParticipant, (chatRoom.getParticipant2()));
    }
}
