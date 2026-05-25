package com.zima.zimasocial.context.social2.domain.entity;

import com.zima.zimasocial.entity.MediaType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostContent {
    @Column(name = "content")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MediaType type;
}
