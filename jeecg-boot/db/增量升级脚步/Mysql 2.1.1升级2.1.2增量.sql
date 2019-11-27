

DROP TABLE IF EXISTS `sys_fill_rule`;
CREATE TABLE `sys_fill_rule` (
  `id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '主键ID',
  `rule_name` varchar(100) DEFAULT NULL COMMENT '规则名称',
  `rule_code` varchar(100) DEFAULT NULL COMMENT '规则Code',
  `rule_class` varchar(100) DEFAULT NULL COMMENT '规则实现类',
  `rule_params` varchar(200) DEFAULT NULL COMMENT '规则参数',
  `update_by` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_sys_fill_rule_code` (`rule_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `sys_fill_rule` (`id`, `rule_name`, `rule_code`, `rule_class`, `rule_params`, `update_by`, `update_time`, `create_by`, `create_time`) VALUES ('1192349918153428994', '订单号生成（带参）', 'order_num_rule_param', 'org.jeecg.modules.online.cgform.rule.OrderNumberRule', '{ \"prefix\": \"XD\" }', 'admin', '2019-11-07 17:44:17', 'admin', '2019-11-07 15:55:48');
INSERT INTO `sys_fill_rule` (`id`, `rule_name`, `rule_code`, `rule_class`, `rule_params`, `update_by`, `update_time`, `create_by`, `create_time`) VALUES ('1192350056519323649', '订单号生成', 'order_num_rule', 'org.jeecg.modules.online.cgform.rule.OrderNumberRule', NULL, 'admin', '2019-11-07 16:37:05', 'admin', '2019-11-07 15:56:21');

INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `is_route`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_leaf`, `keep_alive`, `hidden`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`, `rule_flag`, `status`, `internal_or_external`) VALUES ('1192318987661234177', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '填值规则', '/isystem/fillRule', 'system/SysFillRuleList', '1', NULL, NULL, '1', NULL, '1', '1', '0', NULL, '1', '0', '0', NULL, 'admin', '2019-11-07 13:52:53', NULL, NULL, '0', '0', '1', '0');

ALTER TABLE `sys_position`
CHANGE COLUMN `rank` `post_rank` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职级' AFTER `name`;
