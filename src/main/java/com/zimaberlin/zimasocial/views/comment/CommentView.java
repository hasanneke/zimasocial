package com.zimaberlin.zimasocial.views.comment;
import com.zimaberlin.zimasocial.views.user.BasicUserView;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentView {
    private Long id;
    private String content;
    private BasicUserView basicUserView;
    private Integer replyCount = 0;
    private Integer likeCount = 0;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}