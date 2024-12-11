-- ---author:wangshuai---date:20241108-----for: 修改字段变更为为钉钉企业id---
ALTER TABLE sys_third_app_config
    CHANGE COLUMN agent_app_secret corp_id varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '钉钉企业id' AFTER client_secret;

UPDATE `sys_gateway_route` SET  `predicates` = '[{\"args\":[\"/websocket/**\",\"/eoaSocket/**\",\"/newsWebsocket/**\",\"/dragChannelSocket/**\"],\"name\":\"Path\"}]' WHERE `id` = 'jeecg-cloud-websocket';

-- ---author:sunjianlei---date:20240930-----for: 【TV360X-2604】【Online表单】按钮权限未激活时增加提示，添加查询索引 ---
ALTER TABLE onl_auth_page ADD INDEX idx_onl_auth_page_code(code);
ALTER TABLE onl_auth_page ADD INDEX idx_onl_auth_page_cgform_id(cgform_id);