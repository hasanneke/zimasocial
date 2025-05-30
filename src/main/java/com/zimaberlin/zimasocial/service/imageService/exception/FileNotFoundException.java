package com.zimaberlin.zimasocial.service.imageService.exception;

import com.zimaberlin.zimasocial.exception.ResourceNotFoundException;

public class FileNotFoundException extends ResourceNotFoundException {
    public FileNotFoundException() {
        super("file_not_found", "File not found");
    }
}
