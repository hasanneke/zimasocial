package com.zimaberlin.zimasocial.context.social.media.infastructure.music;

import com.github.f4b6a3.uuid.UuidCreator;
import com.zimaberlin.zimasocial.context.social.media.MusicSearcher;
import com.zimaberlin.zimasocial.context.social.media.music.MusicMedia;
import com.zimaberlin.zimasocial.entity.media.SongProvider;
import com.zimaberlin.zimasocial.service.musicService.domain.MusicResponseView;
import com.zimaberlin.zimasocial.service.musicService.domain.SearchMusicClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpotifyMusicSearcher implements MusicSearcher {
    private final SearchMusicClient searchMusicClient;
    @Override
    public Optional<MusicMedia> get(String id) {
        MusicResponseView.MusicView musicView = searchMusicClient.getMusic(id);
        return Optional.of(MusicMedia.builder()
                .id(UuidCreator.getTimeOrdered())
                .resourceId(musicView.getId())
                .songTitle(musicView.getName())
                .uri(musicView.getUri())
                .previewUrl(musicView.getPreviewUrl())
                .artist(musicView.getArtist())
                .album(musicView.getAlbum())
                .type(musicView.getType())
                .provider(musicView.getProvider() != null ? SongProvider.valueOf(musicView.getProvider()) : null)
                .build());
    }
}
