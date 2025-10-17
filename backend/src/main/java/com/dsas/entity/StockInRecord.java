package com.dsas.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 入库记录实体类
 */
@Entity
@Table(name = "stock_in_records")
public class StockInRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "material_id", nullable = false)
    private Long materialId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", insertable = false, updatable = false)
    private AgriculturalMaterial material;
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @Column(name = "unit_price")
    private Double unitPrice;
    
    @Column(name = "total_price")
    private Double totalPrice;
    
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    
    @Column(name = "supplier")
    private String supplier;
    
    @Column(name = "supplier_name")
    private String supplierName;
    
    @Column(name = "batch_number")
    private String batchNumber;
    
    @Column(name = "inbound_number")
    private String inboundNumber;
    
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;
    
    @Column(name = "inbound_date")
    private LocalDateTime inboundDate;
    
    @Column(name = "operator")
    private String operator;
    
    @Column(name = "operator_name")
    private String operatorName;
    
    @Column(name = "inbound_reason")
    private String inboundReason;
    
    @Column(name = "quality_status")
    private String qualityStatus;
    
    @Column(name = "storage_location")
    private String storageLocation;
    
    @Column(name = "remark")
    private String remark;
    
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 状态枚举
    public enum Status {
        PENDING,    // 待处理
        COMPLETED,  // 已完成
        CANCELLED   // 已取消
    }
    
    // 构造函数
    public StockInRecord() {}
    
    public StockInRecord(Long materialId, Integer quantity, Double unitPrice, String supplier) {
        this.materialId = materialId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.supplier = supplier;
        this.totalPrice = quantity * unitPrice;
    }
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getMaterialId() {
        return materialId;
    }
    
    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        if (unitPrice != null) {
            this.totalPrice = quantity * unitPrice;
        }
    }
    
    public Double getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
        if (quantity != null) {
            this.totalPrice = quantity * unitPrice;
        }
    }
    
    public Double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public String getSupplier() {
        return supplier;
    }
    
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    
    public String getBatchNumber() {
        return batchNumber;
    }
    
    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }
    
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public void setOperator(String operator) {
        this.operator = operator;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // 新增字段的getter和setter
    public AgriculturalMaterial getMaterial() {
        return material;
    }
    
    public void setMaterial(AgriculturalMaterial material) {
        this.material = material;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getSupplierName() {
        return supplierName;
    }
    
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
    
    public String getInboundNumber() {
        return inboundNumber;
    }
    
    public void setInboundNumber(String inboundNumber) {
        this.inboundNumber = inboundNumber;
    }
    
    public LocalDateTime getInboundDate() {
        return inboundDate;
    }
    
    public void setInboundDate(LocalDateTime inboundDate) {
        this.inboundDate = inboundDate;
    }
    
    public String getOperatorName() {
        return operatorName;
    }
    
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
    
    public String getInboundReason() {
        return inboundReason;
    }
    
    public void setInboundReason(String inboundReason) {
        this.inboundReason = inboundReason;
    }
    
    public String getQualityStatus() {
        return qualityStatus;
    }
    
    public void setQualityStatus(String qualityStatus) {
        this.qualityStatus = qualityStatus;
    }
    
    public String getStorageLocation() {
        return storageLocation;
    }
    
    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }
    
    // 业务方法
    public void calculateTotalAmount() {
        if (quantity != null && unitPrice != null) {
            this.totalAmount = BigDecimal.valueOf(quantity).multiply(BigDecimal.valueOf(unitPrice));
            this.totalPrice = quantity * unitPrice;
        }
    }
    
    public void complete() {
        this.status = Status.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void cancel() {
        this.status = Status.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "StockInRecord{" +
                "id=" + id +
                ", materialId=" + materialId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                ", supplier='" + supplier + '\'' +
                ", batchNumber='" + batchNumber + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
