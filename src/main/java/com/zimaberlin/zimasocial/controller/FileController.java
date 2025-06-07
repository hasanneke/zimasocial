package com.zimaberlin.zimasocial.controller;

import com.zimaberlin.zimasocial.context.social.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/files")
@RequiredArgsConstructor
public class FileController {
    private final ImageService imageService;
    @GetMapping(path = "/images/{fileName}")
    ResponseEntity<byte[]> getImage(@PathVariable(name = "fileName") String fileName) {
        return ResponseEntity.ok(imageService.getFile(fileName));
    }
}
