package com.zimaberlin.zimasocial.entity.userRelation;

import com.zimaberlin.zimasocial.entity.user.UserEntity;
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

    @ManyToOne
    @JoinColumn(name = "initiated_id")
    private UserEntity actor;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private UserEntity receiver;

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
        return Objects.equals(actor, that.getActor())
                && Objects.equals(receiver, that.getReceiver())
                && Objects.equals(relation, that.getRelation())
                && Objects.equals(getIsDeleted(), that.getIsDeleted());
    }

    @Override
    public int hashCode() {
        return Objects.hash(actor, receiver, relation, getIsDeleted());
    }
}
