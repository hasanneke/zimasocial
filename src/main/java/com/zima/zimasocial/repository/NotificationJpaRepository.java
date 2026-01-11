package com.zima.zimasocial.repository;

import com.zima.zimasocial.entity.NotificationEntity;
import com.zima.zimasocial.entity.NotificationType;
import com.zima.zimasocial.entity.TargetCollection;
import com.zima.zimasocial.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {
    Page<NotificationEntity> findByReceiverUserIdAndTypeNotInOrderByCreatedAtDesc(Long receiverId, Set<NotificationType> notificationType, Pageable page);
    Optional<NotificationEntity> findByReceiverUserAndActorAndPostIdAndTypeAndTargetCollection(UserEntity receiver, UserEntity sender, Long targetId, NotificationType type, TargetCollection collection);
    Optional<NotificationEntity> findByReceiverUserAndActorAndTargetIdAndTypeAndTargetCollection(UserEntity receiver, UserEntity sender, Long targetId, NotificationType type, TargetCollection collection);

    Optional<NotificationEntity> findByActorIdAndTargetIdAndTypeAndTargetCollection(Long actorId, Long targetId, NotificationType type, TargetCollection collection);
    Optional<NotificationEntity> findFirstByActorIdAndReceiverUserIdAndTypeOrderByCreatedAtDesc(Long actorId, Long receiverId, NotificationType type);
    Optional<NotificationEntity> findFirstByActorIdAndReceiverUserIdAndTypeAndTargetIdOrderByCreatedAtDesc(Long actorId, Long receiverId, NotificationType type, Long targetId);
    List<NotificationEntity> findAllByIsPushedFalse();
}
