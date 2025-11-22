package com.zimaberlin.zimasocial.context.communication.domain.value;


import lombok.Getter;

import java.util.Objects;

@Getter
public class DeviceToken {
    private Long id;
    private final String token;
    private final RecipientId recipientId;
    public DeviceToken(Long id, String token, RecipientId recipientId) {
        this.id = id;
        this.token = token;
        this.recipientId = recipientId;
    }
    public DeviceToken(String token, RecipientId recipientId) {
        this.token = token;
        this.recipientId = recipientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceToken that = (DeviceToken) o;
        return Objects.equals(token, that.token) && Objects.equals(recipientId, that.recipientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, recipientId);
    }
}
