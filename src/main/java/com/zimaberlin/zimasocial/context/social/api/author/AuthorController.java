package com.zimaberlin.zimasocial.context.social.api.author;

import com.zimaberlin.zimasocial.context.social.author.Author;
import com.zimaberlin.zimasocial.context.social.author.AuthorRepository;
import com.zimaberlin.zimasocial.context.social.author.AuthorService;
import com.zimaberlin.zimasocial.context.social.author.SlugAlreadyTakenException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/authors")
public class AuthorController {
    private final AuthorControllerBridge authorControllerBridge;
    private final AuthorService authorService;
    private final AuthorRepository authorRepository;
    private final AuthorAuthorViewAdapter authorAuthorViewMapper;

    @Autowired
    public AuthorController(AuthorControllerBridge authorControllerBridge, AuthorService authorService, AuthorRepository authorRepository, AuthorAuthorViewAdapter authorViewAdapter) {
        this.authorControllerBridge = authorControllerBridge;
        this.authorService = authorService;
        this.authorRepository = authorRepository;
        this.authorAuthorViewMapper = authorViewAdapter;
    }

    @GetMapping(path = "/me")
    public ResponseEntity<DetailedAuthorView> getMe(){
        DetailedAuthorView userView = authorControllerBridge.getMe();
        return ResponseEntity.ok(userView);
    }

    @GetMapping(path = "/{slug}")
    public ResponseEntity<AuthorView> getUser(@PathVariable(name = "slug") String slug){
        AuthorView authorView = authorControllerBridge.getAuthor(slug);
        return ResponseEntity.ok(authorView);
    }

    @PatchMapping(path = "/me/upload-image")
    public ResponseEntity<AuthorView> uploadProfileImage(MultipartFile image) throws IOException {
        Author author = authorService.updateProfileImage(image);
        return ResponseEntity.ok(authorAuthorViewMapper.authorViewFromAuthor(author));
    }

    @DeleteMapping(path = "/me/remove-profile")
    public ResponseEntity<Void> deleteProfileImage() {
        authorService.removeMyProfileImage();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/update-slug")
    public ResponseEntity<AuthorView> updateSlug(@Valid @NotBlank @Size(max= 128)  @RequestParam(name = "slug") String slug){
        authorService.updateSlug(slug);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/me/update-bio")
    public ResponseEntity<AuthorView> updateBio(@Valid @NotBlank @Size(max= 128) @RequestParam(name = "bio") String bio){
        authorService.updateBio(bio);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/me/update-name")
    public ResponseEntity<AuthorView> updateName(@Valid @NotBlank @Size(max= 128)  @RequestParam(name = "name") String name){
        authorService.updateName(name);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "/check-username-exists", method = RequestMethod.HEAD)
    public ResponseEntity<Boolean> checkUsernameExists(@RequestParam(name = "slug") String slug){
        Optional<Author> author = authorRepository.findBySlugAndIsDisabledFalse(slug);
        if(author.isPresent()){
            throw new SlugAlreadyTakenException(slug);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{slug}/follow")
    public ResponseEntity<Void> followUser(@PathVariable(name = "slug") String slug) {
        authorService.follow(slug);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{slug}/unfollow")
    public ResponseEntity<Void> unfollowUser(@PathVariable(name = "slug") String slug) {
        authorService.unfollow(slug);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{slug}/block")
    public ResponseEntity<Void> blockUser(@PathVariable(name = "slug") String slug) {
        authorService.block(slug);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{slug}/unblock")
    public ResponseEntity<Void> unblockUser(@PathVariable(name = "slug") String slug) {
        authorService.unblock(slug);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{slug}/followers")
    public HttpEntity<PagedModel<AuthorView>> getFollowers(
            @PathVariable(name = "slug") String slug,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size) throws NoSuchMethodException {
        return new  HttpEntity<>(authorControllerBridge.getFollowers(slug, page, size));
    }

    @DeleteMapping(path = "/{slug}/followers/{targetSlug}")
    public ResponseEntity<Void> removeFollower(
            @PathVariable(name = "slug") String slug,
            @PathVariable(name = "targetSlug") String targetSlug) {
        authorService.removeFollower(targetSlug);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{slug}/followings")
    public HttpEntity<PagedModel<AuthorView>> getFollowing(
            @PathVariable(name = "slug") String slug,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size) throws NoSuchMethodException {
        return new HttpEntity<>(authorControllerBridge.getFollowings(slug, page, size));
    }

    @GetMapping(path = "/blocks")
    public HttpEntity<PagedModel<AuthorView>> getBlocks(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size) throws NoSuchMethodException {
        return new HttpEntity<>(authorControllerBridge.getBlocks(page, size));
    }

    @GetMapping("/search")
    public HttpEntity<PagedModel<AuthorView>> search(@Valid @NotBlank @RequestParam(name = "query") String query,
                                                     @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "size", defaultValue = "20") Integer size) throws NoSuchMethodException {
        return new HttpEntity<>(authorControllerBridge.searchAuthors(query, page, size));
    }
    @GetMapping("/{slug}/follow-requests")
    public ResponseEntity<List<FollowRequestDTO>> getAllFollowRequests() {
        return ResponseEntity.ok(authorControllerBridge.getAllFollowRequests());
    }

    @PostMapping("/{slug}/follow-requests")
    public ResponseEntity<Void> requestToFollow(@PathVariable(name = "slug") String slug) {
        authorService.requestToFollow(slug);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{slug}/follow-requests/{followerSlug}/accept")
    public ResponseEntity<Void> acceptFollowRequest(@PathVariable(name = "followerSlug") String followerSlug) {
        authorControllerBridge.acceptFollowRequest(followerSlug);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{followedSlug}/follow-requests/{followerSlug}")
    public ResponseEntity<Void> deleteFollowRequest(@PathVariable(name = "followedSlug") String followedAuthorSlug, @PathVariable(name = "followerSlug") String followerAuthorSlug) {
        authorService.deleteFollowRequest(followedAuthorSlug, followerAuthorSlug);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{slug}/follow-requests-info")
    public ResponseEntity<FollowRequestsInfo> getFollowRequestInfo(@PathVariable(name = "slug") String followedAuthorSlug) {
        return ResponseEntity.ok(authorControllerBridge.getFollowRequestInfo(followedAuthorSlug));
    }
}
