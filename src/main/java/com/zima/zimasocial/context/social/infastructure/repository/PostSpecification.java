package com.zima.zimasocial.context.social.infastructure.repository;

import com.zima.zimasocial.entity.PostJpaEntity;
import com.zima.zimasocial.entity.MediaType;
import com.zima.zimasocial.entity.user.UserEntity;
import com.zima.zimasocial.entity.userRelation.UserRelationEntity;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class PostSpecification {
   public static Specification<PostJpaEntity> authorId(Long authorId){
       return (root, query, builder)-> builder
               .equal(root.get("userId"), authorId);
   }

    public static Specification<PostJpaEntity> type(MediaType type){
        return (root, query, builder)-> builder
                .equal(root.get("type"), type);
    }

    public static Specification<PostJpaEntity> notBlocked(Long userId, List<UserRelationEntity> blockRelations) {
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

    public static Specification<PostJpaEntity> isVisible() {
        return (root, query, builder) -> builder.equal(root.get("isVisible"), true);
    }

    public static Specification<PostJpaEntity> isAuthorPublicOrAuthorFollowed(Long selfUserId, List<Long> followedAuthorIds) {
       return (root, query, builder) -> {
           Path<UserEntity> userEntityPath = root.get("user");
           return builder.or(builder.equal(userEntityPath.get("isPrivate"), false),
                   builder.equal(root.get("userId"), selfUserId),
                   root.get("userId").in(followedAuthorIds));
       };
    }
}
