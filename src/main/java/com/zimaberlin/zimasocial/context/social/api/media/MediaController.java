package com.zimaberlin.zimasocial.context.social.api.media;

import com.zimaberlin.zimasocial.context.social.media.MediaCollection;
import com.zimaberlin.zimasocial.context.social.media.book.BookMedia;
import com.zimaberlin.zimasocial.context.social.media.book.BookNotFoundException;
import com.zimaberlin.zimasocial.context.social.media.movie.MovieMedia;
import com.zimaberlin.zimasocial.context.social.media.movie.MovieNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping(path = "/api/v1/media")
@RestController
@RequiredArgsConstructor
public class MediaController {
    private final MediaCollection mediaCollection;
    @GetMapping(path = "/movies/{movieId}")
    public ResponseEntity<MovieMedia> getMovie(@PathVariable(name = "movieId") UUID movieId){
        MovieMedia movieMedia = mediaCollection.findMovieById(movieId).orElseThrow(MovieNotFoundException::new);
        return ResponseEntity.ok(movieMedia);
    }
    @GetMapping(path = "/books/{bookId}")
    public ResponseEntity<BookMedia> getBook(@PathVariable(name = "bookId") String bookId) {
        BookMedia book = mediaCollection.findBookById(UUID.fromString(bookId)).orElseThrow(BookNotFoundException::new);
        return ResponseEntity.ok(book);
    }
}
