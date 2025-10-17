package com.dsas.service.impl;

import com.dsas.entity.*;
import com.dsas.repository.*;
import com.dsas.service.InventoryService;
import com.dsas.service.AgriculturalMaterialService;
import com.dsas.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 库存管理服务实现类
 */
@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {
    
    @Autowired
    private StockRepository stockRepository;
    
    @Autowired
    private InventoryRecordRepository inventoryRecordRepository;
    
    @Autowired
    private StockInRecordRepository stockInRecordRepository;
    
    @Autowired
    private StockOutRecordRepository stockOutRecordRepository;
    
    @Autowired
    private AgriculturalMaterialService agriculturalMaterialService;
    
    // ==================== 库存管理 ====================
    
    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }
    
    @Override
    public Stock getStockByMaterialId(Long materialId) {
        return stockRepository.findByMaterialId(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("库存不存在，农资ID: " + materialId));
    }
    
    @Override
    public List<Stock> getLowStockItems() {
        return stockRepository.findLowStockItems();
    }
    
    @Override
    public List<Stock> getOverStockItems() {
        return stockRepository.findOverStockItems();
    }
    
    @Override
    public Stock updateStock(Stock stock) {
        return stockRepository.save(stock);
    }
    
    @Override
    public Stock setSafetyStock(Long materialId, BigDecimal safetyStock) {
        Stock stock = getStockByMaterialId(materialId);
        stock.setSafetyStock(safetyStock);
        return stockRepository.save(stock);
    }
    
    @Override
    public Stock setMaxStock(Long materialId, BigDecimal maxStock) {
        Stock stock = getStockByMaterialId(materialId);
        stock.setMaxStock(maxStock);
        return stockRepository.save(stock);
    }
    
    // ==================== 入库管理 ====================
    
    @Override
    public StockInRecord createStockInRecord(StockInRecord stockInRecord) {
        // 生成入库单号
        if (stockInRecord.getInboundNumber() == null || stockInRecord.getInboundNumber().isEmpty()) {
            stockInRecord.setInboundNumber(generateInboundNumber());
        }
        
        // 计算总金额
        stockInRecord.calculateTotalAmount();
        
        return stockInRecordRepository.save(stockInRecord);
    }
    
    @Override
    public StockInRecord completeStockIn(Long stockInId) {
        StockInRecord stockInRecord = stockInRecordRepository.findById(stockInId)
                .orElseThrow(() -> new ResourceNotFoundException("入库记录不存在，ID: " + stockInId));
        
        if (stockInRecord.getStatus() != StockInRecord.Status.PENDING) {
            throw new IllegalStateException("只能完成待处理的入库记录");
        }
        
        // 完成入库
        stockInRecord.complete();
        
        // 更新库存
        updateStockAfterInbound(stockInRecord);
        
        // 创建库存记录
        createInventoryRecord(stockInRecord);
        
        return stockInRecordRepository.save(stockInRecord);
    }
    
    @Override
    public StockInRecord cancelStockIn(Long stockInId) {
        StockInRecord stockInRecord = stockInRecordRepository.findById(stockInId)
                .orElseThrow(() -> new ResourceNotFoundException("入库记录不存在，ID: " + stockInId));
        
        if (stockInRecord.getStatus() != StockInRecord.Status.PENDING) {
            throw new IllegalStateException("只能取消待处理的入库记录");
        }
        
        stockInRecord.cancel();
        return stockInRecordRepository.save(stockInRecord);
    }
    
    @Override
    public List<StockInRecord> getAllStockInRecords() {
        return stockInRecordRepository.findAll();
    }
    
    @Override
    public StockInRecord getStockInRecordById(Long id) {
        return stockInRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("入库记录不存在，ID: " + id));
    }
    
    @Override
    public List<StockInRecord> getStockInRecordsByMaterialId(Long materialId) {
        return stockInRecordRepository.findByMaterialIdOrderByCreatedTimeDesc(materialId);
    }
    
    @Override
    public List<StockInRecord> getStockInRecordsByStatus(StockInRecord.Status status) {
        return stockInRecordRepository.findByStatusOrderByCreatedTimeDesc(status);
    }
    
    // ==================== 出库管理 ====================
    
    @Override
    public StockOutRecord createStockOutRecord(StockOutRecord stockOutRecord) {
        // 生成出库单号
        if (stockOutRecord.getOutboundNumber() == null || stockOutRecord.getOutboundNumber().isEmpty()) {
            stockOutRecord.setOutboundNumber(generateOutboundNumber());
        }
        
        // 计算总金额
        stockOutRecord.calculateTotalAmount();
        
        // 检查库存是否充足
        checkStockAvailability(stockOutRecord.getMaterial().getId(), stockOutRecord.getQuantity());
        
        return stockOutRecordRepository.save(stockOutRecord);
    }
    
    @Override
    public StockOutRecord completeStockOut(Long stockOutId) {
        StockOutRecord stockOutRecord = stockOutRecordRepository.findById(stockOutId)
                .orElseThrow(() -> new ResourceNotFoundException("出库记录不存在，ID: " + stockOutId));
        
        if (stockOutRecord.getStatus() != StockOutRecord.Status.PENDING) {
            throw new IllegalStateException("只能完成待处理的出库记录");
        }
        
        // 再次检查库存
        checkStockAvailability(stockOutRecord.getMaterial().getId(), stockOutRecord.getQuantity());
        
        // 完成出库
        stockOutRecord.complete();
        
        // 更新库存
        updateStockAfterOutbound(stockOutRecord);
        
        // 创建库存记录
        createInventoryRecord(stockOutRecord);
        
        return stockOutRecordRepository.save(stockOutRecord);
    }
    
    @Override
    public StockOutRecord cancelStockOut(Long stockOutId) {
        StockOutRecord stockOutRecord = stockOutRecordRepository.findById(stockOutId)
                .orElseThrow(() -> new ResourceNotFoundException("出库记录不存在，ID: " + stockOutId));
        
        if (stockOutRecord.getStatus() != StockOutRecord.Status.PENDING) {
            throw new IllegalStateException("只能取消待处理的出库记录");
        }
        
        stockOutRecord.cancel();
        return stockOutRecordRepository.save(stockOutRecord);
    }
    
    @Override
    public List<StockOutRecord> getAllStockOutRecords() {
        return stockOutRecordRepository.findAll();
    }
    
    @Override
    public StockOutRecord getStockOutRecordById(Long id) {
        return stockOutRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("出库记录不存在，ID: " + id));
    }
    
    @Override
    public List<StockOutRecord> getStockOutRecordsByMaterialId(Long materialId) {
        return stockOutRecordRepository.findByMaterialIdOrderByCreatedTimeDesc(materialId);
    }
    
    @Override
    public List<StockOutRecord> getStockOutRecordsByStatus(StockOutRecord.Status status) {
        return stockOutRecordRepository.findByStatusOrderByCreatedTimeDesc(status);
    }
    
    // ==================== 库存计算 ====================
    
    @Override
    public BigDecimal calculateCurrentStock(Long materialId) {
        // 计算入库总量
        Double inboundTotal = stockInRecordRepository.sumInboundQuantityByMaterialId(materialId);
        BigDecimal inboundQuantity = BigDecimal.valueOf(inboundTotal != null ? inboundTotal : 0.0);
        
        // 计算出库总量
        Double outboundTotal = stockOutRecordRepository.sumOutboundQuantityByMaterialId(materialId);
        BigDecimal outboundQuantity = BigDecimal.valueOf(outboundTotal != null ? outboundTotal : 0.0);
        
        // 当前库存 = 入库总量 - 出库总量
        return inboundQuantity.subtract(outboundQuantity);
    }
    
    @Override
    public BigDecimal calculateAvailableStock(Long materialId) {
        Stock stock = stockRepository.findByMaterialId(materialId).orElse(null);
        if (stock == null) {
            return BigDecimal.ZERO;
        }
        
        return stock.getCurrentQuantity().subtract(stock.getReservedQuantity());
    }
    
    @Override
    public void updateMaterialStock(Long materialId) {
        AgriculturalMaterial material = agriculturalMaterialService.getAgriculturalMaterialById(materialId);
        if (material == null) {
            throw new ResourceNotFoundException("农资不存在，ID: " + materialId);
        }
        
        // 计算当前库存
        BigDecimal currentQuantity = calculateCurrentStock(materialId);
        
        // 获取或创建库存记录
        Stock stock = stockRepository.findByMaterialId(materialId).orElse(new Stock(material));
        
        // 更新库存信息
        stock.setCurrentQuantity(currentQuantity);
        stock.setLastInDate(getLastInDate(materialId));
        stock.setLastOutDate(getLastOutDate(materialId));
        
        // 计算平均价格
        BigDecimal averagePrice = calculateAveragePrice(materialId);
        stock.setAveragePrice(averagePrice);
        
        // 计算库存总价值
        BigDecimal totalValue = currentQuantity.multiply(averagePrice);
        stock.setTotalValue(totalValue);
        
        // 更新可用库存
        stock.updateAvailableQuantity();
        
        stockRepository.save(stock);
    }
    
    @Override
    public void updateAllMaterialStocks() {
        List<AgriculturalMaterial> materials = agriculturalMaterialService.getAllAgriculturalMaterials();
        for (AgriculturalMaterial material : materials) {
            updateMaterialStock(material.getId());
        }
    }
    
    // ==================== 库存记录 ====================
    
    @Override
    public List<InventoryRecord> getInventoryRecordsByMaterialId(Long materialId) {
        return inventoryRecordRepository.findByMaterialIdOrderByCreatedTimeDesc(materialId);
    }
    
    @Override
    public List<InventoryRecord> getInventoryRecordsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return inventoryRecordRepository.findByTimeRange(startTime, endTime);
    }
    
    @Override
    public BigDecimal getTotalInboundQuantity(Long materialId) {
        Double total = stockInRecordRepository.sumInboundQuantityByMaterialId(materialId);
        return BigDecimal.valueOf(total != null ? total : 0.0);
    }
    
    @Override
    public BigDecimal getTotalOutboundQuantity(Long materialId) {
        Double total = stockOutRecordRepository.sumOutboundQuantityByMaterialId(materialId);
        return BigDecimal.valueOf(total != null ? total : 0.0);
    }
    
    // ==================== 统计报表 ====================
    
    @Override
    public Map<String, Object> getStockStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 总库存价值
        BigDecimal totalValue = stockRepository.getTotalStockValue();
        statistics.put("totalValue", totalValue);
        
        // 总库存数量
        BigDecimal totalQuantity = stockRepository.getTotalStockQuantity();
        statistics.put("totalQuantity", totalQuantity);
        
        // 库存不足的农资数量
        List<Stock> lowStockItems = getLowStockItems();
        statistics.put("lowStockCount", lowStockItems.size());
        
        // 库存过量的农资数量
        List<Stock> overStockItems = getOverStockItems();
        statistics.put("overStockCount", overStockItems.size());
        
        // 总农资种类数
        long materialCount = stockRepository.count();
        statistics.put("materialCount", materialCount);
        
        return statistics;
    }
    
    @Override
    public Map<String, Object> getMaterialStockStatistics(Long materialId) {
        Map<String, Object> statistics = new HashMap<>();
        
        // 当前库存
        BigDecimal currentStock = calculateCurrentStock(materialId);
        statistics.put("currentStock", currentStock);
        
        // 可用库存
        BigDecimal availableStock = calculateAvailableStock(materialId);
        statistics.put("availableStock", availableStock);
        
        // 入库总量
        BigDecimal totalInbound = getTotalInboundQuantity(materialId);
        statistics.put("totalInbound", totalInbound);
        
        // 出库总量
        BigDecimal totalOutbound = getTotalOutboundQuantity(materialId);
        statistics.put("totalOutbound", totalOutbound);
        
        // 库存记录
        List<InventoryRecord> records = getInventoryRecordsByMaterialId(materialId);
        statistics.put("recordCount", records.size());
        
        return statistics;
    }
    
    @Override
    public Map<String, Object> getStockAlerts() {
        Map<String, Object> alerts = new HashMap<>();
        
        // 库存不足预警
        List<Stock> lowStockItems = getLowStockItems();
        alerts.put("lowStockItems", lowStockItems);
        
        // 库存过量预警
        List<Stock> overStockItems = getOverStockItems();
        alerts.put("overStockItems", overStockItems);
        
        // 即将过期的库存
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(30); // 30天内过期
        List<Stock> expiringItems = stockRepository.findExpiringStock(expiryDate);
        alerts.put("expiringItems", expiringItems);
        
        return alerts;
    }
    
    @Override
    public Map<String, Object> generateStockReport(LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> report = new HashMap<>();
        
        // 时间范围
        report.put("startTime", startTime);
        report.put("endTime", endTime);
        
        // 入库统计
        Double totalInbound = stockInRecordRepository.sumInboundQuantityByTimeRange(startTime, endTime);
        report.put("totalInboundQuantity", totalInbound != null ? totalInbound : 0.0);
        
        // 出库统计
        Double totalOutbound = stockOutRecordRepository.sumOutboundQuantityByTimeRange(startTime, endTime);
        report.put("totalOutboundQuantity", totalOutbound != null ? totalOutbound : 0.0);
        
        // 库存记录
        List<InventoryRecord> records = getInventoryRecordsByTimeRange(startTime, endTime);
        report.put("inventoryRecords", records);
        
        return report;
    }
    
    // ==================== 私有方法 ====================
    
    /**
     * 生成入库单号
     */
    private String generateInboundNumber() {
        String prefix = "IN";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + timestamp.substring(timestamp.length() - 8);
    }
    
    /**
     * 生成出库单号
     */
    private String generateOutboundNumber() {
        String prefix = "OUT";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + timestamp.substring(timestamp.length() - 8);
    }
    
    /**
     * 检查库存是否充足
     */
    private void checkStockAvailability(Long materialId, BigDecimal requiredQuantity) {
        BigDecimal availableStock = calculateAvailableStock(materialId);
        if (availableStock.compareTo(requiredQuantity) < 0) {
            throw new IllegalStateException("库存不足，可用库存: " + availableStock + "，需要: " + requiredQuantity);
        }
    }
    
    /**
     * 入库后更新库存
     */
    private void updateStockAfterInbound(StockInRecord stockInRecord) {
        updateMaterialStock(stockInRecord.getMaterial().getId());
    }
    
    /**
     * 出库后更新库存
     */
    private void updateStockAfterOutbound(StockOutRecord stockOutRecord) {
        updateMaterialStock(stockOutRecord.getMaterial().getId());
    }
    
    /**
     * 创建库存记录
     */
    private void createInventoryRecord(StockInRecord stockInRecord) {
        InventoryRecord record = new InventoryRecord();
        record.setMaterial(stockInRecord.getMaterial());
        record.setOperationType(InventoryRecord.OperationType.IN);
        record.setQuantity(stockInRecord.getQuantity());
        record.setUnitPrice(stockInRecord.getUnitPrice());
        record.setTotalAmount(stockInRecord.getTotalAmount());
        record.setOperatorName(stockInRecord.getOperatorName());
        record.setOperationReason(stockInRecord.getInboundReason());
        record.setRelatedDocument(stockInRecord.getInboundNumber());
        
        // 计算操作前后的库存
        BigDecimal beforeQuantity = calculateCurrentStock(stockInRecord.getMaterial().getId()).subtract(stockInRecord.getQuantity());
        record.setBeforeQuantity(beforeQuantity);
        record.setAfterQuantity(calculateCurrentStock(stockInRecord.getMaterial().getId()));
        
        inventoryRecordRepository.save(record);
    }
    
    /**
     * 创建库存记录
     */
    private void createInventoryRecord(StockOutRecord stockOutRecord) {
        InventoryRecord record = new InventoryRecord();
        record.setMaterial(stockOutRecord.getMaterial());
        record.setOperationType(InventoryRecord.OperationType.OUT);
        record.setQuantity(stockOutRecord.getQuantity());
        record.setUnitPrice(stockOutRecord.getUnitPrice());
        record.setTotalAmount(stockOutRecord.getTotalAmount());
        record.setOperatorName(stockOutRecord.getOperatorName());
        record.setOperationReason(stockOutRecord.getOutboundReason());
        record.setRelatedDocument(stockOutRecord.getOutboundNumber());
        
        // 计算操作前后的库存
        BigDecimal afterQuantity = calculateCurrentStock(stockOutRecord.getMaterial().getId());
        record.setBeforeQuantity(afterQuantity.add(stockOutRecord.getQuantity()));
        record.setAfterQuantity(afterQuantity);
        
        inventoryRecordRepository.save(record);
    }
    
    /**
     * 获取最后入库时间
     */
    private LocalDateTime getLastInDate(Long materialId) {
        List<StockInRecord> records = stockInRecordRepository.findByMaterialIdOrderByCreatedTimeDesc(materialId);
        return records.isEmpty() ? null : records.get(0).getInboundDate();
    }
    
    /**
     * 获取最后出库时间
     */
    private LocalDateTime getLastOutDate(Long materialId) {
        List<StockOutRecord> records = stockOutRecordRepository.findByMaterialIdOrderByCreatedTimeDesc(materialId);
        return records.isEmpty() ? null : records.get(0).getOutboundDate();
    }
    
    /**
     * 计算平均价格
     */
    private BigDecimal calculateAveragePrice(Long materialId) {
        // 这里可以实现更复杂的平均价格计算逻辑
        // 暂时返回农资的基础价格
        AgriculturalMaterial material = agriculturalMaterialService.getAgriculturalMaterialById(materialId);
        return material != null && material.getUnitPrice() != null ? material.getUnitPrice() : BigDecimal.ZERO;
    }
}





