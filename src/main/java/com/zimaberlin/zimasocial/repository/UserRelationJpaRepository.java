package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.userRelation.Relation;
import com.zimaberlin.zimasocial.entity.userRelation.UserRelationEntity;
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
    Page<UserRelationEntity> findByActorIdAndRelation(Long actorId, Relation relation, Pageable pageable);
    List<UserRelationEntity> findByActorIdAndRelation(Long actorId, Relation relation);
}
