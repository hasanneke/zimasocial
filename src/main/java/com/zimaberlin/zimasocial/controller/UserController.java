package com.zimaberlin.zimasocial.controller;
import com.zimaberlin.zimasocial.service.users.Payload.UserUpdatePayload;
import com.zimaberlin.zimasocial.service.users.UserService;
import com.zimaberlin.zimasocial.views.user.UserView;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(path = "api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/me")
    ResponseEntity<UserView> getMe(){
        UserView userView = userService.getUserMe();
        return ResponseEntity.ok(userView);
    }

    @GetMapping(path = "/{slug}")
    public ResponseEntity<UserView> getUser(@PathVariable(name = "slug") String slug){
        UserView userView = userService.getUser(slug);
        return ResponseEntity.ok(userView);
    }

    @PatchMapping(path = "/me")
    public ResponseEntity<UserView> updateUser(@RequestBody UserUpdatePayload payload){
        UserView userView = userService.updateUser(payload);
        return ResponseEntity.ok(userView);
    }

    @PatchMapping(path = "/me/upload-image")
    public  ResponseEntity<UserView> uploadProfileImage(MultipartFile image){
        UserView user = userService.updateProfileImage(image);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/me/change-username")
    public ResponseEntity<UserView> changeUsername(@NotBlank @RequestParam(name = "slug") String slug){
        UserView userView = userService.updateUsername(slug);
        return ResponseEntity.ok(userView);
    }

    @RequestMapping(path = "/check-username-exists", method = RequestMethod.HEAD)
    public ResponseEntity<Boolean> checkUsernameExists(@NotBlank @RequestParam(name = "slug") String slug){
        boolean usernameExists = userService.checkUsernameExists(slug);
        return ResponseEntity.ok(usernameExists);
    }

    @GetMapping(path = "/{slug}/follow")
    public ResponseEntity<Void> followUser(@PathVariable(name = "slug") String slug) {
        userService.followUser(slug);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{slug}/unfollow")
    public ResponseEntity<Void> unfollowUser(@PathVariable(name = "slug") String slug) throws BadRequestException {
        userService.unfollowUser(slug);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{slug}/followers")
    public HttpEntity<PagedModel<UserView>> getFollowers(
            @PathVariable(name = "slug") String slug,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size) throws NoSuchMethodException {
        Page followersPage = userService.getFollowers(slug, page, size);

        PagedModel<UserView> pagedModel = PagedModel.of(
                followersPage.getContent(),
                new PagedModel.PageMetadata(followersPage.getSize(),
                        followersPage.getNumber(),
                        followersPage.getTotalElements(),
                        followersPage.getTotalPages()));

        Method method = this.getClass().getMethod("getFollowers",
                String.class,
                Integer.class,
                Integer.class);

        if(page < followersPage.getTotalPages()){
            Link link = linkTo(method, slug,page + 1, size).withRel(LinkRelation.of("next"));
            pagedModel.add(link);
        }

        if(page > 0){
            Link link = linkTo(method, slug, page - 1, size).withRel(LinkRelation.of("previous"));
            pagedModel.add(link);
        }
        return new  HttpEntity<>(pagedModel);
    }

    @GetMapping(path = "/{slug}/followings")
    public HttpEntity<PagedModel<UserView>> getFollowing(
            @PathVariable(name = "slug") String slug,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size) throws NoSuchMethodException {
        Page followingsPage = userService.getFollowing(slug, page, size);

        PagedModel<UserView> pagedModel = PagedModel.of(
                followingsPage.getContent(),
                new PagedModel.PageMetadata(followingsPage.getSize(),
                        followingsPage.getNumber(),
                        followingsPage.getTotalElements(),
                        followingsPage.getTotalPages()));

        Method method = this.getClass().getMethod("getFollowing",
                String.class,
                Integer.class,
                Integer.class);

        if(page < followingsPage.getTotalPages()){
            Link link = linkTo(method, slug,page + 1, size).withRel(LinkRelation.of("next"));
            pagedModel.add(link);
        }

        if(page > 0){
            Link link = linkTo(method, slug, page - 1, size).withRel(LinkRelation.of("previous"));
            pagedModel.add(link);
        }
        return new  HttpEntity<>(pagedModel);
    }
}
