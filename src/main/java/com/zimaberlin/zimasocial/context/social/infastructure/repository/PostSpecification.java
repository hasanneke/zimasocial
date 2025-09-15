package com.zimaberlin.zimasocial.context.social.infastructure.repository;

import com.zimaberlin.zimasocial.context.social.post.Post;
import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.entity.userRelation.UserRelationEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class PostSpecification {
   public static Specification<PostEntity> authorId(Long authorId){
       return (root, query, builder)-> builder
               .equal(root.get("userId"), authorId);
   }

    public static Specification<PostEntity> type(PostType type){
        return (root, query, builder)-> builder
                .equal(root.get("type"), type);
    }

    public static Specification<PostEntity> notBlocked(Long userId, List<UserRelationEntity> blockRelations) {
       return (root, query, builder) -> {
          List<Long> blockedOrBeingBlockedUserIds = blockRelations.stream().map(e->{
              if(e.getActorId().equals(userId)){
                  return e.getReceiverId();
              }else{
                  return e.getActorId();
              }
          }).toList();
          return root.get("userId")
                  .in(blockedOrBeingBlockedUserIds)
                  .not();
       };
    }

    public static Specification<PostEntity> isVisible() {
        return (root, query, builder) -> builder.equal(root.get("isVisible"), true);
    }

    public static Specification<PostEntity> isAuthorPublicOrAuthorFollowed(Long selfUserId, List<Long> followedAuthorIds) {
       return (root, query, builder) -> {
           Path<UserEntity> userEntityPath = root.get("user");
           return builder.or(builder.equal(userEntityPath.get("isPrivate"), false),
                   builder.equal(root.get("userId"), selfUserId),
                   root.get("userId").in(followedAuthorIds));
       };
    }
}
