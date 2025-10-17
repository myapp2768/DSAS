package com.dsas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一的API测试控制器
 * 替代多个分散的测试控制器
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    /**
     * 健康检查端点
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "DSAS Backend");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    /**
     * 系统信息端点
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "DSAS - Digital Smart Agriculture System");
        response.put("version", "1.0.0");
        response.put("description", "农业数字化智能系统后端服务");
        response.put("java.version", System.getProperty("java.version"));
        response.put("spring.boot.version", org.springframework.boot.SpringBootVersion.getVersion());
        return ResponseEntity.ok(response);
    }

    /**
     * 简单的测试端点
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "DSAS API is working correctly!");
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }
}

