package com.zimaberlin.zimasocial.context.social.authorrelation;

import com.zimaberlin.zimasocial.context.social.author.Author;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface AuthorRelationRepository {
    Optional<FollowRelation> findFollowRelationBetween(Long followerId, Long followedId);
    Optional<BlockRelation> findBlockRelationBetween(Long blockerId, Long followedId);
    Optional<MutedRelation> findMutedRelationBetween(Long muterId, Long mutedId);
    Page<Author> findFollowers(String slug, int page, int size);
    Page<Author> findFollowings(String slug,  int page, int size);
    Page<Author> findBlocks(String slug, int page, int size);
    void save(AuthorRelation relation);
    void delete(AuthorRelation relation);
}
