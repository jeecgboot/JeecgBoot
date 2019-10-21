

UPDATE `sys_dict_item` SET  `item_text` = '可见/可访问(授权后可见/可访问)'  WHERE `id` = '83250269359855501ec4e9c0b7e21596';


-- ----------------------------
-- Table structure for oss_file
-- ----------------------------
DROP TABLE IF EXISTS `oss_file`;
CREATE TABLE `oss_file` (
  `id` varchar(32) NOT NULL COMMENT '主键id',
  `file_name` varchar(255) default NULL COMMENT '文件名称',
  `url` varchar(255) default NULL COMMENT '文件地址',
  `create_by` varchar(32) default NULL COMMENT '创建人登录名称',
  `create_time` datetime default NULL COMMENT '创建日期',
  `update_by` varchar(32) default NULL COMMENT '更新人登录名称',
  `update_time` datetime default NULL COMMENT '更新日期',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Oss File';
INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status) VALUES ('1166535831146504193', '2a470fc0c3954d9dbb61de6d80846549', '对象存储', '/oss/file', 'modules/oss/OSSFileList', null, null, 1, null, '1', 1, 0, '', 1, 1, 0, 0, null, 'admin', '2019-08-28 02:19:50', 'admin', '2019-08-28 02:20:57', 0, 0, '1');



ALTER TABLE sys_permission
  add COLUMN  internal_or_external  tinyint(1)
  comment '外链菜单打开方式 0/内部打开 1/外部打开';

INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1170592628746878978', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '菜单管理', '/isystem/newPermissionList', 'system/NewPermissionList', NULL, NULL, '1', NULL, '1', '100', '0', NULL, '1', '1', '0', '0', NULL, 'admin', '2019-09-08 15:00:05', 'admin', '2019-09-08 15:02:57', '0', '0', '1', '0');

ALTER TABLE `sys_permission`
ADD INDEX `index_menu_type`(`menu_type`),
ADD INDEX `index_menu_hidden`(`hidden`),
ADD INDEX `index_menu_status`(`status`);

ALTER TABLE `sys_depart`
ADD COLUMN `org_category`  varchar(10) NOT NULL DEFAULT 1 COMMENT '机构类别 1组织机构，2岗位' AFTER `description`;

INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('1174511106530525185', '机构类型', 'org_category', '机构类型 1组织机构，2岗位', '0', 'admin', '2019-09-19 10:30:43', NULL, NULL, '0');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174511197735665665', '1174511106530525185', '组织机构', '1', '组织机构', '1', '1', 'admin', '2019-09-19 10:31:05', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174511244036587521', '1174511106530525185', '岗位', '2', '岗位', '1', '1', 'admin', '2019-09-19 10:31:16', NULL, NULL);

ALTER TABLE `sys_user`
  ADD COLUMN `work_no` varchar(100) NULL COMMENT '工号，唯一键' AFTER `activiti_sync`,
  ADD COLUMN `post` varchar(100) NULL COMMENT '职务，关联职务表' AFTER `work_no`,
  ADD COLUMN `telephone` varchar(45) NULL COMMENT '座机号' AFTER `post`,
  ADD UNIQUE INDEX `uniq_sys_user_work_no` (`work_no`);


DROP TABLE IF EXISTS `sys_position`;
CREATE TABLE `sys_position` (
  `id` varchar(32) NOT NULL,
  `code` varchar(100) DEFAULT NULL COMMENT '职务编码',
  `name` varchar(100) DEFAULT NULL COMMENT '职务名称',
  `rank` varchar(2) DEFAULT NULL COMMENT '职级',
  `company_id` varchar(255) DEFAULT NULL COMMENT '公司id',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `sys_org_code` varchar(50) DEFAULT NULL COMMENT '组织机构编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 职务菜单
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1174506953255182338', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '职务管理', '/isystem/position', 'system/SysPositionList', '1', NULL, NULL, '1', NULL, '1', '2', '0', NULL, '1', '0', '0', NULL, 'admin', '2019-09-19 10:14:13', 'admin', '2019-09-19 10:15:22', '0', '0', '1', '0');

-- 职务职级字典
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('1174509082208395266', '职务职级', 'position_rank', '职务表职级字典', '0', 'admin', '2019-09-19 10:22:41', NULL, NULL, '0');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509601047994369', '1174509082208395266', '员工', '1', '', '1', '1', 'admin', '2019-09-19 10:24:45', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509667297026049', '1174509082208395266', '小组长', '2', '', '2', '1', 'admin', '2019-09-19 10:25:01', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509713568587777', '1174509082208395266', '部门经理', '3', '', '3', '1', 'admin', '2019-09-19 10:25:12', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509788361416705', '1174509082208395266', '副总经理', '4', '', '4', '1', 'admin', '2019-09-19 10:25:30', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509835803189250', '1174509082208395266', '总经理', '5', '', '5', '1', 'admin', '2019-09-19 10:25:41', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509861333917697', '1174509082208395266', '董事长', '6', '', '6', '1', 'admin', '2019-09-19 10:25:47', NULL, NULL);

