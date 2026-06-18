package com.zima.zimasocial.context.social.image.exception;

import com.zima.zimasocial.shared.exception.DataNotFoundException;

public class FileNotFoundException extends DataNotFoundException {
    public FileNotFoundException() {
        super("file_not_found", "File not found");
    }
}
