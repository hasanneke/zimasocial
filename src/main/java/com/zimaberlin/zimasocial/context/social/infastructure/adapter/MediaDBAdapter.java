package com.zimaberlin.zimasocial.context.social.infastructure.adapter;

import com.zimaberlin.zimasocial.context.social.media.MovieMedia;
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
                .build();
    }
}
