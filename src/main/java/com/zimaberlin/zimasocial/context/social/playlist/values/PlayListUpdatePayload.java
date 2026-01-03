package com.zimaberlin.zimasocial.context.social.playlist.values;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PlayListUpdatePayload {
    @NotEmpty
    @Size(max = 24)
    private String name;
}
