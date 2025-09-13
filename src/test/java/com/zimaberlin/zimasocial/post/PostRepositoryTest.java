package com.zimaberlin.zimasocial.post;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.infastructure.adapter.PostDBAdapter;
import com.zimaberlin.zimasocial.context.social.infastructure.repository.PostDBRepository;
import com.zimaberlin.zimasocial.context.social.media.MovieMedia;
import com.zimaberlin.zimasocial.context.social.media.MovieMediaType;
import com.zimaberlin.zimasocial.context.social.media.book.BookMedia;
import com.zimaberlin.zimasocial.context.social.post.Post;
import com.zimaberlin.zimasocial.context.social.post.PostRepository;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.entity.media.BookMediaJpa;
import com.zimaberlin.zimasocial.entity.media.BookProvider;
import com.zimaberlin.zimasocial.entity.media.MovieMediaJpa;
import com.zimaberlin.zimasocial.entity.media.MovieProvider;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.repository.PostJpaRepository;
import com.zimaberlin.zimasocial.repository.TodaysPostRepository;
import com.zimaberlin.zimasocial.repository.UserJpaRepository;
import com.zimaberlin.zimasocial.repository.UserRelationJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostRepositoryTest {
    @Mock
    private PostJpaRepository postJpaRepository;
    @Mock
    private UserJpaRepository userRepository;
    @Mock
    private TodaysPostRepository todaysPostRepository;
    @Mock
    private UserRelationJpaRepository userRelationJpaRepository;
    private PostRepository postRepository;
    @BeforeEach
    void setUp() {
        postRepository = new PostDBRepository(postJpaRepository,  new PostDBAdapter(), userRepository, todaysPostRepository, userRelationJpaRepository);
    }
    @Test
    void testSaveAnyPost() {
        AuthorId authorId = new AuthorId(0L);
        Long postId = 0L;
        Post post = new Post(postId, "", PostType.any, authorId);
        UserEntity user = new UserEntity(authorId.getId());
        PostEntity returnedEntity = new PostEntity();
        returnedEntity.setId(0L);
        returnedEntity.setType(PostType.any);
        returnedEntity.setUser(user);
        when(userRepository.findById(authorId.getId())).thenReturn(Optional.of(user));
        when(postJpaRepository.save(returnedEntity)).thenReturn(returnedEntity);
        postRepository.save(post);

        ArgumentCaptor<PostEntity> postEntityArgumentCaptor = ArgumentCaptor.forClass(PostEntity.class);
        verify(postJpaRepository).save(postEntityArgumentCaptor.capture());
        PostEntity postEntity = postEntityArgumentCaptor.getValue();

        Assertions.assertEquals(postEntity.getUserId(), authorId.getId());
        Assertions.assertEquals(postEntity.getContent(), post.getContent());
        Assertions.assertEquals(postEntity.getType(), post.getType());
    }

    @Test
    void testSavePost_WhenPostTypeIsMovie_CreateMovieAndPost() {
        AuthorId authorId = new AuthorId(0L);
        Long postId = 0L;
        UUID mediaId = UUID.randomUUID();
        MovieMedia movieMedia = MovieMedia.builder()
                .id(mediaId)
                .type(MovieMediaType.tv)
                .movieProvider(MovieProvider.TMDB)
                .movieGenres("genres")
                .description("description")
                .name("name")
                .imdbScore(90.0)
                .backdropUrl("backdropUrl")
                .posterUrl("posterUrl")
                .voteAverage(3.8)
                .numberOfSeasons(5)
                .numberOfEpisodes(100)
                .releaseDate(LocalDate.now())
                .build();

        Post post = new Post(postId, "", authorId, movieMedia);
        UserEntity user = new UserEntity(authorId.getId());
        PostEntity returnedEntity = new PostEntity();
        returnedEntity.setId(0L);
        returnedEntity.setType(PostType.movie);
        returnedEntity.setUser(user);
        when(userRepository.findById(authorId.getId())).thenReturn(Optional.of(user));
        when(postJpaRepository.save(returnedEntity)).thenReturn(returnedEntity);
        postRepository.save(post);

        ArgumentCaptor<PostEntity> postEntityArgumentCaptor = ArgumentCaptor.forClass(PostEntity.class);
        verify(postJpaRepository).save(postEntityArgumentCaptor.capture());
        PostEntity postEntity = postEntityArgumentCaptor.getValue();

        Assertions.assertEquals(postEntity.getUserId(), authorId.getId());
        Assertions.assertEquals(postEntity.getContent(), post.getContent());
        Assertions.assertEquals(postEntity.getType(), post.getType());

        MovieMediaJpa movieMediaJpa = postEntity.getMedia().getMovie();

        Assertions.assertEquals(MovieMediaType.tv, movieMediaJpa.getMovieMediaType());
        Assertions.assertEquals(MovieProvider.TMDB, movieMediaJpa.getMovieProvider());
        Assertions.assertEquals("genres", movieMediaJpa.getMovieGenres());
        Assertions.assertEquals("description", movieMediaJpa.getDescription());
        Assertions.assertEquals("name", movieMediaJpa.getName());
        Assertions.assertEquals(90.0, movieMediaJpa.getImdbScore());
        Assertions.assertEquals("backdropUrl", movieMediaJpa.getBackdropUrl());
        Assertions.assertEquals("posterUrl", movieMediaJpa.getPosterUrl());
        Assertions.assertEquals(3.8, movieMediaJpa.getVoteAverage());
        Assertions.assertEquals(5, movieMediaJpa.getNumberOfSeasons());
        Assertions.assertEquals(100, movieMediaJpa.getNumberOfEpisodes());
        Assertions.assertEquals(LocalDate.now(), movieMediaJpa.getReleaseDate());
    }

    @Test
    void testSavePost_WhenPostTypeIsBook_CreateBookAndPost() {
        AuthorId authorId = new AuthorId(0L);
        Long postId = 0L;
        UUID mediaId = UUID.randomUUID();

        BookMedia bookMedia = BookMedia.builder()
                .id(mediaId)
                .title("title")
                .smallThumbnail("smallThumbnail")
                .printType("printType")
                .thumbnail("thumbnail")
                .pageCount(50)
                .author("zima")
                .provider(BookProvider.google)
                .description("description")
                .publishDate("2010")
                .summary("summary")
                .publisher("zimabooks")
                .build();

        Post post = new Post(postId, "", authorId, bookMedia);
        UserEntity user = new UserEntity(authorId.getId());
        PostEntity returnedEntity = new PostEntity();
        returnedEntity.setId(0L);
        returnedEntity.setType(PostType.movie);
        returnedEntity.setUser(user);

        when(userRepository.findById(authorId.getId())).thenReturn(Optional.of(user));
        when(postJpaRepository.save(returnedEntity)).thenReturn(returnedEntity);
        postRepository.save(post);
        ArgumentCaptor<PostEntity> postEntityArgumentCaptor = ArgumentCaptor.forClass(PostEntity.class);
        verify(postJpaRepository).save(postEntityArgumentCaptor.capture());
        PostEntity postEntity = postEntityArgumentCaptor.getValue();

        Assertions.assertEquals(postEntity.getUserId(), authorId.getId());
        Assertions.assertEquals(postEntity.getContent(), post.getContent());
        Assertions.assertEquals(postEntity.getType(), post.getType());

        BookMediaJpa bookMediaJpa = postEntity.getMedia().getBook();

        Assertions.assertEquals(mediaId, postEntity.getMedia().getId());
        Assertions.assertEquals("title", bookMediaJpa.getTitle());
        Assertions.assertEquals("smallThumbnail", bookMediaJpa.getSmallThumbnail());
        Assertions.assertEquals("printType", bookMediaJpa.getPrintType());
        Assertions.assertEquals("thumbnail", bookMediaJpa.getThumbnail());
        Assertions.assertEquals(50, bookMediaJpa.getPageCount());
        Assertions.assertEquals("zima", bookMediaJpa.getAuthor());
        Assertions.assertEquals(BookProvider.google, bookMediaJpa.getProvider());
        Assertions.assertEquals("description", bookMediaJpa.getDescription());
        Assertions.assertEquals("2010", bookMediaJpa.getPublishDate());
        Assertions.assertEquals("summary", bookMediaJpa.getSummary());
        Assertions.assertEquals("zimabooks", bookMediaJpa.getPublisher());
    }
}
