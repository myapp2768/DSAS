package com.dsas.repository;

import com.dsas.entity.InventoryRecord;
import com.dsas.entity.AgriculturalMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 库存记录Repository
 */
@Repository
public interface InventoryRecordRepository extends JpaRepository<InventoryRecord, Long> {
    
    /**
     * 根据农资ID查询库存记录
     */
    List<InventoryRecord> findByMaterialIdOrderByCreatedTimeDesc(Long materialId);
    
    /**
     * 根据农资查询库存记录
     */
    List<InventoryRecord> findByMaterialOrderByCreatedTimeDesc(AgriculturalMaterial material);
    
    /**
     * 根据操作类型查询库存记录
     */
    List<InventoryRecord> findByOperationTypeOrderByCreatedTimeDesc(InventoryRecord.OperationType operationType);
    
    /**
     * 根据时间范围查询库存记录
     */
    @Query("SELECT ir FROM InventoryRecord ir WHERE ir.createdTime BETWEEN :startTime AND :endTime ORDER BY ir.createdTime DESC")
    List<InventoryRecord> findByTimeRange(@Param("startTime") LocalDateTime startTime, 
                                         @Param("endTime") LocalDateTime endTime);
    
    /**
     * 根据农资ID和时间范围查询库存记录
     */
    @Query("SELECT ir FROM InventoryRecord ir WHERE ir.material.id = :materialId AND ir.createdTime BETWEEN :startTime AND :endTime ORDER BY ir.createdTime DESC")
    List<InventoryRecord> findByMaterialIdAndTimeRange(@Param("materialId") Long materialId,
                                                      @Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime);
    
    /**
     * 根据操作员查询库存记录
     */
    List<InventoryRecord> findByOperatorNameOrderByCreatedTimeDesc(String operatorName);
    
    /**
     * 统计农资的入库总量
     */
    @Query("SELECT COALESCE(SUM(ir.quantity), 0) FROM InventoryRecord ir WHERE ir.material.id = :materialId AND ir.operationType = 'IN'")
    Double sumInboundQuantityByMaterialId(@Param("materialId") Long materialId);
    
    /**
     * 统计农资的出库总量
     */
    @Query("SELECT COALESCE(SUM(ir.quantity), 0) FROM InventoryRecord ir WHERE ir.material.id = :materialId AND ir.operationType = 'OUT'")
    Double sumOutboundQuantityByMaterialId(@Param("materialId") Long materialId);
    
    /**
     * 查询农资的最新库存记录
     */
    @Query("SELECT ir FROM InventoryRecord ir WHERE ir.material.id = :materialId ORDER BY ir.createdTime DESC")
    List<InventoryRecord> findLatestByMaterialId(@Param("materialId") Long materialId);
}





