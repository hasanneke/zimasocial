package com.zimaberlin.zimasocial.context.contentmoderation.report.exception;

import com.zimaberlin.zimasocial.exception.BadRequestException;

public class UnknownResourceTypeException extends BadRequestException {
    public UnknownResourceTypeException() {
        super("unknown_resource_type", "Unknown resource type exception");
    }
}
