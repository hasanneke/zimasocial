package com.zima.zimasocial.context.social2.playlist.service;

import com.zima.zimasocial.context.social2.playlist.infastructure.PlaylistRepository;
import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayListVerifier {
    private final PlaylistRepository playlistRepository;

    public boolean maxNumberOfPlayListReached(AuthorId authorId) {
        long numOfPlayListCreated = playlistRepository.countByOwnerId(authorId);
        return numOfPlayListCreated >= 10;
    }
}
