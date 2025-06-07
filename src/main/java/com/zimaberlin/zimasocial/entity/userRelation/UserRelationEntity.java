package com.zimaberlin.zimasocial.entity.userRelation;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "user_relation")
@SQLRestriction(value = "IS_DELETED IS FALSE")
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

    @Column(name = "IS_DELETED", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    public void markAsDeleted() {
        this.isDeleted = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRelationEntity that = (UserRelationEntity) o;
        return Objects.equals(actorId, that.actorId)
                && Objects.equals(receiverId, that.receiverId)
                && Objects.equals(relation, that.getRelation())
                && Objects.equals(getIsDeleted(), that.getIsDeleted());
    }

    @Override
    public int hashCode() {
        return Objects.hash(actorId, receiverId, relation, getIsDeleted());
    }
}
