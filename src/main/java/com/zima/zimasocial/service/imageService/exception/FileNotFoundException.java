package com.zima.zimasocial.service.imageService.exception;

import com.zima.zimasocial.exception.DataNotFoundException;

public class FileNotFoundException extends DataNotFoundException {
    public FileNotFoundException() {
        super("file_not_found", "File not found");
    }
}
