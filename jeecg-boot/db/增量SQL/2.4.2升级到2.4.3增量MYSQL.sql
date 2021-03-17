
--  redis监控菜单sql
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `status`, `del_flag`, `rule_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `internal_or_external`) VALUES ('1352200630711652354', 'f0675b52d89100ee88472b6800754a08', 'redis监控', '{{ window._CONFIG[\'domianURL\'] }}/jmreport/view/1352160857479581696', 'layouts/IframePageView', NULL, NULL, '1', NULL, '1', '1.00', '0', '', '1', '1', '0', '0', NULL, '1', '0', '0', 'admin', '2021-01-21 18:25:28', 'admin', '2021-01-22 16:49:43', '0');



--  新增开关字典
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('1356445645198135298', '开关', 'is_open', '', '0', 'admin', '2021-02-02 11:33:38', 'admin', '2021-02-02 15:28:12', '0');
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1356445705549975553', '1356445645198135298', '是', 'Y', '', '1', '1', 'admin', '2021-02-02 11:33:52', NULL, NULL);
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1356445754212290561', '1356445645198135298', '否', 'N', '', '1', '1', 'admin', '2021-02-02 11:34:04', NULL, NULL);
-- author:liusq----date:20210202----for:  新增开关字典


--  是否字典
INSERT INTO `sys_dict`(`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES ('a7adbcd86c37f7dbc9b66945c82ef9e6', '1是0否', 'yn', '', 0, 'admin', '2019-05-22 19:29:29', NULL, NULL, 0);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('51222413e5906cdaf160bb5c86fb827c', 'a7adbcd86c37f7dbc9b66945c82ef9e6', '是', '1', '', 1, 1, 'admin', '2019-05-22 19:29:45', NULL, NULL);
INSERT INTO `sys_dict_item`(`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c5700a71ad08994d18ad1dacc37a71a9', 'a7adbcd86c37f7dbc9b66945c82ef9e6', '否', '0', '', 1, 1, 'admin', '2019-05-22 19:29:55', NULL, NULL);
-- author:scott----date:20210202----for:   是否字典


-- 精简积木报表的示例表，由22个减至4个----
drop table	rep_demo_dadong;
drop table	rep_demo_daibu;
drop table	rep_demo_deliveryorder;
drop table	rep_demo_huizong;
drop table	rep_demo_income;
drop table	rep_demo_jiehsao;
drop table	rep_demo_kaoqin;
drop table	rep_demo_salesrate;
drop table	rep_demo_xiaoshou;
drop table	xianlu1_wxtl;
drop table	xianlu_wxtl;
drop table	yanshi_duozu;
drop table	yanshi_jdcx;
drop table	yanshi_qipaosandian;
drop table	yanshi_sandian;
drop table	yanshi_tima;
drop table	yanshi_wxtl;
drop table	yanshi_yipan;
alter table yanshi_dxtj rename to rep_demo_dxtj;
delete from jimu_report_data_source where report_id in ('1331429368098066432','1333962561053396992','1334028738995818496','1334074491629867008','1337271712059887616','1339478701846433792','1339859143477039104','8e69f5396643b199c92d6f401be4d833');
delete from jimu_report_db where jimu_report_id in('1331429368098066432','1333962561053396992','1334028738995818496','1334074491629867008','1337271712059887616','1339478701846433792','1339859143477039104','8e69f5396643b199c92d6f401be4d833');
delete from jimu_report_db_param where jimu_report_head_id in ('1331429368098066432','1333962561053396992','1334028738995818496','1334074491629867008','1337271712059887616','1339478701846433792','1339859143477039104','8e69f5396643b199c92d6f401be4d833');
delete from jimu_report_db_field where jimu_report_db_id not in (select id from jimu_report_db);
delete from jimu_report where id in ('1331429368098066432','1333962561053396992','1334028738995818496','1334074491629867008','1337271712059887616','1339478701846433792','1339859143477039104','8e69f5396643b199c92d6f401be4d833');


-- 添加一对多JVxeTable案例
INSERT INTO  sys_permission (`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `description`, `status`, `del_flag`, `rule_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `internal_or_external`) VALUES ('1365187528377102337', '2a470fc0c3954d9dbb61de6d80846549', '一对多JVxeTable', '/jeecg/JeecgOrderMainListForJVxeTable', 'jeecg/JeecgOrderMainListForJVxeTable', NULL, NULL, 1, NULL, '1', 2.00, 0, NULL, 1, 1, 0, 0, NULL, '1', 0, 0, 'admin', '2021-02-26 14:30:45', 'admin', '2021-02-26 14:32:05', 0);

-- 积木权限表
CREATE TABLE `jimu_report_share`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `report_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '在线excel设计器id',
  `preview_url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预览地址',
  `preview_lock` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码锁',
  `last_update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  `term_of_validity` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '有效期(0:永久有效，1:1天，2:7天)',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否过期(0未过期，1已过期)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '积木报表预览权限表';


-- 积木报表链接表
CREATE TABLE `jimu_report_link`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `report_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '积木设计器id',
  `parameter` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '参数',
  `eject_type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '弹出方式（0 当前页面 1 新窗口）',
  `link_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '链接名称',
  `api_method` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法0-get,1-post',
  `link_type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '链接方式(0 网络报表 1 网络连接 2 图表联动)',
  `api_url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '外网api',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '超链接配置表';


update jimu_report_link set link_type = '0';



