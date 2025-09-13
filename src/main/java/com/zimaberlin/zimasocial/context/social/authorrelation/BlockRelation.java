package com.zimaberlin.zimasocial.context.social.authorrelation;
import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import lombok.Getter;


@Getter
public final class BlockRelation extends AuthorRelation {
    private final AuthorId blockerId;
    private final AuthorId blockedId;

    public BlockRelation(AuthorId blockerId, AuthorId blockedId) {
        this.blockerId = blockerId;
        this.blockedId = blockedId;
    }
}
