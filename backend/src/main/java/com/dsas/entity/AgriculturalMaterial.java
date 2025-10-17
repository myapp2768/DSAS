package com.dsas.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "agricultural_materials")
public class AgriculturalMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "internal_code", length = 20, unique = true, nullable = false)
    @Size(max = 20, message = "内部编码长度不能超过20个字符")
    private String internalCode; // 内部编码: XS-0001

    @Column(name = "category", length = 50, nullable = false)
    @NotBlank(message = "种类不能为空")
    @Size(max = 50, message = "种类长度不能超过50个字符")
    private String category; // 种类: 肥料

    @Column(name = "name", length = 100, nullable = false)
    @NotBlank(message = "名称不能为空")
    @Size(max = 100, message = "名称长度不能超过100个字符")
    private String name; // 名称: 中量元素水溶肥

    @Column(name = "brand", length = 50)
    @Size(max = 50, message = "品牌长度不能超过50个字符")
    private String brand; // 品牌: 中化摩彩

    @Column(name = "specification", length = 100)
    @Size(max = 100, message = "规格长度不能超过100个字符")
    private String specification; // 规格: 10kg/包

    @Column(name = "unit", length = 20)
    @Size(max = 20, message = "单位长度不能超过20个字符")
    private String unit; // 单位: kg

    @Column(name = "content", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", message = "含量不能为负数")
    private BigDecimal content; // 含量: 10

    @Column(name = "unit_price", precision = 10, scale = 4)
    @DecimalMin(value = "0.0", message = "单价不能为负数")
    private BigDecimal unitPrice; // 单价: 36.0656

    @Column(name = "price_per_unit", precision = 10, scale = 4)
    @DecimalMin(value = "0.0", message = "单位价不能为负数")
    private BigDecimal pricePerUnit; // 单位价: 3.6066

    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark; // 备注

    @Column(name = "created_time")
    @PastOrPresent(message = "创建时间不能是未来时间")
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    @PastOrPresent(message = "更新时间不能是未来时间")
    private LocalDateTime updatedTime;

    // 构造函数
    public AgriculturalMaterial() {
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getInternalCode() { return internalCode; }
    public void setInternalCode(String internalCode) { this.internalCode = internalCode; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getSpecification() { return specification; }
    public void setSpecification(String specification) { this.specification = specification; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public BigDecimal getContent() { return content; }
    public void setContent(BigDecimal content) { this.content = content; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getPricePerUnit() { return pricePerUnit; }
    public void setPricePerUnit(BigDecimal pricePerUnit) { this.pricePerUnit = pricePerUnit; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }

    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }

    @PreUpdate
    public void preUpdate() {
        this.updatedTime = LocalDateTime.now();
    }
}