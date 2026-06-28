package com.zima.zimasocial.context.social.chat.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.entity.AuthorRelation;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFollowedException;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFoundException;
import com.zima.zimasocial.context.social.author.repository.AuthorRelationRepository;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.author.value.Relation;
import com.zima.zimasocial.context.social.chat.entity.ChatRoom;
import com.zima.zimasocial.context.social.chat.exception.AuthorIsNotInRoom;
import com.zima.zimasocial.context.social.chat.exception.ChatRoomAlreadyExist;
import com.zima.zimasocial.context.social.chat.exception.ChatRoomNotFoundException;
import com.zima.zimasocial.context.social.chat.repository.ChatRoomRepository;
import com.zima.zimasocial.context.social.chat.value.ChatRoomId;
import com.zima.zimasocial.shared.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final AuthorRepository authorRepository;
    private final AuthorRelationRepository authorRelationRepository;

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
