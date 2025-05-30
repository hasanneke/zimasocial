package com.zimaberlin.zimasocial.controller;

import com.zimaberlin.zimasocial.calculators.todayspost.TodaysPostGenerator;
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
    @GetMapping(path = "/todays-posts")
    public ResponseEntity<Void> triggerTodaysPosts() {
        todaysPostGenerator.createTodaysPost();
        return ResponseEntity.ok().build();
    }
}
