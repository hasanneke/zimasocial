package com.zima.zimasocial.context.social.chat.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.zima.zimasocial.context.communication.controller.request.ChatMessageRequest;
import com.zima.zimasocial.context.social.chat.entity.ChatMessage;
import com.zima.zimasocial.context.social.chat.entity.ChatRoom;
import com.zima.zimasocial.context.social.chat.exception.AuthorIsNotInRoom;
import com.zima.zimasocial.context.social.chat.exception.ChatRoomAlreadyExist;
import com.zima.zimasocial.context.social.chat.exception.ChatRoomNotFoundException;
import com.zima.zimasocial.context.social.chat.repository.ChatMessageRepository;
import com.zima.zimasocial.context.social.chat.repository.ChatRoomRepository;
import com.zima.zimasocial.context.social.chat.value.ChatMessageId;
import com.zima.zimasocial.context.social.chat.value.ChatRoomId;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.entity.AuthorRelation;
import com.zima.zimasocial.context.social.chat.event.ChatMessageSentEvent;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFollowedException;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFoundException;
import com.zima.zimasocial.context.social.author.repository.AuthorRelationRepository;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.author.value.Relation;
import com.zima.zimasocial.shared.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final AuthorRepository authorRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final AuthorRelationRepository authorRelationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    @Transactional
    public ChatMessage sendMessage(ChatRoomId chatRoomId, ChatMessageRequest messageRequest) {
        Author sender = authorRepository.getAuthenticatedAuthor();
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(ChatRoomNotFoundException::new);
        Author receiver = chatRoom.getOtherParticipant(sender);
        List<AuthorRelation> blockRelationBetween = authorRelationRepository.hasAnyBlockRelationBetween(sender.getId(), receiver.getId());
        if(!blockRelationBetween.isEmpty()){
            throw new UnauthorizedException("Messenger is blocked");
        }
        ChatMessage chatMessage = chatRoom.sendMessage(new ChatMessageId(UuidCreator.getTimeOrdered()), messageRequest.getMessage(), sender, receiver);
        applicationEventPublisher.publishEvent(new ChatMessageSentEvent(sender, receiver, chatMessage));
        chatMessageRepository.save(chatMessage);
        chatRoomRepository.save(chatRoom);
        return chatMessage;
    }
    @Transactional
    public ChatRoom createChatRoomWith(AuthorId participant) {
        Author roomCreator = authorRepository.getAuthenticatedAuthor();
        Author otherParticipant = authorRepository.findById(participant).orElseThrow(()->new AuthorNotFoundException(participant.getValue()));
        List<AuthorRelation> blockRelation = authorRelationRepository.hasAnyBlockRelationBetween(otherParticipant.getId(), roomCreator.getId());
        if(!blockRelation.isEmpty()){
            throw new UnauthorizedException("Messenger is blocked");
        }
        if(otherParticipant.getIsPrivate()){
            Optional<AuthorRelation> followRelation = authorRelationRepository.findByActorAndReceiverAndRelation(roomCreator, otherParticipant, Relation.followed);
            if(followRelation.isEmpty()){
                throw new AuthorNotFollowedException(otherParticipant.getSlug());
            }
        }
        Optional<ChatRoom> chatRoom = chatRoomRepository.findBetween(roomCreator.getId(), participant);
        if(chatRoom.isPresent()){
            throw new ChatRoomAlreadyExist();
        }
        ChatRoom newChatRoom = new ChatRoom(new ChatRoomId(UuidCreator.getTimeOrdered()), roomCreator.getId(), otherParticipant.getId());
        chatRoomRepository.save(newChatRoom);
        return newChatRoom;
    }

    @Transactional
    public void deleteChat(ChatRoomId chatId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatId).orElseThrow(ChatRoomNotFoundException::new);
        Author me = authorRepository.getAuthenticatedAuthor();
        if(chatRoom.isAuthorInRoom(me)){
            chatRoomRepository.delete(chatRoom);
        }else{
            throw new AuthorIsNotInRoom();
        }
    }
}
