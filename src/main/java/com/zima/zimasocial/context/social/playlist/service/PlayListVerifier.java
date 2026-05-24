package com.zima.zimasocial.context.social.playlist.service;

import com.zima.zimasocial.context.social.author.repository.AuthorRepositoryDomain;
import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.context.social.playlist.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayListVerifier {
    private final PlaylistRepository playlistRepository;
    private final AuthorRepositoryDomain authorRepository;

    @Autowired
    public PlayListVerifier(PlaylistRepository playlistRepository, AuthorRepositoryDomain authorRepository) {
        this.playlistRepository = playlistRepository;
        this.authorRepository = authorRepository;
    }
    public boolean maxNumberOfPlayListReached(AuthorDomainId authorId) {
        long numOfPlayListCreated = playlistRepository.countByAuthorId(authorId);
        return numOfPlayListCreated >= 10;
    }
}
