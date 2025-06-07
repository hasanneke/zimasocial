package com.zimaberlin.zimasocial.context.social.userRelation;

import com.zimaberlin.zimasocial.context.social.author.Author;
import lombok.Getter;

@Getter
public final class MutedRelation extends AuthorRelation {
    private final Long muterId;
    private final Long mutedId;

    public MutedRelation(Long muterId, Long mutedId) {
        this.muterId = muterId;
        this.mutedId = mutedId;
    }
}
