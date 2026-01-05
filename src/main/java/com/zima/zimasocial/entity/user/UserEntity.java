package com.zima.zimasocial.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.exception.CharachterLengthExceededException;
import com.zima.zimasocial.context.account.infastructure.entity.RefreshTokenEntity;
import com.zima.zimasocial.context.account.value.DeleteReason;
import com.zima.zimasocial.context.account.value.DisableReason;
import com.zima.zimasocial.context.communication.domain.entity.Recipient;
import com.zima.zimasocial.context.contentmoderation.user.User;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.entity.*;
import com.zima.zimasocial.entity.todayspost.TodaysPost;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Table(name = "users")
@Entity
@Getter
@SQLRestriction(value = "IS_DELETED IS FALSE")
public class UserEntity implements Serializable {
    public UserEntity(Long id) {
        this.id = id;
    }
    public UserEntity(Long id, String email, String name, String familyName, String authProvider, Set<UserRole> roles, String slug){
        this.id = id;
        this.email = email;
        this.name = name;
        this.familyName = familyName;
        this.authProvider = authProvider;
        this.roles = roles;
        this.slug = slug;
        this.isDeleted = false;
    }
    @Id
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

    @Column(name = "follower_count")
    private int followerCount = 0;

    @Column(name = "following_count")
    private int followingCount = 0;

    @Column(name = "trust_score")
    private Double trustScore = 100.0;

    @Column(name = "avatar_file_name")
    private String avatarFileName;

    @Column(name = "auth_provider")
    private String authProvider;

    @Column(name = "last_slug_changed_at")
    private LocalDate lastSlugChangedAt;

    @Column(name = "TERMS_OF_USE_ACCEPTED")
    private Boolean termsOfUseAccepted = false;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<PostEntity> posts = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<CommentEntity> comments = new HashSet<>();

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

    @OneToMany(mappedBy = "author")
    @JsonIgnore
    private Set<TodaysPost> todaysPost =  new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<UserDeviceToken> deviceTokens = new HashSet<>();

    @Column(name = "is_disabled")
    private Boolean isDisabled = false;

    @Column(name = "disable_date")
    private LocalDate disableDate;

    @Column(name = "disable_reason")
    @Enumerated(EnumType.STRING)
    private DisableReason disableReason;

    @Column(name = "delete_date")
    private LocalDate deleteDate;

    @Column(name = "delete_reason")
    @Enumerated(EnumType.STRING)
    private DeleteReason deleteReason;

    @Column(name = "IS_DELETED", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    public UserEntity() {
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

    protected void setId(Long id){
        this.id = id;
    }
    public void attachFileName(String avatarFileName) {
        this.avatarFileName = avatarFileName;
    }
    public void updateBio(String bio){
        if(bio.length() > 128) {
            throw new CharachterLengthExceededException(128);
        }
        this.bio = bio;
    }
    public void updateName(String name) {
        if(name.length() > 32){
            throw new CharachterLengthExceededException(32);
        }
        this.name = name;
    }
    public void updateSlug(String slug){
        if(name.length() > 32){
            throw new CharachterLengthExceededException(32);
        }
        this.slug = slug;
    }
    public void removeProfilePhoto() {
        avatarFileName = null;
    }
    public void unfollowUser(UserEntity follower) {
    }
    public void mergeAccount(Account account) {
        this.isDeleted = account.getIsDeleted();
        this.isDisabled = account.getIsDisabled();
        this.isPrivate = account.getIsPrivate();
        this.deleteDate = account.getDeleteDate();
        this.disableDate = account.getDisableDate();
        this.deleteReason = account.getDeleteReason();
        this.disableReason = account.getDisableReason();
        this.slug = account.getSlug();
        this.termsOfUseAccepted = account.getTermsOfUseAccepted();
    }

    public void margeAuthor(Author author){
        this.name = author.getName();
        this.familyName = author.getFamilyName();
        this.slug = author.getSlug();
        this.bio = author.getBio();
        this.avatarFileName = author.getAvatarFileName();
        this.followerCount = author.getFollowersCount();
        this.followingCount = author.getFollowingCount();
        this.lastSlugChangedAt = author.getLastSlugChangedAt();
    }

    public void mergeUser(User user){
        this.trustScore = user.getTrustScore();
        this.isDisabled = user.getIsBanned();
    }
    public void mergeRecipient(Recipient recipient) {
        this.deviceTokens = recipient.getDeviceTokens().stream().map(UserDeviceToken::new).collect(Collectors.toSet());
    }

    public String getFullName(){
        return name + " " + familyName;
    }

    public Author toDomain() {
        return new Author(new AuthorId(this.getId()), this.getSlug(), this.getName(),  this.getBio(), this.getFamilyName(), this.getAvatarFileName(), this.isPrivate(), this.getEmail(), this.getFollowerCount(), this.getFollowingCount(), this.getCreatedAt(), this.getLastSlugChangedAt());
    }
}
