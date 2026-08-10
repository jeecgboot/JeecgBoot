-- =====================================================================
-- Chat2BI（积木问数）模块 升级/建表 SQL（MySQL）
-- 适用：在已有 JeecgBoot 库上新增本模块所需表。
--
-- 多表规范设计：5 张表（一对象一表，逻辑清晰、便于维护）。
--   chat2bi_domain     数据域      绑定一个数据源 datasource_id（复用积木报表 jimu_report_data_source 表）；表关联 joins_json 列存于本表
--   chat2bi_table      注册表（属于数据域）  列定义 columns_json 作 JSON 列
--   chat2bi_topic      查询主题（属于数据域）主题字段 fields_json 作 JSON 列
--   chat2bi_assistant  对话助理（绑定数据域）推荐问题 questions_json 作 JSON 列
--   chat2bi_table_meta 表元数据    数据源整库 schema 缓存（表+字段），选表/字段从此表读取（在「AI数据表」中同步维护）
--   chat2bi_term       业务术语（属于数据域）近义词 synonyms_json 作 JSON 列；可配「仅释义/时间口径/数据筛选」
-- 数据源不单独建表，直接复用积木报表 jimu_report_data_source（在积木报表「数据源管理」中维护）。
-- 权限/安全护栏配置（访问授权/敏感词/行级/列级/敏感度/公开）较轻量，作 perm_json 列存于 chat2bi_domain。
--
-- 升级（已有旧库执行）：
--   ALTER TABLE `chat2bi_domain` ADD COLUMN `perm_json` text NULL COMMENT '权限/安全护栏配置JSON';
--   （chat2bi_term 直接执行下方 CREATE TABLE 即可）
--   助理多数据域（按问智能路由）：
--   ALTER TABLE `chat2bi_assistant` ADD COLUMN `domain_ids` varchar(1000) NULL COMMENT '绑定的全部数据域id列表JSON数组,domain_id为主域';
--   UPDATE `chat2bi_assistant` SET `domain_ids` = CONCAT('["', `domain_id`, '"]') WHERE `domain_id` IS NOT NULL AND `domain_id` <> '' AND (`domain_ids` IS NULL OR `domain_ids` = '');
-- =====================================================================



