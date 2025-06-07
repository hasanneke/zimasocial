package com.zimaberlin.zimasocial.service.report.exception;

import com.zimaberlin.zimasocial.exception.DataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReportDataTypeNotFound extends DataNotFoundException {
    public ReportDataTypeNotFound() {
        super("Report resource not found");
    }
}
