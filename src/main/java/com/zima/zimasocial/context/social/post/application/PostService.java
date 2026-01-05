package com.zima.zimasocial.context.social.post.application;

import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.comment.Comment;
import com.zima.zimasocial.context.social.comment.CommentLike;
import com.zima.zimasocial.context.social.comment.CommentRepliedEvent;
import com.zima.zimasocial.context.social.comment.CommentRepository;
import com.zima.zimasocial.context.social.infastructure.repository.MediaItemJpaRepository;
import com.zima.zimasocial.context.social.like.Like;
import com.zima.zimasocial.context.social.like.LikeRepository;
import com.zima.zimasocial.context.social.post.entity.Post;
import com.zima.zimasocial.context.social.post.event.PostCommentedEvent;
import com.zima.zimasocial.context.social.post.repository.FeedFilter;
import com.zima.zimasocial.context.social.post.repository.PostRepository;
import com.zima.zimasocial.context.social.post.value.CreatePost;
import com.zima.zimasocial.context.social.post.value.MediaId;
import com.zima.zimasocial.context.social.post.value.PostContent;
import com.zima.zimasocial.context.social.post.value.PostLike;
import com.zima.zimasocial.entity.MediaType;
import com.zima.zimasocial.exception.ConflictException;
import com.zima.zimasocial.exception.DataNotFoundException;
import com.zima.zimasocial.service.posts.exception.CommentNotFoundException;
import com.zima.zimasocial.service.posts.exception.PostNotFoundException;
import com.zima.zimasocial.shared.StaticEventPublisher;
import com.zima.zimasocial.views.post.PostDTO;
import com.zima.zimasocial.views.post.PostView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final MediaItemJpaRepository mediaItemJpaRepository;
    private final Clock clock;

    @Transactional
    public Post createPost(CreatePost createPost) {
        Author author = authorRepository.getAuthenticatedAuthor();
        MediaId mediaId = null;
        if(createPost.mediaId() != null){
            mediaId = new MediaId(mediaItemJpaRepository.findIdById(UUID.fromString(createPost.mediaId())).orElseThrow(()-> new DataNotFoundException("media_not_found")));
        }
        Post post = Post.create(postRepository.nextSequence(), author.getId(), new PostContent(createPost.content(), createPost.type() == null ? MediaType.any : createPost.type(), mediaId), clock);
        return postRepository.save(post);
    }

    @Transactional
    public void like(Long postId) {
        Author author = authorRepository.getAuthenticatedAuthor();
        Optional<Like> like = likeRepository.findByPostIdAndAuthorId(postId, author.getId());
        if(like.isPresent()){
            throw new ConflictException("Post is already liked");
        }
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        PostLike postLike = post.like(author.getId());
        postRepository.save(post);
        likeRepository.save(postLike);
    }

    @Transactional
    public void unlikePost(Long postId) {
        Author author = authorRepository.getAuthenticatedAuthor();
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        Optional<Like> like = likeRepository.findByPostIdAndAuthorId(postId, author.getId());
        if(like.isEmpty()){
            throw new ConflictException("Post is not liked");
        }
        post.unliked(author.getId());
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
        Comment comment = post.comment(author.getId(), content);
        Comment savedComment = commentRepository.save(comment);
        postRepository.save(post);
        StaticEventPublisher.publishEvent(new PostCommentedEvent(postId, savedComment.getCommentId(), author.getId(), post.getAuthorId()));
        return savedComment;
    }

    @Transactional
    public void removeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        Post post = postRepository.findById(comment.getPostId()).orElseThrow(PostNotFoundException::new);
        post.removeComment(comment.getAuthorId());
        postRepository.save(post);
        commentRepository.delete(comment);
    }

    @Transactional
    public void likeComment(Long commentId) {
        Author authenticatedAuthor = authorRepository.getAuthenticatedAuthor();
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new DataNotFoundException("Comment not found"));
        Optional<CommentLike> checkLike = likeRepository.findByCommentIdAndAuthorId(commentId, authenticatedAuthor.getId());
        if(checkLike.isEmpty()){
            CommentLike commentLike = comment.like(authenticatedAuthor);
            commentRepository.save(comment);
            likeRepository.save(commentLike);
        }else{
            throw new ConflictException("Comment is already liked");
        }
    }

    @Transactional
    public void unlikeComment(Long commentId) {
        Author authenticatedAuthor = authorRepository.getAuthenticatedAuthor();
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new DataNotFoundException("Comment not found"));
        Optional<CommentLike> checkLike = likeRepository.findByCommentIdAndAuthorId(commentId, authenticatedAuthor.getId());
        if(checkLike.isPresent()){
            comment.unlike();
            commentRepository.save(comment);
            likeRepository.delete(checkLike.get());
        }else{
            throw new DataNotFoundException("Comment not liked");
        }
    }

    @Transactional
    public Comment replyComment(Long parentId, String content) {
        Author author = authorRepository.getAuthenticatedAuthor();
        Comment parent = commentRepository.findById(parentId).orElseThrow(CommentNotFoundException::new);
        Comment reply = parent.reply(author.getId(), content);
        Comment savedReply = commentRepository.save(reply);
        commentRepository.save(parent);
        StaticEventPublisher.publishEvent(new CommentRepliedEvent(parentId, savedReply.getCommentId(), parent.getAuthorId(), author.getId()));
        return savedReply;
    }

    @Transactional
    public void deleteReply(Long replyId) {
        Comment reply = commentRepository.findById(replyId).orElseThrow(CommentNotFoundException::new);
        Comment parent = commentRepository.findById(reply.getParentCommentId()).orElseThrow(CommentNotFoundException::new);
        parent.removeReply(reply);
        commentRepository.delete(reply);
        commentRepository.save(parent);
    }
    public void makePostInvisible(Post post) {
        post.makeInvisible();
        postRepository.save(post);
    }
    public void makePostVisible(Post post) {
        post.makeVisible();
        postRepository.save(post);
    }

    public List<PostView> getFeed(FeedFilter filter) {
        Author author = authorRepository.getAuthenticatedAuthor();
        filter.setUserId(author.getId().getValue());
        List<PostDTO> postDTOS = postRepository.findFeed(filter);
        return postDTOS.stream().map(PostView::new).toList();
    }
}
