package com.zimaberlin.zimasocial.context.social.infastructure.adapter;

import com.zimaberlin.zimasocial.context.social.media.MovieMedia;
import com.zimaberlin.zimasocial.context.social.media.book.BookMedia;
import com.zimaberlin.zimasocial.entity.media.BookMediaJpa;
import com.zimaberlin.zimasocial.entity.media.MediaJpa;
import com.zimaberlin.zimasocial.entity.media.MovieMediaJpa;
import org.springframework.stereotype.Component;

@Component
public class MediaDBAdapter {
    public MovieMedia convertToMedia(MediaJpa media) {
        if(media == null) return null;
        MovieMediaJpa movie = media.getMovie();
        return MovieMedia.builder()
                .id(media.getId())
                .backdropUrl(movie.getBackdropUrl())
                .posterUrl(movie.getPosterUrl())
                .description(movie.getDescription())
                .movieProvider(movie.getMovieProvider())
                .originalLanguage(movie.getOriginalLanguage())
                .name(movie.getName())
                .summary(movie.getSummary())
                .description(movie.getDescription())
                .movieGenres(movie.getMovieGenres())
                .releaseDate(movie.getReleaseDate())
                .voteCount(movie.getVoteCount())
                .voteAverage(movie.getVoteAverage())
                .imdbScore(movie.getImdbScore())
                .type(movie.getMovieMediaType())
                .numberOfSeasons(movie.getNumberOfSeasons())
                .numberOfEpisodes(movie.getNumberOfEpisodes())
                .build();
    }
    public BookMedia convertToBookMedia(MediaJpa media) {
        if(media == null) return null;
        BookMediaJpa book = media.getBook();
        return BookMedia.builder()
                .id(media.getId())
                .summary(book.getSummary())
                .provider(book.getProvider())
                .thumbnail(book.getThumbnail())
                .smallThumbnail(book.getSmallThumbnail())
                .author(book.getAuthor())
                .title(book.getTitle())
                .description(book.getDescription())
                .postId(media.getPostId())
                .publishDate(book.getPublishDate())
                .pageCount(book.getPageCount())
                .printType(book.getPrintType())
                .publisher(book.getPublisher())
                .build();
    }
}
