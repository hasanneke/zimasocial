package com.zimaberlin.zimasocial.entity;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.entity.user.exceptions.CircularFollowException;
import com.zimaberlin.zimasocial.entity.user.exceptions.CircularUnfollowException;
import com.zimaberlin.zimasocial.entity.user.exceptions.UserAlreadyFollowed;
import com.zimaberlin.zimasocial.entity.user.exceptions.UserNotFollowed;
import com.zimaberlin.zimasocial.entity.userRelation.Relation;
import com.zimaberlin.zimasocial.entity.userRelation.UserRelationEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.stream.Collectors;


@ExtendWith(MockitoExtension.class)
public class UserEntityTest {
    @Test
    void followUser_WhenUserIsNotFollower() {
        UserEntity follower = new UserEntity();
        follower.setId(0L);
        UserEntity followed = new UserEntity();
        followed.setId(1L);
        followed.follow(follower);

        Assertions.assertEquals(followed.getFollowersCount(), 1);
        Assertions.assertEquals(follower.getFollowingCount(), 1);
        Assertions.assertTrue(followed.getFollowers().contains(follower));
    }

    @Test
    void unfollowUser_WhenUserIsNotFollower() {
        UserEntity follower = new UserEntity();
        follower.setId(0L);
        UserEntity followed = new UserEntity();
        followed.setId(1L);
        // SEED FOLLOWER
        followed.follow(follower);
        // UNFOLLOW
        followed.unfollowUser(follower);
        Assertions.assertEquals(followed.getFollowersCount(), 0);
        Assertions.assertEquals(follower.getFollowingCount(), 0);
        Assertions.assertTrue(followed.getReceivedRelations().contains(UserRelationEntity.builder()
                .initiatedUser(follower)
                .receiverUser(followed)
                .relation(Relation.followed)
                .isDeleted(true)
                .build()
        ));
    }

    @Test
    void followUser_WhenUserFollowSelf_ThrowCircularFollowException(){
        UserEntity follower = new UserEntity();
        follower.setId(0L);
        UserEntity followed = new UserEntity();
        followed.setId(0L);
        Assertions.assertThrows(CircularFollowException.class, () -> followed.follow(follower));
    }

    @Test
    void unfollowUser_WhenUserUnfollowSelf_ThrowCircularUnfollowException(){
        UserEntity follower = new UserEntity();
        follower.setId(0L);
        UserEntity followed = new UserEntity();
        followed.setId(0L);
        Assertions.assertThrows(CircularUnfollowException.class, () -> followed.unfollowUser(follower));
    }

    @Test
    void followUser_WhenUserIsAlreadyFollower_ThrowAlreadyFollowedException() {
        UserEntity follower = new UserEntity();
        follower.setId(0L);
        UserEntity followed = new UserEntity();
        followed.setId(1L);
        followed.follow(follower);

        Assertions.assertThrows(UserAlreadyFollowed.class, () -> followed.follow(follower));
    }

    @Test
    void unfollowUser_WhenUserIsNotFollowed_ThrowUserNotFollowedException() {
        UserEntity follower = new UserEntity();
        follower.setId(0L);
        UserEntity followed = new UserEntity();
        followed.setId(1L);

        Assertions.assertThrows(UserNotFollowed.class, () -> followed.unfollowUser(follower));
    }

    @Test
    void blockUser() {
        UserEntity blocker = new UserEntity();
        blocker.setId(0L);
        UserEntity blocked = new UserEntity();
        blocked.setId(1L);

        blocked.block(blocker);

        Assertions.assertTrue(blocked
                .beingBlockedRelations().contains(
                        UserRelationEntity.builder()
                                .receiverUser(blocked)
                                .initiatedUser(blocker)
                                .relation(Relation.blocked)
                                .build()
                ));
    }

    @Test
    void unblockUser() {
        UserEntity blocker = new UserEntity();
        blocker.setId(0L);
        UserEntity blocked = new UserEntity();
        blocked.setId(1L);
        blocked.setReceivedRelations(Set.of( UserRelationEntity.builder()
                .receiverUser(blocked)
                .initiatedUser(blocker)
                .relation(Relation.blocked)
                .build()));

        blocked.unblock(blocker);

        Assertions.assertTrue(blocked
                .beingBlockedRelations().contains(
                        UserRelationEntity.builder()
                                .receiverUser(blocked)
                                .initiatedUser(blocker)
                                .isDeleted(true)
                                .relation(Relation.blocked)

                                .build()
                ));
    }
}
