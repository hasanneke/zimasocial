package com.zimaberlin.zimasocial.service.users;

import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.entity.user.exceptions.SlugAlreadyExistException;
import com.zimaberlin.zimasocial.exception.ResourceNotFoundException;
import com.zimaberlin.zimasocial.repository.UserRepository;
import com.zimaberlin.zimasocial.service.imageService.ImageService;
import com.zimaberlin.zimasocial.service.users.Payload.UserUpdatePayload;
import com.zimaberlin.zimasocial.service.users.exception.UserNotFoundException;
import com.zimaberlin.zimasocial.utility.CurrentUser;
import com.zimaberlin.zimasocial.utility.UserViewFactory;
import com.zimaberlin.zimasocial.views.user.DetailedUserView;
import com.zimaberlin.zimasocial.views.user.UserView;
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
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private UserViewFactory userMapper;
    private ImageService imageService;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserViewFactory userMapper, ImageService imageService) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.imageService = imageService;
    }
    @Override
    @Transactional
    public UserView updateProfileImage(MultipartFile image) throws IOException {
        String avatarFileName = imageService.uploadProfileImage(image);
        UserEntity user = CurrentUser.getCurrentUserProfile();
        if(user.getAvatarFileName() != null){
            imageService.deleteFile(user.getAvatarFileName());
        }
        user.attachFileName(avatarFileName);
        userRepository.save(user);

        return userMapper.populated(user);
    }
    @Override
    public UserView updateUser(@Valid UserUpdatePayload payload) {
        UserEntity user = CurrentUser.getCurrentUserProfile();
        if(payload.getBio() != null){
            user.updateBio(payload.getBio());
        }
        if(payload.getName() != null){
            user.updateName(payload.getName());
        }
        userRepository.save(user);
        return userMapper.populated(user);
    }
    @Override
    public boolean checkUsernameExists(String slug) {
        userRepository.findBySlug(slug).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        return true;
    }
    @Override
    @Transactional
    public void followUser(String slug) {
        UserEntity followedUser = userRepository.findBySlug(slug).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        UserEntity me = userRepository.findById(CurrentUser.getCurrentUserProfile().getId()).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        followedUser.follow(me);
        userRepository.save(followedUser);
    }
    @Override
    @Transactional
    public void unfollowUser(String slug) {
        UserEntity unfollowedUser = userRepository.findBySlug(slug).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        UserEntity me = userRepository.findById(CurrentUser.getCurrentUserProfile().getId()).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        unfollowedUser.unfollowUser(me);
        userRepository.save(me);
    }
    @Override
    public Page<UserView> getFollowers(String slug, int page, int size) {
        UserEntity user = userRepository.findBySlug(slug).orElseThrow(()->new ResourceNotFoundException("User not found"));

        PageRequest requestPage = PageRequest.of(page, size);
        Page<UserEntity> getFollowers = userRepository.findFollowersByUserAndRelation(user, requestPage);

        return getFollowers.map(userMapper::populated);
    }
    @Override
    public Page<UserView> getFollowing(String slug, int page, int size) {
        UserEntity user = userRepository.findBySlug(slug).orElseThrow(()->new ResourceNotFoundException("User not found"));

        PageRequest requestPage = PageRequest.of(page, size);
        Page<UserEntity> getFollowers = userRepository.findFollowingsByUserAndRelation(user, requestPage);

        return getFollowers.map(userMapper::populated);
    }
    @Override
    @Transactional
    public void blockUser(String slug) {
        UserEntity user = userRepository.findBySlug(slug).orElseThrow(UserNotFoundException::new);
        user.block(CurrentUser.getCurrentUserProfile());
        userRepository.save(user);
    }
    @Override
    public void unblockUser(String slug) {
        UserEntity user = userRepository.findBySlug(slug).orElseThrow(UserNotFoundException::new);
        user.unblock(CurrentUser.getCurrentUserProfile());
        userRepository.save(user);
    }
    @Override
    public void removeMyProfileImage() {
        UserEntity user = CurrentUser.getCurrentUserProfile();
        if(user.getAvatarFileName() != null){
            imageService.deleteFile(user.getAvatarFileName());
            user.removeProfilePhoto();
            userRepository.save(user);
        }
    }
    @Override
    public DetailedUserView getUserMe() {
        return userMapper.populateDetailed(CurrentUser.getCurrentUserProfile());
    }
    @Override
    public UserView getUser(String slug) {
        UserEntity user = userRepository.findBySlug(slug).orElseThrow(()->new ResourceNotFoundException("User not found"));
        return userMapper.populated(user);
    }
    @Override
    @Transactional
    public UserView updateBio(String bio) {
        UserEntity user = CurrentUser.getCurrentUserProfile();
        user.updateBio(bio);
        userRepository.save(user);
        return userMapper.populated(user);
    }
    @Override
    @Transactional
    public UserView updateName(String name) {
        UserEntity user = CurrentUser.getCurrentUserProfile();
        user.updateName(name);
        userRepository.save(user);
        return userMapper.populated(user);
    }
    @Override
    @Transactional
    public UserView updateSlug(String slug) {
        boolean isSlugExists = userRepository.findBySlug(slug).isPresent();
        if(isSlugExists){
            throw new SlugAlreadyExistException();
        }
        UserEntity user = CurrentUser.getCurrentUserProfile();
        user.updateSlug(slug);
        userRepository.save(user);
        return userMapper.populated(user);
    }
    @Override
    @Transactional
    public UserView makeAccountPrivate() {
        UserEntity user = CurrentUser.getCurrentUserProfile();
        user.makeAccountPrivate();
        userRepository.save(user);
        return userMapper.populated(user);
    }

    @Override
    @Transactional
    public UserView makeAccountPublic() {
        UserEntity user = CurrentUser.getCurrentUserProfile();
        user.makeAccountPublic();
        userRepository.save(user);
        return userMapper.populated(user);
    }

    @Override
    public boolean checkSlugExists(String slug) {
        Optional<UserEntity> user = userRepository.findBySlug(slug);
        user.ifPresent(e-> {
            throw new SlugAlreadyExistException();
        });
        return false;
    }
}
