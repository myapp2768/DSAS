package com.dsas.controller;

import com.dsas.entity.AgriculturalMaterial;
import com.dsas.service.AgriculturalMaterialService;
import com.dsas.service.MaterialStatistics;
import com.dsas.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/agricultural-materials")
@Validated
public class AgriculturalMaterialController {

    private static final Logger logger = LoggerFactory.getLogger(AgriculturalMaterialController.class);

    @Autowired
    private AgriculturalMaterialService agriculturalMaterialService;

    public AgriculturalMaterialController() {
        logger.info("AgriculturalMaterialController initialized!");
    }

    // 测试端点
    @GetMapping("/test")
    public String test() {
        logger.info("Test endpoint called");
        return "Agricultural Material API is working!";
    }

    // 获取所有农资编码
    @GetMapping
    public List<AgriculturalMaterial> getAllAgriculturalMaterials() {
        return agriculturalMaterialService.getAllAgriculturalMaterials();
    }

    // 分页获取农资编码
    @GetMapping("/page")
    public Page<AgriculturalMaterial> getAgriculturalMaterials(Pageable pageable) {
        return agriculturalMaterialService.getAgriculturalMaterials(pageable);
    }

    // 根据ID获取农资编码
    @GetMapping("/{id}")
    public ResponseEntity<AgriculturalMaterial> getAgriculturalMaterialById(@PathVariable Long id) {
        AgriculturalMaterial material = agriculturalMaterialService.getAgriculturalMaterialById(id);
        if (material == null) {
            throw new ResourceNotFoundException("农资编码不存在，ID: " + id);
        }
        return ResponseEntity.ok(material);
    }

    // 根据内部编码获取农资
    @GetMapping("/code/{internalCode}")
    public ResponseEntity<AgriculturalMaterial> getAgriculturalMaterialByInternalCode(@PathVariable String internalCode) {
        return agriculturalMaterialService.getAgriculturalMaterialByInternalCode(internalCode)
                .map(material -> ResponseEntity.ok(material))
                .orElseThrow(() -> new ResourceNotFoundException("农资编码不存在: " + internalCode));
    }

    // 创建农资编码
    @PostMapping
    public ResponseEntity<AgriculturalMaterial> createAgriculturalMaterial(@Valid @RequestBody AgriculturalMaterial agriculturalMaterial) {
        try {
            AgriculturalMaterial created = agriculturalMaterialService.createAgriculturalMaterial(agriculturalMaterial);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 更新农资编码
    @PutMapping("/{id}")
    public ResponseEntity<AgriculturalMaterial> updateAgriculturalMaterial(@PathVariable Long id, @Valid @RequestBody AgriculturalMaterial agriculturalMaterial) {
        try {
            AgriculturalMaterial updatedMaterial = agriculturalMaterialService.updateAgriculturalMaterial(id, agriculturalMaterial);
            if (updatedMaterial == null) {
                throw new ResourceNotFoundException("农资编码不存在，ID: " + id);
            }
            return ResponseEntity.ok(updatedMaterial);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 删除农资编码
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgriculturalMaterial(@PathVariable Long id) {
        try {
            boolean deleted = agriculturalMaterialService.deleteAgriculturalMaterial(id);
            if (!deleted) {
                throw new ResourceNotFoundException("农资编码不存在，ID: " + id);
            }
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // 批量删除农资编码
    @DeleteMapping("/batch")
    public ResponseEntity<Map<String, Object>> deleteAgriculturalMaterials(@RequestBody List<Long> ids) {
        boolean allDeleted = agriculturalMaterialService.deleteAgriculturalMaterials(ids);
        Map<String, Object> response = new HashMap<>();
        response.put("success", allDeleted);
        response.put("message", allDeleted ? "批量删除成功" : "部分删除失败");
        return ResponseEntity.ok(response);
    }

    // 根据种类获取农资
    @GetMapping("/category/{category}")
    public List<AgriculturalMaterial> getAgriculturalMaterialsByCategory(@PathVariable String category) {
        return agriculturalMaterialService.getAgriculturalMaterialsByCategory(category);
    }

    // 根据名称搜索农资
    @GetMapping("/search")
    public List<AgriculturalMaterial> searchAgriculturalMaterialsByName(@RequestParam String name) {
        return agriculturalMaterialService.searchAgriculturalMaterialsByName(name);
    }

    // 根据供应商获取农资
    @GetMapping("/supplier/{supplier}")
    public List<AgriculturalMaterial> getAgriculturalMaterialsBySupplier(@PathVariable String supplier) {
        return agriculturalMaterialService.getAgriculturalMaterialsBySupplier(supplier);
    }

    // 获取启用的农资
    @GetMapping("/active")
    public List<AgriculturalMaterial> getActiveAgriculturalMaterials() {
        return agriculturalMaterialService.getActiveAgriculturalMaterials();
    }

    // 检查内部编码是否存在
    @GetMapping("/check-code/{internalCode}")
    public ResponseEntity<Map<String, Boolean>> checkInternalCodeExists(@PathVariable String internalCode) {
        boolean exists = agriculturalMaterialService.isInternalCodeExists(internalCode);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    // 生成新的内部编码
    @GetMapping("/generate-code")
    public ResponseEntity<Map<String, String>> generateInternalCode() {
        String code = agriculturalMaterialService.generateInternalCode();
        Map<String, String> response = new HashMap<>();
        response.put("internalCode", code);
        return ResponseEntity.ok(response);
    }

    // 启用/禁用农资
    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<AgriculturalMaterial> toggleMaterialStatus(@PathVariable Long id, @RequestParam boolean isActive) {
        AgriculturalMaterial material = agriculturalMaterialService.toggleMaterialStatus(id, isActive);
        if (material == null) {
            throw new ResourceNotFoundException("农资编码不存在，ID: " + id);
        }
        return ResponseEntity.ok(material);
    }

    // 获取农资统计信息
    @GetMapping("/statistics")
    public ResponseEntity<MaterialStatistics> getMaterialStatistics() {
        MaterialStatistics statistics = agriculturalMaterialService.getMaterialStatistics();
        return ResponseEntity.ok(statistics);
    }

    // 多条件搜索
    @GetMapping("/search/advanced")
    public Page<AgriculturalMaterial> searchByMultipleConditions(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String supplier,
            @RequestParam(required = false) Boolean isActive,
            Pageable pageable) {
        // 这里需要实现多条件搜索的逻辑
        return agriculturalMaterialService.getAgriculturalMaterials(pageable);
    }
}