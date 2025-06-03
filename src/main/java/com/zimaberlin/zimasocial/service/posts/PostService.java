package com.zimaberlin.zimasocial.service.posts;
import com.zimaberlin.zimasocial.aop.ResourceAcess.HasCommentAccess;
import com.zimaberlin.zimasocial.aop.ResourceAcess.HasPostAccess;
import com.zimaberlin.zimasocial.entity.todayspost.TodaysPost;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.entity.userRelation.Relation;
import com.zimaberlin.zimasocial.entity.userRelation.UserRelationEntity;
import com.zimaberlin.zimasocial.factory.CommentViewFactory;
import com.zimaberlin.zimasocial.repository.*;
import com.zimaberlin.zimasocial.service.notification.NotificationService;
import com.zimaberlin.zimasocial.service.posts.exception.PostNotFoundException;
import com.zimaberlin.zimasocial.utility.PostViewFactory;
import com.zimaberlin.zimasocial.views.comment.CommentView;
import com.zimaberlin.zimasocial.views.post.PostView;
import com.zimaberlin.zimasocial.config.CustomUserDetails;
import com.zimaberlin.zimasocial.exception.ConflictException;
import com.zimaberlin.zimasocial.service.posts.Payload.PostPayload;
import com.zimaberlin.zimasocial.entity.*;
import com.zimaberlin.zimasocial.exception.ResourceNotFoundException;
import com.zimaberlin.zimasocial.service.posts.Payload.CommentPayload;
import com.zimaberlin.zimasocial.utility.CurrentUser;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService  {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificationService notificationService;
    private final CommentViewFactory commentViewFactory;
    private final PostViewFactory postFactory;
    private final TodaysPostRepository todaysPostRepository;
    private final UserRelationRepository userRelationRepository;
    public Page<PostView> getPosts(int page, int size, String slug, PostType type) {
        UserEntity user = userRepository.findBySlug(slug).orElseThrow(()->new ResourceNotFoundException("User not found"));
        Pageable pageable = PageRequest.of(page, size);
        Page<PostEntity> postsPage;

        if(type != null){
            postsPage = postRepository.findByUserAndTypeOrderByCreatedAt(pageable, user, type);
        }else{
            postsPage = postRepository.findByUserOrderByCreatedAt(pageable, user);
        }
        List<PostView> postViews = postFactory.populated(postsPage.getContent());
        postViews = filterPostsForBlocked(postViews);
        return new PageImpl<>(postViews, pageable, postsPage.getTotalElements());
    }

      
    public Page<PostView> getPosts(int page, int size, PostType type) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<PostEntity> postsPage;
        if(type == PostType.any){
          postsPage = postRepository.findAll(pageable);
        }else{
            postsPage = postRepository.findByType(pageable, type);
        }
        List<PostView> postViews = postFactory.populated(postsPage.getContent());
        postViews = filterPostsForBlocked(postViews);
        return new PageImpl<>(postViews, pageable, postsPage.getTotalElements());
    }

      
    public Page<CommentView> getComments(int page, int size, Long postId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<CommentEntity> commentsPage = commentRepository.findByPostIdAndParentId(pageable, postId, null);
        List<CommentView> commentViews = commentViewFactory.populated(commentsPage.getContent());
        return new PageImpl<>(commentViews, pageable, commentsPage.getTotalElements());
    }

      
    public Page<CommentView> getCommentReplies(int page, int size, Long postId, Long commentId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentEntity> repliesPage = commentRepository.findByParentIdOrderByCreatedAtDesc(commentId, pageable);
        List<CommentView> commentViews = commentViewFactory.populated(repliesPage.getContent());
        return new PageImpl<>(commentViews, pageable, repliesPage.getTotalElements());
    }

      
    public PostView getPost(Long postId) {
        PostEntity post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post not found"));
        return  postFactory.populated(post);
    }

      
    @Transactional
    public PostView createPost(@Valid PostPayload payload) {
        UserEntity userObject = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getProfile();
        UserEntity user = userRepository.findById(userObject.getId()).orElseThrow(()->new ResourceNotFoundException("User not found"));
        if ( payload == null ) {
            return null;
        }
        PostEntity post = new PostEntity();
        post.setContent( payload.getContent() );
        post.setUrl( payload.getUrl() );
        post.setType( payload.getType() == null ? PostType.any : payload.getType() );
        post.setUser(user);

        PostEntity createdPost = postRepository.save(post);
        return postFactory.populated(createdPost);
    }

      
    @HasPostAccess(idParameterName = "id")
    public void deletePost(Long id){
        PostEntity post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        post.markAsDeleted();
        postRepository.save(post);
    }

      
    @Transactional
    public void likePost(Long postId)  {
        PostEntity post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        boolean isAlreadyLiked = likeRepository.existsByUserAndPost(CurrentUser.getCurrentUserProfile(), post);
        UserEntity currentUser = CurrentUser.getCurrentUserProfile();
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

      
    @Transactional
    public void unlikePost(Long postId) {
        LikeEntity likeEntity = likeRepository.findByUserIdAndPostId(CurrentUser.getCurrentUserProfile().getId(), postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post is not liked"));
        likeEntity.getPost().decrementLikeCount();
        notificationService.removePostLikedNotification(likeEntity.getPost());
        likeRepository.delete(likeEntity);
    }

      
    @Transactional
    public CommentView commentPost(Long postId, CommentPayload payload) {
        UserEntity currentUser = CurrentUser.getCurrentUserProfile();
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

        return commentViewFactory.plain(commentEntity);
    }

      
    @HasCommentAccess(idParameterName = "commentId")
    @Transactional
    public void deleteComment(Long postId, Long commentId) {
        PostEntity post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment not found"));
        post.decrementCommentCount();
        notificationService.removePostCommentedNotification(commentEntity);
        commentEntity.markAsDeleted();
        commentRepository.save(commentEntity);
    }

      
    @Transactional
    public void likeComment(Long postId, Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment not found"));
        boolean isAlreadyLiked = likeRepository.existsByUserAndComment(CurrentUser.getCurrentUserProfile(), comment);
        UserEntity currentUser = CurrentUser.getCurrentUserProfile();

        if(!isAlreadyLiked){
            LikeEntity like = new LikeEntity();
            like.setComment(comment);
            like.setUser(currentUser);

            comment.incrementLikeCount();

            likeRepository.save(like);
            boolean selfLiked = currentUser.equals(comment.getUser());
            if(!selfLiked){
                notificationService.sendCommentLikedNotification(comment);
            }
        }else{
            throw new ConflictException("Comment is already liked");
        }
    }

      
    @Transactional
    public void unlikeComment(Long postId, Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment not found"));
        LikeEntity like = likeRepository
                .findByUserIdAndCommentId(CurrentUser.getCurrentUserProfile().getId(), commentId)
                .orElseThrow(()-> new ResourceNotFoundException("Comment not liked"));

        like.getComment().decrementLikeCount();
        notificationService.removeCommentLikedNotification(comment);
        likeRepository.delete(like);
    }

      
    @Transactional
    public CommentView replyComment(Long postId, Long commentId, CommentPayload payload) {
        CommentEntity parent = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment not found"));
        PostEntity post = parent.getPost();
        UserEntity currentUser = CurrentUser.getCurrentUserProfile();

        CommentEntity reply = new CommentEntity();

        reply.setContent(payload.getContent());
        reply.setParent(parent);
        reply.setUser(currentUser);
        reply.setPost(post);

        parent.incrementReplyCount();
        CommentEntity comment = commentRepository.save(reply);
        boolean selfReplied = currentUser.equals(parent.getUser());
        if(!selfReplied){
            notificationService.sendCommentRepliedNotification(reply);
        }
        return commentViewFactory.plain(comment);
    }

      
    @HasCommentAccess(idParameterName = "replyCommentId")
    public CommentView deleteReplyComment(Long postId, Long commentId, Long replyCommentId) {
        CommentEntity reply = commentRepository.findById(replyCommentId).orElseThrow(()->new ResourceNotFoundException("Comment not found"));
        reply.getParent().decrementReplyCount();
        reply.markAsDeleted();
        commentRepository.save(reply);
        notificationService.removeCommentRepliedNotification(reply);
        return commentViewFactory.plain(reply);
    }

      
    public List<PostView> getTodaysPosts() {
        final List<TodaysPost> todaysPosts = todaysPostRepository.findTodaysPostByDate(LocalDate.now().minusDays(1));
        return postFactory.populated(todaysPosts.stream().map(TodaysPost::getPost).filter(Objects::nonNull).toList());
    }

    public List<PostView> filterPostsForBlocked(List<PostView> posts) {
        return posts.stream().filter(e-> !e.getUser().getIsBlocked()).toList();
    }
}
