package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.entity.userRelation.Relation;
import com.zimaberlin.zimasocial.entity.userRelation.UserRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRelationRepository extends JpaRepository<UserRelationEntity, Long> {
    Optional<UserRelationEntity> findByInitiatedUserAndReceiverUserAndRelation(UserEntity initiated, UserEntity receiver, Relation relation);
}
