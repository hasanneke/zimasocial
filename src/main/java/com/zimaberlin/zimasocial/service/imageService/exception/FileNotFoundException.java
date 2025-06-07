package com.zimaberlin.zimasocial.service.imageService.exception;

import com.zimaberlin.zimasocial.exception.DataNotFoundException;

public class FileNotFoundException extends DataNotFoundException {
    public FileNotFoundException() {
        super("file_not_found", "File not found");
    }
}
