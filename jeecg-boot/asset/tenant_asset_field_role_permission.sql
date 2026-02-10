drop table if exists tenant_asset_field_role_permission;
create table tenant_asset_field_role_permission
(
    `id`                  bigint primary key auto_increment,
    `tenant_id`           bigint not null comment '租户ID',
    `field_definition_id` bigint not null comment '字段定义ID',
    `role_id`             bigint not null comment '角色ID',
    `permission_bits`     bigint                                                        default null comment '权限位',
    `create_time`         datetime                                                      default null comment '创建时间',
    `create_by`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci default null comment '创建人',
    `update_time`         datetime                                                      default null comment '更新时间',
    `update_by`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci default null comment '更新人',
    unique key `uniq_tenant_id_role_id_field_definition_id` (`tenant_id`, `role_id`, `field_definition_id`) Using BTREE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci comment = '租户资产字段角色权限表';