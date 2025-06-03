package com.zimaberlin.zimasocial.entity.user;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zimaberlin.zimasocial.entity.*;
import com.zimaberlin.zimasocial.entity.report.ReportEntity;
import com.zimaberlin.zimasocial.entity.todayspost.TodaysPost;
import com.zimaberlin.zimasocial.entity.user.exceptions.*;
import com.zimaberlin.zimasocial.entity.userRelation.Relation;
import com.zimaberlin.zimasocial.entity.userRelation.UserRelationEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Table(name = "users")
@SQLRestriction(value = "IS_DELETED IS FALSE")
public class UserEntity {
    protected UserEntity() {}
    protected UserEntity(String email, String name, String familyName, String authProvider, Set<UserRole> roles, String slug){
        this.email = email;
        this.name = name;
        this.familyName = familyName;
        this.authProvider = authProvider;
        this.roles = roles;
        this.slug = slug;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slug", unique = true, length = 65)
    private String slug;

    @NotEmpty
    @Email
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

    @Column(name = "followers_count")
    private int followersCount = 0;

    @Column(name = "following_count")
    private int followingCount = 0;

    @Column(name = "avatar_file_name")
    private String avatarFileName;

    @Column(name = "auth_provider")
    private String authProvider;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<LikeEntity> likes = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<PostEntity> posts = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<CommentEntity> comments = new HashSet<>();

    @OneToMany(mappedBy = "reporter", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JsonIgnore
    private Set<ReportEntity> reports = new HashSet<>();

    @OneToMany(mappedBy = "reportedUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JsonIgnore
    private Set<ReportEntity> filedReports = new HashSet<>();

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<UserRole> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<RefreshTokenEntity> refreshTokens = new HashSet<>();

    @OneToMany(mappedBy = "receiverUser")
    @JsonIgnore
    private Set<NotificationEntity> notifications = new HashSet<>();

    @OneToMany(mappedBy = "actor", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<UserRelationEntity> initiatedRelations = new HashSet<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<UserRelationEntity> receivedRelations = new HashSet<>();

    @OneToMany(mappedBy = "author")
    @JsonIgnore
    private Set<TodaysPost> todaysPost =  new HashSet<>();

    @Column(name = "IS_DELETED", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;


    public Set<UserEntity> getFollowers() {
        return receivedRelations.stream().filter(e->e.getRelation().equals(Relation.followed)).map(UserRelationEntity::getActor).collect(Collectors.toSet());
    }

    public Set<UserEntity> getFollowings() {
        return initiatedRelations.stream().filter(e->e.getRelation().equals(Relation.followed)).map(UserRelationEntity::getReceiver).collect(Collectors.toSet());
    }
    boolean isBlocked(UserEntity blocker){
        Optional<UserRelationEntity> blockRelation = beingBlockedRelations().stream().filter(e->e.getActor().equals(blocker)).toList().stream().findFirst();
        return blockRelation.isPresent();
    }
    private Optional<UserRelationEntity> followRelationWith(UserEntity user) {
        return receivedRelations.stream().filter(e-> user.equals(e.getActor()) && e.getRelation().equals(Relation.followed)).findFirst();
    }
    private Optional<UserRelationEntity> followedByRelationWith(UserEntity user) {
        return receivedRelations.stream().filter(e-> user.equals(e.getActor()) && e.getRelation().equals(Relation.followed)).findFirst();
    }
    private Set<UserRelationEntity> blockedRelations() {
        return initiatedRelations.stream().filter(e->e.getRelation().equals(Relation.blocked)).collect(Collectors.toSet());
    }
    public Set<UserRelationEntity> beingBlockedRelations() {
        return receivedRelations.stream().filter(e->e.getRelation().equals(Relation.blocked)).collect(Collectors.toSet());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    protected void incrementFollowingCount(){
        followingCount = getFollowingCount() + 1;
    }
    protected void decrementFollowingCount(){
        followingCount = getFollowingCount() - 1;
    }
    protected void incrementFollowerCount(){
        followersCount = getFollowersCount() + 1;
    }
    protected void decrementFollowerCount(){
        followersCount = getFollowersCount() - 1;
    }

    protected void setReceivedRelations(Set<UserRelationEntity> relations) {
        this.receivedRelations = relations;
    }
    protected void setId(Long id){
        this.id = id;
    }
    public void attachFileName(String avatarFileName) {
        this.avatarFileName = avatarFileName;
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
    public void unfollowUser(UserEntity follower) {
        if(follower.equals(this)){
            throw new CircularUnfollowException();
        }
        Optional<UserRelationEntity> relation = followRelationWith(follower);
        if(relation.isPresent()){
            relation.get().markAsDeleted();
            decrementFollowerCount();
            follower.decrementFollowingCount();
        }else{
            throw new UserNotFollowed("You are not following the user");
        }
    }
    public void follow(UserEntity follower)  {
        if(follower.equals(this)){
            throw new CircularFollowException();
        }
        Optional<UserRelationEntity> relation = followedByRelationWith(follower);
        if(relation.isEmpty()){
            UserRelationEntity relationEntity = UserRelationEntity.builder()
                    .actor(follower)
                    .receiver(this)
                    .relation(Relation.followed)
                    .isDeleted(false)
                    .build();
            receivedRelations.add(relationEntity);
            follower.incrementFollowingCount();
            incrementFollowerCount();
        }else{
            throw new UserAlreadyFollowed("You are already following the user");
        }
    }

    public void block(UserEntity blocker) {
        Optional<UserRelationEntity> checkRelation = beingBlockedRelations().stream().filter(e->e.getActor().equals(blocker)).toList().stream().findFirst();
        if(checkRelation.isEmpty()){
            UserRelationEntity blockRelation = UserRelationEntity.builder()
                    .relation(Relation.blocked)
                    .actor(blocker)
                    .receiver(this)
                    .build();
            receivedRelations.add(blockRelation);
        }else{
            throw new UserIsAlreadyBlockedException();
        }
    }

    public void unblock(UserEntity blocker){
        Optional<UserRelationEntity> blockRelation = beingBlockedRelations().stream().filter(e->e.getActor().equals(blocker)).toList().stream().findFirst();
        if(blockRelation.isPresent()){
            blockRelation.get().markAsDeleted();
        }else{
            throw new UserIsNotBlockedException();
        }
    }
    public void makeAccountPrivate() {
        if(isPrivate){
            throw new AccountIsAlreadyPrivateException();
        }
        this.isPrivate = true;
    }
    public void makeAccountPublic() {
        if(!isPrivate){
            throw new AccountIsAlreadyPublicException();
        }
        this.isPrivate = false;
    }
}
