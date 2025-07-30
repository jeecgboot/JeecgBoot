-- 租户初始套餐添加时 提示违反唯一约束
ALTER TABLE sys_tenant_pack
ADD INDEX idx__stp_tenant_id_pack_code(tenant_id, pack_code) USING BTREE;

-- 添加通知消息大分类
ALTER TABLE sys_announcement
    ADD COLUMN notice_type varchar(10) NULL COMMENT '通知类型(system:系统消息、file:知识库、flow:流程、plan:日程计划、meeting:会议)' AFTER tenant_id;

-- 更新通知消息字段旧数据默认为系统消息
update sys_announcement set notice_type = 'flow' where bus_type in('bpm','bpm_cc','bpm_task');
update sys_announcement set notice_type = 'system' where bus_type ='email';
update sys_announcement set notice_type = 'system' where notice_type is null;

-- 系统公告新增字段修改
ALTER TABLE `sys_announcement`
    ADD COLUMN `files` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '附件' AFTER `tenant_id`,
ADD COLUMN `visits_num` int(11) NULL DEFAULT NULL COMMENT '访问次数' AFTER `files`,
ADD COLUMN `iz_top` int(10) NULL DEFAULT NULL COMMENT '是否置顶（0:否;  1:是）' AFTER `visits_num`,
ADD COLUMN `iz_approval` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否审批（0否 1是）' AFTER `iz_top`,
ADD COLUMN `bpm_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流程状态' AFTER `iz_approval`,
ADD COLUMN `msg_classify` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息归类' AFTER `bpm_status`;

-- 系统公告--新增字典
INSERT INTO `sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`, `tenant_id`, `low_app_id`) VALUES ('1934846825077878786', '公告分类', 'notice_type', NULL, 0, 'admin', '2025-06-17 13:33:25', NULL, NULL, 0, 0, NULL);
INSERT INTO `sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`, `tenant_id`, `low_app_id`) VALUES ('1937393911539384322', '模版分类', 'msgCategory', NULL, 0, 'admin', '2025-06-24 14:14:38', NULL, NULL, 0, 0, NULL);

INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `item_color`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1934846897383485441', '1934846825077878786', '发布性通知', '1', NULL, NULL, 1, 1, 'admin', '2025-06-17 13:33:43', NULL, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `item_color`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1934846933030875138', '1934846825077878786', '转发性通知', '2', NULL, NULL, 1, 1, 'admin', '2025-06-17 13:33:51', NULL, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `item_color`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1934846963749957633', '1934846825077878786', '指示性通知', '3', NULL, NULL, 1, 1, 'admin', '2025-06-17 13:33:59', NULL, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `item_color`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1934846993449824257', '1934846825077878786', '任免性通知', '4', NULL, NULL, 1, 1, 'admin', '2025-06-17 13:34:06', NULL, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `item_color`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1934847047262744577', '1934846825077878786', '事务性（周知）通知', '5', NULL, NULL, 1, 1, 'admin', '2025-06-17 13:34:18', NULL, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `item_color`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1934847082905939969', '1934846825077878786', '会议通知', '6', NULL, NULL, 1, 1, 'admin', '2025-06-17 13:34:27', NULL, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `item_color`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1934847117039185921', '1934846825077878786', '其他通知', '7', NULL, NULL, 1, 1, 'admin', '2025-06-17 13:34:35', NULL, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `item_color`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1937394006326460418', '1937393911539384322', '通知公告', 'notice', NULL, NULL, 1, 1, 'admin', '2025-06-24 14:15:01', NULL, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `item_color`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1937394038412886018', '1937393911539384322', '其他', 'other', NULL, NULL, 1, 1, 'admin', '2025-06-24 14:15:08', NULL, NULL);


-- 消息模版增加 模版分类 字段
ALTER TABLE `sys_sms_template`
    ADD COLUMN `template_category` varchar(10) NULL COMMENT '模版分类：notice通知公告 other其他' AFTER `template_type`;


-- 修改表iz_top的默认值
ALTER TABLE `sys_announcement`
    MODIFY COLUMN `iz_top` int(10) NULL DEFAULT 0 COMMENT '是否置顶（0:否;  1:是）' AFTER `visits_num`;

-- 补充旧数据iz_top的默认值
UPDATE sys_announcement SET iz_top = 0 WHERE iz_top IS NULL OR iz_top = '';

-- 新增首页配置菜单
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1939572818833301506', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '首页配置', '/system/homeConfig', 'system/homeConfig/index', 1, '', NULL, 1, NULL, '0', 1.00, 0, 'ant-design:appstore-outlined', 1, 0, 0, 0, NULL, 'admin', '2025-06-30 14:32:50', 'admin', '2025-07-01 20:13:22', 0, 0, NULL, 0);
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1941349550087168001', '1939572818833301506', '首页配置-批量删除', NULL, NULL, 0, NULL, NULL, 2, 'system:roleindex:deleteBatch', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-07-05 12:12:56', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1941349462887587842', '1939572818833301506', '首页配置-删除', NULL, NULL, 0, NULL, NULL, 2, 'system:roleindex:delete', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-07-05 12:12:35', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1941349335431077889', '1939572818833301506', '首页配置-编辑', NULL, NULL, 0, NULL, NULL, 2, 'system:roleindex:edit', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-07-05 12:12:05', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1941349246536998913', '1939572818833301506', '首页配置-添加', NULL, NULL, 0, NULL, NULL, 2, 'system:roleindex:add', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-07-05 12:11:44', NULL, NULL, 0, 0, '1', 0);

-- 首页字典
INSERT INTO `sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`, `tenant_id`, `low_app_id`) VALUES ('1939572486447292418', '首页关联', 'relation_type', NULL, 0, 'admin', '2025-06-30 14:31:31', NULL, NULL, 0, 0, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `item_color`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1939572554533429250', '1939572486447292418', '角色', 'ROLE', NULL, NULL, 1, 1, 'admin', '2025-06-30 14:31:47', 'admin', '2025-06-30 15:04:18');
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `item_color`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1939572602289774594', '1939572486447292418', '用户', 'USER', NULL, NULL, 2, 1, 'admin', '2025-06-30 14:31:59', 'admin', '2025-06-30 15:04:21');


-- 角色首页表新增 relation_type 字段
ALTER TABLE `sys_role_index`
    ADD COLUMN `relation_type` varchar(20) NULL COMMENT '关联关系(ROLE:角色 USER:用户)' AFTER `sys_org_code`;

-- 首页角色补充默认值
UPDATE sys_role_index SET relation_type = 'ROLE' WHERE relation_type IS NULL OR relation_type = '';

-- app3支持版本管理
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1930152938891608066', '1455100420297859074', 'APP版本管理', '/app/version', 'system/appVersion/SysAppVersion', 1, '', NULL, 1, NULL, '0', 1.00, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-06-04 14:41:36', 'admin', '2025-07-03 10:09:46', 0, 0, NULL, 0);


-- 首页配置菜单
UPDATE `sys_permission` SET `is_leaf` = 0 WHERE `id` = '1939572818833301506';


-- APP版本管理配置菜单
UPDATE `sys_permission` SET `is_leaf` = 0 WHERE `id` = '1930152938891608066';
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1942160438629109761', '1930152938891608066', 'APP版本编辑', NULL, NULL, 0, NULL, NULL, 2, 'app:edit:version', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-07-07 17:55:07', NULL, NULL, 0, 0, '1', 0);

-- 删除第三方配置添加权限
INSERT INTO sys_permission (id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external) VALUES ('1947833384695164929', '1629109281748291586', '第三方配置删除', NULL, NULL, 0, NULL, NULL, 2, 'system:third:config:delete', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2025-07-23 09:37:23', NULL, NULL, 0, 0, '1', 0);

-- 人员代理表添加process_ids字段
ALTER TABLE `sys_user_agent`
    ADD COLUMN `process_ids` varchar(255) NULL COMMENT '代理流程ID' AFTER `end_time`;