package com.zima.zimasocial.context.social.author.repository;

import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.entity.AuthorRelation;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.author.value.Relation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRelationRepository extends JpaRepository<AuthorRelation, Long> {
    Optional<AuthorRelation> findByActorAndReceiverAndRelation(Author actor, Author receiver, Relation relation);
    Optional<AuthorRelation> findFirstByActorIdAndReceiverIdAndRelation(AuthorId actorId, AuthorId receiverId, Relation relation);
    @Query("""
            SELECT authorRelation FROM AuthorRelation authorRelation
            JOIN FETCH authorRelation.actor WHERE authorRelation.receiverId = :receiverId AND authorRelation.relation = :relation
    """)
    Page<AuthorRelation> findAllByReceiverIdAndRelation(AuthorId receiverId, Relation relation, Pageable pageable);
    @Query("""
            SELECT authorRelation FROM AuthorRelation authorRelation
            JOIN FETCH authorRelation.actor WHERE authorRelation.receiverId = :receiverId AND authorRelation.relation = :relation
    """)
    List<AuthorRelation> findAllByReceiverIdAndRelation(AuthorId receiverId, Relation relation);

    @Query("""
            SELECT authorRelation FROM AuthorRelation authorRelation
            JOIN FETCH authorRelation.receiver WHERE authorRelation.actorId = :actorId AND authorRelation.relation = :relation
    """)
    Page<AuthorRelation> findAllByActorIdAndRelation(AuthorId actorId, Relation relation, Pageable pageable);

    @Query("""
        SELECT authorRelation
        FROM AuthorRelation authorRelation
        WHERE authorRelation.relation = Relation.blocked
        AND authorRelation.actorId IN (:author1, :author2)
        AND authorRelation.receiverId IN (:author1, :author2)""")
    List<AuthorRelation> hasAnyBlockRelationBetween(AuthorId author1, AuthorId author2);
}
