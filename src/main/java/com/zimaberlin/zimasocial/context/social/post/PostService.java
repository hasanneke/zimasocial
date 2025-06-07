package com.zimaberlin.zimasocial.context.social.post;

import com.zimaberlin.zimasocial.aop.ResourceAcess.HasPostAccess;
import com.zimaberlin.zimasocial.context.social.comment.Comment;
import com.zimaberlin.zimasocial.exception.ConflictException;
import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.comment.CommentRepository;
import com.zimaberlin.zimasocial.context.social.values.Like;
import com.zimaberlin.zimasocial.context.social.author.AuthorRepository;
import com.zimaberlin.zimasocial.context.social.repository.LikeRepository;
import com.zimaberlin.zimasocial.service.posts.exception.CommentNotFoundException;
import com.zimaberlin.zimasocial.service.posts.exception.PostNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

//@Service
public class PostService {
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository, LikeRepository likeRepository, CommentRepository commentRepository) {
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

    @HasPostAccess(idParameterName = "id")
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
    public Comment replyComment(Long postId, Long parentId, String content) {
        Comment parent = commentRepository.findById(parentId).orElseThrow(CommentNotFoundException::new);
        Author author = authorRepository.getAuthenticatedAuthor();
        Comment reply = new Comment(postId, parentId, author.getAuthorId(), content);
        Comment savedReply  = commentRepository.save(reply);
        parent.replied(savedReply);
        return savedReply;
    }
}
