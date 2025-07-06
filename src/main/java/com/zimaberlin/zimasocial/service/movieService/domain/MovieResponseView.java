package com.zimaberlin.zimasocial.service.movieService.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
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
        private boolean adult;
    }
}
