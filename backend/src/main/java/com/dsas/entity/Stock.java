package com.dsas.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存实体
 * 记录农资的当前库存情况
 */
@Entity
@Table(name = "stocks")
public class Stock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false, unique = true)
    @NotNull(message = "农资不能为空")
    private AgriculturalMaterial material;
    
    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "当前库存不能为空")
    @DecimalMin(value = "0.0", message = "库存不能为负数")
    private BigDecimal currentQuantity; // 当前库存数量
    
    @Column(name = "available_quantity", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", message = "可用库存不能为负数")
    private BigDecimal availableQuantity; // 可用库存（当前库存 - 预留库存）
    
    @Column(name = "reserved_quantity", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", message = "预留库存不能为负数")
    private BigDecimal reservedQuantity; // 预留库存
    
    @Column(name = "safety_stock", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", message = "安全库存不能为负数")
    private BigDecimal safetyStock; // 安全库存
    
    @Column(name = "max_stock", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", message = "最大库存不能为负数")
    private BigDecimal maxStock; // 最大库存
    
    @Column(name = "last_in_date")
    private LocalDateTime lastInDate; // 最后入库时间
    
    @Column(name = "last_out_date")
    private LocalDateTime lastOutDate; // 最后出库时间
    
    @Column(name = "average_price", precision = 10, scale = 4)
    private BigDecimal averagePrice; // 平均价格
    
    @Column(name = "total_value", precision = 15, scale = 2)
    private BigDecimal totalValue; // 库存总价值
    
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;
    
    // 构造函数
    public Stock() {
        this.currentQuantity = BigDecimal.ZERO;
        this.availableQuantity = BigDecimal.ZERO;
        this.reservedQuantity = BigDecimal.ZERO;
        this.safetyStock = BigDecimal.ZERO;
        this.maxStock = BigDecimal.ZERO;
        this.averagePrice = BigDecimal.ZERO;
        this.totalValue = BigDecimal.ZERO;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }
    
    // 构造函数
    public Stock(AgriculturalMaterial material) {
        this();
        this.material = material;
    }
    
    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public AgriculturalMaterial getMaterial() { return material; }
    public void setMaterial(AgriculturalMaterial material) { this.material = material; }
    
    public BigDecimal getCurrentQuantity() { return currentQuantity; }
    public void setCurrentQuantity(BigDecimal currentQuantity) { this.currentQuantity = currentQuantity; }
    
    public BigDecimal getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(BigDecimal availableQuantity) { this.availableQuantity = availableQuantity; }
    
    public BigDecimal getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(BigDecimal reservedQuantity) { this.reservedQuantity = reservedQuantity; }
    
    public BigDecimal getSafetyStock() { return safetyStock; }
    public void setSafetyStock(BigDecimal safetyStock) { this.safetyStock = safetyStock; }
    
    public BigDecimal getMaxStock() { return maxStock; }
    public void setMaxStock(BigDecimal maxStock) { this.maxStock = maxStock; }
    
    public LocalDateTime getLastInDate() { return lastInDate; }
    public void setLastInDate(LocalDateTime lastInDate) { this.lastInDate = lastInDate; }
    
    public LocalDateTime getLastOutDate() { return lastOutDate; }
    public void setLastOutDate(LocalDateTime lastOutDate) { this.lastOutDate = lastOutDate; }
    
    public BigDecimal getAveragePrice() { return averagePrice; }
    public void setAveragePrice(BigDecimal averagePrice) { this.averagePrice = averagePrice; }
    
    public BigDecimal getTotalValue() { return totalValue; }
    public void setTotalValue(BigDecimal totalValue) { this.totalValue = totalValue; }
    
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    
    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }
    
    // 业务方法
    /**
     * 更新可用库存
     */
    public void updateAvailableQuantity() {
        this.availableQuantity = this.currentQuantity.subtract(this.reservedQuantity);
    }
    
    /**
     * 检查是否低于安全库存
     */
    public boolean isBelowSafetyStock() {
        return this.currentQuantity.compareTo(this.safetyStock) < 0;
    }
    
    /**
     * 检查是否超过最大库存
     */
    public boolean isAboveMaxStock() {
        return this.maxStock.compareTo(BigDecimal.ZERO) > 0 && 
               this.currentQuantity.compareTo(this.maxStock) > 0;
    }
    
    /**
     * 更新时间的便捷方法
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedTime = LocalDateTime.now();
        updateAvailableQuantity();
    }
}

