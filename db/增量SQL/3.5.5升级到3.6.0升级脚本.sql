SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 新增用户职位表关系表
CREATE TABLE sys_user_position  (
id varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
user_id varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
position_id varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职位id',
create_by varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
create_time datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
update_by varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
update_time datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE sys_user_position
ADD INDEX idx_sup_user_id(user_id) USING BTREE,
ADD INDEX idx_sup_position_id(position_id) USING BTREE,
ADD INDEX idx_sup_user_position_id(user_id, position_id) USING BTREE;

-- 删除用户表的职位
ALTER TABLE sys_user DROP COLUMN post;



-- 用户租户关系表修改索引
ALTER TABLE `sys_user_tenant` 
DROP INDEX `uniq_sut_user_rel_tenant`,
ADD INDEX `idx_sut_user_rel_tenant`(`user_id`, `tenant_id`) USING BTREE;

ALTER TABLE `sys_user_depart` 
DROP INDEX `idx_sud_user_dep_id`,
ADD UNIQUE INDEX `idx_sud_user_dep_id`(`user_id`, `dep_id`) USING BTREE;


-- 新增企业微信和钉钉配置表，通过租户模式隔离
CREATE TABLE sys_third_app_config  (
id varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
tenant_id int(10) NOT NULL DEFAULT 0 COMMENT '租户id',
agent_id varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '钉钉/企业微信应用id',
client_id varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '钉钉/企业微信 应用id',
client_secret varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '钉钉/企业微信应用id对应的秘钥',
third_type varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方类别(dingtalk 钉钉 wechat_enterprise 企业微信)',
agent_app_secret varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自建应用Secret',
status int(1) NULL DEFAULT 1 COMMENT '是否启用(0-否,1-是)',
create_time datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
update_time datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (id) USING BTREE,
UNIQUE INDEX uniq_stac_third_type_tenant_id(tenant_id, third_type) USING BTREE,
INDEX idx_stac_tenant_id(tenant_id) USING BTREE,
INDEX idx_stac_third_type(third_type) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户第三方配置表' ROW_FORMAT = Dynamic;

ALTER TABLE sys_third_account
ADD UNIQUE INDEX uniq_sta_user_id_third_type(sys_user_id, third_type) USING BTREE,
ADD UNIQUE INDEX uniq_sta_third_user_id_third_type(third_user_id, third_type) USING BTREE,
ADD UNIQUE INDEX uniq_sta_third_user_uuid_third_type(third_user_uuid, third_type) USING BTREE;

INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external) VALUES ('1629109281748291586', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '第三方配置', '/third/app', 'system/appconfig/ThirdAppConfigList', 1, '', NULL, 1, NULL, '0', 13.00, 0, 'ant-design:setting-outlined', 1, 0, 0, 0, NULL, 'admin', '2023-02-24 21:21:35', 'admin', '2023-02-24 21:51:05', 0, 0, NULL, 0);

ALTER TABLE sys_third_account
ADD COLUMN tenant_id int(10) NULL DEFAULT 0 COMMENT '租户id' AFTER realname;
update sys_third_account set tenant_id = 0;

ALTER TABLE sys_third_account
DROP INDEX uniq_sta_third_user_id_third_type,
DROP INDEX uniq_sta_third_user_uuid_third_type,
DROP INDEX uniq_sta_user_id_third_type,
ADD UNIQUE INDEX uniq_sta_third_user_id_third_type(third_user_id, third_type, tenant_id) USING BTREE,
ADD UNIQUE INDEX uniq_sta_third_user_uuid_third_type(third_user_uuid, third_type, tenant_id) USING BTREE;

ALTER TABLE `sys_third_app_config` 
DROP INDEX `uniq_stac_third_type_tenant_id`;


-- 新增租户默认产品包
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external) VALUES ('1674708136602542082', '', '我的租户', '/mytenant', 'layouts/RouteView', 1, '', NULL, 0, NULL, '0', 4.20, 0, 'ant-design:user-outlined', 0, 0, 0, 0, NULL, 'admin', '2023-06-30 17:15:09', 'admin', '2023-06-30 18:35:40', 0, 0, NULL, 0);
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external) VALUES ('1663816667704500225', '1674708136602542082', '我的租户', '/tenant/MyTenantList', 'system/tenant/MyTenantList', 1, '', NULL, 1, NULL, '0', 1.00, 0, 'ant-design:user-outlined', 1, 0, 0, 0, NULL, 'admin', '2023-05-31 15:56:20', 'admin', '2023-06-30 18:37:26', 0, 0, NULL, 0);
UPDATE sys_permission SET parent_id = '1674708136602542082' WHERE component = 'system/user/TenantUserList';
UPDATE sys_permission SET parent_id = '1674708136602542082' WHERE component = 'system/role/TenantRoleList';
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external) VALUES ('1661572802889007106', '', '租户管理', '/tenant/setting', 'layouts/RouteView', 1, NULL, NULL, 0, NULL, '1', 4.10, 0, 'ant-design:setting-outlined', 0, 0, 0, 0, NULL, 'admin', '2023-05-25 11:20:01', 'admin', '2023-06-30 18:37:04', 0, 0, '1', 0);
UPDATE sys_permission SET parent_id = '1661572802889007106' WHERE component = 'system/tenant/index';
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external) VALUES ('1668174661456171010', '1661572802889007106', '租户默认套餐', '/tenant/TenantDefaultPack', 'system/tenant/TenantDefaultPackList', 1, '', NULL, 1, NULL, '0', 5.00, 0, 'ant-design:folder-filled', 1, 0, 0, 0, NULL, 'admin', '2023-06-12 16:33:27', 'admin', '2023-06-30 19:09:24', 0, 0, NULL, 0);
ALTER TABLE sys_tenant_pack
ADD COLUMN pack_type varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'custom' COMMENT '产品包类型(default 默认产品包 custom 自定义产品包)' AFTER pack_code;
update sys_tenant_pack set pack_type = 'custom';

