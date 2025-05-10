package com.zimaberlin.zimasocial.entity.userRelation;

import com.zimaberlin.zimasocial.entity.BaseEntity;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_relation")
@SQLRestriction(value = "IS_DELETED IS FALSE")
public class UserRelationEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "initiated_id")
    private UserEntity initiatedUser;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private UserEntity receiverUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation")
    private Relation relation;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
