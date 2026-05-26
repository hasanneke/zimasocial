package com.zima.zimasocial.context.social2.application;

import com.zima.zimasocial.context.social.post.value.CreatePost;
import com.zima.zimasocial.context.social2.domain.entity.Post;
import com.zima.zimasocial.context.social2.api.views.CommentView;
import com.zima.zimasocial.context.social2.api.views.PostView;

import java.util.UUID;

public interface PostUseCase {

    PostView createPost(CreatePost createPost);

    void likePost(Long postId);

    void unlikePost(Long postId);

    void deletePost(Long id);

    CommentView comment(Long postId, String content, UUID mediaId);

    void removeComment(Long commentId);

    void likeComment(Long commentId);

    void unlikeComment(Long commentId);

    CommentView replyComment(Long parentId, String content, UUID mediaId);

    void deleteReply(Long replyId);

    void makePostInvisible(Post post);

    void makePostVisible(Post post);
}