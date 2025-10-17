package com.dsas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库连接测试控制器
 */
@RestController
@RequestMapping("/api/database")
public class DatabaseTestController {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseTestController.class);
    
    @Autowired
    private DataSource dataSource;
    
    /**
     * 测试数据库连接
     */
    @GetMapping("/test-connection")
    public ResponseEntity<Map<String, Object>> testConnection() {
        Map<String, Object> result = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            result.put("success", true);
            result.put("message", "数据库连接成功");
            result.put("database", Map.of(
                "productName", metaData.getDatabaseProductName(),
                "productVersion", metaData.getDatabaseProductVersion(),
                "driverName", metaData.getDriverName(),
                "driverVersion", metaData.getDriverVersion(),
                "url", metaData.getURL(),
                "username", metaData.getUserName()
            ));
            
            logger.info("数据库连接测试成功: {}", metaData.getDatabaseProductName());
            
        } catch (SQLException e) {
            result.put("success", false);
            result.put("message", "数据库连接失败: " + e.getMessage());
            result.put("error", e.getSQLState());
            
            logger.error("数据库连接测试失败", e);
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取数据库信息
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getDatabaseInfo() {
        Map<String, Object> result = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            result.put("connectionValid", connection.isValid(5));
            result.put("autoCommit", connection.getAutoCommit());
            result.put("readOnly", connection.isReadOnly());
            result.put("catalog", connection.getCatalog());
            result.put("schema", connection.getSchema());
            
            Map<String, Object> dbInfo = new HashMap<>();
            dbInfo.put("productName", metaData.getDatabaseProductName());
            dbInfo.put("productVersion", metaData.getDatabaseProductVersion());
            dbInfo.put("driverName", metaData.getDriverName());
            dbInfo.put("driverVersion", metaData.getDriverVersion());
            dbInfo.put("url", metaData.getURL());
            dbInfo.put("username", metaData.getUserName());
            
            result.put("databaseInfo", dbInfo);
            
        } catch (SQLException e) {
            result.put("error", e.getMessage());
            logger.error("获取数据库信息失败", e);
        }
        
        return ResponseEntity.ok(result);
    }
}





