package com.zimaberlin.zimasocial.context.social.post;

import com.zimaberlin.zimasocial.aop.ResourceAcess.HasPostAccess;
import com.zimaberlin.zimasocial.context.social.comment.Comment;
import com.zimaberlin.zimasocial.context.social.comment.CommentLike;
import com.zimaberlin.zimasocial.entity.CommentEntity;
import com.zimaberlin.zimasocial.entity.LikeEntity;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.exception.ConflictException;
import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.comment.CommentRepository;
import com.zimaberlin.zimasocial.context.social.like.Like;
import com.zimaberlin.zimasocial.context.social.author.AuthorRepository;
import com.zimaberlin.zimasocial.context.social.like.LikeRepository;
import com.zimaberlin.zimasocial.exception.DataNotFoundException;
import com.zimaberlin.zimasocial.service.posts.Payload.CommentPayload;
import com.zimaberlin.zimasocial.service.posts.exception.CommentNotFoundException;
import com.zimaberlin.zimasocial.service.posts.exception.PostNotFoundException;
import com.zimaberlin.zimasocial.utility.CurrentUser;
import com.zimaberlin.zimasocial.views.comment.CommentView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PostServiceBeta {
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    @Autowired
    public PostServiceBeta(PostRepository postRepository, AuthorRepository authorRepository, LikeRepository likeRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
        this.likeRepository = likeRepository;
        this.commentRepository=  commentRepository;
    }
    @Transactional
    public Post createPost(CreatePost createPost) {
        Author author = authorRepository.getAuthenticatedAuthor();
        Post post = new Post(createPost.content(), createPost.type(), author.getAuthorId());
        return postRepository.save(post);
    }

    @Transactional
    public void like(Long postId) {
        Author author = authorRepository.getAuthenticatedAuthor();
        Optional<Like> like = likeRepository.findByPostIdAndAuthorId(postId, author.getAuthorId());
        if(like.isPresent()){
            throw new ConflictException("Post is already liked");
        }
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        post.like(author);
        Like newLike = new PostLike(postId, author.getAuthorId());
        likeRepository.save(newLike);
    }

    @Transactional
    public void unlikePost(Long postId) {
        Author author = authorRepository.getAuthenticatedAuthor();
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        Optional<Like> like = likeRepository.findByPostIdAndAuthorId(postId, author.getAuthorId());
        if(like.isEmpty()){
            throw new ConflictException("Post is not liked");
        }
        post.unliked();
        postRepository.save(post);
        likeRepository.delete(like.get());
    }

    @Transactional
    public void delete(Long id){
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        postRepository.delete(post);
    }

    @Transactional
    public Comment comment(Long postId, String content) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        Author author = authorRepository.getAuthenticatedAuthor();
        Comment comment = new Comment(postId, null, author.getAuthorId(), content);
        Comment createdComment = commentRepository.save(comment);
        post.commented(author);
        return createdComment;
    }

    @Transactional
    public void removeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        Post post = postRepository.findById(comment.getPostId()).orElseThrow(PostNotFoundException::new);
        post.commentRemoved();
        postRepository.save(post);
    }



    @Transactional
    public void likeComment(Long postId, Long commentId) {
        Author authenticatedAuthor = authorRepository.getAuthenticatedAuthor();
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new DataNotFoundException("Comment not found"));
        Optional<Like> checkLike = likeRepository.findByCommentIdAndAuthorId(commentId, authenticatedAuthor.getAuthorId());

        if(checkLike.isEmpty()){
            Like like = new CommentLike(postId, commentId, authenticatedAuthor.getAuthorId() );
            comment.like(authenticatedAuthor);
            likeRepository.save(like);
        }else{
            throw new ConflictException("Comment is already liked");
        }
    }

    @Transactional
    public void unlikeComment(Long commentId) {
        Author authenticatedAuthor = authorRepository.getAuthenticatedAuthor();
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new DataNotFoundException("Comment not found"));
        Optional<Like> checkLike = likeRepository.findByCommentIdAndAuthorId(commentId, authenticatedAuthor.getAuthorId());
        if(checkLike.isPresent()){
            comment.unlike();
            likeRepository.delete(checkLike.get());
        }else{
            throw new DataNotFoundException("Comment not liked");
        }
    }

    @Transactional
    public Comment replyComment(Long postId, Long parentId, String content) {
        Comment parent = commentRepository.findById(parentId).orElseThrow(CommentNotFoundException::new);
        Author author = authorRepository.getAuthenticatedAuthor();
        Comment reply = new Comment(postId, parentId, author.getAuthorId(), content);
        Comment savedReply  = commentRepository.save(reply);
        parent.reply(savedReply);
        return savedReply;
    }

    @Transactional
    public void deleteReply(Long replyId) {
        Comment reply = commentRepository.findById(replyId).orElseThrow(CommentNotFoundException::new);
        Comment parent = commentRepository.findById(reply.getParentCommentId()).orElseThrow(CommentNotFoundException::new);
        commentRepository.delete(reply);
        parent.removeReply(reply);
        commentRepository.save(parent);
    }
}
