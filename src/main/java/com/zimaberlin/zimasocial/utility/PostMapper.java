package com.zimaberlin.zimasocial.utility;

import com.zimaberlin.zimasocial.domain.Post;
import com.zimaberlin.zimasocial.domain.User;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.UserEntity;
import com.zimaberlin.zimasocial.service.Posts.Payload.PostPayload;
import org.hibernate.Hibernate;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        builder = @Builder(disableBuilder = false))  // Enable builder pattern usage
public interface PostMapper {
    @Mapping(target = "user", qualifiedByName = "mapUser")
    Post postEntityToPost(PostEntity postEntity);

    @Named("mapUser")
    default User mapUser(UserEntity userEntity) {
        userEntity = Hibernate.unproxy(userEntity, UserEntity.class);
        return CustomUserMapper.entityToDomain(userEntity);
    }

    PostEntity payloadToPostEntity(PostPayload payload);
}
