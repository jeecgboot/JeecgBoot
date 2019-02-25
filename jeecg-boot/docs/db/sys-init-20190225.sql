/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50037
Source Host           : localhost:3306
Source Database       : jeecg-boot

Target Server Type    : MYSQL
Target Server Version : 50037
File Encoding         : 65001

Date: 2019-02-25 16:29:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for demo
-- ----------------------------
DROP TABLE IF EXISTS `demo`;
CREATE TABLE `demo` (
  `id` varchar(50) NOT NULL COMMENT '主键ID',
  `name` varchar(30) default NULL COMMENT '姓名',
  `key_word` varchar(255) default NULL COMMENT '关键词',
  `punch_time` datetime default NULL COMMENT '打卡时间',
  `salary_money` decimal(10,3) default NULL COMMENT '工资',
  `bonus_money` double(10,2) default NULL COMMENT '奖金',
  `sex` varchar(2) default NULL COMMENT '性别 {男:1,女:2}',
  `age` int(11) default NULL COMMENT '年龄',
  `birthday` date default NULL COMMENT '生日',
  `email` varchar(50) default NULL COMMENT '邮箱',
  `content` varchar(1000) default NULL COMMENT '个人简介',
  `create_by` varchar(32) default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_by` varchar(32) default NULL COMMENT '修改人',
  `update_time` datetime default NULL COMMENT '修改时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of demo
-- ----------------------------
INSERT INTO `demo` VALUES ('1c2ba51b29a42d9de02bbd708ea8121a', '777777', '777', '2018-12-07 19:43:17', null, null, null, '7', '2018-12-07', null, null, null, null, 'admin', '2019-02-21 18:26:04');
INSERT INTO `demo` VALUES ('1dc29e80be14d1400f165b5c6b30c707', 'zhang daihao', null, null, null, null, '2', null, null, 'zhangdaiscott@163.com', null, null, null, null, null);
INSERT INTO `demo` VALUES ('304e651dc769d5c9b6e08fb30457a602', '小白兔', null, null, null, null, '2', '28', null, null, null, 'scott', '2019-01-19 13:12:53', 'qinfeng', '2019-01-19 13:13:12');
INSERT INTO `demo` VALUES ('4', 'Sandy', '开源，很好', '2018-12-15 00:00:00', null, null, '2', '21', '2018-12-15', 'test4@baomidou.com', '聪明00', null, null, null, null);
INSERT INTO `demo` VALUES ('42c08b1a2e5b2a96ffa4cc88383d4b11', '秦50090', null, '2019-01-05 20:33:31', null, null, null, '28', '2019-01-05', null, null, 'admin', '2019-01-19 20:33:54', 'admin', '2019-01-19 20:34:29');
INSERT INTO `demo` VALUES ('4436302a0de50bb83025286bc414d6a9', 'zhang daihao', null, null, null, null, null, null, null, 'zhangdaiscott@163.com', null, 'admin', '2019-01-19 15:39:04', null, null);
INSERT INTO `demo` VALUES ('7', 'zhangdaiscott', null, null, null, null, '1', null, '2019-01-03', null, null, null, null, null, null);
INSERT INTO `demo` VALUES ('917e240eaa0b1b2d198ae869b64a81c3', 'zhang daihao', null, null, null, null, '2', '0', '2018-11-29', 'zhangdaiscott@163.com', null, null, null, null, null);
INSERT INTO `demo` VALUES ('94420c5d8fc4420dde1e7196154b3a24', '秦111', null, null, null, null, null, null, null, null, null, 'scott', '2019-01-19 12:54:58', 'qinfeng', '2019-01-19 13:12:10');
INSERT INTO `demo` VALUES ('b86897900c770503771c7bb88e5d1e9b', 'scott1', '开源、很好、hello', null, null, null, '1', null, null, 'zhangdaiscott@163.com', null, 'scott', '2019-01-19 12:22:34', null, null);
INSERT INTO `demo` VALUES ('c0b7c3de7c62a295ab715943de8a315d', '秦风555', null, null, null, null, null, null, null, null, null, 'admin', '2019-01-19 13:18:30', 'admin', '2019-01-19 13:18:50');
INSERT INTO `demo` VALUES ('c2c0d49e3c01913067cf8d1fb3c971d2', 'zhang daihao', null, null, null, null, null, null, null, 'zhangdaiscott@163.com', null, 'admin', '2019-01-19 23:37:18', 'admin', '2019-01-21 16:49:06');
INSERT INTO `demo` VALUES ('d24668721446e8478eeeafe4db66dcff', 'zhang daihao999', null, null, null, null, '1', null, null, 'zhangdaiscott@163.com', null, null, null, null, null);
INSERT INTO `demo` VALUES ('eaa6c1116b41dc10a94eae34cf990133', 'zhang daihao', null, null, null, null, null, null, null, 'zhangdaiscott@163.com', null, null, null, null, null);
INSERT INTO `demo` VALUES ('ffa9da1ad40632dfcabac51d766865bd', '秦999', null, null, null, null, null, null, null, null, null, 'admin', '2019-01-19 23:36:34', 'admin', '2019-02-14 17:30:43');

-- ----------------------------
-- Table structure for jeecg_order_customer
-- ----------------------------
DROP TABLE IF EXISTS `jeecg_order_customer`;
CREATE TABLE `jeecg_order_customer` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '客户名',
  `sex` varchar(4) default NULL COMMENT '性别',
  `idcard` varchar(18) default NULL COMMENT '身份证号码',
  `idcard_pic` varchar(500) default NULL COMMENT '身份证扫描件',
  `telphone` varchar(32) default NULL COMMENT '电话1',
  `order_id` varchar(32) NOT NULL COMMENT '外键',
  `create_by` varchar(32) default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_by` varchar(32) default NULL COMMENT '修改人',
  `update_time` datetime default NULL COMMENT '修改时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jeecg_order_customer
-- ----------------------------
INSERT INTO `jeecg_order_customer` VALUES ('0e2326ab54710dc4df5eaff164f2a679', '秦风', '1', '370285198602058822', null, '18611788676', '54e739bef5b67569c963c38da52581ec', 'jeecg', '2019-02-20 17:21:02', null, null);
INSERT INTO `jeecg_order_customer` VALUES ('58702a52bd79c093235eac6ab582f0e3', 'zhangdaiscott222', null, null, null, null, 'eb13ab35d2946a2b0cfe3452bca1e73f', 'admin', '2019-02-21 16:23:23', null, null);
INSERT INTO `jeecg_order_customer` VALUES ('62f6494f9ac9d31c84a07c032096256b', 'x秦风', '1', null, null, null, 'b190737bd04cca8360e6f87c9ef9ec4e', 'admin', '2019-02-15 18:39:29', null, null);
INSERT INTO `jeecg_order_customer` VALUES ('80e45b7f44a8277b5cc46764ede67e4e', '小王', '2', '370285198604033222', null, '18611788674', 'eb13ab35d2946a2b0cfe3452bca1e73f', 'admin', '2019-02-18 10:22:24', null, null);
INSERT INTO `jeecg_order_customer` VALUES ('a28195feae7318b69213b5a264c9bd7b', '张经理', '2', '370285198602058823', null, '18611788674', '54e739bef5b67569c963c38da52581ec', 'jeecg', '2019-02-20 17:21:02', null, null);
INSERT INTO `jeecg_order_customer` VALUES ('bce38126fbd86f5720d97f270f44e72a', 'scott', '2', null, null, null, 'b190737bd04cca8360e6f87c9ef9ec4e', 'admin', '2019-02-15 18:39:29', null, null);
INSERT INTO `jeecg_order_customer` VALUES ('c341b7c1999e62c2b3a4bf3802dad8b7', '小张', '1', '370285198602058211', null, '18611788676', 'eb13ab35d2946a2b0cfe3452bca1e73f', 'admin', '2019-02-18 10:22:24', null, null);

-- ----------------------------
-- Table structure for jeecg_order_main
-- ----------------------------
DROP TABLE IF EXISTS `jeecg_order_main`;
CREATE TABLE `jeecg_order_main` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `order_code` varchar(50) default NULL COMMENT '订单号',
  `ctype` varchar(500) default NULL COMMENT '订单类型',
  `order_date` datetime default NULL COMMENT '订单日期',
  `order_money` double(10,3) default NULL COMMENT '订单金额',
  `content` varchar(500) default NULL COMMENT '订单备注',
  `create_by` varchar(32) default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_by` varchar(32) default NULL COMMENT '修改人',
  `update_time` datetime default NULL COMMENT '修改时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jeecg_order_main
-- ----------------------------
INSERT INTO `jeecg_order_main` VALUES ('54e739bef5b67569c963c38da52581ec', 'NC911', '1', '2019-02-18 09:58:51', '40.000', null, 'admin', '2019-02-18 09:58:47', 'admin', '2019-02-18 09:58:59');
INSERT INTO `jeecg_order_main` VALUES ('b190737bd04cca8360e6f87c9ef9ec4e', 'B0018888', '1', null, null, null, 'admin', '2019-02-15 18:39:29', 'admin', '2019-02-15 18:39:37');
INSERT INTO `jeecg_order_main` VALUES ('eb13ab35d2946a2b0cfe3452bca1e73f', 'BJ9980', '1', null, '90.000', null, 'admin', '2019-02-16 17:36:42', 'admin', '2019-02-16 17:46:16');

-- ----------------------------
-- Table structure for jeecg_order_ticket
-- ----------------------------
DROP TABLE IF EXISTS `jeecg_order_ticket`;
CREATE TABLE `jeecg_order_ticket` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `ticket_code` varchar(100) NOT NULL COMMENT '航班号',
  `tickect_date` datetime default NULL COMMENT '航班时间',
  `order_id` varchar(32) NOT NULL COMMENT '外键',
  `create_by` varchar(32) default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_by` varchar(32) default NULL COMMENT '修改人',
  `update_time` datetime default NULL COMMENT '修改时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jeecg_order_ticket
-- ----------------------------
INSERT INTO `jeecg_order_ticket` VALUES ('17fa15b9e8b396338e83dc66d418e9cb', 'ffff', '2019-02-21 00:00:00', 'eb13ab35d2946a2b0cfe3452bca1e73f', 'admin', '2019-02-21 16:24:14', null, null);
INSERT INTO `jeecg_order_ticket` VALUES ('3017078886539ff13f780d6af6ab919f', 'C10029', '2019-02-20 00:00:00', '54e739bef5b67569c963c38da52581ec', 'jeecg', '2019-02-20 17:21:02', null, null);
INSERT INTO `jeecg_order_ticket` VALUES ('57ef198c68dfe0ba83709d3afaa097bc', '111', '2019-02-01 00:00:00', 'b190737bd04cca8360e6f87c9ef9ec4e', 'admin', '2019-02-15 18:39:29', null, null);
INSERT INTO `jeecg_order_ticket` VALUES ('ba9d22c73af43fa109971ee21e3171ce', 'C10019', '2019-02-18 00:00:00', '54e739bef5b67569c963c38da52581ec', 'jeecg', '2019-02-20 17:21:02', null, null);
INSERT INTO `jeecg_order_ticket` VALUES ('c07a0581bdb8915cc70a20fef8e3b2af', '222', '2019-02-23 00:00:00', 'b190737bd04cca8360e6f87c9ef9ec4e', 'admin', '2019-02-15 18:39:29', null, null);

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY  (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY  (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(200) NOT NULL,
  `TIME_ZONE_ID` varchar(80) default NULL,
  PRIMARY KEY  (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------
INSERT INTO `qrtz_cron_triggers` VALUES ('quartzScheduler', 'org.jeecg.modules.quartz.job.SampleJob', 'DEFAULT', '0/1 * * * * ?', 'Asia/Shanghai');

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) default NULL,
  `JOB_GROUP` varchar(200) default NULL,
  `IS_NONCONCURRENT` varchar(1) default NULL,
  `REQUESTS_RECOVERY` varchar(1) default NULL,
  PRIMARY KEY  (`SCHED_NAME`,`ENTRY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) default NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY  (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------
INSERT INTO `qrtz_job_details` VALUES ('quartzScheduler', 'org.jeecg.modules.quartz.job.SampleJob', 'DEFAULT', null, 'org.jeecg.modules.quartz.job.SampleJob', '0', '0', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C77080000001000000001740009706172616D65746572707800);

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY  (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
INSERT INTO `qrtz_locks` VALUES ('quartzScheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY  (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY  (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY  (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) default NULL,
  `STR_PROP_2` varchar(512) default NULL,
  `STR_PROP_3` varchar(512) default NULL,
  `INT_PROP_1` int(11) default NULL,
  `INT_PROP_2` int(11) default NULL,
  `LONG_PROP_1` bigint(20) default NULL,
  `LONG_PROP_2` bigint(20) default NULL,
  `DEC_PROP_1` decimal(13,4) default NULL,
  `DEC_PROP_2` decimal(13,4) default NULL,
  `BOOL_PROP_1` varchar(1) default NULL,
  `BOOL_PROP_2` varchar(1) default NULL,
  PRIMARY KEY  (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) default NULL,
  `NEXT_FIRE_TIME` bigint(13) default NULL,
  `PREV_FIRE_TIME` bigint(13) default NULL,
  `PRIORITY` int(11) default NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) default NULL,
  `CALENDAR_NAME` varchar(200) default NULL,
  `MISFIRE_INSTR` smallint(2) default NULL,
  `JOB_DATA` blob,
  PRIMARY KEY  (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------
INSERT INTO `qrtz_triggers` VALUES ('quartzScheduler', 'org.jeecg.modules.quartz.job.SampleJob', 'DEFAULT', 'org.jeecg.modules.quartz.job.SampleJob', 'DEFAULT', null, '1550751226000', '1550751225000', '5', 'PAUSED', 'CRON', '1547697830000', '0', null, '0', '');

-- ----------------------------
-- Table structure for sys_announcement
-- ----------------------------
DROP TABLE IF EXISTS `sys_announcement`;
CREATE TABLE `sys_announcement` (
  `id` varchar(32) NOT NULL,
  `titile` varchar(100) default NULL COMMENT '标题',
  `msg_content` text COMMENT '内容',
  `start_time` datetime default NULL COMMENT '开始时间',
  `end_time` datetime default NULL COMMENT '结束时间',
  `sender` varchar(100) default NULL COMMENT '发布人',
  `priority` varchar(255) default NULL COMMENT '优先级（L低，M中，H高）',
  `msg_type` varchar(10) default NULL COMMENT '通告对象类型（USER:指定用户，ALL:全体用户）',
  `send_status` varchar(10) default NULL COMMENT '发布状态（0未发布，1已发布，2已撤销）',
  `send_time` datetime default NULL COMMENT '发布时间',
  `cancel_time` datetime default NULL COMMENT '撤销时间',
  `del_flag` varchar(255) default NULL COMMENT '删除状态（0，正常，1已删除）',
  `create_by` varchar(255) default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_by` varchar(255) default NULL COMMENT '更新人',
  `update_time` datetime default NULL COMMENT '更新时间',
  `user_ids` text COMMENT '指定用户',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统通告表';

-- ----------------------------
-- Records of sys_announcement
-- ----------------------------

-- ----------------------------
-- Table structure for sys_announcement_send
-- ----------------------------
DROP TABLE IF EXISTS `sys_announcement_send`;
CREATE TABLE `sys_announcement_send` (
  `id` varchar(32) default NULL,
  `annt_id` varchar(32) default NULL COMMENT '通告ID',
  `user_id` varchar(32) default NULL COMMENT '用户id',
  `read_flag` varchar(10) default NULL COMMENT '阅读状态（0未读，1已读）',
  `read_time` datetime default NULL COMMENT '阅读时间',
  `create_by` varchar(255) default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_by` varchar(255) default NULL COMMENT '更新人',
  `update_time` datetime default NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户通告阅读标记表';

-- ----------------------------
-- Records of sys_announcement_send
-- ----------------------------

-- ----------------------------
-- Table structure for sys_depart
-- ----------------------------
DROP TABLE IF EXISTS `sys_depart`;
CREATE TABLE `sys_depart` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `parent_id` varchar(32) default NULL COMMENT '父机构ID',
  `depart_name` varchar(100) NOT NULL COMMENT '机构/部门名称',
  `depart_name_en` varchar(500) default NULL COMMENT '英文名',
  `depart_name_abbr` varchar(500) default NULL COMMENT '缩写',
  `depart_order` int(11) default '0' COMMENT '排序',
  `description` text COMMENT '描述',
  `org_type` varchar(10) default NULL COMMENT '机构类型',
  `org_code` varchar(64) NOT NULL COMMENT '机构编码',
  `mobile` varchar(32) default NULL COMMENT '手机号',
  `fax` varchar(32) default NULL COMMENT '传真',
  `address` varchar(100) default NULL COMMENT '地址',
  `memo` varchar(500) default NULL COMMENT '备注',
  `status` varchar(10) default NULL COMMENT '状态（1启用，0不启用）',
  `del_flag` varchar(10) default NULL COMMENT '删除状态（0，正常，1已删除）',
  `create_by` varchar(50) default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建日期',
  `update_by` varchar(50) default NULL COMMENT '更新人',
  `update_time` datetime default NULL COMMENT '更新日期',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织机构表';

-- ----------------------------
-- Records of sys_depart
-- ----------------------------
INSERT INTO `sys_depart` VALUES ('4f1765520d6346f9bd9c79e2479e5b12', 'c6d7cb4deeac411cb3384b1b31278596', '市场部', null, null, '0', null, '2', 'A01A03', null, null, null, null, null, '0', 'admin', '2019-02-20 17:15:34', null, null);
INSERT INTO `sys_depart` VALUES ('57197590443c44f083d42ae24ef26a2c', 'c6d7cb4deeac411cb3384b1b31278596', '研发部', null, null, '0', null, '2', 'A01A05', null, null, null, null, null, '0', 'admin', '2019-02-21 16:14:41', null, null);
INSERT INTO `sys_depart` VALUES ('67fc001af12a4f9b8458005d3f19934a', 'c6d7cb4deeac411cb3384b1b31278596', '财务部', null, null, '0', null, '2', 'A01A04', null, null, null, null, null, '0', 'admin', '2019-02-21 16:14:35', 'admin', '2019-02-25 12:49:41');
INSERT INTO `sys_depart` VALUES ('c6d7cb4deeac411cb3384b1b31278596', '', '北京国炬信息技术有限公司', null, null, '0', null, '1', 'A01', null, null, null, null, null, '0', 'admin', '2019-02-11 14:21:51', 'admin', '2019-02-18 23:17:33');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` varchar(32) NOT NULL,
  `dict_name` varchar(255) default NULL COMMENT '字典名称',
  `dict_code` varchar(255) default NULL COMMENT '字典编码',
  `description` varchar(255) default NULL COMMENT '描述',
  `del_flag` int(11) default NULL COMMENT '删除状态',
  `create_by` varchar(255) default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_by` varchar(255) default NULL COMMENT '更新人',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('3d9a351be3436fbefb1307d4cfb49bf2', '性别', 'sex', null, '1', null, '2019-01-04 14:56:32', null, null);
INSERT INTO `sys_dict` VALUES ('6b78e3f59faec1a4750acff08030a79b', '用户类型', 'user_type', null, '1', null, '2019-01-04 14:59:01', null, null);

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item` (
  `id` varchar(255) NOT NULL,
  `dict_id` varchar(255) default NULL COMMENT '字典id',
  `item_text` varchar(255) default NULL COMMENT '字典项文本',
  `item_value` varchar(255) default NULL COMMENT '字典项值',
  `description` varchar(255) default NULL COMMENT '描述',
  `sort_order` decimal(10,2) default NULL COMMENT '排序',
  `status` int(11) default NULL COMMENT '状态（1启用 0不启用）',
  `create_by` varchar(255) default NULL,
  `create_time` datetime default NULL,
  `update_by` varchar(255) default NULL,
  `update_time` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
INSERT INTO `sys_dict_item` VALUES ('5d84a8634c8fdfe96275385075b105c9', '3d9a351be3436fbefb1307d4cfb49bf2', '女', '2', null, '2.00', '1', null, '2019-01-04 14:56:56', null, '2019-01-04 17:38:12');
INSERT INTO `sys_dict_item` VALUES ('700e9f030654f3f90e9ba76ab0713551', '6b78e3f59faec1a4750acff08030a79b', '333', '333', null, null, '1', 'admin', '2019-02-21 19:59:47', null, null);
INSERT INTO `sys_dict_item` VALUES ('b57f98b88363188daf38d42f25991956', '6b78e3f59faec1a4750acff08030a79b', '22', '222', null, null, '1', 'admin', '2019-02-21 19:59:43', null, null);
INSERT INTO `sys_dict_item` VALUES ('df168368dcef46cade2aadd80100d8aa', '3d9a351be3436fbefb1307d4cfb49bf2', '男', '1', null, '1.00', '1', null, '2019-01-04 14:56:49', 'admin', '2019-02-25 12:49:31');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` varchar(255) NOT NULL,
  `log_type` int(11) default NULL COMMENT '日志类型（1登录日志，2操作日志）',
  `log_content` varchar(1000) default NULL COMMENT '日志内容',
  `operate_type` int(11) default NULL COMMENT '操作类型',
  `userid` varchar(255) default NULL COMMENT '操作用户账号',
  `username` varchar(255) default NULL COMMENT '操作用户名称',
  `ip` varchar(255) default NULL COMMENT 'IP',
  `method` varchar(500) default NULL COMMENT '请求java方法',
  `request_url` varchar(255) default NULL COMMENT '请求路径',
  `request_param` varchar(255) default NULL COMMENT '请求参数',
  `request_type` varchar(255) default NULL COMMENT '请求类型',
  `cost_time` bigint(20) default NULL COMMENT '耗时',
  `create_by` varchar(255) default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_by` varchar(255) default NULL COMMENT '更新人',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='系统日志表';

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES ('b09ccd219a1ce5c7270bb659748b8330', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 15:34:38', null, null);
INSERT INTO `sys_log` VALUES ('a22ddd4b5b0b84bd7794edd24b25fc64', '2', '添加测试DEMO', null, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', null, '[{\"createBy\":\"admin\",\"createTime\":1547883299809,\"email\":\"zhangdaiscott@163.com\",\"id\":\"7eac655877842eb39dc2f0469f3964ec\",\"name\":\"zhang daihao\"}]', null, '25', 'admin', '2019-01-19 15:34:59', null, null);
INSERT INTO `sys_log` VALUES ('07a0b3f8b4140a7a586305c2f40a2310', '2', '删除测试DEMO', null, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.delete()', null, '[\"7eac655877842eb39dc2f0469f3964ec\"]', null, '14', 'admin', '2019-01-19 15:38:11', null, null);
INSERT INTO `sys_log` VALUES ('d7902eeab2c34611fad046a79bff1c1b', '2', '添加测试DEMO', null, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', null, '[{\"createBy\":\"admin\",\"createTime\":1547883544104,\"email\":\"zhangdaiscott@163.com\",\"id\":\"4436302a0de50bb83025286bc414d6a9\",\"name\":\"zhang daihao\"}]', null, '1682', 'admin', '2019-01-19 15:39:05', null, null);
INSERT INTO `sys_log` VALUES ('a68160f37cace166fedd299c4ca0be10', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 15:40:00', null, null);
INSERT INTO `sys_log` VALUES ('c6c0316b6989bf1eea0a3803f593bf69', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 15:47:19', null, null);
INSERT INTO `sys_log` VALUES ('4b1341863a8fffeccda8bbe413bd815f', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 15:59:52', null, null);
INSERT INTO `sys_log` VALUES ('ed50b1fbc80c3b953f4551081b10335e', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 16:19:06', null, null);
INSERT INTO `sys_log` VALUES ('dabdcb8e15ea9215a1af22f7567ff73d', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 16:48:13', null, null);
INSERT INTO `sys_log` VALUES ('446724ea6dd41f4a03111c42e00d80cd', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 16:56:36', null, null);
INSERT INTO `sys_log` VALUES ('0e41fe3a34d5715bf4c88e220663583a', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 17:04:06', null, null);
INSERT INTO `sys_log` VALUES ('9f2db1ffaf89518a25cc6701da0c5858', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 17:05:07', null, null);
INSERT INTO `sys_log` VALUES ('954f1ccb8b230d2d7d4858eec3aba0a4', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 17:08:37', null, null);
INSERT INTO `sys_log` VALUES ('7374f3a2ccb20216cf8eecb26037ce0a', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 18:08:51', null, null);
INSERT INTO `sys_log` VALUES ('130de55edac71aab730786307cc65936', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 20:22:57', null, null);
INSERT INTO `sys_log` VALUES ('0bc44e2d682c9f28525d203589a90b43', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 20:31:08', null, null);
INSERT INTO `sys_log` VALUES ('122edcafd54dd06e12838f41123d9d5d', '2', '添加测试DEMO', null, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', null, '[{\"age\":28,\"birthday\":1546617600000,\"createBy\":\"admin\",\"createTime\":1547901234989,\"id\":\"42c08b1a2e5b2a96ffa4cc88383d4b11\",\"name\":\"秦500\",\"punchTime\":1546691611000}]', null, '21387', 'admin', '2019-01-19 20:34:11', null, null);
INSERT INTO `sys_log` VALUES ('1a570aac0c30ac2955b59e2dc7a6204c', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 20:58:26', null, null);
INSERT INTO `sys_log` VALUES ('c18db091677ec01d55e913662b9028a9', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 21:19:07', null, null);
INSERT INTO `sys_log` VALUES ('88d8b4b50bdab58c52fe25fa711fbbef', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 21:21:39', null, null);
INSERT INTO `sys_log` VALUES ('6b876be6e384337b36ad28a4a5868be8', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 21:22:12', null, null);
INSERT INTO `sys_log` VALUES ('cb6b52fbbdd4c5698c17edaf9960e11e', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 21:22:23', null, null);
INSERT INTO `sys_log` VALUES ('fea8e1e2d229557185be0d9a10ebce17', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 21:55:55', null, null);
INSERT INTO `sys_log` VALUES ('c1842fc83cdf0b0cc0264bf093e9c55d', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 21:56:15', null, null);
INSERT INTO `sys_log` VALUES ('543970eba4d1c522e3cb597b0fd4ad13', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 22:53:18', null, null);
INSERT INTO `sys_log` VALUES ('e9ce2b3f7ac1fa3f5f7fd247207ca5c0', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 22:53:35', null, null);
INSERT INTO `sys_log` VALUES ('0e365a21c60e4460813bdc4e3cb320a3', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 23:01:34', null, null);
INSERT INTO `sys_log` VALUES ('d3df1a4057b6d7fb4dab073a727ba21f', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 23:14:02', null, null);
INSERT INTO `sys_log` VALUES ('8f616500d666a5a67bc98e7ccd73c2e2', '2', '添加测试DEMO', null, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', null, '[{\"createBy\":\"admin\",\"createTime\":1547912194199,\"id\":\"ffa9da1ad40632dfcabac51d766865bd\",\"name\":\"秦999\"}]', null, '386', 'admin', '2019-01-19 23:36:34', null, null);
INSERT INTO `sys_log` VALUES ('055cf35c8865761b479c7f289dc36616', '2', '添加测试DEMO', null, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', null, '[{\"createBy\":\"admin\",\"createTime\":1547912238787,\"email\":\"zhangdaiscott@163.com\",\"id\":\"c2c0d49e3c01913067cf8d1fb3c971d2\",\"name\":\"zhang daihao\"}]', null, '16', 'admin', '2019-01-19 23:37:18', null, null);
INSERT INTO `sys_log` VALUES ('69e3164d007be2b9834e4fb398186f39', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-19 23:38:17', null, null);
INSERT INTO `sys_log` VALUES ('92e514fee917a1a459c4ffdb0ca42516', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 10:20:52', null, null);
INSERT INTO `sys_log` VALUES ('d3f08843a9b2b3284711e376fb785beb', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 10:58:03', null, null);
INSERT INTO `sys_log` VALUES ('76bea561f662ec0ccf05bc370f1ffe35', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 11:08:55', null, null);
INSERT INTO `sys_log` VALUES ('273081678d85acebaa6615973bff31db', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 15:02:50', null, null);
INSERT INTO `sys_log` VALUES ('b26369680b41d581649cf865e88331e9', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 15:03:07', null, null);
INSERT INTO `sys_log` VALUES ('7313b43ff53015d79a58b4dc7c660721', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 15:03:07', null, null);
INSERT INTO `sys_log` VALUES ('f99912c5ff252594f14d31b768f8ad15', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 15:03:10', null, null);
INSERT INTO `sys_log` VALUES ('dcec1957987abbe6658f1f2c96980366', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 15:05:21', null, null);
INSERT INTO `sys_log` VALUES ('c7b6156c4f42b70c562b507766f4546c', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 15:14:08', null, null);
INSERT INTO `sys_log` VALUES ('52673feae24ea5bc3ca111f19c9a85d4', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 15:16:46', null, null);
INSERT INTO `sys_log` VALUES ('507b55d3b5ddc487fb40ca1f716a1253', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 15:43:02', null, null);
INSERT INTO `sys_log` VALUES ('7351132f4f5f65e5bf157dd7ad5344a4', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 15:51:36', null, null);
INSERT INTO `sys_log` VALUES ('961992e05772bc7ad2ca927cf7649440', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 15:55:10', null, null);
INSERT INTO `sys_log` VALUES ('3b07fda32423a5696b2097e1c23c00d4', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 16:04:32', null, null);
INSERT INTO `sys_log` VALUES ('8447099784da63b3b2cd2fbbc5eabcea', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 16:04:49', null, null);
INSERT INTO `sys_log` VALUES ('b20ff98a10af3c25c1991741fd59ea64', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 16:07:48', null, null);
INSERT INTO `sys_log` VALUES ('9acebd2d37c9078f9568125fb9696976', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 16:07:59', null, null);
INSERT INTO `sys_log` VALUES ('d70c2847d8d0936a2a761f745a84aa48', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 16:39:34', null, null);
INSERT INTO `sys_log` VALUES ('279e519d647f1a4e1f85f9b90ab370b9', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 17:01:30', null, null);
INSERT INTO `sys_log` VALUES ('b605a83a9b5f3cdaaa1b3f4f41a5f12d', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 17:04:20', null, null);
INSERT INTO `sys_log` VALUES ('0a24b1f04f79a2bcb83c4cd12d077cbc', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 17:34:37', null, null);
INSERT INTO `sys_log` VALUES ('661c4792f00b0814e486c3d623d7259f', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 18:06:11', null, null);
INSERT INTO `sys_log` VALUES ('d1746c5c937fcb650bd835ff74dabdff', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 18:06:32', null, null);
INSERT INTO `sys_log` VALUES ('8ec3a287a37d155047e80a80769d5226', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 18:37:14', null, null);
INSERT INTO `sys_log` VALUES ('6cbd2a9257fae1cb7ff7bc2eb264b3ab', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 19:08:14', null, null);
INSERT INTO `sys_log` VALUES ('f06e8fa83b408be905b4dc7caeaf9a80', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 19:40:33', null, null);
INSERT INTO `sys_log` VALUES ('f84e86c9a21149134b1f2599a424164b', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-21 20:12:27', null, null);
INSERT INTO `sys_log` VALUES ('88bfc5b77b4be0d6d0f7c8661cf24853', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-22 10:25:53', null, null);
INSERT INTO `sys_log` VALUES ('b9bf472a12fc25a9d4b500421b08b025', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-22 10:53:54', null, null);
INSERT INTO `sys_log` VALUES ('dbbcfb7f59311637a613ec9a6c63f04a', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-22 10:53:57', null, null);
INSERT INTO `sys_log` VALUES ('69ea2322f72b41bcdc7f235889132703', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-22 10:54:43', null, null);
INSERT INTO `sys_log` VALUES ('62d197757e2cb40f9e8cb57fa6a207f7', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-22 10:54:54', null, null);
INSERT INTO `sys_log` VALUES ('ccad29843623a6c3ca59548b1d533b15', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-22 10:56:33', null, null);
INSERT INTO `sys_log` VALUES ('4d9299e2daac1f49eac0cec75a90c32e', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-22 11:28:39', null, null);
INSERT INTO `sys_log` VALUES ('43848099c1e70910ba1572868ee40415', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-22 11:28:48', null, null);
INSERT INTO `sys_log` VALUES ('6fb7db45b11bc22347b234fda07700c8', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-22 12:00:22', null, null);
INSERT INTO `sys_log` VALUES ('e8cde8dcd6253b249d67a05aaf10f968', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-22 12:30:28', null, null);
INSERT INTO `sys_log` VALUES ('6a4231540c73ad67128d5a24e6a877ff', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-22 12:54:30', null, null);
INSERT INTO `sys_log` VALUES ('2b3be3da6ba9d1ee49f378d729d69c50', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-22 13:24:41', null, null);
INSERT INTO `sys_log` VALUES ('78f519b618f82a39adad391fbf6b9c7a', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-22 13:49:58', null, null);
INSERT INTO `sys_log` VALUES ('1487d69ff97888f3a899e2ababb5ae48', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-22 14:21:17', null, null);
INSERT INTO `sys_log` VALUES ('cc7fa5567e7833a3475b29b7441a2976', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-22 14:21:31', null, null);
INSERT INTO `sys_log` VALUES ('52e36d72cd04bea2604747e006b038ec', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-22 19:47:17', null, null);
INSERT INTO `sys_log` VALUES ('523a54948d5edaf421566014b66f9465', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-22 19:50:27', null, null);
INSERT INTO `sys_log` VALUES ('48e4e10ac7e583050fd85734f0676a7c', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-22 19:58:08', null, null);
INSERT INTO `sys_log` VALUES ('dee4d42c439b51b228ab5db5d0723fc0', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-22 20:02:56', null, null);
INSERT INTO `sys_log` VALUES ('965c74ffe09d8a06bb817efa6d62254b', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-23 10:01:35', null, null);
INSERT INTO `sys_log` VALUES ('059bac84373e9dae94363ea18802d70f', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-23 10:06:56', null, null);
INSERT INTO `sys_log` VALUES ('9ef3f1ed07003e3abec3445920b062f1', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-23 11:17:05', null, null);
INSERT INTO `sys_log` VALUES ('0169622dcd4e89b177a0917778ac7f9c', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-23 11:17:18', null, null);
INSERT INTO `sys_log` VALUES ('f8960d64e93606fa52220cc9c4ae35a2', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-23 11:21:02', null, null);
INSERT INTO `sys_log` VALUES ('4261867172d0fd5c04c993638661ac0b', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-23 11:24:47', null, null);
INSERT INTO `sys_log` VALUES ('32464c6f7f772ddda0a963b19ad2fd70', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-23 11:30:20', null, null);
INSERT INTO `sys_log` VALUES ('d29cf7aae44523bf2f3d187e91356fe8', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-23 12:20:35', null, null);
INSERT INTO `sys_log` VALUES ('0e9c0d0d26ddc652a7277912e0784d11', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-23 12:27:41', null, null);
INSERT INTO `sys_log` VALUES ('25f8b1b345b1c8a070fe81d715540c85', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-23 15:39:46', null, null);
INSERT INTO `sys_log` VALUES ('8327cced60486bad4009276e14403502', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-24 09:56:29', null, null);
INSERT INTO `sys_log` VALUES ('515c28df59f07478339b61ca5b1b54a8', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-24 10:34:51', null, null);
INSERT INTO `sys_log` VALUES ('fa0612372b332b6c3ce787d9ca6dd2cc', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-24 11:48:21', null, null);
INSERT INTO `sys_log` VALUES ('8300e85a2c2f16c2358d31e8b364edf7', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-24 11:55:39', null, null);
INSERT INTO `sys_log` VALUES ('3d9874f248a984608ca98c36c21c5a7a', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-24 13:05:56', null, null);
INSERT INTO `sys_log` VALUES ('cc8ab347f332c55570830c5fc39bbf9f', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-24 13:08:38', null, null);
INSERT INTO `sys_log` VALUES ('8742a458bf166fd5f134ac65fa8903f9', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-24 13:09:10', null, null);
INSERT INTO `sys_log` VALUES ('bbe2e637bafa0d7f465dc9e1266cff3d', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-25 11:16:50', null, null);
INSERT INTO `sys_log` VALUES ('b3474fc5aad9ec2f36ccbbf7bf864a69', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-25 11:17:24', null, null);
INSERT INTO `sys_log` VALUES ('260bb025d91b59d0135d635ef85eeb82', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-25 11:40:13', null, null);
INSERT INTO `sys_log` VALUES ('1a5b71c9458c17f9bcb19a5747fd47dd', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-25 11:56:48', null, null);
INSERT INTO `sys_log` VALUES ('e720278084b0d4316448ec59d4e3399d', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-25 15:52:45', null, null);
INSERT INTO `sys_log` VALUES ('f6646950c8465da1d1219b7a7a209fc2', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-25 19:14:07', null, null);
INSERT INTO `sys_log` VALUES ('36358cacfc5eb3ba7e85cfe156218b71', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-25 19:14:17', null, null);
INSERT INTO `sys_log` VALUES ('ee2bb63c47c868d59a45503b3d2f34ea', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-25 19:16:14', null, null);
INSERT INTO `sys_log` VALUES ('b0d11dfec52e02f504c63e2f8224b00d', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-25 19:27:44', null, null);
INSERT INTO `sys_log` VALUES ('4acfbc327681d89dab861c77401f8992', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-28 10:54:36', null, null);
INSERT INTO `sys_log` VALUES ('96ada57ac17c4477f4e4c8d596d4cc1a', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-28 10:54:44', null, null);
INSERT INTO `sys_log` VALUES ('e4e40e21437b23b74324e0402cceb71a', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-28 11:34:40', null, null);
INSERT INTO `sys_log` VALUES ('d92d9e003666c6b020f079eaee721f9f', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-28 12:08:43', null, null);
INSERT INTO `sys_log` VALUES ('68f7394ca53c59438b2b41e7bb9f3094', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-28 14:09:34', null, null);
INSERT INTO `sys_log` VALUES ('a9b34565c6460dc9cede00ad150393f9', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-28 14:17:53', null, null);
INSERT INTO `sys_log` VALUES ('fa427f74dc6bd9cca3db478b5842f7f7', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-28 14:19:07', null, null);
INSERT INTO `sys_log` VALUES ('8b66ec251e3107765768dbd0590eeb29', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-28 14:25:48', null, null);
INSERT INTO `sys_log` VALUES ('e42a38382fce916909d6d09f66147006', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-28 14:28:44', null, null);
INSERT INTO `sys_log` VALUES ('ed0bbe9047a7471ae1cdc1c2941eccb1', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-29 17:52:38', null, null);
INSERT INTO `sys_log` VALUES ('36fd54ce8bc1ee4aac9e3ea4bfdcd5a8', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-29 18:49:34', null, null);
INSERT INTO `sys_log` VALUES ('40b3a9bee45b23548250936310b273f4', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-30 14:42:03', null, null);
INSERT INTO `sys_log` VALUES ('c9be887c9292153e39861c91243b7432', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-30 15:12:15', null, null);
INSERT INTO `sys_log` VALUES ('e40823376fa8c0e74a4e760de695e824', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-30 15:36:38', null, null);
INSERT INTO `sys_log` VALUES ('993010965223b8e3a7a784409f7e377e', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-30 15:50:41', null, null);
INSERT INTO `sys_log` VALUES ('aa47c8cf2a4f2de16f415b9d9d3dbf05', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-30 16:14:36', null, null);
INSERT INTO `sys_log` VALUES ('4a0020835a71fc6dcaefd01968d21f81', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-30 18:46:37', null, null);
INSERT INTO `sys_log` VALUES ('fa9cebbb6af23d2830584b3aacd51e46', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-31 13:59:17', null, null);
INSERT INTO `sys_log` VALUES ('60a975067f02cf05e74fa7b71e8e862a', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-31 14:31:58', null, null);
INSERT INTO `sys_log` VALUES ('fbb8834e9736bdd4b6d3baee895c4ca4', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-31 18:05:03', null, null);
INSERT INTO `sys_log` VALUES ('623e4bc7c098f368abcc368227235caf', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-01 09:48:57', null, null);
INSERT INTO `sys_log` VALUES ('9f31eedbe3f3c5c431b490d5fec0094c', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-01 09:56:36', null, null);
INSERT INTO `sys_log` VALUES ('b945fe8b63e0fc26d02c85466f36ebd9', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-01 09:57:34', null, null);
INSERT INTO `sys_log` VALUES ('968d434c45aae64c9ad0e86d18238065', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-01 10:02:22', null, null);
INSERT INTO `sys_log` VALUES ('732a1015057fde25d81ee12a7fbf66b2', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-01 10:05:08', null, null);
INSERT INTO `sys_log` VALUES ('d9a0bb9fe6d2c675aa84f9441c0bd8bb', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-11 10:56:23', null, null);
INSERT INTO `sys_log` VALUES ('9c64406daa2b6e7ad1f6776789d61e43', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-11 10:56:46', null, null);
INSERT INTO `sys_log` VALUES ('1912a44dd4a6ffa1636d2dde9c2f1ab7', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-11 11:01:03', null, null);
INSERT INTO `sys_log` VALUES ('d19b6e77ab1b6d6aa58996a93918754c', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-11 11:33:06', null, null);
INSERT INTO `sys_log` VALUES ('81f7a606359aff9f97f95c15ce8e7c69', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-11 11:33:42', null, null);
INSERT INTO `sys_log` VALUES ('7da063020a42db99e0f3bb9500498828', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-11 13:45:36', null, null);
INSERT INTO `sys_log` VALUES ('b6ee157afd006ceddc8c7558c251192e', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-11 14:20:56', null, null);
INSERT INTO `sys_log` VALUES ('65ace1ae98891f48ab4121d9258e4f1e', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-12 10:45:20', null, null);
INSERT INTO `sys_log` VALUES ('e2af7674bb716a7c0b703c7c7e20b906', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-12 11:38:55', null, null);
INSERT INTO `sys_log` VALUES ('60d4f59974170c67826e64480533d793', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-12 13:25:40', null, null);
INSERT INTO `sys_log` VALUES ('775e987a2ca37edc4f21e022b265a84a', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-12 13:36:13', null, null);
INSERT INTO `sys_log` VALUES ('dd6fbb9b6224c927c0923c16b9285525', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-12 13:37:01', null, null);
INSERT INTO `sys_log` VALUES ('f3d371d6f71409ea2fe52405b725db4a', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-12 13:38:15', null, null);
INSERT INTO `sys_log` VALUES ('c33b4e0bbf998330e44fad65e9d0029e', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-12 15:54:56', null, null);
INSERT INTO `sys_log` VALUES ('189842bf681338dc99dfa66d366a0e6f', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-12 15:55:01', null, null);
INSERT INTO `sys_log` VALUES ('e14cd21cf5eaad9ea3689730a824a50c', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-12 16:12:32', null, null);
INSERT INTO `sys_log` VALUES ('5cf2431447eab30fd3623e831033eea0', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-12 19:17:29', null, null);
INSERT INTO `sys_log` VALUES ('9bfe7312f2951503082a28c2cc966ce4', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-13 10:24:02', null, null);
INSERT INTO `sys_log` VALUES ('da9a15efcf4e1e4f24647db7e2143238', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-13 11:19:54', null, null);
INSERT INTO `sys_log` VALUES ('8317a81bce60a10afeb44af6ef6c807a', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-13 11:27:58', null, null);
INSERT INTO `sys_log` VALUES ('0a6eb1fa998b749012216542a2447ae7', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-13 11:29:30', null, null);
INSERT INTO `sys_log` VALUES ('e5a9b045449136719d4c19c429c2dd56', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-13 13:08:05', null, null);
INSERT INTO `sys_log` VALUES ('aaf10eab9c2b6ed6af1d7a9ce844d146', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-13 13:08:10', null, null);
INSERT INTO `sys_log` VALUES ('b4ccdfc1280e73439eb1ad183076675b', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-13 14:10:45', null, null);
INSERT INTO `sys_log` VALUES ('018fe8d3f049a32fb8b541c893058713', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-13 15:17:42', null, null);
INSERT INTO `sys_log` VALUES ('f3aab8f9dff7bf705aa29c6dcce49011', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-13 15:18:27', null, null);
INSERT INTO `sys_log` VALUES ('efa591832b375b4609a5890b0c6f3250', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-13 16:00:13', null, null);
INSERT INTO `sys_log` VALUES ('2c6822927334eb0810b71465fd9c4945', '1', '用户名: jeecg,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-13 16:02:47', null, null);
INSERT INTO `sys_log` VALUES ('7289cf420ac87ea0538bde81435b1aaa', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-13 16:03:51', null, null);
INSERT INTO `sys_log` VALUES ('db8adca4aa7972fdc283be96d877efe0', '1', '用户名: jeecg,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-13 16:04:28', null, null);
INSERT INTO `sys_log` VALUES ('c5e541648bab341230c93377b4d4e262', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-13 16:05:09', null, null);
INSERT INTO `sys_log` VALUES ('e261674e2640fe6d0a3cd86df631537d', '1', '用户名: jeecg,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-13 16:05:51', null, null);
INSERT INTO `sys_log` VALUES ('406e79995e3340d052d85a74a5d40d1b', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-13 16:23:16', null, null);
INSERT INTO `sys_log` VALUES ('4de1ed55165f7086f1a425a26a2f56ec', '1', '用户名: jeecg,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-13 16:26:27', null, null);
INSERT INTO `sys_log` VALUES ('d8eed69045aae6cedbff402b4e35f495', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-13 18:22:52', null, null);
INSERT INTO `sys_log` VALUES ('bbf4fb593d6918cc767bb50c9b6c16c5', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-14 10:44:20', null, null);
INSERT INTO `sys_log` VALUES ('506ce2d73a038b6e491a35a6c74a7343', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-14 13:44:04', null, null);
INSERT INTO `sys_log` VALUES ('4303dbb3e502f11a3c4078f899bb3070', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-14 17:28:44', null, null);
INSERT INTO `sys_log` VALUES ('2de252a92b59ebfbf16860cc563e3865', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-14 22:04:17', null, null);
INSERT INTO `sys_log` VALUES ('e4c330b381e2fbfde49f1d4dd43e68b7', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-14 22:22:01', null, null);
INSERT INTO `sys_log` VALUES ('22735c059b01949a87cb918f5ef3be76', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-14 22:41:28', null, null);
INSERT INTO `sys_log` VALUES ('c5954beca75d6a0c014e2de3b621275a', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-14 22:41:31', null, null);
INSERT INTO `sys_log` VALUES ('db8c89112bf4706fb558664dd741aa46', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-15 09:33:23', null, null);
INSERT INTO `sys_log` VALUES ('fa0ce422c12a565461eca56006052891', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-15 10:13:21', null, null);
INSERT INTO `sys_log` VALUES ('a34ed4c6fef2b9f07a20e54ef4501b99', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-15 10:48:00', null, null);
INSERT INTO `sys_log` VALUES ('b55cc05e8dd4279c0fa145833db19ba8', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-15 11:37:40', null, null);
INSERT INTO `sys_log` VALUES ('5c675eeb69795180eee2c1069efc114b', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-15 12:59:43', null, null);
INSERT INTO `sys_log` VALUES ('5dee273feb8dd12989b40c2c92ce8c4a', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-15 13:42:58', null, null);
INSERT INTO `sys_log` VALUES ('994efef0ebca19292e14a39b385b0e21', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-15 16:22:24', null, null);
INSERT INTO `sys_log` VALUES ('fc22aaf9660e66558689a58dfa443074', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-15 16:30:55', null, null);
INSERT INTO `sys_log` VALUES ('2c6ede513b83fbc23aaedb89dbfa868a', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-15 18:03:00', null, null);
INSERT INTO `sys_log` VALUES ('13c1e763e9d624a69727a38b85411352', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-15 18:39:00', null, null);
INSERT INTO `sys_log` VALUES ('fadb32d678346ee4bab02997988ff3bc', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-15 22:55:16', null, null);
INSERT INTO `sys_log` VALUES ('0aa792eadeae39a1ed2a98ea5d2f6d27', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-16 09:11:39', null, null);
INSERT INTO `sys_log` VALUES ('0aa9272c0581e1d7f62b1293375b4574', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-16 17:26:36', null, null);
INSERT INTO `sys_log` VALUES ('81c9056ac38e6f881d60f3d41df1845e', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-17 11:44:47', null, null);
INSERT INTO `sys_log` VALUES ('eb4536aa50a58985baf0a763a1ce2ebf', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-17 19:48:49', null, null);
INSERT INTO `sys_log` VALUES ('f9062582881b42f6b139c313d8ab0463', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-17 20:47:26', null, null);
INSERT INTO `sys_log` VALUES ('22d8a2fbd53eafb21f6f62ae073c0fc1', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-17 22:28:52', null, null);
INSERT INTO `sys_log` VALUES ('7bc7b1ff923dbb19fb0ecd800cd690bd', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-18 09:34:59', null, null);
INSERT INTO `sys_log` VALUES ('faea0dbfb7f86b571fed0dd270623831', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-18 14:12:14', null, null);
INSERT INTO `sys_log` VALUES ('063baad688535096d2ed906ae6f3a128', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-18 22:09:21', null, null);
INSERT INTO `sys_log` VALUES ('528baecc596a66eaadc8887bff911f55', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-19 10:08:48', null, null);
INSERT INTO `sys_log` VALUES ('e540ca989819c54baefffbc3d05e8b58', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-19 10:10:54', null, null);
INSERT INTO `sys_log` VALUES ('0643f3ad4394de9fb3c491080c6a7a03', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-19 10:18:42', null, null);
INSERT INTO `sys_log` VALUES ('eb0b8a7cdf77df133566d7bd5a5f1fc0', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-19 11:02:55', null, null);
INSERT INTO `sys_log` VALUES ('0913bb0e92715892c470cf538726dfbc', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-19 16:17:09', null, null);
INSERT INTO `sys_log` VALUES ('5034aec34f0b79da510e66008dbf2fcc', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-19 16:18:29', null, null);
INSERT INTO `sys_log` VALUES ('e4afd66ac249dde9c3bd9da50f9c2469', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-19 17:41:54', null, null);
INSERT INTO `sys_log` VALUES ('07132c1228b1c165f62ea35f4ff1cbe9', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-19 18:15:44', null, null);
INSERT INTO `sys_log` VALUES ('4f7f587bec68ed5bf9f68b0ccd76d62b', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-19 21:01:47', null, null);
INSERT INTO `sys_log` VALUES ('12709e62742056aa4a57fa8c2c82d84a', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 09:13:10', null, null);
INSERT INTO `sys_log` VALUES ('680b3e6e4768d80d6ea0ce8ba71bdd0e', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 09:14:03', null, null);
INSERT INTO `sys_log` VALUES ('a6e323785535592ee208aa7e53554644', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 09:15:27', null, null);
INSERT INTO `sys_log` VALUES ('3a4a0e27d77aa8b624180e5fd5e4004e', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 09:51:58', null, null);
INSERT INTO `sys_log` VALUES ('b98b7ac9e890657aa86a900763afbe2a', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 11:49:44', null, null);
INSERT INTO `sys_log` VALUES ('d1eb2a8ebed28d34199c5fc4a1579c4c', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 12:55:09', null, null);
INSERT INTO `sys_log` VALUES ('85949de2d54078e6b8f3df0a3c79c43d', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 17:08:44', null, null);
INSERT INTO `sys_log` VALUES ('77579d78a903635cc4942882f568e9e5', '1', '用户名: jeecg,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 17:13:33', null, null);
INSERT INTO `sys_log` VALUES ('679e12ba247575749e03aa8f67347ac6', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 17:14:42', null, null);
INSERT INTO `sys_log` VALUES ('5c35117cbeb39428fcc2ddd90ce96a2b', '1', '用户名: jeecg,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 17:18:51', null, null);
INSERT INTO `sys_log` VALUES ('7225200c3cec4789af4f1da2c46b129d', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 17:19:23', null, null);
INSERT INTO `sys_log` VALUES ('22ad9f87788506456c774801389d6a01', '1', '用户名: jeecg,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 17:20:10', null, null);
INSERT INTO `sys_log` VALUES ('81c95e1c8805fa191753fc99ba54c3e9', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 18:01:21', null, null);
INSERT INTO `sys_log` VALUES ('7285730e2644f49def0937dc99bfbe3d', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 18:07:01', null, null);
INSERT INTO `sys_log` VALUES ('4922f2f1173a1edc11dfd11cb2a100ae', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 18:08:27', null, null);
INSERT INTO `sys_log` VALUES ('e37cce529d0c98c47b4977d7ddf963c0', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 18:17:54', null, null);
INSERT INTO `sys_log` VALUES ('66493cd0347eeb6ee2ef5ee923604683', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 18:29:47', null, null);
INSERT INTO `sys_log` VALUES ('f04910792a74c563d057c4fcb345f963', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 18:30:00', null, null);
INSERT INTO `sys_log` VALUES ('210a01dcb34302eaed0d1e95820655d0', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 18:30:15', null, null);
INSERT INTO `sys_log` VALUES ('48929ec94226d9ccff9fae4ff48e95e3', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 18:32:55', null, null);
INSERT INTO `sys_log` VALUES ('d2ac19a709ea08f7259286df28efd635', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 18:35:37', null, null);
INSERT INTO `sys_log` VALUES ('d8fd478e6ceb03a575719e1a54342333', '1', '用户名: jeecg,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 18:43:43', null, null);
INSERT INTO `sys_log` VALUES ('a35a476c303983701045507c9af3fa03', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-20 18:44:58', null, null);
INSERT INTO `sys_log` VALUES ('7e41208e29d412d586fc39375628b0d0', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-21 15:34:35', null, null);
INSERT INTO `sys_log` VALUES ('1f33d11e1833ae497e3ef65a3f02dd5b', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-21 19:51:12', null, null);
INSERT INTO `sys_log` VALUES ('dae0658783324c81fa6909b6e4a25a65', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-22 11:46:41', null, null);
INSERT INTO `sys_log` VALUES ('a77d29673cfe97c9e03cfb879b934f62', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-22 12:41:15', null, null);
INSERT INTO `sys_log` VALUES ('baaf37e5937f938ac92856bc74cc2b86', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-22 13:48:53', null, null);
INSERT INTO `sys_log` VALUES ('3f47afcdce94596494746ac34eebf13b', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-22 13:59:10', null, null);
INSERT INTO `sys_log` VALUES ('b99fc7c53d4e3edc0c618edc11d3a073', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-22 15:58:43', null, null);
INSERT INTO `sys_log` VALUES ('024a4c5ba78538d05373dac650b227d1', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-22 15:59:50', null, null);
INSERT INTO `sys_log` VALUES ('873f425879ef9ca7ced982acda19ea58', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-22 16:35:59', null, null);
INSERT INTO `sys_log` VALUES ('1b05434820cbcb038028da9f5cda31bb', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-22 17:45:16', null, null);
INSERT INTO `sys_log` VALUES ('5f314fc45492d7f90b74d1ca74d1d392', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-22 17:45:48', null, null);
INSERT INTO `sys_log` VALUES ('20751803c1e5b2d758b981ba22f61fcd', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-22 18:11:05', null, null);
INSERT INTO `sys_log` VALUES ('50e8de3e6b45f8625b8fd5590c9fd834', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-22 18:23:18', null, null);
INSERT INTO `sys_log` VALUES ('6737424e01b38f2273e9728bf39f3e37', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-22 19:43:37', null, null);
INSERT INTO `sys_log` VALUES ('0473dedf4aa653b253b008dacff2937c', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-24 13:04:44', null, null);
INSERT INTO `sys_log` VALUES ('a95192071de908f37f4998af4c269bcb', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-24 14:26:53', null, null);
INSERT INTO `sys_log` VALUES ('3569ada5c43a4022d3d13ac801aff40e', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-24 14:50:55', null, null);
INSERT INTO `sys_log` VALUES ('562092eb81561ee0f63be5dd9367d298', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-24 22:20:59', null, null);
INSERT INTO `sys_log` VALUES ('131ccd390401b6e3894a37e4d1d195d3', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-24 22:26:52', null, null);
INSERT INTO `sys_log` VALUES ('c12e3d7655a5a8b192bb9964a2a66946', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-24 22:35:45', null, null);
INSERT INTO `sys_log` VALUES ('6bc98b7dc91a3924f794202867367aca', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-24 22:50:34', null, null);
INSERT INTO `sys_log` VALUES ('002b7112a147edeb6149a891494577d0', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-24 22:52:15', null, null);
INSERT INTO `sys_log` VALUES ('202344b08b69ad70754e6adaa777eae0', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-24 22:54:22', null, null);
INSERT INTO `sys_log` VALUES ('eeb1f2e2c1b480e0bb62533848cbb176', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-24 22:55:46', null, null);
INSERT INTO `sys_log` VALUES ('94fe4465d779e0438cfe6f0cb1a1aa7e', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-24 22:57:42', null, null);
INSERT INTO `sys_log` VALUES ('d03aaee882d13b796db860cb95f27724', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-24 22:59:54', null, null);
INSERT INTO `sys_log` VALUES ('1db82f78233c120c6ec7648ca1177986', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-24 23:07:39', null, null);
INSERT INTO `sys_log` VALUES ('7dc448f04edf4b9655362ad1a1c58753', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-24 23:10:17', null, null);
INSERT INTO `sys_log` VALUES ('fce1553149aea9bfd93e089f387199c8', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-24 23:11:35', null, null);
INSERT INTO `sys_log` VALUES ('e713a89e753cbecf1e10247b2112c3f8', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-24 23:14:36', null, null);
INSERT INTO `sys_log` VALUES ('0a634ed086442afa7a5fc9aa000b898a', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 10:10:39', null, null);
INSERT INTO `sys_log` VALUES ('bfa0766f53dbd3a0fe4043f57bd9bbee', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 10:35:33', null, null);
INSERT INTO `sys_log` VALUES ('e3b531fa12e47ac19a2ab0c883dee595', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 10:40:13', null, null);
INSERT INTO `sys_log` VALUES ('18eafaeec588403245269a41732d1a74', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 10:45:14', null, null);
INSERT INTO `sys_log` VALUES ('99357d793f2507cfb7b270677b4fe56c', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 10:46:42', null, null);
INSERT INTO `sys_log` VALUES ('b38f42f4e15ee72e494bdf6f6feb0ae7', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 10:49:58', null, null);
INSERT INTO `sys_log` VALUES ('bfe758860662ae07a15598396a12cfaa', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 10:50:00', null, null);
INSERT INTO `sys_log` VALUES ('69a7a5b960d6aedda5c4bd8b877be0a8', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 10:50:48', null, null);
INSERT INTO `sys_log` VALUES ('4084f184160940a96e47d7be1fab4ea3', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 10:51:11', null, null);
INSERT INTO `sys_log` VALUES ('1241cf8e9fd0e28478a07bf755f528c5', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 10:51:12', null, null);
INSERT INTO `sys_log` VALUES ('e0da357be27d66de1c9e9b8ecb22f9f9', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 10:51:13', null, null);
INSERT INTO `sys_log` VALUES ('9f4960f89a10d7fdcf22d1ea46143fff', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 10:51:13', null, null);
INSERT INTO `sys_log` VALUES ('ab8a71b7565d356d12e12c6730b0ceb0', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 10:51:13', null, null);
INSERT INTO `sys_log` VALUES ('35fdedc363d9fe514b44095da40f170b', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 10:51:30', null, null);
INSERT INTO `sys_log` VALUES ('7126b35521cd0dba932e6f04b0dac88f', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 10:52:22', null, null);
INSERT INTO `sys_log` VALUES ('9bd6e11c5a2f0bb70215cfa097a4b29c', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 10:57:52', null, null);
INSERT INTO `sys_log` VALUES ('7e2edea80050d2e46aa2e8faef8e29ce', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 11:01:38', null, null);
INSERT INTO `sys_log` VALUES ('190eb7b4d493eb01b13c5b97916eeb13', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 11:09:05', null, null);
INSERT INTO `sys_log` VALUES ('ea268ad02db29012b2f1bd3d4aea1419', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 11:10:22', null, null);
INSERT INTO `sys_log` VALUES ('7dc498b45fbf25c59686d9dda0d3eb66', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 11:12:38', null, null);
INSERT INTO `sys_log` VALUES ('583d3aa445d408f4ecd19ee0a85514af', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 11:18:04', null, null);
INSERT INTO `sys_log` VALUES ('9cea908c7a78dc77fdaed975819983bd', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 11:20:15', null, null);
INSERT INTO `sys_log` VALUES ('d70329497664391dabc25effe7406c50', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 11:25:12', null, null);
INSERT INTO `sys_log` VALUES ('0b9940fc5487026a3f16cade73efead5', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 11:28:28', null, null);
INSERT INTO `sys_log` VALUES ('f21f9f700bf4f5bd9edda7a16ed338f8', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 11:30:05', null, null);
INSERT INTO `sys_log` VALUES ('f5c08b45885d248c422a5d406cd5f223', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 11:41:54', null, null);
INSERT INTO `sys_log` VALUES ('e9a31bfc128b3f5ae01656916c605ddb', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 11:44:56', null, null);
INSERT INTO `sys_log` VALUES ('6baccd034e970c6f109791cff43bc327', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 11:46:12', null, null);
INSERT INTO `sys_log` VALUES ('d2b516c5d834bd0fca91cda416fe499e', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 11:46:42', null, null);
INSERT INTO `sys_log` VALUES ('f9abb524e0dc3571571dc6e50ec6db75', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 11:47:13', null, null);
INSERT INTO `sys_log` VALUES ('d1111594fef195980370c5f91ccf9212', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 11:48:09', null, null);
INSERT INTO `sys_log` VALUES ('9174fe77fe8ba69243f72d5577b391d3', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 11:48:38', null, null);
INSERT INTO `sys_log` VALUES ('2ab9cf95ac35fdbb8fe976e13c404c41', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 11:49:05', null, null);
INSERT INTO `sys_log` VALUES ('9be945480d69038865279f02df5cee45', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 11:49:47', null, null);
INSERT INTO `sys_log` VALUES ('c2bfe3b92e6bfb7016cc82e95419a602', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 11:54:05', null, null);
INSERT INTO `sys_log` VALUES ('7c310b99a84411798a2aaf4074a28e7e', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 12:42:57', null, null);
INSERT INTO `sys_log` VALUES ('b23293288a84ba965509f466ed0e7e2f', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 12:43:11', null, null);
INSERT INTO `sys_log` VALUES ('cf590576a5f6a42b347e6b5bf5ebf5bd', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 12:43:31', null, null);
INSERT INTO `sys_log` VALUES ('b5df1807f08af5db640da11affec24d3', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 12:49:25', null, null);
INSERT INTO `sys_log` VALUES ('2746af3dd0309cdeeff7d27999fbcda1', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 12:52:55', null, null);
INSERT INTO `sys_log` VALUES ('2a383edf5445dc8493f5240144ca72f5', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 12:56:05', null, null);
INSERT INTO `sys_log` VALUES ('d2910961a0ff046cc3ef6cf8d33a8094', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 15:38:47', null, null);
INSERT INTO `sys_log` VALUES ('00f763e007e5a6bddf4cb8e562a53005', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 15:41:31', null, null);
INSERT INTO `sys_log` VALUES ('8ab131214232450ca202103ef81f0a2d', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 15:46:29', null, null);
INSERT INTO `sys_log` VALUES ('606cb4f81f9bb412e2b2bdaa0f3e5dda', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 16:27:23', null, null);
INSERT INTO `sys_log` VALUES ('7b85fba62bc001773fff1a54e1609aef', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 16:28:20', null, null);
INSERT INTO `sys_log` VALUES ('b3127e34f395e1f1790450da5689a4a1', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-02-25 16:28:35', null, null);

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` varchar(32) NOT NULL COMMENT '主键id',
  `parent_id` varchar(32) default NULL COMMENT '父id',
  `name` varchar(255) default NULL COMMENT '菜单标题',
  `url` varchar(255) default NULL COMMENT '路径',
  `component` varchar(255) default NULL COMMENT '组件',
  `redirect` varchar(255) default NULL COMMENT '一级菜单跳转地址',
  `menu_type` int(11) default NULL COMMENT '菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)',
  `perms` varchar(255) default NULL COMMENT '菜单权限编码',
  `sort_no` double(3,2) default NULL COMMENT '菜单排序',
  `always_show` int(3) default NULL COMMENT '聚合子路由: 1是0否',
  `icon` varchar(255) default NULL COMMENT '菜单图标',
  `is_leaf` int(2) default NULL COMMENT '是否叶子节点:    1:是   0:不是',
  `hidden` int(2) default '0' COMMENT '是否隐藏路由: 0否,1是',
  `description` varchar(255) default NULL COMMENT '描述',
  `create_by` varchar(255) default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_by` varchar(255) default NULL COMMENT '更新人',
  `update_time` datetime default NULL COMMENT '更新时间',
  `del_flag` int(11) default '0' COMMENT '删除状态 0正常 1已删除',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单权限表';

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('00a2a0ae65cdca5e93209cdbde97cbe6', '2e42e3835c2b44ec9f7bc26c146ee531', '成功', '/result/success', 'result/Success', null, '1', null, '1.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('05b3c82ddb2536a4a5ee1a4c46b5abef', '540a2936940846cb98114ffb0d145cb8', '用户列表', '/list/user-list', 'list/UserList', null, '1', null, '3.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('078f9558cdeab239aecb2bda1a8ed0d1', 'fb07ca05a3e13674dbf6d3245956da2e', '搜索列表（文章）', '/list/search/article', 'list/TableList', null, '1', null, '1.00', '0', null, '1', '0', null, 'admin', '2019-02-12 14:00:34', 'admin', '2019-02-12 14:17:54', '0');
INSERT INTO `sys_permission` VALUES ('08e6b9dc3c04489c8e1ff2ce6f105aa4', null, '系统监控', '/dashboard3', 'layouts/RouteView', null, '0', null, '2.00', '0', 'dashboard', '0', '0', null, null, '2018-12-25 20:34:38', 'admin', '2019-01-30 15:10:05', '0');
INSERT INTO `sys_permission` VALUES ('13212d3416eb690c2e1d5033166ff47a', '2e42e3835c2b44ec9f7bc26c146ee531', '失败', '/result/fail', 'result/Error', null, '1', null, '2.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('1367a93f2c410b169faa7abcbad2f77c', '6e73eb3c26099c191bf03852ee1310a1', '基本设置', '/account/settings/base', 'account/settings/BaseSetting', null, '1', 'BaseSettings', null, null, null, '1', null, null, null, '2018-12-26 18:58:35', null, null, '0');
INSERT INTO `sys_permission` VALUES ('200006f0edf145a2b50eacca07585451', 'fb07ca05a3e13674dbf6d3245956da2e', '搜索列表（应用）', '/list/search/application', 'list/TableList', null, '1', null, '1.00', '0', null, '1', '0', null, 'admin', '2019-02-12 14:02:51', 'admin', '2019-02-12 14:14:01', '0');
INSERT INTO `sys_permission` VALUES ('277bfabef7d76e89b33062b16a9a5020', 'e3c13679c73a4f829bcff2aba8fd68b1', '基础表单', '/form/base-form', 'form/BasicForm', null, '1', null, '1.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('2a470fc0c3954d9dbb61de6d80846549', null, '常见案例', '/jeecg', 'layouts/RouteView', null, '0', null, '3.00', '0', 'qrcode', '0', '0', null, null, '2018-12-25 20:34:38', 'admin', '2019-02-19 22:46:09', '0');
INSERT INTO `sys_permission` VALUES ('2dbbafa22cda07fa5d169d741b81fe12', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '在线文档', 'http://localhost:8080/jeecg-boot/swagger-ui.html#/', 'layouts/IframePageView', null, '1', null, '3.00', '0', null, '1', '0', null, 'admin', '2019-01-30 10:00:01', null, null, '0');
INSERT INTO `sys_permission` VALUES ('2e42e3835c2b44ec9f7bc26c146ee531', null, '结果页', '/result', 'layouts/PageView', null, '0', null, '7.00', null, 'check-circle-o', '0', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('339329ed54cf255e1f9392e84f136901', '2a470fc0c3954d9dbb61de6d80846549', 'helloworld', '/jeecg/helloworld', 'jeecg/helloworld', null, '1', null, '4.00', '0', null, '1', '0', null, null, '2018-12-25 20:34:38', 'admin', '2019-02-15 16:24:56', '0');
INSERT INTO `sys_permission` VALUES ('3f915b2769fc80648e92d04e84ca059d', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '用户管理', '/isystem/user', 'system/UserList', null, '1', null, '1.00', '0', null, '1', '0', null, null, '2018-12-25 20:34:38', 'admin', '2019-01-23 11:52:22', '0');
INSERT INTO `sys_permission` VALUES ('4148ec82b6acd69f470bea75fe41c357', '2a470fc0c3954d9dbb61de6d80846549', '单表模型示例', '/jeecg/jeecgDemoList', 'jeecg/JeecgDemoList', null, '1', null, '1.00', '0', null, '1', '0', null, null, '2018-12-28 15:57:30', 'admin', '2019-02-15 16:24:37', '0');
INSERT INTO `sys_permission` VALUES ('418964ba087b90a84897b62474496b93', '540a2936940846cb98114ffb0d145cb8', '查询表格', '/list/query-list', 'list/TableList', null, '1', null, '1.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('45c966826eeff4c99b8f8ebfe74511fc', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '部门管理', '/isystem/depart', 'system/DepartList', null, '1', null, '1.00', '0', null, '1', '0', null, 'admin', '2019-01-29 18:47:40', 'admin', '2019-01-29 18:51:29', '0');
INSERT INTO `sys_permission` VALUES ('4875ebe289344e14844d8e3ea1edd73f', null, '详情页', '/profile', 'layouts/RouteView', null, '0', null, '6.00', null, 'profile', '0', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('4f66409ef3bbd69c1d80469d6e2a885e', '6e73eb3c26099c191bf03852ee1310a1', '账户绑定', '/account/settings/binding', 'account/settings/Binding', null, '1', 'BindingSettings', null, null, null, '1', null, null, null, '2018-12-26 19:01:20', null, null, '0');
INSERT INTO `sys_permission` VALUES ('4f84f9400e5e92c95f05b554724c2b58', '540a2936940846cb98114ffb0d145cb8', '角色列表', '/list/role-list', 'list/RoleList', null, '1', null, '4.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('540a2936940846cb98114ffb0d145cb8', null, '列表页', '/list', 'layouts/PageView', '/list/query-list', '0', null, '5.00', '0', 'table', '0', '0', null, null, '2018-12-25 20:34:38', 'admin', '2019-02-12 14:55:20', '0');
INSERT INTO `sys_permission` VALUES ('54dd5457a3190740005c1bfec55b1c34', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '菜单管理', '/isystem/permission', 'system/PermissionList', null, '1', null, '3.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('58857ff846e61794c69208e9d3a85466', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '日志管理', '/isystem/log', 'system/LogList', null, '1', null, '4.00', '0', 'question-circle', '1', '0', null, null, '2018-12-26 10:11:18', 'admin', '2019-02-19 16:38:58', '0');
INSERT INTO `sys_permission` VALUES ('6531cf3421b1265aeeeabaab5e176e6d', 'e3c13679c73a4f829bcff2aba8fd68b1', '分步表单', '/form/step-form', 'form/stepForm/StepForm', null, '1', null, '2.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('65a8f489f25a345836b7f44b1181197a', 'c65321e57b7949b7a975313220de0422', '403', '/exception/403', 'exception/403', null, '1', null, '1.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('693ce69af3432bd00be13c3971a57961', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '监控页', '/dashboard/monitor', 'dashboard/Monitor', null, '1', null, '2.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('6ad53fd1b220989a8b71ff482d683a5a', '2a470fc0c3954d9dbb61de6d80846549', '一对多Tab示例', '/jeecg/tablist/JeecgOrderDMainList', 'jeecg/tablist/JeecgOrderDMainList', null, '1', null, '2.00', '0', null, '1', '0', null, 'admin', '2019-02-20 14:45:09', 'admin', '2019-02-21 16:26:21', '0');
INSERT INTO `sys_permission` VALUES ('6e73eb3c26099c191bf03852ee1310a1', '717f6bee46f44a3897eca9abd6e2ec44', '个人设置', '/account/settings', 'account/settings/Index', null, '1', null, '2.00', '1', null, '0', null, null, null, '2018-12-25 20:34:38', null, '2018-12-26 19:05:26', '0');
INSERT INTO `sys_permission` VALUES ('717f6bee46f44a3897eca9abd6e2ec44', null, '个人页', '/account', 'layouts/RouteView', null, '0', null, '9.00', '0', 'user', '0', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('73678f9daa45ed17a3674131b03432fb', '540a2936940846cb98114ffb0d145cb8', '权限列表', '/list/permission-list', 'list/PermissionList', null, '1', null, '5.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('7ac9eb9ccbde2f7a033cd4944272bf1e', '540a2936940846cb98114ffb0d145cb8', '卡片列表', '/list/card', 'list/CardList', null, '1', null, '7.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('882a73768cfd7f78f3a37584f7299656', '6e73eb3c26099c191bf03852ee1310a1', '个性化设置', '/account/settings/custom', 'account/settings/Custom', null, '1', 'CustomSettings', null, null, null, '1', null, null, null, '2018-12-26 19:00:46', null, '2018-12-26 21:13:25', '0');
INSERT INTO `sys_permission` VALUES ('8fb8172747a78756c11916216b8b8066', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '工作台', '/dashboard/workplace', 'dashboard/Workplace', null, '1', null, '3.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('9502685863ab87f0ad1134142788a385', '', '首页', '/dashboard/analysis', 'dashboard/Analysis', null, '0', null, '1.00', '0', 'bank', '1', '0', null, null, '2018-12-25 20:34:38', 'admin', '2019-02-22 12:40:02', '0');
INSERT INTO `sys_permission` VALUES ('a400e4f4d54f79bf5ce160a3432231af', '2a470fc0c3954d9dbb61de6d80846549', 'online表单', 'http://localhost:8080/jeecg-boot/auto/cgform/list', 'layouts/IframePageView', null, '1', null, '4.00', '0', null, '1', '0', null, 'admin', '2019-01-29 19:44:06', 'admin', '2019-02-15 16:25:11', '0');
INSERT INTO `sys_permission` VALUES ('a400e4f4d54f79bf5ce160ae432231af', '2a470fc0c3954d9dbb61de6d80846549', '百度', 'http://www.baidu.com', 'layouts/IframePageView', null, '1', null, '4.00', '0', null, '1', '0', null, 'admin', '2019-01-29 19:44:06', 'admin', '2019-02-15 16:25:02', '0');
INSERT INTO `sys_permission` VALUES ('ae4fed059f67086fd52a73d913cf473d', '540a2936940846cb98114ffb0d145cb8', '内联编辑表格', '/list/edit-table', 'list/TableInnerEditList', null, '1', null, '2.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('aedbf679b5773c1f25e9f7b10111da73', '08e6b9dc3c04489c8e1ff2ce6f105aa4', 'SQL监控', 'http://localhost:8080/jeecg-boot/druid/', 'layouts/IframePageView', null, '1', null, '1.00', '0', null, '1', '0', null, 'admin', '2019-01-30 09:43:22', null, null, '0');
INSERT INTO `sys_permission` VALUES ('b1cb0a3fedf7ed0e4653cb5a229837ee', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '定时任务', '/isystem/QuartzJobList', 'system/QuartzJobList', null, '1', null, '5.00', null, null, '1', null, null, null, '2019-01-03 09:38:52', 'admin', '2019-01-19 14:08:59', '0');
INSERT INTO `sys_permission` VALUES ('b3c824fc22bd953e2eb16ae6914ac8f9', '4875ebe289344e14844d8e3ea1edd73f', '高级详情页', '/profile/advanced', 'profile/advanced/Advanced', null, '1', null, '2.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('b4dfc7d5dd9e8d5b6dd6d4579b1aa559', 'c65321e57b7949b7a975313220de0422', '500', '/exception/500', 'exception/500', null, '1', null, '3.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('c65321e57b7949b7a975313220de0422', null, '异常页', '/exception', 'layouts/RouteView', null, '0', null, '8.00', null, 'warning', '0', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('c6cf95444d80435eb37b2f9db3971ae6', '2a470fc0c3954d9dbb61de6d80846549', '数据回执模拟', '/jeecg/FlowTest', 'jeecg/FlowTest', null, '1', null, '6.00', '0', null, '1', '0', null, 'admin', '2019-02-19 16:02:23', 'admin', '2019-02-21 16:25:45', '0');
INSERT INTO `sys_permission` VALUES ('cc50656cf9ca528e6f2150eba4714ad2', '4875ebe289344e14844d8e3ea1edd73f', '基础详情页', '/profile/basic', 'profile/basic/Index', null, '1', null, '1.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('d2bbf9ebca5a8fa2e227af97d2da7548', 'c65321e57b7949b7a975313220de0422', '404', '/exception/404', 'exception/404', null, '1', null, '2.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('d7d6e2e4e2934f2c9385a623fd98c6f3', null, '系统管理', '/isystem', 'layouts/RouteView', null, '0', null, '1.00', '0', 'setting', '0', '0', null, null, '2018-12-25 20:34:38', 'admin', '2019-02-22 13:49:30', '0');
INSERT INTO `sys_permission` VALUES ('d86f58e7ab516d3bc6bfb1fe10585f97', '717f6bee46f44a3897eca9abd6e2ec44', '个人中心', '/account/center', 'account/center/Index', null, '1', null, '1.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('de13e0f6328c069748de7399fcc1dbbd', 'fb07ca05a3e13674dbf6d3245956da2e', '搜索列表（项目）', '/list/search/project', 'list/TableList', null, '1', null, '1.00', '0', null, '1', '0', null, 'admin', '2019-02-12 14:01:40', 'admin', '2019-02-12 14:14:18', '0');
INSERT INTO `sys_permission` VALUES ('e08cb190ef230d5d4f03824198773950', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '系统通告', '/isystem/annountCement', 'system/SysAnnouncementList', null, '1', 'annountCement', '6.00', null, '', '1', null, null, null, '2019-01-02 17:23:01', null, '2019-01-02 17:31:23', '0');
INSERT INTO `sys_permission` VALUES ('e3c13679c73a4f829bcff2aba8fd68b1', null, '表单页', '/form', 'layouts/PageView', null, '0', null, '4.00', null, 'form', '0', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('e5973686ed495c379d829ea8b2881fc6', 'e3c13679c73a4f829bcff2aba8fd68b1', '高级表单', '/form/advanced-form', 'form/advancedForm/AdvancedForm', null, '1', null, '3.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('e6bfd1fcabfd7942fdd05f076d1dad38', '2a470fc0c3954d9dbb61de6d80846549', '打印测试', '/jeecg/PrintDemoList', 'jeecg/PrintDemoList', null, '1', null, '3.00', '0', null, '1', '0', null, 'admin', '2019-02-19 15:58:48', null, null, '0');
INSERT INTO `sys_permission` VALUES ('e8af452d8948ea49d37c934f5100ae6a', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '角色管理', '/isystem/role', 'system/RoleList', null, '1', null, '2.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('ec8d607d0156e198b11853760319c646', '6e73eb3c26099c191bf03852ee1310a1', '安全设置', '/account/settings/security', 'account/settings/Security', null, '1', 'SecuritySettings', null, null, null, '1', null, null, null, '2018-12-26 18:59:52', null, null, '0');
INSERT INTO `sys_permission` VALUES ('f1cb187abf927c88b89470d08615f5ac', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '数据字典', '/isystem/dict', 'system/DictList', null, '1', null, '5.00', null, null, '1', null, null, null, '2018-12-28 13:54:43', null, '2018-12-28 15:37:54', '0');
INSERT INTO `sys_permission` VALUES ('f23d9bfff4d9aa6b68569ba2cff38415', '540a2936940846cb98114ffb0d145cb8', '标准列表', '/list/basic-list', 'list/StandardList', null, '1', null, '6.00', null, null, '1', null, null, null, '2018-12-25 20:34:38', null, null, '0');
INSERT INTO `sys_permission` VALUES ('fb07ca05a3e13674dbf6d3245956da2e', '540a2936940846cb98114ffb0d145cb8', '搜索列表', '/list/search', 'list/search/SearchLayout', '/list/search/article', '1', null, '8.00', '0', null, '0', '0', null, null, '2018-12-25 20:34:38', 'admin', '2019-02-12 15:09:13', '0');
INSERT INTO `sys_permission` VALUES ('fb367426764077dcf94640c843733985', '2a470fc0c3954d9dbb61de6d80846549', '一对多示例', '/jeecg/JeecgOrderMainList', 'jeecg/JeecgOrderMainList', null, '1', null, '2.00', '0', null, '1', '0', null, 'admin', '2019-02-15 16:24:11', 'admin', '2019-02-18 10:50:14', '0');
INSERT INTO `sys_permission` VALUES ('fedfbf4420536cacc0218557d263dfea', '6e73eb3c26099c191bf03852ee1310a1', '新消息通知', '/account/settings/notification', 'account/settings/Notification', null, '1', 'NotificationSettings', null, null, '', '1', null, null, null, '2018-12-26 19:02:05', null, null, '0');

-- ----------------------------
-- Table structure for sys_quartz_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_quartz_job`;
CREATE TABLE `sys_quartz_job` (
  `id` varchar(255) NOT NULL,
  `create_by` varchar(255) default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `del_flag` int(11) default NULL COMMENT '删除状态',
  `update_by` varchar(255) default NULL COMMENT '修改人',
  `update_time` datetime default NULL COMMENT '修改时间',
  `job_class_name` varchar(255) default NULL COMMENT '任务类名',
  `cron_expression` varchar(255) default NULL COMMENT 'cron表达式',
  `parameter` varchar(255) default NULL COMMENT '参数',
  `description` varchar(255) default NULL COMMENT '描述',
  `status` int(11) default NULL COMMENT '状态 0正常 -1停止',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_quartz_job
-- ----------------------------
INSERT INTO `sys_quartz_job` VALUES ('df26ecacf0f75d219d746750fe84bbee', null, null, '0', 'admin', '2019-01-19 15:09:41', 'org.jeecg.modules.quartz.job.SampleParamJob', '0/1 * * * * ?', 'scott', '带参测试 后台将每隔1秒执行输出日志', '-1');
INSERT INTO `sys_quartz_job` VALUES ('58180f2a7c8cd36a121fd0fff3f02a36', null, null, '0', 'admin', '2019-01-19 15:09:44', 'org.jeecg.modules.quartz.job.SampleJob', '0/1 * * * * ?', null, null, '-1');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` varchar(32) NOT NULL COMMENT '主键id',
  `role_name` varchar(255) default NULL COMMENT '角色名称',
  `role_code` varchar(255) default NULL COMMENT '角色编码',
  `description` varchar(255) default NULL COMMENT '描述',
  `create_by` varchar(255) default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_by` varchar(255) default NULL COMMENT '更新人',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('e51758fa916c881624b046d26bd09230', '人力资源部', 'hr', null, 'admin', '2019-01-21 18:07:24', 'admin', '2019-02-25 12:50:57');
INSERT INTO `sys_role` VALUES ('ee8626f80f7c2619917b6236f3a7f02b', '临时角色', 'test', '这是新建的临时角色123', null, '2018-12-20 10:59:04', 'admin', '2019-02-19 15:08:37');
INSERT INTO `sys_role` VALUES ('f6817f48af4fb3af11b9e8bf182f618b', '管理员', 'admin', '管理员', null, '2018-12-21 18:03:39', 'admin', '2019-02-22 19:49:42');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` varchar(32) NOT NULL,
  `role_id` varchar(32) default NULL COMMENT '角色id',
  `permission_id` varchar(32) default NULL COMMENT '权限id',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限表';

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('00597d88ce793df4baa9e84a1584bb04', 'ee8626f80f7c2619917b6236f3a7f02b', '4148ec82b6acd69f470bea75fe41c357');
INSERT INTO `sys_role_permission` VALUES ('00b0748f04d3ea52c8cfa179c1c9d384', '52b0cf022ac4187b2a70dfa4f8b2d940', 'd7d6e2e4e2934f2c9385a623fd98c6f3');
INSERT INTO `sys_role_permission` VALUES ('0156071f9a513ecebe28d821cfe936a9', 'f6817f48af4fb3af11b9e8bf182f618b', 'd2bbf9ebca5a8fa2e227af97d2da7548');
INSERT INTO `sys_role_permission` VALUES ('0243ce4bfcb4a1cbf8d88bcce5b423f6', 'f6817f48af4fb3af11b9e8bf182f618b', 'f23d9bfff4d9aa6b68569ba2cff38415');
INSERT INTO `sys_role_permission` VALUES ('02fce9a21ba1b273a9be22e6d08aa888', 'ee8626f80f7c2619917b6236f3a7f02b', 'fedfbf4420536cacc0218557d263dfea');
INSERT INTO `sys_role_permission` VALUES ('03e071916bac02c5e56892f5fff6fad4', 'ee8626f80f7c2619917b6236f3a7f02b', '45c966826eeff4c99b8f8ebfe74511fc');
INSERT INTO `sys_role_permission` VALUES ('04a5e9edc188b4fb2d4d7c97433699ad', 'ee8626f80f7c2619917b6236f3a7f02b', '717f6bee46f44a3897eca9abd6e2ec44');
INSERT INTO `sys_role_permission` VALUES ('050749293826f69e34e17ad9e881aeb5', 'ee8626f80f7c2619917b6236f3a7f02b', 'd2bbf9ebca5a8fa2e227af97d2da7548');
INSERT INTO `sys_role_permission` VALUES ('073aca2db3df7032ff2a2367dfcb36be', 'f6817f48af4fb3af11b9e8bf182f618b', '418964ba087b90a84897b62474496b93');
INSERT INTO `sys_role_permission` VALUES ('07def0a8f102a6088f5fdc5a7f2de13c', 'ee8626f80f7c2619917b6236f3a7f02b', '4f84f9400e5e92c95f05b554724c2b58');
INSERT INTO `sys_role_permission` VALUES ('0a84e65fe004524ce1dcfbbc9c5d158d', 'ee8626f80f7c2619917b6236f3a7f02b', 'cc50656cf9ca528e6f2150eba4714ad2');
INSERT INTO `sys_role_permission` VALUES ('0b59fa905bd7cd390c1716c7600fd8b6', 'e51758fa916c881624b046d26bd09230', '4148ec82b6acd69f470bea75fe41c357');
INSERT INTO `sys_role_permission` VALUES ('0e01985969b267138ea79289f4d80992', 'f6817f48af4fb3af11b9e8bf182f618b', '13212d3416eb690c2e1d5033166ff47a');
INSERT INTO `sys_role_permission` VALUES ('131ec4598bb76ca85c4bb4a903f17cec', 'ee8626f80f7c2619917b6236f3a7f02b', '3f915b2769fc80648e92d04e84ca059d');
INSERT INTO `sys_role_permission` VALUES ('139f250153158eacc56d717800a744fe', 'ee8626f80f7c2619917b6236f3a7f02b', '882a73768cfd7f78f3a37584f7299656');
INSERT INTO `sys_role_permission` VALUES ('14cf7edb826d41ada12c97fc43289dc4', 'ee8626f80f7c2619917b6236f3a7f02b', '7ac9eb9ccbde2f7a033cd4944272bf1e');
INSERT INTO `sys_role_permission` VALUES ('1605b849beecfdb0364dbefa5fe14164', 'ee8626f80f7c2619917b6236f3a7f02b', '58857ff846e61794c69208e9d3a85466');
INSERT INTO `sys_role_permission` VALUES ('17a3b2758daf79e3bf071c38c9f3fd72', 'ee8626f80f7c2619917b6236f3a7f02b', '2dbbafa22cda07fa5d169d741b81fe12');
INSERT INTO `sys_role_permission` VALUES ('17ead5b7d97ed365398ab20009a69ea3', '52b0cf022ac4187b2a70dfa4f8b2d940', 'e08cb190ef230d5d4f03824198773950');
INSERT INTO `sys_role_permission` VALUES ('1ac1688ef8456f384091a03d88a89ab1', '52b0cf022ac4187b2a70dfa4f8b2d940', '693ce69af3432bd00be13c3971a57961');
INSERT INTO `sys_role_permission` VALUES ('1adc4cf29655e34e172227ed23f69456', 'ee8626f80f7c2619917b6236f3a7f02b', 'fb07ca05a3e13674dbf6d3245956da2e');
INSERT INTO `sys_role_permission` VALUES ('1dd46063cefb13c1c0832328e755fdd6', 'ee8626f80f7c2619917b6236f3a7f02b', 'c6cf95444d80435eb37b2f9db3971ae6');
INSERT INTO `sys_role_permission` VALUES ('1f2d90dddd4a541f6e5739eb696c43c5', 'f6817f48af4fb3af11b9e8bf182f618b', 'b3c824fc22bd953e2eb16ae6914ac8f9');
INSERT INTO `sys_role_permission` VALUES ('1fe4d408b85f19618c15bcb768f0ec22', '1750a8fb3e6d90cb7957c02de1dc8e59', '9502685863ab87f0ad1134142788a385');
INSERT INTO `sys_role_permission` VALUES ('20c5d914795847f1578a720407d45e01', 'f6817f48af4fb3af11b9e8bf182f618b', '58857ff846e61794c69208e9d3a85466');
INSERT INTO `sys_role_permission` VALUES ('21843cd6bcce21a3be77c56357c9e15e', 'f6817f48af4fb3af11b9e8bf182f618b', '882a73768cfd7f78f3a37584f7299656');
INSERT INTO `sys_role_permission` VALUES ('2339c57dd0e5ccb690d0b1cf59167e55', 'f6817f48af4fb3af11b9e8bf182f618b', '8f486c17bf7016301432019fdba7aed8');
INSERT INTO `sys_role_permission` VALUES ('241681d67a51a30252f0fa39e0b6afeb', 'ee8626f80f7c2619917b6236f3a7f02b', 'f23d9bfff4d9aa6b68569ba2cff38415');
INSERT INTO `sys_role_permission` VALUES ('248d288586c6ff3bd14381565df84163', '52b0cf022ac4187b2a70dfa4f8b2d940', '3f915b2769fc80648e92d04e84ca059d');
INSERT INTO `sys_role_permission` VALUES ('26efbc128b53ec664d51cb123aa4af48', 'f6817f48af4fb3af11b9e8bf182f618b', 'b2fc6087dfb66fa4a9d0323b10143aad');
INSERT INTO `sys_role_permission` VALUES ('2710a82c7aa13eec37cdc0666a249770', 'ee8626f80f7c2619917b6236f3a7f02b', 'f1cb187abf927c88b89470d08615f5ac');
INSERT INTO `sys_role_permission` VALUES ('28d1a189ff4c3f2fafe72b9685e2419b', 'f6817f48af4fb3af11b9e8bf182f618b', 'a400e4f4d54f79bf5ce160ae432231af');
INSERT INTO `sys_role_permission` VALUES ('28f53e49d49bd85e26b4c6fe8bc16882', 'f6817f48af4fb3af11b9e8bf182f618b', '9017fb8c7485d04c342e2396c481a045');
INSERT INTO `sys_role_permission` VALUES ('2c462293cbb0eab7e8ae0a3600361b5f', '52b0cf022ac4187b2a70dfa4f8b2d940', '9502685863ab87f0ad1134142788a385');
INSERT INTO `sys_role_permission` VALUES ('2dc5ffc63bd188ae5687522b6de3e645', 'ee8626f80f7c2619917b6236f3a7f02b', 'e5973686ed495c379d829ea8b2881fc6');
INSERT INTO `sys_role_permission` VALUES ('2ecb2f5b5acdeb831c4b55b34563a638', 'ee8626f80f7c2619917b6236f3a7f02b', '6e73eb3c26099c191bf03852ee1310a1');
INSERT INTO `sys_role_permission` VALUES ('2fdaed22dfa4c8d4629e44ef81688c6a', '52b0cf022ac4187b2a70dfa4f8b2d940', 'aedbf679b5773c1f25e9f7b10111da73');
INSERT INTO `sys_role_permission` VALUES ('300c462b7fec09e2ff32574ef8b3f0bd', '52b0cf022ac4187b2a70dfa4f8b2d940', '2a470fc0c3954d9dbb61de6d80846549');
INSERT INTO `sys_role_permission` VALUES ('3126647f19dce968218cfb02a16db559', 'e51758fa916c881624b046d26bd09230', '45c966826eeff4c99b8f8ebfe74511fc');
INSERT INTO `sys_role_permission` VALUES ('35ac7cae648de39eb56213ca1b649713', '52b0cf022ac4187b2a70dfa4f8b2d940', 'b1cb0a3fedf7ed0e4653cb5a229837ee');
INSERT INTO `sys_role_permission` VALUES ('3684f08c808866feabfa3d8954524f1c', 'f6817f48af4fb3af11b9e8bf182f618b', '05b3c82ddb2536a4a5ee1a4c46b5abef');
INSERT INTO `sys_role_permission` VALUES ('37e471af5ee4521192dae98f949fe625', 'ee8626f80f7c2619917b6236f3a7f02b', '200006f0edf145a2b50eacca07585451');
INSERT INTO `sys_role_permission` VALUES ('388657cba491daddde0f6651f101089a', 'ee8626f80f7c2619917b6236f3a7f02b', '277bfabef7d76e89b33062b16a9a5020');
INSERT INTO `sys_role_permission` VALUES ('39e3af8b87c5729b082c64f0516ebc97', 'e51758fa916c881624b046d26bd09230', 'a400e4f4d54f79bf5ce160ae432231af');
INSERT INTO `sys_role_permission` VALUES ('3bef3422fb97c2b51f4af97c6a497185', 'e51758fa916c881624b046d26bd09230', 'b4dfc7d5dd9e8d5b6dd6d4579b1aa559');
INSERT INTO `sys_role_permission` VALUES ('3cd59ac3de34288a2558dbbfb71d12b1', 'ee8626f80f7c2619917b6236f3a7f02b', 'aedbf679b5773c1f25e9f7b10111da73');
INSERT INTO `sys_role_permission` VALUES ('3da5a50baf32a65966b27fed94eb78ca', 'ee8626f80f7c2619917b6236f3a7f02b', 'e08cb190ef230d5d4f03824198773950');
INSERT INTO `sys_role_permission` VALUES ('3e563751942b0879c88ca4de19757b50', '1750a8fb3e6d90cb7957c02de1dc8e59', '58857ff846e61794c69208e9d3a85466');
INSERT INTO `sys_role_permission` VALUES ('406b325850d869ef3ca22451db3d69e2', 'ee8626f80f7c2619917b6236f3a7f02b', '4f66409ef3bbd69c1d80469d6e2a885e');
INSERT INTO `sys_role_permission` VALUES ('412e2de37a35b3442d68db8dd2f3c190', '52b0cf022ac4187b2a70dfa4f8b2d940', 'f1cb187abf927c88b89470d08615f5ac');
INSERT INTO `sys_role_permission` VALUES ('42538c6d8add1897482080420956fe79', 'ee8626f80f7c2619917b6236f3a7f02b', '4875ebe289344e14844d8e3ea1edd73f');
INSERT INTO `sys_role_permission` VALUES ('4439453c41bb4cb39890ae0426be798f', 'ee8626f80f7c2619917b6236f3a7f02b', '2e42e3835c2b44ec9f7bc26c146ee531');
INSERT INTO `sys_role_permission` VALUES ('4825c27d3e256fb4d50ab54cb76365ec', 'e51758fa916c881624b046d26bd09230', '13212d3416eb690c2e1d5033166ff47a');
INSERT INTO `sys_role_permission` VALUES ('48a59d6192d6db2053341056a3b1d079', 'f6817f48af4fb3af11b9e8bf182f618b', '4875ebe289344e14844d8e3ea1edd73f');
INSERT INTO `sys_role_permission` VALUES ('48ec3c2c2fc487e86078aaf4e14273be', 'f6817f48af4fb3af11b9e8bf182f618b', 'd7d6e2e4e2934f2c9385a623fd98c6f3');
INSERT INTO `sys_role_permission` VALUES ('4a1f54fa08889ff9d63f83e01fbfb5ef', 'f6817f48af4fb3af11b9e8bf182f618b', '73678f9daa45ed17a3674131b03432fb');
INSERT INTO `sys_role_permission` VALUES ('4b9eac9fd79a1ca63be34b2a6b640738', 'f6817f48af4fb3af11b9e8bf182f618b', '4f66409ef3bbd69c1d80469d6e2a885e');
INSERT INTO `sys_role_permission` VALUES ('4e2dc0d9aabbaab34120febdd23241b8', 'f6817f48af4fb3af11b9e8bf182f618b', 'e08cb190ef230d5d4f03824198773950');
INSERT INTO `sys_role_permission` VALUES ('50a5f72e811970d107c50ff43587bdfb', 'e51758fa916c881624b046d26bd09230', '3f915b2769fc80648e92d04e84ca059d');
INSERT INTO `sys_role_permission` VALUES ('57c0b3a547b815ea3ec8e509b08948b3', '1750a8fb3e6d90cb7957c02de1dc8e59', '3f915b2769fc80648e92d04e84ca059d');
INSERT INTO `sys_role_permission` VALUES ('58105249b395c46b45ab52050c54716b', 'ee8626f80f7c2619917b6236f3a7f02b', '13212d3416eb690c2e1d5033166ff47a');
INSERT INTO `sys_role_permission` VALUES ('58f1d42a8477733f9a949ceca0599780', 'f6817f48af4fb3af11b9e8bf182f618b', '078f9558cdeab239aecb2bda1a8ed0d1');
INSERT INTO `sys_role_permission` VALUES ('593ee05c4fe4645c7826b7d5e14f23ec', '52b0cf022ac4187b2a70dfa4f8b2d940', '8fb8172747a78756c11916216b8b8066');
INSERT INTO `sys_role_permission` VALUES ('59c24cd9220fba3e6a7e5c0bc2c2bcb5', 'ee8626f80f7c2619917b6236f3a7f02b', '73678f9daa45ed17a3674131b03432fb');
INSERT INTO `sys_role_permission` VALUES ('5a5ba1a8c862f161981ab8ca75bd8f54', 'f6817f48af4fb3af11b9e8bf182f618b', '8e8517f135e55f1ef9340a558c9367f1');
INSERT INTO `sys_role_permission` VALUES ('5b7e0aa65d547bd9586a068525a82efa', 'f6817f48af4fb3af11b9e8bf182f618b', 'e3c13679c73a4f829bcff2aba8fd68b1');
INSERT INTO `sys_role_permission` VALUES ('61ab8ca7bc9dcd63ee49e4298d5d3520', 'f6817f48af4fb3af11b9e8bf182f618b', '6531cf3421b1265aeeeabaab5e176e6d');
INSERT INTO `sys_role_permission` VALUES ('633d508c336b3e20fbbe7b2e1db55631', 'f6817f48af4fb3af11b9e8bf182f618b', '200006f0edf145a2b50eacca07585451');
INSERT INTO `sys_role_permission` VALUES ('66180a9104f7593a74c7cfd8da673db6', 'ee8626f80f7c2619917b6236f3a7f02b', 'c65321e57b7949b7a975313220de0422');
INSERT INTO `sys_role_permission` VALUES ('6a4ecfd721814517205ef5be062dec0b', 'f6817f48af4fb3af11b9e8bf182f618b', 'e8af452d8948ea49d37c934f5100ae6a');
INSERT INTO `sys_role_permission` VALUES ('6ba390047557a46a0c9fe33031a12fde', 'f6817f48af4fb3af11b9e8bf182f618b', 'e6bfd1fcabfd7942fdd05f076d1dad38');
INSERT INTO `sys_role_permission` VALUES ('6bb48ab31621f9dadfc0bff52543516a', 'f6817f48af4fb3af11b9e8bf182f618b', 'c6cf95444d80435eb37b2f9db3971ae6');
INSERT INTO `sys_role_permission` VALUES ('6bbc5e38cd1f62072ee89fddacd93b38', 'ee8626f80f7c2619917b6236f3a7f02b', 'b3c824fc22bd953e2eb16ae6914ac8f9');
INSERT INTO `sys_role_permission` VALUES ('6c09b922b32679365627586284d7809e', 'f6817f48af4fb3af11b9e8bf182f618b', '9502685863ab87f0ad1134142788a385');
INSERT INTO `sys_role_permission` VALUES ('6ece067b09cc2ec8536fde2a4b5abd19', 'ee8626f80f7c2619917b6236f3a7f02b', '8fb8172747a78756c11916216b8b8066');
INSERT INTO `sys_role_permission` VALUES ('6f0bf59157b633ffe1ae2f2f22f65418', 'f6817f48af4fb3af11b9e8bf182f618b', 'a400e4f4d54f79bf5ce160a3432231af');
INSERT INTO `sys_role_permission` VALUES ('6f8a3f1205caf521a5a7cbb9a60763f4', 'f6817f48af4fb3af11b9e8bf182f618b', 'de13e0f6328c069748de7399fcc1dbbd');
INSERT INTO `sys_role_permission` VALUES ('701856cb1422173e171ed851c3cab1ee', 'ee8626f80f7c2619917b6236f3a7f02b', '05b3c82ddb2536a4a5ee1a4c46b5abef');
INSERT INTO `sys_role_permission` VALUES ('717ea664474132c93cae847e1ffc5807', 'ee8626f80f7c2619917b6236f3a7f02b', 'a400e4f4d54f79bf5ce160ae432231af');
INSERT INTO `sys_role_permission` VALUES ('74e999bf3b7f4f67ed51048f281b81bc', 'f6817f48af4fb3af11b9e8bf182f618b', '8fb8172747a78756c11916216b8b8066');
INSERT INTO `sys_role_permission` VALUES ('75002588591820806', '16457350655250432', '5129710648430592');
INSERT INTO `sys_role_permission` VALUES ('75002588604403712', '16457350655250432', '5129710648430593');
INSERT INTO `sys_role_permission` VALUES ('75002588612792320', '16457350655250432', '40238597734928384');
INSERT INTO `sys_role_permission` VALUES ('75002588625375232', '16457350655250432', '57009744761589760');
INSERT INTO `sys_role_permission` VALUES ('75002588633763840', '16457350655250432', '16392452747300864');
INSERT INTO `sys_role_permission` VALUES ('75002588637958144', '16457350655250432', '16392767785668608');
INSERT INTO `sys_role_permission` VALUES ('75002588650541056', '16457350655250432', '16439068543946752');
INSERT INTO `sys_role_permission` VALUES ('75a2852363236af7c35cb4b0ca583a78', 'f6817f48af4fb3af11b9e8bf182f618b', 'aedbf679b5773c1f25e9f7b10111da73');
INSERT INTO `sys_role_permission` VALUES ('77277779875336192', '496138616573952', '5129710648430592');
INSERT INTO `sys_role_permission` VALUES ('77277780043108352', '496138616573952', '5129710648430593');
INSERT INTO `sys_role_permission` VALUES ('77277780055691264', '496138616573952', '15701400130424832');
INSERT INTO `sys_role_permission` VALUES ('77277780064079872', '496138616573952', '16678126574637056');
INSERT INTO `sys_role_permission` VALUES ('77277780072468480', '496138616573952', '15701915807518720');
INSERT INTO `sys_role_permission` VALUES ('77277780076662784', '496138616573952', '15708892205944832');
INSERT INTO `sys_role_permission` VALUES ('77277780085051392', '496138616573952', '16678447719911424');
INSERT INTO `sys_role_permission` VALUES ('77277780089245696', '496138616573952', '25014528525733888');
INSERT INTO `sys_role_permission` VALUES ('77277780097634304', '496138616573952', '56898976661639168');
INSERT INTO `sys_role_permission` VALUES ('77277780135383040', '496138616573952', '40238597734928384');
INSERT INTO `sys_role_permission` VALUES ('77277780139577344', '496138616573952', '45235621697949696');
INSERT INTO `sys_role_permission` VALUES ('77277780147965952', '496138616573952', '45235787867885568');
INSERT INTO `sys_role_permission` VALUES ('77277780156354560', '496138616573952', '45235939278065664');
INSERT INTO `sys_role_permission` VALUES ('77277780164743168', '496138616573952', '43117268627886080');
INSERT INTO `sys_role_permission` VALUES ('77277780168937472', '496138616573952', '45236734832676864');
INSERT INTO `sys_role_permission` VALUES ('77277780181520384', '496138616573952', '45237010692050944');
INSERT INTO `sys_role_permission` VALUES ('77277780189908992', '496138616573952', '45237170029465600');
INSERT INTO `sys_role_permission` VALUES ('77277780198297600', '496138616573952', '57009544286441472');
INSERT INTO `sys_role_permission` VALUES ('77277780206686208', '496138616573952', '57009744761589760');
INSERT INTO `sys_role_permission` VALUES ('77277780215074816', '496138616573952', '57009981228060672');
INSERT INTO `sys_role_permission` VALUES ('77277780219269120', '496138616573952', '56309618086776832');
INSERT INTO `sys_role_permission` VALUES ('77277780227657728', '496138616573952', '57212882168844288');
INSERT INTO `sys_role_permission` VALUES ('77277780236046336', '496138616573952', '61560041605435392');
INSERT INTO `sys_role_permission` VALUES ('77277780244434944', '496138616573952', '61560275261722624');
INSERT INTO `sys_role_permission` VALUES ('77277780257017856', '496138616573952', '61560480518377472');
INSERT INTO `sys_role_permission` VALUES ('77277780265406464', '496138616573952', '44986029924421632');
INSERT INTO `sys_role_permission` VALUES ('77277780324126720', '496138616573952', '45235228800716800');
INSERT INTO `sys_role_permission` VALUES ('77277780332515328', '496138616573952', '45069342940860416');
INSERT INTO `sys_role_permission` VALUES ('77277780340903937', '496138616573952', '5129710648430594');
INSERT INTO `sys_role_permission` VALUES ('77277780349292544', '496138616573952', '16687383932047360');
INSERT INTO `sys_role_permission` VALUES ('77277780357681152', '496138616573952', '16689632049631232');
INSERT INTO `sys_role_permission` VALUES ('77277780366069760', '496138616573952', '16689745006432256');
INSERT INTO `sys_role_permission` VALUES ('77277780370264064', '496138616573952', '16689883993083904');
INSERT INTO `sys_role_permission` VALUES ('77277780374458369', '496138616573952', '16690313745666048');
INSERT INTO `sys_role_permission` VALUES ('77277780387041280', '496138616573952', '5129710648430595');
INSERT INTO `sys_role_permission` VALUES ('77277780395429888', '496138616573952', '16694861252005888');
INSERT INTO `sys_role_permission` VALUES ('77277780403818496', '496138616573952', '16695107491205120');
INSERT INTO `sys_role_permission` VALUES ('77277780412207104', '496138616573952', '16695243126607872');
INSERT INTO `sys_role_permission` VALUES ('77277780420595712', '496138616573952', '75002207560273920');
INSERT INTO `sys_role_permission` VALUES ('77277780428984320', '496138616573952', '76215889006956544');
INSERT INTO `sys_role_permission` VALUES ('77277780433178624', '496138616573952', '76216071333351424');
INSERT INTO `sys_role_permission` VALUES ('77277780441567232', '496138616573952', '76216264070008832');
INSERT INTO `sys_role_permission` VALUES ('77277780449955840', '496138616573952', '76216459709124608');
INSERT INTO `sys_role_permission` VALUES ('77277780458344448', '496138616573952', '76216594207870976');
INSERT INTO `sys_role_permission` VALUES ('77277780466733056', '496138616573952', '76216702639017984');
INSERT INTO `sys_role_permission` VALUES ('77277780475121664', '496138616573952', '58480609315524608');
INSERT INTO `sys_role_permission` VALUES ('77277780483510272', '496138616573952', '61394706252173312');
INSERT INTO `sys_role_permission` VALUES ('77277780491898880', '496138616573952', '61417744146370560');
INSERT INTO `sys_role_permission` VALUES ('77277780496093184', '496138616573952', '76606430504816640');
INSERT INTO `sys_role_permission` VALUES ('77277780504481792', '496138616573952', '76914082455752704');
INSERT INTO `sys_role_permission` VALUES ('77277780508676097', '496138616573952', '76607201262702592');
INSERT INTO `sys_role_permission` VALUES ('77277780517064704', '496138616573952', '39915540965232640');
INSERT INTO `sys_role_permission` VALUES ('77277780525453312', '496138616573952', '41370251991977984');
INSERT INTO `sys_role_permission` VALUES ('77277780538036224', '496138616573952', '45264987354042368');
INSERT INTO `sys_role_permission` VALUES ('77277780546424832', '496138616573952', '45265487029866496');
INSERT INTO `sys_role_permission` VALUES ('77277780554813440', '496138616573952', '45265762415284224');
INSERT INTO `sys_role_permission` VALUES ('77277780559007744', '496138616573952', '45265886315024384');
INSERT INTO `sys_role_permission` VALUES ('77277780567396352', '496138616573952', '45266070000373760');
INSERT INTO `sys_role_permission` VALUES ('77277780571590656', '496138616573952', '41363147411427328');
INSERT INTO `sys_role_permission` VALUES ('77277780579979264', '496138616573952', '41363537456533504');
INSERT INTO `sys_role_permission` VALUES ('77277780588367872', '496138616573952', '41364927394353152');
INSERT INTO `sys_role_permission` VALUES ('77277780596756480', '496138616573952', '41371711400054784');
INSERT INTO `sys_role_permission` VALUES ('77277780605145088', '496138616573952', '41469219249852416');
INSERT INTO `sys_role_permission` VALUES ('77277780613533696', '496138616573952', '39916171171991552');
INSERT INTO `sys_role_permission` VALUES ('77277780621922304', '496138616573952', '39918482854252544');
INSERT INTO `sys_role_permission` VALUES ('77277780630310912', '496138616573952', '41373430515240960');
INSERT INTO `sys_role_permission` VALUES ('77277780718391296', '496138616573952', '41375330996326400');
INSERT INTO `sys_role_permission` VALUES ('77277780722585600', '496138616573952', '63741744973352960');
INSERT INTO `sys_role_permission` VALUES ('77277780730974208', '496138616573952', '42082442672082944');
INSERT INTO `sys_role_permission` VALUES ('77277780739362816', '496138616573952', '41376192166629376');
INSERT INTO `sys_role_permission` VALUES ('77277780747751424', '496138616573952', '41377034236071936');
INSERT INTO `sys_role_permission` VALUES ('77277780756140032', '496138616573952', '56911328312299520');
INSERT INTO `sys_role_permission` VALUES ('77277780764528640', '496138616573952', '41378916912336896');
INSERT INTO `sys_role_permission` VALUES ('77277780768722944', '496138616573952', '63482475359244288');
INSERT INTO `sys_role_permission` VALUES ('77277780772917249', '496138616573952', '64290663792906240');
INSERT INTO `sys_role_permission` VALUES ('77277780785500160', '496138616573952', '66790433014943744');
INSERT INTO `sys_role_permission` VALUES ('77277780789694464', '496138616573952', '42087054753927168');
INSERT INTO `sys_role_permission` VALUES ('77277780798083072', '496138616573952', '67027338952445952');
INSERT INTO `sys_role_permission` VALUES ('77277780806471680', '496138616573952', '67027909637836800');
INSERT INTO `sys_role_permission` VALUES ('77277780810665985', '496138616573952', '67042515441684480');
INSERT INTO `sys_role_permission` VALUES ('77277780823248896', '496138616573952', '67082402312228864');
INSERT INTO `sys_role_permission` VALUES ('77277780827443200', '496138616573952', '16392452747300864');
INSERT INTO `sys_role_permission` VALUES ('77277780835831808', '496138616573952', '16392767785668608');
INSERT INTO `sys_role_permission` VALUES ('77277780840026112', '496138616573952', '16438800255291392');
INSERT INTO `sys_role_permission` VALUES ('77277780844220417', '496138616573952', '16438962738434048');
INSERT INTO `sys_role_permission` VALUES ('77277780852609024', '496138616573952', '16439068543946752');
INSERT INTO `sys_role_permission` VALUES ('77277860062040064', '496138616573953', '5129710648430592');
INSERT INTO `sys_role_permission` VALUES ('77277860070428672', '496138616573953', '5129710648430593');
INSERT INTO `sys_role_permission` VALUES ('77277860078817280', '496138616573953', '40238597734928384');
INSERT INTO `sys_role_permission` VALUES ('77277860091400192', '496138616573953', '43117268627886080');
INSERT INTO `sys_role_permission` VALUES ('77277860099788800', '496138616573953', '57009744761589760');
INSERT INTO `sys_role_permission` VALUES ('77277860112371712', '496138616573953', '56309618086776832');
INSERT INTO `sys_role_permission` VALUES ('77277860120760320', '496138616573953', '44986029924421632');
INSERT INTO `sys_role_permission` VALUES ('77277860129148928', '496138616573953', '5129710648430594');
INSERT INTO `sys_role_permission` VALUES ('77277860141731840', '496138616573953', '5129710648430595');
INSERT INTO `sys_role_permission` VALUES ('77277860150120448', '496138616573953', '75002207560273920');
INSERT INTO `sys_role_permission` VALUES ('77277860158509056', '496138616573953', '58480609315524608');
INSERT INTO `sys_role_permission` VALUES ('77277860162703360', '496138616573953', '76606430504816640');
INSERT INTO `sys_role_permission` VALUES ('77277860171091968', '496138616573953', '76914082455752704');
INSERT INTO `sys_role_permission` VALUES ('77277860179480576', '496138616573953', '76607201262702592');
INSERT INTO `sys_role_permission` VALUES ('77277860187869184', '496138616573953', '39915540965232640');
INSERT INTO `sys_role_permission` VALUES ('77277860196257792', '496138616573953', '41370251991977984');
INSERT INTO `sys_role_permission` VALUES ('77277860204646400', '496138616573953', '41363147411427328');
INSERT INTO `sys_role_permission` VALUES ('77277860208840704', '496138616573953', '41371711400054784');
INSERT INTO `sys_role_permission` VALUES ('77277860213035009', '496138616573953', '39916171171991552');
INSERT INTO `sys_role_permission` VALUES ('77277860221423616', '496138616573953', '39918482854252544');
INSERT INTO `sys_role_permission` VALUES ('77277860225617920', '496138616573953', '41373430515240960');
INSERT INTO `sys_role_permission` VALUES ('77277860234006528', '496138616573953', '41375330996326400');
INSERT INTO `sys_role_permission` VALUES ('77277860242395136', '496138616573953', '63741744973352960');
INSERT INTO `sys_role_permission` VALUES ('77277860250783744', '496138616573953', '42082442672082944');
INSERT INTO `sys_role_permission` VALUES ('77277860254978048', '496138616573953', '41376192166629376');
INSERT INTO `sys_role_permission` VALUES ('77277860263366656', '496138616573953', '41377034236071936');
INSERT INTO `sys_role_permission` VALUES ('77277860271755264', '496138616573953', '56911328312299520');
INSERT INTO `sys_role_permission` VALUES ('77277860313698304', '496138616573953', '41378916912336896');
INSERT INTO `sys_role_permission` VALUES ('77277860322086912', '496138616573953', '63482475359244288');
INSERT INTO `sys_role_permission` VALUES ('77277860326281216', '496138616573953', '64290663792906240');
INSERT INTO `sys_role_permission` VALUES ('77277860334669824', '496138616573953', '66790433014943744');
INSERT INTO `sys_role_permission` VALUES ('77277860343058432', '496138616573953', '42087054753927168');
INSERT INTO `sys_role_permission` VALUES ('77277860347252736', '496138616573953', '67027338952445952');
INSERT INTO `sys_role_permission` VALUES ('77277860351447041', '496138616573953', '67027909637836800');
INSERT INTO `sys_role_permission` VALUES ('77277860359835648', '496138616573953', '67042515441684480');
INSERT INTO `sys_role_permission` VALUES ('77277860364029952', '496138616573953', '67082402312228864');
INSERT INTO `sys_role_permission` VALUES ('77277860368224256', '496138616573953', '16392452747300864');
INSERT INTO `sys_role_permission` VALUES ('77277860372418560', '496138616573953', '16392767785668608');
INSERT INTO `sys_role_permission` VALUES ('77277860376612865', '496138616573953', '16438800255291392');
INSERT INTO `sys_role_permission` VALUES ('77277860385001472', '496138616573953', '16438962738434048');
INSERT INTO `sys_role_permission` VALUES ('77277860389195776', '496138616573953', '16439068543946752');
INSERT INTO `sys_role_permission` VALUES ('7a5d31ba48fe3fb1266bf186dc5f7ba7', '52b0cf022ac4187b2a70dfa4f8b2d940', '58857ff846e61794c69208e9d3a85466');
INSERT INTO `sys_role_permission` VALUES ('7da0a684903280c9582fe3f0063a6b4a', 'f6817f48af4fb3af11b9e8bf182f618b', '08e6b9dc3c04489c8e1ff2ce6f105aa4');
INSERT INTO `sys_role_permission` VALUES ('7de42bdc0b8c5446b7d428c66a7abc12', '52b0cf022ac4187b2a70dfa4f8b2d940', '54dd5457a3190740005c1bfec55b1c34');
INSERT INTO `sys_role_permission` VALUES ('805818784d80e30d60d0bc19c85d6bfd', 'ee8626f80f7c2619917b6236f3a7f02b', 'd7d6e2e4e2934f2c9385a623fd98c6f3');
INSERT INTO `sys_role_permission` VALUES ('8359760581bf3f3a8b17d2d3d813f988', 'e51758fa916c881624b046d26bd09230', 'fb367426764077dcf94640c843733985');
INSERT INTO `sys_role_permission` VALUES ('848d2e5b9a81f46199bbf2026581718f', 'ee8626f80f7c2619917b6236f3a7f02b', '418964ba087b90a84897b62474496b93');
INSERT INTO `sys_role_permission` VALUES ('848d34a47accfce4892be408de900181', 'f6817f48af4fb3af11b9e8bf182f618b', '6e73eb3c26099c191bf03852ee1310a1');
INSERT INTO `sys_role_permission` VALUES ('8501e54beca4d97e9b637fbc8088c9dc', 'f6817f48af4fb3af11b9e8bf182f618b', '540a2936940846cb98114ffb0d145cb8');
INSERT INTO `sys_role_permission` VALUES ('85035d45e89059e7eeec1c5ed4a503ad', 'f6817f48af4fb3af11b9e8bf182f618b', 'ec8d607d0156e198b11853760319c646');
INSERT INTO `sys_role_permission` VALUES ('8677678f8d2f38b2511890cdac0eae0b', 'f6817f48af4fb3af11b9e8bf182f618b', '2a470fc0c3954d9dbb61de6d80846549');
INSERT INTO `sys_role_permission` VALUES ('86a6b794496c82586dd2f7e160f0084b', 'f6817f48af4fb3af11b9e8bf182f618b', '0b6debda2d5a214aa81540971575a6b2');
INSERT INTO `sys_role_permission` VALUES ('86d964c21192cb8aecfb6571a6478041', 'e51758fa916c881624b046d26bd09230', 'd7d6e2e4e2934f2c9385a623fd98c6f3');
INSERT INTO `sys_role_permission` VALUES ('87a130cf99c9bb8d8504e2b4850e8f05', 'e51758fa916c881624b046d26bd09230', 'e8af452d8948ea49d37c934f5100ae6a');
INSERT INTO `sys_role_permission` VALUES ('87d1fe4a702017386ba10b4ee8690f32', 'ee8626f80f7c2619917b6236f3a7f02b', 'fb367426764077dcf94640c843733985');
INSERT INTO `sys_role_permission` VALUES ('88824681e84cbb11ee123460a9dd2be6', 'e51758fa916c881624b046d26bd09230', '58857ff846e61794c69208e9d3a85466');
INSERT INTO `sys_role_permission` VALUES ('8885e0c7239d877a108e5e14913d02c9', 'f6817f48af4fb3af11b9e8bf182f618b', '277bfabef7d76e89b33062b16a9a5020');
INSERT INTO `sys_role_permission` VALUES ('89309192c469ebe55cac2189dde81375', 'e51758fa916c881624b046d26bd09230', 'e08cb190ef230d5d4f03824198773950');
INSERT INTO `sys_role_permission` VALUES ('8a60df8d8b4c9ee5fa63f48aeee3ec00', '1750a8fb3e6d90cb7957c02de1dc8e59', 'd7d6e2e4e2934f2c9385a623fd98c6f3');
INSERT INTO `sys_role_permission` VALUES ('8a63af8ef2a4a1a641eb80d3e66140dd', 'f6817f48af4fb3af11b9e8bf182f618b', '9271af487e0184387c326383d655ca37');
INSERT INTO `sys_role_permission` VALUES ('8b6309dac37f642e41073ecf9d5f8a4f', 'ee8626f80f7c2619917b6236f3a7f02b', 'b4dfc7d5dd9e8d5b6dd6d4579b1aa559');
INSERT INTO `sys_role_permission` VALUES ('8ce1022dac4e558ff9694600515cf510', '1750a8fb3e6d90cb7957c02de1dc8e59', '08e6b9dc3c04489c8e1ff2ce6f105aa4');
INSERT INTO `sys_role_permission` VALUES ('8d848ca7feec5b7ebb3ecb32b2c8857a', '52b0cf022ac4187b2a70dfa4f8b2d940', '4148ec82b6acd69f470bea75fe41c357');
INSERT INTO `sys_role_permission` VALUES ('9264104cee9b10c96241d527b2d0346d', '1750a8fb3e6d90cb7957c02de1dc8e59', '54dd5457a3190740005c1bfec55b1c34');
INSERT INTO `sys_role_permission` VALUES ('97bb13727b2faa5f7ab0f006a600ecb7', 'ee8626f80f7c2619917b6236f3a7f02b', '2a470fc0c3954d9dbb61de6d80846549');
INSERT INTO `sys_role_permission` VALUES ('98943175f29f12ae88c6bc5991559058', 'f6817f48af4fb3af11b9e8bf182f618b', 'e5973686ed495c379d829ea8b2881fc6');
INSERT INTO `sys_role_permission` VALUES ('99fd4a7aa629ab77c0265285f779cfc8', 'f6817f48af4fb3af11b9e8bf182f618b', '4148ec82b6acd69f470bea75fe41c357');
INSERT INTO `sys_role_permission` VALUES ('9a7a8020c3c6544773239c21daebfbc8', 'ee8626f80f7c2619917b6236f3a7f02b', '540a2936940846cb98114ffb0d145cb8');
INSERT INTO `sys_role_permission` VALUES ('9b3be7550e49dae4264d055b9578f937', 'e51758fa916c881624b046d26bd09230', '08e6b9dc3c04489c8e1ff2ce6f105aa4');
INSERT INTO `sys_role_permission` VALUES ('9c4c310005bf9b3aa10d0855b851dd4a', 'f6817f48af4fb3af11b9e8bf182f618b', '45c966826eeff4c99b8f8ebfe74511fc');
INSERT INTO `sys_role_permission` VALUES ('9deb6902c4dd2068cc36068d8f92b129', 'ee8626f80f7c2619917b6236f3a7f02b', 'ec8d607d0156e198b11853760319c646');
INSERT INTO `sys_role_permission` VALUES ('9ea5c9a17bd0fe2998f8f5bf3ff03340', 'ee8626f80f7c2619917b6236f3a7f02b', '65a8f489f25a345836b7f44b1181197a');
INSERT INTO `sys_role_permission` VALUES ('9eb1c9695038cb5821a373d0faf4893a', 'f6817f48af4fb3af11b9e8bf182f618b', '339329ed54cf255e1f9392e84f136901');
INSERT INTO `sys_role_permission` VALUES ('9f3734bd5df9a10e94a61991c8e6385b', 'f6817f48af4fb3af11b9e8bf182f618b', 'b1cb0a3fedf7ed0e4653cb5a229837ee');
INSERT INTO `sys_role_permission` VALUES ('9f8311ecccd44e079723098cf2ffe1cc', '1750a8fb3e6d90cb7957c02de1dc8e59', '693ce69af3432bd00be13c3971a57961');
INSERT INTO `sys_role_permission` VALUES ('a3ad5828891623739f972adec7b72925', 'e51758fa916c881624b046d26bd09230', 'c65321e57b7949b7a975313220de0422');
INSERT INTO `sys_role_permission` VALUES ('a4703e53d8a1ef028f5c1e989aa181ab', 'ee8626f80f7c2619917b6236f3a7f02b', '693ce69af3432bd00be13c3971a57961');
INSERT INTO `sys_role_permission` VALUES ('abc615092450682973b97fdf1517d70a', 'ee8626f80f7c2619917b6236f3a7f02b', '078f9558cdeab239aecb2bda1a8ed0d1');
INSERT INTO `sys_role_permission` VALUES ('ac3a3e4f965e26639ba2157f9c87510f', 'e51758fa916c881624b046d26bd09230', 'f1cb187abf927c88b89470d08615f5ac');
INSERT INTO `sys_role_permission` VALUES ('ac68f782147d10e832361955799f797e', 'f6817f48af4fb3af11b9e8bf182f618b', 'ae4fed059f67086fd52a73d913cf473d');
INSERT INTO `sys_role_permission` VALUES ('acf83ea75957c4479b2454184d43db25', 'f6817f48af4fb3af11b9e8bf182f618b', '54dd5457a3190740005c1bfec55b1c34');
INSERT INTO `sys_role_permission` VALUES ('ad2aff227a7e5c77a28978d5ec0aacd1', 'f6817f48af4fb3af11b9e8bf182f618b', 'c65321e57b7949b7a975313220de0422');
INSERT INTO `sys_role_permission` VALUES ('ada57af3189bbf6ca6403e399b57ddc8', 'f6817f48af4fb3af11b9e8bf182f618b', '5efbf11a764db7759c36c669a263f292');
INSERT INTO `sys_role_permission` VALUES ('aefc8c22e061171806e59cd222f6b7e1', '52b0cf022ac4187b2a70dfa4f8b2d940', 'e8af452d8948ea49d37c934f5100ae6a');
INSERT INTO `sys_role_permission` VALUES ('af984985ac4d9c506dd14b2852db8b46', 'e51758fa916c881624b046d26bd09230', '2a470fc0c3954d9dbb61de6d80846549');
INSERT INTO `sys_role_permission` VALUES ('b10400742083ee1f54965e9a8bd66cf7', 'ee8626f80f7c2619917b6236f3a7f02b', '08e6b9dc3c04489c8e1ff2ce6f105aa4');
INSERT INTO `sys_role_permission` VALUES ('b173f181712c97d9f4090bbe1c842b7d', 'f6817f48af4fb3af11b9e8bf182f618b', '7ac9eb9ccbde2f7a033cd4944272bf1e');
INSERT INTO `sys_role_permission` VALUES ('b3bca65182c8e8e8962be046f92bb114', 'f6817f48af4fb3af11b9e8bf182f618b', 'b4dfc7d5dd9e8d5b6dd6d4579b1aa559');
INSERT INTO `sys_role_permission` VALUES ('b44e8fcab5b56e31a518c03a7365e740', 'f6817f48af4fb3af11b9e8bf182f618b', 'd86f58e7ab516d3bc6bfb1fe10585f97');
INSERT INTO `sys_role_permission` VALUES ('b5ac258a406d007eeda459bac92d2bdb', 'e51758fa916c881624b046d26bd09230', '8fb8172747a78756c11916216b8b8066');
INSERT INTO `sys_role_permission` VALUES ('b5d8777b207bc8542c582d8c0181b026', 'f6817f48af4fb3af11b9e8bf182f618b', '6ad53fd1b220989a8b71ff482d683a5a');
INSERT INTO `sys_role_permission` VALUES ('b6fd9f4c680916c50576089226c868bb', 'ee8626f80f7c2619917b6236f3a7f02b', 'e3c13679c73a4f829bcff2aba8fd68b1');
INSERT INTO `sys_role_permission` VALUES ('b929baebd4f991007f3e9b39c570cacf', 'f6817f48af4fb3af11b9e8bf182f618b', '3eb07a99bd7dcea6d9bef40e37735c7f');
INSERT INTO `sys_role_permission` VALUES ('bc933866f29c688d12ebcbc6ba380609', 'ee8626f80f7c2619917b6236f3a7f02b', 'e6bfd1fcabfd7942fdd05f076d1dad38');
INSERT INTO `sys_role_permission` VALUES ('bcac0166161d3d02fa0797ef6989aa43', 'f6817f48af4fb3af11b9e8bf182f618b', 'fb367426764077dcf94640c843733985');
INSERT INTO `sys_role_permission` VALUES ('bdc0b7f20bf0d4b7a0a23783cd430ca2', 'ee8626f80f7c2619917b6236f3a7f02b', 'de13e0f6328c069748de7399fcc1dbbd');
INSERT INTO `sys_role_permission` VALUES ('c1899d05c0e53a6698c0a2671db4c8e2', 'ee8626f80f7c2619917b6236f3a7f02b', '54dd5457a3190740005c1bfec55b1c34');
INSERT INTO `sys_role_permission` VALUES ('c41c0dbd1e7568b4334f4cee6f1f53b7', 'e51758fa916c881624b046d26bd09230', 'd2bbf9ebca5a8fa2e227af97d2da7548');
INSERT INTO `sys_role_permission` VALUES ('c42611ea289f02e5e1d85aa84df6a215', 'f6817f48af4fb3af11b9e8bf182f618b', 'f1cb187abf927c88b89470d08615f5ac');
INSERT INTO `sys_role_permission` VALUES ('c8a4552dd8f3bb64d8bfd1930631d592', 'f6817f48af4fb3af11b9e8bf182f618b', '2dbbafa22cda07fa5d169d741b81fe12');
INSERT INTO `sys_role_permission` VALUES ('ca4dd892d770632a2bbf3b4174aef0fd', 'f6817f48af4fb3af11b9e8bf182f618b', 'fedfbf4420536cacc0218557d263dfea');
INSERT INTO `sys_role_permission` VALUES ('cd2f88e859c11a292e8b5c8da8005b2f', 'ee8626f80f7c2619917b6236f3a7f02b', '6531cf3421b1265aeeeabaab5e176e6d');
INSERT INTO `sys_role_permission` VALUES ('cd60279d117b4b0435c8c78a4bcadb83', 'e51758fa916c881624b046d26bd09230', 'b1cb0a3fedf7ed0e4653cb5a229837ee');
INSERT INTO `sys_role_permission` VALUES ('d3fe195d59811531c05d31d8436f5c8b', '1750a8fb3e6d90cb7957c02de1dc8e59', 'e8af452d8948ea49d37c934f5100ae6a');
INSERT INTO `sys_role_permission` VALUES ('d553ad49e741d6299acb3b295bcb209f', 'f6817f48af4fb3af11b9e8bf182f618b', '89d70d3c72ad5cc778f511f8ac9f3242');
INSERT INTO `sys_role_permission` VALUES ('d633bcc436fc482a958deeb2a9e72438', 'f6817f48af4fb3af11b9e8bf182f618b', 'fb07ca05a3e13674dbf6d3245956da2e');
INSERT INTO `sys_role_permission` VALUES ('d6d39c7f15c04e48d1f5ad4919c48bee', 'f6817f48af4fb3af11b9e8bf182f618b', '65a8f489f25a345836b7f44b1181197a');
INSERT INTO `sys_role_permission` VALUES ('daa3ba8acc7d5ec69d022db6ad962131', 'f6817f48af4fb3af11b9e8bf182f618b', 'cc50656cf9ca528e6f2150eba4714ad2');
INSERT INTO `sys_role_permission` VALUES ('e046676385b007df88f95650ea885828', 'f6817f48af4fb3af11b9e8bf182f618b', 'ceab6bab4aea3f155de638e737c808ee');
INSERT INTO `sys_role_permission` VALUES ('e0ee13939a1707ff0ada050ee81a35f5', 'f6817f48af4fb3af11b9e8bf182f618b', '717f6bee46f44a3897eca9abd6e2ec44');
INSERT INTO `sys_role_permission` VALUES ('e270ad76a1db8b6cb3352d77eff600f3', 'ee8626f80f7c2619917b6236f3a7f02b', '1367a93f2c410b169faa7abcbad2f77c');
INSERT INTO `sys_role_permission` VALUES ('e27a125002fff5556ca7c8c58a41f495', 'ee8626f80f7c2619917b6236f3a7f02b', '00a2a0ae65cdca5e93209cdbde97cbe6');
INSERT INTO `sys_role_permission` VALUES ('e28bbe8b7bea9946016fe9f26fd47941', 'ee8626f80f7c2619917b6236f3a7f02b', 'b1cb0a3fedf7ed0e4653cb5a229837ee');
INSERT INTO `sys_role_permission` VALUES ('e3e922673f4289b18366bb51b6200f17', '52b0cf022ac4187b2a70dfa4f8b2d940', '45c966826eeff4c99b8f8ebfe74511fc');
INSERT INTO `sys_role_permission` VALUES ('e424befcb56fe26109eb5825783aeae8', 'ee8626f80f7c2619917b6236f3a7f02b', 'd86f58e7ab516d3bc6bfb1fe10585f97');
INSERT INTO `sys_role_permission` VALUES ('e6c4d984c59dbf02cbd53ff9b3592178', 'f6817f48af4fb3af11b9e8bf182f618b', '00a2a0ae65cdca5e93209cdbde97cbe6');
INSERT INTO `sys_role_permission` VALUES ('e853cee937560cb4b68904d382c513fb', 'ee8626f80f7c2619917b6236f3a7f02b', 'ae4fed059f67086fd52a73d913cf473d');
INSERT INTO `sys_role_permission` VALUES ('ec25a3f64638f1b58f42b6a3da708189', 'f6817f48af4fb3af11b9e8bf182f618b', '2e42e3835c2b44ec9f7bc26c146ee531');
INSERT INTO `sys_role_permission` VALUES ('efed119ff9eb2e1ec9b8386aaf69584f', 'f6817f48af4fb3af11b9e8bf182f618b', '1367a93f2c410b169faa7abcbad2f77c');
INSERT INTO `sys_role_permission` VALUES ('f041ba27d075dbe53cc4ad5132cfcd32', 'ee8626f80f7c2619917b6236f3a7f02b', '9502685863ab87f0ad1134142788a385');
INSERT INTO `sys_role_permission` VALUES ('f112a2eac584acf623916e676dbb8280', 'e51758fa916c881624b046d26bd09230', '54dd5457a3190740005c1bfec55b1c34');
INSERT INTO `sys_role_permission` VALUES ('f2742febef62f2c1f1fc05c7252451e3', 'ee8626f80f7c2619917b6236f3a7f02b', 'e8af452d8948ea49d37c934f5100ae6a');
INSERT INTO `sys_role_permission` VALUES ('f27cc0a40bcfa7752238e4b1000519a7', 'f6817f48af4fb3af11b9e8bf182f618b', '3f915b2769fc80648e92d04e84ca059d');
INSERT INTO `sys_role_permission` VALUES ('f7881a0cefda56b986578418ad940063', 'f6817f48af4fb3af11b9e8bf182f618b', '4f84f9400e5e92c95f05b554724c2b58');
INSERT INTO `sys_role_permission` VALUES ('fa3a980e2e135462216af6188c535909', 'e51758fa916c881624b046d26bd09230', 'aedbf679b5773c1f25e9f7b10111da73');
INSERT INTO `sys_role_permission` VALUES ('fe90569d40b7464335760bf17a6a8d32', 'e51758fa916c881624b046d26bd09230', '65a8f489f25a345836b7f44b1181197a');
INSERT INTO `sys_role_permission` VALUES ('fed41a4671285efb266cd404f24dd378', '52b0cf022ac4187b2a70dfa4f8b2d940', '00a2a0ae65cdca5e93209cdbde97cbe6');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(32) NOT NULL COMMENT '主键id',
  `username` varchar(100) default NULL COMMENT '登录账号',
  `realname` varchar(255) default NULL COMMENT '真实姓名',
  `password` varchar(255) default NULL COMMENT '密码',
  `salt` varchar(45) default NULL COMMENT 'md5密码盐',
  `avatar` varchar(255) default NULL COMMENT '头像',
  `birthday` datetime default NULL COMMENT '生日',
  `sex` int(11) default NULL COMMENT '性别（1：男 2：女）',
  `email` varchar(45) default NULL COMMENT '电子邮件',
  `phone` varchar(45) default NULL COMMENT '电话',
  `status` int(11) default NULL COMMENT '状态(1：正常  2：冻结 ）',
  `del_flag` varchar(255) default NULL COMMENT '删除状态（0，正常，1已删除）',
  `create_by` varchar(255) default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_by` varchar(255) default NULL COMMENT '更新人',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('a75d45a015c44384a04449ee80dc3503', 'jeecg', 'jeecg', '3dd8371f3cf8240e', 'vDDkDzrK', 'user/20190220/e1fe9925bc315c60addea1b98eb1cb1349547719_1550656892940.jpg', null, null, null, null, '1', '0', 'admin', '2019-02-13 16:02:36', 'admin', '2019-02-20 18:30:50');
INSERT INTO `sys_user` VALUES ('e9ca23d68d884d4ebb19d07889727dae', 'admin', '管理员', 'cb362cfeefbf3d8d', 'RCGTeGiH', 'user/20190119/logo-2_1547868176839.png', '2018-12-05 00:00:00', '1', '11@qq.com', '18566666666', '1', '0', null, '2018-12-21 17:54:10', 'admin', '2019-01-19 23:38:10');

-- ----------------------------
-- Table structure for sys_user_depart
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_depart`;
CREATE TABLE `sys_user_depart` (
  `ID` varchar(32) NOT NULL COMMENT 'id',
  `user_id` varchar(32) default NULL COMMENT '用户id',
  `dep_id` varchar(32) default NULL COMMENT '部门id',
  PRIMARY KEY  (`ID`),
  KEY `userid` (`user_id`),
  KEY `orgid` (`dep_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_depart
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` varchar(32) NOT NULL COMMENT '主键id',
  `user_id` varchar(32) default NULL COMMENT '用户id',
  `role_id` varchar(32) default NULL COMMENT '角色id',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('0eba8656e7466ecc9504ea23c2466492', 'a75d45a015c44384a04449ee80dc3503', 'ee8626f80f7c2619917b6236f3a7f02b');
INSERT INTO `sys_user_role` VALUES ('0ede6d23d53bc7dc990346ff14faabee', '3db4cf42353f4e868b7ccfeef90505d2', 'ee8626f80f7c2619917b6236f3a7f02b');
INSERT INTO `sys_user_role` VALUES ('469eb183e8fc8cac5ad1963e53fb59fa', 'e9ca23d68d884d4ebb19d07889727dae', 'f6817f48af4fb3af11b9e8bf182f618b');
INSERT INTO `sys_user_role` VALUES ('b3ffd9311a1ca296c44e2409b547384f', '01b802058ea94b978a2c96f4807f6b48', '1');
INSERT INTO `sys_user_role` VALUES ('ee45d0343ecec894b6886effc92cb0b7', '4d8fef4667574b24a9ccfedaf257810c', 'f6817f48af4fb3af11b9e8bf182f618b');
INSERT INTO `sys_user_role` VALUES ('f2922a38ba24fb53749e45a0c459adb3', '439ae3e9bcf7418583fcd429cadb1d72', '1');
INSERT INTO `sys_user_role` VALUES ('f72c6190b0722e798147e73c776c6ac9', '439ae3e9bcf7418583fcd429cadb1d72', 'ee8626f80f7c2619917b6236f3a7f02b');
