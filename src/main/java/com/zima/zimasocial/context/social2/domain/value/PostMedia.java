package com.zima.zimasocial.context.social2.domain.value;

import com.zima.zimasocial.entity.MediaType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostMedia {
    @Embedded
    private MediaId mediaId;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MediaType type;
}
