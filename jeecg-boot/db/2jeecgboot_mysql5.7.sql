/*
 Navicat Premium Data Transfer

 Source Server         : mysql5.7
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : localhost:3306
 Source Schema         : jeecg-boot-os-re

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 24/02/2020 00:51:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for demo
-- ----------------------------
DROP TABLE IF EXISTS `demo`;
CREATE TABLE `demo`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `key_word` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关键词',
  `punch_time` datetime(0) NULL DEFAULT NULL COMMENT '打卡时间',
  `salary_money` decimal(10, 3) NULL DEFAULT NULL COMMENT '工资',
  `bonus_money` double(10, 2) NULL DEFAULT NULL COMMENT '奖金',
  `sex` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别 {男:1,女:2}',
  `age` int(11) NULL DEFAULT NULL COMMENT '年龄',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个人简介',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `sys_org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门编码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of demo
-- ----------------------------
INSERT INTO `demo` VALUES ('08375a2dff80e821d5a158dd98302b23', '导入小虎', NULL, NULL, NULL, NULL, '2', 28, NULL, NULL, NULL, 'jeecg-boot', '2019-04-10 11:42:57', NULL, NULL, NULL);
INSERT INTO `demo` VALUES ('1c2ba51b29a42d9de02bbd708ea8121a', '777777', '777', '2018-12-07 19:43:17', NULL, NULL, NULL, 7, '2018-12-07', NULL, NULL, NULL, NULL, 'admin', '2019-02-21 18:26:04', NULL);
INSERT INTO `demo` VALUES ('1dc29e80be14d1400f165b5c6b30c707', 'zhang daihao', NULL, NULL, NULL, NULL, '2', NULL, NULL, 'zhangdaiscott@163.com', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `demo` VALUES ('304e651dc769d5c9b6e08fb30457a602', '小白兔', NULL, NULL, NULL, NULL, '2', 28, NULL, NULL, NULL, 'scott', '2019-01-19 13:12:53', 'qinfeng', '2019-01-19 13:13:12', NULL);
INSERT INTO `demo` VALUES ('4', 'Sandy', '开源，很好', '2018-12-15 00:00:00', NULL, NULL, '2', 21, '2018-12-15', 'test4@baomidou.com', '聪明00', NULL, NULL, 'admin', '2019-02-25 16:29:27', NULL);
INSERT INTO `demo` VALUES ('42c08b1a2e5b2a96ffa4cc88383d4b11', '秦50090', NULL, '2019-01-05 20:33:31', NULL, NULL, NULL, 28, '2019-01-05', NULL, NULL, 'admin', '2019-01-19 20:33:54', 'admin', '2019-01-19 20:34:29', NULL);
INSERT INTO `demo` VALUES ('4436302a0de50bb83025286bc414d6a9', 'zhang daihao', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'zhangdaiscott@163.com', NULL, 'admin', '2019-01-19 15:39:04', NULL, NULL, NULL);
INSERT INTO `demo` VALUES ('4981637bf71b0c1ed1365241dfcfa0ea', '小虎', NULL, NULL, NULL, NULL, '2', 28, NULL, NULL, NULL, 'scott', '2019-01-19 13:12:53', 'qinfeng', '2019-01-19 13:13:12', NULL);
INSERT INTO `demo` VALUES ('5c16e6a5c31296bcd3f1053d5d118815', '导入zhangdaiscott', NULL, NULL, NULL, NULL, '1', NULL, '2019-01-03', NULL, NULL, 'jeecg-boot', '2019-04-10 11:42:57', 'admin', '2019-05-19 18:35:51', NULL);
INSERT INTO `demo` VALUES ('7', 'zhangdaiscott', NULL, NULL, NULL, NULL, '1', NULL, '2019-01-03', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `demo` VALUES ('73bc58611012617ca446d8999379e4ac', '郭靖11a', '777', '2018-12-07 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 18:16:39', NULL, NULL, NULL);
INSERT INTO `demo` VALUES ('917e240eaa0b1b2d198ae869b64a81c3', 'zhang daihao', NULL, NULL, NULL, NULL, '2', 0, '2018-11-29', 'zhangdaiscott@163.com', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `demo` VALUES ('94420c5d8fc4420dde1e7196154b3a24', '秦111', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'scott', '2019-01-19 12:54:58', 'qinfeng', '2019-01-19 13:12:10', NULL);
INSERT INTO `demo` VALUES ('95740656751c5f22e5932ab0ae33b1e4', '杨康22a', '奸臣', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 18:16:39', NULL, NULL, NULL);
INSERT INTO `demo` VALUES ('b86897900c770503771c7bb88e5d1e9b', 'scott1', '开源、很好、hello', NULL, NULL, NULL, '1', NULL, NULL, 'zhangdaiscott@163.com', NULL, 'scott', '2019-01-19 12:22:34', NULL, NULL, NULL);
INSERT INTO `demo` VALUES ('c0b7c3de7c62a295ab715943de8a315d', '秦风555', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2019-01-19 13:18:30', 'admin', '2019-01-19 13:18:50', NULL);
INSERT INTO `demo` VALUES ('c28fa8391ef81d6fabd8bd894a7615aa', '小麦', NULL, NULL, NULL, NULL, '2', NULL, NULL, 'zhangdaiscott@163.com', NULL, 'jeecg-boot', '2019-04-04 17:18:09', NULL, NULL, NULL);
INSERT INTO `demo` VALUES ('c2c0d49e3c01913067cf8d1fb3c971d2', 'zhang daihao', NULL, NULL, NULL, NULL, '2', NULL, NULL, 'zhangdaiscott@163.com', NULL, 'admin', '2019-01-19 23:37:18', 'admin', '2019-01-21 16:49:06', NULL);
INSERT INTO `demo` VALUES ('c96279c666b4b82e3ef1e4e2978701ce', '报名时间', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 18:00:52', NULL, NULL, NULL);
INSERT INTO `demo` VALUES ('d24668721446e8478eeeafe4db66dcff', 'zhang daihao999', NULL, NULL, NULL, NULL, '1', NULL, NULL, 'zhangdaiscott@163.com', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `demo` VALUES ('eaa6c1116b41dc10a94eae34cf990133', 'zhang daihao', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'zhangdaiscott@163.com', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `demo` VALUES ('ffa9da1ad40632dfcabac51d766865bd', '秦999', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', '2019-01-19 23:36:34', 'admin', '2019-02-14 17:30:43', NULL);

-- ----------------------------
-- Table structure for jeecg_monthly_growth_analysis
-- ----------------------------
DROP TABLE IF EXISTS `jeecg_monthly_growth_analysis`;
CREATE TABLE `jeecg_monthly_growth_analysis`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `year` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `month` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '月份',
  `main_income` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '佣金/主营收入',
  `other_income` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '其他收入',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jeecg_monthly_growth_analysis
-- ----------------------------
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (1, '2018', '1月', 114758.90, 4426054.19);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (2, '2018', '2月', 8970734.12, 1230188.67);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (3, '2018', '3月', 26755421.23, 2048836.84);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (4, '2018', '4月', 2404990.63, 374171.44);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (5, '2018', '5月', 5450793.02, 502306.10);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (6, '2018', '6月', 17186212.11, 1375154.97);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (7, '2018', '7月', 579975.67, 461483.99);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (8, '2018', '8月', 1393590.06, 330403.76);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (9, '2018', '9月', 735761.21, 1647474.92);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (10, '2018', '10月', 1670442.44, 3423368.33);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (11, '2018', '11月', 2993130.34, 3552024.00);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (12, '2018', '12月', 4206227.26, 3645614.92);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (13, '2019', '1月', 483834.66, 418046.77);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (14, '2019', '2月', 11666578.65, 731352.20);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (15, '2019', '3月', 27080982.08, 1878538.81);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (16, '2019', '4月', 0.00, 0.00);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (17, '2019', '5月', 0.00, 0.00);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (18, '2019', '6月', 0.00, 0.00);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (19, '2019', '7月', 0.00, 0.00);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (20, '2019', '8月', 0.00, 0.00);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (21, '2019', '9月', 0.00, 0.00);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (22, '2019', '10月', 0.00, 0.00);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (23, '2019', '11月', 0.00, 0.00);
INSERT INTO `jeecg_monthly_growth_analysis` VALUES (24, '2019', '12月', 0.00, 0.00);

-- ----------------------------
-- Table structure for jeecg_order_customer
-- ----------------------------
DROP TABLE IF EXISTS `jeecg_order_customer`;
CREATE TABLE `jeecg_order_customer`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户名',
  `sex` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `idcard` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号码',
  `idcard_pic` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证扫描件',
  `telphone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话1',
  `order_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '外键',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jeecg_order_customer
-- ----------------------------
INSERT INTO `jeecg_order_customer` VALUES ('15538561502720', '3333', '1', '', NULL, '', '0d4a2e67b538ee1bc881e5ed34f670f0', 'jeecg-boot', '2019-03-29 18:42:55', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('15538561512681', '3332333', '2', '', NULL, '', '0d4a2e67b538ee1bc881e5ed34f670f0', 'jeecg-boot', '2019-03-29 18:42:55', 'admin', '2019-03-29 18:43:12');
INSERT INTO `jeecg_order_customer` VALUES ('15538561550142', '4442', '2', '', NULL, '', '0d4a2e67b538ee1bc881e5ed34f670f0', 'jeecg-boot', '2019-03-29 18:42:55', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('15541168497342', '444', '', '', '', '', 'f71f7f8930b5b6b1703d9948d189982b', 'admin', '2019-04-01 19:08:45', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('15541168499553', '5555', '', '', '', '', 'f71f7f8930b5b6b1703d9948d189982b', 'admin', '2019-04-01 19:08:45', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('15541169272690', '小王1', '1', '', '', '18611788525', 'f618a85b17e2c4dd58d268220c8dd9a1', 'admin', '2019-04-01 19:10:07', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('15541169288141', '效力1', '1', '', '', '18611788525', 'f618a85b17e2c4dd58d268220c8dd9a1', 'admin', '2019-04-01 19:10:07', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('15541169441372', '小红1', '1', '', '', '18611788525', 'f618a85b17e2c4dd58d268220c8dd9a1', 'admin', '2019-04-01 19:10:07', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('15543695362380', '1111', '', '', '', '', '5d6e2b9e44037526270b6206196f6689', 'admin', '2019-04-04 17:19:40', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('15543695397221', '222', '', '', '', '', '5d6e2b9e44037526270b6206196f6689', 'admin', '2019-04-04 17:19:40', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('15543695398992', '333', '', '', '', '', '5d6e2b9e44037526270b6206196f6689', 'admin', '2019-04-04 17:19:40', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('18dc5eb1068ccdfe90e358951ca1a3d6', 'dr2', '', '', '', '', '8ab1186410a65118c4d746eb085d3bed', 'admin', '2019-04-04 17:25:33', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('195d280490fe88ca1475512ddcaf2af9', '12', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('217a2bf83709775d2cd85bf598392327', '2', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('22bc052ae53ed09913b946abba93fa89', '1', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('23bafeae88126c3bf3322a29a04f0d5e', 'x秦风', NULL, NULL, NULL, NULL, '163e2efcbc6d7d54eb3f8a137da8a75a', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('25c4a552c6843f36fad6303bfa99a382', '1', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('2d32144e2bee63264f3f16215c258381', '33333', '2', NULL, NULL, NULL, 'd908bfee3377e946e59220c4a4eb414a', 'admin', '2019-04-01 16:27:03', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('2d43170d6327f941bd1a017999495e25', '1', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('2e5f62a8b6e0a0ce19b52a6feae23d48', '3', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('313abf99558ac5f13ecca3b87e562ad1', 'scott', '2', NULL, NULL, NULL, 'b190737bd04cca8360e6f87c9ef9ec4e', 'admin', '2019-02-25 16:29:48', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('34a1c5cf6cee360ed610ed0bed70e0f9', '导入秦风', NULL, NULL, NULL, NULL, 'a2cce75872cc8fcc47f78de9ffd378c2', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('3c87400f8109b4cf43c5598f0d40e34d', '2', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('40964bcbbecb38e5ac15e6d08cf3cd43', '233', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('41e3dee0b0b6e6530eccb7fbb22fd7a3', '4555', '1', '370285198602058823', NULL, '18611788674', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('4808ae8344c7679a4a2f461db5dc3a70', '44', '1', '370285198602058823', NULL, '18611788674', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('4b6cef12f195fad94d57279b2241770d', 'dr12', '', '', '', '', '8ab1186410a65118c4d746eb085d3bed', 'admin', '2019-04-04 17:25:33', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('524e695283f8e8c256cc24f39d6d8542', '小王', '2', '370285198604033222', NULL, '18611788674', 'eb13ab35d2946a2b0cfe3452bca1e73f', 'admin', '2019-02-25 16:29:41', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('57c2a8367db34016114cbc9fa368dba0', '2', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('5df36a1608b8c7ac99ad9bc408fe54bf', '4', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('6b694e9ba54bb289ae9cc499e40031e7', 'x秦风', '1', NULL, NULL, NULL, 'b190737bd04cca8360e6f87c9ef9ec4e', 'admin', '2019-02-25 16:29:48', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('6c6fd2716c2dcd044ed03c2c95d261f8', '李四', '2', '370285198602058833', '', '18611788676', 'f71f7f8930b5b6b1703d9948d189982b', 'admin', '2019-04-01 19:08:45', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('742d008214dee0afff2145555692973e', '秦风', '1', '370285198602058822', NULL, '18611788676', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('7469c3e5d371767ff90a739d297689b5', '导入秦风', '2', NULL, NULL, NULL, '3a867ebf2cebce9bae3f79676d8d86f3', 'jeecg-boot', '2019-03-29 18:43:59', 'admin', '2019-04-08 17:35:02');
INSERT INTO `jeecg_order_customer` VALUES ('7a96e2c7b24847d4a29940dbc0eda6e5', 'drscott', NULL, NULL, NULL, NULL, 'e73434dad84ebdce2d4e0c2a2f06d8ea', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('7f5a40818e225ee18bda6da7932ac5f9', '2', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('8011575abfd7c8085e71ff66df1124b9', '1', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('8404f31d7196221a573c9bd6c8f15003', '小张', '1', '370285198602058211', NULL, '18611788676', 'eb13ab35d2946a2b0cfe3452bca1e73f', 'admin', '2019-02-25 16:29:41', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('859020e10a2f721f201cdbff78cf7b9f', 'scott', NULL, NULL, NULL, NULL, '163e2efcbc6d7d54eb3f8a137da8a75a', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('8cc3c4d26e3060975df3a2adb781eeb4', 'dr33', NULL, NULL, NULL, NULL, 'b2feb454e43c46b2038768899061e464', 'jeecg-boot', '2019-04-04 17:23:09', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('8d1725c23a6a50685ff0dedfd437030d', '4', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('933cae3a79f60a93922d59aace5346ce', '小王', NULL, '370285198604033222', NULL, '18611788674', '6a719071a29927a14f19482f8693d69a', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('9bdb5400b709ba4eaf3444de475880d7', 'dr22', NULL, NULL, NULL, NULL, '22c17790dcd04b296c4a2a089f71895f', 'jeecg-boot', '2019-04-04 17:23:09', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('9f87677f70e5f864679314389443a3eb', '33', '2', '370285198602058823', NULL, '18611788674', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('a2c2b7101f75c02deb328ba777137897', '44', '2', '370285198602058823', NULL, '18611788674', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('ab4d002dc552c326147e318c87d3bed4', 'ddddd', '1', '370285198604033222', NULL, '18611755848', '9a57c850e4f68cf94ef7d8585dbaf7e6', 'admin', '2019-04-04 17:30:47', 'admin', '2019-04-04 17:31:17');
INSERT INTO `jeecg_order_customer` VALUES ('ad116f722a438e5f23095a0b5fcc8e89', 'dr秦风', NULL, NULL, NULL, NULL, 'e73434dad84ebdce2d4e0c2a2f06d8ea', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('b1ba147b75f5eaa48212586097fc3fd1', '2', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('b43bf432c251f0e6b206e403b8ec29bc', 'lisi', NULL, NULL, NULL, NULL, 'f8889aaef6d1bccffd98d2889c0aafb5', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('bcdd300a7d44c45a66bdaac14903c801', '33', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('beb983293e47e2dc1a9b3d649aa3eb34', 'ddd3', NULL, NULL, NULL, NULL, 'd908bfee3377e946e59220c4a4eb414a', 'admin', '2019-04-01 16:27:03', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('c219808196406f1b8c7f1062589de4b5', '44', '1', '370285198602058823', NULL, '18611788674', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('c8ed061d4b27c0c7a64e100f2b1c8ab5', '张经理', '2', '370285198602058823', NULL, '18611788674', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('cc5de4af7f06cd6d250965ebe92a0395', '1', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('cf8817bd703bf7c7c77a2118edc26cc7', '1', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('d72b26fae42e71270fce2097a88da58a', '导入scott', NULL, 'www', NULL, NULL, '3a867ebf2cebce9bae3f79676d8d86f3', 'jeecg-boot', '2019-03-29 18:43:59', 'admin', '2019-04-08 17:35:05');
INSERT INTO `jeecg_order_customer` VALUES ('dbdc60a6ac1a8c43f24afee384039b68', 'xiaowang', NULL, NULL, NULL, NULL, 'f8889aaef6d1bccffd98d2889c0aafb5', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('dc5883b50466de94d900919ed96d97af', '33', '1', '370285198602058823', NULL, '18611788674', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('deeb73e553ad8dc0a0b3cfd5a338de8e', '3333', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('e2570278bf189ac05df3673231326f47', '1', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('e39cb23bb950b2bdedfc284686c6128a', '1', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('e46fe9111a9100844af582a18a2aa402', '1', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('ee7af0acb9beb9bf8d8b3819a8a7fdc3', '2', NULL, NULL, NULL, NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('f5d2605e844192d9e548f9bd240ac908', '小张', NULL, '370285198602058211', NULL, '18611788676', '6a719071a29927a14f19482f8693d69a', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_customer` VALUES ('f6db6547382126613a3e46e7cd58a5f2', '导入scott', NULL, NULL, NULL, NULL, 'a2cce75872cc8fcc47f78de9ffd378c2', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);

-- ----------------------------
-- Table structure for jeecg_order_main
-- ----------------------------
DROP TABLE IF EXISTS `jeecg_order_main`;
CREATE TABLE `jeecg_order_main`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `order_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `ctype` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单类型',
  `order_date` datetime(0) NULL DEFAULT NULL COMMENT '订单日期',
  `order_money` double(10, 3) NULL DEFAULT NULL COMMENT '订单金额',
  `content` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jeecg_order_main
-- ----------------------------
INSERT INTO `jeecg_order_main` VALUES ('163e2efcbc6d7d54eb3f8a137da8a75a', 'B100', NULL, NULL, 3000.000, NULL, 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_main` VALUES ('3a867ebf2cebce9bae3f79676d8d86f3', '导入B100', '2222', NULL, 3000.000, NULL, 'jeecg-boot', '2019-03-29 18:43:59', 'admin', '2019-04-08 17:35:13');
INSERT INTO `jeecg_order_main` VALUES ('4bca3ea6881d39dbf67ef1e42c649766', '1212', NULL, NULL, NULL, NULL, 'admin', '2019-04-03 10:55:43', NULL, NULL);
INSERT INTO `jeecg_order_main` VALUES ('4cba137333127e8e31df7ad168cc3732', '青岛订单A0001', '2', '2019-04-03 10:56:07', NULL, NULL, 'admin', '2019-04-03 10:56:11', NULL, NULL);
INSERT INTO `jeecg_order_main` VALUES ('54e739bef5b67569c963c38da52581ec', 'NC911', '1', '2019-02-18 09:58:51', 40.000, NULL, 'admin', '2019-02-18 09:58:47', 'admin', '2019-02-18 09:58:59');
INSERT INTO `jeecg_order_main` VALUES ('5d6e2b9e44037526270b6206196f6689', 'N333', NULL, '2019-04-04 17:19:11', NULL, '聪明00', 'admin', '2019-04-04 17:19:40', NULL, NULL);
INSERT INTO `jeecg_order_main` VALUES ('6a719071a29927a14f19482f8693d69a', 'c100', NULL, NULL, 5000.000, NULL, 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_main` VALUES ('8ab1186410a65118c4d746eb085d3bed', '导入400', '1', '2019-02-18 09:58:51', 40.000, NULL, 'admin', '2019-02-18 09:58:47', 'admin', '2019-02-18 09:58:59');
INSERT INTO `jeecg_order_main` VALUES ('9a57c850e4f68cf94ef7d8585dbaf7e6', 'halou100dd', NULL, '2019-04-04 17:30:32', NULL, NULL, 'admin', '2019-04-04 17:30:41', 'admin', '2019-04-04 17:31:08');
INSERT INTO `jeecg_order_main` VALUES ('a2cce75872cc8fcc47f78de9ffd378c2', '导入B100', NULL, NULL, 3000.000, NULL, 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_main` VALUES ('b190737bd04cca8360e6f87c9ef9ec4e', 'B0018888', '1', NULL, NULL, NULL, 'admin', '2019-02-15 18:39:29', 'admin', '2019-02-15 18:39:37');
INSERT INTO `jeecg_order_main` VALUES ('d908bfee3377e946e59220c4a4eb414a', 'SSSS001', NULL, NULL, 599.000, NULL, 'admin', '2019-04-01 15:43:03', 'admin', '2019-04-01 16:26:52');
INSERT INTO `jeecg_order_main` VALUES ('e73434dad84ebdce2d4e0c2a2f06d8ea', '导入200', NULL, NULL, 3000.000, NULL, 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_main` VALUES ('eb13ab35d2946a2b0cfe3452bca1e73f', 'BJ9980', '1', NULL, 90.000, NULL, 'admin', '2019-02-16 17:36:42', 'admin', '2019-02-16 17:46:16');
INSERT INTO `jeecg_order_main` VALUES ('f618a85b17e2c4dd58d268220c8dd9a1', 'N001', NULL, '2019-04-01 19:09:02', 2222.000, NULL, 'admin', '2019-04-01 19:09:47', 'admin', '2019-04-01 19:10:00');
INSERT INTO `jeecg_order_main` VALUES ('f71f7f8930b5b6b1703d9948d189982b', 'BY911', NULL, '2019-04-06 19:08:39', NULL, NULL, 'admin', '2019-04-01 16:36:02', 'admin', '2019-04-01 16:36:08');
INSERT INTO `jeecg_order_main` VALUES ('f8889aaef6d1bccffd98d2889c0aafb5', 'A100', NULL, '2018-10-10 00:00:00', 6000.000, NULL, 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_main` VALUES ('fe81ee5d19bbf9eef2066d4f29dfbe0f', 'uuuu', NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 11:00:39', NULL, NULL);

-- ----------------------------
-- Table structure for jeecg_order_ticket
-- ----------------------------
DROP TABLE IF EXISTS `jeecg_order_ticket`;
CREATE TABLE `jeecg_order_ticket`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `ticket_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '航班号',
  `tickect_date` datetime(0) NULL DEFAULT NULL COMMENT '航班时间',
  `order_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '外键',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jeecg_order_ticket
-- ----------------------------
INSERT INTO `jeecg_order_ticket` VALUES ('0f0e3a40a215958f807eea08a6e1ac0a', '88', NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('0fa3bd0bbcf53650c0bb3c0cac6d8cb7', 'ffff', '2019-02-21 00:00:00', 'eb13ab35d2946a2b0cfe3452bca1e73f', 'admin', '2019-02-25 16:29:41', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('14221afb4f5f749c1deef26ac56fdac3', '33', '2019-03-09 00:00:00', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('15538561502730', '222', NULL, '0d4a2e67b538ee1bc881e5ed34f670f0', 'jeecg-boot', '2019-03-29 18:42:55', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('15538561526461', '2244', '2019-03-29 00:00:00', '0d4a2e67b538ee1bc881e5ed34f670f0', 'jeecg-boot', '2019-03-29 18:42:55', 'admin', '2019-03-29 18:43:26');
INSERT INTO `jeecg_order_ticket` VALUES ('15541168478913', 'hhhhh', NULL, 'f71f7f8930b5b6b1703d9948d189982b', 'admin', '2019-04-01 19:08:45', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('15541169272810', '22211', '2019-04-01 19:09:40', 'f618a85b17e2c4dd58d268220c8dd9a1', 'admin', '2019-04-01 19:10:07', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('15541169302331', '333311', '2019-04-01 19:09:40', 'f618a85b17e2c4dd58d268220c8dd9a1', 'admin', '2019-04-01 19:10:07', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('15541169713092', '333311', '2019-04-01 19:09:47', 'f618a85b17e2c4dd58d268220c8dd9a1', 'admin', '2019-04-01 19:10:07', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('15542604293170', 'c', NULL, 'fe81ee5d19bbf9eef2066d4f29dfbe0f', 'jeecg-boot', '2019-04-03 11:00:39', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('15542604374431', 'd', NULL, 'fe81ee5d19bbf9eef2066d4f29dfbe0f', 'jeecg-boot', '2019-04-03 11:00:39', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('15543695362380', 'ccc2', NULL, '5d6e2b9e44037526270b6206196f6689', 'admin', '2019-04-04 17:19:40', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('15543695381291', 'cccc1', NULL, '5d6e2b9e44037526270b6206196f6689', 'admin', '2019-04-04 17:19:40', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('15543695740352', 'dddd', NULL, '5d6e2b9e44037526270b6206196f6689', 'admin', '2019-04-04 17:19:40', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('18905bc89ee3851805aab38ed3b505ec', '44', NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('1f809cbd26f4e574697e1c10de575d72', 'A100', NULL, 'e73434dad84ebdce2d4e0c2a2f06d8ea', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('21051adb51529bdaa8798b5a3dd7f7f7', 'C10029', '2019-02-20 00:00:00', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('269576e766b917f8b6509a2bb0c4d4bd', 'A100', NULL, '163e2efcbc6d7d54eb3f8a137da8a75a', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('2d473ffc79e5b38a17919e15f8b7078e', '66', '2019-03-29 00:00:00', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('3655b66fca5fef9c6aac6d70182ffda2', 'AA123', '2019-04-01 00:00:00', 'd908bfee3377e946e59220c4a4eb414a', 'admin', '2019-04-01 16:27:03', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('365d5919155473ade45840fd626c51a9', 'dddd', '2019-04-04 17:25:29', '8ab1186410a65118c4d746eb085d3bed', 'admin', '2019-04-04 17:25:33', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('4889a782e78706ab4306a925cfb163a5', 'C34', '2019-04-01 00:00:00', 'd908bfee3377e946e59220c4a4eb414a', 'admin', '2019-04-01 16:35:00', 'admin', '2019-04-01 16:35:07');
INSERT INTO `jeecg_order_ticket` VALUES ('48d385796382cf87fa4bdf13b42d9a28', '导入A100', NULL, '3a867ebf2cebce9bae3f79676d8d86f3', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('541faed56efbeb4be9df581bd8264d3a', '88', NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('57a27a7dfd6a48e7d981f300c181b355', '6', '2019-03-30 00:00:00', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('5ce4dc439c874266e42e6c0ff8dc8b5c', '导入A100', NULL, 'a2cce75872cc8fcc47f78de9ffd378c2', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('5f16e6a64ab22a161bd94cc205f2c662', '222', '2019-02-23 00:00:00', 'b190737bd04cca8360e6f87c9ef9ec4e', 'admin', '2019-02-25 16:29:48', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('645a06152998a576c051474157625c41', '88', '2019-04-04 17:25:31', '8ab1186410a65118c4d746eb085d3bed', 'admin', '2019-04-04 17:25:33', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('6e3562f2571ea9e96b2d24497b5f5eec', '55', '2019-03-23 00:00:00', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('8fd2b389151568738b1cc4d8e27a6110', '导入A100', NULL, 'a2cce75872cc8fcc47f78de9ffd378c2', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('93f1a84053e546f59137432ff5564cac', '55', NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('969ddc5d2e198d50903686917f996470', 'A10029', '2019-04-01 00:00:00', 'f71f7f8930b5b6b1703d9948d189982b', 'admin', '2019-04-01 19:08:45', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('96e7303a8d22a5c384e08d7bcf7ac2bf', 'A100', NULL, 'e73434dad84ebdce2d4e0c2a2f06d8ea', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('9e8a3336f6c63f558f2b68ce2e1e666e', 'dddd', NULL, '9a57c850e4f68cf94ef7d8585dbaf7e6', 'admin', '2019-04-04 17:30:55', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('a28db02c810c65660015095cb81ed434', 'A100', NULL, 'f8889aaef6d1bccffd98d2889c0aafb5', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('b217bb0e4ec6a45b6cbf6db880060c0f', 'A100', NULL, '6a719071a29927a14f19482f8693d69a', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('ba708df70bb2652ed1051a394cfa0bb3', '333', NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('beabbfcb195d39bedeeafe8318794562', 'A1345', '2019-04-01 00:00:00', 'd908bfee3377e946e59220c4a4eb414a', 'admin', '2019-04-01 16:27:04', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('bf450223cb505f89078a311ef7b6ed16', '777', '2019-03-30 00:00:00', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('c06165b6603e3e1335db187b3c841eef', 'fff', NULL, '9a57c850e4f68cf94ef7d8585dbaf7e6', 'admin', '2019-04-04 17:30:58', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('c113136abc26ace3a6da4e41d7dc1c7e', '44', '2019-03-15 00:00:00', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('c1abdc2e30aeb25de13ad6ee3488ac24', '77', '2019-03-22 00:00:00', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('c23751a7deb44f553ce50a94948c042a', '33', '2019-03-09 00:00:00', '8ab1186410a65118c4d746eb085d3bed', 'admin', '2019-04-04 17:25:33', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('c64547666b634b3d6a0feedcf05f25ce', 'C10019', '2019-04-01 00:00:00', 'f71f7f8930b5b6b1703d9948d189982b', 'admin', '2019-04-01 19:08:45', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('c8b8d3217f37da78dddf711a1f7da485', 'A100', NULL, '163e2efcbc6d7d54eb3f8a137da8a75a', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('cab691c1c1ff7a6dfd7248421917fd3c', 'A100', NULL, 'f8889aaef6d1bccffd98d2889c0aafb5', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('cca10a9a850b456d9b72be87da7b0883', '77', NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('d2fbba11f4814d9b1d3cb1a3f342234a', 'C10019', '2019-02-18 00:00:00', '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('d746c1ed956a562e97eef9c6faf94efa', '111', '2019-02-01 00:00:00', 'b190737bd04cca8360e6f87c9ef9ec4e', 'admin', '2019-02-25 16:29:48', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('dbdb07a16826808e4276e84b2aa4731a', '导入A100', NULL, '3a867ebf2cebce9bae3f79676d8d86f3', 'jeecg-boot', '2019-03-29 18:43:59', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('e7075639c37513afc0bbc4bf7b5d98b9', '88', NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('fa759dc104d0371f8aa28665b323dab6', '888', NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);
INSERT INTO `jeecg_order_ticket` VALUES ('ff197da84a9a3af53878eddc91afbb2e', '33', NULL, '54e739bef5b67569c963c38da52581ec', 'admin', '2019-03-15 16:50:15', NULL, NULL);

-- ----------------------------
-- Table structure for jeecg_project_nature_income
-- ----------------------------
DROP TABLE IF EXISTS `jeecg_project_nature_income`;
CREATE TABLE `jeecg_project_nature_income`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nature` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '项目性质',
  `insurance_fee` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '保险经纪佣金费',
  `risk_consulting_fee` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '风险咨询费',
  `evaluation_fee` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '承保公估评估费',
  `insurance_evaluation_fee` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '保险公估费',
  `bidding_consulting_fee` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '投标咨询费',
  `interol_consulting_fee` decimal(18, 2) NULL DEFAULT 0.00 COMMENT '内控咨询费',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jeecg_project_nature_income
-- ----------------------------
INSERT INTO `jeecg_project_nature_income` VALUES (1, '市场化-电商业务', 4865.41, 0.00, 0.00, 0.00, 0.00, 0.00);
INSERT INTO `jeecg_project_nature_income` VALUES (2, '统筹型', 35767081.88, 0.00, 0.00, 0.00, 0.00, 0.00);
INSERT INTO `jeecg_project_nature_income` VALUES (3, '市场化-非股东', 1487045.35, 0.00, 0.00, 0.00, 0.00, 0.00);
INSERT INTO `jeecg_project_nature_income` VALUES (4, '市场化-参控股', 382690.56, 0.00, 0.00, 0.00, 0.00, 0.00);
INSERT INTO `jeecg_project_nature_income` VALUES (5, '市场化-员工福利', 256684.91, 0.00, 0.00, 0.00, 0.00, 0.00);
INSERT INTO `jeecg_project_nature_income` VALUES (6, '市场化-再保险', 563451.03, 0.00, 0.00, 0.00, 0.00, 0.00);
INSERT INTO `jeecg_project_nature_income` VALUES (7, '市场化-海外业务', 760576.25, 770458.75, 0.00, 0.00, 0.00, 0.00);
INSERT INTO `jeecg_project_nature_income` VALUES (8, '市场化-风险咨询', 910183.93, 0.00, 0.00, 0.00, 0.00, 226415.09);

-- ----------------------------
-- Table structure for joa_demo
-- ----------------------------
DROP TABLE IF EXISTS `joa_demo`;
CREATE TABLE `joa_demo`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ID',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请假人',
  `days` int(11) NULL DEFAULT NULL COMMENT '请假天数',
  `begin_date` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_date` datetime(0) NULL DEFAULT NULL COMMENT '请假结束时间',
  `reason` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请假原因',
  `bpm_status` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '流程状态',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人id'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '流程测试' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for onl_cgform_button
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgform_button`;
CREATE TABLE `onl_cgform_button`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
  `BUTTON_CODE` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '按钮编码',
  `BUTTON_ICON` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '按钮图标',
  `BUTTON_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '按钮名称',
  `BUTTON_STATUS` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '按钮状态',
  `BUTTON_STYLE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '按钮样式',
  `EXP` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表达式',
  `CGFORM_HEAD_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单ID',
  `OPT_TYPE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '按钮类型',
  `ORDER_NUM` int(11) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `index_formid`(`CGFORM_HEAD_ID`) USING BTREE,
  INDEX `index_button_code`(`BUTTON_CODE`) USING BTREE,
  INDEX `index_button_status`(`BUTTON_STATUS`) USING BTREE,
  INDEX `index_button_order`(`ORDER_NUM`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of onl_cgform_button
-- ----------------------------
INSERT INTO `onl_cgform_button` VALUES ('a45bc1c6fba96be6b0c91ffcdd6b54aa', 'genereate_person_config', 'icon-edit', '生成配置', '1', 'link', NULL, 'e2faf977fdaf4b25a524f58c2441a51c', 'js', NULL);

-- ----------------------------
-- Table structure for onl_cgform_enhance_java
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgform_enhance_java`;
CREATE TABLE `onl_cgform_enhance_java`  (
  `ID` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `BUTTON_CODE` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '按钮编码',
  `CG_JAVA_TYPE` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类型',
  `CG_JAVA_VALUE` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数值',
  `CGFORM_HEAD_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表单ID',
  `ACTIVE_STATUS` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '生效状态',
  `EVENT` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'end' COMMENT '事件状态(end:结束，start:开始)',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `index_fmid`(`CGFORM_HEAD_ID`) USING BTREE,
  INDEX `index_buttoncode`(`BUTTON_CODE`) USING BTREE,
  INDEX `index_status`(`ACTIVE_STATUS`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for onl_cgform_enhance_js
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgform_enhance_js`;
CREATE TABLE `onl_cgform_enhance_js`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
  `CG_JS` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'JS增强内容',
  `CG_JS_TYPE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `CONTENT` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `CGFORM_HEAD_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单ID',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `index_fmid`(`CGFORM_HEAD_ID`) USING BTREE,
  INDEX `index_jstype`(`CG_JS_TYPE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of onl_cgform_enhance_js
-- ----------------------------
INSERT INTO `onl_cgform_enhance_js` VALUES ('274b5d741a0262d3411958f0c465c5f0', 'genereate_person_config(row){\nconsole.log(\'选择\',row)\nalert(row.name + \'，个人积分配置生成成功！\');\n}', 'list', NULL, 'e2faf977fdaf4b25a524f58c2441a51c');
INSERT INTO `onl_cgform_enhance_js` VALUES ('2cbaf25f1edb620bea2d8de07f8233a1', 'air_china_post_materiel_item_onlChange(){\n    return {\n        wl_name(){\n           \n            let id = event.row.id\n            let cnum = event.row.num\n            let value = event.value\n            let targrt = event.target\n            let columnKey = event.column.key\n           let nval = 200*cnum\n           console.log(\'row\',event.row);\n           console.log(\'cnum\',cnum);\n           let otherValues = {\'jifen\': nval}\n              \n                that.triggleChangeValues(targrt,id,otherValues)\n\n        }\n    }\n}', 'form', NULL, 'e67d26b610dd414c884c4dbb24e71ce3');
INSERT INTO `onl_cgform_enhance_js` VALUES ('35d4ef464e5e8c87c9aa82ea89215fc1', '', 'list', NULL, 'e67d26b610dd414c884c4dbb24e71ce3');
INSERT INTO `onl_cgform_enhance_js` VALUES ('90394fbc3d48978cc0937bc56f2d5370', '', 'list', NULL, 'deea5a8ec619460c9245ba85dbc59e80');

-- ----------------------------
-- Table structure for onl_cgform_enhance_sql
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgform_enhance_sql`;
CREATE TABLE `onl_cgform_enhance_sql`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
  `BUTTON_CODE` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '按钮编码',
  `CGB_SQL` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'SQL内容',
  `CGB_SQL_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Sql名称',
  `CONTENT` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `CGFORM_HEAD_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单ID',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `index_formid`(`CGFORM_HEAD_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for onl_cgform_field
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgform_field`;
CREATE TABLE `onl_cgform_field`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
  `cgform_head_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表ID',
  `db_field_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字段名字',
  `db_field_txt` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段备注',
  `db_field_name_old` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原字段名',
  `db_is_key` tinyint(1) NULL DEFAULT NULL COMMENT '是否主键 0否 1是',
  `db_is_null` tinyint(1) NULL DEFAULT NULL COMMENT '是否允许为空0否 1是',
  `db_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据库字段类型',
  `db_length` int(11) NOT NULL COMMENT '数据库字段长度',
  `db_point_length` int(11) NULL DEFAULT NULL COMMENT '小数点',
  `db_default_val` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表字段默认值',
  `dict_field` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典code',
  `dict_table` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典表',
  `dict_text` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典Text',
  `field_show_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单控件类型',
  `field_href` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '跳转URL',
  `field_length` int(11) NULL DEFAULT NULL COMMENT '表单控件长度',
  `field_valid_type` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单字段校验规则',
  `field_must_input` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段是否必填',
  `field_extend_json` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展参数JSON',
  `field_value_rule_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '填值规则code',
  `is_query` tinyint(1) NULL DEFAULT NULL COMMENT '是否查询条件0否 1是',
  `is_show_form` tinyint(1) NULL DEFAULT NULL COMMENT '表单是否显示0否 1是',
  `is_show_list` tinyint(1) NULL DEFAULT NULL COMMENT '列表是否显示0否 1是',
  `is_read_only` tinyint(1) NULL DEFAULT 0 COMMENT '是否是只读（1是 0否）',
  `query_mode` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '查询模式',
  `main_table` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外键主表名',
  `main_field` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外键主键字段',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '排序',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `inex_table_id`(`cgform_head_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of onl_cgform_field
-- ----------------------------
INSERT INTO `onl_cgform_field` VALUES ('0021c969dc23a9150d6f70a13b52e73e', '402860816aa5921f016aa5921f480000', 'begin_date', '开始时间', 'begin_date', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 4, 'admin', '2019-05-11 15:31:54', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('020c1622c3df0aef30185f57874f6959', '79091e8277c744158530321513119c68', 'bpm_status', '流程状态', NULL, 0, 1, 'String', 32, 0, '1', 'bpm_status', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 1, 0, 'single', '', '', 8, 'admin', '2019-05-11 15:29:47', '2019-05-11 15:29:26', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('02b20e692456403e2fed1a89a06833b4', '402860816bff91c0016bff91d2810005', 'phone', '联系方式', 'phone', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 8, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('03668009f0ad92b20abb1a377197ee47', 'deea5a8ec619460c9245ba85dbc59e80', 'order_fk_id', '订单外键ID', NULL, 0, 0, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', 'test_order_main', 'id', 10, 'admin', '2019-06-10 16:07:16', '2019-04-20 11:42:53', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('03709092184fdf4a66b0cdb4dd10a159', '402860816bff91c0016bffa220a9000b', 'bpm_status', '流程状态', NULL, 0, 1, 'String', 32, 0, '1', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 46, 'admin', '2019-07-22 16:15:32', '2019-07-19 15:34:44', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('03c105d2706c8286416833684de67406', '79091e8277c744158530321513119c68', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, 'admin', '2019-05-11 15:29:47', '2019-05-11 15:27:17', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('03fd5ab69f331ff760c3f7d86b4a93f8', '4028318169e81b970169e81b97650000', 'log_content', '日志内容', 'log_content', 0, 1, 'string', 1000, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 3, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('045eb432c418b2b103b1e1b8e8a8a75d', 'fb7125a344a649b990c12949945cb6c1', 'age', '年龄', NULL, 0, 1, 'int', 32, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', NULL, NULL, NULL, '2019-03-26 19:24:11', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('04e4185a503e6aaaa31c243829ff4ac7', 'd35109c3632c4952a19ecc094943dd71', 'birthday', '生日', NULL, 0, 1, 'Date', 32, 0, '', '', '', '', 'date', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', 9, 'admin', '2019-08-23 23:45:15', '2019-03-15 14:24:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('04ff134cb4aae845059e10b3b85f1451', '7ea60a25fa27470e9080d6a921aabbd1', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 5, NULL, NULL, '2019-04-17 00:22:21', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('051dd70c504c97a028daab2af261ea35', '1acb6f81a1d9439da6cc4e868617b565', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 5, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('052dcc6f34976b66754fd99415bd22ce', '79091e8277c744158530321513119c68', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 5, 'admin', '2019-05-11 15:29:47', '2019-05-11 15:27:17', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('054db05394e83b318f097a60bc044134', '402860816bff91c0016bffa220a9000b', 'residence_address', '户籍地址', 'residence_address', 0, 1, 'string', 200, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 28, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('0604945c206e867644e9a44b4c9b20c6', 'fb19fb067cd841f9ae93d4eb3b883dc0', '2', '4', NULL, 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', 5, NULL, NULL, '2019-03-23 11:39:48', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('0617de6d735b37a3f80f2f35ad5d1511', '4028839a6de2ebd3016de2ebd3870000', 'size_type', '尺码类型', 'size_type', 0, 1, 'string', 2, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 13, NULL, NULL, '2019-10-19 15:29:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('06a1badae6119abf4ec48858a3e94e1c', '402860816bff91c0016bffa220a9000b', 'sys_org_code', '组织机构编码', 'sys_org_code', 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 43, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('06f1cfff58395ff62526b894f6182641', 'e67d26b610dd414c884c4dbb24e71ce3', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, 'admin', '2019-04-24 11:03:32', '2019-04-24 11:02:57', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('07a307972076a392ffc61b11437f89dd', '402860816bff91c0016bff91c0cb0000', 'create_time', '创建时间', 'create_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 13, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('07f4776fd641389a8c98a85713990dce', '402860816bff91c0016bff91c0cb0000', 'update_by', '更新人', 'update_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 14, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('09450359eb90b40d224ec43588a62f9e', '402860816bff91c0016bff91c0cb0000', 'user_id', '用户ID', 'user_id', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 3, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('0a4cdcb7e54f614ab952024f6c72bb6d', 'beee191324fd40c1afec4fda18bd9d47', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 5, 'admin', '2019-04-13 13:41:13', '2019-04-13 13:40:56', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('0adc06d9b497684bcbb5a781e044517c', '1acb6f81a1d9439da6cc4e868617b565', 'supplier', '供应商', NULL, 0, 1, 'String', 32, 0, '', 'air_china_ supplier', '', '', 'list', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 15, 'admin', '2019-06-10 14:47:14', '2019-04-24 16:52:00', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('0b3e833ac4aae3a13ec2f8ae460708f8', '4028839a6de2ebd3016de2ebd3870000', 'no', '预算表序号', 'no', 0, 1, 'string', 50, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 8, NULL, NULL, '2019-10-19 15:29:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('0ba1bf74e2a6a94a7a63010ec7230706', '402860816bff91c0016bffa220a9000b', 'update_time', '更新时间', 'update_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 42, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('0cba94f0497d4d3d829fc573f58eff9f', '402860816bff91c0016bffa220a9000b', 'graduation_time', '毕业时间', 'graduation_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 16, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('0d00c51a4ddad2598a587fadc968a8b2', '402860816bff91c0016bff91cfea0004', 'sys_org_code', '组织机构编码', 'sys_org_code', 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 13, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('0ddd0c0afc967a9ab6050401ca62a4be', 'e67d26b610dd414c884c4dbb24e71ce3', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, 'admin', '2019-04-24 11:03:32', '2019-04-24 11:02:57', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('0fb6fa76c5c78a1e957dbb411e110738', '402860816bff91c0016bff91d8830007', 'politically_status', '政治面貌', 'politically_status', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 7, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('105c8e44ad13026b641f0363601f30f3', 'e5464aa8fa7b47c580e91593cf9b46dc', 'num', '循环数量', NULL, 0, 1, 'int', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 9, 'admin', '2019-04-24 17:09:49', '2019-04-24 11:05:10', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1130f1e252533529bb1167b896dffe32', 'deea5a8ec619460c9245ba85dbc59e80', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 5, 'admin', '2019-06-10 16:07:16', '2019-04-20 11:41:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('117fc4ba649d6690a3ac482ad5e4ad38', '56870166aba54ebfacb20ba6c770bd73', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, 'admin', '2019-11-21 17:58:27', '2019-04-20 11:38:39', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('13246645b7650491b70205d99703ca06', '402860816aa5921f016aa5dedcb90009', 'bpm_status', '流程状态', 'bpm_status', 0, 1, 'string', 32, 0, '1', 'bpm_status', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 1, 0, 'group', '', '', 8, 'admin', '2019-05-11 15:56:47', '2019-05-11 15:50:08', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('135dd0ee50712722db65b8762bd487ea', '8994f2817b5a45d9890aa04497a317c5', 'update_time', '更新日期', NULL, 0, 1, 'date', 20, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', 4, NULL, NULL, '2019-03-23 11:39:16', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('14ec4c83c29966ab42b6b718c5a3e774', '7ea60a25fa27470e9080d6a921aabbd1', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, NULL, NULL, '2019-04-17 00:22:21', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('16363d0bc125125e395772278d0cf22e', '4b556f0168f64976a3d20bfb932bc798', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 5, NULL, NULL, '2019-04-12 23:38:28', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('179c290e383009810fb738f07bd5af8d', '402860816bff91c0016bff91d2810005', 'id', 'id', 'id', 1, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'group', '', '', 1, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('17cbda69da9dd3632625a0647c259070', '73162c3b8161413e8ecdca7eb288d0c9', 'wl_name', '物料名字', NULL, 0, 1, 'String', 200, 0, '', '', '', '', 'text', '', 120, NULL, '1', '', '', 0, 1, 1, 0, 'single', '', '', 7, 'admin', '2019-07-01 14:23:32', '2019-06-10 15:18:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('18fefb2257901f05508f8ec13ada78a3', 'e5464aa8fa7b47c580e91593cf9b46dc', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, 'admin', '2019-04-24 17:09:48', '2019-04-24 11:05:10', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1973ef1d3728fbff2db6a352e001f5f7', 'fb7125a344a649b990c12949945cb6c1', 'name', '用户名', NULL, 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', 5, 'admin', '2019-03-26 19:24:11', '2019-03-26 19:01:52', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1ab5be1f937f393b3e5cc214ef1b855c', '7ea60a25fa27470e9080d6a921aabbd1', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, NULL, NULL, '2019-04-17 00:22:21', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1b6c7b95028bed9ff656d65557dd2bdf', '402860816bff91c0016bffa220a9000b', 'user_id', '用户id', 'user_id', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 3, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1c2f307e315bac77a6d3f02e88387a43', 'deea5a8ec619460c9245ba85dbc59e80', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, 'admin', '2019-06-10 16:07:16', '2019-04-20 11:41:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1c3b2ad0a52ecb47fa7fd53f25875beb', 'deea5a8ec619460c9245ba85dbc59e80', 'price', '价格', NULL, 0, 1, 'double', 32, 0, '', '', '', '', 'text', '', 120, 'n', '0', '', '', 0, 1, 1, 0, 'single', '', '', 7, 'admin', '2019-06-10 16:07:16', '2019-04-20 11:41:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1c4d25a12388c80d397bb4f4664fe4e6', '4b556f0168f64976a3d20bfb932bc798', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, NULL, NULL, '2019-04-12 23:38:28', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1cfe967bb457cbaa6e041e45d019b583', '402860816bff91c0016bff91c7010001', 'update_time', '更新时间', 'update_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 10, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1e3d8cfbf12155559666a23ee2c6c5ca', 'e5464aa8fa7b47c580e91593cf9b46dc', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 5, 'admin', '2019-04-24 17:09:49', '2019-04-24 11:05:10', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1ed46fdeb289bd7805c9b83332ccd3b4', '402860816bff91c0016bff91d2810005', 'relation', '关系', 'relation', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 4, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1f0c6d33b79713fe79fb30373c81f6f7', '758334cb1e7445e2822b60e807aec4a3', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 5, NULL, NULL, '2019-10-18 18:02:09', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('1fa5f07b3e70d4925b69b2bf51309421', '56870166aba54ebfacb20ba6c770bd73', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 5, 'admin', '2019-11-21 17:58:27', '2019-04-20 11:38:39', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('20ff34fb0466089cb633d73d5a6f08d6', 'd35109c3632c4952a19ecc094943dd71', 'update_time', '更新日期', NULL, 0, 1, 'date', 20, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, 'admin', '2019-08-23 23:45:15', '2019-03-15 14:24:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2113a4ec7b88b4820dcbbdf96e46bbb7', 'fbc35f067da94a70adb622ddba259352', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, NULL, NULL, '2019-07-03 19:44:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2150e48b2cb6072d2d8ecd79a7daf7cc', '402860816bff91c0016bff91ca7e0002', 'create_time', '创建时间', 'create_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 10, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2323239efb5a40b73034411868dfc41d', 'fb19fb067cd841f9ae93d4eb3b883dc0', 'update_by', '更新人登录名称', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', 3, NULL, NULL, '2019-03-23 11:39:48', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('23f42061ed218bdbc1262913c071e1cd', 'e5464aa8fa7b47c580e91593cf9b46dc', 'iz_valid', '启动状态', NULL, 0, 1, 'int', 2, 0, '', 'air_china_valid', '', '', 'list', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 12, 'admin', '2019-04-24 17:09:49', '2019-04-24 14:09:06', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('242cc59b23965a92161eca69ffdbf018', 'd35109c3632c4952a19ecc094943dd71', 'age', '年龄', NULL, 0, 1, 'int', 32, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', 7, 'admin', '2019-08-23 23:45:15', '2019-03-15 14:24:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('265702edb8872e322fe72d3640e34ac5', '402860816bff91c0016bff91cfea0004', 'from_time', '开始日期', 'from_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 3, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2739ab1ece4d6053ba931bb6572f4ed8', '4028839a6de2ebd3016de2ebd3870000', 'iz_valid', '启用状态', 'iz_valid', 0, 1, 'string', 2, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 9, NULL, NULL, '2019-10-19 15:29:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('283f42283b9d0bf3b95ba3384ab2d255', '758334cb1e7445e2822b60e807aec4a3', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, NULL, NULL, '2019-10-18 18:02:09', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('284864d99fddfdcb00e188e3a512cb28', '1acb6f81a1d9439da6cc4e868617b565', 'no', '预算表序号', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 10, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2889d3cef706f91e092d76a56b8055be', '402860816bff91c0016bff91cda80003', 'order_no', '序号', 'order_no', 0, 1, 'int', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 8, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('29e4abea55d9fa7dbbd0c8dbbb2b3756', '402860816bff91c0016bff91cda80003', 'update_time', '更新时间', 'update_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 12, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2d53a66f0b72d820b86ff445e2181d76', 'beee191324fd40c1afec4fda18bd9d47', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, 'admin', '2019-04-13 13:41:13', '2019-04-13 13:40:56', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2dfc4c81926f678c5f8d5ffd27858201', 'e2faf977fdaf4b25a524f58c2441a51c', 'account', '用户编码', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 7, 'admin', '2019-06-10 17:27:00', '2019-04-24 17:12:11', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2e5275b6407e1b4265af8519077fa4a5', 'd3ae1c692b9640e0a091f8c46e17bb01', 'sys_org_code', '所属部门', NULL, 0, 1, 'string', 64, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 6, NULL, NULL, '2019-07-24 14:47:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2e66b9db37648389e0846e2204111732', '73162c3b8161413e8ecdca7eb288d0c9', 'has_child', '是否有子节点', NULL, 0, 1, 'string', 3, 0, '', 'valid_status', '', '', 'list', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 10, 'admin', '2019-07-01 14:23:32', '2019-06-10 15:18:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2ee58d8e4844dfe1fa6b1b841ae0b312', '402860816bff91c0016bff91d2810005', 'politically_status', '政治面貌', 'politically_status', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 7, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('2f111722eb3a994450e67e3211fd69a8', '402860816bff91c0016bff91ca7e0002', 'id', 'id', 'id', 1, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'group', '', '', 1, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('30143cc3de69c413828f9fba20662026', '402860816bff91c0016bffa220a9000b', 'healthy', '健康状况', 'healthy', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 12, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('31fd90306c3942f09cb79deabbf2f541', '402860816bff91c0016bff91d2810005', 'employee_id', '员工ID', 'employee_id', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', 'oa_employee_info', 'id', 2, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('336495117e3a31351fed6963b950dddb', '402860816bff91c0016bffa220a9000b', 'inside_transfer', '内部工作调动情况', 'inside_transfer', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 37, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('345c8b48e1e128e77c4c6e2b36512804', '402860816aa5921f016aa5dedcb90009', 'create_by', '创建人', 'create_by', 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'group', '', '', 2, 'admin', '2019-05-11 15:56:47', '2019-05-11 15:50:08', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('351faaeb2dd8105e9c66f678211c9d4f', 'dbf4675875e14676a3f9a8b2b8941140', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 5, NULL, NULL, '2019-05-27 18:02:07', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('354b2ce39c5e8ec3f0bbb01bf8ff0fb7', '32f75e4043ef4070919dbd4337186a3d', 'content', '描述', NULL, 0, 1, 'String', 300, 0, '', '', '', '', 'textarea', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 9, 'admin', '2019-04-11 10:15:31', '2019-03-28 15:24:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('35ca1c8aa1501bc8a79c880928841f18', '402860816aa5921f016aa5921f480000', 'update_by', '修改人id', 'update_by', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'group', '', '', 11, 'admin', '2019-05-11 15:31:55', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('3635793767371c6db9f76b4b79f9d321', '402860816bff91c0016bff91d8830007', 'create_time', '创建时间', 'create_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 11, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('370a6eebc2d732eaf121fe0830d853a6', 'e5464aa8fa7b47c580e91593cf9b46dc', 'wl_code', '物料编码', NULL, 0, 1, 'String', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 7, 'admin', '2019-04-24 17:09:49', '2019-04-24 11:05:10', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('37e2548874f09ef7d08642a30bc918fa', 'fbc35f067da94a70adb622ddba259352', 'group_name', '小组名', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 7, NULL, NULL, '2019-07-03 19:44:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('391e7cbd9f29743b11bb555c50547b1f', '32f75e4043ef4070919dbd4337186a3d', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, 'admin', '2019-04-11 10:15:32', '2019-03-27 15:54:49', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('3b439859f98e30e34d25e983eb22e408', '402860816bff91c0016bff91c7010001', 'award_time', '获奖时间', 'award_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 3, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('3bf44e68de518f3ddf72b87671d0ff90', '8994f2817b5a45d9890aa04497a317c5', 'update_by', '更新人登录名称', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', 3, NULL, NULL, '2019-03-23 11:39:16', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('3cd2061ea15ce9eeb4b7cf2e544ccb6b', 'd35109c3632c4952a19ecc094943dd71', 'file_kk', '附件', NULL, 0, 1, 'String', 500, 0, '', '', '', '', 'file', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 12, 'admin', '2019-08-23 23:45:15', '2019-06-10 20:06:57', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('3cfd4d60c7d8409ae716a579bcb0910d', '402860816bff91c0016bff91c0cb0000', 'sys_org_code', '组织机构编码', 'sys_org_code', 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 16, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('3e32f6c30c9028872388f70743c5d6a5', '402860816bff91c0016bff91c0cb0000', 'reason', '申请理由', 'reason', 0, 1, 'string', 200, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 9, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('3e70d1c516c3533c6698300665c669e1', '402860816bff91c0016bff91c0cb0000', 'id', 'id', 'id', 1, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'group', '', '', 1, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:31', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('3f2ace8f968a0e5b91d1340ee2957cda', '402860816bff91c0016bff91d8830007', 'real_name', '姓名', 'real_name', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 3, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('40471eb4560bf0bbd2ffef17d48a269d', 'dbf4675875e14676a3f9a8b2b8941140', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, NULL, NULL, '2019-05-27 18:02:07', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('404b516d4f2229f292783db595b02ba1', '402860816bff91c0016bff91d8830007', 'update_time', '更新时间', 'update_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 13, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('405de5ea82e54138a0613dd41b006dfb', '56870166aba54ebfacb20ba6c770bd73', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, 'admin', '2019-11-21 17:58:27', '2019-04-20 11:38:39', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('4164314d6a51d100169a29872b7504d8', '402860816bff91c0016bff91ca7e0002', 'cert_time', '发证时间', 'cert_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 3, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('41d4215c01b0d26871f2cb83d3e532ae', '402860816bff91c0016bff91c0cb0000', 'bpm_status', '流程状态', NULL, 0, 1, 'String', 32, 0, '1', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 17, 'admin', '2019-07-19 18:09:01', '2019-07-19 15:35:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('422a44a15fa39fd57c3c23eb601f7c03', '56870166aba54ebfacb20ba6c770bd73', 'descc', '描述', NULL, 0, 1, 'String', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 8, 'admin', '2019-11-21 17:58:28', '2019-04-20 11:38:39', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('42cccfa014c9e131a0a1b23f563d3688', '402860816bff91c0016bffa220a9000b', 'sex', '性别', 'sex', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 6, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('4312f618c83e07db82e468b81a1eaa45', '402860816bff91c0016bffa220a9000b', 'photo', '照片', 'photo', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 20, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('435b57180fc995e3c4ec42516963bca3', '4028839a6de2ebd3016de2ebd3870000', 'wl_code', '物料编码', 'wl_code', 0, 1, 'string', 60, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 6, NULL, NULL, '2019-10-19 15:29:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('44bdc595f1e565fc053e01134b92bb47', 'd3ae1c692b9640e0a091f8c46e17bb01', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, NULL, NULL, '2019-07-24 14:47:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('44e81e24d2384b0f187e8f69eda55390', '402860816bff91c0016bff91cda80003', 'create_time', '创建时间', 'create_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 10, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('45c0a1a89a1e2a72533b9af894be1011', '27fc5f91274344afa7673a732b279939', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, 'admin', '2019-07-01 16:28:20', '2019-07-01 16:26:42', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('45d59eb647257fcbcb9d143ff1ba2080', 'deea5a8ec619460c9245ba85dbc59e80', 'pro_type', '产品类型', NULL, 0, 1, 'String', 32, 0, '', 'sex', '', '', 'radio', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 9, 'admin', '2019-06-10 16:07:16', '2019-04-23 20:54:08', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('469b250595f15dfebe69991d72e4bfb2', 'e9faf717024b4aae95cff224ae9b6d97', 'name', '员工姓名', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 6, 'admin', '2019-07-03 18:23:49', '2019-07-03 18:22:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('46be01bef342519e268902d0d36a7473', 'deea5a8ec619460c9245ba85dbc59e80', 'descc', '描述', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 11, 'admin', '2019-06-10 16:07:16', '2019-04-20 11:41:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('46f1a875f86a4f48d0540ad0d5e667d7', '56870166aba54ebfacb20ba6c770bd73', 'order_date', '下单时间', NULL, 0, 1, 'Date', 32, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 7, 'admin', '2019-11-21 17:58:28', '2019-04-20 11:38:39', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('47c21a6b45e59a6b70bb9c0cc4510a68', '1acb6f81a1d9439da6cc4e868617b565', 'integral_val', '积分值', NULL, 0, 1, 'int', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 13, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('47fa05530f3537a1be8f9e7a9e98be82', 'd35109c3632c4952a19ecc094943dd71', 'sex', '性别', NULL, 0, 1, 'string', 32, 0, '', 'sex', '', '', 'list', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', 6, 'admin', '2019-08-23 23:45:15', '2019-03-15 14:24:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('4851697fdf63709d2bc7451b7415f4af', '32f75e4043ef4070919dbd4337186a3d', 'sex', '性别', NULL, 0, 1, 'String', 32, 0, '1', 'sex', '', '', 'list', '', 120, NULL, '0', '', '', 1, 1, 1, 0, 'single', '', '', 6, 'admin', '2019-04-11 10:15:32', '2019-03-27 15:54:49', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('485a8ddce2c033f88af674ec98b68e32', '402860816bff91c0016bffa220a9000b', 'create_time', '创建时间', 'create_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 40, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('492a462b212fc34b0ee70e872684ed7e', '7ea60a25fa27470e9080d6a921aabbd1', 'name', '用户名', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 6, NULL, NULL, '2019-04-17 00:22:21', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('499a5dac033a01ce58009e4c5b786697', 'e9faf717024b4aae95cff224ae9b6d97', 'age', '员工年龄', NULL, 0, 1, 'int', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 7, 'admin', '2019-07-03 18:23:49', '2019-07-03 18:22:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('49cd3dbd4f7f7cf0d19b1ee1045cfa69', 'e67d26b610dd414c884c4dbb24e71ce3', 'post_code', '岗位编码', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 6, 'admin', '2019-04-24 11:03:32', '2019-04-24 11:02:57', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('4ba7c553ca4babcec75273c531cd65e1', '402860816bff91c0016bff91cfea0004', 'workplace', '工作单位', 'workplace', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 5, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('4c2cba9fc950333421c4193576b8384d', '32f75e4043ef4070919dbd4337186a3d', 'salary', '工资', NULL, 0, 1, 'double', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 10, 'admin', '2019-04-11 10:15:32', '2019-03-28 15:24:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('4c570c5cf05590348e12621ca62773cf', '402860816aa5921f016aa5921f480000', 'name', '请假人', 'name', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 2, 'admin', '2019-05-11 15:31:54', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('4cacfa054e96791ab938b5c8f8e02cd1', '27fc5f91274344afa7673a732b279939', 'bpm_status', '流程状态', NULL, 0, 1, 'String', 2, 0, '', 'bpm_status', '', '', 'list', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 9, NULL, NULL, '2019-07-01 16:28:20', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('4f718d95ad9de33eac18fd0663e4c1f1', '32f75e4043ef4070919dbd4337186a3d', 'birthday', '生日', NULL, 0, 1, 'Date', 32, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 1, 1, 1, 0, 'single', '', '', 8, 'admin', '2019-04-11 10:15:32', '2019-03-27 15:54:49', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('4f7cba71de7afe6efbd024f5f9935521', '402860816bff91c0016bff91cda80003', 'to_time', '截止时间', 'to_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 4, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('506c9b0b2331a24e5c284274d28fe569', '27fc5f91274344afa7673a732b279939', 'kkk', '描述', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 8, 'admin', '2019-07-01 16:28:20', '2019-07-01 16:26:42', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('508eb28e1409a2a9501cdf6fd7eb24c7', 'dbf4675875e14676a3f9a8b2b8941140', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, NULL, NULL, '2019-05-27 18:02:07', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('509a4f63f02e784bc04499a6a9be8528', 'd35109c3632c4952a19ecc094943dd71', 'update_by', '更新人登录名称', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, 'admin', '2019-08-23 23:45:15', '2019-03-15 14:24:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('519f68557b953fc2d38400182b187366', '402860816bff91c0016bffa220a9000b', 'residence_type', '户籍类别', 'residence_type', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 13, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('52ee861bc1b62cd8e4f10632b3d9d1b2', '79091e8277c744158530321513119c68', 'name', '顺序会签标题', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 6, 'admin', '2019-05-11 15:29:47', '2019-05-11 15:27:17', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('56a7800e4e476812c74217c2aad781aa', '32feeb502544416c9bf41329c10a88f4', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 5, 'admin', '2019-08-23 20:03:40', '2019-07-02 18:23:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('56cd0a76f922bf76d982b24a509e4782', '4028839a6de2ebd3016de2ebd3870000', 'create_time', '创建日期', 'create_time', 0, 1, 'Date', 0, 0, NULL, NULL, NULL, NULL, 'date', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 3, NULL, NULL, '2019-10-19 15:29:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('56e247f12d62b49cd9bd537e3efecf16', '402860816bff91c0016bff91c0cb0000', 'create_by', '创建人', 'create_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 12, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('588400f6ebcdd0bc9bb560dd36636af9', 'e2faf977fdaf4b25a524f58c2441a51c', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, 'admin', '2019-06-10 17:27:00', '2019-04-24 17:12:11', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('58a96f945912d33b64ebf5dee98156dc', '402860816bff91c0016bffa220a9000b', 'mobile', '手机号', 'mobile', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 19, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('58eea85add4788b83c893092434bc413', 'fb19fb067cd841f9ae93d4eb3b883dc0', 'update_time', '更新日期', NULL, 0, 1, 'date', 20, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', 4, NULL, NULL, '2019-03-23 11:39:48', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('59ae1e853668c676e85329aa029c68a6', '402860816bff91c0016bff91c0cb0000', 'status', '状态（1：申请中 2：通过）', 'status', 0, 1, 'string', 2, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 11, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5a1ab458d88bb766f92c3d791495cdcd', '402860816bff91c0016bff91d2810005', 'age', '年龄', 'age', 0, 1, 'int', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 5, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5a4ef083dd572114aeb581b6828de545', '402860816bff91c0016bff91c7010001', 'award_name', '获奖名称', 'award_name', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 5, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5a621f27aa443fe9eccc73717e4fa172', '4028318169e81b970169e81b97650000', 'method', '请求java方法', 'method', 0, 1, 'string', 500, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 8, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5a655b208d6318ed02f236f15a319b5f', 'fbc35f067da94a70adb622ddba259352', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 5, NULL, NULL, '2019-07-03 19:44:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5a6f88710c49bbe8e2e0ca58e149abad', '402860816bff91c0016bff91cda80003', 'create_by', '创建人', 'create_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 9, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5b17ba693745c258f6b66380ac851e5f', 'd35109c3632c4952a19ecc094943dd71', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', 0, 'admin', '2019-08-23 23:45:15', '2019-03-15 14:24:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5c76f5ecc774d7339eb0c2199c0052bc', '402860816bff91c0016bff91c0cb0000', 'biz_no', '编号', 'biz_no', 0, 1, 'string', 64, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 2, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5c8c8d573e01e4f40b5a7c451515e1d2', '32feeb502544416c9bf41329c10a88f4', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, 'admin', '2019-08-23 20:03:40', '2019-07-02 18:23:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5dfbea516ee2390d712eace5405c5219', '402860816bff91c0016bff91ca7e0002', 'create_by', '创建人', 'create_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 9, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5e4484b7348dc3e59a0c58bdc3828cc0', '27fc5f91274344afa7673a732b279939', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, 'admin', '2019-07-01 16:28:20', '2019-07-01 16:26:42', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('5e4ac29ac2007ceabf93368330290a42', '402860816bff91c0016bff91d8830007', 'order_no', '序号', 'order_no', 0, 1, 'int', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 9, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('617349b18dab429009ccd304fd7d459c', '4028839a6de2ebd3016de2ebd3870000', 'update_by', '更新人', 'update_by', 0, 1, 'string', 50, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 4, NULL, NULL, '2019-10-19 15:29:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('61c7a0058c264dd746eb35e6f50fc15b', '402860816aa5921f016aa5dedcb90009', 'update_time', '更新日期', 'update_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'group', '', '', 5, 'admin', '2019-05-11 15:56:47', '2019-05-11 15:50:08', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('6232ade7e2a0c1e97e2c0945b32e61b6', '402860816bff91c0016bffa220a9000b', 'paying_social_insurance', '是否上社保', 'paying_social_insurance', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 32, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('6490a98dccb6df218feaeb4ce11bc03b', '402860816aa5921f016aa5921f480000', 'update_time', '修改时间', 'update_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'group', '', '', 10, 'admin', '2019-05-11 15:31:54', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('654362725195829005036b3db47ec826', '402860816bff91c0016bffa220a9000b', 'post', '职务', 'post', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 4, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('66a7ef842bc34e105a90186e48167ef2', 'dbf4675875e14676a3f9a8b2b8941140', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, NULL, NULL, '2019-05-27 18:02:07', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('68769fa7e4696e3a28f4cecf63076b7b', '402860816bff91c0016bff91ca7e0002', 'order_no', '序号', 'order_no', 0, 1, 'int', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 8, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('69666f21896136af16a6303aff440156', '402860816bff91c0016bffa220a9000b', 'nation', '民族', 'nation', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 11, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('69d11490788fecfc9fb7d74bf449ba86', '32f75e4043ef4070919dbd4337186a3d', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, 'admin', '2019-04-11 10:15:32', '2019-03-27 15:54:49', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('6a30c2e6f01ddd24349da55a37025cc0', 'd35109c3632c4952a19ecc094943dd71', 'top_pic', '头像', NULL, 0, 1, 'String', 500, 0, '', '', '', '', 'image', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 11, 'admin', '2019-08-23 23:45:15', '2019-06-10 20:06:56', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('6b6f3aa00b8e73fb785154e795189739', '402860816aa5921f016aa5dedcb90009', 'start_time', '会签发起时间', 'start_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 7, 'admin', '2019-05-11 15:56:47', '2019-05-11 15:50:08', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('6cfb5acbbb69782bf0c7043b53f595b2', '402860816bff91c0016bff91cda80003', 'update_by', '更新人', 'update_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 11, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('6f73e96a659c200c083006b6fce1f043', '402860816bff91c0016bff91ca7e0002', 'cert_name', '证书名称', 'cert_name', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 4, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7154c75d754a5f88bef2b68829baf576', '4028318169e81b970169e81b97650000', 'operate_type', '操作类型', 'operate_type', 0, 1, 'string', 10, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 4, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('71afb00a1971125ecfa13b4dfa49665e', '402860816bff91c0016bff91cfea0004', 'order_no', '序号', 'order_no', 0, 1, 'int', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 8, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('71d5b0675df5aba71688c9d7d75cccee', '4028318169e81b970169e81b97650000', 'log_type', '日志类型（1登录日志，2操作日志）', 'log_type', 0, 1, 'string', 10, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 2, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('71e9ab74dae687837365e50eed090591', '1acb6f81a1d9439da6cc4e868617b565', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7280c56a210e6a47794fda855d0c6abb', 'fbc35f067da94a70adb622ddba259352', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, NULL, NULL, '2019-07-03 19:44:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('72e784af5c47bbbc0534b29bf656bd61', '4028839a6de2ebd3016de2ebd3870000', 'id', '主键', 'id', 1, 0, 'string', 36, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 0, 0, 0, 'group', NULL, NULL, 1, NULL, NULL, '2019-10-19 15:29:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7365f05f551092716223d5d449efd8c7', 'beee191324fd40c1afec4fda18bd9d47', 'name', 'ss', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 6, 'admin', '2019-04-13 13:41:13', '2019-04-13 13:40:56', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('742329ccdb185cf5d3e0b5b0c05dcffa', '402860816bff91c0016bffa220a9000b', 'interest', '兴趣爱好', 'interest', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 34, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('744444a7ada3bbb05c6b114b5ba0d477', '402860816aa5921f016aa5dedcb90009', 'id', 'id', 'id', 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'group', '', '', 1, 'admin', '2019-05-11 15:56:47', '2019-05-11 15:50:08', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('74af99545de724a4abd2022581a36026', 'fb7125a344a649b990c12949945cb6c1', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, 'admin', '2019-03-26 19:24:11', '2019-03-26 19:01:52', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('756b07656386dbd91245f7ffda32ae61', '402860816bff91c0016bff91d8830007', 'id', 'id', 'id', 1, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'group', '', '', 1, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('75841fa7c75ebdc94655bd5e44fbc9f6', '402860816bff91c0016bffa220a9000b', 'native_place', '籍贯', 'native_place', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 10, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('75ba781c67711bed71bba1c3e3c68e11', '8994f2817b5a45d9890aa04497a317c5', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', 0, NULL, NULL, '2019-03-23 11:39:16', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('75e82f151e8cc12455f7f0d25bf4dac0', '4028839a6de2ebd3016de2ebd3870000', 'wl_name', '物料名称', 'wl_name', 0, 1, 'string', 100, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 7, NULL, NULL, '2019-10-19 15:29:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7639c1bc4327f1f674ffeab2ca261134', '32f75e4043ef4070919dbd4337186a3d', 'update_by', '更新人登录名称', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, 'admin', '2019-04-11 10:15:32', '2019-03-27 15:54:49', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('766ca866b72d118f5d8883de46a8c043', '4028839a6de2ebd3016de2ebd3870000', 'supplier', '供应商', 'supplier', 0, 1, 'string', 32, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 15, NULL, NULL, '2019-10-19 15:29:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('78a40344207c791b8d7ac7de721ce1c4', '79091e8277c744158530321513119c68', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, 'admin', '2019-05-11 15:29:47', '2019-05-11 15:27:17', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('78eb7e3b77cd49f9acb9b024cfe834e1', '402860816aa5921f016aa5dedcb90009', 'create_time', '创建日期', 'create_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'group', '', '', 3, 'admin', '2019-05-11 15:56:47', '2019-05-11 15:50:08', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('78fd804d93dc716fd8c2ccc45f788565', 'fb7125a344a649b990c12949945cb6c1', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, 'admin', '2019-03-26 19:24:11', '2019-03-26 19:01:52', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('790c9f3dba773ed9a6ea3ad627393f57', '402860816bff91c0016bffa220a9000b', 'archives_location', '档案所在地', 'archives_location', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 36, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7a665ed90ef64b4d65632c941e5795b2', '4b556f0168f64976a3d20bfb932bc798', 'sex', '性别', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 7, NULL, NULL, '2019-04-12 23:38:29', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7b642d983ac06bfef91edde2c932dbe7', '1acb6f81a1d9439da6cc4e868617b565', 'xg_shangxian', '选购上限', NULL, 0, 1, 'int', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 14, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7b849e57debfb889caea5e0fef09062b', 'beee191324fd40c1afec4fda18bd9d47', 'sex2', 'dd', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 7, 'admin', '2019-04-13 13:41:13', '2019-04-13 13:40:56', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7beb639aa9ffda07edb5ce1e49c2287f', '402860816bff91c0016bff91d2810005', 'update_time', '更新时间', 'update_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 13, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7bf091da401b74d55b107fe9f930ea54', '4028839a6de2ebd3016de2ebd3870000', 'create_by', '创建人', 'create_by', 0, 1, 'string', 50, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 2, NULL, NULL, '2019-10-19 15:29:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7c6aecc377816c69147f1193b17dfcc5', 'e9faf717024b4aae95cff224ae9b6d97', 'sex', '员工性别', NULL, 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 8, 'admin', '2019-07-03 18:23:49', '2019-07-03 18:22:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7cadf4e0be0b173bb5b8d39613e25190', '402860816bff91c0016bffa220a9000b', 'residence_postcode', '户籍邮编', 'residence_postcode', 0, 1, 'string', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 29, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7d107728408c21ece332406a21f2d692', '402860816bff91c0016bff91cfea0004', 'update_by', '更新人', 'update_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 11, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7e066f60680158d47b328ef519d80e49', 'beee191324fd40c1afec4fda18bd9d47', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, 'admin', '2019-04-13 13:41:13', '2019-04-13 13:40:56', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('7f10901c6ade3aa9d9ff46ed7039c70f', '1acb6f81a1d9439da6cc4e868617b565', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('81ed9556c9fda1bbb46d94a53a6c90c7', '402860816bff91c0016bff91c0cb0000', 'depart_name', '部门名称', 'depart', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 7, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8422485e1cbf4455f9ded7d0af59379c', '402860816bff91c0016bff91cfea0004', 'to_time', '截止时间', 'to_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 4, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('845c70568d44a074f067d6d277950525', '402860816bff91c0016bffa220a9000b', 'entrytime', '入职时间', 'entrytime', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 23, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8529ddaed8d5f3d9084e873203d55cac', '402860816bff91c0016bffa220a9000b', 'marital_status', '婚姻状况', 'marital_status', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 24, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('857a0daa9cd8a058f2f15fc7c5fb3571', '402860816bff91c0016bffa220a9000b', 'email', '邮箱', 'email', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 17, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8652ca69a947fd4c961a3ac7c0fa252a', 'fb19fb067cd841f9ae93d4eb3b883dc0', 'create_by', '创建人登录名称', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', 1, NULL, NULL, '2019-03-23 11:39:48', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('86bbafef5683674a736cf7241c458d44', '27fc5f91274344afa7673a732b279939', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, 'admin', '2019-07-01 16:28:20', '2019-07-01 16:26:42', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('86e0f3a8f31c60698157f139ed993954', '402860816bff91c0016bffa220a9000b', 'having_reserve_funds', '是否有公积金', 'having_reserve_funds', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 33, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('873e2bb041b17bff77d3aca72900ea1b', '56870166aba54ebfacb20ba6c770bd73', 'order_code', '订单编码', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 6, 'admin', '2019-11-21 17:58:28', '2019-04-20 11:38:39', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('877391ae770a4ce2c95181ef410982ce', '4028318169e81b970169e81b97650000', 'request_param', '请求参数', 'request_param', 0, 1, 'string', 255, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 10, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('87e82e3c723a6abb020122babdac6bd1', '8994f2817b5a45d9890aa04497a317c5', 'create_by', '创建人登录名称', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', 1, NULL, NULL, '2019-03-23 11:39:16', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('87f7a2703c5850f0b063dd866d0e2917', '402860816bff91c0016bffa220a9000b', 'birthday', '出生日期', 'birthday', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 7, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('87fafe1a4a8a626e3875697574c19f15', '402860816bff91c0016bff91d2810005', 'sys_org_code', '组织机构编码', 'sys_org_code', 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 14, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('88a12570e14c9f6f442e731ae5ad0eb1', 'beee191324fd40c1afec4fda18bd9d47', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, 'admin', '2019-04-13 13:41:13', '2019-04-13 13:40:56', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('89ab9eedbac6141e7a0df6d37a3655d0', 'e67d26b610dd414c884c4dbb24e71ce3', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, 'admin', '2019-04-24 11:03:32', '2019-04-24 11:02:57', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8a24fb45e2af120c253c8b61c0085f7a', '402860816bff91c0016bff91cda80003', 'sys_org_code', '组织机构编码', 'sys_org_code', 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 13, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8ac8a0c0087469a4e7579229ff17f273', 'e5464aa8fa7b47c580e91593cf9b46dc', 'jifen', '合计积分', NULL, 0, 1, 'int', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 10, 'admin', '2019-04-24 17:09:49', '2019-04-24 11:05:10', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8bd4deadc9e96c1a6d7abd77033105f6', 'e67d26b610dd414c884c4dbb24e71ce3', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 5, 'admin', '2019-04-24 11:03:32', '2019-04-24 11:02:57', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8c6518fec11fc4769ba4eb770c9e00f7', '4028839a6de2ebd3016de2ebd3870000', 'integral_val', '积分值', 'integral_val', 0, 1, 'int', 10, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 11, NULL, NULL, '2019-10-19 15:29:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8ca56210938fbe649f840e505eb9fd41', '56870166aba54ebfacb20ba6c770bd73', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, 'admin', '2019-11-21 17:58:27', '2019-04-20 11:38:39', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8ea43fd1e4ce82becee61b2f1e2e843f', '32feeb502544416c9bf41329c10a88f4', 'sex', '性别', NULL, 0, 1, 'String', 32, 0, '', 'sex', '', '', 'list', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 8, 'admin', '2019-08-23 20:03:40', '2019-07-02 18:23:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8f1d302868640b72cef52171a023a203', 'e9faf717024b4aae95cff224ae9b6d97', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, 'admin', '2019-07-03 18:23:49', '2019-07-03 18:22:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8f3e6fb68179c690f748f3c541fb50f1', '7ea60a25fa27470e9080d6a921aabbd1', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, NULL, NULL, '2019-04-17 00:22:21', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('8fc0be84bed1216635c69af918e097ff', '402860816aa5921f016aa5dedcb90009', 'name', '并行会签标题', 'name', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 6, 'admin', '2019-05-11 15:56:47', '2019-05-11 15:50:08', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('90a822b8a63bbbc1e9575c9f4e21e021', 'd35109c3632c4952a19ecc094943dd71', 'descc', '描述', NULL, 0, 1, 'string', 500, 0, '', '', '', '', 'textarea', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', 8, 'admin', '2019-08-23 23:45:15', '2019-03-15 14:24:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('90f39a6e29dae2e1fbb59d7d605f7c09', '1acb6f81a1d9439da6cc4e868617b565', 'iz_valid', '启用状态', NULL, 0, 1, 'String', 2, 0, '', 'air_china_valid', '', '', 'list', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 11, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9370c9304af30b8d29defe0a5ada6e5b', '62e29cdb81ac44d1a2d8ff89851b853d', 'DC_DDSA', 'DD', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 6, NULL, NULL, '2019-05-11 14:01:14', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9371f61d39c5d57ddb0a2db96b2e2412', '402860816bff91c0016bffa220a9000b', 'speciality', '专业', 'speciality', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 15, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('947174892512ea97fafde899d427ea7e', '402860816bff91c0016bff91c0cb0000', 'real_name', '姓名', 'real_name', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 4, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('94b8bf435175cc545366e11992280757', '32f75e4043ef4070919dbd4337186a3d', 'age', '年龄', NULL, 0, 1, 'int', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 1, 1, 1, 0, 'group', '', '', 7, 'admin', '2019-04-11 10:15:32', '2019-03-27 15:54:49', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('94e682cb802777fe4205536888f69353', '402860816bff91c0016bff91d2810005', 'create_by', '创建人', 'create_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 10, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('957386b500be42a200d6a56d54345392', 'deea5a8ec619460c9245ba85dbc59e80', 'num', '数量', NULL, 0, 1, 'int', 32, 0, '', '', '', '', 'text', '', 120, 'n', '0', '', '', 0, 1, 1, 0, 'single', '', '', 8, 'admin', '2019-06-10 16:07:16', '2019-04-20 11:41:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('960d2847922b61dadeb3518ef55fb0c1', '1acb6f81a1d9439da6cc4e868617b565', 'wl_name', '物料名称', NULL, 0, 1, 'String', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 7, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9665f02764774fdd77c19923d3ff3c3e', '4028318169e81b970169e81b97650000', 'cost_time', '耗时', 'cost_time', 0, 1, 'string', 19, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 12, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('966a4988298d5cb0be47848735ce8cb7', '4028839a6de2ebd3016de2ebd3870000', 'xg_shangxian', '选购上限', 'xg_shangxian', 0, 1, 'int', 10, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 12, NULL, NULL, '2019-10-19 15:29:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('96c585a4f71e5c38ed25b9741366365b', '402860816bff91c0016bff91c7010001', 'sys_org_code', '组织机构编码', 'sys_org_code', 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 11, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9765efa2cafde6d0ede2215848c9e80b', '32f75e4043ef4070919dbd4337186a3d', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 0, 'admin', '2019-04-11 10:15:32', '2019-03-27 15:54:49', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('98e82cb1595609a3b42fa75c60ac1229', '402860816bff91c0016bff91d2810005', 'update_by', '更新人', 'update_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 12, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9914a0c84805e72c4b6075e36edb13f9', '402860816aa5921f016aa5921f480000', 'create_time', '创建时间', 'create_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'group', '', '', 9, 'admin', '2019-05-11 15:31:54', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9920ecec9c9109fc6b93e86f8fdfa03b', '402860816bff91c0016bffa220a9000b', 'depart_name', '所在部门', 'depart_name', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 1, 1, 1, 0, 'group', '', '', 2, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('99b43bbb23237815ebb74b12b4d7ea2f', '62e29cdb81ac44d1a2d8ff89851b853d', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, NULL, NULL, '2019-05-11 14:01:14', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9a579c506f75f75baf88352a5eb2c249', '1acb6f81a1d9439da6cc4e868617b565', 'bpm_status', '流程状态', NULL, 0, 1, 'String', 2, 0, '1', 'bpm_status', '', '', 'list', '', 120, NULL, '0', '', '', 0, 0, 1, 0, 'single', '', '', 16, 'admin', '2019-06-10 14:47:14', '2019-05-07 16:54:43', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9c40fb4db8afed3c682c6b8a732fd69d', 'e2faf977fdaf4b25a524f58c2441a51c', 'post', '用户岗位', NULL, 0, 1, 'String', 32, 0, '', 'post_code', 'air_china_post_materiel_main', 'post_name', 'sel_search', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 8, 'admin', '2019-06-10 17:27:00', '2019-04-24 17:12:11', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9d85bafa399f28a40e1de1eeef747223', '4028318169e81b970169e81b97650000', 'ip', 'IP', 'ip', 0, 1, 'string', 100, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 7, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9d89ff1a019f41d80307652041490944', '32feeb502544416c9bf41329c10a88f4', 'name', '请假人', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 7, 'admin', '2019-08-23 20:03:40', '2019-07-02 18:23:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('9e50680eb4e79b3af352a5933d239dff', 'dbf4675875e14676a3f9a8b2b8941140', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, NULL, NULL, '2019-05-27 18:02:07', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a01a7fe5660206e6f407ed98b6c732d6', '402860816bff91c0016bff91cfea0004', 'phone', '联系方式', 'phone', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 7, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a1f5daba36f536e7acf6a939826183b0', 'fb19fb067cd841f9ae93d4eb3b883dc0', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', 0, NULL, NULL, '2019-03-23 11:39:48', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a45eba33810c485b9d8e6f70818a1dfa', '402860816aa5921f016aa5921f480000', 'bpm_status', '流程状态', 'bpm_status', 0, 1, 'string', 50, 0, '1', 'bpm_status', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 1, 0, 'group', '', '', 7, 'admin', '2019-05-11 15:31:54', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a6471d4fb3dbffef01dab1f7d452bb30', '27fc5f91274344afa7673a732b279939', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 5, 'admin', '2019-07-01 16:28:20', '2019-07-01 16:26:42', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a6722b498602d7d7b5177b16789d8cc1', 'e5464aa8fa7b47c580e91593cf9b46dc', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, 'admin', '2019-04-24 17:09:48', '2019-04-24 11:05:10', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a76f561057ac9e43a8ca09e478a1eab8', '402860816bff91c0016bff91ca7e0002', 'update_time', '更新时间', 'update_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 12, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a7822f6e4cffb37fc0729cbd4cfd8655', '32f75e4043ef4070919dbd4337186a3d', 'name', '用户名', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 1, 1, 1, 0, 'single', '', '', 5, 'admin', '2019-04-11 10:15:32', '2019-03-27 15:54:49', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a82ca42a76e9d2b8dae6d57dbb5edb54', 'deea5a8ec619460c9245ba85dbc59e80', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, 'admin', '2019-06-10 16:07:16', '2019-04-20 11:41:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a940adc4585fa3b5bd2114ea9abe8491', '402860816bff91c0016bff91ca7e0002', 'cert_level', '证书级别', 'cert_level', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 5, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a94f1d7da64f3aa35c32155ea00ccb2f', '402860816bff91c0016bffa220a9000b', 'id', 'id', 'id', 1, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'group', '', '', 1, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('a9780eace237a15f26931dd6a9ec02e9', '758334cb1e7445e2822b60e807aec4a3', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, NULL, NULL, '2019-10-18 18:02:09', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('aa07931514727913413880b7a2b76dcb', 'd3ae1c692b9640e0a091f8c46e17bb01', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, NULL, NULL, '2019-07-24 14:47:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('aa4780601419c21dabb6c42fc511e71c', '402860816bff91c0016bffa220a9000b', 'have_children', '有无子女', 'have_children', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 25, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ab10e0aa029ded2f4420a33420de225d', '1acb6f81a1d9439da6cc4e868617b565', 'wl_code', '物料编码', NULL, 0, 1, 'String', 60, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 6, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ab1f880ba593f3757dac70e003945aa2', '402860816bff91c0016bff91c0cb0000', 'depart_id', '部门ID', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 6, 'admin', '2019-07-19 18:09:01', '2019-07-17 19:38:45', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ab58f43f853fd1f65f83c22966883afb', 'beee191324fd40c1afec4fda18bd9d47', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, 'admin', '2019-04-13 13:41:13', '2019-04-13 13:40:56', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ab8e6f1cca421c5ce395a2c1fdfd2100', '32feeb502544416c9bf41329c10a88f4', 'sys_org_code', '所属部门', NULL, 0, 1, 'string', 64, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 6, 'admin', '2019-08-23 20:03:40', '2019-07-02 18:23:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('abe61a8ddf966a979457b763329a537b', 'e5464aa8fa7b47c580e91593cf9b46dc', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, 'admin', '2019-04-24 17:09:49', '2019-04-24 11:05:10', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ac91565da5fb8fe43a4da3dec660b25f', '402860816bff91c0016bff91c7010001', 'award_place', '获奖地点', 'award_place', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 4, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('acff5c8aef3b6288b87fd91215012206', 'e5464aa8fa7b47c580e91593cf9b46dc', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, 'admin', '2019-04-24 17:09:49', '2019-04-24 11:05:10', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ad061417d5b53c67975eb83657505218', '73162c3b8161413e8ecdca7eb288d0c9', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, 'admin', '2019-07-01 14:23:32', '2019-06-10 15:18:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ad93762c6c4a1dd8331e5fa11215b568', 'e2faf977fdaf4b25a524f58c2441a51c', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, 'admin', '2019-06-10 17:27:00', '2019-04-24 17:12:11', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ae31da96f38fc2941cb93d1bb1ab9431', 'deea5a8ec619460c9245ba85dbc59e80', 'product_name', '产品名字', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 6, 'admin', '2019-06-10 16:07:16', '2019-04-20 11:41:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ae77bb317366622698c8ab9bf2325833', 'deea5a8ec619460c9245ba85dbc59e80', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, 'admin', '2019-06-10 16:07:16', '2019-04-20 11:41:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('af0fe0df8b626129de62e22212732517', '402860816bff91c0016bff91cda80003', 'speciality', '专业', 'speciality', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 6, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('af6c582b902e2f2bf9930eba61ae7938', '73162c3b8161413e8ecdca7eb288d0c9', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 5, 'admin', '2019-07-01 14:23:32', '2019-06-10 15:18:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('afd3ef1d494a9b69d2c7a3cdde937f6f', '402860816bff91c0016bffa220a9000b', 'create_by', '创建人', 'create_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 39, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b01304904babd7479de2acfe8a77157f', '402860816aa5921f016aa5921f480000', 'id', 'ID', 'id', 1, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'group', '', '', 1, 'admin', '2019-05-11 15:31:54', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b05b4cbb74f389a7376f51ed9fd97030', '402860816bff91c0016bff91d8830007', 'create_by', '创建人', 'create_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 10, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b0a06bdbefd304d81a1838d8d94deda9', '4b556f0168f64976a3d20bfb932bc798', 'name', '用户名', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 6, NULL, NULL, '2019-04-12 23:38:28', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b0b1cf271dd6b221a902da2d2f8f889a', 'e9faf717024b4aae95cff224ae9b6d97', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 5, 'admin', '2019-07-03 18:23:49', '2019-07-03 18:22:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b1fc6e2ca671b19e57b08a4f57fc2454', 'fb7125a344a649b990c12949945cb6c1', 'update_time', '更新日期', NULL, 0, 1, 'date', 20, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, 'admin', '2019-03-26 19:24:11', '2019-03-26 19:01:52', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b22694cf34ffb967b8717647816ad5df', 'e5464aa8fa7b47c580e91593cf9b46dc', 'fk_id', '外键', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', 'air_china_post_materiel_main', 'id', 15, 'admin', '2019-04-24 17:09:49', '2019-04-24 11:05:10', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b276facab025f9750b0aff391693cc4b', '402860816bff91c0016bff91c7010001', 'id', 'id', 'id', 1, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'group', '', '', 1, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b2b0cb30159639bb1190e150322b7541', '4028839a6de2ebd3016de2ebd3870000', 'wl_unit', '计量单位', 'wl_unit', 0, 1, 'string', 100, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 14, NULL, NULL, '2019-10-19 15:29:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b3542d3e7908ed885ecc4ba9e7300705', '4b556f0168f64976a3d20bfb932bc798', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, NULL, NULL, '2019-04-12 23:38:28', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b47af4d937e55c6208939bac5378bfad', '62e29cdb81ac44d1a2d8ff89851b853d', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 5, NULL, NULL, '2019-05-11 14:01:14', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b53203fc52d067bb4730dbcb7e496bd3', '56870166aba54ebfacb20ba6c770bd73', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, 'admin', '2019-11-21 17:58:27', '2019-04-20 11:38:39', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b5cfd3c9691a884430f3d9cd5ecb211f', 'e2faf977fdaf4b25a524f58c2441a51c', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, 'admin', '2019-06-10 17:27:00', '2019-04-24 17:12:11', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b6874a05734cad8bd96ffd2f31f1ebca', '402860816bff91c0016bff91c7010001', 'create_by', '创建人', 'create_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 7, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b733fa73519603b22d401fabbf9e9781', '402860816bff91c0016bff91c0cb0000', 'hiredate', '入职时间', 'hiredate', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 5, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b7938e4518f9062ce62702cf45986e06', 'e2faf977fdaf4b25a524f58c2441a51c', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, 'admin', '2019-06-10 17:27:00', '2019-04-24 17:12:11', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b7a1243aaa712e2c152c0c7a46f88683', '402860816bff91c0016bff91d8830007', 'age', '年龄', 'age', 0, 1, 'int', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 5, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b91258e3dc15b28c2e3f0d934e6e27e8', 'fb7125a344a649b990c12949945cb6c1', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', 0, NULL, NULL, '2019-03-26 19:01:52', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b92572ae142f8dd5f2ac02fb45e6b2c1', 'e2faf977fdaf4b25a524f58c2441a51c', 'name', '用户名', NULL, 0, 1, 'String', 32, 0, '', 'realname,username', 'report_user', 'name,account', 'sel_search', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 6, 'admin', '2019-06-10 17:27:00', '2019-04-24 17:12:11', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('b9fbace24688c9c9a8c9be72c1d014e7', '402860816bff91c0016bffa220a9000b', 'phone', '电话', 'phone', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 18, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ba5f4b2affa94f36eda7f6f133db7ae3', '402860816bff91c0016bff91d2810005', 'workplace', '工作单位', 'workplace', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 6, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bad02e68ea37bf387337516af84a1ddb', '73162c3b8161413e8ecdca7eb288d0c9', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, 'admin', '2019-07-01 14:23:32', '2019-06-10 15:18:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bb44475e1d1738a19745bf9f3ebf9e40', '402860816bff91c0016bff91cfea0004', 'update_time', '更新时间', 'update_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 12, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bbbb38347b1a5340a1d293e455c632ce', 'fb19fb067cd841f9ae93d4eb3b883dc0', '3', '4', NULL, 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', 6, NULL, NULL, '2019-03-23 11:39:48', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bc648624ad14c826bbc6e9b23a2b9858', '402860816bff91c0016bff91ca7e0002', 'employee_id', '员工ID', 'employee_id', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', 'oa_employee_info', 'id', 2, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bc793fdbef9f6474425456c4eb9d197a', '402860816bff91c0016bff91cfea0004', 'witness', '证明人', 'references', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 6, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bc7df6f3cf49f670c36a3de25e25e715', '402860816bff91c0016bff91d2810005', 'order_no', '序号', 'order_no', 0, 1, 'int', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 9, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bd39cb237049ac60218b3f4dd844f30c', '402860816bff91c0016bffa220a9000b', 'current_address', '现居住地', 'current_address', 0, 1, 'string', 200, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 30, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bde81809057b1a4c974fa0f090501fdd', '402860816aa5921f016aa5dedcb90009', 'update_by', '更新人', 'update_by', 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'group', '', '', 4, 'admin', '2019-05-11 15:56:47', '2019-05-11 15:50:08', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('be3f8c157d8a1b40e6f7b836552a8095', '8994f2817b5a45d9890aa04497a317c5', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', 2, NULL, NULL, '2019-03-23 11:39:16', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('be5eeda7d51dace73d3818bd8467b53b', '402860816bff91c0016bff91c0cb0000', 'update_time', '更新时间', 'update_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 15, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('be868eed386da3cfcf49ea9afcdadf11', 'd35109c3632c4952a19ecc094943dd71', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, 'admin', '2019-08-23 23:45:15', '2019-03-15 14:24:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bec3082fc5f0f194be5cd72cc2866ff4', 'e5464aa8fa7b47c580e91593cf9b46dc', 'wl_name', '物料名字', NULL, 0, 1, 'String', 200, 0, '', 'wl_code', 'air_china_materiel', 'wl_name', 'list', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 6, 'admin', '2019-04-24 17:09:49', '2019-04-24 11:05:10', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bed0bc67f570613eaa6a1bd8bcaaddcc', '4b556f0168f64976a3d20bfb932bc798', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, NULL, NULL, '2019-04-12 23:38:28', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bf61aa04c5ca77ad54c764f8f8b2bdec', '402860816bff91c0016bff91d8830007', 'update_by', '更新人', 'update_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 12, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('bfc2f19fae367f885adb8bd82a344391', '4028318169e81b970169e81b97650000', 'userid', '操作用户账号', 'userid', 0, 1, 'string', 32, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 5, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('c0d66c95773774e7ac1f2a88df307e7a', '402860816aa5921f016aa5921f480000', 'reason', '请假原因', 'reason', 0, 1, 'string', 500, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 6, 'admin', '2019-05-11 15:31:54', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('c29216d975fee50af175bca8c664a475', 'e67d26b610dd414c884c4dbb24e71ce3', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, 'admin', '2019-04-24 11:03:32', '2019-04-24 11:02:56', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('c43d87b6340c29c0c354aa9c579f387f', '32feeb502544416c9bf41329c10a88f4', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, 'admin', '2019-08-23 20:03:40', '2019-07-02 18:23:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('c5dd2fc34ae825ebfced2ec74948654c', '402860816aa5921f016aa5921f480000', 'end_date', '请假结束时间', 'end_date', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 5, 'admin', '2019-05-11 15:31:54', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('c5f6ea01a6523a60df153cc61dc92f4d', 'fbc35f067da94a70adb622ddba259352', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, NULL, NULL, '2019-07-03 19:44:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('c6730e00df5efd77fedf181df29102de', '402860816bff91c0016bff91c7010001', 'update_by', '更新人', 'update_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 9, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('c75a7cb0a21958aa7ca5442f66019669', 'e9faf717024b4aae95cff224ae9b6d97', 'depart', '所属部门', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 9, 'admin', '2019-07-03 18:23:49', '2019-07-03 18:22:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('c772ed9cbe2d1dc69e9ffa73d3487021', '4b556f0168f64976a3d20bfb932bc798', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, NULL, NULL, '2019-04-12 23:38:28', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('c91b697b1bcd2be943fc746e2660bc9e', '402860816bff91c0016bff91d2810005', 'real_name', '姓名', 'real_name', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 3, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('c9b698d3f75aa780ee1eb67ef090b15b', '73162c3b8161413e8ecdca7eb288d0c9', 'wl_code', '物料编码', NULL, 0, 1, 'String', 200, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 8, 'admin', '2019-07-01 14:23:32', '2019-06-10 15:18:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('caf5a071f2299c0f9ff2f3038d6d0fc6', '402860816bff91c0016bff91ca7e0002', 'update_by', '更新人', 'update_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 11, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('cb33a708b95e19085f8c9001d2d5c64c', 'e9faf717024b4aae95cff224ae9b6d97', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, 'admin', '2019-07-03 18:23:49', '2019-07-03 18:22:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('cb7da49a981a1b0acc5f7e8a0130bdcd', 'd35109c3632c4952a19ecc094943dd71', 'user_code', '用户编码', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 0, 0, 'single', '', '', 10, 'admin', '2019-08-23 23:45:15', '2019-05-11 16:26:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('cb871284e845e26e88242a96fac9c576', '402860816bff91c0016bff91c7010001', 'order_no', '序号', 'order_no', 0, 1, 'int', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 6, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('cc1ddc1304d3eb5d9a189da0a509ccd0', '32f75e4043ef4070919dbd4337186a3d', 'create_by', '创建人登录名称', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, 'admin', '2019-04-11 10:15:32', '2019-03-27 15:54:49', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ccf2f331af46ddc411b8039dd187621b', '4028839a6de2ebd3016de2ebd3870000', 'price', '单价', 'price', 0, 1, 'double', 10, 2, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 10, NULL, NULL, '2019-10-19 15:29:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('cee3c1dbf67b4a7d9626b8032897a4c7', '402860816bff91c0016bff91d8830007', 'employee_id', '员工ID', 'employee_id', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', 'oa_employee_info', 'id', 2, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('cf4c5a4c06ae6bac701edfeedfcd16aa', 'd3ae1c692b9640e0a091f8c46e17bb01', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, NULL, NULL, '2019-07-24 14:47:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('cfeb6491427aec2b4db9694af867da23', 'e9faf717024b4aae95cff224ae9b6d97', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, 'admin', '2019-07-03 18:23:49', '2019-07-03 18:22:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d2551b70dc96a45a73b304bf755a996f', '402860816bff91c0016bff91d8830007', 'workplace', '工作单位', 'workplace', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 6, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d29dcdba14ea61808391fff2d927efea', '402860816bff91c0016bff91c0cb0000', 'work_summary', '工作总结', 'work_summary', 0, 1, 'Text', 65535, 0, '', '', '', '', 'textarea', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 10, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d3a701472d27ca8435d6a781a597038d', 'deea5a8ec619460c9245ba85dbc59e80', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, 'admin', '2019-06-10 16:07:16', '2019-04-20 11:41:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d48bfd2a840f9b1d00bd3b5599dca0f0', '402860816bff91c0016bff91cda80003', 'post', '职务', 'post', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 7, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d4d8cae3cd9ea93e378fc14303eee105', 'd35109c3632c4952a19ecc094943dd71', 'create_by', '创建人登录名称', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, 'admin', '2019-08-23 23:45:15', '2019-03-15 14:24:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d4dea775487aef5a7aea41791d3a65db', 'e5464aa8fa7b47c580e91593cf9b46dc', 'cycle_time', '发放周期(年)', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 11, 'admin', '2019-04-24 17:09:49', '2019-04-24 14:09:06', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d52c79620e21128fb69b4e8628cf25cc', 'dbf4675875e14676a3f9a8b2b8941140', 'sys_org_code', '所属部门', NULL, 0, 1, 'string', 64, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 6, NULL, NULL, '2019-05-27 18:02:07', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d530ab1bc3c51e8249a506a25d1003c7', '79091e8277c744158530321513119c68', 'start_time', '会签发起时间', NULL, 0, 1, 'Date', 32, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 7, 'admin', '2019-05-11 15:29:47', '2019-05-11 15:27:17', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d53e70411c206efecb8dcd00174e907c', '62e29cdb81ac44d1a2d8ff89851b853d', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, NULL, NULL, '2019-05-11 14:01:14', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d5df0a35352ee960053686e959e9084b', '1acb6f81a1d9439da6cc4e868617b565', 'wl_unit', '计量单位', NULL, 0, 1, 'String', 100, 0, '', 'air_china_unit', '', '', 'list_multi', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 8, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d6fad89f4f26d733291863c2dfbc5945', '27fc5f91274344afa7673a732b279939', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, 'admin', '2019-07-01 16:28:20', '2019-07-01 16:26:42', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d766ea5809e2ec9ff2cdbcb18f610ab3', '7ea60a25fa27470e9080d6a921aabbd1', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, NULL, NULL, '2019-04-17 00:22:21', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d7c3b107f004cbc99dfe1fe6c79894d8', '402860816bff91c0016bffa220a9000b', 'social_insurance_type', '参加社保类型', 'social_insurance_type', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 35, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d8234b56acea1a752271a6c911dd91a0', '7ea60a25fa27470e9080d6a921aabbd1', 'age', '年龄', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 7, NULL, NULL, '2019-04-17 00:22:21', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d86909d794b01ad7fbb5e61d28b6603b', '73162c3b8161413e8ecdca7eb288d0c9', 'sys_org_code', '所属部门', NULL, 0, 1, 'string', 64, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 6, 'admin', '2019-07-01 14:23:32', '2019-06-10 15:18:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d949d9157831c2fb7ba9f175081fe036', '402860816bff91c0016bff91cda80003', 'school', '学校', 'school', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 5, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d9d308feb95968dbb322c9bff0c18452', '32feeb502544416c9bf41329c10a88f4', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, 'admin', '2019-08-23 20:03:40', '2019-07-02 18:23:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d9dde2f59bb148c6b7e95256acad8972', 'e67d26b610dd414c884c4dbb24e71ce3', 'post_name', '岗位名字', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 7, 'admin', '2019-04-24 11:03:32', '2019-04-24 11:02:57', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('d9f9ae6986cb85019a3a4030f62f4d1a', '402860816bff91c0016bff91cfea0004', 'employee_id', '员工ID', 'employee_id', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', 'oa_employee_info', 'id', 2, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('db1fb3e12385cb967b63420cfe97cde6', '402860816bff91c0016bff91cda80003', 'employee_id', '员工ID', 'employee_id', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', 'oa_employee_info', 'id', 2, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('dc2bec862a4f0d600751c632d246f0ed', '4028839a6de2ebd3016de2ebd3870000', 'update_time', '更新日期', 'update_time', 0, 1, 'Date', 0, 0, NULL, NULL, NULL, NULL, 'date', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 5, NULL, NULL, '2019-10-19 15:29:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('dd3e1e13d7429807b83a00a090e060b7', '402860816bff91c0016bffa220a9000b', 'join_party_info', '入党（团）时间地点', 'join_party_info', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 26, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ddc302f84c75a5f056855c664b82202a', '402860816aa5921f016aa5921f480000', 'days', '请假天数', 'days', 0, 1, 'int', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 3, 'admin', '2019-05-11 15:31:54', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ddcc14a2105588982b4ae657f2893d81', '32feeb502544416c9bf41329c10a88f4', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, 'admin', '2019-08-23 20:03:40', '2019-07-02 18:23:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('dedb920a5e876e27eb144464209ebe1e', '27fc5f91274344afa7673a732b279939', 'sys_org_code', '所属部门', NULL, 0, 1, 'string', 64, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 6, 'admin', '2019-07-01 16:28:20', '2019-07-01 16:26:42', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('df489194c6008f3bd21b2c1c11fde337', 'fb19fb067cd841f9ae93d4eb3b883dc0', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', 2, NULL, NULL, '2019-03-23 11:39:48', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e0c5d6e483897d5c4e7894dc66dd1aff', '32feeb502544416c9bf41329c10a88f4', 'bpm_status', '流程状态', NULL, 0, 1, 'String', 2, 0, '', 'bpm_status', '', '', 'list', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 9, 'admin', '2019-08-23 20:03:40', '2019-07-02 18:23:58', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e24de426223dc0271a55eccc1d5457d0', '73162c3b8161413e8ecdca7eb288d0c9', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, 'admin', '2019-07-01 14:23:32', '2019-06-10 15:18:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e2d73ccda7f10f5a1ccce3c48b1e699e', '402860816bff91c0016bffa220a9000b', 'residence_street', '户口所在街道', 'residence_street', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 27, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e4914fdff68ac72486ada105e6e9fa36', 'e9faf717024b4aae95cff224ae9b6d97', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, 'admin', '2019-07-03 18:23:49', '2019-07-03 18:22:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e4a4c1d50b7b46678bc14fd5b90ee082', '73162c3b8161413e8ecdca7eb288d0c9', 'create_time', '创建日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, 'admin', '2019-07-01 14:23:32', '2019-06-10 15:18:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e50b4398731e06572c247993a0dcc38d', 'd35109c3632c4952a19ecc094943dd71', 'name', '用户名', NULL, 0, 1, 'string', 200, 0, '', '', '', '', 'text', '', 120, '*', '0', '', '', 0, 1, 1, 0, 'single', '', '', 5, 'admin', '2019-08-23 23:45:15', '2019-03-15 14:24:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e53f53766d1f7718b3ee5eabe105b969', '402860816bff91c0016bffa220a9000b', 'social_insurance_time', '五险一金日期', 'social_insurance_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 38, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e672d5974a06d5c37b3b4c94a6f29f96', '4028318169e81b970169e81b97650000', 'request_url', '请求路径', 'request_url', 0, 1, 'string', 255, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 9, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e7aade12ca722b59c1ec681d14247ff8', '402860816bff91c0016bff91d8830007', 'sys_org_code', '组织机构编码', 'sys_org_code', 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 14, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e845925368919482df6dac58e6ed708d', '402860816bff91c0016bff91d8830007', 'phone', '联系方式', 'phone', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 8, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e88d328af34dd8a17f51437c52b68a2d', '402860816bff91c0016bff91cfea0004', 'create_by', '创建人', 'create_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 9, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('e99cc08f4d88dd8f788399db8d448ee8', '62e29cdb81ac44d1a2d8ff89851b853d', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, NULL, NULL, '2019-05-11 14:01:14', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ea644c4c208739640933ba6e568045c1', 'e2faf977fdaf4b25a524f58c2441a51c', 'ruz_date', '入职时间', NULL, 0, 1, 'Date', 32, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 9, 'admin', '2019-06-10 17:27:00', '2019-04-24 17:12:11', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ea96d1c33f0f0d7245045e70a5793257', '402860816bff91c0016bffa220a9000b', 'current_postcode', '现居住地邮编', 'current_postcode', 0, 1, 'string', 10, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 31, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ebc41362681919cc680fcc58bf87fdcb', '1acb6f81a1d9439da6cc4e868617b565', 'price', '单价', NULL, 0, 1, 'double', 10, 2, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 12, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ec20e66d5eb9b8b7f58de9edc0f7630b', '1acb6f81a1d9439da6cc4e868617b565', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ec5e9cb5809b2f8ce1446df4a27693f0', '27fc5f91274344afa7673a732b279939', 'name', '用户名', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 7, 'admin', '2019-07-01 16:28:20', '2019-07-01 16:26:42', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ed16f23d08e7bcda11a1383fda68057e', '402860816bff91c0016bff91c7010001', 'employee_id', '员工ID', 'employee_id', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', 'oa_employee_info', 'id', 2, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('edda30c64e1dccee510d40b77a8ca094', 'fb7125a344a649b990c12949945cb6c1', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 0, 0, 0, 'single', '', '', 3, 'admin', '2019-03-26 19:24:11', '2019-03-26 19:01:52', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ee09e0e21fa350b9346b70292dcfca00', '79091e8277c744158530321513119c68', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, 'admin', '2019-05-11 15:29:47', '2019-05-11 15:27:17', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ee4ffe04a25fcf556e78183f1f521546', '402860816aa5921f016aa5921f480000', 'create_by', '创建人id', 'create_by', 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'group', '', '', 8, 'admin', '2019-05-11 15:31:54', '2019-05-11 14:26:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ee5803611f63643059b6375166d71567', '402860816bff91c0016bff91c7010001', 'create_time', '创建时间', 'create_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 8, 'admin', '2019-07-19 18:07:47', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ef81373c5fd7130d7e23859d90c9eb3e', '402860816bff91c0016bff91cda80003', 'from_time', '开始日期', 'from_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 3, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('efd1b955a75b5046e9857e00fe94ae2c', 'fbc35f067da94a70adb622ddba259352', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, NULL, NULL, '2019-07-03 19:44:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f015cc2ffdcc2c4be1e9b3622eb69b52', 'fbc35f067da94a70adb622ddba259352', 'sys_org_code', '所属部门', NULL, 0, 1, 'string', 64, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 6, NULL, NULL, '2019-07-03 19:44:23', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f06b2bb01ea1fae487b7e3c3eb521d5b', 'd3ae1c692b9640e0a091f8c46e17bb01', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, NULL, NULL, '2019-07-24 14:47:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f0967fd139b440f79f21248bf4e4a209', 'd3ae1c692b9640e0a091f8c46e17bb01', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 5, NULL, NULL, '2019-07-24 14:47:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f0a453930aa16ca32f2e3be860bfe542', '402860816bff91c0016bffa220a9000b', 'education', '学历', 'education', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 14, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f12753b4a3815697a72017a7436fe733', 'e2faf977fdaf4b25a524f58c2441a51c', 'update_time', '更新日期', NULL, 0, 1, 'Date', 20, 0, '', '', '', '', 'datetime', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 5, 'admin', '2019-06-10 17:27:00', '2019-04-24 17:12:11', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f1ab7d3b55ade57eeac6c55b32ce813a', '1acb6f81a1d9439da6cc4e868617b565', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f1c7fde21f26c7ed64a0ef1095900c52', '4028318169e81b970169e81b97650000', 'request_type', '请求类型', 'request_type', 0, 1, 'string', 10, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 11, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f4647a91a4ac5d6d32bb0692b800bffe', '402860816bff91c0016bff91c0cb0000', 'probation_post', '试用期职位', 'probation_post', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 8, 'admin', '2019-07-19 18:09:01', '2019-07-17 18:54:32', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f4b0bc7f3d6562e28d7c5e2d56510ecd', 'e5464aa8fa7b47c580e91593cf9b46dc', 'first_num', '首次数量', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 8, 'admin', '2019-04-24 17:09:49', '2019-04-24 14:31:31', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f558359b06aea79a992c102ce3563a4d', '4028318169e81b970169e81b97650000', 'username', '操作用户名称', 'username', 0, 1, 'string', 100, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 6, NULL, NULL, '2019-04-04 19:28:36', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f6afcb7d8ea81879593ff737b55ddcc0', '402860816bff91c0016bff91cda80003', 'id', 'id', 'id', 1, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'group', '', '', 1, 'admin', '2019-07-19 18:06:36', '2019-07-17 18:54:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f6f8aed87ec73994f6a12abbc079dbb1', '402860816bff91c0016bffa220a9000b', 'update_by', '更新人', 'update_by', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 41, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f7332af7586c83c87f7b9ea144a5292d', '62e29cdb81ac44d1a2d8ff89851b853d', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, NULL, NULL, '2019-05-11 14:01:14', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f76719783433487f4710232e2ae0e521', '402860816bff91c0016bff91cfea0004', 'id', 'id', 'id', 1, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'group', '', '', 1, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f7e7eb84ddc34d7e09d10af213ac6667', '402860816bff91c0016bff91d2810005', 'create_time', '创建时间', 'create_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 11, 'admin', '2019-07-19 18:05:55', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f81d7103c0c857e5c744cda2bc4c000a', '402860816bff91c0016bff91ca7e0002', 'cert_organizations', '发证机关', 'cert_organizations', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 6, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f82db8edef5babf741914b0fa221329b', '4028839a6de2ebd3016de2ebd3870000', 'bpm_status', '流程状态', 'bpm_status', 0, 1, 'string', 2, 0, NULL, NULL, NULL, NULL, 'text', NULL, 120, NULL, '0', NULL, NULL, 0, 1, 1, 0, 'group', NULL, NULL, 16, NULL, NULL, '2019-10-19 15:29:30', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f8a0fd20a1173270afdfed1129d5c669', '402860816bff91c0016bffa220a9000b', 'depart_id', '所在部门id', NULL, 0, 1, 'String', 32, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 44, 'admin', '2019-07-22 16:15:32', '2019-07-19 15:33:44', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f8cc6be747fec10802eb625ac529c16f', '402860816bff91c0016bff91cfea0004', 'create_time', '创建时间', 'create_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 10, 'admin', '2019-07-19 18:05:13', '2019-07-17 18:54:35', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f90bcb38fb89988bd40d1618aa75cea0', '758334cb1e7445e2822b60e807aec4a3', 'id', '主键', NULL, 1, 0, 'string', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 1, NULL, NULL, '2019-10-18 18:02:09', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f94db83e41c69f407d3c9a81c5892269', '402860816bff91c0016bffa220a9000b', 'first_job_time', '首次工作时间', 'first_job_time', 0, 1, 'Date', 0, 0, '', '', '', '', 'date', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 22, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('f95d2cbefd25444909c83aaf8c4f72fb', '402860816bff91c0016bff91ca7e0002', 'memo', '备注', 'memo', 0, 1, 'string', 255, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 7, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('fa3a12d7abf72b23afe425f8dbd57f86', '1acb6f81a1d9439da6cc4e868617b565', 'size_type', '尺码类型', NULL, 0, 1, 'String', 2, 0, '', 'air_china_size', '', '', 'list', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 9, 'admin', '2019-06-10 14:47:14', '2019-04-23 22:58:19', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('fa8f5a0ba673e0208934567462844eab', '402860816bff91c0016bff91ca7e0002', 'sys_org_code', '组织机构编码', 'sys_org_code', 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 13, 'admin', '2019-07-19 18:07:13', '2019-07-17 18:54:33', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('fafb32cf7e63bca93bbd70b0a0ea11fc', '758334cb1e7445e2822b60e807aec4a3', 'sys_org_code', '所属部门', NULL, 0, 1, 'string', 64, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 6, NULL, NULL, '2019-10-18 18:02:09', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('fc55d467102c2c782286f546d7820c3d', '73162c3b8161413e8ecdca7eb288d0c9', 'pid', '父物料', NULL, 0, 1, 'String', 36, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'single', '', '', 9, 'admin', '2019-07-01 14:23:32', '2019-06-10 15:18:34', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('fc76a3832d232829852cae6c66e44f67', '402860816bff91c0016bffa220a9000b', 'identity_no', '身份证号', 'identity_no', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 21, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('fcd519058d68fa4dab192335602b5d24', '402860816bff91c0016bffa220a9000b', 'real_name', '姓名', 'real_name', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 5, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('fd0586cae06685959415d9017b2bdf49', '758334cb1e7445e2822b60e807aec4a3', 'create_by', '创建人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 2, NULL, NULL, '2019-10-18 18:02:09', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('fde00160a5d664effaa4b5552e814e74', 'fb7125a344a649b990c12949945cb6c1', 'sex', '性别', NULL, 0, 1, 'string', 32, 0, '', '', '', '', 'text', '', 120, '', '0', '', '', 0, 1, 1, 0, 'single', '', '', 6, 'admin', '2019-03-26 19:24:11', '2019-03-26 19:01:52', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ff49b468e54e137032f7e4d976b83b5a', '402860816bff91c0016bffa220a9000b', 'politically_status', '政治面貌', 'politically_status', 0, 1, 'string', 20, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 8, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ff601f75d0e7ced226748eb8fba2c896', '402860816bff91c0016bff91d8830007', 'relation', '关系', 'relation', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 4, 'admin', '2019-07-19 18:04:41', '2019-07-17 18:54:37', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ffacafee9fa46eb297ca3252f95acef9', '402860816bff91c0016bffa220a9000b', 'school', '毕业学校', 'school', 0, 1, 'string', 100, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 1, 1, 0, 'group', '', '', 9, 'admin', '2019-07-22 16:15:32', '2019-07-17 19:12:24', 'admin');
INSERT INTO `onl_cgform_field` VALUES ('ffcbf379fffabbd13aa2c22ce565ec12', '79091e8277c744158530321513119c68', 'update_by', '更新人', NULL, 0, 1, 'string', 50, 0, '', '', '', '', 'text', '', 120, NULL, '0', '', '', 0, 0, 0, 0, 'single', '', '', 4, 'admin', '2019-05-11 15:29:47', '2019-05-11 15:27:17', 'admin');

-- ----------------------------
-- Table structure for onl_cgform_head
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgform_head`;
CREATE TABLE `onl_cgform_head`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
  `table_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表名',
  `table_type` int(11) NOT NULL COMMENT '表类型: 0单表、1主表、2附表',
  `table_version` int(11) NULL DEFAULT 1 COMMENT '表版本',
  `table_txt` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表说明',
  `is_checkbox` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否带checkbox',
  `is_db_synch` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N' COMMENT '同步数据库状态',
  `is_page` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否分页',
  `is_tree` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否是树',
  `id_sequence` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主键生成序列',
  `id_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主键类型',
  `query_mode` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '查询模式',
  `relation_type` int(11) NULL DEFAULT NULL COMMENT '映射关系 0一对多  1一对一',
  `sub_table_str` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子表',
  `tab_order_num` int(11) NULL DEFAULT NULL COMMENT '附表排序序号',
  `tree_parent_id_field` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '树形表单父id',
  `tree_id_field` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '树表主键字段',
  `tree_fieldname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '树开表单列字段',
  `form_category` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'bdfl_ptbd' COMMENT '表单分类',
  `form_template` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'PC表单模板',
  `form_template_mobile` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单模板样式(移动端)',
  `scroll` int(3) NULL DEFAULT 0 COMMENT '是否有横向滚动条',
  `copy_version` int(11) NULL DEFAULT NULL COMMENT '复制版本号',
  `copy_type` int(3) NULL DEFAULT 0 COMMENT '复制表类型1为复制表 0为原始表',
  `physic_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原始表ID',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `theme_template` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题模板',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_onlineform_table_name`(`table_name`) USING BTREE,
  INDEX `index_form_templdate`(`form_template`) USING BTREE,
  INDEX `index_templdate_mobile`(`form_template_mobile`) USING BTREE,
  INDEX `index_onlineform_table_version`(`table_version`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of onl_cgform_head
-- ----------------------------
INSERT INTO `onl_cgform_head` VALUES ('56870166aba54ebfacb20ba6c770bd73', 'test_order_main', 2, 5, '测试订单主表', 'N', 'Y', 'Y', 'N', NULL, 'UUID', 'single', NULL, 'test_order_product', NULL, NULL, NULL, NULL, 'bdfl_include', '2', NULL, 0, NULL, 0, NULL, 'admin', '2019-11-21 17:58:28', 'admin', '2019-04-20 11:38:39', 'normal');
INSERT INTO `onl_cgform_head` VALUES ('d35109c3632c4952a19ecc094943dd71', 'test_demo', 1, 14, '测试用户表', 'N', 'Y', 'Y', 'N', NULL, 'UUID', 'single', NULL, NULL, NULL, NULL, NULL, NULL, 'bdfl_include', '1', NULL, 0, NULL, 0, NULL, 'admin', '2019-03-22 21:51:35', 'admin', '2019-03-15 14:24:35', 'normal');
INSERT INTO `onl_cgform_head` VALUES ('deea5a8ec619460c9245ba85dbc59e80', 'test_order_product', 3, 7, '订单产品明细', 'N', 'Y', 'Y', 'N', NULL, 'UUID', 'single', 0, NULL, NULL, NULL, NULL, NULL, 'bdfl_include', '1', NULL, 0, NULL, 0, NULL, 'admin', '2019-04-20 11:42:53', 'admin', '2019-04-20 11:41:19', 'normal');

-- ----------------------------
-- Table structure for onl_cgform_index
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgform_index`;
CREATE TABLE `onl_cgform_index`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `cgform_head_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主表id',
  `index_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '索引名称',
  `index_field` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '索引栏位',
  `index_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '索引类型',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_table_id`(`cgform_head_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for onl_cgreport_head
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgreport_head`;
CREATE TABLE `onl_cgreport_head`  (
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
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of onl_cgreport_head
-- ----------------------------
INSERT INTO `onl_cgreport_head` VALUES ('6c7f59741c814347905a938f06ee003c', 'report_user', '统计在线用户', 'select * from sys_user', NULL, NULL, '1', NULL, NULL, '2019-11-22 16:34:31', 'admin', '2019-03-25 11:20:45', 'admin');
INSERT INTO `onl_cgreport_head` VALUES ('87b55a515d3441b6b98e48e5b35474a6', 'demo', 'Report Demo', 'select * from demo', NULL, NULL, '1', NULL, NULL, '2019-03-15 18:18:17', 'admin', '2019-03-12 11:25:16', 'admin');

-- ----------------------------
-- Table structure for onl_cgreport_item
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgreport_item`;
CREATE TABLE `onl_cgreport_item`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `cgrhead_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '报表ID',
  `field_name` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字段名字',
  `field_txt` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段文本',
  `field_width` int(3) NULL DEFAULT NULL,
  `field_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段类型',
  `search_mode` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '查询模式',
  `is_order` int(2) NULL DEFAULT 0 COMMENT '是否排序  0否,1是',
  `is_search` int(2) NULL DEFAULT 0 COMMENT '是否查询  0否,1是',
  `dict_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典CODE',
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
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of onl_cgreport_item
-- ----------------------------
INSERT INTO `onl_cgreport_item` VALUES ('0ba0dc69589a85a96be30f59451c81df', 'b32a3fdd008f4506b2bac4ac00f0bd4f', '222', '222', NULL, 'String', NULL, 0, 0, '', '', 1, 0, '', 'admin', '2019-05-31 14:37:25', 'admin', '2019-05-31 14:37:30');
INSERT INTO `onl_cgreport_item` VALUES ('1740bb02519db90c44cb2cba8b755136', '6c7f59741c814347905a938f06ee003c', 'realname', '用户名称', NULL, 'String', NULL, 0, 0, '', '', 1, 3, '', 'admin', '2019-11-22 16:34:31', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('1b181e6d2813bcb263adc39737f9df46', '87b55a515d3441b6b98e48e5b35474a6', 'name', '用户名', NULL, 'String', 'single', 0, 1, '', '', 1, 4, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');
INSERT INTO `onl_cgreport_item` VALUES ('1fb45af29af4e792bdc5a4a2c06a4d4d', '402880ec5d872157015d87f2dd940010', 'data_table', '表名', NULL, 'String', NULL, 0, 0, NULL, NULL, 1, 0, NULL, 'admin', '2019-03-20 13:24:21', 'admin', '2019-03-20 13:25:08');
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ec0003', '402880e64e1ef94d014e1efefc2a0001', 'id', 'id', 0, 'String', '', NULL, 0, '', '', 0, 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ed0004', '402880e64e1ef94d014e1efefc2a0001', 'accountname', 'accountname', 0, 'String', '', NULL, 0, '', '', 0, 1, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ed0005', '402880e64e1ef94d014e1efefc2a0001', 'accounttoken', 'accounttoken', 0, 'String', '', NULL, 0, '', '', 0, 2, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ed0006', '402880e64e1ef94d014e1efefc2a0001', 'accountnumber', 'accountnumber', 0, 'String', '', NULL, 0, '', '', 0, 3, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ed0007', '402880e64e1ef94d014e1efefc2a0001', 'accounttype', 'accounttype', 0, 'String', '', NULL, 0, '', '', 0, 4, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ed0008', '402880e64e1ef94d014e1efefc2a0001', 'accountemail', 'accountemail', 0, 'String', '', NULL, 0, '', '', 0, 5, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ed0009', '402880e64e1ef94d014e1efefc2a0001', 'accountdesc', 'accountdesc', 0, 'String', '', NULL, 0, '', '', 0, 6, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ee000b', '402880e64e1ef94d014e1efefc2a0001', 'accountappid', 'accountappid', 0, 'String', '', NULL, 0, '', '', 0, 8, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ee000c', '402880e64e1ef94d014e1efefc2a0001', 'accountappsecret', 'accountappsecret', 0, 'String', '', NULL, 0, '', '', 0, 9, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ee000d', '402880e64e1ef94d014e1efefc2a0001', 'ADDTOEKNTIME', 'ADDTOEKNTIME', 0, 'String', '', NULL, 0, '', '', 0, 10, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ee000e', '402880e64e1ef94d014e1efefc2a0001', 'USERNAME', 'USERNAME', 0, 'String', '', NULL, 0, '', '', 0, 11, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ee0010', '402880e64e1ef94d014e1efefc2a0001', 'jsapiticket', 'jsapiticket', 0, 'String', '', NULL, 0, '', '', 0, 13, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ee0011', '402880e64e1ef94d014e1efefc2a0001', 'jsapitickettime', 'jsapitickettime', 0, 'String', '', NULL, 0, '', '', 0, 14, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f167cf82600167cfa154ef0012', '402880e64e1ef94d014e1efefc2a0001', 'init_data_flag', 'init_data_flag', 0, 'String', '', NULL, 0, '', '', 0, 15, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f363aa9a380163aa9ebe490002', '402881f363aa9a380163aa9ebe480001', 'id', 'id', 0, 'String', '', NULL, 0, '', '', 0, 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f363aa9a380163aa9ebe490003', '402881f363aa9a380163aa9ebe480001', 'name', 'name', 0, 'String', 'single', NULL, 0, '', '', 0, 10, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f363aa9a380163aa9ebe490004', '402881f363aa9a380163aa9ebe480001', 'sex', 'sex', 0, 'String', 'single', NULL, 0, 'sex', '', 0, 11, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f363aa9a380163aa9ebe490005', '402881f363aa9a380163aa9ebe480001', 'age', 'age', 0, 'String', '', NULL, 0, '', '', 0, 12, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f363aa9a380163aa9ebe490006', '402881f363aa9a380163aa9ebe480001', 'address', 'address', 0, 'String', '', NULL, 0, '', '', 0, 13, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f363aa9a380163aa9ebe490007', '402881f363aa9a380163aa9ebe480001', 'phone', 'phone', 0, 'String', '', NULL, 0, '', '', 0, 14, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f363aa9a380163aa9ebe4a0008', '402881f363aa9a380163aa9ebe480001', 'memo', 'memo', 0, 'String', '', NULL, 0, '', '', 0, 15, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d2c0004', '402880e64eb9a22c014eb9a4d5890001', 'ID', 'ID', 0, 'String', '', NULL, 0, '', '', 0, 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d2c0005', '402880e64eb9a22c014eb9a4d5890001', 'activitiSync', 'activitiSync', 0, 'String', '', NULL, 0, '', '', 0, 1, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d2d0006', '402880e64eb9a22c014eb9a4d5890001', 'browser', 'browser', 0, 'String', '', NULL, 0, '', '', 0, 2, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d2d0007', '402880e64eb9a22c014eb9a4d5890001', 'password', 'password', 0, 'String', '', NULL, 0, '', '', 0, 3, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d2e0008', '402880e64eb9a22c014eb9a4d5890001', 'realname', 'realname', 0, 'String', '', NULL, 0, '', '', 0, 4, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d2e0009', '402880e64eb9a22c014eb9a4d5890001', 'signature', 'signature', 0, 'String', '', NULL, 0, '', '', 0, 5, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d2f000a', '402880e64eb9a22c014eb9a4d5890001', 'status', 'status', 0, 'String', '', NULL, 0, '', '', 0, 6, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d30000b', '402880e64eb9a22c014eb9a4d5890001', 'userkey', 'userkey', 0, 'String', '', NULL, 0, '', '', 0, 7, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d30000c', '402880e64eb9a22c014eb9a4d5890001', 'username', 'username', 0, 'String', '', NULL, 0, '', '', 0, 8, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d31000d', '402880e64eb9a22c014eb9a4d5890001', 'departid', 'departid', 0, 'String', '', NULL, 0, '', '', 0, 9, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d31000e', '402880e64eb9a22c014eb9a4d5890001', 'user_name_en', 'user_name_en', 0, 'String', '', NULL, 0, '', '', 0, 10, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f36402f3de016403035d32000f', '402880e64eb9a22c014eb9a4d5890001', 'delete_flag', 'delete_flag', 0, 'String', '', NULL, 0, '', '', 0, 11, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f3647e95be01647eb88c400003', '402880e74d76e784014d76f9e783001e', 'account', 'account', 0, 'String', 'single', NULL, 0, '', '', 0, 0, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('402881f3647e95be01647eb88c410004', '402880e74d76e784014d76f9e783001e', 'realname', 'realname', 0, 'String', '', NULL, 0, '', '', 0, 1, '', NULL, NULL, NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('61ef5b323134938fdd07ad5e3ea16cd3', '87b55a515d3441b6b98e48e5b35474a6', 'key_word', '关键词', NULL, 'String', 'single', 0, 1, '', '', 1, 5, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');
INSERT INTO `onl_cgreport_item` VALUES ('627768efd9ba2c41e905579048f21000', '6c7f59741c814347905a938f06ee003c', 'username', '用户账号', NULL, 'String', 'single', 0, 1, '', '', 1, 2, '', 'admin', '2019-11-22 16:34:31', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('8a2dfe672f3c0d391ace4a9f9bf564ff', '402880ec5d872157015d87f2dd940010', 'data_id', '数据ID', NULL, 'String', NULL, 0, 0, NULL, NULL, 1, 0, NULL, 'admin', '2019-03-20 13:24:21', 'admin', '2019-03-20 13:25:08');
INSERT INTO `onl_cgreport_item` VALUES ('8bb087a9aa2000bcae17a1b3f5768435', '6c7f59741c814347905a938f06ee003c', 'sex', '性别', NULL, 'Integer', 'single', 0, 1, 'sex', '', 1, 5, '', 'admin', '2019-11-22 16:34:31', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('90d4fa57d301801abb26a9b86b6b94c4', '6c7f59741c814347905a938f06ee003c', 'birthday', '生日', NULL, 'Date', 'single', 0, 0, '', '', 1, 4, '', 'admin', '2019-11-22 16:34:31', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('9a0a7375479b7657e16c6a228354b495', '402880ec5d872157015d87f2dd940010', 'data_version', '数据版本', NULL, 'String', NULL, 0, 0, NULL, NULL, 1, 0, NULL, 'admin', '2019-03-20 13:24:21', 'admin', '2019-03-20 13:25:08');
INSERT INTO `onl_cgreport_item` VALUES ('a4ac355f07a05218854e5f23e2930163', '6c7f59741c814347905a938f06ee003c', 'avatar', '头像', NULL, 'String', NULL, 0, 0, '', '', 0, 6, '', 'admin', '2019-11-22 16:34:31', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('ae4d621e391a1392779175cf5a65134c', '87b55a515d3441b6b98e48e5b35474a6', 'update_by', '修改人', NULL, 'String', NULL, 0, 0, '', '', 1, 7, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');
INSERT INTO `onl_cgreport_item` VALUES ('b27bea35b1264003c79d38cb86d6929e', '6c7f59741c814347905a938f06ee003c', 'id', 'id', NULL, 'String', NULL, 0, 0, '', '', 0, 1, '', 'admin', '2019-11-22 16:34:31', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('ce5168755a734ea09dd190e28bf8d9f4', '87b55a515d3441b6b98e48e5b35474a6', 'update_time', '修改时间', NULL, 'String', NULL, 0, 0, '', '', 1, 2, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');
INSERT INTO `onl_cgreport_item` VALUES ('d6e86b5ffd096ddcc445c0f320a45004', '6c7f59741c814347905a938f06ee003c', 'phone', '手机号', NULL, 'String', NULL, 0, 0, '', '', 1, 11, '', 'admin', '2019-11-22 16:34:31', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('df365cd357699eea96c29763d1dd7f9d', '6c7f59741c814347905a938f06ee003c', 'email', '邮箱', NULL, 'String', NULL, 0, 0, '', '', 1, 14, '', 'admin', '2019-11-22 16:34:31', NULL, NULL);
INSERT INTO `onl_cgreport_item` VALUES ('edf9932912b81ad01dd557d3d593a559', '87b55a515d3441b6b98e48e5b35474a6', 'age', '年龄', NULL, 'String', NULL, 0, 0, '', '', 1, 8, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');
INSERT INTO `onl_cgreport_item` VALUES ('f985883e509a6faaaf62ca07fd24a73c', '87b55a515d3441b6b98e48e5b35474a6', 'birthday', '生日', NULL, 'Date', 'single', 0, 1, '', '', 1, 1, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');
INSERT INTO `onl_cgreport_item` VALUES ('fce83e4258de3e2f114ab3116397670c', '87b55a515d3441b6b98e48e5b35474a6', 'punch_time', '发布时间', NULL, 'String', NULL, 0, 0, '', '', 1, 3, '', 'admin', '2019-03-20 19:26:39', 'admin', '2019-03-27 18:05:04');

-- ----------------------------
-- Table structure for onl_cgreport_param
-- ----------------------------
DROP TABLE IF EXISTS `onl_cgreport_param`;
CREATE TABLE `onl_cgreport_param`  (
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
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of onl_cgreport_param
-- ----------------------------
INSERT INTO `onl_cgreport_param` VALUES ('402881f36402f3de016403035d350010', '402880e64eb9a22c014eb9a4d5890001', 'usekey', 'usekey', '', 0, 'admin', '2018-06-15 18:35:09', NULL, NULL);

-- ----------------------------
-- Table structure for oss_file
-- ----------------------------
DROP TABLE IF EXISTS `oss_file`;
CREATE TABLE `oss_file`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件地址',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'Oss File' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `BLOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'InnoDB free: 504832 kB; (`SCHED_NAME` `TRIGGER_NAME` `TRIGGE' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CRON_EXPRESSION` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'InnoDB free: 504832 kB; (`SCHED_NAME` `TRIGGER_NAME` `TRIGGE' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ENTRY_ID` varchar(95) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `IS_DURABLE` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `LOCK_NAME` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
INSERT INTO `qrtz_locks` VALUES ('quartzScheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'InnoDB free: 504832 kB; (`SCHED_NAME` `TRIGGER_NAME` `TRIGGE' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `STR_PROP_1` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STR_PROP_2` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STR_PROP_3` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `INT_PROP_1` int(11) NULL DEFAULT NULL,
  `INT_PROP_2` int(11) NULL DEFAULT NULL,
  `LONG_PROP_1` bigint(20) NULL DEFAULT NULL,
  `LONG_PROP_2` bigint(20) NULL DEFAULT NULL,
  `DEC_PROP_1` decimal(13, 4) NULL DEFAULT NULL,
  `DEC_PROP_2` decimal(13, 4) NULL DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'InnoDB free: 504832 kB; (`SCHED_NAME` `TRIGGER_NAME` `TRIGGE' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
  `PRIORITY` int(11) NULL DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) NULL DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) NULL DEFAULT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `SCHED_NAME`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'InnoDB free: 504832 kB; (`SCHED_NAME` `JOB_NAME` `JOB_GROUP`' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_announcement
-- ----------------------------
DROP TABLE IF EXISTS `sys_announcement`;
CREATE TABLE `sys_announcement`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `titile` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `msg_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `sender` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发布人',
  `priority` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优先级（L低，M中，H高）',
  `msg_category` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '2' COMMENT '消息类型1:通知公告2:系统消息',
  `msg_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通告对象类型（USER:指定用户，ALL:全体用户）',
  `send_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发布状态（0未发布，1已发布，2已撤销）',
  `send_time` datetime(0) NULL DEFAULT NULL COMMENT '发布时间',
  `cancel_time` datetime(0) NULL DEFAULT NULL COMMENT '撤销时间',
  `del_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除状态（0，正常，1已删除）',
  `bus_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型(email:邮件 bpm:流程)',
  `bus_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务id',
  `open_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '打开方式(组件：component 路由：url)',
  `open_page` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件/路由 地址',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `user_ids` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '指定用户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统通告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_announcement
-- ----------------------------
INSERT INTO `sys_announcement` VALUES ('1b714f8ebc3cc33f8b4f906103b6a18d', '5467567', NULL, NULL, NULL, 'admin', NULL, '2', NULL, '1', '2019-03-30 12:40:38', NULL, '0', NULL, NULL, NULL, NULL, 'admin', '2019-02-26 17:23:26', 'admin', '2019-02-26 17:35:10', NULL);
INSERT INTO `sys_announcement` VALUES ('3d11237ccdf62450d20bb8abdb331178', '111222', NULL, NULL, NULL, NULL, NULL, '2', NULL, '0', NULL, NULL, '1', NULL, NULL, NULL, NULL, 'admin', '2019-03-29 17:19:47', 'admin', '2019-03-29 17:19:50', NULL);
INSERT INTO `sys_announcement` VALUES ('7ef04e95f8de030b1d5f7a9144090dc6', '111', NULL, '2019-02-06 17:28:10', '2019-03-08 17:28:11', NULL, NULL, '2', NULL, '0', NULL, NULL, '1', NULL, NULL, NULL, NULL, 'admin', '2019-02-26 17:28:17', 'admin', '2019-03-26 19:59:49', NULL);
INSERT INTO `sys_announcement` VALUES ('93a9060a1c20e4bf98b3f768a02c2ff9', '111', '111', '2019-02-06 17:20:17', '2019-02-21 17:20:20', 'admin', 'M', '2', 'ALL', '1', '2019-02-26 17:24:29', NULL, '0', NULL, NULL, NULL, NULL, 'admin', '2019-02-26 17:16:26', 'admin', '2019-02-26 17:19:35', NULL);
INSERT INTO `sys_announcement` VALUES ('de1dc57f31037079e1e55c8347fe6ef7', '222', '2222', '2019-02-06 17:28:26', '2019-02-23 17:28:28', 'admin', 'M', '2', 'ALL', '1', '2019-03-29 17:19:56', NULL, '1', NULL, NULL, NULL, NULL, 'admin', '2019-02-26 17:28:36', 'admin', '2019-02-26 17:28:40', NULL);
INSERT INTO `sys_announcement` VALUES ('e52f3eb6215f139cb2224c52517af3bd', '334', '334', NULL, NULL, NULL, NULL, '2', NULL, '0', NULL, NULL, '1', NULL, NULL, NULL, NULL, 'admin', '2019-03-30 12:40:28', 'admin', '2019-03-30 12:40:32', NULL);

-- ----------------------------
-- Table structure for sys_announcement_send
-- ----------------------------
DROP TABLE IF EXISTS `sys_announcement_send`;
CREATE TABLE `sys_announcement_send`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `annt_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通告ID',
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `read_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阅读状态（0未读，1已读）',
  `read_time` datetime(0) NULL DEFAULT NULL COMMENT '阅读时间',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户通告阅读标记表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_announcement_send
-- ----------------------------
INSERT INTO `sys_announcement_send` VALUES ('646c0c405ec643d4dc4160db2446f8ff', '93a9060a1c20e4bf98b3f768a02c2ff9', 'e9ca23d68d884d4ebb19d07889727dae', '1', '2019-11-21 16:30:01', 'admin', '2019-05-17 11:50:56', 'admin', '2019-11-21 16:30:01');
INSERT INTO `sys_announcement_send` VALUES ('1197434450981543938', '93a9060a1c20e4bf98b3f768a02c2ff9', 'a75d45a015c44384a04449ee80dc3503', '0', NULL, 'jeecg', '2019-11-21 16:39:55', NULL, NULL);

-- ----------------------------
-- Table structure for sys_category
-- ----------------------------
DROP TABLE IF EXISTS `sys_category`;
CREATE TABLE `sys_category`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pid` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父级节点',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型编码',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门',
  `has_child` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否有子节点',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_category
-- ----------------------------
INSERT INTO `sys_category` VALUES ('1183693424827564034', '0', '物料树', 'B02', 'admin', '2019-10-14 18:37:59', 'admin', '2019-10-14 18:38:15', 'A01', '1');
INSERT INTO `sys_category` VALUES ('1183693491043041282', '1183693424827564034', '上衣', 'B02A01', 'admin', '2019-10-14 18:38:15', 'admin', '2019-10-14 18:38:43', 'A01', '1');
INSERT INTO `sys_category` VALUES ('1183693534173069314', '1183693424827564034', '裤子', 'B02A02', 'admin', '2019-10-14 18:38:25', NULL, NULL, 'A01', NULL);
INSERT INTO `sys_category` VALUES ('1183693610534567937', '1183693491043041282', '秋衣', 'B02A01A01', 'admin', '2019-10-14 18:38:43', NULL, NULL, 'A01', NULL);
INSERT INTO `sys_category` VALUES ('1183693700254924802', '1183693491043041282', '兵装', 'B02A01A02', 'admin', '2019-10-14 18:39:05', NULL, NULL, 'A01', NULL);
INSERT INTO `sys_category` VALUES ('1183693773974011906', '1183693491043041282', '女装', 'B02A01A03', 'admin', '2019-10-14 18:39:22', NULL, NULL, 'A01', NULL);
INSERT INTO `sys_category` VALUES ('1185039122143719425', '0', '电脑产品', 'A01', 'admin', '2019-10-18 11:45:18', 'admin', '2019-10-18 11:45:31', 'A01', '1');
INSERT INTO `sys_category` VALUES ('1185039176799694850', '1185039122143719425', 'thinkpad', 'A01A01', 'admin', '2019-10-18 11:45:31', NULL, NULL, 'A01', NULL);
INSERT INTO `sys_category` VALUES ('1185039255115739138', '1185039122143719425', 'mackbook', 'A01A02', 'admin', '2019-10-18 11:45:50', NULL, NULL, 'A01', NULL);
INSERT INTO `sys_category` VALUES ('1185039299051073537', '1185039122143719425', '华为电脑', 'A01A03', 'admin', '2019-10-18 11:46:01', NULL, NULL, 'A01', NULL);
INSERT INTO `sys_category` VALUES ('22a50b413c5e1ef661fb8aea9469cf52', 'e9ded10fd33e5753face506f4f1564b5', 'MacBook', 'B01-2-1', 'admin', '2019-06-10 15:43:13', NULL, NULL, 'A01', NULL);
INSERT INTO `sys_category` VALUES ('5c8f68845e57f68ab93a2c8d82d26ae1', '0', '笔记本', 'B01', 'admin', '2019-06-10 15:34:11', 'admin', '2019-06-10 15:34:24', 'A01', '1');
INSERT INTO `sys_category` VALUES ('937fd2e9aa13b8bab1da1ca36d3fd344', 'e9ded10fd33e5753face506f4f1564b5', '台式机', 'B02-2-2', 'admin', '2019-06-10 15:43:32', 'admin', '2019-08-21 12:01:59', 'A01', NULL);
INSERT INTO `sys_category` VALUES ('e9ded10fd33e5753face506f4f1564b5', '5c8f68845e57f68ab93a2c8d82d26ae1', '苹果电脑', 'B01-2', 'admin', '2019-06-10 15:41:14', 'admin', '2019-06-10 15:43:13', 'A01', '1');
INSERT INTO `sys_category` VALUES ('f39a06bf9f390ba4a53d11bc4e0018d7', '5c8f68845e57f68ab93a2c8d82d26ae1', '华为', 'B01-1', 'admin', '2019-06-10 15:34:24', 'admin', '2019-08-21 12:01:56', 'A01', NULL);

-- ----------------------------
-- Table structure for sys_check_rule
-- ----------------------------
DROP TABLE IF EXISTS `sys_check_rule`;
CREATE TABLE `sys_check_rule`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `rule_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则名称',
  `rule_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则Code',
  `rule_json` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则JSON',
  `rule_description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则描述',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_sys_check_rule_code`(`rule_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_check_rule
-- ----------------------------
INSERT INTO `sys_check_rule` VALUES ('1224980593992388610', '通用编码规则', 'common', '[{\"digits\":\"1\",\"pattern\":\"^[a-z|A-Z]$\",\"message\":\"第一位只能是字母\"},{\"digits\":\"*\",\"pattern\":\"^[0-9|a-z|A-Z|_]{0,}$\",\"message\":\"只能填写数字、大小写字母、下划线\"},{\"digits\":\"*\",\"pattern\":\"^.{3,}$\",\"message\":\"最少输入3位数\"},{\"digits\":\"*\",\"pattern\":\"^.{3,12}$\",\"message\":\"最多输入12位数\"}]', '规则：1、首位只能是字母；2、只能填写数字、大小写字母、下划线；3、最少3位数，最多12位数。', 'admin', '2020-02-07 11:25:48', 'admin', '2020-02-05 16:58:27');
INSERT INTO `sys_check_rule` VALUES ('1225001845524004866', '负责的功能测试', 'test', '[{\"digits\":\"*\",\"pattern\":\"^.{3,12}$\",\"message\":\"只能输入3-12位字符\"},{\"digits\":\"3\",\"pattern\":\"^\\\\d{3}$\",\"message\":\"前3位必须是数字\"},{\"digits\":\"*\",\"pattern\":\"^[^pP]*$\",\"message\":\"不能输入P\"},{\"digits\":\"4\",\"pattern\":\"^@{4}$\",\"message\":\"第4-7位必须都为 @\"},{\"digits\":\"2\",\"pattern\":\"^#=$\",\"message\":\"第8-9位必须是 #=\"},{\"digits\":\"1\",\"pattern\":\"^O$\",\"message\":\"第10位必须为大写的O\"},{\"digits\":\"*\",\"pattern\":\"^.*。$\",\"message\":\"必须以。结尾\"}]', '包含长度校验、特殊字符校验等', 'admin', '2020-02-07 11:57:31', 'admin', '2020-02-05 18:22:54');

-- ----------------------------
-- Table structure for sys_data_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_data_log`;
CREATE TABLE `sys_data_log`  (
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
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_data_log
-- ----------------------------
INSERT INTO `sys_data_log` VALUES ('402880f05ab0d198015ab12274bf0006', 'admin', '2017-03-09 11:35:09', NULL, NULL, 'jeecg_demo', '4028ef81550c1a7901550c1cd6e70001', '{\"mobilePhone\":\"\",\"officePhone\":\"\",\"email\":\"\",\"createDate\":\"Jun 23, 2016 12:00:00 PM\",\"sex\":\"1\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"9001\",\"status\":\"1\",\"content\":\"111\",\"id\":\"4028ef81550c1a7901550c1cd6e70001\"}', 3);
INSERT INTO `sys_data_log` VALUES ('402880f05ab6d12b015ab700bead0009', 'admin', '2017-03-10 14:56:03', NULL, NULL, 'jeecg_demo', '402880f05ab6d12b015ab700be8d0008', '{\"mobilePhone\":\"\",\"officePhone\":\"\",\"email\":\"\",\"createDate\":\"Mar 10, 2017 2:56:03 PM\",\"sex\":\"0\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"111\",\"status\":\"0\",\"id\":\"402880f05ab6d12b015ab700be8d0008\"}', 1);
INSERT INTO `sys_data_log` VALUES ('402880f05ab6d12b015ab705a23f000d', 'admin', '2017-03-10 15:01:24', NULL, NULL, 'jeecg_demo', '402880f05ab6d12b015ab705a233000c', '{\"mobilePhone\":\"\",\"officePhone\":\"11\",\"email\":\"\",\"createDate\":\"Mar 10, 2017 3:01:24 PM\",\"sex\":\"0\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"11\",\"status\":\"0\",\"id\":\"402880f05ab6d12b015ab705a233000c\"}', 1);
INSERT INTO `sys_data_log` VALUES ('402880f05ab6d12b015ab712a6420013', 'admin', '2017-03-10 15:15:37', NULL, NULL, 'jeecg_demo', '402880f05ab6d12b015ab712a6360012', '{\"mobilePhone\":\"\",\"officePhone\":\"\",\"email\":\"\",\"createDate\":\"Mar 10, 2017 3:15:37 PM\",\"sex\":\"0\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"小王\",\"status\":\"0\",\"id\":\"402880f05ab6d12b015ab712a6360012\"}', 1);
INSERT INTO `sys_data_log` VALUES ('402880f05ab6d12b015ab712d0510015', 'admin', '2017-03-10 15:15:47', NULL, NULL, 'jeecg_demo', '402880f05ab6d12b015ab712a6360012', '{\"mobilePhone\":\"18611788525\",\"officePhone\":\"\",\"email\":\"\",\"createDate\":\"Mar 10, 2017 3:15:37 AM\",\"sex\":\"0\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"小王\",\"status\":\"0\",\"id\":\"402880f05ab6d12b015ab712a6360012\"}', 2);
INSERT INTO `sys_data_log` VALUES ('402880f05ab6d12b015ab71308240018', 'admin', '2017-03-10 15:16:02', NULL, NULL, 'jeecg_demo', '8a8ab0b246dc81120146dc81860f016f', '{\"mobilePhone\":\"13111111111\",\"officePhone\":\"66666666\",\"email\":\"demo@jeecg.com\",\"age\":12,\"salary\":10.00,\"birthday\":\"Feb 14, 2014 12:00:00 AM\",\"sex\":\"1\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"小明\",\"status\":\"\",\"content\":\"\",\"id\":\"8a8ab0b246dc81120146dc81860f016f\"}', 1);
INSERT INTO `sys_data_log` VALUES ('402880f05ab6d12b015ab72806c3001b', 'admin', '2017-03-10 15:38:58', NULL, NULL, 'jeecg_demo', '8a8ab0b246dc81120146dc81860f016f', '{\"mobilePhone\":\"18611788888\",\"officePhone\":\"66666666\",\"email\":\"demo@jeecg.com\",\"age\":12,\"salary\":10.00,\"birthday\":\"Feb 14, 2014 12:00:00 AM\",\"sex\":\"1\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"小明\",\"status\":\"\",\"content\":\"\",\"id\":\"8a8ab0b246dc81120146dc81860f016f\"}', 2);
INSERT INTO `sys_data_log` VALUES ('4028ef815318148a0153181567690001', 'admin', '2016-02-25 18:59:29', NULL, NULL, 'jeecg_demo', '4028ef815318148a0153181566270000', '{\"mobilePhone\":\"13423423423\",\"officePhone\":\"1\",\"email\":\"\",\"age\":1,\"salary\":1,\"birthday\":\"Feb 25, 2016 12:00:00 AM\",\"createDate\":\"Feb 25, 2016 6:59:24 PM\",\"depId\":\"402880e447e9a9570147e9b6a3be0005\",\"userName\":\"1\",\"status\":\"0\",\"id\":\"4028ef815318148a0153181566270000\"}', 1);
INSERT INTO `sys_data_log` VALUES ('4028ef815318148a01531815ec5c0003', 'admin', '2016-02-25 19:00:03', NULL, NULL, 'jeecg_demo', '4028ef815318148a0153181566270000', '{\"mobilePhone\":\"13426498659\",\"officePhone\":\"1\",\"email\":\"\",\"age\":1,\"salary\":1.00,\"birthday\":\"Feb 25, 2016 12:00:00 AM\",\"createDate\":\"Feb 25, 2016 6:59:24 AM\",\"depId\":\"402880e447e9a9570147e9b6a3be0005\",\"userName\":\"1\",\"status\":\"0\",\"id\":\"4028ef815318148a0153181566270000\"}', 2);
INSERT INTO `sys_data_log` VALUES ('4028ef8153c028db0153c0502e6b0003', 'admin', '2016-03-29 10:59:53', NULL, NULL, 'jeecg_demo', '4028ef8153c028db0153c0502d420002', '{\"mobilePhone\":\"18455477548\",\"officePhone\":\"123\",\"email\":\"\",\"createDate\":\"Mar 29, 2016 10:59:53 AM\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"123\",\"status\":\"0\",\"id\":\"4028ef8153c028db0153c0502d420002\"}', 1);
INSERT INTO `sys_data_log` VALUES ('4028ef8153c028db0153c0509aa40006', 'admin', '2016-03-29 11:00:21', NULL, NULL, 'jeecg_demo', '4028ef8153c028db0153c0509a3e0005', '{\"mobilePhone\":\"13565486458\",\"officePhone\":\"\",\"email\":\"\",\"createDate\":\"Mar 29, 2016 11:00:21 AM\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"22\",\"status\":\"0\",\"id\":\"4028ef8153c028db0153c0509a3e0005\"}', 1);
INSERT INTO `sys_data_log` VALUES ('4028ef8153c028db0153c051c4a70008', 'admin', '2016-03-29 11:01:37', NULL, NULL, 'jeecg_demo', '4028ef8153c028db0153c0509a3e0005', '{\"mobilePhone\":\"13565486458\",\"officePhone\":\"\",\"email\":\"\",\"createDate\":\"Mar 29, 2016 11:00:21 AM\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"22\",\"status\":\"0\",\"id\":\"4028ef8153c028db0153c0509a3e0005\"}', 2);
INSERT INTO `sys_data_log` VALUES ('4028ef8153c028db0153c051d4b5000a', 'admin', '2016-03-29 11:01:41', NULL, NULL, 'jeecg_demo', '4028ef8153c028db0153c0502d420002', '{\"mobilePhone\":\"13565486458\",\"officePhone\":\"123\",\"email\":\"\",\"createDate\":\"Mar 29, 2016 10:59:53 AM\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"123\",\"status\":\"0\",\"id\":\"4028ef8153c028db0153c0502d420002\"}', 2);
INSERT INTO `sys_data_log` VALUES ('4028ef8153c028db0153c07033d8000d', 'admin', '2016-03-29 11:34:52', NULL, NULL, 'jeecg_demo', '4028ef8153c028db0153c0502d420002', '{\"mobilePhone\":\"13565486458\",\"officePhone\":\"123\",\"email\":\"\",\"age\":23,\"createDate\":\"Mar 29, 2016 10:59:53 AM\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"123\",\"status\":\"0\",\"id\":\"4028ef8153c028db0153c0502d420002\"}', 3);
INSERT INTO `sys_data_log` VALUES ('4028ef8153c028db0153c070492e000f', 'admin', '2016-03-29 11:34:57', NULL, NULL, 'jeecg_demo', '4028ef8153c028db0153c0509a3e0005', '{\"mobilePhone\":\"13565486458\",\"officePhone\":\"\",\"email\":\"\",\"age\":22,\"createDate\":\"Mar 29, 2016 11:00:21 AM\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"22\",\"status\":\"0\",\"id\":\"4028ef8153c028db0153c0509a3e0005\"}', 3);
INSERT INTO `sys_data_log` VALUES ('4028ef81550c1a7901550c1cd7850002', 'admin', '2016-06-01 21:17:44', NULL, NULL, 'jeecg_demo', '4028ef81550c1a7901550c1cd6e70001', '{\"mobilePhone\":\"\",\"officePhone\":\"\",\"email\":\"\",\"createDate\":\"Jun 1, 2016 9:17:44 PM\",\"sex\":\"1\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"121221\",\"status\":\"0\",\"id\":\"4028ef81550c1a7901550c1cd6e70001\"}', 1);
INSERT INTO `sys_data_log` VALUES ('4028ef81568c31ec01568c3307080004', 'admin', '2016-08-15 11:16:09', NULL, NULL, 'jeecg_demo', '4028ef81550c1a7901550c1cd6e70001', '{\"mobilePhone\":\"\",\"officePhone\":\"\",\"email\":\"\",\"createDate\":\"Jun 23, 2016 12:00:00 PM\",\"sex\":\"1\",\"depId\":\"402880e447e99cf10147e9a03b320003\",\"userName\":\"9001\",\"status\":\"1\",\"content\":\"111\",\"id\":\"4028ef81550c1a7901550c1cd6e70001\"}', 2);

-- ----------------------------
-- Table structure for sys_data_source
-- ----------------------------
DROP TABLE IF EXISTS `sys_data_source`;
CREATE TABLE `sys_data_source`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据源编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据源名称',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `db_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据库类型',
  `db_driver` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '驱动类',
  `db_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据源地址',
  `db_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据库名称',
  `db_username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `db_password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属部门',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `sys_data_source_code_uni`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_data_source
-- ----------------------------
INSERT INTO `sys_data_source` VALUES ('1209779538310004737', NULL, 'MySQL5.7', '本地数据库MySQL5.7', '1', 'com.mysql.jdbc.Driver', 'jdbc:mysql://127.0.0.1:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false', 'jeecg-boot', 'root', 'root', 'admin', '2019-12-25 18:14:53', NULL, NULL, 'A01');

-- ----------------------------
-- Table structure for sys_depart
-- ----------------------------
DROP TABLE IF EXISTS `sys_depart`;
CREATE TABLE `sys_depart`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
  `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父机构ID',
  `depart_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构/部门名称',
  `depart_name_en` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '英文名',
  `depart_name_abbr` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缩写',
  `depart_order` int(11) NULL DEFAULT 0 COMMENT '排序',
  `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '描述',
  `org_category` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '机构类别 1组织机构，2岗位',
  `org_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构类型 1一级部门 2子部门',
  `org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构编码',
  `mobile` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `fax` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '传真',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `memo` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态（1启用，0不启用）',
  `del_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除状态（0，正常，1已删除）',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_depart_parent_id`(`parent_id`) USING BTREE,
  INDEX `index_depart_depart_order`(`depart_order`) USING BTREE,
  INDEX `index_depart_org_code`(`org_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '组织机构表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_depart
-- ----------------------------
INSERT INTO `sys_depart` VALUES ('4f1765520d6346f9bd9c79e2479e5b12', 'c6d7cb4deeac411cb3384b1b31278596', '市场部', NULL, NULL, 0, NULL, '1', '2', 'A01A03', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2019-02-20 17:15:34', 'admin', '2019-02-26 16:36:18');
INSERT INTO `sys_depart` VALUES ('5159cde220114246b045e574adceafe9', '6d35e179cd814e3299bd588ea7daed3f', '研发部', NULL, NULL, 0, NULL, '1', '2', 'A02A02', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2019-02-26 16:44:38', 'admin', '2019-03-07 09:36:53');
INSERT INTO `sys_depart` VALUES ('57197590443c44f083d42ae24ef26a2c', 'c6d7cb4deeac411cb3384b1b31278596', '研发部', NULL, NULL, 0, NULL, '1', '2', 'A01A05', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2019-02-21 16:14:41', 'admin', '2019-03-27 19:05:49');
INSERT INTO `sys_depart` VALUES ('67fc001af12a4f9b8458005d3f19934a', 'c6d7cb4deeac411cb3384b1b31278596', '财务部', NULL, NULL, 0, NULL, '1', '2', 'A01A04', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2019-02-21 16:14:35', 'admin', '2019-02-25 12:49:41');
INSERT INTO `sys_depart` VALUES ('6d35e179cd814e3299bd588ea7daed3f', '', '卓尔互动公司', NULL, NULL, 0, NULL, '1', '1', 'A02', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2019-02-26 16:36:39', 'admin', '2019-03-22 16:47:25');
INSERT INTO `sys_depart` VALUES ('743ba9dbdc114af8953a11022ef3096a', 'f28c6f53abd841ac87ead43afc483433', '财务部', NULL, NULL, 0, NULL, '1', '2', 'A03A01', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2019-03-22 16:45:43', NULL, NULL);
INSERT INTO `sys_depart` VALUES ('a7d7e77e06c84325a40932163adcdaa6', '6d35e179cd814e3299bd588ea7daed3f', '财务部', NULL, NULL, 0, NULL, '1', '2', 'A02A01', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2019-02-26 16:36:47', 'admin', '2019-02-26 16:37:25');
INSERT INTO `sys_depart` VALUES ('c6d7cb4deeac411cb3384b1b31278596', '', '北京国炬公司', NULL, NULL, 0, NULL, '1', '1', 'A01', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2019-02-11 14:21:51', 'admin', '2019-03-22 16:47:19');

-- ----------------------------
-- Table structure for sys_depart_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_depart_permission`;
CREATE TABLE `sys_depart_permission`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `depart_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门id',
  `permission_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限id',
  `data_rule_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据规则id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_depart_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_depart_role`;
CREATE TABLE `sys_depart_role`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `depart_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门id',
  `role_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门角色名称',
  `role_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门角色编码',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_depart_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_depart_role_permission`;
CREATE TABLE `sys_depart_role_permission`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `depart_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门id',
  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id',
  `permission_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限id',
  `data_rule_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_group_role_per_id`(`role_id`, `permission_id`) USING BTREE,
  INDEX `index_group_role_id`(`role_id`) USING BTREE,
  INDEX `index_group_per_id`(`permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门角色权限表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sys_depart_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_depart_role_user`;
CREATE TABLE `sys_depart_role_user`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `drole_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门角色用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `dict_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典名称',
  `dict_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典编码',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `del_flag` int(1) NULL DEFAULT NULL COMMENT '删除状态',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `type` int(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '字典类型0为string,1为number',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `indextable_dict_code`(`dict_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('0b5d19e1fce4b2e6647e6b4a17760c14', '通告类型', 'msg_category', '消息类型1:通知公告2:系统消息', 0, 'admin', '2019-04-22 18:01:35', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('1174509082208395266', '职务职级', 'position_rank', '职务表职级字典', 0, 'admin', '2019-09-19 10:22:41', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('1174511106530525185', '机构类型', 'org_category', '机构类型 1组织机构，2岗位', 0, 'admin', '2019-09-19 10:30:43', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('1178295274528845826', '表单权限策略', 'form_perms_type', '', 0, 'admin', '2019-09-29 21:07:39', 'admin', '2019-09-29 21:08:26', NULL);
INSERT INTO `sys_dict` VALUES ('1199517671259906049', '紧急程度', 'urgent_level', '日程计划紧急程度', 0, 'admin', '2019-11-27 10:37:53', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('1199518099888414722', '日程计划类型', 'eoa_plan_type', '', 0, 'admin', '2019-11-27 10:39:36', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('1199520177767587841', '分类栏目类型', 'eoa_cms_menu_type', '', 0, 'admin', '2019-11-27 10:47:51', 'admin', '2019-11-27 10:49:35', 0);
INSERT INTO `sys_dict` VALUES ('1199525215290306561', '日程计划状态', 'eoa_plan_status', '', 0, 'admin', '2019-11-27 11:07:52', 'admin', '2019-11-27 11:10:11', 0);
INSERT INTO `sys_dict` VALUES ('1209733563293962241', '数据库类型', 'database_type', '', 0, 'admin', '2019-12-25 15:12:12', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('236e8a4baff0db8c62c00dd95632834f', '同步工作流引擎', 'activiti_sync', '同步工作流引擎', 0, 'admin', '2019-05-15 15:27:33', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('2e02df51611a4b9632828ab7e5338f00', '权限策略', 'perms_type', '权限策略', 0, 'admin', '2019-04-26 18:26:55', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('2f0320997ade5dd147c90130f7218c3e', '推送类别', 'msg_type', '', 0, 'admin', '2019-03-17 21:21:32', 'admin', '2019-03-26 19:57:45', 0);
INSERT INTO `sys_dict` VALUES ('3486f32803bb953e7155dab3513dc68b', '删除状态', 'del_flag', NULL, 0, 'admin', '2019-01-18 21:46:26', 'admin', '2019-03-30 11:17:11', 0);
INSERT INTO `sys_dict` VALUES ('3d9a351be3436fbefb1307d4cfb49bf2', '性别', 'sex', NULL, 0, NULL, '2019-01-04 14:56:32', 'admin', '2019-03-30 11:28:27', 1);
INSERT INTO `sys_dict` VALUES ('404a04a15f371566c658ee9ef9fc392a', 'cehis2', '22', NULL, 1, 'admin', '2019-01-30 11:17:21', 'admin', '2019-03-30 11:18:12', 0);
INSERT INTO `sys_dict` VALUES ('4274efc2292239b6f000b153f50823ff', '全局权限策略', 'global_perms_type', '全局权限策略', 0, 'admin', '2019-05-10 17:54:05', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('4c03fca6bf1f0299c381213961566349', 'Online图表展示模板', 'online_graph_display_template', 'Online图表展示模板', 0, 'admin', '2019-04-12 17:28:50', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('4c753b5293304e7a445fd2741b46529d', '字典状态', 'dict_item_status', NULL, 0, 'admin', '2020-06-18 23:18:42', 'admin', '2019-03-30 19:33:52', 1);
INSERT INTO `sys_dict` VALUES ('4d7fec1a7799a436d26d02325eff295e', '优先级', 'priority', '优先级', 0, 'admin', '2019-03-16 17:03:34', 'admin', '2019-04-16 17:39:23', 0);
INSERT INTO `sys_dict` VALUES ('4e4602b3e3686f0911384e188dc7efb4', '条件规则', 'rule_conditions', '', 0, 'admin', '2019-04-01 10:15:03', 'admin', '2019-04-01 10:30:47', 0);
INSERT INTO `sys_dict` VALUES ('4f69be5f507accea8d5df5f11346181a', '发送消息类型', 'msgType', NULL, 0, 'admin', '2019-04-11 14:27:09', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('68168534ff5065a152bfab275c2136f8', '有效无效状态', 'valid_status', '有效无效状态', 0, 'admin', '2020-09-26 19:21:14', 'admin', '2019-04-26 19:21:23', 0);
INSERT INTO `sys_dict` VALUES ('6b78e3f59faec1a4750acff08030a79b', '用户类型', 'user_type', NULL, 1, NULL, '2019-01-04 14:59:01', 'admin', '2019-03-18 23:28:18', 0);
INSERT INTO `sys_dict` VALUES ('72cce0989df68887546746d8f09811aa', 'Online表单类型', 'cgform_table_type', '', 0, 'admin', '2019-01-27 10:13:02', 'admin', '2019-03-30 11:37:36', 0);
INSERT INTO `sys_dict` VALUES ('78bda155fe380b1b3f175f1e88c284c6', '流程状态', 'bpm_status', '流程状态', 0, 'admin', '2019-05-09 16:31:52', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('83bfb33147013cc81640d5fd9eda030c', '日志类型', 'log_type', NULL, 0, 'admin', '2019-03-18 23:22:19', NULL, NULL, 1);
INSERT INTO `sys_dict` VALUES ('845da5006c97754728bf48b6a10f79cc', '状态', 'status', NULL, 1, 'admin', '2019-03-18 21:45:25', 'admin', '2019-03-18 21:58:25', 0);
INSERT INTO `sys_dict` VALUES ('880a895c98afeca9d9ac39f29e67c13e', '操作类型', 'operate_type', '操作类型', 0, 'admin', '2019-07-22 10:54:29', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('8dfe32e2d29ea9430a988b3b558bf233', '发布状态', 'send_status', '发布状态', 0, 'admin', '2019-04-16 17:40:42', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('a7adbcd86c37f7dbc9b66945c82ef9e6', '1是0否', 'yn', '', 1, 'admin', '2019-05-22 19:29:29', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('a9d9942bd0eccb6e89de92d130ec4c4a', '消息发送状态', 'msgSendStatus', NULL, 0, 'admin', '2019-04-12 18:18:17', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('ac2f7c0c5c5775fcea7e2387bcb22f01', '菜单类型', 'menu_type', NULL, 0, 'admin', '2020-12-18 23:24:32', 'admin', '2019-04-01 15:27:06', 1);
INSERT INTO `sys_dict` VALUES ('ad7c65ba97c20a6805d5dcdf13cdaf36', 'onlineT类型', 'ceshi_online', NULL, 1, 'admin', '2019-03-22 16:31:49', 'admin', '2019-03-22 16:34:16', 0);
INSERT INTO `sys_dict` VALUES ('bd1b8bc28e65d6feefefb6f3c79f42fd', 'Online图表数据类型', 'online_graph_data_type', 'Online图表数据类型', 0, 'admin', '2019-04-12 17:24:24', 'admin', '2019-04-12 17:24:57', 0);
INSERT INTO `sys_dict` VALUES ('c36169beb12de8a71c8683ee7c28a503', '部门状态', 'depart_status', NULL, 0, 'admin', '2019-03-18 21:59:51', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('c5a14c75172783d72cbee6ee7f5df5d1', 'Online图表类型', 'online_graph_type', 'Online图表类型', 0, 'admin', '2019-04-12 17:04:06', NULL, NULL, 0);
INSERT INTO `sys_dict` VALUES ('d6e1152968b02d69ff358c75b48a6ee1', '流程类型', 'bpm_process_type', NULL, 0, 'admin', '2021-02-22 19:26:54', 'admin', '2019-03-30 18:14:44', 0);
INSERT INTO `sys_dict` VALUES ('fc6cd58fde2e8481db10d3a1e68ce70c', '用户状态', 'user_status', NULL, 0, 'admin', '2019-03-18 21:57:25', 'admin', '2019-03-18 23:11:58', 1);

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `dict_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典id',
  `item_text` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典项文本',
  `item_value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典项值',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `sort_order` int(10) NULL DEFAULT NULL COMMENT '排序',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态（1启用 0不启用）',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_table_dict_id`(`dict_id`) USING BTREE,
  INDEX `index_table_sort_order`(`sort_order`) USING BTREE,
  INDEX `index_table_dict_status`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
INSERT INTO `sys_dict_item` VALUES ('0072d115e07c875d76c9b022e2179128', '4d7fec1a7799a436d26d02325eff295e', '低', 'L', '低', 3, 1, 'admin', '2019-04-16 17:04:59', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('05a2e732ce7b00aa52141ecc3e330b4e', '3486f32803bb953e7155dab3513dc68b', '已删除', '1', NULL, NULL, 1, 'admin', '2025-10-18 21:46:56', 'admin', '2019-03-28 22:23:20');
INSERT INTO `sys_dict_item` VALUES ('096c2e758d823def3855f6376bc736fb', 'bd1b8bc28e65d6feefefb6f3c79f42fd', 'SQL', 'sql', NULL, 1, 1, 'admin', '2019-04-12 17:26:26', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('0c9532916f5cd722017b46bc4d953e41', '2f0320997ade5dd147c90130f7218c3e', '指定用户', 'USER', NULL, NULL, 1, 'admin', '2019-03-17 21:22:19', 'admin', '2019-03-17 21:22:28');
INSERT INTO `sys_dict_item` VALUES ('0ca4beba9efc4f9dd54af0911a946d5c', '72cce0989df68887546746d8f09811aa', '附表', '3', NULL, 3, 1, 'admin', '2019-03-27 10:13:43', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1030a2652608f5eac3b49d70458b8532', '2e02df51611a4b9632828ab7e5338f00', '禁用', '2', '禁用', 2, 1, 'admin', '2021-03-26 18:27:28', 'admin', '2019-04-26 18:39:11');
INSERT INTO `sys_dict_item` VALUES ('1174509601047994369', '1174509082208395266', '员级', '1', '', 1, 1, 'admin', '2019-09-19 10:24:45', 'admin', '2019-09-23 11:46:39');
INSERT INTO `sys_dict_item` VALUES ('1174509667297026049', '1174509082208395266', '助级', '2', '', 2, 1, 'admin', '2019-09-19 10:25:01', 'admin', '2019-09-23 11:46:47');
INSERT INTO `sys_dict_item` VALUES ('1174509713568587777', '1174509082208395266', '中级', '3', '', 3, 1, 'admin', '2019-09-19 10:25:12', 'admin', '2019-09-23 11:46:56');
INSERT INTO `sys_dict_item` VALUES ('1174509788361416705', '1174509082208395266', '副高级', '4', '', 4, 1, 'admin', '2019-09-19 10:25:30', 'admin', '2019-09-23 11:47:06');
INSERT INTO `sys_dict_item` VALUES ('1174509835803189250', '1174509082208395266', '正高级', '5', '', 5, 1, 'admin', '2019-09-19 10:25:41', 'admin', '2019-09-23 11:47:12');
INSERT INTO `sys_dict_item` VALUES ('1174511197735665665', '1174511106530525185', '组织机构', '1', '组织机构', 1, 1, 'admin', '2019-09-19 10:31:05', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1174511244036587521', '1174511106530525185', '岗位', '2', '岗位', 1, 1, 'admin', '2019-09-19 10:31:16', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1178295553450061826', '1178295274528845826', '可编辑(未授权禁用)', '2', '', 2, 1, 'admin', '2019-09-29 21:08:46', 'admin', '2019-09-29 21:09:18');
INSERT INTO `sys_dict_item` VALUES ('1178295639554928641', '1178295274528845826', '可见(未授权不可见)', '1', '', 1, 1, 'admin', '2019-09-29 21:09:06', 'admin', '2019-09-29 21:09:24');
INSERT INTO `sys_dict_item` VALUES ('1199517884758368257', '1199517671259906049', '一般', '1', '', 1, 1, 'admin', '2019-11-27 10:38:44', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199517914017832962', '1199517671259906049', '重要', '2', '', 1, 1, 'admin', '2019-11-27 10:38:51', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199517941339529217', '1199517671259906049', '紧急', '3', '', 1, 1, 'admin', '2019-11-27 10:38:58', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199518186144276482', '1199518099888414722', '日常记录', '1', '', 1, 1, 'admin', '2019-11-27 10:39:56', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199518214858481666', '1199518099888414722', '本周工作', '2', '', 1, 1, 'admin', '2019-11-27 10:40:03', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199518235943247874', '1199518099888414722', '下周计划', '3', '', 1, 1, 'admin', '2019-11-27 10:40:08', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199520817285701634', '1199520177767587841', '列表', '1', '', 1, 1, 'admin', '2019-11-27 10:50:24', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199520835035996161', '1199520177767587841', '链接', '2', '', 1, 1, 'admin', '2019-11-27 10:50:28', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199525468672405505', '1199525215290306561', '未开始', '0', '', 1, 1, 'admin', '2019-11-27 11:08:52', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199525490575060993', '1199525215290306561', '进行中', '1', '', 1, 1, 'admin', '2019-11-27 11:08:58', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1199525506429530114', '1199525215290306561', '已完成', '2', '', 1, 1, 'admin', '2019-11-27 11:09:02', 'admin', '2019-11-27 11:10:02');
INSERT INTO `sys_dict_item` VALUES ('1199607547704647681', '4f69be5f507accea8d5df5f11346181a', '系统', '4', '', 1, 1, 'admin', '2019-11-27 16:35:02', 'admin', '2019-11-27 19:37:46');
INSERT INTO `sys_dict_item` VALUES ('1209733775114702850', '1209733563293962241', 'MySQL', '1', '', 1, 1, 'admin', '2019-12-25 15:13:02', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1209733839933476865', '1209733563293962241', 'Oracle', '2', '', 1, 1, 'admin', '2019-12-25 15:13:18', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1209733903020003330', '1209733563293962241', 'SQLServer', '3', '', 1, 1, 'admin', '2019-12-25 15:13:33', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('147c48ff4b51545032a9119d13f3222a', 'd6e1152968b02d69ff358c75b48a6ee1', '测试流程', 'test', NULL, 1, 1, 'admin', '2019-03-22 19:27:05', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1543fe7e5e26fb97cdafe4981bedc0c8', '4c03fca6bf1f0299c381213961566349', '单排布局', 'single', NULL, 2, 1, 'admin', '2022-07-12 17:43:39', 'admin', '2019-04-12 17:43:57');
INSERT INTO `sys_dict_item` VALUES ('1b8a6341163062dad8cb2fddd34e0c3b', '404a04a15f371566c658ee9ef9fc392a', '22', '222', NULL, 1, 1, 'admin', '2019-03-30 11:17:48', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1ce390c52453891f93514c1bd2795d44', 'ad7c65ba97c20a6805d5dcdf13cdaf36', '000', '00', NULL, 1, 1, 'admin', '2019-03-22 16:34:34', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('1db531bcff19649fa82a644c8a939dc4', '4c03fca6bf1f0299c381213961566349', '组合布局', 'combination', '', 4, 1, 'admin', '2019-05-11 16:07:08', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('222705e11ef0264d4214affff1fb4ff9', '4f69be5f507accea8d5df5f11346181a', '短信', '1', '', 1, 1, 'admin', '2023-02-28 10:50:36', 'admin', '2019-04-28 10:58:11');
INSERT INTO `sys_dict_item` VALUES ('23a5bb76004ed0e39414e928c4cde155', '4e4602b3e3686f0911384e188dc7efb4', '不等于', '!=', '不等于', 3, 1, 'admin', '2019-04-01 16:46:15', 'admin', '2019-04-01 17:48:40');
INSERT INTO `sys_dict_item` VALUES ('25847e9cb661a7c711f9998452dc09e6', '4e4602b3e3686f0911384e188dc7efb4', '小于等于', '<=', '小于等于', 6, 1, 'admin', '2019-04-01 16:44:34', 'admin', '2019-04-01 17:49:10');
INSERT INTO `sys_dict_item` VALUES ('2d51376643f220afdeb6d216a8ac2c01', '68168534ff5065a152bfab275c2136f8', '有效', '1', '有效', 2, 1, 'admin', '2019-04-26 19:22:01', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('308c8aadf0c37ecdde188b97ca9833f5', '8dfe32e2d29ea9430a988b3b558bf233', '已发布', '1', '已发布', 2, 1, 'admin', '2019-04-16 17:41:24', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('333e6b2196e01ef9a5f76d74e86a6e33', '8dfe32e2d29ea9430a988b3b558bf233', '未发布', '0', '未发布', 1, 1, 'admin', '2019-04-16 17:41:12', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('337ea1e401bda7233f6258c284ce4f50', 'bd1b8bc28e65d6feefefb6f3c79f42fd', 'JSON', 'json', NULL, 1, 1, 'admin', '2019-04-12 17:26:33', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('33bc9d9f753cf7dc40e70461e50fdc54', 'a9d9942bd0eccb6e89de92d130ec4c4a', '发送失败', '2', NULL, 3, 1, 'admin', '2019-04-12 18:20:02', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('3fbc03d6c994ae06d083751248037c0e', '78bda155fe380b1b3f175f1e88c284c6', '已完成', '3', '已完成', 3, 1, 'admin', '2019-05-09 16:33:25', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('41d7aaa40c9b61756ffb1f28da5ead8e', '0b5d19e1fce4b2e6647e6b4a17760c14', '通知公告', '1', NULL, 1, 1, 'admin', '2019-04-22 18:01:57', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('41fa1e9571505d643aea87aeb83d4d76', '4e4602b3e3686f0911384e188dc7efb4', '等于', '=', '等于', 4, 1, 'admin', '2019-04-01 16:45:24', 'admin', '2019-04-01 17:49:00');
INSERT INTO `sys_dict_item` VALUES ('43d2295b8610adce9510ff196a49c6e9', '845da5006c97754728bf48b6a10f79cc', '正常', '1', NULL, NULL, 1, 'admin', '2019-03-18 21:45:51', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('4f05fb5376f4c61502c5105f52e4dd2b', '83bfb33147013cc81640d5fd9eda030c', '操作日志', '2', NULL, NULL, 1, 'admin', '2019-03-18 23:22:49', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('50223341bfb5ba30bf6319789d8d17fe', 'd6e1152968b02d69ff358c75b48a6ee1', '业务办理', 'business', NULL, 3, 1, 'admin', '2023-04-22 19:28:05', 'admin', '2019-03-22 23:24:39');
INSERT INTO `sys_dict_item` VALUES ('51222413e5906cdaf160bb5c86fb827c', 'a7adbcd86c37f7dbc9b66945c82ef9e6', '是', '1', '', 1, 1, 'admin', '2019-05-22 19:29:45', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('538fca35afe004972c5f3947c039e766', '2e02df51611a4b9632828ab7e5338f00', '显示', '1', '显示', 1, 1, 'admin', '2025-03-26 18:27:13', 'admin', '2019-04-26 18:39:07');
INSERT INTO `sys_dict_item` VALUES ('5584c21993bde231bbde2b966f2633ac', '4e4602b3e3686f0911384e188dc7efb4', '自定义SQL表达式', 'USE_SQL_RULES', '自定义SQL表达式', 9, 1, 'admin', '2019-04-01 10:45:24', 'admin', '2019-04-01 17:49:27');
INSERT INTO `sys_dict_item` VALUES ('58b73b344305c99b9d8db0fc056bbc0a', '72cce0989df68887546746d8f09811aa', '主表', '2', NULL, 2, 1, 'admin', '2019-03-27 10:13:36', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('5b65a88f076b32e8e69d19bbaadb52d5', '2f0320997ade5dd147c90130f7218c3e', '全体用户', 'ALL', NULL, NULL, 1, 'admin', '2020-10-17 21:22:43', 'admin', '2019-03-28 22:17:09');
INSERT INTO `sys_dict_item` VALUES ('5d833f69296f691843ccdd0c91212b6b', '880a895c98afeca9d9ac39f29e67c13e', '修改', '3', '', 3, 1, 'admin', '2019-07-22 10:55:07', 'admin', '2019-07-22 10:55:41');
INSERT INTO `sys_dict_item` VALUES ('5d84a8634c8fdfe96275385075b105c9', '3d9a351be3436fbefb1307d4cfb49bf2', '女', '2', NULL, 2, 1, NULL, '2019-01-04 14:56:56', NULL, '2019-01-04 17:38:12');
INSERT INTO `sys_dict_item` VALUES ('66c952ae2c3701a993e7db58f3baf55e', '4e4602b3e3686f0911384e188dc7efb4', '大于', '>', '大于', 1, 1, 'admin', '2019-04-01 10:45:46', 'admin', '2019-04-01 17:48:29');
INSERT INTO `sys_dict_item` VALUES ('6937c5dde8f92e9a00d4e2ded9198694', 'ad7c65ba97c20a6805d5dcdf13cdaf36', 'easyui', '3', NULL, 1, 1, 'admin', '2019-03-22 16:32:15', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('69cacf64e244100289ddd4aa9fa3b915', 'a9d9942bd0eccb6e89de92d130ec4c4a', '未发送', '0', NULL, 1, 1, 'admin', '2019-04-12 18:19:23', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('6a7a9e1403a7943aba69e54ebeff9762', '4f69be5f507accea8d5df5f11346181a', '邮件', '2', '', 2, 1, 'admin', '2031-02-28 10:50:44', 'admin', '2019-04-28 10:59:03');
INSERT INTO `sys_dict_item` VALUES ('6c682d78ddf1715baf79a1d52d2aa8c2', '72cce0989df68887546746d8f09811aa', '单表', '1', NULL, 1, 1, 'admin', '2019-03-27 10:13:29', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('6d404fd2d82311fbc87722cd302a28bc', '4e4602b3e3686f0911384e188dc7efb4', '模糊', 'LIKE', '模糊', 7, 1, 'admin', '2019-04-01 16:46:02', 'admin', '2019-04-01 17:49:20');
INSERT INTO `sys_dict_item` VALUES ('6d4e26e78e1a09699182e08516c49fc4', '4d7fec1a7799a436d26d02325eff295e', '高', 'H', '高', 1, 1, 'admin', '2019-04-16 17:04:24', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('700e9f030654f3f90e9ba76ab0713551', '6b78e3f59faec1a4750acff08030a79b', '333', '333', NULL, NULL, 1, 'admin', '2019-02-21 19:59:47', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('7050c1522702bac3be40e3b7d2e1dfd8', 'c5a14c75172783d72cbee6ee7f5df5d1', '柱状图', 'bar', NULL, 1, 1, 'admin', '2019-04-12 17:05:17', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('71b924faa93805c5c1579f12e001c809', 'd6e1152968b02d69ff358c75b48a6ee1', 'OA办公', 'oa', NULL, 2, 1, 'admin', '2021-03-22 19:27:17', 'admin', '2019-03-22 23:24:36');
INSERT INTO `sys_dict_item` VALUES ('75b260d7db45a39fc7f21badeabdb0ed', 'c36169beb12de8a71c8683ee7c28a503', '不启用', '0', NULL, NULL, 1, 'admin', '2019-03-18 23:29:41', 'admin', '2019-03-18 23:29:54');
INSERT INTO `sys_dict_item` VALUES ('7688469db4a3eba61e6e35578dc7c2e5', 'c36169beb12de8a71c8683ee7c28a503', '启用', '1', NULL, NULL, 1, 'admin', '2019-03-18 23:29:28', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('78ea6cadac457967a4b1c4eb7aaa418c', 'fc6cd58fde2e8481db10d3a1e68ce70c', '正常', '1', NULL, NULL, 1, 'admin', '2019-03-18 23:30:28', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('7ccf7b80c70ee002eceb3116854b75cb', 'ac2f7c0c5c5775fcea7e2387bcb22f01', '按钮权限', '2', NULL, NULL, 1, 'admin', '2019-03-18 23:25:40', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('81fb2bb0e838dc68b43f96cc309f8257', 'fc6cd58fde2e8481db10d3a1e68ce70c', '冻结', '2', NULL, NULL, 1, 'admin', '2019-03-18 23:30:37', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('83250269359855501ec4e9c0b7e21596', '4274efc2292239b6f000b153f50823ff', '可见/可访问(授权后可见/可访问)', '1', '', 1, 1, 'admin', '2019-05-10 17:54:51', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('84778d7e928bc843ad4756db1322301f', '4e4602b3e3686f0911384e188dc7efb4', '大于等于', '>=', '大于等于', 5, 1, 'admin', '2019-04-01 10:46:02', 'admin', '2019-04-01 17:49:05');
INSERT INTO `sys_dict_item` VALUES ('848d4da35ebd93782029c57b103e5b36', 'c5a14c75172783d72cbee6ee7f5df5d1', '饼图', 'pie', NULL, 3, 1, 'admin', '2019-04-12 17:05:49', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('84dfc178dd61b95a72900fcdd624c471', '78bda155fe380b1b3f175f1e88c284c6', '处理中', '2', '处理中', 2, 1, 'admin', '2019-05-09 16:33:01', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('86f19c7e0a73a0bae451021ac05b99dd', 'ac2f7c0c5c5775fcea7e2387bcb22f01', '子菜单', '1', NULL, NULL, 1, 'admin', '2019-03-18 23:25:27', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('8bccb963e1cd9e8d42482c54cc609ca2', '4f69be5f507accea8d5df5f11346181a', '微信', '3', NULL, 3, 1, 'admin', '2021-05-11 14:29:12', 'admin', '2019-04-11 14:29:31');
INSERT INTO `sys_dict_item` VALUES ('8c618902365ca681ebbbe1e28f11a548', '4c753b5293304e7a445fd2741b46529d', '启用', '1', '', 0, 1, 'admin', '2020-07-18 23:19:27', 'admin', '2019-05-17 14:51:18');
INSERT INTO `sys_dict_item` VALUES ('8cdf08045056671efd10677b8456c999', '4274efc2292239b6f000b153f50823ff', '可编辑(未授权时禁用)', '2', '', 2, 1, 'admin', '2019-05-10 17:55:38', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('8ff48e657a7c5090d4f2a59b37d1b878', '4d7fec1a7799a436d26d02325eff295e', '中', 'M', '中', 2, 1, 'admin', '2019-04-16 17:04:40', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('948923658baa330319e59b2213cda97c', '880a895c98afeca9d9ac39f29e67c13e', '添加', '2', '', 2, 1, 'admin', '2019-07-22 10:54:59', 'admin', '2019-07-22 10:55:36');
INSERT INTO `sys_dict_item` VALUES ('9a96c4a4e4c5c9b4e4d0cbf6eb3243cc', '4c753b5293304e7a445fd2741b46529d', '不启用', '0', NULL, 1, 1, 'admin', '2019-03-18 23:19:53', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('a1e7d1ca507cff4a480c8caba7c1339e', '880a895c98afeca9d9ac39f29e67c13e', '导出', '6', '', 6, 1, 'admin', '2019-07-22 12:06:50', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('a2321496db6febc956a6c70fab94cb0c', '404a04a15f371566c658ee9ef9fc392a', '3', '3', NULL, 1, 1, 'admin', '2019-03-30 11:18:18', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('a2be752dd4ec980afaec1efd1fb589af', '8dfe32e2d29ea9430a988b3b558bf233', '已撤销', '2', '已撤销', 3, 1, 'admin', '2019-04-16 17:41:39', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('aa0d8a8042a18715a17f0a888d360aa4', 'ac2f7c0c5c5775fcea7e2387bcb22f01', '一级菜单', '0', NULL, NULL, 1, 'admin', '2019-03-18 23:24:52', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('adcf2a1fe93bb99a84833043f475fe0b', '4e4602b3e3686f0911384e188dc7efb4', '包含', 'IN', '包含', 8, 1, 'admin', '2019-04-01 16:45:47', 'admin', '2019-04-01 17:49:24');
INSERT INTO `sys_dict_item` VALUES ('b029a41a851465332ee4ee69dcf0a4c2', '0b5d19e1fce4b2e6647e6b4a17760c14', '系统消息', '2', NULL, 1, 1, 'admin', '2019-02-22 18:02:08', 'admin', '2019-04-22 18:02:13');
INSERT INTO `sys_dict_item` VALUES ('b2a8b4bb2c8e66c2c4b1bb086337f393', '3486f32803bb953e7155dab3513dc68b', '正常', '0', NULL, NULL, 1, 'admin', '2022-10-18 21:46:48', 'admin', '2019-03-28 22:22:20');
INSERT INTO `sys_dict_item` VALUES ('b57f98b88363188daf38d42f25991956', '6b78e3f59faec1a4750acff08030a79b', '22', '222', NULL, NULL, 0, 'admin', '2019-02-21 19:59:43', 'admin', '2019-03-11 21:23:27');
INSERT INTO `sys_dict_item` VALUES ('b5f3bd5f66bb9a83fecd89228c0d93d1', '68168534ff5065a152bfab275c2136f8', '无效', '0', '无效', 1, 1, 'admin', '2019-04-26 19:21:49', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('b9fbe2a3602d4a27b45c100ac5328484', '78bda155fe380b1b3f175f1e88c284c6', '待提交', '1', '待提交', 1, 1, 'admin', '2019-05-09 16:32:35', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('ba27737829c6e0e582e334832703d75e', '236e8a4baff0db8c62c00dd95632834f', '同步', '1', '同步', 1, 1, 'admin', '2019-05-15 15:28:15', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('bcec04526b04307e24a005d6dcd27fd6', '880a895c98afeca9d9ac39f29e67c13e', '导入', '5', '', 5, 1, 'admin', '2019-07-22 12:06:41', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('c53da022b9912e0aed691bbec3c78473', '880a895c98afeca9d9ac39f29e67c13e', '查询', '1', '', 1, 1, 'admin', '2019-07-22 10:54:51', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('c5700a71ad08994d18ad1dacc37a71a9', 'a7adbcd86c37f7dbc9b66945c82ef9e6', '否', '0', '', 1, 1, 'admin', '2019-05-22 19:29:55', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('cbfcc5b88fc3a90975df23ffc8cbe29c', 'c5a14c75172783d72cbee6ee7f5df5d1', '曲线图', 'line', NULL, 2, 1, 'admin', '2019-05-12 17:05:30', 'admin', '2019-04-12 17:06:06');
INSERT INTO `sys_dict_item` VALUES ('d217592908ea3e00ff986ce97f24fb98', 'c5a14c75172783d72cbee6ee7f5df5d1', '数据列表', 'table', NULL, 4, 1, 'admin', '2019-04-12 17:05:56', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('df168368dcef46cade2aadd80100d8aa', '3d9a351be3436fbefb1307d4cfb49bf2', '男', '1', NULL, 1, 1, NULL, '2027-08-04 14:56:49', 'admin', '2019-03-23 22:44:44');
INSERT INTO `sys_dict_item` VALUES ('e6329e3a66a003819e2eb830b0ca2ea0', '4e4602b3e3686f0911384e188dc7efb4', '小于', '<', '小于', 2, 1, 'admin', '2019-04-01 16:44:15', 'admin', '2019-04-01 17:48:34');
INSERT INTO `sys_dict_item` VALUES ('e94eb7af89f1dbfa0d823580a7a6e66a', '236e8a4baff0db8c62c00dd95632834f', '不同步', '0', '不同步', 2, 1, 'admin', '2019-05-15 15:28:28', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('f0162f4cc572c9273f3e26b2b4d8c082', 'ad7c65ba97c20a6805d5dcdf13cdaf36', 'booostrap', '1', NULL, 1, 1, 'admin', '2021-08-22 16:32:04', 'admin', '2019-03-22 16:33:57');
INSERT INTO `sys_dict_item` VALUES ('f16c5706f3ae05c57a53850c64ce7c45', 'a9d9942bd0eccb6e89de92d130ec4c4a', '发送成功', '1', NULL, 2, 1, 'admin', '2019-04-12 18:19:43', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('f2a7920421f3335afdf6ad2b342f6b5d', '845da5006c97754728bf48b6a10f79cc', '冻结', '2', NULL, NULL, 1, 'admin', '2019-03-18 21:46:02', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('f37f90c496ec9841c4c326b065e00bb2', '83bfb33147013cc81640d5fd9eda030c', '登录日志', '1', NULL, NULL, 1, 'admin', '2019-03-18 23:22:37', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('f753aff60ff3931c0ecb4812d8b5e643', '4c03fca6bf1f0299c381213961566349', '双排布局', 'double', NULL, 3, 1, 'admin', '2019-04-12 17:43:51', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('f80a8f6838215753b05e1a5ba3346d22', '880a895c98afeca9d9ac39f29e67c13e', '删除', '4', '', 4, 1, 'admin', '2019-07-22 10:55:14', 'admin', '2019-07-22 10:55:30');
INSERT INTO `sys_dict_item` VALUES ('fcec03570f68a175e1964808dc3f1c91', '4c03fca6bf1f0299c381213961566349', 'Tab风格', 'tab', NULL, 1, 1, 'admin', '2019-04-12 17:43:31', NULL, NULL);
INSERT INTO `sys_dict_item` VALUES ('fe50b23ae5e68434def76f67cef35d2d', '78bda155fe380b1b3f175f1e88c284c6', '已作废', '4', '已作废', 4, 1, 'admin', '2021-09-09 16:33:43', 'admin', '2019-05-09 16:34:40');

-- ----------------------------
-- Table structure for sys_fill_rule
-- ----------------------------
DROP TABLE IF EXISTS `sys_fill_rule`;
CREATE TABLE `sys_fill_rule`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
  `rule_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则名称',
  `rule_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则Code',
  `rule_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则实现类',
  `rule_params` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则参数',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_sys_fill_rule_code`(`rule_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_fill_rule
-- ----------------------------
INSERT INTO `sys_fill_rule` VALUES ('1202551334738382850', '机构编码生成', 'org_num_role', 'org.jeecg.modules.system.rule.OrgCodeRule', '{\"parentId\":\"c6d7cb4deeac411cb3384b1b31278596\"}', 'admin', '2019-12-09 10:37:06', 'admin', '2019-12-05 19:32:35');
INSERT INTO `sys_fill_rule` VALUES ('1202787623203065858', '分类字典编码生成', 'category_code_rule', 'org.jeecg.modules.system.rule.CategoryCodeRule', '{\"pid\":\"\"}', 'admin', '2019-12-09 10:36:54', 'admin', '2019-12-06 11:11:31');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `log_type` int(2) NULL DEFAULT NULL COMMENT '日志类型（1登录日志，2操作日志）',
  `log_content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志内容',
  `operate_type` int(2) NULL DEFAULT NULL COMMENT '操作类型',
  `userid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作用户账号',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作用户名称',
  `ip` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP',
  `method` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求java方法',
  `request_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求路径',
  `request_param` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '请求参数',
  `request_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求类型',
  `cost_time` bigint(20) NULL DEFAULT NULL COMMENT '耗时',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_table_userid`(`userid`) USING BTREE,
  INDEX `index_logt_ype`(`log_type`) USING BTREE,
  INDEX `index_operate_type`(`operate_type`) USING BTREE,
  INDEX `index_log_type`(`log_type`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES ('b09ccd219a1ce5c7270bb659748b8330', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 15:34:38', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a22ddd4b5b0b84bd7794edd24b25fc64', 2, '添加测试DEMO', NULL, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', NULL, '[{\"createBy\":\"admin\",\"createTime\":1547883299809,\"email\":\"zhangdaiscott@163.com\",\"id\":\"7eac655877842eb39dc2f0469f3964ec\",\"name\":\"zhang daihao\"}]', NULL, 25, 'admin', '2019-01-19 15:34:59', NULL, NULL);
INSERT INTO `sys_log` VALUES ('07a0b3f8b4140a7a586305c2f40a2310', 2, '删除测试DEMO', NULL, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.delete()', NULL, '[\"7eac655877842eb39dc2f0469f3964ec\"]', NULL, 14, 'admin', '2019-01-19 15:38:11', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d7902eeab2c34611fad046a79bff1c1b', 2, '添加测试DEMO', NULL, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', NULL, '[{\"createBy\":\"admin\",\"createTime\":1547883544104,\"email\":\"zhangdaiscott@163.com\",\"id\":\"4436302a0de50bb83025286bc414d6a9\",\"name\":\"zhang daihao\"}]', NULL, 1682, 'admin', '2019-01-19 15:39:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a68160f37cace166fedd299c4ca0be10', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 15:40:00', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c6c0316b6989bf1eea0a3803f593bf69', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 15:47:19', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4b1341863a8fffeccda8bbe413bd815f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 15:59:52', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ed50b1fbc80c3b953f4551081b10335e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 16:19:06', NULL, NULL);
INSERT INTO `sys_log` VALUES ('dabdcb8e15ea9215a1af22f7567ff73d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 16:48:13', NULL, NULL);
INSERT INTO `sys_log` VALUES ('446724ea6dd41f4a03111c42e00d80cd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 16:56:36', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0e41fe3a34d5715bf4c88e220663583a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 17:04:06', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9f2db1ffaf89518a25cc6701da0c5858', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 17:05:07', NULL, NULL);
INSERT INTO `sys_log` VALUES ('954f1ccb8b230d2d7d4858eec3aba0a4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 17:08:37', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7374f3a2ccb20216cf8eecb26037ce0a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 18:08:51', NULL, NULL);
INSERT INTO `sys_log` VALUES ('130de55edac71aab730786307cc65936', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 20:22:57', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0bc44e2d682c9f28525d203589a90b43', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 20:31:08', NULL, NULL);
INSERT INTO `sys_log` VALUES ('122edcafd54dd06e12838f41123d9d5d', 2, '添加测试DEMO', NULL, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', NULL, '[{\"age\":28,\"birthday\":1546617600000,\"createBy\":\"admin\",\"createTime\":1547901234989,\"id\":\"42c08b1a2e5b2a96ffa4cc88383d4b11\",\"name\":\"秦500\",\"punchTime\":1546691611000}]', NULL, 21387, 'admin', '2019-01-19 20:34:11', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1a570aac0c30ac2955b59e2dc7a6204c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 20:58:26', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c18db091677ec01d55e913662b9028a9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 21:19:07', NULL, NULL);
INSERT INTO `sys_log` VALUES ('88d8b4b50bdab58c52fe25fa711fbbef', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 21:21:39', NULL, NULL);
INSERT INTO `sys_log` VALUES ('6b876be6e384337b36ad28a4a5868be8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 21:22:12', NULL, NULL);
INSERT INTO `sys_log` VALUES ('cb6b52fbbdd4c5698c17edaf9960e11e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 21:22:23', NULL, NULL);
INSERT INTO `sys_log` VALUES ('fea8e1e2d229557185be0d9a10ebce17', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 21:55:55', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c1842fc83cdf0b0cc0264bf093e9c55d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 21:56:15', NULL, NULL);
INSERT INTO `sys_log` VALUES ('543970eba4d1c522e3cb597b0fd4ad13', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 22:53:18', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e9ce2b3f7ac1fa3f5f7fd247207ca5c0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 22:53:35', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0e365a21c60e4460813bdc4e3cb320a3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 23:01:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d3df1a4057b6d7fb4dab073a727ba21f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 23:14:02', NULL, NULL);
INSERT INTO `sys_log` VALUES ('8f616500d666a5a67bc98e7ccd73c2e2', 2, '添加测试DEMO', NULL, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', NULL, '[{\"createBy\":\"admin\",\"createTime\":1547912194199,\"id\":\"ffa9da1ad40632dfcabac51d766865bd\",\"name\":\"秦999\"}]', NULL, 386, 'admin', '2019-01-19 23:36:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('055cf35c8865761b479c7f289dc36616', 2, '添加测试DEMO', NULL, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', NULL, '[{\"createBy\":\"admin\",\"createTime\":1547912238787,\"email\":\"zhangdaiscott@163.com\",\"id\":\"c2c0d49e3c01913067cf8d1fb3c971d2\",\"name\":\"zhang daihao\"}]', NULL, 16, 'admin', '2019-01-19 23:37:18', NULL, NULL);
INSERT INTO `sys_log` VALUES ('69e3164d007be2b9834e4fb398186f39', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-19 23:38:17', NULL, NULL);
INSERT INTO `sys_log` VALUES ('92e514fee917a1a459c4ffdb0ca42516', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 10:20:52', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d3f08843a9b2b3284711e376fb785beb', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 10:58:03', NULL, NULL);
INSERT INTO `sys_log` VALUES ('76bea561f662ec0ccf05bc370f1ffe35', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 11:08:55', NULL, NULL);
INSERT INTO `sys_log` VALUES ('273081678d85acebaa6615973bff31db', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 15:02:50', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b26369680b41d581649cf865e88331e9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 15:03:07', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7313b43ff53015d79a58b4dc7c660721', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 15:03:07', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f99912c5ff252594f14d31b768f8ad15', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 15:03:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('dcec1957987abbe6658f1f2c96980366', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 15:05:21', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c7b6156c4f42b70c562b507766f4546c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 15:14:08', NULL, NULL);
INSERT INTO `sys_log` VALUES ('52673feae24ea5bc3ca111f19c9a85d4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 15:16:46', NULL, NULL);
INSERT INTO `sys_log` VALUES ('507b55d3b5ddc487fb40ca1f716a1253', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 15:43:02', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7351132f4f5f65e5bf157dd7ad5344a4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 15:51:36', NULL, NULL);
INSERT INTO `sys_log` VALUES ('961992e05772bc7ad2ca927cf7649440', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 15:55:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3b07fda32423a5696b2097e1c23c00d4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 16:04:32', NULL, NULL);
INSERT INTO `sys_log` VALUES ('8447099784da63b3b2cd2fbbc5eabcea', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 16:04:49', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b20ff98a10af3c25c1991741fd59ea64', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 16:07:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9acebd2d37c9078f9568125fb9696976', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 16:07:59', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d70c2847d8d0936a2a761f745a84aa48', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 16:39:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('279e519d647f1a4e1f85f9b90ab370b9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 17:01:30', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b605a83a9b5f3cdaaa1b3f4f41a5f12d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 17:04:20', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0a24b1f04f79a2bcb83c4cd12d077cbc', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 17:34:37', NULL, NULL);
INSERT INTO `sys_log` VALUES ('661c4792f00b0814e486c3d623d7259f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 18:06:11', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d1746c5c937fcb650bd835ff74dabdff', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 18:06:32', NULL, NULL);
INSERT INTO `sys_log` VALUES ('8ec3a287a37d155047e80a80769d5226', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 18:37:14', NULL, NULL);
INSERT INTO `sys_log` VALUES ('6cbd2a9257fae1cb7ff7bc2eb264b3ab', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 19:08:14', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f06e8fa83b408be905b4dc7caeaf9a80', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 19:40:33', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f84e86c9a21149134b1f2599a424164b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-21 20:12:27', NULL, NULL);
INSERT INTO `sys_log` VALUES ('88bfc5b77b4be0d6d0f7c8661cf24853', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-22 10:25:53', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b9bf472a12fc25a9d4b500421b08b025', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-22 10:53:54', NULL, NULL);
INSERT INTO `sys_log` VALUES ('dbbcfb7f59311637a613ec9a6c63f04a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-22 10:53:57', NULL, NULL);
INSERT INTO `sys_log` VALUES ('69ea2322f72b41bcdc7f235889132703', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-22 10:54:43', NULL, NULL);
INSERT INTO `sys_log` VALUES ('62d197757e2cb40f9e8cb57fa6a207f7', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-22 10:54:54', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ccad29843623a6c3ca59548b1d533b15', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-22 10:56:33', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4d9299e2daac1f49eac0cec75a90c32e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-22 11:28:39', NULL, NULL);
INSERT INTO `sys_log` VALUES ('43848099c1e70910ba1572868ee40415', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-22 11:28:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('6fb7db45b11bc22347b234fda07700c8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-22 12:00:22', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e8cde8dcd6253b249d67a05aaf10f968', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-22 12:30:28', NULL, NULL);
INSERT INTO `sys_log` VALUES ('6a4231540c73ad67128d5a24e6a877ff', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-22 12:54:30', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2b3be3da6ba9d1ee49f378d729d69c50', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-22 13:24:41', NULL, NULL);
INSERT INTO `sys_log` VALUES ('78f519b618f82a39adad391fbf6b9c7a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-22 13:49:58', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1487d69ff97888f3a899e2ababb5ae48', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-22 14:21:17', NULL, NULL);
INSERT INTO `sys_log` VALUES ('cc7fa5567e7833a3475b29b7441a2976', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-22 14:21:31', NULL, NULL);
INSERT INTO `sys_log` VALUES ('52e36d72cd04bea2604747e006b038ec', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-22 19:47:17', NULL, NULL);
INSERT INTO `sys_log` VALUES ('523a54948d5edaf421566014b66f9465', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-22 19:50:27', NULL, NULL);
INSERT INTO `sys_log` VALUES ('48e4e10ac7e583050fd85734f0676a7c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-22 19:58:08', NULL, NULL);
INSERT INTO `sys_log` VALUES ('dee4d42c439b51b228ab5db5d0723fc0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-22 20:02:56', NULL, NULL);
INSERT INTO `sys_log` VALUES ('965c74ffe09d8a06bb817efa6d62254b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-23 10:01:35', NULL, NULL);
INSERT INTO `sys_log` VALUES ('059bac84373e9dae94363ea18802d70f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-23 10:06:56', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9ef3f1ed07003e3abec3445920b062f1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-23 11:17:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0169622dcd4e89b177a0917778ac7f9c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-23 11:17:18', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f8960d64e93606fa52220cc9c4ae35a2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-23 11:21:02', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4261867172d0fd5c04c993638661ac0b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-23 11:24:47', NULL, NULL);
INSERT INTO `sys_log` VALUES ('32464c6f7f772ddda0a963b19ad2fd70', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-23 11:30:20', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d29cf7aae44523bf2f3d187e91356fe8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-23 12:20:35', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0e9c0d0d26ddc652a7277912e0784d11', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-23 12:27:41', NULL, NULL);
INSERT INTO `sys_log` VALUES ('25f8b1b345b1c8a070fe81d715540c85', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-23 15:39:46', NULL, NULL);
INSERT INTO `sys_log` VALUES ('8327cced60486bad4009276e14403502', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-24 09:56:29', NULL, NULL);
INSERT INTO `sys_log` VALUES ('515c28df59f07478339b61ca5b1b54a8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-24 10:34:51', NULL, NULL);
INSERT INTO `sys_log` VALUES ('fa0612372b332b6c3ce787d9ca6dd2cc', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-24 11:48:21', NULL, NULL);
INSERT INTO `sys_log` VALUES ('8300e85a2c2f16c2358d31e8b364edf7', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-24 11:55:39', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3d9874f248a984608ca98c36c21c5a7a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-24 13:05:56', NULL, NULL);
INSERT INTO `sys_log` VALUES ('cc8ab347f332c55570830c5fc39bbf9f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-24 13:08:38', NULL, NULL);
INSERT INTO `sys_log` VALUES ('8742a458bf166fd5f134ac65fa8903f9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-24 13:09:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('bbe2e637bafa0d7f465dc9e1266cff3d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-25 11:16:50', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b3474fc5aad9ec2f36ccbbf7bf864a69', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-25 11:17:24', NULL, NULL);
INSERT INTO `sys_log` VALUES ('260bb025d91b59d0135d635ef85eeb82', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-25 11:40:13', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1a5b71c9458c17f9bcb19a5747fd47dd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-25 11:56:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e720278084b0d4316448ec59d4e3399d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-25 15:52:45', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f6646950c8465da1d1219b7a7a209fc2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-25 19:14:07', NULL, NULL);
INSERT INTO `sys_log` VALUES ('36358cacfc5eb3ba7e85cfe156218b71', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-25 19:14:17', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ee2bb63c47c868d59a45503b3d2f34ea', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-25 19:16:14', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b0d11dfec52e02f504c63e2f8224b00d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-25 19:27:44', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4acfbc327681d89dab861c77401f8992', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-28 10:54:36', NULL, NULL);
INSERT INTO `sys_log` VALUES ('96ada57ac17c4477f4e4c8d596d4cc1a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-28 10:54:44', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e4e40e21437b23b74324e0402cceb71a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-28 11:34:40', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d92d9e003666c6b020f079eaee721f9f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-28 12:08:43', NULL, NULL);
INSERT INTO `sys_log` VALUES ('68f7394ca53c59438b2b41e7bb9f3094', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-28 14:09:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a9b34565c6460dc9cede00ad150393f9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-28 14:17:53', NULL, NULL);
INSERT INTO `sys_log` VALUES ('fa427f74dc6bd9cca3db478b5842f7f7', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-28 14:19:07', NULL, NULL);
INSERT INTO `sys_log` VALUES ('8b66ec251e3107765768dbd0590eeb29', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-28 14:25:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e42a38382fce916909d6d09f66147006', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-28 14:28:44', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ed0bbe9047a7471ae1cdc1c2941eccb1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-29 17:52:38', NULL, NULL);
INSERT INTO `sys_log` VALUES ('36fd54ce8bc1ee4aac9e3ea4bfdcd5a8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-29 18:49:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('40b3a9bee45b23548250936310b273f4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-30 14:42:03', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c9be887c9292153e39861c91243b7432', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-30 15:12:15', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e40823376fa8c0e74a4e760de695e824', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-30 15:36:38', NULL, NULL);
INSERT INTO `sys_log` VALUES ('993010965223b8e3a7a784409f7e377e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-30 15:50:41', NULL, NULL);
INSERT INTO `sys_log` VALUES ('aa47c8cf2a4f2de16f415b9d9d3dbf05', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-30 16:14:36', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4a0020835a71fc6dcaefd01968d21f81', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-30 18:46:37', NULL, NULL);
INSERT INTO `sys_log` VALUES ('fa9cebbb6af23d2830584b3aacd51e46', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-31 13:59:17', NULL, NULL);
INSERT INTO `sys_log` VALUES ('60a975067f02cf05e74fa7b71e8e862a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-31 14:31:58', NULL, NULL);
INSERT INTO `sys_log` VALUES ('fbb8834e9736bdd4b6d3baee895c4ca4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-01-31 18:05:03', NULL, NULL);
INSERT INTO `sys_log` VALUES ('623e4bc7c098f368abcc368227235caf', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-01 09:48:57', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9f31eedbe3f3c5c431b490d5fec0094c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-01 09:56:36', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b945fe8b63e0fc26d02c85466f36ebd9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-01 09:57:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('968d434c45aae64c9ad0e86d18238065', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-01 10:02:22', NULL, NULL);
INSERT INTO `sys_log` VALUES ('732a1015057fde25d81ee12a7fbf66b2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-01 10:05:08', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d9a0bb9fe6d2c675aa84f9441c0bd8bb', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-11 10:56:23', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9c64406daa2b6e7ad1f6776789d61e43', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-11 10:56:46', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1912a44dd4a6ffa1636d2dde9c2f1ab7', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-11 11:01:03', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d19b6e77ab1b6d6aa58996a93918754c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-11 11:33:06', NULL, NULL);
INSERT INTO `sys_log` VALUES ('81f7a606359aff9f97f95c15ce8e7c69', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-11 11:33:42', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7da063020a42db99e0f3bb9500498828', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-11 13:45:36', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b6ee157afd006ceddc8c7558c251192e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-11 14:20:56', NULL, NULL);
INSERT INTO `sys_log` VALUES ('65ace1ae98891f48ab4121d9258e4f1e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-12 10:45:20', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e2af7674bb716a7c0b703c7c7e20b906', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-12 11:38:55', NULL, NULL);
INSERT INTO `sys_log` VALUES ('60d4f59974170c67826e64480533d793', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-12 13:25:40', NULL, NULL);
INSERT INTO `sys_log` VALUES ('775e987a2ca37edc4f21e022b265a84a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-12 13:36:13', NULL, NULL);
INSERT INTO `sys_log` VALUES ('dd6fbb9b6224c927c0923c16b9285525', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-12 13:37:01', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f3d371d6f71409ea2fe52405b725db4a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-12 13:38:15', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c33b4e0bbf998330e44fad65e9d0029e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-12 15:54:56', NULL, NULL);
INSERT INTO `sys_log` VALUES ('189842bf681338dc99dfa66d366a0e6f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-12 15:55:01', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e14cd21cf5eaad9ea3689730a824a50c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-12 16:12:32', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5cf2431447eab30fd3623e831033eea0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-12 19:17:29', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9bfe7312f2951503082a28c2cc966ce4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-13 10:24:02', NULL, NULL);
INSERT INTO `sys_log` VALUES ('da9a15efcf4e1e4f24647db7e2143238', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-13 11:19:54', NULL, NULL);
INSERT INTO `sys_log` VALUES ('8317a81bce60a10afeb44af6ef6c807a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-13 11:27:58', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0a6eb1fa998b749012216542a2447ae7', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-13 11:29:30', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e5a9b045449136719d4c19c429c2dd56', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-13 13:08:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('aaf10eab9c2b6ed6af1d7a9ce844d146', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-13 13:08:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b4ccdfc1280e73439eb1ad183076675b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-13 14:10:45', NULL, NULL);
INSERT INTO `sys_log` VALUES ('018fe8d3f049a32fb8b541c893058713', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-13 15:17:42', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f3aab8f9dff7bf705aa29c6dcce49011', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-13 15:18:27', NULL, NULL);
INSERT INTO `sys_log` VALUES ('efa591832b375b4609a5890b0c6f3250', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-13 16:00:13', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2c6822927334eb0810b71465fd9c4945', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-13 16:02:47', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7289cf420ac87ea0538bde81435b1aaa', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-13 16:03:51', NULL, NULL);
INSERT INTO `sys_log` VALUES ('db8adca4aa7972fdc283be96d877efe0', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-13 16:04:28', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c5e541648bab341230c93377b4d4e262', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-13 16:05:09', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e261674e2640fe6d0a3cd86df631537d', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-13 16:05:51', NULL, NULL);
INSERT INTO `sys_log` VALUES ('406e79995e3340d052d85a74a5d40d1b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-13 16:23:16', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4de1ed55165f7086f1a425a26a2f56ec', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-13 16:26:27', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d8eed69045aae6cedbff402b4e35f495', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-13 18:22:52', NULL, NULL);
INSERT INTO `sys_log` VALUES ('bbf4fb593d6918cc767bb50c9b6c16c5', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-14 10:44:20', NULL, NULL);
INSERT INTO `sys_log` VALUES ('506ce2d73a038b6e491a35a6c74a7343', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-14 13:44:04', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4303dbb3e502f11a3c4078f899bb3070', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-14 17:28:44', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2de252a92b59ebfbf16860cc563e3865', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-14 22:04:17', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e4c330b381e2fbfde49f1d4dd43e68b7', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-14 22:22:01', NULL, NULL);
INSERT INTO `sys_log` VALUES ('22735c059b01949a87cb918f5ef3be76', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-14 22:41:28', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c5954beca75d6a0c014e2de3b621275a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-14 22:41:31', NULL, NULL);
INSERT INTO `sys_log` VALUES ('db8c89112bf4706fb558664dd741aa46', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-15 09:33:23', NULL, NULL);
INSERT INTO `sys_log` VALUES ('fa0ce422c12a565461eca56006052891', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-15 10:13:21', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a34ed4c6fef2b9f07a20e54ef4501b99', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-15 10:48:00', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b55cc05e8dd4279c0fa145833db19ba8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-15 11:37:40', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5c675eeb69795180eee2c1069efc114b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-15 12:59:43', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5dee273feb8dd12989b40c2c92ce8c4a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-15 13:42:58', NULL, NULL);
INSERT INTO `sys_log` VALUES ('994efef0ebca19292e14a39b385b0e21', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-15 16:22:24', NULL, NULL);
INSERT INTO `sys_log` VALUES ('fc22aaf9660e66558689a58dfa443074', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-15 16:30:55', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2c6ede513b83fbc23aaedb89dbfa868a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-15 18:03:00', NULL, NULL);
INSERT INTO `sys_log` VALUES ('13c1e763e9d624a69727a38b85411352', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-15 18:39:00', NULL, NULL);
INSERT INTO `sys_log` VALUES ('fadb32d678346ee4bab02997988ff3bc', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-15 22:55:16', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0aa792eadeae39a1ed2a98ea5d2f6d27', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-16 09:11:39', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0aa9272c0581e1d7f62b1293375b4574', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-16 17:26:36', NULL, NULL);
INSERT INTO `sys_log` VALUES ('81c9056ac38e6f881d60f3d41df1845e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-17 11:44:47', NULL, NULL);
INSERT INTO `sys_log` VALUES ('eb4536aa50a58985baf0a763a1ce2ebf', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-17 19:48:49', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f9062582881b42f6b139c313d8ab0463', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-17 20:47:26', NULL, NULL);
INSERT INTO `sys_log` VALUES ('22d8a2fbd53eafb21f6f62ae073c0fc1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-17 22:28:52', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7bc7b1ff923dbb19fb0ecd800cd690bd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-18 09:34:59', NULL, NULL);
INSERT INTO `sys_log` VALUES ('faea0dbfb7f86b571fed0dd270623831', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-18 14:12:14', NULL, NULL);
INSERT INTO `sys_log` VALUES ('063baad688535096d2ed906ae6f3a128', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-18 22:09:21', NULL, NULL);
INSERT INTO `sys_log` VALUES ('528baecc596a66eaadc8887bff911f55', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-19 10:08:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e540ca989819c54baefffbc3d05e8b58', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-19 10:10:54', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0643f3ad4394de9fb3c491080c6a7a03', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-19 10:18:42', NULL, NULL);
INSERT INTO `sys_log` VALUES ('eb0b8a7cdf77df133566d7bd5a5f1fc0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-19 11:02:55', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0913bb0e92715892c470cf538726dfbc', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-19 16:17:09', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5034aec34f0b79da510e66008dbf2fcc', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-19 16:18:29', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e4afd66ac249dde9c3bd9da50f9c2469', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-19 17:41:54', NULL, NULL);
INSERT INTO `sys_log` VALUES ('07132c1228b1c165f62ea35f4ff1cbe9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-19 18:15:44', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4f7f587bec68ed5bf9f68b0ccd76d62b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-19 21:01:47', NULL, NULL);
INSERT INTO `sys_log` VALUES ('12709e62742056aa4a57fa8c2c82d84a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 09:13:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('680b3e6e4768d80d6ea0ce8ba71bdd0e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 09:14:03', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a6e323785535592ee208aa7e53554644', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 09:15:27', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3a4a0e27d77aa8b624180e5fd5e4004e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 09:51:58', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b98b7ac9e890657aa86a900763afbe2a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 11:49:44', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d1eb2a8ebed28d34199c5fc4a1579c4c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 12:55:09', NULL, NULL);
INSERT INTO `sys_log` VALUES ('85949de2d54078e6b8f3df0a3c79c43d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 17:08:44', NULL, NULL);
INSERT INTO `sys_log` VALUES ('77579d78a903635cc4942882f568e9e5', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 17:13:33', NULL, NULL);
INSERT INTO `sys_log` VALUES ('679e12ba247575749e03aa8f67347ac6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 17:14:42', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5c35117cbeb39428fcc2ddd90ce96a2b', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 17:18:51', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7225200c3cec4789af4f1da2c46b129d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 17:19:23', NULL, NULL);
INSERT INTO `sys_log` VALUES ('22ad9f87788506456c774801389d6a01', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 17:20:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('81c95e1c8805fa191753fc99ba54c3e9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 18:01:21', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7285730e2644f49def0937dc99bfbe3d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 18:07:01', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4922f2f1173a1edc11dfd11cb2a100ae', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 18:08:27', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e37cce529d0c98c47b4977d7ddf963c0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 18:17:54', NULL, NULL);
INSERT INTO `sys_log` VALUES ('66493cd0347eeb6ee2ef5ee923604683', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 18:29:47', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f04910792a74c563d057c4fcb345f963', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 18:30:00', NULL, NULL);
INSERT INTO `sys_log` VALUES ('210a01dcb34302eaed0d1e95820655d0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 18:30:15', NULL, NULL);
INSERT INTO `sys_log` VALUES ('48929ec94226d9ccff9fae4ff48e95e3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 18:32:55', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d2ac19a709ea08f7259286df28efd635', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 18:35:37', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d8fd478e6ceb03a575719e1a54342333', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 18:43:43', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a35a476c303983701045507c9af3fa03', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-20 18:44:58', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7e41208e29d412d586fc39375628b0d0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-21 15:34:35', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1f33d11e1833ae497e3ef65a3f02dd5b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-21 19:51:12', NULL, NULL);
INSERT INTO `sys_log` VALUES ('dae0658783324c81fa6909b6e4a25a65', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-22 11:46:41', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a77d29673cfe97c9e03cfb879b934f62', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-22 12:41:15', NULL, NULL);
INSERT INTO `sys_log` VALUES ('baaf37e5937f938ac92856bc74cc2b86', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-22 13:48:53', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3f47afcdce94596494746ac34eebf13b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-22 13:59:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b99fc7c53d4e3edc0c618edc11d3a073', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-22 15:58:43', NULL, NULL);
INSERT INTO `sys_log` VALUES ('024a4c5ba78538d05373dac650b227d1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-22 15:59:50', NULL, NULL);
INSERT INTO `sys_log` VALUES ('873f425879ef9ca7ced982acda19ea58', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-22 16:35:59', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1b05434820cbcb038028da9f5cda31bb', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-22 17:45:16', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5f314fc45492d7f90b74d1ca74d1d392', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-22 17:45:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('20751803c1e5b2d758b981ba22f61fcd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-22 18:11:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('50e8de3e6b45f8625b8fd5590c9fd834', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-22 18:23:18', NULL, NULL);
INSERT INTO `sys_log` VALUES ('6737424e01b38f2273e9728bf39f3e37', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-22 19:43:37', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0473dedf4aa653b253b008dacff2937c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-24 13:04:44', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a95192071de908f37f4998af4c269bcb', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-24 14:26:53', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3569ada5c43a4022d3d13ac801aff40e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-24 14:50:55', NULL, NULL);
INSERT INTO `sys_log` VALUES ('562092eb81561ee0f63be5dd9367d298', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-24 22:20:59', NULL, NULL);
INSERT INTO `sys_log` VALUES ('131ccd390401b6e3894a37e4d1d195d3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-24 22:26:52', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c12e3d7655a5a8b192bb9964a2a66946', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-24 22:35:45', NULL, NULL);
INSERT INTO `sys_log` VALUES ('6bc98b7dc91a3924f794202867367aca', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-24 22:50:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('002b7112a147edeb6149a891494577d0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-24 22:52:15', NULL, NULL);
INSERT INTO `sys_log` VALUES ('202344b08b69ad70754e6adaa777eae0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-24 22:54:22', NULL, NULL);
INSERT INTO `sys_log` VALUES ('eeb1f2e2c1b480e0bb62533848cbb176', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-24 22:55:46', NULL, NULL);
INSERT INTO `sys_log` VALUES ('94fe4465d779e0438cfe6f0cb1a1aa7e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-24 22:57:42', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d03aaee882d13b796db860cb95f27724', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-24 22:59:54', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1db82f78233c120c6ec7648ca1177986', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-24 23:07:39', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7dc448f04edf4b9655362ad1a1c58753', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-24 23:10:17', NULL, NULL);
INSERT INTO `sys_log` VALUES ('fce1553149aea9bfd93e089f387199c8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-24 23:11:35', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e713a89e753cbecf1e10247b2112c3f8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-24 23:14:36', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0a634ed086442afa7a5fc9aa000b898a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 10:10:39', NULL, NULL);
INSERT INTO `sys_log` VALUES ('bfa0766f53dbd3a0fe4043f57bd9bbee', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 10:35:33', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e3b531fa12e47ac19a2ab0c883dee595', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 10:40:13', NULL, NULL);
INSERT INTO `sys_log` VALUES ('18eafaeec588403245269a41732d1a74', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 10:45:14', NULL, NULL);
INSERT INTO `sys_log` VALUES ('99357d793f2507cfb7b270677b4fe56c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 10:46:42', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b38f42f4e15ee72e494bdf6f6feb0ae7', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 10:49:58', NULL, NULL);
INSERT INTO `sys_log` VALUES ('bfe758860662ae07a15598396a12cfaa', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 10:50:00', NULL, NULL);
INSERT INTO `sys_log` VALUES ('69a7a5b960d6aedda5c4bd8b877be0a8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 10:50:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4084f184160940a96e47d7be1fab4ea3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 10:51:11', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1241cf8e9fd0e28478a07bf755f528c5', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 10:51:12', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e0da357be27d66de1c9e9b8ecb22f9f9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 10:51:13', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9f4960f89a10d7fdcf22d1ea46143fff', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 10:51:13', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ab8a71b7565d356d12e12c6730b0ceb0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 10:51:13', NULL, NULL);
INSERT INTO `sys_log` VALUES ('35fdedc363d9fe514b44095da40f170b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 10:51:30', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7126b35521cd0dba932e6f04b0dac88f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 10:52:22', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9bd6e11c5a2f0bb70215cfa097a4b29c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 10:57:52', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7e2edea80050d2e46aa2e8faef8e29ce', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 11:01:38', NULL, NULL);
INSERT INTO `sys_log` VALUES ('190eb7b4d493eb01b13c5b97916eeb13', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 11:09:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ea268ad02db29012b2f1bd3d4aea1419', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 11:10:22', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7dc498b45fbf25c59686d9dda0d3eb66', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 11:12:38', NULL, NULL);
INSERT INTO `sys_log` VALUES ('583d3aa445d408f4ecd19ee0a85514af', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 11:18:04', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9cea908c7a78dc77fdaed975819983bd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 11:20:15', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d70329497664391dabc25effe7406c50', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 11:25:12', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0b9940fc5487026a3f16cade73efead5', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 11:28:28', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f21f9f700bf4f5bd9edda7a16ed338f8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 11:30:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f5c08b45885d248c422a5d406cd5f223', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 11:41:54', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e9a31bfc128b3f5ae01656916c605ddb', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 11:44:56', NULL, NULL);
INSERT INTO `sys_log` VALUES ('6baccd034e970c6f109791cff43bc327', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 11:46:12', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d2b516c5d834bd0fca91cda416fe499e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 11:46:42', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f9abb524e0dc3571571dc6e50ec6db75', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 11:47:13', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d1111594fef195980370c5f91ccf9212', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 11:48:09', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9174fe77fe8ba69243f72d5577b391d3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 11:48:38', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2ab9cf95ac35fdbb8fe976e13c404c41', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 11:49:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9be945480d69038865279f02df5cee45', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 11:49:47', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c2bfe3b92e6bfb7016cc82e95419a602', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 11:54:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7c310b99a84411798a2aaf4074a28e7e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 12:42:57', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b23293288a84ba965509f466ed0e7e2f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 12:43:11', NULL, NULL);
INSERT INTO `sys_log` VALUES ('cf590576a5f6a42b347e6b5bf5ebf5bd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 12:43:31', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b5df1807f08af5db640da11affec24d3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 12:49:25', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2746af3dd0309cdeeff7d27999fbcda1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 12:52:55', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2a383edf5445dc8493f5240144ca72f5', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 12:56:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d2910961a0ff046cc3ef6cf8d33a8094', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 15:38:47', NULL, NULL);
INSERT INTO `sys_log` VALUES ('00f763e007e5a6bddf4cb8e562a53005', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 15:41:31', NULL, NULL);
INSERT INTO `sys_log` VALUES ('8ab131214232450ca202103ef81f0a2d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 15:46:29', NULL, NULL);
INSERT INTO `sys_log` VALUES ('606cb4f81f9bb412e2b2bdaa0f3e5dda', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 16:27:23', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7b85fba62bc001773fff1a54e1609aef', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 16:28:20', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b3127e34f395e1f1790450da5689a4a1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 16:28:35', NULL, NULL);
INSERT INTO `sys_log` VALUES ('fb2871cda1421b766f8e68cb36a22bf3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 17:35:01', NULL, NULL);
INSERT INTO `sys_log` VALUES ('29fb5d4297748af3cd1c7f2611b7a2d6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 17:38:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('580256f7c7ea658786dba919009451b6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 17:39:43', NULL, NULL);
INSERT INTO `sys_log` VALUES ('8802209912ca66d56f2ea241ffd0cc13', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 17:52:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4778fe2992fd5efd65f86cb0e00e338e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 17:53:11', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d68957c067fb27e80a23babebdb1591f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 17:55:06', NULL, NULL);
INSERT INTO `sys_log` VALUES ('472c34745b8f86a46efa28f408465a63', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 17:56:31', NULL, NULL);
INSERT INTO `sys_log` VALUES ('26975f09c66025d1c8d87a6894a3c262', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 18:29:08', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e4a166fcd0fc4037cb26e35cc1fb87b2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 18:32:41', NULL, NULL);
INSERT INTO `sys_log` VALUES ('286af82485388bfcd3bb9821ff1a4727', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 18:34:37', NULL, NULL);
INSERT INTO `sys_log` VALUES ('eaf74cd1489b02d39c470eed131fc918', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 18:37:39', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e48a6bd82c92a8005c80c5ef36746117', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 19:32:26', NULL, NULL);
INSERT INTO `sys_log` VALUES ('10922e0d030960e6b026c67c6179247b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-25 20:07:59', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b53c9e8ce1e129a09a3cda8c01fe644c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 11:38:49', NULL, NULL);
INSERT INTO `sys_log` VALUES ('65be8e015c9f2c493bd0a4e405dd8221', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 11:40:20', NULL, NULL);
INSERT INTO `sys_log` VALUES ('8ff27392165c8c707ee10ec0010c7bb8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 11:44:07', NULL, NULL);
INSERT INTO `sys_log` VALUES ('337b647a4085e48f61c7832e6527517d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 11:45:16', NULL, NULL);
INSERT INTO `sys_log` VALUES ('caee69e55ec929f7ba904280cac971e6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 11:49:15', NULL, NULL);
INSERT INTO `sys_log` VALUES ('bdeae62057ae9858b6a546c1bdb6efc7', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 11:49:29', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ea66ed22fde49640cee5d73c6ef69718', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 11:50:04', NULL, NULL);
INSERT INTO `sys_log` VALUES ('47c5a5b799e10255c96ccd65286541ef', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 11:50:35', NULL, NULL);
INSERT INTO `sys_log` VALUES ('cfba34db2d7fbb15a2971212f09b59ec', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 11:51:43', NULL, NULL);
INSERT INTO `sys_log` VALUES ('faad055dd212ed9506b444f8f1a920b9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 12:00:38', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ef7219725c4b84cc71f56f97a8eab01a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 12:08:00', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2811e224e4e8d70f2946c815988b9b7c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 12:08:14', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7feae2fb5001ca0095c05a8b08270317', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 12:17:31', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b4c3c7af9899b9af3f42f730cfabc9b2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 12:17:58', NULL, NULL);
INSERT INTO `sys_log` VALUES ('28e8a7ed786eaced3182c70f68c7ea78', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 12:18:33', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3a76114e431912ff9a19a4b6eb795112', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 12:19:41', NULL, NULL);
INSERT INTO `sys_log` VALUES ('557b3c346d9bc8f7a83fac9f5b12dc1b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 12:20:28', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e92544c6102243e7908e0cbb217f5198', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 12:48:28', NULL, NULL);
INSERT INTO `sys_log` VALUES ('61445cc950f5d04d91339089b18edef9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:13:22', NULL, NULL);
INSERT INTO `sys_log` VALUES ('776c2e546c9ab0375d97590b048b8a9d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:13:51', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ef7669ac0350730d198f59b8411b19d1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:28:43', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b3cceb535fa5577cc21b12502535ad29', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:29:01', NULL, NULL);
INSERT INTO `sys_log` VALUES ('18a51a5f04eeaad6530665f6b0883f0c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:34:11', NULL, NULL);
INSERT INTO `sys_log` VALUES ('8a13971104d70e35111d10dd99de392e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:34:25', NULL, NULL);
INSERT INTO `sys_log` VALUES ('6d93d5667245ef8e5d6eafdbc9113f51', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:34:42', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7ba3df5d2612ac3dd724e07a55411386', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:35:03', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7148b3d58f121ef04bcbea5dd2e5fe3b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:35:23', NULL, NULL);
INSERT INTO `sys_log` VALUES ('709b0f2bf8cb8f785f883509e54ace28', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:37:37', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4114145795da30b34545e9e39b7822d9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:39:33', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f543c25bdd741055aeb4f77c5b5acf58', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:40:58', NULL, NULL);
INSERT INTO `sys_log` VALUES ('bc594b8921a0bcdb26d4a93916316092', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 14:42:58', NULL, NULL);
INSERT INTO `sys_log` VALUES ('11f511eeeb2e91af86b9d5e05132fc89', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 15:13:43', NULL, NULL);
INSERT INTO `sys_log` VALUES ('810deb9fd39fa2f0a8e30e3db42f7c2b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 18:51:00', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d5b9e5d9bfbbd8e6d651087ead76d9f7', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 20:17:41', NULL, NULL);
INSERT INTO `sys_log` VALUES ('016510fc662d9bb24d0186c5478df268', 1, '用户名: admin,登录成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-02-26 20:27:18', NULL, NULL);
INSERT INTO `sys_log` VALUES ('eb6f5196de91dd2e8316696bddd61345', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 22:26:36', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d762a1cba3dc23068f352323d98909e0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-02-26 22:26:49', NULL, NULL);
INSERT INTO `sys_log` VALUES ('69fc2407b46abad64fa44482c0dca59f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-01 12:04:25', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0d6cd835072c83f46d1d2a3cf13225d3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-01 12:04:44', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e78f8832d61c1603c17767ee2b78ef07', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-01 19:50:03', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0475b4445d5f58f29212258c1644f339', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-01 20:20:23', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2b5a76869a7d1900487cd220de378dba', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-05 16:32:00', NULL, NULL);
INSERT INTO `sys_log` VALUES ('21b8493a05050584d9bb06cfc2a05a6b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-05 17:29:52', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a61d9db83888d42b0d24621de48a880d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-05 22:49:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('097be3e8fdf77a245f5c85884e97b88c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-05 22:52:45', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7b2b322a47e1ce131d71c50b46d7d29e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-06 15:55:20', NULL, NULL);
INSERT INTO `sys_log` VALUES ('cedf399271592c27dcb6f6ce3312e77d', 2, '删除测试DEMO', NULL, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.delete()', NULL, '[\"7501\"]', NULL, 24, 'admin', '2019-03-06 16:03:13', NULL, NULL);
INSERT INTO `sys_log` VALUES ('efe77038e00cfff98d6931c3e7a4c3d6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-06 16:20:19', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ffac84fff3c65bb17aa1bda3a0d2029e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-06 20:10:50', NULL, NULL);
INSERT INTO `sys_log` VALUES ('45819fe1b96af820575a12e9f973014e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-07 09:19:22', NULL, NULL);
INSERT INTO `sys_log` VALUES ('87885bc889d23c7c208614da8e021fb0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-07 10:23:22', NULL, NULL);
INSERT INTO `sys_log` VALUES ('54c2bad38dafd9e636ce992aa93b26af', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-07 11:57:14', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d3c4f120d8a23b62ec9e24b431a58496', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-07 14:17:24', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d01d658731dac4b580a879d986b03456', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-07 15:00:37', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ab550d09101a88bc999ea57cbb05aa5a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-07 17:59:59', NULL, NULL);
INSERT INTO `sys_log` VALUES ('aeb738ab880c262772453d35fc98f2f2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-07 18:50:30', NULL, NULL);
INSERT INTO `sys_log` VALUES ('375aadb2833e57a0d5a2ce0546a65ca4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-07 20:38:52', NULL, NULL);
INSERT INTO `sys_log` VALUES ('96d7fe922f46123e0497e22dedf89328', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-07 23:10:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('636d37d423199e15b4030f35c60859fe', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-08 10:07:21', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a7d1f4a774eb8644e2b1d37ca5f93641', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-08 10:16:03', NULL, NULL);
INSERT INTO `sys_log` VALUES ('017e9596f489951f1cc7d978085adc00', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-08 10:58:00', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0b42292a532c796495a34d8d9c633afa', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-08 12:58:03', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b428718441be738cf8b5ce92109068c3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-08 16:21:37', NULL, NULL);
INSERT INTO `sys_log` VALUES ('89d2bc84e056f327291c53821d421034', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-08 16:57:46', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e09bb0a74c268a9aaf1f94edcc2eb65a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-08 18:26:14', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0dc22e52c9173e4e880728bc7734ff65', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 11:14:47', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5358b182eab53a79eec236a9cee1e0fc', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 13:01:21', NULL, NULL);
INSERT INTO `sys_log` VALUES ('23176e4b29c3d2f3abadd99ebeffa347', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 16:37:50', NULL, NULL);
INSERT INTO `sys_log` VALUES ('703fbcb7e198e8e64978ec0518971420', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 17:53:55', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9e9d01c430b72703ce3a94589be54bbe', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 18:26:06', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ef54197116da89bf091c0ed58321eea4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 19:22:06', NULL, NULL);
INSERT INTO `sys_log` VALUES ('111156480d4d18ebf40427083f25830f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.104', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 19:48:58', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a9bd713f975bfbff87638432a104b715', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.104', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 20:04:44', NULL, NULL);
INSERT INTO `sys_log` VALUES ('06fbb85b34f518cd211b948552de72f8', 1, '登录失败，用户名:null不存在！', NULL, NULL, NULL, '192.168.1.104', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 20:08:38', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9b568a868e57f24c5ba146848061613f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.104', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 20:09:15', NULL, NULL);
INSERT INTO `sys_log` VALUES ('02026841bf8a9204db2c500c86a4a9be', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.104', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 20:44:58', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c1a68605bee6b3d1264390c1cfe7a9fa', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 20:49:55', NULL, NULL);
INSERT INTO `sys_log` VALUES ('cbd720f20fc090c7350a98be0738816a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.104', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 20:55:19', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2676be4ffc66f83221fd95e23d494827', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.104', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 21:31:28', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e9d3202c14f7f2812346fb4c2b781c67', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.104', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 21:38:36', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d9e0150666b69cced93eb4defb19788b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 23:11:12', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1f0b36f7e021aa5d059ffb0a74ef6de4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-09 23:11:25', NULL, NULL);
INSERT INTO `sys_log` VALUES ('326b2df4ab05a8dbb03a0a0087e82a25', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-10 11:53:20', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7ae9cad197aee3d50e93bc3a242d68ec', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-10 13:12:26', NULL, NULL);
INSERT INTO `sys_log` VALUES ('78caf9e97aedfb8c7feef0fc8fdb4fb5', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-10 17:04:46', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d00964eee24c6f9a8609a42eeebef957', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-10 17:04:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('04f97d7f906c1e97384a94f3728606a4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 12:08:51', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2b433e88db411bef115bc9357ba6a78b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.105', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 12:09:36', NULL, NULL);
INSERT INTO `sys_log` VALUES ('63ccf8dda5d9bf825ecdbfb9ff9f456c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.105', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 12:14:08', NULL, NULL);
INSERT INTO `sys_log` VALUES ('404d5fb6cce1001c3553a69089a618c8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 12:29:12', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9ed114408a130e69c0de4c91b2d6bf7e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 13:03:40', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2eb964935df6f3a4d2f3af6ac5f2ded1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.200', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 13:27:18', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e864c0007983211026d6987bd0cd4dc8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.114', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 13:37:08', NULL, NULL);
INSERT INTO `sys_log` VALUES ('8b2ad448021fbb5509ea04c9a780b165', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.104', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 14:35:25', NULL, NULL);
INSERT INTO `sys_log` VALUES ('69a9dfb2fb02e4537b86c9c5c05184ae', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.104', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 15:22:14', NULL, NULL);
INSERT INTO `sys_log` VALUES ('98b7fc431e4654f403e27ec9af845c7b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 17:31:38', NULL, NULL);
INSERT INTO `sys_log` VALUES ('42bf42af90d4df949ad0a6cd1b39805e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.200', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 17:39:01', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e234abc35a52f0dd2512b0ce2ea0e4f2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-11 20:05:26', NULL, NULL);
INSERT INTO `sys_log` VALUES ('69baa4f883fe881f401ea063ddfd0079', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-12 20:51:03', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a867c282a8d97f7758235f881804bb48', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-13 18:28:20', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1aa593c64062f0137c0691eabac07521', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-14 10:45:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('de978382f59685babf3684d1c090d136', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-14 12:55:55', NULL, NULL);
INSERT INTO `sys_log` VALUES ('75c7fa1a7d3639be1b112e263561e43a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-14 17:07:59', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7a9d307d22fb2301d6a9396094afc82f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-14 18:45:04', NULL, NULL);
INSERT INTO `sys_log` VALUES ('28dbc8d16f98fb4b1f481462fcaba48b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-14 20:56:57', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f1186792c6584729a0f6da4432d951f9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-14 21:45:52', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4f31f3ebaf5d1a159d2bb11dd9984909', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-15 11:14:15', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9a5c1fbf3543880af6461182e24b75db', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-15 13:51:27', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b86958d773b2c2bd79baa2e8c3c84050', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-15 16:49:36', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a052befb699ee69b3197b139fd9263f0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-15 17:34:58', NULL, NULL);
INSERT INTO `sys_log` VALUES ('6836a652dc96246c028577e510695c6f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-15 20:47:02', NULL, NULL);
INSERT INTO `sys_log` VALUES ('8fe913a5b037943c6667ee4908f88bea', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-16 11:18:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9410b7974fbc9df415867095b210e572', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-16 11:18:58', NULL, NULL);
INSERT INTO `sys_log` VALUES ('98d4b573769af6d9c10cd5c509bfb7af', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-16 11:21:25', NULL, NULL);
INSERT INTO `sys_log` VALUES ('18b16a451fec0fe7bf491ab348c65e30', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-16 11:55:45', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0d85728028ed67da696137c0e82ab2f6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-16 12:58:43', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4aa770f37a7de0039ba0f720c5246486', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 17:26:12', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2fecb508d344c5b3a40f471d7b110f14', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 17:36:53', NULL, NULL);
INSERT INTO `sys_log` VALUES ('61aac4cfe67ec6437cd901f95fbd6f45', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 17:40:21', NULL, NULL);
INSERT INTO `sys_log` VALUES ('62e208389a400e37250cfa51c204bdc8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 17:44:49', NULL, NULL);
INSERT INTO `sys_log` VALUES ('eb9a522fd947c7a706c5a106ca32b8c9', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 17:50:17', NULL, NULL);
INSERT INTO `sys_log` VALUES ('bd9167a87aee4574a30d67825acaad0a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 17:51:03', NULL, NULL);
INSERT INTO `sys_log` VALUES ('49f1ec54eb16af2001ff6809a089e940', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 17:59:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('bdfd95b4d4c271d7d8d38f89f4a55da9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 17:59:12', NULL, NULL);
INSERT INTO `sys_log` VALUES ('95063e0bdfa5c9817cc0f66e96baad93', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 17:59:16', NULL, NULL);
INSERT INTO `sys_log` VALUES ('30da94dd068a5a57f3cece2ca5ac1a25', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 18:01:43', NULL, NULL);
INSERT INTO `sys_log` VALUES ('8fde5f89e8ad30cf3811b8683a9a77b1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 18:02:41', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2ebe7f0432f01788d69d39bc6df04a1a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 18:05:09', NULL, NULL);
INSERT INTO `sys_log` VALUES ('beb9ef68b586f05bd7cf43058e01ad4a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-17 22:29:02', NULL, NULL);
INSERT INTO `sys_log` VALUES ('befbcf5a27ef8d2ca8e6234077f9413d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 16:01:33', NULL, NULL);
INSERT INTO `sys_log` VALUES ('378b44af9c1042c1438450b11c707fcf', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 16:07:39', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0571e5730ee624d0dd1b095ad7101738', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 16:10:50', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3ec2023daa4a7d6a542bf28b11acf586', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 16:18:20', NULL, NULL);
INSERT INTO `sys_log` VALUES ('64c00f27ddc93fda22f91b38d2b828b5', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 17:34:45', NULL, NULL);
INSERT INTO `sys_log` VALUES ('21bad1470a40da8336294ca7330f443d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 17:35:32', NULL, NULL);
INSERT INTO `sys_log` VALUES ('72ee87d0637fb3365fdff9ccbf286c4a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 17:36:44', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d8c43edd685431ab3ef7b867efc29214', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 17:37:18', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0ad51ba59da2c8763a4e6ed6e0a292b2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 17:37:53', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d916bd1d956418e569549ee1c7220576', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 19:18:42', NULL, NULL);
INSERT INTO `sys_log` VALUES ('917dbb5db85d1a6f142135827e259bbf', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 20:21:03', NULL, NULL);
INSERT INTO `sys_log` VALUES ('db2b518e7086a0561f936d327a0ab522', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 22:39:23', NULL, NULL);
INSERT INTO `sys_log` VALUES ('61d2d2fd3e9e23f67c23b893a1ae1e72', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 22:44:56', NULL, NULL);
INSERT INTO `sys_log` VALUES ('671a44fd91bf267549d407e0c2a680ee', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 22:45:16', NULL, NULL);
INSERT INTO `sys_log` VALUES ('586e8244eff6d6761077ef15ab9a82d9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-18 23:03:51', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2b4d33d9be98e1e4cdd408a55f731050', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 10:32:00', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3267222d9387284b864792531b450bfe', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 10:33:23', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a28de45f52c027a3348a557efab6f430', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 10:34:26', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9db7e7d214dbe9fe8fff5ff20634e282', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 11:19:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('74209dfc97285eb7919868545fc2c649', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 11:23:08', NULL, NULL);
INSERT INTO `sys_log` VALUES ('49d48fda33126595f6936a5d64e47af0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 13:17:51', NULL, NULL);
INSERT INTO `sys_log` VALUES ('fe0dc06eaef69047131f39052fcce5c4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 13:56:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f540eff3f6e86c1e0beccd300efd357f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 15:15:26', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3fd0d771bbdd34fae8b48690ddd57799', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 17:17:22', NULL, NULL);
INSERT INTO `sys_log` VALUES ('27e8812c9a16889f14935eecacf188eb', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 18:52:19', NULL, NULL);
INSERT INTO `sys_log` VALUES ('88bab180edf685549c7344ec8db7d954', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 19:07:06', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ed9b4ffc8afab10732aac2d0f84c567b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-19 19:10:52', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ad97829fe7fefcd38c80d1eb1328e40f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 09:28:18', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3d25a4cdd75b9c4c137394ce68e67154', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 09:59:31', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5c7e834e089ef86555d8c2627b1b29b5', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 11:25:26', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b3adf055f54878657611ef430f85803e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 11:33:57', NULL, NULL);
INSERT INTO `sys_log` VALUES ('8d105ea6c89691bc8ee7d4fd568aa690', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 11:39:33', NULL, NULL);
INSERT INTO `sys_log` VALUES ('445436e800d306ec1d7763c0fe28ad38', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 11:43:00', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7f9c3d539030049a39756208670be394', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 11:44:40', NULL, NULL);
INSERT INTO `sys_log` VALUES ('feaf7c377abc5824c1757d280dd3c164', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 11:58:54', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c72bb25acd132303788699834ae039b4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 12:06:06', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e4c06405615399d6b1ebea45c8112b4d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 12:07:26', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f95d517f43ba2229c80c14c1883a4ee9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 12:11:59', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d18bff297a5c2fa54d708f25a7d790d6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 12:13:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b5f6636c6e24e559ddf1feb3e1a77fd5', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 12:14:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('aeca30df24ce26f008a7e2101f7c513c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 12:27:53', NULL, NULL);
INSERT INTO `sys_log` VALUES ('cd7a7c49e02ca9613b6879fda4e563cf', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 12:29:08', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a7ee4b4c236bc0e8f56db5fdf1e5ac38', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 13:21:36', NULL, NULL);
INSERT INTO `sys_log` VALUES ('6d45672f99bbfd01d6385153e9c3ad91', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 13:49:20', NULL, NULL);
INSERT INTO `sys_log` VALUES ('905d2cf4308f70a3a2121a3476e38ed0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 14:00:30', NULL, NULL);
INSERT INTO `sys_log` VALUES ('27d23027dc320175d22391d06f50082f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 15:50:06', NULL, NULL);
INSERT INTO `sys_log` VALUES ('52fde989fb8bb78d03fb9c14242f5613', 1, '用户名: admin,登录成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-20 17:04:09', NULL, NULL);
INSERT INTO `sys_log` VALUES ('952947331f8f3379494c4742be797fc3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-20 18:42:11', NULL, NULL);
INSERT INTO `sys_log` VALUES ('39caf3d5d308001aeb0a18e15ae480b9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 10:31:07', NULL, NULL);
INSERT INTO `sys_log` VALUES ('772f238d46531a75fff31bae5841057c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 11:31:16', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f79af48e6aeb150432640483f3bb7f2a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:09:11', NULL, NULL);
INSERT INTO `sys_log` VALUES ('20fc3263762c80ab9268ddd3d4b06500', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:36:44', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e8b37ad67ef15925352a4ac3342cef07', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:38:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d6aaf0f8e2428bf3c957becbf4bcedb4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:38:14', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3bc73699a9fd3245b87336787422729b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:41:07', NULL, NULL);
INSERT INTO `sys_log` VALUES ('862aa0e6e101a794715174eef96f7847', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:41:09', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a6209166e1e9b224cca09de1e9ea1ed7', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:41:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b954f7c34dfbe9f6a1fc12244e0a7d59', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:41:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('64711edfb8c4eb24517d86baca005c96', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:41:11', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0efc9df0d52c65ec318e7b46db21655f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:42:47', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c03985d6e038b5d8ebdeec27fce249ba', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:43:24', NULL, NULL);
INSERT INTO `sys_log` VALUES ('69e6fd7891d4b42b0cccdc0874a43752', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:45:58', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9b23981621d5265a55681883ec19fa91', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:46:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('37ca8ff7098b9d118adb0a586bdc0d13', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:46:07', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ea5f9191b0f593a1d6cb585538caa815', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:46:08', NULL, NULL);
INSERT INTO `sys_log` VALUES ('8e03def9e0283005161d062d4c0a5a80', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:46:09', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f78e24f5e841acac2a720f46f6c554bc', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:47:17', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f93279c6899dc5e6cec975906f8bf811', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:47:20', NULL, NULL);
INSERT INTO `sys_log` VALUES ('cd5af66a87bb40026c72a748155b47e8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:47:26', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5902fb4ba61ccf7ff4d2dd97072b7e5b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:48:30', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e1d1fc464cf48ec26b7412585bdded1a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 13:49:15', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5ea258e1f478d27e0879e2f4bcb89ecd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 14:01:59', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2e44c368eda5a7f7a23305b61d82cddb', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 18:14:23', NULL, NULL);
INSERT INTO `sys_log` VALUES ('34a6b86424857a63159f0e8254e238c2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 18:22:08', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ffc6178ffa099bb90b9a4d0a64dae42b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 18:28:32', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3612f8d40add5a7754ea3d54de0b5f20', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 19:59:59', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7a511b225189342b778647db3db385cd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 20:50:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b7085f003b4336af4d4ba18147f8e5ae', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 22:29:37', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c66e22782dd3916d1361c76b0cc4ec8a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-21 22:44:06', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c6cbe54fcb194d025a081e5f91a7e3f0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 10:26:38', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1ab7c74d217152081f4fa59e4a56cc7b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 12:03:39', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5f00b5514a11cd2fe240c131e9ddd136', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 16:30:52', NULL, NULL);
INSERT INTO `sys_log` VALUES ('82cee1c403025fc1db514c60fc7d8d29', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 16:41:50', NULL, NULL);
INSERT INTO `sys_log` VALUES ('af5869701738a6f4c2c58fe8dfe02726', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 16:42:40', NULL, NULL);
INSERT INTO `sys_log` VALUES ('233e39d8b7aa90459ebef23587c25448', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 17:38:36', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b0cebd174565a88bb850a2475ce14625', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 18:19:39', NULL, NULL);
INSERT INTO `sys_log` VALUES ('26529d5753ceebbd0d774542ec83a43e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 20:17:41', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f3e1f7fb81004ccd64df12d94ef1e695', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 21:30:37', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a2e0435673b17f4fb848eecdf8ecacd6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 21:32:37', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5323f848cddbb80ba4f0d19c0580eba9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 22:58:40', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5858f2f8436460a94a517904c0bfcacb', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-22 23:42:21', NULL, NULL);
INSERT INTO `sys_log` VALUES ('8d9ce65020320d46882be43b22b12a62', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 10:56:43', NULL, NULL);
INSERT INTO `sys_log` VALUES ('11802c7a3644af411bc4e085553cfd4f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 14:46:35', NULL, NULL);
INSERT INTO `sys_log` VALUES ('fc69a1640a4772c8edf2548d053fa6de', 1, '用户名: admin,登录成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-23 14:55:33', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e3031f999984909f9048d8ec15543ad0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 18:43:02', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f43e38800d779422c75075448af738d1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 18:47:11', NULL, NULL);
INSERT INTO `sys_log` VALUES ('dcfe23b155d5c6fa9a302c063b19451e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 18:47:21', NULL, NULL);
INSERT INTO `sys_log` VALUES ('802cec0efbe9d862b7cea29fefc5448b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 18:58:50', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f58e160e97d13a851f59b70bf54e0d06', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 20:11:58', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b8bd2a9de3fb917dfb6b435e58389901', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 20:13:31', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e01ed1516e8ae3a2180acbd4e4508fa5', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 20:28:12', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b7f33b5a514045878447fc64636ac3e6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 22:00:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d0ce9bfc790a573d48d49d3bbbf1a1cb', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 22:09:06', NULL, NULL);
INSERT INTO `sys_log` VALUES ('74c991568d8bcb2049a0dbff53f72875', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 22:12:15', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0251bbee51c28f83459f4a57eeb61777', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 22:14:27', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a5848ab4e8d0fb6ecf71ee1d99165468', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 22:14:50', NULL, NULL);
INSERT INTO `sys_log` VALUES ('79a1737fcc199c8262f344e48afb000d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-23 23:25:25', NULL, NULL);
INSERT INTO `sys_log` VALUES ('6cfeaf6a6be5bb993b9578667999c354', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-24 11:43:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c5d4597b38275dcb890c6568a7c113f2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-24 12:18:46', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0f173ec7e8819358819aa14aafc724c0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 11:15:58', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ce9893f4d0dd163e900fcd537f2c292d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 11:55:55', NULL, NULL);
INSERT INTO `sys_log` VALUES ('90711ddb861e28bd8774631c98f3edb9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 11:57:42', NULL, NULL);
INSERT INTO `sys_log` VALUES ('fb73d58bf6503270025972f99e50335d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 11:57:56', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3a290289b4b30a1caaac2d03ad3161cd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 11:58:13', NULL, NULL);
INSERT INTO `sys_log` VALUES ('716f9f5f066a6f75a58b7b05f2f7f861', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 11:59:01', NULL, NULL);
INSERT INTO `sys_log` VALUES ('151a9f1b01e4e749124d274313cd138c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 11:59:17', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0ef3e7ae8c073a7e3bdd736068f86c84', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:02:27', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d7e7cb4c21372e48b8e0ec7e679466e3', 1, '用户名: null,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:02:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('15b9599cb02b49a62fb4a1a71ccebc18', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:02:50', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ecfee5b948602a274093b8890e5e7f3f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:05:11', NULL, NULL);
INSERT INTO `sys_log` VALUES ('cbf83d11486a8d57814ae38c9822b022', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:05:39', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f2ce8024e62740f63c134c3cfb3cae23', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:07:41', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c665d704539483630cc9ed5715ed57a8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:10:12', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e93f1a170e3cd33f90dd132540c7a39b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:12:43', NULL, NULL);
INSERT INTO `sys_log` VALUES ('fded8eb5d78d13791baec769019fee54', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:15:07', NULL, NULL);
INSERT INTO `sys_log` VALUES ('03ec66b6b6d17c007ec2f918efe5b898', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:16:03', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5e8bac7831de49146d568c9a8477ddad', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:16:37', NULL, NULL);
INSERT INTO `sys_log` VALUES ('cd064a2f6cb6c640cb97a74aaa6041d7', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:17:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a521d9f2a0087daa37923fa704dea85b', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-25 12:45:52', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4816854636129e31c2a5f9d38af842ef', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-25 12:45:54', NULL, NULL);
INSERT INTO `sys_log` VALUES ('90b4bad7939233a1e0d7935f079ea0fa', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-25 12:45:54', NULL, NULL);
INSERT INTO `sys_log` VALUES ('aec0817ecc0063bde76c1f6b6889d117', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:47:06', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e169938510c9320cb1495ddb9aabb9d1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:47:40', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b7478d917ab6f663e03d458f0bb022a3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:50:55', NULL, NULL);
INSERT INTO `sys_log` VALUES ('642e48f2e5ac8fe64f1bfacf4d234dc8', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-25 12:51:21', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ed2740de487c684be9fa3cf72113ae30', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:51:43', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b1e9797721dbfcc51bbe7182142cbdcd', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-25 12:52:25', NULL, NULL);
INSERT INTO `sys_log` VALUES ('6a67bf2ff924548dee04aa97e1d64d38', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:52:41', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a56661bbc72b8586778513c71f4764f5', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:53:09', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ae61be664d2f30d4f2248347c5998a45', 1, '用户名: jeecg,退出成功！', NULL, 'jeecg', 'jeecg', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-03-25 12:53:17', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4ab79469ba556fa890258a532623d1dc', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:54:20', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3a0330033a8d3b51ffbfb2e0a7db9bba', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:54:27', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b972484d206b36420efac466fae1c53f', 1, '用户名: jeecg,退出成功！', NULL, 'jeecg', 'jeecg', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-03-25 12:54:38', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7e92abdc0c1f54596df499a5a2d11683', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 12:59:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('fa9b4d7d42bc9d1ba058455b4afedbfb', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-25 12:59:46', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1d970c0e396ffc869e3a723d51f88b46', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 13:01:17', NULL, NULL);
INSERT INTO `sys_log` VALUES ('88d7136ed5c7630057451816dbaff183', 1, '用户名: jeecg,退出成功！', NULL, 'jeecg', 'jeecg', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-03-25 13:01:24', NULL, NULL);
INSERT INTO `sys_log` VALUES ('48eac0dd1c11fe8f0cb49f1bd14529c2', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 13:01:31', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a8c7ba2d11315b171940def2cbeb0e8f', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-25 13:01:40', NULL, NULL);
INSERT INTO `sys_log` VALUES ('20fea778f4e1ac5c01b5a5a58e3805be', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 19:01:49', NULL, NULL);
INSERT INTO `sys_log` VALUES ('48e5faf2d21ead650422dc2eaf1bb6c5', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 22:08:09', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f74f759b43afa639fd1c4f215c984ae0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-25 22:08:18', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d82b170459d99fc05eb8aa1774e1a1c9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-26 18:45:14', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e088a2607864d3e6aadf239874d51756', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-26 18:46:56', NULL, NULL);
INSERT INTO `sys_log` VALUES ('95d906e6f048c3e71ddbcc0c9448cf49', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-26 19:23:30', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3767186b722b7fefd465e147d3170ad1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-26 21:57:19', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f21e30d73c337ea913849ed65808525c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-27 10:23:22', NULL, NULL);
INSERT INTO `sys_log` VALUES ('189e3428e35e27dfe92ece2848b10ba8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-27 15:52:21', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e2b6d0e751f130d35c0c3b8c6bd2a77e', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-27 16:18:40', NULL, NULL);
INSERT INTO `sys_log` VALUES ('586002e1fb4e60902735070bab48afe3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-27 16:18:52', NULL, NULL);
INSERT INTO `sys_log` VALUES ('611fa74c70bd5a7a8af376464a2133e8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-27 17:48:13', NULL, NULL);
INSERT INTO `sys_log` VALUES ('90555a39c0b02180df74752e4d33f253', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-27 18:26:25', NULL, NULL);
INSERT INTO `sys_log` VALUES ('217aa2f713b0903e6be699136e374012', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-27 20:07:32', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5554869b3475770046602061775e0e57', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 14:38:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('bfec8c8c88868391041667d924e3af7f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 14:38:27', NULL, NULL);
INSERT INTO `sys_log` VALUES ('675153568c479d8b7c6fe63327066c9f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 15:29:42', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4930e32672465979adbc592e116226a6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 16:53:28', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9a1456ef58a2b1fb63cdc54b723f2539', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 17:26:39', NULL, NULL);
INSERT INTO `sys_log` VALUES ('484cdb8db40e3f76ef686552f57d8099', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 18:14:49', NULL, NULL);
INSERT INTO `sys_log` VALUES ('02d4447c9d97ac4fc1c3a9a4c789c2a8', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-28 18:24:18', NULL, NULL);
INSERT INTO `sys_log` VALUES ('59558082e1b1d754fa3def125ed4db3c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 18:24:19', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c434dc5172dc993ee7cd96187ca58653', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-28 19:46:08', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a6261bbbf8e964324935722ea1384a5d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 19:46:27', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f0748a25728348591c7b73a66f273457', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-28 19:46:27', NULL, NULL);
INSERT INTO `sys_log` VALUES ('14f447d9b60725cc86b3100a5cb20b75', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 19:46:30', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7c88e9cf6018a1b97b420b8cb6122815', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-28 19:46:30', NULL, NULL);
INSERT INTO `sys_log` VALUES ('cb7c6178101ef049d3f1820ee41df539', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 19:59:19', NULL, NULL);
INSERT INTO `sys_log` VALUES ('21fed0f2d080e04cf0901436721a77a6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-28 21:53:31', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4ba055970859a6f1afcc01227cb82a2d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-29 09:43:56', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3ba1e54aa9aa760b59dfe1d1259459bc', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-29 09:44:07', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7b44138c1b80b67da13b89db756a22b0', 2, '添加测试DEMO', NULL, NULL, NULL, '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', NULL, '[{\"createBy\":\"jeecg-boot\",\"createTime\":1553824172062,\"id\":\"5fce01cb7f0457746c97d8ca05628f81\",\"name\":\"1212\"}]', NULL, 25, 'jeecg-boot', '2019-03-29 09:49:32', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d7e8a7f14967c70d68f5569cb4d11d0a', 2, '删除测试DEMO', NULL, NULL, NULL, '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.delete()', NULL, '[\"5fce01cb7f0457746c97d8ca05628f81\"]', NULL, 9, 'jeecg-boot', '2019-03-29 09:49:39', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e7f2b0a7493e7858c5db1f1595fa54b1', 2, '添加测试DEMO', NULL, NULL, NULL, '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', NULL, '[{\"createBy\":\"jeecg-boot\",\"createTime\":1553824376817,\"id\":\"e771211b77cd3b326d3e61edfd9a5a19\",\"keyWord\":\"222\",\"name\":\"222\"}]', NULL, 7, 'jeecg-boot', '2019-03-29 09:52:56', NULL, NULL);
INSERT INTO `sys_log` VALUES ('997bb4cb1ad24439b6f7656222af0710', 2, '添加测试DEMO', NULL, NULL, NULL, '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', NULL, '[{\"createBy\":\"jeecg-boot\",\"createTime\":1553824768819,\"id\":\"ee84471f0dff5ae88c45e852bfa0280f\",\"keyWord\":\"22\",\"name\":\"222\"}]', NULL, 5, 'jeecg-boot', '2019-03-29 09:59:28', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a6c3b28530416dace21371abe8cae00b', 2, '删除测试DEMO', NULL, NULL, NULL, '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.delete()', NULL, '[\"ee84471f0dff5ae88c45e852bfa0280f\"]', NULL, 9, 'jeecg-boot', '2019-03-29 09:59:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d2fe98d661f1651b639bf74499f124db', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-29 10:16:29', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2186244ae450e83d1487aa01fbeae664', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-29 14:47:43', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a5daa58b078cb8b3653af869aeecebd0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-29 17:14:31', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f29f3b7b7e14b1389a0c53d263c0b26b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-29 17:44:25', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2659c59136fb1a284ab0642361b10cdd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-29 18:40:22', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a42e5cd05566ea226c2e2fc201860f2c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 11:15:50', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f06048c147c5bcdbed672e32b2c86b1c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 14:07:28', NULL, NULL);
INSERT INTO `sys_log` VALUES ('13c83c56a0de8a702aeb2aa0c330e42c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 14:53:52', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ab1d707bbfdf44aa17307d30ca872403', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 15:50:42', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5d8ed15778aa7d99224ee62c606589fb', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-30 15:51:02', NULL, NULL);
INSERT INTO `sys_log` VALUES ('42aef93749cc6222d5debe3fb31ba41b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 15:51:04', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5c04e3d9429e3bcff4d55f6205c4aa83', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 18:14:29', NULL, NULL);
INSERT INTO `sys_log` VALUES ('af8fe96a9f0b325e4833fc0d9c4721bf', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-30 18:14:56', NULL, NULL);
INSERT INTO `sys_log` VALUES ('21910e350c9083e107d39ff4278f51d6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 18:14:59', NULL, NULL);
INSERT INTO `sys_log` VALUES ('636309eec5e750bc94ce06fb98526fb2', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-03-30 18:15:03', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9d0416e09fae7aeeeefc8511a61650c2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 18:15:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2e63fd1b3b6a6145bc04b2a1df18d2f5', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 19:01:33', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2966ed2bdf67c9f3306b058d13bef301', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 21:25:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3683743d1936d06f3aaa03d6470e5178', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-30 22:40:12', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0ba24c5f61ff53f93134cf932dd486db', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-31 21:06:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('bbd3e1f27e025502a67cf54945b0b269', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-03-31 22:13:16', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1f8f46118336b2cacf854c1abf8ae144', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 11:02:59', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ac8cf22c2f10a38c7a631fc590551c40', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 12:04:16', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7d11535270734de80bd52ec0daa4fc1f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.1.105', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 12:20:14', NULL, NULL);
INSERT INTO `sys_log` VALUES ('6b4cdd499885ccba43b40f10abf64a78', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 13:04:43', NULL, NULL);
INSERT INTO `sys_log` VALUES ('77a329e5eb85754075165b06b7d877fd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 13:25:17', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0e754ee377033067f7b2f10b56b8784c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 17:17:45', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9b7a830914668881335da1b0ce2274b1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 17:19:02', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a1b870eee811cfa4960f577b667b0973', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 18:23:44', NULL, NULL);
INSERT INTO `sys_log` VALUES ('85b3106d757d136b48172a9ab1f35bb6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 18:34:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7f31435ca2f5a4ef998a4152b2433dec', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 18:36:40', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f20cf3fe228ba6196a48015b98d0d354', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 19:25:22', NULL, NULL);
INSERT INTO `sys_log` VALUES ('65771bce3f5786dfb4d84570df61a47a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 22:07:57', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c98a6367b152cf5311d0eec98fab390c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 22:13:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('93b4d26f60d7fb45a60524760bf053e4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 22:20:06', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3087ac4988a961fa1ec0b4713615c719', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-01 22:54:24', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a69f4ff4e48754de96ae6fa4fabc1579', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 09:18:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a63147887c6ca54ce31f6c9e6279a714', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 09:19:07', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a2950ae3b86f786a6a6c1ce996823b53', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 09:47:11', NULL, NULL);
INSERT INTO `sys_log` VALUES ('615625178b01fc20c60184cd28e64a70', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 09:47:13', NULL, NULL);
INSERT INTO `sys_log` VALUES ('89fbc93e77defb34c609c84a7fe83039', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 09:47:14', NULL, NULL);
INSERT INTO `sys_log` VALUES ('432067d777447423f1ce3db11a273f6f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 09:47:17', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7d8539ff876aad698fba235a1c467fb8', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 09:47:18', NULL, NULL);
INSERT INTO `sys_log` VALUES ('689b8f2110f99c52e18268cbaf05bbb6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 09:58:44', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2919d2f18db064978a619707bde4d613', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 09:58:45', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0dc6d04b99e76ad400eef1ded2d3d97c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 09:59:45', NULL, NULL);
INSERT INTO `sys_log` VALUES ('45f0309632984f5f7c70b3d40dbafe8b', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 09:59:46', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d869534109332e770c70fad65ef37998', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 10:02:30', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c21422fa08f8480a53367fda7ddddf12', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 10:02:30', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1e4533a02fb9c739a3555fa7be6e7899', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 10:04:56', NULL, NULL);
INSERT INTO `sys_log` VALUES ('03c0ab177bd7d840b778713b37daf86f', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 10:04:57', NULL, NULL);
INSERT INTO `sys_log` VALUES ('60886d5de8a18935824faf8b0bed489e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 10:11:35', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4218b30015501ee966548c139c14f43f', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 10:11:35', NULL, NULL);
INSERT INTO `sys_log` VALUES ('de938485a45097d1bf3fa311d0216ed4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 10:15:22', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e1d0b1fd3be59e465b740e32346e85b0', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 10:16:37', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4234117751af62ac87343cbf8a6f1e0f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 10:17:17', NULL, NULL);
INSERT INTO `sys_log` VALUES ('845f732f6a0f0d575debc4103e92bea2', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 10:17:18', NULL, NULL);
INSERT INTO `sys_log` VALUES ('cc39057ae0a8a996fb0b3a8ad5b8f341', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 10:20:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('155d2991204d541388d837d1457e56ab', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 11:32:57', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2312c2693d6b50ca06799fee0ad2554a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 12:11:32', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d98115c02c0ac478a16d6c35de35053d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 12:50:09', NULL, NULL);
INSERT INTO `sys_log` VALUES ('55e906361eeabb6ec16d66c7196a06f0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 12:50:20', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3836dc3f91d072e838092bc8d3143906', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 12:50:32', NULL, NULL);
INSERT INTO `sys_log` VALUES ('add13f513772a63f8ca8bf85634bb72c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 13:09:03', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a6971e63e3d9158020e0186cda81467d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 14:59:23', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2eb75cb6ca5bc60241e01fa7471c0ccf', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 18:34:04', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3e69108be63179550afe424330a8a9e4', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-02 18:38:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('6c558d70dc5794f9f473d8826485727a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 18:38:44', NULL, NULL);
INSERT INTO `sys_log` VALUES ('687810e7fea7e480962c58db515a5e1c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 18:42:30', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d23e6766cecf911fb2e593eeee354e18', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 18:42:35', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0819ea9729ddf70f64ace59370a62cf1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 18:59:43', NULL, NULL);
INSERT INTO `sys_log` VALUES ('939b3ff4733247a47efe1352157b1f27', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 19:01:06', NULL, NULL);
INSERT INTO `sys_log` VALUES ('6cf638853ef5384bf81ed84572a6445d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-02 19:25:24', NULL, NULL);
INSERT INTO `sys_log` VALUES ('bc28d4275c7c7fcd067e1aef40ec1dd4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 10:53:03', NULL, NULL);
INSERT INTO `sys_log` VALUES ('43536edd8aa99f9b120872e2c768206c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 10:53:26', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7268539fbe77c5cc572fb46d71d838f1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 13:22:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f0409312093beb563ac4016f2b2c6dfd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 13:24:59', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a710ed2de7e31fd72b1efb1b54ba5a87', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 15:30:43', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b01c3f89bcfd263de7cb1a9b0210a7af', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 17:53:55', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e1fa52ecbcc0970622cc5a0c06de9317', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-03 18:33:04', NULL, NULL);
INSERT INTO `sys_log` VALUES ('790b722fa99a8f3a0bc38f61e13c1cf4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 18:34:07', NULL, NULL);
INSERT INTO `sys_log` VALUES ('20e5887d0c9c7981159fe91a51961141', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 20:12:07', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ce6aa822166b97a78b0bbea62366f8e0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 20:14:29', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3e6116220fa8d4808175738c6de51b12', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 21:04:46', NULL, NULL);
INSERT INTO `sys_log` VALUES ('10a434c326e39b1d046defddc8c57f4a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 21:18:29', NULL, NULL);
INSERT INTO `sys_log` VALUES ('317e3ae1b6ccdfb5db6940789e12d300', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-03 21:44:31', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2b801129457c05d23653ecaca88f1711', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-03 21:44:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7a99cf653439ca82ac3b0d189ddaad4a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 10:41:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('68e90e08a866de748e9901e923406959', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 12:37:06', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2942a12521ac8e3d441429e6c4b04207', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 14:12:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('dfacaa7c01ccf0bade680044cced3f11', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 15:25:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f3cafb545e5693e446f641fa0b5ac8cd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 17:07:56', NULL, NULL);
INSERT INTO `sys_log` VALUES ('060d541a9571ca2b0d24790a98d170a6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 19:28:04', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9df97c1b3213aa64eda81c6bf818b02b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 22:42:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('43079866b75ee6a031835795bb681e16', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-04 22:44:16', NULL, NULL);
INSERT INTO `sys_log` VALUES ('55d649432efa7eaecd750b4b6b883f83', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 22:44:19', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ca737885d9034f71f70c4ae7986fafa8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 22:47:28', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1534f0c50e67c5682e91af5160a67a80', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 22:47:28', NULL, NULL);
INSERT INTO `sys_log` VALUES ('93bb98ba996dacebfb4f61503067352e', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 22:47:28', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5c48703e3a2d4f81ee5227f0e2245990', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-04 23:12:04', NULL, NULL);
INSERT INTO `sys_log` VALUES ('70849167f54fd50d8906647176d90fdf', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 23:12:29', NULL, NULL);
INSERT INTO `sys_log` VALUES ('310bb368795f4985ed4eada030a435a0', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-04 23:22:20', NULL, NULL);
INSERT INTO `sys_log` VALUES ('477592ab95cd219a2ccad79de2f69f51', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-05 10:38:24', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e39f051ba6fdb7447f975421f3b090a7', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-05 12:49:18', NULL, NULL);
INSERT INTO `sys_log` VALUES ('4d1be4b4991a5c2d4d17d0275e4209cf', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-05 20:47:21', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9eb3fb6d9d45e3847a88f65ed47da935', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '192.168.3.22', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-05 20:52:47', NULL, NULL);
INSERT INTO `sys_log` VALUES ('6664dc299f547f6702f93e2358810cc1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '192.168.3.22', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-05 21:04:14', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9c32ec437d8f8d407b1bd1165fc0305d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-08 15:01:25', NULL, NULL);
INSERT INTO `sys_log` VALUES ('68df65639e82cc6a889282fbef53afbb', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-08 15:01:37', NULL, NULL);
INSERT INTO `sys_log` VALUES ('79e76353faffd0beb0544c0aede8564f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-08 17:28:14', NULL, NULL);
INSERT INTO `sys_log` VALUES ('da3fda67aea2e565574ec2bcfab5b750', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-08 22:36:20', NULL, NULL);
INSERT INTO `sys_log` VALUES ('de37620b6921abcfe642606a0358d30f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-09 15:42:46', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b0e6b3a0ec5d8c73166fb8129d21a834', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-09 16:56:16', NULL, NULL);
INSERT INTO `sys_log` VALUES ('01075aa535274735e0df0a8bc44f62f9', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-09 16:56:46', NULL, NULL);
INSERT INTO `sys_log` VALUES ('01ebe1cbeae916a9228770f63130fdac', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-09 16:56:50', NULL, NULL);
INSERT INTO `sys_log` VALUES ('baa53d6a534e669f6150ea47565fa5b9', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-09 17:27:24', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e232f89df26cc9e5eced10476c4e4a2b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-10 10:05:11', NULL, NULL);
INSERT INTO `sys_log` VALUES ('335956cbad23d1974138752199bf1d84', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-10 10:05:36', NULL, NULL);
INSERT INTO `sys_log` VALUES ('bd6d7d720b9dd803f8ad26e2d40870f3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-10 11:04:06', NULL, NULL);
INSERT INTO `sys_log` VALUES ('ff3f7dbda20cd2734b1238fa5ba17fcf', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-10 11:26:43', NULL, NULL);
INSERT INTO `sys_log` VALUES ('672b527c49dc349689288ebf2c43ed4d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-10 11:26:47', NULL, NULL);
INSERT INTO `sys_log` VALUES ('21510ebaa4eca640852420ed6f6cbe01', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-10 11:41:26', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3e2574b7b723fbc9c712b8e200ea0c84', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-10 14:24:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('0d4582c6b7719b0bfc0260939d97274f', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-10 21:48:47', NULL, NULL);
INSERT INTO `sys_log` VALUES ('3e64011b4bea7cdb76953bfbf57135ce', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-10 23:09:32', NULL, NULL);
INSERT INTO `sys_log` VALUES ('a83e37b55a07fe48272b0005a193dee6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-11 09:17:59', NULL, NULL);
INSERT INTO `sys_log` VALUES ('30ec2dc50347240f131c1004ee9b3a40', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-11 10:19:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('7ce1934fb542a406e92867aec5b7254d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-11 14:53:23', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5ee6d5fe1e6adcc4ad441b230fae802d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-11 15:56:33', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d4ef00700436645680657f72445d38db', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-11 18:05:29', NULL, NULL);
INSERT INTO `sys_log` VALUES ('aa49341b29865b45588ad2f9b89c47ea', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-11 19:42:42', NULL, NULL);
INSERT INTO `sys_log` VALUES ('d3b54be0510db6a6da27bf30becb5335', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-11 19:42:46', NULL, NULL);
INSERT INTO `sys_log` VALUES ('dd4e1ab492e59719173d8ae0f5dbc9a2', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-04-11 19:47:12', NULL, NULL);
INSERT INTO `sys_log` VALUES ('056dd4466f4ed51de26c535fd9864828', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-11 19:47:15', NULL, NULL);
INSERT INTO `sys_log` VALUES ('89bfd8b9d6fa57a8e7017a2345ec1534', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-12 09:27:32', NULL, NULL);
INSERT INTO `sys_log` VALUES ('51aeabed335ab4e238640a4d17dd51a3', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg-boot', '2019-04-12 10:12:41', NULL, NULL);
INSERT INTO `sys_log` VALUES ('67181c36b55b06047a16a031fd1262c1', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-05-17 13:55:22', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2d5af41d2df82b316ba31fcdf6168d6a', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-05-17 14:43:58', NULL, NULL);
INSERT INTO `sys_log` VALUES ('e2f703771f64b1bcd709204669ae3d93', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-05-17 14:48:39', NULL, NULL);
INSERT INTO `sys_log` VALUES ('8143ce0b35bfe6e7b8113e1ecc066acd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-05-17 14:48:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2bca2d6666c1f6630225252c7b31326c', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-05-17 18:30:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('11695a9dcf44859cda97a4226bebe21b', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-05-17 18:30:57', NULL, NULL);
INSERT INTO `sys_log` VALUES ('40209016cadff6b571a8150c6218cfa8', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-05-18 11:33:28', NULL, NULL);
INSERT INTO `sys_log` VALUES ('23a314588249752842447e4f98783be4', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-05-19 18:28:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5f0a5e85efbe9c79645ffc0c15fcee1a', 2, '添加测试DEMO', NULL, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', NULL, '[{\"createBy\":\"admin\",\"createTime\":1558261833637,\"id\":\"94f78b1c9753dfb1202d731f540666e1\",\"keyWord\":\"1\",\"name\":\"1\",\"sysOrgCode\":\"A01\"}]', NULL, 30, 'admin', '2019-05-19 18:30:33', NULL, NULL);
INSERT INTO `sys_log` VALUES ('57264fff74c4f857bddf5d766951f3c9', 2, '添加测试DEMO', NULL, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.add()', NULL, '[{\"createBy\":\"admin\",\"createTime\":1558262155067,\"id\":\"dcb45a2fc661e5cdc341b806e5914873\",\"name\":\"111\",\"sysOrgCode\":\"A01\"}]', NULL, 5, 'admin', '2019-05-19 18:35:55', NULL, NULL);
INSERT INTO `sys_log` VALUES ('eef5b90eea8e7394193443cfd7476529', 2, '删除测试DEMO', NULL, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.demo.test.controller.JeecgDemoController.delete()', NULL, '[\"dcb45a2fc661e5cdc341b806e5914873\"]', NULL, 9, 'admin', '2019-05-19 18:36:02', NULL, NULL);
INSERT INTO `sys_log` VALUES ('488fc8f3d040fa75c6802898ea88f7d6', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-05-20 11:38:38', NULL, NULL);
INSERT INTO `sys_log` VALUES ('6c99cfe2774c15ad030b83723f81d70d', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-05-20 14:07:25', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1bf5c5603b79f749d4ee75965b3698db', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-05-20 14:54:39', NULL, NULL);
INSERT INTO `sys_log` VALUES ('5bca377b50c362009738d612cac82006', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-05-20 14:54:38', NULL, NULL);
INSERT INTO `sys_log` VALUES ('2255d6f5e2a3d0839b8b9cfc67816c5c', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-05-20 15:01:51', NULL, NULL);
INSERT INTO `sys_log` VALUES ('c7384ed6a6b09ff6704a6b1c1e378cee', 1, '用户名: 管理员,退出成功！', NULL, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-07-05 14:45:30', NULL, NULL);
INSERT INTO `sys_log` VALUES ('63c998d68b4d0d1529d86b4c0628e072', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-07-05 14:45:40', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b096a9e76395f1a52d8c260c4eb811fd', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-08-23 18:51:24', NULL, NULL);
INSERT INTO `sys_log` VALUES ('da7d2236f6f9e0f61897e5ea9b968d4d', 1, '用户名: 管理员,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-08-23 20:02:07', NULL, NULL);
INSERT INTO `sys_log` VALUES ('6ee846271a3d4b9e576b5a0749c49d12', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-08-23 20:02:16', NULL, NULL);
INSERT INTO `sys_log` VALUES ('f6b0f562257bf02c983b9e3998ff864e', 1, '用户名: 管理员,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-08-23 22:43:40', NULL, NULL);
INSERT INTO `sys_log` VALUES ('9e1da5d8758f2681543971ee43ee14e2', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-08-23 22:43:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('dd9b2cfffb798a22d90dada4fdbbbc61', 1, '用户名: 管理员,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-08-23 23:39:56', NULL, NULL);
INSERT INTO `sys_log` VALUES ('b99f5b6975350d86db4c5dd91de9f608', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-08-23 23:40:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1185035214331654145', 1, '用户名: 管理员,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-10-18 11:29:46', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1185035238713143298', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-10-18 11:29:52', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1185035239463923713', 1, '用户名: 管理员,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-10-18 11:29:53', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1185037047527714817', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-10-18 11:37:04', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1185037216109375490', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 133, 'admin', '2019-10-18 11:37:44', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1185037734533738497', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 24, 'admin', '2019-10-18 11:39:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1185037756142792705', 2, '编辑用户，id： e9ca23d68d884d4ebb19d07889727dae', 2, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-10-18 11:39:53', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1185037829144653825', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 12, 'admin', '2019-10-18 11:40:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1185039907581669377', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 6, 'admin', '2019-10-18 11:48:26', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1185039947956039681', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 5, 'admin', '2019-10-18 11:48:35', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1185040064834514945', 2, '职务表-添加', 2, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.add()', NULL, '[{\"code\":\"devleader\",\"createBy\":\"admin\",\"createTime\":1571370543072,\"id\":\"1185040064792571906\",\"name\":\"研发部经理\",\"rank\":\"3\",\"sysOrgCode\":\"A01\"}]', NULL, 22, 'admin', '2019-10-18 11:49:03', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1185040066562568193', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 26, 'admin', '2019-10-18 11:49:04', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1185129808478158850', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-10-18 17:45:39', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1185452151318528001', 1, '用户名: 管理员,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-10-19 15:06:32', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1185452231589117953', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-10-19 15:06:51', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197430985244905474', 1, '用户名: 管理员,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-11-21 16:26:08', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197431068325679105', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-11-21 16:26:29', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197431850395602945', 2, '填值规则-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysFillRuleController.queryPageList()', NULL, NULL, NULL, 41, 'admin', '2019-11-21 16:29:35', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197431872520556545', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 65, 'admin', '2019-11-21 16:29:40', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197431893076840450', 2, '职务表-编辑', 3, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.edit()', NULL, '[{\"code\":\"devleader\",\"createBy\":\"admin\",\"createTime\":1571370543000,\"id\":\"1185040064792571906\",\"name\":\"研发部经理\",\"postRank\":\"2\",\"sysOrgCode\":\"A01\",\"updateBy\":\"admin\",\"updateTime\":1574324985161}]', NULL, 19, 'admin', '2019-11-21 16:29:45', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197431893756317697', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 42, 'admin', '2019-11-21 16:29:45', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197433172830294018', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 22, 'admin', '2019-11-21 16:34:50', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197433546278539266', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 16, 'admin', '2019-11-21 16:36:19', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197433566151151618', 1, '用户名: 管理员,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-11-21 16:36:24', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197434320656130049', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-11-21 16:39:24', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197434360204222466', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 240, 'admin', '2019-11-21 16:39:33', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197434364826345474', 2, '编辑用户，id： e9ca23d68d884d4ebb19d07889727dae', 2, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2019-11-21 16:39:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197434403829178369', 1, '用户名: 管理员,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-11-21 16:39:44', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197434443708620802', 1, '用户名: jeecg,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-11-21 16:39:53', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197434470036267009', 2, '职务表-分页列表查询', 1, 'jeecg', 'jeecg', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 45, 'jeecg', '2019-11-21 16:40:00', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197434579583098881', 2, '职务表-分页列表查询', 1, 'jeecg', 'jeecg', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 46, 'jeecg', '2019-11-21 16:40:26', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197448449475338242', 1, '用户名: jeecg,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-11-21 17:35:33', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197448475219976193', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-11-21 17:35:39', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197448491221245953', 2, '填值规则-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysFillRuleController.queryPageList()', NULL, NULL, NULL, 98, 'admin', '2019-11-21 17:35:42', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197449269478547458', 2, '填值规则-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysFillRuleController.queryPageList()', NULL, NULL, NULL, 43, 'admin', '2019-11-21 17:38:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197450304582443009', 2, '填值规则-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysFillRuleController.queryPageList()', NULL, NULL, NULL, 27, 'admin', '2019-11-21 17:42:55', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197453216322797569', 2, '填值规则-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysFillRuleController.queryPageList()', NULL, NULL, NULL, 526, 'admin', '2019-11-21 17:54:29', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197454357987155969', 2, '填值规则-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysFillRuleController.queryPageList()', NULL, NULL, NULL, 329, 'admin', '2019-11-21 17:59:01', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197792320302452738', 1, '用户名: 管理员,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-11-22 16:21:57', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197792369866543106', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'jeecg', '2019-11-22 16:22:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1197793164787802113', 2, '填值规则-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysFillRuleController.queryPageList()', NULL, NULL, NULL, 381, 'admin', '2019-11-22 16:25:19', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209402348671193089', 1, '用户名: 管理员,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2019-12-24 17:16:04', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209402408473579521', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2019-12-24 17:16:18', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209402471128092673', 2, '填值规则-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysFillRuleController.queryPageList()', NULL, NULL, NULL, 472, 'admin', '2019-12-24 17:16:33', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209417003212853250', 2, '填值规则-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysFillRuleController.queryPageList()', NULL, NULL, NULL, 42, 'admin', '2019-12-24 18:14:18', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209646296664756226', 1, '用户名: 管理员,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2019-12-25 09:25:26', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209646643298816001', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2019-12-25 09:26:49', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209648067323744257', 2, '填值规则-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysFillRuleController.queryPageList()', NULL, NULL, NULL, 28, 'admin', '2019-12-25 09:32:28', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209648217823760385', 2, '填值规则-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysFillRuleController.queryPageList()', NULL, NULL, NULL, 21, 'admin', '2019-12-25 09:33:04', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209648344894394369', 2, '填值规则-批量删除', 4, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysFillRuleController.deleteBatch()', NULL, '[\"1192350056519323649,1192349918153428994,\"]', NULL, 23, 'admin', '2019-12-25 09:33:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209648345854889985', 2, '填值规则-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysFillRuleController.queryPageList()', NULL, NULL, NULL, 17, 'admin', '2019-12-25 09:33:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209648456588709890', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 122, 'admin', '2019-12-25 09:34:01', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209648997351936002', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 24, 'admin', '2019-12-25 09:36:10', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209649005795069954', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 25, 'admin', '2019-12-25 09:36:12', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209649012203966466', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 17, 'admin', '2019-12-25 09:36:13', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209653324963500034', 2, '填值规则-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysFillRuleController.queryPageList()', NULL, NULL, NULL, 19, 'admin', '2019-12-25 09:53:22', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209653453917376513', 2, '填值规则-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysFillRuleController.queryPageList()', NULL, NULL, NULL, 15, 'admin', '2019-12-25 09:53:52', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209660332378755073', 2, '填值规则-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysFillRuleController.queryPageList()', NULL, NULL, NULL, 126, 'admin', '2019-12-25 10:21:12', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209660342411530241', 2, '填值规则-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysFillRuleController.queryPageList()', NULL, NULL, NULL, 26, 'admin', '2019-12-25 10:21:15', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209672160353783810', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 170, 'admin', '2019-12-25 11:08:12', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209672431796555778', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 42, 'admin', '2019-12-25 11:09:17', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1209847153045913602', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2019-12-25 22:43:34', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231582569151074306', 1, '用户名: 管理员,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2020-02-23 22:12:20', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231582603032662018', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2020-02-23 22:12:29', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231582605322752002', 1, '用户名: 管理员,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2020-02-23 22:12:29', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231586195961556994', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2020-02-23 22:26:45', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231586370448797697', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 540, 'admin', '2020-02-23 22:27:27', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231589034586804226', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 37, 'admin', '2020-02-23 22:38:02', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231589059643576321', 2, '编辑用户，id： e9ca23d68d884d4ebb19d07889727dae', 2, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2020-02-23 22:38:08', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231590342991548417', 2, '填值规则-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysFillRuleController.queryPageList()', NULL, NULL, NULL, 26, 'admin', '2020-02-23 22:43:14', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231590348280565761', 2, '编码校验规则-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysCheckRuleController.queryPageList()', NULL, NULL, NULL, 27, 'admin', '2020-02-23 22:43:16', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231590455747022850', 2, '部门角色-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysDepartRoleController.queryPageList()', NULL, NULL, NULL, 6, 'admin', '2020-02-23 22:43:41', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231590510293946370', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 15, 'admin', '2020-02-23 22:43:54', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231590533404561410', 2, '编辑用户，id： e9ca23d68d884d4ebb19d07889727dae', 2, 'admin', '管理员', '127.0.0.1', NULL, NULL, NULL, NULL, NULL, 'admin', '2020-02-23 22:44:00', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231590588148617218', 2, '部门角色-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysDepartRoleController.queryPageList()', NULL, NULL, NULL, 45, 'admin', '2020-02-23 22:44:13', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231590598588239874', 2, '部门角色-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysDepartRoleController.queryPageList()', NULL, NULL, NULL, 8, 'admin', '2020-02-23 22:44:15', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231590600106577921', 2, '部门角色-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysDepartRoleController.queryPageList()', NULL, NULL, NULL, 11, 'admin', '2020-02-23 22:44:16', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231590978101448705', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 17, 'admin', '2020-02-23 22:45:46', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231591017582432257', 2, '多数据源管理-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysDataSourceController.queryPageList()', NULL, NULL, NULL, 41, 'admin', '2020-02-23 22:45:55', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231593459988566017', 2, '部门角色-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysDepartRoleController.queryPageList()', NULL, NULL, NULL, 22, 'admin', '2020-02-23 22:55:37', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231593466594594817', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 13, 'admin', '2020-02-23 22:55:39', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231593479697600513', 2, '职务表-编辑', 3, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.edit()', NULL, '[{\"code\":\"devleader\",\"createBy\":\"admin\",\"createTime\":1571370543000,\"id\":\"1185040064792571906\",\"name\":\"研发部经理\",\"postRank\":\"2\",\"sysOrgCode\":\"A01\",\"updateBy\":\"admin\",\"updateTime\":1582469742105}]', NULL, 16, 'admin', '2020-02-23 22:55:42', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231593480725204994', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 16, 'admin', '2020-02-23 22:55:42', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231596465543032834', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2020-02-23 23:07:33', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231596468936224769', 1, '用户名: 管理员,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2020-02-23 23:07:35', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231596595327381506', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2020-02-23 23:08:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231596596799582209', 1, '用户名: 管理员,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2020-02-23 23:08:05', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231596742199324674', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2020-02-23 23:08:40', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231596743692496897', 1, '用户名: 管理员,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2020-02-23 23:08:40', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231597287962157057', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2020-02-23 23:10:49', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231597290327744513', 1, '用户名: 管理员,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2020-02-23 23:10:51', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231598580457857025', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2020-02-23 23:15:57', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231599446049009666', 2, '部门角色-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysDepartRoleController.queryPageList()', NULL, NULL, NULL, 62, 'admin', '2020-02-23 23:19:24', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231599453611339778', 2, '部门角色-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysDepartRoleController.queryPageList()', NULL, NULL, NULL, 17, 'admin', '2020-02-23 23:19:26', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231599534171336706', 2, '职务表-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysPositionController.queryPageList()', NULL, NULL, NULL, 25, 'admin', '2020-02-23 23:19:46', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231599542991958018', 2, '部门角色-分页列表查询', 1, 'admin', '管理员', '127.0.0.1', 'org.jeecg.modules.system.controller.SysDepartRoleController.queryPageList()', NULL, NULL, NULL, 23, 'admin', '2020-02-23 23:19:48', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231615277050998786', 1, '用户名: 管理员,退出成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2020-02-24 00:22:19', NULL, NULL);
INSERT INTO `sys_log` VALUES ('1231615300820119553', 1, '用户名: admin,登录成功！', NULL, NULL, NULL, '127.0.0.1', NULL, NULL, NULL, NULL, NULL, NULL, '2020-02-24 00:22:25', NULL, NULL);

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父id',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单标题',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路径',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件',
  `component_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件名字',
  `redirect` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级菜单跳转地址',
  `menu_type` int(11) NULL DEFAULT NULL COMMENT '菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)',
  `perms` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单权限编码',
  `perms_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '权限策略1显示2禁用',
  `sort_no` double(8, 2) NULL DEFAULT NULL COMMENT '菜单排序',
  `always_show` tinyint(1) NULL DEFAULT NULL COMMENT '聚合子路由: 1是0否',
  `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `is_route` tinyint(1) NULL DEFAULT 1 COMMENT '是否路由菜单: 0:不是  1:是（默认值1）',
  `is_leaf` tinyint(1) NULL DEFAULT NULL COMMENT '是否叶子节点:    1:是   0:不是',
  `keep_alive` tinyint(1) NULL DEFAULT NULL COMMENT '是否缓存该页面:    1:是   0:不是',
  `hidden` int(2) NULL DEFAULT 0 COMMENT '是否隐藏路由: 0否,1是',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` int(1) NULL DEFAULT 0 COMMENT '删除状态 0正常 1已删除',
  `rule_flag` int(3) NULL DEFAULT 0 COMMENT '是否添加数据权限1是0否',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '按钮权限状态(0无效1有效)',
  `internal_or_external` tinyint(1) NULL DEFAULT NULL COMMENT '外链菜单打开方式 0/内部打开 1/外部打开',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_prem_pid`(`parent_id`) USING BTREE,
  INDEX `index_prem_is_route`(`is_route`) USING BTREE,
  INDEX `index_prem_is_leaf`(`is_leaf`) USING BTREE,
  INDEX `index_prem_sort_no`(`sort_no`) USING BTREE,
  INDEX `index_prem_del_flag`(`del_flag`) USING BTREE,
  INDEX `index_menu_type`(`menu_type`) USING BTREE,
  INDEX `index_menu_hidden`(`hidden`) USING BTREE,
  INDEX `index_menu_status`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('00a2a0ae65cdca5e93209cdbde97cbe6', '2e42e3835c2b44ec9f7bc26c146ee531', '成功', '/result/success', 'result/Success', NULL, NULL, 1, NULL, NULL, 1.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('020b06793e4de2eee0007f603000c769', 'f0675b52d89100ee88472b6800754a08', 'ViserChartDemo', '/report/ViserChartDemo', 'jeecg/report/ViserChartDemo', NULL, NULL, 1, NULL, NULL, 3.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-03 19:08:53', 'admin', '2019-04-03 19:08:53', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('024f1fd1283dc632458976463d8984e1', '700b7f95165c46cc7a78bf227aa8fed3', 'Tomcat信息', '/monitor/TomcatInfo', 'modules/monitor/TomcatInfo', NULL, NULL, 1, NULL, NULL, 4.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-02 09:44:29', 'admin', '2019-05-07 15:19:10', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('043780fa095ff1b2bec4dc406d76f023', '2a470fc0c3954d9dbb61de6d80846549', '表格合计', '/jeecg/tableTotal', 'jeecg/TableTotal', NULL, NULL, 1, NULL, '1', 3.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-08-14 10:28:46', NULL, NULL, 0, 0, '1', NULL);
INSERT INTO `sys_permission` VALUES ('05b3c82ddb2536a4a5ee1a4c46b5abef', '540a2936940846cb98114ffb0d145cb8', '用户列表', '/list/user-list', 'list/UserList', NULL, NULL, 1, NULL, NULL, 3.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('0620e402857b8c5b605e1ad9f4b89350', '2a470fc0c3954d9dbb61de6d80846549', '异步树列表Demo', '/jeecg/JeecgTreeTable', 'jeecg/JeecgTreeTable', NULL, NULL, 1, NULL, '0', 3.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-05-13 17:30:30', 'admin', '2019-05-13 17:32:17', 0, 0, '1', NULL);
INSERT INTO `sys_permission` VALUES ('078f9558cdeab239aecb2bda1a8ed0d1', 'fb07ca05a3e13674dbf6d3245956da2e', '搜索列表（文章）', '/list/search/article', 'list/TableList', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-02-12 14:00:34', 'admin', '2019-02-12 14:17:54', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('08e6b9dc3c04489c8e1ff2ce6f105aa4', '', '系统监控', '/dashboard3', 'layouts/RouteView', NULL, NULL, 0, NULL, NULL, 6.00, 0, 'dashboard', 1, 0, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-03-31 22:19:58', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('0ac2ad938963b6c6d1af25477d5b8b51', '8d4683aacaa997ab86b966b464360338', '代码生成按钮', NULL, NULL, NULL, NULL, 2, 'online:goGenerateCode', '1', 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-06-11 14:20:09', NULL, NULL, 0, 0, '1', NULL);
INSERT INTO `sys_permission` VALUES ('109c78a583d4693ce2f16551b7786786', 'e41b69c57a941a3bbcce45032fe57605', 'Online报表配置', '/online/cgreport', 'modules/online/cgreport/OnlCgreportHeadList', NULL, NULL, 1, NULL, NULL, 2.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-03-08 10:51:07', 'admin', '2019-03-30 19:04:28', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('1166535831146504193', '2a470fc0c3954d9dbb61de6d80846549', '对象存储', '/oss/file', 'modules/oss/OSSFileList', NULL, NULL, 1, NULL, '1', 1.00, 0, '', 1, 1, 0, 0, NULL, 'admin', '2019-08-28 02:19:50', 'admin', '2019-08-28 02:20:57', 0, 0, '1', NULL);
INSERT INTO `sys_permission` VALUES ('1170592628746878978', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '菜单管理2', '/isystem/newPermissionList', 'system/NewPermissionList', NULL, NULL, 1, NULL, '1', 100.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-09-08 15:00:05', 'admin', '2019-12-25 09:58:18', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1174506953255182338', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '职务管理', '/isystem/position', 'system/SysPositionList', NULL, NULL, 1, NULL, '1', 2.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-09-19 10:14:13', 'admin', '2019-09-19 10:15:22', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1174590283938041857', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '通讯录', '/isystem/addressList', 'system/AddressList', NULL, NULL, 1, NULL, '1', 3.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-09-19 15:45:21', NULL, NULL, 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1192318987661234177', 'e41b69c57a941a3bbcce45032fe57605', '系统编码生成规则', '/isystem/fillRule', 'system/SysFillRuleList', NULL, NULL, 1, NULL, '1', 3.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-11-07 13:52:53', 'admin', '2020-02-23 22:42:30', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1205097455226462210', '', '大屏设计', '/big/screen', 'layouts/RouteView', NULL, NULL, 0, NULL, '1', 1.10, 0, 'area-chart', 1, 0, 0, 0, NULL, 'admin', '2019-12-12 20:09:58', 'admin', '2020-02-23 23:17:59', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1205098241075453953', '1205097455226462210', '生产销售监控', '{{ window._CONFIG[\'domianURL\'] }}/big/screen/templat/index1', 'layouts/IframePageView', NULL, NULL, 1, NULL, '1', 1.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-12-12 20:13:05', 'admin', '2019-12-12 20:15:27', 0, 0, '1', 1);
INSERT INTO `sys_permission` VALUES ('1205306106780364802', '1205097455226462210', '智慧物流监控', '{{ window._CONFIG[\'domianURL\'] }}/big/screen/templat/index2', 'layouts/IframePageView', NULL, NULL, 1, NULL, '1', 2.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-12-13 09:59:04', 'admin', '2019-12-25 09:28:03', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1209731624921534465', 'e41b69c57a941a3bbcce45032fe57605', '多数据源管理', '/isystem/dataSource', 'system/SysDataSourceList', NULL, NULL, 1, NULL, '1', 6.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-12-25 15:04:30', 'admin', '2020-02-23 22:43:37', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1224641973866467330', 'e41b69c57a941a3bbcce45032fe57605', '系统编码校验规则', '/isystem/checkRule', 'system/SysCheckRuleList', NULL, NULL, 1, NULL, '1', 5.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-11-07 13:52:53', 'admin', '2020-02-23 22:43:05', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('1229674163694841857', 'e41b69c57a941a3bbcce45032fe57605', 'AUTO在线表单ERP', '/online/cgformErpList/:code', 'modules/online/cgform/auto/erp/OnlCgformErpList', NULL, NULL, 1, NULL, '1', 5.00, 0, NULL, 1, 1, 0, 1, NULL, 'admin', '2020-02-18 15:49:00', 'admin', '2020-02-18 15:52:25', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('13212d3416eb690c2e1d5033166ff47a', '2e42e3835c2b44ec9f7bc26c146ee531', '失败', '/result/fail', 'result/Error', NULL, NULL, 1, NULL, NULL, 2.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('1367a93f2c410b169faa7abcbad2f77c', '6e73eb3c26099c191bf03852ee1310a1', '基本设置', '/account/settings/base', 'account/settings/BaseSetting', NULL, NULL, 1, 'BaseSettings', NULL, NULL, 0, NULL, 1, 1, NULL, 1, NULL, NULL, '2018-12-26 18:58:35', 'admin', '2019-03-20 12:57:31', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('190c2b43bec6a5f7a4194a85db67d96a', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '角色管理', '/isystem/roleUserList', 'system/RoleUserList', NULL, NULL, 1, NULL, NULL, 1.20, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-04-17 15:13:56', 'admin', '2019-12-25 09:36:31', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('1a0811914300741f4e11838ff37a1d3a', '3f915b2769fc80648e92d04e84ca059d', '手机号禁用', NULL, NULL, NULL, NULL, 2, 'user:form:phone', '2', 1.00, 0, NULL, 0, 1, NULL, 0, NULL, 'admin', '2019-05-11 17:19:30', 'admin', '2019-05-11 18:00:22', 0, 0, '1', NULL);
INSERT INTO `sys_permission` VALUES ('200006f0edf145a2b50eacca07585451', 'fb07ca05a3e13674dbf6d3245956da2e', '搜索列表（应用）', '/list/search/application', 'list/TableList', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-02-12 14:02:51', 'admin', '2019-02-12 14:14:01', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('22d6a3d39a59dd7ea9a30acfa6bfb0a5', 'e41b69c57a941a3bbcce45032fe57605', 'AUTO动态表单', '/online/df/:table/:id', 'modules/online/cgform/auto/OnlineDynamicForm', NULL, NULL, 1, NULL, NULL, 9.00, 0, NULL, 0, 1, NULL, 1, NULL, 'admin', '2019-04-22 15:15:43', 'admin', '2019-04-30 18:18:26', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('265de841c58907954b8877fb85212622', '2a470fc0c3954d9dbb61de6d80846549', '图片拖拽排序', '/jeecg/imgDragSort', 'jeecg/ImgDragSort', NULL, NULL, 1, NULL, NULL, 4.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-25 10:43:08', 'admin', '2019-04-25 10:46:26', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('277bfabef7d76e89b33062b16a9a5020', 'e3c13679c73a4f829bcff2aba8fd68b1', '基础表单', '/form/base-form', 'form/BasicForm', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 0, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-02-26 17:02:08', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('2a470fc0c3954d9dbb61de6d80846549', '', '常见案例', '/jeecg', 'layouts/RouteView', NULL, NULL, 0, NULL, NULL, 7.00, 0, 'qrcode', 1, 0, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-04-02 11:46:42', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('2aeddae571695cd6380f6d6d334d6e7d', 'f0675b52d89100ee88472b6800754a08', '布局统计报表', '/report/ArchivesStatisticst', 'jeecg/report/ArchivesStatisticst', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-03 18:32:48', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('2dbbafa22cda07fa5d169d741b81fe12', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '在线文档', '{{ window._CONFIG[\'domianURL\'] }}/doc.html', 'layouts/IframePageView', NULL, NULL, 1, NULL, NULL, 3.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-01-30 10:00:01', 'admin', '2019-03-23 19:44:43', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('2e42e3835c2b44ec9f7bc26c146ee531', '', '结果页', '/result', 'layouts/PageView', NULL, NULL, 0, NULL, NULL, 8.00, 0, 'check-circle-o', 1, 0, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-04-02 11:46:56', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('339329ed54cf255e1f9392e84f136901', '2a470fc0c3954d9dbb61de6d80846549', 'helloworld', '/jeecg/helloworld', 'jeecg/helloworld', NULL, NULL, 1, NULL, NULL, 4.00, 0, NULL, 1, 1, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-02-15 16:24:56', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('3f915b2769fc80648e92d04e84ca059d', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '用户管理', '/isystem/user', 'system/UserList', NULL, NULL, 1, NULL, NULL, 1.10, 0, NULL, 1, 0, 0, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-12-25 09:36:24', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('3fac0d3c9cd40fa53ab70d4c583821f8', '2a470fc0c3954d9dbb61de6d80846549', '分屏', '/jeecg/splitPanel', 'jeecg/SplitPanel', NULL, NULL, 1, NULL, NULL, 6.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-25 16:27:06', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('4148ec82b6acd69f470bea75fe41c357', '2a470fc0c3954d9dbb61de6d80846549', '单表模型示例', '/jeecg/jeecgDemoList', 'jeecg/JeecgDemoList', 'DemoList', NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, NULL, 0, NULL, NULL, '2018-12-28 15:57:30', 'admin', '2019-02-15 16:24:37', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('418964ba087b90a84897b62474496b93', '540a2936940846cb98114ffb0d145cb8', '查询表格', '/list/query-list', 'list/TableList', NULL, NULL, 1, NULL, NULL, 1.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('4356a1a67b564f0988a484f5531fd4d9', '2a470fc0c3954d9dbb61de6d80846549', '内嵌Table', '/jeecg/TableExpandeSub', 'jeecg/TableExpandeSub', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-04 22:48:13', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('45c966826eeff4c99b8f8ebfe74511fc', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '部门管理', '/isystem/depart', 'system/DepartList', NULL, NULL, 1, NULL, NULL, 1.40, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-01-29 18:47:40', 'admin', '2019-12-25 09:36:47', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('4875ebe289344e14844d8e3ea1edd73f', '', '详情页', '/profile', 'layouts/RouteView', NULL, NULL, 0, NULL, NULL, 8.00, 0, 'profile', 1, 0, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-04-02 11:46:48', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('4f66409ef3bbd69c1d80469d6e2a885e', '6e73eb3c26099c191bf03852ee1310a1', '账户绑定', '/account/settings/binding', 'account/settings/Binding', NULL, NULL, 1, 'BindingSettings', NULL, NULL, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-26 19:01:20', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('4f84f9400e5e92c95f05b554724c2b58', '540a2936940846cb98114ffb0d145cb8', '角色列表', '/list/role-list', 'list/RoleList', NULL, NULL, 1, NULL, NULL, 4.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('53a9230444d33de28aa11cc108fb1dba', '5c8042bd6c601270b2bbd9b20bccc68b', '我的消息', '/isps/userAnnouncement', 'system/UserAnnouncementList', NULL, NULL, 1, NULL, NULL, 3.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-04-19 10:16:00', 'admin', '2019-12-25 09:54:34', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('54097c6a3cf50fad0793a34beff1efdf', 'e41b69c57a941a3bbcce45032fe57605', 'AUTO在线表单', '/online/cgformList/:code', 'modules/online/cgform/auto/OnlCgformAutoList', NULL, NULL, 1, NULL, NULL, 9.00, 0, NULL, 1, 1, NULL, 1, NULL, 'admin', '2019-03-19 16:03:06', 'admin', '2019-04-30 18:19:03', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('540a2936940846cb98114ffb0d145cb8', '', '列表页', '/list', 'layouts/PageView', NULL, '/list/query-list', 0, NULL, NULL, 9.00, 0, 'table', 1, 0, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-03-31 22:20:20', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('54dd5457a3190740005c1bfec55b1c34', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '菜单管理', '/isystem/permission', 'system/PermissionList', NULL, NULL, 1, NULL, NULL, 1.30, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-12-25 09:36:39', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('58857ff846e61794c69208e9d3a85466', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '日志管理', '/isystem/log', 'system/LogList', NULL, NULL, 1, NULL, NULL, 1.00, 0, '', 1, 1, NULL, 0, NULL, NULL, '2018-12-26 10:11:18', 'admin', '2019-04-02 11:38:17', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('58b9204feaf07e47284ddb36cd2d8468', '2a470fc0c3954d9dbb61de6d80846549', '图片翻页', '/jeecg/imgTurnPage', 'jeecg/ImgTurnPage', NULL, NULL, 1, NULL, NULL, 4.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-25 11:36:42', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('5c2f42277948043026b7a14692456828', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '我的部门', '/isystem/departUserList', 'system/DepartUserList', NULL, NULL, 1, NULL, NULL, 2.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-04-17 15:12:24', 'admin', '2019-12-25 09:35:26', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('5c8042bd6c601270b2bbd9b20bccc68b', '', '消息中心', '/message', 'layouts/RouteView', NULL, NULL, 0, NULL, NULL, 6.00, 0, 'message', 1, 0, NULL, 0, NULL, 'admin', '2019-04-09 11:05:04', 'admin', '2019-04-11 19:47:54', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('6531cf3421b1265aeeeabaab5e176e6d', 'e3c13679c73a4f829bcff2aba8fd68b1', '分步表单', '/form/step-form', 'form/stepForm/StepForm', NULL, NULL, 1, NULL, NULL, 2.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('655563cd64b75dcf52ef7bcdd4836953', '2a470fc0c3954d9dbb61de6d80846549', '图片预览', '/jeecg/ImagPreview', 'jeecg/ImagPreview', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-17 11:18:45', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('65a8f489f25a345836b7f44b1181197a', 'c65321e57b7949b7a975313220de0422', '403', '/exception/403', 'exception/403', NULL, NULL, 1, NULL, NULL, 1.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('6ad53fd1b220989a8b71ff482d683a5a', '2a470fc0c3954d9dbb61de6d80846549', '一对多Tab示例', '/jeecg/tablist/JeecgOrderDMainList', 'jeecg/tablist/JeecgOrderDMainList', NULL, NULL, 1, NULL, NULL, 2.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-02-20 14:45:09', 'admin', '2019-02-21 16:26:21', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('6e73eb3c26099c191bf03852ee1310a1', '717f6bee46f44a3897eca9abd6e2ec44', '个人设置', '/account/settings/base', 'account/settings/Index', NULL, NULL, 1, NULL, NULL, 2.00, 1, NULL, 1, 0, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-04-19 09:41:05', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('700b7f95165c46cc7a78bf227aa8fed3', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '性能监控', '/monitor', 'layouts/RouteView', NULL, NULL, 1, NULL, NULL, 0.00, 0, NULL, 1, 0, NULL, 0, NULL, 'admin', '2019-04-02 11:34:34', 'admin', '2019-05-05 17:49:47', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('717f6bee46f44a3897eca9abd6e2ec44', '', '个人页', '/account', 'layouts/RouteView', NULL, NULL, 0, NULL, NULL, 9.00, 0, 'user', 1, 0, 0, 1, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2020-02-23 22:41:37', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('73678f9daa45ed17a3674131b03432fb', '540a2936940846cb98114ffb0d145cb8', '权限列表', '/list/permission-list', 'list/PermissionList', NULL, NULL, 1, NULL, NULL, 5.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('7593c9e3523a17bca83b8d7fe8a34e58', '3f915b2769fc80648e92d04e84ca059d', '添加用户按钮', '', NULL, NULL, NULL, 2, 'user:add', '1', 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-03-16 11:20:33', 'admin', '2019-05-17 18:31:25', 0, 0, '1', NULL);
INSERT INTO `sys_permission` VALUES ('7960961b0063228937da5fa8dd73d371', '2a470fc0c3954d9dbb61de6d80846549', 'JEditableTable示例', '/jeecg/JEditableTable', 'jeecg/JeecgEditableTableExample', NULL, NULL, 1, NULL, NULL, 2.10, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-03-22 15:22:18', 'admin', '2019-12-25 09:48:16', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('7ac9eb9ccbde2f7a033cd4944272bf1e', '540a2936940846cb98114ffb0d145cb8', '卡片列表', '/list/card', 'list/CardList', NULL, NULL, 1, NULL, NULL, 7.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('841057b8a1bef8f6b4b20f9a618a7fa6', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '数据日志', '/sys/dataLog-list', 'system/DataLogList', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-03-11 19:26:49', 'admin', '2019-03-12 11:40:47', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('882a73768cfd7f78f3a37584f7299656', '6e73eb3c26099c191bf03852ee1310a1', '个性化设置', '/account/settings/custom', 'account/settings/Custom', NULL, NULL, 1, 'CustomSettings', NULL, NULL, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-26 19:00:46', NULL, '2018-12-26 21:13:25', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('8b3bff2eee6f1939147f5c68292a1642', '700b7f95165c46cc7a78bf227aa8fed3', '服务器信息', '/monitor/SystemInfo', 'modules/monitor/SystemInfo', NULL, NULL, 1, NULL, NULL, 4.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-02 11:39:19', 'admin', '2019-04-02 15:40:02', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('8d1ebd663688965f1fd86a2f0ead3416', '700b7f95165c46cc7a78bf227aa8fed3', 'Redis监控', '/monitor/redis/info', 'modules/monitor/RedisInfo', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-02 13:11:33', 'admin', '2019-05-07 15:18:54', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('8d4683aacaa997ab86b966b464360338', 'e41b69c57a941a3bbcce45032fe57605', 'Online表单开发', '/online/cgform', 'modules/online/cgform/OnlCgformHeadList', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 0, NULL, 0, NULL, 'admin', '2019-03-12 15:48:14', 'admin', '2019-06-11 14:19:17', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('8fb8172747a78756c11916216b8b8066', '717f6bee46f44a3897eca9abd6e2ec44', '工作台', '/dashboard/workplace', 'dashboard/Workplace', NULL, NULL, 1, NULL, NULL, 3.00, 0, NULL, 1, 1, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-04-02 11:45:02', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('944abf0a8fc22fe1f1154a389a574154', '5c8042bd6c601270b2bbd9b20bccc68b', '消息管理', '/modules/message/sysMessageList', 'modules/message/SysMessageList', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-09 11:27:53', 'admin', '2019-04-09 19:31:23', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('9502685863ab87f0ad1134142788a385', '', '首页', '/dashboard/analysis', 'dashboard/Analysis', NULL, NULL, 0, NULL, NULL, 0.00, 0, 'home', 1, 1, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-03-29 11:04:13', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('97c8629abc7848eccdb6d77c24bb3ebb', '700b7f95165c46cc7a78bf227aa8fed3', '磁盘监控', '/monitor/Disk', 'modules/monitor/DiskMonitoring', NULL, NULL, 1, NULL, NULL, 6.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-25 14:30:06', 'admin', '2019-05-05 14:37:14', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('9a90363f216a6a08f32eecb3f0bf12a3', '2a470fc0c3954d9dbb61de6d80846549', '自定义组件', '/jeecg/SelectDemo', 'jeecg/SelectDemo', NULL, NULL, 1, NULL, NULL, 0.00, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-03-19 11:19:05', 'admin', '2019-12-25 09:47:04', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('9cb91b8851db0cf7b19d7ecc2a8193dd', '1939e035e803a99ceecb6f5563570fb2', '我的任务表单', '/modules/bpm/task/form/FormModule', 'modules/bpm/task/form/FormModule', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-03-08 16:49:05', 'admin', '2019-03-08 18:37:56', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('9fe26464838de2ea5e90f2367e35efa0', 'e41b69c57a941a3bbcce45032fe57605', 'AUTO在线报表', '/online/cgreport/:code', 'modules/online/cgreport/auto/OnlCgreportAutoList', 'onlineAutoList', NULL, 1, NULL, NULL, 9.00, 0, NULL, 1, 1, NULL, 1, NULL, 'admin', '2019-03-12 11:06:48', 'admin', '2019-04-30 18:19:10', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('a400e4f4d54f79bf5ce160ae432231af', '2a470fc0c3954d9dbb61de6d80846549', '百度', 'http://www.baidu.com', 'layouts/IframePageView', NULL, NULL, 1, NULL, NULL, 4.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-01-29 19:44:06', 'admin', '2019-02-15 16:25:02', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('ae4fed059f67086fd52a73d913cf473d', '540a2936940846cb98114ffb0d145cb8', '内联编辑表格', '/list/edit-table', 'list/TableInnerEditList', NULL, NULL, 1, NULL, NULL, 2.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('aedbf679b5773c1f25e9f7b10111da73', '08e6b9dc3c04489c8e1ff2ce6f105aa4', 'SQL监控', '{{ window._CONFIG[\'domianURL\'] }}/druid/', 'layouts/IframePageView', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-01-30 09:43:22', 'admin', '2019-03-23 19:00:46', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('b1cb0a3fedf7ed0e4653cb5a229837ee', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '定时任务', '/isystem/QuartzJobList', 'system/QuartzJobList', NULL, NULL, 1, NULL, NULL, 3.00, 0, NULL, 1, 1, NULL, 0, NULL, NULL, '2019-01-03 09:38:52', 'admin', '2019-04-02 10:24:13', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('b3c824fc22bd953e2eb16ae6914ac8f9', '4875ebe289344e14844d8e3ea1edd73f', '高级详情页', '/profile/advanced', 'profile/advanced/Advanced', NULL, NULL, 1, NULL, NULL, 2.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('b4dfc7d5dd9e8d5b6dd6d4579b1aa559', 'c65321e57b7949b7a975313220de0422', '500', '/exception/500', 'exception/500', NULL, NULL, 1, NULL, NULL, 3.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('b6bcee2ccc854052d3cc3e9c96d90197', '71102b3b87fb07e5527bbd2c530dd90a', '加班申请', '/modules/extbpm/joa/JoaOvertimeList', 'modules/extbpm/joa/JoaOvertimeList', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-03 15:33:10', 'admin', '2019-04-03 15:34:48', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('c431130c0bc0ec71b0a5be37747bb36a', '2a470fc0c3954d9dbb61de6d80846549', '一对多JEditable', '/jeecg/JeecgOrderMainListForJEditableTable', 'jeecg/JeecgOrderMainListForJEditableTable', NULL, NULL, 1, NULL, NULL, 3.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-03-29 10:51:59', 'admin', '2019-04-04 20:09:39', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('c65321e57b7949b7a975313220de0422', NULL, '异常页', '/exception', 'layouts/RouteView', NULL, NULL, 0, NULL, NULL, 8.00, NULL, 'warning', 1, 0, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('c6cf95444d80435eb37b2f9db3971ae6', '2a470fc0c3954d9dbb61de6d80846549', '数据回执模拟', '/jeecg/InterfaceTest', 'jeecg/InterfaceTest', NULL, NULL, 1, NULL, NULL, 6.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-02-19 16:02:23', 'admin', '2019-02-21 16:25:45', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('cc50656cf9ca528e6f2150eba4714ad2', '4875ebe289344e14844d8e3ea1edd73f', '基础详情页', '/profile/basic', 'profile/basic/Index', NULL, NULL, 1, NULL, NULL, 1.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('d07a2c87a451434c99ab06296727ec4f', '700b7f95165c46cc7a78bf227aa8fed3', 'JVM信息', '/monitor/JvmInfo', 'modules/monitor/JvmInfo', NULL, NULL, 1, NULL, NULL, 4.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-01 23:07:48', 'admin', '2019-04-02 11:37:16', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('d2bbf9ebca5a8fa2e227af97d2da7548', 'c65321e57b7949b7a975313220de0422', '404', '/exception/404', 'exception/404', NULL, NULL, 1, NULL, NULL, 2.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('d7d6e2e4e2934f2c9385a623fd98c6f3', '', '系统管理', '/isystem', 'layouts/RouteView', NULL, NULL, 0, NULL, NULL, 4.00, 0, 'setting', 1, 0, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-03-31 22:19:52', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('d86f58e7ab516d3bc6bfb1fe10585f97', '717f6bee46f44a3897eca9abd6e2ec44', '个人中心', '/account/center', 'account/center/Index', NULL, NULL, 1, NULL, NULL, 1.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('de13e0f6328c069748de7399fcc1dbbd', 'fb07ca05a3e13674dbf6d3245956da2e', '搜索列表（项目）', '/list/search/project', 'list/TableList', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-02-12 14:01:40', 'admin', '2019-02-12 14:14:18', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('e08cb190ef230d5d4f03824198773950', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '系统通告', '/isystem/annountCement', 'system/SysAnnouncementList', NULL, NULL, 1, 'annountCement', NULL, 6.00, NULL, '', 1, 1, NULL, NULL, NULL, NULL, '2019-01-02 17:23:01', NULL, '2019-01-02 17:31:23', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('e1979bb53e9ea51cecc74d86fd9d2f64', '2a470fc0c3954d9dbb61de6d80846549', 'PDF预览', '/jeecg/jeecgPdfView', 'jeecg/JeecgPdfView', NULL, NULL, 1, NULL, NULL, 3.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-25 10:39:35', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('e3c13679c73a4f829bcff2aba8fd68b1', '', '表单页', '/form', 'layouts/PageView', NULL, NULL, 0, NULL, NULL, 9.00, 0, 'form', 1, 0, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-03-31 22:20:14', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('e41b69c57a941a3bbcce45032fe57605', '', '在线开发', '/online', 'layouts/RouteView', NULL, NULL, 0, NULL, NULL, 5.00, 0, 'cloud', 1, 0, NULL, 0, NULL, 'admin', '2019-03-08 10:43:10', 'admin', '2019-05-11 10:36:01', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('e5973686ed495c379d829ea8b2881fc6', 'e3c13679c73a4f829bcff2aba8fd68b1', '高级表单', '/form/advanced-form', 'form/advancedForm/AdvancedForm', NULL, NULL, 1, NULL, NULL, 3.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('e6bfd1fcabfd7942fdd05f076d1dad38', '2a470fc0c3954d9dbb61de6d80846549', '打印测试', '/jeecg/PrintDemo', 'jeecg/PrintDemo', NULL, NULL, 1, NULL, NULL, 3.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-02-19 15:58:48', 'admin', '2019-05-07 20:14:39', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('ebb9d82ea16ad864071158e0c449d186', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '分类字典', '/isys/category', 'system/SysCategoryList', NULL, NULL, 1, NULL, '1', 5.20, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-05-29 18:48:07', 'admin', '2020-02-23 22:45:33', 0, 0, '1', 0);
INSERT INTO `sys_permission` VALUES ('ec8d607d0156e198b11853760319c646', '6e73eb3c26099c191bf03852ee1310a1', '安全设置', '/account/settings/security', 'account/settings/Security', NULL, NULL, 1, 'SecuritySettings', NULL, NULL, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-26 18:59:52', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('f0675b52d89100ee88472b6800754a08', '', '统计报表', '/report', 'layouts/RouteView', NULL, NULL, 0, NULL, NULL, 1.00, 0, 'bar-chart', 1, 0, NULL, 0, NULL, 'admin', '2019-04-03 18:32:02', 'admin', '2019-05-19 18:34:13', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('f1cb187abf927c88b89470d08615f5ac', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '数据字典', '/isystem/dict', 'system/DictList', NULL, NULL, 1, NULL, NULL, 5.00, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2018-12-28 13:54:43', 'admin', '2020-02-23 22:45:25', 0, 0, NULL, 0);
INSERT INTO `sys_permission` VALUES ('f23d9bfff4d9aa6b68569ba2cff38415', '540a2936940846cb98114ffb0d145cb8', '标准列表', '/list/basic-list', 'list/StandardList', NULL, NULL, 1, NULL, NULL, 6.00, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, '2018-12-25 20:34:38', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('f2849d3814fc97993bfc519ae6bbf049', 'e41b69c57a941a3bbcce45032fe57605', 'AUTO复制表单', '/online/copyform/:code', 'modules/online/cgform/OnlCgformCopyList', NULL, NULL, 1, NULL, '1', 1.00, 0, NULL, 1, 1, 0, 1, NULL, 'admin', '2019-08-29 16:05:37', NULL, NULL, 0, 0, '1', NULL);
INSERT INTO `sys_permission` VALUES ('f780d0d3083d849ccbdb1b1baee4911d', '5c8042bd6c601270b2bbd9b20bccc68b', '模板管理', '/modules/message/sysMessageTemplateList', 'modules/message/SysMessageTemplateList', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-09 11:50:31', 'admin', '2019-04-12 10:16:34', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('fb07ca05a3e13674dbf6d3245956da2e', '540a2936940846cb98114ffb0d145cb8', '搜索列表', '/list/search', 'list/search/SearchLayout', NULL, '/list/search/article', 1, NULL, NULL, 8.00, 0, NULL, 1, 0, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-02-12 15:09:13', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('fb367426764077dcf94640c843733985', '2a470fc0c3954d9dbb61de6d80846549', '一对多示例', '/jeecg/JeecgOrderMainList', 'jeecg/JeecgOrderMainList', NULL, NULL, 1, NULL, NULL, 2.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-02-15 16:24:11', 'admin', '2019-02-18 10:50:14', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('fba41089766888023411a978d13c0aa4', 'e41b69c57a941a3bbcce45032fe57605', 'AUTO树表单列表', '/online/cgformTreeList/:code', 'modules/online/cgform/auto/OnlCgformTreeList', NULL, NULL, 1, NULL, '1', 9.00, 0, NULL, 1, 1, NULL, 1, NULL, 'admin', '2019-05-21 14:46:50', 'admin', '2019-06-11 13:52:52', 0, 0, '1', NULL);
INSERT INTO `sys_permission` VALUES ('fc810a2267dd183e4ef7c71cc60f4670', '700b7f95165c46cc7a78bf227aa8fed3', '请求追踪', '/monitor/HttpTrace', 'modules/monitor/HttpTrace', NULL, NULL, 1, NULL, NULL, 4.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-02 09:46:19', 'admin', '2019-04-02 11:37:27', 0, 0, NULL, NULL);
INSERT INTO `sys_permission` VALUES ('fedfbf4420536cacc0218557d263dfea', '6e73eb3c26099c191bf03852ee1310a1', '新消息通知', '/account/settings/notification', 'account/settings/Notification', NULL, NULL, 1, 'NotificationSettings', NULL, NULL, NULL, '', 1, 1, NULL, NULL, NULL, NULL, '2018-12-26 19:02:05', NULL, NULL, 0, 0, NULL, NULL);

-- ----------------------------
-- Table structure for sys_permission_data_rule
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission_data_rule`;
CREATE TABLE `sys_permission_data_rule`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
  `permission_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单ID',
  `rule_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则名称',
  `rule_column` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段',
  `rule_conditions` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '条件',
  `rule_value` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则值',
  `status` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限有效状态1有0否',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_fucntionid`(`permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission_data_rule
-- ----------------------------
INSERT INTO `sys_permission_data_rule` VALUES ('32b62cb04d6c788d9d92e3ff5e66854e', '8d4683aacaa997ab86b966b464360338', '000', '00', '!=', '00', '1', '2019-04-02 18:36:08', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('40283181614231d401614234fe670003', '40283181614231d401614232cd1c0001', 'createBy', 'createBy', '=', '#{sys_user_code}', '1', '2018-01-29 21:57:04', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('4028318161424e730161424fca6f0004', '4028318161424e730161424f61510002', 'createBy', 'createBy', '=', '#{sys_user_code}', '1', '2018-01-29 22:26:20', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402880e6487e661a01487e732c020005', '402889fb486e848101486e93a7c80014', 'SYS_ORG_CODE', 'SYS_ORG_CODE', 'LIKE', '010201%', '1', '2014-09-16 20:32:30', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402880e6487e661a01487e8153ee0007', '402889fb486e848101486e93a7c80014', 'create_by', 'create_by', '', '#{SYS_USER_CODE}', '1', '2014-09-16 20:47:57', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402880ec5ddec439015ddf9225060038', '40288088481d019401481d2fcebf000d', '复杂关系', '', 'USE_SQL_RULES', 'name like \'%张%\' or age > 10', '1', NULL, NULL, '2017-08-14 15:10:25', 'demo');
INSERT INTO `sys_permission_data_rule` VALUES ('402880ec5ddfdd26015ddfe3e0570011', '4028ab775dca0d1b015dca3fccb60016', '复杂sql配置', '', 'USE_SQL_RULES', 'table_name like \'%test%\' or is_tree = \'Y\'', '1', NULL, NULL, '2017-08-14 16:38:55', 'demo');
INSERT INTO `sys_permission_data_rule` VALUES ('402880f25b1e2ac7015b1e5fdebc0012', '402880f25b1e2ac7015b1e5cdc340010', '只能看自己数据', 'create_by', '=', '#{sys_user_code}', '1', '2017-03-30 16:40:51', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402881875b19f141015b19f8125e0014', '40288088481d019401481d2fcebf000d', '可看下属业务数据', 'sys_org_code', 'LIKE', '#{sys_org_code}', '1', NULL, NULL, '2017-08-14 15:04:32', 'demo');
INSERT INTO `sys_permission_data_rule` VALUES ('402881e45394d66901539500a4450001', '402881e54df73c73014df75ab670000f', 'sysCompanyCode', 'sysCompanyCode', '=', '#{SYS_COMPANY_CODE}', '1', '2016-03-21 01:09:21', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402881e45394d6690153950177cb0003', '402881e54df73c73014df75ab670000f', 'sysOrgCode', 'sysOrgCode', '=', '#{SYS_ORG_CODE}', '1', '2016-03-21 01:10:15', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402881e56266f43101626727aff60067', '402881e56266f43101626724eb730065', '销售自己看自己的数据', 'createBy', '=', '#{sys_user_code}', '1', '2018-03-27 19:11:16', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402881e56266f4310162672fb1a70082', '402881e56266f43101626724eb730065', '销售经理看所有下级数据', 'sysOrgCode', 'LIKE', '#{sys_org_code}', '1', '2018-03-27 19:20:01', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402881e56266f431016267387c9f0088', '402881e56266f43101626724eb730065', '只看金额大于1000的数据', 'money', '>=', '1000', '1', '2018-03-27 19:29:37', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402881f3650de25101650dfb5a3a0010', '402881e56266f4310162671d62050044', '22', '', 'USE_SQL_RULES', '22', '1', '2018-08-06 14:45:01', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402889fb486e848101486e913cd6000b', '402889fb486e848101486e8e2e8b0007', 'userName', 'userName', '=', 'admin', '1', '2014-09-13 18:31:25', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('402889fb486e848101486e98d20d0016', '402889fb486e848101486e93a7c80014', 'title', 'title', '=', '12', '1', NULL, NULL, '2014-09-13 22:18:22', 'scott');
INSERT INTO `sys_permission_data_rule` VALUES ('402889fe47fcb29c0147fcb6b6220001', '8a8ab0b246dc81120146dc8180fe002b', '12', '12', '>', '12', '1', '2014-08-22 15:55:38', '8a8ab0b246dc81120146dc8181950052', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('4028ab775dca0d1b015dca4183530018', '4028ab775dca0d1b015dca3fccb60016', '表名限制', 'isDbSynch', '=', 'Y', '1', NULL, NULL, '2017-08-14 16:43:45', 'demo');
INSERT INTO `sys_permission_data_rule` VALUES ('4028ef815595a881015595b0ccb60001', '40288088481d019401481d2fcebf000d', '限只能看自己', 'create_by', '=', '#{sys_user_code}', '1', NULL, NULL, '2017-08-14 15:03:56', 'demo');
INSERT INTO `sys_permission_data_rule` VALUES ('4028ef81574ae99701574aed26530005', '4028ef81574ae99701574aeb97bd0003', '用户名', 'userName', '!=', 'admin', '1', '2016-09-21 12:07:18', 'admin', NULL, NULL);
INSERT INTO `sys_permission_data_rule` VALUES ('53609e1854f4a87eb23ed23a18a1042c', '4148ec82b6acd69f470bea75fe41c357', '只看当前部门数据', 'sysOrgCode', '=', '#{sys_org_code}', '1', '2019-05-11 19:40:39', 'admin', '2019-05-11 19:40:50', 'admin');
INSERT INTO `sys_permission_data_rule` VALUES ('a7d661ef5ac168b2b162420c6804dac5', '4148ec82b6acd69f470bea75fe41c357', '只看自己的数据', 'createBy', '=', '#{sys_user_code}', '1', '2019-05-11 19:19:05', 'admin', '2019-05-11 19:24:58', 'admin');
INSERT INTO `sys_permission_data_rule` VALUES ('f852d85d47f224990147f2284c0c0005', NULL, '小于', 'test', '<=', '11', '1', '2014-08-20 14:43:52', '8a8ab0b246dc81120146dc8181950052', NULL, NULL);

-- ----------------------------
-- Table structure for sys_position
-- ----------------------------
DROP TABLE IF EXISTS `sys_position`;
CREATE TABLE `sys_position`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务编码',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务名称',
  `post_rank` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职级',
  `company_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司id',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `sys_org_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织机构编码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_position
-- ----------------------------
INSERT INTO `sys_position` VALUES ('1185040064792571906', 'devleader', '研发部经理', '2', NULL, 'admin', '2019-10-18 11:49:03', 'admin', '2020-02-23 22:55:42', 'A01');

-- ----------------------------
-- Table structure for sys_quartz_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_quartz_job`;
CREATE TABLE `sys_quartz_job`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `del_flag` int(1) NULL DEFAULT NULL COMMENT '删除状态',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `job_class_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务类名',
  `cron_expression` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'cron表达式',
  `parameter` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态 0正常 -1停止',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_quartz_job
-- ----------------------------
INSERT INTO `sys_quartz_job` VALUES ('df26ecacf0f75d219d746750fe84bbee', NULL, NULL, 0, 'admin', '2019-01-19 15:09:41', 'org.jeecg.modules.quartz.job.SampleParamJob', '0/1 * * * * ?', 'scott', '带参测试 后台将每隔1秒执行输出日志', -1);
INSERT INTO `sys_quartz_job` VALUES ('a253cdfc811d69fa0efc70d052bc8128', 'admin', '2019-03-30 12:44:48', 0, 'admin', '2019-03-30 12:44:52', 'org.jeecg.modules.quartz.job.SampleJob', '0/1 * * * * ?', NULL, NULL, -1);
INSERT INTO `sys_quartz_job` VALUES ('5b3d2c087ad41aa755fc4f89697b01e7', 'admin', '2019-04-11 19:04:21', 0, 'admin', '2019-04-11 19:49:49', 'org.jeecg.modules.message.job.SendMsgJob', '0/60 * * * * ?', NULL, NULL, -1);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `role_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色编码',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_sys_role_role_code`(`role_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('e51758fa916c881624b046d26bd09230', '人力资源部', 'hr', NULL, 'admin', '2019-01-21 18:07:24', 'admin', '2019-10-18 11:39:43');
INSERT INTO `sys_role` VALUES ('ee8626f80f7c2619917b6236f3a7f02b', '临时角色', 'test', '这是新建的临时角色123', NULL, '2018-12-20 10:59:04', 'admin', '2019-02-19 15:08:37');
INSERT INTO `sys_role` VALUES ('f6817f48af4fb3af11b9e8bf182f618b', '管理员', 'admin', '管理员', NULL, '2018-12-21 18:03:39', 'admin', '2019-05-20 11:40:26');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id',
  `permission_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限id',
  `data_rule_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_group_role_per_id`(`role_id`, `permission_id`) USING BTREE,
  INDEX `index_group_role_id`(`role_id`) USING BTREE,
  INDEX `index_group_per_id`(`permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('00b0748f04d3ea52c8cfa179c1c9d384', '52b0cf022ac4187b2a70dfa4f8b2d940', 'd7d6e2e4e2934f2c9385a623fd98c6f3', NULL);
INSERT INTO `sys_role_permission` VALUES ('00b82058779cca5106fbb84783534c9b', 'f6817f48af4fb3af11b9e8bf182f618b', '4148ec82b6acd69f470bea75fe41c357', NULL);
INSERT INTO `sys_role_permission` VALUES ('0254c0b25694ad5479e6d6935bbc176e', 'f6817f48af4fb3af11b9e8bf182f618b', '944abf0a8fc22fe1f1154a389a574154', NULL);
INSERT INTO `sys_role_permission` VALUES ('09bd4fc30ffe88c4a44ed3868f442719', 'f6817f48af4fb3af11b9e8bf182f618b', 'e6bfd1fcabfd7942fdd05f076d1dad38', NULL);
INSERT INTO `sys_role_permission` VALUES ('0c2d2db76ee3aa81a4fe0925b0f31365', 'f6817f48af4fb3af11b9e8bf182f618b', '024f1fd1283dc632458976463d8984e1', NULL);
INSERT INTO `sys_role_permission` VALUES ('0c6b8facbb1cc874964c87a8cf01e4b1', 'f6817f48af4fb3af11b9e8bf182f618b', '841057b8a1bef8f6b4b20f9a618a7fa6', NULL);
INSERT INTO `sys_role_permission` VALUES ('0c6e1075e422972083c3e854d9af7851', 'f6817f48af4fb3af11b9e8bf182f618b', '08e6b9dc3c04489c8e1ff2ce6f105aa4', NULL);
INSERT INTO `sys_role_permission` VALUES ('0d9d14bc66e9d5e99b0280095fdc8587', 'ee8626f80f7c2619917b6236f3a7f02b', '277bfabef7d76e89b33062b16a9a5020', NULL);
INSERT INTO `sys_role_permission` VALUES ('0dec36b68c234767cd35466efef3b941', 'ee8626f80f7c2619917b6236f3a7f02b', '54dd5457a3190740005c1bfec55b1c34', NULL);
INSERT INTO `sys_role_permission` VALUES ('0e1469997af2d3b97fff56a59ee29eeb', 'f6817f48af4fb3af11b9e8bf182f618b', 'e41b69c57a941a3bbcce45032fe57605', NULL);
INSERT INTO `sys_role_permission` VALUES ('0f861cb988fdc639bb1ab943471f3a72', 'f6817f48af4fb3af11b9e8bf182f618b', '97c8629abc7848eccdb6d77c24bb3ebb', NULL);
INSERT INTO `sys_role_permission` VALUES ('105c2ac10741e56a618a82cd58c461d7', 'e51758fa916c881624b046d26bd09230', '1663f3faba244d16c94552f849627d84', NULL);
INSERT INTO `sys_role_permission` VALUES ('115a6673ae6c0816d3f60de221520274', '21c5a3187763729408b40afb0d0fdfa8', '63b551e81c5956d5c861593d366d8c57', NULL);
INSERT INTO `sys_role_permission` VALUES ('1185039870491439105', 'f6817f48af4fb3af11b9e8bf182f618b', '1174506953255182338', NULL);
INSERT INTO `sys_role_permission` VALUES ('1185039870529187841', 'f6817f48af4fb3af11b9e8bf182f618b', '1174590283938041857', NULL);
INSERT INTO `sys_role_permission` VALUES ('1185039870537576450', 'f6817f48af4fb3af11b9e8bf182f618b', '1166535831146504193', NULL);
INSERT INTO `sys_role_permission` VALUES ('1197431682208206850', 'f6817f48af4fb3af11b9e8bf182f618b', '1192318987661234177', NULL);
INSERT INTO `sys_role_permission` VALUES ('1197795315916271617', 'f6817f48af4fb3af11b9e8bf182f618b', '109c78a583d4693ce2f16551b7786786', NULL);
INSERT INTO `sys_role_permission` VALUES ('1197795316268593154', 'f6817f48af4fb3af11b9e8bf182f618b', '9fe26464838de2ea5e90f2367e35efa0', NULL);
INSERT INTO `sys_role_permission` VALUES ('1209423530518761473', 'f6817f48af4fb3af11b9e8bf182f618b', '1205097455226462210', NULL);
INSERT INTO `sys_role_permission` VALUES ('1209423530594258945', 'f6817f48af4fb3af11b9e8bf182f618b', '1205098241075453953', NULL);
INSERT INTO `sys_role_permission` VALUES ('1209423530606841858', 'f6817f48af4fb3af11b9e8bf182f618b', '1205306106780364802', NULL);
INSERT INTO `sys_role_permission` VALUES ('1209423580355481602', 'f6817f48af4fb3af11b9e8bf182f618b', '190c2b43bec6a5f7a4194a85db67d96a', NULL);
INSERT INTO `sys_role_permission` VALUES ('1209654501558046722', 'f6817f48af4fb3af11b9e8bf182f618b', 'f2849d3814fc97993bfc519ae6bbf049', NULL);
INSERT INTO `sys_role_permission` VALUES ('1231590078632955905', 'f6817f48af4fb3af11b9e8bf182f618b', '1224641973866467330', NULL);
INSERT INTO `sys_role_permission` VALUES ('1231590078658121729', 'f6817f48af4fb3af11b9e8bf182f618b', '1209731624921534465', NULL);
INSERT INTO `sys_role_permission` VALUES ('1231590078662316033', 'f6817f48af4fb3af11b9e8bf182f618b', '1229674163694841857', NULL);
INSERT INTO `sys_role_permission` VALUES ('126ea9faebeec2b914d6d9bef957afb6', 'f6817f48af4fb3af11b9e8bf182f618b', 'f1cb187abf927c88b89470d08615f5ac', NULL);
INSERT INTO `sys_role_permission` VALUES ('145eac8dd88eddbd4ce0a800ab40a92c', 'e51758fa916c881624b046d26bd09230', '08e6b9dc3c04489c8e1ff2ce6f105aa4', NULL);
INSERT INTO `sys_role_permission` VALUES ('154edd0599bd1dc2c7de220b489cd1e2', 'f6817f48af4fb3af11b9e8bf182f618b', '7ac9eb9ccbde2f7a033cd4944272bf1e', NULL);
INSERT INTO `sys_role_permission` VALUES ('165acd6046a0eaf975099f46a3c898ea', 'f6817f48af4fb3af11b9e8bf182f618b', '4f66409ef3bbd69c1d80469d6e2a885e', NULL);
INSERT INTO `sys_role_permission` VALUES ('1664b92dff13e1575e3a929caa2fa14d', 'f6817f48af4fb3af11b9e8bf182f618b', 'd2bbf9ebca5a8fa2e227af97d2da7548', NULL);
INSERT INTO `sys_role_permission` VALUES ('16ef8ed3865ccc6f6306200760896c50', 'ee8626f80f7c2619917b6236f3a7f02b', 'e8af452d8948ea49d37c934f5100ae6a', NULL);
INSERT INTO `sys_role_permission` VALUES ('17ead5b7d97ed365398ab20009a69ea3', '52b0cf022ac4187b2a70dfa4f8b2d940', 'e08cb190ef230d5d4f03824198773950', NULL);
INSERT INTO `sys_role_permission` VALUES ('1ac1688ef8456f384091a03d88a89ab1', '52b0cf022ac4187b2a70dfa4f8b2d940', '693ce69af3432bd00be13c3971a57961', NULL);
INSERT INTO `sys_role_permission` VALUES ('1af4babaa4227c3cbb830bc5eb513abb', 'ee8626f80f7c2619917b6236f3a7f02b', 'e08cb190ef230d5d4f03824198773950', NULL);
INSERT INTO `sys_role_permission` VALUES ('1ba162bbc2076c25561f8622f610d5bf', 'ee8626f80f7c2619917b6236f3a7f02b', 'aedbf679b5773c1f25e9f7b10111da73', NULL);
INSERT INTO `sys_role_permission` VALUES ('1c1dbba68ef1817e7fb19c822d2854e8', 'f6817f48af4fb3af11b9e8bf182f618b', 'fb367426764077dcf94640c843733985', NULL);
INSERT INTO `sys_role_permission` VALUES ('1c55c4ced20765b8ebab383caa60f0b6', 'e51758fa916c881624b046d26bd09230', 'fb367426764077dcf94640c843733985', NULL);
INSERT INTO `sys_role_permission` VALUES ('1e099baeae01b747d67aca06bdfc34d1', 'e51758fa916c881624b046d26bd09230', '6ad53fd1b220989a8b71ff482d683a5a', NULL);
INSERT INTO `sys_role_permission` VALUES ('1e47db875601fd97723254046b5bba90', 'f6817f48af4fb3af11b9e8bf182f618b', 'baf16b7174bd821b6bab23fa9abb200d', NULL);
INSERT INTO `sys_role_permission` VALUES ('1fe4d408b85f19618c15bcb768f0ec22', '1750a8fb3e6d90cb7957c02de1dc8e59', '9502685863ab87f0ad1134142788a385', NULL);
INSERT INTO `sys_role_permission` VALUES ('20e53c87a785688bdc0a5bb6de394ef1', 'f6817f48af4fb3af11b9e8bf182f618b', '540a2936940846cb98114ffb0d145cb8', NULL);
INSERT INTO `sys_role_permission` VALUES ('248d288586c6ff3bd14381565df84163', '52b0cf022ac4187b2a70dfa4f8b2d940', '3f915b2769fc80648e92d04e84ca059d', NULL);
INSERT INTO `sys_role_permission` VALUES ('25491ecbd5a9b34f09c8bc447a10ede1', 'f6817f48af4fb3af11b9e8bf182f618b', 'd07a2c87a451434c99ab06296727ec4f', NULL);
INSERT INTO `sys_role_permission` VALUES ('25f5443f19c34d99718a016d5f54112e', 'ee8626f80f7c2619917b6236f3a7f02b', '6e73eb3c26099c191bf03852ee1310a1', NULL);
INSERT INTO `sys_role_permission` VALUES ('27489816708b18859768dfed5945c405', 'a799c3b1b12dd3ed4bd046bfaef5fe6e', '9502685863ab87f0ad1134142788a385', NULL);
INSERT INTO `sys_role_permission` VALUES ('2779cdea8367fff37db26a42c1a1f531', 'f6817f48af4fb3af11b9e8bf182f618b', 'fef097f3903caf3a3c3a6efa8de43fbb', NULL);
INSERT INTO `sys_role_permission` VALUES ('296f9c75ca0e172ae5ce4c1022c996df', '646c628b2b8295fbdab2d34044de0354', '732d48f8e0abe99fe6a23d18a3171cd1', NULL);
INSERT INTO `sys_role_permission` VALUES ('29fb4d37aa29b9fa400f389237cf9fe7', 'ee8626f80f7c2619917b6236f3a7f02b', '05b3c82ddb2536a4a5ee1a4c46b5abef', NULL);
INSERT INTO `sys_role_permission` VALUES ('29fb6b0ad59a7e911c8d27e0bdc42d23', 'f6817f48af4fb3af11b9e8bf182f618b', '9a90363f216a6a08f32eecb3f0bf12a3', NULL);
INSERT INTO `sys_role_permission` VALUES ('2ad37346c1b83ddeebc008f6987b2227', 'f6817f48af4fb3af11b9e8bf182f618b', '8d1ebd663688965f1fd86a2f0ead3416', NULL);
INSERT INTO `sys_role_permission` VALUES ('2c462293cbb0eab7e8ae0a3600361b5f', '52b0cf022ac4187b2a70dfa4f8b2d940', '9502685863ab87f0ad1134142788a385', NULL);
INSERT INTO `sys_role_permission` VALUES ('2dc1a0eb5e39aaa131ddd0082a492d76', 'ee8626f80f7c2619917b6236f3a7f02b', '08e6b9dc3c04489c8e1ff2ce6f105aa4', NULL);
INSERT INTO `sys_role_permission` VALUES ('2ea2382af618ba7d1e80491a0185fb8a', 'ee8626f80f7c2619917b6236f3a7f02b', 'f23d9bfff4d9aa6b68569ba2cff38415', NULL);
INSERT INTO `sys_role_permission` VALUES ('2fcfa2ac3dcfadc7c67107dae9a0de6d', 'ee8626f80f7c2619917b6236f3a7f02b', '73678f9daa45ed17a3674131b03432fb', NULL);
INSERT INTO `sys_role_permission` VALUES ('2fdaed22dfa4c8d4629e44ef81688c6a', '52b0cf022ac4187b2a70dfa4f8b2d940', 'aedbf679b5773c1f25e9f7b10111da73', NULL);
INSERT INTO `sys_role_permission` VALUES ('300c462b7fec09e2ff32574ef8b3f0bd', '52b0cf022ac4187b2a70dfa4f8b2d940', '2a470fc0c3954d9dbb61de6d80846549', NULL);
INSERT INTO `sys_role_permission` VALUES ('326181da3248a62a05e872119a462be1', 'ee8626f80f7c2619917b6236f3a7f02b', '3f915b2769fc80648e92d04e84ca059d', NULL);
INSERT INTO `sys_role_permission` VALUES ('3369650f5072b330543f8caa556b1b33', 'e51758fa916c881624b046d26bd09230', 'a400e4f4d54f79bf5ce160ae432231af', NULL);
INSERT INTO `sys_role_permission` VALUES ('35a7e156c20e93aa7e825fe514bf9787', 'e51758fa916c881624b046d26bd09230', 'c6cf95444d80435eb37b2f9db3971ae6', NULL);
INSERT INTO `sys_role_permission` VALUES ('35ac7cae648de39eb56213ca1b649713', '52b0cf022ac4187b2a70dfa4f8b2d940', 'b1cb0a3fedf7ed0e4653cb5a229837ee', NULL);
INSERT INTO `sys_role_permission` VALUES ('37112f4d372541e105473f18da3dc50d', 'ee8626f80f7c2619917b6236f3a7f02b', 'a400e4f4d54f79bf5ce160ae432231af', NULL);
INSERT INTO `sys_role_permission` VALUES ('37789f70cd8bd802c4a69e9e1f633eaa', 'ee8626f80f7c2619917b6236f3a7f02b', 'ae4fed059f67086fd52a73d913cf473d', NULL);
INSERT INTO `sys_role_permission` VALUES ('381504a717cb3ce77dcd4070c9689a7e', 'ee8626f80f7c2619917b6236f3a7f02b', '4f84f9400e5e92c95f05b554724c2b58', NULL);
INSERT INTO `sys_role_permission` VALUES ('38a2e55db0960262800576e34b3af44c', 'f6817f48af4fb3af11b9e8bf182f618b', '5c2f42277948043026b7a14692456828', NULL);
INSERT INTO `sys_role_permission` VALUES ('38dd7a19711e7ffe864232954c06fae9', 'e51758fa916c881624b046d26bd09230', 'd2bbf9ebca5a8fa2e227af97d2da7548', NULL);
INSERT INTO `sys_role_permission` VALUES ('3b1886f727ac503c93fecdd06dcb9622', 'f6817f48af4fb3af11b9e8bf182f618b', 'c431130c0bc0ec71b0a5be37747bb36a', NULL);
INSERT INTO `sys_role_permission` VALUES ('3de2a60c7e42a521fecf6fcc5cb54978', 'f6817f48af4fb3af11b9e8bf182f618b', '2d83d62bd2544b8994c8f38cf17b0ddf', NULL);
INSERT INTO `sys_role_permission` VALUES ('3e4e38f748b8d87178dd62082e5b7b60', 'f6817f48af4fb3af11b9e8bf182f618b', '7960961b0063228937da5fa8dd73d371', NULL);
INSERT INTO `sys_role_permission` VALUES ('3e563751942b0879c88ca4de19757b50', '1750a8fb3e6d90cb7957c02de1dc8e59', '58857ff846e61794c69208e9d3a85466', NULL);
INSERT INTO `sys_role_permission` VALUES ('3f1d04075e3c3254666a4138106a4e51', 'f6817f48af4fb3af11b9e8bf182f618b', '3fac0d3c9cd40fa53ab70d4c583821f8', NULL);
INSERT INTO `sys_role_permission` VALUES ('412e2de37a35b3442d68db8dd2f3c190', '52b0cf022ac4187b2a70dfa4f8b2d940', 'f1cb187abf927c88b89470d08615f5ac', NULL);
INSERT INTO `sys_role_permission` VALUES ('4204f91fb61911ba8ce40afa7c02369f', 'f6817f48af4fb3af11b9e8bf182f618b', '3f915b2769fc80648e92d04e84ca059d', NULL);
INSERT INTO `sys_role_permission` VALUES ('439568ff7db6f329bf6dd45b3dfc9456', 'f6817f48af4fb3af11b9e8bf182f618b', '7593c9e3523a17bca83b8d7fe8a34e58', NULL);
INSERT INTO `sys_role_permission` VALUES ('444126230885d5d38b8fa6072c9f43f8', 'f6817f48af4fb3af11b9e8bf182f618b', 'f780d0d3083d849ccbdb1b1baee4911d', NULL);
INSERT INTO `sys_role_permission` VALUES ('445656dd187bd8a71605f4bbab1938a3', 'f6817f48af4fb3af11b9e8bf182f618b', '020b06793e4de2eee0007f603000c769', NULL);
INSERT INTO `sys_role_permission` VALUES ('44b5a73541bcb854dd5d38c6d1fb93a1', 'ee8626f80f7c2619917b6236f3a7f02b', '418964ba087b90a84897b62474496b93', NULL);
INSERT INTO `sys_role_permission` VALUES ('455cdb482457f529b79b479a2ff74427', 'f6817f48af4fb3af11b9e8bf182f618b', 'e1979bb53e9ea51cecc74d86fd9d2f64', NULL);
INSERT INTO `sys_role_permission` VALUES ('459aa2e7021b435b4d65414cfbc71c66', 'e51758fa916c881624b046d26bd09230', '4148ec82b6acd69f470bea75fe41c357', NULL);
INSERT INTO `sys_role_permission` VALUES ('45a358bb738782d1a0edbf7485e81459', 'f6817f48af4fb3af11b9e8bf182f618b', '0ac2ad938963b6c6d1af25477d5b8b51', NULL);
INSERT INTO `sys_role_permission` VALUES ('4c0940badae3ef9231ee9d042338f2a4', 'e51758fa916c881624b046d26bd09230', '2a470fc0c3954d9dbb61de6d80846549', NULL);
INSERT INTO `sys_role_permission` VALUES ('4d56ce2f67c94b74a1d3abdbea340e42', 'ee8626f80f7c2619917b6236f3a7f02b', 'd86f58e7ab516d3bc6bfb1fe10585f97', NULL);
INSERT INTO `sys_role_permission` VALUES ('4dab5a06acc8ef3297889872caa74747', 'f6817f48af4fb3af11b9e8bf182f618b', 'ffb423d25cc59dcd0532213c4a518261', NULL);
INSERT INTO `sys_role_permission` VALUES ('4e0a37ed49524df5f08fc6593aee875c', 'f6817f48af4fb3af11b9e8bf182f618b', 'f23d9bfff4d9aa6b68569ba2cff38415', NULL);
INSERT INTO `sys_role_permission` VALUES ('4ea403fc1d19feb871c8bdd9f94a4ecc', 'f6817f48af4fb3af11b9e8bf182f618b', '2e42e3835c2b44ec9f7bc26c146ee531', NULL);
INSERT INTO `sys_role_permission` VALUES ('4f254549d9498f06f4cc9b23f3e2c070', 'f6817f48af4fb3af11b9e8bf182f618b', '93d5cfb4448f11e9916698e7f462b4b6', NULL);
INSERT INTO `sys_role_permission` VALUES ('4f2fd4a190db856e21476de2704bbd99', 'f6817f48af4fb3af11b9e8bf182f618b', '1a0811914300741f4e11838ff37a1d3a', NULL);
INSERT INTO `sys_role_permission` VALUES ('4faad8ff93cb2b5607cd3d07c1b624ee', 'a799c3b1b12dd3ed4bd046bfaef5fe6e', '70b8f33da5f39de1981bf89cf6c99792', NULL);
INSERT INTO `sys_role_permission` VALUES ('504e326de3f03562cdd186748b48a8c7', 'f6817f48af4fb3af11b9e8bf182f618b', '027aee69baee98a0ed2e01806e89c891', NULL);
INSERT INTO `sys_role_permission` VALUES ('520b5989e6fe4a302a573d4fee12a40a', 'f6817f48af4fb3af11b9e8bf182f618b', '6531cf3421b1265aeeeabaab5e176e6d', NULL);
INSERT INTO `sys_role_permission` VALUES ('54fdf85e52807bdb32ce450814abc256', 'f6817f48af4fb3af11b9e8bf182f618b', 'cc50656cf9ca528e6f2150eba4714ad2', NULL);
INSERT INTO `sys_role_permission` VALUES ('57c0b3a547b815ea3ec8e509b08948b3', '1750a8fb3e6d90cb7957c02de1dc8e59', '3f915b2769fc80648e92d04e84ca059d', NULL);
INSERT INTO `sys_role_permission` VALUES ('593ee05c4fe4645c7826b7d5e14f23ec', '52b0cf022ac4187b2a70dfa4f8b2d940', '8fb8172747a78756c11916216b8b8066', NULL);
INSERT INTO `sys_role_permission` VALUES ('5affc85021fcba07d81c09a6fdfa8dc6', 'ee8626f80f7c2619917b6236f3a7f02b', '078f9558cdeab239aecb2bda1a8ed0d1', NULL);
INSERT INTO `sys_role_permission` VALUES ('5d230e6cd2935c4117f6cb9a7a749e39', 'f6817f48af4fb3af11b9e8bf182f618b', 'fc810a2267dd183e4ef7c71cc60f4670', NULL);
INSERT INTO `sys_role_permission` VALUES ('5de6871fadb4fe1cdd28989da0126b07', 'f6817f48af4fb3af11b9e8bf182f618b', 'a400e4f4d54f79bf5ce160a3432231af', NULL);
INSERT INTO `sys_role_permission` VALUES ('5e4015a9a641cbf3fb5d28d9f885d81a', 'f6817f48af4fb3af11b9e8bf182f618b', '2dbbafa22cda07fa5d169d741b81fe12', NULL);
INSERT INTO `sys_role_permission` VALUES ('5e634a89f75b7a421c02aecfd520325f', 'e51758fa916c881624b046d26bd09230', '339329ed54cf255e1f9392e84f136901', NULL);
INSERT INTO `sys_role_permission` VALUES ('5e74637c4bec048d1880ad0bd1b00552', 'e51758fa916c881624b046d26bd09230', 'a400e4f4d54f79bf5ce160a3432231af', NULL);
INSERT INTO `sys_role_permission` VALUES ('5fc194b709336d354640fe29fefd65a3', 'a799c3b1b12dd3ed4bd046bfaef5fe6e', '9ba60e626bf2882c31c488aba62b89f0', NULL);
INSERT INTO `sys_role_permission` VALUES ('60eda4b4db138bdb47edbe8e10e71675', 'f6817f48af4fb3af11b9e8bf182f618b', 'fb07ca05a3e13674dbf6d3245956da2e', NULL);
INSERT INTO `sys_role_permission` VALUES ('61835e48f3e675f7d3f5c9dd3a10dcf3', 'f6817f48af4fb3af11b9e8bf182f618b', 'f0675b52d89100ee88472b6800754a08', NULL);
INSERT INTO `sys_role_permission` VALUES ('6451dac67ba4acafb570fd6a03f47460', 'ee8626f80f7c2619917b6236f3a7f02b', 'e3c13679c73a4f829bcff2aba8fd68b1', NULL);
INSERT INTO `sys_role_permission` VALUES ('660fbc40bcb1044738f7cabdf1708c28', 'f6817f48af4fb3af11b9e8bf182f618b', 'b3c824fc22bd953e2eb16ae6914ac8f9', NULL);
INSERT INTO `sys_role_permission` VALUES ('66b202f8f84fe766176b3f51071836ef', 'f6817f48af4fb3af11b9e8bf182f618b', '1367a93f2c410b169faa7abcbad2f77c', NULL);
INSERT INTO `sys_role_permission` VALUES ('6b605c261ffbc8ac8a98ae33579c8c78', 'f6817f48af4fb3af11b9e8bf182f618b', 'fba41089766888023411a978d13c0aa4', NULL);
INSERT INTO `sys_role_permission` VALUES ('6c43fd3f10fdaf2a0646434ae68709b5', 'ee8626f80f7c2619917b6236f3a7f02b', '540a2936940846cb98114ffb0d145cb8', NULL);
INSERT INTO `sys_role_permission` VALUES ('6c74518eb6bb9a353f6a6c459c77e64b', 'f6817f48af4fb3af11b9e8bf182f618b', 'b4dfc7d5dd9e8d5b6dd6d4579b1aa559', NULL);
INSERT INTO `sys_role_permission` VALUES ('6daddafacd7eccb91309530c17c5855d', 'f6817f48af4fb3af11b9e8bf182f618b', 'edfa74d66e8ea63ea432c2910837b150', NULL);
INSERT INTO `sys_role_permission` VALUES ('6fb4c2142498dd6d5b6c014ef985cb66', 'f6817f48af4fb3af11b9e8bf182f618b', '6e73eb3c26099c191bf03852ee1310a1', NULL);
INSERT INTO `sys_role_permission` VALUES ('71a5f54a90aa8c7a250a38b7dba39f6f', 'ee8626f80f7c2619917b6236f3a7f02b', '8fb8172747a78756c11916216b8b8066', NULL);
INSERT INTO `sys_role_permission` VALUES ('737d35f582036cd18bfd4c8e5748eaa4', 'e51758fa916c881624b046d26bd09230', '693ce69af3432bd00be13c3971a57961', NULL);
INSERT INTO `sys_role_permission` VALUES ('7413acf23b56c906aedb5a36fb75bd3a', 'f6817f48af4fb3af11b9e8bf182f618b', 'a4fc7b64b01a224da066bb16230f9c5a', NULL);
INSERT INTO `sys_role_permission` VALUES ('75002588591820806', '16457350655250432', '5129710648430592', NULL);
INSERT INTO `sys_role_permission` VALUES ('75002588604403712', '16457350655250432', '5129710648430593', NULL);
INSERT INTO `sys_role_permission` VALUES ('75002588612792320', '16457350655250432', '40238597734928384', NULL);
INSERT INTO `sys_role_permission` VALUES ('75002588625375232', '16457350655250432', '57009744761589760', NULL);
INSERT INTO `sys_role_permission` VALUES ('75002588633763840', '16457350655250432', '16392452747300864', NULL);
INSERT INTO `sys_role_permission` VALUES ('75002588637958144', '16457350655250432', '16392767785668608', NULL);
INSERT INTO `sys_role_permission` VALUES ('75002588650541056', '16457350655250432', '16439068543946752', NULL);
INSERT INTO `sys_role_permission` VALUES ('76a54a8cc609754360bf9f57e7dbb2db', 'f6817f48af4fb3af11b9e8bf182f618b', 'c65321e57b7949b7a975313220de0422', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277779875336192', '496138616573952', '5129710648430592', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780043108352', '496138616573952', '5129710648430593', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780055691264', '496138616573952', '15701400130424832', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780064079872', '496138616573952', '16678126574637056', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780072468480', '496138616573952', '15701915807518720', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780076662784', '496138616573952', '15708892205944832', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780085051392', '496138616573952', '16678447719911424', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780089245696', '496138616573952', '25014528525733888', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780097634304', '496138616573952', '56898976661639168', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780135383040', '496138616573952', '40238597734928384', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780139577344', '496138616573952', '45235621697949696', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780147965952', '496138616573952', '45235787867885568', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780156354560', '496138616573952', '45235939278065664', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780164743168', '496138616573952', '43117268627886080', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780168937472', '496138616573952', '45236734832676864', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780181520384', '496138616573952', '45237010692050944', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780189908992', '496138616573952', '45237170029465600', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780198297600', '496138616573952', '57009544286441472', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780206686208', '496138616573952', '57009744761589760', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780215074816', '496138616573952', '57009981228060672', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780219269120', '496138616573952', '56309618086776832', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780227657728', '496138616573952', '57212882168844288', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780236046336', '496138616573952', '61560041605435392', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780244434944', '496138616573952', '61560275261722624', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780257017856', '496138616573952', '61560480518377472', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780265406464', '496138616573952', '44986029924421632', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780324126720', '496138616573952', '45235228800716800', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780332515328', '496138616573952', '45069342940860416', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780340903937', '496138616573952', '5129710648430594', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780349292544', '496138616573952', '16687383932047360', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780357681152', '496138616573952', '16689632049631232', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780366069760', '496138616573952', '16689745006432256', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780370264064', '496138616573952', '16689883993083904', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780374458369', '496138616573952', '16690313745666048', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780387041280', '496138616573952', '5129710648430595', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780395429888', '496138616573952', '16694861252005888', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780403818496', '496138616573952', '16695107491205120', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780412207104', '496138616573952', '16695243126607872', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780420595712', '496138616573952', '75002207560273920', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780428984320', '496138616573952', '76215889006956544', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780433178624', '496138616573952', '76216071333351424', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780441567232', '496138616573952', '76216264070008832', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780449955840', '496138616573952', '76216459709124608', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780458344448', '496138616573952', '76216594207870976', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780466733056', '496138616573952', '76216702639017984', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780475121664', '496138616573952', '58480609315524608', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780483510272', '496138616573952', '61394706252173312', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780491898880', '496138616573952', '61417744146370560', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780496093184', '496138616573952', '76606430504816640', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780504481792', '496138616573952', '76914082455752704', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780508676097', '496138616573952', '76607201262702592', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780517064704', '496138616573952', '39915540965232640', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780525453312', '496138616573952', '41370251991977984', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780538036224', '496138616573952', '45264987354042368', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780546424832', '496138616573952', '45265487029866496', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780554813440', '496138616573952', '45265762415284224', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780559007744', '496138616573952', '45265886315024384', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780567396352', '496138616573952', '45266070000373760', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780571590656', '496138616573952', '41363147411427328', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780579979264', '496138616573952', '41363537456533504', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780588367872', '496138616573952', '41364927394353152', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780596756480', '496138616573952', '41371711400054784', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780605145088', '496138616573952', '41469219249852416', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780613533696', '496138616573952', '39916171171991552', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780621922304', '496138616573952', '39918482854252544', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780630310912', '496138616573952', '41373430515240960', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780718391296', '496138616573952', '41375330996326400', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780722585600', '496138616573952', '63741744973352960', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780730974208', '496138616573952', '42082442672082944', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780739362816', '496138616573952', '41376192166629376', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780747751424', '496138616573952', '41377034236071936', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780756140032', '496138616573952', '56911328312299520', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780764528640', '496138616573952', '41378916912336896', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780768722944', '496138616573952', '63482475359244288', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780772917249', '496138616573952', '64290663792906240', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780785500160', '496138616573952', '66790433014943744', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780789694464', '496138616573952', '42087054753927168', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780798083072', '496138616573952', '67027338952445952', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780806471680', '496138616573952', '67027909637836800', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780810665985', '496138616573952', '67042515441684480', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780823248896', '496138616573952', '67082402312228864', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780827443200', '496138616573952', '16392452747300864', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780835831808', '496138616573952', '16392767785668608', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780840026112', '496138616573952', '16438800255291392', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780844220417', '496138616573952', '16438962738434048', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277780852609024', '496138616573952', '16439068543946752', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860062040064', '496138616573953', '5129710648430592', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860070428672', '496138616573953', '5129710648430593', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860078817280', '496138616573953', '40238597734928384', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860091400192', '496138616573953', '43117268627886080', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860099788800', '496138616573953', '57009744761589760', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860112371712', '496138616573953', '56309618086776832', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860120760320', '496138616573953', '44986029924421632', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860129148928', '496138616573953', '5129710648430594', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860141731840', '496138616573953', '5129710648430595', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860150120448', '496138616573953', '75002207560273920', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860158509056', '496138616573953', '58480609315524608', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860162703360', '496138616573953', '76606430504816640', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860171091968', '496138616573953', '76914082455752704', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860179480576', '496138616573953', '76607201262702592', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860187869184', '496138616573953', '39915540965232640', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860196257792', '496138616573953', '41370251991977984', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860204646400', '496138616573953', '41363147411427328', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860208840704', '496138616573953', '41371711400054784', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860213035009', '496138616573953', '39916171171991552', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860221423616', '496138616573953', '39918482854252544', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860225617920', '496138616573953', '41373430515240960', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860234006528', '496138616573953', '41375330996326400', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860242395136', '496138616573953', '63741744973352960', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860250783744', '496138616573953', '42082442672082944', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860254978048', '496138616573953', '41376192166629376', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860263366656', '496138616573953', '41377034236071936', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860271755264', '496138616573953', '56911328312299520', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860313698304', '496138616573953', '41378916912336896', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860322086912', '496138616573953', '63482475359244288', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860326281216', '496138616573953', '64290663792906240', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860334669824', '496138616573953', '66790433014943744', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860343058432', '496138616573953', '42087054753927168', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860347252736', '496138616573953', '67027338952445952', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860351447041', '496138616573953', '67027909637836800', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860359835648', '496138616573953', '67042515441684480', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860364029952', '496138616573953', '67082402312228864', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860368224256', '496138616573953', '16392452747300864', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860372418560', '496138616573953', '16392767785668608', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860376612865', '496138616573953', '16438800255291392', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860385001472', '496138616573953', '16438962738434048', NULL);
INSERT INTO `sys_role_permission` VALUES ('77277860389195776', '496138616573953', '16439068543946752', NULL);
INSERT INTO `sys_role_permission` VALUES ('7750f9be48ee09cd561fce718219a3e2', 'ee8626f80f7c2619917b6236f3a7f02b', '882a73768cfd7f78f3a37584f7299656', NULL);
INSERT INTO `sys_role_permission` VALUES ('7a5d31ba48fe3fb1266bf186dc5f7ba7', '52b0cf022ac4187b2a70dfa4f8b2d940', '58857ff846e61794c69208e9d3a85466', NULL);
INSERT INTO `sys_role_permission` VALUES ('7a6bca9276c128309c80d21e795c66c6', 'f6817f48af4fb3af11b9e8bf182f618b', '54097c6a3cf50fad0793a34beff1efdf', NULL);
INSERT INTO `sys_role_permission` VALUES ('7ca833caa5eac837b7200d8b6de8b2e3', 'f6817f48af4fb3af11b9e8bf182f618b', 'fedfbf4420536cacc0218557d263dfea', NULL);
INSERT INTO `sys_role_permission` VALUES ('7d2ea745950be3357747ec7750c31c57', 'ee8626f80f7c2619917b6236f3a7f02b', '2a470fc0c3954d9dbb61de6d80846549', NULL);
INSERT INTO `sys_role_permission` VALUES ('7de42bdc0b8c5446b7d428c66a7abc12', '52b0cf022ac4187b2a70dfa4f8b2d940', '54dd5457a3190740005c1bfec55b1c34', NULL);
INSERT INTO `sys_role_permission` VALUES ('7e19d90cec0dd87aaef351b9ff8f4902', '646c628b2b8295fbdab2d34044de0354', 'f9d3f4f27653a71c52faa9fb8070fbe7', NULL);
INSERT INTO `sys_role_permission` VALUES ('7f862c47003eb20e8bad05f506371f92', 'ee8626f80f7c2619917b6236f3a7f02b', 'd7d6e2e4e2934f2c9385a623fd98c6f3', NULL);
INSERT INTO `sys_role_permission` VALUES ('812ed54661b1a24b81b58974691a73f5', 'e51758fa916c881624b046d26bd09230', 'e6bfd1fcabfd7942fdd05f076d1dad38', NULL);
INSERT INTO `sys_role_permission` VALUES ('83f704524b21b6a3ae324b8736c65333', 'ee8626f80f7c2619917b6236f3a7f02b', '7ac9eb9ccbde2f7a033cd4944272bf1e', NULL);
INSERT INTO `sys_role_permission` VALUES ('84d32474316a43b01256d6644e6e7751', 'ee8626f80f7c2619917b6236f3a7f02b', 'ec8d607d0156e198b11853760319c646', NULL);
INSERT INTO `sys_role_permission` VALUES ('84eac2f113c23737128fb099d1d1da89', 'f6817f48af4fb3af11b9e8bf182f618b', '03dc3d93261dda19fc86dd7ca486c6cf', NULL);
INSERT INTO `sys_role_permission` VALUES ('85755a6c0bdff78b3860b52d35310c7f', 'e51758fa916c881624b046d26bd09230', 'c65321e57b7949b7a975313220de0422', NULL);
INSERT INTO `sys_role_permission` VALUES ('86060e2867a5049d8a80d9fe5d8bc28b', 'f6817f48af4fb3af11b9e8bf182f618b', '765dd244f37b804e3d00f475fd56149b', NULL);
INSERT INTO `sys_role_permission` VALUES ('8703a2410cddb713c33232ce16ec04b9', 'ee8626f80f7c2619917b6236f3a7f02b', '1367a93f2c410b169faa7abcbad2f77c', NULL);
INSERT INTO `sys_role_permission` VALUES ('884f147c20e003cc80ed5b7efa598cbe', 'f6817f48af4fb3af11b9e8bf182f618b', 'e5973686ed495c379d829ea8b2881fc6', NULL);
INSERT INTO `sys_role_permission` VALUES ('885c1a827383e5b2c6c4f8ca72a7b493', 'ee8626f80f7c2619917b6236f3a7f02b', '4148ec82b6acd69f470bea75fe41c357', NULL);
INSERT INTO `sys_role_permission` VALUES ('8a60df8d8b4c9ee5fa63f48aeee3ec00', '1750a8fb3e6d90cb7957c02de1dc8e59', 'd7d6e2e4e2934f2c9385a623fd98c6f3', NULL);
INSERT INTO `sys_role_permission` VALUES ('8b09925bdc194ab7f3559cd3a7ea0507', 'f6817f48af4fb3af11b9e8bf182f618b', 'ebb9d82ea16ad864071158e0c449d186', NULL);
INSERT INTO `sys_role_permission` VALUES ('8b1e326791375f325d3e6b797753b65e', 'ee8626f80f7c2619917b6236f3a7f02b', '2dbbafa22cda07fa5d169d741b81fe12', NULL);
INSERT INTO `sys_role_permission` VALUES ('8ce1022dac4e558ff9694600515cf510', '1750a8fb3e6d90cb7957c02de1dc8e59', '08e6b9dc3c04489c8e1ff2ce6f105aa4', NULL);
INSERT INTO `sys_role_permission` VALUES ('8d154c2382a8ae5c8d1b84bd38df2a93', 'f6817f48af4fb3af11b9e8bf182f618b', 'd86f58e7ab516d3bc6bfb1fe10585f97', NULL);
INSERT INTO `sys_role_permission` VALUES ('8d848ca7feec5b7ebb3ecb32b2c8857a', '52b0cf022ac4187b2a70dfa4f8b2d940', '4148ec82b6acd69f470bea75fe41c357', NULL);
INSERT INTO `sys_role_permission` VALUES ('8dd64f65a1014196078d0882f767cd85', 'f6817f48af4fb3af11b9e8bf182f618b', 'e3c13679c73a4f829bcff2aba8fd68b1', NULL);
INSERT INTO `sys_role_permission` VALUES ('8e3dc1671abad4f3c83883b194d2e05a', 'f6817f48af4fb3af11b9e8bf182f618b', 'b1cb0a3fedf7ed0e4653cb5a229837ee', NULL);
INSERT INTO `sys_role_permission` VALUES ('8eec2c510f1ac9c5eee26c041b1f00ca', 'ee8626f80f7c2619917b6236f3a7f02b', '58857ff846e61794c69208e9d3a85466', NULL);
INSERT INTO `sys_role_permission` VALUES ('8f762ff80253f634b08cf59a77742ba4', 'ee8626f80f7c2619917b6236f3a7f02b', '9502685863ab87f0ad1134142788a385', NULL);
INSERT INTO `sys_role_permission` VALUES ('903b790e6090414343502c6dc393b7c9', 'ee8626f80f7c2619917b6236f3a7f02b', 'de13e0f6328c069748de7399fcc1dbbd', NULL);
INSERT INTO `sys_role_permission` VALUES ('905bf419332ebcb83863603b3ebe30f0', 'f6817f48af4fb3af11b9e8bf182f618b', '8fb8172747a78756c11916216b8b8066', NULL);
INSERT INTO `sys_role_permission` VALUES ('90996d56357730e173e636b99fc48bea', 'ee8626f80f7c2619917b6236f3a7f02b', 'fb07ca05a3e13674dbf6d3245956da2e', NULL);
INSERT INTO `sys_role_permission` VALUES ('90e1c607a0631364eec310f3cc4acebd', 'ee8626f80f7c2619917b6236f3a7f02b', '4f66409ef3bbd69c1d80469d6e2a885e', NULL);
INSERT INTO `sys_role_permission` VALUES ('9264104cee9b10c96241d527b2d0346d', '1750a8fb3e6d90cb7957c02de1dc8e59', '54dd5457a3190740005c1bfec55b1c34', NULL);
INSERT INTO `sys_role_permission` VALUES ('9380121ca9cfee4b372194630fce150e', 'f6817f48af4fb3af11b9e8bf182f618b', '65a8f489f25a345836b7f44b1181197a', NULL);
INSERT INTO `sys_role_permission` VALUES ('94911fef73a590f6824105ebf9b6cab3', 'f6817f48af4fb3af11b9e8bf182f618b', '8b3bff2eee6f1939147f5c68292a1642', NULL);
INSERT INTO `sys_role_permission` VALUES ('9700d20dbc1ae3cbf7de1c810b521fe6', 'f6817f48af4fb3af11b9e8bf182f618b', 'ec8d607d0156e198b11853760319c646', NULL);
INSERT INTO `sys_role_permission` VALUES ('980171fda43adfe24840959b1d048d4d', 'f6817f48af4fb3af11b9e8bf182f618b', 'd7d6e2e4e2934f2c9385a623fd98c6f3', NULL);
INSERT INTO `sys_role_permission` VALUES ('987c23b70873bd1d6dca52f30aafd8c2', 'f6817f48af4fb3af11b9e8bf182f618b', '00a2a0ae65cdca5e93209cdbde97cbe6', NULL);
INSERT INTO `sys_role_permission` VALUES ('98f02353f91dd569e3c6b8fd6b4f4034', 'ee8626f80f7c2619917b6236f3a7f02b', '6531cf3421b1265aeeeabaab5e176e6d', NULL);
INSERT INTO `sys_role_permission` VALUES ('9b2ad767f9861e64a20b097538feafd3', 'f6817f48af4fb3af11b9e8bf182f618b', '73678f9daa45ed17a3674131b03432fb', NULL);
INSERT INTO `sys_role_permission` VALUES ('9d8772c310b675ae43eacdbc6c7fa04a', 'a799c3b1b12dd3ed4bd046bfaef5fe6e', '1663f3faba244d16c94552f849627d84', NULL);
INSERT INTO `sys_role_permission` VALUES ('9d980ec0489040e631a9c24a6af42934', 'f6817f48af4fb3af11b9e8bf182f618b', '05b3c82ddb2536a4a5ee1a4c46b5abef', NULL);
INSERT INTO `sys_role_permission` VALUES ('9f8311ecccd44e079723098cf2ffe1cc', '1750a8fb3e6d90cb7957c02de1dc8e59', '693ce69af3432bd00be13c3971a57961', NULL);
INSERT INTO `sys_role_permission` VALUES ('a034ed7c38c996b880d3e78f586fe0ae', 'f6817f48af4fb3af11b9e8bf182f618b', 'c89018ea6286e852b424466fd92a2ffc', NULL);
INSERT INTO `sys_role_permission` VALUES ('a098e2acc3f90316f161f6648d085640', 'ee8626f80f7c2619917b6236f3a7f02b', 'e6bfd1fcabfd7942fdd05f076d1dad38', NULL);
INSERT INTO `sys_role_permission` VALUES ('a307a9349ad64a2eff8ab69582fa9be4', 'f6817f48af4fb3af11b9e8bf182f618b', '0620e402857b8c5b605e1ad9f4b89350', NULL);
INSERT INTO `sys_role_permission` VALUES ('a5d25fdb3c62904a8474182706ce11a0', 'f6817f48af4fb3af11b9e8bf182f618b', '418964ba087b90a84897b62474496b93', NULL);
INSERT INTO `sys_role_permission` VALUES ('a66feaaf128417ad762e946abccf27ec', 'ee8626f80f7c2619917b6236f3a7f02b', 'c6cf95444d80435eb37b2f9db3971ae6', NULL);
INSERT INTO `sys_role_permission` VALUES ('a72c31a3913c736d4eca11d13be99183', 'e51758fa916c881624b046d26bd09230', 'a44c30db536349e91106223957e684eb', NULL);
INSERT INTO `sys_role_permission` VALUES ('a7ab87eac0f8fafa2efa4b1f9351923f', 'ee8626f80f7c2619917b6236f3a7f02b', 'fedfbf4420536cacc0218557d263dfea', NULL);
INSERT INTO `sys_role_permission` VALUES ('abdc324a2df9f13ee6e73d44c6e62bc8', 'ee8626f80f7c2619917b6236f3a7f02b', 'f1cb187abf927c88b89470d08615f5ac', NULL);
INSERT INTO `sys_role_permission` VALUES ('acacce4417e5d7f96a9c3be2ded5b4be', 'f6817f48af4fb3af11b9e8bf182f618b', 'f9d3f4f27653a71c52faa9fb8070fbe7', NULL);
INSERT INTO `sys_role_permission` VALUES ('ae1852fb349d8513eb3fdc173da3ee56', 'f6817f48af4fb3af11b9e8bf182f618b', '8d4683aacaa997ab86b966b464360338', NULL);
INSERT INTO `sys_role_permission` VALUES ('aefc8c22e061171806e59cd222f6b7e1', '52b0cf022ac4187b2a70dfa4f8b2d940', 'e8af452d8948ea49d37c934f5100ae6a', NULL);
INSERT INTO `sys_role_permission` VALUES ('af60ac8fafd807ed6b6b354613b9ccbc', 'f6817f48af4fb3af11b9e8bf182f618b', '58857ff846e61794c69208e9d3a85466', NULL);
INSERT INTO `sys_role_permission` VALUES ('b0c8a20800b8bf1ebdd7be473bceb44f', 'f6817f48af4fb3af11b9e8bf182f618b', '58b9204feaf07e47284ddb36cd2d8468', NULL);
INSERT INTO `sys_role_permission` VALUES ('b128ebe78fa5abb54a3a82c6689bdca3', 'f6817f48af4fb3af11b9e8bf182f618b', 'aedbf679b5773c1f25e9f7b10111da73', NULL);
INSERT INTO `sys_role_permission` VALUES ('b131ebeafcfd059f3c7e542606ea9ff5', 'ee8626f80f7c2619917b6236f3a7f02b', 'e5973686ed495c379d829ea8b2881fc6', NULL);
INSERT INTO `sys_role_permission` VALUES ('b21b07951bb547b09cc85624a841aea0', 'f6817f48af4fb3af11b9e8bf182f618b', '4356a1a67b564f0988a484f5531fd4d9', NULL);
INSERT INTO `sys_role_permission` VALUES ('b2b2dcfff6986d3d7f890ea62d474651', 'ee8626f80f7c2619917b6236f3a7f02b', '200006f0edf145a2b50eacca07585451', NULL);
INSERT INTO `sys_role_permission` VALUES ('b495a46fa0e0d4637abe0db7fd12fe1a', 'ee8626f80f7c2619917b6236f3a7f02b', '717f6bee46f44a3897eca9abd6e2ec44', NULL);
INSERT INTO `sys_role_permission` VALUES ('b64c4ab9cd9a2ea8ac1e9db5fb7cf522', 'f6817f48af4fb3af11b9e8bf182f618b', '2aeddae571695cd6380f6d6d334d6e7d', NULL);
INSERT INTO `sys_role_permission` VALUES ('bbec16ad016efec9ea2def38f4d3d9dc', 'f6817f48af4fb3af11b9e8bf182f618b', '13212d3416eb690c2e1d5033166ff47a', NULL);
INSERT INTO `sys_role_permission` VALUES ('bd30561f141f07827b836878137fddd8', 'e51758fa916c881624b046d26bd09230', '65a8f489f25a345836b7f44b1181197a', NULL);
INSERT INTO `sys_role_permission` VALUES ('be8e5a9080569e59863f20c4c57a8e22', 'f6817f48af4fb3af11b9e8bf182f618b', '22d6a3d39a59dd7ea9a30acfa6bfb0a5', NULL);
INSERT INTO `sys_role_permission` VALUES ('bea2986432079d89203da888d99b3f16', 'f6817f48af4fb3af11b9e8bf182f618b', '54dd5457a3190740005c1bfec55b1c34', NULL);
INSERT INTO `sys_role_permission` VALUES ('c09373ebfc73fb5740db5ff02cba4f91', 'f6817f48af4fb3af11b9e8bf182f618b', '339329ed54cf255e1f9392e84f136901', NULL);
INSERT INTO `sys_role_permission` VALUES ('c56fb1658ee5f7476380786bf5905399', 'f6817f48af4fb3af11b9e8bf182f618b', 'de13e0f6328c069748de7399fcc1dbbd', NULL);
INSERT INTO `sys_role_permission` VALUES ('c689539d20a445b0896270290c58d01f', 'e51758fa916c881624b046d26bd09230', '13212d3416eb690c2e1d5033166ff47a', NULL);
INSERT INTO `sys_role_permission` VALUES ('c6fee38d293b9d0596436a0cbd205070', 'f6817f48af4fb3af11b9e8bf182f618b', '4f84f9400e5e92c95f05b554724c2b58', NULL);
INSERT INTO `sys_role_permission` VALUES ('c8571839e6b14796e661f3e2843b80b6', 'ee8626f80f7c2619917b6236f3a7f02b', '45c966826eeff4c99b8f8ebfe74511fc', NULL);
INSERT INTO `sys_role_permission` VALUES ('c90b0b01c7ca454d2a1cb7408563e696', 'f6817f48af4fb3af11b9e8bf182f618b', '882a73768cfd7f78f3a37584f7299656', NULL);
INSERT INTO `sys_role_permission` VALUES ('c9d35261cccd67ab2932107a0967a7d7', 'e51758fa916c881624b046d26bd09230', 'b4dfc7d5dd9e8d5b6dd6d4579b1aa559', NULL);
INSERT INTO `sys_role_permission` VALUES ('ced80e43584ce15e97bb07298e93020d', 'e51758fa916c881624b046d26bd09230', '45c966826eeff4c99b8f8ebfe74511fc', NULL);
INSERT INTO `sys_role_permission` VALUES ('cf1feb1bf69eafc982295ad6c9c8d698', 'f6817f48af4fb3af11b9e8bf182f618b', 'a2b11669e98c5fe54a53c3e3c4f35d14', NULL);
INSERT INTO `sys_role_permission` VALUES ('cf2ef620217673e4042f695743294f01', 'f6817f48af4fb3af11b9e8bf182f618b', '717f6bee46f44a3897eca9abd6e2ec44', NULL);
INSERT INTO `sys_role_permission` VALUES ('cf43895aef7fc684669483ab00ef2257', 'f6817f48af4fb3af11b9e8bf182f618b', '700b7f95165c46cc7a78bf227aa8fed3', NULL);
INSERT INTO `sys_role_permission` VALUES ('d03d792b0f312e7b490afc5cec3dd6c5', 'e51758fa916c881624b046d26bd09230', '8fb8172747a78756c11916216b8b8066', NULL);
INSERT INTO `sys_role_permission` VALUES ('d281a95b8f293d0fa2a136f46c4e0b10', 'f6817f48af4fb3af11b9e8bf182f618b', '5c8042bd6c601270b2bbd9b20bccc68b', NULL);
INSERT INTO `sys_role_permission` VALUES ('d37ad568e26f46ed0feca227aa9c2ffa', 'f6817f48af4fb3af11b9e8bf182f618b', '9502685863ab87f0ad1134142788a385', NULL);
INSERT INTO `sys_role_permission` VALUES ('d3ddcacee1acdfaa0810618b74e38ef2', 'f6817f48af4fb3af11b9e8bf182f618b', 'c6cf95444d80435eb37b2f9db3971ae6', NULL);
INSERT INTO `sys_role_permission` VALUES ('d3fe195d59811531c05d31d8436f5c8b', '1750a8fb3e6d90cb7957c02de1dc8e59', 'e8af452d8948ea49d37c934f5100ae6a', NULL);
INSERT INTO `sys_role_permission` VALUES ('d5267597a4450f06d49d2fb63859641a', 'e51758fa916c881624b046d26bd09230', '2dbbafa22cda07fa5d169d741b81fe12', NULL);
INSERT INTO `sys_role_permission` VALUES ('d83282192a69514cfe6161b3087ff962', 'f6817f48af4fb3af11b9e8bf182f618b', '53a9230444d33de28aa11cc108fb1dba', NULL);
INSERT INTO `sys_role_permission` VALUES ('d8a5c9079df12090e108e21be94b4fd7', 'f6817f48af4fb3af11b9e8bf182f618b', '078f9558cdeab239aecb2bda1a8ed0d1', NULL);
INSERT INTO `sys_role_permission` VALUES ('dbc5dd836d45c5bc7bc94b22596ab956', 'f6817f48af4fb3af11b9e8bf182f618b', '1939e035e803a99ceecb6f5563570fb2', NULL);
INSERT INTO `sys_role_permission` VALUES ('dc83bb13c0e8c930e79d28b2db26f01f', 'f6817f48af4fb3af11b9e8bf182f618b', '63b551e81c5956d5c861593d366d8c57', NULL);
INSERT INTO `sys_role_permission` VALUES ('dc8fd3f79bd85bd832608b42167a1c71', 'f6817f48af4fb3af11b9e8bf182f618b', '91c23960fab49335831cf43d820b0a61', NULL);
INSERT INTO `sys_role_permission` VALUES ('de82e89b8b60a3ea99be5348f565c240', 'f6817f48af4fb3af11b9e8bf182f618b', '56ca78fe0f22d815fabc793461af67b8', NULL);
INSERT INTO `sys_role_permission` VALUES ('de8f43229e351d34af3c95b1b9f0a15d', 'f6817f48af4fb3af11b9e8bf182f618b', 'a400e4f4d54f79bf5ce160ae432231af', NULL);
INSERT INTO `sys_role_permission` VALUES ('e258ca8bf7ee168b93bfee739668eb15', 'ee8626f80f7c2619917b6236f3a7f02b', 'fb367426764077dcf94640c843733985', NULL);
INSERT INTO `sys_role_permission` VALUES ('e339f7db7418a4fd2bd2c113f1182186', 'ee8626f80f7c2619917b6236f3a7f02b', 'b1cb0a3fedf7ed0e4653cb5a229837ee', NULL);
INSERT INTO `sys_role_permission` VALUES ('e3e922673f4289b18366bb51b6200f17', '52b0cf022ac4187b2a70dfa4f8b2d940', '45c966826eeff4c99b8f8ebfe74511fc', NULL);
INSERT INTO `sys_role_permission` VALUES ('e7467726ee72235baaeb47df04a35e73', 'f6817f48af4fb3af11b9e8bf182f618b', 'e08cb190ef230d5d4f03824198773950', NULL);
INSERT INTO `sys_role_permission` VALUES ('eaef4486f1c9b0408580bbfa2037eb66', 'f6817f48af4fb3af11b9e8bf182f618b', '2a470fc0c3954d9dbb61de6d80846549', NULL);
INSERT INTO `sys_role_permission` VALUES ('ec4bc97829ab56afd83f428b6dc37ff6', 'f6817f48af4fb3af11b9e8bf182f618b', '200006f0edf145a2b50eacca07585451', NULL);
INSERT INTO `sys_role_permission` VALUES ('ec846a3f85fdb6813e515be71f11b331', 'f6817f48af4fb3af11b9e8bf182f618b', '732d48f8e0abe99fe6a23d18a3171cd1', NULL);
INSERT INTO `sys_role_permission` VALUES ('ec93bb06f5be4c1f19522ca78180e2ef', 'f6817f48af4fb3af11b9e8bf182f618b', '265de841c58907954b8877fb85212622', NULL);
INSERT INTO `sys_role_permission` VALUES ('ecdd72fe694e6bba9c1d9fc925ee79de', 'f6817f48af4fb3af11b9e8bf182f618b', '45c966826eeff4c99b8f8ebfe74511fc', NULL);
INSERT INTO `sys_role_permission` VALUES ('edefd8d468f5727db465cf1b860af474', 'f6817f48af4fb3af11b9e8bf182f618b', '6ad53fd1b220989a8b71ff482d683a5a', NULL);
INSERT INTO `sys_role_permission` VALUES ('ef8bdd20d29447681ec91d3603e80c7b', 'f6817f48af4fb3af11b9e8bf182f618b', 'ae4fed059f67086fd52a73d913cf473d', NULL);
INSERT INTO `sys_role_permission` VALUES ('f12b6c90e8913183d7ca547c66600891', 'e51758fa916c881624b046d26bd09230', 'aedbf679b5773c1f25e9f7b10111da73', NULL);
INSERT INTO `sys_role_permission` VALUES ('f177acac0276329dc66af0c9ad30558a', 'f6817f48af4fb3af11b9e8bf182f618b', 'c2c356bf4ddd29975347a7047a062440', NULL);
INSERT INTO `sys_role_permission` VALUES ('f17ab8ad1e71341140857ef4914ef297', '21c5a3187763729408b40afb0d0fdfa8', '732d48f8e0abe99fe6a23d18a3171cd1', NULL);
INSERT INTO `sys_role_permission` VALUES ('f99f99cc3bc27220cdd4f5aced33b7d7', 'f6817f48af4fb3af11b9e8bf182f618b', '655563cd64b75dcf52ef7bcdd4836953', NULL);
INSERT INTO `sys_role_permission` VALUES ('fafe73c4448b977fe42880a6750c3ee8', 'f6817f48af4fb3af11b9e8bf182f618b', '9cb91b8851db0cf7b19d7ecc2a8193dd', NULL);
INSERT INTO `sys_role_permission` VALUES ('fced905c7598973b970d42d833f73474', 'f6817f48af4fb3af11b9e8bf182f618b', '4875ebe289344e14844d8e3ea1edd73f', NULL);
INSERT INTO `sys_role_permission` VALUES ('fd86f6b08eb683720ba499f9d9421726', 'ee8626f80f7c2619917b6236f3a7f02b', '693ce69af3432bd00be13c3971a57961', NULL);
INSERT INTO `sys_role_permission` VALUES ('fd97963dc5f144d3aecfc7045a883427', 'f6817f48af4fb3af11b9e8bf182f618b', '043780fa095ff1b2bec4dc406d76f023', NULL);
INSERT INTO `sys_role_permission` VALUES ('fed41a4671285efb266cd404f24dd378', '52b0cf022ac4187b2a70dfa4f8b2d940', '00a2a0ae65cdca5e93209cdbde97cbe6', NULL);

-- ----------------------------
-- Table structure for sys_sms
-- ----------------------------
DROP TABLE IF EXISTS `sys_sms`;
CREATE TABLE `sys_sms`  (
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
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_sms
-- ----------------------------
INSERT INTO `sys_sms` VALUES ('402880e74dc2f361014dc2f8411e0001', '消息推送测试333', '2', '411944058@qq.com', NULL, '张三你好，你的订单4028d881436d514601436d521ae80165已付款!', '2015-06-05 17:06:01', '3', NULL, NULL, '认证失败错误的用户名或者密码', 'admin', '2015-06-05 17:05:59', 'admin', '2015-11-19 22:30:39');
INSERT INTO `sys_sms` VALUES ('402880ea533647b00153364e74770001', '发个问候', '3', 'admin', NULL, '你好', '2016-03-02 00:00:00', '2', NULL, NULL, NULL, 'admin', '2016-03-02 15:50:24', 'admin', '2018-07-05 19:53:01');
INSERT INTO `sys_sms` VALUES ('402880ee5a17e711015a17f3188e013f', '消息推送测试333', '2', '411944058@qq.com', NULL, '张三你好，你的订单4028d881436d514601436d521ae80165已付款!', NULL, '2', NULL, NULL, NULL, 'admin', '2017-02-07 17:41:31', 'admin', '2017-03-10 11:37:05');
INSERT INTO `sys_sms` VALUES ('402880f05ab649b4015ab64b9cd80012', '消息推送测试333', '2', '411944058@qq.com', NULL, '张三你好，你的订单4028d881436d514601436d521ae80165已付款!', '2017-11-16 15:58:15', '3', NULL, NULL, NULL, 'admin', '2017-03-10 11:38:13', 'admin', '2017-07-31 17:24:54');
INSERT INTO `sys_sms` VALUES ('402880f05ab7b035015ab7c4462c0004', '消息推送测试333', '2', '411944058@qq.com', NULL, '张三你好，你的订单4028d881436d514601436d521ae80165已付款!', '2017-11-16 15:58:15', '3', NULL, NULL, NULL, 'admin', '2017-03-10 18:29:37', NULL, NULL);
INSERT INTO `sys_sms` VALUES ('402881f3646a472b01646a4a5af00001', '催办：HR审批', '3', 'admin', NULL, 'admin，您好！\r\n请前待办任务办理事项！HR审批\r\n\r\n\r\n===========================\r\n此消息由系统发出', '2018-07-05 19:53:35', '2', NULL, NULL, NULL, 'admin', '2018-07-05 19:53:35', 'admin', '2018-07-07 13:45:24');
INSERT INTO `sys_sms` VALUES ('402881f3647da06c01647da43a940014', '催办：HR审批', '3', 'admin', NULL, 'admin，您好！\r\n请前待办任务办理事项！HR审批\r\n\r\n\r\n===========================\r\n此消息由系统发出', '2018-07-09 14:04:32', '2', NULL, NULL, NULL, 'admin', '2018-07-09 14:04:32', 'admin', '2018-07-09 18:51:30');

-- ----------------------------
-- Table structure for sys_sms_template
-- ----------------------------
DROP TABLE IF EXISTS `sys_sms_template`;
CREATE TABLE `sys_sms_template`  (
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
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_sms_template
-- ----------------------------
INSERT INTO `sys_sms_template` VALUES ('1199606397416775681', '系统消息通知', 'sys_ts_note', '4', '<h1>&nbsp; &nbsp; 系统通知</h1>\n<ul>\n<li>通知时间：&nbsp; ${ts_date}</li>\n<li>通知内容：&nbsp; ${ts_content}</li>\n</ul>', NULL, '2019-11-27 16:30:27', 'admin', '2019-11-27 19:36:50', 'admin');
INSERT INTO `sys_sms_template` VALUES ('1199615897335095298', '流程催办', 'bpm_cuiban', '4', '<h1>&nbsp; &nbsp;流程催办提醒</h1>\n<ul>\n<li>流程名称：&nbsp; ${bpm_name}</li>\n<li>催办任务：&nbsp; ${bpm_task}</li>\n<li>催办时间 :&nbsp; &nbsp; ${datetime}</li>\n<li>催办内容 :&nbsp; &nbsp; ${remark}</li>\n</ul>', NULL, '2019-11-27 17:08:12', 'admin', '2019-11-27 19:36:45', 'admin');
INSERT INTO `sys_sms_template` VALUES ('1199648914107625473', '流程办理超时提醒', 'bpm_chaoshi_tip', '4', '<h1>&nbsp; &nbsp;流程办理超时提醒</h1>\n<ul>\n<li>&nbsp; &nbsp;超时提醒信息：&nbsp; &nbsp; 您有待处理的超时任务，请尽快处理！</li>\n<li>&nbsp; &nbsp;超时任务标题：&nbsp; &nbsp; ${title}</li>\n<li>&nbsp; &nbsp;超时任务节点：&nbsp; &nbsp; ${task}</li>\n<li>&nbsp; &nbsp;任务处理人：&nbsp; &nbsp; &nbsp; &nbsp;${user}</li>\n<li>&nbsp; &nbsp;任务开始时间：&nbsp; &nbsp; ${time}</li>\n</ul>', NULL, '2019-11-27 19:19:24', 'admin', '2019-11-27 19:36:37', 'admin');
INSERT INTO `sys_sms_template` VALUES ('4028608164691b000164693108140003', '催办：${taskName}', 'SYS001', '3', '${userName}，您好！\r\n请前待办任务办理事项！${taskName}\r\n\r\n\r\n===========================\r\n此消息由系统发出', '{\r\n\"taskName\":\"HR审批\",\r\n\"userName\":\"admin\"\r\n}', '2018-07-05 14:46:18', 'admin', '2018-07-05 18:31:34', 'admin');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录账号',
  `realname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'md5密码盐',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `birthday` datetime(0) NULL DEFAULT NULL COMMENT '生日',
  `sex` tinyint(1) NULL DEFAULT NULL COMMENT '性别(0-默认未知,1-男,2-女)',
  `email` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构编码',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '性别(1-正常,2-冻结)',
  `del_flag` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态(0-正常,1-已删除)',
  `activiti_sync` tinyint(1) NULL DEFAULT NULL COMMENT '同步工作流引擎(1-同步,0-不同步)',
  `work_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工号，唯一键',
  `post` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务，关联职务表',
  `telephone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '座机号',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `identity` tinyint(1) NULL DEFAULT NULL COMMENT '身份（1普通成员 2上级）',
  `depart_ids` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '负责部门',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_user_name`(`username`) USING BTREE,
  UNIQUE INDEX `uniq_sys_user_work_no`(`work_no`) USING BTREE,
  INDEX `index_user_status`(`status`) USING BTREE,
  INDEX `index_user_del_flag`(`del_flag`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('42d153bffeea74f72a9c1697874fa4a7', 'test22', '23232', 'ac52e15671a377cf', '5FMD48RM', 'user/20190314/ly-plate-e_1552531617500.png', '2019-02-09 00:00:00', 1, 'zhangdaiscott@163.com', '18611782222', NULL, 1, 0, 1, NULL, NULL, NULL, 'admin', '2019-01-26 18:01:10', 'admin', '2019-03-23 15:05:50', NULL, NULL);
INSERT INTO `sys_user` VALUES ('a75d45a015c44384a04449ee80dc3503', 'jeecg', 'jeecg', '3dd8371f3cf8240e', 'vDDkDzrK', 'user/20190220/e1fe9925bc315c60addea1b98eb1cb1349547719_1550656892940.jpg', NULL, 2, NULL, NULL, 'A02A01', 1, 0, 1, NULL, NULL, NULL, 'admin', '2019-02-13 16:02:36', 'admin', '2019-04-09 15:47:36', NULL, NULL);
INSERT INTO `sys_user` VALUES ('e9ca23d68d884d4ebb19d07889727dae', 'admin', '管理员', 'cb362cfeefbf3d8d', 'RCGTeGiH', 'temp/11_1582468686154.jpg', '2018-12-05 00:00:00', 1, '11@qq.com', '18566666661', 'A01', 1, 0, 1, '111', '', NULL, NULL, '2038-06-21 17:54:10', 'admin', '2020-02-23 22:44:00', 2, 'c6d7cb4deeac411cb3384b1b31278596');
INSERT INTO `sys_user` VALUES ('f0019fdebedb443c98dcb17d88222c38', 'zhagnxiao', '张小红', 'f898134e5e52ae11a2ffb2c3b57a4e90', 'go3jJ4zX', 'user/20190401/20180607175028Fn1Lq7zw_1554118444672.png', '2019-04-01 00:00:00', NULL, NULL, NULL, NULL, 1, 0, 1, NULL, NULL, NULL, 'admin', '2023-10-01 19:34:10', 'admin', '2019-04-10 22:00:22', NULL, NULL);

-- ----------------------------
-- Table structure for sys_user_agent
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_agent`;
CREATE TABLE `sys_user_agent`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '序号',
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `agent_user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理人用户名',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '代理开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '代理结束时间',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态0无效1有效',
  `create_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人名称',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `sys_org_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门',
  `sys_company_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属公司',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_username`(`user_name`) USING BTREE,
  INDEX `statux_index`(`status`) USING BTREE,
  INDEX `begintime_index`(`start_time`) USING BTREE,
  INDEX `endtime_index`(`end_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户代理人设置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_depart
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_depart`;
CREATE TABLE `sys_user_depart`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `dep_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门id',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `index_depart_groupk_userid`(`user_id`) USING BTREE,
  INDEX `index_depart_groupkorgid`(`dep_id`) USING BTREE,
  INDEX `index_depart_groupk_uidanddid`(`user_id`, `dep_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_depart
-- ----------------------------
INSERT INTO `sys_user_depart` VALUES ('0c42ba309c2c4cad35836ec2336676fa', '42d153bffeea74f72a9c1697874fa4a7', '6d35e179cd814e3299bd588ea7daed3f');
INSERT INTO `sys_user_depart` VALUES ('2835834d133f9118ee87a666e0f5501e', 'a75d45a015c44384a04449ee80dc3503', 'a7d7e77e06c84325a40932163adcdaa6');
INSERT INTO `sys_user_depart` VALUES ('1f3a0267811327b9eca86b0cc2b956f3', 'bcbe1290783a469a83ae3bd8effe15d4', '5159cde220114246b045e574adceafe9');
INSERT INTO `sys_user_depart` VALUES ('1231590533597499393', 'e9ca23d68d884d4ebb19d07889727dae', 'c6d7cb4deeac411cb3384b1b31278596');
INSERT INTO `sys_user_depart` VALUES ('ac52f23ae625eb6560c9227170b88166', 'f0019fdebedb443c98dcb17d88222c38', '57197590443c44f083d42ae24ef26a2c');
INSERT INTO `sys_user_depart` VALUES ('179660a8b9a122f66b73603799a10924', 'f0019fdebedb443c98dcb17d88222c38', '67fc001af12a4f9b8458005d3f19934a');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index2_groupuu_user_id`(`user_id`) USING BTREE,
  INDEX `index2_groupuu_ole_id`(`role_id`) USING BTREE,
  INDEX `index2_groupuu_useridandroleid`(`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('b3ffd9311a1ca296c44e2409b547384f', '01b802058ea94b978a2c96f4807f6b48', '1');
INSERT INTO `sys_user_role` VALUES ('0ede6d23d53bc7dc990346ff14faabee', '3db4cf42353f4e868b7ccfeef90505d2', 'ee8626f80f7c2619917b6236f3a7f02b');
INSERT INTO `sys_user_role` VALUES ('e78d210d24aaff48e0a736e2ddff4cdc', '3e177fede453430387a8279ced685679', 'ee8626f80f7c2619917b6236f3a7f02b');
INSERT INTO `sys_user_role` VALUES ('f2de3ae7b5efd8345581aa802a6675d6', '41b1be8d4c52023b0798f51164ca682d', 'e51758fa916c881624b046d26bd09230');
INSERT INTO `sys_user_role` VALUES ('6f9da7310489bac1e5f95e0efe92b4ce', '42d153bffeea74f72a9c1697874fa4a7', 'e51758fa916c881624b046d26bd09230');
INSERT INTO `sys_user_role` VALUES ('f2922a38ba24fb53749e45a0c459adb3', '439ae3e9bcf7418583fcd429cadb1d72', '1');
INSERT INTO `sys_user_role` VALUES ('f72c6190b0722e798147e73c776c6ac9', '439ae3e9bcf7418583fcd429cadb1d72', 'ee8626f80f7c2619917b6236f3a7f02b');
INSERT INTO `sys_user_role` VALUES ('ee45d0343ecec894b6886effc92cb0b7', '4d8fef4667574b24a9ccfedaf257810c', 'f6817f48af4fb3af11b9e8bf182f618b');
INSERT INTO `sys_user_role` VALUES ('be2639167ede09379937daca7fc3bb73', '526f300ab35e44faaed54a9fb0742845', 'ee8626f80f7c2619917b6236f3a7f02b');
INSERT INTO `sys_user_role` VALUES ('31af310584bd5795f76b1fe8c38294a0', '70f5dcf03f36471dabba81381919291f', 'e51758fa916c881624b046d26bd09230');
INSERT INTO `sys_user_role` VALUES ('8d7846ec783e157174e4ce2949231a65', '7ee6630e89d17afbf6d12150197b578d', 'e51758fa916c881624b046d26bd09230');
INSERT INTO `sys_user_role` VALUES ('79d66ef7aa137cfa9957081a1483009d', '9a668858c4c74cf5a2b25ad9608ba095', 'ee8626f80f7c2619917b6236f3a7f02b');
INSERT INTO `sys_user_role` VALUES ('f3a4ca33848daba3e43490707ae859e7', 'a75d45a015c44384a04449ee80dc3503', 'e51758fa916c881624b046d26bd09230');
INSERT INTO `sys_user_role` VALUES ('fe38580871c5061ba59d5c03a0840b0e', 'a75d45a015c44384a04449ee80dc3503', 'ee8626f80f7c2619917b6236f3a7f02b');
INSERT INTO `sys_user_role` VALUES ('1231590533484253186', 'e9ca23d68d884d4ebb19d07889727dae', 'f6817f48af4fb3af11b9e8bf182f618b');
INSERT INTO `sys_user_role` VALUES ('d2233e5be091d39da5abb0073c766224', 'f0019fdebedb443c98dcb17d88222c38', 'ee8626f80f7c2619917b6236f3a7f02b');

-- ----------------------------
-- Table structure for test_demo
-- ----------------------------
DROP TABLE IF EXISTS `test_demo`;
CREATE TABLE `test_demo`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人登录名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人登录名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `sex` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `age` int(32) NULL DEFAULT NULL COMMENT '年龄',
  `descc` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `birthday` datetime(0) NULL DEFAULT NULL COMMENT '生日',
  `user_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户编码',
  `file_kk` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件',
  `top_pic` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test_demo
-- ----------------------------
INSERT INTO `test_demo` VALUES ('4028810c6aed99e1016aed9b31b40002', NULL, NULL, 'admin', '2019-10-19 15:37:27', 'jeecg', '2', 55, '5', '2019-05-15 00:00:00', NULL, '', '');
INSERT INTO `test_demo` VALUES ('4028810c6b02cba2016b02cba21f0000', 'admin', '2019-05-29 16:53:48', 'admin', '2019-08-23 23:45:21', '张小红', '1', 8222, '8', '2019-04-01 00:00:00', NULL, '', '');
INSERT INTO `test_demo` VALUES ('4028810c6b40244b016b4030a0e40001', 'admin', '2019-06-10 15:00:57', 'admin', '2019-08-23 23:42:49', '小芳', '2', 0, NULL, '2019-04-01 00:00:00', NULL, '', '');
INSERT INTO `test_demo` VALUES ('fa1d1c249461498d90f405b94f60aae0', '', NULL, 'admin', '2019-05-15 12:30:28', '战三', '2', 222, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for test_order_main
-- ----------------------------
DROP TABLE IF EXISTS `test_order_main`;
CREATE TABLE `test_order_main`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `order_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单编码',
  `order_date` datetime(0) NULL DEFAULT NULL COMMENT '下单时间',
  `descc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test_order_main
-- ----------------------------
INSERT INTO `test_order_main` VALUES ('402831816a38e7fd016a38e825c90003', 'admin', '2019-04-20 12:01:39', 'admin', '2019-08-23 23:43:17', '111333', '2019-04-20 00:00:00', '11');
INSERT INTO `test_order_main` VALUES ('4028810c6b40244b016b40686dfb0003', 'admin', '2019-06-10 16:01:54', 'admin', '2019-06-10 16:06:51', '1231', '2019-06-10 00:00:00', '123');
INSERT INTO `test_order_main` VALUES ('4028810c6b40244b016b4068ef890006', 'admin', '2019-06-10 16:02:27', 'admin', '2019-08-23 23:43:08', 'A001', '2019-06-10 00:00:00', '购买产品BOOT');

-- ----------------------------
-- Table structure for test_order_product
-- ----------------------------
DROP TABLE IF EXISTS `test_order_product`;
CREATE TABLE `test_order_product`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `product_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品名字',
  `price` double(32, 0) NULL DEFAULT NULL COMMENT '价格',
  `num` int(32) NULL DEFAULT NULL COMMENT '数量',
  `descc` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `order_fk_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单外键ID',
  `pro_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test_order_product
-- ----------------------------
INSERT INTO `test_order_product` VALUES ('15665749852471', 'admin', '2019-08-23 23:43:08', NULL, NULL, '222', 222, 22, '', '4028810c6b40244b016b4068ef890006', '2');
INSERT INTO `test_order_product` VALUES ('15665749948861', 'admin', '2019-08-23 23:43:17', NULL, NULL, '333', 33, NULL, '', '402831816a38e7fd016a38e825c90003', '');
INSERT INTO `test_order_product` VALUES ('402831816a38e7fd016a38e7fdeb0001', 'admin', '2019-04-20 12:01:29', NULL, NULL, '笔记本', 100, 10, NULL, '402831816a38e7fd016a38e7fddf0000', NULL);
INSERT INTO `test_order_product` VALUES ('402831816a38e7fd016a38e7fdf10002', 'admin', '2019-04-20 12:01:29', NULL, NULL, '显示器', 300, 1, NULL, '402831816a38e7fd016a38e7fddf0000', NULL);
INSERT INTO `test_order_product` VALUES ('4028810c6b40244b016b40686e050004', 'admin', '2019-06-10 16:06:51', NULL, NULL, '123', 222, 123, '123', '4028810c6b40244b016b40686dfb0003', NULL);
INSERT INTO `test_order_product` VALUES ('4028810c6b40244b016b406884080005', 'admin', '2019-08-23 23:43:17', NULL, NULL, '333', NULL, 33, '', '402831816a38e7fd016a38e825c90003', '');
INSERT INTO `test_order_product` VALUES ('4028810c6b40244b016b4068ef8f0007', 'admin', '2019-08-23 23:43:08', NULL, NULL, 'JEECG-BOOT开发平台', 10000, 1, '', '4028810c6b40244b016b4068ef890006', '1');

-- ----------------------------
-- Table structure for test_person
-- ----------------------------
DROP TABLE IF EXISTS `test_person`;
CREATE TABLE `test_person`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `sex` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '请假原因',
  `be_date` datetime(0) NULL DEFAULT NULL COMMENT '请假时间',
  `qj_days` int(11) NULL DEFAULT NULL COMMENT '请假天数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test_person
-- ----------------------------
INSERT INTO `test_person` VALUES ('8ca668defdae47df8649a5477ae08b05', 'admin', '2019-04-12 09:51:37', NULL, NULL, '1', 'zhangdaiscott', 'dsdsd', '2019-04-12 00:00:00', 2);

SET FOREIGN_KEY_CHECKS = 1;
