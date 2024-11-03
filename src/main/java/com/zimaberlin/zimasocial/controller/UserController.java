package com.zimaberlin.zimasocial.controller;
import com.zimaberlin.zimasocial.service.Users.Payload.UserUpdatePayload;
import com.zimaberlin.zimasocial.service.Users.UserService;
import com.zimaberlin.zimasocial.views.user.BasicUserView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    ResponseEntity<BasicUserView> getMe(){
        BasicUserView basicUserView = userService.getUserMe();
        return ResponseEntity.ok(basicUserView);
    }

    @GetMapping(path = "/{slug}")
    ResponseEntity<BasicUserView> getUser(@PathVariable(name = "slug") String slug){
        BasicUserView basicUserView = userService.getUser(slug);
        return ResponseEntity.ok(basicUserView);
    }

    @PatchMapping(path = "/me")
    ResponseEntity<BasicUserView> updateUser(@RequestBody UserUpdatePayload payload){
        BasicUserView basicUserView = userService.updateUser(payload);
        return ResponseEntity.ok(basicUserView);
    }

    @PatchMapping(path = "/me/upload-image")
    ResponseEntity<BasicUserView> uploadProfileImage(MultipartFile image){
        BasicUserView user = userService.uploadImage(image);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/me/change-username")
    ResponseEntity<BasicUserView> changeUsername(@NotBlank @RequestParam(name = "slug") String slug){
        BasicUserView userView = userService.updateUsername(slug);
        return ResponseEntity.ok(userView);
    }

    @RequestMapping(path = "/check-username-exists", method = RequestMethod.HEAD)
    ResponseEntity<Boolean> checkUsernameExists(@NotBlank @RequestParam(name = "slug") String slug){
        boolean usernameExists = userService.checkUsernameExists(slug);
        return ResponseEntity.ok(usernameExists);
    }
}
