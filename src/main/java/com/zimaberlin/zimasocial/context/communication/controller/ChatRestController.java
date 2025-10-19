package com.zimaberlin.zimasocial.context.communication.controller;

import com.zimaberlin.zimasocial.context.communication.chat.application.ChatServiceApplication;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoomId;
import com.zimaberlin.zimasocial.context.communication.controller.bridge.ChatRestControllerBridge;
import com.zimaberlin.zimasocial.context.communication.controller.request.ChatMessageRequest;
import com.zimaberlin.zimasocial.context.communication.controller.views.ChatMessageView;
import com.zimaberlin.zimasocial.context.communication.controller.views.ChatRoomView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.PagedModel;
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
    public ResponseEntity<PagedModel<ChatMessageView>> getMessages(@PathVariable(name = "chatId") UUID chatId,
                                                                   @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                   @RequestParam(name = "size", defaultValue = "20") Integer size) {
        return ResponseEntity.ok(chatRestControllerBridge.getMessages(chatId, PageRequest.of(page, size)));
    }
}
