package com.zima.zimasocial.context.social.playlist.infastructure;

import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.playlist.api.dto.PlaylistDTO;
import com.zima.zimasocial.context.social.playlist.entity.Playlist;
import com.zima.zimasocial.context.social.playlist.repository.PlaylistRepository;
import com.zima.zimasocial.context.social.playlist.values.PlayListId;
import com.zima.zimasocial.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlaylistDBRepository implements PlaylistRepository {
    private final PlaylistJpaRepository playlistJpaRepository;
    @Override
    public List<Playlist> findByAuthorIdOrderByCreatedAtDesc(AuthorId authorId) {
        return playlistJpaRepository.findByUserIdOrderByCreatedAtDesc(authorId.getValue()).stream()
                .map(PlaylistJpaEntity::rehydrate).toList();
    }

    @Override
    public List<PlaylistDTO> findAllWithCount(AuthorId authorId) {
        return playlistJpaRepository.findAllWithCount(authorId.getValue());
    }

    @Override
    public long countByAuthorId(AuthorId authorId) {
        return playlistJpaRepository.countByUserId(authorId.getValue());
    }

    @Override
    public void save(Playlist playlist) {
        Optional<PlaylistJpaEntity> playlistJpaEntity = playlistJpaRepository.findById(playlist.getId().value());
        if(playlistJpaEntity.isEmpty()){
            playlistJpaRepository.save(new PlaylistJpaEntity(playlist));
        }else{
            PlaylistJpaEntity playlistEntity = playlistJpaRepository.findById(playlist.getId().value())
                    .orElseThrow(() -> new DataNotFoundException("playlist_not_found"));
            playlistEntity.merge(playlist);
            playlistJpaRepository.save(playlistEntity);
        }
    }

    @Override
    public Optional<Playlist> findById(PlayListId id) {
        return playlistJpaRepository.findById(id.value()).map(PlaylistJpaEntity::rehydrate);
    }

    @Override
    public void delete(Playlist playlist) {
        playlistJpaRepository.deleteById(playlist.getId().value());
    }
}
