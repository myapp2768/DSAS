package com.dsas.repository;

import com.dsas.entity.StockInRecord;
import com.dsas.entity.AgriculturalMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 入库记录Repository
 */
@Repository
public interface StockInRecordRepository extends JpaRepository<StockInRecord, Long> {
    
    /**
     * 根据入库单号查询
     */
    Optional<StockInRecord> findByInboundNumber(String inboundNumber);
    
    /**
     * 根据农资ID查询入库记录
     */
    List<StockInRecord> findByMaterialIdOrderByCreatedTimeDesc(Long materialId);
    
    /**
     * 根据农资查询入库记录
     */
    List<StockInRecord> findByMaterialOrderByCreatedTimeDesc(AgriculturalMaterial material);
    
    /**
     * 根据状态查询入库记录
     */
    List<StockInRecord> findByStatusOrderByCreatedTimeDesc(StockInRecord.Status status);
    
    /**
     * 根据供应商查询入库记录
     */
    List<StockInRecord> findBySupplierNameOrderByCreatedTimeDesc(String supplierName);
    
    /**
     * 根据操作员查询入库记录
     */
    List<StockInRecord> findByOperatorNameOrderByCreatedTimeDesc(String operatorName);
    
    /**
     * 根据时间范围查询入库记录
     */
    @Query("SELECT sir FROM StockInRecord sir WHERE sir.createdTime BETWEEN :startTime AND :endTime ORDER BY sir.createdTime DESC")
    List<StockInRecord> findByTimeRange(@Param("startTime") LocalDateTime startTime, 
                                       @Param("endTime") LocalDateTime endTime);
    
    /**
     * 根据农资ID和时间范围查询入库记录
     */
    @Query("SELECT sir FROM StockInRecord sir WHERE sir.material.id = :materialId AND sir.createdTime BETWEEN :startTime AND :endTime ORDER BY sir.createdTime DESC")
    List<StockInRecord> findByMaterialIdAndTimeRange(@Param("materialId") Long materialId,
                                                    @Param("startTime") LocalDateTime startTime,
                                                    @Param("endTime") LocalDateTime endTime);
    
    /**
     * 根据批次号查询入库记录
     */
    List<StockInRecord> findByBatchNumberOrderByCreatedTimeDesc(String batchNumber);
    
    /**
     * 统计指定农资的入库总量
     */
    @Query("SELECT COALESCE(SUM(sir.quantity), 0) FROM StockInRecord sir WHERE sir.material.id = :materialId AND sir.status = 'COMPLETED'")
    Double sumInboundQuantityByMaterialId(@Param("materialId") Long materialId);
    
    /**
     * 统计指定时间范围内的入库总量
     */
    @Query("SELECT COALESCE(SUM(sir.quantity), 0) FROM StockInRecord sir WHERE sir.createdTime BETWEEN :startTime AND :endTime AND sir.status = 'COMPLETED'")
    Double sumInboundQuantityByTimeRange(@Param("startTime") LocalDateTime startTime, 
                                        @Param("endTime") LocalDateTime endTime);
    
    /**
     * 统计指定农资的入库总金额
     */
    @Query("SELECT COALESCE(SUM(sir.totalAmount), 0) FROM StockInRecord sir WHERE sir.material.id = :materialId AND sir.status = 'COMPLETED'")
    Double sumInboundAmountByMaterialId(@Param("materialId") Long materialId);
    
    /**
     * 查询待处理的入库记录
     */
    @Query("SELECT sir FROM StockInRecord sir WHERE sir.status = 'PENDING' ORDER BY sir.createdTime ASC")
    List<StockInRecord> findPendingRecords();
    
    /**
     * 查询已完成的入库记录
     */
    @Query("SELECT sir FROM StockInRecord sir WHERE sir.status = 'COMPLETED' ORDER BY sir.inboundDate DESC")
    List<StockInRecord> findCompletedRecords();
    
    /**
     * 根据质量状态查询入库记录
     */
    List<StockInRecord> findByQualityStatusOrderByCreatedTimeDesc(String qualityStatus);
    
    /**
     * 查询即将过期的入库记录
     */
    @Query("SELECT sir FROM StockInRecord sir WHERE sir.expiryDate IS NOT NULL AND sir.expiryDate <= :expiryDate ORDER BY sir.expiryDate ASC")
    List<StockInRecord> findExpiringRecords(@Param("expiryDate") LocalDateTime expiryDate);
}





