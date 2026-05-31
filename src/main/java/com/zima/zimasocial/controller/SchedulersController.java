package com.zima.zimasocial.controller;

import com.zima.zimasocial.context.social.author.abstracted.TodaysPostGenerator;
import com.zima.zimasocial.context.social.post.application.PostScorePunisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/schedulers")
@RequiredArgsConstructor
public class SchedulersController {
    final private TodaysPostGenerator todaysPostGenerator;
    private final PostScorePunisherService postScorePunisherService;
    @GetMapping(path = "/todays-posts")
    public ResponseEntity<Void> triggerTodaysPosts() {
        todaysPostGenerator.createTodaysPost();
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/post-score-punisher")
    public ResponseEntity<Void> triggerPostScorePunisher(){
        postScorePunisherService.punishPosts();
        return ResponseEntity.noContent().build();
    }
}
