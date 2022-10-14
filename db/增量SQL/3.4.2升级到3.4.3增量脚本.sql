ALTER TABLE `onl_cgform_field`
MODIFY COLUMN `field_show_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单控件类型' AFTER `dict_text`;

ALTER TABLE `onl_cgform_field` 
ADD COLUMN `db_is_persist` tinyint(1) NULL COMMENT '是否需要同步数据库字段， 1是0否' AFTER `db_is_null`;
update onl_cgform_field set db_is_persist = 1;