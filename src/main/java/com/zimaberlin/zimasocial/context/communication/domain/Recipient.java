package com.zimaberlin.zimasocial.context.communication.domain;

import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
public class Recipient {
    private final String fullName;
    private final String avatarUrl;
    private final RecipientId recipientId;
    private final String slug;
    private final Set<DeviceToken> deviceTokens;
    private final String email;
    private final Boolean isPrivate;
    public Recipient(RecipientId recipientId,
                     String slug,
                     Set<DeviceToken> deviceTokens,
                     String email,
                     String fullName,
                     String avatarUrl,
                     Boolean privateAccount) {
        this.recipientId = recipientId;
        this.slug = slug;
        if (deviceTokens.isEmpty()){
            this.deviceTokens = new HashSet<>();
        }else{
            this.deviceTokens = deviceTokens;
        }
        this.email = email;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.isPrivate = privateAccount;
    }

    public void addToken(DeviceToken deviceToken) {
        this.deviceTokens.add(deviceToken);
    }
    public void removeToken(DeviceToken deviceToken) {
        this.deviceTokens.remove(deviceToken);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipient recipient = (Recipient) o;
        return Objects.equals(recipientId, recipient.recipientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipientId);
    }
}
