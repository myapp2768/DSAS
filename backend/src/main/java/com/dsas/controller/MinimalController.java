package com.dsas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MinimalController {

    @GetMapping("/minimal")
    public String minimal() {
        return "Minimal Controller is working!";
    }
}