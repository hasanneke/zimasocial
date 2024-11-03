package com.zimaberlin.zimasocial.controller;

import com.zimaberlin.zimasocial.service.MusicService.DTOs.SpotifyResponse;
import com.zimaberlin.zimasocial.service.MusicService.SpotifyMusicService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/mediasearch")
public class SearchMultimediaController {
    private SpotifyMusicService spotifyMusicService;

    @Autowired
    public SearchMultimediaController(SpotifyMusicService spotifyMusicService) {
        this.spotifyMusicService = spotifyMusicService;
    }

    @GetMapping(path = "/music")
    ResponseEntity<SpotifyResponse.SpotifyTracks> searchTracks(
            @RequestParam(name = "q") String query,
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "20")  int limit
    ){
        SpotifyResponse.SpotifyTracks spotifyTracks = spotifyMusicService.searchTracks(query, offset, limit);

        return ResponseEntity.ok(spotifyTracks);
    }
}
