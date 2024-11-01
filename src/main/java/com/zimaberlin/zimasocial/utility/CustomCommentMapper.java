package com.zimaberlin.zimasocial.utility;

import com.zimaberlin.zimasocial.domain.Comment;
import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.UserEntity;
import com.zimaberlin.zimasocial.service.Posts.Payload.CommentPayload;
import org.hibernate.Hibernate;

public class CustomCommentMapper {
    public static Comment entityToDomain(CommentEntity entity){
        if(entity == null){
            return null;
        }
        // Create domain instance
        Comment comment = new Comment();
        // UnProxy Proxies
        UserEntity user = (UserEntity) Hibernate.unproxy(entity.getUser());
        // Set domain values
        comment.setContent(entity.getContent());
        comment.setId(entity.getId());
        comment.setUser(CustomUserMapper.entityToDomain(user));
        comment.setUpdatedAt(entity.getUpdatedAt());
        comment.setCreatedAt(entity.getCreatedAt());
        comment.setLikeCount(entity.getLikeCount());
        comment.setReplyCount(entity.getReplyCount());

        return comment;
    }
}
