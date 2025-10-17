package com.dsas.controller;

import com.dsas.entity.*;
import com.dsas.service.InventoryService;
import com.dsas.service.AgriculturalMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 库存管理测试控制器
 * 用于测试库存管理功能
 */
@RestController
@RequestMapping("/api/inventory-test")
public class InventoryTestController {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryTestController.class);
    
    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private AgriculturalMaterialService agriculturalMaterialService;
    
    /**
     * 测试库存管理功能
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testInventoryManagement() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. 测试获取所有库存
            result.put("allStocks", inventoryService.getAllStocks().size());
            
            // 2. 测试获取库存统计
            result.put("stockStatistics", inventoryService.getStockStatistics());
            
            // 3. 测试获取库存预警
            result.put("stockAlerts", inventoryService.getStockAlerts());
            
            // 4. 测试获取所有农资
            result.put("allMaterials", agriculturalMaterialService.getAllAgriculturalMaterials().size());
            
            result.put("status", "success");
            result.put("message", "库存管理功能测试成功");
            
        } catch (Exception e) {
            logger.error("库存管理功能测试失败", e);
            result.put("status", "error");
            result.put("message", "库存管理功能测试失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 创建测试数据
     */
    @PostMapping("/create-test-data")
    public ResponseEntity<Map<String, Object>> createTestData() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 创建测试农资
            AgriculturalMaterial material = new AgriculturalMaterial();
            material.setInternalCode("TEST-001");
            material.setCategory("测试肥料");
            material.setName("测试肥料001");
            material.setBrand("测试品牌");
            material.setSpecification("10kg/包");
            material.setUnit("kg");
            material.setContent(new BigDecimal("10"));
            material.setUnitPrice(new BigDecimal("100.00"));
            material.setPricePerUnit(new BigDecimal("10.00"));
            material.setRemark("测试用农资");
            
            AgriculturalMaterial savedMaterial = agriculturalMaterialService.createAgriculturalMaterial(material);
            result.put("createdMaterial", savedMaterial);
            
            // 创建测试入库单
            StockInRecord stockInRecord = new StockInRecord();
            stockInRecord.setMaterial(savedMaterial);
            stockInRecord.setQuantity(new BigDecimal("100"));
            stockInRecord.setUnitPrice(new BigDecimal("100.00"));
            stockInRecord.setSupplierName("测试供应商");
            stockInRecord.setOperatorName("测试操作员");
            stockInRecord.setInboundReason("测试入库");
            stockInRecord.setQualityStatus("GOOD");
            stockInRecord.setStorageLocation("A区1号货架");
            
            StockInRecord savedStockIn = inventoryService.createStockInRecord(stockInRecord);
            result.put("createdStockIn", savedStockIn);
            
            // 完成入库
            StockInRecord completedStockIn = inventoryService.completeStockIn(savedStockIn.getId());
            result.put("completedStockIn", completedStockIn);
            
            // 创建测试出库单
            StockOutRecord stockOutRecord = new StockOutRecord();
            stockOutRecord.setMaterial(savedMaterial);
            stockOutRecord.setQuantity(new BigDecimal("20"));
            stockOutRecord.setUnitPrice(new BigDecimal("100.00"));
            stockOutRecord.setCustomerName("测试客户");
            stockOutRecord.setOperatorName("测试操作员");
            stockOutRecord.setOutboundReason("测试出库");
            stockOutRecord.setOutboundType(StockOutRecord.OutboundType.SALE);
            stockOutRecord.setStorageLocation("A区1号货架");
            
            StockOutRecord savedStockOut = inventoryService.createStockOutRecord(stockOutRecord);
            result.put("createdStockOut", savedStockOut);
            
            // 完成出库
            StockOutRecord completedStockOut = inventoryService.completeStockOut(savedStockOut.getId());
            result.put("completedStockOut", completedStockOut);
            
            // 获取库存信息
            BigDecimal currentStock = inventoryService.calculateCurrentStock(savedMaterial.getId());
            result.put("currentStock", currentStock);
            
            // 获取库存记录
            result.put("inventoryRecords", inventoryService.getInventoryRecordsByMaterialId(savedMaterial.getId()));
            
            result.put("status", "success");
            result.put("message", "测试数据创建成功");
            
        } catch (Exception e) {
            logger.error("创建测试数据失败", e);
            result.put("status", "error");
            result.put("message", "创建测试数据失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 测试库存计算
     */
    @GetMapping("/test-calculation/{materialId}")
    public ResponseEntity<Map<String, Object>> testStockCalculation(@PathVariable Long materialId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 计算当前库存
            BigDecimal currentStock = inventoryService.calculateCurrentStock(materialId);
            result.put("currentStock", currentStock);
            
            // 计算可用库存
            BigDecimal availableStock = inventoryService.calculateAvailableStock(materialId);
            result.put("availableStock", availableStock);
            
            // 获取入库总量
            BigDecimal totalInbound = inventoryService.getTotalInboundQuantity(materialId);
            result.put("totalInbound", totalInbound);
            
            // 获取出库总量
            BigDecimal totalOutbound = inventoryService.getTotalOutboundQuantity(materialId);
            result.put("totalOutbound", totalOutbound);
            
            // 获取库存记录
            result.put("inventoryRecords", inventoryService.getInventoryRecordsByMaterialId(materialId));
            
            // 获取农资库存统计
            result.put("materialStatistics", inventoryService.getMaterialStockStatistics(materialId));
            
            result.put("status", "success");
            result.put("message", "库存计算测试成功");
            
        } catch (Exception e) {
            logger.error("库存计算测试失败", e);
            result.put("status", "error");
            result.put("message", "库存计算测试失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 测试库存预警
     */
    @GetMapping("/test-alerts")
    public ResponseEntity<Map<String, Object>> testStockAlerts() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取库存预警
            Map<String, Object> alerts = inventoryService.getStockAlerts();
            result.put("alerts", alerts);
            
            // 获取库存统计
            Map<String, Object> statistics = inventoryService.getStockStatistics();
            result.put("statistics", statistics);
            
            result.put("status", "success");
            result.put("message", "库存预警测试成功");
            
        } catch (Exception e) {
            logger.error("库存预警测试失败", e);
            result.put("status", "error");
            result.put("message", "库存预警测试失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
}





