package com.zima.zimasocial.views.comment;
import com.zima.zimasocial.context.social.api.author.AuthorView;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentView {
    private Long id;
    private String content;
    private AuthorView author;
    private Integer replyCount = 0;
    private Integer likeCount = 0;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isLiked;
    private Boolean isReported;
    private UUID mediaId;
}