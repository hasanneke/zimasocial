package com.zima.zimasocial.context.social.author.entity;

import com.zima.zimasocial.context.account.exception.*;
import com.zima.zimasocial.context.social.author.value.AuthorFollowRequestSentEvent;
import com.zima.zimasocial.context.social.author.value.AuthorFollowedEvent;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.author.exception.SlugCannotBeChangedException;
import com.zima.zimasocial.context.social.authorrelation.entity.FollowRequest;
import com.zima.zimasocial.shared.StaticEventPublisher;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Author {
    private final AuthorId id;
    private String slug;
    private String name;
    private String familyName;
    private String bio;
    private String avatarFileName;
    private String email;
    private Boolean isPrivate;
    private int followersCount;
    private int followingCount;
    private LocalDate lastSlugChangedAt;
    private final LocalDateTime createdAt;

    public Author(AuthorId id, String slug, String name, String bio, String familyName, String avatarFileName,Boolean isPrivate, String email, int followersCount, int followingCount, LocalDateTime createdAt, LocalDate lastSlugChangedAt) {
        Assert.notNull(id, "Author Id cannot be null");
        Assert.notNull(slug, "Slug cannot be null");
        Assert.notNull(name, "Name cannot be null");
        Assert.notNull(createdAt, "Created At cannot be null");
        this.id = id;
        this.slug = slug;
        this.name = name;
        this.familyName = familyName;
        this.bio = bio;
        this.avatarFileName = avatarFileName;
        this.email = email;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.createdAt = createdAt;
        this.isPrivate = isPrivate;
        this.lastSlugChangedAt = lastSlugChangedAt;
    }

    public Author(AuthorId authorId, String slug, String name, LocalDateTime createdAt){
        Assert.notNull(authorId, "Author Id cannot be null");
        Assert.notNull(slug, "Slug cannot be null");
        Assert.notNull(name, "Name cannot be null");
        Assert.notNull(createdAt, "Created At cannot be null");
        this.id = authorId;
        this.slug = slug;
        this.name = name;
        this.createdAt = createdAt;
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

    public void removeProfilePhoto() {
        avatarFileName = null;
    }

    public void follow(Author follower){
        if(this.equals(follower)){
            throw new CircularFollowException();
        }
        incrementFollowerCount();
        follower.incrementFollowingCount();
    }

    public void unfollow(Author follower){
        if(this.equals(follower)){
            throw new CircularUnfollowException();
        }
        decrementFollowerCount();
        follower.decrementFollowingCount();
        StaticEventPublisher.publishEvent(new AuthorFollowedEvent(this, follower));
    }

    public void attachFileName(String fileName) {
        this.avatarFileName = fileName;
    }
    private void incrementFollowingCount(){
        followingCount = getFollowingCount() + 1;
    }
    private void decrementFollowingCount(){
        followingCount = getFollowingCount() - 1;
    }
    private void incrementFollowerCount(){
        followersCount = getFollowersCount() + 1;
    }
    private void decrementFollowerCount(){
        followersCount = getFollowersCount() - 1;
    }

    public FollowRequest requestToFollow(AuthorId followerAuthorId, UUID id) {
        if(followerAuthorId.equals(this.id)){
            throw new CircularFollowException();
        }
        StaticEventPublisher.publishEvent(new AuthorFollowRequestSentEvent(followerAuthorId, this.getId()));
        return new FollowRequest(id, followerAuthorId, this.id, false, LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
