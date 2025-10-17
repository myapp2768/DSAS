package com.dsas.service;

import com.dsas.entity.AgriculturalMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AgriculturalMaterialService {

    // 获取所有农资编码
    List<AgriculturalMaterial> getAllAgriculturalMaterials();

    // 分页获取农资编码
    Page<AgriculturalMaterial> getAgriculturalMaterials(Pageable pageable);

    // 根据ID获取农资编码
    AgriculturalMaterial getAgriculturalMaterialById(Long id);

    // 根据内部编码获取农资
    Optional<AgriculturalMaterial> getAgriculturalMaterialByInternalCode(String internalCode);

    // 创建农资编码
    AgriculturalMaterial createAgriculturalMaterial(AgriculturalMaterial agriculturalMaterial);

    // 更新农资编码
    AgriculturalMaterial updateAgriculturalMaterial(Long id, AgriculturalMaterial agriculturalMaterial);

    // 删除农资编码
    boolean deleteAgriculturalMaterial(Long id);

    // 批量删除农资编码
    boolean deleteAgriculturalMaterials(List<Long> ids);

    // 生成内部编码
    String generateInternalCode();

    // 根据种类获取农资
    List<AgriculturalMaterial> getAgriculturalMaterialsByCategory(String category);

    // 根据名称模糊查询农资
    List<AgriculturalMaterial> searchAgriculturalMaterialsByName(String name);

    // 根据供应商获取农资
    List<AgriculturalMaterial> getAgriculturalMaterialsBySupplier(String supplier);

    // 获取启用的农资
    List<AgriculturalMaterial> getActiveAgriculturalMaterials();

    // 检查内部编码是否已存在
    boolean isInternalCodeExists(String internalCode);

    // 检查农资是否被使用（有库存或出入库记录）
    boolean isMaterialInUse(Long materialId);

    // 启用/禁用农资
    AgriculturalMaterial toggleMaterialStatus(Long id, boolean isActive);

    // 获取农资统计信息
    MaterialStatistics getMaterialStatistics();
}