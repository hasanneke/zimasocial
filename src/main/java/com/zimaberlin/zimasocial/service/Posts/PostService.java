package com.zimaberlin.zimasocial.service.Posts;

import com.zimaberlin.zimasocial.domain.Comment;
import com.zimaberlin.zimasocial.domain.Post;
import com.zimaberlin.zimasocial.service.Posts.Payload.PostPayload;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.service.Posts.Payload.CommentPayload;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;

interface PostService {
    // GET POSTS FOR SLUG
    Page<Post> getPosts(int page, int size, String slug);
    // GET POSTS FOR TYPE
    Page<Post> getPosts(int page, int size, PostType type);
    // GET COMMENTS
    Page<Comment> getComments(int page, int size, Long postId);
    // GET COMMENT REPLIES
    Page<Comment> getCommentReplies(int page, int size, Long postId, Long commentId);
    // GET POST
    Post getPost(Long id);
    // CREATE POST
    Post createPost(PostPayload payload);
    // DELETE POST
    void deletePost(Long id);
    // LIKE POST
    void likePost(Long postId) throws BadRequestException;
    // UNLIKE POST
    void unlikePost(Long postId) throws BadRequestException;
    // COMMENT POST
    Comment commentPost(Long postId, CommentPayload payload);
    // DELETE COMMENT
    void deleteComment(Long postId, Long commentId);
    // LIKE COMMENT
    void likeComment(Long postId, Long commentId);
    // UNLIKE COMMENT
    void unlikeComment(Long postId, Long commentId);
    // REPLY COMMENT
    Comment replyComment(Long postId, Long commentId, CommentPayload payload);
}
