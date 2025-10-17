package com.dsas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/materials-test")
public class MaterialTestController {

    @GetMapping("/hello")
    public String hello() {
        return "Material Test Controller is working!";
    }

    @GetMapping
    public String getAll() {
        return "Get all materials test";
    }

    @GetMapping("/{id}")
    public String getById() {
        return "Get material by ID test";
    }
}