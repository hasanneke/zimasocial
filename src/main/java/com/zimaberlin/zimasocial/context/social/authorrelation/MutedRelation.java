package com.zimaberlin.zimasocial.context.social.authorrelation;

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
