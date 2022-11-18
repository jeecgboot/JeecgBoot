-- 注意：该页面对应的前台目录为views/${entityPackage}文件夹下
-- 如果你想更改到其他目录，请修改sql中component字段对应的值

<#assign id = '${.now?string["yyyyMMddhhmmSSsss"]}0'>

INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external) 
VALUES ('${id}', NULL, '${tableVo.ftlDescription}', '/${entityPackage}/${entityName?uncap_first}List', '${entityPackage}/${entityName}List', NULL, NULL, 0, NULL, '1', 0.00, 0, NULL, 1, 0, 0, 0, 0, NULL, '1', 0, 0, 'admin', '${.now?string["yyyy-MM-dd HH:mm:ss"]}', NULL, NULL, 0);

-- 权限控制sql
-- 新增
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('${.now?string["yyyyMMddhhmmSSsss"]}1', '${id}', '添加${tableVo.ftlDescription}', NULL, NULL, 0, NULL, NULL, 2, '${entityPackage}:${tableName}:add', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '${.now?string["yyyy-MM-dd HH:mm:ss"]}', NULL, NULL, 0, 0, '1', 0);
-- 编辑
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('${.now?string["yyyyMMddhhmmSSsss"]}2', '${id}', '编辑${tableVo.ftlDescription}', NULL, NULL, 0, NULL, NULL, 2, '${entityPackage}:${tableName}:edit', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '${.now?string["yyyy-MM-dd HH:mm:ss"]}', NULL, NULL, 0, 0, '1', 0);
-- 删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('${.now?string["yyyyMMddhhmmSSsss"]}3', '${id}', '删除${tableVo.ftlDescription}', NULL, NULL, 0, NULL, NULL, 2, '${entityPackage}:${tableName}:delete', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '${.now?string["yyyy-MM-dd HH:mm:ss"]}', NULL, NULL, 0, 0, '1', 0);
-- 批量删除
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('${.now?string["yyyyMMddhhmmSSsss"]}4', '${id}', '批量删除${tableVo.ftlDescription}', NULL, NULL, 0, NULL, NULL, 2, '${entityPackage}:${tableName}:deleteBatch', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '${.now?string["yyyy-MM-dd HH:mm:ss"]}', NULL, NULL, 0, 0, '1', 0);
-- 导出excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('${.now?string["yyyyMMddhhmmSSsss"]}5', '${id}', '导出excel_${tableVo.ftlDescription}', NULL, NULL, 0, NULL, NULL, 2, '${entityPackage}:${tableName}:exportXls', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '${.now?string["yyyy-MM-dd HH:mm:ss"]}', NULL, NULL, 0, 0, '1', 0);
-- 导入excel
INSERT INTO sys_permission(id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('${.now?string["yyyyMMddhhmmSSsss"]}6', '${id}', '导入excel_${tableVo.ftlDescription}', NULL, NULL, 0, NULL, NULL, 2, '${entityPackage}:${tableName}:importExcel', '1', NULL, 0, NULL, 1, 0, 0, 0, NULL, 'admin', '${.now?string["yyyy-MM-dd HH:mm:ss"]}', NULL, NULL, 0, 0, '1', 0);