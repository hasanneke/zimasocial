package com.zima.zimasocial.entity.userRelation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "user_relation")
public class UserRelationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "initiated_id")
    private Long actorId;

    @Column(name = "receiver_id")
    private Long receiverId;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation")
    private Relation relation;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
