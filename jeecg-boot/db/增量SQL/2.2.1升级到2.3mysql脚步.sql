
-- author:sunjianlei--------date:20200721------for: 新增JVXETable示例菜单 --------
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1285157614715457537', '2a470fc0c3954d9dbb61de6d80846549', 'JVXETable示例', '/jeecg/j-vxe-table-demo', 'jeecg/JVXETableDemo', '1', NULL, NULL, '1', NULL, '1', '10', '0', NULL, '1', '0', '0', NULL, 'admin', '2020-07-20 18:20:27', 'admin', '2020-07-20 18:20:36', '0', '0', '1', '0');
-- author:sunjianlei--------date:20200721------for: 新增JVXETable示例菜单 --------

-- author:sunjianlei--------date:20200727------for: 新增JVXETable示例菜单 --------
-- 删除旧菜单
DELETE FROM `sys_permission` WHERE (`id`='1285157614715457537');
DELETE FROM `sys_role_permission` WHERE (`permission_id`='1285157614715457537');
-- 添加新菜单
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1287715272999944193', '2a470fc0c3954d9dbb61de6d80846549', 'JVXETable示例', '/jeecg/j-vxe-table-demo', 'layouts/RouteView', '1', NULL, NULL, '1', NULL, '1', '10', '0', '', '0', '0', '0', NULL, 'admin', '2020-07-27 19:43:40', 'admin', '2020-07-27 19:47:19', '0', '0', '1', '0');
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1287715783966834689', '1287715272999944193', '普通示例', '/jeecg/j-vxe-table-demo/normal', 'jeecg/JVXETableDemo', '1', NULL, NULL, '1', NULL, '1', '1', '0', NULL, '1', '0', '0', NULL, 'admin', '2020-07-27 19:45:42', NULL, NULL, '0', '0', '1', '0');
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1287716451494510593', '1287715272999944193', '布局模板', '/jeecg/j-vxe-table-demo/layout', 'jeecg/JVxeDemo/layout-demo/Index', '1', NULL, NULL, '1', NULL, '1', '2', '0', NULL, '1', '0', '0', NULL, 'admin', '2020-07-27 19:48:21', NULL, NULL, '0', '0', '1', '0');
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1287718919049691137', '1287715272999944193', '即时保存', '/jeecg/j-vxe-table-demo/jsbc', 'jeecg/JVxeDemo/demo/JSBCDemo', '1', NULL, NULL, '1', NULL, '1', '3', '0', NULL, '1', '0', '0', NULL, 'admin', '2020-07-27 19:57:36', 'admin', '2020-07-27 20:03:37', '0', '0', '1', '0');
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1287718938179911682', '1287715272999944193', '弹出子表', '/jeecg/j-vxe-table-demo/tczb', 'jeecg/JVxeDemo/demo/PopupSubTable', '1', NULL, NULL, '1', NULL, '1', '4', '0', NULL, '1', '0', '0', NULL, 'admin', '2020-07-27 19:57:41', 'admin', '2020-07-27 20:03:47', '0', '0', '1', '0');
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1287718956957810689', '1287715272999944193', '无痕刷新', '/jeecg/j-vxe-table-demo/whsx', 'jeecg/JVxeDemo/demo/SocketReload', '1', NULL, NULL, '1', NULL, '1', '5', '0', NULL, '1', '0', '0', NULL, 'admin', '2020-07-27 19:57:44', 'admin', '2020-07-27 20:03:57', '0', '0', '1', '0');
-- author:sunjianlei--------date:20200727------for: 新增JVXETable示例菜单 --------





DROP TABLE IF EXISTS `sys_gateway_route`;
CREATE TABLE `sys_gateway_route`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务名',
  `uri` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务地址',
  `predicates` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '断言',
  `filters` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '过滤器',
  `retryable` int(3) NULL DEFAULT NULL COMMENT '是否重试:0-否 1-是',
  `strip_prefix` int(3) NULL DEFAULT NULL COMMENT '是否忽略前缀0-否 1-是',
  `persist` int(3) NULL DEFAULT NULL COMMENT '是否为保留数据:0-否 1-是',
  `show_api` int(3) NULL DEFAULT NULL COMMENT '是否在接口文档中展示:0-否 1-是',
  `status` int(3) NULL DEFAULT NULL COMMENT '状态:0-无效 1-有效',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
-- author:taoyan--------date:20200815---for: gateway 路由配置管理

-- author:liusq--------date:20200904---for: 新增字段
ALTER TABLE `sys_user`
ADD COLUMN `client_id`  varchar(64) NULL COMMENT '设备ID' AFTER `rel_tenant_ids`;
-- author:liusq--------date:20200904---for:新增字段



-- author:taoyan--------date:20200723--------for: online权限改造
-- ----------------------------
-- Table structure for onl_auth_data
-- ----------------------------
DROP TABLE IF EXISTS `onl_auth_data`;
CREATE TABLE `onl_auth_data`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `cgform_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'online表ID',
  `rule_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则名',
  `rule_column` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则列',
  `rule_operator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则条件 大于小于like',
  `rule_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则值',
  `status` int(1) NULL DEFAULT NULL COMMENT '1有效 0无效',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for onl_auth_page
-- ----------------------------
DROP TABLE IF EXISTS `onl_auth_page`;
CREATE TABLE `onl_auth_page`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT ' 主键',
  `cgform_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'online表id',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段名/按钮编码',
  `type` int(1) NULL DEFAULT NULL COMMENT '1字段 2按钮',
  `control` int(1) NULL DEFAULT NULL COMMENT '3可编辑 5可见(仅支持两种状态值3,5)',
  `page` int(1) NULL DEFAULT NULL COMMENT '3列表 5表单(仅支持两种状态值3,5)',
  `status` int(1) NULL DEFAULT NULL COMMENT '1有效 0无效',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for onl_auth_relation
-- ----------------------------
DROP TABLE IF EXISTS `onl_auth_relation`;
CREATE TABLE `onl_auth_relation`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色id',
  `auth_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限id',
  `type` int(1) NULL DEFAULT NULL COMMENT '1字段 2按钮 3数据权限',
  `cgform_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'online表单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
-- author:taoyan--------date:20200723--------for: online权限改造




-- author:liusq----date:20200909----for:  更新大屏表单模版url ------------
-- 生产销售监控 --
update sys_permission set url='{{ window._CONFIG[\'domianURL\'] }}/test/bigScreen/templat/index1' where id='1205098241075453953';
-- 智慧物流监控 --
update sys_permission set url='{{ window._CONFIG[\'domianURL\'] }}/test/bigScreen/templat/index2' where id='1205306106780364802';
-- author:liusq----date:20200909----for:  更新大屏表单模版url ------------




