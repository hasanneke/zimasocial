package com.zima.zimasocial.repository;

import com.zima.zimasocial.entity.userRelation.Relation;
import com.zima.zimasocial.entity.userRelation.UserRelationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRelationJpaRepository extends JpaRepository<UserRelationEntity, Long> {
    Optional<UserRelationEntity> findByActorIdAndReceiverIdAndRelation(Long actorId, Long receiverId, Relation relation);
    @Query("""
                SELECT userRelation FROM UserRelationEntity userRelation
                WHERE userRelation.relation = Relation.blocked AND (userRelation.actorId = :userId OR userRelation.receiverId = :userId)
            """)
    List<UserRelationEntity> findAllBlockRelations(Long userId);
    void deleteByActorIdAndReceiverIdAndRelation(Long actorId, Long receiverId, Relation relation);
    Page<UserRelationEntity> findByReceiverIdAndRelation(Long receiverId, Relation relation, Pageable pageable);
    @Query("""
            SELECT userRelation FROM UserRelationEntity userRelation
            JOIN FETCH userRelation.actor WHERE userRelation.receiverId = :receiverId AND userRelation.relation = :relation
    """)
    Page<UserRelationEntity> findUserIdsByReceiverIdAndRelation(Long receiverId, Relation relation, Pageable pageable);
    List<UserRelationEntity> findAllByReceiverIdAndRelation(Long receiverId, Relation relation);
    Page<UserRelationEntity> findByActorIdAndRelation(Long actorId, Relation relation, Pageable pageable);
    @Query("""
            SELECT userRelation FROM UserRelationEntity userRelation
            JOIN FETCH userRelation.receiver WHERE userRelation.actorId = :actorId AND userRelation.relation = :relation
    """)
    Page<UserRelationEntity> findAllByActorIdAndRelation(Long actorId, Relation relation, Pageable pageable);
    List<UserRelationEntity> findByActorIdAndRelation(Long actorId, Relation relation);
}
