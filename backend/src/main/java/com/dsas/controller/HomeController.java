package com.dsas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 首页控制器
 * 处理根路径访问
 */
@Controller
public class HomeController {
    
    /**
     * 首页 - 重定向到API文档
     */
    @GetMapping("/")
    @ResponseBody
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "欢迎使用DSAS数字化智慧农业系统");
        response.put("version", "1.0.0");
        response.put("description", "农业数字化智能系统后端服务");
        response.put("api_docs", "http://localhost:8080/api/health");
        response.put("h2_console", "http://localhost:8080/h2-console");
        response.put("frontend", "http://localhost:3000");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("健康检查", "/api/health");
        endpoints.put("系统信息", "/api/info");
        endpoints.put("农资管理", "/api/agricultural-materials");
        endpoints.put("库存管理", "/api/inventory/stocks");
        endpoints.put("库存统计", "/api/inventory/statistics");
        endpoints.put("库存预警", "/api/inventory/alerts");
        
        response.put("available_endpoints", endpoints);
        
        return response;
    }
    
    /**
     * 错误页面处理
     */
    @GetMapping("/error")
    @ResponseBody
    public Map<String, Object> error() {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "页面未找到");
        response.put("message", "请检查URL是否正确");
        response.put("available_endpoints", Map.of(
            "API文档", "/api/health",
            "前端应用", "http://localhost:3000"
        ));
        return response;
    }
}





