package com.zimaberlin.zimasocial.service.users;

import com.zimaberlin.zimasocial.service.users.Payload.UserUpdatePayload;
import com.zimaberlin.zimasocial.views.user.DetailedUserView;
import com.zimaberlin.zimasocial.views.user.UserView;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    UserView updateProfileImage(MultipartFile image) throws IOException;
    UserView updateUser(UserUpdatePayload payload);
    DetailedUserView getUserMe();
    UserView getUser(String slug);
    boolean checkUsernameExists(String slug);
    void followUser(String slug) throws BadRequestException;
    void unfollowUser(String slug) throws BadRequestException;
    Page<UserView> getFollowers(String slug, int page, int size);
    Page<UserView> getFollowing(String slug, int page, int size);
    void blockUser(String slug);
    void unblockUser(String slug);
    void removeMyProfileImage();
    UserView updateBio(String bio);
    UserView updateSlug(String slug);
    UserView updateName(String name);
    UserView makeAccountPrivate();
    UserView makeAccountPublic();
    boolean checkSlugExists(String slug);
}
