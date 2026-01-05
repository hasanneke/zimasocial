package com.zima.zimasocial.context.social.authorrelation.values;
import com.zima.zimasocial.context.social.author.value.AuthorId;
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
