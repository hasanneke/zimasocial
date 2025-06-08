package com.zimaberlin.zimasocial.context.social.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
     String  uploadProfileImage(MultipartFile file) throws IOException;
     String  uploadFile(MultipartFile file);
     void deleteFile(String key);
     byte[] getFile(String key);
}
