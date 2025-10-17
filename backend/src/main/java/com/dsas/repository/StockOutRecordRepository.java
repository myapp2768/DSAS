package com.dsas.repository;

import com.dsas.entity.StockOutRecord;
import com.dsas.entity.AgriculturalMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 出库记录Repository
 */
@Repository
public interface StockOutRecordRepository extends JpaRepository<StockOutRecord, Long> {
    
    /**
     * 根据出库单号查询
     */
    Optional<StockOutRecord> findByOutboundNumber(String outboundNumber);
    
    /**
     * 根据农资ID查询出库记录
     */
    List<StockOutRecord> findByMaterialIdOrderByCreatedTimeDesc(Long materialId);
    
    /**
     * 根据农资查询出库记录
     */
    List<StockOutRecord> findByMaterialOrderByCreatedTimeDesc(AgriculturalMaterial material);
    
    /**
     * 根据状态查询出库记录
     */
    List<StockOutRecord> findByStatusOrderByCreatedTimeDesc(StockOutRecord.Status status);
    
    /**
     * 根据出库类型查询出库记录
     */
    List<StockOutRecord> findByOutboundTypeOrderByCreatedTimeDesc(StockOutRecord.OutboundType outboundType);
    
    /**
     * 根据客户查询出库记录
     */
    List<StockOutRecord> findByCustomerNameOrderByCreatedTimeDesc(String customerName);
    
    /**
     * 根据操作员查询出库记录
     */
    List<StockOutRecord> findByOperatorNameOrderByCreatedTimeDesc(String operatorName);
    
    /**
     * 根据时间范围查询出库记录
     */
    @Query("SELECT sor FROM StockOutRecord sor WHERE sor.createdTime BETWEEN :startTime AND :endTime ORDER BY sor.createdTime DESC")
    List<StockOutRecord> findByTimeRange(@Param("startTime") LocalDateTime startTime, 
                                        @Param("endTime") LocalDateTime endTime);
    
    /**
     * 根据农资ID和时间范围查询出库记录
     */
    @Query("SELECT sor FROM StockOutRecord sor WHERE sor.material.id = :materialId AND sor.createdTime BETWEEN :startTime AND :endTime ORDER BY sor.createdTime DESC")
    List<StockOutRecord> findByMaterialIdAndTimeRange(@Param("materialId") Long materialId,
                                                     @Param("startTime") LocalDateTime startTime,
                                                     @Param("endTime") LocalDateTime endTime);
    
    /**
     * 根据批次号查询出库记录
     */
    List<StockOutRecord> findByBatchNumberOrderByCreatedTimeDesc(String batchNumber);
    
    /**
     * 统计指定农资的出库总量
     */
    @Query("SELECT COALESCE(SUM(sor.quantity), 0) FROM StockOutRecord sor WHERE sor.material.id = :materialId AND sor.status = 'COMPLETED'")
    Double sumOutboundQuantityByMaterialId(@Param("materialId") Long materialId);
    
    /**
     * 统计指定时间范围内的出库总量
     */
    @Query("SELECT COALESCE(SUM(sor.quantity), 0) FROM StockOutRecord sor WHERE sor.createdTime BETWEEN :startTime AND :endTime AND sor.status = 'COMPLETED'")
    Double sumOutboundQuantityByTimeRange(@Param("startTime") LocalDateTime startTime, 
                                         @Param("endTime") LocalDateTime endTime);
    
    /**
     * 统计指定农资的出库总金额
     */
    @Query("SELECT COALESCE(SUM(sor.totalAmount), 0) FROM StockOutRecord sor WHERE sor.material.id = :materialId AND sor.status = 'COMPLETED'")
    Double sumOutboundAmountByMaterialId(@Param("materialId") Long materialId);
    
    /**
     * 查询待处理的出库记录
     */
    @Query("SELECT sor FROM StockOutRecord sor WHERE sor.status = 'PENDING' ORDER BY sor.createdTime ASC")
    List<StockOutRecord> findPendingRecords();
    
    /**
     * 查询已完成的出库记录
     */
    @Query("SELECT sor FROM StockOutRecord sor WHERE sor.status = 'COMPLETED' ORDER BY sor.outboundDate DESC")
    List<StockOutRecord> findCompletedRecords();
    
    /**
     * 根据出库原因查询出库记录
     */
    List<StockOutRecord> findByOutboundReasonContainingOrderByCreatedTimeDesc(String reason);
    
    /**
     * 统计指定出库类型的出库总量
     */
    @Query("SELECT COALESCE(SUM(sor.quantity), 0) FROM StockOutRecord sor WHERE sor.outboundType = :outboundType AND sor.status = 'COMPLETED'")
    Double sumOutboundQuantityByType(@Param("outboundType") StockOutRecord.OutboundType outboundType);
    
    /**
     * 统计指定出库类型的出库总金额
     */
    @Query("SELECT COALESCE(SUM(sor.totalAmount), 0) FROM StockOutRecord sor WHERE sor.outboundType = :outboundType AND sor.status = 'COMPLETED'")
    Double sumOutboundAmountByType(@Param("outboundType") StockOutRecord.OutboundType outboundType);
}





