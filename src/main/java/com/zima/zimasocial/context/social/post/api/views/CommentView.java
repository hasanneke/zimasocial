package com.zima.zimasocial.context.social.post.api.views;
import com.zima.zimasocial.context.social.author.api.view.AuthorView;
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