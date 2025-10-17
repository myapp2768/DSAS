package com.dsas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleTestController {

    @GetMapping("/api/simple")
    public String simple() {
        return "Simple Test - Works!";
    }
}