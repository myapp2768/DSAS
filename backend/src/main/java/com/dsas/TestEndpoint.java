package com.dsas;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestEndpoint {

    @GetMapping("/test-endpoint")
    public String test() {
        return "Test Endpoint is working!";
    }
}