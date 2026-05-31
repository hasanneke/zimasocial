package com.zima.zimasocial.context.social.author.abstracted;

import com.zima.zimasocial.context.social.author.entity.Author;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AuthorUseCase {
    void requestToFollow(String slug);
    void follow(String slug);
    void unfollow(String slug);
    void removeFollower(String slug);
    void block(String slug);
    void unblock(String slug);
    void updateName(String name);
    void updateSlug(String slug);
    void updateBio(String bio);
    Author updateProfileImage(MultipartFile image) throws IOException;
    void removeMyProfileImage();
    void acceptFollowRequest(String followerSlug);
    void deleteFollowRequest(String followedAuthorSlug, String followerAuthorSlug);
}
