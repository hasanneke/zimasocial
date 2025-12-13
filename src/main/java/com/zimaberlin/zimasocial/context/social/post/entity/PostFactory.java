package com.zimaberlin.zimasocial.context.social.post.entity;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.media.book.BookMedia;
import com.zimaberlin.zimasocial.context.social.media.movie.MovieMedia;
import com.zimaberlin.zimasocial.context.social.media.music.MusicMedia;
import com.zimaberlin.zimasocial.entity.PostType;

public class PostFactory {
    public static Post newAnyPost(Long postId, String content, AuthorId authorId) {
        return new Post(postId, content, PostType.any, authorId);
    }
    public static Post newMusicPost(Long postId, String content, AuthorId authorId, MusicMedia music) {
        return new Post(postId, content, authorId, music);
    }
    public static Post newMoviePost(Long postId, String content, AuthorId authorId, MovieMedia movie) {
        return new Post(postId, content, authorId, movie);
    }
    public static Post newBookPost(Long postId, String content, AuthorId authorId, BookMedia book) {
        return new Post(postId, content, authorId, book);
    }
}
