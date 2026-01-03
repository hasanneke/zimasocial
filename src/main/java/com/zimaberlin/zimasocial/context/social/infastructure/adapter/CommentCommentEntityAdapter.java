package com.zimaberlin.zimasocial.context.social.infastructure.adapter;

import com.zimaberlin.zimasocial.context.social.author.value.AuthorId;
import com.zimaberlin.zimasocial.context.social.comment.Comment;
import com.zimaberlin.zimasocial.entity.CommentEntity;
import org.springframework.stereotype.Component;

@Component
public class CommentCommentEntityAdapter {
   public Comment convertCommentEntityToComment(CommentEntity comment) {
       if(comment == null) return null;
       return new Comment(
                comment.getId(),
                comment.getParent() != null ? comment.getParent().getId() : null,
                comment.getPost().getId(),
                new AuthorId( comment.getUser().getId()),
                comment.getContent(),
                comment.getLikeCount(),
                comment.getReplyCount(),
                comment.getCreatedAt(),
                comment.getUpdatedAt());
    }
}
