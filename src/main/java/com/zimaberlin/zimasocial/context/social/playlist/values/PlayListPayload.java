package com.zimaberlin.zimasocial.context.social.playlist.values;

import com.zimaberlin.zimasocial.entity.MediaType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PlayListPayload {
    @NotEmpty
    @Size(max = 24)
    private String name;
    @NotNull
    private MediaType type;
}
