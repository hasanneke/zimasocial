package com.zimaberlin.zimasocial.context.social.infastructure.repository;

import com.zimaberlin.zimasocial.context.social.infastructure.adapter.MediaDBAdapter;
import com.zimaberlin.zimasocial.context.social.media.MediaRepository;
import com.zimaberlin.zimasocial.context.social.media.MovieMedia;
import com.zimaberlin.zimasocial.entity.media.MediaJpa;
import com.zimaberlin.zimasocial.entity.media.MediaType;
import com.zimaberlin.zimasocial.entity.media.MovieMediaJpa;
import com.zimaberlin.zimasocial.repository.MediaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class MediaDBRepository implements MediaRepository {
    private final MediaJpaRepository mediaJpaRepository;
    private final MediaDBAdapter mediaDBAdapter;
    @Override
    public MovieMedia save(MovieMedia movie) {
      MediaJpa mediaJpa = mediaJpaRepository.save(
                MediaJpa.builder()
                        .type(MediaType.MOVIE)
                        .movie(MovieMediaJpa
                                .builder()
                                .name(movie.getName())
                                .movieProvider(movie.getMovieProvider())
                                .description(movie.getDescription())
                                .posterUrl(movie.getPosterUrl())
                                .backdropUrl(movie.getBackdropUrl())
                                .movieGenres(movie.getMovieGenres())
                                .summary(movie.getSummary())
                                .imdbScore(movie.getImdbScore())
                                .originalLanguage(movie.getOriginalLanguage())
                                .releaseDate(movie.getReleaseDate())
                                .voteCount(movie.getVoteCount())
                                .voteAverage(movie.getVoteAverage())
                                .build())
                        .build());
        movie.assignId(mediaJpa.getId());
        return movie;
    }

    @Override
    public Optional<MovieMedia> findMovieById(UUID id) {
        MediaJpa mediaJpa = mediaJpaRepository.findById(id).orElse(null);
        return Optional.ofNullable(mediaDBAdapter.convertToMedia(mediaJpa));
    }
}
