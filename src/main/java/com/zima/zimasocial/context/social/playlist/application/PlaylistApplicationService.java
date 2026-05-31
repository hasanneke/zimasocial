package com.zima.zimasocial.context.social.playlist.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.f4b6a3.uuid.UuidCreator;
import com.zima.zimasocial.context.social.media.repository.MediaItemJpaRepository;
import com.zima.zimasocial.context.social.media.exception.MediaNotFoundException;
import com.zima.zimasocial.context.social.media.entity.Media;
import com.zima.zimasocial.context.social.playlist.api.dto.PlaylistDTO;
import com.zima.zimasocial.context.social.playlist.api.dto.PlaylistItemDTO;
import com.zima.zimasocial.context.social.playlist.exception.PlaylistNotFoundException;
import com.zima.zimasocial.context.social.playlist.infastructure.PlaylistItem;
import com.zima.zimasocial.context.social.playlist.infastructure.Playlist;
import com.zima.zimasocial.context.social.playlist.infastructure.PlaylistRepository;
import com.zima.zimasocial.context.social.playlist.service.PlayListVerifier;
import com.zima.zimasocial.context.social.playlist.values.PlayListId;
import com.zima.zimasocial.context.social.playlist.values.PlayListItemPayload;
import com.zima.zimasocial.context.social.playlist.values.PlayListPayload;
import com.zima.zimasocial.context.social.playlist.values.PlayListUpdatePayload;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.media.value.MediaId;
import com.zima.zimasocial.context.social.author.exception.AuthorNotFoundException;
import com.zima.zimasocial.context.social.author.repository.AuthorRepository;
import com.zima.zimasocial.entity.MediaType;
import com.zima.zimasocial.exception.ConflictException;
import com.zima.zimasocial.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistApplicationService {

    private final PlayListVerifier playListVerifier;
    private final AuthorRepository authorRepository;
    private final MediaItemJpaRepository mediaItemJpaRepository;
    private final PlaylistRepository playlistJpaRepository;

    @Transactional
    public void create(PlayListPayload payload) {
        Author author = authorRepository.getAuthenticatedAuthor();
        if(playListVerifier.maxNumberOfPlayListReached(author.getId())){
            throw new ConflictException("max_number_of_play_list_reached", "max_number_of_play_list_reached");
        }
        Playlist playlist = Playlist.create(new PlayListId(UuidCreator.getTimeOrdered()), payload.getName(), author.getId(), payload.getType());
        playlistJpaRepository.save(playlist);
    }

    @Transactional
    public void create(PlayListPayload payload, Author author) {
        if(playListVerifier.maxNumberOfPlayListReached(author.getId())){
            throw new ConflictException("max_number_of_play_list_reached", "max_number_of_play_list_reached");
        }
        Playlist playlist = Playlist.create(new PlayListId(UuidCreator.getTimeOrdered()), payload.getName(), author.getId(), payload.getType());
        playlistJpaRepository.save(playlist);
    }
    @Transactional
    public void update(PlayListId id, PlayListUpdatePayload payload) {
        Author author = authorRepository.getAuthenticatedAuthor();
        Playlist playlist = playlistJpaRepository.findById(id.value()).orElseThrow(PlaylistNotFoundException::new);
        playlist.updateName(payload.getName(), author.getId());
        playlistJpaRepository.save(playlist);
    }
    @Transactional
    public void addItem(PlayListId playlistId, PlayListItemPayload payload) {
        Media media = mediaItemJpaRepository.findById(new MediaId(payload.getMediaId())).orElseThrow(MediaNotFoundException::new);
        Author modifier = authorRepository.getAuthenticatedAuthor();
        Playlist playlist = playlistJpaRepository.findById(playlistId.value()).orElseThrow(PlaylistNotFoundException::new);
        playlist.addItem(media, modifier.getId());
        playlistJpaRepository.save(playlist);
    }
    @Transactional
    public void removeItem(PlayListId playListId, MediaId mediaId) {
        Author author = authorRepository.getAuthenticatedAuthor();
        Playlist playlist = playlistJpaRepository.findById(playListId.value()).orElseThrow(PlaylistNotFoundException::new);
        playlist.removeItem(mediaId, author.getId());
        playlistJpaRepository.save(playlist);
    }
    @Transactional
    public void remove(PlayListId playListId) {
        Author author = authorRepository.getAuthenticatedAuthor();
        Playlist playlist = playlistJpaRepository.findById(playListId.value()).orElseThrow(PlaylistNotFoundException::new);
        if(!playlist.getOwnerId().equals(author.getId())){
            throw new UnauthorizedException("Author is not the owner of the list");
        }
        playlistJpaRepository.delete(playlist);
    }

    public List<PlaylistDTO> getAllList(String slug) {
        Author author = slug == null ?
                authorRepository.getAuthenticatedAuthor() :
                authorRepository.findBySlug(slug).orElseThrow(AuthorNotFoundException::new);
        return playlistJpaRepository.findAllWithCount(author.getId());
    }

    public List<PlaylistItemDTO> getAllListItems(PlayListId playListId) throws JsonProcessingException {
        Playlist playlist = playlistJpaRepository.findById(playListId.value()).orElseThrow(PlaylistNotFoundException::new);
        List<PlaylistItemDTO> dtoList = new ArrayList<>();
        for (PlaylistItem playListItem : playlist.getItems()) {
            Media media = mediaItemJpaRepository.findDistinctById(playListItem.getMediaItemId());
            PlaylistItemDTO playlistItemDTO = new PlaylistItemDTO(media);
            dtoList.add(playlistItemDTO);
        }
        return dtoList;
    }

    public PlaylistDTO getById(PlayListId playlistId) {
        Playlist playlist = playlistJpaRepository.findById(playlistId.value()).orElseThrow(PlaylistNotFoundException::new);
        Author owner = authorRepository.findById(playlist.getOwnerId()).orElseThrow(AuthorNotFoundException::new);
        return new PlaylistDTO(playlist, owner.getSlug());
    }

    @Transactional
    public void createDefaultPlaylistsForAuthor(AuthorId authorId) {
        Author author = authorRepository.findById(authorId).orElse(null);
        if(author == null) return;

        PlayListPayload moviePayload = PlayListPayload.builder()
                .type(MediaType.movie)
                .name("İzlenecek Filmler")
                .build();
        PlayListPayload tvPayload = PlayListPayload.builder()
                .type(MediaType.tv)
                .name("İzlenecek Diziler")
                .build();
        PlayListPayload musicPayload = PlayListPayload.builder()
                .type(MediaType.music)
                .name("Dinlenecek Müzikler")
                .build();
        PlayListPayload booksPayload = PlayListPayload.builder()
                .type(MediaType.book)
                .name("Okunacak Kitaplar")
                .build();

        List<PlayListPayload> playListPayloadList = List.of(booksPayload, tvPayload, moviePayload, musicPayload);

        for (PlayListPayload payload : playListPayloadList) {
            create(payload, author);
        }
    }
}
