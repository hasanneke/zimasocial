package com.zimaberlin.zimasocial.service.Posts;
import com.zimaberlin.zimasocial.aop.ResourceAcess.HasPostAccess;
import com.zimaberlin.zimasocial.domain.Comment;
import com.zimaberlin.zimasocial.domain.Post;
import com.zimaberlin.zimasocial.config.CustomUserDetails;
import com.zimaberlin.zimasocial.exception.ConflictException;
import com.zimaberlin.zimasocial.repository.CommentRepository;
import com.zimaberlin.zimasocial.repository.UserRepository;
import com.zimaberlin.zimasocial.service.Posts.Payload.PostPayload;
import com.zimaberlin.zimasocial.entity.*;
import com.zimaberlin.zimasocial.exception.ResourceNotFoundException;
import com.zimaberlin.zimasocial.repository.LikeRepository;
import com.zimaberlin.zimasocial.repository.PostRepository;
import com.zimaberlin.zimasocial.service.Posts.Payload.CommentPayload;
import com.zimaberlin.zimasocial.utility.CommentMapper;
import com.zimaberlin.zimasocial.utility.CurrentUser;
import com.zimaberlin.zimasocial.utility.PostMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, LikeRepository likeRepository, CommentRepository commentRepository, CommentMapper commentMapper, PostMapper postMapper, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
    }

    @Override
    public Page<Post> getPosts(int page, int size, String slug) {
        UserEntity user = userRepository.findBySlug(slug).orElseThrow(()->new ResourceNotFoundException("User not found"));
        Pageable pageable = PageRequest.of(page, size);
        Page<PostEntity> postsPage;
        postsPage = postRepository.findByUser(pageable, user);

        List<Post> posts = fillWithIsLiked(postsPage);

        return new PageImpl<>(posts, pageable, postsPage.getTotalElements());
    }


    @Override
    public Page<Post> getPosts(int page, int size, PostType type) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostEntity> postsPage;
        if(type == null){
          postsPage = postRepository.findAll(pageable);
        }else{
            postsPage = postRepository.findByType(pageable, type);
        }

        List<Post> posts = fillWithIsLiked(postsPage);

        return new PageImpl<>(posts, pageable, postsPage.getTotalElements());
    }

    @Override
    public Page<Comment> getComments(int page, int size, Long postId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentEntity> commentEntities = commentRepository.findByPostId(postId, pageable);
        return commentEntities.map(commentMapper::entityToComment);
    }

    @Override
    public Page<Comment> getCommentReplies(int page, int size, Long postId, Long commentId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentEntity> commentEntities = commentRepository.findByParentId(commentId, pageable);
        return commentEntities.map(commentMapper::entityToComment);
    }

    @Override
    public Post getPost(Long id) {
        PostEntity post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        return postMapper.postEntityToPost(post);
    }

    @Override
    public Post createPost(@Validated PostPayload payload) {
        UserEntity user = getCurrentUserProfile();
        PostEntity postEntity = postMapper.payloadToPostEntity(payload);
        postEntity.setUser(user);

        PostEntity createdPost = postRepository.save(postEntity);

        return postMapper.postEntityToPost(createdPost);
    }
    // TODO: Check if post is private. If so, check if the requester is a follower
    @Override
    @HasPostAccess(idParameterName = "id")
    public void deletePost(Long id){
        postRepository.deleteById(id);
    }

    // TODO: Check if post is private. If so, check if the requester is a follower
    @Override
    @Transactional
    public void likePost(Long postId)  {
        PostEntity post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        boolean isAlreadyLiked = likeRepository.existsByUserAndPost(getCurrentUserProfile(), post);
        if(!isAlreadyLiked){
            LikeEntity like = new LikeEntity();
            like.setPost(post);
            like.setUser(getCurrentUserProfile());
            likeRepository.save(like);
        }else{
            throw new ConflictException("Post is already liked");
        }
    }
    // TODO: Check if post is private. If so, check if the requester is a follower
    @Override
    public void unlikePost(Long postId) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post not found"));
        LikeEntity likeEntity = likeRepository.findByUserAndPost(getCurrentUserProfile(), post)
                .orElseThrow(()-> new ResourceNotFoundException("Post is not liked"));

        likeRepository.delete(likeEntity);
    }
    // TODO: Check if post is private. If so, check if the requester is a follower
    @Override
    public Comment commentPost(Long postId, CommentPayload payload) {
        PostEntity post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        CommentEntity comment = new CommentEntity();
        comment.setPost(post);
        comment.setUser(getCurrentUserProfile());
        CommentEntity commentEntity = commentRepository.save(comment);
        return commentMapper.entityToComment(commentEntity);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment not found"));
        commentRepository.delete(commentEntity);
    }

    // TODO: Check if post is private. If so, check if the requester is a follower
    @Override
    public void likeComment(Long postId, Long commentId) {
        PostEntity post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment not found"));
        boolean isAlreadyLiked = likeRepository.existsByUserAndPostAndComment(getCurrentUserProfile(), post, commentEntity);
        if(!isAlreadyLiked){
            LikeEntity like = new LikeEntity();
            like.setComment(commentEntity);
            like.setPost(post);
            like.setUser(getCurrentUserProfile());
        }else{
            throw new ConflictException("Comment is already liked");
        }
    }

    // TODO: Check if post is private. If so, check if the requester is a follower
    @Override
    public void unlikeComment(Long postId, Long commentId) {
        LikeEntity like = likeRepository.findByUserIdAndPostIdAndCommentId(getCurrentUserProfile().getId(), postId, commentId).orElseThrow(()-> new ResourceNotFoundException("Post not liked"));
        likeRepository.delete(like);
    }

    @Override
    public Comment replyComment(Long postId, Long commentId, CommentPayload payload) {
        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment not found"));

        CommentEntity reply = commentMapper.payloadToCommentEntity(payload);
        reply.setParent(commentEntity);
        reply.setUser(CurrentUser.getCurrentUserProfile());
        CommentEntity comment = commentRepository.save(reply);

        return commentMapper.entityToComment(comment);
    }



    private List<Post> fillWithIsLiked(Page<PostEntity> postsPage) {
        List<Post> posts = postsPage.getContent().stream().map(postMapper::postEntityToPost).toList();
        Object authenticationPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(authenticationPrincipal != "anonymousUser"){
            UserEntity profile =  ((CustomUserDetails) authenticationPrincipal).getProfile();
            for (int i = 0; i < posts.size(); i++) {
                LikeEntity like = likeRepository.findByUserAndPost(profile, postsPage.getContent().get(i)).orElse(null);
                if(like != null){
                    posts.get(i).setLiked(true);
                }
            }
        }
        return posts;
    }

    private UserEntity getCurrentUserProfile(){
        return CurrentUser.getCurrentUserProfile();
    }
}
