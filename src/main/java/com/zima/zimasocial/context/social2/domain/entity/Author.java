package com.zima.zimasocial.context.social2.domain.entity;

import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;

@Table(name = "users")
@Entity
@Getter
@SQLRestriction(value = "IS_DELETED IS FALSE AND IS_DISABLED IS FALSE AND IS_BANNED IS FALSE")
public class Author {
    protected Author() {
        // JPA only
    }
    private Author(AuthorId id, String slug, String email, String name, String familyName) {
        if (id == null) throw new IllegalArgumentException("Author id cannot be null");
        if (slug == null || slug.isBlank()) throw new IllegalArgumentException("Slug cannot be blank");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email cannot be blank");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be blank");

        this.id = id;
        this.slug = slug;
        this.email = email;
        this.name = name;
        this.familyName = familyName;
        this.isPrivate = false;
        this.followerCount = 0;
        this.followingCount = 0;
        this.isDisabled = false;
        this.isBanned = false;
    }

    public static Author create(
            AuthorId id,
            String slug,
            String email,
            String name,
            String familyName
    ) {
        return new Author(id, slug, email, name, familyName);
    }

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

    @Column(name = "is_disabled")
    private Boolean isDisabled = false;

    @Column(name = "is_banned")
    private Boolean isBanned = false;

    public void makePrivate()  {
        this.isPrivate = true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
