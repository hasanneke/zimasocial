package com.zimaberlin.zimasocial.entity.user;

import com.zimaberlin.zimasocial.entity.UserRole;
import com.zimaberlin.zimasocial.entity.userRelation.UserRelationEntity;

import java.util.Set;

public class UserFactory {
    // TEST PURPOSES ONLY, HIBERNATE WILL PERSIST THE ENTITY
    public static UserEntity createUser(Long id){
        UserEntity user = new UserEntity(0L);
        user.setId(id);
        return user;
    }
    // TEST PURPOSES ONLY, HIBERNATE WILL PERSIST THE ENTITY
    public static UserEntity createUser(Long id, Set<UserRelationEntity> receivedRelations){
        UserEntity user = new UserEntity(0L);
        user.setId(id);
//        user.setReceivedRelations(receivedRelations);
        return user;
    }
    public static UserEntity createUser(Long id, String email, String name, String familyName, String authProvider, Set<UserRole> roles, String slug) {
        return new UserEntity(id, email, name, familyName, "google", Set.of(UserRole.regular), slug);
    };
}
