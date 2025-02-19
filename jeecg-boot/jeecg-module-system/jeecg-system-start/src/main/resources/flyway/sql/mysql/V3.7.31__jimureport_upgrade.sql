ALTER TABLE `jimu_report_db_field`
    ADD COLUMN `field_name_physics` varchar(200) NULL COMMENT '物理字段名（文件数据集使用，存的是excel的字段标题）' AFTER `field_name`;

CREATE TABLE `jimu_report_icon_lib` (
    `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图片名称',
    `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图片类型',
    `image_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '图片地址',
    `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `tenant_id` int(11) DEFAULT NULL COMMENT '租户id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='积木图库表';

INSERT INTO `jimu_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`, `tenant_id`) VALUES ('1047797573274468352', '系统图库', 'gallery', '', 0, 'admin', '2025-02-07 19:00:19', NULL, NULL, 0, '1');
INSERT INTO `jimu_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1047797624512086016', '1047797573274468352', '常规', 'common', NULL, 1, 1, 'admin', '2025-02-07 19:00:31', NULL, NULL);
INSERT INTO `jimu_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1047797669877678080', '1047797573274468352', '指向', 'point', NULL, 1, 1, 'admin', '2025-02-07 19:00:42', '15931993294', '2025-02-07 19:01:11');
INSERT INTO `jimu_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1047797751893098496', '1047797573274468352', '专业', 'major', NULL, 1, 1, 'admin', '2025-02-07 19:01:01', NULL, NULL);