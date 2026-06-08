package com.zima.zimasocial.context.social.post.application;

import com.zima.zimasocial.context.social.author.abstracted.PostReadUseCase;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.post.api.PostController;
import com.zima.zimasocial.context.social.post.api.adapter.CommentViewAdapter;
import com.zima.zimasocial.context.social.post.api.adapter.PostViewAdapter;
import com.zima.zimasocial.context.social.post.api.views.CommentView;
import com.zima.zimasocial.context.social.post.api.views.LikeView;
import com.zima.zimasocial.context.social.post.api.views.PostDTO;
import com.zima.zimasocial.context.social.post.api.views.PostView;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.post.entity.Comment;
import com.zima.zimasocial.context.social.post.entity.Post;
import com.zima.zimasocial.context.social.author.service.AuthorRelationService;
import com.zima.zimasocial.context.social.post.repository.CommentRepository;
import com.zima.zimasocial.context.social.post.repository.LikeRepository;
import com.zima.zimasocial.context.social.post.repository.PostRepository;
import com.zima.zimasocial.context.social.post.repository.TodaysPostRepository;
import com.zima.zimasocial.context.social.post.value.CommentId;
import com.zima.zimasocial.context.social.post.value.PostId;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFollowedException;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFoundException;
import com.zima.zimasocial.context.social.post.value.FeedFilter;
import com.zima.zimasocial.context.social.post.api.payload.FeedFilterPlain;
import com.zima.zimasocial.context.social.post.repository.PostCustomRepository;
import com.zima.zimasocial.context.social.post.exception.PostNotFoundException;
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
import java.time.LocalDate;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
@RequiredArgsConstructor
public class PostReadService implements PostReadUseCase {
    private final AuthorRepository authorRepository;
    private final AuthorRelationService authorRelationService;
    private final TodaysPostRepository todaysPostRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final CommentViewAdapter commentViewAdapter;
    private final PostViewAdapter postViewAdapterv2;
    private final PostCustomRepository postCustomRepository;

    @Override
    public List<PostView> getFeed(FeedFilterPlain filterPlain) {
        Author author = authorRepository.getAuthenticatedAuthor();
        FeedFilter feedFilter = new FeedFilter();
        if(filterPlain.getSlug() != null){
            Author ownerAuthor = authorRepository.findBySlug(filterPlain.getSlug()).orElseThrow(AuthorNotFoundException::new);
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

        List<PostDTO> postDTOS = postCustomRepository.findFeed(feedFilter);
        return postDTOS.stream().map(PostView::new).toList();
    }

    public PostView getPost(Long postId) {
        Post post = postRepository.findById(new PostId(postId)).orElseThrow(PostNotFoundException::new);
        return postViewAdapterv2.toView(post);
    }
    @Override
    public Page<LikeView> getAllPostLikes(Long postId, Pageable pageable) {
        Author author = authorRepository.getAuthenticatedAuthor();
        return likeRepository.findAllLikes(postId, author.getId(), pageable);
    }

    @Override
    public Page<LikeView> getAllCommentLikes(Long commentId, Pageable pageable) {
        Author author = authorRepository.getAuthenticatedAuthor();
        return likeRepository.findAllLikes(commentId, author.getId(), pageable);
    }

    @Override
    public PagedModel<CommentView> getCommentReplies(int page, int size, Long commentId) throws NoSuchMethodException {
        Page<Comment> commentPage = commentRepository.findByParentIdOrderByCreatedAt(new CommentId(commentId), PageRequest.of(page, size));

        PagedModel<CommentView> pagedModel = PagedModel.of(
                commentViewAdapter.populated(commentPage.getContent()),
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
        Page<Comment> commentPage = commentRepository.findByPostIdAndParentIdIsNull(new PostId(postId), PageRequest.of(page, size, Sort.by("createdAt").descending()));

        PagedModel<CommentView> pagedModel = PagedModel.of(
                commentViewAdapter.populated(commentPage.getContent()),
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
        return todaysPostRepository.findAllByDate(LocalDate.now().minusDays(1))
                .stream().map((e)->postViewAdapterv2.toView(e.getPost())).toList();
    }
}
