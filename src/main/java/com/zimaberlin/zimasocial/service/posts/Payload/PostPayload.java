package com.zimaberlin.zimasocial.service.posts.Payload;

import com.zimaberlin.zimasocial.context.social.media.movie.MovieMediaType;
import com.zimaberlin.zimasocial.entity.PostType;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Selected media id")
    private String mediaId;

    @Schema(description = "Selected TV media type (eg: movie, tv)")
    private MovieMediaType movieMediaType;
}
