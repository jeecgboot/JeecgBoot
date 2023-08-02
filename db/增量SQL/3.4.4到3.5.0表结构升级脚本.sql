-- author: scott---date:20221009 -for: 乐观锁测试
ALTER TABLE `demo` 
ADD COLUMN `update_count` int NULL COMMENT '乐观锁测试' AFTER `tenant_id`;
-- author: scott---date:20221009 -for: 乐观锁测试

-- author: scott---date:20221108 -for:VUEN-2064 改造登录选择部门和租户逻辑
ALTER TABLE `sys_user` 
ADD COLUMN `login_tenant_id` int NULL COMMENT '上次登录选择租户ID' AFTER `client_id`;
-- author: scott---date:20221108 -for:VUEN-2064 改造登录选择部门和租户逻辑

-- author: scott---date:20221129-for: 系统管理模块支持多租户机制，默认加字段[tenant_id]-----
ALTER TABLE `sys_depart` 
ADD COLUMN `tenant_id` int(10) NULL DEFAULT 0 COMMENT '租户ID';

ALTER TABLE `sys_role` 
ADD COLUMN `tenant_id` int(10) NULL DEFAULT 0 COMMENT '租户ID';

ALTER TABLE `sys_dict` 
ADD COLUMN `tenant_id` int(10) NULL DEFAULT 0 COMMENT '租户ID';

ALTER TABLE `sys_position` 
ADD COLUMN `tenant_id` int(10) NULL DEFAULT 0 COMMENT '租户ID';

ALTER TABLE `sys_category` 
ADD COLUMN `tenant_id` int(10) NULL DEFAULT 0 COMMENT '租户ID';

ALTER TABLE `sys_data_source` 
ADD COLUMN `tenant_id` int(10) NULL DEFAULT 0 COMMENT '租户ID';

ALTER TABLE `sys_announcement` 
ADD COLUMN `tenant_id` int(10) NULL DEFAULT 0 COMMENT '租户ID';

ALTER TABLE `sys_user_role` 
ADD COLUMN `tenant_id` int(10) NULL DEFAULT 0 COMMENT '租户ID';
-- author: scott---date:20221129--for:系统管理模块支持多租户机制，默认加字段[tenant_id]-----

-- author: wangshuai---date:20221209--for:租户改造新增字段、菜单权限、字典-----
ALTER TABLE sys_tenant
ADD COLUMN trade varchar(10) NULL COMMENT '所属行业' AFTER status,
ADD COLUMN company_size varchar(10) NULL COMMENT '公司规模' AFTER trade,
ADD COLUMN company_address varchar(100) NULL COMMENT '公司地址' AFTER company_size,
ADD COLUMN company_logo varchar(200) NULL COMMENT '公司logo' AFTER company_address;

