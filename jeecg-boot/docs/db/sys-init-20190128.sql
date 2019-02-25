/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50037
Source Host           : 127.0.0.1:3306
Source Database       : jeecg-boot-new

Target Server Type    : MYSQL
Target Server Version : 50037
File Encoding         : 65001

Date: 2019-01-28 13:58:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for demo
-- ----------------------------
DROP TABLE IF EXISTS `demo`;
CREATE TABLE `demo` (
  `id` varchar(32) NOT NULL COMMENT '主键ID',
  `name` varchar(30) default NULL COMMENT '姓名',
  `sex` varchar(2) default NULL,
  `age` int(11) default NULL COMMENT '年龄',
  `birthday` date default NULL COMMENT '生日',
  `email` varchar(50) default NULL COMMENT '邮箱',
  `content` varchar(1000) default NULL COMMENT '个人简介',
  `key_word` varchar(255) default NULL,
  `punch_time` datetime default NULL,
  `salary_money` decimal(11,2) default NULL,
  `bonus_money` double(11,0) default NULL,
  `create_time` datetime default NULL,
  `create_by` varchar(50) default NULL COMMENT '创建人',
  `update_by` varchar(50) default NULL COMMENT '更新人',
  `update_time` datetime default NULL COMMENT '更新日期',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of demo
-- ----------------------------

-- ----------------------------
-- Table structure for demo2
-- ----------------------------
DROP TABLE IF EXISTS `demo2`;
CREATE TABLE `demo2` (
  `id` int(30) NOT NULL auto_increment COMMENT '主键自增ID',
  `name` varchar(20) default NULL COMMENT '姓名',
  `gender` varchar(10) default NULL COMMENT '性别 1为男 0为女',
  `age` int(10) default NULL COMMENT '年龄',
  `address` varchar(200) default NULL COMMENT '住址',
  `job` varchar(40) default NULL COMMENT '职业',
  `isEmployed` varchar(10) default NULL COMMENT '1为在职,0为待业',
  `create_time` date default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of demo2
-- ----------------------------
INSERT INTO `demo2` VALUES ('2', 'Jack', '1', '25', '河北', '工程师', '0', null);
INSERT INTO `demo2` VALUES ('3', 'Tom', '1', '26', '广东', '大师', '1', null);
INSERT INTO `demo2` VALUES ('4', 'Sandy', '0', '27', '四川', '理发师', '0', null);
INSERT INTO `demo2` VALUES ('5', 'Billie', '1', '23', '杭州', '教师', '1', null);

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
INSERT INTO `qrtz_cron_triggers` VALUES ('quartzScheduler', 'org.jeecg.modules.quartz.job.SampleParamJob', 'DEFAULT', '0/1 * * * * ?', 'Asia/Shanghai');

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
INSERT INTO `qrtz_job_details` VALUES ('quartzScheduler', 'org.jeecg.modules.quartz.job.SampleParamJob', 'DEFAULT', null, 'org.jeecg.modules.quartz.job.SampleParamJob', '0', '0', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C77080000001000000001740009706172616D6574657274000573636F74747800);

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
INSERT INTO `qrtz_triggers` VALUES ('quartzScheduler', 'org.jeecg.modules.quartz.job.SampleJob', 'DEFAULT', 'org.jeecg.modules.quartz.job.SampleJob', 'DEFAULT', null, '1548655079000', '1548655078000', '5', 'PAUSED', 'CRON', '1548655070000', '0', null, '0', '');
INSERT INTO `qrtz_triggers` VALUES ('quartzScheduler', 'org.jeecg.modules.quartz.job.SampleParamJob', 'DEFAULT', 'org.jeecg.modules.quartz.job.SampleParamJob', 'DEFAULT', null, '1548655081000', '1548655080000', '5', 'PAUSED', 'CRON', '1548655047000', '0', null, '0', '');

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
  `annt_id` varchar(32) default NULL COMMENT '通过id',
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
INSERT INTO `sys_depart` VALUES ('4028608164d15ec60164d4cce4f4003e', '402880e447e99cf10147e9a03b320003', '11', null, null, null, '11', '2', 'A01A04', '', '', '', null, null, null, 'admin', '2018-07-26 12:15:55', null, null);
INSERT INTO `sys_depart` VALUES ('4028608164d15ec60164d4cd0cf10040', '402880e447e99cf10147e9a03b320003', '22', null, null, null, '22', '2', 'A01A05', '', '', '', null, null, null, 'admin', '2018-07-26 12:16:05', null, null);
INSERT INTO `sys_depart` VALUES ('4028608164d15ec60164d4cd99c20042', '402880e447e99cf10147e9a03b320003', '433', null, null, null, '', '2', 'A01A06', '', '', '', null, null, null, 'admin', '2018-07-26 12:16:41', null, null);
INSERT INTO `sys_depart` VALUES ('4028608164d15ec60164d4cded7f0044', '402880e447e99cf10147e9a03b320003', '33', null, null, null, '33', '2', 'A01A07', '33', '', '', null, null, null, 'admin', '2018-07-26 12:17:02', null, null);
INSERT INTO `sys_depart` VALUES ('4028608164d15ec60164d4ce46ca0046', '402880e447e99cf10147e9a03b320003', '55', null, null, null, '55', '2', 'A01A08', '', '', '', null, null, null, 'admin', '2018-07-26 12:17:25', null, null);
INSERT INTO `sys_depart` VALUES ('40286081650e0f9801650e92a4a0013f', '402880e447e99cf10147e9a03b320003', '11', null, null, null, '11', '2', 'A01A09', '11', '11', '11', null, null, null, 'admin', '2018-08-06 17:30:16', null, null);
INSERT INTO `sys_depart` VALUES ('40286081650e0f9801650e92d3f00141', '4028608164d15ec60164d4cce4f4003e', '11', null, null, null, '11', '2', 'A01A04A01', '11', '11', '', null, null, null, 'admin', '2018-08-06 17:30:28', null, null);
INSERT INTO `sys_depart` VALUES ('40286081651283c2016513046721007d', '402880e447e9a9570147e9b677320003', '技术经理', null, null, null, '技术经理', '3', 'A01A01A01', '', '', '', null, null, null, 'admin', '2018-08-07 14:13:00', null, null);
INSERT INTO `sys_depart` VALUES ('40286081651283c201651305b2fa007f', '40286081651283c2016513046721007d', 'T3_Java工程师', null, null, null, '高级工程师', '3', 'A01A01A01A01', '', '', '', null, null, null, 'admin', '2018-08-07 14:14:25', null, null);
INSERT INTO `sys_depart` VALUES ('402860816518408f0165184abe450001', '402880e447e99cf10147e9a03b320003', '1111', null, null, null, '', '2', 'A01A10', '', '', '', null, null, null, 'admin', '2018-08-08 14:47:56', null, null);
INSERT INTO `sys_depart` VALUES ('402860816518408f0165184f714b0003', '4028608164d15ec60164d4cce4f4003e', '44', null, null, null, '44', '2', 'A01A04A02', '', '', '', null, null, null, 'admin', '2018-08-08 14:53:04', null, null);
INSERT INTO `sys_depart` VALUES ('402860816518408f01651850ab660005', '4028608164d15ec60164d4cce4f4003e', '55', null, null, null, '55', '2', 'A01A04A03', '', '', '', null, null, null, 'admin', '2018-08-08 14:54:24', null, null);
INSERT INTO `sys_depart` VALUES ('402860816518408f01651853166f0007', '4028608164d15ec60164d4cce4f4003e', '66', null, null, null, '', '2', 'A01A04A04', '', '', '', null, null, null, 'admin', '2018-08-08 14:57:03', null, null);
INSERT INTO `sys_depart` VALUES ('402860816518408f016518546bfc0009', '4028608164d15ec60164d4cce4f4003e', '77', null, null, null, '', '2', 'A01A04A05', '', '', '', null, null, null, 'admin', '2018-08-08 14:58:30', null, null);
INSERT INTO `sys_depart` VALUES ('402860816518408f01651854f5ae000b', null, '12', null, null, null, '', '1', 'A04', '', '', '', null, null, null, null, null, 'admin', '2018-08-08 16:42:35');
INSERT INTO `sys_depart` VALUES ('402860816518408f0165185aa3430010', '402860816518408f01651854f5ae000b', '112', null, null, null, '', '2', 'A04A01', '', '', '', null, null, null, null, null, 'admin', '2018-08-08 15:06:18');
INSERT INTO `sys_depart` VALUES ('402860816518408f0165185bd01c0014', '402860816518408f0165185aa3430010', '11', null, null, null, '', '3', 'A04A01A01', '', '', '', null, null, null, null, null, 'admin', '2018-08-08 15:07:05');
INSERT INTO `sys_depart` VALUES ('402860816518408f016518b0a27b001a', '402880e447e99cf10147e9a03b320003', '33', null, null, null, '33', '2', 'A01A11', '', '', '', null, null, null, 'admin', '2018-08-08 16:39:13', null, null);
INSERT INTO `sys_depart` VALUES ('402860816518408f016518b3e2b5001d', '402880e447e99cf10147e9a03b320003', '11', null, null, null, '', '3', 'A01A12', '', '', '', null, null, null, 'admin', '2018-08-08 16:42:46', null, null);
INSERT INTO `sys_depart` VALUES ('4028608165417d1201654186d7200001', '402860816518408f0165185bd01c0014', 'dfdf', null, null, null, '', '3', 'A04A01A01A01', '', '', '', null, null, null, null, null, 'admin', '2018-08-16 14:58:08');
INSERT INTO `sys_depart` VALUES ('402880e447e99cf10147e9a03b320003', null, '北京国炬软件', null, null, null, '', '1', 'A01', '', '', '', null, null, null, null, null, 'admin', '2018-07-26 12:15:32');
INSERT INTO `sys_depart` VALUES ('402880e447e9a9570147e9b677320003', '402880e447e99cf10147e9a03b320003', '软件信息部', null, null, null, '', '2', 'A01A01', '', '', '', null, null, null, null, null, 'admin', '2018-07-26 12:15:28');
INSERT INTO `sys_depart` VALUES ('402880e447e9a9570147e9b6a3be0005', '402880e447e99cf10147e9a03b320003', '销售部门', null, null, '0', '', '2', 'A01A02', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_depart` VALUES ('402880e447e9a9570147e9b710d20007', '402880e447e99cf10147e9a03b320003', '人力资源部', null, null, null, '', '2', 'A01A03', '', '', '', null, null, null, null, null, 'admin', '2018-02-22 17:17:30');
INSERT INTO `sys_depart` VALUES ('402880e447e9a9570147e9b762e30009', '402880e447e9a9570147e9b6a3be0005', '销售经理', null, null, null, '', '2', 'A01A02A01', '', '', '', null, null, null, null, null, 'admin', '2018-06-11 17:17:28');
INSERT INTO `sys_depart` VALUES ('402880e447e9ba550147e9c53b2e0013', '8a8ab0b246dc81120146dc8180ba0017', '财务', null, null, '0', '', '2', 'A03A02', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_depart` VALUES ('402880e6487e661a01487e6b504e0001', '402880e447e9a9570147e9b762e30009', '销售人员', null, null, null, '销售人员', '2', 'A01A02A01A01', '', '', '', null, null, null, null, null, null, null);
INSERT INTO `sys_depart` VALUES ('402880f15986303c0159864816180002', '8a8ab0b246dc81120146dc8180a20016', '部门经理', null, null, null, '', '2', 'A02A01', '', '', '', null, null, null, null, null, null, null);
INSERT INTO `sys_depart` VALUES ('8a8ab0b246dc81120146dc8180a20016', null, '中国人寿总公司', null, null, null, '1111', '1', 'A02', '', '', '', null, null, null, null, null, null, null);
INSERT INTO `sys_depart` VALUES ('8a8ab0b246dc81120146dc8180ba0017', null, 'JEECG开源社区', null, null, '2', '', '1', 'A03', '', '', '', null, null, null, null, null, null, null);
INSERT INTO `sys_depart` VALUES ('8a8ab0b246dc81120146dc8180bd0018', '8a8ab0b246dc81120146dc8180ba0017', '软件开发部', null, null, '0', '研发技术难题', '2', 'A03A01', null, null, null, null, null, null, null, null, null, null);

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
INSERT INTO `sys_dict` VALUES ('d3ba35be350dab9c740eb07ade088a38', '性别', 'sex', '性别', '1', null, '2019-01-08 17:38:37', null, '2019-01-11 10:50:04');

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
INSERT INTO `sys_log` VALUES ('e88dde0f78fd219b78fa62dc1e43468f', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-28 13:32:04', null, null);
INSERT INTO `sys_log` VALUES ('22eebf6a25b3c8db0c7753f58cb9d821', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-28 13:37:09', null, null);
INSERT INTO `sys_log` VALUES ('2dbd854c93f81302d4c53f840d8a2035', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-28 13:37:18', null, null);
INSERT INTO `sys_log` VALUES ('8b087628098550785bb7bfdb8ec91351', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-28 13:39:19', null, null);
INSERT INTO `sys_log` VALUES ('4e5e7485a5357ef6316d74da40ae304d', '1', '用户名: admin,登录成功！', null, null, null, '127.0.0.1', null, null, null, null, null, 'jeecg-boot', '2019-01-28 13:44:05', null, null);

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
  `menu_type` int(11) default NULL COMMENT '菜单类型(1:菜单 2:按钮)',
  `perms` varchar(255) default NULL COMMENT '菜单权限编码',
  `sort_no` double(3,2) default NULL COMMENT '菜单排序',
  `always_show` int(3) default NULL COMMENT '聚合子路由: 1是0否',
  `icon` varchar(255) default NULL COMMENT '菜单图标',
  `is_leaf` int(2) default NULL COMMENT '是否叶子节点:    1:是   0:不是',
  `hidden` int(2) default '0' COMMENT '是否隐藏路由: 0否,1是（默认值0）',
  `description` varchar(255) default NULL COMMENT '描述',
  `del_flag` int(11) default NULL COMMENT '删除状态 0正常  1已删除',
  `create_by` varchar(255) default NULL COMMENT '创建人',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_by` varchar(255) default NULL COMMENT '更新人',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单权限表';

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('00a2a0ae65cdca5e93209cdbde97cbe6', '2e42e3835c2b44ec9f7bc26c146ee531', '成功', '/result/success', 'result/Success', null, '1', null, '1.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('05b3c82ddb2536a4a5ee1a4c46b5abef', '540a2936940846cb98114ffb0d145cb8', '用户列表', '/list/user-list', 'list/UserList', null, '1', null, '3.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('07acdd347152814619169844847d20d6', '2a470fc0c3954d9dbb61de6d80846549', '列表例子', '/jeecg/JeecgDemoList', 'jeecg/JeecgDemoList', null, '1', null, '1.00', '0', null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, '2019-01-22 11:00:54');
INSERT INTO `sys_permission` VALUES ('08e6b9dc3c04489c8e1ff2ce6f105aa4', null, '仪表盘', '/dashboard2', 'layouts/RouteView', null, '1', null, '9.99', '0', 'dashboard', '0', '0', null, '0', null, '2018-12-25 20:34:38', 'admin', '2019-01-21 19:59:04');
INSERT INTO `sys_permission` VALUES ('11e73a1d9f2678d617e44317828ed926', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '消息管理', 'system/msg', 'system/SysAnnouncementList', null, '1', null, null, '0', null, '1', '0', null, '0', 'admin', '2019-01-21 20:33:28', 'admin', '2019-01-21 20:33:42');
INSERT INTO `sys_permission` VALUES ('13212d3416eb690c2e1d5033166ff47a', '2e42e3835c2b44ec9f7bc26c146ee531', '失败', '/result/fail', 'result/Error', null, '1', null, '2.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('1367a93f2c410b169faa7abcbad2f77c', '6e73eb3c26099c191bf03852ee1310a1', '基本设置', '/account/settings/base', 'account/settings/BaseSetting', null, '1', 'BaseSettings', null, '0', null, '1', '1', null, '0', null, '2018-12-26 18:58:35', null, null);
INSERT INTO `sys_permission` VALUES ('277bfabef7d76e89b33062b16a9a5020', 'e3c13679c73a4f829bcff2aba8fd68b1', '基础表单', '/form/base-form', 'form/BasicForm', null, '1', null, '1.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('2a470fc0c3954d9dbb61de6d80846549', null, 'JEECG案例', '/jeecg', 'layouts/RouteView', null, '1', null, '9.99', '0', 'dashboard', '0', '0', null, '0', null, '2018-12-25 20:34:38', 'admin', '2019-01-21 19:59:31');
INSERT INTO `sys_permission` VALUES ('2e42e3835c2b44ec9f7bc26c146ee531', null, '结果页', '/result', 'layouts/PageView', null, '1', null, '9.99', '0', 'check-circle-o', '0', '0', null, '0', null, '2018-12-25 20:34:38', 'admin', '2019-01-21 19:59:39');
INSERT INTO `sys_permission` VALUES ('339329ed54cf255e1f9392e84f136901', '2a470fc0c3954d9dbb61de6d80846549', 'helloworld', '/jeecg/helloworld', 'jeecg/helloworld', null, '1', null, '2.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('34a10d0973052776d7a378605c904417', '9c2bede8b0f8ac9b981c54269c0d9bde', '第三方流程测试', '/sps/ThirdFlowTest', 'sps/test/ThirdFlowTest', null, '1', null, null, '0', null, '1', '0', null, '1', null, '2019-01-11 18:30:06', null, '2019-01-11 18:32:44');
INSERT INTO `sys_permission` VALUES ('3f915b2769fc80648e92d04e84ca059d', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '用户管理', '/isystem/user', 'system/UserList', null, '1', null, '1.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('418964ba087b90a84897b62474496b93', '540a2936940846cb98114ffb0d145cb8', '查询表格', '/list/query-list', 'list/TableList', null, '1', null, '1.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('4875ebe289344e14844d8e3ea1edd73f', null, '详情页', '/profile', 'layouts/RouteView', null, '1', null, '9.99', '0', 'profile', '0', '0', null, '0', null, '2018-12-25 20:34:38', 'admin', '2019-01-21 19:59:15');
INSERT INTO `sys_permission` VALUES ('4f66409ef3bbd69c1d80469d6e2a885e', '6e73eb3c26099c191bf03852ee1310a1', '账户绑定', '/account/settings/binding', 'account/settings/Binding', null, '1', 'BindingSettings', null, '0', null, '1', '1', null, '0', null, '2018-12-26 19:01:20', null, null);
INSERT INTO `sys_permission` VALUES ('4f84f9400e5e92c95f05b554724c2b58', '540a2936940846cb98114ffb0d145cb8', '角色列表', '/list/role-list', 'list/RoleList', null, '1', null, '4.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('540a2936940846cb98114ffb0d145cb8', null, '列表页', '/list', 'layouts/PageView', null, '1', null, '9.99', '0', 'table', '0', '0', null, '0', null, '2018-12-25 20:34:38', 'admin', '2019-01-21 19:59:46');
INSERT INTO `sys_permission` VALUES ('54dd5457a3190740005c1bfec55b1c34', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '菜单管理', '/isystem/permission', 'system/PermissionList', null, '1', null, '3.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('58857ff846e61794c69208e9d3a85466', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '日志管理', '/isystem/log', 'system/LogList', null, '1', null, '4.00', null, null, '1', '0', null, '0', null, '2018-12-26 10:11:18', null, '2018-12-26 15:28:06');
INSERT INTO `sys_permission` VALUES ('6531cf3421b1265aeeeabaab5e176e6d', 'e3c13679c73a4f829bcff2aba8fd68b1', '分步表单', '/form/step-form', 'form/stepForm/StepForm', null, '1', null, '2.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('65a8f489f25a345836b7f44b1181197a', 'c65321e57b7949b7a975313220de0422', '403', '/exception/403', 'exception/403', null, '1', null, '1.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('693ce69af3432bd00be13c3971a57961', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '监控页', '/dashboard/monitor', 'dashboard/Monitor', null, '1', null, '2.00', null, null, '1', '1', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('6e73eb3c26099c191bf03852ee1310a1', '717f6bee46f44a3897eca9abd6e2ec44', '个人设置', '/account/settings', 'account/settings/Index', null, '1', null, '2.00', '1', null, '0', '0', null, '0', null, '2018-12-25 20:34:38', null, '2018-12-26 19:05:26');
INSERT INTO `sys_permission` VALUES ('717f6bee46f44a3897eca9abd6e2ec44', null, '个人页', '/account', 'layouts/RouteView', null, '1', null, '9.99', '0', 'user', '0', '0', null, '0', null, '2018-12-25 20:34:38', 'admin', '2019-01-21 20:00:02');
INSERT INTO `sys_permission` VALUES ('73678f9daa45ed17a3674131b03432fb', '540a2936940846cb98114ffb0d145cb8', '权限列表', '/list/permission-list', 'list/PermissionList', null, '1', null, '5.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('7ac9eb9ccbde2f7a033cd4944272bf1e', '540a2936940846cb98114ffb0d145cb8', '卡片列表', '/list/card', 'list/CardList', null, '1', null, '7.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('882a73768cfd7f78f3a37584f7299656', '6e73eb3c26099c191bf03852ee1310a1', '个性化设置', '/account/settings/custom', 'account/settings/Custom', null, '1', 'CustomSettings', null, '0', null, '1', '1', null, '0', null, '2018-12-26 19:00:46', null, null);
INSERT INTO `sys_permission` VALUES ('8fb8172747a78756c11916216b8b8066', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '工作台', '/dashboard/workplace', 'dashboard/Workplace', null, '1', null, '3.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('9502685863ab87f0ad1134142788a385', null, '首页', '/dashboard/analysis', 'dashboard/Analysis', null, '1', null, '1.00', '0', 'dashboard', '1', '0', null, '0', null, '2018-12-25 20:34:38', 'admin', '2019-01-21 19:21:01');
INSERT INTO `sys_permission` VALUES ('ae4fed059f67086fd52a73d913cf473d', '540a2936940846cb98114ffb0d145cb8', '内联编辑表格', '/list/edit-table', 'list/TableInnerEditList', null, '1', null, '2.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('b1cb0a3fedf7ed0e4653cb5a229837ee', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '定时任务', 'system/QuartzJobList', 'system/QuartzJobList', null, '1', null, '5.00', '0', null, '1', '0', null, '0', null, '2019-01-03 09:38:52', null, '2019-01-03 09:42:07');
INSERT INTO `sys_permission` VALUES ('b3c824fc22bd953e2eb16ae6914ac8f9', '4875ebe289344e14844d8e3ea1edd73f', '高级详情页', '/profile/advanced', 'profile/advanced/Advanced', null, '1', null, '2.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('b4dfc7d5dd9e8d5b6dd6d4579b1aa559', 'c65321e57b7949b7a975313220de0422', '500', '/exception/500', 'exception/500', null, '1', null, '3.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('be71f37d127b02d6b09b9ddaa241313d', '9c2bede8b0f8ac9b981c54269c0d9bde', '第三方流程测试', '/sps/ThirdFlow', '/sps/test/ThirdFlow', null, '1', 'ThirdFlow', null, '0', null, '1', '0', null, '1', null, '2019-01-11 18:28:54', null, null);
INSERT INTO `sys_permission` VALUES ('c65321e57b7949b7a975313220de0422', null, '异常页', '/exception', 'layouts/RouteView', null, '1', null, '9.99', '0', 'warning', '0', '0', null, '0', null, '2018-12-25 20:34:38', 'admin', '2019-01-21 19:59:54');
INSERT INTO `sys_permission` VALUES ('c670cd41b4156e83061d5d710d991270', '9c2bede8b0f8ac9b981c54269c0d9bde', '流程测试', '/isps/flowTest', 'sps/test/FlowTest', null, '1', 'FlowTest', null, '0', null, '1', '0', null, '1', null, '2019-01-09 18:20:51', null, '2019-01-09 20:52:17');
INSERT INTO `sys_permission` VALUES ('c71c0623ea9c9a846a1dafb999271fcd', '66de730f242bb917264698b422ddeb09', '流程管理', '/isps/flowList', 'sps/flow/FlowList', null, '1', null, '4.00', '0', 'bars', '1', '0', null, '1', null, '2019-01-15 11:50:49', null, '2019-01-15 15:01:42');
INSERT INTO `sys_permission` VALUES ('cc50656cf9ca528e6f2150eba4714ad2', '4875ebe289344e14844d8e3ea1edd73f', '基础详情页', '/profile/basic', 'profile/basic/Index', null, '1', null, '1.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('d2bbf9ebca5a8fa2e227af97d2da7548', 'c65321e57b7949b7a975313220de0422', '404', '/exception/404', 'exception/404', null, '1', null, '2.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('d7d6e2e4e2934f2c9385a623fd98c6f3', null, '系统管理', '/isystem', 'layouts/RouteView', null, '1', null, '8.00', '0', 'dashboard', '0', '0', null, '0', null, '2018-12-25 20:34:38', 'admin', '2019-01-21 20:33:42');
INSERT INTO `sys_permission` VALUES ('d86f58e7ab516d3bc6bfb1fe10585f97', '717f6bee46f44a3897eca9abd6e2ec44', '个人中心', '/account/center', 'account/center/Index', null, '1', null, '1.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('e3c13679c73a4f829bcff2aba8fd68b1', null, '表单页', '/form', 'layouts/PageView', null, '1', null, '9.99', '0', 'form', '0', '0', null, '0', null, '2018-12-25 20:34:38', 'admin', '2019-01-21 19:59:22');
INSERT INTO `sys_permission` VALUES ('e5973686ed495c379d829ea8b2881fc6', 'e3c13679c73a4f829bcff2aba8fd68b1', '高级表单', '/form/advanced-form', 'form/advancedForm/AdvancedForm', null, '1', null, '3.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('e8af452d8948ea49d37c934f5100ae6a', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '角色管理', '/isystem/role', 'system/RoleList', null, '1', null, '2.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('ec8d607d0156e198b11853760319c646', '6e73eb3c26099c191bf03852ee1310a1', '安全设置', '/account/settings/security', 'account/settings/Security', null, '1', 'SecuritySettings', null, '0', null, '1', '1', null, '0', null, '2018-12-26 18:59:52', null, null);
INSERT INTO `sys_permission` VALUES ('f1cb187abf927c88b89470d08615f5ac', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '数据字典', '/isystem/dict', 'system/DictList', null, '1', null, '5.00', '0', null, '1', '0', null, '0', null, '2018-12-28 13:54:43', null, '2018-12-28 15:37:54');
INSERT INTO `sys_permission` VALUES ('f23d9bfff4d9aa6b68569ba2cff38415', '540a2936940846cb98114ffb0d145cb8', '标准列表', '/list/basic-list', 'list/StandardList', null, '1', null, '6.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('fb07ca05a3e13674dbf6d3245956da2e', '540a2936940846cb98114ffb0d145cb8', '搜索列表', '/list/search', 'list/search/SearchLayout', null, '1', null, '8.00', null, null, '1', '0', null, '0', null, '2018-12-25 20:34:38', null, null);
INSERT INTO `sys_permission` VALUES ('fedfbf4420536cacc0218557d263dfea', '6e73eb3c26099c191bf03852ee1310a1', '新消息通知', '/account/settings/notification', 'account/settings/Notification', null, '1', 'NotificationSettings', null, '0', '', '1', '1', null, '0', null, '2018-12-26 19:02:05', null, null);

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
INSERT INTO `sys_quartz_job` VALUES ('df26ecacf0f75d219d746750fe84bbee', null, null, '0', 'admin', '2019-01-28 13:57:22', 'org.jeecg.modules.quartz.job.SampleParamJob', '0/1 * * * * ?', 'scott', '带参测试 后台将每隔1秒执行输出日志', '-1');
INSERT INTO `sys_quartz_job` VALUES ('58180f2a7c8cd36a121fd0fff3f02a36', null, null, '0', 'admin', '2019-01-28 13:57:45', 'org.jeecg.modules.quartz.job.SampleJob', '0/1 * * * * ?', null, null, '-1');

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
INSERT INTO `sys_role` VALUES ('f6817f48af4fb3af11b9e8bf182f618b', '管理员', 'gly', '管理员', null, '2018-12-21 18:03:39', null, '2019-01-10 16:20:04');

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
INSERT INTO `sys_role_permission` VALUES ('026351916c841e114468e941627a787b', 'f6817f48af4fb3af11b9e8bf182f618b', '58857ff846e61794c69208e9d3a85466');
INSERT INTO `sys_role_permission` VALUES ('064e67f62aea92462a0875ce294ab5d6', 'f6817f48af4fb3af11b9e8bf182f618b', '6e73eb3c26099c191bf03852ee1310a1');
INSERT INTO `sys_role_permission` VALUES ('0ca8b65b108dd0ab3d9eb90f554f71ba', 'f6817f48af4fb3af11b9e8bf182f618b', '8f486c17bf7016301432019fdba7aed8');
INSERT INTO `sys_role_permission` VALUES ('118fa3d45d246d50a3c5d9fa92c2a400', 'f6817f48af4fb3af11b9e8bf182f618b', 'e8af452d8948ea49d37c934f5100ae6a');
INSERT INTO `sys_role_permission` VALUES ('11b4fd5d11b50afdc9a0dc7676037ba2', 'f6817f48af4fb3af11b9e8bf182f618b', 'c65321e57b7949b7a975313220de0422');
INSERT INTO `sys_role_permission` VALUES ('127ae6e13af03f07180f7ffff41ec7bb', 'f6817f48af4fb3af11b9e8bf182f618b', 'd2bbf9ebca5a8fa2e227af97d2da7548');
INSERT INTO `sys_role_permission` VALUES ('164a6f118aa15138d6c8bdc667892ba3', 'f6817f48af4fb3af11b9e8bf182f618b', '3f915b2769fc80648e92d04e84ca059d');
INSERT INTO `sys_role_permission` VALUES ('171ba1c7306b2cb38c980b29801ba37d', 'f6817f48af4fb3af11b9e8bf182f618b', 'ddf29a50766ebf228a76d676f34d1121');
INSERT INTO `sys_role_permission` VALUES ('2658494328be1ebc035134c05fc2f11e', 'f6817f48af4fb3af11b9e8bf182f618b', '54dd5457a3190740005c1bfec55b1c34');
INSERT INTO `sys_role_permission` VALUES ('26c98a73fac3c79dfdd16016356a9faa', 'f6817f48af4fb3af11b9e8bf182f618b', '4875ebe289344e14844d8e3ea1edd73f');
INSERT INTO `sys_role_permission` VALUES ('27db84db281961e840b596c49346ef7e', 'f6817f48af4fb3af11b9e8bf182f618b', '1367a93f2c410b169faa7abcbad2f77c');
INSERT INTO `sys_role_permission` VALUES ('287594a1ec30caa90810ef7a58178bd0', 'f6817f48af4fb3af11b9e8bf182f618b', 'f1cb187abf927c88b89470d08615f5ac');
INSERT INTO `sys_role_permission` VALUES ('2be31e6e281acf08578a2e5b4f73f815', 'f6817f48af4fb3af11b9e8bf182f618b', 'e5973686ed495c379d829ea8b2881fc6');
INSERT INTO `sys_role_permission` VALUES ('2c4bb54630e60fe2660da36b11bcf12e', 'f6817f48af4fb3af11b9e8bf182f618b', 'b4dfc7d5dd9e8d5b6dd6d4579b1aa559');
INSERT INTO `sys_role_permission` VALUES ('2df3ab2b5214cc1d034507e152771a41', 'f6817f48af4fb3af11b9e8bf182f618b', '717f6bee46f44a3897eca9abd6e2ec44');
INSERT INTO `sys_role_permission` VALUES ('2e3f1ec4c21885ce061b61e2519e79c8', 'f6817f48af4fb3af11b9e8bf182f618b', 'c71c0623ea9c9a846a1dafb999271fcd');
INSERT INTO `sys_role_permission` VALUES ('2f0aa12fafe792f6c786780fdd5cfeeb', 'f6817f48af4fb3af11b9e8bf182f618b', 'a68e9113be42055075189de4f77853e0');
INSERT INTO `sys_role_permission` VALUES ('37608598f14cfe94e4ccc288ce1ea336', 'f6817f48af4fb3af11b9e8bf182f618b', 'd7d6e2e4e2934f2c9385a623fd98c6f3');
INSERT INTO `sys_role_permission` VALUES ('3a23f807225746ceff686953e9544859', 'f6817f48af4fb3af11b9e8bf182f618b', '418964ba087b90a84897b62474496b93');
INSERT INTO `sys_role_permission` VALUES ('40d2734e7379ef44a6b6d42070e3ff16', 'f6817f48af4fb3af11b9e8bf182f618b', '37236b347227b5d3f5cd9e463099f91c');
INSERT INTO `sys_role_permission` VALUES ('45ad5bc317e6cf9fd98fce84b8500205', 'f6817f48af4fb3af11b9e8bf182f618b', '2a470fc0c3954d9dbb61de6d80846549');
INSERT INTO `sys_role_permission` VALUES ('48117105a2234463ffd9fe82e033b26b', 'f6817f48af4fb3af11b9e8bf182f618b', '00a2a0ae65cdca5e93209cdbde97cbe6');
INSERT INTO `sys_role_permission` VALUES ('496108f88375b90ee7ee50909ff6a049', 'f6817f48af4fb3af11b9e8bf182f618b', 'a8d3ffd0c52950023291834e370ec3e2');
INSERT INTO `sys_role_permission` VALUES ('4c84fa4daab1b4d7c109d7cab49e9743', 'f6817f48af4fb3af11b9e8bf182f618b', '08e6b9dc3c04489c8e1ff2ce6f105aa4');
INSERT INTO `sys_role_permission` VALUES ('4e122c75b51533ec51e1e80a2c240658', 'f6817f48af4fb3af11b9e8bf182f618b', 'fedfbf4420536cacc0218557d263dfea');
INSERT INTO `sys_role_permission` VALUES ('4e5cf9a9adec9cddece86e86d6f31278', 'f6817f48af4fb3af11b9e8bf182f618b', '693ce69af3432bd00be13c3971a57961');
INSERT INTO `sys_role_permission` VALUES ('4f28ab1dbfbc4219a352191329c51842', 'f6817f48af4fb3af11b9e8bf182f618b', '8e8517f135e55f1ef9340a558c9367f1');
INSERT INTO `sys_role_permission` VALUES ('5a99079ca98546c838a225b0e6012cb9', 'ee8626f80f7c2619917b6236f3a7f02b', 'fedfbf4420536cacc0218557d263dfea');
INSERT INTO `sys_role_permission` VALUES ('5b32a4a35f8aa6ffc94c93847ea1940f', 'ee8626f80f7c2619917b6236f3a7f02b', '4f66409ef3bbd69c1d80469d6e2a885e');
INSERT INTO `sys_role_permission` VALUES ('5e1aa00e6434a749109523d2a4693b80', 'f6817f48af4fb3af11b9e8bf182f618b', '34a10d0973052776d7a378605c904417');
INSERT INTO `sys_role_permission` VALUES ('64390c46bff797016556fbe1ac88fb8c', 'f6817f48af4fb3af11b9e8bf182f618b', '05b3c82ddb2536a4a5ee1a4c46b5abef');
INSERT INTO `sys_role_permission` VALUES ('6b98c1bbc3ff966ca8f88b7192b38ae6', 'f6817f48af4fb3af11b9e8bf182f618b', '73678f9daa45ed17a3674131b03432fb');
INSERT INTO `sys_role_permission` VALUES ('6fa242fce7ba2c5d71cd16602f007a4d', 'f6817f48af4fb3af11b9e8bf182f618b', '5efbf11a764db7759c36c669a263f292');
INSERT INTO `sys_role_permission` VALUES ('714b9b0bd92212ba86bafe7bbc902d6e', 'f6817f48af4fb3af11b9e8bf182f618b', 'c670cd41b4156e83061d5d710d991270');
INSERT INTO `sys_role_permission` VALUES ('75002588591820806', '16457350655250432', '5129710648430592');
INSERT INTO `sys_role_permission` VALUES ('75002588604403712', '16457350655250432', '5129710648430593');
INSERT INTO `sys_role_permission` VALUES ('75002588612792320', '16457350655250432', '40238597734928384');
INSERT INTO `sys_role_permission` VALUES ('75002588625375232', '16457350655250432', '57009744761589760');
INSERT INTO `sys_role_permission` VALUES ('75002588633763840', '16457350655250432', '16392452747300864');
INSERT INTO `sys_role_permission` VALUES ('75002588637958144', '16457350655250432', '16392767785668608');
INSERT INTO `sys_role_permission` VALUES ('75002588650541056', '16457350655250432', '16439068543946752');
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
INSERT INTO `sys_role_permission` VALUES ('7be61b677280c540159c769557b26f2b', 'f6817f48af4fb3af11b9e8bf182f618b', '4f84f9400e5e92c95f05b554724c2b58');
INSERT INTO `sys_role_permission` VALUES ('7ef49dbe94ca5bbd675b70b7dcde595c', 'f6817f48af4fb3af11b9e8bf182f618b', 'f23d9bfff4d9aa6b68569ba2cff38415');
INSERT INTO `sys_role_permission` VALUES ('7f089d0c2d9740c7824a26d0b05b7a7c', 'f6817f48af4fb3af11b9e8bf182f618b', '9502685863ab87f0ad1134142788a385');
INSERT INTO `sys_role_permission` VALUES ('86ac4619a3ef549b75926b6756a8f64d', 'f6817f48af4fb3af11b9e8bf182f618b', '0b6debda2d5a214aa81540971575a6b2');
INSERT INTO `sys_role_permission` VALUES ('89f45a7ec6f9b69443d222267ac58713', 'f6817f48af4fb3af11b9e8bf182f618b', 'fb07ca05a3e13674dbf6d3245956da2e');
INSERT INTO `sys_role_permission` VALUES ('912068609b28ee52603494f814881dd4', 'f6817f48af4fb3af11b9e8bf182f618b', '540a2936940846cb98114ffb0d145cb8');
INSERT INTO `sys_role_permission` VALUES ('95528c4a7eb49790e58415c6ec6e3611', 'f6817f48af4fb3af11b9e8bf182f618b', 'ec8d607d0156e198b11853760319c646');
INSERT INTO `sys_role_permission` VALUES ('9c9e3ea9159cb505b857f7f19819041b', 'f6817f48af4fb3af11b9e8bf182f618b', '7ac9eb9ccbde2f7a033cd4944272bf1e');
INSERT INTO `sys_role_permission` VALUES ('a043544bf8b3f53dbca0ccc5847aff9f', 'f6817f48af4fb3af11b9e8bf182f618b', '37fc0af6d161dc6f73b7d4b875e35a93');
INSERT INTO `sys_role_permission` VALUES ('a3bfe5ca069c1c96f14a5b0e4cee07e0', 'f6817f48af4fb3af11b9e8bf182f618b', 'cc50656cf9ca528e6f2150eba4714ad2');
INSERT INTO `sys_role_permission` VALUES ('a4fe0c33261a78b5302be163e89bc276', 'f6817f48af4fb3af11b9e8bf182f618b', 'b3c824fc22bd953e2eb16ae6914ac8f9');
INSERT INTO `sys_role_permission` VALUES ('a5a2155cc655121f5d16d04b50beaaf2', 'f6817f48af4fb3af11b9e8bf182f618b', 'd86f58e7ab516d3bc6bfb1fe10585f97');
INSERT INTO `sys_role_permission` VALUES ('a7731e047246e321629e2ad9346054fd', 'f6817f48af4fb3af11b9e8bf182f618b', '882a73768cfd7f78f3a37584f7299656');
INSERT INTO `sys_role_permission` VALUES ('abc60b9977934277aacdb60c3ad06ebf', 'ee8626f80f7c2619917b6236f3a7f02b', 'ec8d607d0156e198b11853760319c646');
INSERT INTO `sys_role_permission` VALUES ('aefcfc55ae66e975d92798fc5431fe82', 'f6817f48af4fb3af11b9e8bf182f618b', '11e73a1d9f2678d617e44317828ed926');
INSERT INTO `sys_role_permission` VALUES ('af19013fed4af1a6cdb2f54054119d0b', 'ee8626f80f7c2619917b6236f3a7f02b', '882a73768cfd7f78f3a37584f7299656');
INSERT INTO `sys_role_permission` VALUES ('b01aab380957b42a3e43838292da9f54', 'f6817f48af4fb3af11b9e8bf182f618b', '339329ed54cf255e1f9392e84f136901');
INSERT INTO `sys_role_permission` VALUES ('b1fe8ab8c91963aa031b1dfda310e1f9', 'f6817f48af4fb3af11b9e8bf182f618b', '8fb8172747a78756c11916216b8b8066');
INSERT INTO `sys_role_permission` VALUES ('b402c7c5e8dad60e236edc5bcffdb6df', 'f6817f48af4fb3af11b9e8bf182f618b', '2e42e3835c2b44ec9f7bc26c146ee531');
INSERT INTO `sys_role_permission` VALUES ('b9cdd4b5f397b0ec67bd0b8b4a75328e', 'f6817f48af4fb3af11b9e8bf182f618b', '131edfc0a0c6d89647eb4a89f85a1125');
INSERT INTO `sys_role_permission` VALUES ('b9e2f8d4ce01d61b58ea35253ba16388', 'f6817f48af4fb3af11b9e8bf182f618b', '13212d3416eb690c2e1d5033166ff47a');
INSERT INTO `sys_role_permission` VALUES ('ba393ff6e8832dffdf8bed9e388cf19a', 'f6817f48af4fb3af11b9e8bf182f618b', '83741ee67fd3ba1d981744244dd74462');
INSERT INTO `sys_role_permission` VALUES ('bdf7aea996a57122c789a302bd30d328', 'f6817f48af4fb3af11b9e8bf182f618b', '07acdd347152814619169844847d20d6');
INSERT INTO `sys_role_permission` VALUES ('c1419439350fc381d980c43f17246861', 'f6817f48af4fb3af11b9e8bf182f618b', '6531cf3421b1265aeeeabaab5e176e6d');
INSERT INTO `sys_role_permission` VALUES ('cb36b119fa0df98b7c5420ea9ab80300', 'f6817f48af4fb3af11b9e8bf182f618b', '3eb07a99bd7dcea6d9bef40e37735c7f');
INSERT INTO `sys_role_permission` VALUES ('cd55c2ebba2124c51dcd1c5e14987c40', 'f6817f48af4fb3af11b9e8bf182f618b', '4f66409ef3bbd69c1d80469d6e2a885e');
INSERT INTO `sys_role_permission` VALUES ('cf9e06d4b6ffd1fb40aefb164f613598', 'f6817f48af4fb3af11b9e8bf182f618b', '65a8f489f25a345836b7f44b1181197a');
INSERT INTO `sys_role_permission` VALUES ('d1914e1fea6d398f053e5971340448bc', 'f6817f48af4fb3af11b9e8bf182f618b', 'be71f37d127b02d6b09b9ddaa241313d');
INSERT INTO `sys_role_permission` VALUES ('d90fe0542d327d90e1817a0427c17f1b', 'f6817f48af4fb3af11b9e8bf182f618b', '277bfabef7d76e89b33062b16a9a5020');
INSERT INTO `sys_role_permission` VALUES ('e06b6637ed1c90ebe1e396e56ce034c2', 'f6817f48af4fb3af11b9e8bf182f618b', 'cf02da6d64ff41229ed81f5dfcd898fa');
INSERT INTO `sys_role_permission` VALUES ('e2d1ce200e0161866ccb47505dc5dc34', 'ee8626f80f7c2619917b6236f3a7f02b', '1367a93f2c410b169faa7abcbad2f77c');
INSERT INTO `sys_role_permission` VALUES ('e82315c63da6f2c659b458f6e1377716', 'f6817f48af4fb3af11b9e8bf182f618b', '9271af487e0184387c326383d655ca37');
INSERT INTO `sys_role_permission` VALUES ('f349c4a1151b4d5db1873b7abf508281', 'f6817f48af4fb3af11b9e8bf182f618b', 'b1cb0a3fedf7ed0e4653cb5a229837ee');
INSERT INTO `sys_role_permission` VALUES ('f73e005160a9d146e8eb1b6241e87aba', 'f6817f48af4fb3af11b9e8bf182f618b', '89d70d3c72ad5cc778f511f8ac9f3242');
INSERT INTO `sys_role_permission` VALUES ('fc0902a26aa69dab30603c19b9a41f44', 'f6817f48af4fb3af11b9e8bf182f618b', 'ae4fed059f67086fd52a73d913cf473d');
INSERT INTO `sys_role_permission` VALUES ('fd0eb799bb38fe939b486e12a2db338f', 'f6817f48af4fb3af11b9e8bf182f618b', 'e3c13679c73a4f829bcff2aba8fd68b1');

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
INSERT INTO `sys_user` VALUES ('e9ca23d68d884d4ebb19d07889727dae', 'admin', '管理员', 'a5fcaadb846aaedc', '6MTVvy1z', 'user/20181226/favicon_1545796089238.ico', '2018-12-05 00:00:00', '1', '11@qq.com', '18566666666', '1', '0', null, '2018-12-21 17:54:10', null, '2019-01-19 10:59:46');

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
INSERT INTO `sys_user_role` VALUES ('6d096d48ce7281b50c5f224aab2ff87e', 'e9ca23d68d884d4ebb19d07889727dae', 'f6817f48af4fb3af11b9e8bf182f618b');
