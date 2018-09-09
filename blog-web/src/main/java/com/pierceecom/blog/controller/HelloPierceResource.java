package com.pierceecom.blog.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello-pierce")
public class HelloPierceResource {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String hello() {
        return "{\"message\":\"Hello Pierce\"}";
    }
}
