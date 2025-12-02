-- 升级积木BI到V2.2.0版本
ALTER TABLE `onl_drag_page`
MODIFY COLUMN `des_json` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仪表盘主配置JSON' AFTER `cover_url`;