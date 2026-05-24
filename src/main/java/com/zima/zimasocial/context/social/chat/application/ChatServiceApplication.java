package com.zima.zimasocial.context.social.chat.application;
import com.zima.zimasocial.context.communication.controller.request.ChatMessageRequest;
import com.zima.zimasocial.context.communication.controller.views.ChatMessageView;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.chat.entity.ChatMessage;
import com.zima.zimasocial.context.social.chat.entity.ChatRoom;
import com.zima.zimasocial.context.social.chat.entity.ChatRoomId;
import com.zima.zimasocial.context.social.chat.repository.ChatRoomRepository;
import com.zima.zimasocial.context.social.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceApplication {
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;
    private final AuthorRepository authorRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatRoom createOrFindRoomWithParticipant(AuthorId participantId) {
        AuthorId currentParticipant = authorRepository.getAuthenticatedAuthor().getId();
        Optional<ChatRoom> existingRoom = chatRoomRepository.findByParticipantsBetween(currentParticipant, participantId);
        return existingRoom.orElseGet(() -> chatService.createChatRoomWith(participantId));
    }

    @Transactional
    public void sendMessage(ChatRoomId id, ChatMessageRequest request) {
        ChatMessage chatMessage = chatService.sendMessage(id, request);
        simpMessagingTemplate.convertAndSend("/topic/chats/%s".formatted(id.value()), new ChatMessageView(chatMessage));
    }

    public void deleteChat(ChatRoomId chatId){
        chatService.deleteChat(chatId);
    }
}
