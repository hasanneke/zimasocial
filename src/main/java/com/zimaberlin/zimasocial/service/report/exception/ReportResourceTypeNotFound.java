package com.zimaberlin.zimasocial.service.report.exception;

import com.zimaberlin.zimasocial.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReportResourceTypeNotFound extends ResourceNotFoundException {
    public ReportResourceTypeNotFound() {
        super("Report resource not found");
    }
}
