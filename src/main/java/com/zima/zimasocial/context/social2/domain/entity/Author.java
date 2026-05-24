package com.zima.zimasocial.context.social2.domain.entity;

import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Table(name = "users")
@Entity
@Getter
@SQLRestriction(value = "IS_DELETED IS FALSE AND IS_DISABLED IS FALSE AND IS_BANNED IS FALSE")
public class Author {
    @EmbeddedId
    @AttributeOverride(
            name = "value",
            column = @Column(name = "id", updatable = false)
    )
    private AuthorId id;

    @Column(name = "slug", unique = true, length = 65)
    private String slug;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "family_name")
    private String familyName;

    @Column(name = "is_private")
    private boolean isPrivate = false;

    @Column(name = "bio")
    private String bio;

    @Column(name = "follower_count")
    private int followerCount = 0;

    @Column(name = "following_count")
    private int followingCount = 0;

    @Column(name = "avatar_file_name")
    private String avatarFileName;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
