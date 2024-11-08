package com.zimaberlin.zimasocial.controller;

import com.zimaberlin.zimasocial.service.bookService.domain.BookResponseView;
import com.zimaberlin.zimasocial.service.bookService.domain.SearchBookService;
import com.zimaberlin.zimasocial.service.movieService.Impl.TDMBMovieService;
import com.zimaberlin.zimasocial.service.movieService.domain.MovieResponseView;
import com.zimaberlin.zimasocial.service.movieService.domain.SearchMovieService;
import com.zimaberlin.zimasocial.service.musicService.domain.MusicResponseView;
import com.zimaberlin.zimasocial.service.musicService.impl.SpotifyMusicService;
import com.zimaberlin.zimasocial.service.musicService.domain.SearchMusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/mediasearch")
public class SearchMultimediaController {
    private final SearchMusicService spotifyMusicService;
    private final SearchMovieService movieService;
    private final SearchBookService searchBookService;

    @Autowired
    public SearchMultimediaController(SpotifyMusicService spotifyMusicService, TDMBMovieService movieService, SearchBookService searchBookService) {
        this.spotifyMusicService = spotifyMusicService;
        this.movieService = movieService;
        this.searchBookService = searchBookService;
    }

    @GetMapping(path = "/music")
   public ResponseEntity<MusicResponseView> searchTracks(
            @RequestParam(name = "q") String query,
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "20")  int limit
    ){
        MusicResponseView music = spotifyMusicService.searchMusic(query, offset, limit);

        return ResponseEntity.ok(music);
    }

    @GetMapping(path = "/movie")
    public ResponseEntity<MovieResponseView> searchMovies(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "language", defaultValue = "en") String language,
            @RequestParam(name = "page", defaultValue = "1") int page
    ){
        MovieResponseView responseView = movieService.searchMovie(query, page, language);
        return ResponseEntity.ok(responseView);
    }

    @GetMapping(path = "/movie/{moveId}")
    public ResponseEntity<MovieResponseView.Movie> getMovie(@PathVariable(name = "movieId") Integer movieId){
        MovieResponseView.Movie movie= movieService.getMovie(movieId);
        return ResponseEntity.ok(movie);
    }

    @GetMapping(path = "/books/search")
    public ResponseEntity<BookResponseView> searchBooks(@RequestParam(name = "query") String query){
        BookResponseView bookResponseView = searchBookService.searchBook(query);

        return ResponseEntity.ok(bookResponseView);
    }
}
