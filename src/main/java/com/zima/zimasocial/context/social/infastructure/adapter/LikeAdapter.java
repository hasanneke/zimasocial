package com.zima.zimasocial.context.social.infastructure.adapter;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.comment.CommentLike;
import com.zima.zimasocial.context.social.post.value.PostLike;
import com.zima.zimasocial.entity.LikeEntity;
import com.zima.zimasocial.context.social.like.Like;
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
