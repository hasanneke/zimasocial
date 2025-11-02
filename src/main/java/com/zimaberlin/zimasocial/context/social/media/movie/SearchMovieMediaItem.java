package com.zimaberlin.zimasocial.context.social.media.movie;

import com.zimaberlin.zimasocial.entity.media.MovieProvider;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class SearchMovieMediaItem {
    private int resourceId;
    private String name;
    private String posterUrl;
    private String backdropUrl;
    private String description;
    private String summary;
    private Double imdbScore;
    private Integer voteCount;
    private Double voteAverage;
    private LocalDate releaseDate;
    private String movieGenres;
    private String originalLanguage;
    private MovieProvider movieProvider;
    private MovieMediaType type;
}