-- 通讯录菜单
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1174590283938041857', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '通讯录', '/isystem/addressList', 'system/AddressList', '1', NULL, NULL, '1', NULL, '1', '3', '0', NULL, '1', '0', '0', NULL, 'admin', '2019-09-19 15:45:21', NULL, NULL, '0', '0', '1', '0');


DELETE FROM sys_dict_item WHERE dict_id = "1174509082208395266";
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509601047994369', '1174509082208395266', '员级', '1', '', '1', '1', 'admin', '2019-09-19 10:24:45', 'admin', '2019-09-23 11:46:39');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509667297026049', '1174509082208395266', '助级', '2', '', '2', '1', 'admin', '2019-09-19 10:25:01', 'admin', '2019-09-23 11:46:47');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509713568587777', '1174509082208395266', '中级', '3', '', '3', '1', 'admin', '2019-09-19 10:25:12', 'admin', '2019-09-23 11:46:56');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509788361416705', '1174509082208395266', '副高级', '4', '', '4', '1', 'admin', '2019-09-19 10:25:30', 'admin', '2019-09-23 11:47:06');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1174509835803189250', '1174509082208395266', '正高级', '5', '', '5', '1', 'admin', '2019-09-19 10:25:41', 'admin', '2019-09-23 11:47:12');

ALTER TABLE  sys_user
MODIFY COLUMN `sex` tinyint(1) NULL COMMENT '性别(0-默认未知,1-男,2-女)' AFTER `birthday`;

ALTER TABLE  sys_permission
MODIFY COLUMN `sort_no` double(8, 2) NULL DEFAULT NULL COMMENT '菜单排序' AFTER `perms_type`;

INSERT INTO `sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('1178295274528845826', '表单权限策略', 'form_perms_type', '', 0, 'admin', '2019-09-29 21:07:39', 'admin', '2019-09-29 21:08:26', NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1178295553450061826', '1178295274528845826', '可编辑(未授权禁用)', '2', '', 2, 1, 'admin', '2019-09-29 21:08:46', 'admin', '2019-09-29 21:09:18');
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1178295639554928641', '1178295274528845826', '可见(未授权不可见)', '1', '', 1, 1, 'admin', '2019-09-29 21:09:06', 'admin', '2019-09-29 21:09:24');

ALTER TABLE `sys_role`
  ADD UNIQUE INDEX `uniq_sys_role_role_code` (`role_code`) USING BTREE ;

INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`) VALUES ('f2849d3814fc97993bfc519ae6bbf049', 'e41b69c57a941a3bbcce45032fe57605', 'AUTO复制表单', '/online/copyform/:code', 'modules/online/cgform/OnlCgformCopyList', NULL, NULL, '1', NULL, '1', '1', '0', NULL, '1', '1', '0', '1', NULL, 'admin', '2019-08-29 16:05:37', NULL, NULL, '0', '0', '1');

ALTER TABLE `onl_cgform_head`
ADD COLUMN `copy_version`  int(11) NULL COMMENT '复制版本号' AFTER `form_template_mobile`,
ADD COLUMN `copy_type`  int(3) NULL DEFAULT 0 COMMENT '复制表类型1为复制表 0为原始表' AFTER `copy_version`,
ADD COLUMN `physic_id`  varchar(32) NULL COMMENT '原始表ID' AFTER `copy_type`;

ALTER TABLE `onl_cgform_head`
ADD COLUMN `scroll`  int(3) NULL DEFAULT 0 COMMENT '是否有横向滚动条' AFTER `form_template_mobile`;

ALTER TABLE `onl_cgreport_item`
MODIFY COLUMN `dict_code`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典CODE' AFTER `is_search`;