-- 所属行业字典
INSERT INTO sys_dict(id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type, tenant_id) VALUES ('1600042215909134338', '所属行业', 'trade', '行业', 0, 'admin', '2022-12-06 16:19:26', 'admin', '2022-12-06 16:20:50', 0, 0);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600042651777011713', '1600042215909134338', '信息传输、软件和信息技术服务业', '1', NULL, 1, 1, 'admin', '2022-12-06 16:21:10', 'admin', '2022-12-06 16:21:27');
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600042736254488578', '1600042215909134338', '制造业', '2', NULL, 1, 1, 'admin', '2022-12-06 16:21:30', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600042785646612481', '1600042215909134338', '租赁和商务服务业', '3', NULL, 1, 1, 'admin', '2022-12-06 16:21:42', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600042835433000961', '1600042215909134338', '教育', '4', NULL, 1, 1, 'admin', '2022-12-06 16:21:54', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600042892072882177', '1600042215909134338', '金融业', '5', NULL, 1, 1, 'admin', '2022-12-06 16:22:07', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600042975539531778', '1600042215909134338', '建筑业', '6', NULL, 1, 1, 'admin', '2022-12-06 16:22:27', 'admin', '2022-12-06 16:22:32');
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600043052177854466', '1600042215909134338', '科学研究和技术服务业', '7', NULL, 1, 1, 'admin', '2022-12-06 16:22:46', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600043101976825857', '1600042215909134338', '批发和零售业', '8', NULL, 1, 1, 'admin', '2022-12-06 16:22:58', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600043157069008898', '1600042215909134338', '住宿和餐饮业', '9', NULL, 1, 1, 'admin', '2022-12-06 16:23:11', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600043203105689601', '1600042215909134338', '电子商务', '10', NULL, 1, 1, 'admin', '2022-12-06 16:23:22', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600043277504253953', '1600042215909134338', '线下零售与服务业', '11', NULL, 1, 1, 'admin', '2022-12-06 16:23:39', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600043334618091521', '1600042215909134338', '文化、体育和娱乐业', '12', NULL, 1, 1, 'admin', '2022-12-06 16:23:53', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600043401030701058', '1600042215909134338', '房地产业', '13', NULL, 1, 1, 'admin', '2022-12-06 16:24:09', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600043476440092673', '1600042215909134338', '交通运输、仓储和邮政业', '14', NULL, 1, 1, 'admin', '2022-12-06 16:24:27', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600043553837584386', '1600042215909134338', '卫生和社会工作', '15', NULL, 1, 1, 'admin', '2022-12-06 16:24:45', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600043628793991170', '1600042215909134338', '公共管理、社会保障和社会组织', '16', NULL, 1, 1, 'admin', '2022-12-06 16:25:03', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600043675329794050', '1600042215909134338', '电力、热力、燃气及水生产和供应业', '18', NULL, 1, 1, 'admin', '2022-12-06 16:25:14', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600043734607892482', '1600042215909134338', '水利、环境和公共设施管理业', '19', NULL, 1, 1, 'admin', '2022-12-06 16:25:28', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600043783068880897', '1600042215909134338', '居民服务、修理和其他服务业', '20', NULL, 1, 1, 'admin', '2022-12-06 16:25:40', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600043822679887874', '1600042215909134338', '政府机构', '21', NULL, 1, 1, 'admin', '2022-12-06 16:25:49', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600043859539431426', '1600042215909134338', '农、林、牧、渔业', '22', NULL, 1, 1, 'admin', '2022-12-06 16:25:58', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600043907551629313', '1600042215909134338', '采矿业', '23', NULL, 1, 1, 'admin', '2022-12-06 16:26:10', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600043955731599362', '1600042215909134338', '国际组织', '24', NULL, 1, 1, 'admin', '2022-12-06 16:26:21', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600043991685173249', '1600042215909134338', '其他', '25', NULL, 1, 1, 'admin', '2022-12-06 16:26:30', NULL, NULL);

-- 公司规模字典
INSERT INTO sys_dict(id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type, tenant_id) VALUES ('1600044537800331266', '公司规模', 'company_size', '公司规模', 0, 'admin', '2022-12-06 16:28:40', 'admin', '2022-12-06 16:30:23', 0, 0);

INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600044644096577538', '1600044537800331266', '20人以下', '1', NULL, 1, 1, 'admin', '2022-12-06 16:29:05', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600044698618335233', '1600044537800331266', '21-99人', '2', NULL, 1, 1, 'admin', '2022-12-06 16:29:18', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600044744172670978', '1600044537800331266', '100-499人', '3', NULL, 1, 1, 'admin', '2022-12-06 16:29:29', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600044792306503681', '1600044537800331266', '500-999人', '4', NULL, 1, 1, 'admin', '2022-12-06 16:29:41', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600044861302804481', '1600044537800331266', '1000-9999人', '5', NULL, 1, 1, 'admin', '2022-12-06 16:29:57', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1600044924313833473', '1600044537800331266', '10000人以上', '6', NULL, 1, 1, 'admin', '2022-12-06 16:30:12', NULL, NULL);



-- author: scott---date:20221227--for:字典增加应用ID-----
ALTER TABLE `sys_dict` 
ADD COLUMN `low_app_id` varchar(32) NULL COMMENT '低代码应用ID' AFTER `tenant_id`;
-- author: scott---date:20221227--for:字典增加应用ID-----

-- author: scott---date:20221227--for:租户ID改成10位整数-----
ALTER TABLE `sys_tenant` 
MODIFY COLUMN `id` int(10) NOT NULL COMMENT '租户编码' FIRST;
-- author: scott---date:20221227--for:租户ID改成10位整数-----

