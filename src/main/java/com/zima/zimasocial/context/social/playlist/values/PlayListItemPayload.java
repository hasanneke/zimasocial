package com.zima.zimasocial.context.social.playlist.values;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PlayListItemPayload {
    @NotNull
    private UUID mediaId;
    private Long postIdReferencedFrom;
}
