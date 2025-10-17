package com.dsas.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存记录实体
 * 记录农资的库存变动情况
 */
@Entity
@Table(name = "inventory_records")
public class InventoryRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    @NotNull(message = "农资不能为空")
    private AgriculturalMaterial material;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "操作类型不能为空")
    private OperationType operationType; // 操作类型：IN(入库)、OUT(出库)
    
    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "数量不能为空")
    @Positive(message = "数量必须大于0")
    private BigDecimal quantity; // 操作数量
    
    @Column(precision = 10, scale = 4)
    private BigDecimal unitPrice; // 单价
    
    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount; // 总金额
    
    @Column(name = "before_quantity", precision = 10, scale = 2)
    private BigDecimal beforeQuantity; // 操作前库存
    
    @Column(name = "after_quantity", precision = 10, scale = 2)
    private BigDecimal afterQuantity; // 操作后库存
    
    @Column(name = "operator_name", length = 50)
    private String operatorName; // 操作员
    
    @Column(name = "operation_reason", length = 200)
    private String operationReason; // 操作原因
    
    @Column(name = "related_document", length = 100)
    private String relatedDocument; // 关联单据号
    
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;
    
    // 操作类型枚举
    public enum OperationType {
        IN("入库"),
        OUT("出库"),
        ADJUST("调整"),
        TRANSFER("调拨");
        
        private final String description;
        
        OperationType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    // 构造函数
    public InventoryRecord() {
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }
    
    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public AgriculturalMaterial getMaterial() { return material; }
    public void setMaterial(AgriculturalMaterial material) { this.material = material; }
    
    public OperationType getOperationType() { return operationType; }
    public void setOperationType(OperationType operationType) { this.operationType = operationType; }
    
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public BigDecimal getBeforeQuantity() { return beforeQuantity; }
    public void setBeforeQuantity(BigDecimal beforeQuantity) { this.beforeQuantity = beforeQuantity; }
    
    public BigDecimal getAfterQuantity() { return afterQuantity; }
    public void setAfterQuantity(BigDecimal afterQuantity) { this.afterQuantity = afterQuantity; }
    
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    
    public String getOperationReason() { return operationReason; }
    public void setOperationReason(String operationReason) { this.operationReason = operationReason; }
    
    public String getRelatedDocument() { return relatedDocument; }
    public void setRelatedDocument(String relatedDocument) { this.relatedDocument = relatedDocument; }
    
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    
    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }
    
    // 更新时间的便捷方法
    @PreUpdate
    public void preUpdate() {
        this.updatedTime = LocalDateTime.now();
    }
}

