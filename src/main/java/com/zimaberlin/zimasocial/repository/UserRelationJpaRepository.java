package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.entity.userRelation.Relation;
import com.zimaberlin.zimasocial.entity.userRelation.UserRelationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRelationJpaRepository extends JpaRepository<UserRelationEntity, Long> {
    Optional<UserRelationEntity> findByActorIdAndReceiverIdAndRelation(Long actorId, Long receiverId, Relation relation);
    void deleteByActorIdAndReceiverIdAndRelation(Long actorId, Long receiverId, Relation relation);
    Page<UserRelationEntity> findByReceiverIdAndRelation(Long receiverId, Relation relation, Pageable pageable);
    Page<UserRelationEntity> findByActorIdAndRelation(Long actorId, Relation relation, Pageable pageable);
}
