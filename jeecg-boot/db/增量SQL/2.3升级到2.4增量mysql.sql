
UPDATE `sys_data_source`
SET `id` = '1209779538310004737',
 `db_password` = 'f5b6775e8d1749483f2320627de0e706'
WHERE
	(`id` = '1209779538310004737');

delete from `sys_permission` where id='a2b11669e98c5fe54a53c3e3c4f35d14';



DROP TABLE IF EXISTS `sys_third_account`;
CREATE TABLE `sys_third_account` (
  `id` varchar(32) NOT NULL COMMENT '编号',
  `sys_user_id` varchar(32) DEFAULT NULL COMMENT '第三方登录id',
  `third_type` varchar(255) DEFAULT NULL COMMENT '登录来源',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态(1-正常,2-冻结)',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除状态(0-正常,1-已删除)',
  `realname` varchar(100) DEFAULT NULL COMMENT '真实姓名',
  `third_user_uuid` varchar(100) DEFAULT NULL COMMENT '第三方账号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `sys_user`
DROP COLUMN `third_id`
DROP COLUMN `third_type`;

update sys_permission set component = 'examples/list/UserList' where id = '05b3c82ddb2536a4a5ee1a4c46b5abef';
update sys_permission set component = 'examples/list/TableList' where id = '078f9558cdeab239aecb2bda1a8ed0d1';
update sys_permission set component = 'examples/list/TableList' where id = '200006f0edf145a2b50eacca07585451';
update sys_permission set component = 'examples/form/BasicForm' where id = '277bfabef7d76e89b33062b16a9a5020';
update sys_permission set component = 'examples/list/TableList' where id = '418964ba087b90a84897b62474496b93';
update sys_permission set component = 'examples/list/RoleList' where id = '4f84f9400e5e92c95f05b554724c2b58';
update sys_permission set component = 'examples/form/stepForm/StepForm' where id = '6531cf3421b1265aeeeabaab5e176e6d';
update sys_permission set component = 'examples/list/PermissionList' where id = '73678f9daa45ed17a3674131b03432fb';
update sys_permission set component = 'examples/list/CardList' where id = '7ac9eb9ccbde2f7a033cd4944272bf1e';
update sys_permission set component = 'examples/list/TableInnerEditList' where id = 'ae4fed059f67086fd52a73d913cf473d';
update sys_permission set component = 'examples/profile/advanced/Advanced' where id = 'b3c824fc22bd953e2eb16ae6914ac8f9';
update sys_permission set component = 'examples/profile/basic/Index' where id = 'cc50656cf9ca528e6f2150eba4714ad2';
update sys_permission set component = 'examples/list/TableList' where id = 'de13e0f6328c069748de7399fcc1dbbd';
update sys_permission set component = 'examples/form/advancedForm/AdvancedForm' where id = 'e5973686ed495c379d829ea8b2881fc6';
update sys_permission set component = 'examples/list/StandardList' where id = 'f23d9bfff4d9aa6b68569ba2cff38415';
update sys_permission set component = 'examples/list/search/SearchLayout' where id = 'fb07ca05a3e13674dbf6d3245956da2e';


INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `status`, `del_flag`, `rule_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `internal_or_external`) VALUES ('1304032910990495745', 'e41b69c57a941a3bbcce45032fe57605', 'AUTO在线表单TAB', '/online/cgformTabList/:code', 'modules/online/cgform/auto/tab/OnlCgformTabList', NULL, NULL, 1, NULL, '1', 8.00, 0, NULL, 1, 1, 0, 1, NULL, '1', 0, 0, 'admin', '2020-09-10 20:24:08', 'admin', '2020-09-10 20:36:37', 0);

INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1305827309355302914', 'bd1b8bc28e65d6feefefb6f3c79f42fd', 'API', 'api', '', 3, 1, 'admin', '2020-09-15 19:14:26', 'admin', '2020-09-15 19:14:41');

ALTER TABLE `onl_cgreport_item`
ADD COLUMN `is_total` varchar(2) COMMENT '是否合计 0否,1是（仅对数值有效）' AFTER `replace_val`;

ALTER TABLE `onl_cgform_head`
ADD COLUMN `is_des_form` varchar(2) COMMENT '是否用设计器表单' AFTER `theme_template`,
ADD COLUMN `des_form_code` varchar(50) COMMENT '设计器表单编码' AFTER `is_des_form`;

ALTER TABLE `onl_cgreport_item`
ADD COLUMN `group_title` varchar(50) COMMENT '分组标题' AFTER `is_total`;


DROP TABLE IF EXISTS `sys_gateway_route`;
CREATE TABLE `sys_gateway_route`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `router_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路由ID',
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

-- ----------------------------
-- Records of sys_gateway_route
-- ----------------------------
delete from `sys_gateway_route`;
INSERT INTO `sys_gateway_route`(`id`, `router_id`, `name`, `uri`, `predicates`, `filters`, `retryable`, `strip_prefix`, `persist`, `show_api`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `sys_org_code`) VALUES ('1331051599401857026', 'jeecg-demo-websocket', 'jeecg-demo-websocket', 'lb:ws://jeecg-demo', '[{\"args\":[\"/vxeSocket/**\"],\"name\":\"Path\"}]', '[]', NULL, NULL, NULL, NULL, 1, 'admin', '2020-11-24 09:46:46', NULL, NULL, NULL);
INSERT INTO `sys_gateway_route`(`id`, `router_id`, `name`, `uri`, `predicates`, `filters`, `retryable`, `strip_prefix`, `persist`, `show_api`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `sys_org_code`) VALUES ('jeecg-cloud-websocket', 'jeecg-system-websocket', 'jeecg-system-websocket', 'lb:ws://jeecg-system', '[{\"args\":[\"/websocket/**\",\"/eoaSocket/**\",\"/newsWebsocket/**\"],\"name\":\"Path\"}]', '[]', NULL, NULL, NULL, NULL, 1, 'admin', '2020-11-16 19:41:51', NULL, NULL, NULL);
INSERT INTO `sys_gateway_route`(`id`, `router_id`, `name`, `uri`, `predicates`, `filters`, `retryable`, `strip_prefix`, `persist`, `show_api`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `sys_org_code`) VALUES ('jeecg-demo', 'jeecg-demo', 'jeecg-demo', 'lb://jeecg-demo', '[{\"args\":[\"/mock/**\",\"/test/**\",\"/bigscreen/template1/**\",\"/bigscreen/template2/**\"],\"name\":\"Path\"}]', '[]', NULL, NULL, NULL, NULL, 1, 'admin', '2020-11-16 19:41:51', NULL, NULL, NULL);
INSERT INTO `sys_gateway_route`(`id`, `router_id`, `name`, `uri`, `predicates`, `filters`, `retryable`, `strip_prefix`, `persist`, `show_api`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `sys_org_code`) VALUES ('jeecg-system', 'jeecg-system', 'jeecg-system', 'lb://jeecg-system', '[{\"args\":[\"/sys/**\",\"/eoa/**\",\"/joa/**\",\"/online/**\",\"/bigscreen/**\",\"/jmreport/**\",\"/desform/**\",\"/process/**\",\"/act/**\",\"/plug-in/***/\",\"/druid/**\",\"/generic/**\"],\"name\":\"Path\"}]', '[]', NULL, NULL, NULL, NULL, 1, 'admin', '2020-11-16 19:41:51', NULL, NULL, NULL);




