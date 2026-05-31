package com.zima.zimasocial.context.social.media.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MediaId {
    @Column(name = "media_id")
    private UUID value;
}
