package com.zimaberlin.zimasocial.service.movieService.Impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Getter
public class TDMBMovieResponse {

    private int page;
    private List<MovieResult> results;
    private int total_pages;
    private int total_results;

    @Getter
    public static class MovieResult {
        private boolean adult;
        private String backdrop_path;
        private List<Integer> genre_ids;
        private int id;
        private String original_language;
        private String original_title;
        private String overview;
        private double popularity;
        private String poster_path;
        private Date release_date;
        private String title;
        private boolean video;
        private double vote_average;
        private int vote_count;
    }
}
