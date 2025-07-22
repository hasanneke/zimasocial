package com.zimaberlin.zimasocial.entity.media;

import com.zimaberlin.zimasocial.context.social.media.MovieMediaType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Embeddable
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MovieMediaJpa {
    @Column(name = "movie_source_id", length = 64)
    private String id;
    @Column(name = "movie_name")
    private String name;
    @Column(name = "movie_poster_image_url", length = 512)
    private String posterUrl;
    @Column(name = "movie_backdrop_image_url")
    private String backdropUrl;
    @Column(name = "movie_description")
    private String description;
    @Column(name = "movie_summary")
    private String summary;
    @Column(name = "imdb_score")
    private Double imdbScore;
    @Column(name = "vote_count")
    private Integer voteCount;
    @Column(name = "vote_average")
    private Double voteAverage;
    @Column(name = "movie_release_date")
    private LocalDate releaseDate;
    @Column(name = "movie_genres")
    private String movieGenres;
    @Column(name = "movie_original_language")
    private String originalLanguage;
    @Enumerated(EnumType.STRING)
    private MovieProvider movieProvider;
    @Column(name = "movie_media_type")
    @Enumerated(EnumType.STRING)
    private MovieMediaType movieMediaType;
    @Column(name = "number_of_seasons")
    private Integer numberOfSeasons;
    @Column(name = "number_episodes")
    private Integer numberOfEpisodes;
}
