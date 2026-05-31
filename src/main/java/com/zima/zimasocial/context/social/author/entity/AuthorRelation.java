package com.zima.zimasocial.context.social.author.entity;

import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.entity.userRelation.Relation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@Table(name = "user_relation")
public class AuthorRelation {

    protected AuthorRelation() {}

    public AuthorRelation(AuthorId actorId, AuthorId receiverId, Relation relation) {
        this.actorId = actorId;
        this.receiverId = receiverId;
        this.relation = relation;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "initiated_id", updatable = false)
    )
    private AuthorId actorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiated_id", insertable = false, updatable = false)
    private Author actor;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "receiver_id", updatable = false)
    )
    private AuthorId receiverId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id",insertable = false, updatable = false)
    private Author receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation")
    private Relation relation;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
