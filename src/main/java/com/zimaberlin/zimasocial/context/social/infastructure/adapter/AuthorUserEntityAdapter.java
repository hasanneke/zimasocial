package com.zimaberlin.zimasocial.context.social.infastructure.adapter;

import com.zimaberlin.zimasocial.context.social.author.entity.Author;
import com.zimaberlin.zimasocial.context.social.author.value.AuthorId;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthorUserEntityAdapter {
   public static Author convertUserEntityToAuthor(UserEntity user){
       if(user == null) return null;

       return new Author(new AuthorId(user.getId()), user.getSlug(), user.getName(),  user.getBio(), user.getFamilyName(), user.getAvatarFileName(), user.isPrivate(), user.getEmail(), user.getFollowersCount(), user.getFollowingCount(), user.getCreatedAt(), user.getLastSlugChangedAt());
    }
}
