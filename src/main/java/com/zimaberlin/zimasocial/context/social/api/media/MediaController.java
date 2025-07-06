package com.zimaberlin.zimasocial.context.social.api.media;
import com.zimaberlin.zimasocial.context.social.media.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping(path = "/api/v1/media")
@RestController
@RequiredArgsConstructor
public class MediaController {
    private final MovieSearcher movieSearcher;
    private final MediaRepository mediaRepository;
    @GetMapping(path = "/movies/search")
    public ResponseEntity<List<SearchMovieMediaItem>> searchMovies(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "language", defaultValue = "en") String language,
            @RequestParam(name = "page", defaultValue = "1") int page){
        List<SearchMovieMediaItem> responseView = movieSearcher.search(query, page, language);
        return ResponseEntity.ok(responseView);
    }
    @GetMapping(path = "/movies/{movieId}")
    public ResponseEntity<MovieMedia> getMovie(@PathVariable(name = "movieId") UUID movieId){
        MovieMedia movieMedia = mediaRepository.findMovieById(movieId).orElseThrow(MovieNotFoundException::new);
        return ResponseEntity.ok(movieMedia);
    }
}
