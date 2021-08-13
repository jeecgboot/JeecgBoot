
-- 平台基础模块
ALTER TABLE `sys_third_account`
ADD UNIQUE INDEX `uniq_sys_third_account_third_type_third_user_id` (`third_type`, `third_user_id`) USING BTREE ;

INSERT INTO `sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `status`, `del_flag`, `rule_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `internal_or_external`) VALUES ('1404684556047024130', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '在线用户', '/isystem/online', 'system/SysUserOnlineList', NULL, NULL, 1, NULL, '1', NULL, 0, NULL, 1, 1, 0, 0, NULL, '1', 0, 0, 'admin', '2021-06-15 14:17:51', NULL, NULL, 0);

DELETE FROM sys_depart WHERE id = '743ba9dbdc114af8953a11022ef3096a';

alter table sys_quartz_job engine = InnoDB;

UPDATE `sys_dict_item` SET `item_value` = '6' WHERE `item_text` = 'MYSQL5.7';

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1414837074500976641', '1209733563293962241', 'Postgresql', '6', '', '5', '1', 'admin', '2021-07-13 14:40:20', 'admin', '2021-07-15 13:44:15');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1415547541091504129', '1209733563293962241', 'MarialDB', '5', '', '6', '1', 'admin', '2021-07-15 13:43:28', 'admin', '2021-07-15 13:44:23');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1418050323111399425', '1209733563293962241', 'Derby', '13', '', '13', '1', 'admin', '2021-07-22 11:28:38', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1418050209823248385', '1209733563293962241', 'Hsqldb', '12', '', '12', '1', 'admin', '2021-07-22 11:28:11', 'admin', '2021-07-22 11:28:27');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1418050149475602434', '1209733563293962241', 'DB2', '11', '', '11', '1', 'admin', '2021-07-22 11:27:56', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1418050110669901826', '1209733563293962241', 'SQLite', '10', '', '10', '1', 'admin', '2021-07-22 11:27:47', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1418050075555188737', '1209733563293962241', '神通', '9', '', '9', '1', 'admin', '2021-07-22 11:27:39', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1418050017053036545', '1209733563293962241', '人大金仓', '8', '', '8', '1', 'admin', '2021-07-22 11:27:25', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1418049969003089922', '1209733563293962241', '达梦', '7', '', '7', '1', 'admin', '2021-07-22 11:27:13', 'admin', '2021-07-22 11:27:30');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1418117316707590146', '1209733563293962241', 'H2', '14', '', '14', '1', 'admin', '2021-07-22 15:54:50', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1418491604048449537', '1209733563293962241', '其他数据库', '15', '', 15, 1, 'admin', '2021-07-23 16:42:07', NULL, NULL);

ALTER TABLE demo ADD COLUMN tenant_id int(10) NULL DEFAULT 0;



-- Online模块
ALTER TABLE `onl_cgform_head`
ADD COLUMN `ext_config_json`  varchar(1000) NULL COMMENT '扩展JSON' AFTER `physic_id`;

