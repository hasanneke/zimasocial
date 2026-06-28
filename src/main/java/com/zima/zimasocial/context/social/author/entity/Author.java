package com.zima.zimasocial.context.social.author.entity;

import com.github.f4b6a3.uuid.UuidCreator;
import com.zima.zimasocial.context.account.exception.*;
import com.zima.zimasocial.context.social.author.event.AuthorFollowRequestSentEvent;
import com.zima.zimasocial.context.social.author.exception.SlugCannotBeChangedException;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.author.value.Relation;
import com.zima.zimasocial.shared.exception.ConflictException;
import com.zima.zimasocial.shared.StaticEventPublisher;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Table(name = "users")
@Entity
@Getter
@SQLRestriction(value = "IS_DELETED IS FALSE AND IS_DISABLED IS FALSE AND IS_BANNED IS FALSE")
public class Author {
    protected Author() {
        // JPA only
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
    private Boolean isPrivate = false;

    @Column(name = "bio")
    private String bio;

    @Column(name = "avatar_file_name")
    private String avatarFileName;

    @Column(name = "follower_count")
    private int followerCount = 0;

    @Column(name = "following_count")
    private int followingCount = 0;

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

    @Column(name = "last_slug_changed_at")
    private LocalDate lastSlugChangedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    public FollowRequest requestToFollow(AuthorId followerAuthorId) {
        if(followerAuthorId.equals(this.id)){
            throw new CircularFollowException();
        }
        StaticEventPublisher.publishEvent(new AuthorFollowRequestSentEvent(followerAuthorId, this.getId()));
        return new FollowRequest(UuidCreator.getTimeOrdered(), followerAuthorId, id);
    }

    public AuthorRelation follow(AuthorId followerId) {
        if(this.id.equals(followerId)){
            throw new CircularFollowException();
        }
        incrementFollowerCount();
        return new AuthorRelation(followerId, id, Relation.followed);
    }

    public void unfollow(AuthorId unfollowerId){
        if(this.id.equals(unfollowerId)){
            throw new CircularUnfollowException();
        }
        decrementFollowingCount();
    }

    public AuthorRelation block(AuthorId blocker) {
        if(this.id.equals(blocker)){
            throw new ConflictException();
        }
        return new AuthorRelation(blocker, id, Relation.blocked);
    }

    public void updateBio(String bio){
        if(bio.length() > 128) {
            throw new CharachterLengthExceededException(128);
        }
        this.bio = bio;
    }
    public void updateName(String name) {
        if(name.length() > 32){
            throw new NameLengthExceededException();
        }
        this.name = name;
    }

    public void updateSlug(String slug){
        if(lastSlugChangedAt != null && ChronoUnit.DAYS.between(lastSlugChangedAt, LocalDateTime.now()) < 7){
            throw new SlugCannotBeChangedException("Last slug change hasn't passed 7 days");
        }
        if(name.length() > 16){
            throw new SlugLengthExceededException();
        }
        this.lastSlugChangedAt = LocalDate.now();
        this.slug = slug;
    }
    public void attachFileName(String fileName) {
        this.avatarFileName = fileName;
    }

    public void removeProfilePhoto() {
        avatarFileName = null;
    }

    public void incrementFollowingCount(){
        followingCount = getFollowingCount() + 1;
    }
    public void decrementFollowingCount(){
        followingCount = getFollowingCount() - 1;
    }
    public void incrementFollowerCount(){
        followerCount = getFollowerCount() + 1;
    }
    public void decrementFollowerCount(){
        followerCount = getFollowerCount() - 1;
    }
    public String getFullName(){
        return name + " " + familyName;
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
