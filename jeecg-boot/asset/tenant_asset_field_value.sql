drop table if exists tenant_asset_field_value;
create table tenant_asset_field_value
(
    `id`                  bigint primary key auto_increment,
    `asset_id`            bigint                                                        not null comment '资产ID',
    `tenant_id`           bigint                                                        not null comment '租户ID',
    `field_definition_id` bigint                                                        not null comment '字段定义ID',
    `field_key`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci not null comment '字段名称',
    `value_text`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci default null comment '字段值',
    `value_number`        decimal(10, 2) default null comment '字段值',
    `value_date`          date           default null comment '字段值',
    `value_time`          time           default null comment '字段值',
    `value_datetime`      datetime       default null comment '字段值',
    `value_boolean`       tinyint(1) default 0 comment '字段值',
    `value_json`          json           default null comment '字段值',
    `create_time`         datetime       default null comment '创建时间',
    `create_by`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci default null comment '创建人',
    `update_time`         datetime       default null comment '更新时间',
    `update_by`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci default null comment '更新人',
    key `idx_tenant_asset_id` (`tenant_id`, `asset_id`) Using BTREE,
    unique key `uniq_tenant_id_field_key` (`tenant_id`, `field_key`) Using BTREE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci comment = '租户资产字段值表';