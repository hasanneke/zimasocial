package com.zimaberlin.zimasocial.controller;

import com.zimaberlin.zimasocial.entity.postReport.PostReportReason;
import com.zimaberlin.zimasocial.entity.postReport.ReportedPostType;
import com.zimaberlin.zimasocial.service.postReport.PostReportService;
import com.zimaberlin.zimasocial.service.posts.PostService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/post-reports")
@RequiredArgsConstructor
@AllArgsConstructor
public class PostReportController {
    private PostReportService postReportService;
    @GetMapping("/{postId}/report")
    public ResponseEntity<Void> report(@PathVariable(name = "postId") Long postId,
                                       @RequestParam(name = "reasonId") Long reasonId,
                                       @RequestParam(name = "type") ReportedPostType type) {

        postReportService.reportPost(postId, reasonId, type);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
