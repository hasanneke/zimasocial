package com.zima.zimasocial.context.communication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zima.zimasocial.context.communication.domain.value.RecipientId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

@Table(name = "users")
@Entity
@Getter
@SQLRestriction(value = "IS_DELETED IS FALSE AND IS_DISABLED IS FALSE AND IS_BANNED IS FALSE")
public class Recipient {
    @EmbeddedId
    @AttributeOverride(
            name = "value",
            column = @Column(name = "id", updatable = false)
    )
    private RecipientId id;

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
    private Boolean isPrivate = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<UserDeviceToken> deviceTokens = new HashSet<>();

    public void addToken(String token) {
        UserDeviceToken deviceToken = UserDeviceToken.fromDeviceTokenAndUserId(token, id);
        deviceTokens.add(deviceToken);
    }
    public void removeToken(UserDeviceToken deviceToken) {
        this.deviceTokens.remove(deviceToken);
    }
}
