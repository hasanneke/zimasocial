package com.zima.zimasocial.context.social.playlist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zima.zimasocial.context.social.playlist.api.dto.PlaylistDTO;
import com.zima.zimasocial.context.social.playlist.api.dto.PlaylistItemDTO;
import com.zima.zimasocial.context.social.playlist.application.PlaylistApplicationService;
import com.zima.zimasocial.context.social.playlist.values.PlayListId;
import com.zima.zimasocial.context.social.playlist.values.PlayListItemPayload;
import com.zima.zimasocial.context.social.playlist.values.PlayListPayload;
import com.zima.zimasocial.context.social.playlist.values.PlayListUpdatePayload;
import com.zima.zimasocial.context.social.media.value.MediaId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistApplicationService playListService;

    @GetMapping
    public ResponseEntity<List<PlaylistDTO>> getAllList(@RequestParam(name = "slug", required = false) String slug) {
        return ResponseEntity.ok(playListService.getAllList(slug));
    }

    @GetMapping(path = "/{playlistId}")
    public ResponseEntity<PlaylistDTO> getPlaylist(@PathVariable(name = "playlistId") UUID playlistId) {
        return ResponseEntity.ok(playListService.getById(new PlayListId(playlistId)));
    }

    @PostMapping
    public ResponseEntity<PlaylistDTO> createPlaylist(@Validated @RequestBody PlayListPayload payload) {
        playListService.create(payload);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{playlistId}")
    public ResponseEntity<PlaylistDTO> updatePlaylist(@PathVariable UUID playlistId,
                                                      @Validated @RequestBody PlayListUpdatePayload payload) {
        playListService.update(new PlayListId(playlistId), payload);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deleteList(@PathVariable UUID playlistId) {
        playListService
                .remove(new PlayListId(playlistId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{playlistId}/items")
    public ResponseEntity<List<PlaylistItemDTO>> getAllItems(@PathVariable UUID playlistId) throws JsonProcessingException {
        return ResponseEntity.ok(playListService
                .getAllListItems(new PlayListId(playlistId)));
    }

    @DeleteMapping("/{playlistId}/items/{mediaId}")
    public ResponseEntity<Void> deleteItem(@PathVariable UUID playlistId, @PathVariable UUID mediaId) {
        playListService
                .removeItem(new PlayListId(playlistId), new MediaId(mediaId));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{playlistId}/items")
    public ResponseEntity<Void> addItem(@PathVariable UUID playlistId, @Validated @RequestBody PlayListItemPayload playListItemPayload) throws JsonProcessingException {
        playListService.addItem(new PlayListId(playlistId), playListItemPayload);
        return ResponseEntity.noContent().build();
    }
}
