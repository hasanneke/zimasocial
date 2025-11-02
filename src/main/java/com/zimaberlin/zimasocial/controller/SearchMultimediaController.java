package com.zimaberlin.zimasocial.controller;

import com.zimaberlin.zimasocial.context.social.media.BookSearcher;
import com.zimaberlin.zimasocial.service.musicService.domain.MusicResponseView;
import com.zimaberlin.zimasocial.service.musicService.domain.SearchMusicClient;
import com.zimaberlin.zimasocial.service.musicService.impl.SpotifyMusicClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/mediasearch")
public class SearchMultimediaController {
    private final SearchMusicClient spotifyMusicService;
    private final BookSearcher bookSearcher;

    @Autowired
    public SearchMultimediaController(SpotifyMusicClient spotifyMusicService, BookSearcher bookSearcher) {
        this.spotifyMusicService = spotifyMusicService;
        this.bookSearcher = bookSearcher;
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
}
