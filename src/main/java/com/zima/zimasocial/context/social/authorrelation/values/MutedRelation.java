package com.zima.zimasocial.context.social.authorrelation.values;

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
