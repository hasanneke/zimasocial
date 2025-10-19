package com.zimaberlin.zimasocial.context.communication.chat.application;

import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatMessage;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoom;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoomId;
import com.zimaberlin.zimasocial.context.communication.chat.repository.ChatRoomRepository;
import com.zimaberlin.zimasocial.context.communication.chat.service.ChatService;
import com.zimaberlin.zimasocial.context.communication.controller.request.ChatMessageRequest;
import com.zimaberlin.zimasocial.context.communication.controller.views.ChatMessageView;
import com.zimaberlin.zimasocial.context.communication.domain.RecipientId;
import com.zimaberlin.zimasocial.context.communication.repository.RecipientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceApplication {
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;
    private final RecipientRepository recipientRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatRoom createOrFindRoomWithParticipant(RecipientId participantId) {
        RecipientId currentParticipant = recipientRepository.getAuthenticatedRecipient().getRecipientId();
        Optional<ChatRoom> existingRoom = chatRoomRepository.findByParticipantsBetween(currentParticipant, participantId);
        return existingRoom.orElseGet(() -> chatService.createChatRoomWith(participantId));
    }

    public void sendMessage(ChatRoomId id, ChatMessageRequest request) {
        ChatMessage chatMessage = chatService.sendMessage(id, request);
        simpMessagingTemplate.convertAndSend("/topic/chats/%s".formatted(id.value()), new ChatMessageView(chatMessage));
    }
}
