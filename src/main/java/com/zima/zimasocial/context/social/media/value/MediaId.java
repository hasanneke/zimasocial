package com.zima.zimasocial.context.social.media.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MediaId {
    @Column(name = "media_id")
    private UUID value;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MediaId mediaId = (MediaId) o;
        return Objects.equals(value, mediaId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
