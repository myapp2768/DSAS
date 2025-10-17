package com.dsas.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "material_out_records")
public class MaterialOutRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    @NotNull(message = "农资不能为空")
    private AgriculturalMaterial material; // 关联农资编码ID

    @Column(name = "internal_code", length = 20, nullable = false)
    @NotBlank(message = "内部编码不能为空")
    @Size(max = 20, message = "内部编码长度不能超过20个字符")
    private String internalCode; // 内部编码

    @Column(name = "out_date", nullable = false)
    @NotNull(message = "出库日期不能为空")
    @PastOrPresent(message = "出库日期不能是未来时间")
    private LocalDate outDate; // 出库日期: 2024-09-01

    @Column(name = "quantity", precision = 10, scale = 2, nullable = false)
    @NotNull(message = "出库数量不能为空")
    @DecimalMin(value = "0.01", message = "出库数量必须大于0")
    private BigDecimal quantity; // 出库数量: 3.5

    @Column(name = "unit_price", precision = 10, scale = 4, nullable = false)
    @NotNull(message = "单位价不能为空")
    @DecimalMin(value = "0.0", message = "单位价不能为负数")
    private BigDecimal unitPrice; // 单位价

    @Column(name = "total_amount", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", message = "出库金额不能为负数")
    private BigDecimal totalAmount; // 出库金额

    @Column(name = "out_person", length = 50)
    @Size(max = 50, message = "出库人长度不能超过50个字符")
    private String outPerson; // 出库人: 周尚平

    @Column(name = "out_type", length = 50)
    @Size(max = 50, message = "出入形式长度不能超过50个字符")
    private String outType; // 出入形式

    @Column(name = "purpose", length = 100)
    @Size(max = 100, message = "物料用途长度不能超过100个字符")
    private String purpose; // 物料用途: 家庭农场

    @Column(name = "recorder", length = 50)
    @Size(max = 50, message = "录入人长度不能超过50个字符")
    private String recorder; // 录入人: 陈春秀

    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark; // 备注

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    // 构造函数
    public MaterialOutRecord() {
        this.createdTime = LocalDateTime.now();
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AgriculturalMaterial getMaterial() { return material; }
    public void setMaterial(AgriculturalMaterial material) { 
        this.material = material;
        if (material != null) {
            this.internalCode = material.getInternalCode();
        }
    }

    public String getInternalCode() { return internalCode; }
    public void setInternalCode(String internalCode) { this.internalCode = internalCode; }

    public LocalDate getOutDate() { return outDate; }
    public void setOutDate(LocalDate outDate) { this.outDate = outDate; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getOutPerson() { return outPerson; }
    public void setOutPerson(String outPerson) { this.outPerson = outPerson; }

    public String getOutType() { return outType; }
    public void setOutType(String outType) { this.outType = outType; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getRecorder() { return recorder; }
    public void setRecorder(String recorder) { this.recorder = recorder; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }

    // 计算总金额的方法
    public BigDecimal calculateTotalAmount() {
        if (quantity != null && unitPrice != null) {
            return quantity.multiply(unitPrice);
        }
        return BigDecimal.ZERO;
    }

    @PrePersist
    @PreUpdate
    public void calculateAmount() {
        this.totalAmount = calculateTotalAmount();
    }
}