ALTER TABLE sys_user_tenant
MODIFY COLUMN status varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态(1 正常 2 离职 3 待审核 4 拒绝 5 邀请加入)' AFTER tenant_id;

UPDATE sys_permission SET component = 'system/tenant/my/MyTenantList' WHERE component = 'system/tenant/MyTenantList';
UPDATE sys_permission SET component = 'system/tenant/pack/TenantDefaultPackList' WHERE component = 'system/tenant/TenantDefaultPackList';
UPDATE sys_permission SET component = 'system/tenant/TenantUserList', url='/system/tenant/TenantUserList' WHERE component = 'system/user/TenantUserList';


-- 系统通知类型新增租户邀请
ALTER TABLE sys_announcement
MODIFY COLUMN bus_type varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型(email:邮件 bpm:流程 tenant_invite:租户邀请)' AFTER del_flag;


-- 修改部门表org_category字段的注释
ALTER TABLE `sys_depart`
MODIFY COLUMN `org_category` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '机构类别 1公司，2组织机构，3岗位' AFTER `description`;


-- 日志显示实际名称 ---
ALTER TABLE `sys_data_log`
ADD COLUMN `create_name` varchar(100) NULL COMMENT '创建人真实名称' AFTER `create_by`;

UPDATE sys_data_log
SET create_name = (SELECT sys_user.realname FROM sys_user WHERE sys_user.username = sys_data_log.create_by)
WHERE create_name = '' OR create_name IS NULL;


-- 新增表字典白名单配置表
CREATE TABLE `sys_table_white_list`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `table_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '允许的表名',
  `field_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '允许的字段名，多个用逗号分割',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '状态，1=启用，0=禁用',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_sys_table_white_list_table_name`(`table_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典表白名单' ROW_FORMAT = DYNAMIC;
-- 默认数据
INSERT INTO `sys_table_white_list` (`id`, `table_name`, `field_name`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1701578033271521282', 'sys_user', 'id,realname,username', '1', 'admin', '2023-09-12 10:46:32', NULL, NULL);
INSERT INTO `sys_table_white_list` (`id`, `table_name`, `field_name`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1701581935488385025', 'oa_officialdoc_organcode', 'id,organ_name', '1', 'admin', '2023-09-12 11:02:02', NULL, NULL);
INSERT INTO `sys_table_white_list` (`id`, `table_name`, `field_name`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1701581977733414913', 'demo', 'id,name', '1', 'admin', '2023-09-12 11:02:12', NULL, NULL);
INSERT INTO `sys_table_white_list` (`id`, `table_name`, `field_name`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1701582035472203777', 'sys_permission', 'id,name', '1', 'admin', '2023-09-12 11:02:26', NULL, NULL);
INSERT INTO `sys_table_white_list` (`id`, `table_name`, `field_name`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1701582087619985409', 'onl_drag_comp', 'id,comp_name', '1', 'admin', '2023-09-12 11:02:38', NULL, NULL);
INSERT INTO `sys_table_white_list` (`id`, `table_name`, `field_name`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1701582136420712450', 'sys_depart', 'id,depart_name', '1', 'admin', '2023-09-12 11:02:50', NULL, NULL);
INSERT INTO `sys_table_white_list` (`id`, `table_name`, `field_name`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1701582163599802370', 'design_form', 'id,desform_name,desform_code', '1', 'admin', '2023-09-12 11:02:56', NULL, NULL);
INSERT INTO `sys_table_white_list` (`id`, `table_name`, `field_name`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1701582190187495426', 'onl_cgform_head', 'table_txt,table_name', '1', 'admin', '2023-09-12 11:03:03', NULL, NULL);
INSERT INTO `sys_table_white_list` (`id`, `table_name`, `field_name`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1701582254301626370', 'oa_wps_file', 'id,name', '1', 'admin', '2023-09-12 11:03:18', NULL, NULL);

INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1701575168519839746', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '字典表白名单', '/system/tableWhiteList', 'system/tableWhiteList/SysTableWhiteListList', 1, '', NULL, 1, NULL, '0', 13.00, 0, 'ant-design:table-outlined', 1, 0, 0, 0, NULL, 'admin', '2023-09-12 20:35:09', 'admin', '2023-09-12 20:45:08', 0, 0, NULL, 0);


-- 系统通知卡顿问题性能优化 ---
ALTER TABLE `sys_announcement_send` 
MODIFY COLUMN `read_flag` int(2) NULL DEFAULT NULL COMMENT '阅读状态（0未读，1已读）' AFTER `user_id`;

ALTER TABLE `sys_announcement`
MODIFY COLUMN `msg_abstract` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '摘要/扩展业务参数' AFTER `user_ids`;


-- 字典表 加颜色配置---
ALTER TABLE sys_dict_item
ADD COLUMN item_color varchar(10) NULL COMMENT '字典项颜色' AFTER item_value;


SET FOREIGN_KEY_CHECKS = 1;
