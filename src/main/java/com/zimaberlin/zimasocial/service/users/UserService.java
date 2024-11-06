package com.zimaberlin.zimasocial.service.users;

import com.zimaberlin.zimasocial.service.users.Payload.UserUpdatePayload;
import com.zimaberlin.zimasocial.views.user.UserView;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserView updateProfileImage(MultipartFile image);
    UserView updateUser(UserUpdatePayload payload);
    UserView updateUsername(String slug);
    UserView getUserMe();
    UserView getUser(String slug);
    boolean checkUsernameExists(String slug);
    void followUser(String slug);
    void unfollowUser(String slug) throws BadRequestException;
    Page<UserView> getFollowers(String slug, int page, int size);
    Page<UserView> getFollowing(String slug, int page, int size);
}
