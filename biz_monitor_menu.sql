-- 业务监控中心菜单配置
-- 业务监控中心父菜单
INSERT INTO `sys_permission` VALUES ('1680877743141933001', '1439398677984878593', '业务监控中心', '/monitor/biz', 'layouts/RouteView', 1, NULL, NULL, 1, NULL, '0', 12.00, 0, 'ant-design:dashboard-filled', 1, 0, 0, 0, NULL, 'admin', NOW(), NULL, NULL, 0, 0, NULL, 0);

-- 实时业务看板
INSERT INTO `sys_permission` VALUES ('1680877743141933002', '1680877743141933001', '实时业务看板', '/monitor/biz/realTimeBoard', 'monitor/biz/RealTimeBoard', 1, NULL, NULL, 1, NULL, '0', 1.00, 0, NULL, 1, 0, 0, 0, NULL, 'admin', NOW(), NULL, NULL, 0, 0, NULL, 0);

-- 预警规则配置
INSERT INTO `sys_permission` VALUES ('1680877743141933003', '1680877743141933001', '预警规则配置', '/monitor/biz/alertRuleConfig', 'monitor/biz/AlertRuleConfig', 1, NULL, NULL, 1, NULL, '0', 2.00, 0, NULL, 1, 0, 0, 0, NULL, 'admin', NOW(), NULL, NULL, 0, 0, NULL, 0);

-- 分配权限给admin角色
INSERT INTO `sys_role_permission` VALUES ('1680877743141933001', 'admin', '1680877743141933001');
INSERT INTO `sys_role_permission` VALUES ('1680877743141933002', 'admin', '1680877743141933002');
INSERT INTO `sys_role_permission` VALUES ('1680877743141933003', 'admin', '1680877743141933003');