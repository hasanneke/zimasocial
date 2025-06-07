package com.zimaberlin.zimasocial.context.social.userRelation;
import lombok.Getter;


@Getter
public final class BlockRelation extends AuthorRelation {
    private final Long blockerId;
    private final Long blockedId;

    public BlockRelation(Long blockerId, Long blockedId) {
        this.blockerId = blockerId;
        this.blockedId = blockedId;
    }
}
