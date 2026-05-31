package com.zima.zimasocial.repository;

import com.zima.zimasocial.entity.NotificationEntity;
import com.zima.zimasocial.entity.NotificationType;
import com.zima.zimasocial.entity.TargetCollection;
import com.zima.zimasocial.entity.user.UserEntity;
import com.zima.zimasocial.views.notification.NotificationView2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {
    @Query(
            """
                   SELECT new com.zima.zimasocial.views.notification.
                              NotificationView2(
                                         notification.id,
                                         notification.content,
                                         notification.url,
                                         notification.type,
                                         notification.createdAt,
                                         notification.targetCollection,
                                         notification.targetId,
                                         notification.postId,
                                         user.slug,
                                         user.name,
                                         user.familyName,
                                         user.avatarFileName avatarUrl,
                                         CASE WHEN EXISTS (
                                                     SELECT 1 FROM UserRelationEntity relation
                                                     WHERE relation.relation = com.zima.zimasocial.entity.userRelation.Relation.followed
                                                     AND relation.actorId = :receiverId AND relation.receiverId = user.id.value
                                         ) THEN true ELSE false END,
                                        CASE WHEN EXISTS (
                                                     SELECT 1 FROM UserRelationEntity relation
                                                     WHERE relation.relation = com.zima.zimasocial.entity.userRelation.Relation.followed
                                                     AND relation.receiverId = :receiverId AND relation.actorId = user.id.value
                                         ) THEN true ELSE false END,
                                         CASE WHEN EXISTS (
                                                     SELECT 1 FROM FollowRequest followRequest
                                                     WHERE followRequest.followerId = :receiverId
                                                     AND followRequest.followedId = user.id
                                         ) THEN true ELSE false END,
                                         CASE WHEN EXISTS (
                                                     SELECT 1 FROM FollowRequest followRequest
                                                     WHERE followRequest.followerId = user.id
                                                     AND followRequest.followedId = :receiverId
                                         ) THEN true ELSE false END)
                              FROM NotificationEntity notification
                   JOIN Author user ON notification.actorId = user.id.value
                   WHERE notification.receiverUserId = :receiverId
                   AND notification.type NOT IN :notificationTypes
                   AND user.isDisabled = false AND user.isBanned = false AND user.isDeleted = false
                   ORDER BY notification.createdAt DESC
           """
    )
    Page<NotificationView2> findAllNotifications(Long receiverId, Set<NotificationType> notificationTypes, Pageable page);
    Page<NotificationEntity> findByReceiverUserIdAndTypeNotInOrderByCreatedAtDesc(Long receiverId, Set<NotificationType> notificationType, Pageable page);
    Optional<NotificationEntity> findByReceiverUserAndActorAndPostIdAndTypeAndTargetCollection(UserEntity receiver, UserEntity sender, Long targetId, NotificationType type, TargetCollection collection);
    Optional<NotificationEntity> findByReceiverUserAndActorAndTargetIdAndTypeAndTargetCollection(UserEntity receiver, UserEntity sender, Long targetId, NotificationType type, TargetCollection collection);

    Optional<NotificationEntity> findByActorIdAndTargetIdAndTypeAndTargetCollection(Long actorId, Long targetId, NotificationType type, TargetCollection collection);
    Optional<NotificationEntity> findFirstByActorIdAndReceiverUserIdAndTypeOrderByCreatedAtDesc(Long actorId, Long receiverId, NotificationType type);
    Optional<NotificationEntity> findFirstByActorIdAndReceiverUserIdAndTypeAndTargetIdOrderByCreatedAtDesc(Long actorId, Long receiverId, NotificationType type, Long targetId);
    Optional<NotificationEntity> findFirstByActorIdAndTypeOrderByCreatedAtDesc(Long actorId, NotificationType notificationType);
    List<NotificationEntity> findAllByIsPushedFalse();
}
