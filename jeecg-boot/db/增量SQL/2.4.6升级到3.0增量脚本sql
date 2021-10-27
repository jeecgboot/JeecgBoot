-- 字段长度不规范，导致转库错误Specified key was too long; max key length is 767 bytes
ALTER TABLE `rep_demo_dxtj`
MODIFY COLUMN `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键' FIRST;

ALTER TABLE `sys_third_account`
MODIFY COLUMN `third_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录来源' AFTER `third_user_id`;


-- 数据源字典sql整理
DELETE FROM `sys_dict_item` WHERE dict_id ='1209733563293962241';
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1209733775114702850', '1209733563293962241', 'MySQL5.5', '1', '', 1, 1, 'admin', '2019-12-25 15:13:02', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1334440962954936321', '1209733563293962241', 'MYSQL5.7+', '4', '', 2, 1, 'admin', '2020-12-03 18:16:02', 'admin', '2021-07-15 13:44:29');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1209733839933476865', '1209733563293962241', 'Oracle', '2', '', 3, 1, 'admin', '2019-12-25 15:13:18', 'admin', '2021-07-15 13:44:08');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1209733903020003330', '1209733563293962241', 'SQLServer', '3', '', 4, 1, 'admin', '2019-12-25 15:13:33', 'admin', '2021-07-15 13:44:11');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1414837074500976641', '1209733563293962241', 'postgresql', '6', '', 5, 1, 'admin', '2021-07-13 14:40:20', 'admin', '2021-07-15 13:44:15');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1415547541091504129', '1209733563293962241', 'marialDB', '5', '', 6, 1, 'admin', '2021-07-15 13:43:28', 'admin', '2021-07-15 13:44:23');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1418049969003089922', '1209733563293962241', '达梦', '7', '', 7, 1, 'admin', '2021-07-22 11:27:13', 'admin', '2021-07-22 11:27:30');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1418050017053036545', '1209733563293962241', '人大金仓', '8', '', 8, 1, 'admin', '2021-07-22 11:27:25', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1418050075555188737', '1209733563293962241', '神通', '9', '', 9, 1, 'admin', '2021-07-22 11:27:39', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1418050110669901826', '1209733563293962241', 'SQLite', '10', '', 10, 1, 'admin', '2021-07-22 11:27:47', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1418050149475602434', '1209733563293962241', 'DB2', '11', '', 11, 1, 'admin', '2021-07-22 11:27:56', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1418050209823248385', '1209733563293962241', 'Hsqldb', '12', '', 12, 1, 'admin', '2021-07-22 11:28:11', 'admin', '2021-07-22 11:28:27');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1418050323111399425', '1209733563293962241', 'Derby', '13', '', 13, 1, 'admin', '2021-07-22 11:28:38', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1418117316707590146', '1209733563293962241', 'H2', '14', '', 14, 1, 'admin', '2021-07-22 15:54:50', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1418491604048449537', '1209733563293962241', '其他数据库', '15', '', 15, 1, 'admin', '2021-07-23 16:42:07', NULL, NULL);


-- 新增 hideTab 字段
ALTER TABLE `sys_permission`
ADD COLUMN `hide_tab` int(2) NULL COMMENT '是否隐藏tab: 0否,1是' AFTER `hidden`;



-- 【online表单】新增 low_app_id 字段 
ALTER TABLE `onl_cgform_head`
ADD COLUMN `low_app_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联的应用ID' AFTER `des_form_code`;



-- online老数据,存在字符串类型key值不一致的情况,String改为string
UPDATE onl_cgform_field SET db_type = 'string' where binary  db_type = 'String';

--  积木报表升级
ALTER TABLE `jimu_report`
MODIFY COLUMN `view_count` bigint(15) NULL DEFAULT 0 COMMENT '浏览次数' AFTER `template`;
ALTER TABLE `jimu_report`
MODIFY COLUMN `json_str` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'json字符串' AFTER `type`;
ALTER TABLE `jimu_report_db_field`
ADD COLUMN `search_format` varchar(50) NULL COMMENT '查询时间格式化表达式' AFTER `search_value`;
ALTER TABLE `jimu_report_db_param`
ADD COLUMN `search_format` varchar(50) NULL COMMENT '查询时间格式化表达式' AFTER `dict_code`;
UPDATE jimu_report SET json_str=replace(json_str,'"subtotal":"totalField"','"funcname":"SUM"');
ALTER TABLE `jimu_report`
ADD COLUMN `css_str` text NULL COMMENT 'css增强' AFTER `view_count`,
ADD COLUMN `js_str` text NULL COMMENT 'js增强' AFTER `css_str`;
ALTER TABLE `jimu_report_link` 
CHANGE COLUMN `expression` `requirement` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条件' AFTER `link_chart_id`;
ALTER TABLE `jimu_report_db_field` 
ADD COLUMN `ext_json` text  NULL COMMENT '参数配置' AFTER `search_format`;
ALTER TABLE `jimu_report_db_param` 
ADD COLUMN `ext_json`  text  NULL COMMENT '参数配置' AFTER `search_format`;
ALTER TABLE `jimu_report_db` 
MODIFY COLUMN `is_list` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否是列表0否1是 默认0' AFTER `api_method`;
