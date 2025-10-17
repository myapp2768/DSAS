package com.dsas.controller;

import com.dsas.entity.*;
import com.dsas.service.InventoryService;
import com.dsas.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 库存管理控制器
 */
@RestController
@RequestMapping("/api/inventory")
@Validated
public class InventoryController {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);
    
    @Autowired
    private InventoryService inventoryService;
    
    // ==================== 库存管理 ====================
    
    /**
     * 获取所有库存
     */
    @GetMapping("/stocks")
    public ResponseEntity<List<Stock>> getAllStocks() {
        List<Stock> stocks = inventoryService.getAllStocks();
        return ResponseEntity.ok(stocks);
    }
    
    /**
     * 根据农资ID获取库存
     */
    @GetMapping("/stocks/material/{materialId}")
    public ResponseEntity<Stock> getStockByMaterialId(@PathVariable Long materialId) {
        try {
            Stock stock = inventoryService.getStockByMaterialId(materialId);
            return ResponseEntity.ok(stock);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 获取库存不足的农资
     */
    @GetMapping("/stocks/low-stock")
    public ResponseEntity<List<Stock>> getLowStockItems() {
        List<Stock> lowStockItems = inventoryService.getLowStockItems();
        return ResponseEntity.ok(lowStockItems);
    }
    
    /**
     * 获取库存过量的农资
     */
    @GetMapping("/stocks/over-stock")
    public ResponseEntity<List<Stock>> getOverStockItems() {
        List<Stock> overStockItems = inventoryService.getOverStockItems();
        return ResponseEntity.ok(overStockItems);
    }
    
    /**
     * 更新库存信息
     */
    @PutMapping("/stocks/{materialId}")
    public ResponseEntity<Stock> updateStock(@PathVariable Long materialId, @Valid @RequestBody Stock stock) {
        try {
            Stock existingStock = inventoryService.getStockByMaterialId(materialId);
            stock.setId(existingStock.getId());
            stock.setMaterial(existingStock.getMaterial());
            Stock updatedStock = inventoryService.updateStock(stock);
            return ResponseEntity.ok(updatedStock);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 设置安全库存
     */
    @PutMapping("/stocks/{materialId}/safety-stock")
    public ResponseEntity<Stock> setSafetyStock(@PathVariable Long materialId, @RequestParam BigDecimal safetyStock) {
        try {
            Stock stock = inventoryService.setSafetyStock(materialId, safetyStock);
            return ResponseEntity.ok(stock);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 设置最大库存
     */
    @PutMapping("/stocks/{materialId}/max-stock")
    public ResponseEntity<Stock> setMaxStock(@PathVariable Long materialId, @RequestParam BigDecimal maxStock) {
        try {
            Stock stock = inventoryService.setMaxStock(materialId, maxStock);
            return ResponseEntity.ok(stock);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // ==================== 入库管理 ====================
    
    /**
     * 创建入库单
     */
    @PostMapping("/stock-in")
    public ResponseEntity<StockInRecord> createStockInRecord(@Valid @RequestBody StockInRecord stockInRecord) {
        try {
            StockInRecord created = inventoryService.createStockInRecord(stockInRecord);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            logger.error("创建入库单失败", e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 完成入库
     */
    @PutMapping("/stock-in/{id}/complete")
    public ResponseEntity<StockInRecord> completeStockIn(@PathVariable Long id) {
        try {
            StockInRecord stockInRecord = inventoryService.completeStockIn(id);
            return ResponseEntity.ok(stockInRecord);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 取消入库
     */
    @PutMapping("/stock-in/{id}/cancel")
    public ResponseEntity<StockInRecord> cancelStockIn(@PathVariable Long id) {
        try {
            StockInRecord stockInRecord = inventoryService.cancelStockIn(id);
            return ResponseEntity.ok(stockInRecord);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 获取所有入库记录
     */
    @GetMapping("/stock-in")
    public ResponseEntity<List<StockInRecord>> getAllStockInRecords() {
        List<StockInRecord> records = inventoryService.getAllStockInRecords();
        return ResponseEntity.ok(records);
    }
    
    /**
     * 根据ID获取入库记录
     */
    @GetMapping("/stock-in/{id}")
    public ResponseEntity<StockInRecord> getStockInRecordById(@PathVariable Long id) {
        try {
            StockInRecord record = inventoryService.getStockInRecordById(id);
            return ResponseEntity.ok(record);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 根据农资ID获取入库记录
     */
    @GetMapping("/stock-in/material/{materialId}")
    public ResponseEntity<List<StockInRecord>> getStockInRecordsByMaterialId(@PathVariable Long materialId) {
        List<StockInRecord> records = inventoryService.getStockInRecordsByMaterialId(materialId);
        return ResponseEntity.ok(records);
    }
    
    /**
     * 根据状态获取入库记录
     */
    @GetMapping("/stock-in/status/{status}")
    public ResponseEntity<List<StockInRecord>> getStockInRecordsByStatus(@PathVariable StockInRecord.Status status) {
        List<StockInRecord> records = inventoryService.getStockInRecordsByStatus(status);
        return ResponseEntity.ok(records);
    }
    
    // ==================== 出库管理 ====================
    
    /**
     * 创建出库单
     */
    @PostMapping("/stock-out")
    public ResponseEntity<StockOutRecord> createStockOutRecord(@Valid @RequestBody StockOutRecord stockOutRecord) {
        try {
            StockOutRecord created = inventoryService.createStockOutRecord(stockOutRecord);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            logger.error("创建出库单失败", e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 完成出库
     */
    @PutMapping("/stock-out/{id}/complete")
    public ResponseEntity<StockOutRecord> completeStockOut(@PathVariable Long id) {
        try {
            StockOutRecord stockOutRecord = inventoryService.completeStockOut(id);
            return ResponseEntity.ok(stockOutRecord);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 取消出库
     */
    @PutMapping("/stock-out/{id}/cancel")
    public ResponseEntity<StockOutRecord> cancelStockOut(@PathVariable Long id) {
        try {
            StockOutRecord stockOutRecord = inventoryService.cancelStockOut(id);
            return ResponseEntity.ok(stockOutRecord);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 获取所有出库记录
     */
    @GetMapping("/stock-out")
    public ResponseEntity<List<StockOutRecord>> getAllStockOutRecords() {
        List<StockOutRecord> records = inventoryService.getAllStockOutRecords();
        return ResponseEntity.ok(records);
    }
    
    /**
     * 根据ID获取出库记录
     */
    @GetMapping("/stock-out/{id}")
    public ResponseEntity<StockOutRecord> getStockOutRecordById(@PathVariable Long id) {
        try {
            StockOutRecord record = inventoryService.getStockOutRecordById(id);
            return ResponseEntity.ok(record);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 根据农资ID获取出库记录
     */
    @GetMapping("/stock-out/material/{materialId}")
    public ResponseEntity<List<StockOutRecord>> getStockOutRecordsByMaterialId(@PathVariable Long materialId) {
        List<StockOutRecord> records = inventoryService.getStockOutRecordsByMaterialId(materialId);
        return ResponseEntity.ok(records);
    }
    
    /**
     * 根据状态获取出库记录
     */
    @GetMapping("/stock-out/status/{status}")
    public ResponseEntity<List<StockOutRecord>> getStockOutRecordsByStatus(@PathVariable StockOutRecord.Status status) {
        List<StockOutRecord> records = inventoryService.getStockOutRecordsByStatus(status);
        return ResponseEntity.ok(records);
    }
    
    // ==================== 库存计算 ====================
    
    /**
     * 计算农资当前库存
     */
    @GetMapping("/stocks/material/{materialId}/current")
    public ResponseEntity<Map<String, Object>> getCurrentStock(@PathVariable Long materialId) {
        try {
            BigDecimal currentStock = inventoryService.calculateCurrentStock(materialId);
            BigDecimal availableStock = inventoryService.calculateAvailableStock(materialId);
            
            Map<String, Object> result = Map.of(
                "materialId", materialId,
                "currentStock", currentStock,
                "availableStock", availableStock
            );
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("计算库存失败", e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 更新农资库存
     */
    @PutMapping("/stocks/material/{materialId}/update")
    public ResponseEntity<Map<String, String>> updateMaterialStock(@PathVariable Long materialId) {
        try {
            inventoryService.updateMaterialStock(materialId);
            return ResponseEntity.ok(Map.of("message", "库存更新成功"));
        } catch (Exception e) {
            logger.error("更新库存失败", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * 批量更新所有农资库存
     */
    @PutMapping("/stocks/update-all")
    public ResponseEntity<Map<String, String>> updateAllMaterialStocks() {
        try {
            inventoryService.updateAllMaterialStocks();
            return ResponseEntity.ok(Map.of("message", "所有库存更新成功"));
        } catch (Exception e) {
            logger.error("批量更新库存失败", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // ==================== 库存记录 ====================
    
    /**
     * 获取农资的库存变动记录
     */
    @GetMapping("/records/material/{materialId}")
    public ResponseEntity<List<InventoryRecord>> getInventoryRecordsByMaterialId(@PathVariable Long materialId) {
        List<InventoryRecord> records = inventoryService.getInventoryRecordsByMaterialId(materialId);
        return ResponseEntity.ok(records);
    }
    
    /**
     * 获取指定时间范围的库存记录
     */
    @GetMapping("/records")
    public ResponseEntity<List<InventoryRecord>> getInventoryRecordsByTimeRange(
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        List<InventoryRecord> records = inventoryService.getInventoryRecordsByTimeRange(startTime, endTime);
        return ResponseEntity.ok(records);
    }
    
    // ==================== 统计报表 ====================
    
    /**
     * 获取库存统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStockStatistics() {
        Map<String, Object> statistics = inventoryService.getStockStatistics();
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * 获取农资库存统计
     */
    @GetMapping("/statistics/material/{materialId}")
    public ResponseEntity<Map<String, Object>> getMaterialStockStatistics(@PathVariable Long materialId) {
        Map<String, Object> statistics = inventoryService.getMaterialStockStatistics(materialId);
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * 获取库存预警信息
     */
    @GetMapping("/alerts")
    public ResponseEntity<Map<String, Object>> getStockAlerts() {
        Map<String, Object> alerts = inventoryService.getStockAlerts();
        return ResponseEntity.ok(alerts);
    }
    
    /**
     * 生成库存报表
     */
    @GetMapping("/reports")
    public ResponseEntity<Map<String, Object>> generateStockReport(
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        Map<String, Object> report = inventoryService.generateStockReport(startTime, endTime);
        return ResponseEntity.ok(report);
    }
}





