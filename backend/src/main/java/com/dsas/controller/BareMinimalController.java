package com.dsas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BareMinimalController {

    @GetMapping("/bare")
    public String bare() {
        return "Bare Minimal - Works!";
    }
}