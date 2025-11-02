package com.zimaberlin.zimasocial.context.social.api.post;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.author.AuthorRepository;
import com.zimaberlin.zimasocial.context.social.comment.Comment;
import com.zimaberlin.zimasocial.context.social.comment.CommentRepository;
import com.zimaberlin.zimasocial.context.social.comment.CommentViewAdapter;
import com.zimaberlin.zimasocial.context.social.post.CreatePost;
import com.zimaberlin.zimasocial.context.social.post.Post;
import com.zimaberlin.zimasocial.context.social.post.PostRepository;
import com.zimaberlin.zimasocial.context.social.post.PostService;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.service.posts.Payload.PostPayload;
import com.zimaberlin.zimasocial.service.posts.exception.PostNotFoundException;
import com.zimaberlin.zimasocial.views.comment.CommentView;
import com.zimaberlin.zimasocial.views.post.PostView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class PostControllerBridge {
    private final PostService postService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostViewAdapter postViewAdapter;
    private final CommentViewAdapter commentViewAdapter;
    private final AuthorRepository authorRepository;

    @Autowired
    public PostControllerBridge(PostService postService, PostRepository postRepository, CommentRepository commentRepository, PostViewAdapter postViewAdapter, CommentViewAdapter commentViewAdapter, AuthorRepository authorRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
        this.postViewAdapter = postViewAdapter;
        this.commentRepository = commentRepository;
        this.commentViewAdapter = commentViewAdapter;
        this.authorRepository = authorRepository;
    }

    public PostView createPost(PostPayload payload, String language) {
        switch (payload.getType()){
            case PostType.movie -> {
                Post post = postService.createMoviePost(
                        payload.getContent(),
                        Integer.valueOf(payload.getMediaId()),
                        payload.getMovieMediaType(),
                        language
                );
                return postViewAdapter.populated(post);
            }
            case PostType.book ->  {
                Post post = postService.createBookPost(payload.getContent(), payload.getMediaId());
                return postViewAdapter.populated(post);
            }
            case PostType.music -> {
                Post post = postService.createMusicPost(payload.getContent(), payload.getMediaId());
                return postViewAdapter.populated(post);
            }
            default -> {
                payload.setType(PostType.any);
                Post post = postService.createPost(
                        CreatePost.builder()
                                .type(payload.getType())
                                .content(payload.getContent())
                                .build()
                );
                return postViewAdapter.populated(post);
            }
        }
    }
    public PagedModel<PostView> getPosts(
           Integer page,
           Integer size,
           PostCategory category,
           String slug
    ) throws NoSuchMethodException {
        System.out.println("Start getPosts at %s".formatted(LocalDateTime.now()));
        Page<Post> postPage;
        if(category == PostCategory.followings){
            AuthorId authorId = authorRepository.getAuthenticatedAuthor().getId();
            postPage = postRepository.findFollowingsPosts(PageRequest.of(page, size), authorId);
        }else{
            postPage = postRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()), slug, category);
        }
        PagedModel<PostView> pagedModel = PagedModel.of(
                postViewAdapter.populated(postPage.getContent()),
                new PagedModel.PageMetadata(
                        postPage.getSize(),
                        postPage.getNumber(),
                        postPage.getTotalElements(),
                        postPage.getTotalPages()));
        Method method = PostController.class.getMethod("getPosts",
                Integer.class,
                Integer.class,
                PostCategory.class,
                String.class);

        if(page + 1 < postPage.getTotalPages()){
            Link link = linkTo(method, page + 1, size, category, slug).withRel(LinkRelation.of("next"));
            pagedModel.add(link);
        }

        if(page > 0){
            Link link = linkTo(method, page - 1, size, category, slug).withRel(LinkRelation.of("previous"));
            pagedModel.add(link);
        }
        System.out.println("End getPosts at %s".formatted(LocalDateTime.now()));
        return pagedModel;
    }

    public PostView getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        return postViewAdapter.populated(post);
    }

    public PagedModel<CommentView> getComments(int page, int size, Long postId) throws NoSuchMethodException {
        Page<Comment> commentPage = commentRepository.findByPostIdOrderByCreatedAtDesc(postId, PageRequest.of(page, size, Sort.by("createdAt").descending()));

        PagedModel<CommentView> pagedModel = PagedModel.of(
                commentViewAdapter.populated(commentPage.getContent()),
                new PagedModel.PageMetadata(commentPage.getSize(),
                        commentPage.getNumber(),
                        commentPage.getTotalElements(),
                        commentPage.getTotalPages()));

        Method method = this.getClass().getMethod("getComments", int.class, int.class, Long.class);
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

    public PagedModel<CommentView> getCommentReplies(int page, int size, Long commentId) throws NoSuchMethodException {
        Page<Comment> commentPage = commentRepository.findByParentIdOrderByCreatedAt(commentId, PageRequest.of(page, size));

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

    public List<PostView> getTodaysPosts() {
        return postRepository.findTodaysPosts().stream().map(postViewAdapter::populated).toList();
    }
}
