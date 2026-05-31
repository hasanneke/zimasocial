package com.zima.zimasocial.context.social.post.application;

import com.zima.zimasocial.context.social.author.abstracted.PostUseCase;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFoundException;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.media.entity.Media;
import com.zima.zimasocial.context.social.media.exception.MediaNotFoundException;
import com.zima.zimasocial.context.social.media.repository.MediaItemJpaRepository;
import com.zima.zimasocial.context.social.media.value.MediaId;
import com.zima.zimasocial.context.social.post.api.adapter.CommentViewAdapter;
import com.zima.zimasocial.context.social.post.api.adapter.PostViewAdapter;
import com.zima.zimasocial.context.social.post.api.views.CommentView;
import com.zima.zimasocial.context.social.post.api.views.PostView;
import com.zima.zimasocial.context.social.post.entity.Comment;
import com.zima.zimasocial.context.social.post.entity.Like;
import com.zima.zimasocial.context.social.post.entity.Post;
import com.zima.zimasocial.context.social.post.event.CommentRepliedEvent;
import com.zima.zimasocial.context.social.post.event.PostCommentedEvent;
import com.zima.zimasocial.context.social.post.event.PostLikedEvent;
import com.zima.zimasocial.context.social.post.repository.CommentRepository;
import com.zima.zimasocial.context.social.post.repository.LikeRepository;
import com.zima.zimasocial.context.social.post.repository.PostRepository;
import com.zima.zimasocial.context.social.post.value.CommentId;
import com.zima.zimasocial.context.social.post.value.CreatePost;
import com.zima.zimasocial.context.social.post.value.PostContent;
import com.zima.zimasocial.context.social.post.value.PostId;
import com.zima.zimasocial.entity.LikeType;
import com.zima.zimasocial.exception.ConflictException;
import com.zima.zimasocial.exception.DataNotFoundException;
import com.zima.zimasocial.exception.UnauthorizedException;
import com.zima.zimasocial.service.posts.exception.CommentNotFoundException;
import com.zima.zimasocial.service.posts.exception.PostNotFoundException;
import com.zima.zimasocial.utility.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostApplicationService implements PostUseCase {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final CommentViewAdapter commentViewAdapter;
    private final AuthorRepository authorRepository;
    private final MediaItemJpaRepository mediaRepository;
    private final PostViewAdapter postViewAdapter;
    private final ApplicationEventPublisher applicationEventPublisher;
    @Override
    @Transactional
    public PostView createPost(CreatePost createPost) {
        Author author = authorRepository.findById(CurrentUser.getCurrentUserId()).orElseThrow(AuthorNotFoundException::new);
        MediaId mediaId = null;
        if(createPost.mediaId() != null){
            Media media = mediaRepository.findById(new MediaId(UUID.fromString(createPost.mediaId()))).orElseThrow(MediaNotFoundException::new);
            mediaId = media.getId();
        }
        PostContent postContent = new PostContent(createPost.content(), createPost.type());
        Post post = new Post(postRepository.getNextSequence(), postContent, author.getId(), mediaId);
        postRepository.save(post);
        return postViewAdapter.toView(post);
    }

    @Transactional
    @Override
    public void likePost(Long postId) {
        Author author = authorRepository.getAuthenticatedAuthor();
        Post post = postRepository.findById(new PostId(postId)).orElseThrow(PostNotFoundException::new);
        if(likeRepository.existsByAuthorIdAndPostIdAndType(author.getId(), post.getId(), LikeType.post)){
            throw new ConflictException("Post is already liked");
        }
        Like like = post.like(author.getId());
        likeRepository.save(like);
        postRepository.save(post);
        applicationEventPublisher.publishEvent(new PostLikedEvent(post.getId(), post.getAuthorId(), author.getId()));
    }

    @Transactional
    @Override
    public void unlikePost(Long postId) {
        Author liker = authorRepository.getAuthenticatedAuthor();
        Post post = postRepository.findById(new PostId(postId)).orElseThrow(PostNotFoundException::new);
        Optional<Like> like = likeRepository.findByAuthorIdAndPostIdAndType(liker.getId(), post.getId(), LikeType.post);
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
        Post post = postRepository.findById(new PostId(postId)).orElseThrow(PostNotFoundException::new);
        Author attempter = authorRepository.getAuthenticatedAuthor();
        if(!post.getAuthorId().equals(attempter.getId())) {
            throw new UnauthorizedException();
        }
        postRepository.delete(post);
    }

    @Transactional
    @Override
    public CommentView comment(Long postId, String content, UUID mediaId) {
        Author commenter = authorRepository.getAuthenticatedAuthor();
        Post post = postRepository.findById(new PostId(postId)).orElseThrow(PostNotFoundException::new);
        List<Comment> previousComments = commentRepository.findByAuthorIdAndPostIdAndParentIdIsNull(commenter.getId(), post.getId());
        Comment comment = post.comment(commentRepository.getNextId(), commenter.getId(), content, new MediaId(mediaId), previousComments.isEmpty());
        Comment savedComment = commentRepository.save(comment);
        postRepository.save(post);
        applicationEventPublisher.publishEvent(new PostCommentedEvent(post.getId(), savedComment.getId(), commenter.getId(), commenter.getId()));
        return commentViewAdapter.populated(savedComment);
    }

    @Transactional
    @Override
    public void removeComment(Long commentId) {
        Comment comment = commentRepository.findById(new CommentId(commentId)).orElseThrow(CommentNotFoundException::new);
        Post post = postRepository.findById(comment.getPostId()).orElseThrow(PostNotFoundException::new);
        Author remover = authorRepository.getAuthenticatedAuthor();
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
        Author author = authorRepository.getAuthenticatedAuthor();
        Comment comment = commentRepository.findById(new CommentId(commentId)).orElseThrow(()-> new DataNotFoundException("Comment not found"));
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
        Author authenticatedAuthor = authorRepository.getAuthenticatedAuthor();
        Comment comment = commentRepository.findById(new CommentId(commentId)).orElseThrow(()-> new DataNotFoundException("Comment not found"));
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
        Author author = authorRepository.getAuthenticatedAuthor();
        Comment parent = commentRepository.findById(new CommentId(parentId)).orElseThrow(CommentNotFoundException::new);
        Comment reply = parent.reply(author.getId(), content, new MediaId(mediaId));
        Comment savedReply = commentRepository.save(reply);
        commentRepository.save(parent);
        applicationEventPublisher.publishEvent(new CommentRepliedEvent(parent.getId(), savedReply.getId(), parent.getAuthorId(), author.getId(), parent.getPostId()));
        return commentViewAdapter.populated(savedReply);
    }
    @Transactional
    @Override
    public void deleteReply(Long replyId) {
        Comment reply = commentRepository.findById(new CommentId(replyId)).orElseThrow(CommentNotFoundException::new);
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
