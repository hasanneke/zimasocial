package com.zima.zimasocial.context.social.author.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Slug {
    @Column(name = "slug")
    private String name;

    public String name() {
        return name;
    }
}
