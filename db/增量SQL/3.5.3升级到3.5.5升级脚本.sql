-- 产品包升级sql
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external) VALUES ('1609123240547344385', '1280350452934307841', '产品包分页列表查询', NULL, NULL, 0, NULL, NULL, 2, 'system:tenant:packList', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-12-31 17:44:11', NULL, NULL, 0, 0, '1', 0);
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external) VALUES ('1609123437247619074', '1280350452934307841', '创建租户产品包', NULL, NULL, 0, NULL, NULL, 2, 'system:tenant:add:pack', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-12-31 17:44:58', 'admin', '2022-12-31 20:27:56', 0, 0, '1', 0);
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external) VALUES ('1609164542165012482', '1280350452934307841', '编辑租户产品包', NULL, NULL, 0, NULL, NULL, 2, 'system:tenant:edit:pack', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-12-31 20:28:18', NULL, NULL, 0, 0, '1', 0);
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external) VALUES ('1609164635442139138', '1280350452934307841', '批量删除租户产品包', NULL, NULL, 0, NULL, NULL, 2, 'system:tenant:delete:pack', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-12-31 20:28:41', NULL, NULL, 0, 0, '1', 0);

-- 新增部门默认是叶子节点，即没有子节点
ALTER TABLE sys_depart
MODIFY COLUMN iz_leaf tinyint(1) NULL DEFAULT 1 COMMENT '是否有叶子节点: 1是0否' AFTER tenant_id;


-- 部门数据错了修复---
update sys_depart set iz_leaf = 0 where id in ( select a.parent_id from (select parent_id from sys_depart where parent_id!='' and parent_id is not null) as a);
update sys_depart set iz_leaf = 1 where id not in ( select a.parent_id from (select parent_id from sys_depart where parent_id!='' and parent_id is not null) as a);


-- 日志接口类，没有加权限注解
-- vue2
UPDATE sys_permission_v2 SET is_leaf = 0 WHERE id = '58857ff846e61794c69208e9d3a85466';
INSERT INTO sys_permission_v2(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external) VALUES ('1660568280725127169', '58857ff846e61794c69208e9d3a85466', '日志列表', NULL, NULL, NULL, NULL, 2, 'system:log:list', '1', NULL, 0, NULL, 1, 1, 0, 0, 0, NULL, '1', 0, 0, 'admin', '2023-05-22 16:48:25', NULL, NULL, 0);
INSERT INTO sys_permission_v2(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external) VALUES ('1660568368558047234', '58857ff846e61794c69208e9d3a85466', '日志删除', NULL, NULL, NULL, NULL, 2, 'system:log:delete', '1', NULL, 0, NULL, 1, 1, 0, 0, 0, NULL, '1', 0, 0, 'admin', '2023-05-22 16:48:46', NULL, NULL, 0);
INSERT INTO sys_permission_v2(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external) VALUES ('1660568426632380417', '58857ff846e61794c69208e9d3a85466', '日志批量删除', NULL, NULL, NULL, NULL, 2, 'system:log:deleteBatch', '1', NULL, 0, NULL, 1, 1, 0, 0, 0, NULL, '1', 0, 0, 'admin', '2023-05-22 16:48:59', NULL, NULL, 0);

-- vue3
UPDATE sys_permission SET is_leaf = 0 WHERE id = '1439533711676973057';
INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external) VALUES ('1660568280725127169', '1439533711676973057', '日志列表', NULL, NULL, NULL, NULL, 2, 'system:log:list', '1', NULL, 0, NULL, 1, 1, 0, 0, 0, NULL, '1', 0, 0, 'admin', '2023-05-22 16:48:25', NULL, NULL, 0);
INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external) VALUES ('1660568368558047234', '1439533711676973057', '日志删除', NULL, NULL, NULL, NULL, 2, 'system:log:delete', '1', NULL, 0, NULL, 1, 1, 0, 0, 0, NULL, '1', 0, 0, 'admin', '2023-05-22 16:48:46', NULL, NULL, 0);
INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external) VALUES ('1660568426632380417', '1439533711676973057', '日志批量删除', NULL, NULL, NULL, NULL, 2, 'system:log:deleteBatch', '1', NULL, 0, NULL, 1, 1, 0, 0, 0, NULL, '1', 0, 0, 'admin', '2023-05-22 16:48:59', NULL, NULL, 0);

-- 字段长度不够 ---
ALTER TABLE sys_data_log
MODIFY COLUMN `data_table` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表名' AFTER `update_time`;

-- 系统通知卡顿问题性能优化 ---
ALTER TABLE `sys_announcement_send` 
ADD INDEX `idx_sacm_annt_id`(`annt_id`),
ADD INDEX `idx_sacm_user_id`(`user_id`),
ADD INDEX `idx_sacm_read_flag`(`read_flag`),
ADD INDEX `idx_sacm_star_flag`(`star_flag`);

ALTER TABLE `sys_announcement` 
ADD INDEX `idx_sanno_endtime`(`end_time`),
ADD INDEX `idx_sanno_start_time`(`start_time`),
ADD INDEX `idx_sanno_msg_type`(`msg_type`),
ADD INDEX `idx_sanno_send_status`(`send_status`),
ADD INDEX `idx_sanno_del_flag`(`del_flag`),
ADD INDEX `idx_sanno_tenant_id`(`tenant_id`),
ADD INDEX `idx_sanno_sender`(`sender`),
ADD INDEX `idx_sanno_create_time`(`create_time`);
