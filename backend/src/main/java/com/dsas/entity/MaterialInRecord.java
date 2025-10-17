package com.dsas.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "material_in_records")
public class MaterialInRecord {

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

    @Column(name = "in_date", nullable = false)
    @NotNull(message = "入库日期不能为空")
    @PastOrPresent(message = "入库日期不能是未来时间")
    private LocalDate inDate; // 入库日期: 2024-09-10

    @Column(name = "quantity", precision = 10, scale = 2, nullable = false)
    @NotNull(message = "入库数量不能为空")
    @DecimalMin(value = "0.01", message = "入库数量必须大于0")
    private BigDecimal quantity; // 入库数量: 800

    @Column(name = "unit_price", precision = 10, scale = 4, nullable = false)
    @NotNull(message = "单位价不能为空")
    @DecimalMin(value = "0.0", message = "单位价不能为负数")
    private BigDecimal unitPrice; // 单位价

    @Column(name = "location", length = 100)
    @Size(max = 100, message = "入库位置长度不能超过100个字符")
    private String location; // 入库位置: 1号药房

    @Column(name = "in_person", length = 50)
    @Size(max = 50, message = "入库人长度不能超过50个字符")
    private String inPerson; // 入库人: 张栋杰

    @Column(name = "recorder", length = 50)
    @Size(max = 50, message = "录入人长度不能超过50个字符")
    private String recorder; // 录入人: 陈春秀

    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark; // 备注

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    // 构造函数
    public MaterialInRecord() {
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

    public LocalDate getInDate() { return inDate; }
    public void setInDate(LocalDate inDate) { this.inDate = inDate; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getInPerson() { return inPerson; }
    public void setInPerson(String inPerson) { this.inPerson = inPerson; }

    public String getRecorder() { return recorder; }
    public void setRecorder(String recorder) { this.recorder = recorder; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }

    // 计算总金额的方法
    public BigDecimal getTotalAmount() {
        if (quantity != null && unitPrice != null) {
            return quantity.multiply(unitPrice);
        }
        return BigDecimal.ZERO;
    }
}





