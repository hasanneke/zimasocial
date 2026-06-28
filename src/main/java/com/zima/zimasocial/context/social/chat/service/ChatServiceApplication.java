package com.zima.zimasocial.context.social.chat.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.zima.zimasocial.context.communication.controller.request.ChatMessageRequest;
import com.zima.zimasocial.context.communication.controller.views.ChatMessageView;
import com.zima.zimasocial.context.social.author.entity.AuthorRelation;
import com.zima.zimasocial.context.social.author.repository.AuthorRelationRepository;
import com.zima.zimasocial.context.social.chat.event.ChatMessageSentEvent;
import com.zima.zimasocial.context.social.chat.exception.ChatRoomNotFoundException;
import com.zima.zimasocial.context.social.chat.repository.ChatMessageRepository;
import com.zima.zimasocial.context.social.chat.repository.ChatRoomRepository;
import com.zima.zimasocial.context.social.chat.value.ChatMessageId;
import com.zima.zimasocial.context.social.chat.value.ChatRoomId;
import com.zima.zimasocial.context.social.chat.entity.ChatMessage;
import com.zima.zimasocial.context.social.chat.entity.ChatRoom;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.shared.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceApplication {
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;
    private final AuthorRepository authorRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ChatMessageRepository chatMessageRepository;
    private final AuthorRelationRepository authorRelationRepository;

    public ChatRoom createOrFindRoomWithParticipant(AuthorId participantId) {
        Author currentParticipant = authorRepository.getAuthenticatedAuthor();
        Optional<ChatRoom> existingRoom = chatRoomRepository.findBetween(currentParticipant.getId(), participantId);
        return existingRoom.orElseGet(() -> chatService.createChatRoomWith(participantId));
    }

    @Transactional
    public void sendMessage(ChatRoomId id, ChatMessageRequest request) {
        Author sender = authorRepository.getAuthenticatedAuthor();
        ChatRoom chatRoom = chatRoomRepository.findById(id).orElseThrow(ChatRoomNotFoundException::new);
        Author receiver = chatRoom.getOtherParticipant(sender);
        List<AuthorRelation> blockRelationBetween = authorRelationRepository.hasAnyBlockRelationBetween(sender.getId(), receiver.getId());
        if(!blockRelationBetween.isEmpty()){
            throw new UnauthorizedException("Messenger is blocked");
        }
        ChatMessage chatMessage = chatRoom.sendMessage(new ChatMessageId(UuidCreator.getTimeOrdered()), request.getMessage(), sender);
        chatMessage = chatMessageRepository.save(chatMessage);
        chatRoom.updateLastMessage(chatMessage);
        chatRoomRepository.save(chatRoom);
        simpMessagingTemplate.convertAndSend("/topic/chats/%s".formatted(id.getValue()), new ChatMessageView(chatMessage));
        applicationEventPublisher.publishEvent(new ChatMessageSentEvent(sender, receiver, chatMessage));
    }

    public void deleteChat(ChatRoomId chatId){
        chatService.deleteChat(chatId);
    }
}
