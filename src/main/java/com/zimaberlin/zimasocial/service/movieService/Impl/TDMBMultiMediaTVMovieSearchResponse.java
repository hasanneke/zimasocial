package com.zimaberlin.zimasocial.service.movieService.Impl;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class TDMBMultiMediaTVMovieSearchResponse {
    private int page;
    private List<TDMBMultiMediaTVMovieSearchResponse.Item> results;
    private int total_pages;
    private int total_results;

    @Getter
    public static class Item {
        private int id;
        private boolean adult;
        private String backdrop_path;
        private List<Integer> genre_ids;
        private String original_language;
        private String original_title;
        private String overview;
        private double popularity;
        private String poster_path;
        private LocalDate release_date;
        private LocalDate first_air_date;
        private String name;
        private String title;
        private boolean video;
        private double vote_average;
        private int vote_count;
        private String media_type;
        private List<String> origin_country;
        private String original_name;
        private Integer number_of_episodes;
        private Integer number_of_seasons;
    }

    @Getter
    public static class MovieItem {
        private int id;
        private boolean adult;
        private String backdrop_path;
        private List<Integer> genre_ids;
        private String original_language;
        private String original_title;
        private String overview;
        private double popularity;
        private String poster_path;
        private LocalDate release_date;
        private String name;
        private String title;
        private boolean video;
        private double vote_average;
        private int vote_count;
        private List<String> origin_country;
    }

    @Getter
    public static class TvShow {
        private int id;
        private boolean adult;
        private String backdrop_path;
        private List<Integer> genre_ids;
        private String original_language;
        private String overview;
        private double popularity;
        private String poster_path;
        private LocalDate first_air_date;
        private String name;
        private boolean video;
        private double vote_average;
        private int vote_count;
        private List<String> origin_country;
        private String original_name;
        private Integer number_of_episodes;
        private Integer number_of_seasons;
    }
}
