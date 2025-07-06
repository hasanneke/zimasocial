package com.zimaberlin.zimasocial.service.musicService.domain;

public interface SearchMusicClient {
    MusicResponseView searchMusic(String query, int offset, int limit);
    MusicResponseView.MusicView getMusic(String id);
}
