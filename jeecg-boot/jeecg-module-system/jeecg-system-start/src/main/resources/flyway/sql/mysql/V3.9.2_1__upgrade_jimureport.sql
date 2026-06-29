CREATE TABLE `chat2bi_table_meta`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `db_source_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '数据源来源表标识, 如: db_source / api_source / file_source / system_source',
  `db_source_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '对应来源表的主键ID',
  `schema_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Schema名',
  `table_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '表名',
  `source_comment` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '表注释-源(同步写入, 每次同步覆盖)',
  `table_comment` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '表注释-用户(用户手动填写, 同步不覆盖)',
  `columns_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '列定义[{name,type,source_comment,comment,is_primary,sample_values,nulltable}]',
  `relations_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '关联关系[{column,ref_table,ref_column,type}]',
  `is_enabled` tinyint(4) NOT NULL DEFAULT 1 COMMENT '对LLM可见: 0=否 1=是',
  `synced_time` datetime NULL DEFAULT NULL COMMENT '最近同步时间',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `tenant_id` int(11) NULL DEFAULT NULL COMMENT '多租户标识',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_source_table`(`db_source_type`, `db_source_id`, `schema_name`, `table_name`) USING BTREE,
  INDEX `idx_source`(`db_source_type`, `db_source_id`) USING BTREE,
  INDEX `idx_table_name`(`table_name`) USING BTREE,
  INDEX `idx_table_name_comment`(`table_name`, `source_comment`(320), `table_comment`(320)) USING BTREE,
  INDEX `idx_table_comment`(`table_comment`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '表元数据(同步+手动维护, 支持多数据源表)' ROW_FORMAT = DYNAMIC;

-- 修复错误接口数据
UPDATE onl_drag_dataset_head
  SET query_sql = REPLACE(query_sql, 'https://apijeecgcom/', 'http://api.jeecg.com/')
  WHERE query_sql LIKE '%https://apijeecgcom/%';
