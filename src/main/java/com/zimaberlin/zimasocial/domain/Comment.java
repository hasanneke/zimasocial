package com.zimaberlin.zimasocial.domain;
import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Comment {
    private Long id;
    private Post post;
    private UserEntity user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Comment parent;
}