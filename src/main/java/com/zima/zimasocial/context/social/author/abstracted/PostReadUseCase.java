package com.zima.zimasocial.context.social.author.abstracted;

import com.zima.zimasocial.context.social.post.api.payload.FeedFilterPlain;
import com.zima.zimasocial.context.social.post.api.views.LikeView;
import com.zima.zimasocial.context.social.post.api.views.CommentView;
import com.zima.zimasocial.context.social.post.api.views.PostView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import java.util.List;

public interface PostReadUseCase {
    List<PostView> getFeed(FeedFilterPlain filterPlain);

    Page<LikeView> getAllPostLikes(Long postId, Pageable pageable);

    Page<LikeView> getAllCommentLikes(Long commentId, Pageable pageable);
    PagedModel<CommentView> getCommentReplies(int page, int size, Long commentId) throws NoSuchMethodException;
    PagedModel<CommentView> getComments(int page, int size, Long postId) throws NoSuchMethodException;
}
