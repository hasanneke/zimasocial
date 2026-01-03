package com.zimaberlin.zimasocial.context.communication.controller.views;

import com.zimaberlin.zimasocial.context.social.author.entity.Author;
import lombok.Getter;

@Getter
public class RecipientView {
    private final String slug;
    private final String fullName;
    private final String avatarUrl;

    public RecipientView(Author author) {
        this.slug = author.getSlug();
        this.fullName = author.getName();
        this.avatarUrl = author.getAvatarFileName();
    }
}
