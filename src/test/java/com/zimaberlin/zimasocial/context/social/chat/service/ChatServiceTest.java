package com.zimaberlin.zimasocial.context.social.chat.service;

import com.zimaberlin.zimasocial.context.social.author.exception.AuthorNotFollowedException;
import com.zimaberlin.zimasocial.context.social.authorrelation.AuthorRelationCollection;
import com.zimaberlin.zimasocial.context.social.chat.ChatTestUtility;
import com.zimaberlin.zimasocial.context.communication.controller.request.ChatMessageRequest;
import com.zimaberlin.zimasocial.context.social.author.entity.Author;
import com.zimaberlin.zimasocial.context.social.author.repository.AuthorRepository;
import com.zimaberlin.zimasocial.context.social.chat.entity.ChatMessage;
import com.zimaberlin.zimasocial.context.social.chat.entity.ChatMessageId;
import com.zimaberlin.zimasocial.context.social.chat.entity.ChatRoom;
import com.zimaberlin.zimasocial.context.social.chat.entity.ChatRoomId;
import com.zimaberlin.zimasocial.context.social.chat.exception.ChatRoomAlreadyExist;
import com.zimaberlin.zimasocial.context.social.chat.repository.ChatMessageRepository;
import com.zimaberlin.zimasocial.context.social.chat.repository.ChatRoomRepository;
import com.zimaberlin.zimasocial.context.social.testutility.AuthorTestUtility;
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
    private AuthorRepository authorRepository;
    @Mock
    private ChatMessageRepository chatMessageRepository;
    @Mock
    private AuthorRelationCollection authorRelationCollection;
    @InjectMocks
    private ChatService chatService;

    @Test
    void testCreateChatRoom_WhenRoomAlreadyExist_ThrowRoomAlreadyExistException() {
        Author roomCreator = AuthorTestUtility.mockPrivateAccountAuthor();
        Author with = AuthorTestUtility.mockPublicAccountAuthor();
        ChatRoom chatRoom = ChatTestUtility.mockChatRoomPlain(roomCreator.getId(), with.getId());
        when(authorRepository.getAuthenticatedAuthor()).thenReturn(roomCreator);
        when(authorRepository.findById(with.getId())).thenReturn(Optional.of(with));
        when(chatRoomRepository.findByParticipantsBetween(roomCreator.getId(), with.getId())).thenReturn(Optional.of(chatRoom));
        Assertions.assertThrows(ChatRoomAlreadyExist.class, ()-> chatService.createChatRoomWith(with.getId()));
    }

    @Test
    void testCreateChatRoom_WhenUserIsPrivateAndNotFollowed_ThrowUserNotFollowedException() {
        Author participant1 = AuthorTestUtility.mockPublicAccountAuthor();
        Author withParticipant = AuthorTestUtility.mockPrivateAccountAuthor();
        when(authorRepository.getAuthenticatedAuthor()).thenReturn(participant1);
        when(authorRepository.findById(withParticipant.getId())).thenReturn(Optional.of(withParticipant));
        when(authorRelationCollection.findFollowRelationBetween(participant1.getId(), withParticipant.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(AuthorNotFollowedException.class, ()->chatService.createChatRoomWith(withParticipant.getId()));
    }
    @Test
    void testCreateChatRoom_WhenSuccess_ReturnRoom() {
        ChatRoomId chatRoomId = ChatTestUtility.mockChatRoomId();
        Author participant1 = AuthorTestUtility.mockPublicAccountAuthor();
        Author participant2 = AuthorTestUtility.mockPublicAccountAuthor();
        when(authorRepository.getAuthenticatedAuthor()).thenReturn(participant1);
        when(authorRepository.findById(participant2.getId())).thenReturn(Optional.of(participant2));
        when(chatRoomRepository.findByParticipantsBetween(participant1.getId(), participant2.getId())).thenReturn(Optional.empty());
        when(chatRoomRepository.nextId()).thenReturn(chatRoomId);
        ChatRoom newChatRoom = chatService.createChatRoomWith(participant2.getId());
        verify(chatRoomRepository,times(1)).save(any(ChatRoom.class));
        Assertions.assertEquals(chatRoomId, newChatRoom.getId());
        Assertions.assertEquals(newChatRoom.getParticipant1(), participant1);
        Assertions.assertEquals(newChatRoom.getParticipant2(), participant2);
    }

    @Test
    void testSendMessage_WhenSuccess_SaveMessage() {
        ChatMessageId chatMessageId = new ChatMessageId(UUID.randomUUID());
        Author sender = AuthorTestUtility.mockPublicAccountAuthor();
        Author receiver = AuthorTestUtility.mockPublicAccountAuthor();
        ChatRoom chatRoom = ChatTestUtility.mockChatRoomPlain(sender.getId(), receiver.getId());
        ChatMessageRequest chatMessageRequest = new ChatMessageRequest("Test message");
        when(authorRepository.getAuthenticatedAuthor()).thenReturn(sender);
        when(chatRoomRepository.findById(chatRoom.getId())).thenReturn(Optional.of(chatRoom));
        when(chatMessageRepository.nextId()).thenReturn(chatMessageId);
        chatService.sendMessage(chatRoom.getId(), chatMessageRequest);
        verify(chatMessageRepository).save(any(ChatMessage.class));
    }
}
