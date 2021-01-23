INSERT INTO SYS_DICT_ITEM(ID, DICT_ID, ITEM_TEXT, ITEM_VALUE, DESCRIPTION, SORT_ORDER, STATUS, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME) VALUES ('1334440962954936321', '1209733563293962241', 'MYSQL5.7', '4', NULL, '1', '1', 'admin', '2020-12-03 18:16:02', 'admin', '2020-12-03 18:16:02');
UPDATE SYS_DICT_ITEM SET ITEM_TEXT = 'MySQL5.5' WHERE ID = '1209733775114702850';
UPDATE SYS_DICT_ITEM SET SORT_ORDER = '3' WHERE ID = '1209733839933476865';
UPDATE SYS_DICT_ITEM SET SORT_ORDER = '4' WHERE ID = '1209733903020003330';

ALTER TABLE `sys_gateway_route`
CHANGE COLUMN `persist` `persistable` int(3) NULL DEFAULT NULL COMMENT '是否为保留数据:0-否 1-是' AFTER `strip_prefix`;

DROP TABLE IF EXISTS `test_online_link`;
CREATE TABLE `test_online_link`  (
  `id` varchar(32) NOT NULL,
  `pid` varchar(32) DEFAULT NULL COMMENT 'pid',
  `name` varchar(255) DEFAULT NULL COMMENT 'name',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `test_online_link` VALUES ('1', NULL, '中国');
INSERT INTO `test_online_link` VALUES ('10', '8', '庐阳区');
INSERT INTO `test_online_link` VALUES ('11', '7', '黄山市');
INSERT INTO `test_online_link` VALUES ('2', '1', '山东省');
INSERT INTO `test_online_link` VALUES ('3', '2', '济南市');
INSERT INTO `test_online_link` VALUES ('4', '3', '历城区');
INSERT INTO `test_online_link` VALUES ('5', '3', '长青区');
INSERT INTO `test_online_link` VALUES ('6', '2', '青岛市');
INSERT INTO `test_online_link` VALUES ('7', '1', '安徽省');
INSERT INTO `test_online_link` VALUES ('8', '7', '合肥市');
INSERT INTO `test_online_link` VALUES ('9', '8', '包河区');

update ONL_CGFORM_FIELD set DB_TYPE = 'Date' WHERE DB_TYPE = 'date';