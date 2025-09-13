package com.zimaberlin.zimasocial.context.social.media;

import com.zimaberlin.zimasocial.entity.media.MovieProvider;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
public class MovieMedia {
        private UUID id;
        private String resourceId;
        private String name;
        private String posterUrl;
        private String description;
        private String summary;
        private Double imdbScore;
        private Integer voteCount;
        private Double voteAverage;
        private LocalDate releaseDate;
        private String movieGenres;
        private String originalLanguage;
        private MovieProvider movieProvider;
        private String backdropUrl;
        private MovieMediaType type;
        private Integer numberOfEpisodes;
        private Integer numberOfSeasons;

        public void assignId(UUID id){
                this.id = id;
        }
}
