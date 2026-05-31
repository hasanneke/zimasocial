package com.zima.zimasocial.context.social.media.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zima.zimasocial.context.social.media.api.dto.MediaDTO;
import com.zima.zimasocial.context.social.media.repository.MediaItemJpaRepository;
import com.zima.zimasocial.context.social.media.entity.Media;
import com.zima.zimasocial.context.social.media.value.MediaId;
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
            Media media = mediaManager.searcher(provider).get(resourceId);
            mediaItemJpaRepository.save(media);
            return media.getId().getValue();
        }
    }

    public JsonNode getMedia(MediaId mediaId) throws JsonProcessingException {
        Media media = mediaItemJpaRepository.findDistinctById(mediaId);
        return objectMapper.readTree(media.getContent());
    }

    public MediaDTO getMediaV2(UUID mediaId) throws JsonProcessingException {
        Media media = mediaItemJpaRepository.findDistinctById(new MediaId(mediaId));
        MediaDTO mediaDTO = new MediaDTO(mediaItemJpaRepository.findDistinctById(new MediaId(mediaId)));
        mediaDTO.setContent(objectMapper.readTree(media.getContent()));
        return mediaDTO;
    }
}
