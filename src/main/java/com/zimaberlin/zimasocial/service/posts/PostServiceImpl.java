package com.zimaberlin.zimasocial.service.posts;
import com.zimaberlin.zimasocial.aop.ResourceAcess.HasCommentAccess;
import com.zimaberlin.zimasocial.aop.ResourceAcess.HasPostAccess;
import com.zimaberlin.zimasocial.views.comment.CommentView;
import com.zimaberlin.zimasocial.views.post.PostView;
import com.zimaberlin.zimasocial.config.CustomUserDetails;
import com.zimaberlin.zimasocial.events.CommentLikedEvent;
import com.zimaberlin.zimasocial.events.CommentUnlikedEvent;
import com.zimaberlin.zimasocial.events.PostLikedEvent;
import com.zimaberlin.zimasocial.exception.ConflictException;
import com.zimaberlin.zimasocial.repository.CommentRepository;
import com.zimaberlin.zimasocial.repository.UserRepository;
import com.zimaberlin.zimasocial.service.posts.Payload.PostPayload;
import com.zimaberlin.zimasocial.entity.*;
import com.zimaberlin.zimasocial.exception.ResourceNotFoundException;
import com.zimaberlin.zimasocial.repository.LikeRepository;
import com.zimaberlin.zimasocial.repository.PostRepository;
import com.zimaberlin.zimasocial.service.posts.Payload.CommentPayload;
import com.zimaberlin.zimasocial.utility.CurrentUser;
import com.zimaberlin.zimasocial.utility.CustomCommentMapper;
import com.zimaberlin.zimasocial.utility.PostMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final ApplicationEventPublisher eventPublisher;

    public PostServiceImpl(PostRepository postRepository, LikeRepository likeRepository, CommentRepository commentRepository, UserRepository userRepository, PostMapper postMapper, ApplicationEventPublisher eventPublisher) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Page<PostView> getPosts(int page, int size, String slug, PostType type) {
        UserEntity user = userRepository.findBySlug(slug).orElseThrow(()->new ResourceNotFoundException("User not found"));
        Pageable pageable = PageRequest.of(page, size);
        Page<PostEntity> postsPage;

        if(type != null){
            postsPage = postRepository.findByUserAndType(pageable, user, type);
        }else{
            postsPage = postRepository.findByUser(pageable, user);
        }

        List<PostView> postViews = fillWithIsLiked(postsPage);

        return new PageImpl<>(postViews, pageable, postsPage.getTotalElements());
    }

    @Override
    public Page<PostView> getPosts(int page, int size, PostType type) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostEntity> postsPage;
        if(type == null){
          postsPage = postRepository.findAll(pageable);
        }else{
            postsPage = postRepository.findByType(pageable, type);
        }
        List<PostView> postViews = fillWithIsLiked(postsPage);
        return new PageImpl<>(postViews, pageable, postsPage.getTotalElements());
    }

    @Override
    public Page<CommentView> getComments(int page, int size, Long postId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentEntity> commentEntities = commentRepository.findByPostIdAndParentId(pageable, postId, null);
        return commentEntities.map(CustomCommentMapper::entityToDomain);
    }

    @Override
    public Page<CommentView> getCommentReplies(int page, int size, Long postId, Long commentId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentEntity> commentEntities = commentRepository.findByParentId(commentId, pageable);
        return commentEntities.map(CustomCommentMapper:: entityToDomain);
    }

    @Override
    public PostView getPost(Long postId) {
        PostEntity post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        return fillWithIsLiked(post);
    }

    @Override
    public PostView createPost(@Valid PostPayload payload) {
        UserEntity user = getCurrentUserProfile();
        PostEntity postEntity = postMapper.payloadToPostEntity(payload);
        postEntity.setUser(user);

        PostEntity createdPost = postRepository.save(postEntity);

        return postMapper.postEntityToPost(createdPost);
    }

    @Override
    @HasPostAccess(idParameterName = "id")
    public void deletePost(Long id){
        postRepository.deleteById(id);
    }

    @Override
    public void likePost(Long postId)  {
        PostEntity post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        boolean isAlreadyLiked = likeRepository.existsByUserAndPost(getCurrentUserProfile(), post);
        if(!isAlreadyLiked){
            LikeEntity like = new LikeEntity();
            like.setPost(post);
            like.setUser(getCurrentUserProfile());

            post.incrementLikeCount();

            likeRepository.save(like);
            eventPublisher.publishEvent(new PostLikedEvent(this, post));
        }else{
            throw new ConflictException("Post is already liked");
        }
    }

    @Override
    public void unlikePost(Long postId) {
        LikeEntity likeEntity = likeRepository.findByUserIdAndPostId(getCurrentUserProfile().getId(), postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post is not liked"));

        likeEntity.getPost().decrementLikeCount();

        likeRepository.delete(likeEntity);
    }

    @Override
    public CommentView commentPost(Long postId, CommentPayload payload) {
        PostEntity post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        CommentEntity comment = new CommentEntity();
        comment.setPost(post);
        comment.setUser(getCurrentUserProfile());
        comment.setContent(payload.getContent());

        post.getComments().add(comment);
        post.incrementCommentCount();

        CommentEntity commentEntity = commentRepository.save(comment);
        return CustomCommentMapper.entityToDomain(commentEntity);
    }

    @Override
    @HasCommentAccess(idParameterName = "commentId")
    public void deleteComment(Long postId, Long commentId) {
        PostEntity post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment not found"));
        post.decrementCommentCount();
        commentRepository.delete(commentEntity);
    }

    @Override
    public void likeComment(Long postId, Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment not found"));
        boolean isAlreadyLiked = likeRepository.existsByUserAndComment(getCurrentUserProfile(), comment);
        if(!isAlreadyLiked){
            LikeEntity like = new LikeEntity();
            like.setComment(comment);
            like.setPost(comment.getPost());
            like.setUser(getCurrentUserProfile());

            comment.incrementLikeCount();

            likeRepository.save(like);
            eventPublisher.publishEvent(new CommentLikedEvent(this, comment));
        }else{
            throw new ConflictException("Comment is already liked");
        }
    }

    @Override
    public void unlikeComment(Long postId, Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment not found"));

        LikeEntity like = likeRepository
                .findByUserIdAndPostIdAndCommentId(getCurrentUserProfile().getId(), postId, commentId)
                .orElseThrow(()-> new ResourceNotFoundException("Comment not liked"));

        like.getComment().decrementLikeCount();

        likeRepository.delete(like);
        eventPublisher.publishEvent(new CommentUnlikedEvent(this, comment));
    }

    @Override
    public CommentView replyComment(Long postId, Long commentId, CommentPayload payload) {
        CommentEntity parent = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment not found"));
        PostEntity post = parent.getPost();
        CommentEntity reply = new CommentEntity();

        reply.setContent(payload.getContent());
        reply.setParent(parent);
        reply.setUser(CurrentUser.getCurrentUserProfile());
        reply.setPost(post);

        parent.incrementReplyCount();
        CommentEntity comment = commentRepository.save(reply);

        return CustomCommentMapper.entityToDomain(comment);
    }

    @Override
    @HasCommentAccess(idParameterName = "replyCommentId")
    public CommentView deleteReplyComment(Long postId, Long commentId, Long replyCommentId) {
        CommentEntity reply = commentRepository.findById(replyCommentId).orElseThrow(()->new ResourceNotFoundException("Comment not found"));
        reply.getParent().decrementReplyCount();

        commentRepository.delete(reply);
        return CustomCommentMapper.entityToDomain(reply);
    }

    private List<PostView> fillWithIsLiked(Page<PostEntity> postsPage) {
        List<PostView> postViews = postsPage.getContent().stream().map(postMapper::postEntityToPost).toList();
        Object authenticationPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(authenticationPrincipal != "anonymousUser"){
            UserEntity profile =  ((CustomUserDetails) authenticationPrincipal).getProfile();
            for (int i = 0; i < postViews.size(); i++) {
                LikeEntity like = likeRepository.findByUserAndPost(profile, postsPage.getContent().get(i)).orElse(null);
                if(like != null){
                    postViews.get(i).setLiked(true);
                }
            }
        }
        return postViews;
    }

    private PostView fillWithIsLiked(PostEntity post){
        UserEntity profile = CurrentUser.getCurrentUserProfile();
        LikeEntity like = likeRepository.findByUserAndPost(profile, post).orElse(null);
        PostView domainPostView = postMapper.postEntityToPost(post);
        if(like != null){
            domainPostView.setLiked(true);;
        }
        return domainPostView;
    }

    private UserEntity getCurrentUserProfile(){
        return CurrentUser.getCurrentUserProfile();
    }
}
