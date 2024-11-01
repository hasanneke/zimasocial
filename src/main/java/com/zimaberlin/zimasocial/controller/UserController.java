package com.zimaberlin.zimasocial.controller;

import com.zimaberlin.zimasocial.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "api/v1/users")
public interface UserController {
    @PatchMapping(path = "/me")
    ResponseEntity<User> updatedUserInfo();

    @PostMapping(path = "/me/upload-avatar")
    ResponseEntity<User> upload();

    @GetMapping(path = "/me")
    ResponseEntity<User> getLoggedInUser();

    @GetMapping(path = "/{userId}")
    ResponseEntity<User> getUser();
}
