package com.zima.zimasocial.context.contentmoderation.exception;

import com.zima.zimasocial.shared.exception.DataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReportDataTypeNotFound extends DataNotFoundException {
    public ReportDataTypeNotFound() {
        super("Report resource not found");
    }
}
