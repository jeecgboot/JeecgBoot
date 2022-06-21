-- 注意：该页面对应的前台目录为views/${entityPackage}文件夹下
-- 如果你想更改到其他目录，请修改sql中component字段对应的值

INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external) 
VALUES ('${.now?string["yyyyMMddhhmmSSsss"]}', NULL, '${tableVo.ftlDescription}', '/${entityPackage}/${entityName?uncap_first}List', '${entityPackage}/${entityName}List', NULL, NULL, 0, NULL, '1', 1.00, 0, NULL, 1, 1, 0, 0, 0, NULL, '1', 0, 0, 'admin', '${.now?string["yyyy-MM-dd HH:mm:ss"]}', NULL, NULL, 0);