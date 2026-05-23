package com.zima.zimasocial.context.social2.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PostContent {
    @Column(name = "content")
    private String text;
}
