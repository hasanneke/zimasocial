package com.zimaberlin.zimasocial.controller;
import com.zimaberlin.zimasocial.service.users.UserService;
import com.zimaberlin.zimasocial.context.social.api.author.DetailedAuthorView;
import com.zimaberlin.zimasocial.context.social.api.author.AuthorView;
import jakarta.validation.Valid;
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
    ResponseEntity<DetailedAuthorView> getMe(){
        DetailedAuthorView userView = userService.getUserMe();
        return ResponseEntity.ok(userView);
    }

    @GetMapping(path = "/{slug}")
    public ResponseEntity<AuthorView> getUser(@PathVariable(name = "slug") String slug){
        AuthorView authorView = userService.getUser(slug);
        return ResponseEntity.ok(authorView);
    }

    @PatchMapping(path = "/me/upload-image")
    public ResponseEntity<AuthorView> uploadProfileImage(MultipartFile image) throws IOException {
        AuthorView user = userService.updateProfileImage(image);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping(path = "/me/remove-profile")
    public ResponseEntity<Void> deleteProfileImage() {
        userService.removeMyProfileImage();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/update-slug")
    public ResponseEntity<AuthorView> updateSlug(@Valid @NotBlank @Size(max= 128)  @RequestParam(name = "slug") String slug){
        AuthorView authorView = userService.updateSlug(slug);
        return ResponseEntity.ok(authorView);
    }
    @PatchMapping("/me/update-bio")
    public ResponseEntity<AuthorView> updateBio(@Valid @NotBlank @Size(max= 128) @RequestParam(name = "bio") String bio){
        AuthorView authorView = userService.updateBio(bio);
        return ResponseEntity.ok(authorView);
    }

    @PatchMapping("/me/update-name")
    public ResponseEntity<AuthorView> updateName(@Valid @NotBlank @Size(max= 128)  @RequestParam(name = "name") String name){
        AuthorView authorView = userService.updateName(name);
        return ResponseEntity.ok(authorView);
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
    public HttpEntity<PagedModel<AuthorView>> getFollowers(
            @PathVariable(name = "slug") String slug,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size) throws NoSuchMethodException {
        Page<AuthorView> followersPage = userService.getFollowers(slug, page, size);

        PagedModel<AuthorView> pagedModel = PagedModel.of(
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
    public HttpEntity<PagedModel<AuthorView>> getFollowing(
            @PathVariable(name = "slug") String slug,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size) throws NoSuchMethodException {
        Page<AuthorView> followingsPage = userService.getFollowing(slug, page, size);

        PagedModel<AuthorView> pagedModel = PagedModel.of(
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

    @GetMapping("/search")
    public HttpEntity<PagedModel<AuthorView>> search(@Valid @NotBlank @RequestParam(name = "query") String query,
                                                     @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "size", defaultValue = "20") Integer size) throws NoSuchMethodException {
        Page<AuthorView> userPage = userService.searchUsers(query, page, size);
        PagedModel<AuthorView> pagedModel = PagedModel.of(
                userPage.getContent(),
                new PagedModel.PageMetadata(userPage.getSize(),
                        userPage.getNumber(),
                        userPage.getTotalElements(),
                        userPage.getTotalPages()));

        Method method = this.getClass().getMethod("search",
                String.class,
                Integer.class,
                Integer.class);

        if(page < userPage.getTotalPages()){
            Link link = linkTo(method, query, page + 1, size).withRel(LinkRelation.of("next"));
            pagedModel.add(link);
        }

        if(page > 0){
            Link link = linkTo(method, query, page - 1, size).withRel(LinkRelation.of("previous"));
            pagedModel.add(link);
        }
        return new  HttpEntity<>(pagedModel);
    }
}
