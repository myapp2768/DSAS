package com.dsas.repository;

import com.dsas.entity.Stock;
import com.dsas.entity.AgriculturalMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 库存Repository
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    
    /**
     * 根据农资ID查询库存
     */
    Optional<Stock> findByMaterialId(Long materialId);
    
    /**
     * 根据农资查询库存
     */
    Optional<Stock> findByMaterial(AgriculturalMaterial material);
    
    /**
     * 查询所有有库存的农资
     */
    @Query("SELECT s FROM Stock s WHERE s.currentQuantity > 0 ORDER BY s.material.name")
    List<Stock> findAllWithStock();
    
    /**
     * 查询库存不足的农资（低于安全库存）
     */
    @Query("SELECT s FROM Stock s WHERE s.currentQuantity < s.safetyStock ORDER BY s.material.name")
    List<Stock> findLowStockItems();
    
    /**
     * 查询库存过量的农资（超过最大库存）
     */
    @Query("SELECT s FROM Stock s WHERE s.maxStock > 0 AND s.currentQuantity > s.maxStock ORDER BY s.material.name")
    List<Stock> findOverStockItems();
    
    /**
     * 根据库存数量范围查询
     */
    @Query("SELECT s FROM Stock s WHERE s.currentQuantity BETWEEN :minQuantity AND :maxQuantity ORDER BY s.currentQuantity DESC")
    List<Stock> findByQuantityRange(@Param("minQuantity") BigDecimal minQuantity, 
                                   @Param("maxQuantity") BigDecimal maxQuantity);
    
    /**
     * 根据农资名称模糊查询库存
     */
    @Query("SELECT s FROM Stock s WHERE s.material.name LIKE %:name% ORDER BY s.material.name")
    List<Stock> findByMaterialNameContaining(@Param("name") String name);
    
    /**
     * 根据农资种类查询库存
     */
    @Query("SELECT s FROM Stock s WHERE s.material.category = :category ORDER BY s.material.name")
    List<Stock> findByMaterialCategory(@Param("category") String category);
    
    /**
     * 统计总库存价值
     */
    @Query("SELECT COALESCE(SUM(s.totalValue), 0) FROM Stock s")
    BigDecimal getTotalStockValue();
    
    /**
     * 统计总库存数量
     */
    @Query("SELECT COALESCE(SUM(s.currentQuantity), 0) FROM Stock s")
    BigDecimal getTotalStockQuantity();
    
    /**
     * 查询即将过期的库存（基于入库记录的过期日期）
     */
    @Query("SELECT s FROM Stock s WHERE EXISTS (SELECT sir FROM StockInRecord sir WHERE sir.material = s.material AND sir.expiryDate IS NOT NULL AND sir.expiryDate <= :expiryDate)")
    List<Stock> findExpiringStock(@Param("expiryDate") java.time.LocalDateTime expiryDate);
}
