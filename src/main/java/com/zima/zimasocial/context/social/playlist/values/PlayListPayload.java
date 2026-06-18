package com.zima.zimasocial.context.social.playlist.values;

import com.zima.zimasocial.context.social.media.value.MediaType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlayListPayload {
    @NotEmpty
    @Size(max = 24)
    private String name;
    @NotNull
    private MediaType type;
}
