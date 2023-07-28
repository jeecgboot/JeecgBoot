ALTER TABLE config_info ADD encrypted_data_key varchar(255) DEFAULT NULL COMMENT '加密key';
ALTER TABLE his_config_info ADD encrypted_data_key varchar(255) DEFAULT NULL COMMENT '加密key';
ALTER TABLE config_info_beta ADD encrypted_data_key varchar(255) DEFAULT NULL COMMENT '加密key';
ALTER TABLE config_info_tag ADD encrypted_data_key varchar(255) DEFAULT NULL COMMENT '加密key';