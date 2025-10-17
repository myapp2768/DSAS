package com.dsas.controller;

import com.dsas.service.DatabaseInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/init")
public class DataInitController {

    @Autowired
    private DatabaseInitService databaseInitService;

    @PostMapping("/agricultural-materials")
    public ResponseEntity<Map<String, Object>> initAgriculturalMaterials() {
        try {
            // 通过真正的数据库服务初始化数据
            databaseInitService.initRealData();
            
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "农资数据初始化成功（真实数据库操作）",
                "count", 10
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", "数据初始化失败: " + e.getMessage()
            ));
        }
    }
}
