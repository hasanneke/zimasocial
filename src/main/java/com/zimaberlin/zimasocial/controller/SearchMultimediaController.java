package com.zimaberlin.zimasocial.controller;

import com.zimaberlin.zimasocial.service.bookService.domain.BookResponseView;
import com.zimaberlin.zimasocial.service.bookService.domain.SearchBookService;
import com.zimaberlin.zimasocial.service.movieService.domain.MovieResponseView;
import com.zimaberlin.zimasocial.service.movieService.domain.SearchMovieService;
import com.zimaberlin.zimasocial.service.musicService.domain.MusicResponseView;
import com.zimaberlin.zimasocial.service.musicService.impl.SpotifyMusicClient;
import com.zimaberlin.zimasocial.service.musicService.domain.SearchMusicClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/mediasearch")
public class SearchMultimediaController {
    private final SearchMusicClient spotifyMusicService;
    private final SearchMovieService movieService;
    private final SearchBookService searchBookService;

    @Autowired
    public SearchMultimediaController(SpotifyMusicClient spotifyMusicService, SearchMovieService movieService, SearchBookService searchBookService) {
        this.spotifyMusicService = spotifyMusicService;
        this.movieService = movieService;
        this.searchBookService = searchBookService;
    }

    @GetMapping(path = "/musics/search")
   public ResponseEntity<MusicResponseView> searchTracks(
            @RequestParam(name = "q") String query,
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "20")  int limit){
        MusicResponseView music = spotifyMusicService.searchMusic(query, offset, limit);
        return ResponseEntity.ok(music);
    }

    @GetMapping(path = "/musics/{musicId}")
    public ResponseEntity<MusicResponseView.MusicView> searchTracks(
            String musicId){
        MusicResponseView.MusicView music = spotifyMusicService.getMusic(musicId);
        return ResponseEntity.ok(music);
    }

    @GetMapping(path = "/movies/search")
    public ResponseEntity<MovieResponseView> searchMovies(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "language", defaultValue = "en") String language,
            @RequestParam(name = "page", defaultValue = "1") int page){
        MovieResponseView responseView = movieService.searchMovie(query, page, language);
        return ResponseEntity.ok(responseView);
    }

//    @GetMapping(path = "/movies/{movieId}")
//    public ResponseEntity<MovieResponseView.Movie> getMovie(@PathVariable(name = "movieId") Integer movieId, @RequestParam(name = "language") String language){
//        MovieResponseView.Movie movie= movieService.getMovie(movieId, language);
//        return ResponseEntity.ok(movie);
//    }

    @GetMapping(path = "/books/search")
    public ResponseEntity<BookResponseView> searchBooks(@RequestParam(name = "query") String query){
        BookResponseView bookResponseView = searchBookService.searchBook(query);

        return ResponseEntity.ok(bookResponseView);
    }

    @GetMapping(path = "/books/{bookId}")
    public ResponseEntity<BookResponseView.Book> getBook(@PathVariable(name = "bookId") String bookId){
        BookResponseView.Book book= searchBookService.getBook(bookId);
        return ResponseEntity.ok(book);
    }
}
