--  online 新增部门授权 扩展字段
ALTER TABLE `onl_auth_relation`
ADD COLUMN `auth_mode` varchar(50) NULL COMMENT '授权方式role角色，depart部门，user人' AFTER `cgform_id`;
update onl_auth_relation set auth_mode = 'role';

-- 部门表新增 qywx_identifier 字段
ALTER TABLE `sys_depart`
ADD COLUMN `qywx_identifier`  varchar(100) NULL COMMENT '对接企业微信的ID' AFTER `del_flag`;

--  sys_third_account 表新增 third_user_id 字段
ALTER TABLE `sys_third_account`
ADD COLUMN `third_user_id`  varchar(100) NULL COMMENT '第三方app用户账号' AFTER `third_user_uuid`;

--  新增第三方APP消息测试菜单
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1387612436586065922', '2a470fc0c3954d9dbb61de6d80846549', '第三方APP消息测试', '/jeecg/ThirdAppMessageTest', 'jeecg/ThirdAppMessageTest', '1', NULL, NULL, '1', NULL, '1', '3', '0', NULL, '1', '0', '0', NULL, 'admin', '2021-04-29 11:39:20', 'admin', '2021-04-29 11:39:27', '0', '0', '1', '0');

-- 定时任务：一个类允许配置多个调度
-- 删除定时任务表唯一索引
ALTER TABLE `sys_quartz_job`
DROP INDEX `uniq_job_class_name`;

-- 停止所有的定时任务，用于旧数据，只执行一次即可，【执行完毕后需要重启后台项目，并[手动]再次打开定时任务】！
DELETE FROM `qrtz_cron_triggers`;
DELETE FROM `qrtz_fired_triggers`;
DELETE FROM `qrtz_triggers`;
DELETE FROM `qrtz_job_details`;
UPDATE `sys_quartz_job` SET `status` = '-1';

-- 不支持mariaDB数据库处理 issues/I3QID1
update sys_dict_item set item_text = 'MariaDB' where id ='1349250340104474626';
update sys_dict_item set item_text = 'Postgresql' where id ='1349254569766457345';

-- sys_announcement新增dt_task_id字段
ALTER TABLE `sys_announcement`
ADD COLUMN `dt_task_id`  varchar(100) NULL COMMENT '钉钉task_id，用于撤回消息' AFTER `msg_abstract`;


-- 模板类型注释错误
ALTER TABLE `jimu_report`
MODIFY COLUMN `template` tinyint(1) NULL DEFAULT NULL COMMENT '是否是模板 0不是,1是' AFTER `api_code`;

--  查询支持默认值
ALTER TABLE `jimu_report_db_field`
ADD COLUMN `search_value` varchar(100) NULL COMMENT '查询默认值' AFTER `dict_code`;

-- jimu_report_db增加JSON数据字段
ALTER TABLE `jimu_report_db`
ADD COLUMN `json_data` text NULL COMMENT 'json数据，直接解析json内容' AFTER `db_source_type`;

--  连接失败次数
ALTER TABLE `jimu_report_data_source`
ADD COLUMN `connect_times` int(1) NULL COMMENT '连接失败次数' AFTER `update_time`;

-- 联动图表ID
ALTER TABLE `jimu_report_link`
ADD COLUMN `link_chart_id` varchar(50) NULL COMMENT '联动图表的ID' AFTER `api_url`;

-- 修复脚本的一些问题---
update sys_permission set url="{{ window._CONFIG['domianURL'] }}/jmreport/view/1352160857479581696?token=${token}" where id ='1352200630711652354';
UPDATE `jimu_report_db` SET `jimu_report_id` = '1352160857479581696', `create_by` = 'admin', `update_by` = 'admin', `create_time` = '2021-05-19 19:20:44', `update_time` = '2021-05-19 19:20:44', `db_code` = 'infoForReport', `db_ch_name` = '信息', `db_type` = '1', `db_table_name` = NULL, `db_dyn_sql` = NULL, `db_key` = NULL, `tb_db_key` = NULL, `tb_db_table_name` = NULL, `java_type` = NULL, `java_value` = NULL, `api_url` = '{{ domainURL }}/sys/actuator/redis/infoForReport', `api_method` = '0', `is_list` = 1, `is_page` = '1', `db_source` = '', `db_source_type` = NULL, `json_data` = NULL WHERE `id` = '60b3feffadc55eb49baa5a48fdf1ff0e';
UPDATE `jimu_report_db` SET `jimu_report_id` = '1352160857479581696', `create_by` = 'admin', `update_by` = 'admin', `create_time` = '2021-05-19 19:20:50', `update_time` = '2021-05-19 19:20:50', `db_code` = 'memoryForReport', `db_ch_name` = '内存', `db_type` = '1', `db_table_name` = NULL, `db_dyn_sql` = NULL, `db_key` = NULL, `tb_db_key` = NULL, `tb_db_table_name` = NULL, `java_type` = NULL, `java_value` = NULL, `api_url` = '{{ domainURL }}/sys/actuator/redis/memoryForReport', `api_method` = '0', `is_list` = 1, `is_page` = '0', `db_source` = '', `db_source_type` = NULL, `json_data` = NULL WHERE `id` = '6a1d22ca4c95e8fab655d3ceed43a84d';
UPDATE `jimu_report_db` SET `jimu_report_id` = '1352160857479581696', `create_by` = 'admin', `update_by` = 'admin', `create_time` = '2021-05-19 19:21:03', `update_time` = '2021-05-19 19:21:03', `db_code` = 'keysSizeForReport', `db_ch_name` = '数量', `db_type` = '1', `db_table_name` = NULL, `db_dyn_sql` = NULL, `db_key` = NULL, `tb_db_key` = NULL, `tb_db_table_name` = NULL, `java_type` = NULL, `java_value` = NULL, `api_url` = '{{ domainURL }}/sys/actuator/redis/keysSizeForReport', `api_method` = '0', `is_list` = 1, `is_page` = '0', `db_source` = '', `db_source_type` = NULL, `json_data` = NULL WHERE `id` = 'd4a29dfda94357308faf62be2b94db08';
