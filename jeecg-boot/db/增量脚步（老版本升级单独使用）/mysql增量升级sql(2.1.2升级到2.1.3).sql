

-- 字典：日程计划状态
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('1199525215290306561', '日程计划状态', 'eoa_plan_status', '', '0', 'admin', '2019-11-27 11:07:52', 'admin', '2019-11-27 11:10:11', '0');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1199525468672405505', '1199525215290306561', '未开始', '0', '', '1.00', '1', 'admin', '2019-11-27 11:08:52', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1199525490575060993', '1199525215290306561', '进行中', '1', '', '1.00', '1', 'admin', '2019-11-27 11:08:58', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1199525506429530114', '1199525215290306561', '已完成', '2', '', '1.00', '1', 'admin', '2019-11-27 11:09:02', 'admin', '2019-11-27 11:10:02');

-- 字典：分类栏目类型
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('1199520177767587841', '分类栏目类型', 'eoa_cms_menu_type', '', '0', 'admin', '2019-11-27 10:47:51', 'admin', '2019-11-27 10:49:35', '0');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1199520817285701634', '1199520177767587841', '列表', '1', '', '1.00', '1', 'admin', '2019-11-27 10:50:24', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1199520835035996161', '1199520177767587841', '链接', '2', '', '1.00', '1', 'admin', '2019-11-27 10:50:28', NULL, NULL);

-- 字典：日程计划类型
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('1199518099888414722', '日程计划类型', 'eoa_plan_type', '', '0', 'admin', '2019-11-27 10:39:36', NULL, NULL, '0');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1199518186144276482', '1199518099888414722', '日常记录', '1', '', '1.00', '1', 'admin', '2019-11-27 10:39:56', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1199518214858481666', '1199518099888414722', '本周工作', '2', '', '1.00', '1', 'admin', '2019-11-27 10:40:03', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1199518235943247874', '1199518099888414722', '下周计划', '3', '', '1.00', '1', 'admin', '2019-11-27 10:40:08', NULL, NULL);

-- 字典：紧急程度
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('1199517671259906049', '紧急程度', 'urgent_level', '日程计划紧急程度', '0', 'admin', '2019-11-27 10:37:53', NULL, NULL, '0');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1199517884758368257', '1199517671259906049', '一般', '1', '', '1.00', '1', 'admin', '2019-11-27 10:38:44', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1199517914017832962', '1199517671259906049', '重要', '2', '', '1.00', '1', 'admin', '2019-11-27 10:38:51', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1199517941339529217', '1199517671259906049', '紧急', '3', '', '1.00', '1', 'admin', '2019-11-27 10:38:58', NULL, NULL);


INSERT INTO `sys_fill_rule` VALUES ('1202551334738382850', '机构编码生成', 'org_num_role', 'org.jeecg.modules.system.rule.OrgCodeRule', '{\"parentId\":\"c6d7cb4deeac411cb3384b1b31278596\"}', 'admin', '2019-12-9 10:37:06', 'admin', '2019-12-5 19:32:35');
INSERT INTO `sys_fill_rule` VALUES ('1202787623203065858', '分类字典编码生成', 'category_code_rule', 'org.jeecg.modules.system.rule.CategoryCodeRule', '{\"pid\":\"\"}', 'admin', '2019-12-9 10:36:54', 'admin', '2019-12-6 11:11:31');


INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1205097455226462210', '', '大屏设计', '/big/screen', 'layouts/RouteView', NULL, NULL, 0, NULL, '1', 10.00, 0, 'area-chart', 1, 0, 0, 0, NULL, 'admin', '2019-12-12 20:09:58', 'admin', '2019-12-12 20:36:56', 0, 0, '1', 0);
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1205098241075453953', '1205097455226462210', '生产销售监控', '{{ window._CONFIG[\'domianURL\'] }}/big/screen/index1', 'layouts/IframePageView', NULL, NULL, 1, NULL, '1', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-12-12 20:13:05', 'admin', '2019-12-12 20:15:27', 0, 0, '1', 1);

INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1205306106780364802', '1205097455226462210', '智慧物流监控', '{{ window._CONFIG[\'domianURL\'] }}/big/screen/index2', 'layouts/IframePageView', NULL, NULL, 1, NULL, '1', 2.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-12-13 09:59:04', NULL, NULL, 0, 0, '1', 1);


/*
 online报表增量sql
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('109c78a583d4693ce2f16551b7786786', 'e41b69c57a941a3bbcce45032fe57605', 'Online报表配置', '/online/cgreport', 'modules/online/cgreport/OnlCgreportHeadList', NULL, NULL, 1, NULL, NULL, 2.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-03-08 10:51:07', 'admin', '2019-03-30 19:04:28', 0, 0, NULL, NULL);
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('9fe26464838de2ea5e90f2367e35efa0', 'e41b69c57a941a3bbcce45032fe57605', 'AUTO在线报表', '/online/cgreport/:code', 'modules/online/cgreport/auto/OnlCgreportAutoList', 'onlineAutoList', NULL, 1, NULL, NULL, 9.00, 0, NULL, 1, 1, NULL, 1, NULL, 'admin', '2019-03-12 11:06:48', 'admin', '2019-04-30 18:19:10', 0, 0, NULL, NULL);


-- ----------------------------
-- Table structure for onl_cgreport_head
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgreport_head`;
CREATE TABLE `onl_cgreport_head`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '报表编码',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '报表名字',
  `cgr_sql` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '报表SQL',
  `return_val_field` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回值字段',
  `return_txt_field` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回文本字段',
  `return_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '返回类型，单选或多选',
  `db_source` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '动态数据源',
  `content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_onlinereport_code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of onl_cgreport_head
-- ----------------------------
INSERT INTO `onl_cgreport_head` VALUES ('6c7f59741c814347905a938f06ee003c', 'report_user', '统计在线用户', 'select * from sys_user', NULL, NULL, '1', NULL, NULL, '2019-11-22 16:34:31', 'admin', '2019-03-25 11:20:45', 'admin');
INSERT INTO `onl_cgreport_head` VALUES ('87b55a515d3441b6b98e48e5b35474a6', 'demo', 'Report Demo', 'select * from demo', NULL, NULL, '1', NULL, NULL, '2019-03-15 18:18:17', 'admin', '2019-03-12 11:25:16', 'admin');

-- ----------------------------
-- Table structure for onl_cgreport_item
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgreport_item`;
CREATE TABLE `onl_cgreport_item`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `cgrhead_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '报表ID',
  `field_name` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字段名字',
  `field_txt` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段文本',
  `field_width` int(3) NULL DEFAULT NULL,
  `field_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段类型',
  `search_mode` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '查询模式',
  `is_order` int(2) NULL DEFAULT 0 COMMENT '是否排序  0否,1是',
  `is_search` int(2) NULL DEFAULT 0 COMMENT '是否查询  0否,1是',
  `dict_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典CODE',
  `field_href` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段跳转URL',
  `is_show` int(2) NULL DEFAULT 1 COMMENT '是否显示  0否,1显示',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '排序',
  `replace_val` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '取值表达式',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_CGRHEAD_ID`(`cgrhead_id`) USING BTREE,
  INDEX `index_isshow`(`is_show`) USING BTREE,
  INDEX `index_order_num`(`order_num`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of onl_cgreport_item
-- ----------------------------
INSERT INTO `onl_cgreport_item` VALUES ('0ba0dc69589a85a96be30f59451c81df', 'b32a3fdd008f4506b2bac4ac00f0bd4f', '222', '222', NULL, 'String', NULL, 0, 0, '', '', 1, 0, '', 'admin', '2019-05-31 14:37:25', 'admin', '2019-05-31 14:37:30');
INSERT INTO `onl_cgreport_item` VALUES ('1740bb02519db90c44cb2cba8b755136', '6c7f59741c814347905a938f06ee003c', 'realname', '用户名称', NULL, 'String', NULL, 0, 0, '', '', 1, 3, '', 'admin', '2019-11-22 16:34:31', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('1b181e6d2813bcb263adc39737f9df46', '87b55a515d3441b6b98e48e5b35474a6', 'name', '用户名', NULL, 'String', 'single', 0, 1, '', '', 1, 4, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');
INSERT INTO `onl_cgreport_item` VALUES ('1fb45af29af4e792bdc5a4a2c06a4d4d', '402880ec5d872157015d87f2dd940010', 'data_table', '表名', NULL, 'String', NULL, 0, 0, NULL, NULL, 1, 0, NULL, 'admin', '2019-03-20 13:24:21', 'admin', '2019-03-20 13:25:08');
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ec0003', '402880e64e1ef94d014e1efefc2a0001', 'id', 'id', 0, 'String', '', NULL, 0, '', '', 0, 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ed0004', '402880e64e1ef94d014e1efefc2a0001', 'accountname', 'accountname', 0, 'String', '', NULL, 0, '', '', 0, 1, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ed0005', '402880e64e1ef94d014e1efefc2a0001', 'accounttoken', 'accounttoken', 0, 'String', '', NULL, 0, '', '', 0, 2, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ed0006', '402880e64e1ef94d014e1efefc2a0001', 'accountnumber', 'accountnumber', 0, 'String', '', NULL, 0, '', '', 0, 3, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ed0007', '402880e64e1ef94d014e1efefc2a0001', 'accounttype', 'accounttype', 0, 'String', '', NULL, 0, '', '', 0, 4, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ed0008', '402880e64e1ef94d014e1efefc2a0001', 'accountemail', 'accountemail', 0, 'String', '', NULL, 0, '', '', 0, 5, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ed0009', '402880e64e1ef94d014e1efefc2a0001', 'accountdesc', 'accountdesc', 0, 'String', '', NULL, 0, '', '', 0, 6, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ee000b', '402880e64e1ef94d014e1efefc2a0001', 'accountappid', 'accountappid', 0, 'String', '', NULL, 0, '', '', 0, 8, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ee000c', '402880e64e1ef94d014e1efefc2a0001', 'accountappsecret', 'accountappsecret', 0, 'String', '', NULL, 0, '', '', 0, 9, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ee000d', '402880e64e1ef94d014e1efefc2a0001', 'ADDTOEKNTIME', 'ADDTOEKNTIME', 0, 'String', '', NULL, 0, '', '', 0, 10, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ee000e', '402880e64e1ef94d014e1efefc2a0001', 'USERNAME', 'USERNAME', 0, 'String', '', NULL, 0, '', '', 0, 11, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ee0010', '402880e64e1ef94d014e1efefc2a0001', 'jsapiticket', 'jsapiticket', 0, 'String', '', NULL, 0, '', '', 0, 13, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ee0011', '402880e64e1ef94d014e1efefc2a0001', 'jsapitickettime', 'jsapitickettime', 0, 'String', '', NULL, 0, '', '', 0, 14, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ef0012', '402880e64e1ef94d014e1efefc2a0001', 'init_data_flag', 'init_data_flag', 0, 'String', '', NULL, 0, '', '', 0, 15, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f363aa9a380163aa9ebe490002', '402881f363aa9a380163aa9ebe480001', 'id', 'id', 0, 'String', '', NULL, 0, '', '', 0, 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f363aa9a380163aa9ebe490003', '402881f363aa9a380163aa9ebe480001', 'name', 'name', 0, 'String', 'single', NULL, 0, '', '', 0, 10, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f363aa9a380163aa9ebe490004', '402881f363aa9a380163aa9ebe480001', 'sex', 'sex', 0, 'String', 'single', NULL, 0, 'sex', '', 0, 11, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f363aa9a380163aa9ebe490005', '402881f363aa9a380163aa9ebe480001', 'age', 'age', 0, 'String', '', NULL, 0, '', '', 0, 12, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f363aa9a380163aa9ebe490006', '402881f363aa9a380163aa9ebe480001', 'address', 'address', 0, 'String', '', NULL, 0, '', '', 0, 13, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f363aa9a380163aa9ebe490007', '402881f363aa9a380163aa9ebe480001', 'phone', 'phone', 0, 'String', '', NULL, 0, '', '', 0, 14, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f363aa9a380163aa9ebe4a0008', '402881f363aa9a380163aa9ebe480001', 'memo', 'memo', 0, 'String', '', NULL, 0, '', '', 0, 15, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d2c0004', '402880e64eb9a22c014eb9a4d5890001', 'ID', 'ID', 0, 'String', '', NULL, 0, '', '', 0, 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d2c0005', '402880e64eb9a22c014eb9a4d5890001', 'activitiSync', 'activitiSync', 0, 'String', '', NULL, 0, '', '', 0, 1, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d2d0006', '402880e64eb9a22c014eb9a4d5890001', 'browser', 'browser', 0, 'String', '', NULL, 0, '', '', 0, 2, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d2d0007', '402880e64eb9a22c014eb9a4d5890001', 'password', 'password', 0, 'String', '', NULL, 0, '', '', 0, 3, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d2e0008', '402880e64eb9a22c014eb9a4d5890001', 'realname', 'realname', 0, 'String', '', NULL, 0, '', '', 0, 4, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d2e0009', '402880e64eb9a22c014eb9a4d5890001', 'signature', 'signature', 0, 'String', '', NULL, 0, '', '', 0, 5, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d2f000a', '402880e64eb9a22c014eb9a4d5890001', 'status', 'status', 0, 'String', '', NULL, 0, '', '', 0, 6, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d30000b', '402880e64eb9a22c014eb9a4d5890001', 'userkey', 'userkey', 0, 'String', '', NULL, 0, '', '', 0, 7, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d30000c', '402880e64eb9a22c014eb9a4d5890001', 'username', 'username', 0, 'String', '', NULL, 0, '', '', 0, 8, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d31000d', '402880e64eb9a22c014eb9a4d5890001', 'departid', 'departid', 0, 'String', '', NULL, 0, '', '', 0, 9, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d31000e', '402880e64eb9a22c014eb9a4d5890001', 'user_name_en', 'user_name_en', 0, 'String', '', NULL, 0, '', '', 0, 10, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d32000f', '402880e64eb9a22c014eb9a4d5890001', 'delete_flag', 'delete_flag', 0, 'String', '', NULL, 0, '', '', 0, 11, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f3647e95be01647eb88c400003', '402880e74d76e784014d76f9e783001e', 'account', 'account', 0, 'String', 'single', NULL, 0, '', '', 0, 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f3647e95be01647eb88c410004', '402880e74d76e784014d76f9e783001e', 'realname', 'realname', 0, 'String', '', NULL, 0, '', '', 0, 1, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('61ef5b323134938fdd07ad5e3ea16cd3', '87b55a515d3441b6b98e48e5b35474a6', 'key_word', '关键词', NULL, 'String', 'single', 0, 1, '', '', 1, 5, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');
INSERT INTO `onl_cgreport_item` VALUES ('627768efd9ba2c41e905579048f21000', '6c7f59741c814347905a938f06ee003c', 'username', '用户账号', NULL, 'String', 'single', 0, 1, '', '', 1, 2, '', 'admin', '2019-11-22 16:34:31', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('8a2dfe672f3c0d391ace4a9f9bf564ff', '402880ec5d872157015d87f2dd940010', 'data_id', '数据ID', NULL, 'String', NULL, 0, 0, NULL, NULL, 1, 0, NULL, 'admin', '2019-03-20 13:24:21', 'admin', '2019-03-20 13:25:08');
INSERT INTO `onl_cgreport_item` VALUES ('8bb087a9aa2000bcae17a1b3f5768435', '6c7f59741c814347905a938f06ee003c', 'sex', '性别', NULL, 'Integer', 'single', 0, 1, 'sex', '', 1, 5, '', 'admin', '2019-11-22 16:34:31', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('90d4fa57d301801abb26a9b86b6b94c4', '6c7f59741c814347905a938f06ee003c', 'birthday', '生日', NULL, 'Date', 'single', 0, 0, '', '', 1, 4, '', 'admin', '2019-11-22 16:34:31', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('9a0a7375479b7657e16c6a228354b495', '402880ec5d872157015d87f2dd940010', 'data_version', '数据版本', NULL, 'String', NULL, 0, 0, NULL, NULL, 1, 0, NULL, 'admin', '2019-03-20 13:24:21', 'admin', '2019-03-20 13:25:08');
INSERT INTO `onl_cgreport_item` VALUES ('a4ac355f07a05218854e5f23e2930163', '6c7f59741c814347905a938f06ee003c', 'avatar', '头像', NULL, 'String', NULL, 0, 0, '', '', 0, 6, '', 'admin', '2019-11-22 16:34:31', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('ae4d621e391a1392779175cf5a65134c', '87b55a515d3441b6b98e48e5b35474a6', 'update_by', '修改人', NULL, 'String', NULL, 0, 0, '', '', 1, 7, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');
INSERT INTO `onl_cgreport_item` VALUES ('b27bea35b1264003c79d38cb86d6929e', '6c7f59741c814347905a938f06ee003c', 'id', 'id', NULL, 'String', NULL, 0, 0, '', '', 0, 1, '', 'admin', '2019-11-22 16:34:31', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('ce5168755a734ea09dd190e28bf8d9f4', '87b55a515d3441b6b98e48e5b35474a6', 'update_time', '修改时间', NULL, 'String', NULL, 0, 0, '', '', 1, 2, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');
INSERT INTO `onl_cgreport_item` VALUES ('d6e86b5ffd096ddcc445c0f320a45004', '6c7f59741c814347905a938f06ee003c', 'phone', '手机号', NULL, 'String', NULL, 0, 0, '', '', 1, 11, '', 'admin', '2019-11-22 16:34:31', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('df365cd357699eea96c29763d1dd7f9d', '6c7f59741c814347905a938f06ee003c', 'email', '邮箱', NULL, 'String', NULL, 0, 0, '', '', 1, 14, '', 'admin', '2019-11-22 16:34:31', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('edf9932912b81ad01dd557d3d593a559', '87b55a515d3441b6b98e48e5b35474a6', 'age', '年龄', NULL, 'String', NULL, 0, 0, '', '', 1, 8, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');
INSERT INTO `onl_cgreport_item` VALUES ('f985883e509a6faaaf62ca07fd24a73c', '87b55a515d3441b6b98e48e5b35474a6', 'birthday', '生日', NULL, 'Date', 'single', 0, 1, '', '', 1, 1, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');
INSERT INTO `onl_cgreport_item` VALUES ('fce83e4258de3e2f114ab3116397670c', '87b55a515d3441b6b98e48e5b35474a6', 'punch_time', '发布时间', NULL, 'String', NULL, 0, 0, '', '', 1, 3, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');

-- ----------------------------
-- Table structure for onl_cgreport_param
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgreport_param`;
CREATE TABLE `onl_cgreport_param`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `cgrhead_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '动态报表ID',
  `param_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '参数字段',
  `param_txt` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数文本',
  `param_value` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数默认值',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_cgrheadid`(`cgrhead_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of onl_cgreport_param
-- ----------------------------
INSERT INTO `onl_cgreport_param` VALUES ('402881f36402f3de016403035d350010', '402880e64eb9a22c014eb9a4d5890001', 'usekey', 'usekey', '', 0, 'admin', '2018-06-15 18:35:09', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
