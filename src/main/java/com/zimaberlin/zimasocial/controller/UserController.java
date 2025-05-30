package com.zimaberlin.zimasocial.controller;
import com.zimaberlin.zimasocial.service.users.Payload.UserUpdatePayload;
import com.zimaberlin.zimasocial.service.users.UserService;
import com.zimaberlin.zimasocial.views.user.DetailedUserView;
import com.zimaberlin.zimasocial.views.user.UserView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

import java.io.IOException;
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
    ResponseEntity<DetailedUserView> getMe(){
        DetailedUserView userView = userService.getUserMe();
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
    public ResponseEntity<UserView> uploadProfileImage(MultipartFile image) throws IOException {
        UserView user = userService.updateProfileImage(image);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping(path = "/me/remove-profile")
    public ResponseEntity<Void> deleteProfileImage() {
        userService.removeMyProfileImage();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/update-slug")
    public ResponseEntity<UserView> updateSlug(@Valid @NotBlank @Size(max= 128)  @RequestParam(name = "slug") String slug){
        UserView userView = userService.updateSlug(slug);
        return ResponseEntity.ok(userView);
    }
    @PatchMapping("/me/update-bio")
    public ResponseEntity<UserView> updateBio(@Valid @NotBlank @Size(max= 128) @RequestParam(name = "bio") String bio){
        UserView userView = userService.updateBio(bio);
        return ResponseEntity.ok(userView);
    }

    @PatchMapping("/me/update-name")
    public ResponseEntity<UserView> updateName(@Valid @NotBlank @Size(max= 128)  @RequestParam(name = "name") String name){
        UserView userView = userService.updateName(name);
        return ResponseEntity.ok(userView);
    }
    @PatchMapping("/me/make-account-public")
    public ResponseEntity<UserView> makeAccountPublic(){
        UserView userView = userService.makeAccountPublic();
        return ResponseEntity.ok(userView);
    }
    @PatchMapping("/me/make-account-private")
    public ResponseEntity<UserView> makeAccountPrivate(){
        UserView userView = userService.makeAccountPrivate();
        return ResponseEntity.ok(userView);
    }
    @RequestMapping(path = "/check-username-exists", method = RequestMethod.HEAD)
    public ResponseEntity<Boolean> checkUsernameExists(@RequestParam(name = "slug") String slug){
        boolean usernameExists = userService.checkUsernameExists(slug);
        return ResponseEntity.ok(usernameExists);
    }

    @GetMapping(path = "/{slug}/follow")
    public ResponseEntity<Void> followUser(@PathVariable(name = "slug") String slug) throws BadRequestException {
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
        Page<UserView> followersPage = userService.getFollowers(slug, page, size);

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
        Page<UserView> followingsPage = userService.getFollowing(slug, page, size);

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
    @GetMapping("/{slug}/block")
    public ResponseEntity<Void> blockUser(@PathVariable(name = "slug") String slug) {
        userService.blockUser(slug);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{slug}/unblock")
    public ResponseEntity<Void> unblockUser(@PathVariable(name = "slug") String slug) {
        userService.unblockUser(slug);
        return ResponseEntity.ok().build();
    }
}
