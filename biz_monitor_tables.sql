-- 创建业务指标快照表
CREATE TABLE `biz_monitor_metric` (
  `id` VARCHAR(32) NOT NULL COMMENT '主键ID',
  `metric_code` VARCHAR(100) NOT NULL COMMENT '指标编码',
  `metric_name` VARCHAR(100) NOT NULL COMMENT '指标名称',
  `metric_value` DECIMAL(18,6) NOT NULL COMMENT '指标值',
  `metric_unit` VARCHAR(20) DEFAULT NULL COMMENT '指标单位',
  `biz_type` VARCHAR(50) DEFAULT NULL COMMENT '业务类型',
  `biz_date` DATE NOT NULL COMMENT '业务日期',
  `biz_time` TIME NOT NULL COMMENT '业务时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_metric_code` (`metric_code`),
  KEY `idx_biz_date` (`biz_date`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务指标快照表';

-- 创建预警规则表
CREATE TABLE `biz_alert_rule` (
  `id` VARCHAR(32) NOT NULL COMMENT '主键ID',
  `rule_code` VARCHAR(100) NOT NULL COMMENT '规则编码',
  `rule_name` VARCHAR(100) NOT NULL COMMENT '规则名称',
  `metric_code` VARCHAR(100) NOT NULL COMMENT '关联指标编码',
  `threshold_type` VARCHAR(20) NOT NULL COMMENT '阈值类型(GT:大于,LT:小于,EQ:等于)',
  `threshold_value` DECIMAL(18,6) NOT NULL COMMENT '阈值',
  `notify_type` VARCHAR(100) DEFAULT NULL COMMENT '通知方式(多个用逗号分隔:WECHAT,SMS,SITE_MESSAGE)',
  `shield_start_time` TIME DEFAULT NULL COMMENT '屏蔽开始时间',
  `shield_end_time` TIME DEFAULT NULL COMMENT '屏蔽结束时间',
  `is_enabled` CHAR(1) NOT NULL DEFAULT '1' COMMENT '是否启用(0:禁用,1:启用)',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
  `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rule_code` (`rule_code`),
  KEY `idx_metric_code` (`metric_code`),
  KEY `idx_is_enabled` (`is_enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警规则表';
