package com.dsas.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 出库记录实体
 * 记录农资出库的详细信息
 */
@Entity
@Table(name = "stock_out_records")
public class StockOutRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "outbound_number", unique = true, nullable = false, length = 50)
    @NotBlank(message = "出库单号不能为空")
    private String outboundNumber; // 出库单号
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    @NotNull(message = "农资不能为空")
    private AgriculturalMaterial material;
    
    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "出库数量不能为空")
    @Positive(message = "出库数量必须大于0")
    private BigDecimal quantity; // 出库数量
    
    @Column(precision = 10, scale = 4)
    private BigDecimal unitPrice; // 单价
    
    @Column(precision = 15, scale = 2)
    private BigDecimal totalAmount; // 总金额
    
    @Column(name = "customer_name", length = 100)
    private String customerName; // 客户名称
    
    @Column(name = "customer_contact", length = 50)
    private String customerContact; // 客户联系方式
    
    @Column(name = "batch_number", length = 50)
    private String batchNumber; // 批次号
    
    @Column(name = "storage_location", length = 100)
    private String storageLocation; // 存储位置
    
    @Column(name = "operator_name", length = 50)
    private String operatorName; // 操作员
    
    @Column(name = "outbound_reason", length = 200)
    private String outboundReason; // 出库原因
    
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark; // 备注
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull(message = "状态不能为空")
    private Status status; // 状态：PENDING(待处理)、COMPLETED(已完成)、CANCELLED(已取消)
    
    @Enumerated(EnumType.STRING)
    @Column(name = "outbound_type", nullable = false)
    @NotNull(message = "出库类型不能为空")
    private OutboundType outboundType; // 出库类型：SALE(销售)、USE(自用)、TRANSFER(调拨)、LOSS(损耗)
    
    @Column(name = "outbound_date")
    private LocalDateTime outboundDate; // 出库日期
    
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;
    
    // 状态枚举
    public enum Status {
        PENDING("待处理"),
        COMPLETED("已完成"),
        CANCELLED("已取消");
        
        private final String description;
        
        Status(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    // 出库类型枚举
    public enum OutboundType {
        SALE("销售"),
        USE("自用"),
        TRANSFER("调拨"),
        LOSS("损耗");
        
        private final String description;
        
        OutboundType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    // 构造函数
    public StockOutRecord() {
        this.status = Status.PENDING;
        this.outboundType = OutboundType.USE;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }
    
    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getOutboundNumber() { return outboundNumber; }
    public void setOutboundNumber(String outboundNumber) { this.outboundNumber = outboundNumber; }
    
    public AgriculturalMaterial getMaterial() { return material; }
    public void setMaterial(AgriculturalMaterial material) { this.material = material; }
    
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getCustomerContact() { return customerContact; }
    public void setCustomerContact(String customerContact) { this.customerContact = customerContact; }
    
    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }
    
    public String getStorageLocation() { return storageLocation; }
    public void setStorageLocation(String storageLocation) { this.storageLocation = storageLocation; }
    
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    
    public String getOutboundReason() { return outboundReason; }
    public void setOutboundReason(String outboundReason) { this.outboundReason = outboundReason; }
    
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    
    public OutboundType getOutboundType() { return outboundType; }
    public void setOutboundType(OutboundType outboundType) { this.outboundType = outboundType; }
    
    public LocalDateTime getOutboundDate() { return outboundDate; }
    public void setOutboundDate(LocalDateTime outboundDate) { this.outboundDate = outboundDate; }
    
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    
    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }
    
    // 业务方法
    /**
     * 计算总金额
     */
    public void calculateTotalAmount() {
        if (this.quantity != null && this.unitPrice != null) {
            this.totalAmount = this.quantity.multiply(this.unitPrice);
        }
    }
    
    /**
     * 完成出库
     */
    public void complete() {
        this.status = Status.COMPLETED;
        this.outboundDate = LocalDateTime.now();
    }
    
    /**
     * 取消出库
     */
    public void cancel() {
        this.status = Status.CANCELLED;
    }
    
    /**
     * 更新时间的便捷方法
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedTime = LocalDateTime.now();
    }
}





