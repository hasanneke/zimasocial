package com.zima.zimasocial.context.social.post.application;

import com.zima.zimasocial.context.social.api.FeedFilterPlain;
import com.zima.zimasocial.context.social.api.dto.LikeDTO;
import com.zima.zimasocial.context.social.author.entity.AuthorDomain;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFollowedException;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFoundException;
import com.zima.zimasocial.context.social.author.repository.AuthorRepositoryDomain;
import com.zima.zimasocial.context.social.authorrelation.service.AuthorRelationService;
import com.zima.zimasocial.context.social.comment.CommentDomain;
import com.zima.zimasocial.context.social.comment.CommentLike;
import com.zima.zimasocial.context.social.comment.CommentRepliedEvent;
import com.zima.zimasocial.context.social.comment.CommentDomainRepository;
import com.zima.zimasocial.context.social.infastructure.repository.MediaItemJpaRepository;
import com.zima.zimasocial.context.social.like.LikeDomain;
import com.zima.zimasocial.context.social.like.LikeDomainRepository;
import com.zima.zimasocial.context.social.post.entity.PostDomain;
import com.zima.zimasocial.context.social.post.event.PostCommentedEvent;
import com.zima.zimasocial.context.social.post.event.PostSharedEvent;
import com.zima.zimasocial.context.social.post.repository.FeedFilter;
import com.zima.zimasocial.context.social.post.repository.PostDomainRepository;
import com.zima.zimasocial.context.social.post.value.CreatePost;
import com.zima.zimasocial.context.social.post.value.MediaId;
import com.zima.zimasocial.context.social.post.value.PostContent;
import com.zima.zimasocial.context.social.post.value.PostLike;
import com.zima.zimasocial.entity.MediaType;
import com.zima.zimasocial.exception.ConflictException;
import com.zima.zimasocial.exception.DataNotFoundException;
import com.zima.zimasocial.repository.LikeJpaRepository;
import com.zima.zimasocial.service.posts.exception.CommentNotFoundException;
import com.zima.zimasocial.service.posts.exception.PostNotFoundException;
import com.zima.zimasocial.shared.StaticEventPublisher;
import com.zima.zimasocial.views.post.PostDTO;
import com.zima.zimasocial.views.post.PostView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostDomainRepository postRepository;
    private final AuthorRepositoryDomain authorRepository;
    private final LikeDomainRepository likeRepository;
    private final CommentDomainRepository commentRepository;
    private final MediaItemJpaRepository mediaItemJpaRepository;
    private final AuthorRelationService authorRelationService;
    private final LikeJpaRepository likeJpaRepository;

    @Transactional
    public PostDomain createPost(CreatePost createPost) {
        AuthorDomain author = authorRepository.getAuthenticatedAuthor();
        MediaId mediaId = null;
        if(createPost.mediaId() != null){
            mediaId = new MediaId(mediaItemJpaRepository.findIdById(UUID.fromString(createPost.mediaId())).orElseThrow(()-> new DataNotFoundException("media_not_found")));
        }
        PostDomain post = PostDomain.create(postRepository.nextSequence(), author.getId(), new PostContent(createPost.content(), createPost.type() == null ? MediaType.any : createPost.type(), mediaId));
        postRepository.save(post);
        StaticEventPublisher.publishEvent(new PostSharedEvent(post.getPostId(), post.getAuthorId(), post.getContent()));;
        return post;
    }

    @Transactional
    public void like(Long postId) {
        AuthorDomain author = authorRepository.getAuthenticatedAuthor();
        Optional<LikeDomain> like = likeRepository.findByPostIdAndAuthorId(postId, author.getId());
        if(like.isPresent()){
            throw new ConflictException("Post is already liked");
        }
        PostDomain post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        PostLike postLike = post.like(author.getId());
        postRepository.save(post);
        likeRepository.save(postLike);
    }

    @Transactional
    public void unlikePost(Long postId) {
        AuthorDomain author = authorRepository.getAuthenticatedAuthor();
        PostDomain post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        Optional<LikeDomain> like = likeRepository.findByPostIdAndAuthorId(postId, author.getId());
        if(like.isEmpty()){
            throw new ConflictException("Post is not liked");
        }
        post.unliked(author.getId());
        postRepository.save(post);
        likeRepository.delete(like.get());
    }

    @Transactional
    public void delete(Long id){
        PostDomain post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        postRepository.delete(post);
    }

    @Transactional
    public CommentDomain comment(Long postId, String content, UUID mediaId) {
        PostDomain post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        AuthorDomain author = authorRepository.getAuthenticatedAuthor();
        CommentDomain comment = post.comment(author.getId(), content, mediaId);
        CommentDomain savedComment = commentRepository.save(comment);
        postRepository.save(post);
        StaticEventPublisher.publishEvent(new PostCommentedEvent(postId, savedComment.getCommentId(), author.getId(), post.getAuthorId()));
        return savedComment;
    }

    @Transactional
    public void removeComment(Long commentId) {
        CommentDomain comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        PostDomain post = postRepository.findById(comment.getPostId()).orElseThrow(PostNotFoundException::new);
        post.removeComment(comment.getAuthorId());
        postRepository.save(post);
        commentRepository.delete(comment);
    }

    @Transactional
    public void likeComment(Long commentId) {
        AuthorDomain authenticatedAuthor = authorRepository.getAuthenticatedAuthor();
        CommentDomain comment = commentRepository.findById(commentId).orElseThrow(()-> new DataNotFoundException("Comment not found"));
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
        AuthorDomain authenticatedAuthor = authorRepository.getAuthenticatedAuthor();
        CommentDomain comment = commentRepository.findById(commentId).orElseThrow(()-> new DataNotFoundException("Comment not found"));
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
    public CommentDomain replyComment(Long parentId, String content, UUID mediaId) {
        AuthorDomain author = authorRepository.getAuthenticatedAuthor();
        CommentDomain parent = commentRepository.findById(parentId).orElseThrow(CommentNotFoundException::new);
        CommentDomain reply = parent.reply(author.getId(), content, mediaId);
        CommentDomain savedReply = commentRepository.save(reply);
        commentRepository.save(parent);
        StaticEventPublisher.publishEvent(new CommentRepliedEvent(parentId, savedReply.getCommentId(), parent.getAuthorId().getValue(), author.getId().getValue(), parent.getPostId()));
        return savedReply;
    }

    @Transactional
    public void deleteReply(Long replyId) {
        CommentDomain reply = commentRepository.findById(replyId).orElseThrow(CommentNotFoundException::new);
        CommentDomain parent = commentRepository.findById(reply.getParentCommentId()).orElseThrow(CommentNotFoundException::new);
        parent.removeReply(reply);
        commentRepository.delete(reply);
        commentRepository.save(parent);
    }
    public void makePostInvisible(PostDomain post) {
        post.makeInvisible();
        postRepository.save(post);
    }
    public void makePostVisible(PostDomain post) {
        post.makeVisible();
        postRepository.save(post);
    }

    public List<PostView> getFeed(FeedFilterPlain filterPlain) {
        AuthorDomain author = authorRepository.getAuthenticatedAuthor();
        FeedFilter feedFilter = new FeedFilter();
        if(filterPlain.getSlug() != null){
            AuthorDomain ownerAuthor = authorRepository.findBySlugAndIsDisabledFalseAndNotBeingBlocked(filterPlain.getSlug()).orElseThrow(AuthorNotFoundException::new);
            if(ownerAuthor.equals(author) || !ownerAuthor.getIsPrivate() || authorRelationService.isAuthorFollowed(author.getId(), ownerAuthor.getId())){
                feedFilter.setOwnerAuthorId(ownerAuthor.getId());
            }else{
                throw new AuthorNotFollowedException(ownerAuthor.getSlug());
            }
        }
        feedFilter.setReaderAuthorId(author.getId());
        feedFilter.setOffset(filterPlain.getOffset());
        feedFilter.setSize(filterPlain.getSize());
        feedFilter.setLastId(filterPlain.getLastPostId());
        feedFilter.setLastScore(filterPlain.getLastScore());
        feedFilter.setCategory(filterPlain.getCategory());
        feedFilter.setSortType(filterPlain.getSortType());

        List<PostDTO> postDTOS = postRepository.findFeed(feedFilter);
        return postDTOS.stream().map(PostView::new).toList();
    }

    public Page<LikeDTO> getAllPostLikes(Long postId, Pageable pageable) {
        AuthorDomain author = authorRepository.getAuthenticatedAuthor();
        return likeJpaRepository.findAllLikes(postId, author.getId().getValue(), pageable);
    }

    public Page<LikeDTO> getAllCommentLikes(Long commentId, Pageable pageable) {
        AuthorDomain author = authorRepository.getAuthenticatedAuthor();
        return likeJpaRepository.findAllLikes(commentId, author.getId().getValue(), pageable);
    }
}
