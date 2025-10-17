-- DSAS农资管理系统数据库初始化脚本
-- 基于用户提供的数据库样例设计

-- 创建农资编码表
CREATE TABLE IF NOT EXISTS agricultural_materials (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    internal_code VARCHAR(20) UNIQUE NOT NULL COMMENT '内部编码: XS-0001',
    category VARCHAR(50) NOT NULL COMMENT '种类: 肥料',
    name VARCHAR(100) NOT NULL COMMENT '名称: 中量元素水溶肥',
    brand VARCHAR(50) COMMENT '品牌: 中化摩彩',
    specification VARCHAR(100) COMMENT '规格: 10kg/包',
    unit VARCHAR(20) COMMENT '单位: kg',
    content DECIMAL(10,2) COMMENT '含量: 10',
    unit_price DECIMAL(10,4) COMMENT '单价: 36.0656',
    price_per_unit DECIMAL(10,4) COMMENT '单位价: 3.6066',
    remark TEXT COMMENT '备注',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='农资编码表';

-- 创建入库明细表
CREATE TABLE IF NOT EXISTS material_in_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    material_id BIGINT NOT NULL COMMENT '关联农资编码ID',
    internal_code VARCHAR(20) NOT NULL COMMENT '内部编码',
    in_date DATE NOT NULL COMMENT '入库日期: 2024-09-10',
    quantity DECIMAL(10,2) NOT NULL COMMENT '入库数量: 800',
    unit_price DECIMAL(10,4) NOT NULL COMMENT '单位价',
    location VARCHAR(100) COMMENT '入库位置: 1号药房',
    in_person VARCHAR(50) COMMENT '入库人: 张栋杰',
    recorder VARCHAR(50) COMMENT '录入人: 陈春秀',
    remark TEXT COMMENT '备注',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (material_id) REFERENCES agricultural_materials(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='入库明细表';

-- 创建出库明细表
CREATE TABLE IF NOT EXISTS material_out_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    material_id BIGINT NOT NULL COMMENT '关联农资编码ID',
    internal_code VARCHAR(20) NOT NULL COMMENT '内部编码',
    out_date DATE NOT NULL COMMENT '出库日期: 2024-09-01',
    quantity DECIMAL(10,2) NOT NULL COMMENT '出库数量: 3.5',
    unit_price DECIMAL(10,4) NOT NULL COMMENT '单位价',
    total_amount DECIMAL(10,2) COMMENT '出库金额',
    out_person VARCHAR(50) COMMENT '出库人: 周尚平',
    out_type VARCHAR(50) COMMENT '出入形式',
    purpose VARCHAR(100) COMMENT '物料用途: 家庭农场',
    recorder VARCHAR(50) COMMENT '录入人: 陈春秀',
    remark TEXT COMMENT '备注',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (material_id) REFERENCES agricultural_materials(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='出库明细表';

-- 创建库存明细表
CREATE TABLE IF NOT EXISTS material_inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    material_id BIGINT NOT NULL COMMENT '关联农资编码ID',
    internal_code VARCHAR(20) NOT NULL COMMENT '内部编码',
    initial_stock DECIMAL(10,2) DEFAULT 0 COMMENT '初始库存',
    current_quantity DECIMAL(10,2) DEFAULT 0 COMMENT '当前库存数量',
    total_in_quantity DECIMAL(10,2) DEFAULT 0 COMMENT '总入库数量',
    total_out_quantity DECIMAL(10,2) DEFAULT 0 COMMENT '总出库数量',
    unit_price DECIMAL(10,4) COMMENT '当前单位价',
    stock_amount DECIMAL(10,2) DEFAULT 0 COMMENT '库存金额',
    location VARCHAR(100) COMMENT '库存位置',
    last_updated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    FOREIGN KEY (material_id) REFERENCES agricultural_materials(id) ON DELETE CASCADE,
    UNIQUE KEY unique_material (material_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存明细表';

-- 插入示例数据
-- 农资编码示例数据
INSERT INTO agricultural_materials (internal_code, category, name, brand, specification, unit, content, unit_price, price_per_unit, remark) VALUES
('XS-0001', '肥料', '中量元素水溶肥', '中化摩彩', '10kg/包', 'kg', 10.00, 36.0656, 3.6066, '中量元素水溶肥，适用于各种作物'),
('XS-0002', '肥料', '中量元素水溶肥料', '中化', '20kg/包', 'kg', 20.00, 72.1443, 3.6072, '中量元素肥料'),
('XS-0003', '肥料', '平衡肥19-19-19', '海法', '5L/桶', 'L', 5.00, 347.50, 69.5000, '平衡型肥料'),
('XS-0004', '肥料', '平衡肥20-20-20', '吉尔肥施通', '25kg/包', 'kg', 25.00, 347.50, 13.9000, '平衡型肥料'),
('XS-0005', '肥料', '高钾肥15-5-30', '绿世界', '25kg/包', 'kg', 25.00, 384.37, 15.3749, '高钾型肥料'),
('XS-0006', '肥料', '高钾肥16-8-32', '海法', '10kg/包', 'kg', 10.00, 157.00, 15.7000, '高钾型肥料'),
('XS-0007', '肥料', '高磷肥8-32-10', '绿世界', '25kg/包', 'kg', 25.00, 347.73, 13.9092, '高磷型肥料'),
('XS-0008', '肥料', '高磷肥10-34-18', '吉尔肥施通', '10kg/包', 'kg', 10.00, 322.50, 32.2500, '高磷型肥料'),
('XS-0009', '肥料', '高氮肥30-10-10', '吉尔肥施通', '25kg/包', 'kg', 25.00, 445.11, 17.8045, '高氮型肥料'),
('XS-0010', '肥料', '钙镁硼锌铁铜', '银海化工', '25kg/包', 'kg', 25.00, 435.65, 17.4259, '微量元素肥料');

-- 库存明细示例数据
INSERT INTO material_inventory (material_id, internal_code, initial_stock, current_quantity, total_in_quantity, total_out_quantity, unit_price, stock_amount, location) VALUES
(1, 'XS-0001', 55.39, 855.39, 800.00, 0.00, 3.6066, 3085.00, '1号药房'),
(2, 'XS-0002', 193.85, 193.85, 0.00, 0.00, 3.6072, 699.26, '2号药房'),
(3, 'XS-0003', 0.00, 0.00, 0.00, 0.00, 69.5000, 0.00, '3号药房'),
(4, 'XS-0004', 776.75, 773.25, 0.00, 3.50, 13.9000, 10748.18, '4号药房'),
(5, 'XS-0005', 0.00, 0.00, 0.00, 0.00, 15.3749, 0.00, '5号药房'),
(6, 'XS-0006', 468.40, 468.40, 0.00, 0.00, 15.7000, 7353.88, '6号药房'),
(7, 'XS-0007', 0.00, 0.00, 0.00, 0.00, 13.9092, 0.00, '7号药房'),
(8, 'XS-0008', 351.80, 351.80, 0.00, 0.00, 32.2500, 11345.55, '8号药房'),
(9, 'XS-0009', 47.58, 47.58, 0.00, 0.00, 17.8045, 847.14, '9号药房'),
(10, 'XS-0010', 11.00, 11.00, 0.00, 0.00, 17.4259, 191.68, '10号药房');

-- 入库明细示例数据
INSERT INTO material_in_records (material_id, internal_code, in_date, quantity, unit_price, location, in_person, recorder, remark) VALUES
(1, 'XS-0001', '2024-09-10', 800.00, 3.6066, '1号药房', '张栋杰', '陈春秀', '采购入库');

-- 出库明细示例数据
INSERT INTO material_out_records (material_id, internal_code, out_date, quantity, unit_price, total_amount, out_person, out_type, purpose, recorder, remark) VALUES
(4, 'XS-0004', '2024-09-01', 3.50, 13.9000, 48.65, '周尚平', '使用', '家庭农场', '陈香秀', '农场使用');

-- 创建索引以提高查询性能
CREATE INDEX idx_agricultural_materials_category ON agricultural_materials(category);
CREATE INDEX idx_agricultural_materials_name ON agricultural_materials(name);
CREATE INDEX idx_material_in_records_date ON material_in_records(in_date);
CREATE INDEX idx_material_in_records_material ON material_in_records(material_id);
CREATE INDEX idx_material_out_records_date ON material_out_records(out_date);
CREATE INDEX idx_material_out_records_material ON material_out_records(material_id);
CREATE INDEX idx_material_inventory_material ON material_inventory(material_id);

-- 创建触发器：入库时自动更新库存
DELIMITER //
CREATE TRIGGER tr_material_in_update_inventory
AFTER INSERT ON material_in_records
FOR EACH ROW
BEGIN
    INSERT INTO material_inventory (material_id, internal_code, current_quantity, total_in_quantity, unit_price, stock_amount, location)
    VALUES (NEW.material_id, NEW.internal_code, NEW.quantity, NEW.quantity, NEW.unit_price, NEW.quantity * NEW.unit_price, NEW.location)
    ON DUPLICATE KEY UPDATE
        current_quantity = current_quantity + NEW.quantity,
        total_in_quantity = total_in_quantity + NEW.quantity,
        unit_price = NEW.unit_price,
        stock_amount = current_quantity * NEW.unit_price,
        location = NEW.location,
        last_updated = CURRENT_TIMESTAMP;
END//
DELIMITER ;

-- 创建触发器：出库时自动更新库存
DELIMITER //
CREATE TRIGGER tr_material_out_update_inventory
AFTER INSERT ON material_out_records
FOR EACH ROW
BEGIN
    UPDATE material_inventory SET
        current_quantity = current_quantity - NEW.quantity,
        total_out_quantity = total_out_quantity + NEW.quantity,
        stock_amount = current_quantity * unit_price,
        last_updated = CURRENT_TIMESTAMP
    WHERE material_id = NEW.material_id;
END//
DELIMITER ;