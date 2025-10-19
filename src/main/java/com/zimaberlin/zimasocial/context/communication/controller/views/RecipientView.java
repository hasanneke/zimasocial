package com.zimaberlin.zimasocial.context.communication.controller.views;

import com.zimaberlin.zimasocial.context.communication.domain.Recipient;
import lombok.Getter;

@Getter
public class RecipientView {
    private final String slug;
    private final String fullName;
    private final String avatarUrl;

    public RecipientView(Recipient recipient) {
        this.slug = recipient.getSlug();
        this.fullName = recipient.getFullName();
        this.avatarUrl = recipient.getAvatarUrl();
    }
}
