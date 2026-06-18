package com.zima.zimasocial.context.contentmoderation.report.exception;

import com.zima.zimasocial.shared.exception.ConflictException;

public class ReportAlreadyMadeException extends ConflictException {
    public ReportAlreadyMadeException() {
        super("report_already_exists", "Report already made");
    }
}
