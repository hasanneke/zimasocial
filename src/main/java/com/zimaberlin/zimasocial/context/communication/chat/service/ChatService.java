package com.zimaberlin.zimasocial.context.communication.chat.service;

import com.zimaberlin.zimasocial.context.communication.RecipientNotFoundException;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatMessage;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoomId;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoom;
import com.zimaberlin.zimasocial.context.communication.chat.exception.ChatRoomAlreadyExist;
import com.zimaberlin.zimasocial.context.communication.chat.exception.ChatRoomNotFoundException;
import com.zimaberlin.zimasocial.context.communication.chat.repository.ChatMessageRepository;
import com.zimaberlin.zimasocial.context.communication.chat.repository.ChatRoomRepository;
import com.zimaberlin.zimasocial.context.communication.controller.request.ChatMessageRequest;
import com.zimaberlin.zimasocial.context.communication.domain.Recipient;
import com.zimaberlin.zimasocial.context.communication.domain.RecipientId;
import com.zimaberlin.zimasocial.context.communication.repository.RecipientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final RecipientRepository recipientRepository;
    private final ChatMessageRepository chatMessageRepository;
    @Autowired
    public ChatService(ChatRoomRepository chatRoomRepository, RecipientRepository recipientRepository, ChatMessageRepository chatMessageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.recipientRepository = recipientRepository;
        this.chatMessageRepository = chatMessageRepository;
    }
    public ChatMessage sendMessage(ChatRoomId chatRoomId, ChatMessageRequest messageRequest) {
        Recipient sender = recipientRepository.getAuthenticatedRecipient();
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(ChatRoomNotFoundException::new);
        Recipient receiver = chatRoom.getOtherParticipant(sender);
        ChatMessage chatMessage = chatRoom.sendMessage(messageRequest.getMessage(), sender, receiver, chatMessageRepository.nextId());
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }
    public ChatRoom createChatRoomWith(RecipientId participant) {
        Recipient roomCreator = recipientRepository.getAuthenticatedRecipient();
        Recipient otherParticipant = recipientRepository.findByRecipientId(participant).orElseThrow(RecipientNotFoundException::new);
        Optional<ChatRoom> chatRoom = chatRoomRepository.findByParticipantsBetween(roomCreator.getRecipientId(), participant);
        if(chatRoom.isPresent()){
            throw new ChatRoomAlreadyExist();
        }
        ChatRoom newChatRoom = new ChatRoom(chatRoomRepository.nextId(), roomCreator, otherParticipant);
        chatRoomRepository.save(newChatRoom);
        return newChatRoom;
    }
}
