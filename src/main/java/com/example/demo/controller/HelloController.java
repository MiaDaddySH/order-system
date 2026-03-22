package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.HelloResponse;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public HelloResponse hello(@RequestParam String name) {
        return new HelloResponse("Hello, " + name);
    }
}
