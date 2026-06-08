package com.zima.zimasocial.context.contentmoderation.api;

import com.zima.zimasocial.context.contentmoderation.report.ReportService;
import com.zima.zimasocial.context.contentmoderation.api.payload.ReportRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v2/reports")
public class ReportsController {
    private final ReportService reportService;
    @Autowired
    public ReportsController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping(path = "/posts")
    public ResponseEntity<Void> reportPost(@RequestBody ReportRequest request) {
        reportService.reportPost(request.getResourceId(), request.getReason(), request.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(path = "/comments")
    public ResponseEntity<Void> reportComment(@RequestBody ReportRequest request) {
        reportService.reportComment(request.getResourceId(), request.getReason(), request.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
