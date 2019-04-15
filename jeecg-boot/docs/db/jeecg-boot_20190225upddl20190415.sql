ALTER TABLE `jeecg_boot`.`qrtz_blob_triggers` COMMENT = 'InnoDB free: 504832 kB; (`SCHED_NAME` `TRIGGER_NAME` `TRIGGE';
ALTER TABLE `jeecg_boot`.`qrtz_cron_triggers` COMMENT = 'InnoDB free: 504832 kB; (`SCHED_NAME` `TRIGGER_NAME` `TRIGGE';
ALTER TABLE `jeecg_boot`.`qrtz_simple_triggers` COMMENT = 'InnoDB free: 504832 kB; (`SCHED_NAME` `TRIGGER_NAME` `TRIGGE';
ALTER TABLE `jeecg_boot`.`qrtz_simprop_triggers` COMMENT = 'InnoDB free: 504832 kB; (`SCHED_NAME` `TRIGGER_NAME` `TRIGGE';
ALTER TABLE `jeecg_boot`.`qrtz_triggers` COMMENT = 'InnoDB free: 504832 kB; (`SCHED_NAME` `JOB_NAME` `JOB_GROUP`';
ALTER TABLE `jeecg_boot`.`sys_announcement` MODIFY COLUMN `del_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除状态（0，正常，1已删除）' AFTER `cancel_time`;

ALTER TABLE `jeecg_boot`.`sys_announcement` MODIFY COLUMN `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人' AFTER `del_flag`;

ALTER TABLE `jeecg_boot`.`sys_announcement` MODIFY COLUMN `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人' AFTER `create_time`;
ALTER TABLE `jeecg_boot`.`sys_announcement_send` MODIFY COLUMN `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人' AFTER `read_time`;

ALTER TABLE `jeecg_boot`.`sys_announcement_send` MODIFY COLUMN `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人' AFTER `create_time`;
ALTER TABLE `jeecg_boot`.`sys_depart` MODIFY COLUMN `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态（1启用，0不启用）' AFTER `memo`;

ALTER TABLE `jeecg_boot`.`sys_depart` MODIFY COLUMN `del_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除状态（0，正常，1已删除）' AFTER `status`;

ALTER TABLE `jeecg_boot`.`sys_depart` MODIFY COLUMN `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人' AFTER `del_flag`;

ALTER TABLE `jeecg_boot`.`sys_depart` MODIFY COLUMN `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人' AFTER `create_time`;

ALTER TABLE `jeecg_boot`.`sys_depart` ADD INDEX `index_depart_parent_id`(`parent_id`) USING BTREE;

ALTER TABLE `jeecg_boot`.`sys_depart` ADD INDEX `index_depart_depart_order`(`depart_order`) USING BTREE;

ALTER TABLE `jeecg_boot`.`sys_depart` ADD INDEX `index_depart_org_code`(`org_code`) USING BTREE;
ALTER TABLE `jeecg_boot`.`sys_dict` COMMENT = '';

ALTER TABLE `jeecg_boot`.`sys_dict` MODIFY COLUMN `dict_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典名称' AFTER `id`;

ALTER TABLE `jeecg_boot`.`sys_dict` MODIFY COLUMN `dict_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典编码' AFTER `dict_name`;

ALTER TABLE `jeecg_boot`.`sys_dict` MODIFY COLUMN `del_flag` int(1) NULL DEFAULT NULL COMMENT '删除状态' AFTER `description`;

ALTER TABLE `jeecg_boot`.`sys_dict` MODIFY COLUMN `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人' AFTER `del_flag`;

ALTER TABLE `jeecg_boot`.`sys_dict` MODIFY COLUMN `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人' AFTER `create_time`;

ALTER TABLE `jeecg_boot`.`sys_dict` ADD COLUMN `type` int(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '字典类型0为string,1为number' AFTER `update_time`;

ALTER TABLE `jeecg_boot`.`sys_dict` ADD UNIQUE INDEX `indextable_dict_code`(`dict_code`) USING BTREE;
ALTER TABLE `jeecg_boot`.`sys_dict_item` MODIFY COLUMN `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL FIRST;

ALTER TABLE `jeecg_boot`.`sys_dict_item` MODIFY COLUMN `dict_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典id' AFTER `id`;

ALTER TABLE `jeecg_boot`.`sys_dict_item` MODIFY COLUMN `item_text` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典项文本' AFTER `dict_id`;

ALTER TABLE `jeecg_boot`.`sys_dict_item` MODIFY COLUMN `item_value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典项值' AFTER `item_text`;

ALTER TABLE `jeecg_boot`.`sys_dict_item` MODIFY COLUMN `sort_order` int(10) NULL DEFAULT NULL COMMENT '排序' AFTER `description`;

ALTER TABLE `jeecg_boot`.`sys_dict_item` MODIFY COLUMN `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `status`;

ALTER TABLE `jeecg_boot`.`sys_dict_item` MODIFY COLUMN `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `create_time`;

ALTER TABLE `jeecg_boot`.`sys_dict_item` ADD INDEX `index_table_dict_id`(`dict_id`) USING BTREE;

ALTER TABLE `jeecg_boot`.`sys_dict_item` ADD INDEX `index_table_sort_order`(`sort_order`) USING BTREE;

ALTER TABLE `jeecg_boot`.`sys_dict_item` ADD INDEX `index_table_dict_status`(`status`) USING BTREE;
ALTER TABLE `jeecg_boot`.`sys_log` ENGINE = MyISAM;

ALTER TABLE `jeecg_boot`.`sys_log` MODIFY COLUMN `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL FIRST;

ALTER TABLE `jeecg_boot`.`sys_log` MODIFY COLUMN `log_type` int(2) NULL DEFAULT NULL COMMENT '日志类型（1登录日志，2操作日志）' AFTER `id`;

ALTER TABLE `jeecg_boot`.`sys_log` MODIFY COLUMN `operate_type` int(2) NULL DEFAULT NULL COMMENT '操作类型' AFTER `log_content`;

ALTER TABLE `jeecg_boot`.`sys_log` MODIFY COLUMN `userid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作用户账号' AFTER `operate_type`;

ALTER TABLE `jeecg_boot`.`sys_log` MODIFY COLUMN `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作用户名称' AFTER `userid`;

ALTER TABLE `jeecg_boot`.`sys_log` MODIFY COLUMN `ip` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP' AFTER `username`;

ALTER TABLE `jeecg_boot`.`sys_log` MODIFY COLUMN `request_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求类型' AFTER `request_param`;

ALTER TABLE `jeecg_boot`.`sys_log` MODIFY COLUMN `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人' AFTER `cost_time`;

ALTER TABLE `jeecg_boot`.`sys_log` MODIFY COLUMN `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人' AFTER `create_time`;

ALTER TABLE `jeecg_boot`.`sys_log` ADD INDEX `index_table_userid`(`userid`) USING BTREE;

ALTER TABLE `jeecg_boot`.`sys_log` ADD INDEX `index_logt_ype`(`log_type`) USING BTREE;

ALTER TABLE `jeecg_boot`.`sys_log` ADD INDEX `index_operate_type`(`operate_type`) USING BTREE;
ALTER TABLE `jeecg_boot`.`sys_permission` MODIFY COLUMN `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单标题' AFTER `parent_id`;

ALTER TABLE `jeecg_boot`.`sys_permission` ADD COLUMN `component_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件名字' AFTER `component`;

ALTER TABLE `jeecg_boot`.`sys_permission` MODIFY COLUMN `sort_no` double(3, 2) NULL DEFAULT NULL COMMENT '菜单排序' AFTER `perms`;

ALTER TABLE `jeecg_boot`.`sys_permission` MODIFY COLUMN `always_show` tinyint(1) NULL DEFAULT NULL COMMENT '聚合子路由: 1是0否' AFTER `sort_no`;

ALTER TABLE `jeecg_boot`.`sys_permission` MODIFY COLUMN `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标' AFTER `always_show`;

ALTER TABLE `jeecg_boot`.`sys_permission` ADD COLUMN `is_route` tinyint(1) NULL DEFAULT 1 COMMENT '是否路由菜单: 0:不是  1:是（默认值1）' AFTER `icon`;

ALTER TABLE `jeecg_boot`.`sys_permission` MODIFY COLUMN `is_leaf` tinyint(1) NULL DEFAULT NULL COMMENT '是否叶子节点:    1:是   0:不是' AFTER `is_route`;

ALTER TABLE `jeecg_boot`.`sys_permission` MODIFY COLUMN `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人' AFTER `description`;

ALTER TABLE `jeecg_boot`.`sys_permission` MODIFY COLUMN `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人' AFTER `create_time`;

ALTER TABLE `jeecg_boot`.`sys_permission` MODIFY COLUMN `del_flag` int(1) NULL DEFAULT 0 COMMENT '删除状态 0正常 1已删除' AFTER `update_time`;

ALTER TABLE `jeecg_boot`.`sys_permission` ADD INDEX `index_prem_pid`(`parent_id`) USING BTREE;

ALTER TABLE `jeecg_boot`.`sys_permission` ADD INDEX `index_prem_is_route`(`is_route`) USING BTREE;

ALTER TABLE `jeecg_boot`.`sys_permission` ADD INDEX `index_prem_is_leaf`(`is_leaf`) USING BTREE;

ALTER TABLE `jeecg_boot`.`sys_permission` ADD INDEX `index_prem_sort_no`(`sort_no`) USING BTREE;

ALTER TABLE `jeecg_boot`.`sys_permission` ADD INDEX `index_prem_del_flag`(`del_flag`) USING BTREE;
ALTER TABLE `jeecg_boot`.`sys_quartz_job` ENGINE = MyISAM;

ALTER TABLE `jeecg_boot`.`sys_quartz_job` MODIFY COLUMN `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL FIRST;

ALTER TABLE `jeecg_boot`.`sys_quartz_job` MODIFY COLUMN `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人' AFTER `id`;

ALTER TABLE `jeecg_boot`.`sys_quartz_job` MODIFY COLUMN `del_flag` int(1) NULL DEFAULT NULL COMMENT '删除状态' AFTER `create_time`;

ALTER TABLE `jeecg_boot`.`sys_quartz_job` MODIFY COLUMN `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人' AFTER `del_flag`;

ALTER TABLE `jeecg_boot`.`sys_quartz_job` MODIFY COLUMN `status` int(1) NULL DEFAULT NULL COMMENT '状态 0正常 -1停止' AFTER `description`;
ALTER TABLE `jeecg_boot`.`sys_role` MODIFY COLUMN `role_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称' AFTER `id`;

ALTER TABLE `jeecg_boot`.`sys_role` MODIFY COLUMN `role_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色编码' AFTER `role_name`;

ALTER TABLE `jeecg_boot`.`sys_role` MODIFY COLUMN `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人' AFTER `description`;

ALTER TABLE `jeecg_boot`.`sys_role` MODIFY COLUMN `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人' AFTER `create_time`;

ALTER TABLE `jeecg_boot`.`sys_role` ADD UNIQUE INDEX `index_role_code`(`role_code`) USING BTREE;
ALTER TABLE `jeecg_boot`.`sys_role_permission` ADD COLUMN `data_rule_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `permission_id`;

ALTER TABLE `jeecg_boot`.`sys_role_permission` ADD INDEX `index_group_role_per_id`(`role_id`, `permission_id`) USING BTREE;

ALTER TABLE `jeecg_boot`.`sys_role_permission` ADD INDEX `index_group_role_id`(`role_id`) USING BTREE;

ALTER TABLE `jeecg_boot`.`sys_role_permission` ADD INDEX `index_group_per_id`(`permission_id`) USING BTREE;
ALTER TABLE `jeecg_boot`.`sys_user` MODIFY COLUMN `realname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名' AFTER `username`;

ALTER TABLE `jeecg_boot`.`sys_user` MODIFY COLUMN `status` int(2) NULL DEFAULT NULL COMMENT '状态(1：正常  2：冻结 ）' AFTER `phone`;

ALTER TABLE `jeecg_boot`.`sys_user` MODIFY COLUMN `del_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除状态（0，正常，1已删除）' AFTER `status`;

ALTER TABLE `jeecg_boot`.`sys_user` MODIFY COLUMN `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人' AFTER `del_flag`;

