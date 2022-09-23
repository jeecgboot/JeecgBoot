-- 系统通知消息支持标星
ALTER TABLE `sys_announcement_send` 
ADD COLUMN `star_flag` varchar(10) NULL COMMENT '标星状态( 1为标星 空/0没有标星)' AFTER `update_time`;

ALTER TABLE `sys_comment`
MODIFY COLUMN `comment_content` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '回复内容' AFTER `comment_id`;

ALTER TABLE `sys_data_log`
ADD COLUMN `type` varchar(20) NULL DEFAULT 'json' COMMENT '类型' AFTER `data_version`;
update sys_data_log set type = 'json';


-- vue3单表原生组件菜单 
INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1534418199197323265', '1438108197958311537', '单表原生示例', '/one/OneNativeList', 'demo/jeecg/Native/one/OneNativeList', 1, NULL, NULL, 1, NULL, '0', 13.00, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '2022-06-08 14:13:01', 'admin', '2022-06-08 14:13:12', 0, 0, NULL, 0);

INSERT INTO  `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('1242298510024429569', '提醒方式', 'remindMode', '', 0, 'admin', '2020-03-24 11:53:40', 'admin', '2020-03-24 12:03:22', 0);
INSERT INTO  `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1242300779390357505', '1242298510024429569', '短信提醒', '2', '', 2, 1, 'admin', '2020-03-24 12:02:41', 'admin', '2020-03-30 18:21:33');
INSERT INTO  `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1242300814383435777', '1242298510024429569', '邮件提醒', '1', '', 1, 1, 'admin', '2020-03-24 12:02:49', 'admin', '2020-03-30 18:21:26');
INSERT INTO  `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1242300887343353857', '1242298510024429569', '系统消息', '4', '', 4, 1, 'admin', '2020-03-24 12:03:07', 'admin', '2020-03-30 18:21:43');

-- 删除vue3作废菜单
DELETE from sys_permission  where component = 'demo/comp/upload/index';

-- vue3示例菜单缓存配置修正
UPDATE `sys_permission` SET `parent_id` = '1438108187455774722', `name` = '多级菜单', `url` = '/level', `component` = 'layouts/default/index', `is_route` = 1, `component_name` = NULL, `redirect` = '/level/menu1/menu1-1/menu1-1-1', `menu_type` = 0, `perms` = NULL, `perms_type` = '1', `sort_no` = 9.00, `always_show` = 0, `icon` = 'ion:menu-outline', `is_leaf` = 0, `keep_alive` = 0, `hidden` = 0, `hide_tab` = NULL, `description` = NULL, `create_by` = 'admin', `create_time` = '2021-09-15 19:51:33', `update_by` = NULL, `update_time` = NULL, `del_flag` = 0, `rule_flag` = 0, `status` = '1', `internal_or_external` = 0 WHERE `id` = '1438108220418809857';
UPDATE `sys_permission` SET `parent_id` = '1438108220418809857', `name` = 'Menu1', `url` = '/level/menu1', `component` = NULL, `is_route` = 1, `component_name` = NULL, `redirect` = '/level/menu1/menu1-1/menu1-1-1', `menu_type` = 1, `perms` = NULL, `perms_type` = '1', `sort_no` = 0.00, `always_show` = 0, `icon` = NULL, `is_leaf` = 0, `keep_alive` = 0, `hidden` = 0, `hide_tab` = NULL, `description` = NULL, `create_by` = 'admin', `create_time` = '2021-09-15 19:51:33', `update_by` = NULL, `update_time` = NULL, `del_flag` = 0, `rule_flag` = 0, `status` = '1', `internal_or_external` = 0 WHERE `id` = '1438108220523667458';
UPDATE `sys_permission` SET `parent_id` = '1438108220418809857', `name` = 'Menu2', `url` = '/level/menu2', `component` = 'demo/level/Menu2', `is_route` = 1, `component_name` = 'Menu2Demo', `redirect` = NULL, `menu_type` = 1, `perms` = NULL, `perms_type` = '1', `sort_no` = 1.00, `always_show` = 0, `icon` = NULL, `is_leaf` = 1, `keep_alive` = 1, `hidden` = 0, `hide_tab` = 0, `description` = NULL, `create_by` = 'admin', `create_time` = '2021-09-15 19:51:33', `update_by` = 'admin', `update_time` = '2022-09-20 15:24:13', `del_flag` = 0, `rule_flag` = 0, `status` = '1', `internal_or_external` = 0 WHERE `id` = '1438108220724994049';
UPDATE `sys_permission` SET `parent_id` = '1438108220523667458', `name` = 'Menu1-1', `url` = '/level/menu1/menu1-1', `component` = NULL, `is_route` = 1, `component_name` = NULL, `redirect` = '/level/menu1/menu1-1/menu1-1-1', `menu_type` = 1, `perms` = NULL, `perms_type` = '1', `sort_no` = 0.00, `always_show` = 0, `icon` = NULL, `is_leaf` = 0, `keep_alive` = 0, `hidden` = 0, `hide_tab` = NULL, `description` = NULL, `create_by` = 'admin', `create_time` = '2021-09-15 19:51:33', `update_by` = NULL, `update_time` = NULL, `del_flag` = 0, `rule_flag` = 0, `status` = '1', `internal_or_external` = 0 WHERE `id` = '1438108220896960513';
UPDATE `sys_permission` SET `parent_id` = '1438108220896960513', `name` = 'Menu1-1-1', `url` = '/level/menu1/menu1-1/menu1-1-1', `component` = 'demo/level/Menu111', `is_route` = 1, `component_name` = 'Menu111Demo', `redirect` = NULL, `menu_type` = 1, `perms` = NULL, `perms_type` = '1', `sort_no` = 0.00, `always_show` = 0, `icon` = NULL, `is_leaf` = 1, `keep_alive` = 1, `hidden` = 0, `hide_tab` = 0, `description` = NULL, `create_by` = 'admin', `create_time` = '2021-09-15 19:51:33', `update_by` = 'admin', `update_time` = '2022-09-20 15:24:03', `del_flag` = 0, `rule_flag` = 0, `status` = '1', `internal_or_external` = 0 WHERE `id` = '1438108221127647234';
UPDATE `sys_permission` SET `parent_id` = '1438108220523667458', `name` = 'Menu1-2', `url` = '/level/menu1/menu1-2', `component` = 'demo/level/Menu12', `is_route` = 1, `component_name` = 'Menu12Demo', `redirect` = NULL, `menu_type` = 1, `perms` = NULL, `perms_type` = '1', `sort_no` = 1.00, `always_show` = 0, `icon` = NULL, `is_leaf` = 1, `keep_alive` = 1, `hidden` = 0, `hide_tab` = 0, `description` = NULL, `create_by` = 'admin', `create_time` = '2021-09-15 19:51:33', `update_by` = 'admin', `update_time` = '2022-09-20 15:24:08', `del_flag` = 0, `rule_flag` = 0, `status` = '1', `internal_or_external` = 0 WHERE `id` = '1438108221270253570';


