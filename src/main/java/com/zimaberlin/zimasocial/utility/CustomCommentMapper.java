package com.zimaberlin.zimasocial.utility;

import com.zimaberlin.zimasocial.views.comment.CommentView;
import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.UserEntity;
import org.hibernate.Hibernate;

public class CustomCommentMapper {
    public static CommentView entityToDomain(CommentEntity entity){
        if(entity == null){
            return null;
        }
        // Create domain instance
        CommentView commentView = new CommentView();
        // UnProxy Proxies
        UserEntity user = (UserEntity) Hibernate.unproxy(entity.getUser());
        // Set domain values
        commentView.setContent(entity.getContent());
        commentView.setId(entity.getId());
        commentView.setUserView(CustomUserMapper.entityToDomain(user));
        commentView.setUpdatedAt(entity.getUpdatedAt());
        commentView.setCreatedAt(entity.getCreatedAt());
        commentView.setLikeCount(entity.getLikeCount());
        commentView.setReplyCount(entity.getReplyCount());

        return commentView;
    }
}
