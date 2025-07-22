package com.zimaberlin.zimasocial.context.social.infastructure.adapter;

import com.zimaberlin.zimasocial.context.social.media.BookMedia;
import com.zimaberlin.zimasocial.context.social.media.MovieMedia;
import com.zimaberlin.zimasocial.context.social.post.Post;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.entity.media.BookMediaJpa;
import com.zimaberlin.zimasocial.entity.media.MovieMediaJpa;
import org.springframework.stereotype.Component;

@Component
public class PostDBAdapter {
    public Post convertPostEntityToPost(PostEntity post) {
        switch (post.getType()){
            case PostType.movie -> {
                MovieMedia movieDomain = null;
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
                    return new Post(post.getId(), post.getContent(), post.getLikeCount(), post.getCommentCount(), post.getCreatedAt(), post.getUpdatedAt(), post.getUser().getId(), movieDomain);
                }
            }
            case PostType.book ->  {
                if(post.getMedia() != null && post.getMedia().getBook() != null){
                    final BookMediaJpa bookMediaJpa = post.getMedia().getBook();
                   BookMedia bookMedia = BookMedia
                            .builder()
                            .author(bookMediaJpa.getAuthor())
                            .title(bookMediaJpa.getTitle())
                            .publisher(bookMediaJpa.getPublisher())
                            .pageCount(bookMediaJpa.getPageCount())
                            .publishDate(bookMediaJpa.getPublishDate())
                            .language(bookMediaJpa.getLanguage())
                            .thumbnail(bookMediaJpa.getThumbnail())
                            .smallThumbnail(bookMediaJpa.getSmallThumbnail())
                            .printType(bookMediaJpa.getPrintType())
                            .description(bookMediaJpa.getDescription())
                            .build();
                    return new Post(post.getId(), post.getContent(), post.getLikeCount(), post.getCommentCount(), post.getCreatedAt(), post.getUpdatedAt(), post.getUser().getId(), bookMedia);
                }
            }
            default -> {}
        }
        return null;
    }
}
