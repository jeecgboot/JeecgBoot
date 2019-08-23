
-- author:huangxutao-- date:20190710-- for: 用户表字段长度优化 ----------
-- 默认0未知在字典表添加0-未知属性
ALTER TABLE `sys_user`
MODIFY COLUMN `sex` tinyint(1) DEFAULT 0 COMMENT '性别(0-默认未知,1-男,2-女)';
-- tinyint 占1字节在类型、状态时候采用
ALTER TABLE `sys_user`
MODIFY COLUMN `status` tinyint(1) COMMENT '性别(1-正常,2-冻结)';

ALTER TABLE `sys_user`
MODIFY COLUMN `del_flag` tinyint(1) COMMENT '删除状态(0-正常,1-已删除)';

ALTER TABLE `sys_user`
MODIFY COLUMN `activiti_sync` tinyint(1) COMMENT '同步工作流引擎(1-同步,0-不同步)';

-- 保持与部门表中标识大小一致
ALTER TABLE `sys_user`
MODIFY COLUMN `org_code` VARCHAR(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '机构编码';
-- author:huangxutao -- date:20190710--  for: 用户表字段长度优化 ----------



-- --author:os_chengtgen-- date:20190722-- for: 日志操作添加类型(数据字典)--------
INSERT INTO `sys_dict` (`id`,`dict_name`,`dict_code`,`description`,`del_flag`,`create_by`, `create_time`,`update_by` ,`update_time`,`type`)
VALUES ('880a895c98afeca9d9ac39f29e67c13e', '操作类型', 'operate_type', '操作类型', 0, 'admin', '2019-07-22 10:54:29', NULL, NULL, 0);

INSERT INTO `sys_dict_item` (`id`,`dict_id`,`item_text`,`item_value`,`description`,`sort_order`,`status`,`create_by` ,`create_time`,`update_by`,`update_time`)
VALUES ('948923658baa330319e59b2213cda97c', '880a895c98afeca9d9ac39f29e67c13e', '添加', '2', '', 2, 1, 'admin', '2019-07-22 10:54:59', 'admin', '2019-07-22 10:55:36'),
  ('5d833f69296f691843ccdd0c91212b6b', '880a895c98afeca9d9ac39f29e67c13e', '修改', '3', '', 3, 1, 'admin', '2019-07-22 10:55:07', 'admin', '2019-07-22 10:55:41'),
  ('a1e7d1ca507cff4a480c8caba7c1339e', '880a895c98afeca9d9ac39f29e67c13e', '导出', '6', '', 6, 1, 'admin', '2019-07-22 12:06:50', NULL, NULL),
  ('bcec04526b04307e24a005d6dcd27fd6', '880a895c98afeca9d9ac39f29e67c13e', '导入', '5', '', 5, 1, 'admin', '2019-07-22 12:06:41', NULL, NULL),
  ('f80a8f6838215753b05e1a5ba3346d22', '880a895c98afeca9d9ac39f29e67c13e', '删除', '4', '', 4, 1, 'admin', '2019-07-22 10:55:14', 'admin', '2019-07-22 10:55:30'),
  ('c53da022b9912e0aed691bbec3c78473', '880a895c98afeca9d9ac39f29e67c13e', '查询', '1', '', 1, 1, 'admin', '2019-07-22 10:54:51', NULL, NULL);
-- --author:os_chengtgen-- date:20190722-- for: 日志操作添加类型(数据字典)--------

-- author:sunjianlei-- date:20190814-- for: 新增表格合计示例 --------
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`) VALUES ('043780fa095ff1b2bec4dc406d76f023', '2a470fc0c3954d9dbb61de6d80846549', '表格合计', '/jeecg/tableTotal', 'jeecg/TableTotal', '1', NULL, NULL, '1', NULL, '1', '3', '0', NULL, '1', '0', '0', NULL, 'admin', '2019-08-14 10:28:46', NULL, NULL, '0', '0', '1');
-- author:sunjianlei-- date:20190814-- for: 新增表格合计示例 --------


INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `status`, `del_flag`, `rule_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e41b69c57a941a3bbcce45032fe57605', '', '在线开发', '/online', 'layouts/RouteView', NULL, NULL, 0, NULL, NULL, 5, 0, 'cloud', 1, 0, NULL, 0, NULL, NULL, 0, 0, 'admin', '2019-03-08 10:43:10', 'admin', '2019-05-11 10:36:01');


INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `status`, `del_flag`, `rule_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8d4683aacaa997ab86b966b464360338', 'e41b69c57a941a3bbcce45032fe57605', 'Online表单开发', '/online/cgform', 'modules/online/cgform/OnlCgformHeadList', NULL, NULL, 1, NULL, NULL, 1, 0, NULL, 1, 0, NULL, 0, NULL, NULL, 0, 0, 'admin', '2019-03-12 15:48:14', 'admin', '2019-06-11 14:19:17');


INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `status`, `del_flag`, `rule_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('fba41089766888023411a978d13c0aa4', 'e41b69c57a941a3bbcce45032fe57605', 'AUTO树表单列表', '/online/cgformTreeList/:code', 'modules/online/cgform/auto/OnlCgformTreeList', NULL, NULL, 1, NULL, '1', 9, 0, NULL, 1, 1, NULL, 1, NULL, '1', 0, 0, 'admin', '2019-05-21 14:46:50', 'admin', '2019-06-11 13:52:52');

INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `status`, `del_flag`, `rule_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('22d6a3d39a59dd7ea9a30acfa6bfb0a5', 'e41b69c57a941a3bbcce45032fe57605', 'AUTO动态表单', '/online/df/:table/:id', 'modules/online/cgform/auto/OnlineDynamicForm', NULL, NULL, 1, NULL, NULL, 9, 0, NULL, 0, 1, NULL, 1, NULL, NULL, 0, 0, 'admin', '2019-04-22 15:15:43', 'admin', '2019-04-30 18:18:26');

INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `status`, `del_flag`, `rule_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('54097c6a3cf50fad0793a34beff1efdf', 'e41b69c57a941a3bbcce45032fe57605', 'AUTO在线表单', '/online/cgformList/:code', 'modules/online/cgform/auto/OnlCgformAutoList', NULL, NULL, 1, NULL, NULL, 9, 0, NULL, 1, 1, NULL, 1, NULL, NULL, 0, 0, 'admin', '2019-03-19 16:03:06', 'admin', '2019-04-30 18:19:03');


INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `status`, `del_flag`, `rule_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0ac2ad938963b6c6d1af25477d5b8b51', '8d4683aacaa997ab86b966b464360338', '代码生成按钮', NULL, NULL, NULL, NULL, 2, 'online:goGenerateCode', '1', 1, 0, NULL, 1, 1, NULL, 0, NULL, '1', 0, 0, 'admin', '2019-06-11 14:20:09', NULL, NULL);


/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : localhost:3306
 Source Schema         : jeecg-boot-os

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 23/08/2019 23:46:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for onl_cgform_button
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgform_button`;
CREATE TABLE `onl_cgform_button`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
  `BUTTON_CODE` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '按钮编码',
  `BUTTON_ICON` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '按钮图标',
  `BUTTON_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '按钮名称',
  `BUTTON_STATUS` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '按钮状态',
  `BUTTON_STYLE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '按钮样式',
  `EXP` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表达式',
  `CGFORM_HEAD_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单ID',
  `OPT_TYPE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '按钮类型',
  `ORDER_NUM` int(11) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `index_formid`(`CGFORM_HEAD_ID`) USING BTREE,
  INDEX `index_button_code`(`BUTTON_CODE`) USING BTREE,
  INDEX `index_button_status`(`BUTTON_STATUS`) USING BTREE,
  INDEX `index_button_order`(`ORDER_NUM`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of onl_cgform_button
-- ----------------------------
INSERT INTO `onl_cgform_button` VALUES ('a45bc1c6fba96be6b0c91ffcdd6b54aa', 'genereate_person_config', 'icon-edit', '生成配置', '1', 'link', NULL, 'e2faf977fdaf4b25a524f58c2441a51c', 'js', NULL);

-- ----------------------------
-- Table structure for onl_cgform_enhance_java
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgform_enhance_java`;
CREATE TABLE `onl_cgform_enhance_java`  (
  `ID` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `BUTTON_CODE` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '按钮编码',
  `CG_JAVA_TYPE` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类型',
  `CG_JAVA_VALUE` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数值',
  `CGFORM_HEAD_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表单ID',
  `ACTIVE_STATUS` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '生效状态',
  `EVENT` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'end' COMMENT '事件状态(end:结束，start:开始)',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `index_fmid`(`CGFORM_HEAD_ID`) USING BTREE,
  INDEX `index_buttoncode`(`BUTTON_CODE`) USING BTREE,
  INDEX `index_status`(`ACTIVE_STATUS`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for onl_cgform_enhance_js
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgform_enhance_js`;
CREATE TABLE `onl_cgform_enhance_js`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
  `CG_JS` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'JS增强内容',
  `CG_JS_TYPE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `CONTENT` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `CGFORM_HEAD_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单ID',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `index_fmid`(`CGFORM_HEAD_ID`) USING BTREE,
  INDEX `index_jstype`(`CG_JS_TYPE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of onl_cgform_enhance_js
-- ----------------------------
INSERT INTO `onl_cgform_enhance_js` VALUES ('274b5d741a0262d3411958f0c465c5f0', 'genereate_person_config(row){\nconsole.log(\'选择\',row)\nalert(row.name + \'，个人积分配置生成成功！\');\n}', 'list', NULL, 'e2faf977fdaf4b25a524f58c2441a51c');
INSERT INTO `onl_cgform_enhance_js` VALUES ('2cbaf25f1edb620bea2d8de07f8233a1', 'air_china_post_materiel_item_onlChange(){\n    return {\n        wl_name(){\n           \n            let id = event.row.id\n            let cnum = event.row.num\n            let value = event.value\n            let targrt = event.target\n            let columnKey = event.column.key\n           let nval = 200*cnum\n           console.log(\'row\',event.row);\n           console.log(\'cnum\',cnum);\n           let otherValues = {\'jifen\': nval}\n              \n                that.triggleChangeValues(targrt,id,otherValues)\n\n        }\n    }\n}', 'form', NULL, 'e67d26b610dd414c884c4dbb24e71ce3');
INSERT INTO `onl_cgform_enhance_js` VALUES ('35d4ef464e5e8c87c9aa82ea89215fc1', '', 'list', NULL, 'e67d26b610dd414c884c4dbb24e71ce3');

-- ----------------------------
-- Table structure for onl_cgform_enhance_sql
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgform_enhance_sql`;
CREATE TABLE `onl_cgform_enhance_sql`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
  `BUTTON_CODE` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '按钮编码',
  `CGB_SQL` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'SQL内容',
  `CGB_SQL_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Sql名称',
  `CONTENT` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `CGFORM_HEAD_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单ID',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `index_formid`(`CGFORM_HEAD_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for onl_cgform_field
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgform_field`;
CREATE TABLE `onl_cgform_field`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
  `cgform_head_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表ID',
  `db_field_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字段名字',
  `db_field_txt` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段备注',
  `db_field_name_old` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原字段名',
  `db_is_key` tinyint(1) NULL DEFAULT NULL COMMENT '是否主键 0否 1是',
  `db_is_null` tinyint(1) NULL DEFAULT NULL COMMENT '是否允许为空0否 1是',
  `db_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据库字段类型',
  `db_length` int(11) NOT NULL COMMENT '数据库字段长度',
  `db_point_length` int(11) NULL DEFAULT NULL COMMENT '小数点',
  `db_default_val` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表字段默认值',
  `dict_field` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典code',
  `dict_table` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典表',
  `dict_text` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典Text',
  `field_show_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单控件类型',
  `field_href` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '跳转URL',
  `field_length` int(11) NULL DEFAULT NULL COMMENT '表单控件长度',
  `field_valid_type` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单字段校验规则',
  `field_must_input` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段是否必填',
  `field_extend_json` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展参数JSON',
  `field_value_rule_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '填值规则code',
  `is_query` tinyint(1) NULL DEFAULT NULL COMMENT '是否查询条件0否 1是',
  `is_show_form` tinyint(1) NULL DEFAULT NULL COMMENT '表单是否显示0否 1是',
  `is_show_list` tinyint(1) NULL DEFAULT NULL COMMENT '列表是否显示0否 1是',
  `query_mode` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '查询模式',
  `main_table` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外键主表名',
  `main_field` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外键主键字段',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '排序',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `inex_table_id`(`cgform_head_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of onl_cgform_field
-- ----------------------------
INSERT INTO `onl_cgform_field` VALUES ('0021c969dc23a9150d6f70a13b52e73e', '402860816aa5921f016aa5921f480000', 'begin_date', '开始时间', 'begin_date', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 4, 'admin', '2019-05-11 15:31:54', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('020c1622c3df0aef30185f57874f6959', '79091e8277c744158530321513119c68', 'bpm_status', '流程状态', NULL, 0, 1, 'String', 32, 0, '1', 'bpm_status', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 1, 'single', '', '', 8, 'admin', '2019-05-11 15:29:47', '2019-05-11 15:29:26', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('02b20e692456403e2fed1a89a06833b4', '402860816bff91c0016bff91d2810005', 'phone', '联系方式', 'phone', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 8, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('03668009f0ad92b20abb1a377197ee47', 'deea5a8ec619460c9245ba85dbc59e80', 'order_fk_id', '订单外键ID', NULL, 0, 0, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', 'test_order_main', 'id', 10, 'admin', '2019-06-10 16:07:16', '2019-04-20 11:42:53', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('03709092184fdf4a66b0cdb4dd10a159', '402860816bff91c0016bffa220a9000b', 'bpm_status', '流程状态', NULL, 0, 1, 'String', 32, 0, '1', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 46, 'admin', '2019-07-22 16:15:32', '2019-07-19 15:34:44', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('03c105d2706c8286416833684de67406', '79091e8277c744158530321513119c68', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 2, 'admin', '2019-05-11 15:29:47', '2019-05-11 15:27:17', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('03fd5ab69f331ff760c3f7d86b4a93f8', '4028318169e81b970169e81b97650000', 'log_content', '日志内容', 'log_content', 0, 1, 'string', 1000, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 'group', NULL, NULL, 3, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('045eb432c418b2b103b1e1b8e8a8a75d', 'fb7125a344a649b990c12949945cb6c1', 'age', '年龄', NULL, 0, 1, 'int', 32, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', NULL, NULL, NULL, '2019-03-26 19:24:11', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('04e4185a503e6aaaa31c243829ff4ac7', 'd35109c3632c4952a19ecc094943dd71', 'birthday', '生日', NULL, 0, 1, 'Date', 32, 0, '', '', '', '', 'date', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', 9, 'admin', '2019-08-23 23:45:15', '2019-03-15 14:24:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('04ff134cb4aae845059e10b3b85f1451', '7ea60a25fa27470e9080d6a921aabbd1', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 5, NULL, NULL, '2019-04-17 00:22:21', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('051dd70c504c97a028daab2af261ea35', '1acb6f81a1d9439da6cc4e868617b565', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 5, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('052dcc6f34976b66754fd99415bd22ce', '79091e8277c744158530321513119c68', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 5, 'admin', '2019-05-11 15:29:47', '2019-05-11 15:27:17', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('054db05394e83b318f097a60bc044134', '402860816bff91c0016bffa220a9000b', 'residence_address', '户籍地址', 'residence_address', 0, 1, 'string', 200, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 28, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('0604945c206e867644e9a44b4c9b20c6', 'fb19fb067cd841f9ae93d4eb3b883dc0', '2', '4', NULL, 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', 5, NULL, NULL, '2019-03-23 11:39:48', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('06a1badae6119abf4ec48858a3e94e1c', '402860816bff91c0016bffa220a9000b', 'sys_org_code', '组织机构编码', 'sys_org_code', 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 43, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('06f1cfff58395ff62526b894f6182641', 'e67d26b610dd414c884c4dbb24e71ce3', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 2, 'admin', '2019-04-24 11:03:32', '2019-04-24 11:02:57', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('07a307972076a392ffc61b11437f89dd', '402860816bff91c0016bff91c0cb0000', 'create_time', '创建时间', 'create_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 13, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('07f4776fd641389a8c98a85713990dce', '402860816bff91c0016bff91c0cb0000', 'update_by', '更新人', 'update_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 14, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('09450359eb90b40d224ec43588a62f9e', '402860816bff91c0016bff91c0cb0000', 'user_id', '用户ID', 'user_id', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 3, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('0a4cdcb7e54f614ab952024f6c72bb6d', 'beee191324fd40c1afec4fda18bd9d47', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 5, 'admin', '2019-04-13 13:41:13', '2019-04-13 13:40:56', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('0adc06d9b497684bcbb5a781e044517c', '1acb6f81a1d9439da6cc4e868617b565', 'supplier', '供应商', NULL, 0, 1, 'String', 32, 0, '', 'air_china_ supplier', '', '', 'list', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 15, 'admin', '2019-06-10 14:47:14', '2019-04-24 16:52:00', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('0ba1bf74e2a6a94a7a63010ec7230706', '402860816bff91c0016bffa220a9000b', 'update_time', '更新时间', 'update_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 42, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('0cba94f0497d4d3d829fc573f58eff9f', '402860816bff91c0016bffa220a9000b', 'graduation_time', '毕业时间', 'graduation_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 16, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('0d00c51a4ddad2598a587fadc968a8b2', '402860816bff91c0016bff91cfea0004', 'sys_org_code', '组织机构编码', 'sys_org_code', 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 13, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('0ddd0c0afc967a9ab6050401ca62a4be', 'e67d26b610dd414c884c4dbb24e71ce3', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 3, 'admin', '2019-04-24 11:03:32', '2019-04-24 11:02:57', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('0fb6fa76c5c78a1e957dbb411e110738', '402860816bff91c0016bff91d8830007', 'politically_status', '政治面貌', 'politically_status', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 7, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('105c8e44ad13026b641f0363601f30f3', 'e5464aa8fa7b47c580e91593cf9b46dc', 'num', '循环数量', NULL, 0, 1, 'int', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 9, 'admin', '2019-04-24 17:09:49', '2019-04-24 11:05:10', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1130f1e252533529bb1167b896dffe32', 'deea5a8ec619460c9245ba85dbc59e80', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 5, 'admin', '2019-06-10 16:07:16', '2019-04-20 11:41:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('117fc4ba649d6690a3ac482ad5e4ad38', '56870166aba54ebfacb20ba6c770bd73', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 3, 'admin', '2019-06-10 16:01:35', '2019-04-20 11:38:39', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('13246645b7650491b70205d99703ca06', '402860816aa5921f016aa5dedcb90009', 'bpm_status', '流程状态', 'bpm_status', 0, 1, 'string', 32, 0, '1', 'bpm_status', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 1, 'group', '', '', 8, 'admin', '2019-05-11 15:56:47', '2019-05-11 15:50:08', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('135dd0ee50712722db65b8762bd487ea', '8994f2817b5a45d9890aa04497a317c5', 'update_time', '更新日期', NULL, 0, 1, 'date', 20, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', 4, NULL, NULL, '2019-03-23 11:39:16', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('14ec4c83c29966ab42b6b718c5a3e774', '7ea60a25fa27470e9080d6a921aabbd1', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 2, NULL, NULL, '2019-04-17 00:22:21', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('16363d0bc125125e395772278d0cf22e', '4b556f0168f64976a3d20bfb932bc798', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 5, NULL, NULL, '2019-04-12 23:38:28', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('179c290e383009810fb738f07bd5af8d', '402860816bff91c0016bff91d2810005', 'id', 'id', 'id', 1, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'group', '', '', 1, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('17cbda69da9dd3632625a0647c259070', '73162c3b8161413e8ecdca7eb288d0c9', 'wl_name', '物料名字', NULL, 0, 1, 'String', 200, 0, '', '', '', '', 'text', '', 120, NULL, '1', '', '', 0, 1, 1, 'single', '', '', 7, 'admin', '2019-07-01 14:23:32', '2019-06-10 15:18:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('18fefb2257901f05508f8ec13ada78a3', 'e5464aa8fa7b47c580e91593cf9b46dc', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 1, 'admin', '2019-04-24 17:09:48', '2019-04-24 11:05:10', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1973ef1d3728fbff2db6a352e001f5f7', 'fb7125a344a649b990c12949945cb6c1', 'name', '用户名', NULL, 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', 5, 'admin', '2019-03-26 19:24:11', '2019-03-26 19:01:52', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1ab5be1f937f393b3e5cc214ef1b855c', '7ea60a25fa27470e9080d6a921aabbd1', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 4, NULL, NULL, '2019-04-17 00:22:21', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1b6c7b95028bed9ff656d65557dd2bdf', '402860816bff91c0016bffa220a9000b', 'user_id', '用户id', 'user_id', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 3, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1c2f307e315bac77a6d3f02e88387a43', 'deea5a8ec619460c9245ba85dbc59e80', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 1, 'admin', '2019-06-10 16:07:16', '2019-04-20 11:41:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1c3b2ad0a52ecb47fa7fd53f25875beb', 'deea5a8ec619460c9245ba85dbc59e80', 'price', '价格', NULL, 0, 1, 'double', 32, 0, '', '', '', '', 'text', '', 120, 'n', '0', '', '', 0, 1, 1, 'single', '', '', 7, 'admin', '2019-06-10 16:07:16', '2019-04-20 11:41:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1c4d25a12388c80d397bb4f4664fe4e6', '4b556f0168f64976a3d20bfb932bc798', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 4, NULL, NULL, '2019-04-12 23:38:28', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1cfe967bb457cbaa6e041e45d019b583', '402860816bff91c0016bff91c7010001', 'update_time', '更新时间', 'update_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 10, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1e3d8cfbf12155559666a23ee2c6c5ca', 'e5464aa8fa7b47c580e91593cf9b46dc', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 5, 'admin', '2019-04-24 17:09:49', '2019-04-24 11:05:10', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1ed46fdeb289bd7805c9b83332ccd3b4', '402860816bff91c0016bff91d2810005', 'relation', '关系', 'relation', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 4, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1fa5f07b3e70d4925b69b2bf51309421', '56870166aba54ebfacb20ba6c770bd73', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 5, 'admin', '2019-06-10 16:01:35', '2019-04-20 11:38:39', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('20ff34fb0466089cb633d73d5a6f08d6', 'd35109c3632c4952a19ecc094943dd71', 'update_time', '更新日期', NULL, 0, 1, 'date', 20, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 0, 0, 'single', '', '', 4, 'admin', '2019-08-23 23:45:15', '2019-03-15 14:24:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2113a4ec7b88b4820dcbbdf96e46bbb7', 'fbc35f067da94a70adb622ddba259352', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 3, NULL, NULL, '2019-07-03 19:44:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2150e48b2cb6072d2d8ecd79a7daf7cc', '402860816bff91c0016bff91ca7e0002', 'create_time', '创建时间', 'create_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 10, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2323239efb5a40b73034411868dfc41d', 'fb19fb067cd841f9ae93d4eb3b883dc0', 'update_by', '更新人登录名称', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', 3, NULL, NULL, '2019-03-23 11:39:48', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('23f42061ed218bdbc1262913c071e1cd', 'e5464aa8fa7b47c580e91593cf9b46dc', 'iz_valid', '启动状态', NULL, 0, 1, 'int', 2, 0, '', 'air_china_valid', '', '', 'list', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 12, 'admin', '2019-04-24 17:09:49', '2019-04-24 14:09:06', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('242cc59b23965a92161eca69ffdbf018', 'd35109c3632c4952a19ecc094943dd71', 'age', '年龄', NULL, 0, 1, 'int', 32, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', 7, 'admin', '2019-08-23 23:45:15', '2019-03-15 14:24:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('265702edb8872e322fe72d3640e34ac5', '402860816bff91c0016bff91cfea0004', 'from_time', '开始日期', 'from_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 3, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('284864d99fddfdcb00e188e3a512cb28', '1acb6f81a1d9439da6cc4e868617b565', 'no', '预算表序号', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 10, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2889d3cef706f91e092d76a56b8055be', '402860816bff91c0016bff91cda80003', 'order_no', '序号', 'order_no', 0, 1, 'int', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 8, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('29e4abea55d9fa7dbbd0c8dbbb2b3756', '402860816bff91c0016bff91cda80003', 'update_time', '更新时间', 'update_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 12, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2d53a66f0b72d820b86ff445e2181d76', 'beee191324fd40c1afec4fda18bd9d47', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 1, 'admin', '2019-04-13 13:41:13', '2019-04-13 13:40:56', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2dfc4c81926f678c5f8d5ffd27858201', 'e2faf977fdaf4b25a524f58c2441a51c', 'account', '用户编码', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 7, 'admin', '2019-06-10 17:27:00', '2019-04-24 17:12:11', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2e5275b6407e1b4265af8519077fa4a5', 'd3ae1c692b9640e0a091f8c46e17bb01', 'sys_org_code', '所属部门', NULL, 0, 1, 'string', 64, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 6, NULL, NULL, '2019-07-24 14:47:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2e66b9db37648389e0846e2204111732', '73162c3b8161413e8ecdca7eb288d0c9', 'has_child', '是否有子节点', NULL, 0, 1, 'string', 3, 0, '', 'valid_status', '', '', 'list', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 10, 'admin', '2019-07-01 14:23:32', '2019-06-10 15:18:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2ee58d8e4844dfe1fa6b1b841ae0b312', '402860816bff91c0016bff91d2810005', 'politically_status', '政治面貌', 'politically_status', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 7, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2f111722eb3a994450e67e3211fd69a8', '402860816bff91c0016bff91ca7e0002', 'id', 'id', 'id', 1, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'group', '', '', 1, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('30143cc3de69c413828f9fba20662026', '402860816bff91c0016bffa220a9000b', 'healthy', '健康状况', 'healthy', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 12, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('31fd90306c3942f09cb79deabbf2f541', '402860816bff91c0016bff91d2810005', 'employee_id', '员工ID', 'employee_id', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', 'oa_employee_info', 'id', 2, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('336495117e3a31351fed6963b950dddb', '402860816bff91c0016bffa220a9000b', 'inside_transfer', '内部工作调动情况', 'inside_transfer', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 37, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('345c8b48e1e128e77c4c6e2b36512804', '402860816aa5921f016aa5dedcb90009', 'create_by', '创建人', 'create_by', 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'group', '', '', 2, 'admin', '2019-05-11 15:56:47', '2019-05-11 15:50:08', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('351faaeb2dd8105e9c66f678211c9d4f', 'dbf4675875e14676a3f9a8b2b8941140', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 5, NULL, NULL, '2019-05-27 18:02:07', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('354b2ce39c5e8ec3f0bbb01bf8ff0fb7', '32f75e4043ef4070919dbd4337186a3d', 'content', '描述', NULL, 0, 1, 'String', 300, 0, '', '', '', '', 'textarea', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 9, 'admin', '2019-04-11 10:15:31', '2019-03-28 15:24:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('35ca1c8aa1501bc8a79c880928841f18', '402860816aa5921f016aa5921f480000', 'update_by', '修改人id', 'update_by', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'group', '', '', 11, 'admin', '2019-05-11 15:31:55', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('3635793767371c6db9f76b4b79f9d321', '402860816bff91c0016bff91d8830007', 'create_time', '创建时间', 'create_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 11, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('370a6eebc2d732eaf121fe0830d853a6', 'e5464aa8fa7b47c580e91593cf9b46dc', 'wl_code', '物料编码', NULL, 0, 1, 'String', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 7, 'admin', '2019-04-24 17:09:49', '2019-04-24 11:05:10', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('37e2548874f09ef7d08642a30bc918fa', 'fbc35f067da94a70adb622ddba259352', 'group_name', '小组名', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 7, NULL, NULL, '2019-07-03 19:44:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('391e7cbd9f29743b11bb555c50547b1f', '32f75e4043ef4070919dbd4337186a3d', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 4, 'admin', '2019-04-11 10:15:32', '2019-03-27 15:54:49', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('3b439859f98e30e34d25e983eb22e408', '402860816bff91c0016bff91c7010001', 'award_time', '获奖时间', 'award_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 3, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('3bf44e68de518f3ddf72b87671d0ff90', '8994f2817b5a45d9890aa04497a317c5', 'update_by', '更新人登录名称', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', 3, NULL, NULL, '2019-03-23 11:39:16', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('3cd2061ea15ce9eeb4b7cf2e544ccb6b', 'd35109c3632c4952a19ecc094943dd71', 'file_kk', '附件', NULL, 0, 1, 'String', 500, 0, '', '', '', '', 'file', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 12, 'admin', '2019-08-23 23:45:15', '2019-06-10 20:06:57', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('3cfd4d60c7d8409ae716a579bcb0910d', '402860816bff91c0016bff91c0cb0000', 'sys_org_code', '组织机构编码', 'sys_org_code', 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 16, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('3e32f6c30c9028872388f70743c5d6a5', '402860816bff91c0016bff91c0cb0000', 'reason', '申请理由', 'reason', 0, 1, 'string', 200, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 9, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('3e70d1c516c3533c6698300665c669e1', '402860816bff91c0016bff91c0cb0000', 'id', 'id', 'id', 1, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'group', '', '', 1, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:31', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('3f2ace8f968a0e5b91d1340ee2957cda', '402860816bff91c0016bff91d8830007', 'real_name', '姓名', 'real_name', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 3, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('40471eb4560bf0bbd2ffef17d48a269d', 'dbf4675875e14676a3f9a8b2b8941140', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 4, NULL, NULL, '2019-05-27 18:02:07', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('404b516d4f2229f292783db595b02ba1', '402860816bff91c0016bff91d8830007', 'update_time', '更新时间', 'update_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 13, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('405de5ea82e54138a0613dd41b006dfb', '56870166aba54ebfacb20ba6c770bd73', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 4, 'admin', '2019-06-10 16:01:35', '2019-04-20 11:38:39', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('4164314d6a51d100169a29872b7504d8', '402860816bff91c0016bff91ca7e0002', 'cert_time', '发证时间', 'cert_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 3, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('41d4215c01b0d26871f2cb83d3e532ae', '402860816bff91c0016bff91c0cb0000', 'bpm_status', '流程状态', NULL, 0, 1, 'String', 32, 0, '1', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 17, 'admin', '2019-07-19 18:09:01', '2019-07-19 15:35:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('422a44a15fa39fd57c3c23eb601f7c03', '56870166aba54ebfacb20ba6c770bd73', 'descc', '描述', NULL, 0, 1, 'String', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 8, 'admin', '2019-06-10 16:01:35', '2019-04-20 11:38:39', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('42cccfa014c9e131a0a1b23f563d3688', '402860816bff91c0016bffa220a9000b', 'sex', '性别', 'sex', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 6, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('4312f618c83e07db82e468b81a1eaa45', '402860816bff91c0016bffa220a9000b', 'photo', '照片', 'photo', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 20, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('44bdc595f1e565fc053e01134b92bb47', 'd3ae1c692b9640e0a091f8c46e17bb01', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 4, NULL, NULL, '2019-07-24 14:47:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('44e81e24d2384b0f187e8f69eda55390', '402860816bff91c0016bff91cda80003', 'create_time', '创建时间', 'create_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 10, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('45c0a1a89a1e2a72533b9af894be1011', '27fc5f91274344afa7673a732b279939', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 3, 'admin', '2019-07-01 16:28:20', '2019-07-01 16:26:42', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('45d59eb647257fcbcb9d143ff1ba2080', 'deea5a8ec619460c9245ba85dbc59e80', 'pro_type', '产品类型', NULL, 0, 1, 'String', 32, 0, '', 'sex', '', '', 'radio', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 9, 'admin', '2019-06-10 16:07:16', '2019-04-23 20:54:08', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('469b250595f15dfebe69991d72e4bfb2', 'e9faf717024b4aae95cff224ae9b6d97', 'name', '员工姓名', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 6, 'admin', '2019-07-03 18:23:49', '2019-07-03 18:22:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('46be01bef342519e268902d0d36a7473', 'deea5a8ec619460c9245ba85dbc59e80', 'descc', '描述', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 11, 'admin', '2019-06-10 16:07:16', '2019-04-20 11:41:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('46f1a875f86a4f48d0540ad0d5e667d7', '56870166aba54ebfacb20ba6c770bd73', 'order_date', '下单时间', NULL, 0, 1, 'Date', 32, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 7, 'admin', '2019-06-10 16:01:35', '2019-04-20 11:38:39', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('47c21a6b45e59a6b70bb9c0cc4510a68', '1acb6f81a1d9439da6cc4e868617b565', 'integral_val', '积分值', NULL, 0, 1, 'int', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 13, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('47fa05530f3537a1be8f9e7a9e98be82', 'd35109c3632c4952a19ecc094943dd71', 'sex', '性别', NULL, 0, 1, 'string', 32, 0, '', 'sex', '', '', 'list', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', 6, 'admin', '2019-08-23 23:45:15', '2019-03-15 14:24:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('4851697fdf63709d2bc7451b7415f4af', '32f75e4043ef4070919dbd4337186a3d', 'sex', '性别', NULL, 0, 1, 'String', 32, 0, '1', 'sex', '', '', 'list', '', 120, NULL, '0', '', '', 1, 1, 1, 'single', '', '', 6, 'admin', '2019-04-11 10:15:32', '2019-03-27 15:54:49', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('485a8ddce2c033f88af674ec98b68e32', '402860816bff91c0016bffa220a9000b', 'create_time', '创建时间', 'create_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 40, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('492a462b212fc34b0ee70e872684ed7e', '7ea60a25fa27470e9080d6a921aabbd1', 'name', '用户名', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 6, NULL, NULL, '2019-04-17 00:22:21', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('499a5dac033a01ce58009e4c5b786697', 'e9faf717024b4aae95cff224ae9b6d97', 'age', '员工年龄', NULL, 0, 1, 'int', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 7, 'admin', '2019-07-03 18:23:49', '2019-07-03 18:22:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('49cd3dbd4f7f7cf0d19b1ee1045cfa69', 'e67d26b610dd414c884c4dbb24e71ce3', 'post_code', '岗位编码', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 6, 'admin', '2019-04-24 11:03:32', '2019-04-24 11:02:57', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('4ba7c553ca4babcec75273c531cd65e1', '402860816bff91c0016bff91cfea0004', 'workplace', '工作单位', 'workplace', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 5, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('4c2cba9fc950333421c4193576b8384d', '32f75e4043ef4070919dbd4337186a3d', 'salary', '工资', NULL, 0, 1, 'double', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 10, 'admin', '2019-04-11 10:15:32', '2019-03-28 15:24:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('4c570c5cf05590348e12621ca62773cf', '402860816aa5921f016aa5921f480000', 'name', '请假人', 'name', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 2, 'admin', '2019-05-11 15:31:54', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('4cacfa054e96791ab938b5c8f8e02cd1', '27fc5f91274344afa7673a732b279939', 'bpm_status', '流程状态', NULL, 0, 1, 'String', 2, 0, '', 'bpm_status', '', '', 'list', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 9, NULL, NULL, '2019-07-01 16:28:20', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('4f718d95ad9de33eac18fd0663e4c1f1', '32f75e4043ef4070919dbd4337186a3d', 'birthday', '生日', NULL, 0, 1, 'Date', 32, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 1, 1, 1, 'single', '', '', 8, 'admin', '2019-04-11 10:15:32', '2019-03-27 15:54:49', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('4f7cba71de7afe6efbd024f5f9935521', '402860816bff91c0016bff91cda80003', 'to_time', '截止时间', 'to_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 4, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('506c9b0b2331a24e5c284274d28fe569', '27fc5f91274344afa7673a732b279939', 'kkk', '描述', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 8, 'admin', '2019-07-01 16:28:20', '2019-07-01 16:26:42', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('508eb28e1409a2a9501cdf6fd7eb24c7', 'dbf4675875e14676a3f9a8b2b8941140', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 2, NULL, NULL, '2019-05-27 18:02:07', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('509a4f63f02e784bc04499a6a9be8528', 'd35109c3632c4952a19ecc094943dd71', 'update_by', '更新人登录名称', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 0, 0, 'single', '', '', 3, 'admin', '2019-08-23 23:45:15', '2019-03-15 14:24:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('519f68557b953fc2d38400182b187366', '402860816bff91c0016bffa220a9000b', 'residence_type', '户籍类别', 'residence_type', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 13, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('52ee861bc1b62cd8e4f10632b3d9d1b2', '79091e8277c744158530321513119c68', 'name', '顺序会签标题', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 6, 'admin', '2019-05-11 15:29:47', '2019-05-11 15:27:17', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('56a7800e4e476812c74217c2aad781aa', '32feeb502544416c9bf41329c10a88f4', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 5, 'admin', '2019-08-23 20:03:40', '2019-07-02 18:23:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('56e247f12d62b49cd9bd537e3efecf16', '402860816bff91c0016bff91c0cb0000', 'create_by', '创建人', 'create_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 12, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('588400f6ebcdd0bc9bb560dd36636af9', 'e2faf977fdaf4b25a524f58c2441a51c', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 4, 'admin', '2019-06-10 17:27:00', '2019-04-24 17:12:11', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('58a96f945912d33b64ebf5dee98156dc', '402860816bff91c0016bffa220a9000b', 'mobile', '手机号', 'mobile', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 19, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('58eea85add4788b83c893092434bc413', 'fb19fb067cd841f9ae93d4eb3b883dc0', 'update_time', '更新日期', NULL, 0, 1, 'date', 20, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', 4, NULL, NULL, '2019-03-23 11:39:48', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('59ae1e853668c676e85329aa029c68a6', '402860816bff91c0016bff91c0cb0000', 'status', '状态（1：申请中 2：通过）', 'status', 0, 1, 'string', 2, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 11, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5a1ab458d88bb766f92c3d791495cdcd', '402860816bff91c0016bff91d2810005', 'age', '年龄', 'age', 0, 1, 'int', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 5, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5a4ef083dd572114aeb581b6828de545', '402860816bff91c0016bff91c7010001', 'award_name', '获奖名称', 'award_name', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 5, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5a621f27aa443fe9eccc73717e4fa172', '4028318169e81b970169e81b97650000', 'method', '请求java方法', 'method', 0, 1, 'string', 500, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 'group', NULL, NULL, 8, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5a655b208d6318ed02f236f15a319b5f', 'fbc35f067da94a70adb622ddba259352', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 5, NULL, NULL, '2019-07-03 19:44:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5a6f88710c49bbe8e2e0ca58e149abad', '402860816bff91c0016bff91cda80003', 'create_by', '创建人', 'create_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 9, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5b17ba693745c258f6b66380ac851e5f', 'd35109c3632c4952a19ecc094943dd71', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', 0, 'admin', '2019-08-23 23:45:15', '2019-03-15 14:24:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5c76f5ecc774d7339eb0c2199c0052bc', '402860816bff91c0016bff91c0cb0000', 'biz_no', '编号', 'biz_no', 0, 1, 'string', 64, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 2, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5c8c8d573e01e4f40b5a7c451515e1d2', '32feeb502544416c9bf41329c10a88f4', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 3, 'admin', '2019-08-23 20:03:40', '2019-07-02 18:23:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5dfbea516ee2390d712eace5405c5219', '402860816bff91c0016bff91ca7e0002', 'create_by', '创建人', 'create_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 9, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5e4484b7348dc3e59a0c58bdc3828cc0', '27fc5f91274344afa7673a732b279939', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 4, 'admin', '2019-07-01 16:28:20', '2019-07-01 16:26:42', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5e4ac29ac2007ceabf93368330290a42', '402860816bff91c0016bff91d8830007', 'order_no', '序号', 'order_no', 0, 1, 'int', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 9, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('61c7a0058c264dd746eb35e6f50fc15b', '402860816aa5921f016aa5dedcb90009', 'update_time', '更新日期', 'update_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 0, 0, 'group', '', '', 5, 'admin', '2019-05-11 15:56:47', '2019-05-11 15:50:08', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('6232ade7e2a0c1e97e2c0945b32e61b6', '402860816bff91c0016bffa220a9000b', 'paying_social_insurance', '是否上社保', 'paying_social_insurance', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 32, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('6490a98dccb6df218feaeb4ce11bc03b', '402860816aa5921f016aa5921f480000', 'update_time', '修改时间', 'update_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 0, 0, 'group', '', '', 10, 'admin', '2019-05-11 15:31:54', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('654362725195829005036b3db47ec826', '402860816bff91c0016bffa220a9000b', 'post', '职务', 'post', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 4, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('66a7ef842bc34e105a90186e48167ef2', 'dbf4675875e14676a3f9a8b2b8941140', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 3, NULL, NULL, '2019-05-27 18:02:07', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('68769fa7e4696e3a28f4cecf63076b7b', '402860816bff91c0016bff91ca7e0002', 'order_no', '序号', 'order_no', 0, 1, 'int', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 8, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('69666f21896136af16a6303aff440156', '402860816bff91c0016bffa220a9000b', 'nation', '民族', 'nation', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 11, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('69d11490788fecfc9fb7d74bf449ba86', '32f75e4043ef4070919dbd4337186a3d', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 2, 'admin', '2019-04-11 10:15:32', '2019-03-27 15:54:49', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('6a30c2e6f01ddd24349da55a37025cc0', 'd35109c3632c4952a19ecc094943dd71', 'top_pic', '头像', NULL, 0, 1, 'String', 500, 0, '', '', '', '', 'image', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 11, 'admin', '2019-08-23 23:45:15', '2019-06-10 20:06:56', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('6b6f3aa00b8e73fb785154e795189739', '402860816aa5921f016aa5dedcb90009', 'start_time', '会签发起时间', 'start_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 7, 'admin', '2019-05-11 15:56:47', '2019-05-11 15:50:08', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('6cfb5acbbb69782bf0c7043b53f595b2', '402860816bff91c0016bff91cda80003', 'update_by', '更新人', 'update_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 11, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('6f73e96a659c200c083006b6fce1f043', '402860816bff91c0016bff91ca7e0002', 'cert_name', '证书名称', 'cert_name', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 4, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7154c75d754a5f88bef2b68829baf576', '4028318169e81b970169e81b97650000', 'operate_type', '操作类型', 'operate_type', 0, 1, 'string', 10, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 'group', NULL, NULL, 4, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('71afb00a1971125ecfa13b4dfa49665e', '402860816bff91c0016bff91cfea0004', 'order_no', '序号', 'order_no', 0, 1, 'int', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 8, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('71d5b0675df5aba71688c9d7d75cccee', '4028318169e81b970169e81b97650000', 'log_type', '日志类型（1登录日志，2操作日志）', 'log_type', 0, 1, 'string', 10, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 'group', NULL, NULL, 2, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('71e9ab74dae687837365e50eed090591', '1acb6f81a1d9439da6cc4e868617b565', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 1, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7280c56a210e6a47794fda855d0c6abb', 'fbc35f067da94a70adb622ddba259352', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 4, NULL, NULL, '2019-07-03 19:44:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7365f05f551092716223d5d449efd8c7', 'beee191324fd40c1afec4fda18bd9d47', 'name', 'ss', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 6, 'admin', '2019-04-13 13:41:13', '2019-04-13 13:40:56', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('742329ccdb185cf5d3e0b5b0c05dcffa', '402860816bff91c0016bffa220a9000b', 'interest', '兴趣爱好', 'interest', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 34, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('744444a7ada3bbb05c6b114b5ba0d477', '402860816aa5921f016aa5dedcb90009', 'id', 'id', 'id', 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'group', '', '', 1, 'admin', '2019-05-11 15:56:47', '2019-05-11 15:50:08', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('74af99545de724a4abd2022581a36026', 'fb7125a344a649b990c12949945cb6c1', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 0, 0, 'single', '', '', 2, 'admin', '2019-03-26 19:24:11', '2019-03-26 19:01:52', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('756b07656386dbd91245f7ffda32ae61', '402860816bff91c0016bff91d8830007', 'id', 'id', 'id', 1, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'group', '', '', 1, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('75841fa7c75ebdc94655bd5e44fbc9f6', '402860816bff91c0016bffa220a9000b', 'native_place', '籍贯', 'native_place', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 10, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('75ba781c67711bed71bba1c3e3c68e11', '8994f2817b5a45d9890aa04497a317c5', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', 0, NULL, NULL, '2019-03-23 11:39:16', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7639c1bc4327f1f674ffeab2ca261134', '32f75e4043ef4070919dbd4337186a3d', 'update_by', '更新人登录名称', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 3, 'admin', '2019-04-11 10:15:32', '2019-03-27 15:54:49', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('78a40344207c791b8d7ac7de721ce1c4', '79091e8277c744158530321513119c68', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 3, 'admin', '2019-05-11 15:29:47', '2019-05-11 15:27:17', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('78eb7e3b77cd49f9acb9b024cfe834e1', '402860816aa5921f016aa5dedcb90009', 'create_time', '创建日期', 'create_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 0, 0, 'group', '', '', 3, 'admin', '2019-05-11 15:56:47', '2019-05-11 15:50:08', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('78fd804d93dc716fd8c2ccc45f788565', 'fb7125a344a649b990c12949945cb6c1', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 0, 0, 'single', '', '', 1, 'admin', '2019-03-26 19:24:11', '2019-03-26 19:01:52', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('790c9f3dba773ed9a6ea3ad627393f57', '402860816bff91c0016bffa220a9000b', 'archives_location', '档案所在地', 'archives_location', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 36, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7a665ed90ef64b4d65632c941e5795b2', '4b556f0168f64976a3d20bfb932bc798', 'sex', '性别', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 7, NULL, NULL, '2019-04-12 23:38:29', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7b642d983ac06bfef91edde2c932dbe7', '1acb6f81a1d9439da6cc4e868617b565', 'xg_shangxian', '选购上限', NULL, 0, 1, 'int', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 14, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7b849e57debfb889caea5e0fef09062b', 'beee191324fd40c1afec4fda18bd9d47', 'sex2', 'dd', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 7, 'admin', '2019-04-13 13:41:13', '2019-04-13 13:40:56', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7beb639aa9ffda07edb5ce1e49c2287f', '402860816bff91c0016bff91d2810005', 'update_time', '更新时间', 'update_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 13, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7c6aecc377816c69147f1193b17dfcc5', 'e9faf717024b4aae95cff224ae9b6d97', 'sex', '员工性别', NULL, 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 8, 'admin', '2019-07-03 18:23:49', '2019-07-03 18:22:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7cadf4e0be0b173bb5b8d39613e25190', '402860816bff91c0016bffa220a9000b', 'residence_postcode', '户籍邮编', 'residence_postcode', 0, 1, 'string', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 29, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7d107728408c21ece332406a21f2d692', '402860816bff91c0016bff91cfea0004', 'update_by', '更新人', 'update_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 11, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7e066f60680158d47b328ef519d80e49', 'beee191324fd40c1afec4fda18bd9d47', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 3, 'admin', '2019-04-13 13:41:13', '2019-04-13 13:40:56', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7f10901c6ade3aa9d9ff46ed7039c70f', '1acb6f81a1d9439da6cc4e868617b565', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 3, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('81ed9556c9fda1bbb46d94a53a6c90c7', '402860816bff91c0016bff91c0cb0000', 'depart_name', '部门名称', 'depart', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 7, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8422485e1cbf4455f9ded7d0af59379c', '402860816bff91c0016bff91cfea0004', 'to_time', '截止时间', 'to_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 4, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('845c70568d44a074f067d6d277950525', '402860816bff91c0016bffa220a9000b', 'entrytime', '入职时间', 'entrytime', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 23, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8529ddaed8d5f3d9084e873203d55cac', '402860816bff91c0016bffa220a9000b', 'marital_status', '婚姻状况', 'marital_status', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 24, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('857a0daa9cd8a058f2f15fc7c5fb3571', '402860816bff91c0016bffa220a9000b', 'email', '邮箱', 'email', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 17, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8652ca69a947fd4c961a3ac7c0fa252a', 'fb19fb067cd841f9ae93d4eb3b883dc0', 'create_by', '创建人登录名称', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', 1, NULL, NULL, '2019-03-23 11:39:48', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('86bbafef5683674a736cf7241c458d44', '27fc5f91274344afa7673a732b279939', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 2, 'admin', '2019-07-01 16:28:20', '2019-07-01 16:26:42', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('86e0f3a8f31c60698157f139ed993954', '402860816bff91c0016bffa220a9000b', 'having_reserve_funds', '是否有公积金', 'having_reserve_funds', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 33, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('873e2bb041b17bff77d3aca72900ea1b', '56870166aba54ebfacb20ba6c770bd73', 'order_code', '订单编码', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 6, 'admin', '2019-06-10 16:01:35', '2019-04-20 11:38:39', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('877391ae770a4ce2c95181ef410982ce', '4028318169e81b970169e81b97650000', 'request_param', '请求参数', 'request_param', 0, 1, 'string', 255, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 'group', NULL, NULL, 10, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('87e82e3c723a6abb020122babdac6bd1', '8994f2817b5a45d9890aa04497a317c5', 'create_by', '创建人登录名称', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', 1, NULL, NULL, '2019-03-23 11:39:16', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('87f7a2703c5850f0b063dd866d0e2917', '402860816bff91c0016bffa220a9000b', 'birthday', '出生日期', 'birthday', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 7, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('87fafe1a4a8a626e3875697574c19f15', '402860816bff91c0016bff91d2810005', 'sys_org_code', '组织机构编码', 'sys_org_code', 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 14, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('88a12570e14c9f6f442e731ae5ad0eb1', 'beee191324fd40c1afec4fda18bd9d47', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 4, 'admin', '2019-04-13 13:41:13', '2019-04-13 13:40:56', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('89ab9eedbac6141e7a0df6d37a3655d0', 'e67d26b610dd414c884c4dbb24e71ce3', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 4, 'admin', '2019-04-24 11:03:32', '2019-04-24 11:02:57', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8a24fb45e2af120c253c8b61c0085f7a', '402860816bff91c0016bff91cda80003', 'sys_org_code', '组织机构编码', 'sys_org_code', 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 13, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8ac8a0c0087469a4e7579229ff17f273', 'e5464aa8fa7b47c580e91593cf9b46dc', 'jifen', '合计积分', NULL, 0, 1, 'int', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 10, 'admin', '2019-04-24 17:09:49', '2019-04-24 11:05:10', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8bd4deadc9e96c1a6d7abd77033105f6', 'e67d26b610dd414c884c4dbb24e71ce3', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 5, 'admin', '2019-04-24 11:03:32', '2019-04-24 11:02:57', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8ca56210938fbe649f840e505eb9fd41', '56870166aba54ebfacb20ba6c770bd73', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 2, 'admin', '2019-06-10 16:01:35', '2019-04-20 11:38:39', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8ea43fd1e4ce82becee61b2f1e2e843f', '32feeb502544416c9bf41329c10a88f4', 'sex', '性别', NULL, 0, 1, 'String', 32, 0, '', 'sex', '', '', 'list', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 8, 'admin', '2019-08-23 20:03:40', '2019-07-02 18:23:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8f1d302868640b72cef52171a023a203', 'e9faf717024b4aae95cff224ae9b6d97', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 4, 'admin', '2019-07-03 18:23:49', '2019-07-03 18:22:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8f3e6fb68179c690f748f3c541fb50f1', '7ea60a25fa27470e9080d6a921aabbd1', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 3, NULL, NULL, '2019-04-17 00:22:21', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8fc0be84bed1216635c69af918e097ff', '402860816aa5921f016aa5dedcb90009', 'name', '并行会签标题', 'name', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 6, 'admin', '2019-05-11 15:56:47', '2019-05-11 15:50:08', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('90a822b8a63bbbc1e9575c9f4e21e021', 'd35109c3632c4952a19ecc094943dd71', 'descc', '描述', NULL, 0, 1, 'string', 500, 0, '', '', '', '', 'textarea', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', 8, 'admin', '2019-08-23 23:45:15', '2019-03-15 14:24:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('90f39a6e29dae2e1fbb59d7d605f7c09', '1acb6f81a1d9439da6cc4e868617b565', 'iz_valid', '启用状态', NULL, 0, 1, 'String', 2, 0, '', 'air_china_valid', '', '', 'list', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 11, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9370c9304af30b8d29defe0a5ada6e5b', '62e29cdb81ac44d1a2d8ff89851b853d', 'DC_DDSA', 'DD', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 6, NULL, NULL, '2019-05-11 14:01:14', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9371f61d39c5d57ddb0a2db96b2e2412', '402860816bff91c0016bffa220a9000b', 'speciality', '专业', 'speciality', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 15, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('947174892512ea97fafde899d427ea7e', '402860816bff91c0016bff91c0cb0000', 'real_name', '姓名', 'real_name', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 4, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('94b8bf435175cc545366e11992280757', '32f75e4043ef4070919dbd4337186a3d', 'age', '年龄', NULL, 0, 1, 'int', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 1, 1, 1, 'group', '', '', 7, 'admin', '2019-04-11 10:15:32', '2019-03-27 15:54:49', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('94e682cb802777fe4205536888f69353', '402860816bff91c0016bff91d2810005', 'create_by', '创建人', 'create_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 10, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('957386b500be42a200d6a56d54345392', 'deea5a8ec619460c9245ba85dbc59e80', 'num', '数量', NULL, 0, 1, 'int', 32, 0, '', '', '', '', 'text', '', 120, 'n', '0', '', '', 0, 1, 1, 'single', '', '', 8, 'admin', '2019-06-10 16:07:16', '2019-04-20 11:41:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('960d2847922b61dadeb3518ef55fb0c1', '1acb6f81a1d9439da6cc4e868617b565', 'wl_name', '物料名称', NULL, 0, 1, 'String', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 7, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9665f02764774fdd77c19923d3ff3c3e', '4028318169e81b970169e81b97650000', 'cost_time', '耗时', 'cost_time', 0, 1, 'string', 19, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 'group', NULL, NULL, 12, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('96c585a4f71e5c38ed25b9741366365b', '402860816bff91c0016bff91c7010001', 'sys_org_code', '组织机构编码', 'sys_org_code', 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 11, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9765efa2cafde6d0ede2215848c9e80b', '32f75e4043ef4070919dbd4337186a3d', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 0, 'admin', '2019-04-11 10:15:32', '2019-03-27 15:54:49', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('98e82cb1595609a3b42fa75c60ac1229', '402860816bff91c0016bff91d2810005', 'update_by', '更新人', 'update_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 12, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9914a0c84805e72c4b6075e36edb13f9', '402860816aa5921f016aa5921f480000', 'create_time', '创建时间', 'create_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 0, 0, 'group', '', '', 9, 'admin', '2019-05-11 15:31:54', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9920ecec9c9109fc6b93e86f8fdfa03b', '402860816bff91c0016bffa220a9000b', 'depart_name', '所在部门', 'depart_name', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 1, 1, 1, 'group', '', '', 2, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('99b43bbb23237815ebb74b12b4d7ea2f', '62e29cdb81ac44d1a2d8ff89851b853d', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 1, NULL, NULL, '2019-05-11 14:01:14', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9a579c506f75f75baf88352a5eb2c249', '1acb6f81a1d9439da6cc4e868617b565', 'bpm_status', '流程状态', NULL, 0, 1, 'String', 2, 0, '1', 'bpm_status', '', '', 'list', '', 120, NULL, '0', '', '', 0, 0, 1, 'single', '', '', 16, 'admin', '2019-06-10 14:47:14', '2019-05-07 16:54:43', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9c40fb4db8afed3c682c6b8a732fd69d', 'e2faf977fdaf4b25a524f58c2441a51c', 'post', '用户岗位', NULL, 0, 1, 'String', 32, 0, '', 'post_code', 'air_china_post_materiel_main', 'post_name', 'sel_search', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 8, 'admin', '2019-06-10 17:27:00', '2019-04-24 17:12:11', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9d85bafa399f28a40e1de1eeef747223', '4028318169e81b970169e81b97650000', 'ip', 'IP', 'ip', 0, 1, 'string', 100, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 'group', NULL, NULL, 7, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9d89ff1a019f41d80307652041490944', '32feeb502544416c9bf41329c10a88f4', 'name', '请假人', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 7, 'admin', '2019-08-23 20:03:40', '2019-07-02 18:23:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9e50680eb4e79b3af352a5933d239dff', 'dbf4675875e14676a3f9a8b2b8941140', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 1, NULL, NULL, '2019-05-27 18:02:07', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a01a7fe5660206e6f407ed98b6c732d6', '402860816bff91c0016bff91cfea0004', 'phone', '联系方式', 'phone', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 7, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a1f5daba36f536e7acf6a939826183b0', 'fb19fb067cd841f9ae93d4eb3b883dc0', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', 0, NULL, NULL, '2019-03-23 11:39:48', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a45eba33810c485b9d8e6f70818a1dfa', '402860816aa5921f016aa5921f480000', 'bpm_status', '流程状态', 'bpm_status', 0, 1, 'string', 50, 0, '1', 'bpm_status', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 1, 'group', '', '', 7, 'admin', '2019-05-11 15:31:54', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a6471d4fb3dbffef01dab1f7d452bb30', '27fc5f91274344afa7673a732b279939', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 5, 'admin', '2019-07-01 16:28:20', '2019-07-01 16:26:42', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a6722b498602d7d7b5177b16789d8cc1', 'e5464aa8fa7b47c580e91593cf9b46dc', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 2, 'admin', '2019-04-24 17:09:48', '2019-04-24 11:05:10', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a76f561057ac9e43a8ca09e478a1eab8', '402860816bff91c0016bff91ca7e0002', 'update_time', '更新时间', 'update_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 12, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a7822f6e4cffb37fc0729cbd4cfd8655', '32f75e4043ef4070919dbd4337186a3d', 'name', '用户名', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 1, 1, 1, 'single', '', '', 5, 'admin', '2019-04-11 10:15:32', '2019-03-27 15:54:49', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a82ca42a76e9d2b8dae6d57dbb5edb54', 'deea5a8ec619460c9245ba85dbc59e80', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 4, 'admin', '2019-06-10 16:07:16', '2019-04-20 11:41:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a940adc4585fa3b5bd2114ea9abe8491', '402860816bff91c0016bff91ca7e0002', 'cert_level', '证书级别', 'cert_level', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 5, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a94f1d7da64f3aa35c32155ea00ccb2f', '402860816bff91c0016bffa220a9000b', 'id', 'id', 'id', 1, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'group', '', '', 1, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('aa07931514727913413880b7a2b76dcb', 'd3ae1c692b9640e0a091f8c46e17bb01', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 3, NULL, NULL, '2019-07-24 14:47:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('aa4780601419c21dabb6c42fc511e71c', '402860816bff91c0016bffa220a9000b', 'have_children', '有无子女', 'have_children', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 25, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ab10e0aa029ded2f4420a33420de225d', '1acb6f81a1d9439da6cc4e868617b565', 'wl_code', '物料编码', NULL, 0, 1, 'String', 60, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 6, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ab1f880ba593f3757dac70e003945aa2', '402860816bff91c0016bff91c0cb0000', 'depart_id', '部门ID', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 6, 'admin', '2019-07-19 18:09:01', '2019-07-17 19:38:45', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ab58f43f853fd1f65f83c22966883afb', 'beee191324fd40c1afec4fda18bd9d47', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 2, 'admin', '2019-04-13 13:41:13', '2019-04-13 13:40:56', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ab8e6f1cca421c5ce395a2c1fdfd2100', '32feeb502544416c9bf41329c10a88f4', 'sys_org_code', '所属部门', NULL, 0, 1, 'string', 64, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 6, 'admin', '2019-08-23 20:03:40', '2019-07-02 18:23:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('abe61a8ddf966a979457b763329a537b', 'e5464aa8fa7b47c580e91593cf9b46dc', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 3, 'admin', '2019-04-24 17:09:49', '2019-04-24 11:05:10', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ac91565da5fb8fe43a4da3dec660b25f', '402860816bff91c0016bff91c7010001', 'award_place', '获奖地点', 'award_place', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 4, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('acff5c8aef3b6288b87fd91215012206', 'e5464aa8fa7b47c580e91593cf9b46dc', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 4, 'admin', '2019-04-24 17:09:49', '2019-04-24 11:05:10', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ad061417d5b53c67975eb83657505218', '73162c3b8161413e8ecdca7eb288d0c9', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 4, 'admin', '2019-07-01 14:23:32', '2019-06-10 15:18:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ad93762c6c4a1dd8331e5fa11215b568', 'e2faf977fdaf4b25a524f58c2441a51c', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 1, 'admin', '2019-06-10 17:27:00', '2019-04-24 17:12:11', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ae31da96f38fc2941cb93d1bb1ab9431', 'deea5a8ec619460c9245ba85dbc59e80', 'product_name', '产品名字', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 6, 'admin', '2019-06-10 16:07:16', '2019-04-20 11:41:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ae77bb317366622698c8ab9bf2325833', 'deea5a8ec619460c9245ba85dbc59e80', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 2, 'admin', '2019-06-10 16:07:16', '2019-04-20 11:41:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('af0fe0df8b626129de62e22212732517', '402860816bff91c0016bff91cda80003', 'speciality', '专业', 'speciality', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 6, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('af6c582b902e2f2bf9930eba61ae7938', '73162c3b8161413e8ecdca7eb288d0c9', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 5, 'admin', '2019-07-01 14:23:32', '2019-06-10 15:18:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('afd3ef1d494a9b69d2c7a3cdde937f6f', '402860816bff91c0016bffa220a9000b', 'create_by', '创建人', 'create_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 39, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b01304904babd7479de2acfe8a77157f', '402860816aa5921f016aa5921f480000', 'id', 'ID', 'id', 1, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'group', '', '', 1, 'admin', '2019-05-11 15:31:54', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b05b4cbb74f389a7376f51ed9fd97030', '402860816bff91c0016bff91d8830007', 'create_by', '创建人', 'create_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 10, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b0a06bdbefd304d81a1838d8d94deda9', '4b556f0168f64976a3d20bfb932bc798', 'name', '用户名', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 6, NULL, NULL, '2019-04-12 23:38:28', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b0b1cf271dd6b221a902da2d2f8f889a', 'e9faf717024b4aae95cff224ae9b6d97', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 5, 'admin', '2019-07-03 18:23:49', '2019-07-03 18:22:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b1fc6e2ca671b19e57b08a4f57fc2454', 'fb7125a344a649b990c12949945cb6c1', 'update_time', '更新日期', NULL, 0, 1, 'date', 20, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 0, 0, 'single', '', '', 4, 'admin', '2019-03-26 19:24:11', '2019-03-26 19:01:52', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b22694cf34ffb967b8717647816ad5df', 'e5464aa8fa7b47c580e91593cf9b46dc', 'fk_id', '外键', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', 'air_china_post_materiel_main', 'id', 15, 'admin', '2019-04-24 17:09:49', '2019-04-24 11:05:10', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b276facab025f9750b0aff391693cc4b', '402860816bff91c0016bff91c7010001', 'id', 'id', 'id', 1, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'group', '', '', 1, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b3542d3e7908ed885ecc4ba9e7300705', '4b556f0168f64976a3d20bfb932bc798', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 2, NULL, NULL, '2019-04-12 23:38:28', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b47af4d937e55c6208939bac5378bfad', '62e29cdb81ac44d1a2d8ff89851b853d', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 5, NULL, NULL, '2019-05-11 14:01:14', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b53203fc52d067bb4730dbcb7e496bd3', '56870166aba54ebfacb20ba6c770bd73', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 1, 'admin', '2019-06-10 16:01:35', '2019-04-20 11:38:39', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b5cfd3c9691a884430f3d9cd5ecb211f', 'e2faf977fdaf4b25a524f58c2441a51c', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 2, 'admin', '2019-06-10 17:27:00', '2019-04-24 17:12:11', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b6874a05734cad8bd96ffd2f31f1ebca', '402860816bff91c0016bff91c7010001', 'create_by', '创建人', 'create_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 7, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b733fa73519603b22d401fabbf9e9781', '402860816bff91c0016bff91c0cb0000', 'hiredate', '入职时间', 'hiredate', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 5, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b7938e4518f9062ce62702cf45986e06', 'e2faf977fdaf4b25a524f58c2441a51c', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 3, 'admin', '2019-06-10 17:27:00', '2019-04-24 17:12:11', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b7a1243aaa712e2c152c0c7a46f88683', '402860816bff91c0016bff91d8830007', 'age', '年龄', 'age', 0, 1, 'int', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 5, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b91258e3dc15b28c2e3f0d934e6e27e8', 'fb7125a344a649b990c12949945cb6c1', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', 0, NULL, NULL, '2019-03-26 19:01:52', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b92572ae142f8dd5f2ac02fb45e6b2c1', 'e2faf977fdaf4b25a524f58c2441a51c', 'name', '用户名', NULL, 0, 1, 'String', 32, 0, '', 'realname,username', 'report_user', 'name,account', 'sel_search', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 6, 'admin', '2019-06-10 17:27:00', '2019-04-24 17:12:11', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b9fbace24688c9c9a8c9be72c1d014e7', '402860816bff91c0016bffa220a9000b', 'phone', '电话', 'phone', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 18, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ba5f4b2affa94f36eda7f6f133db7ae3', '402860816bff91c0016bff91d2810005', 'workplace', '工作单位', 'workplace', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 6, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bad02e68ea37bf387337516af84a1ddb', '73162c3b8161413e8ecdca7eb288d0c9', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 1, 'admin', '2019-07-01 14:23:32', '2019-06-10 15:18:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bb44475e1d1738a19745bf9f3ebf9e40', '402860816bff91c0016bff91cfea0004', 'update_time', '更新时间', 'update_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 12, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bbbb38347b1a5340a1d293e455c632ce', 'fb19fb067cd841f9ae93d4eb3b883dc0', '3', '4', NULL, 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', 6, NULL, NULL, '2019-03-23 11:39:48', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bc648624ad14c826bbc6e9b23a2b9858', '402860816bff91c0016bff91ca7e0002', 'employee_id', '员工ID', 'employee_id', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', 'oa_employee_info', 'id', 2, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bc793fdbef9f6474425456c4eb9d197a', '402860816bff91c0016bff91cfea0004', 'witness', '证明人', 'references', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 6, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bc7df6f3cf49f670c36a3de25e25e715', '402860816bff91c0016bff91d2810005', 'order_no', '序号', 'order_no', 0, 1, 'int', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 9, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bd39cb237049ac60218b3f4dd844f30c', '402860816bff91c0016bffa220a9000b', 'current_address', '现居住地', 'current_address', 0, 1, 'string', 200, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 30, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bde81809057b1a4c974fa0f090501fdd', '402860816aa5921f016aa5dedcb90009', 'update_by', '更新人', 'update_by', 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'group', '', '', 4, 'admin', '2019-05-11 15:56:47', '2019-05-11 15:50:08', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('be3f8c157d8a1b40e6f7b836552a8095', '8994f2817b5a45d9890aa04497a317c5', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', 2, NULL, NULL, '2019-03-23 11:39:16', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('be5eeda7d51dace73d3818bd8467b53b', '402860816bff91c0016bff91c0cb0000', 'update_time', '更新时间', 'update_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 15, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('be868eed386da3cfcf49ea9afcdadf11', 'd35109c3632c4952a19ecc094943dd71', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 0, 0, 'single', '', '', 2, 'admin', '2019-08-23 23:45:15', '2019-03-15 14:24:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bec3082fc5f0f194be5cd72cc2866ff4', 'e5464aa8fa7b47c580e91593cf9b46dc', 'wl_name', '物料名字', NULL, 0, 1, 'String', 200, 0, '', 'wl_code', 'air_china_materiel', 'wl_name', 'list', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 6, 'admin', '2019-04-24 17:09:49', '2019-04-24 11:05:10', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bed0bc67f570613eaa6a1bd8bcaaddcc', '4b556f0168f64976a3d20bfb932bc798', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 3, NULL, NULL, '2019-04-12 23:38:28', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bf61aa04c5ca77ad54c764f8f8b2bdec', '402860816bff91c0016bff91d8830007', 'update_by', '更新人', 'update_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 12, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bfc2f19fae367f885adb8bd82a344391', '4028318169e81b970169e81b97650000', 'userid', '操作用户账号', 'userid', 0, 1, 'string', 32, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 'group', NULL, NULL, 5, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('c0d66c95773774e7ac1f2a88df307e7a', '402860816aa5921f016aa5921f480000', 'reason', '请假原因', 'reason', 0, 1, 'string', 500, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 6, 'admin', '2019-05-11 15:31:54', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('c29216d975fee50af175bca8c664a475', 'e67d26b610dd414c884c4dbb24e71ce3', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 1, 'admin', '2019-04-24 11:03:32', '2019-04-24 11:02:56', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('c43d87b6340c29c0c354aa9c579f387f', '32feeb502544416c9bf41329c10a88f4', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 2, 'admin', '2019-08-23 20:03:40', '2019-07-02 18:23:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('c5dd2fc34ae825ebfced2ec74948654c', '402860816aa5921f016aa5921f480000', 'end_date', '请假结束时间', 'end_date', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 5, 'admin', '2019-05-11 15:31:54', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('c5f6ea01a6523a60df153cc61dc92f4d', 'fbc35f067da94a70adb622ddba259352', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 2, NULL, NULL, '2019-07-03 19:44:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('c6730e00df5efd77fedf181df29102de', '402860816bff91c0016bff91c7010001', 'update_by', '更新人', 'update_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 9, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('c75a7cb0a21958aa7ca5442f66019669', 'e9faf717024b4aae95cff224ae9b6d97', 'depart', '所属部门', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 9, 'admin', '2019-07-03 18:23:49', '2019-07-03 18:22:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('c772ed9cbe2d1dc69e9ffa73d3487021', '4b556f0168f64976a3d20bfb932bc798', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 1, NULL, NULL, '2019-04-12 23:38:28', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('c91b697b1bcd2be943fc746e2660bc9e', '402860816bff91c0016bff91d2810005', 'real_name', '姓名', 'real_name', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 3, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('c9b698d3f75aa780ee1eb67ef090b15b', '73162c3b8161413e8ecdca7eb288d0c9', 'wl_code', '物料编码', NULL, 0, 1, 'String', 200, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 8, 'admin', '2019-07-01 14:23:32', '2019-06-10 15:18:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('caf5a071f2299c0f9ff2f3038d6d0fc6', '402860816bff91c0016bff91ca7e0002', 'update_by', '更新人', 'update_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 11, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('cb33a708b95e19085f8c9001d2d5c64c', 'e9faf717024b4aae95cff224ae9b6d97', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 2, 'admin', '2019-07-03 18:23:49', '2019-07-03 18:22:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('cb7da49a981a1b0acc5f7e8a0130bdcd', 'd35109c3632c4952a19ecc094943dd71', 'user_code', '用户编码', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 0, 'single', '', '', 10, 'admin', '2019-08-23 23:45:15', '2019-05-11 16:26:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('cb871284e845e26e88242a96fac9c576', '402860816bff91c0016bff91c7010001', 'order_no', '序号', 'order_no', 0, 1, 'int', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 6, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('cc1ddc1304d3eb5d9a189da0a509ccd0', '32f75e4043ef4070919dbd4337186a3d', 'create_by', '创建人登录名称', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 1, 'admin', '2019-04-11 10:15:32', '2019-03-27 15:54:49', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('cee3c1dbf67b4a7d9626b8032897a4c7', '402860816bff91c0016bff91d8830007', 'employee_id', '员工ID', 'employee_id', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', 'oa_employee_info', 'id', 2, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('cf4c5a4c06ae6bac701edfeedfcd16aa', 'd3ae1c692b9640e0a091f8c46e17bb01', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 1, NULL, NULL, '2019-07-24 14:47:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('cfeb6491427aec2b4db9694af867da23', 'e9faf717024b4aae95cff224ae9b6d97', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 3, 'admin', '2019-07-03 18:23:49', '2019-07-03 18:22:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d2551b70dc96a45a73b304bf755a996f', '402860816bff91c0016bff91d8830007', 'workplace', '工作单位', 'workplace', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 6, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d29dcdba14ea61808391fff2d927efea', '402860816bff91c0016bff91c0cb0000', 'work_summary', '工作总结', 'work_summary', 0, 1, 'Text', 65535, 0, '', '', '', '', 'textarea', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 10, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d3a701472d27ca8435d6a781a597038d', 'deea5a8ec619460c9245ba85dbc59e80', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 3, 'admin', '2019-06-10 16:07:16', '2019-04-20 11:41:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d48bfd2a840f9b1d00bd3b5599dca0f0', '402860816bff91c0016bff91cda80003', 'post', '职务', 'post', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 7, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d4d8cae3cd9ea93e378fc14303eee105', 'd35109c3632c4952a19ecc094943dd71', 'create_by', '创建人登录名称', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 0, 0, 'single', '', '', 1, 'admin', '2019-08-23 23:45:15', '2019-03-15 14:24:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d4dea775487aef5a7aea41791d3a65db', 'e5464aa8fa7b47c580e91593cf9b46dc', 'cycle_time', '发放周期(年)', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 11, 'admin', '2019-04-24 17:09:49', '2019-04-24 14:09:06', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d52c79620e21128fb69b4e8628cf25cc', 'dbf4675875e14676a3f9a8b2b8941140', 'sys_org_code', '所属部门', NULL, 0, 1, 'string', 64, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 6, NULL, NULL, '2019-05-27 18:02:07', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d530ab1bc3c51e8249a506a25d1003c7', '79091e8277c744158530321513119c68', 'start_time', '会签发起时间', NULL, 0, 1, 'Date', 32, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 7, 'admin', '2019-05-11 15:29:47', '2019-05-11 15:27:17', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d53e70411c206efecb8dcd00174e907c', '62e29cdb81ac44d1a2d8ff89851b853d', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 3, NULL, NULL, '2019-05-11 14:01:14', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d5df0a35352ee960053686e959e9084b', '1acb6f81a1d9439da6cc4e868617b565', 'wl_unit', '计量单位', NULL, 0, 1, 'String', 100, 0, '', 'air_china_unit', '', '', 'list_multi', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 8, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d6fad89f4f26d733291863c2dfbc5945', '27fc5f91274344afa7673a732b279939', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 1, 'admin', '2019-07-01 16:28:20', '2019-07-01 16:26:42', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d766ea5809e2ec9ff2cdbcb18f610ab3', '7ea60a25fa27470e9080d6a921aabbd1', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 1, NULL, NULL, '2019-04-17 00:22:21', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d7c3b107f004cbc99dfe1fe6c79894d8', '402860816bff91c0016bffa220a9000b', 'social_insurance_type', '参加社保类型', 'social_insurance_type', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 35, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d8234b56acea1a752271a6c911dd91a0', '7ea60a25fa27470e9080d6a921aabbd1', 'age', '年龄', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 7, NULL, NULL, '2019-04-17 00:22:21', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d86909d794b01ad7fbb5e61d28b6603b', '73162c3b8161413e8ecdca7eb288d0c9', 'sys_org_code', '所属部门', NULL, 0, 1, 'string', 64, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 6, 'admin', '2019-07-01 14:23:32', '2019-06-10 15:18:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d949d9157831c2fb7ba9f175081fe036', '402860816bff91c0016bff91cda80003', 'school', '学校', 'school', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 5, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d9d308feb95968dbb322c9bff0c18452', '32feeb502544416c9bf41329c10a88f4', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 4, 'admin', '2019-08-23 20:03:40', '2019-07-02 18:23:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d9dde2f59bb148c6b7e95256acad8972', 'e67d26b610dd414c884c4dbb24e71ce3', 'post_name', '岗位名字', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 7, 'admin', '2019-04-24 11:03:32', '2019-04-24 11:02:57', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d9f9ae6986cb85019a3a4030f62f4d1a', '402860816bff91c0016bff91cfea0004', 'employee_id', '员工ID', 'employee_id', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', 'oa_employee_info', 'id', 2, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('db1fb3e12385cb967b63420cfe97cde6', '402860816bff91c0016bff91cda80003', 'employee_id', '员工ID', 'employee_id', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', 'oa_employee_info', 'id', 2, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('dd3e1e13d7429807b83a00a090e060b7', '402860816bff91c0016bffa220a9000b', 'join_party_info', '入党（团）时间地点', 'join_party_info', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 26, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ddc302f84c75a5f056855c664b82202a', '402860816aa5921f016aa5921f480000', 'days', '请假天数', 'days', 0, 1, 'int', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 3, 'admin', '2019-05-11 15:31:54', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ddcc14a2105588982b4ae657f2893d81', '32feeb502544416c9bf41329c10a88f4', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 1, 'admin', '2019-08-23 20:03:40', '2019-07-02 18:23:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('dedb920a5e876e27eb144464209ebe1e', '27fc5f91274344afa7673a732b279939', 'sys_org_code', '所属部门', NULL, 0, 1, 'string', 64, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 6, 'admin', '2019-07-01 16:28:20', '2019-07-01 16:26:42', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('df489194c6008f3bd21b2c1c11fde337', 'fb19fb067cd841f9ae93d4eb3b883dc0', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', 2, NULL, NULL, '2019-03-23 11:39:48', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e0c5d6e483897d5c4e7894dc66dd1aff', '32feeb502544416c9bf41329c10a88f4', 'bpm_status', '流程状态', NULL, 0, 1, 'String', 2, 0, '', 'bpm_status', '', '', 'list', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 9, 'admin', '2019-08-23 20:03:40', '2019-07-02 18:23:58', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e24de426223dc0271a55eccc1d5457d0', '73162c3b8161413e8ecdca7eb288d0c9', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 2, 'admin', '2019-07-01 14:23:32', '2019-06-10 15:18:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e2d73ccda7f10f5a1ccce3c48b1e699e', '402860816bff91c0016bffa220a9000b', 'residence_street', '户口所在街道', 'residence_street', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 27, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e4914fdff68ac72486ada105e6e9fa36', 'e9faf717024b4aae95cff224ae9b6d97', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 1, 'admin', '2019-07-03 18:23:49', '2019-07-03 18:22:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e4a4c1d50b7b46678bc14fd5b90ee082', '73162c3b8161413e8ecdca7eb288d0c9', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 3, 'admin', '2019-07-01 14:23:32', '2019-06-10 15:18:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e50b4398731e06572c247993a0dcc38d', 'd35109c3632c4952a19ecc094943dd71', 'name', '用户名', NULL, 0, 1, 'string', 200, 0, '', '', '', '', 'text', '', 120, '*', '0', '', '', 0, 1, 1, 'single', '', '', 5, 'admin', '2019-08-23 23:45:15', '2019-03-15 14:24:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e53f53766d1f7718b3ee5eabe105b969', '402860816bff91c0016bffa220a9000b', 'social_insurance_time', '五险一金日期', 'social_insurance_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 38, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e672d5974a06d5c37b3b4c94a6f29f96', '4028318169e81b970169e81b97650000', 'request_url', '请求路径', 'request_url', 0, 1, 'string', 255, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 'group', NULL, NULL, 9, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e7aade12ca722b59c1ec681d14247ff8', '402860816bff91c0016bff91d8830007', 'sys_org_code', '组织机构编码', 'sys_org_code', 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 14, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e845925368919482df6dac58e6ed708d', '402860816bff91c0016bff91d8830007', 'phone', '联系方式', 'phone', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 8, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e88d328af34dd8a17f51437c52b68a2d', '402860816bff91c0016bff91cfea0004', 'create_by', '创建人', 'create_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 9, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e99cc08f4d88dd8f788399db8d448ee8', '62e29cdb81ac44d1a2d8ff89851b853d', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 2, NULL, NULL, '2019-05-11 14:01:14', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ea644c4c208739640933ba6e568045c1', 'e2faf977fdaf4b25a524f58c2441a51c', 'ruz_date', '入职时间', NULL, 0, 1, 'Date', 32, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 9, 'admin', '2019-06-10 17:27:00', '2019-04-24 17:12:11', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ea96d1c33f0f0d7245045e70a5793257', '402860816bff91c0016bffa220a9000b', 'current_postcode', '现居住地邮编', 'current_postcode', 0, 1, 'string', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 31, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ebc41362681919cc680fcc58bf87fdcb', '1acb6f81a1d9439da6cc4e868617b565', 'price', '单价', NULL, 0, 1, 'double', 10, 2, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 12, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ec20e66d5eb9b8b7f58de9edc0f7630b', '1acb6f81a1d9439da6cc4e868617b565', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 2, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ec5e9cb5809b2f8ce1446df4a27693f0', '27fc5f91274344afa7673a732b279939', 'name', '用户名', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 7, 'admin', '2019-07-01 16:28:20', '2019-07-01 16:26:42', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ed16f23d08e7bcda11a1383fda68057e', '402860816bff91c0016bff91c7010001', 'employee_id', '员工ID', 'employee_id', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', 'oa_employee_info', 'id', 2, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('edda30c64e1dccee510d40b77a8ca094', 'fb7125a344a649b990c12949945cb6c1', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 0, 0, 'single', '', '', 3, 'admin', '2019-03-26 19:24:11', '2019-03-26 19:01:52', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ee09e0e21fa350b9346b70292dcfca00', '79091e8277c744158530321513119c68', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 1, 'admin', '2019-05-11 15:29:47', '2019-05-11 15:27:17', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ee4ffe04a25fcf556e78183f1f521546', '402860816aa5921f016aa5921f480000', 'create_by', '创建人id', 'create_by', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'group', '', '', 8, 'admin', '2019-05-11 15:31:54', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ee5803611f63643059b6375166d71567', '402860816bff91c0016bff91c7010001', 'create_time', '创建时间', 'create_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 8, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ef81373c5fd7130d7e23859d90c9eb3e', '402860816bff91c0016bff91cda80003', 'from_time', '开始日期', 'from_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 3, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('efd1b955a75b5046e9857e00fe94ae2c', 'fbc35f067da94a70adb622ddba259352', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 1, NULL, NULL, '2019-07-03 19:44:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f015cc2ffdcc2c4be1e9b3622eb69b52', 'fbc35f067da94a70adb622ddba259352', 'sys_org_code', '所属部门', NULL, 0, 1, 'string', 64, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 6, NULL, NULL, '2019-07-03 19:44:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f06b2bb01ea1fae487b7e3c3eb521d5b', 'd3ae1c692b9640e0a091f8c46e17bb01', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 2, NULL, NULL, '2019-07-24 14:47:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f0967fd139b440f79f21248bf4e4a209', 'd3ae1c692b9640e0a091f8c46e17bb01', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 5, NULL, NULL, '2019-07-24 14:47:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f0a453930aa16ca32f2e3be860bfe542', '402860816bff91c0016bffa220a9000b', 'education', '学历', 'education', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 14, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f12753b4a3815697a72017a7436fe733', 'e2faf977fdaf4b25a524f58c2441a51c', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 5, 'admin', '2019-06-10 17:27:00', '2019-04-24 17:12:11', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f1ab7d3b55ade57eeac6c55b32ce813a', '1acb6f81a1d9439da6cc4e868617b565', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 4, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f1c7fde21f26c7ed64a0ef1095900c52', '4028318169e81b970169e81b97650000', 'request_type', '请求类型', 'request_type', 0, 1, 'string', 10, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 'group', NULL, NULL, 11, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f4647a91a4ac5d6d32bb0692b800bffe', '402860816bff91c0016bff91c0cb0000', 'probation_post', '试用期职位', 'probation_post', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 8, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f4b0bc7f3d6562e28d7c5e2d56510ecd', 'e5464aa8fa7b47c580e91593cf9b46dc', 'first_num', '首次数量', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 8, 'admin', '2019-04-24 17:09:49', '2019-04-24 14:31:31', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f558359b06aea79a992c102ce3563a4d', '4028318169e81b970169e81b97650000', 'username', '操作用户名称', 'username', 0, 1, 'string', 100, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 'group', NULL, NULL, 6, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f6afcb7d8ea81879593ff737b55ddcc0', '402860816bff91c0016bff91cda80003', 'id', 'id', 'id', 1, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'group', '', '', 1, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f6f8aed87ec73994f6a12abbc079dbb1', '402860816bff91c0016bffa220a9000b', 'update_by', '更新人', 'update_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 41, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f7332af7586c83c87f7b9ea144a5292d', '62e29cdb81ac44d1a2d8ff89851b853d', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 4, NULL, NULL, '2019-05-11 14:01:14', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f76719783433487f4710232e2ae0e521', '402860816bff91c0016bff91cfea0004', 'id', 'id', 'id', 1, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'group', '', '', 1, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f7e7eb84ddc34d7e09d10af213ac6667', '402860816bff91c0016bff91d2810005', 'create_time', '创建时间', 'create_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 11, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f81d7103c0c857e5c744cda2bc4c000a', '402860816bff91c0016bff91ca7e0002', 'cert_organizations', '发证机关', 'cert_organizations', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 6, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f8a0fd20a1173270afdfed1129d5c669', '402860816bff91c0016bffa220a9000b', 'depart_id', '所在部门id', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 44, 'admin', '2019-07-22 16:15:32', '2019-07-19 15:33:44', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f8cc6be747fec10802eb625ac529c16f', '402860816bff91c0016bff91cfea0004', 'create_time', '创建时间', 'create_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 10, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f94db83e41c69f407d3c9a81c5892269', '402860816bff91c0016bffa220a9000b', 'first_job_time', '首次工作时间', 'first_job_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 22, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f95d2cbefd25444909c83aaf8c4f72fb', '402860816bff91c0016bff91ca7e0002', 'memo', '备注', 'memo', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 7, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('fa3a12d7abf72b23afe425f8dbd57f86', '1acb6f81a1d9439da6cc4e868617b565', 'size_type', '尺码类型', NULL, 0, 1, 'String', 2, 0, '', 'air_china_size', '', '', 'list', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 9, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('fa8f5a0ba673e0208934567462844eab', '402860816bff91c0016bff91ca7e0002', 'sys_org_code', '组织机构编码', 'sys_org_code', 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 13, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('fc55d467102c2c782286f546d7820c3d', '73162c3b8161413e8ecdca7eb288d0c9', 'pid', '父物料', NULL, 0, 1, 'String', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'single', '', '', 9, 'admin', '2019-07-01 14:23:32', '2019-06-10 15:18:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('fc76a3832d232829852cae6c66e44f67', '402860816bff91c0016bffa220a9000b', 'identity_no', '身份证号', 'identity_no', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 21, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('fcd519058d68fa4dab192335602b5d24', '402860816bff91c0016bffa220a9000b', 'real_name', '姓名', 'real_name', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 5, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('fde00160a5d664effaa4b5552e814e74', 'fb7125a344a649b990c12949945cb6c1', 'sex', '性别', NULL, 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 'single', '', '', 6, 'admin', '2019-03-26 19:24:11', '2019-03-26 19:01:52', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ff49b468e54e137032f7e4d976b83b5a', '402860816bff91c0016bffa220a9000b', 'politically_status', '政治面貌', 'politically_status', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 8, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ff601f75d0e7ced226748eb8fba2c896', '402860816bff91c0016bff91d8830007', 'relation', '关系', 'relation', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 4, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ffacafee9fa46eb297ca3252f95acef9', '402860816bff91c0016bffa220a9000b', 'school', '毕业学校', 'school', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 'group', '', '', 9, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ffcbf379fffabbd13aa2c22ce565ec12', '79091e8277c744158530321513119c68', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 'single', '', '', 4, 'admin', '2019-05-11 15:29:47', '2019-05-11 15:27:17', 'admin');

-- ----------------------------
-- Table structure for onl_cgform_head
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgform_head`;
CREATE TABLE `onl_cgform_head`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
  `table_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表名',
  `table_type` int(11) NOT NULL COMMENT '表类型: 0单表、1主表、2附表',
  `table_version` int(11) NULL DEFAULT 1 COMMENT '表版本',
  `table_txt` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表说明',
  `is_checkbox` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否带checkbox',
  `is_db_synch` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N' COMMENT '同步数据库状态',
  `is_page` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否分页',
  `is_tree` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否是树',
  `id_sequence` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主键生成序列',
  `id_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主键类型',
  `query_mode` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '查询模式',
  `relation_type` int(11) NULL DEFAULT NULL COMMENT '映射关系 0一对多  1一对一',
  `sub_table_str` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子表',
  `tab_order_num` int(11) NULL DEFAULT NULL COMMENT '附表排序序号',
  `tree_parent_id_field` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '树形表单父id',
  `tree_id_field` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '树表主键字段',
  `tree_fieldname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '树开表单列字段',
  `form_category` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'bdfl_ptbd' COMMENT '表单分类',
  `form_template` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'PC表单模板',
  `form_template_mobile` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单模板样式(移动端)',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_onlineform_table_name`(`table_name`) USING BTREE,
  INDEX `index_form_templdate`(`form_template`) USING BTREE,
  INDEX `index_templdate_mobile`(`form_template_mobile`) USING BTREE,
  INDEX `index_onlineform_table_version`(`table_version`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of onl_cgform_head
-- ----------------------------
INSERT INTO `onl_cgform_head` VALUES ('56870166aba54ebfacb20ba6c770bd73', 'test_order_main', 2, 4, '测试订单主表', 'N', 'Y', 'Y', 'N', NULL, 'UUID', 'single', NULL, 'test_order_product', NULL, NULL, NULL, NULL, 'bdfl_include', '2', NULL, 'admin', '2019-04-20 11:39:54', 'admin', '2019-04-20 11:38:39');
INSERT INTO `onl_cgform_head` VALUES ('d35109c3632c4952a19ecc094943dd71', 'test_demo', 1, 14, '测试用户表', 'N', 'Y', 'Y', 'N', NULL, 'UUID', 'single', NULL, NULL, NULL, NULL, NULL, NULL, 'bdfl_include', '1', NULL, 'admin', '2019-03-22 21:51:35', 'admin', '2019-03-15 14:24:35');
INSERT INTO `onl_cgform_head` VALUES ('deea5a8ec619460c9245ba85dbc59e80', 'test_order_product', 3, 7, '订单产品明细', 'N', 'Y', 'Y', 'N', NULL, 'UUID', 'single', 0, NULL, NULL, NULL, NULL, NULL, 'bdfl_include', '1', NULL, 'admin', '2019-04-20 11:42:53', 'admin', '2019-04-20 11:41:19');

-- ----------------------------
-- Table structure for onl_cgform_index
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgform_index`;
CREATE TABLE `onl_cgform_index`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `cgform_head_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主表id',
  `index_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '索引名称',
  `index_field` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '索引栏位',
  `index_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '索引类型',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_table_id`(`cgform_head_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for test_demo
-- ----------------------------
DROP TABLE IF EXISTS `test_demo`;
CREATE TABLE `test_demo`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `sex` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `age` int(32) NULL DEFAULT NULL COMMENT '年龄',
  `descc` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `birthday` datetime(0) NULL DEFAULT NULL COMMENT '生日',
  `user_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户编码',
  `file_kk` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件',
  `top_pic` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test_demo
-- ----------------------------
INSERT INTO `test_demo` VALUES ('4028810c6aed99e1016aed9b31b40002', NULL, NULL, 'admin', '2019-05-29 17:09:25', 'jeecg', '2', 55, '5', '2019-05-15 00:00:00', NULL, NULL, NULL);
INSERT INTO `test_demo` VALUES ('4028810c6b02cba2016b02cba21f0000', 'admin', '2019-05-29 16:53:48', 'admin', '2019-08-23 23:45:21', '张小红', '1', 8222, '8', '2019-04-01 00:00:00', NULL, '', '');
INSERT INTO `test_demo` VALUES ('4028810c6b40244b016b4030a0e40001', 'admin', '2019-06-10 15:00:57', 'admin', '2019-08-23 23:42:49', '小芳', '2', 0, NULL, '2019-04-01 00:00:00', NULL, '', '');
INSERT INTO `test_demo` VALUES ('fa1d1c249461498d90f405b94f60aae0', '', NULL, 'admin', '2019-05-15 12:30:28', '战三', '2', 222, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for test_order_main
-- ----------------------------
DROP TABLE IF EXISTS `test_order_main`;
CREATE TABLE `test_order_main`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `order_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单编码',
  `order_date` datetime(0) NULL DEFAULT NULL COMMENT '下单时间',
  `descc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test_order_main
-- ----------------------------
INSERT INTO `test_order_main` VALUES ('402831816a38e7fd016a38e825c90003', 'admin', '2019-04-20 12:01:39', 'admin', '2019-08-23 23:43:17', '111333', '2019-04-20 00:00:00', '11');
INSERT INTO `test_order_main` VALUES ('4028810c6b40244b016b40686dfb0003', 'admin', '2019-06-10 16:01:54', 'admin', '2019-06-10 16:06:51', '1231', '2019-06-10 00:00:00', '123');
INSERT INTO `test_order_main` VALUES ('4028810c6b40244b016b4068ef890006', 'admin', '2019-06-10 16:02:27', 'admin', '2019-08-23 23:43:08', 'A001', '2019-06-10 00:00:00', '购买产品BOOT');

-- ----------------------------
-- Table structure for test_order_product
-- ----------------------------
DROP TABLE IF EXISTS `test_order_product`;
CREATE TABLE `test_order_product`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `product_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品名字',
  `price` double(32, 0) NULL DEFAULT NULL COMMENT '价格',
  `num` int(32) NULL DEFAULT NULL COMMENT '数量',
  `descc` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `order_fk_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单外键ID',
  `pro_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test_order_product
-- ----------------------------
INSERT INTO `test_order_product` VALUES ('15665749852471', 'admin', '2019-08-23 23:43:08', NULL, NULL, '222', 222, 22, '', '4028810c6b40244b016b4068ef890006', '2');
INSERT INTO `test_order_product` VALUES ('15665749948861', 'admin', '2019-08-23 23:43:17', NULL, NULL, '333', 33, NULL, '', '402831816a38e7fd016a38e825c90003', '');
INSERT INTO `test_order_product` VALUES ('402831816a38e7fd016a38e7fdeb0001', 'admin', '2019-04-20 12:01:29', NULL, NULL, '笔记本', 100, 10, NULL, '402831816a38e7fd016a38e7fddf0000', NULL);
INSERT INTO `test_order_product` VALUES ('402831816a38e7fd016a38e7fdf10002', 'admin', '2019-04-20 12:01:29', NULL, NULL, '显示器', 300, 1, NULL, '402831816a38e7fd016a38e7fddf0000', NULL);
INSERT INTO `test_order_product` VALUES ('4028810c6b40244b016b40686e050004', 'admin', '2019-06-10 16:06:51', NULL, NULL, '123', 222, 123, '123', '4028810c6b40244b016b40686dfb0003', NULL);
INSERT INTO `test_order_product` VALUES ('4028810c6b40244b016b406884080005', 'admin', '2019-08-23 23:43:17', NULL, NULL, '333', NULL, 33, '', '402831816a38e7fd016a38e825c90003', '');
INSERT INTO `test_order_product` VALUES ('4028810c6b40244b016b4068ef8f0007', 'admin', '2019-08-23 23:43:08', NULL, NULL, 'JEECG-BOOT开发平台', 10000, 1, '', '4028810c6b40244b016b4068ef890006', '1');

SET FOREIGN_KEY_CHECKS = 1;

