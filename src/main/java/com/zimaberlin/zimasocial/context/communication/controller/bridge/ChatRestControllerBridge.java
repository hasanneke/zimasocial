package com.zimaberlin.zimasocial.context.communication.controller.bridge;

import com.zimaberlin.zimasocial.context.communication.RecipientNotFoundException;
import com.zimaberlin.zimasocial.context.communication.chat.application.ChatServiceApplication;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatMessage;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoom;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoomId;
import com.zimaberlin.zimasocial.context.communication.chat.repository.ChatMessageRepository;
import com.zimaberlin.zimasocial.context.communication.controller.views.ChatMessageView;
import com.zimaberlin.zimasocial.context.communication.controller.views.ChatRoomView;
import com.zimaberlin.zimasocial.context.communication.domain.Recipient;
import com.zimaberlin.zimasocial.context.communication.repository.RecipientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatRestControllerBridge {
    private final RecipientRepository recipientRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatServiceApplication chatServiceApplication;
    public ChatRoomView createRoomWith(String slug){
        Recipient recipient = recipientRepository.findBySlug(slug).orElseThrow(RecipientNotFoundException::new);
        Recipient me = recipientRepository.getAuthenticatedRecipient();
        ChatRoom room = chatServiceApplication.createOrFindRoomWithParticipant(recipient.getRecipientId());
        return new ChatRoomView(room, me);
    }

    public PagedModel<ChatMessageView> getMessages(UUID chatId, PageRequest request) {
        Page<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoomIdOrderBySentAtDesc(new ChatRoomId(chatId), request);
        return PagedModel.of(chatMessages.getContent().stream().map(ChatMessageView::new).toList(),
                new PagedModel.PageMetadata(request.getPageSize(), request.getPageNumber(), chatMessages.getTotalElements(), chatMessages.getTotalPages()));
    }
}
