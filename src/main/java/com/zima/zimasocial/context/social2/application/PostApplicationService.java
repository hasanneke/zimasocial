package com.zima.zimasocial.context.social2.application;

import com.zima.zimasocial.context.communication.application.NotificationManager;
import com.zima.zimasocial.context.communication.domain.entity.PostCommentedNotification;
import com.zima.zimasocial.context.communication.domain.entity.PostLikedNotification;
import com.zima.zimasocial.context.communication.domain.value.RecipientId;
import com.zima.zimasocial.context.social.comment.CommentViewAdapter;
import com.zima.zimasocial.context.social2.domain.entity.Comment;
import com.zima.zimasocial.context.social2.domain.entity.Like;
import com.zima.zimasocial.context.social2.domain.entity.Post;
import com.zima.zimasocial.context.social2.repository.CommentRepository;
import com.zima.zimasocial.context.social2.repository.LikeRepository;
import com.zima.zimasocial.context.social2.repository.PostRepository;
import com.zima.zimasocial.entity.LikeType;
import com.zima.zimasocial.entity.user.UserEntity;
import com.zima.zimasocial.exception.ConflictException;
import com.zima.zimasocial.exception.UnauthorizedException;
import com.zima.zimasocial.service.posts.exception.CommentNotFoundException;
import com.zima.zimasocial.service.posts.exception.PostNotFoundException;
import com.zima.zimasocial.utility.CurrentUser;
import com.zima.zimasocial.views.comment.CommentView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostApplicationService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final NotificationManager notificationManager;
    private final CommentRepository commentRepository;
    private final CommentViewAdapter commentViewAdapter;
    @Transactional
    public void likePost(Long postId) {
        UserEntity liker = CurrentUser.getCurrentUserProfile();
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        Optional<Like> existingLike = likeRepository.findByUserIdAndPostIdAndType(postId, liker.getId(), LikeType.post);
        if(existingLike.isPresent()){
            throw new ConflictException("Post is already liked");
        }
        Like like = post.like(liker.getId());
        likeRepository.save(like);
        postRepository.save(post);
        if(!post.getUserId().equals(like.getUserId())){
            PostLikedNotification postLikedNotification = PostLikedNotification.builder()
                    .postId(post.getId())
                    .actorId(new RecipientId(like.getUserId()))
                    .recipientId(new RecipientId(post.getUserId()))
                    .createdAt(OffsetDateTime.now())
                    .build();
            notificationManager.throttled().sendNotification(postLikedNotification);
        }
        return;
    }

    @Transactional
    public void unlikePost(Long postId) {
        UserEntity liker = CurrentUser.getCurrentUserProfile();
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        Optional<Like> like = likeRepository.findByUserIdAndPostIdAndType(liker.getId(), postId, LikeType.post);
        if(like.isEmpty()){
            throw new ConflictException("Post is not liked");
        }
        post.unlike(liker.getId());
        postRepository.save(post);
        likeRepository.deleteById(like.get().getPostId());
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        UserEntity attempter = CurrentUser.getCurrentUserProfile();
        if(!post.getUserId().equals(attempter.getId())) {
            throw new UnauthorizedException();
        }
        commentRepository.deleteAllByPostId(postId);
        postRepository.delete(post);
    }

    @Transactional
    public CommentView comment(Long postId, String content, UUID mediaId) {
        UserEntity commenter = CurrentUser.getCurrentUserProfile();
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        List<Comment> previousComments = commentRepository.findByUserIdAndPostIdAndParentIdIsNull(commenter.getId(), postId);
        Comment comment = post.comment(commenter.getId(), content, mediaId, previousComments.isEmpty());
        Comment savedComment = commentRepository.save(comment);
        postRepository.save(post);
        if(!commenter.getId().equals(post.getUserId())){
            PostCommentedNotification postCommentedNotification = PostCommentedNotification.builder()
                    .postId(post.getId())
                    .actorId(new RecipientId(commenter.getId()))
                    .recipientId(new RecipientId(post.getUserId()))
                    .createdAt(OffsetDateTime.now())
                    .build();
            notificationManager.sendNotification(postCommentedNotification);
        }
        return commentViewAdapter.populatedV2(savedComment);
    }

    public void removeComment(Long commentId) {
        // TODO: do not decrease score for every comment removal. Only if left 1.
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        Post post = postRepository.findById(comment.getPostId()).orElseThrow(PostNotFoundException::new);
        UserEntity remover = CurrentUser.getCurrentUserProfile();
        List<Comment> previousComments = commentRepository.findByUserIdAndPostIdAndParentIdIsNull(remover.getId(), post.getId());
        if(previousComments.isEmpty()){
            post.decreaseCommentScore();
        }
        if(!comment.getUserId().equals(remover.getId())){
            throw new UnauthorizedException();
        }
        post.removeComment(remover.getId(), previousComments.isEmpty());
        commentRepository.delete(comment);
    }
}
