package com.zima.zimasocial.context.social.infastructure.adapter;

import com.zima.zimasocial.context.social.author.entity.AuthorDomain;
import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.entity.user.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthorUserEntityAdapter {
   public static AuthorDomain convertUserEntityToAuthor(UserEntity user){
       if(user == null) return null;

       return new AuthorDomain(new AuthorDomainId(user.getId()), user.getSlug(), user.getName(),  user.getBio(), user.getFamilyName(), user.getAvatarFileName(), user.isPrivate(), user.getEmail(), user.getFollowerCount(), user.getFollowingCount(), user.getCreatedAt(), user.getLastSlugChangedAt());
    }
}
