package com.zimaberlin.zimasocial.context.social.infastructure.adapter;

import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthorUserEntityAdapter {
   public Author convertUserEntityToAuthor(UserEntity user){
       if(user == null) return null;

       return new Author(user.getId(), user.getSlug(), user.getName(), user.getFamilyName(), user.getBio(), user.getAvatarFileName(), user.isPrivate(), user.getEmail(), user.getFollowersCount(), user.getFollowingCount(), user.getCreatedAt());
    }
}
