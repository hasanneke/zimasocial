package com.zimaberlin.zimasocial.context.social.author;

import com.zimaberlin.zimasocial.context.account.exception.BioLengthExceededException;
import com.zimaberlin.zimasocial.context.account.exception.CircularFollowException;
import com.zimaberlin.zimasocial.context.account.exception.CircularUnfollowException;
import com.zimaberlin.zimasocial.context.account.exception.NameLengthExceededException;
import com.zimaberlin.zimasocial.shared.StaticEventPublisher;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Author {
    private final Long authorId;
    private String slug;
    private String name;
    private String familyName;
    private String bio;
    private String avatarFileName;
    private String email;
    private Boolean isPrivate;
    private int followersCount;
    private int followingCount;
    private final LocalDateTime createdAt;

    public Author(Long authorId, String slug, String name, String bio, String familyName, String avatarFileName,Boolean isPrivate, String email, int followersCount, int followingCount, LocalDateTime createdAt) {
        Assert.notNull(authorId, "Author Id cannot be null");
        Assert.notNull(slug, "Slug cannot be null");
        Assert.notNull(name, "Name cannot be null");
        Assert.notNull(createdAt, "Created At cannot be null");
        this.authorId = authorId;
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
    }

    public Author(Long authorId, String slug, String name, LocalDateTime createdAt){
        Assert.notNull(authorId, "Author Id cannot be null");
        Assert.notNull(slug, "Slug cannot be null");
        Assert.notNull(name, "Name cannot be null");
        Assert.notNull(createdAt, "Created At cannot be null");
        this.authorId = authorId;
        this.slug = slug;
        this.name = name;
        this.createdAt = createdAt;
    }

    public void updateBio(String bio){
        if(bio.length() > 128) {
            throw new BioLengthExceededException();
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
        if(name.length() > 32){
            throw new NameLengthExceededException();
        }
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
        StaticEventPublisher.publishEvent(new AuthorFollowedEvent(this, follower));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(authorId, author.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId);
    }
}
