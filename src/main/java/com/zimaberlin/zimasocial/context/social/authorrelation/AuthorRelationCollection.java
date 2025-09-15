package com.zimaberlin.zimasocial.context.social.authorrelation;

import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface AuthorRelationCollection {
    Optional<FollowRelation> findFollowRelationBetween(AuthorId followerId, AuthorId followedId);
    Optional<BlockRelation> findBlockRelationBetween(AuthorId blockerId, AuthorId followedId);
    Optional<MutedRelation> findMutedRelationBetween(Long muterId, Long mutedId);
    Page<Author> findFollowers(String slug, int page, int size);
    Page<Author> findFollowings(String slug,int page, int size);
    Page<Author> findBlocks(int page, int size);
    void save(AuthorRelation relation);
    void delete(AuthorRelation relation);
}
