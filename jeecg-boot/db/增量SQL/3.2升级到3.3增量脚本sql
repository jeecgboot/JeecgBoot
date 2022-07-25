
ALTER TABLE `sys_user`
MODIFY COLUMN `org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录会话的机构编码' AFTER `phone`;

ALTER TABLE `sys_role_index`
ADD COLUMN `component`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件' AFTER `url`,
ADD COLUMN `is_route`  tinyint(1) NULL DEFAULT 1 COMMENT '是否路由菜单: 0:不是  1:是（默认值1）' AFTER `component`;

ALTER TABLE `jeecg_order_main`
ADD COLUMN `bpm_status` varchar(3) NULL COMMENT '流程状态' AFTER `update_time`;

UPDATE `sys_dict_item` SET `dict_id` = '4f69be5f507accea8d5df5f11346181a', `item_text` = '文本', `item_value` = '1', `description` = '', `sort_order` = 1, `status` = 1, `create_by` = 'admin', `create_time` = '2023-02-28 10:50:36', `update_by` = 'admin', `update_time` = '2022-07-04 16:29:21' WHERE `id` = '222705e11ef0264d4214affff1fb4ff9';
UPDATE `sys_dict_item` SET `dict_id` = '4f69be5f507accea8d5df5f11346181a', `item_text` = '富文本', `item_value` = '2', `description` = '', `sort_order` = 2, `status` = 1, `create_by` = 'admin', `create_time` = '2031-02-28 10:50:44', `update_by` = 'admin', `update_time` = '2022-07-04 16:29:30' WHERE `id` = '6a7a9e1403a7943aba69e54ebeff9762';
delete from sys_dict_item where id in ('1199607547704647681', '8bccb963e1cd9e8d42482c54cc609ca2');
update sys_sms_template set template_type = '2' where template_type='4';
update sys_sms_template set template_type = '1' where template_type='3';

ALTER TABLE `sys_sms_template`
ADD COLUMN `use_status` varchar(1) NULL COMMENT '是否使用中 1是0否' AFTER `update_by`;

ALTER TABLE `sys_sms`
MODIFY COLUMN `es_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送方式：参考枚举MessageTypeEnum' AFTER `es_title`;


ALTER TABLE  jimu_report_data_source
ADD COLUMN tenant_id varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '多租户标识' AFTER connect_times;

ALTER TABLE jimu_dict
ADD COLUMN tenant_id varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '多租户标识' AFTER type;

ALTER TABLE jimu_report
ADD COLUMN tenant_id varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '多租户标识' AFTER js_str;