-- author: wangshuai---date:20221229--for:[QQYUN-3608]租户逻辑改造-----
-- 新增租户中间表
CREATE TABLE sys_user_tenant(
  id varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  user_id varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
  tenant_id int(10) NULL DEFAULT NULL COMMENT '租户id',
  status varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态(0 冻结 1 正常)',
  create_by varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人登录名称',
  create_time datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  update_by varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人登录名称',
  update_time datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户租户关系表' ROW_FORMAT = Dynamic;

-- 职级字典
INSERT INTO sys_dict(id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type, tenant_id) VALUES ('1606645341269299201', '职级', 'company_rank', '公司职级', 0, 'admin', '2022-12-24 21:37:54', 'admin', '2022-12-24 21:38:25', 0, 0);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1606645562573361153', '1606645341269299201', '总裁/总经理/CEO', '1', NULL, 1, 1, 'admin', '2022-12-24 21:38:47', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1606645619930468354', '1606645341269299201', '副总裁/副总经理/VP', '2', NULL, 2, 1, 'admin', '2022-12-24 21:39:00', 'admin', '2022-12-24 21:40:00');
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1606645660241924097', '1606645341269299201', '总监/主管/经理', '3', NULL, 3, 1, 'admin', '2022-12-24 21:39:10', 'admin', '2022-12-24 21:39:41');
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1606645696715591682', '1606645341269299201', '员工/专员/执行', '4', NULL, 4, 1, 'admin', '2022-12-24 21:39:19', 'admin', '2022-12-24 21:39:37');
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1606645744023146497', '1606645341269299201', '其他', '5', NULL, 5, 1, 'admin', '2022-12-24 21:39:30', NULL, NULL);
-- 部门字典
INSERT INTO sys_dict(id, dict_name, dict_code, description, del_flag, create_by, create_time, update_by, update_time, type, tenant_id) VALUES ('1606646440684457986', '公司部门', 'company_department', '公司部门', 0, 'admin', '2022-12-24 21:42:16', NULL, NULL, 0, 0);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1606647668965412866', '1606646440684457986', '总经办', '1', NULL, 1, 1, 'admin', '2022-12-24 21:47:09', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1606647703098658817', '1606646440684457986', '技术/IT/研发', '2', NULL, 2, 1, 'admin', '2022-12-24 21:47:17', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1606647737919770625', '1606646440684457986', '产品/设计', '3', NULL, 3, 1, 'admin', '2022-12-24 21:47:25', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1606647789614567425', '1606646440684457986', '销售/市场/运营', '4', '', 4, 1, 'admin', '2022-12-24 21:47:38', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1606647827921145857', '1606646440684457986', '人事/财务/行政', '5', NULL, 5, 1, 'admin', '2022-12-24 21:47:47', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1606647860955484162', '1606646440684457986', '资源/仓储/采购', '6', NULL, 6, 1, 'admin', '2022-12-24 21:47:55', NULL, NULL);
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time, update_by, update_time) VALUES ('1606647915473047553', '1606646440684457986', '其他', '7', NULL, 7, 1, 'admin', '2022-12-24 21:48:08', NULL, NULL);

-- 租户新增字段
ALTER TABLE sys_tenant 
ADD COLUMN house_number varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门牌号' AFTER company_logo,
ADD COLUMN work_place varchar(100) CHARACTER SET utf32 COLLATE utf32_general_ci NULL DEFAULT NULL COMMENT '工作地点' AFTER house_number,
ADD COLUMN secondary_domain varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '二级域名' AFTER work_place,
ADD COLUMN login_bkgd_img varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录背景图片' AFTER secondary_domain,
ADD COLUMN position varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职级' AFTER login_bkgd_img,
ADD COLUMN department varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门' AFTER position;

-- 移除用户多租户id
ALTER TABLE sys_user 
DROP COLUMN rel_tenant_ids;
-- author: wangshuai---date:20221229--for:[QQYUN-3608]租户逻辑改造-----

-- author: wangshuai---date:20221229--for:租户表创建查询索引,避免数据量大查询慢-----
ALTER TABLE sys_user_tenant 
ADD INDEX idx_sut_user_id(user_id) USING BTREE,
ADD INDEX idx_sut_tenant_id(tenant_id) USING BTREE;
-- author: wangshuai---date:20221229--for:租户表创建查询索引,避免数据量大查询慢-----


