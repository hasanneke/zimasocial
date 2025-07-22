package com.zimaberlin.zimasocial.service.movieService.Impl;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class TDMBTVResponse {
    private int page;
    private List<TvShow> results;
    private int total_pages;
    private int total_results;

    @Getter
    public static class TvShow {
        private boolean adult;
        private String backdrop_path;
        private List<Integer> genre_ids;
        private int id;
        private String original_language;
        private String original_title;
        private String overview;
        private double popularity;
        private String poster_path;
        private LocalDate first_air_date;
        private String name;
        private boolean video;
        private double vote_average;
        private int vote_count;
    }
}
