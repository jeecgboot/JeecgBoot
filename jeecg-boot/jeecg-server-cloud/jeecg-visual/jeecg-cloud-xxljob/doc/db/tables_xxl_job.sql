/*
 Navicat Premium Data Transfer

 Source Server         : mysql5.7
 Source Server Type    : MySQL
 Source Server Version : 50738
 Source Host           : 127.0.0.1:3306
 Source Schema         : xxl_job_241

 Target Server Type    : MySQL
 Target Server Version : 50738
 File Encoding         : 65001

 Date: 21/08/2024 22:43:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for xxl_job_group
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_group`;
CREATE TABLE `xxl_job_group`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '执行器AppName',
  `title` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '执行器名称',
  `address_type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '执行器地址类型：0=自动注册、1=手动录入',
  `address_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '执行器地址列表，多地址逗号分隔',
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of xxl_job_group
-- ----------------------------
INSERT INTO `xxl_job_group` VALUES (1, 'xxl-job-executor-sample', '示例执行器', 0, NULL, '2024-08-21 22:42:43');
INSERT INTO `xxl_job_group` VALUES (2, 'jeecg-demo', '测试Demo模块', 0, 'http://192.168.0.105:10001/', '2024-08-21 22:42:43');
INSERT INTO `xxl_job_group` VALUES (3, 'jeecg-system', '系统System模块', 0, 'http://192.168.0.105:10002/', '2024-08-21 22:42:43');

-- ----------------------------
-- Table structure for xxl_job_info
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_info`;
CREATE TABLE `xxl_job_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `add_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `author` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作者',
  `alarm_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警邮件',
  `schedule_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'NONE' COMMENT '调度类型',
  `schedule_conf` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '调度配置，值含义取决于调度类型',
  `misfire_strategy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DO_NOTHING' COMMENT '调度过期策略',
  `executor_route_strategy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行器路由策略',
  `executor_handler` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行器任务参数',
  `executor_block_strategy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '阻塞处理策略',
  `executor_timeout` int(11) NOT NULL DEFAULT 0 COMMENT '任务执行超时时间，单位秒',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT 0 COMMENT '失败重试次数',
  `glue_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'GLUE备注',
  `glue_updatetime` datetime NULL DEFAULT NULL COMMENT 'GLUE更新时间',
  `child_jobid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子任务ID，多个逗号分隔',
  `trigger_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '调度状态：0-停止，1-运行',
  `trigger_last_time` bigint(13) NOT NULL DEFAULT 0 COMMENT '上次调度时间',
  `trigger_next_time` bigint(13) NOT NULL DEFAULT 0 COMMENT '下次调度时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of xxl_job_info
-- ----------------------------
INSERT INTO `xxl_job_info` VALUES (1, 1, '测试任务1', '2018-11-03 22:21:31', '2024-08-21 22:30:30', 'XXL', '', 'CRON', '0 0 0 * * ? *', 'DO_NOTHING', 'FIRST', 'demoJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2018-11-03 22:21:31', '', 1, 0, 1724256000000);
INSERT INTO `xxl_job_info` VALUES (2, 3, '测试jeecg xxljob', '2024-08-21 22:41:10', '2024-08-21 22:41:30', 'JEECG', '', 'CRON', '* * * * * ?', 'DO_NOTHING', 'FIRST', 'demoJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2024-08-21 22:41:10', '', 1, 1724251373000, 1724251374000);

-- ----------------------------
-- Table structure for xxl_job_lock
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_lock`;
CREATE TABLE `xxl_job_lock`  (
  `lock_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '锁名称',
  PRIMARY KEY (`lock_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of xxl_job_lock
-- ----------------------------
INSERT INTO `xxl_job_lock` VALUES ('schedule_lock');

-- ----------------------------
-- Table structure for xxl_job_log
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_log`;
CREATE TABLE `xxl_job_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `executor_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
  `executor_handler` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行器任务参数',
  `executor_sharding_param` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行器任务分片参数，格式如 1/2',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT 0 COMMENT '失败重试次数',
  `trigger_time` datetime NULL DEFAULT NULL COMMENT '调度-时间',
  `trigger_code` int(11) NOT NULL COMMENT '调度-结果',
  `trigger_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '调度-日志',
  `handle_time` datetime NULL DEFAULT NULL COMMENT '执行-时间',
  `handle_code` int(11) NOT NULL COMMENT '执行-状态',
  `handle_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '执行-日志',
  `alarm_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `I_trigger_time`(`trigger_time`) USING BTREE,
  INDEX `I_handle_code`(`handle_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 82 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of xxl_job_log
-- ----------------------------
INSERT INTO `xxl_job_log` VALUES (1, 1, 1, NULL, 'demoJobHandler', '', NULL, 0, '2024-08-21 22:29:07', 500, '任务触发类型：手动触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 2);
INSERT INTO `xxl_job_log` VALUES (2, 1, 1, NULL, 'demoJobHandler', '', NULL, 0, '2024-08-21 22:29:48', 500, '任务触发类型：手动触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 2);
INSERT INTO `xxl_job_log` VALUES (3, 1, 1, NULL, 'demoJob', '', NULL, 0, '2024-08-21 22:30:34', 500, '任务触发类型：手动触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 2);
INSERT INTO `xxl_job_log` VALUES (4, 1, 1, NULL, 'demoJob', '', NULL, 0, '2024-08-21 22:30:40', 500, '任务触发类型：手动触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：null<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>调度失败：执行器地址为空<br><br>', NULL, 0, NULL, 2);
INSERT INTO `xxl_job_log` VALUES (5, 2, 2, 'http://192.168.0.105:10001/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:14', 500, '任务触发类型：手动触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10001/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10001/<br>code：500<br>msg：job handler [demoJob] not found.', NULL, 0, NULL, 2);
INSERT INTO `xxl_job_log` VALUES (6, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:27', 200, '任务触发类型：手动触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:27', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (7, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:36', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:36', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (8, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:37', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:37', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (9, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:38', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:38', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (10, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:39', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:39', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (11, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:40', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:40', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (12, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:40', 200, '任务触发类型：手动触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:40', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (13, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:41', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:41', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (14, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:42', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:42', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (15, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:43', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:43', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (16, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:44', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:44', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (17, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:45', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:45', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (18, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:46', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:46', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (19, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:47', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:47', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (20, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:48', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:48', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (21, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:49', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:49', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (22, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:50', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:50', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (23, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:51', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:51', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (24, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:52', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:52', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (25, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:53', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:53', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (26, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:54', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:54', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (27, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:55', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:55', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (28, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:56', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:56', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (29, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:57', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:57', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (30, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:58', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:58', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (31, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:41:59', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:41:59', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (32, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:00', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:00', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (33, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:01', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:01', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (34, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:02', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:02', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (35, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:03', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:03', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (36, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:04', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:04', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (37, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:05', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:05', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (38, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:06', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:06', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (39, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:07', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:07', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (40, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:08', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:08', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (41, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:09', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:09', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (42, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:10', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:10', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (43, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:11', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:11', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (44, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:12', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:12', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (45, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:13', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:13', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (46, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:14', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:14', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (47, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:15', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:15', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (48, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:16', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:16', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (49, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:17', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:17', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (50, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:18', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:18', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (51, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:19', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:19', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (52, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:20', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:20', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (53, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:21', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:21', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (54, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:22', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:22', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (55, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:23', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:23', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (56, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:24', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:24', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (57, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:25', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:25', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (58, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:26', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:26', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (59, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:27', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:27', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (60, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:28', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:28', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (61, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:29', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:29', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (62, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:30', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:30', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (63, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:31', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:31', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (64, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:32', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:32', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (65, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:33', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:33', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (66, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:34', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:34', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (67, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:35', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:35', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (68, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:36', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:36', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (69, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:37', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:37', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (70, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:38', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:38', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (71, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:39', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:39', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (72, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:40', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:40', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (73, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:41', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:41', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (74, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:42', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:42', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (75, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:43', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:43', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (76, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:44', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:44', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (77, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:45', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:45', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (78, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:46', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:46', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (79, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:47', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:47', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (80, 3, 2, 'http://192.168.0.105:10002/', 'demoJob', '', NULL, 0, '2024-08-21 22:42:48', 200, '任务触发类型：Cron触发<br>调度机器：192.168.0.105<br>执行器-注册方式：自动注册<br>执行器-地址列表：[http://192.168.0.105:10002/]<br>路由策略：第一个<br>阻塞处理策略：单机串行<br>任务超时时间：0<br>失败重试次数：0<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>触发调度：<br>address：http://192.168.0.105:10002/<br>code：200<br>msg：null', '2024-08-21 22:42:48', 0, '', 0);
INSERT INTO `xxl_job_log` VALUES (81, 3, 2, NULL, NULL, NULL, NULL, 0, '2024-08-21 22:42:49', 0, NULL, NULL, 0, NULL, 0);

-- ----------------------------
-- Table structure for xxl_job_log_report
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_log_report`;
CREATE TABLE `xxl_job_log_report`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `trigger_day` datetime NULL DEFAULT NULL COMMENT '调度-时间',
  `running_count` int(11) NOT NULL DEFAULT 0 COMMENT '运行中-日志数量',
  `suc_count` int(11) NOT NULL DEFAULT 0 COMMENT '执行成功-日志数量',
  `fail_count` int(11) NOT NULL DEFAULT 0 COMMENT '执行失败-日志数量',
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `i_trigger_day`(`trigger_day`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of xxl_job_log_report
-- ----------------------------
INSERT INTO `xxl_job_log_report` VALUES (1, '2024-08-21 00:00:00', 70, 0, 5, NULL);
INSERT INTO `xxl_job_log_report` VALUES (2, '2024-08-20 00:00:00', 0, 0, 0, NULL);
INSERT INTO `xxl_job_log_report` VALUES (3, '2024-08-19 00:00:00', 0, 0, 0, NULL);

-- ----------------------------
-- Table structure for xxl_job_logglue
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_logglue`;
CREATE TABLE `xxl_job_logglue`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `glue_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'GLUE备注',
  `add_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of xxl_job_logglue
-- ----------------------------

-- ----------------------------
-- Table structure for xxl_job_registry
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_registry`;
CREATE TABLE `xxl_job_registry`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `registry_group` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `registry_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `registry_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `i_g_k_v`(`registry_group`, `registry_key`, `registry_value`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of xxl_job_registry
-- ----------------------------
INSERT INTO `xxl_job_registry` VALUES (1, 'EXECUTOR', 'jeecg-demo', 'http://192.168.0.105:10001/', '2024-08-21 22:42:43');
INSERT INTO `xxl_job_registry` VALUES (3, 'EXECUTOR', 'jeecg-system', 'http://192.168.0.105:10002/', '2024-08-21 22:42:21');

-- ----------------------------
-- Table structure for xxl_job_user
-- ----------------------------
DROP TABLE IF EXISTS `xxl_job_user`;
CREATE TABLE `xxl_job_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `role` tinyint(4) NOT NULL COMMENT '角色：0-普通用户、1-管理员',
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限：执行器ID列表，多个逗号分割',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `i_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of xxl_job_user
-- ----------------------------
INSERT INTO `xxl_job_user` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1, NULL);

SET FOREIGN_KEY_CHECKS = 1;
