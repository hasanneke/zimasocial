package com.zimaberlin.zimasocial.context.social.infastructure.adapter;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.media.book.BookMedia;
import com.zimaberlin.zimasocial.context.social.media.movie.MovieMedia;
import com.zimaberlin.zimasocial.context.social.media.music.MusicMedia;
import com.zimaberlin.zimasocial.context.social.post.entity.Post;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.entity.media.MovieMediaJpa;
import org.springframework.stereotype.Component;

@Component
public class PostDBAdapter {
    public Post convertToPostDomain(PostEntity postEntity) {
        switch (postEntity.getType()){
            case PostType.any -> {
                return new Post(postEntity.getId(), postEntity.getContent(), postEntity.getLikeCount(), postEntity.getCommentCount(), postEntity.getCreatedAt(), postEntity.getUpdatedAt(), new AuthorId(postEntity.getUser().getId()), postEntity.getScore());
            }
            case PostType.movie -> {
                MovieMedia movieDomain = null;
                if(postEntity.getMedia() != null && postEntity.getMedia().getMovie() != null){
                    final MovieMediaJpa movie = postEntity.getMedia().getMovie();
                    movieDomain = MovieMedia
                            .builder()
                            .id(postEntity.getMedia().getId())
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
                    return new Post(postEntity.getId(), postEntity.getContent(), postEntity.getLikeCount(), postEntity.getCommentCount(), postEntity.getCreatedAt(), postEntity.getUpdatedAt(), new AuthorId(postEntity.getUser().getId()), movieDomain, postEntity.getScore());
                }
                return new Post(postEntity.getId(), postEntity.getContent(), postEntity.getLikeCount(), postEntity.getCommentCount(), postEntity.getCreatedAt(), postEntity.getUpdatedAt(), new AuthorId(postEntity.getUser().getId()), movieDomain, postEntity.getScore());
            }
            case PostType.book ->  {
                BookMedia bookMedia = null;
                if(postEntity.getMedia() != null && postEntity.getMedia().getBook() != null){
                    bookMedia = postEntity.getMedia().getBook().toBookMedia(postEntity.getMedia().getId());
                }
                assert bookMedia != null;
                return new Post(postEntity.getId(), postEntity.getContent(), postEntity.getLikeCount(), postEntity.getCommentCount(), postEntity.getCreatedAt(), postEntity.getUpdatedAt(), new AuthorId(postEntity.getUser().getId()), bookMedia, postEntity.getScore());
            }
            case PostType.music -> {
                MusicMedia musicMedia;
                if(postEntity.getMedia() != null && postEntity.getMedia().getSong() != null){
                    musicMedia = postEntity.getMedia().getSong().toMusicMedia(postEntity.getMedia().getId());
                    return new Post(postEntity.getId(), postEntity.getContent(), postEntity.getLikeCount(), postEntity.getCommentCount(), postEntity.getCreatedAt(), postEntity.getUpdatedAt(), new AuthorId(postEntity.getUser().getId()), musicMedia, postEntity.getScore());
                }
            }
            default -> {}
        }
        return null;
    }
}
