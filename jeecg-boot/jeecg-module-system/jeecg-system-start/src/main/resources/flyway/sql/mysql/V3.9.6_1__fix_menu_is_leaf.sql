-- ---author:scott---date:20261104-----for:AI提示词/AI评估器/租户用户菜单已挂按钮权限子节点，父菜单 is_leaf 仍为 1，导致菜单树/角色授权树不显示子节点，修正为 0
UPDATE `sys_permission` SET `is_leaf` = 0
WHERE `id` IN ('1999367175911657473', '2000880658872508417', '119213522910765570') AND `is_leaf` = 1;
