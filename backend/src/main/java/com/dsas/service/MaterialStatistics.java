package com.dsas.service;

import java.math.BigDecimal;

/**
 * 农资统计信息
 */
public class MaterialStatistics {
    
    private Long totalCount;           // 总数量
    private Long activeCount;          // 启用数量
    private Long inactiveCount;        // 禁用数量
    private Long categoryCount;        // 种类数量
    private Long supplierCount;        // 供应商数量
    private BigDecimal totalValue;     // 总价值
    private BigDecimal averagePrice;   // 平均价格
    
    // 构造函数
    public MaterialStatistics() {}
    
    public MaterialStatistics(Long totalCount, Long activeCount, Long inactiveCount, 
                             Long categoryCount, Long supplierCount, 
                             BigDecimal totalValue, BigDecimal averagePrice) {
        this.totalCount = totalCount;
        this.activeCount = activeCount;
        this.inactiveCount = inactiveCount;
        this.categoryCount = categoryCount;
        this.supplierCount = supplierCount;
        this.totalValue = totalValue;
        this.averagePrice = averagePrice;
    }
    
    // Getter和Setter方法
    public Long getTotalCount() { return totalCount; }
    public void setTotalCount(Long totalCount) { this.totalCount = totalCount; }
    
    public Long getActiveCount() { return activeCount; }
    public void setActiveCount(Long activeCount) { this.activeCount = activeCount; }
    
    public Long getInactiveCount() { return inactiveCount; }
    public void setInactiveCount(Long inactiveCount) { this.inactiveCount = inactiveCount; }
    
    public Long getCategoryCount() { return categoryCount; }
    public void setCategoryCount(Long categoryCount) { this.categoryCount = categoryCount; }
    
    public Long getSupplierCount() { return supplierCount; }
    public void setSupplierCount(Long supplierCount) { this.supplierCount = supplierCount; }
    
    public BigDecimal getTotalValue() { return totalValue; }
    public void setTotalValue(BigDecimal totalValue) { this.totalValue = totalValue; }
    
    public BigDecimal getAveragePrice() { return averagePrice; }
    public void setAveragePrice(BigDecimal averagePrice) { this.averagePrice = averagePrice; }
}





