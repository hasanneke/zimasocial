package com.zimaberlin.zimasocial.context.communication.controller.bridge;

import com.zimaberlin.zimasocial.context.common.SimplePagedModel;
import com.zimaberlin.zimasocial.context.communication.RecipientNotFoundException;
import com.zimaberlin.zimasocial.context.communication.controller.views.ChatMessageView;
import com.zimaberlin.zimasocial.context.communication.controller.views.ChatRoomView;
import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.author.AuthorRepository;
import com.zimaberlin.zimasocial.context.social.chat.application.ChatServiceApplication;
import com.zimaberlin.zimasocial.context.social.chat.entity.ChatMessage;
import com.zimaberlin.zimasocial.context.social.chat.entity.ChatRoom;
import com.zimaberlin.zimasocial.context.social.chat.entity.ChatRoomId;
import com.zimaberlin.zimasocial.context.social.chat.repository.ChatMessageRepository;
import com.zimaberlin.zimasocial.context.social.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatRestControllerBridge {
    private final AuthorRepository recipientRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatServiceApplication chatServiceApplication;
    private final ChatRoomRepository chatRoomRepository;
    public ChatRoomView createRoomWith(String slug){
        Author author = recipientRepository.findBySlugAndIsDisabledFalse(slug).orElseThrow(RecipientNotFoundException::new);
        Author me = recipientRepository.getAuthenticatedAuthor();
        ChatRoom room = chatServiceApplication.createOrFindRoomWithParticipant(author.getId());
        return new ChatRoomView(room, me);
    }

    public SimplePagedModel<ChatMessageView> getMessages(UUID chatId, PageRequest request) {
        Page<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoomIdOrderBySentAtDesc(new ChatRoomId(chatId), request);
        return SimplePagedModel.of(chatMessages.getContent().stream().map(ChatMessageView::new).toList(), chatMessages.getTotalElements(), chatMessages.getTotalPages());
    }

    public SimplePagedModel<ChatRoomView> getChats(PageRequest request) {
        Author recipient = recipientRepository.getAuthenticatedAuthor();
        Page<ChatRoom> chatRooms = chatRoomRepository.findByParticipantIn(recipient.getId(), request);
        return SimplePagedModel.of(chatRooms.getContent().stream().map(e->new ChatRoomView(e, recipient)).toList(),  chatRooms.getTotalElements(), chatRooms.getTotalPages());
    }
}