-- -author:wangshuai---date:2023-01-04---for: 产品包升级sql
CREATE TABLE sys_pack_permission  (
  id varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键编号',
  pack_id varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户产品包名称',
  permission_id varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单id',
  create_by varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  create_time date NULL DEFAULT NULL COMMENT '创建时间',
  update_by varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  update_time date NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户产品包和菜单关系表' ROW_FORMAT = Dynamic;

CREATE TABLE sys_tenant_pack  (
  id varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  tenant_id int(10) NULL DEFAULT NULL COMMENT '租户id',
  pack_name varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品包名',
  status varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开启状态(0 未开启 1开启)',
  remarks varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  create_by varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  create_time date NULL DEFAULT NULL COMMENT '创建时间',
  update_by varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  update_time date NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户产品包' ROW_FORMAT = Dynamic;



-- author:wangshuai---date:2023-01-09---for: 关系表状态修改/租户表增加逻辑删除/新增接口权限菜单
-- 关系表状态修改
ALTER TABLE sys_user_tenant 
MODIFY COLUMN status varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态(1 正常 2 冻结 3 待审核 4 拒绝)' AFTER tenant_id;

-- 租户表增加逻辑删除
ALTER TABLE sys_tenant 
ADD COLUMN del_flag tinyint(1) NULL DEFAULT NULL COMMENT '删除状态(0-正常,1-已删除)' AFTER department;

ALTER TABLE `sys_tenant` 
MODIFY COLUMN `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态(0-正常,1-已删除)' AFTER `department`;

-- 需要将租户删除状态改成0正常,否则可能导致数据出不来
update sys_tenant set del_flag = 0;


-- -author:wangshuai---date:2023-01-11--for：【QQYUN-3938】租户表加修改人、修改时间
ALTER TABLE sys_tenant 
ADD COLUMN update_by varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人' AFTER del_flag,
ADD COLUMN update_time datetime(0) NULL DEFAULT NULL COMMENT '更新时间' AFTER update_by;
-- -author:wangshuai---date:2023-01-11--for：【QQYUN-3938】租户表加修改人、修改时间

-- -author:wangshuai---date:2023-01-11--for：用户租户修改离职状态
ALTER TABLE sys_user_tenant 
MODIFY COLUMN status varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态(1 正常 2 离职 3 待审核 4 审核未通过)' AFTER tenant_id;
-- -author:wangshuai---date:2023-01-11--for：用户租户修改离职状态



-- -author:wangshuai---date:2023-02-02--for：[QQYUN-3988]租户产品包表改名
ALTER TABLE sys_pack_permission RENAME sys_tenant_pack_perms;
-- -author:wangshuai---date:2023-02-02--for：[QQYUN-3988]租户产品包表改名

-- -author:zyf---date:2023-02-02--for：添加流程离职入职状态
ALTER TABLE sys_user
ADD COLUMN bpm_status varchar(2) NULL DEFAULT NULL COMMENT '流程入职离职状态';
-- -author:zyf---date:2023-02-02--for：添加流程离职入职状态

-- -author:scott---date:2023-02-08--for：尽量不要用大字段，会出问题
ALTER TABLE `sys_user` 
MODIFY COLUMN `depart_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责部门' AFTER `user_identity`;
-- -author:scott---date:2023-02-08--for：尽量不要用大字段，会出问题

-- -author:wangshuai---date:2023-02-16--for：[QQYUN-4163]部门新增是否叶子结点
ALTER TABLE sys_depart
ADD COLUMN iz_leaf tinyint(1) NULL DEFAULT 0 COMMENT '是否有叶子节点: 1是0否' AFTER tenant_id;
-- 更新父级部门不为叶子结点
update sys_depart set iz_leaf = 0 where id in ( select a.parent_id from (select parent_id from sys_depart where parent_id!='' and parent_id is not null) as a);
-- -author:wangshuai---date:2023-02-16--for：[QQYUN-4163]部门新增是否叶子结点


-- -author: taoyan---date:2023-02-17--for：QQYUN-3851【租户】租户管理员功能
ALTER TABLE `sys_tenant_pack` 
ADD COLUMN `pack_code` varchar(50) NULL COMMENT '编码,默认添加的三个管理员需要设置编码' AFTER `update_time`;

ALTER TABLE `sys_log` 
ADD COLUMN `tenant_id` int(10) NULL COMMENT '租户ID' AFTER `update_time`;


CREATE TABLE `sys_tenant_pack_user`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pack_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户产品包ID',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `tenant_id` int(10) NULL DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户套餐人员表' ROW_FORMAT = Dynamic;

ALTER TABLE `sys_tenant_pack_user` 
ADD COLUMN `status` int(3) NULL COMMENT '状态 正常状态1 申请状态0' AFTER `update_time`;

ALTER TABLE `sys_tenant` 
ADD COLUMN `apply_status` int(3) NULL COMMENT '允许申请管理员 1允许 0不允许' AFTER `update_time`;
update sys_tenant set apply_status = 1;

ALTER TABLE `sys_log` 
MODIFY COLUMN `log_type` int(3) NULL DEFAULT NULL COMMENT '日志类型（1登录日志，2操作日志, 3.租户操作日志）' AFTER `id`;
-- -author: taoyan---date:2023-02-17--for：QQYUN-3851【租户】租户管理员功能





