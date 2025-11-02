package com.zimaberlin.zimasocial.service.movieService.Impl;

import com.zimaberlin.zimasocial.context.social.media.movie.MovieMediaType;
import com.zimaberlin.zimasocial.entity.media.MovieProvider;
import com.zimaberlin.zimasocial.context.social.media.infastructure.movie.MovieResponseView;

public class MovieResponseViewAdapter {
    public static MovieResponseView.Movie convertTDMBMovieToDomain(TDMBMultiMediaTVMovieSearchResponse.Item e) {
        MovieMediaType type = e.getMedia_type().equals("tv") ? MovieMediaType.tv : MovieMediaType.movie;
        MovieResponseView.Movie movie = new MovieResponseView.Movie();
        movie.setId(e.getId());
        movie.setTitle(type.equals(MovieMediaType.movie) ? e.getTitle() : e.getName());
        movie.setBackdropUrl(String.format("https://image.tmdb.org/t/p/w500/%s", e.getBackdrop_path()));
        movie.setReleaseDate(type == MovieMediaType.movie ? e.getRelease_date() : e.getFirst_air_date());
        movie.setOverview(e.getOverview());
        movie.setVoteCount(e.getVote_count());
        movie.setVoteAverage(e.getVote_average());
        movie.setAdult(e.isAdult());
        movie.setPopularity(e.getPopularity());
        movie.setOriginalLanguage(e.getOriginal_language());
        movie.setOriginalTitle(type.equals(MovieMediaType.tv) ? e.getOriginal_name() : e.getOriginal_title());
        movie.setNumberOfEpisodes(e.getNumber_of_episodes());
        movie.setNumberOfSeasons(e.getNumber_of_seasons());
        movie.setType(type);
        String imageUrl = String.format("https://image.tmdb.org/t/p/w500/%s", e.getPoster_path());
        movie.setPosterUrl(imageUrl);
        movie.setProvider(MovieProvider.TMDB);
        return movie;
    }
    public static MovieResponseView.Movie convertTDMBMovieToDomain(TDMBMultiMediaTVMovieSearchResponse.MovieItem e) {
        MovieResponseView.Movie movie = new MovieResponseView.Movie();
        movie.setId(e.getId());
        movie.setTitle(e.getTitle());
        movie.setBackdropUrl(String.format("https://image.tmdb.org/t/p/w500/%s", e.getBackdrop_path()));
        movie.setReleaseDate(e.getRelease_date());
        movie.setOverview(e.getOverview());
        movie.setVoteCount(e.getVote_count());
        movie.setVoteAverage(e.getVote_average());
        movie.setAdult(e.isAdult());
        movie.setPopularity(e.getPopularity());
        movie.setOriginalLanguage(e.getOriginal_language());
        movie.setOriginalTitle(e.getOriginal_title());
        movie.setType(MovieMediaType.movie);
        String imageUrl = String.format("https://image.tmdb.org/t/p/w500/%s", e.getPoster_path());
        movie.setPosterUrl(imageUrl);
        movie.setProvider(MovieProvider.TMDB);
        return movie;
    }

    public static MovieResponseView.Movie convertTDMBMovieToDomain(TDMBMultiMediaTVMovieSearchResponse.TvShow e) {
        MovieResponseView.Movie movie = new MovieResponseView.Movie();
        movie.setId(e.getId());
        movie.setTitle(e.getName());
        movie.setBackdropUrl(String.format("https://image.tmdb.org/t/p/w500/%s", e.getBackdrop_path()));
        movie.setReleaseDate(e.getFirst_air_date());
        movie.setOverview(e.getOverview());
        movie.setVoteCount(e.getVote_count());
        movie.setVoteAverage(e.getVote_average());
        movie.setAdult(e.isAdult());
        movie.setPopularity(e.getPopularity());
        movie.setOriginalLanguage(e.getOriginal_language());
        movie.setOriginalTitle(e.getOriginal_name());
        movie.setType(MovieMediaType.tv);
        String imageUrl = String.format("https://image.tmdb.org/t/p/w500/%s", e.getPoster_path());
        movie.setPosterUrl(imageUrl);
        movie.setProvider(MovieProvider.TMDB);
        return movie;
    }
}
