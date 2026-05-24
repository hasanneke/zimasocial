package com.zima.zimasocial.context.social.authorrelation.values;
import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import lombok.Getter;


@Getter
public final class BlockRelation extends AuthorRelation {
    private final AuthorDomainId blockerId;
    private final AuthorDomainId blockedId;

    public BlockRelation(AuthorDomainId blockerId, AuthorDomainId blockedId) {
        this.blockerId = blockerId;
        this.blockedId = blockedId;
    }
}
