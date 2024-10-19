package com.zimaberlin.zimasocial.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/hello")
public class HomeController {
    @GetMapping
    String hello(){
        return "Start of deployment";
    }
}
