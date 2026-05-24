package com.zima.zimasocial.context.social2.application;

import com.zima.zimasocial.context.social.api.FeedFilterPlain;
import com.zima.zimasocial.context.social.api.dto.LikeDTO;
import com.zima.zimasocial.context.social.api.post.PostController;
import com.zima.zimasocial.context.social.api.post.PostViewAdapter;
import com.zima.zimasocial.context.social.author.entity.AuthorDomain;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFollowedException;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFoundException;
import com.zima.zimasocial.context.social.author.repository.AuthorRepositoryDomain;
import com.zima.zimasocial.context.social.authorrelation.service.AuthorRelationService;
import com.zima.zimasocial.context.social.comment.CommentViewAdapter;
import com.zima.zimasocial.context.social.post.entity.PostDomain;
import com.zima.zimasocial.context.social.post.repository.FeedFilter;
import com.zima.zimasocial.context.social.post.repository.PostDomainRepository;
import com.zima.zimasocial.context.social2.domain.entity.Comment;
import com.zima.zimasocial.context.social2.repository.CommentRepository;
import com.zima.zimasocial.repository.LikeJpaRepository;
import com.zima.zimasocial.service.posts.exception.PostNotFoundException;
import com.zima.zimasocial.views.comment.CommentView;
import com.zima.zimasocial.views.post.PostDTO;
import com.zima.zimasocial.views.post.PostView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
@RequiredArgsConstructor
public class PostReadService implements PostReadUseCase{
    private final AuthorRepositoryDomain authorRepository;
    private final AuthorRelationService authorRelationService;
    private final PostDomainRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeJpaRepository likeJpaRepository;
    private final CommentViewAdapter commentViewAdapter;
    private final PostViewAdapter postViewAdapter;
    @Override
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

    public PostView getPost(Long postId) {
        PostDomain post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        return postViewAdapter.populated(post);
    }
    @Override
    public Page<LikeDTO> getAllPostLikes(Long postId, Pageable pageable) {
        AuthorDomain author = authorRepository.getAuthenticatedAuthor();
        return likeJpaRepository.findAllLikes(postId, author.getId().getValue(), pageable);
    }

    @Override
    public Page<LikeDTO> getAllCommentLikes(Long commentId, Pageable pageable) {
        AuthorDomain author = authorRepository.getAuthenticatedAuthor();
        return likeJpaRepository.findAllLikes(commentId, author.getId().getValue(), pageable);
    }

    @Override
    public PagedModel<CommentView> getCommentReplies(int page, int size, Long commentId) throws NoSuchMethodException {
        Page<Comment> commentPage = commentRepository.findByParentIdOrderByCreatedAt(commentId, PageRequest.of(page, size));

        PagedModel<CommentView> pagedModel = PagedModel.of(
                commentViewAdapter.populatedV2(commentPage.getContent()),
                new PagedModel.PageMetadata(commentPage.getSize(),
                        commentPage.getNumber(),
                        commentPage.getTotalElements(),
                        commentPage.getTotalPages()));

        Method method = PostController.class.getMethod("getCommentReplies", Integer.class, Integer.class, Long.class);
        if(page + 1 < commentPage.getTotalPages()){
            Link link = linkTo(method, page + 1, size, commentId).withRel(LinkRelation.of("next"));
            pagedModel.add(link);
        }
        if(page > 0){
            Link link = linkTo(method, page - 1, size, commentId).withRel(LinkRelation.of("previous"));
            pagedModel.add(link);
        }
        return pagedModel;
    }

    public PagedModel<CommentView> getComments(int page, int size, Long postId) throws NoSuchMethodException {
        Page<Comment> commentPage = commentRepository.findByPostIdAndParentIdIsNull(postId, PageRequest.of(page, size, Sort.by("createdAt").descending()));

        PagedModel<CommentView> pagedModel = PagedModel.of(
                commentViewAdapter.populatedV2(commentPage.getContent()),
                new PagedModel.PageMetadata(commentPage.getSize(),
                        commentPage.getNumber(),
                        commentPage.getTotalElements(),
                        commentPage.getTotalPages()));
        Method method = PostController.class.getMethod("getComments", Integer.class, Integer.class, Long.class);
        if(page + 1 < commentPage.getTotalPages()){
            Link link = linkTo(method, page + 1, size, postId).withRel(LinkRelation.of("next"));
            pagedModel.add(link);
        }
        if(page > 0){
            Link link = linkTo(method, page - 1, size, postId).withRel(LinkRelation.of("previous"));
            pagedModel.add(link);
        }
        return pagedModel;
    }

    public List<PostView> getTodaysPosts() {
        return postRepository.findTodaysPosts().stream().map(postViewAdapter::populated).toList();
    }
}
