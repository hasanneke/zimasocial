package com.zimaberlin.zimasocial.service.musicService.domain;

import com.zimaberlin.zimasocial.service.musicService.domain.MusicResponseView;

public interface SearchMusicService {
    MusicResponseView searchMusic(String query, int offset, int limit);
}
