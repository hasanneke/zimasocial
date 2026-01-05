package com.zima.zimasocial.context.communication.infastructure;

import com.zima.zimasocial.context.communication.domain.value.DeviceToken;
import com.zima.zimasocial.context.communication.domain.entity.Recipient;
import com.zima.zimasocial.context.communication.domain.value.RecipientId;
import com.zima.zimasocial.entity.user.UserEntity;

import java.util.stream.Collectors;

public class UserEntityRecipientConverter {
    public static Recipient toRecipient(UserEntity user) {
        return new Recipient(new RecipientId(user.getId()), user.getSlug(), user.getDeviceTokens().stream().map(e->new DeviceToken(e.getId(), e.getToken(), new RecipientId(e.getUserId()))).collect(Collectors.toSet()) , user.getEmail(), user.getFullName(), user.getAvatarFileName(), user.isPrivate());
    }
}
