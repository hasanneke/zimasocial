package com.zima.zimasocial.context.social.infastructure.adapter;
import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.context.social.comment.CommentLike;
import com.zima.zimasocial.context.social.post.value.PostLike;
import com.zima.zimasocial.entity.LikeEntity;
import com.zima.zimasocial.context.social.like.LikeDomain;
import org.springframework.stereotype.Component;

@Component
public class LikeAdapter {
    public LikeDomain convertLikeEntityToLikeForPost(LikeEntity like) {
        if(like == null) return null;
        return new PostLike(like.getPostId(), new AuthorDomainId(like.getUserId()));
    }

    public CommentLike convertLikeEntityToLikeForComment(LikeEntity like) {
        if(like == null) return null;
        return new CommentLike(like.getPostId(), new AuthorDomainId(like.getUserId()), like.getCommentId());
    }
}
