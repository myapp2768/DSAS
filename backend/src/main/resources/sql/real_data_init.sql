-- 农资编码真实初始数据
-- 删除现有数据（如果存在）
DELETE FROM material_inventory WHERE material_id IN (SELECT id FROM agricultural_materials);
DELETE FROM material_in_records WHERE material_id IN (SELECT id FROM agricultural_materials);
DELETE FROM material_out_records WHERE material_id IN (SELECT id FROM agricultural_materials);
DELETE FROM agricultural_materials;

-- 重置自增ID
ALTER TABLE agricultural_materials AUTO_INCREMENT = 1;
ALTER TABLE material_inventory AUTO_INCREMENT = 1;
ALTER TABLE material_in_records AUTO_INCREMENT = 1;
ALTER TABLE material_out_records AUTO_INCREMENT = 1;

-- 插入农资编码真实数据
INSERT INTO agricultural_materials (internal_code, category, name, brand, specification, unit, content, unit_price, price_per_unit, remark, created_time, updated_time) VALUES
('XS-0001', '肥料', '中量元素水溶肥', '中化摩彩', '10kg/包', 'kg', 10.00, 36.0656, 3.6066, '中量元素水溶肥，适用于各种作物', NOW(), NOW()),
('XS-0002', '肥料', '中量元素水溶肥料', '中化', '20kg/包', 'kg', 20.00, 72.1443, 3.6072, '中量元素肥料', NOW(), NOW()),
('XS-0003', '肥料', '平衡肥19-19-19', '海法', '5L/桶', 'L', 5.00, 347.50, 69.5000, '平衡型肥料', NOW(), NOW()),
('XS-0004', '肥料', '平衡肥20-20-20', '吉尔肥施通', '25kg/包', 'kg', 25.00, 347.50, 13.9000, '平衡型肥料', NOW(), NOW()),
('XS-0005', '肥料', '高钾肥15-5-30', '绿世界', '25kg/包', 'kg', 25.00, 384.37, 15.3749, '高钾型肥料', NOW(), NOW()),
('XS-0006', '肥料', '高钾肥16-8-32', '海法', '10kg/包', 'kg', 10.00, 157.00, 15.7000, '高钾型肥料', NOW(), NOW()),
('XS-0007', '肥料', '高磷肥8-32-10', '绿世界', '25kg/包', 'kg', 25.00, 347.73, 13.9092, '高磷型肥料', NOW(), NOW()),
('XS-0008', '肥料', '高磷肥10-34-18', '吉尔肥施通', '10kg/包', 'kg', 10.00, 322.50, 32.2500, '高磷型肥料', NOW(), NOW()),
('XS-0009', '肥料', '高氮肥30-10-10', '吉尔肥施通', '25kg/包', 'kg', 25.00, 445.11, 17.8045, '高氮型肥料', NOW(), NOW()),
('XS-0010', '肥料', '钙镁硼锌铁铜', '银海化工', '25kg/包', 'kg', 25.00, 435.65, 17.4259, '微量元素肥料', NOW(), NOW());

-- 插入对应的库存数据
INSERT INTO material_inventory (material_id, internal_code, initial_stock, current_quantity, total_in_quantity, total_out_quantity, unit_price, stock_amount, location, last_updated) VALUES
(1, 'XS-0001', 55.39, 855.39, 800.00, 0.00, 3.6066, 3085.00, '1号药房', NOW()),
(2, 'XS-0002', 193.85, 193.85, 0.00, 0.00, 3.6072, 699.26, '2号药房', NOW()),
(3, 'XS-0003', 0.00, 0.00, 0.00, 0.00, 69.5000, 0.00, '3号药房', NOW()),
(4, 'XS-0004', 776.75, 773.25, 0.00, 3.50, 13.9000, 10748.18, '4号药房', NOW()),
(5, 'XS-0005', 0.00, 0.00, 0.00, 0.00, 15.3749, 0.00, '5号药房', NOW()),
(6, 'XS-0006', 468.40, 468.40, 0.00, 0.00, 15.7000, 7353.88, '6号药房', NOW()),
(7, 'XS-0007', 0.00, 0.00, 0.00, 0.00, 13.9092, 0.00, '7号药房', NOW()),
(8, 'XS-0008', 351.80, 351.80, 0.00, 0.00, 32.2500, 11345.55, '8号药房', NOW()),
(9, 'XS-0009', 47.58, 47.58, 0.00, 0.00, 17.8045, 847.14, '9号药房', NOW()),
(10, 'XS-0010', 11.00, 11.00, 0.00, 0.00, 17.4259, 191.68, '10号药房', NOW());

-- 插入入库记录
INSERT INTO material_in_records (material_id, internal_code, in_date, quantity, unit_price, location, in_person, recorder, remark, created_time) VALUES
(1, 'XS-0001', '2024-09-10', 800.00, 3.6066, '1号药房', '张栋杰', '陈春秀', '采购入库', NOW());

-- 插入出库记录
INSERT INTO material_out_records (material_id, internal_code, out_date, quantity, unit_price, total_amount, out_person, out_type, purpose, recorder, remark, created_time) VALUES
(4, 'XS-0004', '2024-09-01', 3.50, 13.9000, 48.65, '周尚平', '使用', '家庭农场', '陈香秀', '农场使用', NOW());





