package com.zimaberlin.zimasocial.service.users;

import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.entity.userRelation.Relation;
import com.zimaberlin.zimasocial.exception.ConflictException;
import com.zimaberlin.zimasocial.exception.ResourceNotFoundException;
import com.zimaberlin.zimasocial.repository.UserRelationRepository;
import com.zimaberlin.zimasocial.repository.UserRepository;
import com.zimaberlin.zimasocial.service.imageService.S3Service;
import com.zimaberlin.zimasocial.service.users.Payload.UserUpdatePayload;
import com.zimaberlin.zimasocial.service.users.exception.UserNotFoundException;
import com.zimaberlin.zimasocial.utility.CurrentUser;
import com.zimaberlin.zimasocial.utility.CustomUserMapper;
import com.zimaberlin.zimasocial.views.user.UserView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private CustomUserMapper userMapper;
    private S3Service s3Service;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CustomUserMapper userMapper, S3Service s3Service) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.s3Service = s3Service;
    }

    @Override
    public UserView updateProfileImage(@NotNull(message = "Image cannot be null") MultipartFile image)  {
        String url = s3Service.uploadImage(image);
        UserEntity user = CurrentUser.getCurrentUserProfile();
        if(user.getAvatarUrl() != null){
            String fileName = Arrays.stream(user.getAvatarUrl().split("/")).toList().getLast();
            s3Service.deleteImage(fileName);
        }
        user.setAvatarUrl(url);
        userRepository.save(user);

        return userMapper.entityToDomain(user);
    }

    @Override
    public UserView updateUser(@Valid UserUpdatePayload payload) {
        UserEntity user = CurrentUser.getCurrentUserProfile();
        if(payload.getBio() != null){
            user.setBio(payload.getBio());
        }
        if(payload.getName() != null){
            user.setName(payload.getName());
        }
        userRepository.save(user);
        return userMapper.entityToDomain(user);
    }

    @Override
    public UserView updateUsername(@NotBlank @Max(20) String slug) {
        boolean isSlugExists = checkUsernameExists(slug);
        if(isSlugExists){
            throw new ConflictException("Username already exists");
        }
        UserEntity user = CurrentUser.getCurrentUserProfile();
        user.setSlug(slug);
        userRepository.save(user);
        return userMapper.entityToDomain(user);
    }

    @Override
    public boolean checkUsernameExists( String slug) {
        UserEntity user = userRepository.findBySlug(slug).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        return user != null;
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

        return getFollowers.map(userMapper::entityToDomain);
    }

    @Override
    public Page<UserView> getFollowing(String slug, int page, int size) {
        UserEntity user = userRepository.findBySlug(slug).orElseThrow(()->new ResourceNotFoundException("User not found"));

        PageRequest requestPage = PageRequest.of(page, size);
        Page<UserEntity> getFollowers = userRepository.findFollowingsByUserAndRelation(user, requestPage);

        return getFollowers.map(userMapper::entityToDomain);
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
    public UserView getUserMe() {
        return userMapper.entityToDomain(CurrentUser.getCurrentUserProfile());
    }

    @Override
    public UserView getUser(String slug) {
        UserEntity user = userRepository.findBySlug(slug).orElseThrow(()->new ResourceNotFoundException("User not found"));
        return userMapper.entityToDomain(user);
    }
}
