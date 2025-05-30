package com.zimaberlin.zimasocial.service.report.exception;

import com.zimaberlin.zimasocial.exception.ConflictException;

public class ReportAlreadyMadeException extends ConflictException {
    public ReportAlreadyMadeException() {
        super("report_already_exists", "Report already made");
    }
}
