-- 注意：该页面对应的前台目录为views/${entityPackagePath}文件夹下
-- 如果你想更改到其他目录，请修改sql中component字段对应的值

<#assign mainId = "${.now?long}01">
<#assign addId = "${.now?long}02">
<#assign editId = "${.now?long}03">
<#assign delId = "${.now?long}04">
<#assign batchDelId = "${.now?long}05">
<#assign exportId = "${.now?long}06">
<#assign importId = "${.now?long}07">

-- 主菜单
INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external)
VALUES ('${mainId}', NULL, '${tableVo.ftlDescription}', '/${entityPackagePath}/${entityName?uncap_first}List', '${entityPackagePath}/${entityName}List', NULL, NULL, 0, NULL, '1', 0.00, 0, NULL, 1, 0, 0, 0, 0, NULL, '1', 0, 0, 'admin', '${.now?string["yyyy-MM-dd HH:mm:ss"]}', NULL, NULL, 0);

-- 新增
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('${addId}', '${mainId}', '添加${tableVo.ftlDescription}', NULL, NULL, 0, NULL, NULL, 2, '${entityPackage}:${tableName}:add', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '${.now?string["yyyy-MM-dd HH:mm:ss"]}', NULL, NULL, 0, 0, '1', 0);

-- 编辑
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('${editId}', '${mainId}', '编辑${tableVo.ftlDescription}', NULL, NULL, 0, NULL, NULL, 2, '${entityPackage}:${tableName}:edit', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '${.now?string["yyyy-MM-dd HH:mm:ss"]}', NULL, NULL, 0, 0, '1', 0);

-- 删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('${delId}', '${mainId}', '删除${tableVo.ftlDescription}', NULL, NULL, 0, NULL, NULL, 2, '${entityPackage}:${tableName}:delete', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '${.now?string["yyyy-MM-dd HH:mm:ss"]}', NULL, NULL, 0, 0, '1', 0);

-- 批量删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('${batchDelId}', '${mainId}', '批量删除${tableVo.ftlDescription}', NULL, NULL, 0, NULL, NULL, 2, '${entityPackage}:${tableName}:deleteBatch', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '${.now?string["yyyy-MM-dd HH:mm:ss"]}', NULL, NULL, 0, 0, '1', 0);

-- 导出excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('${exportId}', '${mainId}', '导出excel_${tableVo.ftlDescription}', NULL, NULL, 0, NULL, NULL, 2, '${entityPackage}:${tableName}:exportXls', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '${.now?string["yyyy-MM-dd HH:mm:ss"]}', NULL, NULL, 0, 0, '1', 0);

-- 导入excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('${importId}', '${mainId}', '导入excel_${tableVo.ftlDescription}', NULL, NULL, 0, NULL, NULL, 2, '${entityPackage}:${tableName}:importExcel', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '${.now?string["yyyy-MM-dd HH:mm:ss"]}', NULL, NULL, 0, 0, '1', 0);

-- 角色授权（以 admin 角色为例，role_id 可替换）
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('${.now?long}08', 'f6817f48af4fb3af11b9e8bf182f618b', '${mainId}', NULL, '${.now?string["yyyy-MM-dd HH:mm:ss"]}', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('${.now?long}09', 'f6817f48af4fb3af11b9e8bf182f618b', '${addId}', NULL, '${.now?string["yyyy-MM-dd HH:mm:ss"]}', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('${.now?long}10', 'f6817f48af4fb3af11b9e8bf182f618b', '${editId}', NULL, '${.now?string["yyyy-MM-dd HH:mm:ss"]}', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('${.now?long}11', 'f6817f48af4fb3af11b9e8bf182f618b', '${delId}', NULL, '${.now?string["yyyy-MM-dd HH:mm:ss"]}', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('${.now?long}12', 'f6817f48af4fb3af11b9e8bf182f618b', '${batchDelId}', NULL, '${.now?string["yyyy-MM-dd HH:mm:ss"]}', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('${.now?long}13', 'f6817f48af4fb3af11b9e8bf182f618b', '${exportId}', NULL, '${.now?string["yyyy-MM-dd HH:mm:ss"]}', '127.0.0.1');
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip) VALUES ('${.now?long}14', 'f6817f48af4fb3af11b9e8bf182f618b', '${importId}', NULL, '${.now?string["yyyy-MM-dd HH:mm:ss"]}', '127.0.0.1');