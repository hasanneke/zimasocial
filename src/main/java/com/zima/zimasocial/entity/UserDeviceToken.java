package com.zima.zimasocial.entity;

import com.zima.zimasocial.context.communication.domain.value.DeviceToken;
import com.zima.zimasocial.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user_device_token")
public class UserDeviceToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "device_token")
    private String token;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public UserDeviceToken(DeviceToken deviceToken) {
        this.id = deviceToken.getId();
        this.userId = deviceToken.getRecipientId().getValue();
        this.token = deviceToken.getToken();
        this.createdAt = LocalDateTime.now();
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