/*
 Navicat Premium Data Transfer

 Source Server         : mysql5.7
 Source Server Type    : MySQL
 Source Server Version : 50738 (5.7.38)
 Source Host           : 127.0.0.1:3306
 Source Schema         : jeecgboot3

 Target Server Type    : MySQL
 Target Server Version : 50738 (5.7.38)
 File Encoding         : 65001

 Date: 24/05/2026 21:27:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chat2bi_assistant
-- ----------------------------
DROP TABLE IF EXISTS `chat2bi_assistant`;
CREATE TABLE `chat2bi_assistant`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '助理名称',
  `avatar` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像emoji',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `welcome` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '欢迎语',
  `domain_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '绑定数据域id(主域,向后兼容)',
  `domain_ids` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '绑定的全部数据域id列表JSON数组["id1","id2"],按问智能路由,domain_id为主域',
  `show_sql` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否展示SQL 1/是,0/否',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态 online/已上线,draft/草稿,offline/已下线',
  `questions_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '推荐问题JSON [\"...\",...]',
  `sort_no` int(11) NULL DEFAULT NULL COMMENT '排序号',
  `perm_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '访问权限配置JSON {grantRoles,grantUsers}',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `tenant_id` int(11) NULL DEFAULT 0 COMMENT '多租户标识',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_c2b_assistant_domain`(`domain_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Chat2BI 对话助理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat2bi_assistant
-- ----------------------------
INSERT INTO `chat2bi_assistant` VALUES ('956f9607116047ada54619734d85f99f', '保险问数助手', '🛡️', '面向保险经营的智能问数助手，支持保单、保费、理赔多维分析', '你好！我是保险数据分析助手，可以帮你查询保单、保费、理赔等数据，试试点击下方推荐问题。', '6a01a7bce0174875a413f60e5bdec16b', '[\"6a01a7bce0174875a413f60e5bdec16b\"]', '1', 'online', '[\"各险种的保费总额\", \"各渠道的保单数量\", \"已赔付理赔金额按险种汇总\", \"各大区的保费排名\", \"各客户等级的保费总额\", \"保费最高的10张保单明细\", \"查询白金客户的明细\", \"月度保费趋势\"]', 1, NULL, 'admin', '2026-05-24 10:00:00', 'admin', '2026-05-24 10:00:00', 0);
INSERT INTO `chat2bi_assistant` VALUES ('eaba9231e2db4b348a321857067508eb', '系统用户助手', '👥', '面向平台系统用户的智能问数助手，支持用户、部门、归属多维分析', '你好！我是系统用户分析助手，可以帮你统计用户、部门、归属等数据，试试点击下方推荐问题。', '8422bb4067874cd5b9c8976962621104', '[\"8422bb4067874cd5b9c8976962621104\"]', '1', 'online', '[\"各部门的用户数量\", \"按月份统计新增用户数\", \"用户性别分布\", \"各机构类别的部门数量\"]', 2, NULL, 'admin', '2026-05-24 11:00:00', 'admin', '2026-05-24 11:00:00', 0);

-- ----------------------------
-- Table structure for chat2bi_conversation
-- ----------------------------
DROP TABLE IF EXISTS `chat2bi_conversation`;
CREATE TABLE `chat2bi_conversation`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `assistant_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属助理id',
  `domain_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据域id（冗余，独立访问时还原数据范围）',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会话标题（默认取首条提问）',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '归属用户（鉴权用）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `tenant_id` int(11) NULL DEFAULT 0 COMMENT '多租户标识',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_c2b_conv_ast_user`(`assistant_id`, `create_by`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Chat2BI 对话会话' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat2bi_conversation
-- ----------------------------
INSERT INTO `chat2bi_conversation` VALUES ('2887ab895ff842ef8a294f7573da6f74', '956f9607116047ada54619734d85f99f', '6a01a7bce0174875a413f60e5bdec16b', '各大区的保费排名', 'admin', '2026-05-24 09:00:00', NULL, '2026-05-24 21:12:56', 0);
INSERT INTO `chat2bi_conversation` VALUES ('332e10bc88b046be9b34186e07779a11', 'eaba9231e2db4b348a321857067508eb', '8422bb4067874cd5b9c8976962621104', '各部门的用户数量', 'admin', '2026-05-24 08:00:00', NULL, '2026-05-24 08:00:00', 0);
INSERT INTO `chat2bi_conversation` VALUES ('3d83bf2875f34bb4aa4119a43d515b99', '956f9607116047ada54619734d85f99f', '6a01a7bce0174875a413f60e5bdec16b', '已赔付理赔金额按险种汇总', 'admin', '2026-05-24 08:00:00', NULL, '2026-05-24 08:00:00', 0);
INSERT INTO `chat2bi_conversation` VALUES ('45938e194754426ba692ab50cde2d275', 'eaba9231e2db4b348a321857067508eb', '8422bb4067874cd5b9c8976962621104', '用户性别分布', 'admin', '2026-05-24 10:00:00', NULL, '2026-05-24 11:43:41', 0);
INSERT INTO `chat2bi_conversation` VALUES ('70c175254bfd4d88a9fe42efc050a540', '956f9607116047ada54619734d85f99f', '6a01a7bce0174875a413f60e5bdec16b', '各渠道的保单数量', 'admin', '2026-05-24 07:00:00', NULL, '2026-05-24 07:00:00', 0);
INSERT INTO `chat2bi_conversation` VALUES ('77718e1573e74f43ac326c99e608c611', 'bcd9638b3a574654bc606d5648642fab', 'c5c943945ba343a295f10d0ffb227b13', '下载量前10歌手', 'admin', '2026-05-23 23:57:44', NULL, '2026-05-24 00:01:00', 0);
INSERT INTO `chat2bi_conversation` VALUES ('bc75e772eafe43639e4e8f1698e51543', '956f9607116047ada54619734d85f99f', '6a01a7bce0174875a413f60e5bdec16b', '各险种的保费总额', 'admin', '2026-05-24 06:00:00', NULL, '2026-05-24 06:00:00', 0);
INSERT INTO `chat2bi_conversation` VALUES ('c6bca8f52ee548f48e7a7facdade7cb5', 'eaba9231e2db4b348a321857067508eb', '8422bb4067874cd5b9c8976962621104', '按月份统计新增用户数', 'admin', '2026-05-24 09:00:00', NULL, '2026-05-24 09:00:00', 0);
INSERT INTO `chat2bi_conversation` VALUES ('da121e06d85d4829b0ec1f2fcb5c80f6', 'bcd9638b3a574654bc606d5648642fab', 'c5c943945ba343a295f10d0ffb227b13', '哪个流派总播放量最高？', 'admin', '2026-05-23 23:57:25', NULL, '2026-05-24 00:09:11', 0);

-- ----------------------------
-- Table structure for chat2bi_domain
-- ----------------------------
DROP TABLE IF EXISTS `chat2bi_domain`;
CREATE TABLE `chat2bi_domain`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据域名称',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `datasource_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '绑定数据源id（jimu_report_data_source.id，空=应用默认库）',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态 enabled/启用,disabled/停用',
  `sort_no` int(11) NULL DEFAULT NULL COMMENT '排序号',
  `joins_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '表关联JSON [{id,left,right,leftCol,rightCol,type}]',
  `perm_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '权限/安全护栏配置JSON {grantRoles,grantUsers,sensWords,sensCats,rowRules,colRules,sensitivity,isPublic}',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `tenant_id` int(11) NULL DEFAULT 0 COMMENT '多租户标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Chat2BI 数据域' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat2bi_domain
-- ----------------------------
INSERT INTO `chat2bi_domain` VALUES ('6a01a7bce0174875a413f60e5bdec16b', '保险分析域', '保险业务问数：保单、保费、保额、理赔的多维分析', '', 'enabled', 1, '[{"id": 1, "left": "f7c4d22abafa4ac982679e9b1fba3cc5", "right": "70b99ccd7def497592a7df9f333f42fe", "leftCol": "cust_id", "rightCol": "id", "type": "LEFT JOIN"}, {"id": 2, "left": "56b5a44298cc4dfa82d8594be16ff3b3", "right": "f7c4d22abafa4ac982679e9b1fba3cc5", "leftCol": "policy_id", "rightCol": "id", "type": "LEFT JOIN"}]', '{"grantRoles": [], "grantUsers": [], "sensWords": ["王总"], "sensCats": {"politics": true, "abuse": true}, "rowRules": [{"id": 1, "role": "区域经理", "field": "region", "op": "=", "value": "华东", "dynamic": false, "sysVar": ""}], "colRules": [{"id": 1, "role": "分析师", "field": "premium", "action": "脱敏"}], "sensitivity": {}, "isPublic": false}', 'admin', '2026-05-24 10:00:00', 'admin', '2026-05-25 10:00:00', 0);
INSERT INTO `chat2bi_domain` VALUES ('8422bb4067874cd5b9c8976962621104', '系统用户分析域', '平台系统用户分析：用户、部门、归属关系的多维统计', '', 'enabled', 2, '[{"id": 1, "left": "6e67740eef90410ea898c08dc0159aad", "right": "e7a66f18e0a24068934ab3972ca2d718", "leftCol": "user_id", "rightCol": "id", "type": "LEFT JOIN"}, {"id": 2, "left": "6e67740eef90410ea898c08dc0159aad", "right": "d1c0fc67d9a64c19b36bd12bfcf22cdd", "leftCol": "dep_id", "rightCol": "id", "type": "LEFT JOIN"}]', '{"grantRoles":[],"grantUsers":[],"sensWords":[],"sensCats":{"politics":true,"abuse":true},"rowRules":[],"colRules":[{"id":1779706530340,"role":"区域经理","field":"org_code","action":"脱敏"},{"id":1779706535255,"role":"区域经理","field":"parent_id","action":"脱敏"},{"id":1779706542086,"role":"财务","field":"status","action":"脱敏"}],"sensitivity":{},"isPublic":true}', 'admin', '2026-05-24 11:00:00', 'admin', '2026-05-25 18:56:10', 0);

-- ----------------------------
-- Table structure for chat2bi_term（业务术语，属于数据域）
-- ----------------------------
DROP TABLE IF EXISTS `chat2bi_term`;
CREATE TABLE `chat2bi_term`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `domain_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属数据域id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '术语名称',
  `synonyms_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '近义词JSON [\"...\",...]',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '释义/描述',
  `apply_kind` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用方式 none/仅释义,time/时间口径,filter/数据筛选',
  `apply_time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间口径取值（apply_kind=time）',
  `apply_clause` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '追加WHERE过滤条件（apply_kind=filter）',
  `sort_no` int(11) NULL DEFAULT NULL COMMENT '排序号',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `tenant_id` int(11) NULL DEFAULT 0 COMMENT '多租户标识',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_c2b_term_domain`(`domain_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Chat2BI 业务术语' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat2bi_term
-- ----------------------------
INSERT INTO `chat2bi_term` VALUES ('ccbf357c925142ff94a06b60ed23c7d4', '6a01a7bce0174875a413f60e5bdec16b', '核心险种', '["主力险种","重点险种"]', '重点经营的险种，当前为寿险、健康险', 'filter', '', 'product_type IN (''寿险'',''健康险'')', NULL, 'admin', '2026-05-25 10:00:00', 'admin', '2026-05-25 18:48:19', 0);
INSERT INTO `chat2bi_term` VALUES ('d69a4114798b457db0e92188c3c675cf', '6a01a7bce0174875a413f60e5bdec16b', '近期', '["最近", "近一段时间"]', '统计口径取近10天', 'time', '近10天', '', 2, 'admin', '2026-05-25 10:00:00', 'admin', '2026-05-25 10:00:00', 0);
INSERT INTO `chat2bi_term` VALUES ('99ead4dbebc74c50ac962360e2e80c7b', '6a01a7bce0174875a413f60e5bdec16b', '高净值客户', '["白金客户", "VIP客户"]', '客户等级为白金的高价值客户', 'filter', '', 'cust_level = ''白金''', 3, 'admin', '2026-05-25 10:00:00', 'admin', '2026-05-25 10:00:00', 0);
INSERT INTO `chat2bi_term` VALUES ('68083475fac642c3910dc2dc42996d79', '6a01a7bce0174875a413f60e5bdec16b', '保费', '["保险费", "年缴保费"]', '指 premium 字段，保单的年缴保险费（仅作释义，不改口径）', 'none', '', '', 4, 'admin', '2026-05-25 10:00:00', 'admin', '2026-05-25 10:00:00', 0);
INSERT INTO `chat2bi_term` VALUES ('036b9e2bc8f3440db0ec10a986464d36', '8422bb4067874cd5b9c8976962621104', '正常用户', '["活跃用户", "启用用户", "在用用户"]', '状态为正常（未冻结）的系统用户', 'filter', '', 'status = 1', 1, 'admin', '2026-05-25 11:00:00', 'admin', '2026-05-25 11:00:00', 0);
INSERT INTO `chat2bi_term` VALUES ('8c755a4fe8c3463a95a781c6b6cc80d2', '8422bb4067874cd5b9c8976962621104', '冻结用户', '["禁用用户", "停用用户", "封禁用户"]', '状态为冻结、无法登录的用户', 'filter', '', 'status = 2', 2, 'admin', '2026-05-25 11:00:00', 'admin', '2026-05-25 11:00:00', 0);
INSERT INTO `chat2bi_term` VALUES ('551c7126628245279ebe726451ecec91', '8422bb4067874cd5b9c8976962621104', '近期', '["最近", "近一个月", "本月"]', '时间口径取本月，用于新增用户等趋势统计', 'time', '本月', '', 3, 'admin', '2026-05-25 11:00:00', 'admin', '2026-05-25 11:00:00', 0);
INSERT INTO `chat2bi_term` VALUES ('91455d7611f94723bd8b6c207771bccd', '8422bb4067874cd5b9c8976962621104', '公司', '["一级机构", "集团总部", "总公司"]', '机构类别为公司的顶层组织节点', 'filter', '', 'org_category = ''1''', 4, 'admin', '2026-05-25 11:00:00', 'admin', '2026-05-25 11:00:00', 0);
INSERT INTO `chat2bi_term` VALUES ('9db5c917ad2d4c30bf863c3a87928c50', '8422bb4067874cd5b9c8976962621104', '部门', '["机构", "组织节点"]', '指 sys_depart 的组织节点；org_category 区分 公司/部门/岗位/子公司（仅释义，不改口径）', 'none', '', '', 5, 'admin', '2026-05-25 11:00:00', 'admin', '2026-05-25 11:00:00', 0);


-- ----------------------------
-- Table structure for chat2bi_message
-- ----------------------------
DROP TABLE IF EXISTS `chat2bi_message`;
CREATE TABLE `chat2bi_message`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `conversation_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属会话id',
  `role` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色 user/ai',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文本内容（预览/检索用）',
  `msg_json` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '完整消息对象JSON（含dsl/data/sql/chartType/insight等，无损还原）',
  `sort_no` int(11) NULL DEFAULT NULL COMMENT '顺序号',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_c2b_msg_conv`(`conversation_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Chat2BI 对话消息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat2bi_message
-- ----------------------------
INSERT INTO `chat2bi_message` VALUES ('0000f0a1dc9c4ec6b1c8f92b059602e5', '45938e194754426ba692ab50cde2d275', 'ai', '已理解你的问题，按下面的口径为你取数（可直接调整）：', '{\"role\":\"ai\",\"text\":\"已理解你的问题，按下面的口径为你取数（可直接调整）：\",\"dsl\":{\"metric\":\"用户数\",\"dimension\":\"性别\",\"time\":\"全部\",\"filter\":\"\"},\"data\":[[\"男\",\"14\"],[\"女\",\"11\"],[\"未知\",\"4\"]],\"sql\":\"SELECT CASE sex WHEN 1 THEN \'男\' WHEN 2 THEN \'女\' ELSE \'未知\' END AS 性别, COUNT(*) AS 用户数\\nFROM sys_user\\nGROUP BY 性别\\nORDER BY 2 DESC\",\"insight\":\"「男」的用户数最高（14），约占总量的 48%。\",\"chart\":false,\"chartType\":\"饼图\",\"followup\":false}', 1, '2026-05-24 11:43:41');
INSERT INTO `chat2bi_message` VALUES ('0244b7c6f47e484eb7f0d49fbd91a634', '2887ab895ff842ef8a294f7573da6f74', 'ai', '已理解你的问题，为你查询到以下明细数据：', '{\"role\":\"ai\",\"text\":\"已理解你的问题，为你查询到以下明细数据：\",\"dsl\":{\"metric\":\"保单状态\",\"dimension\":\"保单ID\",\"time\":\"全部\",\"filter\":\"\"},\"detail\":true,\"columns\":[\"保单ID\",\"保单号\",\"客户姓名\",\"险种名称\",\"年保费\",\"保额\",\"投保日期\",\"保单状态\"],\"data\":[[\"200116\",\"P200116\",\"郑梓涵\",\"商业车险全保\",\"17997.47\",\"1350869.03\",\"2024-01-25\",\"已过期\"],[\"200033\",\"P200033\",\"宋宇勇\",\"好医保长期医疗\",\"17962.57\",\"1588838.64\",\"2024-11-08\",\"生效\"],[\"200130\",\"P200130\",\"郭玲\",\"雇主责任险\",\"17899.36\",\"812303.59\",\"2024-01-17\",\"生效\"],[\"200170\",\"P200170\",\"田洋\",\"车损险尊享\",\"17843.61\",\"1979911.50\",\"2025-12-21\",\"生效\"],[\"200290\",\"P200290\",\"梁磊\",\"少儿门诊医疗\",\"17768.79\",\"385975.90\",\"2025-01-20\",\"生效\"],[\"200045\",\"P200045\",\"曾娜\",\"商业车险全保\",\"17711.83\",\"1973682.71\",\"2023-04-01\",\"已过期\"],[\"200095\",\"P200095\",\"肖勇\",\"商业车险全保\",\"17599.99\",\"779816.14\",\"2025-10-11\",\"生效\"],[\"200101\",\"P200101\",\"罗强\",\"雇主责任险\",\"17537.99\",\"429642.99\",\"2026-03-28\",\"生效\"],[\"200015\",\"P200015\",\"袁嘉怡\",\"综合意外险\",\"17508.37\",\"1258736.91\",\"2023-12-26\",\"已过期\"],[\"200171\",\"P200171\",\"苏芳浩然\",\"百万守护意外险\",\"17422.52\",\"578822.53\",\"2023-06-25\",\"生效\"]],\"sql\":\"SELECT id AS 保单ID, policy_no AS 保单号, cust_name AS 客户姓名, product_name AS 险种名称, premium AS 年保费, coverage_amount AS 保额, sign_date AS 投保日期, status AS 保单状态 FROM ins_policy ORDER BY premium DESC LIMIT 10\",\"insight\":\"共查询到 10 条记录，8 个字段。\",\"chart\":false,\"chartType\":\"柱状图\",\"followup\":false}', 3, '2026-05-24 21:12:56');
INSERT INTO `chat2bi_message` VALUES ('05a430c90bd2438b9daf729f1f5270e5', 'bc75e772eafe43639e4e8f1698e51543', 'user', '各险种的保费总额', '{\"role\": \"user\", \"text\": \"各险种的保费总额\"}', 0, '2026-05-24 06:00:00');
INSERT INTO `chat2bi_message` VALUES ('063575b460304bd6af6bef809338180e', '3d83bf2875f34bb4aa4119a43d515b99', 'ai', '已理解你的问题，按下面的口径为你取数（可直接调整）：', '{\"role\": \"ai\", \"text\": \"已理解你的问题，按下面的口径为你取数（可直接调整）：\", \"dsl\": {\"metric\": \"实赔金额\", \"dimension\": \"险种类型\", \"time\": \"全部\", \"filter\": \"\", \"limit\": 100}, \"data\": [[\"意外险\", \"661,410\"], [\"健康险\", \"606,549\"], [\"车险\", \"539,711\"], [\"财产险\", \"425,733\"], [\"寿险\", \"408,964\"]], \"sql\": \"SELECT product_type, SUM(settle_amount) AS settle_sum\\nFROM ins_claim\\nWHERE status=\'已赔付\'\\nGROUP BY product_type\\nORDER BY 2 DESC\", \"insight\": \"「意外险」的实赔金额最高（661,410），约占总量的 25%。\", \"chart\": false, \"chartType\": \"柱状图\", \"followup\": false}', 1, '2026-05-24 08:00:00');
INSERT INTO `chat2bi_message` VALUES ('127c0e14fd94437e818735f6b685d521', '45938e194754426ba692ab50cde2d275', 'ai', '已理解你的问题，按下面的口径为你取数（可直接调整）：', '{\"role\":\"ai\",\"text\":\"已理解你的问题，按下面的口径为你取数（可直接调整）：\",\"dsl\":{\"metric\":\"用户数量\",\"dimension\":\"性别\",\"time\":\"全部\",\"filter\":\"\"},\"detail\":false,\"columns\":[\"性别\",\"用户数量\"],\"data\":[[\"男\",\"14\"],[\"女\",\"11\"],[\"其他\",\"3\"],[\"未知\",\"1\"]],\"sql\":\"SELECT CASE WHEN sex = 0 THEN \'未知\' WHEN sex = 1 THEN \'男\' WHEN sex = 2 THEN \'女\' ELSE \'其他\' END AS 性别, COUNT(*) AS 用户数量 FROM sys_user GROUP BY sex ORDER BY 用户数量 DESC LIMIT 50\",\"insight\":\"「男」的用户数量最高（14），本次共返回 4 组数据。\",\"chart\":false,\"chartType\":\"柱状图\",\"followup\":false}', 5, '2026-05-24 11:43:41');
INSERT INTO `chat2bi_message` VALUES ('15090e2fe6594e71bc32db520353e701', '332e10bc88b046be9b34186e07779a11', 'user', '各部门的用户数量', '{\"role\": \"user\", \"text\": \"各部门的用户数量\"}', 0, '2026-05-24 08:00:00');
INSERT INTO `chat2bi_message` VALUES ('183bfa8ee9704fc281a384fbae085bbf', '77718e1573e74f43ac326c99e608c611', 'ai', '已理解你的问题，按下面的口径为你取数（可直接调整）：', '{\"role\":\"ai\",\"text\":\"已理解你的问题，按下面的口径为你取数（可直接调整）：\",\"dsl\":{\"metric\":\"收藏量\",\"dimension\":\"月份\",\"time\":\"近10天\",\"filter\":\"\",\"limit\":10},\"data\":[[\"1月\",\"980,123\"],[\"2月\",\"1,120,400\"],[\"3月\",\"1,540,900\"],[\"4月\",\"1,733,210\"],[\"5月\",\"1,612,540\"],[\"6月\",\"1,489,330\"],[\"7月\",\"1,701,220\"],[\"8月\",\"1,820,915\"],[\"9月\",\"1,560,488\"],[\"10月\",\"1,402,377\"]],\"sql\":\"SELECT month, SUM(favor_cnt) AS favor_cnt_sum\\nFROM singer\\nGROUP BY month\\nORDER BY 2 DESC\\nLIMIT 10\",\"insight\":\"「1月」的收藏量最高（980,123），约占总量的 38%；近10天环比 +9.2%，主要由头部月份带动。\",\"chart\":false,\"chartType\":\"柱状图\",\"followup\":false,\"recomputed\":true}', 1, '2026-05-24 00:01:00');
INSERT INTO `chat2bi_message` VALUES ('1c09d4ea78e046feb6a73b3cabe23c47', '45938e194754426ba692ab50cde2d275', 'user', '各部门的用户数量', '{\"role\":\"user\",\"text\":\"各部门的用户数量\"}', 2, '2026-05-24 11:43:41');
INSERT INTO `chat2bi_message` VALUES ('22ec1932aefc4feda3ec81fe51226391', '45938e194754426ba692ab50cde2d275', 'user', '查询近期新增用户详细', '{\"role\":\"user\",\"text\":\"查询近期新增用户详细\"}', 11, '2026-05-24 11:43:41');
INSERT INTO `chat2bi_message` VALUES ('3e316ac367244e2bba75126ad2b1ce7b', '45938e194754426ba692ab50cde2d275', 'user', '用户性别分布', '{\"role\":\"user\",\"text\":\"用户性别分布\"}', 4, '2026-05-24 11:43:41');
INSERT INTO `chat2bi_message` VALUES ('3f4afec58eb2470daa8ed2154b3f113c', 'da121e06d85d4829b0ec1f2fcb5c80f6', 'user', '哪个流派总播放量最高？', '{\"role\":\"user\",\"text\":\"哪个流派总播放量最高？\"}', 2, '2026-05-24 00:09:11');
INSERT INTO `chat2bi_message` VALUES ('4283aa33434d43778d6391a9c7f598ef', '2887ab895ff842ef8a294f7573da6f74', 'ai', '已理解你的问题，按下面的口径为你取数（可直接调整）：', '{\"role\":\"ai\",\"text\":\"已理解你的问题，按下面的口径为你取数（可直接调整）：\",\"dsl\":{\"metric\":\"保费\",\"dimension\":\"大区\",\"time\":\"全部\",\"filter\":\"\",\"limit\":100},\"data\":[[\"华东\",\"627,604\"],[\"华北\",\"622,788\"],[\"西南\",\"466,631\"],[\"华南\",\"391,663\"],[\"华中\",\"263,998\"],[\"西北\",\"233,384\"]],\"sql\":\"SELECT region, SUM(premium) AS premium_sum\\nFROM ins_policy\\nGROUP BY region\\nORDER BY 2 DESC\",\"insight\":\"「华东」的保费最高（627,604），约占总量的 24%。\",\"chart\":false,\"chartType\":\"柱状图\",\"followup\":false}', 1, '2026-05-24 21:12:56');
INSERT INTO `chat2bi_message` VALUES ('5100a672f4014c938bd4a950c88cec08', '332e10bc88b046be9b34186e07779a11', 'ai', '已理解你的问题，按下面的口径为你取数（可直接调整）：', '{\"role\": \"ai\", \"text\": \"已理解你的问题，按下面的口径为你取数（可直接调整）：\", \"dsl\": {\"metric\": \"用户数\", \"dimension\": \"部门\", \"time\": \"全部\", \"filter\": \"\"}, \"data\": [[\"北京国炬软件信息\", \"5\"], [\"项目研发部\", \"3\"], [\"投资控股集团有限公司\", \"2\"], [\"财务部\", \"1\"], [\"研发部\", \"1\"], [\"信息技术发展有限公司\", \"1\"]], \"sql\": \"SELECT d.depart_name AS 部门, COUNT(ud.user_id) AS 用户数\\nFROM sys_depart d\\nJOIN sys_user_depart ud ON ud.dep_id = d.id\\nGROUP BY d.id\\nORDER BY 2 DESC\", \"insight\": \"「北京国炬软件信息」的用户数最高（5），约占总量的 38%。\", \"chart\": false, \"chartType\": \"柱状图\", \"followup\": false}', 1, '2026-05-24 08:00:00');
INSERT INTO `chat2bi_message` VALUES ('54da3bdc0c32415496a7f2671a750885', '45938e194754426ba692ab50cde2d275', 'ai', '已理解你的问题，按下面的口径为你取数（可直接调整）：', '{\"role\":\"ai\",\"text\":\"已理解你的问题，按下面的口径为你取数（可直接调整）：\",\"dsl\":{\"metric\":\"用户数量\",\"dimension\":\"机构类别\",\"time\":\"全部\",\"filter\":\"\"},\"detail\":false,\"columns\":[\"机构类别\",\"用户数量\"],\"data\":[[\"1\",\"8\"],[\"\",\"6\"],[\"4\",\"3\"],[\"2\",\"2\"]],\"sql\":\"SELECT d.org_category AS \'机构类别\', COUNT(DISTINCT ud.user_id) AS \'用户数量\' FROM sys_user_depart ud LEFT JOIN sys_depart d ON ud.dep_id = d.id GROUP BY d.org_category ORDER BY COUNT(DISTINCT ud.user_id) DESC LIMIT 50\",\"insight\":\"「1」的用户数量最高（8），本次共返回 4 组数据。\",\"chart\":false,\"chartType\":\"柱状图\",\"followup\":false,\"_recomputing\":false,\"recomputed\":true}', 3, '2026-05-24 11:43:41');
INSERT INTO `chat2bi_message` VALUES ('63cc2f835a68473d98fbc8c72ceaa236', '45938e194754426ba692ab50cde2d275', 'ai', '已理解你的问题，为你查询到以下明细数据：', '{\"role\":\"ai\",\"text\":\"已理解你的问题，为你查询到以下明细数据：\",\"dsl\":{\"metric\":\"部门名称\",\"dimension\":\"用户ID\",\"time\":\"全部\",\"filter\":\"\"},\"detail\":true,\"columns\":[\"用户ID\",\"登录账号\",\"真实姓名\",\"性别\",\"状态\",\"工号\",\"邮箱\",\"手机号\",\"用户机构编码\",\"创建时间\",\"部门名称\"],\"data\":[[\"2052653612026327042\",\"zhangsan\",\"张三\",\"\",\"1\",\"\",\"\",\"13800138000\",\"\",\"2026-05-08T15:35:32\",\"\"],[\"2039670497696014337\",\"temp_013\",\"林燕\",\"2\",\"1\",\"T0013\",\"temp13@test.com\",\"13810000012\",\"\",\"2026-04-02T19:45:17\",\"\"],[\"2039670498023170049\",\"temp_014\",\"何勇\",\"1\",\"1\",\"T0014\",\"temp14@test.com\",\"13810000013\",\"\",\"2026-04-02T19:45:17\",\"\"],[\"2039670498476154882\",\"temp_015\",\"徐梅\",\"2\",\"1\",\"T0015\",\"temp15@test.com\",\"13810000014\",\"\",\"2026-04-02T19:45:17\",\"\"],[\"2039670498933334018\",\"temp_016\",\"马杰\",\"2\",\"1\",\"T0016\",\"temp16@test.com\",\"13810000015\",\"\",\"2026-04-02T19:45:17\",\"\"],[\"2039670499398901761\",\"temp_017\",\"高雪\",\"1\",\"1\",\"T0017\",\"temp17@test.com\",\"13810000016\",\"\",\"2026-04-02T19:45:17\",\"\"],[\"2039670499780583426\",\"temp_018\",\"梁超\",\"2\",\"1\",\"T0018\",\"temp18@test.com\",\"13810000017\",\"\",\"2026-04-02T19:45:17\",\"\"],[\"2039670500107739138\",\"temp_019\",\"罗婷\",\"1\",\"1\",\"T0019\",\"temp19@test.com\",\"13810000018\",\"\",\"2026-04-02T19:45:17\",\"\"],[\"2039670500367785986\",\"temp_020\",\"宋刚\",\"1\",\"1\",\"T0020\",\"temp20@test.com\",\"13810000019\",\"\",\"2026-04-02T19:45:17\",\"\"],[\"2039670493526876162\",\"temp_002\",\"李娜\",\"2\",\"1\",\"T0002\",\"temp02@test.com\",\"13810000001\",\"\",\"2026-04-02T19:45:16\",\"\"],[\"2039670493916946434\",\"temp_003\",\"王强\",\"1\",\"1\",\"T0003\",\"temp03@test.com\",\"13810000002\",\"\",\"2026-04-02T19:45:16\",\"\"],[\"2039670494176993282\",\"temp_004\",\"赵丽\",\"1\",\"1\",\"T0004\",\"temp04@test.com\",\"13810000003\",\"\",\"2026-04-02T19:45:16\",\"\"],[\"2039670494634172417\",\"temp_005\",\"陈伟\",\"2\",\"1\",\"T0005\",\"temp05@test.com\",\"13810000004\",\"\",\"2026-04-02T19:45:16\",\"\"],[\"2039670495082962946\",\"temp_006\",\"刘洋\",\"2\",\"1\",\"T0006\",\"temp06@test.com\",\"13810000005\",\"\",\"2026-04-02T19:45:16\",\"\"],[\"2039670495477227522\",\"temp_007\",\"周敏\",\"1\",\"1\",\"T0007\",\"temp07@test.com\",\"13810000006\",\"\",\"2026-04-02T19:45:16\",\"\"],[\"2039670495875686401\",\"temp_008\",\"吴芳\",\"2\",\"1\",\"T0008\",\"temp08@test.com\",\"13810000007\",\"\",\"2026-04-02T19:45:16\",\"\"],[\"2039670496198647810\",\"temp_009\",\"郑磊\",\"1\",\"1\",\"T0009\",\"temp09@test.com\",\"13810000008\",\"\",\"2026-04-02T19:45:16\",\"\"],[\"2039670496592912385\",\"temp_010\",\"孙华\",\"1\",\"1\",\"T0010\",\"temp10@test.com\",\"13810000009\",\"\",\"2026-04-02T19:45:16\",\"\"],[\"2039670496987176961\",\"temp_011\",\"朱静\",\"2\",\"1\",\"T0011\",\"temp11@test.com\",\"13810000010\",\"\",\"2026-04-02T19:45:16\",\"\"],[\"2039670497373052929\",\"temp_012\",\"胡涛\",\"1\",\"1\",\"T0012\",\"temp12@test.com\",\"13810000011\",\"\",\"2026-04-02T19:45:16\",\"\"],[\"2039670403072516098\",\"temp_001\",\"张明\",\"1\",\"1\",\"T0001\",\"temp01@test.com\",\"13810000000\",\"\",\"2026-04-02T19:44:54\",\"\"],[\"2029522765715570689\",\"ai123\",\"张三\",\"\",\"1\",\"\",\"\",\"18611522656\",\"\",\"2026-03-05T19:41:49\",\"\"],[\"2003062533853843458\",\"lisi\",\"李四\",\"\",\"1\",\"\",\"\",\"18611111110\",\"\",\"2025-12-22T19:18:18\",\"\"],[\"1985289937397248002\",\"zhangli\",\"张丽\",\"2\",\"1\",\"\",\"\",\"18764100073\",\"\",\"2025-11-03T18:16:21\",\"\"],[\"1287687492661800962\",\"test\",\"测试用户\",\"2\",\"1\",\"004\",\"1611@1.com\",\"18511111111\",\"A05A01\",\"2020-07-27T17:53:17\",\"投资控股集团有限公司\"],[\"1227492235802251265\",\"_reserve_user_external\",\"预留非登录用户\",\"0\",\"1\",\"_reserve_user_external\",\"\",\"\",\"\",\"2020-02-12T15:18:49\",\"\"],[\"1179675874431131649\",\"qinfeng\",\"秦风\",\"1\",\"1\",\"20011\",\"qinfeng@jeecg.org\",\"13411111111\",\"A05A01\",\"2019-10-03T16:33:40\",\"投资控股集团有限公司\"],[\"e9ca23d68d884d4ebb19d07889727dae\",\"admin\",\"管理员\",\"1\",\"1\",\"A0001\",\"zhangdaiscott@163.com\",\"18611111111\",\"A01A05\",\"2019-06-21T17:54:10\",\"信息技术发展有限公司\"],[\"e9ca23d68d884d4ebb19d07889727dae\",\"admin\",\"管理员\",\"1\",\"1\",\"A0001\",\"zhangdaiscott@163.com\",\"18611111111\",\"A01A05\",\"2019-06-21T17:54:10\",\"研发部\"],[\"a75d45a015c44384a04449ee80dc3503\",\"jeecg\",\"JEECG演示\",\"1\",\"1\",\"A002\",\"jeecgos@163.com\",\"18611788525\",\"A01\",\"2019-02-13T16:02:36\",\"北京国炬软件信息\"]],\"sql\":\"SELECT \\n    u.id AS 用户ID,\\n    u.username AS 登录账号,\\n    u.realname AS 真实姓名,\\n    u.sex AS 性别,\\n    u.status AS 状态,\\n    u.work_no AS 工号,\\n    u.email AS 邮箱,\\n    u.phone AS 手机号,\\n    u.org_code AS 用户机构编码,\\n    u.create_time AS 创建时间,\\n    d.depart_name AS 部门名称\\nFROM sys_user u\\nLEFT JOIN sys_user_depart ud ON u.id = ud.user_id\\nLEFT JOIN sys_depart d ON ud.dep_id = d.id\\nORDER BY u.create_time DESC\\nLIMIT 50\",\"insight\":\"共查询到 30 条记录，11 个字段。\",\"chart\":false,\"chartType\":\"柱状图\",\"followup\":false}', 12, '2026-05-24 11:43:41');
INSERT INTO `chat2bi_message` VALUES ('6dfa327efcf846448e0f7a68f0d2dd7b', '2887ab895ff842ef8a294f7573da6f74', 'user', '查询白金客户的明细', '{\"role\":\"user\",\"text\":\"查询白金客户的明细\"}', 4, '2026-05-24 21:12:56');
INSERT INTO `chat2bi_message` VALUES ('78a54cfbee244b638bfc03b3d0cf9003', '2887ab895ff842ef8a294f7573da6f74', 'ai', '已理解你的问题，按下面的口径为你取数（可直接调整）：', '{\"role\":\"ai\",\"text\":\"已理解你的问题，按下面的口径为你取数（可直接调整）：\",\"dsl\":{\"metric\":\"保费总额\",\"dimension\":\"险种类型\",\"time\":\"全部\",\"filter\":\"\"},\"detail\":false,\"columns\":[\"险种类型\",\"保费总额\"],\"data\":[[\"财产险\",\"624703.92\"],[\"意外险\",\"530727.44\"],[\"寿险\",\"504139.35\"],[\"健康险\",\"488370.47\"],[\"车险\",\"458129.73\"]],\"sql\":\"SELECT product_type AS 险种类型, SUM(premium) AS 保费总额 FROM ins_policy GROUP BY product_type ORDER BY 保费总额 DESC LIMIT 50\",\"insight\":\"「财产险」的保费总额最高（624703.92），本次共返回 5 组数据。\",\"chart\":false,\"chartType\":\"柱状图\",\"followup\":false}', 7, '2026-05-24 21:12:56');
INSERT INTO `chat2bi_message` VALUES ('7c2793d4b5b14adfabd646fa6c42a26f', 'c6bca8f52ee548f48e7a7facdade7cb5', 'ai', '已理解你的问题，按下面的口径为你取数（可直接调整）：', '{\"role\": \"ai\", \"text\": \"已理解你的问题，按下面的口径为你取数（可直接调整）：\", \"dsl\": {\"metric\": \"新增用户数\", \"dimension\": \"月份\", \"time\": \"全部\", \"filter\": \"\"}, \"data\": [[\"2019-02\", \"1\"], [\"2019-06\", \"1\"], [\"2019-10\", \"1\"], [\"2020-02\", \"1\"], [\"2020-07\", \"1\"], [\"2025-11\", \"1\"], [\"2025-12\", \"1\"], [\"2026-03\", \"1\"], [\"2026-04\", \"20\"], [\"2026-05\", \"1\"]], \"sql\": \"SELECT DATE_FORMAT(create_time,\'%Y-%m\') AS 月份, COUNT(*) AS 新增用户数\\nFROM sys_user\\nWHERE create_time IS NOT NULL\\nGROUP BY 月份\\nORDER BY 月份\", \"insight\": \"「2019-02」的新增用户数最高（1），约占总量的 3%。\", \"chart\": false, \"chartType\": \"折线图\", \"followup\": false}', 1, '2026-05-24 09:00:00');
INSERT INTO `chat2bi_message` VALUES ('7d4ccd0aa1f743df9aef2ac5bd5c5559', '45938e194754426ba692ab50cde2d275', 'user', '用户性别分布', '{\"role\":\"user\",\"text\":\"用户性别分布\"}', 0, '2026-05-24 11:43:41');
INSERT INTO `chat2bi_message` VALUES ('7f5322febbea45b69f0cad9b76fafd03', 'bc75e772eafe43639e4e8f1698e51543', 'ai', '已理解你的问题，按下面的口径为你取数（可直接调整）：', '{\"role\": \"ai\", \"text\": \"已理解你的问题，按下面的口径为你取数（可直接调整）：\", \"dsl\": {\"metric\": \"保费\", \"dimension\": \"险种类型\", \"time\": \"全部\", \"filter\": \"\", \"limit\": 100}, \"data\": [[\"财产险\", \"624,703\"], [\"意外险\", \"530,727\"], [\"寿险\", \"504,139\"], [\"健康险\", \"488,370\"], [\"车险\", \"458,129\"]], \"sql\": \"SELECT product_type, SUM(premium) AS premium_sum\\nFROM ins_policy\\nGROUP BY product_type\\nORDER BY 2 DESC\", \"insight\": \"「财产险」的保费最高（624,703），约占总量的 24%。\", \"chart\": false, \"chartType\": \"柱状图\", \"followup\": false}', 1, '2026-05-24 06:00:00');
INSERT INTO `chat2bi_message` VALUES ('80021bddef6549f59be7c679944ccf13', 'da121e06d85d4829b0ec1f2fcb5c80f6', 'ai', '已理解你的问题，按下面的口径为你取数（可直接调整）：', '{\"role\":\"ai\",\"text\":\"已理解你的问题，按下面的口径为你取数（可直接调整）：\",\"dsl\":{\"metric\":\"播放量\",\"dimension\":\"流派\",\"time\":\"近10天\",\"filter\":\"\",\"limit\":100},\"data\":[[\"流行\",\"4,201,500\"],[\"摇滚\",\"2,402,330\"],[\"民谣\",\"1,890,120\"],[\"电子\",\"940,210\"],[\"古典\",\"612,540\"],[\"嘻哈\",\"588,330\"],[\"爵士\",\"421,770\"],[\"乡村\",\"305,210\"]],\"sql\":\"SELECT genre, SUM(js_play_cnt) AS js_play_cnt_sum\\nFROM singer\\nGROUP BY genre\\nORDER BY 2 DESC\\nLIMIT 100\",\"insight\":\"「流行」的播放量最高（4,201,500），约占总量的 38%；近10天环比 +9.2%，主要由流行品类带动。\",\"chart\":false,\"chartType\":\"柱状图\",\"followup\":true}', 3, '2026-05-24 00:09:11');
INSERT INTO `chat2bi_message` VALUES ('8448f548619e416986caa906075bc7a0', '2887ab895ff842ef8a294f7573da6f74', 'user', '各险种的保费总额', '{\"role\":\"user\",\"text\":\"各险种的保费总额\"}', 6, '2026-05-24 21:12:56');
INSERT INTO `chat2bi_message` VALUES ('8eafaa50042b49beb6b3186551f7c8ac', '45938e194754426ba692ab50cde2d275', 'user', '各机构类别的部门数量', '{\"role\":\"user\",\"text\":\"各机构类别的部门数量\"}', 6, '2026-05-24 11:43:41');
INSERT INTO `chat2bi_message` VALUES ('9cac484edfe44cff92e195ef15675654', '2887ab895ff842ef8a294f7573da6f74', 'user', '保费最高的10张保单明细', '{\"role\":\"user\",\"text\":\"保费最高的10张保单明细\"}', 2, '2026-05-24 21:12:56');
INSERT INTO `chat2bi_message` VALUES ('9d57013a95554b858ea5f660e13df2de', '45938e194754426ba692ab50cde2d275', 'user', '各部门的用户数量', '{\"role\":\"user\",\"text\":\"各部门的用户数量\"}', 7, '2026-05-24 11:43:41');
INSERT INTO `chat2bi_message` VALUES ('9e590ddf675c47349c6d0823ebb5bb7e', '70c175254bfd4d88a9fe42efc050a540', 'user', '各渠道的保单数量', '{\"role\": \"user\", \"text\": \"各渠道的保单数量\"}', 0, '2026-05-24 07:00:00');
INSERT INTO `chat2bi_message` VALUES ('b7a2382ebfa4424e8e46f54bc8832789', '45938e194754426ba692ab50cde2d275', 'ai', '已理解你的问题，按下面的口径为你取数（可直接调整）：', '{\"role\":\"ai\",\"text\":\"已理解你的问题，按下面的口径为你取数（可直接调整）：\",\"dsl\":{\"metric\":\"用户数量\",\"dimension\":\"部门名称\",\"time\":\"全部\",\"filter\":\"\"},\"detail\":false,\"columns\":[\"部门名称\",\"用户数量\"],\"data\":[[\"北京国炬软件信息\",\"5\"],[\"项目研发部\",\"3\"],[\"投资控股集团有限公司\",\"2\"],[\"信息技术发展有限公司\",\"1\"],[\"财务部\",\"1\"],[\"研发部\",\"1\"],[\"出纳岗\",\"0\"],[\"投资发展部\",\"0\"],[\"主任\",\"0\"],[\"总工程师\",\"0\"],[\"出纳\",\"0\"],[\"普通职员\",\"0\"],[\"项目部\",\"0\"],[\"研发二部(JEEWX)\",\"0\"],[\"低代码研发小组\",\"0\"],[\"领导班子\",\"0\"],[\"控股办公室主任\",\"0\"],[\"副部长\",\"0\"],[\"综合管理部\",\"0\"],[\"副部长\",\"0\"],[\"44\",\"0\"],[\"财务\",\"0\"],[\"项目核算部\",\"0\"],[\"控股集团\",\"0\"],[\"党委书记\",\"0\"],[\"副主任\",\"0\"],[\"副总经理\",\"0\"],[\"总工程师\",\"0\"],[\"软件工程师\",\"0\"],[\"架构部\",\"0\"],[\"财务总监\",\"0\"],[\"财务管理中心\",\"0\"],[\"职员\",\"0\"],[\"领导班子\",\"0\"],[\"软件研发部\",\"0\"],[\"部长\",\"0\"],[\"董事长\",\"0\"],[\"城市运营管理集团有限公司\",\"0\"],[\"纪委书记\",\"0\"],[\"部长\",\"0\"],[\"领导班子\",\"0\"],[\"副总经理\",\"0\"],[\"333\",\"0\"],[\"研发一部(JEECG)\",\"0\"],[\"产品部门\",\"0\"],[\"大连油田\",\"0\"],[\"董事长\",\"0\"],[\"控股副总经理\",\"0\"],[\"董事长\",\"0\"],[\"办公室\",\"0\"]],\"sql\":\"SELECT d.depart_name AS 部门名称, COUNT(ud.user_id) AS 用户数量\\nFROM sys_depart d\\nLEFT JOIN sys_user_depart ud ON d.id = ud.dep_id\\nGROUP BY d.id, d.depart_name\\nORDER BY 用户数量 DESC\\nLIMIT 50\",\"insight\":\"「北京国炬软件信息」的用户数量最高（5），本次共返回 50 组数据。\",\"chart\":true,\"chartType\":\"折线图\",\"followup\":false}', 8, '2026-05-24 11:43:41');
INSERT INTO `chat2bi_message` VALUES ('bf7760d1a30844a189a3214b51018deb', '45938e194754426ba692ab50cde2d275', 'ai', '已理解你的问题，按下面的口径为你取数（可直接调整）：', '{\"role\":\"ai\",\"text\":\"已理解你的问题，按下面的口径为你取数（可直接调整）：\",\"dsl\":{\"metric\":\"新增用户数\",\"dimension\":\"月份\",\"time\":\"全部\",\"filter\":\"\"},\"detail\":false,\"columns\":[\"月份\",\"新增用户数\"],\"data\":[[\"2026-05\",\"1\"],[\"2026-04\",\"20\"],[\"2026-03\",\"1\"],[\"2025-12\",\"1\"],[\"2025-11\",\"1\"],[\"2020-07\",\"1\"],[\"2020-02\",\"1\"],[\"2019-10\",\"1\"],[\"2019-06\",\"1\"],[\"2019-02\",\"1\"]],\"sql\":\"SELECT DATE_FORMAT(create_time, \'%Y-%m\') AS 月份, COUNT(*) AS 新增用户数 FROM sys_user GROUP BY DATE_FORMAT(create_time, \'%Y-%m\') ORDER BY 月份 DESC LIMIT 50\",\"insight\":\"「2026-05」的新增用户数最高（1），本次共返回 10 组数据。\",\"chart\":false,\"chartType\":\"折线图\",\"followup\":false}', 10, '2026-05-24 11:43:41');
INSERT INTO `chat2bi_message` VALUES ('c3a0361ea75847ff8f3024a2bddd535e', '77718e1573e74f43ac326c99e608c611', 'user', '下载量前10歌手', '{\"role\":\"user\",\"text\":\"下载量前10歌手\"}', 0, '2026-05-24 00:01:00');
INSERT INTO `chat2bi_message` VALUES ('ce0086742db3401c9dfc7a40d7be96e4', '70c175254bfd4d88a9fe42efc050a540', 'ai', '已理解你的问题，按下面的口径为你取数（可直接调整）：', '{\"role\": \"ai\", \"text\": \"已理解你的问题，按下面的口径为你取数（可直接调整）：\", \"dsl\": {\"metric\": \"保单数\", \"dimension\": \"销售渠道\", \"time\": \"全部\", \"filter\": \"\", \"limit\": 100}, \"data\": [[\"银保\", \"68\"], [\"代理人\", \"67\"], [\"电销\", \"62\"], [\"线上直销\", \"53\"], [\"经纪\", \"50\"]], \"sql\": \"SELECT channel, COUNT(*) AS policy_cnt\\nFROM ins_policy\\nGROUP BY channel\\nORDER BY 2 DESC\", \"insight\": \"「银保」的保单数最高（68），约占总量的 23%。\", \"chart\": false, \"chartType\": \"柱状图\", \"followup\": false}', 1, '2026-05-24 07:00:00');
INSERT INTO `chat2bi_message` VALUES ('cff85cfffcdf42b1be74aeba3b4348bd', 'da121e06d85d4829b0ec1f2fcb5c80f6', 'ai', '已理解你的问题，按下面的口径为你取数（可直接调整）：', '{\"role\":\"ai\",\"text\":\"已理解你的问题，按下面的口径为你取数（可直接调整）：\",\"dsl\":{\"metric\":\"播放量\",\"dimension\":\"流派\",\"time\":\"近10天\",\"filter\":\"\",\"limit\":100},\"data\":[[\"流行\",\"4,201,500\"],[\"摇滚\",\"2,402,330\"],[\"民谣\",\"1,890,120\"],[\"电子\",\"940,210\"],[\"古典\",\"612,540\"],[\"嘻哈\",\"588,330\"],[\"爵士\",\"421,770\"],[\"乡村\",\"305,210\"]],\"sql\":\"SELECT genre, SUM(js_play_cnt) AS js_play_cnt_sum\\nFROM singer\\nGROUP BY genre\\nORDER BY 2 DESC\\nLIMIT 100\",\"insight\":\"「流行」的播放量最高（4,201,500），约占总量的 38%；近10天环比 +9.2%，主要由流行品类带动。\",\"chart\":false,\"chartType\":\"柱状图\",\"followup\":false}', 1, '2026-05-24 00:09:11');
INSERT INTO `chat2bi_message` VALUES ('d55d8cae334d49f4a6d40d5e6481e034', '2887ab895ff842ef8a294f7573da6f74', 'ai', '已理解你的问题，为你查询到以下明细数据：', '{\"role\":\"ai\",\"text\":\"已理解你的问题，为你查询到以下明细数据：\",\"dsl\":{\"metric\":\"投保日期\",\"dimension\":\"客户姓名\",\"time\":\"全部\",\"filter\":\"\"},\"detail\":true,\"columns\":[\"客户姓名\",\"客户等级\",\"省份\",\"保单号\",\"险种名称\",\"年保费\",\"投保日期\"],\"data\":[[\"任婷嘉怡\",\"白金\",\"广东省\",\"P200003\",\"企业财产基本险\",\"15416.63\",\"2025-08-25\"],[\"蔡杰\",\"白金\",\"浙江省\",\"P200004\",\"货运物流险\",\"796.51\",\"2025-04-24\"],[\"姚敏\",\"白金\",\"天津市\",\"P200005\",\"交强险\",\"1134.32\",\"2023-01-10\"],[\"王明\",\"白金\",\"浙江省\",\"P200011\",\"交通意外险\",\"12686.54\",\"2025-02-24\"],[\"丁军\",\"白金\",\"山东省\",\"P200015\",\"综合意外险\",\"17508.37\",\"2023-12-26\"],[\"何娜雪\",\"白金\",\"天津市\",\"P200016\",\"成人意外伤害险\",\"3017.16\",\"2026-05-05\"],[\"丁军\",\"白金\",\"山东省\",\"P200017\",\"交强险\",\"8645.17\",\"2025-09-28\"],[\"胡涛\",\"白金\",\"云南省\",\"P200023\",\"综合意外险\",\"1482.86\",\"2023-10-06\"],[\"程宇\",\"白金\",\"山西省\",\"P200025\",\"交通意外险\",\"16818.51\",\"2025-04-13\"],[\"蔡杰\",\"白金\",\"浙江省\",\"P200027\",\"增额终身寿2026\",\"978.66\",\"2024-08-19\"],[\"王明\",\"白金\",\"浙江省\",\"P200028\",\"第三者责任险\",\"17304.52\",\"2025-01-13\"],[\"陈鑫\",\"白金\",\"四川省\",\"P200037\",\"增额终身寿2026\",\"2827.86\",\"2023-06-07\"],[\"韩婷\",\"白金\",\"河北省\",\"P200041\",\"百万守护意外险\",\"8734.15\",\"2023-08-14\"],[\"王明\",\"白金\",\"浙江省\",\"P200042\",\"国寿福满一生\",\"13179.69\",\"2023-01-29\"],[\"胡艳刚\",\"白金\",\"云南省\",\"P200043\",\"交强险\",\"9091.53\",\"2025-09-30\"],[\"魏子轩\",\"白金\",\"广东省\",\"P200050\",\"重疾无忧A款\",\"5384.67\",\"2025-04-29\"],[\"魏子轩\",\"白金\",\"广东省\",\"P200057\",\"好医保长期医疗\",\"2947.94\",\"2025-07-23\"],[\"何欣怡\",\"白金\",\"云南省\",\"P200058\",\"成人意外伤害险\",\"2910.70\",\"2023-11-07\"],[\"魏子轩\",\"白金\",\"广东省\",\"P200063\",\"家庭财产综合险\",\"17084.42\",\"2024-10-25\"],[\"王明\",\"白金\",\"浙江省\",\"P200070\",\"第三者责任险\",\"1379.64\",\"2026-02-19\"],[\"马嘉怡\",\"白金\",\"江苏省\",\"P200072\",\"车损险尊享\",\"886.07\",\"2023-07-15\"],[\"袁晓东\",\"白金\",\"甘肃省\",\"P200073\",\"少儿门诊医疗\",\"4979.17\",\"2026-03-04\"],[\"陈鑫\",\"白金\",\"四川省\",\"P200076\",\"平安福终身寿险\",\"8845.93\",\"2024-05-05\"],[\"曹娜\",\"白金\",\"云南省\",\"P200079\",\"交通意外险\",\"9546.43\",\"2024-07-16\"],[\"陈鑫\",\"白金\",\"四川省\",\"P200082\",\"商业车险全保\",\"5030.62\",\"2024-09-03\"],[\"王明\",\"白金\",\"浙江省\",\"P200085\",\"百万守护意外险\",\"16004.35\",\"2024-04-26\"],[\"韩婷\",\"白金\",\"河北省\",\"P200087\",\"平安福终身寿险\",\"17318.00\",\"2025-01-09\"],[\"韩婷\",\"白金\",\"河北省\",\"P200090\",\"少儿门诊医疗\",\"8950.13\",\"2025-09-10\"],[\"徐婷\",\"白金\",\"湖北省\",\"P200094\",\"综合意外险\",\"5005.41\",\"2025-02-26\"],[\"马嘉怡\",\"白金\",\"江苏省\",\"P200097\",\"雇主责任险\",\"16183.15\",\"2023-07-26\"],[\"何欣怡\",\"白金\",\"云南省\",\"P200098\",\"少儿门诊医疗\",\"5211.02\",\"2023-10-10\"],[\"程娟\",\"白金\",\"湖北省\",\"P200100\",\"车损险尊享\",\"17200.88\",\"2025-03-28\"],[\"宋丽军\",\"白金\",\"山东省\",\"P200104\",\"好医保长期医疗\",\"7846.07\",\"2023-04-15\"],[\"梁思远\",\"白金\",\"山西省\",\"P200107\",\"e生保百万医疗\",\"16713.23\",\"2023-11-06\"],[\"蔡杰\",\"白金\",\"浙江省\",\"P200109\",\"雇主责任险\",\"13114.65\",\"2026-03-07\"],[\"梁思远\",\"白金\",\"山西省\",\"P200111\",\"商业车险全保\",\"313.79\",\"2024-10-06\"],[\"曹娜\",\"白金\",\"云南省\",\"P200116\",\"商业车险全保\",\"17997.47\",\"2024-01-25\"],[\"胡艳刚\",\"白金\",\"云南省\",\"P200119\",\"平安福终身寿险\",\"1432.27\",\"2025-04-12\"],[\"蔡杰\",\"白金\",\"浙江省\",\"P200124\",\"商业车险全保\",\"6524.77\",\"2024-02-28\"],[\"陈鑫\",\"白金\",\"四川省\",\"P200125\",\"增额终身寿2026\",\"6199.07\",\"2024-08-19\"],[\"姚敏\",\"白金\",\"天津市\",\"P200126\",\"成人意外伤害险\",\"6927.93\",\"2024-11-09\"],[\"梁思远\",\"白金\",\"山西省\",\"P200127\",\"国寿福满一生\",\"12654.53\",\"2025-09-19\"],[\"王明\",\"白金\",\"浙江省\",\"P200128\",\"百万守护意外险\",\"10862.82\",\"2023-03-17\"],[\"何娜雪\",\"白金\",\"天津市\",\"P200131\",\"车损险尊享\",\"13488.02\",\"2025-06-01\"],[\"宋丽军\",\"白金\",\"山东省\",\"P200133\",\"雇主责任险\",\"4666.80\",\"2023-02-02\"],[\"魏子轩\",\"白金\",\"广东省\",\"P200134\",\"e生保百万医疗\",\"9477.41\",\"2024-12-27\"],[\"徐婷\",\"白金\",\"湖北省\",\"P200136\",\"货运物流险\",\"2945.62\",\"2024-07-31\"],[\"胡艳刚\",\"白金\",\"云南省\",\"P200138\",\"成人意外伤害险\",\"5898.43\",\"2023-07-22\"],[\"姚敏\",\"白金\",\"天津市\",\"P200139\",\"交强险\",\"16750.52\",\"2024-12-22\"],[\"丁军\",\"白金\",\"山东省\",\"P200144\",\"e生保百万医疗\",\"600.07\",\"2023-05-12\"]],\"sql\":\"SELECT c.cust_name AS 客户姓名, c.cust_level AS 客户等级, c.province AS 省份, p.policy_no AS 保单号, p.product_name AS 险种名称, p.premium AS 年保费, p.sign_date AS 投保日期 FROM ins_customer c LEFT JOIN ins_policy p ON c.id = p.cust_id WHERE c.cust_level = \'白金\' LIMIT 50\",\"insight\":\"共查询到 50 条记录，7 个字段。\",\"chart\":false,\"chartType\":\"柱状图\",\"followup\":false,\"_sql\":true}', 5, '2026-05-24 21:12:56');
INSERT INTO `chat2bi_message` VALUES ('df82863b29354850941c20af6e6d2fd7', '2887ab895ff842ef8a294f7573da6f74', 'user', '各大区的保费排名', '{\"role\":\"user\",\"text\":\"各大区的保费排名\"}', 0, '2026-05-24 21:12:56');
INSERT INTO `chat2bi_message` VALUES ('e7f261eb3ca24c1eabdfd7e5309bd037', '3d83bf2875f34bb4aa4119a43d515b99', 'user', '已赔付理赔金额按险种汇总', '{\"role\": \"user\", \"text\": \"已赔付理赔金额按险种汇总\"}', 0, '2026-05-24 08:00:00');
INSERT INTO `chat2bi_message` VALUES ('ec1bdd8c2caf40ce9da715bf0a8685bf', 'da121e06d85d4829b0ec1f2fcb5c80f6', 'user', '哪个流派总播放量最高？', '{\"role\":\"user\",\"text\":\"哪个流派总播放量最高？\"}', 0, '2026-05-24 00:09:11');
INSERT INTO `chat2bi_message` VALUES ('ee98bd7998ec4203b34b10595cdfa958', '45938e194754426ba692ab50cde2d275', 'user', '按月份统计新增用户数', '{\"role\":\"user\",\"text\":\"按月份统计新增用户数\"}', 9, '2026-05-24 11:43:41');
INSERT INTO `chat2bi_message` VALUES ('fc551b7824c0430bb14e463698ff7d97', 'c6bca8f52ee548f48e7a7facdade7cb5', 'user', '按月份统计新增用户数', '{\"role\": \"user\", \"text\": \"按月份统计新增用户数\"}', 0, '2026-05-24 09:00:00');

-- ----------------------------
-- Table structure for chat2bi_table
-- ----------------------------
DROP TABLE IF EXISTS `chat2bi_table`;
CREATE TABLE `chat2bi_table`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `domain_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属数据域id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表名/英文标识',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '显示名称',
  `mode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型 table/数据库表,sql/SQL虚拟表',
  `sql_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'SQL虚拟表定义（mode=sql时）',
  `columns_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '列定义JSON [{col,type,agg,alias,enabled}]',
  `sort_no` int(11) NULL DEFAULT NULL COMMENT '排序号',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `tenant_id` int(11) NULL DEFAULT 0 COMMENT '多租户标识',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_c2b_table_domain`(`domain_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Chat2BI 注册表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat2bi_table
-- ----------------------------
INSERT INTO `chat2bi_table` VALUES ('2a882f23485844dbadef446659449653', 'c5c943945ba343a295f10d0ffb227b13', 'dd', 'dd', 'sql', 'SELECT count(*) c,DATE_FORMAT(create_time, \'%Y-%m-%d\') as date  from jmreport_big_screen GROUP BY date order by date desc', '[{\"col\":\"col1\",\"type\":\"dim\",\"agg\":\"—\",\"alias\":\"字段1\",\"enabled\":true},{\"col\":\"col2\",\"type\":\"metric\",\"agg\":\"SUM\",\"alias\":\"字段2\",\"enabled\":true}]', NULL, 'admin', '2026-05-23 22:30:20', 'admin', '2026-05-23 22:30:34', 0);
INSERT INTO `chat2bi_table` VALUES ('56b5a44298cc4dfa82d8594be16ff3b3', '6a01a7bce0174875a413f60e5bdec16b', 'ins_claim', '理赔', 'table', '', '[{\"col\": \"id\", \"type\": \"pk\", \"agg\": \"—\", \"alias\": \"理赔ID\", \"enabled\": true}, {\"col\": \"claim_no\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"理赔单号\", \"enabled\": true}, {\"col\": \"policy_id\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"保单ID\", \"enabled\": true}, {\"col\": \"policy_no\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"保单号\", \"enabled\": true}, {\"col\": \"cust_name\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"客户姓名\", \"enabled\": true}, {\"col\": \"product_type\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"险种类型\", \"enabled\": true}, {\"col\": \"report_date\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"报案日期\", \"enabled\": true}, {\"col\": \"claim_type\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"理赔类型（医疗费用/身故赔付/车辆损失/意外伤害/重疾给付）\", \"enabled\": true}, {\"col\": \"claim_amount\", \"type\": \"metric\", \"agg\": \"SUM\", \"alias\": \"申请理赔金额（元）\", \"enabled\": true}, {\"col\": \"settle_amount\", \"type\": \"metric\", \"agg\": \"SUM\", \"alias\": \"实际赔付金额（元）\", \"enabled\": true}, {\"col\": \"status\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"理赔状态（待审核/已赔付/已拒赔）\", \"enabled\": true}, {\"col\": \"province\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"出险省份\", \"enabled\": true}]', 2, 'admin', '2026-05-24 10:00:00', 'admin', '2026-05-24 10:00:00', 0);
INSERT INTO `chat2bi_table` VALUES ('6e67740eef90410ea898c08dc0159aad', '8422bb4067874cd5b9c8976962621104', 'sys_user_depart', '用户部门关联', 'table', '', '[{\"col\": \"ID\", \"type\": \"pk\", \"agg\": \"—\", \"alias\": \"关联ID\", \"enabled\": true}, {\"col\": \"user_id\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"用户ID\", \"enabled\": true}, {\"col\": \"dep_id\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"部门ID\", \"enabled\": true}]', 3, 'admin', '2026-05-24 11:00:00', 'admin', '2026-05-24 11:00:00', 0);
INSERT INTO `chat2bi_table` VALUES ('70b99ccd7def497592a7df9f333f42fe', '6a01a7bce0174875a413f60e5bdec16b', 'ins_customer', '客户', 'table', '', '[{\"col\": \"id\", \"type\": \"pk\", \"agg\": \"—\", \"alias\": \"客户ID\", \"enabled\": true}, {\"col\": \"cust_no\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"客户编号\", \"enabled\": true}, {\"col\": \"cust_name\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"客户姓名\", \"enabled\": true}, {\"col\": \"gender\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"性别（男/女）\", \"enabled\": true}, {\"col\": \"age\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"年龄\", \"enabled\": true}, {\"col\": \"province\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"省份\", \"enabled\": true}, {\"col\": \"city\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"城市\", \"enabled\": true}, {\"col\": \"region\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"大区（华东/华南/华北/华中/西南/西北）\", \"enabled\": true}, {\"col\": \"occupation\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"职业\", \"enabled\": true}, {\"col\": \"cust_level\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"客户等级（普通/银卡/金卡/白金）\", \"enabled\": true}, {\"col\": \"reg_date\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"注册日期\", \"enabled\": true}]', 3, 'admin', '2026-05-24 10:00:00', 'admin', '2026-05-24 10:00:00', 0);
INSERT INTO `chat2bi_table` VALUES ('a0d30daf82504bae9d9a2e18a45c4e4e', '6a01a7bce0174875a413f60e5bdec16b', 'v_ins_premium_trend', '月度保费趋势(虚拟SQL)', 'sql', 'SELECT DATE_FORMAT(sign_date, \'%Y-%m\') AS stat_month, product_type, COUNT(*) AS policy_cnt, SUM(premium) AS premium_total, SUM(coverage_amount) AS coverage_total FROM ins_policy GROUP BY stat_month, product_type ORDER BY stat_month', '[{\"col\": \"stat_month\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"统计月份\", \"enabled\": true}, {\"col\": \"product_type\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"险种类型\", \"enabled\": true}, {\"col\": \"policy_cnt\", \"type\": \"metric\", \"agg\": \"SUM\", \"alias\": \"保单数\", \"enabled\": true}, {\"col\": \"premium_total\", \"type\": \"metric\", \"agg\": \"SUM\", \"alias\": \"保费合计\", \"enabled\": true}, {\"col\": \"coverage_total\", \"type\": \"metric\", \"agg\": \"SUM\", \"alias\": \"保额合计\", \"enabled\": true}]', 4, 'admin', '2026-05-24 10:00:00', 'admin', '2026-05-24 10:00:00', 0);
INSERT INTO `chat2bi_table` VALUES ('d1c0fc67d9a64c19b36bd12bfcf22cdd', '8422bb4067874cd5b9c8976962621104', 'sys_depart', '部门', 'table', '', '[{\"col\": \"id\", \"type\": \"pk\", \"agg\": \"—\", \"alias\": \"部门ID\", \"enabled\": true}, {\"col\": \"depart_name\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"部门名称\", \"enabled\": true}, {\"col\": \"org_category\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"机构类别\", \"enabled\": true, \"dict\": \"org_category\"}, {\"col\": \"org_code\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"机构编码\", \"enabled\": true}, {\"col\": \"parent_id\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"上级部门ID\", \"enabled\": true}, {\"col\": \"status\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"状态\", \"enabled\": true}]', 2, 'admin', '2026-05-24 11:00:00', 'admin', '2026-05-24 11:00:00', 0);
INSERT INTO `chat2bi_table` VALUES ('e7a66f18e0a24068934ab3972ca2d718', '8422bb4067874cd5b9c8976962621104', 'sys_user', '用户', 'table', '', '[{\"col\": \"id\", \"type\": \"pk\", \"agg\": \"—\", \"alias\": \"用户ID\", \"enabled\": true}, {\"col\": \"username\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"登录账号\", \"enabled\": true}, {\"col\": \"realname\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"真实姓名\", \"enabled\": true}, {\"col\": \"sex\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"性别\", \"enabled\": true, \"dict\": \"sex\"}, {\"col\": \"status\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"状态\", \"enabled\": true, \"dict\": \"user_status\"}, {\"col\": \"work_no\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"工号\", \"enabled\": true}, {\"col\": \"email\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"邮箱\", \"enabled\": true}, {\"col\": \"phone\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"手机号\", \"enabled\": true}, {\"col\": \"org_code\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"机构编码\", \"enabled\": true}, {\"col\": \"create_time\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"创建时间\", \"enabled\": true}]', 1, 'admin', '2026-05-24 11:00:00', 'admin', '2026-05-24 11:00:00', 0);
INSERT INTO `chat2bi_table` VALUES ('f1dafa028e9a448c9fcc9880538036ab', 'c5c943945ba343a295f10d0ffb227b13', 'eoa_car_annual_inspection', 'eoa_car_annual_inspection', 'table', '', '[{\"agg\":\"—\",\"col\":\"id\",\"alias\":\"id\",\"type\":\"pk\",\"enabled\":true},{\"agg\":\"—\",\"col\":\"annual_inspection_no\",\"alias\":\"年检章号\",\"type\":\"dim\",\"enabled\":true},{\"agg\":\"—\",\"col\":\"annual_inspection_title\",\"alias\":\"标题\",\"type\":\"dim\",\"enabled\":true},{\"agg\":\"—\",\"col\":\"archive_id\",\"alias\":\"车辆档案id\",\"type\":\"dim\",\"enabled\":true},{\"agg\":\"—\",\"col\":\"registrant\",\"alias\":\"登记人\",\"type\":\"dim\",\"enabled\":true},{\"agg\":\"—\",\"col\":\"annual_inspection_date\",\"alias\":\"年审时间\",\"type\":\"dim\",\"enabled\":true},{\"agg\":\"SUM\",\"col\":\"annual_inspection_cost\",\"alias\":\"年审费用(元)\",\"type\":\"metric\",\"enabled\":true},{\"agg\":\"—\",\"col\":\"next_annual_inspection_date\",\"alias\":\"下次年审日期\",\"type\":\"dim\",\"enabled\":true},{\"agg\":\"SUM\",\"col\":\"iz_reminder\",\"alias\":\"是否提醒(0否 1 是)\",\"type\":\"metric\",\"enabled\":true},{\"agg\":\"SUM\",\"col\":\"reminder\",\"alias\":\"提前几天提醒\",\"type\":\"metric\",\"enabled\":true},{\"agg\":\"—\",\"col\":\"remarks\",\"alias\":\"描述\",\"type\":\"dim\",\"enabled\":true},{\"agg\":\"—\",\"col\":\"create_by\",\"alias\":\"创建人\",\"type\":\"dim\",\"enabled\":true},{\"agg\":\"—\",\"col\":\"create_time\",\"alias\":\"创建时间\",\"type\":\"dim\",\"enabled\":true},{\"agg\":\"—\",\"col\":\"update_by\",\"alias\":\"更新人\",\"type\":\"dim\",\"enabled\":true},{\"agg\":\"—\",\"col\":\"update_time\",\"alias\":\"更新时间\",\"type\":\"dim\",\"enabled\":true},{\"agg\":\"—\",\"col\":\"org_code\",\"alias\":\"机构编码\",\"type\":\"dim\",\"enabled\":true},{\"agg\":\"SUM\",\"col\":\"tenant_id\",\"alias\":\"租户ID\",\"type\":\"metric\",\"enabled\":true}]', NULL, 'admin', '2026-05-23 22:29:31', 'admin', '2026-05-23 22:30:41', 0);
INSERT INTO `chat2bi_table` VALUES ('f7c4d22abafa4ac982679e9b1fba3cc5', '6a01a7bce0174875a413f60e5bdec16b', 'ins_policy', '保单', 'table', '', '[{\"col\": \"id\", \"type\": \"pk\", \"agg\": \"—\", \"alias\": \"保单ID\", \"enabled\": true}, {\"col\": \"policy_no\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"保单号\", \"enabled\": true}, {\"col\": \"cust_id\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"客户ID\", \"enabled\": true}, {\"col\": \"cust_name\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"客户姓名\", \"enabled\": true}, {\"col\": \"product_name\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"险种名称\", \"enabled\": true}, {\"col\": \"product_type\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"险种类型（寿险/健康险/车险/意外险/财产险）\", \"enabled\": true}, {\"col\": \"channel\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"销售渠道（线上直销/代理人/银保/电销/经纪）\", \"enabled\": true}, {\"col\": \"premium\", \"type\": \"metric\", \"agg\": \"SUM\", \"alias\": \"年保费（元）\", \"enabled\": true}, {\"col\": \"coverage_amount\", \"type\": \"metric\", \"agg\": \"SUM\", \"alias\": \"保额（元）\", \"enabled\": true}, {\"col\": \"term_years\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"保障年限\", \"enabled\": true}, {\"col\": \"sign_date\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"投保日期\", \"enabled\": true}, {\"col\": \"effect_date\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"生效日期\", \"enabled\": true}, {\"col\": \"expire_date\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"到期日期\", \"enabled\": true}, {\"col\": \"status\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"保单状态（生效/已过期/退保）\", \"enabled\": true}, {\"col\": \"province\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"出单省份\", \"enabled\": true}, {\"col\": \"region\", \"type\": \"dim\", \"agg\": \"—\", \"alias\": \"大区\", \"enabled\": true}]', 1, 'admin', '2026-05-24 10:00:00', 'admin', '2026-05-24 10:00:00', 0);

-- ----------------------------
-- Table structure for chat2bi_topic
-- ----------------------------
DROP TABLE IF EXISTS `chat2bi_topic`;
CREATE TABLE `chat2bi_topic`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `domain_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属数据域id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主题名称',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态 published/已发布,draft/草稿',
  `fields_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '主题字段JSON [\"table.col\",...]',
  `sort_no` int(11) NULL DEFAULT NULL COMMENT '排序号',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `tenant_id` int(11) NULL DEFAULT 0 COMMENT '多租户标识',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_c2b_topic_domain`(`domain_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Chat2BI 查询主题' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat2bi_topic
-- ----------------------------
INSERT INTO `chat2bi_topic` VALUES ('16bcebfa98584dfea72ee2edefb0242f', '6a01a7bce0174875a413f60e5bdec16b', '保险核心指标', '保费、保额、理赔等核心经营指标', 'published', '[\"f7c4d22abafa4ac982679e9b1fba3cc5.product_type\", \"f7c4d22abafa4ac982679e9b1fba3cc5.channel\", \"f7c4d22abafa4ac982679e9b1fba3cc5.premium\", \"f7c4d22abafa4ac982679e9b1fba3cc5.coverage_amount\", \"56b5a44298cc4dfa82d8594be16ff3b3.claim_type\", \"56b5a44298cc4dfa82d8594be16ff3b3.settle_amount\", \"70b99ccd7def497592a7df9f333f42fe.cust_level\", \"70b99ccd7def497592a7df9f333f42fe.region\", \"a0d30daf82504bae9d9a2e18a45c4e4e.stat_month\", \"a0d30daf82504bae9d9a2e18a45c4e4e.product_type\", \"a0d30daf82504bae9d9a2e18a45c4e4e.policy_cnt\", \"a0d30daf82504bae9d9a2e18a45c4e4e.premium_total\", \"a0d30daf82504bae9d9a2e18a45c4e4e.coverage_total\"]', 1, 'admin', '2026-05-24 10:00:00', 'admin', '2026-05-24 10:00:00', 0);
INSERT INTO `chat2bi_topic` VALUES ('9f7d87761ff64c64b1469b3fd04fa0ab', '8422bb4067874cd5b9c8976962621104', '用户与组织', '用户、部门、归属的核心分析字段', 'published', '[\"e7a66f18e0a24068934ab3972ca2d718.realname\", \"e7a66f18e0a24068934ab3972ca2d718.sex\", \"e7a66f18e0a24068934ab3972ca2d718.status\", \"e7a66f18e0a24068934ab3972ca2d718.create_time\", \"e7a66f18e0a24068934ab3972ca2d718.org_code\", \"d1c0fc67d9a64c19b36bd12bfcf22cdd.depart_name\", \"d1c0fc67d9a64c19b36bd12bfcf22cdd.org_category\"]', 1, 'admin', '2026-05-24 11:00:00', 'admin', '2026-05-24 11:00:00', 0);
INSERT INTO `chat2bi_topic` VALUES ('d7c0137607e9469197f48238d26231c5', 'c5c943945ba343a295f10d0ffb227b13', 'dfdd', 'ddd', 'published', '[\"f1dafa028e9a448c9fcc9880538036ab.id\",\"f1dafa028e9a448c9fcc9880538036ab.annual_inspection_no\",\"2a882f23485844dbadef446659449653.col1\",\"f1dafa028e9a448c9fcc9880538036ab.create_by\",\"f1dafa028e9a448c9fcc9880538036ab.next_annual_inspection_date\"]', NULL, 'admin', '2026-05-23 22:41:28', 'admin', '2026-05-23 22:41:31', 0);

-- ----------------------------
-- Table structure for ins_claim
-- ----------------------------
DROP TABLE IF EXISTS `ins_claim`;
CREATE TABLE `ins_claim`  (
  `id` bigint(20) NOT NULL COMMENT '理赔ID',
  `claim_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '理赔单号',
  `policy_id` bigint(20) NULL DEFAULT NULL COMMENT '保单ID',
  `policy_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '保单号',
  `cust_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户姓名',
  `product_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '险种类型',
  `report_date` date NULL DEFAULT NULL COMMENT '报案日期',
  `claim_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '理赔类型（医疗费用/身故赔付/车辆损失/意外伤害/重疾给付）',
  `claim_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '申请理赔金额（元）',
  `settle_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '实际赔付金额（元）',
  `status` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '理赔状态（待审核/已赔付/已拒赔）',
  `province` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出险省份',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '保险理赔表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ins_claim
-- ----------------------------
INSERT INTO `ins_claim` VALUES (300001, 'CL300001', 200001, 'P200001', '刘思远嘉怡', '意外险', '2025-03-22', '意外伤害', 9121.61, 7598.69, '已赔付', '福建省');
INSERT INTO `ins_claim` VALUES (300002, 'CL300002', 200005, 'P200005', '张玲敏', '车险', '2023-09-12', '车辆损失', 39109.18, 31142.19, '已赔付', '湖南省');
INSERT INTO `ins_claim` VALUES (300003, 'CL300003', 200008, 'P200008', '许辉', '财产险', '2025-07-04', '意外伤害', 20141.40, 0.00, '已拒赔', '河南省');
INSERT INTO `ins_claim` VALUES (300004, 'CL300004', 200011, 'P200011', '卢杰雪', '意外险', '2025-07-20', '意外伤害', 10316.25, 7529.04, '已赔付', '福建省');
INSERT INTO `ins_claim` VALUES (300005, 'CL300005', 200012, 'P200012', '徐嘉怡丽', '财产险', '2025-12-01', '车辆损失', 56392.92, 54439.18, '已赔付', '河北省');
INSERT INTO `ins_claim` VALUES (300006, 'CL300006', 200016, 'P200016', '肖嘉怡军', '意外险', '2026-05-06', '意外伤害', 78914.72, 51167.65, '已赔付', '上海市');
INSERT INTO `ins_claim` VALUES (300007, 'CL300007', 200026, 'P200026', '肖嘉怡', '财产险', '2023-10-12', '车辆损失', 55901.75, 42290.08, '已赔付', '江苏省');
INSERT INTO `ins_claim` VALUES (300008, 'CL300008', 200027, 'P200027', '叶磊涛', '寿险', '2026-02-15', '重疾给付', 13821.44, 0.00, '已拒赔', '上海市');
INSERT INTO `ins_claim` VALUES (300009, 'CL300009', 200029, 'P200029', '任鑫', '健康险', '2025-08-05', '医疗费用', 33130.46, 0.00, '待审核', '山东省');
INSERT INTO `ins_claim` VALUES (300010, 'CL300010', 200030, 'P200030', '胡思远', '健康险', '2025-08-02', '医疗费用', 4858.40, 4385.80, '已赔付', '河南省');
INSERT INTO `ins_claim` VALUES (300011, 'CL300011', 200034, 'P200034', '许辉', '健康险', '2025-09-18', '重疾给付', 35101.08, 27517.01, '已赔付', '浙江省');
INSERT INTO `ins_claim` VALUES (300012, 'CL300012', 200035, 'P200035', '马晓东', '寿险', '2025-11-30', '身故赔付', 7044.32, 6892.69, '已赔付', '湖北省');
INSERT INTO `ins_claim` VALUES (300013, 'CL300013', 200037, 'P200037', '沈芳', '寿险', '2025-01-28', '重疾给付', 38265.21, 0.00, '已拒赔', '重庆市');
INSERT INTO `ins_claim` VALUES (300014, 'CL300014', 200038, 'P200038', '林艳', '意外险', '2026-02-22', '意外伤害', 64951.35, 48677.17, '已赔付', '湖南省');
INSERT INTO `ins_claim` VALUES (300015, 'CL300015', 200039, 'P200039', '朱辉思远', '健康险', '2024-10-21', '医疗费用', 30941.65, 27932.23, '已赔付', '山东省');
INSERT INTO `ins_claim` VALUES (300016, 'CL300016', 200043, 'P200043', '彭思远', '车险', '2026-01-14', '车辆损失', 9984.17, 8690.75, '已赔付', '湖北省');
INSERT INTO `ins_claim` VALUES (300017, 'CL300017', 200045, 'P200045', '曾娜', '车险', '2024-10-01', '车辆损失', 67957.09, 49747.43, '已赔付', '浙江省');
INSERT INTO `ins_claim` VALUES (300018, 'CL300018', 200050, 'P200050', '潘强', '健康险', '2026-05-18', '医疗费用', 9223.56, 8434.76, '已赔付', '甘肃省');
INSERT INTO `ins_claim` VALUES (300019, 'CL300019', 200051, 'P200051', '梁艳', '意外险', '2024-04-24', '医疗费用', 48381.95, 41909.43, '已赔付', '江苏省');
INSERT INTO `ins_claim` VALUES (300020, 'CL300020', 200052, 'P200052', '朱洋娜', '财产险', '2025-12-05', '意外伤害', 66738.47, 0.00, '待审核', '江苏省');
INSERT INTO `ins_claim` VALUES (300021, 'CL300021', 200055, 'P200055', '任霞', '财产险', '2026-03-01', '车辆损失', 1715.22, 0.00, '已拒赔', '广东省');
INSERT INTO `ins_claim` VALUES (300022, 'CL300022', 200056, 'P200056', '罗晓东', '寿险', '2024-07-06', '重疾给付', 6720.40, 4650.15, '已赔付', '重庆市');
INSERT INTO `ins_claim` VALUES (300023, 'CL300023', 200057, 'P200057', '吴芳', '健康险', '2026-01-26', '重疾给付', 32136.76, 19771.22, '已赔付', '陕西省');
INSERT INTO `ins_claim` VALUES (300024, 'CL300024', 200058, 'P200058', '潘丽', '意外险', '2024-07-07', '意外伤害', 4626.07, 3685.66, '已赔付', '江苏省');
INSERT INTO `ins_claim` VALUES (300025, 'CL300025', 200060, 'P200060', '罗欣怡', '寿险', '2025-06-16', '身故赔付', 52808.77, 35741.84, '已赔付', '云南省');
INSERT INTO `ins_claim` VALUES (300026, 'CL300026', 200061, 'P200061', '程晓东', '财产险', '2025-09-21', '意外伤害', 38357.65, 29112.25, '已赔付', '甘肃省');
INSERT INTO `ins_claim` VALUES (300027, 'CL300027', 200065, 'P200065', '徐鑫', '车险', '2026-01-05', '车辆损失', 72724.40, 68823.95, '已赔付', '河南省');
INSERT INTO `ins_claim` VALUES (300028, 'CL300028', 200066, 'P200066', '郭辉丽', '财产险', '2026-03-03', '车辆损失', 78560.02, 76452.23, '已赔付', '湖南省');
INSERT INTO `ins_claim` VALUES (300029, 'CL300029', 200067, 'P200067', '许伟', '寿险', '2025-03-30', '重疾给付', 56632.47, 0.00, '待审核', '四川省');
INSERT INTO `ins_claim` VALUES (300030, 'CL300030', 200069, 'P200069', '罗娟', '寿险', '2025-04-03', '重疾给付', 53478.92, 49289.43, '已赔付', '广东省');
INSERT INTO `ins_claim` VALUES (300031, 'CL300031', 200070, 'P200070', '郑伟艳', '车险', '2026-03-30', '车辆损失', 76407.53, 46678.09, '已赔付', '福建省');
INSERT INTO `ins_claim` VALUES (300032, 'CL300032', 200071, 'P200071', '曾洋', '意外险', '2026-03-13', '医疗费用', 57917.97, 0.00, '已拒赔', '湖北省');
INSERT INTO `ins_claim` VALUES (300033, 'CL300033', 200073, 'P200073', '吕强静', '健康险', '2026-03-30', '医疗费用', 32260.84, 20430.89, '已赔付', '河北省');
INSERT INTO `ins_claim` VALUES (300034, 'CL300034', 200077, 'P200077', '唐明', '健康险', '2024-10-19', '医疗费用', 16613.78, 10376.95, '已赔付', '甘肃省');
INSERT INTO `ins_claim` VALUES (300035, 'CL300035', 200079, 'P200079', '韩子轩', '意外险', '2024-12-08', '医疗费用', 42102.45, 0.00, '已拒赔', '北京市');
INSERT INTO `ins_claim` VALUES (300036, 'CL300036', 200080, 'P200080', '潘浩然', '财产险', '2026-05-20', '车辆损失', 41160.52, 32863.04, '已赔付', '海南省');
INSERT INTO `ins_claim` VALUES (300037, 'CL300037', 200082, 'P200082', '董桂英', '车险', '2025-08-19', '车辆损失', 3722.41, 2746.27, '已赔付', '河南省');
INSERT INTO `ins_claim` VALUES (300038, 'CL300038', 200086, 'P200086', '郭晓东欣怡', '意外险', '2025-10-19', '意外伤害', 75042.25, 58266.55, '已赔付', '河北省');
INSERT INTO `ins_claim` VALUES (300039, 'CL300039', 200088, 'P200088', '田子轩', '寿险', '2025-02-17', '重疾给付', 58507.89, 51647.64, '已赔付', '浙江省');
INSERT INTO `ins_claim` VALUES (300040, 'CL300040', 200090, 'P200090', '赵宇', '健康险', '2025-12-02', '医疗费用', 9995.71, 6580.31, '已赔付', '重庆市');
INSERT INTO `ins_claim` VALUES (300041, 'CL300041', 200093, 'P200093', '肖超', '健康险', '2024-01-11', '重疾给付', 55660.49, 55348.59, '已赔付', '广东省');
INSERT INTO `ins_claim` VALUES (300042, 'CL300042', 200099, 'P200099', '于军', '车险', '2025-08-12', '车辆损失', 54611.29, 0.00, '已拒赔', '广东省');
INSERT INTO `ins_claim` VALUES (300043, 'CL300043', 200101, 'P200101', '罗强', '财产险', '2026-05-05', '车辆损失', 20267.43, 15760.49, '已赔付', '北京市');
INSERT INTO `ins_claim` VALUES (300044, 'CL300044', 200103, 'P200103', '田宇', '寿险', '2024-11-19', '重疾给付', 10812.71, 0.00, '已拒赔', '山东省');
INSERT INTO `ins_claim` VALUES (300045, 'CL300045', 200104, 'P200104', '周娟', '健康险', '2024-10-03', '重疾给付', 11991.61, 7499.52, '已赔付', '湖北省');
INSERT INTO `ins_claim` VALUES (300046, 'CL300046', 200106, 'P200106', '徐勇', '健康险', '2026-04-15', '医疗费用', 38850.24, 32064.79, '已赔付', '四川省');
INSERT INTO `ins_claim` VALUES (300047, 'CL300047', 200111, 'P200111', '吕娟艳', '车险', '2026-03-19', '车辆损失', 4536.05, 3712.06, '已赔付', '广东省');
INSERT INTO `ins_claim` VALUES (300048, 'CL300048', 200114, 'P200114', '赵勇', '财产险', '2026-02-13', '意外伤害', 23953.99, 0.00, '已拒赔', '上海市');
INSERT INTO `ins_claim` VALUES (300049, 'CL300049', 200117, 'P200117', '赵芳', '寿险', '2025-09-12', '身故赔付', 38324.17, 28756.26, '已赔付', '江苏省');
INSERT INTO `ins_claim` VALUES (300050, 'CL300050', 200118, 'P200118', '卢鑫婷', '财产险', '2024-04-14', '意外伤害', 14949.57, 9364.53, '已赔付', '山西省');
INSERT INTO `ins_claim` VALUES (300051, 'CL300051', 200123, 'P200123', '梁娜', '财产险', '2024-04-05', '车辆损失', 22151.15, 0.00, '待审核', '浙江省');
INSERT INTO `ins_claim` VALUES (300052, 'CL300052', 200124, 'P200124', '程玲桂英', '车险', '2024-10-23', '车辆损失', 61436.50, 0.00, '待审核', '江苏省');
INSERT INTO `ins_claim` VALUES (300053, 'CL300053', 200126, 'P200126', '赵军宇', '意外险', '2025-02-24', '意外伤害', 30153.55, 25770.95, '已赔付', '广东省');
INSERT INTO `ins_claim` VALUES (300054, 'CL300054', 200127, 'P200127', '胡静', '寿险', '2026-02-20', '重疾给付', 17586.42, 0.00, '已拒赔', '福建省');
INSERT INTO `ins_claim` VALUES (300055, 'CL300055', 200128, 'P200128', '孙磊', '意外险', '2024-01-25', '意外伤害', 48317.85, 41417.96, '已赔付', '江苏省');
INSERT INTO `ins_claim` VALUES (300056, 'CL300056', 200131, 'P200131', '朱玲鑫', '车险', '2025-07-09', '车辆损失', 34504.90, 30733.74, '已赔付', '重庆市');
INSERT INTO `ins_claim` VALUES (300057, 'CL300057', 200132, 'P200132', '杨玲', '健康险', '2025-09-13', '重疾给付', 28973.30, 17501.14, '已赔付', '上海市');
INSERT INTO `ins_claim` VALUES (300058, 'CL300058', 200133, 'P200133', '宋桂英晓东', '财产险', '2023-11-15', '车辆损失', 3032.03, 0.00, '待审核', '四川省');
INSERT INTO `ins_claim` VALUES (300059, 'CL300059', 200134, 'P200134', '周鑫', '健康险', '2026-04-01', '医疗费用', 66215.65, 53572.00, '已赔付', '湖南省');
INSERT INTO `ins_claim` VALUES (300060, 'CL300060', 200140, 'P200140', '肖桂英建华', '健康险', '2025-11-28', '医疗费用', 3595.43, 2515.52, '已赔付', '福建省');
INSERT INTO `ins_claim` VALUES (300061, 'CL300061', 200142, 'P200142', '马勇', '意外险', '2025-04-30', '意外伤害', 67311.83, 45156.59, '已赔付', '福建省');
INSERT INTO `ins_claim` VALUES (300062, 'CL300062', 200144, 'P200144', '田丽', '健康险', '2023-05-22', '重疾给付', 16527.50, 0.00, '待审核', '湖南省');
INSERT INTO `ins_claim` VALUES (300063, 'CL300063', 200145, 'P200145', '沈思远', '意外险', '2026-02-19', '医疗费用', 2922.28, 1908.61, '已赔付', '湖南省');
INSERT INTO `ins_claim` VALUES (300064, 'CL300064', 200150, 'P200150', '田丽', '意外险', '2025-09-28', '意外伤害', 30852.17, 19799.17, '已赔付', '甘肃省');
INSERT INTO `ins_claim` VALUES (300065, 'CL300065', 200152, 'P200152', '王超玲', '健康险', '2026-04-30', '医疗费用', 63051.77, 55052.77, '已赔付', '浙江省');
INSERT INTO `ins_claim` VALUES (300066, 'CL300066', 200153, 'P200153', '唐宇', '寿险', '2026-05-16', '身故赔付', 23065.18, 0.00, '已拒赔', '陕西省');
INSERT INTO `ins_claim` VALUES (300067, 'CL300067', 200155, 'P200155', '袁宇', '车险', '2026-01-23', '车辆损失', 67809.27, 56331.65, '已赔付', '海南省');
INSERT INTO `ins_claim` VALUES (300068, 'CL300068', 200158, 'P200158', '袁刚', '意外险', '2026-05-23', '医疗费用', 31965.33, 24070.04, '已赔付', '天津市');
INSERT INTO `ins_claim` VALUES (300069, 'CL300069', 200160, 'P200160', '李超明', '车险', '2025-11-05', '车辆损失', 13617.62, 0.00, '已拒赔', '云南省');
INSERT INTO `ins_claim` VALUES (300070, 'CL300070', 200162, 'P200162', '林宇', '意外险', '2026-03-04', '医疗费用', 43242.73, 32894.08, '已赔付', '甘肃省');
INSERT INTO `ins_claim` VALUES (300071, 'CL300071', 200163, 'P200163', '周娜', '财产险', '2026-05-15', '意外伤害', 9038.45, 0.00, '已拒赔', '上海市');
INSERT INTO `ins_claim` VALUES (300072, 'CL300072', 200167, 'P200167', '胡霞', '健康险', '2025-09-13', '重疾给付', 43461.92, 38502.13, '已赔付', '山西省');
INSERT INTO `ins_claim` VALUES (300073, 'CL300073', 200168, 'P200168', '丁鑫宇', '寿险', '2026-02-26', '身故赔付', 39817.79, 34066.33, '已赔付', '四川省');
INSERT INTO `ins_claim` VALUES (300074, 'CL300074', 200170, 'P200170', '田洋', '车险', '2026-05-05', '车辆损失', 35007.30, 0.00, '待审核', '湖北省');
INSERT INTO `ins_claim` VALUES (300075, 'CL300075', 200171, 'P200171', '苏芳浩然', '意外险', '2026-04-18', '意外伤害', 2525.33, 1572.46, '已赔付', '湖南省');
INSERT INTO `ins_claim` VALUES (300076, 'CL300076', 200173, 'P200173', '高欣怡嘉怡', '意外险', '2026-03-06', '医疗费用', 19050.32, 0.00, '已拒赔', '陕西省');
INSERT INTO `ins_claim` VALUES (300077, 'CL300077', 200174, 'P200174', '周强', '车险', '2025-05-09', '车辆损失', 29549.34, 27725.81, '已赔付', '山东省');
INSERT INTO `ins_claim` VALUES (300078, 'CL300078', 200176, 'P200176', '罗杰', '寿险', '2025-02-25', '身故赔付', 19308.56, 17814.06, '已赔付', '甘肃省');
INSERT INTO `ins_claim` VALUES (300079, 'CL300079', 200180, 'P200180', '邓欣怡杰', '财产险', '2024-11-26', '意外伤害', 67131.02, 41061.44, '已赔付', '陕西省');
INSERT INTO `ins_claim` VALUES (300080, 'CL300080', 200182, 'P200182', '冯敏晓东', '意外险', '2023-10-18', '意外伤害', 51408.47, 0.00, '已拒赔', '江苏省');
INSERT INTO `ins_claim` VALUES (300081, 'CL300081', 200183, 'P200183', '曹刚', '健康险', '2026-01-14', '重疾给付', 16343.23, 0.00, '已拒赔', '浙江省');
INSERT INTO `ins_claim` VALUES (300082, 'CL300082', 200184, 'P200184', '郑伟霞', '寿险', '2025-07-25', '重疾给付', 56320.68, 46088.09, '已赔付', '广东省');
INSERT INTO `ins_claim` VALUES (300083, 'CL300083', 200185, 'P200185', '魏洋', '健康险', '2025-09-03', '医疗费用', 40362.28, 0.00, '已拒赔', '陕西省');
INSERT INTO `ins_claim` VALUES (300084, 'CL300084', 200186, 'P200186', '冯辉', '财产险', '2026-05-07', '意外伤害', 50741.14, 36243.60, '已赔付', '山西省');
INSERT INTO `ins_claim` VALUES (300085, 'CL300085', 200191, 'P200191', '陈军', '车险', '2026-03-18', '车辆损失', 30597.37, 29253.23, '已赔付', '北京市');
INSERT INTO `ins_claim` VALUES (300086, 'CL300086', 200192, 'P200192', '赵超', '寿险', '2026-05-21', '身故赔付', 5415.86, 0.00, '待审核', '海南省');
INSERT INTO `ins_claim` VALUES (300087, 'CL300087', 200194, 'P200194', '吴艳霞', '健康险', '2025-08-18', '医疗费用', 77289.33, 58583.14, '已赔付', '广东省');
INSERT INTO `ins_claim` VALUES (300088, 'CL300088', 200196, 'P200196', '杜明', '健康险', '2025-05-04', '重疾给付', 48332.20, 0.00, '已拒赔', '广东省');
INSERT INTO `ins_claim` VALUES (300089, 'CL300089', 200202, 'P200202', '丁敏', '车险', '2026-01-12', '车辆损失', 75809.25, 72327.15, '已赔付', '湖南省');
INSERT INTO `ins_claim` VALUES (300090, 'CL300090', 200203, 'P200203', '肖婷宇', '财产险', '2026-05-12', '车辆损失', 26246.32, 21552.00, '已赔付', '江苏省');
INSERT INTO `ins_claim` VALUES (300091, 'CL300091', 200208, 'P200208', '郭鑫', '车险', '2026-04-16', '车辆损失', 33930.12, 30053.28, '已赔付', '山西省');
INSERT INTO `ins_claim` VALUES (300092, 'CL300092', 200209, 'P200209', '吕平', '财产险', '2026-01-17', '车辆损失', 37143.35, 26403.72, '已赔付', '浙江省');
INSERT INTO `ins_claim` VALUES (300093, 'CL300093', 200210, 'P200210', '潘娜', '健康险', '2025-07-19', '重疾给付', 4999.81, 3496.92, '已赔付', '山东省');
INSERT INTO `ins_claim` VALUES (300094, 'CL300094', 200214, 'P200214', '魏娟', '意外险', '2025-12-25', '医疗费用', 34024.95, 25786.42, '已赔付', '陕西省');
INSERT INTO `ins_claim` VALUES (300095, 'CL300095', 200216, 'P200216', '姚玲', '寿险', '2026-04-07', '重疾给付', 43753.84, 40346.72, '已赔付', '重庆市');
INSERT INTO `ins_claim` VALUES (300096, 'CL300096', 200218, 'P200218', '潘涛建华', '寿险', '2025-09-14', '重疾给付', 3703.25, 2433.60, '已赔付', '北京市');
INSERT INTO `ins_claim` VALUES (300097, 'CL300097', 200220, 'P200220', '于雪丽', '健康险', '2025-08-07', '重疾给付', 73198.19, 0.00, '待审核', '福建省');
INSERT INTO `ins_claim` VALUES (300098, 'CL300098', 200221, 'P200221', '王强', '健康险', '2024-05-26', '重疾给付', 11987.81, 0.00, '已拒赔', '上海市');
INSERT INTO `ins_claim` VALUES (300099, 'CL300099', 200222, 'P200222', '田平', '健康险', '2026-01-13', '重疾给付', 15162.24, 13549.15, '已赔付', '北京市');
INSERT INTO `ins_claim` VALUES (300100, 'CL300100', 200224, 'P200224', '刘思远艳', '意外险', '2025-07-08', '意外伤害', 40997.69, 38784.49, '已赔付', '浙江省');
INSERT INTO `ins_claim` VALUES (300101, 'CL300101', 200225, 'P200225', '邓伟', '意外险', '2025-12-17', '医疗费用', 42048.13, 0.00, '待审核', '甘肃省');
INSERT INTO `ins_claim` VALUES (300102, 'CL300102', 200226, 'P200226', '马浩然', '车险', '2026-05-24', '车辆损失', 8003.73, 0.00, '待审核', '云南省');
INSERT INTO `ins_claim` VALUES (300103, 'CL300103', 200231, 'P200231', '刘强嘉怡', '寿险', '2025-11-19', '重疾给付', 4753.42, 4163.51, '已赔付', '广东省');
INSERT INTO `ins_claim` VALUES (300104, 'CL300104', 200234, 'P200234', '彭思远', '财产险', '2026-02-13', '意外伤害', 15063.48, 14369.54, '已赔付', '陕西省');
INSERT INTO `ins_claim` VALUES (300105, 'CL300105', 200235, 'P200235', '冯婷', '寿险', '2025-09-09', '身故赔付', 14158.29, 0.00, '待审核', '广东省');
INSERT INTO `ins_claim` VALUES (300106, 'CL300106', 200236, 'P200236', '陈娜', '意外险', '2025-11-27', '意外伤害', 62544.17, 0.00, '待审核', '北京市');
INSERT INTO `ins_claim` VALUES (300107, 'CL300107', 200240, 'P200240', '丁刚娜', '寿险', '2025-07-30', '重疾给付', 28841.47, 19934.83, '已赔付', '福建省');
INSERT INTO `ins_claim` VALUES (300108, 'CL300108', 200241, 'P200241', '黄辉', '寿险', '2025-05-25', '重疾给付', 5145.57, 0.00, '已拒赔', '海南省');
INSERT INTO `ins_claim` VALUES (300109, 'CL300109', 200245, 'P200245', '丁鑫思远', '车险', '2024-09-20', '车辆损失', 27734.00, 25696.04, '已赔付', '江苏省');
INSERT INTO `ins_claim` VALUES (300110, 'CL300110', 200247, 'P200247', '何鑫芳', '意外险', '2026-05-07', '意外伤害', 59686.15, 44476.95, '已赔付', '陕西省');
INSERT INTO `ins_claim` VALUES (300111, 'CL300111', 200249, 'P200249', '徐丽', '车险', '2024-02-22', '车辆损失', 79815.52, 56049.49, '已赔付', '江苏省');
INSERT INTO `ins_claim` VALUES (300112, 'CL300112', 200252, 'P200252', '田玲丽', '意外险', '2025-09-08', '意外伤害', 68924.88, 68646.95, '已赔付', '浙江省');
INSERT INTO `ins_claim` VALUES (300113, 'CL300113', 200253, 'P200253', '徐丽宇', '车险', '2025-02-12', '车辆损失', 77294.00, 0.00, '待审核', '河北省');
INSERT INTO `ins_claim` VALUES (300114, 'CL300114', 200259, 'P200259', '马平思远', '健康险', '2024-12-26', '重疾给付', 70236.13, 0.00, '待审核', '江苏省');
INSERT INTO `ins_claim` VALUES (300115, 'CL300115', 200262, 'P200262', '林明', '意外险', '2026-03-14', '意外伤害', 21498.11, 0.00, '待审核', '甘肃省');
INSERT INTO `ins_claim` VALUES (300116, 'CL300116', 200263, 'P200263', '韩洋', '寿险', '2026-04-03', '重疾给付', 38669.07, 24605.45, '已赔付', '上海市');
INSERT INTO `ins_claim` VALUES (300117, 'CL300117', 200265, 'P200265', '余超', '健康险', '2026-04-15', '重疾给付', 73161.77, 72549.77, '已赔付', '湖南省');
INSERT INTO `ins_claim` VALUES (300118, 'CL300118', 200268, 'P200268', '吕子轩雪', '财产险', '2025-04-02', '车辆损失', 31439.89, 25821.36, '已赔付', '广东省');
INSERT INTO `ins_claim` VALUES (300119, 'CL300119', 200273, 'P200273', '周军', '财产险', '2024-05-30', '车辆损失', 63049.71, 0.00, '待审核', '北京市');
INSERT INTO `ins_claim` VALUES (300120, 'CL300120', 200274, 'P200274', '宋欣怡', '意外险', '2025-08-09', '医疗费用', 50891.67, 0.00, '已拒赔', '湖南省');
INSERT INTO `ins_claim` VALUES (300121, 'CL300121', 200275, 'P200275', '肖平', '健康险', '2025-05-16', '医疗费用', 36603.93, 0.00, '已拒赔', '上海市');
INSERT INTO `ins_claim` VALUES (300122, 'CL300122', 200277, 'P200277', '董强', '寿险', '2025-06-24', '重疾给付', 48642.01, 0.00, '已拒赔', '山西省');
INSERT INTO `ins_claim` VALUES (300123, 'CL300123', 200278, 'P200278', '蔡霞', '财产险', '2026-02-05', '意外伤害', 69828.46, 0.00, '已拒赔', '广东省');
INSERT INTO `ins_claim` VALUES (300124, 'CL300124', 200294, 'P200294', '卢子轩', '意外险', '2026-05-12', '意外伤害', 76853.16, 72291.27, '已赔付', '山西省');
INSERT INTO `ins_claim` VALUES (300125, 'CL300125', 200295, 'P200295', '韩超', '健康险', '2025-12-09', '医疗费用', 64959.96, 62562.20, '已赔付', '山东省');
INSERT INTO `ins_claim` VALUES (300126, 'CL300126', 200296, 'P200296', '杜勇梓涵', '寿险', '2026-04-08', '身故赔付', 56525.25, 42534.13, '已赔付', '广东省');
INSERT INTO `ins_claim` VALUES (300127, 'CL300127', 200297, 'P200297', '高芳', '寿险', '2026-04-19', '身故赔付', 58177.42, 0.00, '已拒赔', '天津市');
INSERT INTO `ins_claim` VALUES (300128, 'CL300128', 200299, 'P200299', '赵娟娜', '健康险', '2026-03-18', '重疾给付', 12124.55, 8322.71, '已赔付', '海南省');

-- ----------------------------
-- Table structure for ins_customer
-- ----------------------------
DROP TABLE IF EXISTS `ins_customer`;
CREATE TABLE `ins_customer`  (
  `id` bigint(20) NOT NULL COMMENT '客户ID',
  `cust_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户编号',
  `cust_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户姓名',
  `gender` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别（男/女）',
  `age` int(11) NULL DEFAULT NULL COMMENT '年龄',
  `province` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城市',
  `region` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '大区（华东/华南/华北/华中/西南/西北）',
  `occupation` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职业',
  `cust_level` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户等级（普通/银卡/金卡/白金）',
  `reg_date` date NULL DEFAULT NULL COMMENT '注册日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '保险客户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ins_customer
-- ----------------------------
INSERT INTO `ins_customer` VALUES (100001, 'IC100001', '郭勇辉', '男', 33, '浙江省', '杭州市', '华东', '公务员', '普通', '2025-06-02');
INSERT INTO `ins_customer` VALUES (100002, 'IC100002', '林洋', '男', 63, '甘肃省', '兰州市', '西北', '教师', '银卡', '2023-03-04');
INSERT INTO `ins_customer` VALUES (100003, 'IC100003', '马嘉怡', '男', 54, '江苏省', '南京市', '华东', '自由职业', '白金', '2024-12-30');
INSERT INTO `ins_customer` VALUES (100004, 'IC100004', '郑娟', '男', 38, '河北省', '石家庄市', '华北', '自由职业', '银卡', '2025-08-29');
INSERT INTO `ins_customer` VALUES (100005, 'IC100005', '丁军', '男', 32, '山东省', '青岛市', '华东', '教师', '白金', '2023-03-24');
INSERT INTO `ins_customer` VALUES (100006, 'IC100006', '何欣怡', '女', 43, '云南省', '昆明市', '西南', '学生', '白金', '2024-02-23');
INSERT INTO `ins_customer` VALUES (100007, 'IC100007', '邓超', '男', 29, '广东省', '深圳市', '华南', '企业职员', '金卡', '2023-07-15');
INSERT INTO `ins_customer` VALUES (100008, 'IC100008', '朱静', '女', 45, '甘肃省', '兰州市', '西北', '司机', '普通', '2023-06-25');
INSERT INTO `ins_customer` VALUES (100009, 'IC100009', '谢芳', '女', 56, '广东省', '深圳市', '华南', '公务员', '普通', '2023-09-10');
INSERT INTO `ins_customer` VALUES (100010, 'IC100010', '袁雪', '女', 52, '江苏省', '苏州市', '华东', '企业职员', '金卡', '2025-12-22');
INSERT INTO `ins_customer` VALUES (100011, 'IC100011', '张宇辉', '女', 32, '浙江省', '杭州市', '华东', '退休', '普通', '2025-01-31');
INSERT INTO `ins_customer` VALUES (100012, 'IC100012', '苏玲雪', '女', 39, '北京市', '北京市', '华北', '司机', '普通', '2024-04-30');
INSERT INTO `ins_customer` VALUES (100013, 'IC100013', '徐婷', '女', 63, '湖北省', '武汉市', '华中', '工程师', '白金', '2023-03-14');
INSERT INTO `ins_customer` VALUES (100014, 'IC100014', '魏宇', '女', 58, '湖南省', '长沙市', '华中', '企业职员', '银卡', '2024-04-09');
INSERT INTO `ins_customer` VALUES (100015, 'IC100015', '宋丽军', '男', 54, '山东省', '青岛市', '华东', '企业职员', '白金', '2025-01-18');
INSERT INTO `ins_customer` VALUES (100016, 'IC100016', '黄梓涵子轩', '女', 43, '广东省', '深圳市', '华南', '退休', '金卡', '2026-02-06');
INSERT INTO `ins_customer` VALUES (100017, 'IC100017', '彭敏', '女', 22, '广东省', '深圳市', '华南', '自由职业', '金卡', '2026-01-20');
INSERT INTO `ins_customer` VALUES (100018, 'IC100018', '丁丽', '女', 54, '江苏省', '南京市', '华东', '个体经营', '白金', '2023-04-26');
INSERT INTO `ins_customer` VALUES (100019, 'IC100019', '肖伟', '男', 29, '北京市', '北京市', '华北', '个体经营', '白金', '2024-06-23');
INSERT INTO `ins_customer` VALUES (100020, 'IC100020', '朱静', '女', 38, '重庆市', '重庆市', '西南', '医生', '金卡', '2025-06-14');
INSERT INTO `ins_customer` VALUES (100021, 'IC100021', '郑建华芳', '女', 51, '海南省', '海口市', '华南', '企业职员', '普通', '2023-07-28');
INSERT INTO `ins_customer` VALUES (100022, 'IC100022', '彭勇', '女', 46, '云南省', '昆明市', '西南', '医生', '银卡', '2023-03-13');
INSERT INTO `ins_customer` VALUES (100023, 'IC100023', '周娟', '女', 30, '河北省', '石家庄市', '华北', '医生', '普通', '2025-07-29');
INSERT INTO `ins_customer` VALUES (100024, 'IC100024', '任婷', '女', 42, '湖北省', '武汉市', '华中', '司机', '银卡', '2024-05-18');
INSERT INTO `ins_customer` VALUES (100025, 'IC100025', '梁洋', '女', 61, '甘肃省', '兰州市', '西北', '个体经营', '金卡', '2025-09-06');
INSERT INTO `ins_customer` VALUES (100026, 'IC100026', '徐芳涛', '女', 42, '福建省', '厦门市', '华南', '自由职业', '普通', '2024-12-15');
INSERT INTO `ins_customer` VALUES (100027, 'IC100027', '任建华', '男', 59, '重庆市', '重庆市', '西南', '退休', '普通', '2024-09-29');
INSERT INTO `ins_customer` VALUES (100028, 'IC100028', '程晓东', '女', 43, '河南省', '郑州市', '华中', '企业职员', '普通', '2024-08-21');
INSERT INTO `ins_customer` VALUES (100029, 'IC100029', '任敏', '女', 46, '四川省', '成都市', '西南', '个体经营', '普通', '2024-07-01');
INSERT INTO `ins_customer` VALUES (100030, 'IC100030', '任雪', '男', 50, '湖南省', '长沙市', '华中', '学生', '金卡', '2025-10-09');
INSERT INTO `ins_customer` VALUES (100031, 'IC100031', '陈鑫', '女', 33, '四川省', '成都市', '西南', '医生', '白金', '2024-01-21');
INSERT INTO `ins_customer` VALUES (100032, 'IC100032', '徐军霞', '男', 22, '四川省', '成都市', '西南', '公务员', '普通', '2024-12-30');
INSERT INTO `ins_customer` VALUES (100033, 'IC100033', '黄丽', '女', 32, '上海市', '上海市', '华东', '自由职业', '普通', '2024-11-17');
INSERT INTO `ins_customer` VALUES (100034, 'IC100034', '彭桂英', '女', 31, '湖北省', '武汉市', '华中', '公务员', '普通', '2025-02-28');
INSERT INTO `ins_customer` VALUES (100035, 'IC100035', '何超涛', '男', 28, '浙江省', '宁波市', '华东', '教师', '金卡', '2023-04-08');
INSERT INTO `ins_customer` VALUES (100036, 'IC100036', '杨静', '女', 54, '湖南省', '长沙市', '华中', '教师', '银卡', '2025-08-05');
INSERT INTO `ins_customer` VALUES (100037, 'IC100037', '袁晓东', '女', 32, '甘肃省', '兰州市', '西北', '退休', '白金', '2023-04-03');
INSERT INTO `ins_customer` VALUES (100038, 'IC100038', '田娜子轩', '女', 35, '云南省', '昆明市', '西南', '公务员', '普通', '2025-08-27');
INSERT INTO `ins_customer` VALUES (100039, 'IC100039', '梁嘉怡刚', '男', 49, '江苏省', '南京市', '华东', '公务员', '普通', '2025-01-31');
INSERT INTO `ins_customer` VALUES (100040, 'IC100040', '任玲', '女', 30, '陕西省', '西安市', '西北', '个体经营', '银卡', '2026-02-05');
INSERT INTO `ins_customer` VALUES (100041, 'IC100041', '曾敏', '女', 59, '山西省', '太原市', '华北', '自由职业', '银卡', '2025-12-29');
INSERT INTO `ins_customer` VALUES (100042, 'IC100042', '程娟', '男', 25, '湖北省', '武汉市', '华中', '公务员', '白金', '2025-09-11');
INSERT INTO `ins_customer` VALUES (100043, 'IC100043', '彭嘉怡杰', '男', 61, '陕西省', '西安市', '西北', '学生', '普通', '2026-01-11');
INSERT INTO `ins_customer` VALUES (100044, 'IC100044', '任婷嘉怡', '女', 46, '广东省', '广州市', '华南', '工程师', '白金', '2025-10-04');
INSERT INTO `ins_customer` VALUES (100045, 'IC100045', '宋丽', '女', 24, '河南省', '郑州市', '华中', '学生', '普通', '2026-05-14');
INSERT INTO `ins_customer` VALUES (100046, 'IC100046', '何娜雪', '女', 32, '天津市', '天津市', '华北', '司机', '白金', '2025-02-16');
INSERT INTO `ins_customer` VALUES (100047, 'IC100047', '程洋', '男', 57, '广东省', '广州市', '华南', '司机', '金卡', '2026-01-04');
INSERT INTO `ins_customer` VALUES (100048, 'IC100048', '袁艳杰', '女', 27, '浙江省', '宁波市', '华东', '公务员', '普通', '2026-05-02');
INSERT INTO `ins_customer` VALUES (100049, 'IC100049', '姚辉芳', '男', 52, '广东省', '广州市', '华南', '个体经营', '银卡', '2023-01-16');
INSERT INTO `ins_customer` VALUES (100050, 'IC100050', '刘辉', '女', 61, '山西省', '太原市', '华北', '企业职员', '普通', '2024-05-12');
INSERT INTO `ins_customer` VALUES (100051, 'IC100051', '彭娟', '女', 57, '江苏省', '南京市', '华东', '公务员', '普通', '2023-07-12');
INSERT INTO `ins_customer` VALUES (100052, 'IC100052', '余刚', '男', 42, '广东省', '深圳市', '华南', '个体经营', '金卡', '2024-09-28');
INSERT INTO `ins_customer` VALUES (100053, 'IC100053', '曾晓东敏', '女', 47, '广东省', '深圳市', '华南', '司机', '银卡', '2023-04-26');
INSERT INTO `ins_customer` VALUES (100054, 'IC100054', '唐玲', '男', 32, '重庆市', '重庆市', '西南', '个体经营', '银卡', '2025-01-12');
INSERT INTO `ins_customer` VALUES (100055, 'IC100055', '何艳磊', '女', 25, '天津市', '天津市', '华北', '学生', '金卡', '2024-08-10');
INSERT INTO `ins_customer` VALUES (100056, 'IC100056', '于超', '男', 27, '河北省', '石家庄市', '华北', '司机', '普通', '2023-05-12');
INSERT INTO `ins_customer` VALUES (100057, 'IC100057', '胡涛', '男', 64, '云南省', '昆明市', '西南', '司机', '白金', '2024-07-12');
INSERT INTO `ins_customer` VALUES (100058, 'IC100058', '魏子轩', '女', 39, '广东省', '深圳市', '华南', '医生', '白金', '2025-01-22');
INSERT INTO `ins_customer` VALUES (100059, 'IC100059', '于娜', '男', 21, '江苏省', '南京市', '华东', '教师', '普通', '2023-11-14');
INSERT INTO `ins_customer` VALUES (100060, 'IC100060', '魏雪', '女', 45, '山西省', '太原市', '华北', '司机', '普通', '2026-02-03');
INSERT INTO `ins_customer` VALUES (100061, 'IC100061', '韩婷', '男', 36, '河北省', '石家庄市', '华北', '司机', '白金', '2023-02-25');
INSERT INTO `ins_customer` VALUES (100062, 'IC100062', '杜梓涵', '女', 31, '重庆市', '重庆市', '西南', '个体经营', '金卡', '2026-04-19');
INSERT INTO `ins_customer` VALUES (100063, 'IC100063', '李欣怡杰', '女', 55, '四川省', '成都市', '西南', '企业职员', '银卡', '2024-10-14');
INSERT INTO `ins_customer` VALUES (100064, 'IC100064', '郭辉', '男', 47, '广东省', '东莞市', '华南', '学生', '金卡', '2023-06-30');
INSERT INTO `ins_customer` VALUES (100065, 'IC100065', '袁静', '男', 35, '海南省', '海口市', '华南', '学生', '普通', '2023-07-04');
INSERT INTO `ins_customer` VALUES (100066, 'IC100066', '谢梓涵', '女', 32, '陕西省', '西安市', '西北', '公务员', '金卡', '2023-10-10');
INSERT INTO `ins_customer` VALUES (100067, 'IC100067', '郑刚', '女', 56, '陕西省', '西安市', '西北', '自由职业', '银卡', '2024-10-23');
INSERT INTO `ins_customer` VALUES (100068, 'IC100068', '姚敏', '女', 51, '天津市', '天津市', '华北', '退休', '白金', '2024-12-31');
INSERT INTO `ins_customer` VALUES (100069, 'IC100069', '程宇', '女', 30, '山西省', '太原市', '华北', '自由职业', '白金', '2025-10-13');
INSERT INTO `ins_customer` VALUES (100070, 'IC100070', '梁丽欣怡', '女', 57, '浙江省', '宁波市', '华东', '学生', '银卡', '2023-05-10');
INSERT INTO `ins_customer` VALUES (100071, 'IC100071', '蔡杰', '男', 48, '浙江省', '杭州市', '华东', '自由职业', '白金', '2026-04-25');
INSERT INTO `ins_customer` VALUES (100072, 'IC100072', '胡艳刚', '女', 60, '云南省', '昆明市', '西南', '学生', '白金', '2024-10-07');
INSERT INTO `ins_customer` VALUES (100073, 'IC100073', '李嘉怡', '男', 52, '山东省', '青岛市', '华东', '个体经营', '普通', '2024-06-01');
INSERT INTO `ins_customer` VALUES (100074, 'IC100074', '王明', '女', 34, '浙江省', '杭州市', '华东', '退休', '白金', '2024-08-20');
INSERT INTO `ins_customer` VALUES (100075, 'IC100075', '苏明', '男', 33, '河北省', '石家庄市', '华北', '工程师', '银卡', '2026-04-19');
INSERT INTO `ins_customer` VALUES (100076, 'IC100076', '梁思远', '男', 38, '山西省', '太原市', '华北', '工程师', '白金', '2023-11-03');
INSERT INTO `ins_customer` VALUES (100077, 'IC100077', '徐静艳', '女', 33, '四川省', '成都市', '西南', '自由职业', '普通', '2023-12-28');
INSERT INTO `ins_customer` VALUES (100078, 'IC100078', '沈平浩然', '男', 56, '天津市', '天津市', '华北', '自由职业', '金卡', '2023-06-22');
INSERT INTO `ins_customer` VALUES (100079, 'IC100079', '曹娜', '男', 55, '云南省', '昆明市', '西南', '退休', '白金', '2023-05-24');
INSERT INTO `ins_customer` VALUES (100080, 'IC100080', '卢超桂英', '女', 23, '山西省', '太原市', '华北', '医生', '普通', '2025-03-17');

-- ----------------------------
-- Table structure for ins_policy
-- ----------------------------
DROP TABLE IF EXISTS `ins_policy`;
CREATE TABLE `ins_policy`  (
  `id` bigint(20) NOT NULL COMMENT '保单ID',
  `policy_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '保单号',
  `cust_id` bigint(20) NULL DEFAULT NULL COMMENT '客户ID',
  `cust_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户姓名',
  `product_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '险种名称',
  `product_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '险种类型（寿险/健康险/车险/意外险/财产险）',
  `channel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '销售渠道（线上直销/代理人/银保/电销/经纪）',
  `premium` decimal(12, 2) NULL DEFAULT NULL COMMENT '年保费（元）',
  `coverage_amount` decimal(14, 2) NULL DEFAULT NULL COMMENT '保额（元）',
  `term_years` int(11) NULL DEFAULT NULL COMMENT '保障年限',
  `sign_date` date NULL DEFAULT NULL COMMENT '投保日期',
  `effect_date` date NULL DEFAULT NULL COMMENT '生效日期',
  `expire_date` date NULL DEFAULT NULL COMMENT '到期日期',
  `status` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '保单状态（生效/已过期/退保）',
  `province` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出单省份',
  `region` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '大区',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '保险保单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ins_policy
-- ----------------------------
INSERT INTO `ins_policy` VALUES (200001, 'P200001', 100022, '刘思远嘉怡', '百万守护意外险', '意外险', '代理人', 15288.48, 1593795.46, 10, '2025-02-06', '2025-02-07', '2035-02-06', '生效', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200002, 'P200002', 100010, '郑霞', 'e生保百万医疗', '健康险', '电销', 6309.37, 279801.21, 10, '2023-09-12', '2023-09-13', '2033-09-12', '生效', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200003, 'P200003', 100044, '程婷', '企业财产基本险', '财产险', '经纪', 15416.63, 1785869.13, 5, '2025-08-25', '2025-08-26', '2030-08-25', '生效', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200004, 'P200004', 100071, '徐建华', '货运物流险', '财产险', '电销', 796.51, 65791.87, 5, '2025-04-24', '2025-04-25', '2030-04-24', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200005, 'P200005', 100068, '张玲敏', '交强险', '车险', '电销', 1134.32, 126080.76, 30, '2023-01-10', '2023-01-11', '2053-01-10', '生效', '天津市', '华北');
INSERT INTO `ins_policy` VALUES (200006, 'P200006', 100059, '梁敏', '企业财产基本险', '财产险', '线上直销', 5117.29, 381032.19, 1, '2026-01-11', '2026-01-12', '2027-01-11', '已过期', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200007, 'P200007', 100080, '陈军', '货运物流险', '财产险', '银保', 17327.21, 1329003.72, 1, '2024-03-31', '2024-04-01', '2025-03-28', '生效', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200008, 'P200008', 100027, '许辉', '雇主责任险', '财产险', '经纪', 651.83, 30182.55, 20, '2024-10-29', '2024-10-30', '2044-10-28', '生效', '重庆市', '西南');
INSERT INTO `ins_policy` VALUES (200009, 'P200009', 100007, '林子轩辉', '国寿福满一生', '寿险', '线上直销', 14003.05, 445725.92, 20, '2025-02-20', '2025-02-21', '2045-02-20', '生效', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200010, 'P200010', 100051, '郑敏', '金佑人生两全险', '寿险', '银保', 6113.89, 473759.33, 10, '2023-01-22', '2023-01-23', '2033-01-22', '生效', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200011, 'P200011', 100074, '卢杰雪', '交通意外险', '意外险', '线上直销', 12686.54, 549978.72, 30, '2025-02-24', '2025-02-25', '2055-02-24', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200012, 'P200012', 100047, '徐嘉怡丽', '货运物流险', '财产险', '线上直销', 8240.07, 478499.07, 20, '2025-07-06', '2025-07-07', '2045-07-06', '退保', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200013, 'P200013', 100039, '徐思远', '金佑人生两全险', '寿险', '代理人', 13059.74, 987600.94, 1, '2023-01-09', '2023-01-10', '2024-01-09', '已过期', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200014, 'P200014', 100012, '姚桂英超', '少儿门诊医疗', '健康险', '经纪', 501.39, 18037.89, 30, '2024-05-31', '2024-06-01', '2054-05-28', '生效', '北京市', '华北');
INSERT INTO `ins_policy` VALUES (200015, 'P200015', 100005, '袁嘉怡', '综合意外险', '意外险', '代理人', 17508.37, 1258736.91, 20, '2023-12-26', '2023-12-27', '2043-12-26', '已过期', '山东省', '华东');
INSERT INTO `ins_policy` VALUES (200016, 'P200016', 100046, '肖嘉怡军', '成人意外伤害险', '意外险', '电销', 3017.16, 227153.79, 30, '2026-05-05', '2026-05-06', '2056-05-05', '生效', '天津市', '华北');
INSERT INTO `ins_policy` VALUES (200017, 'P200017', 100005, '郭涛', '交强险', '车险', '代理人', 8645.17, 248629.33, 30, '2025-09-28', '2025-09-29', '2055-09-28', '已过期', '山东省', '华东');
INSERT INTO `ins_policy` VALUES (200018, 'P200018', 100036, '郭平建华', '少儿门诊医疗', '健康险', '代理人', 7667.39, 537651.17, 30, '2023-07-09', '2023-07-10', '2053-07-09', '已过期', '湖南省', '华中');
INSERT INTO `ins_policy` VALUES (200019, 'P200019', 100012, '黄艳宇', 'e生保百万医疗', '健康险', '代理人', 8148.30, 823461.89, 20, '2025-07-25', '2025-07-26', '2045-07-25', '生效', '北京市', '华北');
INSERT INTO `ins_policy` VALUES (200020, 'P200020', 100066, '魏梓涵', '综合意外险', '意外险', '线上直销', 16926.26, 1650225.50, 5, '2023-02-08', '2023-02-09', '2028-02-08', '生效', '陕西省', '西北');
INSERT INTO `ins_policy` VALUES (200021, 'P200021', 100063, '袁明明', '国寿福满一生', '寿险', '电销', 1328.36, 100084.09, 1, '2024-05-22', '2024-05-23', '2025-05-22', '生效', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200022, 'P200022', 100060, '韩嘉怡', 'e生保百万医疗', '健康险', '电销', 11444.48, 578111.96, 1, '2026-03-07', '2026-03-08', '2027-03-07', '生效', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200023, 'P200023', 100057, '叶刚', '综合意外险', '意外险', '银保', 1482.86, 75119.97, 20, '2023-10-06', '2023-10-07', '2043-10-06', '生效', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200024, 'P200024', 100056, '许嘉怡', '百万守护意外险', '意外险', '银保', 11110.83, 228139.82, 1, '2024-11-16', '2024-11-17', '2025-11-16', '已过期', '河北省', '华北');
INSERT INTO `ins_policy` VALUES (200025, 'P200025', 100069, '宋晓东霞', '交通意外险', '意外险', '电销', 16818.51, 790603.27, 30, '2025-04-13', '2025-04-14', '2055-04-13', '退保', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200026, 'P200026', 100010, '肖嘉怡', '企业财产基本险', '财产险', '线上直销', 15134.01, 1130757.85, 20, '2023-08-22', '2023-08-23', '2043-08-22', '退保', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200027, 'P200027', 100071, '叶磊涛', '增额终身寿2026', '寿险', '经纪', 978.66, 108085.75, 20, '2024-08-19', '2024-08-20', '2044-08-19', '已过期', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200028, 'P200028', 100074, '王梓涵浩然', '第三者责任险', '车险', '经纪', 17304.52, 1656364.56, 30, '2025-01-13', '2025-01-14', '2055-01-13', '退保', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200029, 'P200029', 100053, '任鑫', '少儿门诊医疗', '健康险', '经纪', 12045.43, 1071288.42, 1, '2024-03-01', '2024-03-02', '2025-03-01', '退保', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200030, 'P200030', 100009, '胡思远', '好医保长期医疗', '健康险', '代理人', 2465.44, 81195.08, 10, '2025-07-28', '2025-07-29', '2035-07-28', '生效', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200031, 'P200031', 100070, '高浩然', '车损险尊享', '车险', '银保', 3894.92, 279225.38, 30, '2024-09-15', '2024-09-16', '2054-09-15', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200032, 'P200032', 100080, '谢娟', '国寿福满一生', '寿险', '银保', 17000.62, 428127.79, 1, '2024-06-21', '2024-06-22', '2025-06-21', '已过期', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200033, 'P200033', 100030, '宋宇勇', '好医保长期医疗', '健康险', '代理人', 17962.57, 1588838.64, 1, '2024-11-08', '2024-11-09', '2025-11-08', '生效', '湖南省', '华中');
INSERT INTO `ins_policy` VALUES (200034, 'P200034', 100012, '许辉', '重疾无忧A款', '健康险', '电销', 1950.36, 71063.84, 30, '2024-08-01', '2024-08-02', '2054-08-01', '生效', '北京市', '华北');
INSERT INTO `ins_policy` VALUES (200035, 'P200035', 100049, '马晓东', '增额终身寿2026', '寿险', '电销', 15356.55, 1705783.67, 1, '2025-11-23', '2025-11-24', '2026-11-23', '生效', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200036, 'P200036', 100024, '朱娟', 'e生保百万医疗', '健康险', '银保', 9574.05, 1122967.46, 1, '2024-07-06', '2024-07-07', '2025-07-06', '生效', '湖北省', '华中');
INSERT INTO `ins_policy` VALUES (200037, 'P200037', 100031, '沈芳', '增额终身寿2026', '寿险', '银保', 2827.86, 141582.44, 20, '2023-06-07', '2023-06-08', '2043-06-07', '已过期', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200038, 'P200038', 100060, '林艳', '百万守护意外险', '意外险', '电销', 9546.20, 458367.90, 10, '2026-01-12', '2026-01-13', '2036-01-12', '退保', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200039, 'P200039', 100048, '朱辉思远', '重疾无忧A款', '健康险', '电销', 16666.56, 1976949.82, 10, '2024-07-28', '2024-07-29', '2034-07-28', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200040, 'P200040', 100008, '姚玲', '少儿门诊医疗', '健康险', '线上直销', 6061.19, 407455.18, 1, '2024-04-08', '2024-04-09', '2025-04-08', '生效', '甘肃省', '西北');
INSERT INTO `ins_policy` VALUES (200041, 'P200041', 100061, '姚玲', '百万守护意外险', '意外险', '代理人', 8734.15, 523012.02, 20, '2023-08-14', '2023-08-15', '2043-08-14', '生效', '河北省', '华北');
INSERT INTO `ins_policy` VALUES (200042, 'P200042', 100074, '苏丽磊', '国寿福满一生', '寿险', '银保', 13179.69, 1249124.99, 1, '2023-01-29', '2023-01-30', '2024-01-28', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200043, 'P200043', 100072, '彭思远', '交强险', '车险', '线上直销', 9091.53, 457448.04, 1, '2025-09-30', '2025-10-01', '2026-09-28', '生效', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200044, 'P200044', 100010, '王嘉怡', '企业财产基本险', '财产险', '电销', 5174.08, 530179.80, 1, '2025-02-13', '2025-02-14', '2026-02-13', '已过期', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200045, 'P200045', 100029, '曾娜', '商业车险全保', '车险', '电销', 17711.83, 1973682.71, 1, '2023-04-01', '2023-04-02', '2024-04-01', '已过期', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200046, 'P200046', 100011, '潘强', '好医保长期医疗', '健康险', '线上直销', 1835.60, 109472.35, 1, '2024-08-24', '2024-08-25', '2025-08-24', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200047, 'P200047', 100030, '魏嘉怡', '金佑人生两全险', '寿险', '代理人', 14320.07, 1170615.17, 30, '2025-01-10', '2025-01-11', '2055-01-10', '生效', '湖南省', '华中');
INSERT INTO `ins_policy` VALUES (200048, 'P200048', 100067, '任敏', '国寿福满一生', '寿险', '代理人', 10272.78, 1223838.56, 30, '2023-06-29', '2023-06-30', '2053-06-28', '生效', '陕西省', '西北');
INSERT INTO `ins_policy` VALUES (200049, 'P200049', 100075, '叶强', '金佑人生两全险', '寿险', '线上直销', 13969.40, 790171.64, 10, '2024-09-16', '2024-09-17', '2034-09-16', '退保', '河北省', '华北');
INSERT INTO `ins_policy` VALUES (200050, 'P200050', 100058, '潘强', '重疾无忧A款', '健康险', '经纪', 5384.67, 277489.04, 20, '2025-04-29', '2025-04-30', '2045-04-28', '生效', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200051, 'P200051', 100047, '梁艳', '综合意外险', '意外险', '经纪', 10392.83, 1039067.99, 30, '2023-02-04', '2023-02-05', '2053-02-04', '生效', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200052, 'P200052', 100035, '朱洋娜', '企业财产基本险', '财产险', '银保', 2453.79, 54246.46, 1, '2025-10-06', '2025-10-07', '2026-10-06', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200053, 'P200053', 100034, '卢杰', '家庭财产综合险', '财产险', '代理人', 16269.50, 664866.40, 10, '2023-09-09', '2023-09-10', '2033-09-09', '生效', '湖北省', '华中');
INSERT INTO `ins_policy` VALUES (200054, 'P200054', 100029, '周梓涵', '金佑人生两全险', '寿险', '电销', 2532.77, 64699.54, 5, '2025-12-08', '2025-12-09', '2030-12-08', '生效', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200055, 'P200055', 100017, '任霞', '企业财产基本险', '财产险', '银保', 15697.94, 424448.69, 10, '2024-05-11', '2024-05-12', '2034-05-11', '已过期', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200056, 'P200056', 100039, '罗晓东', '平安福终身寿险', '寿险', '银保', 1988.78, 233485.86, 20, '2023-11-25', '2023-11-26', '2043-11-25', '生效', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200057, 'P200057', 100058, '吴芳', '好医保长期医疗', '健康险', '代理人', 2947.94, 205707.16, 10, '2025-07-23', '2025-07-24', '2035-07-23', '已过期', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200058, 'P200058', 100006, '潘丽', '成人意外伤害险', '意外险', '代理人', 2910.70, 251849.94, 5, '2023-11-07', '2023-11-08', '2028-11-07', '生效', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200059, 'P200059', 100032, '唐鑫浩然', '重疾无忧A款', '健康险', '电销', 4757.12, 360682.26, 10, '2025-09-19', '2025-09-20', '2035-09-19', '生效', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200060, 'P200060', 100021, '罗欣怡', '平安福终身寿险', '寿险', '银保', 14052.96, 724865.49, 20, '2024-12-09', '2024-12-10', '2044-12-09', '已过期', '海南省', '华南');
INSERT INTO `ins_policy` VALUES (200061, 'P200061', 100004, '程晓东', '雇主责任险', '财产险', '银保', 15087.48, 1699174.48, 5, '2024-03-27', '2024-03-28', '2029-03-27', '生效', '河北省', '华北');
INSERT INTO `ins_policy` VALUES (200062, 'P200062', 100065, '孙军', '商业车险全保', '车险', '代理人', 16606.22, 1718602.79, 10, '2023-05-11', '2023-05-12', '2033-05-11', '退保', '海南省', '华南');
INSERT INTO `ins_policy` VALUES (200063, 'P200063', 100058, '韩玲', '家庭财产综合险', '财产险', '经纪', 17084.42, 824229.54, 1, '2024-10-25', '2024-10-26', '2025-10-25', '生效', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200064, 'P200064', 100050, '吕军', '少儿门诊医疗', '健康险', '线上直销', 10485.63, 458128.44, 1, '2023-09-27', '2023-09-28', '2024-09-27', '生效', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200065, 'P200065', 100067, '徐鑫', '交强险', '车险', '银保', 8561.82, 203527.37, 1, '2025-07-03', '2025-07-04', '2026-07-03', '生效', '陕西省', '西北');
INSERT INTO `ins_policy` VALUES (200066, 'P200066', 100012, '郭辉丽', '雇主责任险', '财产险', '电销', 11574.98, 1031286.91, 1, '2024-10-06', '2024-10-07', '2025-10-06', '生效', '北京市', '华北');
INSERT INTO `ins_policy` VALUES (200067, 'P200067', 100052, '许伟', '国寿福满一生', '寿险', '代理人', 4673.07, 185483.00, 5, '2024-11-14', '2024-11-15', '2029-11-14', '已过期', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200068, 'P200068', 100021, '孙丽敏', '金佑人生两全险', '寿险', '电销', 1200.36, 49166.92, 30, '2025-11-01', '2025-11-02', '2055-11-01', '生效', '海南省', '华南');
INSERT INTO `ins_policy` VALUES (200069, 'P200069', 100045, '罗娟', '平安福终身寿险', '寿险', '电销', 8940.89, 1040979.65, 20, '2025-01-18', '2025-01-19', '2045-01-18', '生效', '河南省', '华中');
INSERT INTO `ins_policy` VALUES (200070, 'P200070', 100074, '郑伟艳', '第三者责任险', '车险', '代理人', 1379.64, 62173.29, 10, '2026-02-19', '2026-02-20', '2036-02-19', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200071, 'P200071', 100026, '曾洋', '百万守护意外险', '意外险', '经纪', 461.02, 52111.16, 30, '2025-10-14', '2025-10-15', '2055-10-14', '生效', '福建省', '华南');
INSERT INTO `ins_policy` VALUES (200072, 'P200072', 100003, '彭鑫', '车损险尊享', '车险', '银保', 886.07, 98474.12, 1, '2023-07-15', '2023-07-16', '2024-07-15', '已过期', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200073, 'P200073', 100037, '吕强静', '少儿门诊医疗', '健康险', '电销', 4979.17, 123715.56, 1, '2026-03-04', '2026-03-05', '2027-03-04', '已过期', '甘肃省', '西北');
INSERT INTO `ins_policy` VALUES (200074, 'P200074', 100011, '叶艳', '平安福终身寿险', '寿险', '代理人', 6242.74, 213981.41, 1, '2023-01-03', '2023-01-04', '2024-01-03', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200075, 'P200075', 100011, '高晓东', '少儿门诊医疗', '健康险', '银保', 6814.20, 811139.58, 5, '2024-11-29', '2024-11-30', '2029-11-28', '退保', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200076, 'P200076', 100031, '曾艳', '平安福终身寿险', '寿险', '经纪', 8845.93, 876128.18, 1, '2024-05-05', '2024-05-06', '2025-05-05', '已过期', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200077, 'P200077', 100070, '唐明', '少儿门诊医疗', '健康险', '电销', 8348.00, 621591.94, 10, '2023-09-13', '2023-09-14', '2033-09-13', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200078, 'P200078', 100009, '蒋艳梓涵', 'e生保百万医疗', '健康险', '银保', 12634.45, 1321904.99, 5, '2023-11-16', '2023-11-17', '2028-11-16', '退保', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200079, 'P200079', 100079, '韩子轩', '交通意外险', '意外险', '银保', 9546.43, 495985.54, 1, '2024-07-16', '2024-07-17', '2025-07-16', '生效', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200080, 'P200080', 100027, '潘浩然', '家庭财产综合险', '财产险', '银保', 10621.58, 269882.53, 20, '2024-02-12', '2024-02-13', '2044-02-12', '生效', '重庆市', '西南');
INSERT INTO `ins_policy` VALUES (200081, 'P200081', 100041, '韩强', '国寿福满一生', '寿险', '经纪', 5137.23, 448392.35, 20, '2023-03-17', '2023-03-18', '2043-03-17', '生效', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200082, 'P200082', 100031, '董桂英', '商业车险全保', '车险', '电销', 5030.62, 264304.15, 5, '2024-09-03', '2024-09-04', '2029-09-03', '退保', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200083, 'P200083', 100009, '何浩然', '百万守护意外险', '意外险', '经纪', 3278.49, 70536.23, 30, '2023-11-09', '2023-11-10', '2053-11-09', '生效', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200084, 'P200084', 100010, '周婷磊', '企业财产基本险', '财产险', '代理人', 16505.10, 1147804.69, 1, '2024-05-06', '2024-05-07', '2025-05-06', '生效', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200085, 'P200085', 100074, '姚婷雪', '百万守护意外险', '意外险', '线上直销', 16004.35, 1635325.31, 20, '2024-04-26', '2024-04-27', '2044-04-26', '已过期', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200086, 'P200086', 100062, '郭晓东欣怡', '成人意外伤害险', '意外险', '银保', 1019.23, 30575.38, 1, '2024-04-04', '2024-04-05', '2025-04-04', '生效', '重庆市', '西南');
INSERT INTO `ins_policy` VALUES (200087, 'P200087', 100061, '田静军', '平安福终身寿险', '寿险', '经纪', 17318.00, 415513.64, 1, '2025-01-09', '2025-01-10', '2026-01-09', '生效', '河北省', '华北');
INSERT INTO `ins_policy` VALUES (200088, 'P200088', 100038, '田子轩', '平安福终身寿险', '寿险', '经纪', 10417.25, 746629.33, 10, '2023-12-02', '2023-12-03', '2033-12-02', '退保', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200089, 'P200089', 100036, '沈雪子轩', '雇主责任险', '财产险', '代理人', 10186.40, 1007476.62, 1, '2024-01-28', '2024-01-29', '2025-01-28', '退保', '湖南省', '华中');
INSERT INTO `ins_policy` VALUES (200090, 'P200090', 100061, '赵宇', '少儿门诊医疗', '健康险', '银保', 8950.13, 213395.84, 5, '2025-09-10', '2025-09-11', '2030-09-10', '退保', '河北省', '华北');
INSERT INTO `ins_policy` VALUES (200091, 'P200091', 100050, '谢静', '成人意外伤害险', '意外险', '代理人', 8810.99, 763490.71, 1, '2023-08-31', '2023-09-01', '2024-08-28', '生效', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200092, 'P200092', 100020, '郭宇梓涵', '国寿福满一生', '寿险', '电销', 2709.72, 117944.38, 30, '2025-12-05', '2025-12-06', '2055-12-05', '退保', '重庆市', '西南');
INSERT INTO `ins_policy` VALUES (200093, 'P200093', 100017, '肖超', '重疾无忧A款', '健康险', '银保', 13355.72, 275977.99, 1, '2023-05-04', '2023-05-05', '2024-05-04', '已过期', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200094, 'P200094', 100013, '罗磊', '综合意外险', '意外险', '电销', 5005.41, 417980.27, 30, '2025-02-26', '2025-02-27', '2055-02-26', '生效', '湖北省', '华中');
INSERT INTO `ins_policy` VALUES (200095, 'P200095', 100034, '肖勇', '商业车险全保', '车险', '经纪', 17599.99, 779816.14, 30, '2025-10-11', '2025-10-12', '2055-10-11', '生效', '湖北省', '华中');
INSERT INTO `ins_policy` VALUES (200096, 'P200096', 100027, '黄强', '成人意外伤害险', '意外险', '线上直销', 4161.21, 337168.31, 30, '2023-07-03', '2023-07-04', '2053-07-03', '生效', '重庆市', '西南');
INSERT INTO `ins_policy` VALUES (200097, 'P200097', 100003, '姚霞', '雇主责任险', '财产险', '线上直销', 16183.15, 1363776.91, 1, '2023-07-26', '2023-07-27', '2024-07-26', '生效', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200098, 'P200098', 100006, '程辉', '少儿门诊医疗', '健康险', '银保', 5211.02, 355481.50, 30, '2023-10-10', '2023-10-11', '2053-10-10', '已过期', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200099, 'P200099', 100080, '于军', '交强险', '车险', '电销', 7699.55, 283827.72, 5, '2025-07-29', '2025-07-30', '2030-07-28', '生效', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200100, 'P200100', 100042, '马超', '车损险尊享', '车险', '代理人', 17200.88, 1376327.93, 1, '2025-03-28', '2025-03-29', '2026-03-28', '生效', '湖北省', '华中');
INSERT INTO `ins_policy` VALUES (200101, 'P200101', 100055, '罗强', '雇主责任险', '财产险', '电销', 17537.99, 429642.99, 1, '2026-03-28', '2026-03-29', '2027-03-28', '生效', '天津市', '华北');
INSERT INTO `ins_policy` VALUES (200102, 'P200102', 100032, '冯浩然丽', '平安福终身寿险', '寿险', '线上直销', 6987.81, 142652.72, 5, '2025-03-18', '2025-03-19', '2030-03-18', '生效', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200103, 'P200103', 100029, '田宇', '金佑人生两全险', '寿险', '银保', 11681.66, 976621.21, 20, '2024-04-11', '2024-04-12', '2044-04-11', '生效', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200104, 'P200104', 100015, '周娟', '好医保长期医疗', '健康险', '电销', 7846.07, 479065.75, 10, '2023-04-15', '2023-04-16', '2033-04-15', '生效', '山东省', '华东');
INSERT INTO `ins_policy` VALUES (200105, 'P200105', 100059, '陈洋', '第三者责任险', '车险', '代理人', 9892.44, 207503.91, 1, '2023-05-12', '2023-05-13', '2024-05-12', '生效', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200106, 'P200106', 100038, '徐勇', 'e生保百万医疗', '健康险', '线上直销', 9260.75, 439816.81, 1, '2025-05-09', '2025-05-10', '2026-05-09', '已过期', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200107, 'P200107', 100076, '朱晓东超', 'e生保百万医疗', '健康险', '电销', 16713.23, 1012166.55, 30, '2023-11-06', '2023-11-07', '2053-11-06', '已过期', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200108, 'P200108', 100049, '郑洋', 'e生保百万医疗', '健康险', '银保', 14259.08, 349375.26, 30, '2024-11-05', '2024-11-06', '2054-11-05', '生效', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200109, 'P200109', 100071, '苏芳', '雇主责任险', '财产险', '银保', 13114.65, 1560876.12, 30, '2026-03-07', '2026-03-08', '2056-03-07', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200110, 'P200110', 100054, '赵子轩', '平安福终身寿险', '寿险', '银保', 1056.09, 41547.63, 20, '2025-04-02', '2025-04-03', '2045-04-02', '生效', '重庆市', '西南');
INSERT INTO `ins_policy` VALUES (200111, 'P200111', 100076, '吕娟艳', '商业车险全保', '车险', '代理人', 313.79, 21786.85, 10, '2024-10-06', '2024-10-07', '2034-10-06', '已过期', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200112, 'P200112', 100008, '卢刚思远', '综合意外险', '意外险', '银保', 5899.65, 142757.90, 5, '2025-10-12', '2025-10-13', '2030-10-12', '生效', '甘肃省', '西北');
INSERT INTO `ins_policy` VALUES (200113, 'P200113', 100050, '邓娜', '雇主责任险', '财产险', '代理人', 6484.33, 761562.79, 5, '2025-07-06', '2025-07-07', '2030-07-06', '退保', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200114, 'P200114', 100034, '赵勇', '家庭财产综合险', '财产险', '代理人', 2230.81, 180703.89, 5, '2025-12-16', '2025-12-17', '2030-12-16', '生效', '湖北省', '华中');
INSERT INTO `ins_policy` VALUES (200115, 'P200115', 100055, '肖军', '增额终身寿2026', '寿险', '电销', 11466.04, 245446.24, 1, '2024-01-23', '2024-01-24', '2025-01-23', '生效', '天津市', '华北');
INSERT INTO `ins_policy` VALUES (200116, 'P200116', 100079, '郑梓涵', '商业车险全保', '车险', '经纪', 17997.47, 1350869.03, 20, '2024-01-25', '2024-01-26', '2044-01-25', '已过期', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200117, 'P200117', 100038, '赵芳', '增额终身寿2026', '寿险', '代理人', 12235.59, 1101458.81, 10, '2025-06-01', '2025-06-02', '2035-06-01', '已过期', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200118, 'P200118', 100060, '卢鑫婷', '货运物流险', '财产险', '银保', 9642.72, 530944.89, 1, '2024-02-28', '2024-02-29', '2025-02-28', '已过期', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200119, 'P200119', 100072, '任娟', '平安福终身寿险', '寿险', '线上直销', 1432.27, 50299.37, 1, '2025-04-12', '2025-04-13', '2026-04-12', '退保', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200120, 'P200120', 100075, '张子轩', '家庭财产综合险', '财产险', '银保', 15450.80, 922089.74, 10, '2025-08-30', '2025-08-31', '2035-08-28', '生效', '河北省', '华北');
INSERT INTO `ins_policy` VALUES (200121, 'P200121', 100039, '苏娜丽', '货运物流险', '财产险', '电销', 13050.72, 1316619.30, 10, '2024-11-02', '2024-11-03', '2034-11-02', '退保', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200122, 'P200122', 100067, '冯桂英涛', '商业车险全保', '车险', '经纪', 12292.28, 327477.34, 1, '2023-09-16', '2023-09-17', '2024-09-16', '退保', '陕西省', '西北');
INSERT INTO `ins_policy` VALUES (200123, 'P200123', 100002, '梁娜', '雇主责任险', '财产险', '电销', 6869.36, 314282.19, 1, '2023-06-22', '2023-06-23', '2024-06-22', '生效', '甘肃省', '西北');
INSERT INTO `ins_policy` VALUES (200124, 'P200124', 100071, '程玲桂英', '商业车险全保', '车险', '代理人', 6524.77, 639815.55, 1, '2024-02-28', '2024-02-29', '2025-02-28', '退保', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200125, 'P200125', 100031, '蔡平', '增额终身寿2026', '寿险', '银保', 6199.07, 372975.72, 1, '2024-08-19', '2024-08-20', '2025-08-19', '退保', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200126, 'P200126', 100068, '赵军宇', '成人意外伤害险', '意外险', '银保', 6927.93, 705003.32, 10, '2024-11-09', '2024-11-10', '2034-11-09', '生效', '天津市', '华北');
INSERT INTO `ins_policy` VALUES (200127, 'P200127', 100076, '胡静', '国寿福满一生', '寿险', '电销', 12654.53, 1142445.15, 1, '2025-09-19', '2025-09-20', '2026-09-19', '退保', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200128, 'P200128', 100074, '孙磊', '百万守护意外险', '意外险', '代理人', 10862.82, 778442.64, 1, '2023-03-17', '2023-03-18', '2024-03-17', '已过期', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200129, 'P200129', 100029, '程军', '综合意外险', '意外险', '银保', 758.62, 86867.51, 5, '2023-03-12', '2023-03-13', '2028-03-12', '生效', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200130, 'P200130', 100027, '郭玲', '雇主责任险', '财产险', '代理人', 17899.36, 812303.59, 30, '2024-01-17', '2024-01-18', '2054-01-17', '生效', '重庆市', '西南');
INSERT INTO `ins_policy` VALUES (200131, 'P200131', 100046, '朱玲鑫', '车损险尊享', '车险', '银保', 13488.02, 622322.85, 1, '2025-06-01', '2025-06-02', '2026-06-01', '生效', '天津市', '华北');
INSERT INTO `ins_policy` VALUES (200132, 'P200132', 100014, '杨玲', '好医保长期医疗', '健康险', '经纪', 15575.74, 1193409.67, 1, '2024-05-03', '2024-05-04', '2025-05-03', '已过期', '湖南省', '华中');
INSERT INTO `ins_policy` VALUES (200133, 'P200133', 100015, '宋桂英晓东', '雇主责任险', '财产险', '电销', 4666.80, 509795.77, 10, '2023-02-02', '2023-02-03', '2033-02-02', '生效', '山东省', '华东');
INSERT INTO `ins_policy` VALUES (200134, 'P200134', 100058, '周鑫', 'e生保百万医疗', '健康险', '线上直销', 9477.41, 460399.22, 30, '2024-12-27', '2024-12-28', '2054-12-27', '生效', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200135, 'P200135', 100025, '吕洋', '重疾无忧A款', '健康险', '经纪', 2356.51, 113917.48, 30, '2025-07-07', '2025-07-08', '2055-07-07', '已过期', '甘肃省', '西北');
INSERT INTO `ins_policy` VALUES (200136, 'P200136', 100013, '徐静', '货运物流险', '财产险', '线上直销', 2945.62, 121423.91, 20, '2024-07-31', '2024-08-01', '2044-07-28', '已过期', '湖北省', '华中');
INSERT INTO `ins_policy` VALUES (200137, 'P200137', 100038, '叶军', '雇主责任险', '财产险', '电销', 7466.60, 614671.16, 30, '2023-04-19', '2023-04-20', '2053-04-19', '生效', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200138, 'P200138', 100072, '任晓东', '成人意外伤害险', '意外险', '线上直销', 5898.43, 591380.85, 1, '2023-07-22', '2023-07-23', '2024-07-22', '生效', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200139, 'P200139', 100068, '姚芳', '交强险', '车险', '银保', 16750.52, 1875355.49, 30, '2024-12-22', '2024-12-23', '2054-12-22', '已过期', '天津市', '华北');
INSERT INTO `ins_policy` VALUES (200140, 'P200140', 100080, '肖桂英建华', 'e生保百万医疗', '健康险', '代理人', 13399.85, 943408.63, 30, '2025-09-28', '2025-09-29', '2055-09-28', '生效', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200141, 'P200141', 100053, '韩勇', '增额终身寿2026', '寿险', '电销', 3711.43, 368824.39, 1, '2025-10-19', '2025-10-20', '2026-10-19', '已过期', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200142, 'P200142', 100060, '马勇', '综合意外险', '意外险', '经纪', 3709.57, 335643.99, 5, '2023-12-29', '2023-12-30', '2028-12-28', '已过期', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200143, 'P200143', 100045, '郑欣怡欣怡', '综合意外险', '意外险', '银保', 11126.21, 351141.83, 1, '2023-03-04', '2023-03-05', '2024-03-04', '生效', '河南省', '华中');
INSERT INTO `ins_policy` VALUES (200144, 'P200144', 100005, '田丽', 'e生保百万医疗', '健康险', '经纪', 600.07, 36862.88, 5, '2023-05-12', '2023-05-13', '2028-05-12', '退保', '山东省', '华东');
INSERT INTO `ins_policy` VALUES (200145, 'P200145', 100041, '沈思远', '百万守护意外险', '意外险', '线上直销', 14438.44, 1724863.91, 20, '2023-09-19', '2023-09-20', '2043-09-19', '已过期', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200146, 'P200146', 100078, '苏磊', '百万守护意外险', '意外险', '线上直销', 5833.79, 597088.79, 10, '2024-01-06', '2024-01-07', '2034-01-06', '退保', '天津市', '华北');
INSERT INTO `ins_policy` VALUES (200147, 'P200147', 100070, '许明鑫', '成人意外伤害险', '意外险', '线上直销', 16804.21, 1686638.71, 30, '2024-12-30', '2024-12-31', '2054-12-28', '已过期', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200148, 'P200148', 100063, '朱杰', '少儿门诊医疗', '健康险', '代理人', 3185.41, 101398.92, 10, '2026-02-25', '2026-02-26', '2036-02-25', '生效', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200149, 'P200149', 100057, '田浩然勇', '交通意外险', '意外险', '经纪', 8973.36, 1055022.17, 1, '2024-12-30', '2024-12-31', '2025-12-28', '退保', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200150, 'P200150', 100017, '田丽', '交通意外险', '意外险', '电销', 16165.04, 1427563.91, 1, '2025-06-13', '2025-06-14', '2026-06-13', '退保', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200151, 'P200151', 100027, '任敏', '重疾无忧A款', '健康险', '电销', 1506.11, 57683.63, 30, '2024-08-03', '2024-08-04', '2054-08-03', '退保', '重庆市', '西南');
INSERT INTO `ins_policy` VALUES (200152, 'P200152', 100074, '王超玲', '少儿门诊医疗', '健康险', '经纪', 6062.53, 596604.29, 5, '2026-01-24', '2026-01-25', '2031-01-24', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200153, 'P200153', 100080, '唐宇', '国寿福满一生', '寿险', '电销', 11599.98, 1027660.49, 20, '2024-11-02', '2024-11-03', '2044-11-02', '已过期', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200154, 'P200154', 100016, '卢涛娟', '交强险', '车险', '线上直销', 4535.92, 258834.40, 1, '2025-09-13', '2025-09-14', '2026-09-13', '生效', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200155, 'P200155', 100080, '袁宇', '交强险', '车险', '代理人', 14671.58, 560751.29, 1, '2025-01-30', '2025-01-31', '2026-01-28', '退保', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200156, 'P200156', 100026, '吕军强', '货运物流险', '财产险', '银保', 1452.49, 35086.61, 20, '2023-10-26', '2023-10-27', '2043-10-26', '生效', '福建省', '华南');
INSERT INTO `ins_policy` VALUES (200157, 'P200157', 100061, '吴敏', '国寿福满一生', '寿险', '经纪', 9788.79, 506644.84, 10, '2024-11-10', '2024-11-11', '2034-11-10', '已过期', '河北省', '华北');
INSERT INTO `ins_policy` VALUES (200158, 'P200158', 100010, '袁刚', '综合意外险', '意外险', '银保', 10391.10, 379960.18, 10, '2026-04-15', '2026-04-16', '2036-04-15', '生效', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200159, 'P200159', 100035, '何桂英', '雇主责任险', '财产险', '经纪', 6747.65, 682797.18, 30, '2024-02-15', '2024-02-16', '2054-02-15', '退保', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200160, 'P200160', 100066, '李超明', '车损险尊享', '车险', '代理人', 7174.71, 257931.76, 10, '2024-10-15', '2024-10-16', '2034-10-15', '生效', '陕西省', '西北');
INSERT INTO `ins_policy` VALUES (200161, 'P200161', 100011, '陈梓涵', '商业车险全保', '车险', '经纪', 7638.37, 318602.39, 5, '2026-03-02', '2026-03-03', '2031-03-02', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200162, 'P200162', 100050, '林宇', '百万守护意外险', '意外险', '电销', 2159.99, 58100.07, 20, '2025-08-26', '2025-08-27', '2045-08-26', '生效', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200163, 'P200163', 100011, '周娜', '雇主责任险', '财产险', '代理人', 1500.41, 132941.04, 1, '2024-09-30', '2024-10-01', '2025-09-28', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200164, 'P200164', 100012, '朱静', '第三者责任险', '车险', '代理人', 14746.65, 295344.14, 1, '2024-01-24', '2024-01-25', '2025-01-24', '退保', '北京市', '华北');
INSERT INTO `ins_policy` VALUES (200165, 'P200165', 100063, '郭桂英', '综合意外险', '意外险', '电销', 3028.17, 102017.51, 5, '2023-12-18', '2023-12-19', '2028-12-18', '已过期', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200166, 'P200166', 100020, '徐梓涵', '综合意外险', '意外险', '银保', 11767.26, 540327.03, 20, '2023-09-17', '2023-09-18', '2043-09-17', '生效', '重庆市', '西南');
INSERT INTO `ins_policy` VALUES (200167, 'P200167', 100003, '胡霞', '少儿门诊医疗', '健康险', '电销', 14432.26, 1160495.77, 20, '2025-04-19', '2025-04-20', '2045-04-19', '退保', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200168, 'P200168', 100011, '丁鑫宇', '平安福终身寿险', '寿险', '线上直销', 1423.59, 122301.92, 10, '2026-02-11', '2026-02-12', '2036-02-11', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200169, 'P200169', 100015, '刘建华', '重疾无忧A款', '健康险', '银保', 2351.10, 64935.23, 30, '2025-05-08', '2025-05-09', '2055-05-08', '退保', '山东省', '华东');
INSERT INTO `ins_policy` VALUES (200170, 'P200170', 100062, '田洋', '车损险尊享', '车险', '电销', 17843.61, 1979911.50, 5, '2025-12-21', '2025-12-22', '2030-12-21', '生效', '重庆市', '西南');
INSERT INTO `ins_policy` VALUES (200171, 'P200171', 100031, '苏芳浩然', '百万守护意外险', '意外险', '银保', 17422.52, 578822.53, 1, '2023-06-25', '2023-06-26', '2024-06-25', '生效', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200172, 'P200172', 100074, '蒋桂英婷', '企业财产基本险', '财产险', '代理人', 14764.82, 362285.45, 1, '2023-09-21', '2023-09-22', '2024-09-21', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200173, 'P200173', 100003, '高欣怡嘉怡', '成人意外伤害险', '意外险', '电销', 1260.20, 112041.19, 1, '2024-05-24', '2024-05-25', '2025-05-24', '生效', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200174, 'P200174', 100025, '周强', '交强险', '车险', '线上直销', 11140.98, 284282.41, 10, '2025-03-05', '2025-03-06', '2035-03-05', '已过期', '甘肃省', '西北');
INSERT INTO `ins_policy` VALUES (200175, 'P200175', 100078, '潘子轩晓东', '增额终身寿2026', '寿险', '电销', 7735.83, 876034.37, 30, '2025-12-12', '2025-12-13', '2055-12-12', '生效', '天津市', '华北');
INSERT INTO `ins_policy` VALUES (200176, 'P200176', 100069, '罗杰', '金佑人生两全险', '寿险', '代理人', 11142.47, 951678.02, 5, '2024-12-01', '2024-12-02', '2029-12-01', '已过期', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200177, 'P200177', 100007, '姚梓涵', '交强险', '车险', '银保', 5917.94, 688640.45, 10, '2025-09-25', '2025-09-26', '2035-09-25', '生效', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200178, 'P200178', 100018, '曹刚', '企业财产基本险', '财产险', '代理人', 7021.91, 322674.12, 1, '2023-06-02', '2023-06-03', '2024-06-02', '生效', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200179, 'P200179', 100078, '叶伟桂英', '百万守护意外险', '意外险', '电销', 13629.96, 851633.25, 30, '2023-12-26', '2023-12-27', '2053-12-26', '已过期', '天津市', '华北');
INSERT INTO `ins_policy` VALUES (200180, 'P200180', 100055, '邓欣怡杰', '货运物流险', '财产险', '银保', 6551.14, 335160.32, 5, '2023-11-20', '2023-11-21', '2028-11-20', '生效', '天津市', '华北');
INSERT INTO `ins_policy` VALUES (200181, 'P200181', 100029, '魏丽', '成人意外伤害险', '意外险', '经纪', 4629.41, 545390.50, 1, '2026-02-13', '2026-02-14', '2027-02-13', '已过期', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200182, 'P200182', 100001, '冯敏晓东', '成人意外伤害险', '意外险', '代理人', 14521.42, 579030.85, 5, '2023-04-10', '2023-04-11', '2028-04-10', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200183, 'P200183', 100043, '曹刚', '重疾无忧A款', '健康险', '代理人', 11124.21, 946713.01, 1, '2024-10-25', '2024-10-26', '2025-10-25', '生效', '陕西省', '西北');
INSERT INTO `ins_policy` VALUES (200184, 'P200184', 100009, '郑伟霞', '增额终身寿2026', '寿险', '电销', 13078.73, 893886.44, 1, '2024-06-23', '2024-06-24', '2025-06-23', '生效', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200185, 'P200185', 100067, '魏洋', 'e生保百万医疗', '健康险', '线上直销', 9735.27, 433065.09, 10, '2024-01-21', '2024-01-22', '2034-01-21', '已过期', '陕西省', '西北');
INSERT INTO `ins_policy` VALUES (200186, 'P200186', 100047, '冯辉', '企业财产基本险', '财产险', '线上直销', 3187.25, 104729.28, 1, '2023-12-16', '2023-12-17', '2024-12-16', '已过期', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200187, 'P200187', 100012, '沈涛', '商业车险全保', '车险', '电销', 14194.10, 1534548.20, 10, '2024-11-11', '2024-11-12', '2034-11-11', '生效', '北京市', '华北');
INSERT INTO `ins_policy` VALUES (200188, 'P200188', 100048, '魏婷', '企业财产基本险', '财产险', '代理人', 16359.67, 455771.58, 5, '2025-07-07', '2025-07-08', '2030-07-07', '退保', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200189, 'P200189', 100007, '何建华', '金佑人生两全险', '寿险', '银保', 14361.82, 483417.26, 1, '2023-06-14', '2023-06-15', '2024-06-14', '生效', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200190, 'P200190', 100018, '曾玲嘉怡', '重疾无忧A款', '健康险', '银保', 363.96, 8543.59, 5, '2024-02-23', '2024-02-24', '2029-02-23', '已过期', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200191, 'P200191', 100027, '陈军', '商业车险全保', '车险', '线上直销', 9348.46, 632629.61, 1, '2025-10-29', '2025-10-30', '2026-10-28', '已过期', '重庆市', '西南');
INSERT INTO `ins_policy` VALUES (200192, 'P200192', 100013, '赵超', '平安福终身寿险', '寿险', '代理人', 14460.76, 1153484.94, 1, '2026-02-28', '2026-03-01', '2027-02-28', '生效', '湖北省', '华中');
INSERT INTO `ins_policy` VALUES (200193, 'P200193', 100060, '唐艳晓东', '家庭财产综合险', '财产险', '线上直销', 12722.85, 932319.78, 30, '2024-08-10', '2024-08-11', '2054-08-10', '已过期', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200194, 'P200194', 100025, '吴艳霞', '重疾无忧A款', '健康险', '电销', 10456.74, 879297.14, 30, '2023-11-16', '2023-11-17', '2053-11-16', '生效', '甘肃省', '西北');
INSERT INTO `ins_policy` VALUES (200195, 'P200195', 100033, '彭桂英', '货运物流险', '财产险', '电销', 8543.93, 684899.64, 10, '2026-02-24', '2026-02-25', '2036-02-24', '已过期', '上海市', '华东');
INSERT INTO `ins_policy` VALUES (200196, 'P200196', 100068, '杜明', '好医保长期医疗', '健康险', '线上直销', 3072.66, 299527.31, 1, '2024-02-12', '2024-02-13', '2025-02-12', '生效', '天津市', '华北');
INSERT INTO `ins_policy` VALUES (200197, 'P200197', 100001, '朱娜', '少儿门诊医疗', '健康险', '银保', 1525.39, 129366.44, 1, '2023-06-09', '2023-06-10', '2024-06-09', '已过期', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200198, 'P200198', 100029, '苏宇', '雇主责任险', '财产险', '电销', 14564.09, 1595436.00, 1, '2023-09-22', '2023-09-23', '2024-09-22', '生效', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200199, 'P200199', 100013, '蒋娜', '成人意外伤害险', '意外险', '代理人', 10603.06, 610013.93, 30, '2024-03-30', '2024-03-31', '2054-03-28', '生效', '湖北省', '华中');
INSERT INTO `ins_policy` VALUES (200200, 'P200200', 100007, '彭鑫', '雇主责任险', '财产险', '线上直销', 5867.64, 402722.27, 30, '2025-10-22', '2025-10-23', '2055-10-22', '生效', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200201, 'P200201', 100049, '丁强', '商业车险全保', '车险', '经纪', 9857.01, 802135.16, 30, '2023-08-13', '2023-08-14', '2053-08-13', '退保', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200202, 'P200202', 100035, '丁敏', '车损险尊享', '车险', '代理人', 6965.11, 282373.42, 10, '2025-01-04', '2025-01-05', '2035-01-04', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200203, 'P200203', 100016, '肖婷宇', '货运物流险', '财产险', '电销', 14421.60, 1037274.00, 30, '2023-05-11', '2023-05-12', '2053-05-11', '已过期', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200204, 'P200204', 100020, '叶雪磊', '平安福终身寿险', '寿险', '代理人', 666.28, 32170.39, 10, '2023-10-02', '2023-10-03', '2033-10-02', '生效', '重庆市', '西南');
INSERT INTO `ins_policy` VALUES (200205, 'P200205', 100068, '梁辉', '家庭财产综合险', '财产险', '电销', 4757.50, 251031.74, 10, '2024-08-12', '2024-08-13', '2034-08-12', '已过期', '天津市', '华北');
INSERT INTO `ins_policy` VALUES (200206, 'P200206', 100045, '袁欣怡嘉怡', '平安福终身寿险', '寿险', '电销', 17255.05, 1541666.89, 10, '2023-08-06', '2023-08-07', '2033-08-06', '生效', '河南省', '华中');
INSERT INTO `ins_policy` VALUES (200207, 'P200207', 100024, '曾静', '第三者责任险', '车险', '银保', 10087.49, 702239.61, 10, '2023-01-16', '2023-01-17', '2033-01-16', '生效', '湖北省', '华中');
INSERT INTO `ins_policy` VALUES (200208, 'P200208', 100040, '郭鑫', '商业车险全保', '车险', '经纪', 2038.36, 142819.06, 1, '2023-11-05', '2023-11-06', '2024-11-05', '生效', '陕西省', '西北');
INSERT INTO `ins_policy` VALUES (200209, 'P200209', 100067, '吕平', '企业财产基本险', '财产险', '经纪', 1572.88, 138455.26, 5, '2024-07-08', '2024-07-09', '2029-07-08', '已过期', '陕西省', '西北');
INSERT INTO `ins_policy` VALUES (200210, 'P200210', 100053, '潘娜', '好医保长期医疗', '健康险', '银保', 5059.08, 564983.64, 20, '2024-03-05', '2024-03-06', '2044-03-05', '退保', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200211, 'P200211', 100052, '程杰', '交强险', '车险', '银保', 14433.08, 1635681.20, 10, '2023-04-03', '2023-04-04', '2033-04-03', '退保', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200212, 'P200212', 100013, '郭勇', 'e生保百万医疗', '健康险', '线上直销', 821.96, 95405.84, 1, '2023-06-24', '2023-06-25', '2024-06-24', '退保', '湖北省', '华中');
INSERT INTO `ins_policy` VALUES (200213, 'P200213', 100013, '邓宇伟', '综合意外险', '意外险', '代理人', 16280.37, 1909468.74, 10, '2023-08-14', '2023-08-15', '2033-08-14', '退保', '湖北省', '华中');
INSERT INTO `ins_policy` VALUES (200214, 'P200214', 100028, '魏娟', '交通意外险', '意外险', '银保', 2480.70, 292979.78, 10, '2025-02-25', '2025-02-26', '2035-02-25', '退保', '河南省', '华中');
INSERT INTO `ins_policy` VALUES (200215, 'P200215', 100023, '郑敏', '综合意外险', '意外险', '银保', 13243.39, 1211401.76, 10, '2025-09-07', '2025-09-08', '2035-09-07', '已过期', '河北省', '华北');
INSERT INTO `ins_policy` VALUES (200216, 'P200216', 100060, '姚玲', '金佑人生两全险', '寿险', '银保', 607.72, 58313.21, 10, '2025-05-18', '2025-05-19', '2035-05-18', '已过期', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200217, 'P200217', 100074, '何欣怡', '国寿福满一生', '寿险', '银保', 7706.28, 760501.67, 30, '2023-12-01', '2023-12-02', '2053-12-01', '退保', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200218, 'P200218', 100055, '潘涛建华', '增额终身寿2026', '寿险', '电销', 8362.62, 211025.86, 20, '2025-06-30', '2025-07-01', '2045-06-28', '生效', '天津市', '华北');
INSERT INTO `ins_policy` VALUES (200219, 'P200219', 100053, '田勇', '综合意外险', '意外险', '线上直销', 6615.01, 708738.31, 30, '2024-03-27', '2024-03-28', '2054-03-27', '生效', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200220, 'P200220', 100006, '于雪丽', '少儿门诊医疗', '健康险', '线上直销', 6631.47, 371151.54, 1, '2023-05-31', '2023-06-01', '2024-05-28', '生效', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200221, 'P200221', 100024, '王强', '少儿门诊医疗', '健康险', '代理人', 1012.63, 24894.77, 1, '2024-01-11', '2024-01-12', '2025-01-11', '生效', '湖北省', '华中');
INSERT INTO `ins_policy` VALUES (200222, 'P200222', 100058, '田平', '少儿门诊医疗', '健康险', '线上直销', 4576.37, 214942.38, 20, '2025-06-01', '2025-06-02', '2045-06-01', '退保', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200223, 'P200223', 100075, '朱子轩', '车损险尊享', '车险', '代理人', 7312.48, 869693.29, 1, '2025-07-15', '2025-07-16', '2026-07-15', '生效', '河北省', '华北');
INSERT INTO `ins_policy` VALUES (200224, 'P200224', 100009, '刘思远艳', '成人意外伤害险', '意外险', '线上直销', 9275.38, 758162.80, 30, '2024-05-06', '2024-05-07', '2054-05-06', '退保', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200225, 'P200225', 100062, '邓伟', '成人意外伤害险', '意外险', '银保', 9220.35, 1047256.38, 30, '2023-04-18', '2023-04-19', '2053-04-18', '生效', '重庆市', '西南');
INSERT INTO `ins_policy` VALUES (200226, 'P200226', 100010, '马浩然', '第三者责任险', '车险', '线上直销', 10597.56, 1100935.86, 1, '2026-05-22', '2026-05-23', '2027-05-22', '退保', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200227, 'P200227', 100008, '吕杰艳', '货运物流险', '财产险', '银保', 2468.61, 196693.46, 1, '2025-05-08', '2025-05-09', '2026-05-08', '生效', '甘肃省', '西北');
INSERT INTO `ins_policy` VALUES (200228, 'P200228', 100015, '田建华桂英', '第三者责任险', '车险', '代理人', 3597.00, 156143.27, 1, '2024-09-11', '2024-09-12', '2025-09-11', '已过期', '山东省', '华东');
INSERT INTO `ins_policy` VALUES (200229, 'P200229', 100025, '许嘉怡', '金佑人生两全险', '寿险', '线上直销', 2123.12, 238826.58, 30, '2025-08-19', '2025-08-20', '2055-08-19', '退保', '甘肃省', '西北');
INSERT INTO `ins_policy` VALUES (200230, 'P200230', 100035, '曾玲玲', '重疾无忧A款', '健康险', '代理人', 14355.19, 361770.11, 1, '2026-01-05', '2026-01-06', '2027-01-05', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200231, 'P200231', 100078, '刘强嘉怡', '金佑人生两全险', '寿险', '线上直销', 17011.99, 1901267.79, 5, '2025-08-23', '2025-08-24', '2030-08-23', '生效', '天津市', '华北');
INSERT INTO `ins_policy` VALUES (200232, 'P200232', 100025, '袁超', '平安福终身寿险', '寿险', '代理人', 8339.78, 499409.01, 10, '2025-07-29', '2025-07-30', '2035-07-28', '已过期', '甘肃省', '西北');
INSERT INTO `ins_policy` VALUES (200233, 'P200233', 100067, '董梓涵', '重疾无忧A款', '健康险', '代理人', 10659.35, 937540.44, 1, '2024-10-11', '2024-10-12', '2025-10-11', '生效', '陕西省', '西北');
INSERT INTO `ins_policy` VALUES (200234, 'P200234', 100059, '彭思远', '货运物流险', '财产险', '经纪', 15371.33, 688323.25, 1, '2026-01-08', '2026-01-09', '2027-01-08', '生效', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200235, 'P200235', 100040, '冯婷', '平安福终身寿险', '寿险', '代理人', 10926.10, 1018250.11, 20, '2025-09-04', '2025-09-05', '2045-09-04', '已过期', '陕西省', '西北');
INSERT INTO `ins_policy` VALUES (200236, 'P200236', 100048, '陈娜', '综合意外险', '意外险', '银保', 10306.03, 1056420.39, 1, '2023-09-21', '2023-09-22', '2024-09-21', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200237, 'P200237', 100004, '苏玲', '国寿福满一生', '寿险', '电销', 5088.92, 345185.42, 1, '2023-10-15', '2023-10-16', '2024-10-15', '生效', '河北省', '华北');
INSERT INTO `ins_policy` VALUES (200238, 'P200238', 100038, '何强', '家庭财产综合险', '财产险', '经纪', 14292.05, 359513.05, 30, '2024-11-18', '2024-11-19', '2054-11-18', '生效', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200239, 'P200239', 100025, '周艳', '企业财产基本险', '财产险', '经纪', 2150.39, 86960.73, 1, '2025-03-14', '2025-03-15', '2026-03-14', '生效', '甘肃省', '西北');
INSERT INTO `ins_policy` VALUES (200240, 'P200240', 100027, '丁刚娜', '平安福终身寿险', '寿险', '银保', 11874.64, 594876.72, 1, '2023-02-16', '2023-02-17', '2024-02-16', '生效', '重庆市', '西南');
INSERT INTO `ins_policy` VALUES (200241, 'P200241', 100058, '黄辉', '国寿福满一生', '寿险', '线上直销', 4175.45, 137063.09, 5, '2024-12-15', '2024-12-16', '2029-12-15', '生效', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200242, 'P200242', 100051, '丁超', '好医保长期医疗', '健康险', '银保', 3486.63, 297267.44, 20, '2024-07-24', '2024-07-25', '2044-07-24', '生效', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200243, 'P200243', 100029, '于思远', '雇主责任险', '财产险', '电销', 2797.26, 117990.69, 1, '2026-03-05', '2026-03-06', '2027-03-05', '生效', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200244, 'P200244', 100032, '郑磊洋', '综合意外险', '意外险', '代理人', 16657.51, 726913.68, 1, '2025-04-25', '2025-04-26', '2026-04-25', '生效', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200245, 'P200245', 100013, '丁鑫思远', '商业车险全保', '车险', '经纪', 6766.42, 171928.80, 10, '2023-05-26', '2023-05-27', '2033-05-26', '已过期', '湖北省', '华中');
INSERT INTO `ins_policy` VALUES (200246, 'P200246', 100006, '孙强', '百万守护意外险', '意外险', '银保', 11301.50, 1300447.44, 20, '2025-03-28', '2025-03-29', '2045-03-28', '生效', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200247, 'P200247', 100012, '何鑫芳', '成人意外伤害险', '意外险', '代理人', 1306.83, 44918.54, 20, '2025-03-09', '2025-03-10', '2045-03-09', '生效', '北京市', '华北');
INSERT INTO `ins_policy` VALUES (200248, 'P200248', 100070, '黄嘉怡艳', '家庭财产综合险', '财产险', '经纪', 16090.18, 419989.94, 1, '2024-09-09', '2024-09-10', '2025-09-09', '已过期', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200249, 'P200249', 100020, '徐丽', '第三者责任险', '车险', '线上直销', 8730.75, 843157.29, 5, '2023-05-09', '2023-05-10', '2028-05-09', '退保', '重庆市', '西南');
INSERT INTO `ins_policy` VALUES (200250, 'P200250', 100065, '徐梓涵', '货运物流险', '财产险', '线上直销', 9484.89, 474191.52, 20, '2026-04-26', '2026-04-27', '2046-04-26', '生效', '海南省', '华南');
INSERT INTO `ins_policy` VALUES (200251, 'P200251', 100015, '叶娟雪', '国寿福满一生', '寿险', '代理人', 17063.41, 489848.13, 1, '2024-10-22', '2024-10-23', '2025-10-22', '生效', '山东省', '华东');
INSERT INTO `ins_policy` VALUES (200252, 'P200252', 100039, '田玲丽', '百万守护意外险', '意外险', '代理人', 2722.93, 75285.64, 20, '2025-02-03', '2025-02-04', '2045-02-03', '生效', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200253, 'P200253', 100068, '徐丽宇', '商业车险全保', '车险', '线上直销', 9756.88, 477607.27, 10, '2024-04-09', '2024-04-10', '2034-04-09', '生效', '天津市', '华北');
INSERT INTO `ins_policy` VALUES (200254, 'P200254', 100079, '胡娜', '第三者责任险', '车险', '经纪', 7893.68, 803231.24, 10, '2023-12-23', '2023-12-24', '2033-12-23', '生效', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200255, 'P200255', 100077, '唐浩然超', '金佑人生两全险', '寿险', '经纪', 8578.04, 304631.02, 1, '2024-10-30', '2024-10-31', '2025-10-28', '生效', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200256, 'P200256', 100007, '何欣怡', 'e生保百万医疗', '健康险', '线上直销', 6143.54, 322877.65, 5, '2025-03-30', '2025-03-31', '2030-03-28', '退保', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200257, 'P200257', 100025, '董辉思远', '货运物流险', '财产险', '线上直销', 2166.14, 206015.08, 10, '2024-06-20', '2024-06-21', '2034-06-20', '生效', '甘肃省', '西北');
INSERT INTO `ins_policy` VALUES (200258, 'P200258', 100079, '朱嘉怡', '交通意外险', '意外险', '代理人', 11115.22, 619017.66, 20, '2024-02-20', '2024-02-21', '2044-02-20', '生效', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200259, 'P200259', 100052, '马平思远', 'e生保百万医疗', '健康险', '电销', 7254.86, 224123.02, 30, '2023-04-21', '2023-04-22', '2053-04-21', '生效', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200260, 'P200260', 100063, '张梓涵', '重疾无忧A款', '健康险', '代理人', 15131.15, 497754.51, 30, '2023-05-18', '2023-05-19', '2053-05-18', '生效', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200261, 'P200261', 100042, '杨玲', '第三者责任险', '车险', '线上直销', 13874.04, 599878.76, 10, '2024-09-13', '2024-09-14', '2034-09-13', '生效', '湖北省', '华中');
INSERT INTO `ins_policy` VALUES (200262, 'P200262', 100067, '林明', '百万守护意外险', '意外险', '经纪', 13116.79, 1042261.85, 30, '2025-12-25', '2025-12-26', '2055-12-25', '退保', '陕西省', '西北');
INSERT INTO `ins_policy` VALUES (200263, 'P200263', 100024, '韩洋', '平安福终身寿险', '寿险', '银保', 1906.33, 84762.79, 5, '2023-08-08', '2023-08-09', '2028-08-08', '生效', '湖北省', '华中');
INSERT INTO `ins_policy` VALUES (200264, 'P200264', 100029, '袁军', '交通意外险', '意外险', '代理人', 1287.52, 50588.72, 5, '2025-06-14', '2025-06-15', '2030-06-14', '已过期', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200265, 'P200265', 100008, '余超', '少儿门诊医疗', '健康险', '银保', 12226.39, 536188.88, 10, '2024-11-30', '2024-12-01', '2034-11-28', '生效', '甘肃省', '西北');
INSERT INTO `ins_policy` VALUES (200266, 'P200266', 100037, '肖丽', '家庭财产综合险', '财产险', '经纪', 14881.32, 1401161.03, 20, '2023-04-07', '2023-04-08', '2043-04-07', '生效', '甘肃省', '西北');
INSERT INTO `ins_policy` VALUES (200267, 'P200267', 100026, '袁婷', '成人意外伤害险', '意外险', '线上直销', 1356.95, 131901.73, 20, '2024-01-15', '2024-01-16', '2044-01-15', '生效', '福建省', '华南');
INSERT INTO `ins_policy` VALUES (200268, 'P200268', 100008, '吕子轩雪', '企业财产基本险', '财产险', '经纪', 15859.41, 1592580.83, 1, '2024-11-19', '2024-11-20', '2025-11-19', '已过期', '甘肃省', '西北');
INSERT INTO `ins_policy` VALUES (200269, 'P200269', 100048, '魏欣怡平', '交强险', '车险', '电销', 12406.46, 1102394.06, 20, '2026-01-31', '2026-02-01', '2046-01-28', '生效', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200270, 'P200270', 100018, '林霞思远', '平安福终身寿险', '寿险', '线上直销', 8371.41, 401938.66, 5, '2023-05-01', '2023-05-02', '2028-05-01', '生效', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200271, 'P200271', 100066, '徐霞娟', '成人意外伤害险', '意外险', '线上直销', 5059.20, 394149.12, 30, '2024-03-04', '2024-03-05', '2054-03-04', '生效', '陕西省', '西北');
INSERT INTO `ins_policy` VALUES (200272, 'P200272', 100069, '郑芳', 'e生保百万医疗', '健康险', '电销', 8848.45, 296820.72, 30, '2026-04-30', '2026-05-01', '2056-04-28', '生效', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200273, 'P200273', 100023, '周军', '雇主责任险', '财产险', '代理人', 729.01, 43398.01, 20, '2024-04-11', '2024-04-12', '2044-04-11', '退保', '河北省', '华北');
INSERT INTO `ins_policy` VALUES (200274, 'P200274', 100027, '宋欣怡', '百万守护意外险', '意外险', '银保', 10104.99, 551910.35, 20, '2024-03-09', '2024-03-10', '2044-03-09', '已过期', '重庆市', '西南');
INSERT INTO `ins_policy` VALUES (200275, 'P200275', 100002, '肖平', '重疾无忧A款', '健康险', '电销', 5945.85, 145654.97, 1, '2024-07-10', '2024-07-11', '2025-07-10', '生效', '甘肃省', '西北');
INSERT INTO `ins_policy` VALUES (200276, 'P200276', 100018, '田娜', '百万守护意外险', '意外险', '代理人', 4567.79, 149076.71, 10, '2024-01-24', '2024-01-25', '2034-01-24', '生效', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200277, 'P200277', 100011, '董强', '国寿福满一生', '寿险', '经纪', 2372.83, 102749.79, 20, '2023-11-11', '2023-11-12', '2043-11-11', '已过期', '浙江省', '华东');
INSERT INTO `ins_policy` VALUES (200278, 'P200278', 100003, '蔡霞', '企业财产基本险', '财产险', '线上直销', 17151.63, 421755.75, 1, '2025-08-16', '2025-08-17', '2026-08-16', '生效', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200279, 'P200279', 100038, '肖芳', '企业财产基本险', '财产险', '经纪', 15289.37, 1554355.96, 5, '2024-12-04', '2024-12-05', '2029-12-04', '已过期', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200280, 'P200280', 100072, '罗伟', '雇主责任险', '财产险', '经纪', 2600.00, 222093.07, 10, '2023-10-21', '2023-10-22', '2033-10-21', '生效', '云南省', '西南');
INSERT INTO `ins_policy` VALUES (200281, 'P200281', 100014, '宋敏洋', '货运物流险', '财产险', '线上直销', 8163.27, 237050.19, 5, '2023-04-23', '2023-04-24', '2028-04-23', '生效', '湖南省', '华中');
INSERT INTO `ins_policy` VALUES (200282, 'P200282', 100051, '唐浩然', '家庭财产综合险', '财产险', '代理人', 1838.17, 89009.93, 5, '2024-12-19', '2024-12-20', '2029-12-19', '生效', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200283, 'P200283', 100033, '程涛', '交通意外险', '意外险', '电销', 8016.86, 632387.25, 1, '2026-03-18', '2026-03-19', '2027-03-18', '退保', '上海市', '华东');
INSERT INTO `ins_policy` VALUES (200284, 'P200284', 100004, '董涛勇', 'e生保百万医疗', '健康险', '代理人', 4798.73, 310711.76, 20, '2023-08-05', '2023-08-06', '2043-08-05', '生效', '河北省', '华北');
INSERT INTO `ins_policy` VALUES (200285, 'P200285', 100055, '韩浩然', '少儿门诊医疗', '健康险', '银保', 3187.88, 124269.30, 20, '2023-09-11', '2023-09-12', '2043-09-11', '生效', '天津市', '华北');
INSERT INTO `ins_policy` VALUES (200286, 'P200286', 100076, '曹思远', '交强险', '车险', '经纪', 10095.00, 858783.37, 1, '2025-08-07', '2025-08-08', '2026-08-07', '退保', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200287, 'P200287', 100041, '郑静', '商业车险全保', '车险', '银保', 6499.72, 363116.50, 30, '2025-12-20', '2025-12-21', '2055-12-20', '生效', '山西省', '华北');
INSERT INTO `ins_policy` VALUES (200288, 'P200288', 100018, '王艳', '家庭财产综合险', '财产险', '银保', 13358.79, 612356.87, 30, '2024-12-28', '2024-12-29', '2054-12-28', '生效', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200289, 'P200289', 100046, '苏嘉怡', '增额终身寿2026', '寿险', '经纪', 3266.28, 144114.04, 10, '2026-04-20', '2026-04-21', '2036-04-20', '生效', '天津市', '华北');
INSERT INTO `ins_policy` VALUES (200290, 'P200290', 100003, '梁磊', '少儿门诊医疗', '健康险', '银保', 17768.79, 385975.90, 30, '2025-01-20', '2025-01-21', '2055-01-20', '生效', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200291, 'P200291', 100007, '程子轩', '企业财产基本险', '财产险', '线上直销', 11554.43, 1298203.11, 10, '2023-06-16', '2023-06-17', '2033-06-16', '生效', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200292, 'P200292', 100012, '沈勇静', '货运物流险', '财产险', '代理人', 11396.32, 740054.99, 20, '2023-09-11', '2023-09-12', '2043-09-11', '生效', '北京市', '华北');
INSERT INTO `ins_policy` VALUES (200293, 'P200293', 100010, '邓磊思远', '重疾无忧A款', '健康险', '电销', 8717.94, 808813.33, 1, '2023-01-27', '2023-01-28', '2024-01-27', '生效', '江苏省', '华东');
INSERT INTO `ins_policy` VALUES (200294, 'P200294', 100031, '卢子轩', '百万守护意外险', '意外险', '银保', 5170.94, 339147.94, 1, '2026-05-06', '2026-05-07', '2027-05-06', '生效', '四川省', '西南');
INSERT INTO `ins_policy` VALUES (200295, 'P200295', 100019, '韩超', '好医保长期医疗', '健康险', '经纪', 10518.55, 333444.87, 30, '2024-11-02', '2024-11-03', '2054-11-02', '生效', '北京市', '华北');
INSERT INTO `ins_policy` VALUES (200296, 'P200296', 100005, '杜勇梓涵', '平安福终身寿险', '寿险', '经纪', 5290.39, 422514.05, 30, '2025-01-22', '2025-01-23', '2055-01-22', '退保', '山东省', '华东');
INSERT INTO `ins_policy` VALUES (200297, 'P200297', 100024, '高芳', '增额终身寿2026', '寿险', '电销', 2260.20, 199843.67, 5, '2024-10-22', '2024-10-23', '2029-10-22', '已过期', '湖北省', '华中');
INSERT INTO `ins_policy` VALUES (200298, 'P200298', 100016, '张明', '雇主责任险', '财产险', '代理人', 10143.79, 787636.98, 20, '2025-06-29', '2025-06-30', '2045-06-28', '生效', '广东省', '华南');
INSERT INTO `ins_policy` VALUES (200299, 'P200299', 100045, '赵娟娜', '重疾无忧A款', '健康险', '经纪', 1421.13, 42498.47, 1, '2026-01-28', '2026-01-29', '2027-01-28', '已过期', '河南省', '华中');
INSERT INTO `ins_policy` VALUES (200300, 'P200300', 100080, '孙杰', '增额终身寿2026', '寿险', '经纪', 3405.68, 326207.68, 5, '2025-09-19', '2025-09-20', '2030-09-19', '已过期', '山西省', '华北');

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- 敏感词内置分类字典（可选；权限管理「敏感词过滤」从此读取，可在「字典管理」维护增删词）
-- dict_code 以 c2b_sens_ 开头即一个分类；优先写积木 jimu_dict，纯 jeecg 环境可改写 sys_dict/sys_dict_item
-- ----------------------------
DELETE i FROM jimu_dict_item i JOIN jimu_dict d ON i.dict_id=d.id WHERE d.dict_code='c2b_sens_politics';
DELETE FROM jimu_dict WHERE dict_code='c2b_sens_politics';
INSERT INTO `jimu_dict`(`id`,`dict_name`,`dict_code`,`description`,`del_flag`,`create_by`,`create_time`,`type`,`tenant_id`) VALUES ('c2b_sens_politics_dict0001', '政治敏感', 'c2b_sens_politics', 'Chat2BI 敏感词-政治敏感', 0, 'admin', '2026-05-25 12:00:00', 0, NULL);
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_politics_diit000', 'c2b_sens_politics_dict0001', '领导', '领导', 1, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_politics_diit001', 'c2b_sens_politics_dict0001', '书记', '书记', 2, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_politics_diit002', 'c2b_sens_politics_dict0001', '市长', '市长', 3, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_politics_diit003', 'c2b_sens_politics_dict0001', '省长', '省长', 4, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_politics_diit004', 'c2b_sens_politics_dict0001', '县长', '县长', 5, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_politics_diit005', 'c2b_sens_politics_dict0001', '部长', '部长', 6, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_politics_diit006', 'c2b_sens_politics_dict0001', '主席', '主席', 7, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_politics_diit007', 'c2b_sens_politics_dict0001', '总理', '总理', 8, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_politics_diit008', 'c2b_sens_politics_dict0001', '省委', '省委', 9, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_politics_diit009', 'c2b_sens_politics_dict0001', '市委', '市委', 10, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_politics_diit010', 'c2b_sens_politics_dict0001', '中央', '中央', 11, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_politics_diit011', 'c2b_sens_politics_dict0001', '国务院', '国务院', 12, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_politics_diit012', 'c2b_sens_politics_dict0001', '政府', '政府', 13, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_politics_diit013', 'c2b_sens_politics_dict0001', '政治', '政治', 14, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_politics_diit014', 'c2b_sens_politics_dict0001', '政变', '政变', 15, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_politics_diit015', 'c2b_sens_politics_dict0001', '游行', '游行', 16, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_politics_diit016', 'c2b_sens_politics_dict0001', '示威', '示威', 17, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_politics_diit017', 'c2b_sens_politics_dict0001', '颠覆', '颠覆', 18, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_politics_diit018', 'c2b_sens_politics_dict0001', '反动', '反动', 19, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_politics_diit019', 'c2b_sens_politics_dict0001', '邪教', '邪教', 20, 1, 'admin', '2026-05-25 12:00:00');
DELETE i FROM jimu_dict_item i JOIN jimu_dict d ON i.dict_id=d.id WHERE d.dict_code='c2b_sens_abuse';
DELETE FROM jimu_dict WHERE dict_code='c2b_sens_abuse';
INSERT INTO `jimu_dict`(`id`,`dict_name`,`dict_code`,`description`,`del_flag`,`create_by`,`create_time`,`type`,`tenant_id`) VALUES ('c2b_sens_abuse_dict000001', '辱骂脏话', 'c2b_sens_abuse', 'Chat2BI 敏感词-辱骂脏话', 0, 'admin', '2026-05-25 12:00:00', 0, NULL);
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it000', 'c2b_sens_abuse_dict000001', '傻', '傻', 1, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it001', 'c2b_sens_abuse_dict000001', '傻逼', '傻逼', 2, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it002', 'c2b_sens_abuse_dict000001', '傻B', '傻B', 3, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it003', 'c2b_sens_abuse_dict000001', '沙雕', '沙雕', 4, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it004', 'c2b_sens_abuse_dict000001', '蠢货', '蠢货', 5, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it005', 'c2b_sens_abuse_dict000001', '蠢猪', '蠢猪', 6, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it006', 'c2b_sens_abuse_dict000001', '蠢驴', '蠢驴', 7, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it007', 'c2b_sens_abuse_dict000001', '废物', '废物', 8, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it008', 'c2b_sens_abuse_dict000001', '垃圾', '垃圾', 9, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it009', 'c2b_sens_abuse_dict000001', '笨蛋', '笨蛋', 10, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it010', 'c2b_sens_abuse_dict000001', '白痴', '白痴', 11, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it011', 'c2b_sens_abuse_dict000001', '智障', '智障', 12, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it012', 'c2b_sens_abuse_dict000001', '脑残', '脑残', 13, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it013', 'c2b_sens_abuse_dict000001', '弱智', '弱智', 14, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it014', 'c2b_sens_abuse_dict000001', '神经病', '神经病', 15, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it015', 'c2b_sens_abuse_dict000001', '混蛋', '混蛋', 16, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it016', 'c2b_sens_abuse_dict000001', '王八蛋', '王八蛋', 17, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it017', 'c2b_sens_abuse_dict000001', '畜生', '畜生', 18, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it018', 'c2b_sens_abuse_dict000001', '贱人', '贱人', 19, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it019', 'c2b_sens_abuse_dict000001', '贱货', '贱货', 20, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it020', 'c2b_sens_abuse_dict000001', '婊子', '婊子', 21, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it021', 'c2b_sens_abuse_dict000001', '狗东西', '狗东西', 22, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it022', 'c2b_sens_abuse_dict000001', '狗屎', '狗屎', 23, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it023', 'c2b_sens_abuse_dict000001', '去死', '去死', 24, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it024', 'c2b_sens_abuse_dict000001', '该死', '该死', 25, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it025', 'c2b_sens_abuse_dict000001', '滚蛋', '滚蛋', 26, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it026', 'c2b_sens_abuse_dict000001', '二货', '二货', 27, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it027', 'c2b_sens_abuse_dict000001', '饭桶', '饭桶', 28, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it028', 'c2b_sens_abuse_dict000001', '草泥马', '草泥马', 29, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it029', 'c2b_sens_abuse_dict000001', '妈的', '妈的', 30, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it030', 'c2b_sens_abuse_dict000001', '他妈的', '他妈的', 31, 1, 'admin', '2026-05-25 12:00:00');
INSERT INTO `jimu_dict_item`(`id`,`dict_id`,`item_text`,`item_value`,`sort_order`,`status`,`create_by`,`create_time`) VALUES ('c2b_sens_abuse_dict0it031', 'c2b_sens_abuse_dict000001', '卧槽', '卧槽', 32, 1, 'admin', '2026-05-25 12:00:00');

-- 插入菜单 积木 ChatBI
INSERT INTO sys_permission
  (id, parent_id, name, url, component, is_route, component_name, redirect,
   menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive,
   hidden, hide_tab, description, create_by, create_time, update_by, update_time,
   del_flag, rule_flag, status, internal_or_external)
  VALUES
  ('1790521000000000001', '1892553163993931777', '积木 ChatBI', '/jimu/chat2bi/index',
   '{{window._CONFIG[''domianURL'']}}/jimu/chat2bi/index?token=${token}',
   1, NULL, NULL,
   0, NULL, '0', 1, 0, 'ant-design:robot-outlined', 1, 0,
   0, 0, '积木 ChatBI 问答 / 语义建模 / AI数据表', 'admin', NOW(), NULL, NULL,
   0, 0, '1', 0);