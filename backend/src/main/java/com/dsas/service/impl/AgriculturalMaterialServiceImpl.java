package com.dsas.service.impl;

import com.dsas.entity.AgriculturalMaterial;
import com.dsas.repository.AgriculturalMaterialRepository;
import com.dsas.service.AgriculturalMaterialService;
import com.dsas.service.MaterialStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
@Transactional
public class AgriculturalMaterialServiceImpl implements AgriculturalMaterialService {

    @Autowired
    private AgriculturalMaterialRepository agriculturalMaterialRepository;

    @Override
    public List<AgriculturalMaterial> getAllAgriculturalMaterials() {
        return agriculturalMaterialRepository.findAll();
    }

    @Override
    public Page<AgriculturalMaterial> getAgriculturalMaterials(Pageable pageable) {
        return agriculturalMaterialRepository.findAll(pageable);
    }

    @Override
    public AgriculturalMaterial getAgriculturalMaterialById(Long id) {
        Optional<AgriculturalMaterial> optionalMaterial = agriculturalMaterialRepository.findById(id);
        return optionalMaterial.orElse(null);
    }

    @Override
    public Optional<AgriculturalMaterial> getAgriculturalMaterialByInternalCode(String internalCode) {
        return agriculturalMaterialRepository.findByInternalCode(internalCode);
    }

    @Override
    public AgriculturalMaterial createAgriculturalMaterial(AgriculturalMaterial agriculturalMaterial) {
        // 验证内部编码唯一性
        if (agriculturalMaterial.getInternalCode() != null && 
            agriculturalMaterialRepository.existsByInternalCode(agriculturalMaterial.getInternalCode())) {
            throw new IllegalArgumentException("内部编码已存在: " + agriculturalMaterial.getInternalCode());
        }
        
        // 生成内部编码
        if (agriculturalMaterial.getInternalCode() == null || agriculturalMaterial.getInternalCode().isEmpty()) {
            String internalCode = generateInternalCode();
            agriculturalMaterial.setInternalCode(internalCode);
        }
        
        // 设置默认值
        if (agriculturalMaterial.getCreatedTime() == null) {
            agriculturalMaterial.setCreatedTime(LocalDateTime.now());
        }
        if (agriculturalMaterial.getUpdatedTime() == null) {
            agriculturalMaterial.setUpdatedTime(LocalDateTime.now());
        }
        
        return agriculturalMaterialRepository.save(agriculturalMaterial);
    }

    @Override
    public AgriculturalMaterial updateAgriculturalMaterial(Long id, AgriculturalMaterial agriculturalMaterial) {
        Optional<AgriculturalMaterial> optionalExistingMaterial = agriculturalMaterialRepository.findById(id);
        if (optionalExistingMaterial.isPresent()) {
            AgriculturalMaterial existingMaterial = optionalExistingMaterial.get();
            
            // 验证内部编码唯一性（如果修改了内部编码）
            if (!existingMaterial.getInternalCode().equals(agriculturalMaterial.getInternalCode()) &&
                agriculturalMaterialRepository.existsByInternalCode(agriculturalMaterial.getInternalCode())) {
                throw new IllegalArgumentException("内部编码已存在: " + agriculturalMaterial.getInternalCode());
            }
            
            // 更新字段
            existingMaterial.setInternalCode(agriculturalMaterial.getInternalCode());
            existingMaterial.setCategory(agriculturalMaterial.getCategory());
            existingMaterial.setName(agriculturalMaterial.getName());
            existingMaterial.setBrand(agriculturalMaterial.getBrand());
            existingMaterial.setSpecification(agriculturalMaterial.getSpecification());
            existingMaterial.setUnit(agriculturalMaterial.getUnit());
            existingMaterial.setContent(agriculturalMaterial.getContent());
            existingMaterial.setUnitPrice(agriculturalMaterial.getUnitPrice());
            existingMaterial.setPricePerUnit(agriculturalMaterial.getPricePerUnit());
            existingMaterial.setUpdatedTime(LocalDateTime.now());
            existingMaterial.setRemark(agriculturalMaterial.getRemark());
            
            return agriculturalMaterialRepository.save(existingMaterial);
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteAgriculturalMaterial(Long id) {
        if (agriculturalMaterialRepository.existsById(id)) {
            // 检查是否被使用
            if (isMaterialInUse(id)) {
                throw new IllegalStateException("农资正在使用中，无法删除");
            }
            agriculturalMaterialRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteAgriculturalMaterials(List<Long> ids) {
        boolean allDeleted = true;
        for (Long id : ids) {
            try {
                if (!deleteAgriculturalMaterial(id)) {
                    allDeleted = false;
                }
            } catch (Exception e) {
                allDeleted = false;
            }
        }
        return allDeleted;
    }

    @Override
    public String generateInternalCode() {
        String maxInternalCode = agriculturalMaterialRepository.findMaxInternalCode();
        if (maxInternalCode == null) {
            return "XS-0001";
        } else {
            // 提取数字部分
            String numberStr = maxInternalCode.substring(3);
            int number = Integer.parseInt(numberStr);
            number++;
            return "XS-" + String.format("%04d", number);
        }
    }

    @Override
    public List<AgriculturalMaterial> getAgriculturalMaterialsByCategory(String category) {
        return agriculturalMaterialRepository.findByCategory(category);
    }

    @Override
    public List<AgriculturalMaterial> searchAgriculturalMaterialsByName(String name) {
        return agriculturalMaterialRepository.findByNameContaining(name);
    }

    @Override
    public List<AgriculturalMaterial> getAgriculturalMaterialsBySupplier(String supplier) {
        return agriculturalMaterialRepository.findByBrand(supplier);
    }

    @Override
    public List<AgriculturalMaterial> getActiveAgriculturalMaterials() {
        return agriculturalMaterialRepository.findAll();
    }

    @Override
    public boolean isInternalCodeExists(String internalCode) {
        return agriculturalMaterialRepository.existsByInternalCode(internalCode);
    }

    @Override
    public boolean isMaterialInUse(Long materialId) {
        // TODO: 实现检查农资是否被使用的逻辑
        // 检查是否有库存记录、入库记录或出库记录
        return false; // 暂时返回false
    }

    @Override
    public AgriculturalMaterial toggleMaterialStatus(Long id, boolean isActive) {
        Optional<AgriculturalMaterial> optionalMaterial = agriculturalMaterialRepository.findById(id);
        if (optionalMaterial.isPresent()) {
            AgriculturalMaterial material = optionalMaterial.get();
            material.setUpdatedTime(LocalDateTime.now());
            return agriculturalMaterialRepository.save(material);
        }
        return null;
    }

    @Override
    public MaterialStatistics getMaterialStatistics() {
        Long totalCount = agriculturalMaterialRepository.countAllMaterials();
        Long activeCount = agriculturalMaterialRepository.countActiveMaterials();
        Long inactiveCount = agriculturalMaterialRepository.countInactiveMaterials();
        Long categoryCount = agriculturalMaterialRepository.countDistinctCategories();
        Long supplierCount = agriculturalMaterialRepository.countDistinctSuppliers();
        
        // 注意：这里需要根据实际的业务逻辑来计算总价值和平均价格
        // 可能需要关联库存表来计算
        return new MaterialStatistics(totalCount, activeCount, inactiveCount, 
                                     categoryCount, supplierCount, 
                                     null, null); // 暂时设为null
    }
}