package com.zimaberlin.zimasocial.service.posts;

import com.zimaberlin.zimasocial.views.comment.CommentView;
import com.zimaberlin.zimasocial.views.post.PostView;
import com.zimaberlin.zimasocial.service.posts.Payload.PostPayload;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.service.posts.Payload.CommentPayload;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    // GET POSTS FOR SLUG
    Page<PostView> getPosts(int page, int size, String slug, PostType type);
    // GET POSTS FOR TYPE
    Page<PostView> getPosts(int page, int size, PostType type);
    // GET COMMENTS
    Page<CommentView> getComments(int page, int size, Long postId);
    // GET COMMENT REPLIES
    Page<CommentView> getCommentReplies(int page, int size, Long postId, Long commentId);
    // GET POST
    PostView getPost(Long id);
    // CREATE POST
    PostView createPost(@Valid PostPayload payload);
    // DELETE POST
    void deletePost(Long id);
    // LIKE POST
    void likePost(Long postId) throws BadRequestException;
    // UNLIKE POST
    void unlikePost(Long postId) throws BadRequestException;
    // COMMENT POST
    CommentView commentPost(Long postId, CommentPayload payload);
    // DELETE COMMENT
    void deleteComment(Long postId, Long commentId);
    // LIKE COMMENT
    void likeComment(Long postId, Long commentId);
    // UNLIKE COMMENT
    void unlikeComment(Long postId, Long commentId);
    // REPLY COMMENT
    CommentView replyComment(Long postId, Long commentId, CommentPayload payload);
    // DELETE REPLY COMMENT
    CommentView deleteReplyComment(Long postId, Long commentId, Long replyCommentId);
    List<PostView> getTodaysPosts();
}
