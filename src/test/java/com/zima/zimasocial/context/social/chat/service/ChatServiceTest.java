package com.zima.zimasocial.context.social.chat.service;

import com.zima.zimasocial.AuthorFixture;
import com.zima.zimasocial.context.communication.controller.request.ChatMessageRequest;
import com.zima.zimasocial.context.social.chat.ChatTestUtility;
import com.zima.zimasocial.context.social.chat.entity.ChatMessage;
import com.zima.zimasocial.context.social.chat.entity.ChatRoom;
import com.zima.zimasocial.context.social.chat.exception.ChatRoomAlreadyExist;
import com.zima.zimasocial.context.social.chat.repository.ChatMessageRepository;
import com.zima.zimasocial.context.social.chat.repository.ChatRoomRepository;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFollowedException;
import com.zima.zimasocial.context.social.author.repository.AuthorRelationRepository;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.author.value.Relation;
import com.zima.zimasocial.shared.StaticEventPublisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {
    @Mock
    private StaticEventPublisher staticEventPublisher;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private ChatMessageRepository chatMessageRepository;
    @Mock
    private AuthorRelationRepository authorRelationRepository;
    @InjectMocks
    private ChatService chatService;

    @Test
    void testCreateChatRoom_WhenRoomAlreadyExist_ThrowRoomAlreadyExistException() {
        Author roomCreator = AuthorFixture.validPrivateAuthor();
        Author with = AuthorFixture.validAuthor();
        ChatRoom chatRoom = ChatTestUtility.mockChatRoom(roomCreator, with);
        when(authorRepository.getAuthenticatedAuthor()).thenReturn(roomCreator);
        when(authorRepository.findById(with.getId())).thenReturn(Optional.of(with));
        when(chatRoomRepository.findBetween(roomCreator.getId(), with.getId())).thenReturn(Optional.of(chatRoom));
        Assertions.assertThrows(ChatRoomAlreadyExist.class, ()-> chatService.createChatRoomWith(with.getId()));
    }

    @Test
    void testCreateChatRoom_WhenUserIsPrivateAndNotFollowed_ThrowUserNotFollowedException() {
        Author participant1 = AuthorFixture.validAuthor();
        Author withParticipant = AuthorFixture.validPrivateAuthor();
        when(authorRepository.getAuthenticatedAuthor()).thenReturn(participant1);
        when(authorRepository.findById(withParticipant.getId())).thenReturn(Optional.of(withParticipant));
        when(authorRelationRepository.findByActorAndReceiverAndRelation(participant1, withParticipant, Relation.followed)).thenReturn(Optional.empty());
        Assertions.assertThrows(AuthorNotFollowedException.class, ()->chatService.createChatRoomWith(withParticipant.getId()));
    }
    @Test
    void testCreateChatRoom_WhenSuccess_ReturnRoom() {
        Author participant1 = AuthorFixture.validAuthor();
        Author  participant2 = AuthorFixture.validAuthor();
        when(authorRepository.getAuthenticatedAuthor()).thenReturn(participant1);
        when(authorRepository.findById(participant2.getId())).thenReturn(Optional.of(participant2));
        when(chatRoomRepository.findBetween(participant1.getId(), participant2.getId())).thenReturn(Optional.empty());
        ChatRoom newChatRoom = chatService.createChatRoomWith(participant2.getId());
        verify(chatRoomRepository,times(1)).save(any(ChatRoom.class));
        Assertions.assertEquals(newChatRoom.getParticipant1Id(), participant1.getId());
        Assertions.assertEquals(newChatRoom.getParticipant2Id(), participant2.getId());
    }

    @Test
    void testSendMessage_WhenSuccess_SaveMessage() {
        Author sender = AuthorFixture.validAuthor();
        Author receiver = AuthorFixture.validAuthor();
        ChatRoom chatRoom = ChatTestUtility.mockChatRoom(sender, receiver);
        ChatMessageRequest chatMessageRequest = new ChatMessageRequest("Test message");
        when(authorRepository.getAuthenticatedAuthor()).thenReturn(sender);
        when(chatRoomRepository.findById(chatRoom.getId())).thenReturn(Optional.of(chatRoom));
        chatService.sendMessage(chatRoom.getId(), chatMessageRequest);
        verify(chatMessageRepository).save(any(ChatMessage.class));
    }
}
