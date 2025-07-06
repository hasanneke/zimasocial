package com.zimaberlin.zimasocial.context.social.infastructure.adapter;

import com.zimaberlin.zimasocial.context.social.media.MovieMedia;
import com.zimaberlin.zimasocial.context.social.post.Post;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.entity.media.MovieMediaJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostDBAdapter {
    private final AuthorUserEntityAdapter authorUserEntityAdapter;

    @Autowired
    public PostDBAdapter(AuthorUserEntityAdapter authorUserEntityAdapter) {
        this.authorUserEntityAdapter = authorUserEntityAdapter;
    }

    public Post convertPostEntityToPost(PostEntity post) {
        MovieMedia movieDomain = null;
        switch (post.getType()){
            case PostType.movie -> {
                if(post.getMedia() != null && post.getMedia().getMovie() != null){
                    final MovieMediaJpa movie = post.getMedia().getMovie();
                    movieDomain = MovieMedia
                            .builder()
                            .id(post.getMedia().getId())
                            .description(movie.getDescription())
                            .originalLanguage(movie.getOriginalLanguage())
                            .posterUrl(movie.getPosterUrl())
                            .summary(movie.getSummary())
                            .releaseDate(movie.getReleaseDate())
                            .voteAverage(movie.getVoteAverage())
                            .voteCount(movie.getVoteCount())
                            .imdbScore(movie.getImdbScore())
                            .name(movie.getName())
                            .movieGenres(movie.getMovieGenres())
                            .movieProvider(movie.getMovieProvider())
                            .build();
                }
            }
            default -> {}
        }
        return new Post(post.getId(), post.getContent(), post.getType(), post.getLikeCount(), post.getCommentCount(), post.getCreatedAt(), post.getUpdatedAt(), post.getUser().getId(), movieDomain);
    }
}
