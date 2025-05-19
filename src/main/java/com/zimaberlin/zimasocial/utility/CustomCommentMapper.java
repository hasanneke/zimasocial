package com.zimaberlin.zimasocial.utility;

import com.zimaberlin.zimasocial.views.comment.CommentView;
import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomCommentMapper {
    private final CustomUserMapper userMapper;
    @Autowired
    public CustomCommentMapper(CustomUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public CommentView entityToDomain(CommentEntity entity){
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
        commentView.setUserView(userMapper.entityToDomain(user));
        commentView.setUpdatedAt(entity.getUpdatedAt());
        commentView.setCreatedAt(entity.getCreatedAt());
        commentView.setLikeCount(entity.getLikeCount());
        commentView.setReplyCount(entity.getReplyCount());

        return commentView;
    }
}
