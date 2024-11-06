package com.zimaberlin.zimasocial.views.post;

import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.views.user.UserView;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostView {
    private Long id;
    private String content;
    private String url;
    private PostType type;
    private int likeCount = 0;
    private int commentCount = 0;
    private boolean isLiked = false;
    private UserView user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}