ALTER TABLE `jeecg_boot`.`sys_user` MODIFY COLUMN `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人' AFTER `create_time`;

ALTER TABLE `jeecg_boot`.`sys_user` ADD UNIQUE INDEX `index_user_name`(`username`) USING BTREE;

ALTER TABLE `jeecg_boot`.`sys_user` ADD INDEX `index_user_status`(`status`) USING BTREE;

ALTER TABLE `jeecg_boot`.`sys_user` ADD INDEX `index_user_del_flag`(`del_flag`) USING BTREE;
ALTER TABLE `jeecg_boot`.`sys_user_depart` DROP INDEX `userid`;

ALTER TABLE `jeecg_boot`.`sys_user_depart` DROP INDEX `orgid`;

ALTER TABLE `jeecg_boot`.`sys_user_depart` ADD INDEX `index_depart_groupk_userid`(`user_id`) USING BTREE;

ALTER TABLE `jeecg_boot`.`sys_user_depart` ADD INDEX `index_depart_groupkorgid`(`dep_id`) USING BTREE;

ALTER TABLE `jeecg_boot`.`sys_user_depart` ADD INDEX `index_depart_groupk_uidanddid`(`user_id`, `dep_id`) USING BTREE;

ALTER TABLE `jeecg_boot`.`sys_user_role` ADD INDEX `index2_groupuu_user_id`(`user_id`) USING BTREE;

ALTER TABLE `jeecg_boot`.`sys_user_role` ADD INDEX `index2_groupuu_ole_id`(`role_id`) USING BTREE;

ALTER TABLE `jeecg_boot`.`sys_user_role` ADD INDEX `index2_groupuu_useridandroleid`(`user_id`, `role_id`) USING BTREE;

CREATE TABLE `jeecg_boot`.`onl_cgreport_head`  (
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
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
CREATE TABLE `jeecg_boot`.`onl_cgreport_item`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `cgrhead_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '报表ID',
  `field_name` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字段名字',
  `field_txt` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段文本',
  `field_width` int(3) NULL DEFAULT NULL,
  `field_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段类型',
  `search_mode` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '查询模式',
  `is_order` int(2) NULL DEFAULT 0 COMMENT '是否排序  0否,1是',
  `is_search` int(2) NULL DEFAULT 0 COMMENT '是否查询  0否,1是',
  `dict_code` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典CODE',
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
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
CREATE TABLE `jeecg_boot`.`onl_cgreport_param`  (
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
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
CREATE TABLE `jeecg_boot`.`sys_data_log`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `data_table` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表名',
  `data_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据ID',
  `data_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '数据内容',
  `data_version` int(11) NULL DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sindex`(`data_table`, `data_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
CREATE TABLE `jeecg_boot`.`sys_permission_data_rule`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
  `permission_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单ID',
  `rule_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则名称',
  `rule_column` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段',
  `rule_conditions` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '条件',
  `rule_value` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则值',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_fucntionid`(`permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
CREATE TABLE `jeecg_boot`.`sys_sms`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
  `es_title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息标题',
  `es_type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送方式：1短信 2邮件 3微信',
  `es_receiver` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接收人',
  `es_param` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送所需参数Json格式',
  `es_content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '推送内容',
  `es_send_time` datetime(0) NULL DEFAULT NULL COMMENT '推送时间',
  `es_send_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送状态 0未推送 1推送成功 2推送失败 -1失败不再发送',
  `es_send_num` int(11) NULL DEFAULT NULL COMMENT '发送次数 超过5次不再发送',
  `es_result` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送失败原因',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_type`(`es_type`) USING BTREE,
  INDEX `index_receiver`(`es_receiver`) USING BTREE,
  INDEX `index_sendtime`(`es_send_time`) USING BTREE,
  INDEX `index_status`(`es_send_status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
CREATE TABLE `jeecg_boot`.`sys_sms_template`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `template_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板标题',
  `template_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板CODE',
  `template_type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板类型：1短信 2邮件 3微信',
  `template_content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板内容',
  `template_test_json` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板测试json',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人登录名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人登录名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_templatecode`(`template_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO `jeecg_boot`.`demo`(`id`, `name`, `key_word`, `punch_time`, `salary_money`, `bonus_money`, `sex`, `age`, `birthday`, `email`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('08375a2dff80e821d5a158dd98302b23', '导入小虎', NULL, NULL, NULL, NULL, '2', 28, NULL, NULL, NULL, 'jeecg-boot', '2019-04-10 11:42:57', NULL, NULL);

INSERT INTO `jeecg_boot`.`demo`(`id`, `name`, `key_word`, `punch_time`, `salary_money`, `bonus_money`, `sex`, `age`, `birthday`, `email`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4981637bf71b0c1ed1365241dfcfa0ea', '小虎', NULL, NULL, NULL, NULL, '2', 28, NULL, NULL, NULL, 'scott', '2019-01-19 13:12:53', 'qinfeng', '2019-01-19 13:13:12');

INSERT INTO `jeecg_boot`.`demo`(`id`, `name`, `key_word`, `punch_time`, `salary_money`, `bonus_money`, `sex`, `age`, `birthday`, `email`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c16e6a5c31296bcd3f1053d5d118815', '导入zhangdaiscott', NULL, NULL, NULL, NULL, '1', NULL, '2019-01-03', NULL, NULL, 'jeecg-boot', '2019-04-10 11:42:57', NULL, NULL);

INSERT INTO `jeecg_boot`.`demo`(`id`, `name`, `key_word`, `punch_time`, `salary_money`, `bonus_money`, `sex`, `age`, `birthday`, `email`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('73bc58611012617ca446d8999379e4ac', '郭靖11a', '777', '2018-12-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 18:16:39', NULL, NULL);

INSERT INTO `jeecg_boot`.`demo`(`id`, `name`, `key_word`, `punch_time`, `salary_money`, `bonus_money`, `sex`, `age`, `birthday`, `email`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('95740656751c5f22e5932ab0ae33b1e4', '杨康22a', '奸臣', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 18:16:39', NULL, NULL);

INSERT INTO `jeecg_boot`.`demo`(`id`, `name`, `key_word`, `punch_time`, `salary_money`, `bonus_money`, `sex`, `age`, `birthday`, `email`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c28fa8391ef81d6fabd8bd894a7615aa', '小麦', NULL, NULL, NULL, NULL, '2', NULL, NULL, 'zhangdaiscott@163.com', NULL, 'jeecg-boot', '2019-04-04 17:18:09', NULL, NULL);

INSERT INTO `jeecg_boot`.`demo`(`id`, `name`, `key_word`, `punch_time`, `salary_money`, `bonus_money`, `sex`, `age`, `birthday`, `email`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c96279c666b4b82e3ef1e4e2978701ce', '报名时间', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 18:00:52', NULL, NULL);

UPDATE `jeecg_boot`.`demo` SET `name` = 'Sandy', `key_word` = '开源，很好', `punch_time` = '2018-12-15 00:00:00', `salary_money` = NULL, `bonus_money` = NULL, `sex` = '2', `age` = 21, `birthday` = '2018-12-15', `email` = 'test4@baomidou.com', `content` = '聪明00', `create_by` = NULL, `create_time` = NULL, `update_by` = 'admin', `update_time` = '2019-02-25 16:29:27' WHERE `id` = '4';

UPDATE `jeecg_boot`.`demo` SET `name` = 'zhang daihao', `key_word` = NULL, `punch_time` = NULL, `salary_money` = NULL, `bonus_money` = NULL, `sex` = '2', `age` = NULL, `birthday` = NULL, `email` = 'zhangdaiscott@163.com', `content` = NULL, `create_by` = 'admin', `create_time` = '2019-01-19 23:37:18', `update_by` = 'admin', `update_time` = '2019-01-21 16:49:06' WHERE `id` = 'c2c0d49e3c01913067cf8d1fb3c971d2';

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15538561502720', '3333', '1', '', NULL, '', '0d4a2e67b538ee1bc881e5ed34f670f0', 'jeecg-boot', '2019-03-29 18:42:55', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15538561512681', '3332333', '2', '', NULL, '', '0d4a2e67b538ee1bc881e5ed34f670f0', 'jeecg-boot', '2019-03-29 18:42:55', 'admin', '2019-03-29 18:43:12');

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15538561550142', '4442', '2', '', NULL, '', '0d4a2e67b538ee1bc881e5ed34f670f0', 'jeecg-boot', '2019-03-29 18:42:55', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15541168497342', '444', '', '', '', '', 'f71f7f8930b5b6b1703d9948d189982b', 'admin', '2019-04-01 19:08:45', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15541168499553', '5555', '', '', '', '', 'f71f7f8930b5b6b1703d9948d189982b', 'admin', '2019-04-01 19:08:45', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15541169272690', '小王1', '1', '', '', '18611788525', 'f618a85b17e2c4dd58d268220c8dd9a1', 'admin', '2019-04-01 19:10:07', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15541169288141', '效力1', '1', '', '', '18611788525', 'f618a85b17e2c4dd58d268220c8dd9a1', 'admin', '2019-04-01 19:10:07', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15541169441372', '小红1', '1', '', '', '18611788525', 'f618a85b17e2c4dd58d268220c8dd9a1', 'admin', '2019-04-01 19:10:07', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15543695362380', '1111', '', '', '', '', '5d6e2b9e44037526270b6206196f6689', 'admin', '2019-04-04 17:19:40', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15543695397221', '222', '', '', '', '', '5d6e2b9e44037526270b6206196f6689', 'admin', '2019-04-04 17:19:40', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15543695398992', '333', '', '', '', '', '5d6e2b9e44037526270b6206196f6689', 'admin', '2019-04-04 17:19:40', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('18dc5eb1068ccdfe90e358951ca1a3d6', 'dr2', '', '', '', '', '8ab1186410a65118c4d746eb085d3bed', 'admin', '2019-04-04 17:25:33', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('195d280490fe88ca1475512ddcaf2af9', '12', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('217a2bf83709775d2cd85bf598392327', '2', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('22bc052ae53ed09913b946abba93fa89', '1', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('23bafeae88126c3bf3322a29a04f0d5e', 'x秦风', NULL, NULL, NULL, NULL, '163e2efcbc6d7d54eb3f8a137da8a75a', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('25c4a552c6843f36fad6303bfa99a382', '1', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2d32144e2bee63264f3f16215c258381', '33333', '2', NULL, NULL, NULL, 'd908bfee3377e946e59220c4a4eb414a', 'admin', '2019-04-01 16:27:03', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2d43170d6327f941bd1a017999495e25', '1', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2e5f62a8b6e0a0ce19b52a6feae23d48', '3', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('313abf99558ac5f13ecca3b87e562ad1', 'scott', '2', NULL, NULL, NULL, 'b190737bd04cca8360e6f87c9ef9ec4e', 'admin', '2019-02-25 16:29:48', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('34a1c5cf6cee360ed610ed0bed70e0f9', '导入秦风', NULL, NULL, NULL, NULL, 'a2cce75872cc8fcc47f78de9ffd378c2', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3c87400f8109b4cf43c5598f0d40e34d', '2', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('40964bcbbecb38e5ac15e6d08cf3cd43', '233', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('41e3dee0b0b6e6530eccb7fbb22fd7a3', '4555', '1', '370285198602058823', NULL, '18611788674', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4808ae8344c7679a4a2f461db5dc3a70', '44', '1', '370285198602058823', NULL, '18611788674', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4b6cef12f195fad94d57279b2241770d', 'dr12', '', '', '', '', '8ab1186410a65118c4d746eb085d3bed', 'admin', '2019-04-04 17:25:33', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('524e695283f8e8c256cc24f39d6d8542', '小王', '2', '370285198604033222', NULL, '18611788674', 'eb13ab35d2946a2b0cfe3452bca1e73f', 'admin', '2019-02-25 16:29:41', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('57c2a8367db34016114cbc9fa368dba0', '2', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5df36a1608b8c7ac99ad9bc408fe54bf', '4', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6b694e9ba54bb289ae9cc499e40031e7', 'x秦风', '1', NULL, NULL, NULL, 'b190737bd04cca8360e6f87c9ef9ec4e', 'admin', '2019-02-25 16:29:48', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6c6fd2716c2dcd044ed03c2c95d261f8', '李四', '2', '370285198602058833', '', '18611788676', 'f71f7f8930b5b6b1703d9948d189982b', 'admin', '2019-04-01 19:08:45', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('742d008214dee0afff2145555692973e', '秦风', '1', '370285198602058822', NULL, '18611788676', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7469c3e5d371767ff90a739d297689b5', '导入秦风', '2', NULL, NULL, NULL, '3a867ebf2cebce9bae3f79676d8d86f3', 'jeecg-boot', '2019-03-29 18:43:59', 'admin', '2019-04-08 17:35:02');

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7a96e2c7b24847d4a29940dbc0eda6e5', 'drscott', NULL, NULL, NULL, NULL, 'e73434dad84ebdce2d4e0c2a2f06d8ea', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7f5a40818e225ee18bda6da7932ac5f9', '2', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8011575abfd7c8085e71ff66df1124b9', '1', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8404f31d7196221a573c9bd6c8f15003', '小张', '1', '370285198602058211', NULL, '18611788676', 'eb13ab35d2946a2b0cfe3452bca1e73f', 'admin', '2019-02-25 16:29:41', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('859020e10a2f721f201cdbff78cf7b9f', 'scott', NULL, NULL, NULL, NULL, '163e2efcbc6d7d54eb3f8a137da8a75a', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8cc3c4d26e3060975df3a2adb781eeb4', 'dr33', NULL, NULL, NULL, NULL, 'b2feb454e43c46b2038768899061e464', 'jeecg-boot', '2019-04-04 17:23:09', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8d1725c23a6a50685ff0dedfd437030d', '4', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('933cae3a79f60a93922d59aace5346ce', '小王', NULL, '370285198604033222', NULL, '18611788674', '6a719071a29927a14f19482f8693d69a', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9bdb5400b709ba4eaf3444de475880d7', 'dr22', NULL, NULL, NULL, NULL, '22c17790dcd04b296c4a2a089f71895f', 'jeecg-boot', '2019-04-04 17:23:09', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9f87677f70e5f864679314389443a3eb', '33', '2', '370285198602058823', NULL, '18611788674', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a2c2b7101f75c02deb328ba777137897', '44', '2', '370285198602058823', NULL, '18611788674', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ab4d002dc552c326147e318c87d3bed4', 'ddddd', '1', '370285198604033222', NULL, '18611755848', '9a57c850e4f68cf94ef7d8585dbaf7e6', 'admin', '2019-04-04 17:30:47', 'admin', '2019-04-04 17:31:17');

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ad116f722a438e5f23095a0b5fcc8e89', 'dr秦风', NULL, NULL, NULL, NULL, 'e73434dad84ebdce2d4e0c2a2f06d8ea', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b1ba147b75f5eaa48212586097fc3fd1', '2', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b43bf432c251f0e6b206e403b8ec29bc', 'lisi', NULL, NULL, NULL, NULL, 'f8889aaef6d1bccffd98d2889c0aafb5', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('bcdd300a7d44c45a66bdaac14903c801', '33', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('beb983293e47e2dc1a9b3d649aa3eb34', 'ddd3', NULL, NULL, NULL, NULL, 'd908bfee3377e946e59220c4a4eb414a', 'admin', '2019-04-01 16:27:03', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c219808196406f1b8c7f1062589de4b5', '44', '1', '370285198602058823', NULL, '18611788674', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c8ed061d4b27c0c7a64e100f2b1c8ab5', '张经理', '2', '370285198602058823', NULL, '18611788674', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('cc5de4af7f06cd6d250965ebe92a0395', '1', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('cf8817bd703bf7c7c77a2118edc26cc7', '1', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d72b26fae42e71270fce2097a88da58a', '导入scott', NULL, 'www', NULL, NULL, '3a867ebf2cebce9bae3f79676d8d86f3', 'jeecg-boot', '2019-03-29 18:43:59', 'admin', '2019-04-08 17:35:05');

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('dbdc60a6ac1a8c43f24afee384039b68', 'xiaowang', NULL, NULL, NULL, NULL, 'f8889aaef6d1bccffd98d2889c0aafb5', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('dc5883b50466de94d900919ed96d97af', '33', '1', '370285198602058823', NULL, '18611788674', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('deeb73e553ad8dc0a0b3cfd5a338de8e', '3333', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e2570278bf189ac05df3673231326f47', '1', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e39cb23bb950b2bdedfc284686c6128a', '1', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e46fe9111a9100844af582a18a2aa402', '1', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ee7af0acb9beb9bf8d8b3819a8a7fdc3', '2', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f5d2605e844192d9e548f9bd240ac908', '小张', NULL, '370285198602058211', NULL, '18611788676', '6a719071a29927a14f19482f8693d69a', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_customer`(`id`, `name`, `sex`, `idcard`, `idcard_pic`, `telphone`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f6db6547382126613a3e46e7cd58a5f2', '导入scott', NULL, NULL, NULL, NULL, 'a2cce75872cc8fcc47f78de9ffd378c2', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_main`(`id`, `order_code`, `ctype`, `order_date`, `order_money`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('163e2efcbc6d7d54eb3f8a137da8a75a', 'B100', NULL, NULL, 3000.000, NULL, 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_main`(`id`, `order_code`, `ctype`, `order_date`, `order_money`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3a867ebf2cebce9bae3f79676d8d86f3', '导入B100', '2222', NULL, 3000.000, NULL, 'jeecg-boot', '2019-03-29 18:43:59', 'admin', '2019-04-08 17:35:13');

INSERT INTO `jeecg_boot`.`jeecg_order_main`(`id`, `order_code`, `ctype`, `order_date`, `order_money`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4bca3ea6881d39dbf67ef1e42c649766', '1212', NULL, NULL, NULL, NULL, 'admin', '2019-04-03 10:55:43', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_main`(`id`, `order_code`, `ctype`, `order_date`, `order_money`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4cba137333127e8e31df7ad168cc3732', '青岛订单A0001', '2', '2019-04-03 10:56:07', NULL, NULL, 'admin', '2019-04-03 10:56:11', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_main`(`id`, `order_code`, `ctype`, `order_date`, `order_money`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5d6e2b9e44037526270b6206196f6689', 'N333', NULL, '2019-04-04 17:19:11', NULL, '聪明00', 'admin', '2019-04-04 17:19:40', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_main`(`id`, `order_code`, `ctype`, `order_date`, `order_money`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6a719071a29927a14f19482f8693d69a', 'c100', NULL, NULL, 5000.000, NULL, 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_main`(`id`, `order_code`, `ctype`, `order_date`, `order_money`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8ab1186410a65118c4d746eb085d3bed', '导入400', '1', '2019-02-18 09:58:51', 40.000, NULL, 'admin', '2019-02-18 09:58:47', 'admin', '2019-02-18 09:58:59');

INSERT INTO `jeecg_boot`.`jeecg_order_main`(`id`, `order_code`, `ctype`, `order_date`, `order_money`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9a57c850e4f68cf94ef7d8585dbaf7e6', 'halou100dd', NULL, '2019-04-04 17:30:32', NULL, NULL, 'admin', '2019-04-04 17:30:41', 'admin', '2019-04-04 17:31:08');

INSERT INTO `jeecg_boot`.`jeecg_order_main`(`id`, `order_code`, `ctype`, `order_date`, `order_money`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a2cce75872cc8fcc47f78de9ffd378c2', '导入B100', NULL, NULL, 3000.000, NULL, 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_main`(`id`, `order_code`, `ctype`, `order_date`, `order_money`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d908bfee3377e946e59220c4a4eb414a', 'SSSS001', NULL, NULL, 599.000, NULL, 'admin', '2019-04-01 15:43:03', 'admin', '2019-04-01 16:26:52');

INSERT INTO `jeecg_boot`.`jeecg_order_main`(`id`, `order_code`, `ctype`, `order_date`, `order_money`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e73434dad84ebdce2d4e0c2a2f06d8ea', '导入200', NULL, NULL, 3000.000, NULL, 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_main`(`id`, `order_code`, `ctype`, `order_date`, `order_money`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f618a85b17e2c4dd58d268220c8dd9a1', 'N001', NULL, '2019-04-01 19:09:02', 2222.000, NULL, 'admin', '2019-04-01 19:09:47', 'admin', '2019-04-01 19:10:00');

INSERT INTO `jeecg_boot`.`jeecg_order_main`(`id`, `order_code`, `ctype`, `order_date`, `order_money`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f71f7f8930b5b6b1703d9948d189982b', 'BY911', NULL, '2019-04-06 19:08:39', NULL, NULL, 'admin', '2019-04-01 16:36:02', 'admin', '2019-04-01 16:36:08');

INSERT INTO `jeecg_boot`.`jeecg_order_main`(`id`, `order_code`, `ctype`, `order_date`, `order_money`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f8889aaef6d1bccffd98d2889c0aafb5', 'A100', NULL, '2018-10-10 00:00:00', 6000.000, NULL, 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_main`(`id`, `order_code`, `ctype`, `order_date`, `order_money`, `content`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('fe81ee5d19bbf9eef2066d4f29dfbe0f', 'uuuu', NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 11:00:39', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0f0e3a40a215958f807eea08a6e1ac0a', '88', NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0fa3bd0bbcf53650c0bb3c0cac6d8cb7', 'ffff', '2019-02-21 00:00:00', 'eb13ab35d2946a2b0cfe3452bca1e73f', 'admin', '2019-02-25 16:29:41', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('14221afb4f5f749c1deef26ac56fdac3', '33', '2019-03-09 00:00:00', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15538561502730', '222', NULL, '0d4a2e67b538ee1bc881e5ed34f670f0', 'jeecg-boot', '2019-03-29 18:42:55', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15538561526461', '2244', '2019-03-29 00:00:00', '0d4a2e67b538ee1bc881e5ed34f670f0', 'jeecg-boot', '2019-03-29 18:42:55', 'admin', '2019-03-29 18:43:26');

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15541168478913', 'hhhhh', NULL, 'f71f7f8930b5b6b1703d9948d189982b', 'admin', '2019-04-01 19:08:45', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15541169272810', '22211', '2019-04-01 19:09:40', 'f618a85b17e2c4dd58d268220c8dd9a1', 'admin', '2019-04-01 19:10:07', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15541169302331', '333311', '2019-04-01 19:09:40', 'f618a85b17e2c4dd58d268220c8dd9a1', 'admin', '2019-04-01 19:10:07', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15541169713092', '333311', '2019-04-01 19:09:47', 'f618a85b17e2c4dd58d268220c8dd9a1', 'admin', '2019-04-01 19:10:07', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15542604293170', 'c', NULL, 'fe81ee5d19bbf9eef2066d4f29dfbe0f', 'jeecg-boot', '2019-04-03 11:00:39', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15542604374431', 'd', NULL, 'fe81ee5d19bbf9eef2066d4f29dfbe0f', 'jeecg-boot', '2019-04-03 11:00:39', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15543695362380', 'ccc2', NULL, '5d6e2b9e44037526270b6206196f6689', 'admin', '2019-04-04 17:19:40', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15543695381291', 'cccc1', NULL, '5d6e2b9e44037526270b6206196f6689', 'admin', '2019-04-04 17:19:40', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15543695740352', 'dddd', NULL, '5d6e2b9e44037526270b6206196f6689', 'admin', '2019-04-04 17:19:40', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('18905bc89ee3851805aab38ed3b505ec', '44', NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1f809cbd26f4e574697e1c10de575d72', 'A100', NULL, 'e73434dad84ebdce2d4e0c2a2f06d8ea', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('21051adb51529bdaa8798b5a3dd7f7f7', 'C10029', '2019-02-20 00:00:00', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('269576e766b917f8b6509a2bb0c4d4bd', 'A100', NULL, '163e2efcbc6d7d54eb3f8a137da8a75a', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2d473ffc79e5b38a17919e15f8b7078e', '66', '2019-03-29 00:00:00', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3655b66fca5fef9c6aac6d70182ffda2', 'AA123', '2019-04-01 00:00:00', 'd908bfee3377e946e59220c4a4eb414a', 'admin', '2019-04-01 16:27:03', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('365d5919155473ade45840fd626c51a9', 'dddd', '2019-04-04 17:25:29', '8ab1186410a65118c4d746eb085d3bed', 'admin', '2019-04-04 17:25:33', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4889a782e78706ab4306a925cfb163a5', 'C34', '2019-04-01 00:00:00', 'd908bfee3377e946e59220c4a4eb414a', 'admin', '2019-04-01 16:35:00', 'admin', '2019-04-01 16:35:07');

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('48d385796382cf87fa4bdf13b42d9a28', '导入A100', NULL, '3a867ebf2cebce9bae3f79676d8d86f3', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('541faed56efbeb4be9df581bd8264d3a', '88', NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('57a27a7dfd6a48e7d981f300c181b355', '6', '2019-03-30 00:00:00', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5ce4dc439c874266e42e6c0ff8dc8b5c', '导入A100', NULL, 'a2cce75872cc8fcc47f78de9ffd378c2', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5f16e6a64ab22a161bd94cc205f2c662', '222', '2019-02-23 00:00:00', 'b190737bd04cca8360e6f87c9ef9ec4e', 'admin', '2019-02-25 16:29:48', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('645a06152998a576c051474157625c41', '88', '2019-04-04 17:25:31', '8ab1186410a65118c4d746eb085d3bed', 'admin', '2019-04-04 17:25:33', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6e3562f2571ea9e96b2d24497b5f5eec', '55', '2019-03-23 00:00:00', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8fd2b389151568738b1cc4d8e27a6110', '导入A100', NULL, 'a2cce75872cc8fcc47f78de9ffd378c2', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('93f1a84053e546f59137432ff5564cac', '55', NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('969ddc5d2e198d50903686917f996470', 'A10029', '2019-04-01 00:00:00', 'f71f7f8930b5b6b1703d9948d189982b', 'admin', '2019-04-01 19:08:45', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('96e7303a8d22a5c384e08d7bcf7ac2bf', 'A100', NULL, 'e73434dad84ebdce2d4e0c2a2f06d8ea', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9e8a3336f6c63f558f2b68ce2e1e666e', 'dddd', NULL, '9a57c850e4f68cf94ef7d8585dbaf7e6', 'admin', '2019-04-04 17:30:55', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a28db02c810c65660015095cb81ed434', 'A100', NULL, 'f8889aaef6d1bccffd98d2889c0aafb5', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b217bb0e4ec6a45b6cbf6db880060c0f', 'A100', NULL, '6a719071a29927a14f19482f8693d69a', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ba708df70bb2652ed1051a394cfa0bb3', '333', NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('beabbfcb195d39bedeeafe8318794562', 'A1345', '2019-04-01 00:00:00', 'd908bfee3377e946e59220c4a4eb414a', 'admin', '2019-04-01 16:27:04', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('bf450223cb505f89078a311ef7b6ed16', '777', '2019-03-30 00:00:00', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c06165b6603e3e1335db187b3c841eef', 'fff', NULL, '9a57c850e4f68cf94ef7d8585dbaf7e6', 'admin', '2019-04-04 17:30:58', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c113136abc26ace3a6da4e41d7dc1c7e', '44', '2019-03-15 00:00:00', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c1abdc2e30aeb25de13ad6ee3488ac24', '77', '2019-03-22 00:00:00', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c23751a7deb44f553ce50a94948c042a', '33', '2019-03-09 00:00:00', '8ab1186410a65118c4d746eb085d3bed', 'admin', '2019-04-04 17:25:33', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c64547666b634b3d6a0feedcf05f25ce', 'C10019', '2019-04-01 00:00:00', 'f71f7f8930b5b6b1703d9948d189982b', 'admin', '2019-04-01 19:08:45', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c8b8d3217f37da78dddf711a1f7da485', 'A100', NULL, '163e2efcbc6d7d54eb3f8a137da8a75a', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('cab691c1c1ff7a6dfd7248421917fd3c', 'A100', NULL, 'f8889aaef6d1bccffd98d2889c0aafb5', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('cca10a9a850b456d9b72be87da7b0883', '77', NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d2fbba11f4814d9b1d3cb1a3f342234a', 'C10019', '2019-02-18 00:00:00', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d746c1ed956a562e97eef9c6faf94efa', '111', '2019-02-01 00:00:00', 'b190737bd04cca8360e6f87c9ef9ec4e', 'admin', '2019-02-25 16:29:48', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('dbdb07a16826808e4276e84b2aa4731a', '导入A100', NULL, '3a867ebf2cebce9bae3f79676d8d86f3', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e7075639c37513afc0bbc4bf7b5d98b9', '88', NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('fa759dc104d0371f8aa28665b323dab6', '888', NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`jeecg_order_ticket`(`id`, `ticket_code`, `tickect_date`, `order_id`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ff197da84a9a3af53878eddc91afbb2e', '33', NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_head`(`id`, `code`, `name`, `cgr_sql`, `return_val_field`, `return_txt_field`, `return_type`, `db_source`, `content`, `update_time`, `update_by`, `create_time`, `create_by`) VALUES ('6c7f59741c814347905a938f06ee003c', 'report_user', '统计在线用户', 'select * from sys_user', NULL, NULL, '1', NULL, NULL, '2019-03-25 11:21:04', 'admin', '2019-03-25 11:20:45', 'admin');

INSERT INTO `jeecg_boot`.`onl_cgreport_head`(`id`, `code`, `name`, `cgr_sql`, `return_val_field`, `return_txt_field`, `return_type`, `db_source`, `content`, `update_time`, `update_by`, `create_time`, `create_by`) VALUES ('87b55a515d3441b6b98e48e5b35474a6', 'demo', 'Report Demo', 'select * from demo', NULL, NULL, '1', NULL, NULL, '2019-03-15 18:18:17', 'admin', '2019-03-12 11:25:16', 'admin');

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1740bb02519db90c44cb2cba8b755136', '6c7f59741c814347905a938f06ee003c', 'realname', '用户名', NULL, 'String', NULL, 0, 0, '', '', 1, 3, '', 'admin', '2019-03-25 11:20:45', 'admin', '2019-04-03 15:40:31');

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1b181e6d2813bcb263adc39737f9df46', '87b55a515d3441b6b98e48e5b35474a6', 'name', '用户名', NULL, 'String', 'single', 0, 1, '', '', 1, 4, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1fb45af29af4e792bdc5a4a2c06a4d4d', '402880ec5d872157015d87f2dd940010', 'data_table', '表名', NULL, 'String', NULL, 0, 0, NULL, NULL, 1, 0, NULL, 'admin', '2019-03-20 13:24:21', 'admin', '2019-03-20 13:25:08');

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f167cf82600167cfa154ec0003', '402880e64e1ef94d014e1efefc2a0001', 'id', 'id', 0, 'String', '', NULL, 0, '', '', 0, 0, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f167cf82600167cfa154ed0004', '402880e64e1ef94d014e1efefc2a0001', 'accountname', 'accountname', 0, 'String', '', NULL, 0, '', '', 0, 1, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f167cf82600167cfa154ed0005', '402880e64e1ef94d014e1efefc2a0001', 'accounttoken', 'accounttoken', 0, 'String', '', NULL, 0, '', '', 0, 2, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f167cf82600167cfa154ed0006', '402880e64e1ef94d014e1efefc2a0001', 'accountnumber', 'accountnumber', 0, 'String', '', NULL, 0, '', '', 0, 3, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f167cf82600167cfa154ed0007', '402880e64e1ef94d014e1efefc2a0001', 'accounttype', 'accounttype', 0, 'String', '', NULL, 0, '', '', 0, 4, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f167cf82600167cfa154ed0008', '402880e64e1ef94d014e1efefc2a0001', 'accountemail', 'accountemail', 0, 'String', '', NULL, 0, '', '', 0, 5, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f167cf82600167cfa154ed0009', '402880e64e1ef94d014e1efefc2a0001', 'accountdesc', 'accountdesc', 0, 'String', '', NULL, 0, '', '', 0, 6, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f167cf82600167cfa154ee000b', '402880e64e1ef94d014e1efefc2a0001', 'accountappid', 'accountappid', 0, 'String', '', NULL, 0, '', '', 0, 8, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f167cf82600167cfa154ee000c', '402880e64e1ef94d014e1efefc2a0001', 'accountappsecret', 'accountappsecret', 0, 'String', '', NULL, 0, '', '', 0, 9, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f167cf82600167cfa154ee000d', '402880e64e1ef94d014e1efefc2a0001', 'ADDTOEKNTIME', 'ADDTOEKNTIME', 0, 'String', '', NULL, 0, '', '', 0, 10, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f167cf82600167cfa154ee000e', '402880e64e1ef94d014e1efefc2a0001', 'USERNAME', 'USERNAME', 0, 'String', '', NULL, 0, '', '', 0, 11, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f167cf82600167cfa154ee0010', '402880e64e1ef94d014e1efefc2a0001', 'jsapiticket', 'jsapiticket', 0, 'String', '', NULL, 0, '', '', 0, 13, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f167cf82600167cfa154ee0011', '402880e64e1ef94d014e1efefc2a0001', 'jsapitickettime', 'jsapitickettime', 0, 'String', '', NULL, 0, '', '', 0, 14, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f167cf82600167cfa154ef0012', '402880e64e1ef94d014e1efefc2a0001', 'init_data_flag', 'init_data_flag', 0, 'String', '', NULL, 0, '', '', 0, 15, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f363aa9a380163aa9ebe490002', '402881f363aa9a380163aa9ebe480001', 'id', 'id', 0, 'String', '', NULL, 0, '', '', 0, 0, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f363aa9a380163aa9ebe490003', '402881f363aa9a380163aa9ebe480001', 'name', 'name', 0, 'String', 'single', NULL, 0, '', '', 0, 10, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f363aa9a380163aa9ebe490004', '402881f363aa9a380163aa9ebe480001', 'sex', 'sex', 0, 'String', 'single', NULL, 0, 'sex', '', 0, 11, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f363aa9a380163aa9ebe490005', '402881f363aa9a380163aa9ebe480001', 'age', 'age', 0, 'String', '', NULL, 0, '', '', 0, 12, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f363aa9a380163aa9ebe490006', '402881f363aa9a380163aa9ebe480001', 'address', 'address', 0, 'String', '', NULL, 0, '', '', 0, 13, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f363aa9a380163aa9ebe490007', '402881f363aa9a380163aa9ebe480001', 'phone', 'phone', 0, 'String', '', NULL, 0, '', '', 0, 14, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f363aa9a380163aa9ebe4a0008', '402881f363aa9a380163aa9ebe480001', 'memo', 'memo', 0, 'String', '', NULL, 0, '', '', 0, 15, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f36402f3de016403035d2c0004', '402880e64eb9a22c014eb9a4d5890001', 'ID', 'ID', 0, 'String', '', NULL, 0, '', '', 0, 0, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f36402f3de016403035d2c0005', '402880e64eb9a22c014eb9a4d5890001', 'activitiSync', 'activitiSync', 0, 'String', '', NULL, 0, '', '', 0, 1, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f36402f3de016403035d2d0006', '402880e64eb9a22c014eb9a4d5890001', 'browser', 'browser', 0, 'String', '', NULL, 0, '', '', 0, 2, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f36402f3de016403035d2d0007', '402880e64eb9a22c014eb9a4d5890001', 'password', 'password', 0, 'String', '', NULL, 0, '', '', 0, 3, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f36402f3de016403035d2e0008', '402880e64eb9a22c014eb9a4d5890001', 'realname', 'realname', 0, 'String', '', NULL, 0, '', '', 0, 4, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f36402f3de016403035d2e0009', '402880e64eb9a22c014eb9a4d5890001', 'signature', 'signature', 0, 'String', '', NULL, 0, '', '', 0, 5, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f36402f3de016403035d2f000a', '402880e64eb9a22c014eb9a4d5890001', 'status', 'status', 0, 'String', '', NULL, 0, '', '', 0, 6, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f36402f3de016403035d30000b', '402880e64eb9a22c014eb9a4d5890001', 'userkey', 'userkey', 0, 'String', '', NULL, 0, '', '', 0, 7, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f36402f3de016403035d30000c', '402880e64eb9a22c014eb9a4d5890001', 'username', 'username', 0, 'String', '', NULL, 0, '', '', 0, 8, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f36402f3de016403035d31000d', '402880e64eb9a22c014eb9a4d5890001', 'departid', 'departid', 0, 'String', '', NULL, 0, '', '', 0, 9, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f36402f3de016403035d31000e', '402880e64eb9a22c014eb9a4d5890001', 'user_name_en', 'user_name_en', 0, 'String', '', NULL, 0, '', '', 0, 10, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f36402f3de016403035d32000f', '402880e64eb9a22c014eb9a4d5890001', 'delete_flag', 'delete_flag', 0, 'String', '', NULL, 0, '', '', 0, 11, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f3647e95be01647eb88c400003', '402880e74d76e784014d76f9e783001e', 'account', 'account', 0, 'String', 'single', NULL, 0, '', '', 0, 0, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f3647e95be01647eb88c410004', '402880e74d76e784014d76f9e783001e', 'realname', 'realname', 0, 'String', '', NULL, 0, '', '', 0, 1, '', NULL, NULL, NULL, NULL);

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('61ef5b323134938fdd07ad5e3ea16cd3', '87b55a515d3441b6b98e48e5b35474a6', 'key_word', '关键词', NULL, 'String', 'single', 0, 1, '', '', 1, 5, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('627768efd9ba2c41e905579048f21000', '6c7f59741c814347905a938f06ee003c', 'username', '用户名', NULL, 'String', NULL, 0, 0, '', '', 1, 2, '', 'admin', '2019-03-25 11:20:45', 'admin', '2019-04-03 15:40:31');

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8a2dfe672f3c0d391ace4a9f9bf564ff', '402880ec5d872157015d87f2dd940010', 'data_id', '数据ID', NULL, 'String', NULL, 0, 0, NULL, NULL, 1, 0, NULL, 'admin', '2019-03-20 13:24:21', 'admin', '2019-03-20 13:25:08');

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8bb087a9aa2000bcae17a1b3f5768435', '6c7f59741c814347905a938f06ee003c', 'sex', '性别', NULL, 'String', NULL, 0, 0, 'sex', '', 1, 5, '', 'admin', '2019-03-25 11:20:45', 'admin', '2019-04-03 15:40:31');

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('90d4fa57d301801abb26a9b86b6b94c4', '6c7f59741c814347905a938f06ee003c', 'birthday', '生日', NULL, 'Date', NULL, 0, 0, '', '', 1, 4, '', 'admin', '2019-03-25 11:20:45', 'admin', '2019-04-03 15:40:31');

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9a0a7375479b7657e16c6a228354b495', '402880ec5d872157015d87f2dd940010', 'data_version', '数据版本', NULL, 'String', NULL, 0, 0, NULL, NULL, 1, 0, NULL, 'admin', '2019-03-20 13:24:21', 'admin', '2019-03-20 13:25:08');

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a4ac355f07a05218854e5f23e2930163', '6c7f59741c814347905a938f06ee003c', 'avatar', '头像', NULL, 'String', NULL, 0, 0, '', '', 0, 6, '', 'admin', '2019-03-25 11:20:45', 'admin', '2019-04-03 15:40:31');

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ae4d621e391a1392779175cf5a65134c', '87b55a515d3441b6b98e48e5b35474a6', 'update_by', '修改人', NULL, 'String', NULL, 0, 0, '', '', 1, 7, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b27bea35b1264003c79d38cb86d6929e', '6c7f59741c814347905a938f06ee003c', 'id', 'id', NULL, 'String', NULL, 0, 0, '', '', 0, 1, '', 'admin', '2019-03-25 11:20:45', 'admin', '2019-04-03 15:40:31');

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ce5168755a734ea09dd190e28bf8d9f4', '87b55a515d3441b6b98e48e5b35474a6', 'update_time', '修改时间', NULL, 'String', NULL, 0, 0, '', '', 1, 2, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d6e86b5ffd096ddcc445c0f320a45004', '6c7f59741c814347905a938f06ee003c', 'phone', '手机号', NULL, 'String', NULL, 0, 0, '', '', 1, 11, '', 'admin', '2019-03-25 11:20:45', 'admin', '2019-04-03 15:40:31');

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('df365cd357699eea96c29763d1dd7f9d', '6c7f59741c814347905a938f06ee003c', 'email', '邮箱', NULL, 'String', NULL, 0, 0, '', '', 1, 14, '', 'admin', '2019-03-25 11:20:45', 'admin', '2019-04-03 15:40:31');

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('edf9932912b81ad01dd557d3d593a559', '87b55a515d3441b6b98e48e5b35474a6', 'age', '年龄', NULL, 'String', NULL, 0, 0, '', '', 1, 8, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f985883e509a6faaaf62ca07fd24a73c', '87b55a515d3441b6b98e48e5b35474a6', 'birthday', '生日', NULL, 'Date', 'single', 0, 1, '', '', 1, 1, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');

INSERT INTO `jeecg_boot`.`onl_cgreport_item`(`id`, `cgrhead_id`, `field_name`, `field_txt`, `field_width`, `field_type`, `search_mode`, `is_order`, `is_search`, `dict_code`, `field_href`, `is_show`, `order_num`, `replace_val`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('fce83e4258de3e2f114ab3116397670c', '87b55a515d3441b6b98e48e5b35474a6', 'punch_time', '发布时间', NULL, 'String', NULL, 0, 0, '', '', 1, 3, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');

INSERT INTO `jeecg_boot`.`onl_cgreport_param`(`id`, `cgrhead_id`, `param_name`, `param_txt`, `param_value`, `order_num`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f36402f3de016403035d350010', '402880e64eb9a22c014eb9a4d5890001', 'usekey', 'usekey', '', 0, 'admin', '2018-06-15 18:35:09', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_announcement`(`id`, `titile`, `msg_content`, `start_time`, `end_time`, `sender`, `priority`, `msg_type`, `send_status`, `send_time`, `cancel_time`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `user_ids`) VALUES ('1b714f8ebc3cc33f8b4f906103b6a18d', '5467567', NULL, NULL, NULL, 'admin', NULL, NULL, '1', '2019-03-30 12:40:38', NULL, '0', 'admin', '2019-02-26 17:23:26', 'admin', '2019-02-26 17:35:10', NULL);

INSERT INTO `jeecg_boot`.`sys_announcement`(`id`, `titile`, `msg_content`, `start_time`, `end_time`, `sender`, `priority`, `msg_type`, `send_status`, `send_time`, `cancel_time`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `user_ids`) VALUES ('3d11237ccdf62450d20bb8abdb331178', '111222', NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, '1', 'admin', '2019-03-29 17:19:47', 'admin', '2019-03-29 17:19:50', NULL);

INSERT INTO `jeecg_boot`.`sys_announcement`(`id`, `titile`, `msg_content`, `start_time`, `end_time`, `sender`, `priority`, `msg_type`, `send_status`, `send_time`, `cancel_time`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `user_ids`) VALUES ('7ef04e95f8de030b1d5f7a9144090dc6', '111', NULL, '2019-02-06 17:28:10', '2019-03-08 17:28:11', NULL, NULL, NULL, '0', NULL, NULL, '1', 'admin', '2019-02-26 17:28:17', 'admin', '2019-03-26 19:59:49', NULL);

INSERT INTO `jeecg_boot`.`sys_announcement`(`id`, `titile`, `msg_content`, `start_time`, `end_time`, `sender`, `priority`, `msg_type`, `send_status`, `send_time`, `cancel_time`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `user_ids`) VALUES ('93a9060a1c20e4bf98b3f768a02c2ff9', '111', '111', '2019-02-06 17:20:17', '2019-02-21 17:20:20', 'admin', 'M', 'ALL', '1', '2019-02-26 17:24:29', NULL, '0', 'admin', '2019-02-26 17:16:26', 'admin', '2019-02-26 17:19:35', NULL);

INSERT INTO `jeecg_boot`.`sys_announcement`(`id`, `titile`, `msg_content`, `start_time`, `end_time`, `sender`, `priority`, `msg_type`, `send_status`, `send_time`, `cancel_time`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `user_ids`) VALUES ('de1dc57f31037079e1e55c8347fe6ef7', '222', '2222', '2019-02-06 17:28:26', '2019-02-23 17:28:28', 'admin', 'M', 'ALL', '1', '2019-03-29 17:19:56', NULL, '1', 'admin', '2019-02-26 17:28:36', 'admin', '2019-02-26 17:28:40', NULL);

INSERT INTO `jeecg_boot`.`sys_announcement`(`id`, `titile`, `msg_content`, `start_time`, `end_time`, `sender`, `priority`, `msg_type`, `send_status`, `send_time`, `cancel_time`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `user_ids`) VALUES ('e52f3eb6215f139cb2224c52517af3bd', '334', '334', NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, '1', 'admin', '2019-03-30 12:40:28', 'admin', '2019-03-30 12:40:32', NULL);

INSERT INTO `jeecg_boot`.`sys_data_log`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `data_table`, `data_id`, `data_content`, `data_version`) VALUES ('402880f05ab0d198015ab12274bf0006', 'admin', '2017-03-09 11:35:09', NULL, NULL, 'jeecg_demo', '4028ef81550c1a7901550c1cd6e70001', '{\"mobilePhone\":\"\",\"officePhone\":\"\",\"email\":\"\",\"createDate\":\"Jun 23, 2016 12:00:00 PM\",\"sex\":\"1\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"9001\",\"status\":\"1\",\"content\":\"111\",\"id\":\"4028ef81550c1a7901550c1cd6e70001\"}', 3);

INSERT INTO `jeecg_boot`.`sys_data_log`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `data_table`, `data_id`, `data_content`, `data_version`) VALUES ('402880f05ab6d12b015ab700bead0009', 'admin', '2017-03-10 14:56:03', NULL, NULL, 'jeecg_demo', '402880f05ab6d12b015ab700be8d0008', '{\"mobilePhone\":\"\",\"officePhone\":\"\",\"email\":\"\",\"createDate\":\"Mar 10, 2017 2:56:03 PM\",\"sex\":\"0\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"111\",\"status\":\"0\",\"id\":\"402880f05ab6d12b015ab700be8d0008\"}', 1);

INSERT INTO `jeecg_boot`.`sys_data_log`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `data_table`, `data_id`, `data_content`, `data_version`) VALUES ('402880f05ab6d12b015ab705a23f000d', 'admin', '2017-03-10 15:01:24', NULL, NULL, 'jeecg_demo', '402880f05ab6d12b015ab705a233000c', '{\"mobilePhone\":\"\",\"officePhone\":\"11\",\"email\":\"\",\"createDate\":\"Mar 10, 2017 3:01:24 PM\",\"sex\":\"0\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"11\",\"status\":\"0\",\"id\":\"402880f05ab6d12b015ab705a233000c\"}', 1);

INSERT INTO `jeecg_boot`.`sys_data_log`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `data_table`, `data_id`, `data_content`, `data_version`) VALUES ('402880f05ab6d12b015ab712a6420013', 'admin', '2017-03-10 15:15:37', NULL, NULL, 'jeecg_demo', '402880f05ab6d12b015ab712a6360012', '{\"mobilePhone\":\"\",\"officePhone\":\"\",\"email\":\"\",\"createDate\":\"Mar 10, 2017 3:15:37 PM\",\"sex\":\"0\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"小王\",\"status\":\"0\",\"id\":\"402880f05ab6d12b015ab712a6360012\"}', 1);

INSERT INTO `jeecg_boot`.`sys_data_log`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `data_table`, `data_id`, `data_content`, `data_version`) VALUES ('402880f05ab6d12b015ab712d0510015', 'admin', '2017-03-10 15:15:47', NULL, NULL, 'jeecg_demo', '402880f05ab6d12b015ab712a6360012', '{\"mobilePhone\":\"18611788525\",\"officePhone\":\"\",\"email\":\"\",\"createDate\":\"Mar 10, 2017 3:15:37 AM\",\"sex\":\"0\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"小王\",\"status\":\"0\",\"id\":\"402880f05ab6d12b015ab712a6360012\"}', 2);

INSERT INTO `jeecg_boot`.`sys_data_log`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `data_table`, `data_id`, `data_content`, `data_version`) VALUES ('402880f05ab6d12b015ab71308240018', 'admin', '2017-03-10 15:16:02', NULL, NULL, 'jeecg_demo', '8a8ab0b246dc81120146dc81860f016f', '{\"mobilePhone\":\"13111111111\",\"officePhone\":\"66666666\",\"email\":\"demo@jeecg.com\",\"age\":12,\"salary\":10.00,\"birthday\":\"Feb 14, 2014 12:00:00 AM\",\"sex\":\"1\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"小明\",\"status\":\"\",\"content\":\"\",\"id\":\"8a8ab0b246dc81120146dc81860f016f\"}', 1);

INSERT INTO `jeecg_boot`.`sys_data_log`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `data_table`, `data_id`, `data_content`, `data_version`) VALUES ('402880f05ab6d12b015ab72806c3001b', 'admin', '2017-03-10 15:38:58', NULL, NULL, 'jeecg_demo', '8a8ab0b246dc81120146dc81860f016f', '{\"mobilePhone\":\"18611788888\",\"officePhone\":\"66666666\",\"email\":\"demo@jeecg.com\",\"age\":12,\"salary\":10.00,\"birthday\":\"Feb 14, 2014 12:00:00 AM\",\"sex\":\"1\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"小明\",\"status\":\"\",\"content\":\"\",\"id\":\"8a8ab0b246dc81120146dc81860f016f\"}', 2);

INSERT INTO `jeecg_boot`.`sys_data_log`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `data_table`, `data_id`, `data_content`, `data_version`) VALUES ('4028ef815318148a0153181567690001', 'admin', '2016-02-25 18:59:29', NULL, NULL, 'jeecg_demo', '4028ef815318148a0153181566270000', '{\"mobilePhone\":\"13423423423\",\"officePhone\":\"1\",\"email\":\"\",\"age\":1,\"salary\":1,\"birthday\":\"Feb 25, 2016 12:00:00 AM\",\"createDate\":\"Feb 25, 2016 6:59:24 PM\",\"depId\":\"402880e447e9a9570147e9b6a3be0005\",\"userName\":\"1\",\"status\":\"0\",\"id\":\"4028ef815318148a0153181566270000\"}', 1);

INSERT INTO `jeecg_boot`.`sys_data_log`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `data_table`, `data_id`, `data_content`, `data_version`) VALUES ('4028ef815318148a01531815ec5c0003', 'admin', '2016-02-25 19:00:03', NULL, NULL, 'jeecg_demo', '4028ef815318148a0153181566270000', '{\"mobilePhone\":\"13426498659\",\"officePhone\":\"1\",\"email\":\"\",\"age\":1,\"salary\":1.00,\"birthday\":\"Feb 25, 2016 12:00:00 AM\",\"createDate\":\"Feb 25, 2016 6:59:24 AM\",\"depId\":\"402880e447e9a9570147e9b6a3be0005\",\"userName\":\"1\",\"status\":\"0\",\"id\":\"4028ef815318148a0153181566270000\"}', 2);

INSERT INTO `jeecg_boot`.`sys_data_log`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `data_table`, `data_id`, `data_content`, `data_version`) VALUES ('4028ef8153c028db0153c0502e6b0003', 'admin', '2016-03-29 10:59:53', NULL, NULL, 'jeecg_demo', '4028ef8153c028db0153c0502d420002', '{\"mobilePhone\":\"18455477548\",\"officePhone\":\"123\",\"email\":\"\",\"createDate\":\"Mar 29, 2016 10:59:53 AM\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"123\",\"status\":\"0\",\"id\":\"4028ef8153c028db0153c0502d420002\"}', 1);

INSERT INTO `jeecg_boot`.`sys_data_log`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `data_table`, `data_id`, `data_content`, `data_version`) VALUES ('4028ef8153c028db0153c0509aa40006', 'admin', '2016-03-29 11:00:21', NULL, NULL, 'jeecg_demo', '4028ef8153c028db0153c0509a3e0005', '{\"mobilePhone\":\"13565486458\",\"officePhone\":\"\",\"email\":\"\",\"createDate\":\"Mar 29, 2016 11:00:21 AM\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"22\",\"status\":\"0\",\"id\":\"4028ef8153c028db0153c0509a3e0005\"}', 1);

INSERT INTO `jeecg_boot`.`sys_data_log`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `data_table`, `data_id`, `data_content`, `data_version`) VALUES ('4028ef8153c028db0153c051c4a70008', 'admin', '2016-03-29 11:01:37', NULL, NULL, 'jeecg_demo', '4028ef8153c028db0153c0509a3e0005', '{\"mobilePhone\":\"13565486458\",\"officePhone\":\"\",\"email\":\"\",\"createDate\":\"Mar 29, 2016 11:00:21 AM\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"22\",\"status\":\"0\",\"id\":\"4028ef8153c028db0153c0509a3e0005\"}', 2);

INSERT INTO `jeecg_boot`.`sys_data_log`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `data_table`, `data_id`, `data_content`, `data_version`) VALUES ('4028ef8153c028db0153c051d4b5000a', 'admin', '2016-03-29 11:01:41', NULL, NULL, 'jeecg_demo', '4028ef8153c028db0153c0502d420002', '{\"mobilePhone\":\"13565486458\",\"officePhone\":\"123\",\"email\":\"\",\"createDate\":\"Mar 29, 2016 10:59:53 AM\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"123\",\"status\":\"0\",\"id\":\"4028ef8153c028db0153c0502d420002\"}', 2);

INSERT INTO `jeecg_boot`.`sys_data_log`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `data_table`, `data_id`, `data_content`, `data_version`) VALUES ('4028ef8153c028db0153c07033d8000d', 'admin', '2016-03-29 11:34:52', NULL, NULL, 'jeecg_demo', '4028ef8153c028db0153c0502d420002', '{\"mobilePhone\":\"13565486458\",\"officePhone\":\"123\",\"email\":\"\",\"age\":23,\"createDate\":\"Mar 29, 2016 10:59:53 AM\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"123\",\"status\":\"0\",\"id\":\"4028ef8153c028db0153c0502d420002\"}', 3);

INSERT INTO `jeecg_boot`.`sys_data_log`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `data_table`, `data_id`, `data_content`, `data_version`) VALUES ('4028ef8153c028db0153c070492e000f', 'admin', '2016-03-29 11:34:57', NULL, NULL, 'jeecg_demo', '4028ef8153c028db0153c0509a3e0005', '{\"mobilePhone\":\"13565486458\",\"officePhone\":\"\",\"email\":\"\",\"age\":22,\"createDate\":\"Mar 29, 2016 11:00:21 AM\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"22\",\"status\":\"0\",\"id\":\"4028ef8153c028db0153c0509a3e0005\"}', 3);

INSERT INTO `jeecg_boot`.`sys_data_log`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `data_table`, `data_id`, `data_content`, `data_version`) VALUES ('4028ef81550c1a7901550c1cd7850002', 'admin', '2016-06-01 21:17:44', NULL, NULL, 'jeecg_demo', '4028ef81550c1a7901550c1cd6e70001', '{\"mobilePhone\":\"\",\"officePhone\":\"\",\"email\":\"\",\"createDate\":\"Jun 1, 2016 9:17:44 PM\",\"sex\":\"1\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"121221\",\"status\":\"0\",\"id\":\"4028ef81550c1a7901550c1cd6e70001\"}', 1);

INSERT INTO `jeecg_boot`.`sys_data_log`(`id`, `create_by`, `create_time`, `update_by`, `update_time`, `data_table`, `data_id`, `data_content`, `data_version`) VALUES ('4028ef81568c31ec01568c3307080004', 'admin', '2016-08-15 11:16:09', NULL, NULL, 'jeecg_demo', '4028ef81550c1a7901550c1cd6e70001', '{\"mobilePhone\":\"\",\"officePhone\":\"\",\"email\":\"\",\"createDate\":\"Jun 23, 2016 12:00:00 PM\",\"sex\":\"1\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"9001\",\"status\":\"1\",\"content\":\"111\",\"id\":\"4028ef81550c1a7901550c1cd6e70001\"}', 2);

INSERT INTO `jeecg_boot`.`sys_depart`(`id`, `parent_id`, `depart_name`, `depart_name_en`, `depart_name_abbr`, `depart_order`, `description`, `org_type`, `org_code`, `mobile`, `fax`, `address`, `memo`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5159cde220114246b045e574adceafe9', '6d35e179cd814e3299bd588ea7daed3f', '研发部', NULL, NULL, 0, NULL, '2', 'A02A02', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2019-02-26 16:44:38', 'admin', '2019-03-07 09:36:53');

INSERT INTO `jeecg_boot`.`sys_depart`(`id`, `parent_id`, `depart_name`, `depart_name_en`, `depart_name_abbr`, `depart_order`, `description`, `org_type`, `org_code`, `mobile`, `fax`, `address`, `memo`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6d35e179cd814e3299bd588ea7daed3f', '', '卓尔互动公司', NULL, NULL, 0, NULL, '1', 'A02', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2019-02-26 16:36:39', 'admin', '2019-03-22 16:47:25');

INSERT INTO `jeecg_boot`.`sys_depart`(`id`, `parent_id`, `depart_name`, `depart_name_en`, `depart_name_abbr`, `depart_order`, `description`, `org_type`, `org_code`, `mobile`, `fax`, `address`, `memo`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('743ba9dbdc114af8953a11022ef3096a', 'f28c6f53abd841ac87ead43afc483433', '财务部', NULL, NULL, 0, NULL, '2', 'A03A01', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2019-03-22 16:45:43', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_depart`(`id`, `parent_id`, `depart_name`, `depart_name_en`, `depart_name_abbr`, `depart_order`, `description`, `org_type`, `org_code`, `mobile`, `fax`, `address`, `memo`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a7d7e77e06c84325a40932163adcdaa6', '6d35e179cd814e3299bd588ea7daed3f', '财务部', NULL, NULL, 0, NULL, '2', 'A02A01', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2019-02-26 16:36:47', 'admin', '2019-02-26 16:37:25');

UPDATE `jeecg_boot`.`sys_depart` SET `parent_id` = 'c6d7cb4deeac411cb3384b1b31278596', `depart_name` = '市场部', `depart_name_en` = NULL, `depart_name_abbr` = NULL, `depart_order` = 0, `description` = NULL, `org_type` = '2', `org_code` = 'A01A03', `mobile` = NULL, `fax` = NULL, `address` = NULL, `memo` = NULL, `status` = NULL, `del_flag` = '0', `create_by` = 'admin', `create_time` = '2019-02-20 17:15:34', `update_by` = 'admin', `update_time` = '2019-02-26 16:36:18' WHERE `id` = '4f1765520d6346f9bd9c79e2479e5b12';

UPDATE `jeecg_boot`.`sys_depart` SET `parent_id` = 'c6d7cb4deeac411cb3384b1b31278596', `depart_name` = '研发部', `depart_name_en` = NULL, `depart_name_abbr` = NULL, `depart_order` = 0, `description` = NULL, `org_type` = '2', `org_code` = 'A01A05', `mobile` = NULL, `fax` = NULL, `address` = NULL, `memo` = NULL, `status` = NULL, `del_flag` = '0', `create_by` = 'admin', `create_time` = '2019-02-21 16:14:41', `update_by` = 'admin', `update_time` = '2019-03-27 19:05:49' WHERE `id` = '57197590443c44f083d42ae24ef26a2c';

UPDATE `jeecg_boot`.`sys_depart` SET `parent_id` = '', `depart_name` = '北京国炬公司', `depart_name_en` = NULL, `depart_name_abbr` = NULL, `depart_order` = 0, `description` = NULL, `org_type` = '1', `org_code` = 'A01', `mobile` = NULL, `fax` = NULL, `address` = NULL, `memo` = NULL, `status` = NULL, `del_flag` = '0', `create_by` = 'admin', `create_time` = '2019-02-11 14:21:51', `update_by` = 'admin', `update_time` = '2019-03-22 16:47:19' WHERE `id` = 'c6d7cb4deeac411cb3384b1b31278596';

INSERT INTO `jeecg_boot`.`sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('2f0320997ade5dd147c90130f7218c3e', '推送类别', 'msg_type', '', 1, 'admin', '2019-03-17 21:21:32', 'admin', '2019-03-26 19:57:45', 0);

INSERT INTO `jeecg_boot`.`sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('3486f32803bb953e7155dab3513dc68b', '删除状态', 'del_flag', NULL, 1, 'admin', '2019-01-18 21:46:26', 'admin', '2019-03-30 11:17:11', 0);

INSERT INTO `jeecg_boot`.`sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('404a04a15f371566c658ee9ef9fc392a', 'cehis2', '22', NULL, 2, 'admin', '2019-01-30 11:17:21', 'admin', '2019-03-30 11:18:12', 0);

INSERT INTO `jeecg_boot`.`sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('4c753b5293304e7a445fd2741b46529d', '字典状态', 'dict_item_status', NULL, 1, 'admin', '2020-06-18 23:18:42', 'admin', '2019-03-30 19:33:52', 1);

INSERT INTO `jeecg_boot`.`sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('4e4602b3e3686f0911384e188dc7efb4', '条件规则', 'rule_conditions', '', 1, 'admin', '2019-04-01 10:15:03', 'admin', '2019-04-01 10:30:47', 0);

INSERT INTO `jeecg_boot`.`sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('4f69be5f507accea8d5df5f11346181a', '发送消息类型', 'msgType', NULL, 1, 'admin', '2019-04-11 14:27:09', NULL, NULL, 0);

INSERT INTO `jeecg_boot`.`sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('72cce0989df68887546746d8f09811aa', 'Online表单类型', 'cgform_table_type', '', 1, 'admin', '2019-01-27 10:13:02', 'admin', '2019-03-30 11:37:36', 0);

INSERT INTO `jeecg_boot`.`sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('83bfb33147013cc81640d5fd9eda030c', '日志类型', 'log_type', NULL, 1, 'admin', '2019-03-18 23:22:19', NULL, NULL, 1);

INSERT INTO `jeecg_boot`.`sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('845da5006c97754728bf48b6a10f79cc', '状态', 'status', NULL, 2, 'admin', '2019-03-18 21:45:25', 'admin', '2019-03-18 21:58:25', 0);

INSERT INTO `jeecg_boot`.`sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('a9d9942bd0eccb6e89de92d130ec4c4a', '消息发送状态', 'msgSendStatus', NULL, 1, 'admin', '2019-04-12 18:18:17', NULL, NULL, 0);

INSERT INTO `jeecg_boot`.`sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('ac2f7c0c5c5775fcea7e2387bcb22f01', '菜单类型', 'menu_type', NULL, 1, 'admin', '2020-12-18 23:24:32', 'admin', '2019-04-01 15:27:06', 1);

INSERT INTO `jeecg_boot`.`sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('ad7c65ba97c20a6805d5dcdf13cdaf36', 'onlineT类型', 'ceshi_online', NULL, 2, 'admin', '2019-03-22 16:31:49', 'admin', '2019-03-22 16:34:16', 0);

INSERT INTO `jeecg_boot`.`sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('c36169beb12de8a71c8683ee7c28a503', '部门状态', 'depart_status', NULL, 1, 'admin', '2019-03-18 21:59:51', NULL, NULL, 0);

INSERT INTO `jeecg_boot`.`sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('d6e1152968b02d69ff358c75b48a6ee1', '流程类型', 'bpm_process_type', NULL, 1, 'admin', '2021-02-22 19:26:54', 'admin', '2019-03-30 18:14:44', 0);

INSERT INTO `jeecg_boot`.`sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('fc6cd58fde2e8481db10d3a1e68ce70c', '用户状态', 'user_status', NULL, 1, 'admin', '2019-03-18 21:57:25', 'admin', '2019-03-18 23:11:58', 1);

UPDATE `jeecg_boot`.`sys_dict` SET `dict_name` = '性别', `dict_code` = 'sex', `description` = NULL, `del_flag` = 1, `create_by` = NULL, `create_time` = '2019-01-04 14:56:32', `update_by` = 'admin', `update_time` = '2019-03-30 11:28:27', `type` = 1 WHERE `id` = '3d9a351be3436fbefb1307d4cfb49bf2';

UPDATE `jeecg_boot`.`sys_dict` SET `dict_name` = '用户类型', `dict_code` = 'user_type', `description` = NULL, `del_flag` = 2, `create_by` = NULL, `create_time` = '2019-01-04 14:59:01', `update_by` = 'admin', `update_time` = '2019-03-18 23:28:18', `type` = 0 WHERE `id` = '6b78e3f59faec1a4750acff08030a79b';

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('05a2e732ce7b00aa52141ecc3e330b4e', '3486f32803bb953e7155dab3513dc68b', '已删除', '1', NULL, NULL, 1, 'admin', '2025-10-18 21:46:56', 'admin', '2019-03-28 22:23:20');

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0c9532916f5cd722017b46bc4d953e41', '2f0320997ade5dd147c90130f7218c3e', '指定用户', 'USER', NULL, NULL, 1, 'admin', '2019-03-17 21:22:19', 'admin', '2019-03-17 21:22:28');

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0ca4beba9efc4f9dd54af0911a946d5c', '72cce0989df68887546746d8f09811aa', '附表', '3', NULL, 3, 1, 'admin', '2019-03-27 10:13:43', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('147c48ff4b51545032a9119d13f3222a', 'd6e1152968b02d69ff358c75b48a6ee1', '测试流程', 'test', NULL, 1, 1, 'admin', '2019-03-22 19:27:05', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1b8a6341163062dad8cb2fddd34e0c3b', '404a04a15f371566c658ee9ef9fc392a', '22', '222', NULL, 1, 1, 'admin', '2019-03-30 11:17:48', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1ce390c52453891f93514c1bd2795d44', 'ad7c65ba97c20a6805d5dcdf13cdaf36', '000', '00', NULL, 1, 1, 'admin', '2019-03-22 16:34:34', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('23a5bb76004ed0e39414e928c4cde155', '4e4602b3e3686f0911384e188dc7efb4', '不等于', '!=', '不等于', 3, 1, 'admin', '2019-04-01 16:46:15', 'admin', '2019-04-01 17:48:40');

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('25847e9cb661a7c711f9998452dc09e6', '4e4602b3e3686f0911384e188dc7efb4', '小于等于', '<=', '小于等于', 6, 1, 'admin', '2019-04-01 16:44:34', 'admin', '2019-04-01 17:49:10');

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('33bc9d9f753cf7dc40e70461e50fdc54', 'a9d9942bd0eccb6e89de92d130ec4c4a', '发送失败', '2', NULL, 3, 1, 'admin', '2019-04-12 18:20:02', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('41fa1e9571505d643aea87aeb83d4d76', '4e4602b3e3686f0911384e188dc7efb4', '等于', '=', '等于', 4, 1, 'admin', '2019-04-01 16:45:24', 'admin', '2019-04-01 17:49:00');

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('43d2295b8610adce9510ff196a49c6e9', '845da5006c97754728bf48b6a10f79cc', '正常', '1', NULL, NULL, 1, 'admin', '2019-03-18 21:45:51', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4f05fb5376f4c61502c5105f52e4dd2b', '83bfb33147013cc81640d5fd9eda030c', '操作日志', '2', NULL, NULL, 1, 'admin', '2019-03-18 23:22:49', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('50223341bfb5ba30bf6319789d8d17fe', 'd6e1152968b02d69ff358c75b48a6ee1', '业务办理', 'business', NULL, 3, 1, 'admin', '2023-04-22 19:28:05', 'admin', '2019-03-22 23:24:39');

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5584c21993bde231bbde2b966f2633ac', '4e4602b3e3686f0911384e188dc7efb4', '自定义SQL表达式', 'USE_SQL_RULES', '自定义SQL表达式', 9, 1, 'admin', '2019-04-01 10:45:24', 'admin', '2019-04-01 17:49:27');

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('58b73b344305c99b9d8db0fc056bbc0a', '72cce0989df68887546746d8f09811aa', '主表', '2', NULL, 2, 1, 'admin', '2019-03-27 10:13:36', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5b65a88f076b32e8e69d19bbaadb52d5', '2f0320997ade5dd147c90130f7218c3e', '全体用户', 'ALL', NULL, NULL, 1, 'admin', '2020-10-17 21:22:43', 'admin', '2019-03-28 22:17:09');

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('66c952ae2c3701a993e7db58f3baf55e', '4e4602b3e3686f0911384e188dc7efb4', '大于', '>', '大于', 1, 1, 'admin', '2019-04-01 10:45:46', 'admin', '2019-04-01 17:48:29');

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6937c5dde8f92e9a00d4e2ded9198694', 'ad7c65ba97c20a6805d5dcdf13cdaf36', 'easyui', '3', NULL, 1, 1, 'admin', '2019-03-22 16:32:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('69cacf64e244100289ddd4aa9fa3b915', 'a9d9942bd0eccb6e89de92d130ec4c4a', '未发送', '0', NULL, 1, 1, 'admin', '2019-04-12 18:19:23', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6c682d78ddf1715baf79a1d52d2aa8c2', '72cce0989df68887546746d8f09811aa', '单表', '1', NULL, 1, 1, 'admin', '2019-03-27 10:13:29', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6d404fd2d82311fbc87722cd302a28bc', '4e4602b3e3686f0911384e188dc7efb4', '模糊', 'LIKE', '模糊', 7, 1, 'admin', '2019-04-01 16:46:02', 'admin', '2019-04-01 17:49:20');

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('71b924faa93805c5c1579f12e001c809', 'd6e1152968b02d69ff358c75b48a6ee1', 'OA办公', 'oa', NULL, 2, 1, 'admin', '2021-03-22 19:27:17', 'admin', '2019-03-22 23:24:36');

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('75b260d7db45a39fc7f21badeabdb0ed', 'c36169beb12de8a71c8683ee7c28a503', '不启用', '0', NULL, NULL, 1, 'admin', '2019-03-18 23:29:41', 'admin', '2019-03-18 23:29:54');

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7688469db4a3eba61e6e35578dc7c2e5', 'c36169beb12de8a71c8683ee7c28a503', '启用', '1', NULL, NULL, 1, 'admin', '2019-03-18 23:29:28', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('78ea6cadac457967a4b1c4eb7aaa418c', 'fc6cd58fde2e8481db10d3a1e68ce70c', '正常', '1', NULL, NULL, 1, 'admin', '2019-03-18 23:30:28', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7ccf7b80c70ee002eceb3116854b75cb', 'ac2f7c0c5c5775fcea7e2387bcb22f01', '按钮权限', '2', NULL, NULL, 1, 'admin', '2019-03-18 23:25:40', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('81fb2bb0e838dc68b43f96cc309f8257', 'fc6cd58fde2e8481db10d3a1e68ce70c', '冻结', '2', NULL, NULL, 1, 'admin', '2019-03-18 23:30:37', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('84778d7e928bc843ad4756db1322301f', '4e4602b3e3686f0911384e188dc7efb4', '大于等于', '>=', '大于等于', 5, 1, 'admin', '2019-04-01 10:46:02', 'admin', '2019-04-01 17:49:05');

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('86f19c7e0a73a0bae451021ac05b99dd', 'ac2f7c0c5c5775fcea7e2387bcb22f01', '子菜单', '1', NULL, NULL, 1, 'admin', '2019-03-18 23:25:27', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8bccb963e1cd9e8d42482c54cc609ca2', '4f69be5f507accea8d5df5f11346181a', '微信', '3', NULL, 3, 1, 'admin', '2021-05-11 14:29:12', 'admin', '2019-04-11 14:29:31');

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8c618902365ca681ebbbe1e28f11a548', '4c753b5293304e7a445fd2741b46529d', '启用', '1', NULL, 0, 0, 'admin', '2019-03-18 23:19:27', 'admin', '2019-03-20 09:33:30');

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9a96c4a4e4c5c9b4e4d0cbf6eb3243cc', '4c753b5293304e7a445fd2741b46529d', '不启用', '0', NULL, 1, 1, 'admin', '2019-03-18 23:19:53', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a2321496db6febc956a6c70fab94cb0c', '404a04a15f371566c658ee9ef9fc392a', '3', '3', NULL, 1, 1, 'admin', '2019-03-30 11:18:18', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('aa0d8a8042a18715a17f0a888d360aa4', 'ac2f7c0c5c5775fcea7e2387bcb22f01', '一级菜单', '0', NULL, NULL, 1, 'admin', '2019-03-18 23:24:52', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('adcf2a1fe93bb99a84833043f475fe0b', '4e4602b3e3686f0911384e188dc7efb4', '包含', 'IN', '包含', 8, 1, 'admin', '2019-04-01 16:45:47', 'admin', '2019-04-01 17:49:24');

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b2a8b4bb2c8e66c2c4b1bb086337f393', '3486f32803bb953e7155dab3513dc68b', '正常', '0', NULL, NULL, 1, 'admin', '2022-10-18 21:46:48', 'admin', '2019-03-28 22:22:20');

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b4a887dc3ff01d2daadaa412e63189ed', '4f69be5f507accea8d5df5f11346181a', '邮件', '2', NULL, 2, 1, 'admin', '2021-05-11 14:29:03', 'admin', '2019-04-11 14:29:25');

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d75b5b3666d0742f08027af0255b4400', '4f69be5f507accea8d5df5f11346181a', '短信', '1', NULL, 1, 1, 'admin', '2019-04-11 14:28:49', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e6329e3a66a003819e2eb830b0ca2ea0', '4e4602b3e3686f0911384e188dc7efb4', '小于', '<', '小于', 2, 1, 'admin', '2019-04-01 16:44:15', 'admin', '2019-04-01 17:48:34');

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f0162f4cc572c9273f3e26b2b4d8c082', 'ad7c65ba97c20a6805d5dcdf13cdaf36', 'booostrap', '1', NULL, 1, 1, 'admin', '2021-08-22 16:32:04', 'admin', '2019-03-22 16:33:57');

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f16c5706f3ae05c57a53850c64ce7c45', 'a9d9942bd0eccb6e89de92d130ec4c4a', '发送成功', '1', NULL, 2, 1, 'admin', '2019-04-12 18:19:43', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f2a7920421f3335afdf6ad2b342f6b5d', '845da5006c97754728bf48b6a10f79cc', '冻结', '2', NULL, NULL, 1, 'admin', '2019-03-18 21:46:02', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f37f90c496ec9841c4c326b065e00bb2', '83bfb33147013cc81640d5fd9eda030c', '登录日志', '1', NULL, NULL, 1, 'admin', '2019-03-18 23:22:37', NULL, NULL);

UPDATE `jeecg_boot`.`sys_dict_item` SET `dict_id` = '6b78e3f59faec1a4750acff08030a79b', `item_text` = '22', `item_value` = '222', `description` = NULL, `sort_order` = NULL, `status` = 0, `create_by` = 'admin', `create_time` = '2019-02-21 19:59:43', `update_by` = 'admin', `update_time` = '2019-03-11 21:23:27' WHERE `id` = 'b57f98b88363188daf38d42f25991956';

UPDATE `jeecg_boot`.`sys_dict_item` SET `dict_id` = '3d9a351be3436fbefb1307d4cfb49bf2', `item_text` = '男', `item_value` = '1', `description` = NULL, `sort_order` = 1, `status` = 1, `create_by` = NULL, `create_time` = '2027-08-04 14:56:49', `update_by` = 'admin', `update_time` = '2019-03-23 22:44:44' WHERE `id` = 'df168368dcef46cade2aadd80100d8aa';

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('01075aa535274735e0df0a8bc44f62f9', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-09 16:56:46', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('016510fc662d9bb24d0186c5478df268', 1, '用户名: admin,登录成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-02-26 20:27:18', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('017e9596f489951f1cc7d978085adc00', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-08 10:58:00', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('01ebe1cbeae916a9228770f63130fdac', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-09 16:56:50', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('02026841bf8a9204db2c500c86a4a9be', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.104', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 20:44:58', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0251bbee51c28f83459f4a57eeb61777', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 22:14:27', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('02d4447c9d97ac4fc1c3a9a4c789c2a8', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-28 18:24:18', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('03c0ab177bd7d840b778713b37daf86f', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 10:04:57', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('03ec66b6b6d17c007ec2f918efe5b898', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:16:03', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0475b4445d5f58f29212258c1644f339', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-01 20:20:23', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('04f97d7f906c1e97384a94f3728606a4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 12:08:51', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('056dd4466f4ed51de26c535fd9864828', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-11 19:47:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0571e5730ee624d0dd1b095ad7101738', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 16:10:50', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('060d541a9571ca2b0d24790a98d170a6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 19:28:04', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('06fbb85b34f518cd211b948552de72f8', 1, '登录失败，用户名:null不存在！', NULL, NULL, NULL, '192.168.1.104', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 20:08:38', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0819ea9729ddf70f64ace59370a62cf1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 18:59:43', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('097be3e8fdf77a245f5c85884e97b88c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-05 22:52:45', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0ad51ba59da2c8763a4e6ed6e0a292b2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 17:37:53', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0b42292a532c796495a34d8d9c633afa', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-08 12:58:03', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0ba24c5f61ff53f93134cf932dd486db', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-31 21:06:05', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0d4582c6b7719b0bfc0260939d97274f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-10 21:48:47', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0d6cd835072c83f46d1d2a3cf13225d3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-01 12:04:44', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0d85728028ed67da696137c0e82ab2f6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-16 12:58:43', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0dc22e52c9173e4e880728bc7734ff65', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 11:14:47', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0dc6d04b99e76ad400eef1ded2d3d97c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 09:59:45', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0e754ee377033067f7b2f10b56b8784c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 17:17:45', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0ef3e7ae8c073a7e3bdd736068f86c84', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:02:27', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0efc9df0d52c65ec318e7b46db21655f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:42:47', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0f173ec7e8819358819aa14aafc724c0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 11:15:58', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('10922e0d030960e6b026c67c6179247b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 20:07:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('10a434c326e39b1d046defddc8c57f4a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 21:18:29', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('111156480d4d18ebf40427083f25830f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.104', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 19:48:58', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('11802c7a3644af411bc4e085553cfd4f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 14:46:35', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('11f511eeeb2e91af86b9d5e05132fc89', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 15:13:43', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('13c83c56a0de8a702aeb2aa0c330e42c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 14:53:52', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('14f447d9b60725cc86b3100a5cb20b75', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 19:46:30', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('151a9f1b01e4e749124d274313cd138c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 11:59:17', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1534f0c50e67c5682e91af5160a67a80', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 22:47:28', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('155d2991204d541388d837d1457e56ab', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 11:32:57', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('15b9599cb02b49a62fb4a1a71ccebc18', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:02:50', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('189e3428e35e27dfe92ece2848b10ba8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-27 15:52:21', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('18a51a5f04eeaad6530665f6b0883f0c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:34:11', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('18b16a451fec0fe7bf491ab348c65e30', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-16 11:55:45', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1aa593c64062f0137c0691eabac07521', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-14 10:45:10', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1ab7c74d217152081f4fa59e4a56cc7b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 12:03:39', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1d970c0e396ffc869e3a723d51f88b46', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 13:01:17', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1e4533a02fb9c739a3555fa7be6e7899', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 10:04:56', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1f0b36f7e021aa5d059ffb0a74ef6de4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 23:11:25', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1f8f46118336b2cacf854c1abf8ae144', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 11:02:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('20e5887d0c9c7981159fe91a51961141', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 20:12:07', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('20fc3263762c80ab9268ddd3d4b06500', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:36:44', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('20fea778f4e1ac5c01b5a5a58e3805be', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 19:01:49', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('21510ebaa4eca640852420ed6f6cbe01', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-10 11:41:26', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('217aa2f713b0903e6be699136e374012', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-27 20:07:32', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2186244ae450e83d1487aa01fbeae664', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-29 14:47:43', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('21910e350c9083e107d39ff4278f51d6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 18:14:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('21b8493a05050584d9bb06cfc2a05a6b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-05 17:29:52', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('21bad1470a40da8336294ca7330f443d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 17:35:32', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('21fed0f2d080e04cf0901436721a77a6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 21:53:31', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2312c2693d6b50ca06799fee0ad2554a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 12:11:32', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('23176e4b29c3d2f3abadd99ebeffa347', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 16:37:50', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('233e39d8b7aa90459ebef23587c25448', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 17:38:36', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('26529d5753ceebbd0d774542ec83a43e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 20:17:41', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2659c59136fb1a284ab0642361b10cdd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-29 18:40:22', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2676be4ffc66f83221fd95e23d494827', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.104', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 21:31:28', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('26975f09c66025d1c8d87a6894a3c262', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 18:29:08', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('26be99abd7a62d27c751675df196b011', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-12 20:50:55', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('27d23027dc320175d22391d06f50082f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 15:50:06', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('27e8812c9a16889f14935eecacf188eb', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 18:52:19', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2811e224e4e8d70f2946c815988b9b7c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 12:08:14', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('286af82485388bfcd3bb9821ff1a4727', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 18:34:37', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('28dbc8d16f98fb4b1f481462fcaba48b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-14 20:56:57', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('28e8a7ed786eaced3182c70f68c7ea78', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 12:18:33', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2919d2f18db064978a619707bde4d613', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 09:58:45', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2942a12521ac8e3d441429e6c4b04207', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 14:12:10', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2966ed2bdf67c9f3306b058d13bef301', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 21:25:10', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('29fb5d4297748af3cd1c7f2611b7a2d6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 17:38:05', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2b433e88db411bef115bc9357ba6a78b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.105', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 12:09:36', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2b4d33d9be98e1e4cdd408a55f731050', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 10:32:00', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2b5a76869a7d1900487cd220de378dba', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-05 16:32:00', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2b801129457c05d23653ecaca88f1711', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 21:44:34', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2d02b1f2e72203070b0e5e34ae6a39b2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-14 15:21:25', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2e44c368eda5a7f7a23305b61d82cddb', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 18:14:23', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2e63fd1b3b6a6145bc04b2a1df18d2f5', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 19:01:33', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2eb75cb6ca5bc60241e01fa7471c0ccf', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 18:34:04', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2eb964935df6f3a4d2f3af6ac5f2ded1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.200', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 13:27:18', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2ebe7f0432f01788d69d39bc6df04a1a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 18:05:09', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2fecb508d344c5b3a40f471d7b110f14', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 17:36:53', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3087ac4988a961fa1ec0b4713615c719', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 22:54:24', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('30da94dd068a5a57f3cece2ca5ac1a25', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 18:01:43', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('30ec2dc50347240f131c1004ee9b3a40', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-11 10:19:05', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('310bb368795f4985ed4eada030a435a0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 23:22:20', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('317e3ae1b6ccdfb5db6940789e12d300', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-03 21:44:31', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3267222d9387284b864792531b450bfe', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 10:33:23', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('326b2df4ab05a8dbb03a0a0087e82a25', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-10 11:53:20', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('335956cbad23d1974138752199bf1d84', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-10 10:05:36', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('337b647a4085e48f61c7832e6527517d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 11:45:16', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('34a6b86424857a63159f0e8254e238c2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 18:22:08', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3612f8d40add5a7754ea3d54de0b5f20', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 19:59:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3683743d1936d06f3aaa03d6470e5178', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 22:40:12', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('375aadb2833e57a0d5a2ce0546a65ca4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-07 20:38:52', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3767186b722b7fefd465e147d3170ad1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-26 21:57:19', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('378b44af9c1042c1438450b11c707fcf', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 16:07:39', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('37ca8ff7098b9d118adb0a586bdc0d13', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:46:07', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3836dc3f91d072e838092bc8d3143906', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 12:50:32', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('39caf3d5d308001aeb0a18e15ae480b9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 10:31:07', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3a0330033a8d3b51ffbfb2e0a7db9bba', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:54:27', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3a290289b4b30a1caaac2d03ad3161cd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 11:58:13', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3a76114e431912ff9a19a4b6eb795112', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 12:19:41', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3ba1e54aa9aa760b59dfe1d1259459bc', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-29 09:44:07', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3bc73699a9fd3245b87336787422729b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:41:07', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3d25a4cdd75b9c4c137394ce68e67154', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 09:59:31', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3e2574b7b723fbc9c712b8e200ea0c84', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-10 14:24:34', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3e6116220fa8d4808175738c6de51b12', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 21:04:46', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3e64011b4bea7cdb76953bfbf57135ce', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-10 23:09:32', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3e69108be63179550afe424330a8a9e4', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 18:38:05', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3ec2023daa4a7d6a542bf28b11acf586', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 16:18:20', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3fd0d771bbdd34fae8b48690ddd57799', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 17:17:22', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('404d5fb6cce1001c3553a69089a618c8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 12:29:12', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4114145795da30b34545e9e39b7822d9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:39:33', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4218b30015501ee966548c139c14f43f', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 10:11:35', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4234117751af62ac87343cbf8a6f1e0f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 10:17:17', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('42aef93749cc6222d5debe3fb31ba41b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 15:51:04', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('42bf42af90d4df949ad0a6cd1b39805e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.200', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 17:39:01', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('43079866b75ee6a031835795bb681e16', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-04 22:44:16', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('432067d777447423f1ce3db11a273f6f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 09:47:17', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('43536edd8aa99f9b120872e2c768206c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 10:53:26', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('445436e800d306ec1d7763c0fe28ad38', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 11:43:00', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('45819fe1b96af820575a12e9f973014e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-07 09:19:22', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('45f0309632984f5f7c70b3d40dbafe8b', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 09:59:46', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('472c34745b8f86a46efa28f408465a63', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 17:56:31', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('477592ab95cd219a2ccad79de2f69f51', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-05 10:38:24', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4778fe2992fd5efd65f86cb0e00e338e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 17:53:11', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('47c5a5b799e10255c96ccd65286541ef', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 11:50:35', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4816854636129e31c2a5f9d38af842ef', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-25 12:45:54', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('484cdb8db40e3f76ef686552f57d8099', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 18:14:49', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('48e5faf2d21ead650422dc2eaf1bb6c5', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 22:08:09', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('48eac0dd1c11fe8f0cb49f1bd14529c2', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 13:01:31', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4930e32672465979adbc592e116226a6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 16:53:28', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('49d48fda33126595f6936a5d64e47af0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 13:17:51', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('49f1ec54eb16af2001ff6809a089e940', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 17:59:10', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4aa770f37a7de0039ba0f720c5246486', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 17:26:12', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4ab79469ba556fa890258a532623d1dc', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:54:20', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4ba055970859a6f1afcc01227cb82a2d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-29 09:43:56', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4d1be4b4991a5c2d4d17d0275e4209cf', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-05 20:47:21', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4f31f3ebaf5d1a159d2bb11dd9984909', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-15 11:14:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('51aeabed335ab4e238640a4d17dd51a3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-12 10:12:41', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('52fde989fb8bb78d03fb9c14242f5613', 1, '用户名: admin,登录成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-20 17:04:09', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5323f848cddbb80ba4f0d19c0580eba9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 22:58:40', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5358b182eab53a79eec236a9cee1e0fc', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 13:01:21', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('54c2bad38dafd9e636ce992aa93b26af', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-07 11:57:14', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5554869b3475770046602061775e0e57', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 14:38:05', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('557b3c346d9bc8f7a83fac9f5b12dc1b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 12:20:28', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('55d649432efa7eaecd750b4b6b883f83', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 22:44:19', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('55e906361eeabb6ec16d66c7196a06f0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 12:50:20', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('580256f7c7ea658786dba919009451b6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 17:39:43', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5858f2f8436460a94a517904c0bfcacb', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 23:42:21', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('586002e1fb4e60902735070bab48afe3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-27 16:18:52', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('586e8244eff6d6761077ef15ab9a82d9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 23:03:51', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5902fb4ba61ccf7ff4d2dd97072b7e5b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:48:30', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('59558082e1b1d754fa3def125ed4db3c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 18:24:19', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c04e3d9429e3bcff4d55f6205c4aa83', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 18:14:29', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c48703e3a2d4f81ee5227f0e2245990', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-04 23:12:04', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c7e834e089ef86555d8c2627b1b29b5', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 11:25:26', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5d8ed15778aa7d99224ee62c606589fb', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-30 15:51:02', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5e8bac7831de49146d568c9a8477ddad', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:16:37', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5ea258e1f478d27e0879e2f4bcb89ecd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 14:01:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5ee6d5fe1e6adcc4ad441b230fae802d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-11 15:56:33', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5f00b5514a11cd2fe240c131e9ddd136', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 16:30:52', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('60886d5de8a18935824faf8b0bed489e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 10:11:35', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('611fa74c70bd5a7a8af376464a2133e8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-27 17:48:13', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('61445cc950f5d04d91339089b18edef9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:13:22', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('615625178b01fc20c60184cd28e64a70', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 09:47:13', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('61aac4cfe67ec6437cd901f95fbd6f45', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 17:40:21', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('61d2d2fd3e9e23f67c23b893a1ae1e72', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 22:44:56', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('62e208389a400e37250cfa51c204bdc8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 17:44:49', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('636309eec5e750bc94ce06fb98526fb2', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-30 18:15:03', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('636d37d423199e15b4030f35c60859fe', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-08 10:07:21', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('63ccf8dda5d9bf825ecdbfb9ff9f456c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.105', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 12:14:08', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('642e48f2e5ac8fe64f1bfacf4d234dc8', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-25 12:51:21', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('64711edfb8c4eb24517d86baca005c96', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:41:11', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('64c00f27ddc93fda22f91b38d2b828b5', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 17:34:45', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('65771bce3f5786dfb4d84570df61a47a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 22:07:57', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('65be8e015c9f2c493bd0a4e405dd8221', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 11:40:20', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6664dc299f547f6702f93e2358810cc1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.3.22', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-05 21:04:14', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('671a44fd91bf267549d407e0c2a680ee', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 22:45:16', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('672b527c49dc349689288ebf2c43ed4d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-10 11:26:47', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('675153568c479d8b7c6fe63327066c9f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 15:29:42', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6836a652dc96246c028577e510695c6f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-15 20:47:02', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('687810e7fea7e480962c58db515a5e1c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 18:42:30', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('689b8f2110f99c52e18268cbaf05bbb6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 09:58:44', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('68df65639e82cc6a889282fbef53afbb', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-08 15:01:37', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('68e90e08a866de748e9901e923406959', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 12:37:06', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('69a9dfb2fb02e4537b86c9c5c05184ae', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.104', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 15:22:14', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('69baa4f883fe881f401ea063ddfd0079', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-12 20:51:03', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('69e6fd7891d4b42b0cccdc0874a43752', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:45:58', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('69fc2407b46abad64fa44482c0dca59f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-01 12:04:25', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6a67bf2ff924548dee04aa97e1d64d38', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:52:41', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6b4cdd499885ccba43b40f10abf64a78', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 13:04:43', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6c558d70dc5794f9f473d8826485727a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 18:38:44', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6cf638853ef5384bf81ed84572a6445d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 19:25:24', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6cfeaf6a6be5bb993b9578667999c354', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-24 11:43:34', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6d45672f99bbfd01d6385153e9c3ad91', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 13:49:20', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6d93d5667245ef8e5d6eafdbc9113f51', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:34:42', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('703fbcb7e198e8e64978ec0518971420', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 17:53:55', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('70849167f54fd50d8906647176d90fdf', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 23:12:29', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('709b0f2bf8cb8f785f883509e54ace28', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:37:37', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7148b3d58f121ef04bcbea5dd2e5fe3b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:35:23', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('716f9f5f066a6f75a58b7b05f2f7f861', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 11:59:01', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7268539fbe77c5cc572fb46d71d838f1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 13:22:48', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('72ee87d0637fb3365fdff9ccbf286c4a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 17:36:44', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('74209dfc97285eb7919868545fc2c649', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 11:23:08', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('74c991568d8bcb2049a0dbff53f72875', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 22:12:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('75c7fa1a7d3639be1b112e263561e43a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-14 17:07:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('772f238d46531a75fff31bae5841057c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 11:31:16', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('776c2e546c9ab0375d97590b048b8a9d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:13:51', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('77a329e5eb85754075165b06b7d877fd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 13:25:17', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('78caf9e97aedfb8c7feef0fc8fdb4fb5', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-10 17:04:46', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('790b722fa99a8f3a0bc38f61e13c1cf4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 18:34:07', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('79a1737fcc199c8262f344e48afb000d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 23:25:25', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('79e76353faffd0beb0544c0aede8564f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-08 17:28:14', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7a511b225189342b778647db3db385cd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 20:50:10', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7a99cf653439ca82ac3b0d189ddaad4a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 10:41:34', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7a9d307d22fb2301d6a9396094afc82f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-14 18:45:04', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7ae9cad197aee3d50e93bc3a242d68ec', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-10 13:12:26', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7b2b322a47e1ce131d71c50b46d7d29e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-06 15:55:20', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7b44138c1b80b67da13b89db756a22b0', 2, '添加测试DEMO', NULL, NULL, NULL, '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', NULL, '[{\"createBy\":\"jeecg-boot\",\"createTime\":1553824172062,\"id\":\"5fce01cb7f0457746c97d8ca05628f81\",\"name\":\"1212\"}]', NULL, 25, 'jeecg-boot', '2019-03-29 09:49:32', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7ba3df5d2612ac3dd724e07a55411386', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:35:03', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7c88e9cf6018a1b97b420b8cb6122815', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-28 19:46:30', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7ce1934fb542a406e92867aec5b7254d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-11 14:53:23', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7d11535270734de80bd52ec0daa4fc1f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.105', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 12:20:14', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7d8539ff876aad698fba235a1c467fb8', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 09:47:18', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7e92abdc0c1f54596df499a5a2d11683', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:59:34', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7efea1a687e28610a7ccdfc04b27c66a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-12 12:10:02', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7f31435ca2f5a4ef998a4152b2433dec', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 18:36:40', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7f9c3d539030049a39756208670be394', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 11:44:40', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7feae2fb5001ca0095c05a8b08270317', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 12:17:31', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('802cec0efbe9d862b7cea29fefc5448b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 18:58:50', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('810deb9fd39fa2f0a8e30e3db42f7c2b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 18:51:00', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('82cee1c403025fc1db514c60fc7d8d29', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 16:41:50', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('845f732f6a0f0d575debc4103e92bea2', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 10:17:18', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('85780936e962607ee10ce7b6b4838d66', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-12 10:43:38', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('85b3106d757d136b48172a9ab1f35bb6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 18:34:34', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('862aa0e6e101a794715174eef96f7847', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:41:09', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('87885bc889d23c7c208614da8e021fb0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-07 10:23:22', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8802209912ca66d56f2ea241ffd0cc13', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 17:52:34', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('88bab180edf685549c7344ec8db7d954', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 19:07:06', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('88d7136ed5c7630057451816dbaff183', 1, '用户名: jeecg,退出成功！', NULL, 'jeecg', 'jeecg', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-03-25 13:01:24', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('89bfd8b9d6fa57a8e7017a2345ec1534', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-12 09:27:32', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('89d2bc84e056f327291c53821d421034', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-08 16:57:46', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('89fbc93e77defb34c609c84a7fe83039', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 09:47:14', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8a13971104d70e35111d10dd99de392e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:34:25', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8b2ad448021fbb5509ea04c9a780b165', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.104', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 14:35:25', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8d105ea6c89691bc8ee7d4fd568aa690', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 11:39:33', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8d9ce65020320d46882be43b22b12a62', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 10:56:43', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8e03def9e0283005161d062d4c0a5a80', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:46:09', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8fde5f89e8ad30cf3811b8683a9a77b1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 18:02:41', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8fe913a5b037943c6667ee4908f88bea', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-16 11:18:48', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8ff27392165c8c707ee10ec0010c7bb8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 11:44:07', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('90555a39c0b02180df74752e4d33f253', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-27 18:26:25', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('905d2cf4308f70a3a2121a3476e38ed0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 14:00:30', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('90711ddb861e28bd8774631c98f3edb9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 11:57:42', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('90b4bad7939233a1e0d7935f079ea0fa', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-25 12:45:54', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('917dbb5db85d1a6f142135827e259bbf', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 20:21:03', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('939b3ff4733247a47efe1352157b1f27', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 19:01:06', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('93b4d26f60d7fb45a60524760bf053e4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 22:20:06', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('93bb98ba996dacebfb4f61503067352e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 22:47:28', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9410b7974fbc9df415867095b210e572', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-16 11:18:58', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('95063e0bdfa5c9817cc0f66e96baad93', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 17:59:16', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('952947331f8f3379494c4742be797fc3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 18:42:11', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('95d906e6f048c3e71ddbcc0c9448cf49', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-26 19:23:30', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('96d7fe922f46123e0497e22dedf89328', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-07 23:10:48', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('973b3d25b4defcac4fe8c62bd0b7dd8e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-12 19:01:30', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('98b7fc431e4654f403e27ec9af845c7b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 17:31:38', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('98d4b573769af6d9c10cd5c509bfb7af', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-16 11:21:25', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('997bb4cb1ad24439b6f7656222af0710', 2, '添加测试DEMO', NULL, NULL, NULL, '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', NULL, '[{\"createBy\":\"jeecg-boot\",\"createTime\":1553824768819,\"id\":\"ee84471f0dff5ae88c45e852bfa0280f\",\"keyWord\":\"22\",\"name\":\"222\"}]', NULL, 5, 'jeecg-boot', '2019-03-29 09:59:28', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9a1456ef58a2b1fb63cdc54b723f2539', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 17:26:39', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9a5c1fbf3543880af6461182e24b75db', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-15 13:51:27', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9b23981621d5265a55681883ec19fa91', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:46:05', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9b568a868e57f24c5ba146848061613f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.104', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 20:09:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9b7a830914668881335da1b0ce2274b1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 17:19:02', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9c1148d0c35f9ae2d77ccddee84f6761', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-12 22:35:43', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9c32ec437d8f8d407b1bd1165fc0305d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-08 15:01:25', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9d0416e09fae7aeeeefc8511a61650c2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 18:15:05', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9db7e7d214dbe9fe8fff5ff20634e282', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 11:19:34', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9df97c1b3213aa64eda81c6bf818b02b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 22:42:48', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9e9d01c430b72703ce3a94589be54bbe', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 18:26:06', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9eb3fb6d9d45e3847a88f65ed47da935', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '192.168.3.22', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-05 20:52:47', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9ed114408a130e69c0de4c91b2d6bf7e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 13:03:40', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a052befb699ee69b3197b139fd9263f0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-15 17:34:58', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a1b870eee811cfa4960f577b667b0973', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 18:23:44', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a28de45f52c027a3348a557efab6f430', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 10:34:26', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a2950ae3b86f786a6a6c1ce996823b53', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 09:47:11', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a2e0435673b17f4fb848eecdf8ecacd6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 21:32:37', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a42e5cd05566ea226c2e2fc201860f2c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 11:15:50', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a521d9f2a0087daa37923fa704dea85b', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-25 12:45:52', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a56661bbc72b8586778513c71f4764f5', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:53:09', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a5848ab4e8d0fb6ecf71ee1d99165468', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 22:14:50', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a5daa58b078cb8b3653af869aeecebd0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-29 17:14:31', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a61d9db83888d42b0d24621de48a880d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-05 22:49:48', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a6209166e1e9b224cca09de1e9ea1ed7', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:41:10', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a6261bbbf8e964324935722ea1384a5d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 19:46:27', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a63147887c6ca54ce31f6c9e6279a714', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 09:19:07', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a6971e63e3d9158020e0186cda81467d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 14:59:23', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a69f4ff4e48754de96ae6fa4fabc1579', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 09:18:48', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a6c3b28530416dace21371abe8cae00b', 2, '删除测试DEMO', NULL, NULL, NULL, '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.delete()', NULL, '[\"ee84471f0dff5ae88c45e852bfa0280f\"]', NULL, 9, 'jeecg-boot', '2019-03-29 09:59:48', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a710ed2de7e31fd72b1efb1b54ba5a87', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 15:30:43', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a7d1f4a774eb8644e2b1d37ca5f93641', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-08 10:16:03', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a7ee4b4c236bc0e8f56db5fdf1e5ac38', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 13:21:36', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a83e37b55a07fe48272b0005a193dee6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-11 09:17:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a867c282a8d97f7758235f881804bb48', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-13 18:28:20', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a8c7ba2d11315b171940def2cbeb0e8f', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-25 13:01:40', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a9bd713f975bfbff87638432a104b715', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.104', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 20:04:44', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('aa49341b29865b45588ad2f9b89c47ea', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-11 19:42:42', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ab1d707bbfdf44aa17307d30ca872403', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 15:50:42', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ab550d09101a88bc999ea57cbb05aa5a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-07 17:59:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ac8cf22c2f10a38c7a631fc590551c40', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 12:04:16', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ad97829fe7fefcd38c80d1eb1328e40f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 09:28:18', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('add13f513772a63f8ca8bf85634bb72c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 13:09:03', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ae61be664d2f30d4f2248347c5998a45', 1, '用户名: jeecg,退出成功！', NULL, 'jeecg', 'jeecg', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-03-25 12:53:17', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('aeb738ab880c262772453d35fc98f2f2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-07 18:50:30', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('aec0817ecc0063bde76c1f6b6889d117', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:47:06', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('aeca30df24ce26f008a7e2101f7c513c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 12:27:53', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('af5869701738a6f4c2c58fe8dfe02726', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 16:42:40', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('af8fe96a9f0b325e4833fc0d9c4721bf', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-30 18:14:56', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b01c3f89bcfd263de7cb1a9b0210a7af', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 17:53:55', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b0cebd174565a88bb850a2475ce14625', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 18:19:39', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b0e6b3a0ec5d8c73166fb8129d21a834', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-09 16:56:16', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b1e9797721dbfcc51bbe7182142cbdcd', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-25 12:52:25', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b3adf055f54878657611ef430f85803e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 11:33:57', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b3cceb535fa5577cc21b12502535ad29', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:29:01', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b428718441be738cf8b5ce92109068c3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-08 16:21:37', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b4c3c7af9899b9af3f42f730cfabc9b2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 12:17:58', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b53c9e8ce1e129a09a3cda8c01fe644c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 11:38:49', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b5f6636c6e24e559ddf1feb3e1a77fd5', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 12:14:05', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b7085f003b4336af4d4ba18147f8e5ae', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 22:29:37', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b7478d917ab6f663e03d458f0bb022a3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:50:55', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b7f33b5a514045878447fc64636ac3e6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 22:00:34', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b86958d773b2c2bd79baa2e8c3c84050', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-15 16:49:36', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b8bd2a9de3fb917dfb6b435e58389901', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 20:13:31', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b954f7c34dfbe9f6a1fc12244e0a7d59', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:41:10', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b972484d206b36420efac466fae1c53f', 1, '用户名: jeecg,退出成功！', NULL, 'jeecg', 'jeecg', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-03-25 12:54:38', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('baa53d6a534e669f6150ea47565fa5b9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-09 17:27:24', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('bbd3e1f27e025502a67cf54945b0b269', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-31 22:13:16', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('bc28d4275c7c7fcd067e1aef40ec1dd4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 10:53:03', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('bc594b8921a0bcdb26d4a93916316092', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:42:58', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('bd6d7d720b9dd803f8ad26e2d40870f3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-10 11:04:06', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('bd9167a87aee4574a30d67825acaad0a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 17:51:03', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('bdeae62057ae9858b6a546c1bdb6efc7', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 11:49:29', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('bdfd95b4d4c271d7d8d38f89f4a55da9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 17:59:12', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('beb9ef68b586f05bd7cf43058e01ad4a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 22:29:02', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('befbcf5a27ef8d2ca8e6234077f9413d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 16:01:33', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('bfec8c8c88868391041667d924e3af7f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 14:38:27', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c03985d6e038b5d8ebdeec27fce249ba', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:43:24', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c1a68605bee6b3d1264390c1cfe7a9fa', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 20:49:55', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c21422fa08f8480a53367fda7ddddf12', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 10:02:30', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c434dc5172dc993ee7cd96187ca58653', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-28 19:46:08', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c5d4597b38275dcb890c6568a7c113f2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-24 12:18:46', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c665d704539483630cc9ed5715ed57a8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:10:12', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c66e22782dd3916d1361c76b0cc4ec8a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 22:44:06', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c6cbe54fcb194d025a081e5f91a7e3f0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 10:26:38', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c72bb25acd132303788699834ae039b4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 12:06:06', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c98a6367b152cf5311d0eec98fab390c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 22:13:34', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ca737885d9034f71f70c4ae7986fafa8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 22:47:28', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('caee69e55ec929f7ba904280cac971e6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 11:49:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('cb7c6178101ef049d3f1820ee41df539', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 19:59:19', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('cbd720f20fc090c7350a98be0738816a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.104', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 20:55:19', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('cbf83d11486a8d57814ae38c9822b022', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:05:39', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('cc39057ae0a8a996fb0b3a8ad5b8f341', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 10:20:05', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('cd064a2f6cb6c640cb97a74aaa6041d7', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:17:10', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('cd5af66a87bb40026c72a748155b47e8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:47:26', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('cd7a7c49e02ca9613b6879fda4e563cf', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 12:29:08', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ce6aa822166b97a78b0bbea62366f8e0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 20:14:29', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ce9893f4d0dd163e900fcd537f2c292d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 11:55:55', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('cedf399271592c27dcb6f6ce3312e77d', 2, '删除测试DEMO', NULL, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.delete()', NULL, '[\"7501\"]', NULL, 24, 'admin', '2019-03-06 16:03:13', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('cfba34db2d7fbb15a2971212f09b59ec', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 11:51:43', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d00964eee24c6f9a8609a42eeebef957', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-10 17:04:48', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d01d658731dac4b580a879d986b03456', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-07 15:00:37', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d0ce9bfc790a573d48d49d3bbbf1a1cb', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 22:09:06', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d18bff297a5c2fa54d708f25a7d790d6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 12:13:05', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d23e6766cecf911fb2e593eeee354e18', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 18:42:35', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d2fe98d661f1651b639bf74499f124db', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-29 10:16:29', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d3b54be0510db6a6da27bf30becb5335', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-11 19:42:46', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d3c4f120d8a23b62ec9e24b431a58496', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-07 14:17:24', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d4ef00700436645680657f72445d38db', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-11 18:05:29', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d5b9e5d9bfbbd8e6d651087ead76d9f7', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 20:17:41', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d68957c067fb27e80a23babebdb1591f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 17:55:06', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d6aaf0f8e2428bf3c957becbf4bcedb4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:38:14', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d762a1cba3dc23068f352323d98909e0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 22:26:49', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d7e7cb4c21372e48b8e0ec7e679466e3', 1, '用户名: null,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:02:34', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d7e8a7f14967c70d68f5569cb4d11d0a', 2, '删除测试DEMO', NULL, NULL, NULL, '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.delete()', NULL, '[\"5fce01cb7f0457746c97d8ca05628f81\"]', NULL, 9, 'jeecg-boot', '2019-03-29 09:49:39', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d82b170459d99fc05eb8aa1774e1a1c9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-26 18:45:14', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d869534109332e770c70fad65ef37998', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 10:02:30', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d8c43edd685431ab3ef7b867efc29214', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 17:37:18', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d916bd1d956418e569549ee1c7220576', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 19:18:42', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d98115c02c0ac478a16d6c35de35053d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 12:50:09', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d9e0150666b69cced93eb4defb19788b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 23:11:12', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('da3fda67aea2e565574ec2bcfab5b750', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-08 22:36:20', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('db2b518e7086a0561f936d327a0ab522', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 22:39:23', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('dcbee150c5158457b8bb696e5a74aa89', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-14 15:22:25', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('dcfe23b155d5c6fa9a302c063b19451e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 18:47:21', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('dd4e1ab492e59719173d8ae0f5dbc9a2', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-11 19:47:12', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('de37620b6921abcfe642606a0358d30f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-09 15:42:46', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('de938485a45097d1bf3fa311d0216ed4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 10:15:22', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('de978382f59685babf3684d1c090d136', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-14 12:55:55', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('dfacaa7c01ccf0bade680044cced3f11', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 15:25:10', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e01ed1516e8ae3a2180acbd4e4508fa5', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 20:28:12', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e088a2607864d3e6aadf239874d51756', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-26 18:46:56', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e09bb0a74c268a9aaf1f94edcc2eb65a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-08 18:26:14', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e169938510c9320cb1495ddb9aabb9d1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:47:40', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e1d0b1fd3be59e465b740e32346e85b0', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 10:16:37', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e1d1fc464cf48ec26b7412585bdded1a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:49:15', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e1fa52ecbcc0970622cc5a0c06de9317', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-03 18:33:04', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e232f89df26cc9e5eced10476c4e4a2b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-10 10:05:11', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e234abc35a52f0dd2512b0ce2ea0e4f2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 20:05:26', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e2b6d0e751f130d35c0c3b8c6bd2a77e', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-27 16:18:40', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e3031f999984909f9048d8ec15543ad0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 18:43:02', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e39f051ba6fdb7447f975421f3b090a7', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-05 12:49:18', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e48a6bd82c92a8005c80c5ef36746117', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 19:32:26', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e4a166fcd0fc4037cb26e35cc1fb87b2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 18:32:41', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e4c06405615399d6b1ebea45c8112b4d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 12:07:26', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e78f8832d61c1603c17767ee2b78ef07', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-01 19:50:03', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e7f2b0a7493e7858c5db1f1595fa54b1', 2, '添加测试DEMO', NULL, NULL, NULL, '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', NULL, '[{\"createBy\":\"jeecg-boot\",\"createTime\":1553824376817,\"id\":\"e771211b77cd3b326d3e61edfd9a5a19\",\"keyWord\":\"222\",\"name\":\"222\"}]', NULL, 7, 'jeecg-boot', '2019-03-29 09:52:56', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e864c0007983211026d6987bd0cd4dc8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.114', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 13:37:08', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e8b37ad67ef15925352a4ac3342cef07', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:38:10', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e92544c6102243e7908e0cbb217f5198', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 12:48:28', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e93f1a170e3cd33f90dd132540c7a39b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:12:43', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e9d3202c14f7f2812346fb4c2b781c67', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.104', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 21:38:36', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ea5f9191b0f593a1d6cb585538caa815', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:46:08', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ea66ed22fde49640cee5d73c6ef69718', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 11:50:04', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('eaf74cd1489b02d39c470eed131fc918', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 18:37:39', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('eb6f5196de91dd2e8316696bddd61345', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 22:26:36', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('eb9a522fd947c7a706c5a106ca32b8c9', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 17:50:17', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ecfee5b948602a274093b8890e5e7f3f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:05:11', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ed2740de487c684be9fa3cf72113ae30', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:51:43', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ed9b4ffc8afab10732aac2d0f84c567b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 19:10:52', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ef54197116da89bf091c0ed58321eea4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 19:22:06', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ef7219725c4b84cc71f56f97a8eab01a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 12:08:00', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ef7669ac0350730d198f59b8411b19d1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:28:43', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('efe77038e00cfff98d6931c3e7a4c3d6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-06 16:20:19', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f0409312093beb563ac4016f2b2c6dfd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 13:24:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f06048c147c5bcdbed672e32b2c86b1c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 14:07:28', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f0748a25728348591c7b73a66f273457', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-28 19:46:27', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f1186792c6584729a0f6da4432d951f9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-14 21:45:52', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f20cf3fe228ba6196a48015b98d0d354', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 19:25:22', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f21e30d73c337ea913849ed65808525c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-27 10:23:22', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f29f3b7b7e14b1389a0c53d263c0b26b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-29 17:44:25', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f2ce8024e62740f63c134c3cfb3cae23', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:07:41', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f3cafb545e5693e446f641fa0b5ac8cd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 17:07:56', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f3e1f7fb81004ccd64df12d94ef1e695', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 21:30:37', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f43e38800d779422c75075448af738d1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 18:47:11', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f540eff3f6e86c1e0beccd300efd357f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 15:15:26', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f543c25bdd741055aeb4f77c5b5acf58', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:40:58', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f58e160e97d13a851f59b70bf54e0d06', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 20:11:58', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f74f759b43afa639fd1c4f215c984ae0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 22:08:18', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f78e24f5e841acac2a720f46f6c554bc', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:47:17', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f79af48e6aeb150432640483f3bb7f2a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:09:11', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f93279c6899dc5e6cec975906f8bf811', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:47:20', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f95d517f43ba2229c80c14c1883a4ee9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 12:11:59', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('fa9b4d7d42bc9d1ba058455b4afedbfb', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-25 12:59:46', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('faad055dd212ed9506b444f8f1a920b9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 12:00:38', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('fb2871cda1421b766f8e68cb36a22bf3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 17:35:01', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('fb73d58bf6503270025972f99e50335d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 11:57:56', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('fc69a1640a4772c8edf2548d053fa6de', 1, '用户名: admin,登录成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-23 14:55:33', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('fd4a496c56bacd76f93f95a2836965c8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-12 18:35:11', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('fded8eb5d78d13791baec769019fee54', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:15:07', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('fe0dc06eaef69047131f39052fcce5c4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 13:56:05', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('feaf7c377abc5824c1757d280dd3c164', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 11:58:54', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ff3f7dbda20cd2734b1238fa5ba17fcf', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-10 11:26:43', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ffac84fff3c65bb17aa1bda3a0d2029e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-06 20:10:50', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_log`(`id`, `log_type`, `log_content`, `operate_type`, `userid`, `username`, `ip`, `method`, `request_url`, `request_param`, `request_type`, `cost_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ffc6178ffa099bb90b9a4d0a64dae42b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 18:28:32', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('020b06793e4de2eee0007f603000c769', 'f0675b52d89100ee88472b6800754a08', 'ViserChartDemo', '/report/ViserChartDemo', 'jeecg/report/ViserChartDemo', NULL, NULL, 1, NULL, 0.00, 0, NULL, 1, 1, 0, NULL, 'admin', '2019-04-03 19:08:53', 'admin', '2019-04-12 10:54:29', 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('024f1fd1283dc632458976463d8984e1', '700b7f95165c46cc7a78bf227aa8fed3', 'Tomcat信息', '/monitor/TomcatInfo', 'monitor/TomcatInfo', NULL, NULL, 1, NULL, 4.00, 0, NULL, 1, 1, 0, NULL, 'admin', '2019-04-02 09:44:29', 'admin', '2019-04-02 11:36:25', 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('109c78a583d4693ce2f16551b7786786', 'e41b69c57a941a3bbcce45032fe57605', 'Online报表配置', '/online/cgreport', 'modules/online/cgreport/OnlCgreportHeadList', NULL, NULL, 1, NULL, 2.00, 0, NULL, 1, 1, 0, NULL, 'admin', '2019-03-08 10:51:07', 'admin', '2019-03-30 19:04:28', 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('2aeddae571695cd6380f6d6d334d6e7d', 'f0675b52d89100ee88472b6800754a08', '布局统计报表', '/report/ArchivesStatisticst', 'jeecg/report/ArchivesStatisticst', NULL, NULL, 1, NULL, 1.00, 0, NULL, 1, 1, 0, NULL, 'admin', '2019-04-03 18:32:48', NULL, NULL, 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('4356a1a67b564f0988a484f5531fd4d9', '2a470fc0c3954d9dbb61de6d80846549', '内嵌Table', '/jeecg/TableExpandeSub', 'jeecg/TableExpandeSub', NULL, NULL, 1, NULL, 1.00, 0, NULL, 1, 1, 0, NULL, 'admin', '2019-04-04 22:48:13', NULL, NULL, 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('5c8042bd6c601270b2bbd9b20bccc68b', '', '消息中心', '/message', 'layouts/RouteView', NULL, NULL, 0, NULL, 6.00, 0, 'message', 1, 0, 0, NULL, 'admin', '2019-04-09 11:05:04', 'admin', '2019-04-11 19:47:54', 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('700b7f95165c46cc7a78bf227aa8fed3', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '性能监控', '/monitor', 'layouts/RouteView', NULL, NULL, 1, NULL, 0.00, 0, NULL, 1, 0, 0, NULL, 'admin', '2019-04-02 11:34:34', 'admin', '2019-04-14 15:21:54', 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('7593c9e3523a17bca83b8d7fe8a34e58', '3f915b2769fc80648e92d04e84ca059d', '添加用户按钮', '', NULL, NULL, NULL, 2, 'user:add', 1.00, 0, NULL, 1, 1, 0, NULL, 'admin', '2019-03-16 11:20:33', 'admin', '2019-03-16 11:54:15', 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('7960961b0063228937da5fa8dd73d371', '2a470fc0c3954d9dbb61de6d80846549', 'JEditableTable示例', '/jeecg/JEditableTable', 'jeecg/JeecgEditableTableExample', NULL, NULL, 1, NULL, 7.00, 0, NULL, 1, 1, 0, NULL, 'admin', '2019-03-22 15:22:18', NULL, NULL, 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('841057b8a1bef8f6b4b20f9a618a7fa6', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '数据日志', '/sys/dataLog-list', 'system/DataLogList', NULL, NULL, 1, NULL, 1.00, 0, NULL, 1, 1, 0, NULL, 'admin', '2019-03-11 19:26:49', 'admin', '2019-03-12 11:40:47', 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('8b3bff2eee6f1939147f5c68292a1642', '700b7f95165c46cc7a78bf227aa8fed3', '服务器信息', '/monitor/SystemInfo', 'monitor/SystemInfo', NULL, NULL, 1, NULL, 4.00, 0, NULL, 1, 1, 0, NULL, 'admin', '2019-04-02 11:39:19', 'admin', '2019-04-02 15:40:02', 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('8d1ebd663688965f1fd86a2f0ead3416', '700b7f95165c46cc7a78bf227aa8fed3', 'Redis监控', '/monitor/redis/info', 'monitor/RedisInfo', NULL, NULL, 1, NULL, 1.00, 0, NULL, 1, 1, 0, NULL, 'admin', '2019-04-02 13:11:33', 'admin', '2019-04-02 13:24:43', 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('944abf0a8fc22fe1f1154a389a574154', '5c8042bd6c601270b2bbd9b20bccc68b', '消息管理', '/modules/message/sysMessageList', 'modules/message/SysMessageList', NULL, NULL, 1, NULL, 1.00, 0, NULL, 1, 1, 0, NULL, 'admin', '2019-04-09 11:27:53', 'admin', '2019-04-09 19:31:23', 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('9a90363f216a6a08f32eecb3f0bf12a3', '2a470fc0c3954d9dbb61de6d80846549', '常用选择组件', '/jeecg/SelectDemo', 'jeecg/SelectDemo', NULL, NULL, 1, NULL, 1.00, 0, NULL, 1, 1, 0, NULL, 'admin', '2019-03-19 11:19:05', 'admin', '2019-04-10 15:36:50', 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('9cb91b8851db0cf7b19d7ecc2a8193dd', '1939e035e803a99ceecb6f5563570fb2', '我的任务表单', '/modules/bpm/task/form/FormModule', 'modules/bpm/task/form/FormModule', NULL, NULL, 1, NULL, 1.00, 0, NULL, 1, 1, 0, NULL, 'admin', '2019-03-08 16:49:05', 'admin', '2019-03-08 18:37:56', 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('9fe26464838de2ea5e90f2367e35efa0', 'e41b69c57a941a3bbcce45032fe57605', 'AUTO在线报表', '/online/cgreport/:code', 'modules/online/cgreport/auto/OnlCgreportAutoList', 'onlineAutoList', NULL, 1, NULL, 6.00, 0, NULL, 1, 1, 0, NULL, 'admin', '2019-03-12 11:06:48', 'admin', '2019-04-11 19:52:38', 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('c431130c0bc0ec71b0a5be37747bb36a', '2a470fc0c3954d9dbb61de6d80846549', '一对多JEditable', '/jeecg/JeecgOrderMainListForJEditableTable', 'jeecg/JeecgOrderMainListForJEditableTable', NULL, NULL, 1, NULL, 3.00, 0, NULL, 1, 1, 0, NULL, 'admin', '2019-03-29 10:51:59', 'admin', '2019-04-04 20:09:39', 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('d07a2c87a451434c99ab06296727ec4f', '700b7f95165c46cc7a78bf227aa8fed3', 'JVM信息', '/monitor/JvmInfo', 'monitor/JvmInfo', NULL, NULL, 1, NULL, 4.00, 0, NULL, 1, 1, 0, NULL, 'admin', '2019-04-01 23:07:48', 'admin', '2019-04-02 11:37:16', 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('e41b69c57a941a3bbcce45032fe57605', '', '在线开发', '/online', 'layouts/RouteView', NULL, NULL, 0, NULL, 5.00, 0, 'cloud', 1, 0, 0, NULL, 'admin', '2019-03-08 10:43:10', 'admin', '2019-03-31 22:20:35', 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('f0675b52d89100ee88472b6800754a08', '', '统计报表', '/report', 'layouts/RouteView', NULL, NULL, 0, NULL, 7.00, 0, 'bar-chart', 1, 0, 0, NULL, 'admin', '2019-04-03 18:32:02', 'admin', '2019-04-11 19:48:39', 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('f780d0d3083d849ccbdb1b1baee4911d', '5c8042bd6c601270b2bbd9b20bccc68b', '模板管理', '/modules/message/sysMessageTemplateList', 'modules/message/SysMessageTemplateList', NULL, NULL, 1, NULL, 1.00, 0, NULL, 1, 1, 0, NULL, 'admin', '2019-04-09 11:50:31', 'admin', '2019-04-12 10:16:34', 0);

INSERT INTO `jeecg_boot`.`sys_permission`(`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES ('fc810a2267dd183e4ef7c71cc60f4670', '700b7f95165c46cc7a78bf227aa8fed3', '请求追踪', '/monitor/HttpTrace', 'monitor/HttpTrace', NULL, NULL, 1, NULL, 4.00, 0, NULL, 1, 1, 0, NULL, 'admin', '2019-04-02 09:46:19', 'admin', '2019-04-02 11:37:27', 0);

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = '', `name` = '系统监控', `url` = '/dashboard3', `component` = 'layouts/RouteView', `component_name` = NULL, `redirect` = NULL, `menu_type` = 0, `perms` = NULL, `sort_no` = 6.00, `always_show` = 0, `icon` = 'dashboard', `is_route` = 1, `is_leaf` = 0, `hidden` = 0, `description` = NULL, `create_by` = NULL, `create_time` = '2018-12-25 20:34:38', `update_by` = 'admin', `update_time` = '2019-03-31 22:19:58', `del_flag` = 0 WHERE `id` = '08e6b9dc3c04489c8e1ff2ce6f105aa4';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = '6e73eb3c26099c191bf03852ee1310a1', `name` = '基本设置', `url` = '/account/settings/base', `component` = 'account/settings/BaseSetting', `component_name` = NULL, `redirect` = NULL, `menu_type` = 1, `perms` = 'BaseSettings', `sort_no` = NULL, `always_show` = 0, `icon` = NULL, `is_route` = 1, `is_leaf` = 1, `hidden` = 1, `description` = NULL, `create_by` = NULL, `create_time` = '2018-12-26 18:58:35', `update_by` = 'admin', `update_time` = '2019-03-20 12:57:31', `del_flag` = 0 WHERE `id` = '1367a93f2c410b169faa7abcbad2f77c';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = 'e3c13679c73a4f829bcff2aba8fd68b1', `name` = '基础表单', `url` = '/form/base-form', `component` = 'form/BasicForm', `component_name` = NULL, `redirect` = NULL, `menu_type` = 1, `perms` = NULL, `sort_no` = 1.00, `always_show` = 0, `icon` = NULL, `is_route` = 1, `is_leaf` = 0, `hidden` = 0, `description` = NULL, `create_by` = NULL, `create_time` = '2018-12-25 20:34:38', `update_by` = 'admin', `update_time` = '2019-02-26 17:02:08', `del_flag` = 0 WHERE `id` = '277bfabef7d76e89b33062b16a9a5020';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = '', `name` = '常见案例', `url` = '/jeecg', `component` = 'layouts/RouteView', `component_name` = NULL, `redirect` = NULL, `menu_type` = 0, `perms` = NULL, `sort_no` = 7.00, `always_show` = 0, `icon` = 'qrcode', `is_route` = 1, `is_leaf` = 0, `hidden` = 0, `description` = NULL, `create_by` = NULL, `create_time` = '2018-12-25 20:34:38', `update_by` = 'admin', `update_time` = '2019-04-02 11:46:42', `del_flag` = 0 WHERE `id` = '2a470fc0c3954d9dbb61de6d80846549';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = '08e6b9dc3c04489c8e1ff2ce6f105aa4', `name` = '在线文档', `url` = '{{ window._CONFIG[\'domianURL\'] }}/swagger-ui.html#/', `component` = 'layouts/IframePageView', `component_name` = NULL, `redirect` = NULL, `menu_type` = 1, `perms` = NULL, `sort_no` = 3.00, `always_show` = 0, `icon` = NULL, `is_route` = 1, `is_leaf` = 1, `hidden` = 0, `description` = NULL, `create_by` = 'admin', `create_time` = '2019-01-30 10:00:01', `update_by` = 'admin', `update_time` = '2019-03-23 19:44:43', `del_flag` = 0 WHERE `id` = '2dbbafa22cda07fa5d169d741b81fe12';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = '', `name` = '结果页', `url` = '/result', `component` = 'layouts/PageView', `component_name` = NULL, `redirect` = NULL, `menu_type` = 0, `perms` = NULL, `sort_no` = 8.00, `always_show` = 0, `icon` = 'check-circle-o', `is_route` = 1, `is_leaf` = 0, `hidden` = 0, `description` = NULL, `create_by` = NULL, `create_time` = '2018-12-25 20:34:38', `update_by` = 'admin', `update_time` = '2019-04-02 11:46:56', `del_flag` = 0 WHERE `id` = '2e42e3835c2b44ec9f7bc26c146ee531';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = 'd7d6e2e4e2934f2c9385a623fd98c6f3', `name` = '用户管理', `url` = '/isystem/user', `component` = 'system/UserList', `component_name` = NULL, `redirect` = NULL, `menu_type` = 1, `perms` = NULL, `sort_no` = 1.00, `always_show` = 0, `icon` = NULL, `is_route` = 1, `is_leaf` = 0, `hidden` = 0, `description` = NULL, `create_by` = NULL, `create_time` = '2018-12-25 20:34:38', `update_by` = 'admin', `update_time` = '2019-03-16 11:20:33', `del_flag` = 0 WHERE `id` = '3f915b2769fc80648e92d04e84ca059d';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = '2a470fc0c3954d9dbb61de6d80846549', `name` = '单表模型示例', `url` = '/jeecg/jeecgDemoList', `component` = 'jeecg/JeecgDemoList', `component_name` = 'DemoList', `redirect` = NULL, `menu_type` = 1, `perms` = NULL, `sort_no` = 1.00, `always_show` = 0, `icon` = NULL, `is_route` = 1, `is_leaf` = 1, `hidden` = 0, `description` = NULL, `create_by` = NULL, `create_time` = '2018-12-28 15:57:30', `update_by` = 'admin', `update_time` = '2019-02-15 16:24:37', `del_flag` = 0 WHERE `id` = '4148ec82b6acd69f470bea75fe41c357';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = 'd7d6e2e4e2934f2c9385a623fd98c6f3', `name` = '部门管理', `url` = '/isystem/depart', `component` = 'system/DepartList', `component_name` = NULL, `redirect` = NULL, `menu_type` = 1, `perms` = NULL, `sort_no` = 1.00, `always_show` = 0, `icon` = NULL, `is_route` = 1, `is_leaf` = 1, `hidden` = 0, `description` = NULL, `create_by` = 'admin', `create_time` = '2019-01-29 18:47:40', `update_by` = 'admin', `update_time` = '2019-03-07 19:23:16', `del_flag` = 0 WHERE `id` = '45c966826eeff4c99b8f8ebfe74511fc';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = '', `name` = '详情页', `url` = '/profile', `component` = 'layouts/RouteView', `component_name` = NULL, `redirect` = NULL, `menu_type` = 0, `perms` = NULL, `sort_no` = 8.00, `always_show` = 0, `icon` = 'profile', `is_route` = 1, `is_leaf` = 0, `hidden` = 0, `description` = NULL, `create_by` = NULL, `create_time` = '2018-12-25 20:34:38', `update_by` = 'admin', `update_time` = '2019-04-02 11:46:48', `del_flag` = 0 WHERE `id` = '4875ebe289344e14844d8e3ea1edd73f';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = '', `name` = '列表页', `url` = '/list', `component` = 'layouts/PageView', `component_name` = NULL, `redirect` = '/list/query-list', `menu_type` = 0, `perms` = NULL, `sort_no` = 9.00, `always_show` = 0, `icon` = 'table', `is_route` = 1, `is_leaf` = 0, `hidden` = 0, `description` = NULL, `create_by` = NULL, `create_time` = '2018-12-25 20:34:38', `update_by` = 'admin', `update_time` = '2019-03-31 22:20:20', `del_flag` = 0 WHERE `id` = '540a2936940846cb98114ffb0d145cb8';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = '08e6b9dc3c04489c8e1ff2ce6f105aa4', `name` = '日志管理', `url` = '/isystem/log', `component` = 'system/LogList', `component_name` = NULL, `redirect` = NULL, `menu_type` = 1, `perms` = NULL, `sort_no` = 1.00, `always_show` = 0, `icon` = '', `is_route` = 1, `is_leaf` = 1, `hidden` = 0, `description` = NULL, `create_by` = NULL, `create_time` = '2018-12-26 10:11:18', `update_by` = 'admin', `update_time` = '2019-04-02 11:38:17', `del_flag` = 0 WHERE `id` = '58857ff846e61794c69208e9d3a85466';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = NULL, `name` = '个人页', `url` = '/account', `component` = 'layouts/RouteView', `component_name` = NULL, `redirect` = NULL, `menu_type` = 0, `perms` = NULL, `sort_no` = 9.00, `always_show` = 0, `icon` = 'user', `is_route` = 1, `is_leaf` = 0, `hidden` = NULL, `description` = NULL, `create_by` = NULL, `create_time` = '2018-12-25 20:34:38', `update_by` = NULL, `update_time` = NULL, `del_flag` = 0 WHERE `id` = '717f6bee46f44a3897eca9abd6e2ec44';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = '717f6bee46f44a3897eca9abd6e2ec44', `name` = '工作台', `url` = '/dashboard/workplace', `component` = 'dashboard/Workplace', `component_name` = NULL, `redirect` = NULL, `menu_type` = 1, `perms` = NULL, `sort_no` = 3.00, `always_show` = 0, `icon` = NULL, `is_route` = 1, `is_leaf` = 1, `hidden` = 0, `description` = NULL, `create_by` = NULL, `create_time` = '2018-12-25 20:34:38', `update_by` = 'admin', `update_time` = '2019-04-02 11:45:02', `del_flag` = 0 WHERE `id` = '8fb8172747a78756c11916216b8b8066';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = '', `name` = '首页', `url` = '/dashboard/analysis', `component` = 'dashboard/Analysis', `component_name` = NULL, `redirect` = NULL, `menu_type` = 0, `perms` = NULL, `sort_no` = 0.00, `always_show` = 0, `icon` = 'home', `is_route` = 1, `is_leaf` = 1, `hidden` = 0, `description` = NULL, `create_by` = NULL, `create_time` = '2018-12-25 20:34:38', `update_by` = 'admin', `update_time` = '2019-03-29 11:04:13', `del_flag` = 0 WHERE `id` = '9502685863ab87f0ad1134142788a385';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = '08e6b9dc3c04489c8e1ff2ce6f105aa4', `name` = 'SQL监控', `url` = '{{ window._CONFIG[\'domianURL\'] }}/druid/', `component` = 'layouts/IframePageView', `component_name` = NULL, `redirect` = NULL, `menu_type` = 1, `perms` = NULL, `sort_no` = 1.00, `always_show` = 0, `icon` = NULL, `is_route` = 1, `is_leaf` = 1, `hidden` = 0, `description` = NULL, `create_by` = 'admin', `create_time` = '2019-01-30 09:43:22', `update_by` = 'admin', `update_time` = '2019-03-23 19:00:46', `del_flag` = 0 WHERE `id` = 'aedbf679b5773c1f25e9f7b10111da73';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = '08e6b9dc3c04489c8e1ff2ce6f105aa4', `name` = '定时任务', `url` = '/isystem/QuartzJobList', `component` = 'system/QuartzJobList', `component_name` = NULL, `redirect` = NULL, `menu_type` = 1, `perms` = NULL, `sort_no` = 3.00, `always_show` = 0, `icon` = NULL, `is_route` = 1, `is_leaf` = 1, `hidden` = 0, `description` = NULL, `create_by` = NULL, `create_time` = '2019-01-03 09:38:52', `update_by` = 'admin', `update_time` = '2019-04-02 10:24:13', `del_flag` = 0 WHERE `id` = 'b1cb0a3fedf7ed0e4653cb5a229837ee';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = NULL, `name` = '异常页', `url` = '/exception', `component` = 'layouts/RouteView', `component_name` = NULL, `redirect` = NULL, `menu_type` = 0, `perms` = NULL, `sort_no` = 8.00, `always_show` = NULL, `icon` = 'warning', `is_route` = 1, `is_leaf` = 0, `hidden` = NULL, `description` = NULL, `create_by` = NULL, `create_time` = '2018-12-25 20:34:38', `update_by` = NULL, `update_time` = NULL, `del_flag` = 0 WHERE `id` = 'c65321e57b7949b7a975313220de0422';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = '2a470fc0c3954d9dbb61de6d80846549', `name` = '数据回执模拟', `url` = '/jeecg/InterfaceTest', `component` = 'jeecg/InterfaceTest', `component_name` = NULL, `redirect` = NULL, `menu_type` = 1, `perms` = NULL, `sort_no` = 6.00, `always_show` = 0, `icon` = NULL, `is_route` = 1, `is_leaf` = 1, `hidden` = 0, `description` = NULL, `create_by` = 'admin', `create_time` = '2019-02-19 16:02:23', `update_by` = 'admin', `update_time` = '2019-02-21 16:25:45', `del_flag` = 0 WHERE `id` = 'c6cf95444d80435eb37b2f9db3971ae6';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = '', `name` = '系统管理', `url` = '/isystem', `component` = 'layouts/RouteView', `component_name` = NULL, `redirect` = NULL, `menu_type` = 1, `perms` = NULL, `sort_no` = 4.00, `always_show` = 0, `icon` = 'setting', `is_route` = 1, `is_leaf` = 0, `hidden` = 0, `description` = NULL, `create_by` = NULL, `create_time` = '2018-12-25 20:34:38', `update_by` = 'admin', `update_time` = '2019-03-31 22:19:52', `del_flag` = 0 WHERE `id` = 'd7d6e2e4e2934f2c9385a623fd98c6f3';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = '', `name` = '表单页', `url` = '/form', `component` = 'layouts/PageView', `component_name` = NULL, `redirect` = NULL, `menu_type` = 0, `perms` = NULL, `sort_no` = 9.00, `always_show` = 0, `icon` = 'form', `is_route` = 1, `is_leaf` = 0, `hidden` = 0, `description` = NULL, `create_by` = NULL, `create_time` = '2018-12-25 20:34:38', `update_by` = 'admin', `update_time` = '2019-03-31 22:20:14', `del_flag` = 0 WHERE `id` = 'e3c13679c73a4f829bcff2aba8fd68b1';

UPDATE `jeecg_boot`.`sys_permission` SET `parent_id` = '2a470fc0c3954d9dbb61de6d80846549', `name` = '打印测试', `url` = '/jeecg/PrintDemo', `component` = '/jeecg/PrintDemo', `component_name` = NULL, `redirect` = NULL, `menu_type` = 1, `perms` = NULL, `sort_no` = 3.00, `always_show` = 0, `icon` = NULL, `is_route` = 1, `is_leaf` = 1, `hidden` = 0, `description` = NULL, `create_by` = 'admin', `create_time` = '2019-02-19 15:58:48', `update_by` = NULL, `update_time` = NULL, `del_flag` = 0 WHERE `id` = 'e6bfd1fcabfd7942fdd05f076d1dad38';

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('40283181614231d401614234fe670003', '40283181614231d401614232cd1c0001', 'createBy', 'createBy', '=', '#{sys_user_code}', '2018-01-29 21:57:04', 'admin', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('4028318161424e730161424fca6f0004', '4028318161424e730161424f61510002', 'createBy', 'createBy', '=', '#{sys_user_code}', '2018-01-29 22:26:20', 'admin', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('402880e6487e661a01487e732c020005', '402889fb486e848101486e93a7c80014', 'SYS_ORG_CODE', 'SYS_ORG_CODE', 'LIKE', '010201%', '2014-09-16 20:32:30', 'admin', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('402880e6487e661a01487e8153ee0007', '402889fb486e848101486e93a7c80014', 'create_by', 'create_by', '', '#{SYS_USER_CODE}', '2014-09-16 20:47:57', 'admin', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('402880ec5ddec439015ddf9225060038', '40288088481d019401481d2fcebf000d', '复杂关系', '', 'USE_SQL_RULES', 'name like \'%张%\' or age > 10', NULL, NULL, '2017-08-14 15:10:25', 'demo');

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('402880ec5ddfdd26015ddfe3e0570011', '4028ab775dca0d1b015dca3fccb60016', '复杂sql配置', '', 'USE_SQL_RULES', 'table_name like \'%test%\' or is_tree = \'Y\'', NULL, NULL, '2017-08-14 16:38:55', 'demo');

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('402880f25b1e2ac7015b1e5fdebc0012', '402880f25b1e2ac7015b1e5cdc340010', '只能看自己数据', 'create_by', '=', '#{sys_user_code}', '2017-03-30 16:40:51', 'admin', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('402881875b19f141015b19f8125e0014', '40288088481d019401481d2fcebf000d', '可看下属业务数据', 'sys_org_code', 'LIKE', '#{sys_org_code}', NULL, NULL, '2017-08-14 15:04:32', 'demo');

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('402881e45394d66901539500a4450001', '402881e54df73c73014df75ab670000f', 'sysCompanyCode', 'sysCompanyCode', '=', '#{SYS_COMPANY_CODE}', '2016-03-21 01:09:21', 'admin', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('402881e45394d6690153950177cb0003', '402881e54df73c73014df75ab670000f', 'sysOrgCode', 'sysOrgCode', '=', '#{SYS_ORG_CODE}', '2016-03-21 01:10:15', 'admin', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('402881e56266f43101626727aff60067', '402881e56266f43101626724eb730065', '销售自己看自己的数据', 'createBy', '=', '#{sys_user_code}', '2018-03-27 19:11:16', 'admin', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('402881e56266f4310162672fb1a70082', '402881e56266f43101626724eb730065', '销售经理看所有下级数据', 'sysOrgCode', 'LIKE', '#{sys_org_code}', '2018-03-27 19:20:01', 'admin', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('402881e56266f431016267387c9f0088', '402881e56266f43101626724eb730065', '只看金额大于1000的数据', 'money', '>=', '1000', '2018-03-27 19:29:37', 'admin', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('402881f3650de25101650dfb5a3a0010', '402881e56266f4310162671d62050044', '22', '', 'USE_SQL_RULES', '22', '2018-08-06 14:45:01', 'admin', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('402889fb486e848101486e913cd6000b', '402889fb486e848101486e8e2e8b0007', 'userName', 'userName', '=', 'admin', '2014-09-13 18:31:25', 'admin', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('402889fb486e848101486e98d20d0016', '402889fb486e848101486e93a7c80014', 'title', 'title', '=', '12', NULL, NULL, '2014-09-13 22:18:22', 'scott');

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('402889fe47fcb29c0147fcb6b6220001', '8a8ab0b246dc81120146dc8180fe002b', '12', '12', '>', '12', '2014-08-22 15:55:38', '8a8ab0b246dc81120146dc8181950052', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('4028ab775dca0d1b015dca4183530018', '4028ab775dca0d1b015dca3fccb60016', '表名限制', 'isDbSynch', '=', 'Y', NULL, NULL, '2017-08-14 16:43:45', 'demo');

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('4028ef815595a881015595b0ccb60001', '40288088481d019401481d2fcebf000d', '限只能看自己', 'create_by', '=', '#{sys_user_code}', NULL, NULL, '2017-08-14 15:03:56', 'demo');

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('4028ef81574ae99701574aed26530005', '4028ef81574ae99701574aeb97bd0003', '用户名', 'userName', '!=', 'admin', '2016-09-21 12:07:18', 'admin', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_permission_data_rule`(`id`, `permission_id`, `rule_name`, `rule_column`, `rule_conditions`, `rule_value`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('f852d85d47f224990147f2284c0c0005', NULL, '小于', 'test', '<=', '11', '2014-08-20 14:43:52', '8a8ab0b246dc81120146dc8181950052', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_quartz_job`(`id`, `create_by`, `create_time`, `del_flag`, `update_by`, `update_time`, `job_class_name`, `cron_expression`, `parameter`, `description`, `status`) VALUES ('5b3d2c087ad41aa755fc4f89697b01e7', 'admin', '2019-04-11 19:04:21', 0, 'admin', '2019-04-11 19:49:49', 'org.jeecg.modules.message.job.SendMsgJob', '0/60 * * * * ?', NULL, NULL, 0);

INSERT INTO `jeecg_boot`.`sys_quartz_job`(`id`, `create_by`, `create_time`, `del_flag`, `update_by`, `update_time`, `job_class_name`, `cron_expression`, `parameter`, `description`, `status`) VALUES ('a253cdfc811d69fa0efc70d052bc8128', 'admin', '2019-03-30 12:44:48', 0, 'admin', '2019-03-30 12:44:52', 'org.jeecg.modules.quartz.job.SampleJob', '0/1 * * * * ?', NULL, NULL, -1);

UPDATE `jeecg_boot`.`sys_role` SET `role_name` = '人力资源部', `role_code` = 'hr', `description` = NULL, `create_by` = 'admin', `create_time` = '2019-01-21 18:07:24', `update_by` = 'admin', `update_time` = '2019-03-17 18:30:41' WHERE `id` = 'e51758fa916c881624b046d26bd09230';

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('01221378db3443dbddaa494597fb10e3', 'f6817f48af4fb3af11b9e8bf182f618b', '024f1fd1283dc632458976463d8984e1', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('012fe07d6c7d54306f497174ae4ff4c7', 'f6817f48af4fb3af11b9e8bf182f618b', 'f23d9bfff4d9aa6b68569ba2cff38415', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('02cf2fd105e49aeb90bd2868f59659a3', 'f6817f48af4fb3af11b9e8bf182f618b', 'b3c824fc22bd953e2eb16ae6914ac8f9', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('0538e66f1151b80ad45fee3ab99a83b2', 'f6817f48af4fb3af11b9e8bf182f618b', '339329ed54cf255e1f9392e84f136901', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('05a6fadb2a7e774852e19650577f4431', 'f6817f48af4fb3af11b9e8bf182f618b', '8b3bff2eee6f1939147f5c68292a1642', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('077f0484204f514efdf82565e8e2ad0c', 'f6817f48af4fb3af11b9e8bf182f618b', 'e3c13679c73a4f829bcff2aba8fd68b1', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('0aeacc20f807204bee6cbb595602fcb5', 'f6817f48af4fb3af11b9e8bf182f618b', '2a470fc0c3954d9dbb61de6d80846549', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('0be9294ad987d5f2d65738307d18a658', 'f6817f48af4fb3af11b9e8bf182f618b', '078f9558cdeab239aecb2bda1a8ed0d1', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('0c76a4acf236d8e8a777c1072f63556b', 'f6817f48af4fb3af11b9e8bf182f618b', 'c431130c0bc0ec71b0a5be37747bb36a', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('0d9d14bc66e9d5e99b0280095fdc8587', 'ee8626f80f7c2619917b6236f3a7f02b', '277bfabef7d76e89b33062b16a9a5020', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('0dec36b68c234767cd35466efef3b941', 'ee8626f80f7c2619917b6236f3a7f02b', '54dd5457a3190740005c1bfec55b1c34', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('105c2ac10741e56a618a82cd58c461d7', 'e51758fa916c881624b046d26bd09230', '1663f3faba244d16c94552f849627d84', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('115a6673ae6c0816d3f60de221520274', '21c5a3187763729408b40afb0d0fdfa8', '63b551e81c5956d5c861593d366d8c57', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('120693092740d63656a7dc6ddeda6dd0', 'f6817f48af4fb3af11b9e8bf182f618b', '418964ba087b90a84897b62474496b93', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('145eac8dd88eddbd4ce0a800ab40a92c', 'e51758fa916c881624b046d26bd09230', '08e6b9dc3c04489c8e1ff2ce6f105aa4', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('14880dc3cc885742ac91b885bd73ac7a', 'f6817f48af4fb3af11b9e8bf182f618b', '2e42e3835c2b44ec9f7bc26c146ee531', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('15db74f6c965d75f4127e6bdc9d25594', 'f6817f48af4fb3af11b9e8bf182f618b', 'a4fc7b64b01a224da066bb16230f9c5a', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('16ef8ed3865ccc6f6306200760896c50', 'ee8626f80f7c2619917b6236f3a7f02b', 'e8af452d8948ea49d37c934f5100ae6a', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('1af4babaa4227c3cbb830bc5eb513abb', 'ee8626f80f7c2619917b6236f3a7f02b', 'e08cb190ef230d5d4f03824198773950', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('1ba162bbc2076c25561f8622f610d5bf', 'ee8626f80f7c2619917b6236f3a7f02b', 'aedbf679b5773c1f25e9f7b10111da73', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('1bf767127ea2e467d9293d570a16605c', 'f6817f48af4fb3af11b9e8bf182f618b', 'f9d3f4f27653a71c52faa9fb8070fbe7', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('1c55c4ced20765b8ebab383caa60f0b6', 'e51758fa916c881624b046d26bd09230', 'fb367426764077dcf94640c843733985', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('1caf6f14df1566bd270b4ef4299d91f1', 'f6817f48af4fb3af11b9e8bf182f618b', 'fef097f3903caf3a3c3a6efa8de43fbb', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('1de539a7fd0a48421d6b86a14b1985c5', 'f6817f48af4fb3af11b9e8bf182f618b', '4f66409ef3bbd69c1d80469d6e2a885e', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('1e099baeae01b747d67aca06bdfc34d1', 'e51758fa916c881624b046d26bd09230', '6ad53fd1b220989a8b71ff482d683a5a', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('1ec19c55dd19d898724d87ea5014dd98', 'f6817f48af4fb3af11b9e8bf182f618b', '93d5cfb4448f11e9916698e7f462b4b6', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('25f5443f19c34d99718a016d5f54112e', 'ee8626f80f7c2619917b6236f3a7f02b', '6e73eb3c26099c191bf03852ee1310a1', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('26368ba64b5572e3282e4eac0bbe902b', 'f6817f48af4fb3af11b9e8bf182f618b', '8d4683aacaa997ab86b966b464360338', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('27489816708b18859768dfed5945c405', 'a799c3b1b12dd3ed4bd046bfaef5fe6e', '9502685863ab87f0ad1134142788a385', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('296f9c75ca0e172ae5ce4c1022c996df', '646c628b2b8295fbdab2d34044de0354', '732d48f8e0abe99fe6a23d18a3171cd1', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('29fb4d37aa29b9fa400f389237cf9fe7', 'ee8626f80f7c2619917b6236f3a7f02b', '05b3c82ddb2536a4a5ee1a4c46b5abef', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('2a0734aa10d68c01c16f73ded008c147', 'f6817f48af4fb3af11b9e8bf182f618b', '1939e035e803a99ceecb6f5563570fb2', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('2b81f6c5c5e0bd11e8a550b65548390a', 'f6817f48af4fb3af11b9e8bf182f618b', '6e73eb3c26099c191bf03852ee1310a1', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('2dc1a0eb5e39aaa131ddd0082a492d76', 'ee8626f80f7c2619917b6236f3a7f02b', '08e6b9dc3c04489c8e1ff2ce6f105aa4', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('2e067c4cd3d56c5b1906711c7ce4f44c', 'f6817f48af4fb3af11b9e8bf182f618b', 'f1cb187abf927c88b89470d08615f5ac', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('2ea2382af618ba7d1e80491a0185fb8a', 'ee8626f80f7c2619917b6236f3a7f02b', 'f23d9bfff4d9aa6b68569ba2cff38415', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('2fcfa2ac3dcfadc7c67107dae9a0de6d', 'ee8626f80f7c2619917b6236f3a7f02b', '73678f9daa45ed17a3674131b03432fb', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('30312072349ea41db06ecf587d8be6e7', 'f6817f48af4fb3af11b9e8bf182f618b', 'a400e4f4d54f79bf5ce160ae432231af', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('31c1e8a7a37dec06d85c4eeba90f1942', 'f6817f48af4fb3af11b9e8bf182f618b', '65a8f489f25a345836b7f44b1181197a', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('3249742372a0356bb313f19c1bd6f6ee', 'f6817f48af4fb3af11b9e8bf182f618b', '7d08d3885671cf0ff1c4fe5d82fcd0e2', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('326181da3248a62a05e872119a462be1', 'ee8626f80f7c2619917b6236f3a7f02b', '3f915b2769fc80648e92d04e84ca059d', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('3369650f5072b330543f8caa556b1b33', 'e51758fa916c881624b046d26bd09230', 'a400e4f4d54f79bf5ce160ae432231af', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('35a7e156c20e93aa7e825fe514bf9787', 'e51758fa916c881624b046d26bd09230', 'c6cf95444d80435eb37b2f9db3971ae6', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('370127be64a2bfda6203ce72c4811bf4', 'f6817f48af4fb3af11b9e8bf182f618b', '765dd244f37b804e3d00f475fd56149b', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('370e842a683fc6395a1958da681fe749', 'f6817f48af4fb3af11b9e8bf182f618b', '841057b8a1bef8f6b4b20f9a618a7fa6', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('37112f4d372541e105473f18da3dc50d', 'ee8626f80f7c2619917b6236f3a7f02b', 'a400e4f4d54f79bf5ce160ae432231af', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('37789f70cd8bd802c4a69e9e1f633eaa', 'ee8626f80f7c2619917b6236f3a7f02b', 'ae4fed059f67086fd52a73d913cf473d', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('381504a717cb3ce77dcd4070c9689a7e', 'ee8626f80f7c2619917b6236f3a7f02b', '4f84f9400e5e92c95f05b554724c2b58', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('38c74d670bf1ebc612b72b69ebf067cf', 'f6817f48af4fb3af11b9e8bf182f618b', 'baf16b7174bd821b6bab23fa9abb200d', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('38dd7a19711e7ffe864232954c06fae9', 'e51758fa916c881624b046d26bd09230', 'd2bbf9ebca5a8fa2e227af97d2da7548', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('3ab440506de8f51378762d02b79d45c5', 'f6817f48af4fb3af11b9e8bf182f618b', 'fb07ca05a3e13674dbf6d3245956da2e', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('3b6fcd36c9e1114003fc1e6c6a092ab9', 'f6817f48af4fb3af11b9e8bf182f618b', '7ac9eb9ccbde2f7a033cd4944272bf1e', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('4276c23200b37c3be419d16466b21496', 'f6817f48af4fb3af11b9e8bf182f618b', 'e5973686ed495c379d829ea8b2881fc6', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('44b5a73541bcb854dd5d38c6d1fb93a1', 'ee8626f80f7c2619917b6236f3a7f02b', '418964ba087b90a84897b62474496b93', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('45325d8dbf7c9ee548ae06b490b045d3', 'f6817f48af4fb3af11b9e8bf182f618b', 'cc50656cf9ca528e6f2150eba4714ad2', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('45767d6c72d2ca1ce83025d9241d113a', 'f6817f48af4fb3af11b9e8bf182f618b', '05b3c82ddb2536a4a5ee1a4c46b5abef', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('459aa2e7021b435b4d65414cfbc71c66', 'e51758fa916c881624b046d26bd09230', '4148ec82b6acd69f470bea75fe41c357', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('467da0025bf89d8155aa6d290f6d9422', 'f6817f48af4fb3af11b9e8bf182f618b', '03dc3d93261dda19fc86dd7ca486c6cf', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('4a00cb21e8f14d362e3779cdc9abae62', 'f6817f48af4fb3af11b9e8bf182f618b', 'fc810a2267dd183e4ef7c71cc60f4670', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('4ac09a099adad69ab897254dda2db0d3', 'f6817f48af4fb3af11b9e8bf182f618b', '2d83d62bd2544b8994c8f38cf17b0ddf', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('4c0940badae3ef9231ee9d042338f2a4', 'e51758fa916c881624b046d26bd09230', '2a470fc0c3954d9dbb61de6d80846549', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('4d56ce2f67c94b74a1d3abdbea340e42', 'ee8626f80f7c2619917b6236f3a7f02b', 'd86f58e7ab516d3bc6bfb1fe10585f97', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('4da321638e32b193f1b56027ef803a76', 'f6817f48af4fb3af11b9e8bf182f618b', 'd86f58e7ab516d3bc6bfb1fe10585f97', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('4faad8ff93cb2b5607cd3d07c1b624ee', 'a799c3b1b12dd3ed4bd046bfaef5fe6e', '70b8f33da5f39de1981bf89cf6c99792', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('4fb0c63fcd0a2bb7f304e914013ee9e3', 'f6817f48af4fb3af11b9e8bf182f618b', 'c65321e57b7949b7a975313220de0422', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('54aeeecd4405b6e0189da1b5f82e6a9e', 'f6817f48af4fb3af11b9e8bf182f618b', 'de13e0f6328c069748de7399fcc1dbbd', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('552a143a7f527d1d2a4769793824aa84', 'f6817f48af4fb3af11b9e8bf182f618b', '45c966826eeff4c99b8f8ebfe74511fc', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('57ef0491da429c0ea949e6aa2f36383e', 'f6817f48af4fb3af11b9e8bf182f618b', 'aedbf679b5773c1f25e9f7b10111da73', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('59e64243d9b015a67fa8aace53801c2b', 'f6817f48af4fb3af11b9e8bf182f618b', 'd7d6e2e4e2934f2c9385a623fd98c6f3', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('5a93a14d6edcc28020a3e1bb58b550a9', 'f6817f48af4fb3af11b9e8bf182f618b', 'f780d0d3083d849ccbdb1b1baee4911d', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('5affc85021fcba07d81c09a6fdfa8dc6', 'ee8626f80f7c2619917b6236f3a7f02b', '078f9558cdeab239aecb2bda1a8ed0d1', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('5e0c8b2aaf853fe6b1c8b726db5342dc', 'f6817f48af4fb3af11b9e8bf182f618b', '200006f0edf145a2b50eacca07585451', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('5e634a89f75b7a421c02aecfd520325f', 'e51758fa916c881624b046d26bd09230', '339329ed54cf255e1f9392e84f136901', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('5e74637c4bec048d1880ad0bd1b00552', 'e51758fa916c881624b046d26bd09230', 'a400e4f4d54f79bf5ce160a3432231af', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('5f88c86ffc73ea6b311e221245c6bbf9', 'f6817f48af4fb3af11b9e8bf182f618b', '56ca78fe0f22d815fabc793461af67b8', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('5fc194b709336d354640fe29fefd65a3', 'a799c3b1b12dd3ed4bd046bfaef5fe6e', '9ba60e626bf2882c31c488aba62b89f0', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('60c4f0b59a0029429f744ab20b1bb7fa', 'f6817f48af4fb3af11b9e8bf182f618b', 'edfa74d66e8ea63ea432c2910837b150', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('61c71e973503479e8b795c5d40363f13', 'f6817f48af4fb3af11b9e8bf182f618b', 'e6bfd1fcabfd7942fdd05f076d1dad38', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('6369b641d2fa1b4bb9ef2fa630073bd8', 'f6817f48af4fb3af11b9e8bf182f618b', '54097c6a3cf50fad0793a34beff1efdf', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('63e66c8481aa89d2a2a614e7d5bc0184', 'f6817f48af4fb3af11b9e8bf182f618b', 'e8af452d8948ea49d37c934f5100ae6a', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('6451dac67ba4acafb570fd6a03f47460', 'ee8626f80f7c2619917b6236f3a7f02b', 'e3c13679c73a4f829bcff2aba8fd68b1', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('68ffc3f37266f287496e43ff6a329c36', 'f6817f48af4fb3af11b9e8bf182f618b', '109c78a583d4693ce2f16551b7786786', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('6a2375344192922f9bf3ee0f32e042eb', 'f6817f48af4fb3af11b9e8bf182f618b', '13212d3416eb690c2e1d5033166ff47a', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('6a9a93ac415e8ebb98fb5d75ed1bc970', 'f6817f48af4fb3af11b9e8bf182f618b', '700b7f95165c46cc7a78bf227aa8fed3', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('6c43fd3f10fdaf2a0646434ae68709b5', 'ee8626f80f7c2619917b6236f3a7f02b', '540a2936940846cb98114ffb0d145cb8', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('6d230b680a3cbe5081a86e944734435f', 'f6817f48af4fb3af11b9e8bf182f618b', 'fedfbf4420536cacc0218557d263dfea', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('6fe1ccedfebfe359e9cb0dbd4329d257', 'f6817f48af4fb3af11b9e8bf182f618b', 'b1cb0a3fedf7ed0e4653cb5a229837ee', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('71a5f54a90aa8c7a250a38b7dba39f6f', 'ee8626f80f7c2619917b6236f3a7f02b', '8fb8172747a78756c11916216b8b8066', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('7335d2c31f17ec8e91f11e20fdae00ef', 'f6817f48af4fb3af11b9e8bf182f618b', 'e08cb190ef230d5d4f03824198773950', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('737d35f582036cd18bfd4c8e5748eaa4', 'e51758fa916c881624b046d26bd09230', '693ce69af3432bd00be13c3971a57961', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('76de49fa17c97d7ce2b647aa6985e43d', 'f6817f48af4fb3af11b9e8bf182f618b', '9a90363f216a6a08f32eecb3f0bf12a3', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('7750f9be48ee09cd561fce718219a3e2', 'ee8626f80f7c2619917b6236f3a7f02b', '882a73768cfd7f78f3a37584f7299656', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('7d2ea745950be3357747ec7750c31c57', 'ee8626f80f7c2619917b6236f3a7f02b', '2a470fc0c3954d9dbb61de6d80846549', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('7d81581210add7608f4d580ab09c1c9d', 'f6817f48af4fb3af11b9e8bf182f618b', '882a73768cfd7f78f3a37584f7299656', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('7e19d90cec0dd87aaef351b9ff8f4902', '646c628b2b8295fbdab2d34044de0354', 'f9d3f4f27653a71c52faa9fb8070fbe7', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('7f862c47003eb20e8bad05f506371f92', 'ee8626f80f7c2619917b6236f3a7f02b', 'd7d6e2e4e2934f2c9385a623fd98c6f3', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('806bcc05238ef1b0c24dadf18fb0af08', 'f6817f48af4fb3af11b9e8bf182f618b', 'ec8d607d0156e198b11853760319c646', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('812ed54661b1a24b81b58974691a73f5', 'e51758fa916c881624b046d26bd09230', 'e6bfd1fcabfd7942fdd05f076d1dad38', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('8246c649fe7f1845cdc872014c7ad962', 'f6817f48af4fb3af11b9e8bf182f618b', '9502685863ab87f0ad1134142788a385', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('83f704524b21b6a3ae324b8736c65333', 'ee8626f80f7c2619917b6236f3a7f02b', '7ac9eb9ccbde2f7a033cd4944272bf1e', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('84d32474316a43b01256d6644e6e7751', 'ee8626f80f7c2619917b6236f3a7f02b', 'ec8d607d0156e198b11853760319c646', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('85755a6c0bdff78b3860b52d35310c7f', 'e51758fa916c881624b046d26bd09230', 'c65321e57b7949b7a975313220de0422', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('859fb968f3e4560f870f6659d4d2982f', 'f6817f48af4fb3af11b9e8bf182f618b', '63b551e81c5956d5c861593d366d8c57', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('85a009f7be4e792e61dc8dccfc35c273', 'f6817f48af4fb3af11b9e8bf182f618b', '8fb8172747a78756c11916216b8b8066', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('86cc551bcb0cc7d61f1fc5e924cffd2f', 'f6817f48af4fb3af11b9e8bf182f618b', '9fe26464838de2ea5e90f2367e35efa0', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('8703a2410cddb713c33232ce16ec04b9', 'ee8626f80f7c2619917b6236f3a7f02b', '1367a93f2c410b169faa7abcbad2f77c', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('8724588961f747d321885b2c50f3ef67', 'f6817f48af4fb3af11b9e8bf182f618b', '9cb91b8851db0cf7b19d7ecc2a8193dd', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('885c1a827383e5b2c6c4f8ca72a7b493', 'ee8626f80f7c2619917b6236f3a7f02b', '4148ec82b6acd69f470bea75fe41c357', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('8b1e326791375f325d3e6b797753b65e', 'ee8626f80f7c2619917b6236f3a7f02b', '2dbbafa22cda07fa5d169d741b81fe12', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('8eec2c510f1ac9c5eee26c041b1f00ca', 'ee8626f80f7c2619917b6236f3a7f02b', '58857ff846e61794c69208e9d3a85466', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('8f762ff80253f634b08cf59a77742ba4', 'ee8626f80f7c2619917b6236f3a7f02b', '9502685863ab87f0ad1134142788a385', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('903b790e6090414343502c6dc393b7c9', 'ee8626f80f7c2619917b6236f3a7f02b', 'de13e0f6328c069748de7399fcc1dbbd', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('90996d56357730e173e636b99fc48bea', 'ee8626f80f7c2619917b6236f3a7f02b', 'fb07ca05a3e13674dbf6d3245956da2e', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('90e1c607a0631364eec310f3cc4acebd', 'ee8626f80f7c2619917b6236f3a7f02b', '4f66409ef3bbd69c1d80469d6e2a885e', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('9435f03bc69f108d08f3efdb436de278', 'f6817f48af4fb3af11b9e8bf182f618b', '6ad53fd1b220989a8b71ff482d683a5a', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('98f02353f91dd569e3c6b8fd6b4f4034', 'ee8626f80f7c2619917b6236f3a7f02b', '6531cf3421b1265aeeeabaab5e176e6d', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('9b6c9e4bc89343d326f43b0ba14caa35', 'f6817f48af4fb3af11b9e8bf182f618b', '3f915b2769fc80648e92d04e84ca059d', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('9c035b8416d8d074f2a24ae975f9dc66', 'f6817f48af4fb3af11b9e8bf182f618b', '00a2a0ae65cdca5e93209cdbde97cbe6', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('9d8772c310b675ae43eacdbc6c7fa04a', 'a799c3b1b12dd3ed4bd046bfaef5fe6e', '1663f3faba244d16c94552f849627d84', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('a098e2acc3f90316f161f6648d085640', 'ee8626f80f7c2619917b6236f3a7f02b', 'e6bfd1fcabfd7942fdd05f076d1dad38', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('a1326a034fd39447609573ae7f209fc3', 'f6817f48af4fb3af11b9e8bf182f618b', '73678f9daa45ed17a3674131b03432fb', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('a34efa870a95ff078b4a729e5aff5f48', 'f6817f48af4fb3af11b9e8bf182f618b', '732d48f8e0abe99fe6a23d18a3171cd1', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('a4bbd697954d0c9edefdbebf05781cc0', 'f6817f48af4fb3af11b9e8bf182f618b', '7960961b0063228937da5fa8dd73d371', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('a5b56bd1e428d23aaa3d2f4c7424533b', 'f6817f48af4fb3af11b9e8bf182f618b', '54dd5457a3190740005c1bfec55b1c34', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('a66feaaf128417ad762e946abccf27ec', 'ee8626f80f7c2619917b6236f3a7f02b', 'c6cf95444d80435eb37b2f9db3971ae6', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('a72c31a3913c736d4eca11d13be99183', 'e51758fa916c881624b046d26bd09230', 'a44c30db536349e91106223957e684eb', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('a7ab87eac0f8fafa2efa4b1f9351923f', 'ee8626f80f7c2619917b6236f3a7f02b', 'fedfbf4420536cacc0218557d263dfea', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('a80a4528118552a7a3dc9e6a6eaee439', 'f6817f48af4fb3af11b9e8bf182f618b', '717f6bee46f44a3897eca9abd6e2ec44', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('a88f4cdc81d4a8d1e0a27a29c90d8c2a', 'f6817f48af4fb3af11b9e8bf182f618b', 'ffb423d25cc59dcd0532213c4a518261', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('ab639f1526a4ade3411fd20db7a1815e', 'f6817f48af4fb3af11b9e8bf182f618b', '6531cf3421b1265aeeeabaab5e176e6d', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('abdc324a2df9f13ee6e73d44c6e62bc8', 'ee8626f80f7c2619917b6236f3a7f02b', 'f1cb187abf927c88b89470d08615f5ac', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('ac17157d44d559e528a8fb9d04406fe8', 'f6817f48af4fb3af11b9e8bf182f618b', 'c2c356bf4ddd29975347a7047a062440', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('ad040f98d3c29cf93f7dc892a8ba1210', 'f6817f48af4fb3af11b9e8bf182f618b', '020b06793e4de2eee0007f603000c769', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('b05599406e8791d65afacdcb7201f563', 'f6817f48af4fb3af11b9e8bf182f618b', 'd2bbf9ebca5a8fa2e227af97d2da7548', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('b131ebeafcfd059f3c7e542606ea9ff5', 'ee8626f80f7c2619917b6236f3a7f02b', 'e5973686ed495c379d829ea8b2881fc6', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('b24122ddecdca94eb5754456bb2c32f2', 'f6817f48af4fb3af11b9e8bf182f618b', 'e41b69c57a941a3bbcce45032fe57605', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('b2b2dcfff6986d3d7f890ea62d474651', 'ee8626f80f7c2619917b6236f3a7f02b', '200006f0edf145a2b50eacca07585451', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('b3b7f425c0fc1d92d4c2dccbef66af69', 'f6817f48af4fb3af11b9e8bf182f618b', '7593c9e3523a17bca83b8d7fe8a34e58', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('b3db897e3e7e8469ea6218afe42d0a5a', 'f6817f48af4fb3af11b9e8bf182f618b', 'ae4fed059f67086fd52a73d913cf473d', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('b495a46fa0e0d4637abe0db7fd12fe1a', 'ee8626f80f7c2619917b6236f3a7f02b', '717f6bee46f44a3897eca9abd6e2ec44', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('b9d821aae407085c0d91da70f8c09e21', 'f6817f48af4fb3af11b9e8bf182f618b', '4148ec82b6acd69f470bea75fe41c357', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('ba87fd2b2c8eee82dac1cd92d39ca774', 'f6817f48af4fb3af11b9e8bf182f618b', '5c8042bd6c601270b2bbd9b20bccc68b', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('bd30561f141f07827b836878137fddd8', 'e51758fa916c881624b046d26bd09230', '65a8f489f25a345836b7f44b1181197a', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('be5eabad598d67bda03e314168086982', 'f6817f48af4fb3af11b9e8bf182f618b', 'c6cf95444d80435eb37b2f9db3971ae6', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('c316fdb7d377bf25772ce3577677509c', 'f6817f48af4fb3af11b9e8bf182f618b', '4875ebe289344e14844d8e3ea1edd73f', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('c63d3bdbb2a9a523ebfd44d2fc9008dd', 'f6817f48af4fb3af11b9e8bf182f618b', '8d1ebd663688965f1fd86a2f0ead3416', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('c689539d20a445b0896270290c58d01f', 'e51758fa916c881624b046d26bd09230', '13212d3416eb690c2e1d5033166ff47a', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('c77425e2129587a2efc9c7f9059699b7', 'f6817f48af4fb3af11b9e8bf182f618b', 'a2b11669e98c5fe54a53c3e3c4f35d14', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('c8571839e6b14796e661f3e2843b80b6', 'ee8626f80f7c2619917b6236f3a7f02b', '45c966826eeff4c99b8f8ebfe74511fc', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('c9412b70586ce8d729dac8fa3149ab1a', 'f6817f48af4fb3af11b9e8bf182f618b', '4f84f9400e5e92c95f05b554724c2b58', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('c9d35261cccd67ab2932107a0967a7d7', 'e51758fa916c881624b046d26bd09230', 'b4dfc7d5dd9e8d5b6dd6d4579b1aa559', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('ced7337f40e1db9a70de44d1262f0360', 'f6817f48af4fb3af11b9e8bf182f618b', '91c23960fab49335831cf43d820b0a61', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('ced80e43584ce15e97bb07298e93020d', 'e51758fa916c881624b046d26bd09230', '45c966826eeff4c99b8f8ebfe74511fc', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('d03d792b0f312e7b490afc5cec3dd6c5', 'e51758fa916c881624b046d26bd09230', '8fb8172747a78756c11916216b8b8066', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('d5267597a4450f06d49d2fb63859641a', 'e51758fa916c881624b046d26bd09230', '2dbbafa22cda07fa5d169d741b81fe12', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('d9c64c7a34306dc4e3176d95d367520b', 'f6817f48af4fb3af11b9e8bf182f618b', 'f0675b52d89100ee88472b6800754a08', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('dc4e760077a35fcef7995be9c92cef8d', 'f6817f48af4fb3af11b9e8bf182f618b', 'a400e4f4d54f79bf5ce160a3432231af', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('e02b6986b273af631e1aaf169a71f810', 'f6817f48af4fb3af11b9e8bf182f618b', '4356a1a67b564f0988a484f5531fd4d9', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('e258ca8bf7ee168b93bfee739668eb15', 'ee8626f80f7c2619917b6236f3a7f02b', 'fb367426764077dcf94640c843733985', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('e2e29f53362b2ec40cbd4c3414d1e85f', 'f6817f48af4fb3af11b9e8bf182f618b', '58857ff846e61794c69208e9d3a85466', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('e339f7db7418a4fd2bd2c113f1182186', 'ee8626f80f7c2619917b6236f3a7f02b', 'b1cb0a3fedf7ed0e4653cb5a229837ee', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('e36b26c0077205bc87bb3f29b96f9039', 'f6817f48af4fb3af11b9e8bf182f618b', '540a2936940846cb98114ffb0d145cb8', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('e3c30f109d09db2210f3857c1e79baad', 'f6817f48af4fb3af11b9e8bf182f618b', '944abf0a8fc22fe1f1154a389a574154', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('e56a61a4964e0fc71f20856713a0040d', 'f6817f48af4fb3af11b9e8bf182f618b', '2aeddae571695cd6380f6d6d334d6e7d', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('ec3748f2cc58400ffbd3cd1fa3232f5d', 'f6817f48af4fb3af11b9e8bf182f618b', '1367a93f2c410b169faa7abcbad2f77c', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('eeb6b2e31b35a6a2b7778f41b9922ef6', 'f6817f48af4fb3af11b9e8bf182f618b', 'd07a2c87a451434c99ab06296727ec4f', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('f12b6c90e8913183d7ca547c66600891', 'e51758fa916c881624b046d26bd09230', 'aedbf679b5773c1f25e9f7b10111da73', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('f17ab8ad1e71341140857ef4914ef297', '21c5a3187763729408b40afb0d0fdfa8', '732d48f8e0abe99fe6a23d18a3171cd1', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('f1e64337a340851c05ce380a4cd8c827', 'f6817f48af4fb3af11b9e8bf182f618b', 'fb367426764077dcf94640c843733985', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('f4587c9881865a9d25cc3bfc523da70c', 'f6817f48af4fb3af11b9e8bf182f618b', 'b4dfc7d5dd9e8d5b6dd6d4579b1aa559', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('f55be9d4b4ff6ddcbd917cf4feb22ab6', 'f6817f48af4fb3af11b9e8bf182f618b', '027aee69baee98a0ed2e01806e89c891', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('f641ae7d1a22add2697259d16dd0a93b', 'f6817f48af4fb3af11b9e8bf182f618b', 'c89018ea6286e852b424466fd92a2ffc', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('f8f3fb503568a404dad05fd3e67e62a2', 'f6817f48af4fb3af11b9e8bf182f618b', '08e6b9dc3c04489c8e1ff2ce6f105aa4', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('fa242a4d2fcf1582bd4bb72d6ed71cd7', 'f6817f48af4fb3af11b9e8bf182f618b', '277bfabef7d76e89b33062b16a9a5020', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('fcd15dba62849353702fd0499fb2660f', 'f6817f48af4fb3af11b9e8bf182f618b', '2dbbafa22cda07fa5d169d741b81fe12', NULL);

INSERT INTO `jeecg_boot`.`sys_role_permission`(`id`, `role_id`, `permission_id`, `data_rule_ids`) VALUES ('fd86f6b08eb683720ba499f9d9421726', 'ee8626f80f7c2619917b6236f3a7f02b', '693ce69af3432bd00be13c3971a57961', NULL);

INSERT INTO `jeecg_boot`.`sys_sms`(`id`, `es_title`, `es_type`, `es_receiver`, `es_param`, `es_content`, `es_send_time`, `es_send_status`, `es_send_num`, `es_result`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402880e74dc2f361014dc2f8411e0001', '消息推送测试333', '2', '411944058@qq.com', NULL, '张三你好，你的订单4028d881436d514601436d521ae80165已付款!', '2015-06-05 17:06:01', '3', NULL, NULL, '认证失败错误的用户名或者密码', 'admin', '2015-06-05 17:05:59', 'admin', '2015-11-19 22:30:39');

INSERT INTO `jeecg_boot`.`sys_sms`(`id`, `es_title`, `es_type`, `es_receiver`, `es_param`, `es_content`, `es_send_time`, `es_send_status`, `es_send_num`, `es_result`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402880ea533647b00153364e74770001', '发个问候', '3', 'admin', NULL, '你好', '2016-03-02 00:00:00', '2', NULL, NULL, NULL, 'admin', '2016-03-02 15:50:24', 'admin', '2018-07-05 19:53:01');

INSERT INTO `jeecg_boot`.`sys_sms`(`id`, `es_title`, `es_type`, `es_receiver`, `es_param`, `es_content`, `es_send_time`, `es_send_status`, `es_send_num`, `es_result`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402880ee5a17e711015a17f3188e013f', '消息推送测试333', '2', '411944058@qq.com', NULL, '张三你好，你的订单4028d881436d514601436d521ae80165已付款!', NULL, '2', NULL, NULL, NULL, 'admin', '2017-02-07 17:41:31', 'admin', '2017-03-10 11:37:05');

INSERT INTO `jeecg_boot`.`sys_sms`(`id`, `es_title`, `es_type`, `es_receiver`, `es_param`, `es_content`, `es_send_time`, `es_send_status`, `es_send_num`, `es_result`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402880f05ab649b4015ab64b9cd80012', '消息推送测试333', '2', '411944058@qq.com', NULL, '张三你好，你的订单4028d881436d514601436d521ae80165已付款!', '2017-11-16 15:58:15', '3', NULL, NULL, NULL, 'admin', '2017-03-10 11:38:13', 'admin', '2017-07-31 17:24:54');

INSERT INTO `jeecg_boot`.`sys_sms`(`id`, `es_title`, `es_type`, `es_receiver`, `es_param`, `es_content`, `es_send_time`, `es_send_status`, `es_send_num`, `es_result`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402880f05ab7b035015ab7c4462c0004', '消息推送测试333', '2', '411944058@qq.com', NULL, '张三你好，你的订单4028d881436d514601436d521ae80165已付款!', '2017-11-16 15:58:15', '3', NULL, NULL, NULL, 'admin', '2017-03-10 18:29:37', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_sms`(`id`, `es_title`, `es_type`, `es_receiver`, `es_param`, `es_content`, `es_send_time`, `es_send_status`, `es_send_num`, `es_result`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f3646a472b01646a4a5af00001', '催办：HR审批', '3', 'admin', NULL, 'admin，您好！\r\n请前待办任务办理事项！HR审批\r\n\r\n\r\n===========================\r\n此消息由系统发出', '2018-07-05 19:53:35', '2', NULL, NULL, NULL, 'admin', '2018-07-05 19:53:35', 'admin', '2018-07-07 13:45:24');

INSERT INTO `jeecg_boot`.`sys_sms`(`id`, `es_title`, `es_type`, `es_receiver`, `es_param`, `es_content`, `es_send_time`, `es_send_status`, `es_send_num`, `es_result`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('402881f3647da06c01647da43a940014', '催办：HR审批', '3', 'admin', NULL, 'admin，您好！\r\n请前待办任务办理事项！HR审批\r\n\r\n\r\n===========================\r\n此消息由系统发出', '2018-07-09 14:04:32', '2', NULL, NULL, NULL, 'admin', '2018-07-09 14:04:32', 'admin', '2018-07-09 18:51:30');

INSERT INTO `jeecg_boot`.`sys_sms`(`id`, `es_title`, `es_type`, `es_receiver`, `es_param`, `es_content`, `es_send_time`, `es_send_status`, `es_send_num`, `es_result`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c0f7f27f65a5678961306eb0364c05cd', '催办：HR审批', '2', '445654970@qq.com', '{\"taskName\":\"HR审批\",\"userName\":\"admin\"}', 'admin，您好！\r\n请前待办任务办理事项！HR审批\r\n\r\n\r\n===========================\r\n此消息由系统发出', '2019-04-12 20:51:28', '0', 0, NULL, NULL, 'admin', '2019-04-12 20:51:28', NULL, NULL);

INSERT INTO `jeecg_boot`.`sys_sms_template`(`id`, `template_name`, `template_code`, `template_type`, `template_content`, `template_test_json`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES ('4028608164691b000164693108140003', '催办：${taskName}', 'SYS001', '2', '${userName}，您好！\r\n请前待办任务办理事项！${taskName}\r\n\r\n\r\n===========================\r\n此消息由系统发出', '{\r\n\"taskName\":\"HR审批\",\r\n\"userName\":\"admin\"\r\n}', '2018-07-05 14:46:18', 'admin', '2018-07-05 18:31:34', 'admin');

INSERT INTO `jeecg_boot`.`sys_user`(`id`, `username`, `realname`, `password`, `salt`, `avatar`, `birthday`, `sex`, `email`, `phone`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('42d153bffeea74f72a9c1697874fa4a7', 'test22', '23232', 'ac52e15671a377cf', '5FMD48RM', 'user/20190314/ly-plate-e_1552531617500.png', '2019-02-09 00:00:00', 1, 'zhangdaiscott@163.com', '18611782222', 1, '0', 'admin', '2019-01-26 18:01:10', 'admin', '2019-03-23 15:05:50');

INSERT INTO `jeecg_boot`.`sys_user`(`id`, `username`, `realname`, `password`, `salt`, `avatar`, `birthday`, `sex`, `email`, `phone`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f0019fdebedb443c98dcb17d88222c38', 'zhagnxiao', '张小红', 'f898134e5e52ae11a2ffb2c3b57a4e90', 'go3jJ4zX', 'user/20190401/20180607175028Fn1Lq7zw_1554118444672.png', '2019-04-01 00:00:00', NULL, NULL, NULL, 1, '0', 'admin', '2023-10-01 19:34:10', 'admin', '2019-04-10 22:00:22');

UPDATE `jeecg_boot`.`sys_user` SET `username` = 'jeecg', `realname` = 'jeecg', `password` = '3dd8371f3cf8240e', `salt` = 'vDDkDzrK', `avatar` = 'user/20190220/e1fe9925bc315c60addea1b98eb1cb1349547719_1550656892940.jpg', `birthday` = NULL, `sex` = 2, `email` = NULL, `phone` = NULL, `status` = 1, `del_flag` = '0', `create_by` = 'admin', `create_time` = '2019-02-13 16:02:36', `update_by` = 'admin', `update_time` = '2019-04-09 15:47:36' WHERE `id` = 'a75d45a015c44384a04449ee80dc3503';

UPDATE `jeecg_boot`.`sys_user` SET `username` = 'admin', `realname` = '管理员', `password` = 'cb362cfeefbf3d8d', `salt` = 'RCGTeGiH', `avatar` = 'user/20190119/logo-2_1547868176839.png', `birthday` = '2018-12-05 00:00:00', `sex` = 1, `email` = '11@qq.com', `phone` = '18566666666', `status` = 1, `del_flag` = '0', `create_by` = NULL, `create_time` = '2018-12-21 17:54:10', `update_by` = 'admin', `update_time` = '2019-02-26 17:55:02' WHERE `id` = 'e9ca23d68d884d4ebb19d07889727dae';

INSERT INTO `jeecg_boot`.`sys_user_depart`(`ID`, `user_id`, `dep_id`) VALUES ('0c42ba309c2c4cad35836ec2336676fa', '42d153bffeea74f72a9c1697874fa4a7', '6d35e179cd814e3299bd588ea7daed3f');

INSERT INTO `jeecg_boot`.`sys_user_depart`(`ID`, `user_id`, `dep_id`) VALUES ('15e0d72bcbab86a41e38222b1f09428e', 'e9ca23d68d884d4ebb19d07889727dae', 'c6d7cb4deeac411cb3384b1b31278596');

INSERT INTO `jeecg_boot`.`sys_user_depart`(`ID`, `user_id`, `dep_id`) VALUES ('179660a8b9a122f66b73603799a10924', 'f0019fdebedb443c98dcb17d88222c38', '67fc001af12a4f9b8458005d3f19934a');

INSERT INTO `jeecg_boot`.`sys_user_depart`(`ID`, `user_id`, `dep_id`) VALUES ('1f3a0267811327b9eca86b0cc2b956f3', 'bcbe1290783a469a83ae3bd8effe15d4', '5159cde220114246b045e574adceafe9');

INSERT INTO `jeecg_boot`.`sys_user_depart`(`ID`, `user_id`, `dep_id`) VALUES ('2835834d133f9118ee87a666e0f5501e', 'a75d45a015c44384a04449ee80dc3503', 'a7d7e77e06c84325a40932163adcdaa6');

INSERT INTO `jeecg_boot`.`sys_user_depart`(`ID`, `user_id`, `dep_id`) VALUES ('577ae220081f78ceaf8cb26eb75330ab', 'e9ca23d68d884d4ebb19d07889727dae', '4f1765520d6346f9bd9c79e2479e5b12');

INSERT INTO `jeecg_boot`.`sys_user_depart`(`ID`, `user_id`, `dep_id`) VALUES ('ac52f23ae625eb6560c9227170b88166', 'f0019fdebedb443c98dcb17d88222c38', '57197590443c44f083d42ae24ef26a2c');

INSERT INTO `jeecg_boot`.`sys_user_role`(`id`, `user_id`, `role_id`) VALUES ('31af310584bd5795f76b1fe8c38294a0', '70f5dcf03f36471dabba81381919291f', 'e51758fa916c881624b046d26bd09230');

INSERT INTO `jeecg_boot`.`sys_user_role`(`id`, `user_id`, `role_id`) VALUES ('6ec01b4aaab790eac4ddb33d7a524a58', 'e9ca23d68d884d4ebb19d07889727dae', 'f6817f48af4fb3af11b9e8bf182f618b');

INSERT INTO `jeecg_boot`.`sys_user_role`(`id`, `user_id`, `role_id`) VALUES ('6f9da7310489bac1e5f95e0efe92b4ce', '42d153bffeea74f72a9c1697874fa4a7', 'e51758fa916c881624b046d26bd09230');

INSERT INTO `jeecg_boot`.`sys_user_role`(`id`, `user_id`, `role_id`) VALUES ('79d66ef7aa137cfa9957081a1483009d', '9a668858c4c74cf5a2b25ad9608ba095', 'ee8626f80f7c2619917b6236f3a7f02b');

INSERT INTO `jeecg_boot`.`sys_user_role`(`id`, `user_id`, `role_id`) VALUES ('8d7846ec783e157174e4ce2949231a65', '7ee6630e89d17afbf6d12150197b578d', 'e51758fa916c881624b046d26bd09230');

INSERT INTO `jeecg_boot`.`sys_user_role`(`id`, `user_id`, `role_id`) VALUES ('be2639167ede09379937daca7fc3bb73', '526f300ab35e44faaed54a9fb0742845', 'ee8626f80f7c2619917b6236f3a7f02b');

INSERT INTO `jeecg_boot`.`sys_user_role`(`id`, `user_id`, `role_id`) VALUES ('d2233e5be091d39da5abb0073c766224', 'f0019fdebedb443c98dcb17d88222c38', 'ee8626f80f7c2619917b6236f3a7f02b');

INSERT INTO `jeecg_boot`.`sys_user_role`(`id`, `user_id`, `role_id`) VALUES ('e78d210d24aaff48e0a736e2ddff4cdc', '3e177fede453430387a8279ced685679', 'ee8626f80f7c2619917b6236f3a7f02b');

INSERT INTO `jeecg_boot`.`sys_user_role`(`id`, `user_id`, `role_id`) VALUES ('f2de3ae7b5efd8345581aa802a6675d6', '41b1be8d4c52023b0798f51164ca682d', 'e51758fa916c881624b046d26bd09230');

INSERT INTO `jeecg_boot`.`sys_user_role`(`id`, `user_id`, `role_id`) VALUES ('f3a4ca33848daba3e43490707ae859e7', 'a75d45a015c44384a04449ee80dc3503', 'e51758fa916c881624b046d26bd09230');

INSERT INTO `jeecg_boot`.`sys_user_role`(`id`, `user_id`, `role_id`) VALUES ('fe38580871c5061ba59d5c03a0840b0e', 'a75d45a015c44384a04449ee80dc3503', 'ee8626f80f7c2619917b6236f3a7f02b');

SET FOREIGN_KEY_CHECKS = 1;