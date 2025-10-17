package com.dsas.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 认证控制器
 * 处理用户登录、登出等认证相关功能
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        logger.info("用户登录尝试: {}", username);
        
        // 简单的模拟登录验证（实际项目中应该连接数据库验证）
        if (isValidUser(username, password)) {
            String token = generateToken();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "登录成功");
            response.put("token", token);
            response.put("user", Map.of(
                "id", 1,
                "username", username,
                "realName", "管理员",
                "role", "ADMIN"
            ));
            
            logger.info("用户 {} 登录成功", username);
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "用户名或密码错误");
            
            logger.warn("用户 {} 登录失败", username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        logger.info("用户登出");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "登出成功");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !isValidToken(token)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "未登录或token无效");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("user", Map.of(
            "id", 1,
            "username", "admin",
            "realName", "管理员",
            "role", "ADMIN"
        ));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 验证token
     */
    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyToken(@RequestHeader(value = "Authorization", required = false) String token) {
        Map<String, Object> response = new HashMap<>();
        
        if (token != null && isValidToken(token)) {
            response.put("valid", true);
            response.put("message", "Token有效");
        } else {
            response.put("valid", false);
            response.put("message", "Token无效或已过期");
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 简单的用户验证（模拟）
     */
    private boolean isValidUser(String username, String password) {
        // 模拟用户验证
        return "admin".equals(username) && "admin123".equals(password);
    }
    
    /**
     * 生成token
     */
    private String generateToken() {
        return "token_" + UUID.randomUUID().toString().replace("-", "");
    }
    
    /**
     * 验证token
     */
    private boolean isValidToken(String token) {
        // 简单的token验证（实际项目中应该使用JWT等）
        return token != null && token.startsWith("token_");
    }
}





