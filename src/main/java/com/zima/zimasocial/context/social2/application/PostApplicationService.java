package com.zima.zimasocial.context.social2.application;

import com.zima.zimasocial.context.communication.application.NotificationManager;
import com.zima.zimasocial.context.communication.domain.entity.PostCommentedNotification;
import com.zima.zimasocial.context.communication.domain.entity.PostLikedNotification;
import com.zima.zimasocial.context.communication.domain.value.RecipientId;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFoundException;
import com.zima.zimasocial.context.social.comment.CommentRepliedEvent;
import com.zima.zimasocial.context.social.comment.CommentViewAdapter;
import com.zima.zimasocial.context.social.post.value.CreatePost;
import com.zima.zimasocial.context.social2.domain.entity.Author;
import com.zima.zimasocial.context.social2.domain.entity.Comment;
import com.zima.zimasocial.context.social2.domain.entity.Like;
import com.zima.zimasocial.context.social2.domain.entity.Post;
import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import com.zima.zimasocial.context.social2.repository.AuthorRepository;
import com.zima.zimasocial.context.social2.repository.CommentRepository;
import com.zima.zimasocial.context.social2.repository.LikeRepository;
import com.zima.zimasocial.context.social2.repository.PostRepository;
import com.zima.zimasocial.entity.LikeType;
import com.zima.zimasocial.exception.ConflictException;
import com.zima.zimasocial.exception.DataNotFoundException;
import com.zima.zimasocial.exception.UnauthorizedException;
import com.zima.zimasocial.service.posts.exception.CommentNotFoundException;
import com.zima.zimasocial.service.posts.exception.PostNotFoundException;
import com.zima.zimasocial.shared.StaticEventPublisher;
import com.zima.zimasocial.utility.CurrentUser;
import com.zima.zimasocial.views.comment.CommentView;
import com.zima.zimasocial.views.post.PostView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostApplicationService implements PostUseCase{
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final NotificationManager notificationManager;
    private final CommentRepository commentRepository;
    private final CommentViewAdapter commentViewAdapter;
    private final AuthorRepository authorRepository;

    @Override
    public PostView createPost(CreatePost createPost) {
        return null;
    }

    @Transactional
    @Override
    public void likePost(Long postId) {
        AuthorId authorId = CurrentUser.getCurrentUserId();
        Author author = authorRepository.findById(authorId).orElseThrow(AuthorNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        Optional<Like> existingLike = likeRepository.findByAuthorIdAndPostIdAndType(author.getId(), postId, LikeType.post);
        if(existingLike.isPresent()){
            throw new ConflictException("Post is already liked");
        }
        Like like = post.like(author.getId());
        likeRepository.save(like);
        postRepository.save(post);
        if(!post.getAuthorId().equals(like.getAuthorId())){
            PostLikedNotification postLikedNotification = PostLikedNotification.builder()
                    .postId(post.getId())
                    .actorId(new RecipientId(like.getAuthorId().getValue()))
                    .recipientId(new RecipientId(post.getAuthorId().getValue()))
                    .createdAt(OffsetDateTime.now())
                    .build();
            notificationManager.throttled().sendNotification(postLikedNotification);
        }
    }

    @Transactional
    @Override
    public void unlikePost(Long postId) {
        AuthorId authorId = CurrentUser.getCurrentUserId();
        Author liker = authorRepository.findById(authorId).orElseThrow(AuthorNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        Optional<Like> like = likeRepository.findByAuthorIdAndPostIdAndType(liker.getId(), postId, LikeType.post);
        if(like.isEmpty()){
            throw new ConflictException("Post is not liked");
        }
        post.unlike(liker.getId());
        postRepository.save(post);
        likeRepository.delete(like.get());
    }

    @Transactional
    @Override
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        AuthorId authorId = CurrentUser.getCurrentUserId();
        Author attempter = authorRepository.findById(authorId).orElseThrow(AuthorNotFoundException::new);
        if(!post.getAuthorId().equals(attempter.getId())) {
            throw new UnauthorizedException();
        }
        postRepository.delete(post);
    }

    @Transactional
    @Override
    public CommentView comment(Long postId, String content, UUID mediaId) {
        AuthorId userId = CurrentUser.getCurrentUserId();
        Author commenter = authorRepository.findById(userId).orElseThrow(AuthorNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        List<Comment> previousComments = commentRepository.findByAuthorIdAndPostIdAndParentIdIsNull(commenter.getId(), postId);
        Comment comment = post.comment(commenter.getId(), content, mediaId, previousComments.isEmpty());
        Comment savedComment = commentRepository.save(comment);
        postRepository.save(post);
        if(!commenter.getId().equals(post.getAuthorId())){
            PostCommentedNotification postCommentedNotification = PostCommentedNotification.builder()
                    .postId(post.getId())
                    .actorId(new RecipientId(commenter.getId().getValue()))
                    .recipientId(new RecipientId(post.getAuthorId().getValue()))
                    .createdAt(OffsetDateTime.now())
                    .build();
            notificationManager.sendNotification(postCommentedNotification);
        }
        return commentViewAdapter.populatedV2(savedComment);
    }

    @Transactional
    @Override
    public void removeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        Post post = postRepository.findById(comment.getPostId()).orElseThrow(PostNotFoundException::new);
        AuthorId userId = CurrentUser.getCurrentUserId();
        Author remover = authorRepository.findById(userId).orElseThrow(AuthorNotFoundException::new);
        List<Comment> previousComments = commentRepository.findByAuthorIdAndPostIdAndParentIdIsNull(remover.getId(), post.getId());
        if(previousComments.isEmpty()){
            post.decreaseCommentScore();
        }
        if(!comment.getAuthorId().equals(remover.getId())){
            throw new UnauthorizedException();
        }
        post.removeComment(remover.getId(), previousComments.isEmpty());
        commentRepository.delete(comment);
    }

    @Transactional
    @Override
    public void likeComment(Long commentId) {
        Author author = authorRepository.findById(CurrentUser.getCurrentUserId()).orElseThrow(AuthorNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new DataNotFoundException("Comment not found"));
        Optional<Like> checkLike = likeRepository.findByAuthorIdAndCommentIdAndType(author.getId(), commentId, LikeType.comment);
        if(checkLike.isEmpty()){
            Like commentLike = comment.like(author.getId());
            commentRepository.save(comment);
            likeRepository.save(commentLike);
        }else{
            throw new ConflictException("Comment is already liked");
        }
    }

    @Transactional
    @Override
    public void unlikeComment(Long commentId) {
        Author authenticatedAuthor = authorRepository.findById(CurrentUser.getCurrentUserId()).orElseThrow(AuthorNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new DataNotFoundException("Comment not found"));
        Optional<Like> checkLike = likeRepository.findByAuthorIdAndCommentIdAndType(authenticatedAuthor.getId(), commentId, LikeType.comment);
        if(checkLike.isPresent()){
            comment.unlike();
            commentRepository.save(comment);
            likeRepository.delete(checkLike.get());
        }else{
            throw new DataNotFoundException("Comment not liked");
        }
    }

    @Transactional
    @Override
    public CommentView replyComment(Long parentId, String content, UUID mediaId) {
        Author author = authorRepository.findById(CurrentUser.getCurrentUserId()).orElseThrow(AuthorNotFoundException::new);
        Comment parent = commentRepository.findById(parentId).orElseThrow(CommentNotFoundException::new);
        Comment reply = parent.reply(author.getId(), content, mediaId);
        Comment savedReply = commentRepository.save(reply);
        commentRepository.save(parent);
        StaticEventPublisher.publishEvent(new CommentRepliedEvent(parentId, savedReply.getId(), parent.getAuthorId().getValue(), author.getId().getValue(), parent.getPostId()));
        return commentViewAdapter.populatedV2(savedReply);
    }
    @Transactional
    @Override
    public void deleteReply(Long replyId) {
        Comment reply = commentRepository.findById(replyId).orElseThrow(CommentNotFoundException::new);
        Comment parent = commentRepository.findById(reply.getParentId()).orElseThrow(CommentNotFoundException::new);
        parent.removeReply();
        commentRepository.delete(reply);
        commentRepository.save(parent);
    }

    @Override
    public void makePostInvisible(Post post) {
        post.makeInvisible();
        postRepository.save(post);
    }

    @Override
    public void makePostVisible(Post post) {
        post.makeVisible();
        postRepository.save(post);
    }
}
