package com.zimaberlin.zimasocial.context.social.infastructure.repository;

import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.entity.userRelation.Relation;
import com.zimaberlin.zimasocial.entity.userRelation.UserRelationEntity;
import com.zimaberlin.zimasocial.repository.UserRelationJpaRepository;
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
}
