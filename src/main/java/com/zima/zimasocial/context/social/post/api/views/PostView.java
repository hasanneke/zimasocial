package com.zima.zimasocial.context.social.post.api.views;

import com.zima.zimasocial.context.social.author.api.view.AuthorView;
import com.zima.zimasocial.entity.MediaType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PostView {
    private Long id;
    private String content;
    private MediaType type;
    private int likeCount = 0;
    private int commentCount = 0;
    private boolean isLiked = false;
    private AuthorView user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isReported;
    private UUID mediaId;
    private Integer score;

    public PostView(PostDTO postDTO) {
        this.id = postDTO.getId();
        this.content = postDTO.getContent();
        this.likeCount = postDTO.getLikeCount().intValue();
        this.commentCount = postDTO.getCommentCount().intValue();
        this.isLiked = postDTO.getIsLiked();
        this.user = new AuthorView(postDTO);
        this.createdAt = postDTO.getCreatedAt();
        this.updatedAt = postDTO.getUpdatedAt();
        this.isReported = postDTO.getIsReported();
        this.mediaId = postDTO.getMediaId();
        this.type = MediaType.valueOf(postDTO.getType());
        this.score = postDTO.getScore().intValue();
    }
}