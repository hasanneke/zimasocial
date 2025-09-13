package com.zimaberlin.zimasocial.context.social.infastructure.adapter;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.comment.CommentLike;
import com.zimaberlin.zimasocial.context.social.post.PostLike;
import com.zimaberlin.zimasocial.entity.LikeEntity;
import com.zimaberlin.zimasocial.context.social.like.Like;
import org.springframework.stereotype.Component;

@Component
public class LikeAdapter {
    public Like convertLikeEntityToLikeForPost(LikeEntity like) {
        if(like == null) return null;
        return new PostLike(like.getPostId(), new AuthorId(like.getUserId()));
    }

    public CommentLike convertLikeEntityToLikeForComment(LikeEntity like) {
        if(like == null) return null;
        return new CommentLike(like.getPostId(), new AuthorId(like.getUserId()), like.getCommentId());
    }
}
