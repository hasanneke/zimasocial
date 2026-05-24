package com.zima.zimasocial.context.communication.controller.views;

import com.zima.zimasocial.context.social.author.entity.AuthorDomain;
import lombok.Getter;

@Getter
public class RecipientView {
    private final String slug;
    private final String fullName;
    private final String avatarUrl;

    public RecipientView(AuthorDomain author) {
        this.slug = author.getSlug();
        this.fullName = author.getName();
        this.avatarUrl = author.getAvatarFileName();
    }
}
