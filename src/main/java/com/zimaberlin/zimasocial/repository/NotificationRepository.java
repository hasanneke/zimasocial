package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.NotificationEntity;
import com.zimaberlin.zimasocial.entity.NotificationType;
import com.zimaberlin.zimasocial.entity.TargetCollection;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    Page<NotificationEntity> findByReceiverUserIdOrderByCreatedAtDesc(Long receiverId, Pageable page);
    Optional<NotificationEntity> findByReceiverUserAndActorAndPostIdAndTypeAndTargetCollection(UserEntity receiver, UserEntity sender, Long targetId, NotificationType type, TargetCollection collection);
    Optional<NotificationEntity> findByReceiverUserAndActorAndTargetIdAndTypeAndTargetCollection(UserEntity receiver, UserEntity sender, Long targetId, NotificationType type, TargetCollection collection);
}
