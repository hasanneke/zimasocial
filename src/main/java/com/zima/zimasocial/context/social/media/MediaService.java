package com.zima.zimasocial.context.social.media;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zima.zimasocial.context.social.api.dto.MediaDTO;
import com.zima.zimasocial.context.social.infastructure.repository.MediaItemJpaRepository;
import com.zima.zimasocial.context.social.media.infastructure.MediaItem;
import com.zima.zimasocial.context.social.post.value.MediaId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final MediaManager mediaManager;
    private final MediaItemJpaRepository mediaItemJpaRepository;
    private final ObjectMapper objectMapper;

    public UUID getId(String resourceId, String provider) throws JsonProcessingException {
        Optional<UUID> mediaId = mediaManager.getMedia(resourceId, provider);
        if(mediaId.isPresent()){
            return mediaId.get();
        }else{
            MediaItem mediaItem = mediaManager.searcher(provider).get(resourceId);
            mediaItemJpaRepository.save(mediaItem);
            return mediaItem.getId();
        }
    }

    public JsonNode getMedia(MediaId mediaId) throws JsonProcessingException {
        MediaItem mediaItem = mediaItemJpaRepository.findDistinctById(mediaId.value());
        return objectMapper.readTree(mediaItem.getContent());
    }

    public MediaDTO getMediaV2(UUID mediaId) throws JsonProcessingException {
        MediaItem mediaItem = mediaItemJpaRepository.findDistinctById(mediaId);
        MediaDTO mediaDTO = new MediaDTO(mediaItemJpaRepository.findDistinctById(mediaId));
        mediaDTO.setContent(objectMapper.readTree(mediaItem.getContent()));
        return mediaDTO;
    }
}
