package com.zimaberlin.zimasocial.context.social.infastructure.repository;

import com.zimaberlin.zimasocial.entity.PostEntity;
import com.zimaberlin.zimasocial.entity.PostType;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.entity.userRelation.UserRelationEntity;
import org.springframework.data.jpa.domain.Specification;

public class PostSpecification {
   public static Specification<PostEntity> authorId(Long authorId){
       return (root, query, criteriaBuilder)-> criteriaBuilder
               .equal(root.get("userId"), authorId);
   }

    public static Specification<PostEntity> type(PostType type){
        return (root, query, criteriaBuilder)-> criteriaBuilder
                .equal(root.get("type"), type);
    }
}
