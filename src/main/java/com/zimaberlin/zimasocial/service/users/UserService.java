package com.zimaberlin.zimasocial.service.users;

import com.zimaberlin.zimasocial.context.social.ImageService;
import com.zimaberlin.zimasocial.context.social.api.view.AuthorView;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.entity.user.exceptions.SlugAlreadyExistException;
import com.zimaberlin.zimasocial.exception.DataNotFoundException;
import com.zimaberlin.zimasocial.repository.UserRepository;
import com.zimaberlin.zimasocial.service.users.payload.UserUpdatePayload;
import com.zimaberlin.zimasocial.service.users.exception.UserNotFoundException;
import com.zimaberlin.zimasocial.utility.CurrentUser;
import com.zimaberlin.zimasocial.utility.UserViewFactory;
import com.zimaberlin.zimasocial.context.social.api.view.DetailedAuthorView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private UserViewFactory userMapper;
    private ImageService imageService;

    @Autowired
    public UserService(UserRepository userRepository, UserViewFactory userMapper, ImageService imageService) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    @Transactional
    public AuthorView updateProfileImage(MultipartFile image) throws IOException {
        String avatarFileName = imageService.uploadProfileImage(image);
        UserEntity user = CurrentUser.getCurrentUserProfile();
        if (user.getAvatarFileName() != null) {
            imageService.deleteFile(user.getAvatarFileName());
        }
        user.attachFileName(avatarFileName);
        userRepository.save(user);

        return userMapper.populated(user);
    }

    public AuthorView updateUser(@Valid UserUpdatePayload payload) {
        UserEntity user = CurrentUser.getCurrentUserProfile();
        if (payload.getBio() != null) {
            user.updateBio(payload.getBio());
        }
        if (payload.getName() != null) {
            user.updateName(payload.getName());
        }
        userRepository.save(user);
        return userMapper.populated(user);
    }

    public boolean checkUsernameExists(String slug) {
        userRepository.findBySlug(slug).orElseThrow(() -> new DataNotFoundException("User not found"));
        return true;
    }

    @Transactional
    public void followUser(String slug) {
        UserEntity followedUser = userRepository.findBySlug(slug).orElseThrow(() -> new DataNotFoundException("User not found"));
        UserEntity me = userRepository.findById(CurrentUser.getCurrentUserProfile().getId()).orElseThrow(() -> new DataNotFoundException("User not found"));
        followedUser.follow(me);
        userRepository.save(followedUser);
    }

    @Transactional
    public void unfollowUser(String slug) {
        UserEntity unfollowedUser = userRepository.findBySlug(slug).orElseThrow(() -> new DataNotFoundException("User not found"));
        UserEntity me = userRepository.findById(CurrentUser.getCurrentUserProfile().getId()).orElseThrow(() -> new DataNotFoundException("User not found"));
        unfollowedUser.unfollowUser(me);
        userRepository.save(me);
    }

    public Page<AuthorView> getFollowers(String slug, int page, int size) {
//        UserEntity user = userRepository.findBySlug(slug).orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        PageRequest requestPage = PageRequest.of(page, size);
//        Page<UserEntity> getFollowers = userRepository.findFollowersByUserAndRelation(user, requestPage);
//
//        return getFollowers.map(userMapper::populated);
        return null;
    }

    public Page<AuthorView> getFollowing(String slug, int page, int size) {
//        UserEntity user = userRepository.findBySlug(slug).orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        PageRequest requestPage = PageRequest.of(page, size);
//        Page<UserEntity> getFollowers = userRepository.findFollowingsByUserAndRelation(user, requestPage);
//
//        return getFollowers.map(userMapper::populated);
        return null;
    }

    @Transactional
    public void blockUser(String slug) {
        UserEntity user = userRepository.findBySlug(slug).orElseThrow(UserNotFoundException::new);
        user.block(CurrentUser.getCurrentUserProfile());
        userRepository.save(user);
    }

    public void unblockUser(String slug) {
        UserEntity user = userRepository.findBySlug(slug).orElseThrow(UserNotFoundException::new);
        user.unblock(CurrentUser.getCurrentUserProfile());
        userRepository.save(user);
    }

    public void removeMyProfileImage() {
        UserEntity user = CurrentUser.getCurrentUserProfile();
        if (user.getAvatarFileName() != null) {
            imageService.deleteFile(user.getAvatarFileName());
            user.removeProfilePhoto();
            userRepository.save(user);
        }
    }

    public DetailedAuthorView getUserMe() {
        return userMapper.populateDetailed(CurrentUser.getCurrentUserProfile());
    }

    public AuthorView getUser(String slug) {
        UserEntity user = userRepository.findBySlug(slug).orElseThrow(() -> new DataNotFoundException("User not found"));
        return userMapper.populated(user);
    }

    @Transactional
    public AuthorView updateBio(String bio) {
        UserEntity user = CurrentUser.getCurrentUserProfile();
        user.updateBio(bio);
        userRepository.save(user);
        return userMapper.populated(user);
    }

    @Transactional
    public AuthorView updateName(String name) {
        UserEntity user = CurrentUser.getCurrentUserProfile();
        user.updateName(name);
        userRepository.save(user);
        return userMapper.populated(user);
    }

    @Transactional
    public AuthorView updateSlug(String slug) {
        boolean isSlugExists = userRepository.findBySlug(slug).isPresent();
        if (isSlugExists) {
            throw new SlugAlreadyExistException();
        }
        UserEntity user = CurrentUser.getCurrentUserProfile();
        user.updateSlug(slug);
        userRepository.save(user);
        return userMapper.populated(user);
    }

    public boolean checkSlugExists(String slug) {
        Optional<UserEntity> user = userRepository.findBySlug(slug);
        user.ifPresent(e -> {
            throw new SlugAlreadyExistException();
        });
        return false;
    }

    public Page<AuthorView> searchUsers(String query, int page, int size) {
        Page<UserEntity> userEntities = userRepository.searchUser(query, PageRequest.of(page, size));
        return userEntities.map(e -> userMapper.populated(e));
    }
}
