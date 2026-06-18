package com.zima.zimasocial.context.contentmoderation.report.exception;

import com.zima.zimasocial.shared.exception.BadRequestException;

public class UnknownResourceTypeException extends BadRequestException {
    public UnknownResourceTypeException() {
        super("unknown_resource_type", "Unknown resource type exception");
    }
}
