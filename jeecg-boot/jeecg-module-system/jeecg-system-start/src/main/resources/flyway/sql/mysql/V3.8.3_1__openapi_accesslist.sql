/*
  OpenApi 访问清单字段增量升级
  - 新增列：list_mode, allowed_list, comment, dns_cache_ttl_seconds, ip_version, enable_strict
  - 索引：idx_open_api_update_time
  - 数据迁移：老记录 list_mode 置 WHITELIST
*/
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 添加新字段
ALTER TABLE `open_api`
  ADD COLUMN `list_mode` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'WHITELIST' COMMENT '访问清单模式：WHITELIST/BLACKLIST' AFTER `request_url`,
  ADD COLUMN `allowed_list` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '访问清单，多IP/CIDR/域名，逗号或换行分隔' AFTER `black_list`,
  ADD COLUMN `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '访问清单备注' AFTER `allowed_list`,
  ADD COLUMN `dns_cache_ttl_seconds` int(11) NULL DEFAULT NULL COMMENT 'DNS缓存TTL秒' AFTER `comment`,
  ADD COLUMN `ip_version` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'Dual' COMMENT 'IPv4/IPv6/Dual' AFTER `dns_cache_ttl_seconds`,
  ADD COLUMN `enable_strict` tinyint(1) NOT NULL DEFAULT 0 COMMENT '严格模式开关' AFTER `ip_version`;

-- 数据迁移：老记录list_mode统一置为WHITELIST
UPDATE `open_api`
SET `list_mode` = 'WHITELIST'
WHERE `list_mode` IS NULL OR `list_mode` = '';

-- 索引：最近变更排序
ALTER TABLE `open_api`
  ADD INDEX `idx_open_api_update_time` (`update_time`);

SET FOREIGN_KEY_CHECKS = 1;

-- 回滚要点（如需回滚，手工执行以下SQL）
-- ALTER TABLE `open_api`
--   DROP COLUMN `list_mode`,
--   DROP COLUMN `allowed_list`,
--   DROP COLUMN `comment`,
--   DROP COLUMN `dns_cache_ttl_seconds`,
--   DROP COLUMN `ip_version`,
--   DROP COLUMN `enable_strict`;
-- DROP INDEX `idx_open_api_update_time` ON `open_api`;