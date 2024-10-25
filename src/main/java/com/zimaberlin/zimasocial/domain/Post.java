package com.zimaberlin.zimasocial.domain;

import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.entity.ProfileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private Long id;
    private String content;
    private String url;
    private PostType type;
    private int likeCount = 0;
    private int commentCount = 0;
    private boolean isLiked = false;
    private ProfileEntity user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}