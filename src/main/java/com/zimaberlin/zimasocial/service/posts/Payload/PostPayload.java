package com.zimaberlin.zimasocial.service.posts.Payload;

import com.zimaberlin.zimasocial.entity.PostType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Schema(description = "Post creation request")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostPayload {
    @Schema(description = "Post Type")
    private PostType type = PostType.any;

    @Schema(description = "Post Content")
    private String content;

    @Schema(description = "Post Url")
    private String url;
}
