package com.zima.zimasocial.context.social.chat.service;

import com.zima.zimasocial.context.communication.controller.request.ChatMessageRequest;
import com.zima.zimasocial.context.social.author.entity.AuthorDomain;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFollowedException;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFoundException;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.authorrelation.AuthorRelationCollection;
import com.zima.zimasocial.context.social.authorrelation.values.BlockRelation;
import com.zima.zimasocial.context.social.authorrelation.values.FollowRelation;
import com.zima.zimasocial.context.social.chat.entity.ChatMessage;
import com.zima.zimasocial.context.social.chat.entity.ChatRoom;
import com.zima.zimasocial.context.social.chat.entity.ChatRoomId;
import com.zima.zimasocial.context.social.chat.exception.AuthorIsNotInRoom;
import com.zima.zimasocial.context.social.chat.exception.ChatRoomAlreadyExist;
import com.zima.zimasocial.context.social.chat.exception.ChatRoomNotFoundException;
import com.zima.zimasocial.context.social.chat.repository.ChatMessageRepository;
import com.zima.zimasocial.context.social.chat.repository.ChatRoomRepository;
import com.zima.zimasocial.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final AuthorRepository authorRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final AuthorRelationCollection authorRelationCollection;

    @Autowired
    public ChatService(ChatRoomRepository chatRoomRepository, AuthorRepository authorRepository, ChatMessageRepository chatMessageRepository, AuthorRelationCollection authorRelationCollection) {
        this.chatRoomRepository = chatRoomRepository;
        this.authorRepository = authorRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.authorRelationCollection = authorRelationCollection;
    }
    @Transactional
    public ChatMessage sendMessage(ChatRoomId chatRoomId, ChatMessageRequest messageRequest) {
        AuthorDomain sender = authorRepository.getAuthenticatedAuthor();
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(ChatRoomNotFoundException::new);
        AuthorDomain receiver = chatRoom.getOtherParticipant(sender);
        boolean hasBlockRelationBetween = authorRelationCollection.hasBlockRelationBetween(sender.getId().getValue(), receiver.getId().getValue());
        if(hasBlockRelationBetween){
            throw new UnauthorizedException("Messenger is blocked");
        }
        ChatMessage chatMessage = chatRoom.sendMessage(messageRequest.getMessage(), sender, receiver, chatMessageRepository.nextId());
        chatMessageRepository.save(chatMessage);
        chatRoomRepository.save(chatRoom);
        return chatMessage;
    }
    @Transactional
    public ChatRoom createChatRoomWith(AuthorId participant) {
        AuthorDomain roomCreator = authorRepository.getAuthenticatedAuthor();
        AuthorDomain otherParticipant = authorRepository.findById(participant).orElseThrow(()->new AuthorNotFoundException(participant.getValue()));
        Optional<BlockRelation> blockRelation = authorRelationCollection.findBlockRelationBetween(otherParticipant.getId(), roomCreator.getId());
        if(blockRelation.isPresent()){
            throw new UnauthorizedException("Messenger is blocked");
        }
        if(otherParticipant.getIsPrivate()){
            Optional<FollowRelation> followRelation = authorRelationCollection.findFollowRelationBetween(roomCreator.getId(), otherParticipant.getId());
            if(followRelation.isEmpty()){
                throw new AuthorNotFollowedException(otherParticipant.getSlug());
            }
        }
        Optional<ChatRoom> chatRoom = chatRoomRepository.findByParticipantsBetween(roomCreator.getId(), participant);
        if(chatRoom.isPresent()){
            throw new ChatRoomAlreadyExist();
        }
        ChatRoom newChatRoom = new ChatRoom(chatRoomRepository.nextId(), roomCreator, otherParticipant);
        chatRoomRepository.save(newChatRoom);
        return newChatRoom;
    }

    @Transactional
    public void deleteChat(ChatRoomId chatId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatId).orElseThrow(ChatRoomNotFoundException::new);
        AuthorDomain me = authorRepository.getAuthenticatedAuthor();
        if(chatRoom.isAuthorInRoom(me)){
            chatRoomRepository.delete(chatRoom);
        }else{
            throw new AuthorIsNotInRoom();
        }
    }
}
