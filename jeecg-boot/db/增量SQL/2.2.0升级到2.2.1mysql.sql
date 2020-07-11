
-- ----author:scott--------date:20200602-------for: 字典表字段禁止为空------
ALTER TABLE `sys_dict_item`
MODIFY COLUMN `item_text` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典项文本' AFTER `dict_id`,
MODIFY COLUMN `item_value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典项值' AFTER `item_text`;

ALTER TABLE `sys_dict`
MODIFY COLUMN `dict_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典名称' AFTER `id`,
MODIFY COLUMN `dict_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典编码' AFTER `dict_name`;
-- ----author:scott--------date:20200602-------for: 字典表字段禁止为空------

-- author:taoyan--------date:20200619-------for: 同步定时任务
INSERT INTO `sys_quartz_job`(`id`, `create_by`, `create_time`, `del_flag`, `update_by`, `update_time`, `job_class_name`, `cron_expression`, `parameter`, `description`, `status`) VALUES ('1273896435116408834', 'admin', '2020-06-19 16:32:31', 0, 'admin', '2020-06-19 20:08:20', 'org.jeecg.modules.quartz.job.AsyncJob', '0/1 * * * * ? *', NULL, '同步任务', -1);
-- author:taoyan--------date:20200619-------for: 同步定时任务

-- author:lvdandan--------date:20200703-------for: 角色授权、二级部门授权添加操作时间及操作ip
ALTER TABLE `sys_role_permission`
MODIFY COLUMN `data_rule_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据权限ids' AFTER `permission_id`,
ADD COLUMN `operate_date` datetime COMMENT '操作时间' AFTER `data_rule_ids`,
ADD COLUMN `operate_ip` varchar(20) COMMENT '操作ip' AFTER `operate_date`;
ALTER TABLE `sys_depart_role_permission`

MODIFY COLUMN `data_rule_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据权限ids' AFTER `permission_id`,
ADD COLUMN `operate_date` datetime DEFAULT NULL COMMENT '操作时间' AFTER `data_rule_ids`,
ADD COLUMN `operate_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作ip' AFTER `operate_date`;
-- author:lvdandan--------date:20200703-------for: 角色授权、二级部门授权添加操作时间及操作ip

-- author:lvdandan--------date:20200707-------for: 添加图片裁剪
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `status`, `del_flag`, `rule_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `internal_or_external`) VALUES ('1280464606292099074', '2a470fc0c3954d9dbb61de6d80846549', '图片裁剪', '/jeecg/ImagCropper', 'jeecg/ImagCropper', NULL, NULL, 1, NULL, '1', 9.00, 0, NULL, 1, 1, 0, 0, NULL, '1', 0, 0, 'admin', '2020-07-07 19:32:06', NULL, NULL, 0);
-- author:lvdandan--------date:20200707-------for: 添加图片裁剪菜单


-- author:taoyan--------date:20200707-------for: 多租户
ALTER TABLE `sys_user`
ADD COLUMN `rel_tenant_ids` varchar(100) NULL COMMENT '多租户标识' AFTER `depart_ids`;

INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1280350452934307841', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '租户管理', '/isys/tenant', 'system/TenantList', NULL, NULL, 1, NULL, '1', 99.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2020-07-07 11:58:30', NULL, NULL, 0, 0, '1', 0);

DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant`  (
  `id` int(5) NOT NULL COMMENT '租户编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `begin_date` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_date` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态 1正常 0冻结',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '多租户信息表' ROW_FORMAT = Dynamic;

INSERT INTO `sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('1280401766745718786', '租户状态', 'tenant_status', '租户状态', 0, 'admin', '2020-07-07 15:22:25', NULL, NULL, 0);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1280401815068295170', '1280401766745718786', '正常', '1', '', 1, 1, 'admin', '2020-07-07 15:22:36', NULL, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1280401847607705602', '1280401766745718786', '冻结', '0', '', 1, 1, 'admin', '2020-07-07 15:22:44', NULL, NULL);
-- author:taoyan--------date:20200707-------for: 多租户