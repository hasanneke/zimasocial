package com.zimaberlin.zimasocial.service.Users;

import com.zimaberlin.zimasocial.service.Users.Payload.UserUpdatePayload;
import com.zimaberlin.zimasocial.views.user.BasicUserView;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    BasicUserView updateProfileImage(MultipartFile image);
    BasicUserView updateUser(UserUpdatePayload payload);
    BasicUserView updateUsername(String slug);
    BasicUserView getUserMe();
    BasicUserView getUser(String slug);
    boolean checkUsernameExists(String slug);
}
