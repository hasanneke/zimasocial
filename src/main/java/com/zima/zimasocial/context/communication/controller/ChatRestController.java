package com.zima.zimasocial.context.communication.controller;

import com.zima.zimasocial.shared.SimplePagedModel;
import com.zima.zimasocial.context.social.chat.service.ChatServiceApplication;
import com.zima.zimasocial.context.social.chat.value.ChatRoomId;
import com.zima.zimasocial.context.communication.controller.bridge.ChatRestControllerBridge;
import com.zima.zimasocial.context.communication.controller.request.ChatMessageRequest;
import com.zima.zimasocial.context.communication.controller.views.ChatMessageView;
import com.zima.zimasocial.context.communication.controller.views.ChatRoomView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/chats")
@RequiredArgsConstructor
public class ChatRestController {
    private final ChatServiceApplication chatServiceApplication;
    private final ChatRestControllerBridge chatRestControllerBridge;
    @PostMapping
    public ResponseEntity<ChatRoomView> createRoom(@RequestParam(name = "with") String slug) {
        return ResponseEntity.ok(chatRestControllerBridge.createRoomWith(slug));
    }

    @PostMapping(path = "/{chatId}/messages")
    public ResponseEntity<Void> sendMessage(@PathVariable(name = "chatId") UUID chatId,
                                            @RequestBody ChatMessageRequest chatMessageRequest) {
        chatServiceApplication.sendMessage(new ChatRoomId(chatId), chatMessageRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{chatId}/messages")
    public ResponseEntity<SimplePagedModel<ChatMessageView>> getMessages(@PathVariable(name = "chatId") UUID chatId,
                                                                   @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                   @RequestParam(name = "size", defaultValue = "20") Integer size) {
        return ResponseEntity.ok(chatRestControllerBridge.getMessages(chatId, PageRequest.of(page, size)));
    }

    @GetMapping
    public ResponseEntity<SimplePagedModel<ChatRoomView>> getChats(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                   @RequestParam(name = "size", defaultValue = "20") Integer size) {
        return ResponseEntity.ok(chatRestControllerBridge.getChats(PageRequest.of(page, size)));
    }

    @DeleteMapping(path = "/{chatId}")
    public ResponseEntity<Void> deleteChat(@PathVariable(name = "chatId") UUID chatId) {
        chatServiceApplication.deleteChat(new ChatRoomId(chatId));
        return ResponseEntity.ok().build();
    }
}
