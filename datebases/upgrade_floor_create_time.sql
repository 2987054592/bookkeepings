-- ============================================================
-- 记账系统升级脚本：新增楼层(floor) + 创建时间(create_time)
-- 特点：只做 ALTER 增列，不删表不改数据，现有数据零丢失。
-- 执行方式：在 MySQL 8.0 客户端中直接执行（库：bookkeeping）。
-- 幂等性说明：MySQL 8.0 不支持 ADD COLUMN IF NOT EXISTS，
--   若重复执行会报 Duplicate column name 错误，忽略即可。
-- ============================================================

-- 1. 员工表：楼层(存量数据统一 3 楼) + 创建时间；唯一键 name 改为 (name, floor)
ALTER TABLE `employee`
    ADD COLUMN `floor` int NOT NULL DEFAULT 3 COMMENT '楼层' AFTER `name`,
    ADD COLUMN `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间';

ALTER TABLE `employee`
    DROP INDEX `name_UNIQUE`,
    ADD UNIQUE KEY `uk_name_floor` (`name`, `floor`);

-- 2. 书包表：同上
ALTER TABLE `bag`
    ADD COLUMN `floor` int NOT NULL DEFAULT 3 COMMENT '楼层' AFTER `name`,
    ADD COLUMN `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间';

ALTER TABLE `bag`
    DROP INDEX `name_UNIQUE`,
    ADD UNIQUE KEY `uk_name_floor` (`name`, `floor`);

-- 3. 订单表：楼层 + 创建时间
ALTER TABLE `orders`
    ADD COLUMN `floor` int NOT NULL DEFAULT 3 COMMENT '楼层' AFTER `bag_id`,
    ADD COLUMN `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间';

-- 可选：若希望老订单的“创建时间”近似等于其订单时间，取消下行注释执行
-- UPDATE `orders` SET `create_time` = CONCAT(`time`, ' 00:00:00');

-- 4. 工序表：不要楼层，仅补创建时间（用于默认排序）
ALTER TABLE `process`
    ADD COLUMN `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间';

-- ============================================================
-- 说明：
-- 1) ALTER 加列时 DEFAULT 3 会自动把存量行填成 3 楼，无需手工 UPDATE；
-- 2) 存量行的 create_time 都等于执行脚本那一刻的时间，彼此相同，
--    排序时后端用 (create_time DESC, id DESC)，id 兜底保证顺序稳定；
-- 3) 唯一键由 name 变为 (name, floor)：不同楼层允许同名员工/书包，
--    已有数据本来 name 唯一，不会冲突。
-- ============================================================
