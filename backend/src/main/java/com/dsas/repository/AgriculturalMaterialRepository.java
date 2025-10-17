package com.dsas.repository;

import com.dsas.entity.AgriculturalMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgriculturalMaterialRepository extends JpaRepository<AgriculturalMaterial, Long> {

    // 根据内部编码查找农资
    Optional<AgriculturalMaterial> findByInternalCode(String internalCode);

    // 检查内部编码是否存在
    boolean existsByInternalCode(String internalCode);

    // 根据种类查找农资
    List<AgriculturalMaterial> findByCategory(String category);

    // 根据种类分页查找农资
    Page<AgriculturalMaterial> findByCategory(String category, Pageable pageable);

    // 根据名称模糊搜索
    List<AgriculturalMaterial> findByNameContaining(String name);

    // 根据名称模糊搜索（分页）
    Page<AgriculturalMaterial> findByNameContaining(String name, Pageable pageable);

    // 根据品牌查找农资
    List<AgriculturalMaterial> findByBrand(String brand);

    // 根据品牌分页查找农资
    Page<AgriculturalMaterial> findByBrand(String brand, Pageable pageable);

    // 获取最大的内部编码
    @Query("SELECT MAX(am.internalCode) FROM AgriculturalMaterial am WHERE am.internalCode LIKE 'XS-%'")
    String findMaxInternalCode();

    // 统计查询
    @Query("SELECT COUNT(am) FROM AgriculturalMaterial am")
    Long countAllMaterials();

    @Query("SELECT COUNT(am) FROM AgriculturalMaterial am")
    Long countActiveMaterials();

    @Query("SELECT COUNT(am) FROM AgriculturalMaterial am")
    Long countInactiveMaterials();

    @Query("SELECT COUNT(DISTINCT am.category) FROM AgriculturalMaterial am")
    Long countDistinctCategories();

    @Query("SELECT COUNT(DISTINCT am.brand) FROM AgriculturalMaterial am WHERE am.brand IS NOT NULL")
    Long countDistinctSuppliers();

    // 价格统计
    @Query("SELECT COALESCE(SUM(am.unitPrice), 0) FROM AgriculturalMaterial am")
    BigDecimal getTotalValue();

    @Query("SELECT COALESCE(AVG(am.unitPrice), 0) FROM AgriculturalMaterial am")
    BigDecimal getAveragePrice();

    // 复杂查询：多条件搜索
    @Query("SELECT am FROM AgriculturalMaterial am WHERE " +
           "(:name IS NULL OR am.name LIKE %:name%) AND " +
           "(:category IS NULL OR am.category = :category) AND " +
           "(:brand IS NULL OR am.brand = :brand)")
    Page<AgriculturalMaterial> findByMultipleConditions(
            @Param("name") String name,
            @Param("category") String category,
            @Param("brand") String brand,
            Pageable pageable);

    // 查找库存不足的农资（需要关联MaterialInventory表）
    @Query("SELECT am FROM AgriculturalMaterial am WHERE " +
           "EXISTS (SELECT mi FROM MaterialInventory mi WHERE mi.material = am AND mi.currentQuantity < 10)")
    List<AgriculturalMaterial> findLowStockMaterials();
}