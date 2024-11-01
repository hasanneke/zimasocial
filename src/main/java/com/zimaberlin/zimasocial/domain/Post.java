package com.zimaberlin.zimasocial.domain;

import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Post {
    private Long id;
    private String content;
    private String url;
    private PostType type;
    private int likeCount = 0;
    private int commentCount = 0;
    private boolean isLiked = false;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}