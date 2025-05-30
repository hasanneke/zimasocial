package com.zimaberlin.zimasocial.repository;

import com.zimaberlin.zimasocial.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    Page<NotificationEntity> findByReceiverUserIdOrderByCreatedAtDesc(Long receiverId, Pageable page);
}
