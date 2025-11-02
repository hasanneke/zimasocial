package com.zimaberlin.zimasocial.post;

import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import com.zimaberlin.zimasocial.context.social.author.AuthorRepository;
import com.zimaberlin.zimasocial.context.social.comment.CommentRepository;
import com.zimaberlin.zimasocial.context.social.like.LikeRepository;
import com.zimaberlin.zimasocial.context.social.media.MediaCollection;
import com.zimaberlin.zimasocial.context.social.media.movie.MovieMedia;
import com.zimaberlin.zimasocial.context.social.media.movie.MovieMediaType;
import com.zimaberlin.zimasocial.context.social.media.MovieSearcher;
import com.zimaberlin.zimasocial.context.social.media.book.BookMedia;
import com.zimaberlin.zimasocial.context.social.media.book.BookNotFoundException;
import com.zimaberlin.zimasocial.context.social.media.BookSearcher;
import com.zimaberlin.zimasocial.context.social.post.Post;
import com.zimaberlin.zimasocial.context.social.post.PostRepository;
import com.zimaberlin.zimasocial.context.social.post.PostService;
import com.zimaberlin.zimasocial.entity.PostType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private MovieSearcher movieSearcher;
    @Mock
    private BookSearcher bookSearcher;
    @Mock
    private MediaCollection mediaRepository;
    @InjectMocks
    private PostService postService;
    @Test
    void testCreateBookPost_WhenBookExists_CreatePostWithBook() {
        Author authenticatedAuthor = new Author(new AuthorId(0L), "", "", LocalDateTime.now());
        String searchedBookId = "searchedBookId";
        BookMedia searchBookMediaItem = BookMedia.builder().build();
        when(bookSearcher.getBook(searchedBookId)).thenReturn(Optional.of(searchBookMediaItem));
        when(authorRepository.getAuthenticatedAuthor()).thenReturn(authenticatedAuthor);
        postService.createBookPost("", searchedBookId);

        ArgumentCaptor<Post> argumentCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(argumentCaptor.capture());
        Post savedPost = argumentCaptor.getValue();
        Assertions.assertNotNull(savedPost.getBook());
        Assertions.assertEquals(PostType.book, savedPost.getType());
        Assertions.assertEquals("", savedPost.getContent());
    }

    @Test
    void testCreateBookPost_WhenBookNotExists_ThrowBookNotFoundException() {
        Author authenticatedAuthor = new Author(new AuthorId(0L), "", "", LocalDateTime.now());
        String searchedBookId = "searchedBookId";
        when(bookSearcher.getBook(searchedBookId)).thenReturn(Optional.empty());
        when(authorRepository.getAuthenticatedAuthor()).thenReturn(authenticatedAuthor);
        Assertions.assertThrows(BookNotFoundException.class, () -> postService.createBookPost("", searchedBookId));
    }
    @Test
    void testCreateMoviePost_WhenMovieExists_CreatePostWithMovie() {
        Author authenticatedAuthor = new Author(new AuthorId(0L), "", "", LocalDateTime.now());
        Integer movieId = 123;
        MovieMediaType movieType = MovieMediaType.movie;
        String language = "en";
        MovieMedia searchMovieMediaItem = MovieMedia.builder().build();

        when(authorRepository.getAuthenticatedAuthor()).thenReturn(authenticatedAuthor);
        when(movieSearcher.getMovie(movieId, movieType, language)).thenReturn(searchMovieMediaItem);
        when(postRepository.nextSequence()).thenReturn(1L);

        postService.createMoviePost("Test content", movieId, movieType, language);

        ArgumentCaptor<Post> argumentCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(argumentCaptor.capture());
        Post savedPost = argumentCaptor.getValue();

        Assertions.assertNotNull(savedPost.getMovie());
        Assertions.assertEquals(PostType.movie, savedPost.getType());
        Assertions.assertEquals("Test content", savedPost.getContent());
        Assertions.assertEquals(authenticatedAuthor.getId(), savedPost.getAuthorId());
    }
}
