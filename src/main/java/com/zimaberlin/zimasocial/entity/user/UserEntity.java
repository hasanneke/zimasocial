package com.zimaberlin.zimasocial.entity.user;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zimaberlin.zimasocial.entity.*;
import com.zimaberlin.zimasocial.entity.report.ReportEntity;
import com.zimaberlin.zimasocial.entity.user.exceptions.CircularFollowException;
import com.zimaberlin.zimasocial.entity.user.exceptions.CircularUnfollowException;
import com.zimaberlin.zimasocial.entity.user.exceptions.UserAlreadyFollowed;
import com.zimaberlin.zimasocial.entity.user.exceptions.UserNotFollowed;
import com.zimaberlin.zimasocial.entity.userRelation.Relation;
import com.zimaberlin.zimasocial.entity.userRelation.UserRelationEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.apache.coyote.BadRequestException;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@SQLRestriction(value = "IS_DELETED IS FALSE")
public class UserEntity extends BaseEntity {
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

    @Column(name = "avatar_url")
    private String avatarUrl;

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

    @OneToMany(mappedBy = "initiatedUser")
    @JsonIgnore
    private Set<UserRelationEntity> initiatedRelations = new HashSet<>();

    @OneToMany(mappedBy = "receiverUser")
    @JsonIgnore
    private Set<UserRelationEntity> receivedRelations = new HashSet<>();
    public Set<UserEntity> getFollowers() {
        return receivedRelations.stream().filter(e->e.getRelation().equals(Relation.followed)).map(UserRelationEntity::getInitiatedUser).collect(Collectors.toSet());
    }

    public Set<UserEntity> getFollowings() {
        return initiatedRelations.stream().filter(e->e.getRelation().equals(Relation.followed)).map(UserRelationEntity::getReceiverUser).collect(Collectors.toSet());
    }

    public void unfollowUser(UserEntity follower) {
        if(follower.equals(this)){
            throw new CircularUnfollowException();
        }
        Optional<UserRelationEntity> relation = followRelationWith(follower);
        if(relation.isPresent()){
            receivedRelations.remove(relation.get());
            decrementFollowerCount();
            follower.decrementFollowingCount();
            follower.initiatedRelations.remove(relation.get());
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
                    .initiatedUser(follower)
                    .receiverUser(this)
                    .relation(Relation.followed)
                    .build();
            receivedRelations.add(relationEntity);
            follower.initiatedRelations.add(relationEntity);
            follower.incrementFollowingCount();
            incrementFollowerCount();
        }else{
            throw new UserAlreadyFollowed("You are already following the user");
        }
    }

    private Optional<UserRelationEntity> followRelationWith(UserEntity user) {
        return receivedRelations.stream().filter(e-> user.equals(e.getInitiatedUser()) && e.getRelation().equals(Relation.followed)).findFirst();
    }
    private Optional<UserRelationEntity> followedByRelationWith(UserEntity user) {
        return receivedRelations.stream().filter(e-> user.equals(e.getInitiatedUser()) && e.getRelation().equals(Relation.followed)).findFirst();
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

    void incrementFollowingCount(){
        followingCount = getFollowingCount() + 1;
    }
    void decrementFollowingCount(){
        followingCount = getFollowingCount() - 1;
    }
    void incrementFollowerCount(){
        followersCount = getFollowersCount() + 1;
    }
    void decrementFollowerCount(){
        followersCount = getFollowersCount() - 1;
    }
}
