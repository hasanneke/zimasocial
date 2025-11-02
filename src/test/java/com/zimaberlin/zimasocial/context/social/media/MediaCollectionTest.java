package com.zimaberlin.zimasocial.context.social.media;

import com.zimaberlin.zimasocial.context.social.infastructure.adapter.MediaDBAdapter;
import com.zimaberlin.zimasocial.context.social.infastructure.repository.MediaDBCollection;
import com.zimaberlin.zimasocial.context.social.media.book.BookMedia;
import com.zimaberlin.zimasocial.context.social.media.movie.MovieMedia;
import com.zimaberlin.zimasocial.context.social.media.movie.MovieMediaType;
import com.zimaberlin.zimasocial.entity.media.*;
import com.zimaberlin.zimasocial.repository.MediaJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MediaCollectionTest {
    @Mock
    private MediaJpaRepository mediaJpaRepository;
    private MediaCollection mediaCollection;

    @BeforeEach
    void setUp() {
        mediaCollection = new MediaDBCollection(mediaJpaRepository, new MediaDBAdapter());
    }
    @Test
    void testFindMovieById_WhenMovieFound_ReturnMovieOptional() {
        UUID id = UUID.randomUUID();
        MovieMediaJpa movieMediaJpa = MovieMediaJpa.builder()
                .movieMediaType(MovieMediaType.tv)
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

        MediaJpa mediaJpa = MediaJpa.builder()
                        .id(id)
                        .postId(0L)
                        .movie(movieMediaJpa)
                        .build();
        when(mediaJpaRepository.findById(id)).thenReturn(Optional.of(mediaJpa));

        Optional<MovieMedia> movieMediaOpt = mediaCollection.findMovieById(id);

        Assertions.assertTrue(movieMediaOpt.isPresent());
        MovieMedia movieMedia = movieMediaOpt.get();
        Assertions.assertEquals(MovieMediaType.tv, movieMedia.getType());
        Assertions.assertEquals(MovieProvider.TMDB, movieMedia.getMovieProvider());
        Assertions.assertEquals("genres", movieMedia.getMovieGenres());
        Assertions.assertEquals("description", movieMedia.getDescription());
        Assertions.assertEquals("name", movieMedia.getName());
        Assertions.assertEquals(90.0, movieMedia.getImdbScore());
        Assertions.assertEquals("backdropUrl", movieMedia.getBackdropUrl());
        Assertions.assertEquals("posterUrl", movieMedia.getPosterUrl());
        Assertions.assertEquals(3.8, movieMedia.getVoteAverage());
        Assertions.assertEquals(5, movieMedia.getNumberOfSeasons());
        Assertions.assertEquals(100, movieMedia.getNumberOfEpisodes());
        Assertions.assertEquals(LocalDate.now(), movieMedia.getReleaseDate());
    }

    @Test
    void testFindMovieById_WhenMovieNotFound_ReturnMovieOptionalEmpty() {
        UUID id = UUID.randomUUID();
        when(mediaJpaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<MovieMedia> movieMediaOpt = mediaCollection.findMovieById(id);
        Assertions.assertTrue(movieMediaOpt.isEmpty());
    }

    @Test
    void testFindBookById_WhenBookFound_ReturnBookOptional() {
        UUID id = UUID.randomUUID();
        BookMediaJpa bookMediaJpa = BookMediaJpa.builder()
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

        MediaJpa mediaJpa = MediaJpa.builder()
                .id(id)
                .postId(0L)
                .book(bookMediaJpa)
                .build();
        when(mediaJpaRepository.findById(id)).thenReturn(Optional.of(mediaJpa));

        Optional<BookMedia> bookMediaOpt = mediaCollection.findBookById(id);
        Assertions.assertTrue(bookMediaOpt.isPresent());

        BookMedia bookMedia = bookMediaOpt.get();

        Assertions.assertEquals("title", bookMedia.getTitle());
        Assertions.assertEquals("smallThumbnail", bookMedia.getSmallThumbnail());
        Assertions.assertEquals("printType", bookMedia.getPrintType());
        Assertions.assertEquals("thumbnail", bookMedia.getThumbnail());
        Assertions.assertEquals(50, bookMedia.getPageCount());
        Assertions.assertEquals("zima", bookMedia.getAuthor());
        Assertions.assertEquals(BookProvider.google, bookMedia.getProvider());
        Assertions.assertEquals("description", bookMedia.getDescription());
        Assertions.assertEquals("2010", bookMedia.getPublishDate());
        Assertions.assertEquals("summary", bookMedia.getSummary());
        Assertions.assertEquals("zimabooks", bookMedia.getPublisher());
    }
    @Test
    void testFindBookById_WhenBookNotFound_ReturnBookOptionalEmpty() {
        UUID id = UUID.randomUUID();
        when(mediaJpaRepository.findById(id)).thenReturn(Optional.empty());
        Optional<BookMedia> bookMediaOpt = mediaCollection.findBookById(id);
        Assertions.assertTrue(bookMediaOpt.isEmpty());
    }
}
