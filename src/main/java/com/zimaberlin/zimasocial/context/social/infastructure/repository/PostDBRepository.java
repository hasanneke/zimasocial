package com.zimaberlin.zimasocial.context.social.infastructure.repository;

import com.zimaberlin.zimasocial.context.social.api.post.PostCategory;
import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.media.book.BookMedia;
import com.zimaberlin.zimasocial.context.social.media.movie.MovieMedia;
import com.zimaberlin.zimasocial.context.social.media.music.MusicMedia;
import com.zimaberlin.zimasocial.context.social.post.Post;
import com.zimaberlin.zimasocial.context.social.post.PostRepository;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.entity.media.*;
import com.zimaberlin.zimasocial.entity.todayspost.TodaysPost;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.entity.userRelation.Relation;
import com.zimaberlin.zimasocial.entity.userRelation.UserRelationEntity;
import com.zimaberlin.zimasocial.repository.PostJpaRepository;
import com.zimaberlin.zimasocial.repository.TodaysPostRepository;
import com.zimaberlin.zimasocial.repository.UserJpaRepository;
import com.zimaberlin.zimasocial.repository.UserRelationJpaRepository;
import com.zimaberlin.zimasocial.service.posts.exception.PostNotFoundException;
import com.zimaberlin.zimasocial.service.users.exception.UserNotFoundException;
import com.zimaberlin.zimasocial.utility.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PostDBRepository implements PostRepository {
    private final PostJpaRepository postJpaRepository;
    private final UserJpaRepository userRepository;
    private final TodaysPostRepository todaysPostRepository;
    private final UserRelationJpaRepository userRelationJpaRepository;
    @Autowired
    public PostDBRepository(PostJpaRepository postJpaRepository, UserJpaRepository userRepository, TodaysPostRepository todaysPostRepository, UserRelationJpaRepository userRelationJpaRepository) {
        this.postJpaRepository = postJpaRepository;
        this.userRepository = userRepository;
        this.todaysPostRepository = todaysPostRepository;
        this.userRelationJpaRepository = userRelationJpaRepository;
    }
    @Override
    public Optional<Post> findById(Long postId) {
        PostEntity post = postJpaRepository.findByIdAndIsVisibleTrue(postId).orElseThrow(PostNotFoundException::new);
        return Optional.ofNullable(post.convertToPostDomain());
    }

    @Override
    public Page<Post> findAll(Pageable page, String slug, PostCategory category) {
        System.out.println("Start findAll at %s".formatted(LocalDateTime.now()));
        UserEntity currentUser = CurrentUser.getCurrentUserProfile();
        Specification<PostEntity> specification = Specification.where(null);
        if(slug != null){
            UserEntity user = userRepository.findBySlug(slug).orElseThrow(UserNotFoundException::new);
            specification = specification.and(PostSpecification.authorId(user.getId()));
        }
        // Study Specification Pattern
        PostType type = switch (category) {
                case any -> PostType.any;
                case music -> PostType.music;
                case movie -> PostType.movie;
                case book -> PostType.book;
                case followings -> null;
        };
        if(type != PostType.any){
            specification = specification.and(PostSpecification.type(type));
        }
        List<UserRelationEntity> blockRelations = userRelationJpaRepository.findAllBlockRelations(currentUser.getId());
        List<UserRelationEntity> followRelations = userRelationJpaRepository.findByActorIdAndRelation(currentUser.getId(), Relation.followed);
        List<Long> followedAuthors = followRelations.stream().map(UserRelationEntity::getReceiverId).toList();
        
        specification = specification.and(PostSpecification.notBlocked(currentUser.getId(), blockRelations));
        specification = specification.and(PostSpecification.isVisible());
        specification = specification.and(PostSpecification.isAuthorPublicOrAuthorFollowed(currentUser.getId(), followedAuthors));
        Page<PostEntity> postEntityPage = postJpaRepository.findAll(specification, page);
        System.out.println("End findAll at %s".formatted(LocalDateTime.now()));
        return postEntityPage.map(PostEntity::convertToPostDomain);
    }

    @Override
    public Page<Post> findFollowingsPosts(Pageable page, AuthorId authorId) {
        return postJpaRepository.findFollowingsPosts(page, authorId.getValue()).map(PostEntity::convertToPostDomain);
    }

    @Override
    public List<Post> findTodaysPosts() {
        List<TodaysPost> todaysPosts = todaysPostRepository.findTodaysPostByDate(LocalDate.now().minusDays(1));
        return todaysPosts.stream().map((e)-> e.getPost().convertToPostDomain()).toList();
    }

    @Override
    public void delete(Post post) {
        PostEntity postEntity = postJpaRepository.findById(post.getPostId()).orElseThrow(PostNotFoundException::new);
        postEntity.markAsDeleted();
        postJpaRepository.save(postEntity);
    }

    @Override
    public Long nextSequence() {
        return postJpaRepository.getNextSequence();
    }

    @Override
    public List<Post> findAllByAuthorId(AuthorId authorId) {
        return postJpaRepository.findAllByUserId(authorId.getValue()).stream().map(PostEntity::convertToPostDomain).toList();
    }

    @Override
    public List<Post> findAllInvisiblePostsByAuthorId(AuthorId authorId) {
        return postJpaRepository.
                findAllInvisiblePostsByUserId(authorId.getValue()).
                stream().map(PostEntity::convertToPostDomain).toList();
    }

    @Override
    @Transactional
    public Post save(Post post) {
        PostEntity postEntity = postJpaRepository.findById(post.getPostId()).orElse(new PostEntity());
        postEntity.merge(post);
        UserEntity user = userRepository.findById(post.getAuthorId().getValue()).orElseThrow(UserNotFoundException::new);
        postEntity.setUser(user);
        switch (post.getType()){
            case PostType.book -> {
                BookMedia book = post.getBook();
                MediaJpa mediaJpa = MediaJpa.builder()
                        .id(book.getId())
                        .type(MediaType.BOOK)
                        .book(new BookMediaJpa(book))
                        .post(postEntity)
                        .build();
                mediaJpa.setPostId(post.getPostId());
                postEntity.setMedia(mediaJpa);
            }
            case PostType.movie -> {
                MovieMedia movie = post.getMovie();
                MediaJpa mediaJpa = MediaJpa.builder()
                        .id(movie.getId())
                        .type(MediaType.MOVIE)
                        .movie(new MovieMediaJpa(movie))
                        .post(postEntity)
                        .build();
                mediaJpa.setPostId(post.getPostId());
                postEntity.setMedia(mediaJpa);
            }
            case PostType.music -> {
                MusicMedia music = post.getMusic();
                MediaJpa mediaJpa = MediaJpa.builder()
                        .id(music.getId())
                        .type(MediaType.MUSIC)
                        .song(new MusicMediaJpa(music))
                        .post(postEntity)
                        .build();
                mediaJpa.setPostId(post.getPostId());
                postEntity.setMedia(mediaJpa);
            }
        }
        PostEntity savedPost = postJpaRepository.save(postEntity);
        return savedPost.convertToPostDomain();
    }

    @Override
    public void makeInvisiblePostsOfAuthor(AuthorId authorId) {
        List<PostEntity> postEntityList = postJpaRepository.findAllByUserId(authorId.getValue());
        postEntityList.forEach(e->e.setIsVisible(false));
        postJpaRepository.saveAll(postEntityList);
    }

    @Override
    public void makePostsVisibleOfAuthor(AuthorId authorId) {
        List<PostEntity> postEntityList = postJpaRepository.findAllInvisiblePostsByUserId(authorId.getValue());
        postEntityList.forEach(e->e.setIsVisible(true));
        postJpaRepository.saveAll(postEntityList);
    }
}
