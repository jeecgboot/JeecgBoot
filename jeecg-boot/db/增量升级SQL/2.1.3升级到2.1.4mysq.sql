
-- 多数据源表
DROP TABLE IF EXISTS `sys_data_source`;
CREATE TABLE `sys_data_source` (
 `id` varchar(36) NOT NULL,
 `name` varchar(100) DEFAULT NULL COMMENT '数据源名称',
 `remark` varchar(200) DEFAULT NULL COMMENT '备注',
 `db_type` varchar(10) DEFAULT NULL COMMENT '数据库类型',
 `db_driver` varchar(100) DEFAULT NULL COMMENT '驱动类',
 `db_url` varchar(500) DEFAULT NULL COMMENT '数据源地址',
 `db_name` varchar(100) DEFAULT NULL COMMENT '数据库名称',
 `db_username` varchar(100) DEFAULT NULL COMMENT '用户名',
 `db_password` varchar(100) DEFAULT NULL COMMENT '密码',
 `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
 `create_time` datetime DEFAULT NULL COMMENT '创建日期',
 `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
 `update_time` datetime DEFAULT NULL COMMENT '更新日期',
 `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO `sys_data_source` VALUES ('1209779538310004737', 'MySQL5.7', '本地数据库MySQL5.7', '1', 'com.mysql.jdbc.Driver', 'jdbc:mysql://127.0.0.1:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false', 'jeecg-boot', 'root', 'root', 'admin', '2019-12-25 18:14:53', null, null, 'A01');

-- 数据库类型字典
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('1209733563293962241', '数据库类型', 'database_type', '', '0', 'admin', '2019-12-25 15:12:12', NULL, NULL, '0');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1209733775114702850', '1209733563293962241', 'MySQL', '1', '', '1', '1', 'admin', '2019-12-25 15:13:02', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1209733839933476865', '1209733563293962241', 'Oracle', '2', '', '1', '1', 'admin', '2019-12-25 15:13:18', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1209733903020003330', '1209733563293962241', 'SQLServer', '3', '', '1', '1', 'admin', '2019-12-25 15:13:33', NULL, NULL);


-- 生产销售监控 --
update sys_permission set url='{{ window._CONFIG[\'domianURL\'] }}/big/screen/templat/index1' where id='1205098241075453953';
-- 智慧物流监控 --
update sys_permission set url='{{ window._CONFIG[\'domianURL\'] }}/big/screen/templat/index2' where id='1205306106780364802';



ALTER TABLE `sys_user`
    ADD  COLUMN `identity` tinyint(1) DEFAULT NULL COMMENT '身份（1普通成员 2上级）' AFTER `update_time`;

ALTER TABLE `sys_user`
    ADD  COLUMN `depart_id` longtext DEFAULT NULL COMMENT '负责部门' AFTER `identity`;

ALTER TABLE `sys_user`
    CHANGE COLUMN `depart_id` `depart_ids`  longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '负责部门' AFTER `identity`;




ALTER TABLE `sys_announcement`
ADD COLUMN `bus_type` varchar(20) COMMENT '业务类型(email:邮件 bpm:流程)' AFTER `del_flag`,
ADD COLUMN `bus_id` varchar(50) COMMENT '业务id' AFTER `bus_type`,
ADD COLUMN `open_type` varchar(20) COMMENT '打开方式(组件：component 路由：url)' AFTER `bus_id`,
ADD COLUMN `open_page` varchar(255) COMMENT '组件/路由 地址' AFTER `open_type`;


INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1209731624921534465', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '多数据源管理', '/isystem/dataSource', 'system/SysDataSourceList', '1', NULL, NULL, '1', NULL, '1', '3', '0', NULL, '1', '0', '0', NULL, 'admin', '2019-12-25 15:04:30', NULL, NULL, '0', '0', '1', '0');


ALTER TABLE `sys_data_source`
    ADD COLUMN `code`  varchar(100) NULL COMMENT '数据源编码' AFTER `id`;

ALTER TABLE `sys_data_source`
    ADD UNIQUE INDEX `sys_data_source_code_uni` (`code`) USING BTREE ;

-- 编码校验规则表
DROP TABLE IF EXISTS `sys_check_rule`;
CREATE TABLE `sys_check_rule` (
    `id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '主键id',
    `rule_name` varchar(100) DEFAULT NULL COMMENT '规则名称',
    `rule_code` varchar(100) DEFAULT NULL COMMENT '规则Code',
    `rule_json` varchar(1024) DEFAULT NULL COMMENT '规则JSON',
    `rule_description` varchar(200) DEFAULT NULL COMMENT '规则描述',
    `update_by` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `create_by` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建人',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_sys_check_rule_code` (`rule_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 编码校验规则测试数据
INSERT INTO `sys_check_rule` VALUES ('1224980593992388610', '通用编码规则', 'common', '[{\"digits\":\"1\",\"pattern\":\"^[a-z|A-Z]$\",\"message\":\"第一位只能是字母\"},{\"digits\":\"*\",\"pattern\":\"^[0-9|a-z|A-Z|_]{0,}$\",\"message\":\"只能填写数字、大小写字母、下划线\"},{\"digits\":\"*\",\"pattern\":\"^.{3,}$\",\"message\":\"最少输入3位数\"},{\"digits\":\"*\",\"pattern\":\"^.{3,12}$\",\"message\":\"最多输入12位数\"}]', '规则：1、首位只能是字母；2、只能填写数字、大小写字母、下划线；3、最少3位数，最多12位数。', 'admin', '2020-02-07 11:25:48', 'admin', '2020-02-05 16:58:27');
INSERT INTO `sys_check_rule` VALUES ('1225001845524004866', '负责的功能测试', 'test', '[{\"digits\":\"*\",\"pattern\":\"^.{3,12}$\",\"message\":\"只能输入3-12位字符\"},{\"digits\":\"3\",\"pattern\":\"^\\\\d{3}$\",\"message\":\"前3位必须是数字\"},{\"digits\":\"*\",\"pattern\":\"^[^pP]*$\",\"message\":\"不能输入P\"},{\"digits\":\"4\",\"pattern\":\"^@{4}$\",\"message\":\"第4-7位必须都为 @\"},{\"digits\":\"2\",\"pattern\":\"^#=$\",\"message\":\"第8-9位必须是 #=\"},{\"digits\":\"1\",\"pattern\":\"^O$\",\"message\":\"第10位必须为大写的O\"},{\"digits\":\"*\",\"pattern\":\"^.*。$\",\"message\":\"必须以。结尾\"}]', '包含长度校验、特殊字符校验等', 'admin', '2020-02-07 11:57:31', 'admin', '2020-02-05 18:22:54');

-- 编码校验规则菜单
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1224641973866467330', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '编码校验规则', '/isystem/checkRule', 'system/SysCheckRuleList', '1', NULL, NULL, '1', NULL, '1', '2', '0', NULL, '1', '0', '0', NULL, 'admin', '2019-11-07 13:52:53', NULL, NULL, '0', '0', '1', '0');

DROP TABLE IF EXISTS `sys_depart_permission`;
CREATE TABLE `sys_depart_permission` (
  `id` varchar(32) NOT NULL,
  `depart_id` varchar(32) DEFAULT NULL COMMENT '部门id',
  `permission_id` varchar(32) DEFAULT NULL COMMENT '权限id',
  `data_rule_ids` varchar(1000) DEFAULT NULL COMMENT '数据规则id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门权限表';

DROP TABLE IF EXISTS `sys_depart_role`;
CREATE TABLE `sys_depart_role` (
  `id` varchar(32) NOT NULL,
  `depart_id` varchar(32) DEFAULT NULL COMMENT '部门id',
  `role_name` varchar(200) DEFAULT NULL COMMENT '部门角色名称',
  `role_code` varchar(100) DEFAULT NULL COMMENT '部门角色编码',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门角色表';

DROP TABLE IF EXISTS `sys_depart_role_permission`;
CREATE TABLE `sys_depart_role_permission` (
  `id` varchar(32) NOT NULL,
  `depart_id` varchar(32) DEFAULT NULL COMMENT '部门id',
  `role_id` varchar(32) DEFAULT NULL COMMENT '角色id',
  `permission_id` varchar(32) DEFAULT NULL COMMENT '权限id',
  `data_rule_ids` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_group_role_per_id` (`role_id`,`permission_id`) USING BTREE,
  KEY `index_group_role_id` (`role_id`) USING BTREE,
  KEY `index_group_per_id` (`permission_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='部门角色权限表';

DROP TABLE IF EXISTS `sys_depart_role_user`;
CREATE TABLE `sys_depart_role_user` (
  `id` varchar(32) NOT NULL COMMENT '主键id',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `drole_id` varchar(32) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门角色用户表';

ALTER TABLE `onl_cgform_head`
ADD COLUMN `theme_template` varchar(50) NULL COMMENT '主题模板' AFTER `create_time`;
update onl_cgform_head SET theme_template = 'normal';
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1229674163694841857', 'e41b69c57a941a3bbcce45032fe57605', 'AUTO在线表单ERP', '/online/cgformErpList/:code', 'modules/online/cgform/auto/erp/OnlCgformErpList', NULL, NULL, 1, NULL, '1', 5.00, 0, NULL, 1, 1, 0, 1, NULL, 'admin', '2020-02-18 15:49:00', 'admin', '2020-02-18 15:52:25', 0, 0, '1', 0);

ALTER TABLE `onl_cgform_field`
ADD COLUMN `is_read_only`  tinyint(1) NULL DEFAULT 0 COMMENT '是否是只读（1是 0否）' AFTER `is_show_list`;