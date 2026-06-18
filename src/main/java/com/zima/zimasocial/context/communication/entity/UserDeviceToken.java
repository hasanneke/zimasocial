package com.zima.zimasocial.context.communication.entity;

import com.zima.zimasocial.context.communication.domain.value.RecipientId;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@Table(name = "user_device_token")
public class UserDeviceToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private Recipient user;

    @Embedded
    private RecipientId userId;

    @Column(name = "device_token")
    private String token;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    protected UserDeviceToken() {
    }
    public static UserDeviceToken fromDeviceTokenAndUserId(
            String deviceToken,
            RecipientId recipientId
    ) {
        UserDeviceToken userDeviceToken = new UserDeviceToken();
        userDeviceToken.token = deviceToken;
        userDeviceToken.userId = recipientId;
        return userDeviceToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDeviceToken that = (UserDeviceToken) o;
        return Objects.equals(userId, that.userId) && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, token);
    }
}
