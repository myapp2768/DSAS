package com.dsas.service;

import com.dsas.entity.*;
import com.dsas.exception.ResourceNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 库存管理服务接口
 */
public interface InventoryService {
    
    // ==================== 库存管理 ====================
    
    /**
     * 获取所有库存
     */
    List<Stock> getAllStocks();
    
    /**
     * 根据农资ID获取库存
     */
    Stock getStockByMaterialId(Long materialId);
    
    /**
     * 获取库存不足的农资
     */
    List<Stock> getLowStockItems();
    
    /**
     * 获取库存过量的农资
     */
    List<Stock> getOverStockItems();
    
    /**
     * 更新库存信息
     */
    Stock updateStock(Stock stock);
    
    /**
     * 设置安全库存
     */
    Stock setSafetyStock(Long materialId, BigDecimal safetyStock);
    
    /**
     * 设置最大库存
     */
    Stock setMaxStock(Long materialId, BigDecimal maxStock);
    
    // ==================== 入库管理 ====================
    
    /**
     * 创建入库单
     */
    StockInRecord createStockInRecord(StockInRecord stockInRecord);
    
    /**
     * 完成入库
     */
    StockInRecord completeStockIn(Long stockInId);
    
    /**
     * 取消入库
     */
    StockInRecord cancelStockIn(Long stockInId);
    
    /**
     * 获取所有入库记录
     */
    List<StockInRecord> getAllStockInRecords();
    
    /**
     * 根据ID获取入库记录
     */
    StockInRecord getStockInRecordById(Long id);
    
    /**
     * 根据农资ID获取入库记录
     */
    List<StockInRecord> getStockInRecordsByMaterialId(Long materialId);
    
    /**
     * 根据状态获取入库记录
     */
    List<StockInRecord> getStockInRecordsByStatus(StockInRecord.Status status);
    
    // ==================== 出库管理 ====================
    
    /**
     * 创建出库单
     */
    StockOutRecord createStockOutRecord(StockOutRecord stockOutRecord);
    
    /**
     * 完成出库
     */
    StockOutRecord completeStockOut(Long stockOutId);
    
    /**
     * 取消出库
     */
    StockOutRecord cancelStockOut(Long stockOutId);
    
    /**
     * 获取所有出库记录
     */
    List<StockOutRecord> getAllStockOutRecords();
    
    /**
     * 根据ID获取出库记录
     */
    StockOutRecord getStockOutRecordById(Long id);
    
    /**
     * 根据农资ID获取出库记录
     */
    List<StockOutRecord> getStockOutRecordsByMaterialId(Long materialId);
    
    /**
     * 根据状态获取出库记录
     */
    List<StockOutRecord> getStockOutRecordsByStatus(StockOutRecord.Status status);
    
    // ==================== 库存计算 ====================
    
    /**
     * 计算农资当前库存
     */
    BigDecimal calculateCurrentStock(Long materialId);
    
    /**
     * 计算农资可用库存
     */
    BigDecimal calculateAvailableStock(Long materialId);
    
    /**
     * 更新农资库存
     */
    void updateMaterialStock(Long materialId);
    
    /**
     * 批量更新所有农资库存
     */
    void updateAllMaterialStocks();
    
    // ==================== 库存记录 ====================
    
    /**
     * 获取农资的库存变动记录
     */
    List<InventoryRecord> getInventoryRecordsByMaterialId(Long materialId);
    
    /**
     * 获取指定时间范围的库存记录
     */
    List<InventoryRecord> getInventoryRecordsByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 获取农资的入库总量
     */
    BigDecimal getTotalInboundQuantity(Long materialId);
    
    /**
     * 获取农资的出库总量
     */
    BigDecimal getTotalOutboundQuantity(Long materialId);
    
    // ==================== 统计报表 ====================
    
    /**
     * 获取库存统计信息
     */
    Map<String, Object> getStockStatistics();
    
    /**
     * 获取农资库存统计
     */
    Map<String, Object> getMaterialStockStatistics(Long materialId);
    
    /**
     * 获取库存预警信息
     */
    Map<String, Object> getStockAlerts();
    
    /**
     * 生成库存报表
     */
    Map<String, Object> generateStockReport(LocalDateTime startTime, LocalDateTime endTime);
}





