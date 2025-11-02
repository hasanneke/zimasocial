package com.zimaberlin.zimasocial.service.musicService.impl;

import com.zimaberlin.zimasocial.context.social.media.music.MusicMedia;
import com.zimaberlin.zimasocial.service.musicService.domain.MusicResponseView;
import com.zimaberlin.zimasocial.service.musicService.domain.SearchMusicClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Service
public class SpotifyMusicClient implements SearchMusicClient {
    private final RestTemplate restTemplate;
    private String accessToken;
    String baseUrl;

    @Autowired
    public SpotifyMusicClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.baseUrl = "https://api.spotify.com/v1/search";
    }

    private SpotifyResponse.SpotifyTracks searchTracks(String query, int offset, int limit) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uriBuilder = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("type", "track")
                .queryParam("q", query)
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        headers.set("Authorization", "Bearer %s".formatted(accessToken));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        // Create request entity
        HttpEntity<SpotifyResponse> request = new HttpEntity<>(headers);

        ResponseEntity<SpotifyResponse> response = restTemplate.exchange(uriBuilder, HttpMethod.GET,request, SpotifyResponse.class);
        return response.getBody().getTracks();
    }

    @Override
    public MusicResponseView searchMusic(String query, int offset, int limit) {
        SpotifyResponse.SpotifyTracks tracks = searchTracks(query, offset, limit);
        MusicResponseView musicResponseView = new MusicResponseView();

        // SET META DATA
        musicResponseView.setOffset(tracks.getOffset());
        musicResponseView.setLimit(tracks.getLimit());
        musicResponseView.setTotal(tracks.getTotal());
        // SET TRACKS
        List<MusicResponseView.MusicView> domainTracks = tracks.getItems().stream().map((track)->{
            MusicResponseView.MusicView musicView = new MusicResponseView.MusicView();
            musicView.setId(track.getId());
            musicView.setName(track.getName());
            musicView.setType(track.getType());
            musicView.setUri(track.getUri());
            musicView.setPreviewUrl(track.getPreview_url());

            SpotifyResponse.Artist artist= track.getArtists().getFirst();
            SpotifyResponse.Album album = track.getAlbum();

            MusicMedia.Artist domainArtist = new MusicMedia.Artist();
            MusicMedia.Album domainAlbum = new MusicMedia.Album();

            domainAlbum.setAlbumType(album.getAlbum_type());
            domainAlbum.setName(album.getName());
            domainAlbum.setUrl(album.getExternal_urls().getSpotify());
            domainAlbum.setImageUrl(album.getImages().getFirst().getUrl());
            domainAlbum.setUri(album.getUri());

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

    @Override
    public MusicResponseView.MusicView getMusic(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer %s".formatted(accessToken));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer %s".formatted(accessToken));
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<SpotifyResponse.SpotifyMusic> response = restTemplate.exchange(  // Note: changed to exchange
                "https://api.spotify.com/v1/tracks/{id}",
                HttpMethod.GET,
                request,
                SpotifyResponse.SpotifyMusic.class,
                id
        );
        MusicResponseView.MusicView musicView = new MusicResponseView.MusicView();
        MusicMedia.Artist artist = new MusicMedia.Artist();
        artist.setName(response.getBody().getArtists().getFirst().getName());
        MusicMedia.Album album = new MusicMedia.Album();
        album.setImageUrl(response.getBody().getAlbum().getImages().stream().findFirst().get().getUrl());
        musicView.setId(response.getBody().getId());
        musicView.setProvider("spotify");
        musicView.setName(response.getBody().getName());
        musicView.setArtist(artist);
        musicView.setType(response.getBody().getType());
        musicView.setPreviewUrl(response.getBody().getPreview_url());
        musicView.setUri(response.getBody().getUri());
        return musicView;
    }

    @Override
    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
