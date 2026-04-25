package com.zima.zimasocial.service.posts.Payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Schema(description = "Comment creation request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentPayload {
    private String content;
    private UUID mediaId;
}
