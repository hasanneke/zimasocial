package com.zima.zimasocial.context.social2.domain.entity;

import com.zima.zimasocial.entity.MediaType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.UUID;

@Embeddable
public class PostMedia {
    @Column(name = "media_id")
    private UUID mediaId;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MediaType type;
}
