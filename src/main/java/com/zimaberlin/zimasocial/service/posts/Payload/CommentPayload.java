package com.zimaberlin.zimasocial.service.posts.Payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Schema(description = "Comment creation request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentPayload {
    @NotBlank(message = "Content is required")
    private String content;
}
