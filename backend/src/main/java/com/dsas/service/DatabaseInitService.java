package com.dsas.service;

import com.dsas.entity.AgriculturalMaterial;
import com.dsas.entity.MaterialInventory;
import com.dsas.entity.MaterialInRecord;
import com.dsas.entity.MaterialOutRecord;
import com.dsas.repository.AgriculturalMaterialRepository;
import com.dsas.repository.MaterialInventoryRepository;
import com.dsas.repository.MaterialInRecordRepository;
import com.dsas.repository.MaterialOutRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class DatabaseInitService {

    @Autowired
    private AgriculturalMaterialRepository agriculturalMaterialRepository;
    
    @Autowired
    private MaterialInventoryRepository materialInventoryRepository;
    
    @Autowired
    private MaterialInRecordRepository materialInRecordRepository;
    
    @Autowired
    private MaterialOutRecordRepository materialOutRecordRepository;

    public void initRealData() {
        // 清空现有数据
        materialInventoryRepository.deleteAll();
        materialInRecordRepository.deleteAll();
        materialOutRecordRepository.deleteAll();
        agriculturalMaterialRepository.deleteAll();

        // 创建农资编码数据
        AgriculturalMaterial material1 = createMaterial("XS-0001", "肥料", "中量元素水溶肥", "中化摩彩", "10kg/包", "kg", 10.00, 36.0656, 3.6066, "中量元素水溶肥，适用于各种作物");
        AgriculturalMaterial material2 = createMaterial("XS-0002", "肥料", "中量元素水溶肥料", "中化", "20kg/包", "kg", 20.00, 72.1443, 3.6072, "中量元素肥料");
        AgriculturalMaterial material3 = createMaterial("XS-0003", "肥料", "平衡肥19-19-19", "海法", "5L/桶", "L", 5.00, 347.50, 69.5000, "平衡型肥料");
        AgriculturalMaterial material4 = createMaterial("XS-0004", "肥料", "平衡肥20-20-20", "吉尔肥施通", "25kg/包", "kg", 25.00, 347.50, 13.9000, "平衡型肥料");
        AgriculturalMaterial material5 = createMaterial("XS-0005", "肥料", "高钾肥15-5-30", "绿世界", "25kg/包", "kg", 25.00, 384.37, 15.3749, "高钾型肥料");
        AgriculturalMaterial material6 = createMaterial("XS-0006", "肥料", "高钾肥16-8-32", "海法", "10kg/包", "kg", 10.00, 157.00, 15.7000, "高钾型肥料");
        AgriculturalMaterial material7 = createMaterial("XS-0007", "肥料", "高磷肥8-32-10", "绿世界", "25kg/包", "kg", 25.00, 347.73, 13.9092, "高磷型肥料");
        AgriculturalMaterial material8 = createMaterial("XS-0008", "肥料", "高磷肥10-34-18", "吉尔肥施通", "10kg/包", "kg", 10.00, 322.50, 32.2500, "高磷型肥料");
        AgriculturalMaterial material9 = createMaterial("XS-0009", "肥料", "高氮肥30-10-10", "吉尔肥施通", "25kg/包", "kg", 25.00, 445.11, 17.8045, "高氮型肥料");
        AgriculturalMaterial material10 = createMaterial("XS-0010", "肥料", "钙镁硼锌铁铜", "银海化工", "25kg/包", "kg", 25.00, 435.65, 17.4259, "微量元素肥料");

        // 保存农资编码
        List<AgriculturalMaterial> materials = List.of(material1, material2, material3, material4, material5, material6, material7, material8, material9, material10);
        agriculturalMaterialRepository.saveAll(materials);

        // 创建库存数据
        createInventory(material1, "XS-0001", 55.39, 855.39, 800.00, 0.00, 3.6066, 3085.00, "1号药房");
        createInventory(material2, "XS-0002", 193.85, 193.85, 0.00, 0.00, 3.6072, 699.26, "2号药房");
        createInventory(material3, "XS-0003", 0.00, 0.00, 0.00, 0.00, 69.5000, 0.00, "3号药房");
        createInventory(material4, "XS-0004", 776.75, 773.25, 0.00, 3.50, 13.9000, 10748.18, "4号药房");
        createInventory(material5, "XS-0005", 0.00, 0.00, 0.00, 0.00, 15.3749, 0.00, "5号药房");
        createInventory(material6, "XS-0006", 468.40, 468.40, 0.00, 0.00, 15.7000, 7353.88, "6号药房");
        createInventory(material7, "XS-0007", 0.00, 0.00, 0.00, 0.00, 13.9092, 0.00, "7号药房");
        createInventory(material8, "XS-0008", 351.80, 351.80, 0.00, 0.00, 32.2500, 11345.55, "8号药房");
        createInventory(material9, "XS-0009", 47.58, 47.58, 0.00, 0.00, 17.8045, 847.14, "9号药房");
        createInventory(material10, "XS-0010", 11.00, 11.00, 0.00, 0.00, 17.4259, 191.68, "10号药房");

        // 创建入库记录
        createInRecord(material1, "XS-0001", LocalDate.of(2024, 9, 10), 800.00, 3.6066, "1号药房", "张栋杰", "陈春秀", "采购入库");

        // 创建出库记录
        createOutRecord(material4, "XS-0004", LocalDate.of(2024, 9, 1), 3.50, 13.9000, 48.65, "周尚平", "使用", "家庭农场", "陈香秀", "农场使用");
    }

    private AgriculturalMaterial createMaterial(String internalCode, String category, String name, 
                                               String brand, String specification, String unit,
                                               double content, double unitPrice, double pricePerUnit, 
                                               String remark) {
        AgriculturalMaterial material = new AgriculturalMaterial();
        material.setInternalCode(internalCode);
        material.setCategory(category);
        material.setName(name);
        material.setBrand(brand);
        material.setSpecification(specification);
        material.setUnit(unit);
        material.setContent(BigDecimal.valueOf(content));
        material.setUnitPrice(BigDecimal.valueOf(unitPrice));
        material.setPricePerUnit(BigDecimal.valueOf(pricePerUnit));
        material.setRemark(remark);
        material.setCreatedTime(LocalDateTime.now());
        material.setUpdatedTime(LocalDateTime.now());
        return material;
    }

    private void createInventory(AgriculturalMaterial material, String internalCode, 
                               double initialStock, double currentQuantity, double totalInQuantity, 
                               double totalOutQuantity, double unitPrice, double stockAmount, 
                               String location) {
        MaterialInventory inventory = new MaterialInventory();
        inventory.setMaterial(material);
        inventory.setInternalCode(internalCode);
        inventory.setInitialStock(BigDecimal.valueOf(initialStock));
        inventory.setCurrentQuantity(BigDecimal.valueOf(currentQuantity));
        inventory.setTotalInQuantity(BigDecimal.valueOf(totalInQuantity));
        inventory.setTotalOutQuantity(BigDecimal.valueOf(totalOutQuantity));
        inventory.setUnitPrice(BigDecimal.valueOf(unitPrice));
        inventory.setStockAmount(BigDecimal.valueOf(stockAmount));
        inventory.setLocation(location);
        inventory.setLastUpdated(LocalDateTime.now());
        materialInventoryRepository.save(inventory);
    }

    private void createInRecord(AgriculturalMaterial material, String internalCode, 
                              LocalDate inDate, double quantity, double unitPrice, 
                              String location, String inPerson, String recorder, String remark) {
        MaterialInRecord inRecord = new MaterialInRecord();
        inRecord.setMaterial(material);
        inRecord.setInternalCode(internalCode);
        inRecord.setInDate(inDate);
        inRecord.setQuantity(BigDecimal.valueOf(quantity));
        inRecord.setUnitPrice(BigDecimal.valueOf(unitPrice));
        inRecord.setLocation(location);
        inRecord.setInPerson(inPerson);
        inRecord.setRecorder(recorder);
        inRecord.setRemark(remark);
        inRecord.setCreatedTime(LocalDateTime.now());
        materialInRecordRepository.save(inRecord);
    }

    private void createOutRecord(AgriculturalMaterial material, String internalCode, 
                               LocalDate outDate, double quantity, double unitPrice, 
                               double totalAmount, String outPerson, String outType, 
                               String purpose, String recorder, String remark) {
        MaterialOutRecord outRecord = new MaterialOutRecord();
        outRecord.setMaterial(material);
        outRecord.setInternalCode(internalCode);
        outRecord.setOutDate(outDate);
        outRecord.setQuantity(BigDecimal.valueOf(quantity));
        outRecord.setUnitPrice(BigDecimal.valueOf(unitPrice));
        outRecord.setTotalAmount(BigDecimal.valueOf(totalAmount));
        outRecord.setOutPerson(outPerson);
        outRecord.setOutType(outType);
        outRecord.setPurpose(purpose);
        outRecord.setRecorder(recorder);
        outRecord.setRemark(remark);
        outRecord.setCreatedTime(LocalDateTime.now());
        materialOutRecordRepository.save(outRecord);
    }
}





