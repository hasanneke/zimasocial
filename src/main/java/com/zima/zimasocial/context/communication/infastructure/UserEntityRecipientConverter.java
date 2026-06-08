package com.zima.zimasocial.context.communication.infastructure;

import com.zima.zimasocial.context.communication.domain.value.DeviceToken;
import com.zima.zimasocial.context.communication.domain.entity.RecipientDomain;
import com.zima.zimasocial.context.communication.domain.value.RecipientId;
import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.entity.user.UserEntity;

import java.util.stream.Collectors;

public class UserEntityRecipientConverter {
    public static RecipientDomain toRecipient(UserEntity user) {
        return new RecipientDomain(new RecipientId(user.getId()), user.getSlug(), user.getDeviceTokens().stream().map(e->new DeviceToken(e.getId(), e.getToken(), new RecipientId(e.getUserId()))).collect(Collectors.toSet()) , user.getEmail(), user.getFullName(), user.getAvatarFileName(), user.isPrivate());
    }

    public static RecipientDomain toRecipient(Author author) {
        return new RecipientDomain(new RecipientId(author.getId().getValue()), author.getSlug(), author.getDeviceTokens().stream().map(e->new DeviceToken(e.getId(), e.getToken(), new RecipientId(e.getUserId()))).collect(Collectors.toSet()) , author.getEmail(), author.getFullName(), author.getAvatarFileName(), author.getIsPrivate());
    }
}