-- 新增评论表和文件表
DROP TABLE IF EXISTS `sys_comment`;
CREATE TABLE `sys_comment`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `table_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表名',
  `table_data_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据id',
  `from_user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '来源用户id',
  `to_user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送给用户id(允许为空)',
  `comment_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论id(允许为空，不为空时，则为回复)',
  `comment_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回复内容',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_table_data_id`(`table_name`, `table_data_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统评论回复表' ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS `sys_files`;
CREATE TABLE `sys_files`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `url` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件地址',
  `file_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文档类型（folder:文件夹 excel:excel doc:word ppt:ppt image:图片  archive:其他文档 video:视频 pdf:pdf）',
  `store_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件上传类型(temp/本地上传(临时文件) manage/知识库)',
  `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父级id',
  `tenant_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户id',
  `file_size` double(13, 2) NULL DEFAULT NULL COMMENT '文件大小（kb）',
  `iz_folder` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否文件夹(1：是  0：否)',
  `iz_root_folder` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否为1级文件夹，允许为空 (1：是 )',
  `iz_star` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否标星(1：是  0：否)',
  `down_count` int(11) NULL DEFAULT NULL COMMENT '下载次数',
  `read_count` int(11) NULL DEFAULT NULL COMMENT '阅读次数',
  `share_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分享链接',
  `share_perms` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分享权限(1.关闭分享 2.允许所有联系人查看 3.允许任何人查看)',
  `enable_down` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否允许下载(1：是  0：否)',
  `enable_updat` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否允许修改(1：是  0：否)',
  `del_flag` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除状态(0-正常,1-删除至回收站)',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '知识库-文档管理' ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS `sys_form_file`;
CREATE TABLE `sys_form_file`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `table_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表名',
  `table_data_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据id',
  `file_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联文件id',
  `file_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件类型(text:文本, excel:excel doc:word ppt:ppt image:图片  archive:其他文档 video:视频 pdf:pdf）)',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_table_form`(`table_name`, `table_data_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE `sys_files`
MODIFY COLUMN `tenant_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户id' AFTER `parent_id`;

ALTER TABLE `sys_files`
ADD INDEX `index_tenant_id`(`tenant_id`),
ADD INDEX `index_del_flag`(`del_flag`);

ALTER TABLE `sys_form_file`
ADD INDEX `index_file_id`(`file_id`);



