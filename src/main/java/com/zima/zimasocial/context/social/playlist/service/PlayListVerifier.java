package com.zima.zimasocial.context.social.playlist.service;

import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.playlist.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayListVerifier {
    private final PlaylistRepository playlistRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public PlayListVerifier(PlaylistRepository playlistRepository, AuthorRepository authorRepository) {
        this.playlistRepository = playlistRepository;
        this.authorRepository = authorRepository;
    }
    public boolean maxNumberOfPlayListReached(AuthorId authorId) {
        long numOfPlayListCreated = playlistRepository.countByAuthorId(authorId);
        return numOfPlayListCreated >= 10;
    }
}