ALTER TABLE `onl_cgreport_head`
ADD COLUMN `low_app_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联的应用ID' AFTER `content`;




-- 积木报表模块

UPDATE `jimu_report` SET `json_str` = '{\"loopBlockList\":[],\"area\":{\"sri\":16,\"sci\":5,\"eri\":16,\"eci\":5,\"width\":147,\"height\":25},\"excel_config_id\":\"1347373863746539520\",\"printConfig\":{\"paper\":\"A4\",\"width\":210,\"height\":297,\"definition\":1,\"isBackend\":false,\"marginX\":10,\"marginY\":10,\"layout\":\"portrait\"},\"rows\":{\"0\":{\"cells\":{\"0\":{\"text\":\"\"},\"1\":{\"text\":\"\"}}},\"1\":{\"cells\":{\"0\":{\"text\":\"\"}}},\"3\":{\"cells\":{\"2\":{\"text\":\"\",\"rendered\":\"\"}}},\"5\":{\"cells\":{},\"height\":29},\"6\":{\"cells\":{\"2\":{\"text\":\"\",\"style\":2}},\"height\":34},\"7\":{\"cells\":{\"2\":{\"merge\":[0,4],\"text\":\"实习证明\",\"style\":2}},\"height\":41},\"8\":{\"cells\":{\"1\":{\"text\":\"\",\"style\":3},\"2\":{\"text\":\"\"}}},\"9\":{\"cells\":{\"1\":{\"text\":\"\",\"style\":3},\"2\":{\"text\":\"\",\"style\":3},\"3\":{\"text\":\"\"}},\"isDrag\":true,\"height\":33},\"10\":{\"cells\":{\"2\":{\"text\":\"${tt.name}\",\"style\":11},\"3\":{\"text\":\"同学在我公司与 2020年4月1日 至 2020年5月1日 实习。\",\"style\":19,\"merge\":[0,3],\"height\":34}},\"height\":34},\"11\":{\"cells\":{},\"height\":28},\"12\":{\"cells\":{\"1\":{\"text\":\"\",\"style\":6},\"2\":{\"style\":13,\"text\":\"${tt.pingjia}\",\"merge\":[3,4],\"height\":129}},\"height\":36},\"13\":{\"cells\":{},\"height\":29},\"14\":{\"cells\":{},\"height\":33},\"15\":{\"cells\":{},\"height\":31},\"16\":{\"cells\":{}},\"17\":{\"cells\":{\"1\":{\"text\":\"\"},\"2\":{\"text\":\"特此证明！\",\"style\":12}}},\"20\":{\"cells\":{\"2\":{\"text\":\"\"},\"3\":{\"text\":\"\",\"style\":3},\"4\":{\"text\":\"\"}}},\"21\":{\"cells\":{\"4\":{\"text\":\"\"}}},\"22\":{\"cells\":{\"3\":{\"text\":\"\",\"style\":3},\"4\":{\"text\":\"证明人：\",\"style\":11},\"5\":{\"text\":\"${tt.lingdao}\",\"style\":12}}},\"23\":{\"cells\":{\"4\":{\"text\":\"\"},\"5\":{\"text\":\"${tt.shijian}\",\"style\":15}}},\"len\":100},\"dbexps\":[],\"dicts\":[],\"freeze\":\"A1\",\"dataRectWidth\":576,\"displayConfig\":{},\"background\":{\"path\":\"https://static.jeecg.com/designreport/images/11_1611283832037.png\",\"repeat\":\"no-repeat\",\"width\":\"\",\"height\":\"\"},\"name\":\"sheet1\",\"autofilter\":{},\"styles\":[{\"align\":\"center\"},{\"align\":\"center\",\"font\":{\"size\":14}},{\"align\":\"center\",\"font\":{\"size\":16}},{\"align\":\"right\"},{\"align\":\"left\"},{\"align\":\"left\",\"valign\":\"top\"},{\"align\":\"left\",\"valign\":\"top\",\"textwrap\":true},{\"font\":{\"size\":16}},{\"align\":\"left\",\"valign\":\"top\",\"textwrap\":false},{\"textwrap\":false},{\"textwrap\":true},{\"align\":\"right\",\"font\":{\"size\":12}},{\"font\":{\"size\":12}},{\"align\":\"left\",\"valign\":\"top\",\"textwrap\":true,\"font\":{\"size\":12}},{\"textwrap\":true,\"font\":{\"size\":12}},{\"align\":\"left\",\"font\":{\"size\":12}},{\"font\":{\"size\":12},\"border\":{\"bottom\":[\"thin\",\"#000\"],\"top\":[\"thin\",\"#000\"],\"left\":[\"thin\",\"#000\"],\"right\":[\"thin\",\"#000\"]}},{\"font\":{\"size\":14}},{\"font\":{\"size\":10}},{\"textwrap\":false,\"font\":{\"size\":12}}],\"validations\":[],\"cols\":{\"0\":{\"width\":69},\"1\":{\"width\":41},\"4\":{\"width\":119},\"5\":{\"width\":147},\"6\":{\"width\":31},\"len\":50},\"merges\":[\"C8:G8\",\"D11:G11\",\"C13:G16\"]}'  WHERE `id` = '1347373863746539520';

update jimu_report_data_source set connect_times = 0;
ALTER TABLE `jimu_report_data_source`
MODIFY COLUMN `connect_times` int(1) UNSIGNED NULL DEFAULT 0 COMMENT '连接失败次数' AFTER `update_time`;

ALTER TABLE `jimu_report_db_param`
MODIFY COLUMN `param_value` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数默认值' AFTER `param_txt`;

DELETE FROM jimu_report_map
WHERE
	  id IN (SELECT id FROM (SELECT id FROM jimu_report_map WHERE NAME IN ( SELECT NAME FROM jimu_report_map GROUP BY NAME HAVING count(NAME) > 1)) T)
AND id NOT IN (SELECT id FROM ( SELECT min(id) id FROM jimu_report_map GROUP BY NAME HAVING count(NAME) > 1) M);

ALTER TABLE `jimu_report_map`
ADD UNIQUE INDEX `uniq_jmreport_map_name`(`name`);

update jimu_report set VIEW_COUNT = 0 WHERE VIEW_COUNT is null or VIEW_COUNT = '';
ALTER TABLE `jimu_report`
MODIFY COLUMN `view_count` bigint(15) NULL DEFAULT 0 COMMENT '浏览次数' AFTER `template`;

ALTER TABLE `jimu_report_db`
ADD INDEX `idx_jimu_report_id`(`jimu_report_id`);
ALTER TABLE `jimu_report_db`
ADD INDEX `idx_db_source_id`(`db_source`);
ALTER TABLE `jimu_report_db_field`
ADD INDEX `idx_dbfield_order_num`(`order_num`);
ALTER TABLE `jimu_report`
ADD INDEX `uniq_jmreport_createby`(`create_by`);
ALTER TABLE `jimu_report`
ADD INDEX `uniq_jmreport_delflag`(`del_flag`);
ALTER TABLE `jimu_report_link`
ADD INDEX `uniq_link_reportid`(`report_id`);

ALTER TABLE `jimu_report`
MODIFY COLUMN `json_str` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'json字符串' AFTER `type`;

ALTER TABLE `jimu_report_link`
ADD COLUMN `expression` varchar(255) NULL COMMENT '表达式' AFTER `link_chart_id`;


-- 执行下面段可能会报错，说明此部分升级过了，忽略即可
ALTER TABLE `jimu_report_db_param`
ADD COLUMN `search_flag` int(1) NULL COMMENT '查询标识0否1是 默认0' AFTER `update_time`;
update jimu_report_db_param set search_flag = 0;

create table jimu_dict like sys_dict;
insert into jimu_dict select * from sys_dict;
create table jimu_dict_item like sys_dict_item;
insert into jimu_dict_item select * from sys_dict_item;

ALTER TABLE `jimu_report_db_param`
ADD COLUMN `widget_type` varchar(50) NULL COMMENT '查询控件类型' AFTER `search_flag`,
ADD COLUMN `search_mode` int(1) NULL COMMENT '查询模式1简单2范围' AFTER `widget_type`,
ADD COLUMN `dict_code` varchar(255) NULL COMMENT '字典' AFTER `search_mode`;
