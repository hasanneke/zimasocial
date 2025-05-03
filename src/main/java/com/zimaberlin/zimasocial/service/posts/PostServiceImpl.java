package com.zimaberlin.zimasocial.service.posts;
import com.zimaberlin.zimasocial.aop.ResourceAcess.HasCommentAccess;
import com.zimaberlin.zimasocial.aop.ResourceAcess.HasPostAccess;
import com.zimaberlin.zimasocial.events.*;
import com.zimaberlin.zimasocial.repository.*;
import com.zimaberlin.zimasocial.service.notification.NotificationService;
import com.zimaberlin.zimasocial.utility.CustomPostMapper;
import com.zimaberlin.zimasocial.views.comment.CommentView;
import com.zimaberlin.zimasocial.views.post.PostView;
import com.zimaberlin.zimasocial.config.CustomUserDetails;
import com.zimaberlin.zimasocial.exception.ConflictException;
import com.zimaberlin.zimasocial.service.posts.Payload.PostPayload;
import com.zimaberlin.zimasocial.entity.*;
import com.zimaberlin.zimasocial.exception.ResourceNotFoundException;
import com.zimaberlin.zimasocial.service.posts.Payload.CommentPayload;
import com.zimaberlin.zimasocial.utility.CurrentUser;
import com.zimaberlin.zimasocial.utility.CustomCommentMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificationService notificationService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           LikeRepository likeRepository,
                           CommentRepository commentRepository,
                           UserRepository userRepository,
                           ApplicationEventPublisher eventPublisher,
                           NotificationService notificationService) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.notificationService = notificationService;
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

        List<PostView> postViews = fillPostsWithIsLiked(postsPage);

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
        List<PostView> postViews = fillPostsWithIsLiked(postsPage);
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
        return fillPostsWithIsLiked(post);
    }

    @Override
    @Transactional
    public PostView createPost(@Valid PostPayload payload) {
        UserEntity userObject = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getProfile();
        UserEntity user = userRepository.findById(userObject.getId()).orElseThrow(()->new ResourceNotFoundException("User not found"));

        if ( payload == null ) {
            return null;
        }

        PostEntity.PostEntityBuilder postEntity = PostEntity.builder();
        postEntity.content( payload.getContent() );
        postEntity.url( payload.getUrl() );
        postEntity.type( payload.getType() );
        postEntity.user(user);

        PostEntity createdPost = postRepository.save(postEntity.build());
        return CustomPostMapper.postEntityToPost(createdPost);
    }

    @Override
    @HasPostAccess(idParameterName = "id")
    public void deletePost(Long id){
        postRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void likePost(Long postId)  {
        PostEntity post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        boolean isAlreadyLiked = likeRepository.existsByUserAndPost(getCurrentUserProfile(), post);
        UserEntity currentUser = getCurrentUserProfile();
        if(!isAlreadyLiked){
            LikeEntity like = new LikeEntity();
            like.setPost(post);
            like.setUser(currentUser);
            post.incrementLikeCount();
            likeRepository.save(like);
            boolean selfLiked = post.getUser().equals(currentUser);
            if(!selfLiked){
                notificationService.sendPostLikedNotification(like);
            }
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
    @Transactional
    public CommentView commentPost(Long postId, CommentPayload payload) {
        UserEntity currentUser = getCurrentUserProfile();
        PostEntity post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        CommentEntity comment = new CommentEntity();
        comment.setPost(post);
        comment.setUser(currentUser);
        comment.setContent(payload.getContent());

        post.getComments().add(comment);
        post.incrementCommentCount();

        CommentEntity commentEntity = commentRepository.save(comment);
        boolean selfCommented = currentUser.equals(post.getUser());

        if (!selfCommented){
            notificationService.sendPostCommentedNotification(commentEntity);
        }

        return CustomCommentMapper.entityToDomain(commentEntity);
    }

    @Override
    @HasCommentAccess(idParameterName = "commentId")
    @Transactional
    public void deleteComment(Long postId, Long commentId) {
        PostEntity post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment not found"));
        post.decrementCommentCount();
        commentRepository.delete(commentEntity);
    }

    @Override
    @Transactional
    public void likeComment(Long postId, Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment not found"));
        boolean isAlreadyLiked = likeRepository.existsByUserAndComment(getCurrentUserProfile(), comment);
        UserEntity currentUser = getCurrentUserProfile();

        if(!isAlreadyLiked){
            LikeEntity like = new LikeEntity();
            like.setComment(comment);
            like.setPost(comment.getPost());
            like.setUser(currentUser);

            comment.incrementLikeCount();

            likeRepository.save(like);
            boolean selfLiked = getCurrentUserProfile().equals(comment.getUser());
            if(!selfLiked){
                notificationService.sendCommentLikedNotification(comment);
            }
        }else{
            throw new ConflictException("Comment is already liked");
        }
    }

    @Override
    @Transactional
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
    @Transactional
    public CommentView replyComment(Long postId, Long commentId, CommentPayload payload) {
        CommentEntity parent = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment not found"));
        PostEntity post = parent.getPost();
        UserEntity currentUser = getCurrentUserProfile();

        CommentEntity reply = new CommentEntity();

        reply.setContent(payload.getContent());
        reply.setParent(parent);
        reply.setUser(currentUser);
        reply.setPost(post);

        parent.incrementReplyCount();
        CommentEntity comment = commentRepository.save(reply);
        if(currentUser.equals(parent.getUser())){
            notificationService.sendCommentRepliedNotification(reply);
        }
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

    private List<PostView> fillPostsWithIsLiked(Page<PostEntity> postsPage) {
        List<PostView> postViews = postsPage.getContent().stream().map(CustomPostMapper::postEntityToPost).toList();
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

    private PostView fillPostsWithIsLiked(PostEntity post){
        UserEntity profile = CurrentUser.getCurrentUserProfile();
        LikeEntity like = likeRepository.findByUserAndPost(profile, post).orElse(null);
        PostView domainPostView = CustomPostMapper.postEntityToPost(post);
        if(like != null){
            domainPostView.setLiked(true);;
        }
        return domainPostView;
    }

    private UserEntity getCurrentUserProfile(){
        UserEntity userObject = CurrentUser.getCurrentUserProfile();
        return userRepository.findById(userObject.getId()).orElseThrow(()->new ResourceNotFoundException("User not found"));
    }
}
