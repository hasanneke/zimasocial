package com.zimaberlin.zimasocial.entity;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.entity.user.exceptions.CircularFollowException;
import com.zimaberlin.zimasocial.entity.user.exceptions.CircularUnfollowException;
import com.zimaberlin.zimasocial.entity.user.exceptions.UserAlreadyFollowed;
import com.zimaberlin.zimasocial.entity.user.exceptions.UserNotFollowed;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


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
        Assertions.assertTrue(follower.getFollowings().contains(followed));
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
        Assertions.assertFalse(followed.getFollowers().contains(follower));
        Assertions.assertFalse(follower.getFollowings().contains(followed));
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
}
