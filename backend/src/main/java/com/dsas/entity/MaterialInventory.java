package com.dsas.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "material_inventory")
public class MaterialInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false, unique = true)
    @NotNull(message = "农资不能为空")
    private AgriculturalMaterial material; // 关联农资编码ID

    @Column(name = "internal_code", length = 20, nullable = false)
    @NotBlank(message = "内部编码不能为空")
    @Size(max = 20, message = "内部编码长度不能超过20个字符")
    private String internalCode; // 内部编码

    @Column(name = "initial_stock", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", message = "初始库存不能为负数")
    private BigDecimal initialStock = BigDecimal.ZERO; // 初始库存

    @Column(name = "current_quantity", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", message = "当前库存数量不能为负数")
    private BigDecimal currentQuantity = BigDecimal.ZERO; // 当前库存数量

    @Column(name = "total_in_quantity", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", message = "总入库数量不能为负数")
    private BigDecimal totalInQuantity = BigDecimal.ZERO; // 总入库数量

    @Column(name = "total_out_quantity", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", message = "总出库数量不能为负数")
    private BigDecimal totalOutQuantity = BigDecimal.ZERO; // 总出库数量

    @Column(name = "unit_price", precision = 10, scale = 4)
    @DecimalMin(value = "0.0", message = "当前单位价不能为负数")
    private BigDecimal unitPrice; // 当前单位价

    @Column(name = "stock_amount", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", message = "库存金额不能为负数")
    private BigDecimal stockAmount = BigDecimal.ZERO; // 库存金额

    @Column(name = "location", length = 100)
    @Size(max = 100, message = "库存位置长度不能超过100个字符")
    private String location; // 库存位置

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    // 构造函数
    public MaterialInventory() {
        this.lastUpdated = LocalDateTime.now();
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AgriculturalMaterial getMaterial() { return material; }
    public void setMaterial(AgriculturalMaterial material) { 
        this.material = material;
        if (material != null) {
            this.internalCode = material.getInternalCode();
            this.unitPrice = material.getUnitPrice();
        }
    }

    public String getInternalCode() { return internalCode; }
    public void setInternalCode(String internalCode) { this.internalCode = internalCode; }

    public BigDecimal getInitialStock() { return initialStock; }
    public void setInitialStock(BigDecimal initialStock) { this.initialStock = initialStock; }

    public BigDecimal getCurrentQuantity() { return currentQuantity; }
    public void setCurrentQuantity(BigDecimal currentQuantity) { this.currentQuantity = currentQuantity; }

    public BigDecimal getTotalInQuantity() { return totalInQuantity; }
    public void setTotalInQuantity(BigDecimal totalInQuantity) { this.totalInQuantity = totalInQuantity; }

    public BigDecimal getTotalOutQuantity() { return totalOutQuantity; }
    public void setTotalOutQuantity(BigDecimal totalOutQuantity) { this.totalOutQuantity = totalOutQuantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getStockAmount() { return stockAmount; }
    public void setStockAmount(BigDecimal stockAmount) { this.stockAmount = stockAmount; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    // 业务方法
    public void addInQuantity(BigDecimal quantity) {
        if (quantity != null && quantity.compareTo(BigDecimal.ZERO) > 0) {
            this.totalInQuantity = this.totalInQuantity.add(quantity);
            this.currentQuantity = this.currentQuantity.add(quantity);
            calculateStockAmount();
            this.lastUpdated = LocalDateTime.now();
        }
    }

    public void addOutQuantity(BigDecimal quantity) {
        if (quantity != null && quantity.compareTo(BigDecimal.ZERO) > 0) {
            this.totalOutQuantity = this.totalOutQuantity.add(quantity);
            this.currentQuantity = this.currentQuantity.subtract(quantity);
            if (this.currentQuantity.compareTo(BigDecimal.ZERO) < 0) {
                this.currentQuantity = BigDecimal.ZERO;
            }
            calculateStockAmount();
            this.lastUpdated = LocalDateTime.now();
        }
    }

    public void calculateStockAmount() {
        if (currentQuantity != null && unitPrice != null) {
            this.stockAmount = currentQuantity.multiply(unitPrice);
        } else {
            this.stockAmount = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdated = LocalDateTime.now();
        calculateStockAmount();
    }
}





