package com.zima.zimasocial.context.social.api.media;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.zima.zimasocial.context.social.api.dto.MediaDTO;
import com.zima.zimasocial.context.social.media.MediaService;
import com.zima.zimasocial.context.social2.domain.value.MediaId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping(path = "/api/v1/media")
@RestController
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;

    @GetMapping("/{mediaId}")
    public ResponseEntity<JsonNode> getMedia(@PathVariable UUID mediaId) throws JsonProcessingException {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(mediaService.getMedia(new MediaId(mediaId)));
    }

    @GetMapping("/v2/{mediaId}")
    public ResponseEntity<MediaDTO> getMediaV2(@PathVariable UUID mediaId) throws JsonProcessingException {
        return ResponseEntity.ok(mediaService.getMediaV2(mediaId));
    }

    @GetMapping("/external-media/{externalMediaId}/{provider}")
    public ResponseEntity<UUID> getMediaFromExternal(@PathVariable String externalMediaId,
                                                     @PathVariable String provider) throws JsonProcessingException {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(mediaService.getId(externalMediaId, provider));
    }
}
