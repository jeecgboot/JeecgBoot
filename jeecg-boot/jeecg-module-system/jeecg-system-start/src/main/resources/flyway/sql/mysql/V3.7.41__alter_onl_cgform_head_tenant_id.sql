-- -- author:sunjianlei---date:20250120--for: 【QQYUN-10886】【online】仪表盘设计-online表单下拉选择接口，没有做租户隔离逻辑 onl_cgform_head ---
ALTER TABLE `onl_cgform_head` 
ADD COLUMN `tenant_id` int NULL DEFAULT 0 COMMENT '租户ID' AFTER `des_form_code`;