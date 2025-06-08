package com.zimaberlin.zimasocial.views.comment;
import com.zimaberlin.zimasocial.context.social.api.author.AuthorView;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentView {
    private Long id;
    private String content;
    private AuthorView authorView;
    private Integer replyCount = 0;
    private Integer likeCount = 0;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isLiked;
    private Boolean isReported;
}