drop table if exists tenant_asset_field_definition;
create table tenant_asset_field_definition
(
    `id`             bigint primary key auto_increment,
    `tenant_id`      bigint                                                        not null comment '租户ID',
    `field_key`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci not null comment '字段键',
    `field_name`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci not null comment '字段名称',
    `field_category` int                                                           not null comment '字段分类:1: Asset,2:User,3:Reservations',
    `field_type`     int                                                           not null comment '字段类型:Text,Number,Date,Time,DateTime,Boolean,Json',
    `sort`           int                                                           default 1 comment '排序',
    `extra_data`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci default null comment '额外数据',
    `status`         tinyint(1) not null default 1 comment '状态',
    `create_time`    datetime                                                      default null comment '创建时间',
    `create_by`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci default null comment '创建人',
    `update_time`    datetime                                                      default null comment '更新时间',
    `update_by`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci default null comment '更新人',
    key `idx_tenant_id` (`tenant_id`) Using BTREE,
    unique key `uniq_tenant_id_field_key` (`tenant_id`, `field_key`) Using BTREE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci comment = '租户资产字段定义表';