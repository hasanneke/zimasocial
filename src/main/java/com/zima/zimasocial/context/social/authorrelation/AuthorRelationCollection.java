package com.zima.zimasocial.context.social.authorrelation;
import com.zima.zimasocial.context.social.author.entity.AuthorDomain;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.authorrelation.values.AuthorRelation;
import com.zima.zimasocial.context.social.authorrelation.values.BlockRelation;
import com.zima.zimasocial.context.social.authorrelation.values.FollowRelation;
import com.zima.zimasocial.context.social.authorrelation.values.MutedRelation;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface AuthorRelationCollection {
    Optional<FollowRelation> findFollowRelationBetween(AuthorId followerId, AuthorId followedId);
    Optional<BlockRelation> findBlockRelationBetween(AuthorId blockerId, AuthorId blockedId);
    Optional<MutedRelation> findMutedRelationBetween(Long muterId, Long mutedId);
    Page<AuthorDomain> findFollowers(String slug, int page, int size);
    Page<AuthorDomain> findFollowings(String slug, int page, int size);
    Page<AuthorDomain> findBlocks(int page, int size);
    void save(AuthorRelation relation);
    void delete(AuthorRelation relation);
    boolean hasBlockRelationBetween(Long author1, Long author2);
}
