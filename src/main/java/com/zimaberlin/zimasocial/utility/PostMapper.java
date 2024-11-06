package com.zimaberlin.zimasocial.utility;

import com.zimaberlin.zimasocial.views.post.PostView;
import com.zimaberlin.zimasocial.views.user.UserView;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.UserEntity;
import com.zimaberlin.zimasocial.service.posts.Payload.PostPayload;
import org.hibernate.Hibernate;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        builder = @Builder(disableBuilder = false))  // Enable builder pattern usage
public interface PostMapper {
    @Mapping(target = "user", qualifiedByName = "mapUser")
    PostView postEntityToPost(PostEntity postEntity);

    @Named("mapUser")
    default UserView mapUser(UserEntity userEntity) {
        userEntity = Hibernate.unproxy(userEntity, UserEntity.class);
        return CustomUserMapper.entityToDomain(userEntity);
    }

    PostEntity payloadToPostEntity(PostPayload payload);
}
