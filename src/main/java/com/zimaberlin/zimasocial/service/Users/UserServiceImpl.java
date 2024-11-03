package com.zimaberlin.zimasocial.service.Users;

import com.zimaberlin.zimasocial.entity.UserEntity;
import com.zimaberlin.zimasocial.exception.ConflictException;
import com.zimaberlin.zimasocial.exception.ResourceNotFoundException;
import com.zimaberlin.zimasocial.repository.UserRepository;
import com.zimaberlin.zimasocial.service.ImageService.S3Service;
import com.zimaberlin.zimasocial.service.Users.Payload.UserUpdatePayload;
import com.zimaberlin.zimasocial.utility.CurrentUser;
import com.zimaberlin.zimasocial.utility.CustomUserMapper;
import com.zimaberlin.zimasocial.views.user.BasicUserView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.AbstractAuditable_;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private S3Service s3Service;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, S3Service s3Service) {
        this.userRepository = userRepository;
        this.s3Service = s3Service;
    }

    @Override
    public BasicUserView uploadImage(@NotNull(message = "Image cannot be null") MultipartFile image)  {
        String url = s3Service.uploadImage(image);
        UserEntity user = CurrentUser.getCurrentUserProfile();
        user.setAvatarUrl(url);
        return CustomUserMapper.entityToDomain(user);
    }

    @Override
    public BasicUserView updateUser(@Valid UserUpdatePayload payload) {
        UserEntity user = CurrentUser.getCurrentUserProfile();
        if(payload.getBio() != null){
            user.setBio(payload.getBio());
        }
        if(payload.getName() != null){
            user.setName(payload.getName());
        }
        userRepository.save(user);
        return CustomUserMapper.entityToDomain(user);
    }

    @Override
    public BasicUserView updateUsername(@NotBlank @Max(20) String slug) {
        boolean isSlugExists = checkUsernameExists(slug);
        if(isSlugExists){
            throw new ConflictException("Username already exists");
        }
        UserEntity user = CurrentUser.getCurrentUserProfile();
        user.setSlug(slug);
        userRepository.save(user);
        return CustomUserMapper.entityToDomain(user);
    }

    @Override
    public boolean checkUsernameExists( String slug) {
        UserEntity user = userRepository.findBySlug(slug).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        return user != null;
    }
    @Override
    public BasicUserView getUserMe() {
        return CustomUserMapper.entityToDomain(CurrentUser.getCurrentUserProfile());
    }

    @Override
    public BasicUserView getUser(String slug) {
        UserEntity user = userRepository.findBySlug(slug).orElseThrow(()->new ResourceNotFoundException("User not found"));
        return CustomUserMapper.entityToDomain(user);
    }
}
