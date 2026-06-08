package com.zima.zimasocial.context.social.post.api.payload;

import com.zima.zimasocial.entity.MediaType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Post creation request")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostPayload {
    @Schema(description = "Post Type")
    private MediaType type = MediaType.any;

    @Schema(description = "Post Content")
    private String content;

    @Schema(description = "Selected media id")
    private String mediaId;
}



