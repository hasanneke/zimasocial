package com.zimaberlin.zimasocial.utility;

import com.zimaberlin.zimasocial.domain.Post;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.UserEntity;
import com.zimaberlin.zimasocial.service.Posts.Payload.PostPayload;
import org.hibernate.Hibernate;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        builder = @Builder(disableBuilder = false))  // Enable builder pattern usage
public interface PostMapper {

    @Mapping(target = "user", qualifiedByName = "unproxyUser")  // Use custom method for user mapping
    Post postEntityToPost(PostEntity postEntity);

    // Custom method to handle Hibernate proxy
    @Named("unproxyUser")
    default UserEntity unproxyUser(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return Hibernate.unproxy(userEntity, UserEntity.class);
    }

    PostEntity payloadToPostEntity(PostPayload payload);
}
