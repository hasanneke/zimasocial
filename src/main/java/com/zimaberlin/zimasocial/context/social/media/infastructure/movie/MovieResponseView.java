package com.zimaberlin.zimasocial.context.social.media.infastructure.movie;

import com.zimaberlin.zimasocial.context.social.media.movie.MovieMediaType;
import com.zimaberlin.zimasocial.entity.media.MovieProvider;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class MovieResponseView {
    private int page;
    private List<Movie> results;
    private int totalPages;
    private int totalResults;
    private String provider;
    @Getter
    @Setter
    public static class Movie {
        private int id;
        private String title;
        private String posterUrl;
        private String backdropUrl;
        private LocalDate releaseDate;
        private String overview;
        private Integer voteCount;
        private Double voteAverage;
        private String href;
        private double popularity;
        private String originalTitle;
        private String originalLanguage;
        private MovieMediaType type;
        private Integer numberOfSeasons;
        private Integer numberOfEpisodes;
        private boolean adult;
        private MovieProvider provider;
    }
}
