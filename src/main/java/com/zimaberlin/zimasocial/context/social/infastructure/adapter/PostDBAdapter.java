package com.zimaberlin.zimasocial.context.social.infastructure.adapter;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.media.book.BookMedia;
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
            case PostType.any -> {
                return new Post(post.getId(), post.getContent(), post.getLikeCount(), post.getCommentCount(), post.getCreatedAt(), post.getUpdatedAt(), new AuthorId(post.getUser().getId()));
            }
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
                    return new Post(post.getId(), post.getContent(), post.getLikeCount(), post.getCommentCount(), post.getCreatedAt(), post.getUpdatedAt(), new AuthorId(post.getUser().getId()), movieDomain);
                }
                return new Post(post.getId(), post.getContent(), post.getLikeCount(), post.getCommentCount(), post.getCreatedAt(), post.getUpdatedAt(), new AuthorId(post.getUser().getId()), movieDomain);
            }
            case PostType.book ->  {
                BookMedia bookMedia = null;
                if(post.getMedia() != null && post.getMedia().getBook() != null){
                    final BookMediaJpa bookMediaJpa = post.getMedia().getBook();
                    bookMedia = BookMedia
                            .builder()
                            .id(post.getMedia().getId())
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
                }
                return new Post(post.getId(), post.getContent(), post.getLikeCount(), post.getCommentCount(), post.getCreatedAt(), post.getUpdatedAt(), new AuthorId(post.getUser().getId()), bookMedia);
            }
            default -> {}
        }
        return null;
    }
}
