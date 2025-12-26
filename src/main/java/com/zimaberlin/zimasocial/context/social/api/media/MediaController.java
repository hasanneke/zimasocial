package com.zimaberlin.zimasocial.context.social.api.media;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zimaberlin.zimasocial.context.social.infastructure.service.googleBooks.MediaService;
import com.zimaberlin.zimasocial.context.social.media.MediaCollection;
import com.zimaberlin.zimasocial.context.social.media.movie.MovieMedia;
import com.zimaberlin.zimasocial.context.social.media.movie.MovieNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    private final MediaService mediaService;
    private final ObjectMapper objectMapper;

    @GetMapping(path = "/movies/{movieId}")
    public ResponseEntity<MovieMedia> getMovie(@PathVariable(name = "movieId") UUID movieId){
        MovieMedia movieMedia = mediaCollection.findMovieById(movieId).orElseThrow(MovieNotFoundException::new);
        return ResponseEntity.ok(movieMedia);
    }

    @GetMapping("/{mediaId}")
    public ResponseEntity<JsonNode> getMedia(@PathVariable(name = "mediaId") UUID mediaId) throws JsonProcessingException {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(objectMapper.readTree(mediaService.get(mediaId)));
    }
}
