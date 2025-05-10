package com.zimaberlin.zimasocial.controller;

import com.zimaberlin.zimasocial.entity.report.ReportReason;
import com.zimaberlin.zimasocial.entity.report.ReportType;
import com.zimaberlin.zimasocial.service.report.ReportService;
import com.zimaberlin.zimasocial.service.report.dto.ReportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/post-reports")
public class ReportController {
    private ReportService reportService;
    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<Void> report(@RequestBody ReportRequest request) {
        reportService.report(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
