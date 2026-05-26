package com.zima.zimasocial.context.social2.application;

import com.zima.zimasocial.context.social.api.FeedFilterPlain;
import com.zima.zimasocial.context.social.api.dto.LikeDTO;
import com.zima.zimasocial.context.social2.api.views.CommentView;
import com.zima.zimasocial.context.social2.api.views.PostView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import java.util.List;

public interface PostReadUseCase {
    List<PostView> getFeed(FeedFilterPlain filterPlain);

    Page<LikeDTO> getAllPostLikes(Long postId, Pageable pageable);

    Page<LikeDTO> getAllCommentLikes(Long commentId, Pageable pageable);
    PagedModel<CommentView> getCommentReplies(int page, int size, Long commentId) throws NoSuchMethodException;
    PagedModel<CommentView> getComments(int page, int size, Long postId) throws NoSuchMethodException;
}
