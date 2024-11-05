package com.zimaberlin.zimasocial.service.musicService.impl;

import com.zimaberlin.zimasocial.service.musicService.domain.MusicResponseView;
import com.zimaberlin.zimasocial.service.musicService.domain.SearchMusicService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Service
public class SpotifyMusicService implements SearchMusicService {
    private final RestTemplate restTemplate;
    String baseUrl;

    public SpotifyMusicService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.baseUrl = "https://api.spotify.com/v1/search";
    }

    private SpotifyResponse.SpotifyTracks searchTracks(String query, int offset, int limit) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("type", "track")
                .queryParam("q", query)
                .queryParam("offset", offset)
                .queryParam("limit", limit);

        headers.set("Authorization", "Bearer BQC9m1VXpZiwQ4f6l5DxWPDw8sKX4WnKVErqhb00OYx_4EJXtjFSpOp1qbn3xR47TQJ0-gLF0bGsktigxP5v2lth1HZfGUbXB7Q7e45NDamRE5o5vh8");
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        // Create request entity
        HttpEntity<SpotifyResponse> request = new HttpEntity<>(headers);

        ResponseEntity<SpotifyResponse> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET,request, SpotifyResponse.class);
//        System.out.println(response);
       return response.getBody().getTracks();

    }

    @Override
    public MusicResponseView searchMusic(String query, int offset, int limit) {
        SpotifyResponse.SpotifyTracks tracks = searchTracks(query, offset, limit);
        MusicResponseView musicResponseView = new MusicResponseView();

        // SET META DATA
        musicResponseView.setHref(tracks.getHref());
        musicResponseView.setNext(tracks.getNext());
        musicResponseView.setOffset(tracks.getOffset());
        musicResponseView.setLimit(tracks.getLimit());
        musicResponseView.setPrevious(tracks.getPrevious());
        // SET TRACKS
        List<MusicResponseView.MusicView> domainTracks = tracks.getItems().stream().map((track)->{
            MusicResponseView.MusicView musicView = new MusicResponseView.MusicView();
            musicView.setId(track.getId());
            musicView.setName(track.getName());
            musicView.setHref(track.getHref());
            musicView.setType(track.getType());
            musicView.setUri(track.getUri());
            musicView.setPreviewUrl(track.getPreview_url());

            SpotifyResponse.Artist artist= track.getArtists().getFirst();
            SpotifyResponse.Album album = track.getAlbum();

            MusicResponseView.MusicView.Artist domainArtist = new MusicResponseView.MusicView.Artist();
            MusicResponseView.MusicView.Album domainAlbum = new MusicResponseView.MusicView.Album();

            domainAlbum.setAlbumType(album.getAlbum_type());
            domainAlbum.setName(album.getName());
            domainAlbum.setUrl(album.getExternal_urls().getSpotify());
            domainAlbum.setImageUrl(album.getImages().getFirst().getUrl());
            domainAlbum.setUri(album.getUri());

            domainArtist.setHref(artist.getHref());
            domainArtist.setId(artist.getId());
            domainArtist.setName(artist.getName());
            domainArtist.setUri(artist.getUri());

            musicView.setArtist(domainArtist);
            musicView.setAlbum(domainAlbum);
            return musicView;
        }).toList();

        musicResponseView.setItems(domainTracks);
        musicResponseView.setProvider("spotify");
        return musicResponseView;
    }
}
