//package com.zimaberlin.zimasocial.entity;
//import com.zimaberlin.zimasocial.entity.user.UserEntity;
//import com.zimaberlin.zimasocial.entity.user.UserFactory;
//import com.zimaberlin.zimasocial.context.account.exception.CircularFollowException;
//import com.zimaberlin.zimasocial.context.account.exception.CircularUnfollowException;
//import com.zimaberlin.zimasocial.context.account.exception.UserAlreadyFollowed;
//import com.zimaberlin.zimasocial.context.account.exception.UserNotFollowed;
//import com.zimaberlin.zimasocial.entity.userRelation.Relation;
//import com.zimaberlin.zimasocial.entity.userRelation.UserRelationEntity;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//
//@ExtendWith(MockitoExtension.class)
//public class UserEntityTest {
//    @Test
//    void followUser_WhenUserIsNotFollower() {
//        UserEntity follower = UserFactory.createUser(0L);
//        UserEntity followed = UserFactory.createUser(1L);
//        followed.follow(follower);
//
//        Assertions.assertEquals(followed.getFollowersCount(), 1);
//        Assertions.assertEquals(follower.getFollowingCount(), 1);
//        Assertions.assertTrue(followed.getFollowers().contains(follower));
//    }
//
//    @Test
//    void unfollowUser_WhenUserIsNotFollower() {
//        UserEntity follower = UserFactory.createUser(0L);
//        UserEntity followed = UserFactory.createUser(1L);
//        // SEED FOLLOWER
//        followed.follow(follower);
//        // UNFOLLOW
//        followed.unfollowUser(follower);
//        Assertions.assertEquals(followed.getFollowersCount(), 0);
//        Assertions.assertEquals(follower.getFollowingCount(), 0);
//        Assertions.assertTrue(followed.getReceivedRelations().contains(UserRelationEntity.builder()
//                .actor(follower)
//                .receiver(followed)
//                .relation(Relation.followed)
//                .isDeleted(true)
//                .build()
//        ));
//    }
//
//    @Test
//    void followUser_WhenUserFollowSelf_ThrowCircularFollowException(){
//        UserEntity follower = UserFactory.createUser(0L);
//        UserEntity followed = UserFactory.createUser(0L);
//        Assertions.assertThrows(CircularFollowException.class, () -> followed.follow(follower));
//    }
//
//    @Test
//    void unfollowUser_WhenUserUnfollowSelf_ThrowCircularUnfollowException(){
//        UserEntity follower = UserFactory.createUser(0L);
//        UserEntity followed = UserFactory.createUser(0L);
//        Assertions.assertThrows(CircularUnfollowException.class, () -> followed.unfollowUser(follower));
//    }
//
//    @Test
//    void followUser_WhenUserIsAlreadyFollower_ThrowAlreadyFollowedException() {
//        UserEntity follower = UserFactory.createUser(0L);
//        UserEntity followed = UserFactory.createUser(1L);
//        followed.follow(follower);
//
//        Assertions.assertThrows(UserAlreadyFollowed.class, () -> followed.follow(follower));
//    }
//
//    @Test
//    void unfollowUser_WhenUserIsNotFollowed_ThrowUserNotFollowedException() {
//        UserEntity follower = UserFactory.createUser(0L);
//        UserEntity followed = UserFactory.createUser(1L);
//
//        Assertions.assertThrows(UserNotFollowed.class, () -> followed.unfollowUser(follower));
//    }
//
//    @Test
//    void blockUser() {
//        UserEntity blocker = UserFactory.createUser(0L);
//        UserEntity blocked = UserFactory.createUser(1L);
//
//        blocked.block(blocker);
//
//        Assertions.assertTrue(blocked
//                .beingBlockedRelations().contains(
//                        UserRelationEntity.builder()
//                                .receiver(blocked)
//                                .actor(blocker)
//                                .relation(Relation.blocked)
//                                .build()
//                ));
//    }
//
//    @Test
//    void unblockUser() {
//        UserEntity blocker = UserFactory.createUser(0L);
//        UserEntity blocked = UserFactory.createUser(1L);
//        blocked.block(blocker);
//
//        blocked.unblock(blocker);
//
//        Assertions.assertTrue(blocked
//                .beingBlockedRelations().contains(
//                        UserRelationEntity.builder()
//                                .receiver(blocked)
//                                .actor(blocker)
//                                .isDeleted(true)
//                                .relation(Relation.blocked)
//
//                                .build()
//                ));
//    }
//}
